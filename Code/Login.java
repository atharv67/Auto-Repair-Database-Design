import java.sql.*;
import java.util.Scanner;

public class Login {
    public static Scanner scan = new Scanner(System.in);
    public static void loginPage() {
        try{

        Connection conn = dbConnect.conn;
        System.out.println("================Welcome to the Login Page=================");
        boolean hasLoggedin = false;
        int choice;
        int userID;
        String userPassword;
        ResultSet results = null;
        do {
            if(hasLoggedin)
                System.out.print("Invalid UserID or Password. Please re-enter your password\n");
            System.out.println("Please enter your username:");
            userID = Integer.parseInt(scan.nextLine());
            System.out.println("Please enter your password:");
            userPassword = scan.nextLine();
            System.out.println("1)Press 1 to Sign-in");
            System.out.println("2)Press 2 to Go Back");
            choice = Integer.parseInt(scan.nextLine());
            PreparedStatement stmt = conn.prepareStatement("select type from valid where id=? and password=?");
            stmt.setInt(1, userID);
            stmt.setString(2, padRight(userPassword,40));
            results = stmt.executeQuery();
            hasLoggedin = true;
            }while(results.next()==false);
            if(choice == 1){
                String type = results.getString("type");
                System.out.println("Logging in....");
                    if(type.equals(padRight("admin", 20)))
                        Admin.adminMenu();
                    else if (type.equals(padRight("customer", 20)))
                        Customer.customerMenu(userID);
                    else if (type.equals(padRight("receptionist", 20)))
                        Receptionist.receptionMenu(userID);
                    else if (type.equals(padRight("manager", 20)))
                        Manager.managerMenu(userID);
                    else if (type.equals(padRight("mechanic", 20)))
                        Mechanic.mechanicMenu(userID);
            }    
            else if (choice == 2){
                return;
            }
            else{
                System.out.println("Invalid Input");
            }
    } catch (SQLException e){e.printStackTrace();};
    }
    public static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);  
   }
}



