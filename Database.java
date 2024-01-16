import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class Database {
    private static String username = "root";
    private static String password = "103eec29f";
    private static String url = "localhost";
    private static int port = 3306;
    private static String databaseName = "LibraryDatabase";
    private static Database instance;
    private MysqlDataSource dataSource;

    private Database() {
    initializeDataSource();
    }

    ;


    private void initializeDataSource() {
    dataSource = new MysqlDataSource();
    dataSource.setUser(username);
    dataSource.setPassword(password);
    dataSource.setURL("jdbc:mysql://" + url + ":" + port + "/" + databaseName + "?serverTimezone=UTC");
    }

    private static Database getInstance(){
        if (instance == null){
            instance = new Database();
        }

        return instance;
    }


    public Connection getConnection() throws SQLException{
        return dataSource.getConnection();
    }
}