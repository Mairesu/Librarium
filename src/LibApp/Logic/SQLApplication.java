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

        //TODO create sql query dependent on parameters
        //TODO handle empty result set

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
