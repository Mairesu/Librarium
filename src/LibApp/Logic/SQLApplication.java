package LibApp.Logic;

import LibApp.Interface.SearchListener;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class SQLApplication {

    private boolean debug = false;
    private String url;
    private final List<SearchListener> listeners = new LinkedList<>();
    private Connection connection;

    public SQLApplication(boolean debug)   {
        this.debug = debug;
    }

    public boolean isDebug()    {
        return this.debug;
    }

    public void addSearchListeners(SearchListener listener)    {
        if(!listeners.contains(listener))   {
            listeners.add(listener);
        }
    }

    public void connectToDatabase() {
        try {
            url = "jdbc:sqlite:libraryDatabase.db";
            connection = DriverManager.getConnection(url);

            System.out.println("Connection to Database successful");
        }
        catch (SQLException e)   {
            System.err.println(e.getMessage());
        }
    }

    public void executeSQLStatement(String sqlStatement)    {
        try (Connection connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement()) {

            statement.execute(sqlStatement);
        }
        catch (SQLException e)   {
            System.err.println(e.getMessage());
        }
    }

    public void debugClearDatabase() {
        Iterator<String> sqlStatements = readSqlFile("src/LibApp/Data/clearDB.sql");

        while(sqlStatements.hasNext())  {
            executeSQLStatement(sqlStatements.next());
        }

        for(SearchListener s : listeners)   {
            s.onDebugAlert("Database cleared");
        }
    }

    public void debugRebuildDatabase()    {

        Iterator<String> sqlStatements = readSqlFile("src/LibApp/Data/createDB.sql");

        while(sqlStatements.hasNext())  {
            executeSQLStatement(sqlStatements.next());
        }

        for(SearchListener s : listeners)   {
            s.onDebugAlert("Database rebuilt");
        }
    }

    public void debugFillDatabase() {

        Iterator<String> sqlStatements = readSqlFile("src/LibApp/Data/fillDB.sql");

        while(sqlStatements.hasNext())  {
            executeSQLStatement(sqlStatements.next());
        }

        for(SearchListener s : listeners)   {
            s.onDebugAlert("Database filled with dummy data");
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
