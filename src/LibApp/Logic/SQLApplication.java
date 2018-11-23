package LibApp.Logic;

import LibApp.Interface.SearchListener;

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
        Connection connection = null;
        try {
            this.url = "jdbc:sqlite:libraryDatabase.db";
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

    public void debugRebuildDatabase()    {
        executeSQLStatement("DROP TABLE IF EXISTS publisher");
        executeSQLStatement("CREATE TABLE IF NOT EXISTS publisher (" +
                    "\npublisher_id integer NOT NULL UNIQUE," +
                    "\npublisher_name varchar(255) NOT NULL," +
                    "\nPRIMARY KEY (publisher_id)" +
                "\n);");

        for(SearchListener s : listeners)   {
            s.onDebugAlert("Database rebuilt");
        }
    }
}
