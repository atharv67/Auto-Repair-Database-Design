import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceCenter {
    
    public static Connection conn = dbConnect.conn;

    public static int getSid(int id){
        int sid=0;
        try{
            PreparedStatement ps = conn.prepareStatement("select sid from employee where emp works in service center");
            ResultSet rs = ps.executeQuery();
            sid = rs.getInt("sid");
        }catch(SQLException e){e.printStackTrace();}
        return sid;
    }    
}
