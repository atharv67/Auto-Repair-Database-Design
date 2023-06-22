import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Manufacturer {
    
    public static Connection conn = dbConnect.conn;

    public static ResultSet getManfNames(){
        ResultSet rs = null;
        try{
            PreparedStatement ps = conn.prepareStatement("select name from manufacturer");
            rs = ps.executeQuery();
        }catch(SQLException e){e.printStackTrace();}
        return rs;
    }    
}
