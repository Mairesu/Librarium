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
import java.util.LinkedList;
import java.util.List;

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
        executeSQLStatement("DROP TABLE IF EXISTS publisher");
        executeSQLStatement("DROP TABLE IF EXISTS city");

        for(SearchListener s : listeners)   {
            s.onDebugAlert("Database cleared");
        }
    }


    public void debugRebuildDatabase()    {

        String filePath = "src/LibApp/Data/createDB.sql";
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

        String[] statements = streamString.toString().split(";");
        for (String s : statements) {
            executeSQLStatement(s);
        }

        for(SearchListener s : listeners)   {
            s.onDebugAlert("Database rebuilt");
        }

    }

}
