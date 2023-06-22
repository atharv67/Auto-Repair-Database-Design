import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class Manager {

    public static Connection conn = dbConnect.conn;
    public static int mgr_id;
    public static Scanner scan = new Scanner(System.in);
    public static void managerMenu(int m_id){
        mgr_id = m_id;
        int choice;
        System.out.println("===============Welcome to the Manager Panel==============");
        do{
        System.out.println("Press any of the following options from the menu to proceed further:");
        System.out.println("1. Setup Store");
        System.out.println("2. Add New Employee");
        System.out.println("3. Logout");
        choice = Integer.parseInt(scan.nextLine());
            switch (choice) {
            case 1:
                setupStore();
                break;
            case 2:
                addNewEmployee();
                break;
            case 3:
                return;
            default:
                System.out.println("Selection is invalid");
                break;
            }
        } while (choice != 3);

    }
    public static void setupStore(){
            int choice;
            do{
                System.out.println("Press any of the following options from the menu to proceed further:");
                System.out.println("1. Setup Operational Hours");
                System.out.println("2. Setup Service Price");
                System.out.println("3. Go Back");
                choice = Integer.parseInt(scan.nextLine());
                    switch (choice) {
                    case 1:
                        setupOperationalHours();
                        break;
                    case 2:
                        setupServicePrice();
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println("Selection is invalid");
                        break;
                    }
                } while (choice != 3);
        }
    public static void setupOperationalHours(){
        int choice;
        try{
            System.out.println("Please enter the following details to add to the store.");
            System.out.println("1.Setup Operational Hours");
            System.out.println("2.Go Back");
            choice = Integer.parseInt(scan.nextLine());
                    switch (choice) {
                    case 1:
                        System.out.println("Is the store open on Saturdays ?(Enter Y or N)");
                        String char_choice = scan.nextLine();
                        PreparedStatement ps = conn.prepareStatement("update service_centre set sat_open=? where Id=?");
                        ps.setString(1, char_choice);
                        ps.setInt(2, mgr_id);
                        ps.executeUpdate();
                        break;
                    case 2:
                        return;
                    default:
                        System.out.println("Selection is invalid");
                        break;
                    }

        }catch(SQLException e){e.printStackTrace();}
    }
     
    public static void setupServicePrice(){
        int choice;
        do{
            System.out.println("Please enter the following details to add to the store.");
            System.out.println("1.Setup Maintenance Service Price");
            System.out.println("2.Setup Repair Service Price");
            System.out.println("3.Go Back");
            choice = Integer.parseInt(scan.nextLine());
                    switch (choice) {
                    case 1:
                        setupMaintenancePrice();
                        break;
                    case 2:
                        setupRepairPrice();
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println("Selection is invalid");
                        break;
                    }

        }while(choice!=3);
    }
    
    public static void setupMaintenancePrice(){
        try{
            System.out.println("Please enter the following details to add maintenance price.");
            PreparedStatement ps = conn.prepareStatement("select id from employee where e_id=?");
            ps.setInt(1, mgr_id);
            ResultSet rs = ps.executeQuery();rs.next();
            int service_centre_no = rs.getInt("id");
            int service_no_A;
            int service_no_B;
            int service_no_C;
            PreparedStatement ps_A = conn.prepareStatement("select service_no from services where name='A'");
            ResultSet rs_A = ps_A.executeQuery();rs_A.next();
            service_no_A = rs_A.getInt("service_no");
            PreparedStatement ps_B= conn.prepareStatement("select service_no from services where name='B'");
            ResultSet rs_B = ps_B.executeQuery();rs_B.next();
            service_no_B = rs_B.getInt("service_no");
            PreparedStatement ps_C = conn.prepareStatement("select service_no from services where name='C'");
            ResultSet rs_C = ps_C.executeQuery();rs_C.next();
            service_no_C = rs_C.getInt("service_no");
            String insertOffers = "insert into offers(service_no,id) values (?,?)";
            PreparedStatement psOffers = conn.prepareStatement(insertOffers);
            psOffers.setInt(1, service_no_A);
            psOffers.setInt(2, service_centre_no);
            // psOffers.executeUpdate();
            psOffers.setInt(1, service_no_B);
            psOffers.setInt(2, service_centre_no);
            // psOffers.executeUpdate();
            psOffers.setInt(1, service_no_C);
            psOffers.setInt(2, service_centre_no);
            // psOffers.executeUpdate();
            String insertPrice = "insert into car_has_cost_of_service(manufacturer,service_no,Id,cost)"
                                +"values(?,?,?,?)";
            ResultSet manfResults = Manufacturer.getManfNames();
            while(manfResults.next()){                        
                System.out.println("Enter the price for "+manfResults.getString("name").trim()+" for service A:");
                int scheduleA_price = Integer.parseInt(scan.nextLine());
                System.out.println("Enter the price for "+manfResults.getString("name").trim()+" for service B:");
                int scheduleB_price = Integer.parseInt(scan.nextLine());
                System.out.println("Enter the price for "+manfResults.getString("name").trim()+" for service C:");
                int scheduleC_price = Integer.parseInt(scan.nextLine());
                PreparedStatement manfPS = conn.prepareStatement(insertPrice); 
                manfPS.setString(1, manfResults.getString("name"));
                manfPS.setInt(2, service_no_A); 
                manfPS.setInt(3, service_centre_no);
                manfPS.setInt(4, scheduleA_price);
                manfPS.executeUpdate();
                manfPS = conn.prepareStatement(insertPrice); 
                manfPS.setString(1, manfResults.getString("name"));
                manfPS.setInt(2, service_no_B); 
                manfPS.setInt(3, service_centre_no);
                manfPS.setInt(4, scheduleB_price);
                manfPS.executeUpdate();
                manfPS = conn.prepareStatement(insertPrice); 
                manfPS.setString(1, manfResults.getString("name"));
                manfPS.setInt(2, service_no_C); 
                manfPS.setInt(3, service_centre_no);
                manfPS.setInt(4, scheduleC_price);
                manfPS.executeUpdate();
            }
        }catch(SQLException e){e.printStackTrace();}
            
        
    }

    public static void setupRepairPrice(){
        try{
            System.out.println("Please enter the following details to add repair price.");
            PreparedStatement ps = conn.prepareStatement("select id from employee where e_id=?");
            ps.setInt(1, mgr_id);
            ResultSet rs = ps.executeQuery();rs.next();
            int service_centre_no = rs.getInt("id");
            PreparedStatement ps_serviceNo = conn.prepareStatement("select service_no from offers where id=? and "+
                                            "service_no in (select service_no from repairs)");
            ps_serviceNo.setInt(1, service_centre_no);
            ResultSet serviceResults=ps_serviceNo.executeQuery();
            while(serviceResults.next()){
            ResultSet manfResults = Manufacturer.getManfNames();
            while(manfResults.next()){
                System.out.println("Enter the price for "+manfResults.getString("name").trim()+" for service "+
                serviceResults.getString("service_no")+":");
                int price = Integer.parseInt(scan.nextLine());                        
                PreparedStatement pricePS = conn.prepareStatement("insert into car_has_cost_of_service(manufacturer,service_no,Id,cost)"
                                                                +"values(?,?,?,?)"); 
                pricePS.setString(1, manfResults.getString("name"));
                pricePS.setInt(2, serviceResults.getInt("service_no")); 
                pricePS.setInt(3, service_centre_no); 
                pricePS.setInt(4, price);
                pricePS.executeUpdate();
            }}
            }catch(SQLException e){e.printStackTrace();}   
    }

    public static void addNewEmployee(){
        try{
            System.out.println("Please enter the following details to add the employee.");
            System.out.println("1.Name\n2.Address\n3.Email\n4.Phone Number\n5.Role(Receptionist or Mechanic)"+
                               "\n6.Start Date\n7.Salary");
            int eid = (int)Math.random()*10000;
            String name = scan.nextLine();
            String address = scan.nextLine();
            String email = scan.nextLine();
            String phone_number = scan.nextLine();
            String role="";
            while(!(role.equals("receptionist")|| role.equals("mechanic"))){
                System.out.println("Please enter receptionist or mechanic");
                role = scan.nextLine();
            }
            String start_date = scan.nextLine();
            int salary = Integer.parseInt(scan.nextLine());
            PreparedStatement ps = conn.prepareStatement("select id from employee where e_id=?");
            ps.setInt(1, mgr_id);
            ResultSet rs = ps.executeQuery();rs.next();
            int service_centre_no = rs.getInt("id");
            PreparedStatement insertEmployee = conn.prepareStatement("INSERT INTO EMPLOYEE(E_Id,Pno,Name,Addr,Email,Id) VALUES (?,?,?,?,?,?)");
            insertEmployee.setInt(1, eid);
            insertEmployee.setString(2,phone_number);
            insertEmployee.setString(3, name);
            insertEmployee.setString(4, address);
            insertEmployee.setString(5, email);
            insertEmployee.setInt(6, service_centre_no);
            PreparedStatement insertsql;
            PreparedStatement insertrole;
            if(role.equals("receptionist")){
                insertsql = conn.prepareStatement("insert into CONTRACT_EMPLOYEES( E_Id ,Salary) values (?,?)");
                insertrole = conn.prepareStatement("insert into receptionist(E_Id) values(?)");
                insertsql.setInt(1, eid);
                insertsql.setInt(2,salary);
                insertrole.setInt(1, eid);
            }
            else{
                insertsql = conn.prepareStatement("INSERT INTO HOURLY_EMPLOYEES VALUES (?)");
                insertrole = conn.prepareStatement("insert into Mechanics Values(?)");
                insertsql.setInt(1,eid);
                insertrole.setInt(1,eid);
            }
            System.out.println("Press any of the following options from the menu to proceed further:");
            System.out.println("1.Add Employee\n2.Go Back");
            int choice = Integer.parseInt(scan.nextLine());
            if(choice == 1){
                try {
                    insertEmployee.executeUpdate();
                    insertsql.executeUpdate();
                    insertrole.executeUpdate();
                } catch (SQLException e) {
                    System.out.println("Could not insert. Please try again");
                    e.printStackTrace();
                }
            }
            else if (choice == 2){
                return;
            }
            else{
                System.out.println("Invalid Input");
            }
            System.out.println("Success! Adding New Customer...");
        }
        catch(SQLException e){
            System.out.println("Failure! Could not add new customer\n");
            e.printStackTrace();}
    }
}
