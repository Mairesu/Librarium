package LibApp.Logic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLApplication {

    private boolean debug = false;

    public SQLApplication(boolean debug)   {
        this.debug = debug;
    }

    public boolean isDebug()    {
        return this.debug;
    }

    public void connectToDatabase() {
        Connection connection = null;
        try {
            String url = "jdbc:sqlite:libraryDatabase.db";
            connection = DriverManager.getConnection(url);

            System.out.println("Connection to Database successful");
        }
        catch (Exception e)   {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }


}
