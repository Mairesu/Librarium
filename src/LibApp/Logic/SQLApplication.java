package LibApp.Logic;

import LibApp.Interface.SearchListener;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;

public class SQLApplication {

    private boolean debug = false;
    private boolean authorized = false;

    /*THE PASSWORD IS HERE FOR DEMONSTRATION PURPOSES!
     * NEVER EVER EVER EVER EVER EVER -EVER- STORE PASSWORDS IN PLAIN TEXT.
     * EVER
     * NEVER EVER
     * ALWAYS ENCRYPT PASSWORDS WHEN MAKING A PROPER APPLICATION*/
    private String password = "Password";

    private String url;
    private Connection connection;
    private final List<SearchListener> listeners = new LinkedList<>();

    public SQLApplication(boolean debug)   {
        this.debug = debug;
        this.connection = connectToDatabase();
    }

    public boolean isDebug()    {
        return this.debug;
    }

    public void addSearchListeners(SearchListener listener)    {
        if(!listeners.contains(listener))   {
            listeners.add(listener);
        }
    }

    public void authorizeAsAdmin(String password)    {

        if (this.password.equals(password)) {
            authorized = true;
        }

        for (SearchListener s : listeners)  {
            s.onAuthorize(authorized);
        }
    }

    public void adminLogOut()   {
        this.authorized = false;
    }

    public boolean getAuthorized()  {
        return this.authorized;
    }

    public Connection connectToDatabase() {

        url = "jdbc:sqlite:libraryDatabase.db";
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(url);
            System.out.println("Connection to Database successful");

        }
        catch (SQLException e)   {
            System.err.println(e.getMessage());
        }

        return connection;
    }



    public Iterator<String> getAllBranches()    {

        ArrayList<String> strings = new ArrayList<>();

        String sql = "SELECT lb.branch_name, ad.street_address " +
                "FROM library_branch lb, address ad " +
                "WHERE lb.branch_address_id = ad.address_id;";


        try (Statement stmt  = connection.createStatement();
             ResultSet resultSet    = stmt.executeQuery(sql)){

            while (resultSet.next()) {

                 strings.add(resultSet.getString("branch_name") + ", " +
                         resultSet.getString("street_address"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        strings.sort(String::compareToIgnoreCase);
        return strings.iterator();
    }

    public Iterator<JSONObject> doBookSearch(String bookTitle, String authorName, String publisher,
                                             String edition, int year, String branch)  {

        ArrayList<JSONObject> searchResults = new ArrayList<>();

        String branchName = "";
        String branchAddress = "";

        if(branch.contains(",")) {
            branchName = branch.substring(0, branch.indexOf(","));
            branchAddress = branch.substring(branch.indexOf(",")+2);
        }

        String sqlBase = "SELECT DISTINCT b.book_title, br.barcode_number, a.author_name, p.publisher_name, b.edition, " +
                "b.publication_year, lb.branch_name, ad.street_address " +
                "FROM book b, author a, book_has_author bha, publisher p, " +
                "barcode br, library_branch lb, address ad " +
                "WHERE b.publisher_id = p.publisher_id " +
                "AND b.book_id = bha.book_id " +
                "AND a.author_id = bha.author_id " +
                "AND b.book_id = br.book_id " +
                "AND br.library_branch_id = lb.branch_id " +
                "AND lb.branch_address_id = ad.address_id " +
                "AND b.book_title LIKE '%" + bookTitle + "%' " +
                "AND a.author_name LIKE '%" + authorName + "%' " +
                "AND p.publisher_name LIKE '%" + publisher + "%' " +
                "AND ad.street_address LIKE '%" + branchAddress + "%' " +
                "AND lb.branch_name LIKE '%" + branchName + "%' ";

        String sqlQuery = sqlBase;

        if(!edition.equals(""))  {
            sqlQuery += "AND b.edition LIKE '%" + edition + "%' ";
        }

        if(0 <= year)   {
            sqlQuery += "AND b.publication_year = " + year;
        }

        try(PreparedStatement prepStatement = connection.prepareStatement(sqlQuery)) {

            ResultSet resultSet = prepStatement.executeQuery();

            while (resultSet.next())    {
                System.out.println(resultSet.getString("book_title") + "\t" +
                        resultSet.getString("author_name") + "\t" +
                        resultSet.getString("publisher_name") + "\t" +
                        resultSet.getString("edition") + "\t" +
                        resultSet.getInt("publication_year") + "\t" +
                        resultSet.getString("barcode_number") + "\t" +
                        resultSet.getString("branch_name") + "\t" +
                        resultSet.getString("street_address"));

                searchResults.add(new JSONObject().put("title", resultSet.getString("book_title"))
                        .put("author", resultSet.getString("author_name"))
                        .put("publisher", resultSet.getString("publisher_name"))
                        .put("edition", resultSet.getString("edition"))
                        .put("year", resultSet.getInt("publication_year"))
                        .put("barcode", resultSet.getString("barcode_number"))
                        .put("branch", resultSet.getString("branch_name"))
                        .put("address", resultSet.getString("street_address")));
            }
        }
        catch (SQLException e)  {
            System.out.println(e.getMessage());
        }

        return searchResults.iterator();
    }

    public void executeSqlStatement(String sqlStatement)    {

        try (Statement statement = connection.createStatement()) {

            statement.execute(sqlStatement);
        }
        catch (SQLException e)   {
            System.err.println(e.getMessage());
        }
    }

    public void debugClearDatabase() {
        if(debug)   {
            Iterator<String> sqlStatements = readSqlFile("src/LibApp/Data/clearDB.sql");

            while(sqlStatements.hasNext())  {
                executeSqlStatement(sqlStatements.next());
            }

            for(SearchListener s : listeners) {
                s.onDebugAlert("Database cleared");
            }
        }
    }

    public void debugRebuildDatabase()    {
        if(debug) {
            Iterator<String> sqlStatements = readSqlFile("src/LibApp/Data/createDB.sql");

            while (sqlStatements.hasNext()) {
                executeSqlStatement(sqlStatements.next());
            }

            for (SearchListener s : listeners) {
                s.onDebugAlert("Database rebuilt");
            }
        }
    }

    public void debugFillDatabase() {
        if(debug) {
            Iterator<String> sqlStatements = readSqlFile("src/LibApp/Data/fillDB.sql");

            while (sqlStatements.hasNext()) {
                executeSqlStatement(sqlStatements.next());
            }

            for (SearchListener s : listeners) {
                s.onDebugAlert("Database filled with dummy data");
            }
        }
    }

    public Iterator<String> readSqlFile(String filePath)   {

        StringBuilder streamString = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath)))  {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                streamString.append(sCurrentLine);
                streamString.append("\n");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        ArrayList<String> sqlStatements = new ArrayList<>(Arrays.asList(streamString.toString().split(";")));

        return sqlStatements.iterator();
    }
}
