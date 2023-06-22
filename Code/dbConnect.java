import java.sql.*;

public class dbConnect {

    static final String jdbcURL = "jdbc:oracle:thin:@ora.csc.ncsu.edu:1521:orcl01";
    static String user = "apandit3";	// For example, "jsmith"
    static String passwd = "200477792";	// Your 9 digit student ID number

    public static Connection conn = null;
    public static Statement stmt = null;
    public static ResultSet rs = null;
    
    public static void main(String[] args) throws Exception{

        if (conn != null) return; // Return if connection is already established

        Class.forName("oracle.jdbc.OracleDriver");
            
        conn = DriverManager.getConnection(jdbcURL, user, passwd);
        
        System.out.println("Connected: "+ conn);
        Menu.menuOptions();
    }
    

    static void close(Connection conn) {
        if(conn != null) {
            try { conn.close(); } catch(Throwable whatever) {}
        }
    }

    static void close(Statement st) {
        if(st != null) {
            try { st.close(); } catch(Throwable whatever) {}
        }
    }

    static void close(ResultSet rs) {
        if(rs != null) {
            try { rs.close(); } catch(Throwable whatever) {}
        }
    }
}



