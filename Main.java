import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {

    try{
        Connection conn = (Connection) Database.getInstance();
        if (conn != null){
        System.out.println("Connection worked");

        String sql = "SELECT * FROM bookTable";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while(rs.next()){
            System.out.println(rs.getInt("bookID") + rs.getString("bookName") + rs.getInt("stock"));
        }
        stmt.close();
        conn.close();
        rs.close();
    }
    }
    catch (Exception e) {
        System.exit(1);
        throw new RuntimeException(e);
    }
    }
}