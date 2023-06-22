import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;



public class Admin {

    public static Connection conn = dbConnect.conn;
    public static Scanner scan = new Scanner(System.in);
    public static void adminMenu(){
        System.out.println("===============Welcome to the Admin Panel==============");
        int choice;
        
        do{
        System.out.println("Press any of the following options from the menu to proceed further:");
        System.out.println("1. Add New Store");
        System.out.println("2. Add New Service");
        System.out.println("3. Logout");
        choice = Integer.parseInt(scan.nextLine());
        switch (choice) {
        case 1:
            addNewStore();
            break;
        case 2:
            addNewService();
            break;
        case 3:
            return;
        default:
            System.out.println("Selection is invalid");
            break;
        }
    } while(choice!=3);
    }    

    public static void addNewStore(){
        try{
            System.out.println("Please enter the following details to add the store.");
            System.out.println("1.Store Id\n2.Address\n3.Telephone No.\n4.Min wage for Employee\n"+
                                "5.Max wage for Employee\n6.State\n7.Rate\n8.Manager ID\n9.Manager Name\n"+
                                "10.Manager Address\n11.Manager Telephone\n12.Manager Email\n13.Manager Salary");
            int store_id = Integer.parseInt(scan.nextLine());
            String address = scan.nextLine();
            String tele_no = scan.nextLine();
            int min_wage = Integer.parseInt(scan.nextLine());
            int max_wage = Integer.parseInt(scan.nextLine());
            String state = scan.nextLine();
            int hourly_wage = Integer.parseInt(scan.nextLine());
            int mgr_eid = Integer.parseInt(scan.nextLine());
            String mgr_name = scan.nextLine();
            String mgr_addr = scan.nextLine();
            String mgr_tel = scan.nextLine();
            String mgr_email = scan.nextLine();
            int mgr_sal = Integer.parseInt(scan.nextLine());
            String insertSql = "insert into service_centre(Id,Addr,tele_number,state,minimum_wage,maximum_wage,rate)"
                                +"VALUES (?,?,?,?,?,?,?)";
            String insertMgr = "insert into employee(E_Id, Pno , Name ,Addr ,Email ,Id)"+
                               "values(?, ? , ? ,? ,? ,?)"; 
            PreparedStatement ps = conn.prepareStatement(insertSql);
            
            ps.setInt(1, store_id);
            ps.setString(2, address);
            ps.setString(3, tele_no);
            ps.setString(4, state);
            ps.setInt(5, min_wage);
            ps.setInt(6, max_wage);
            ps.setInt(7, hourly_wage);

            PreparedStatement ps_mgr = conn.prepareStatement(insertMgr);
            ps_mgr.setInt(1,mgr_eid);
            ps_mgr.setString(2,mgr_tel);
            ps_mgr.setString(3,mgr_name);
            ps_mgr.setString(4,mgr_addr);
            ps_mgr.setString(5,mgr_email);
            ps_mgr.setInt(6,store_id);

            PreparedStatement ps_contract_emp = conn.prepareStatement("insert into CONTRACT_EMPLOYEES( E_Id ,Salary) values (? ,?)");
            ps_contract_emp.setInt(1, mgr_eid);
            ps_contract_emp.setInt(2,mgr_sal);

            PreparedStatement ps_mgrtable = conn.prepareStatement("insert into manager(E_id) values(?)");
            ps_mgrtable.setInt(1, mgr_eid);

            System.out.println("Press any of the following options from the menu to proceed further:");
            System.out.println("1.Add Store\n2.Go Back");
            int choice = Integer.parseInt(scan.nextLine());
            if(choice == 1){
                try {
                    ps.executeUpdate();
                    ps_mgr.executeUpdate();
                    ps_contract_emp.executeUpdate();
                    ps_mgrtable.executeUpdate();
                    System.out.println("Success! Adding New Store...");
                } catch (SQLException e) {
                    System.out.println("Could not insert. Please try again");
                    e.printStackTrace();
                }
            }
            else if (choice == 2){
                adminMenu();
            }
            else{
                System.out.println("Invalid Input");
            }
        }
        catch(SQLException e){
            System.out.println("Failure! Could not add new store\n");
            e.printStackTrace();}
    }

    public static void addNewService(){
        try{
            System.out.println("Please enter the following details to add the service.");
            System.out.println("1.Service Number\n2.Service Type(repair or maintenance)\n3.Service Category(Engine,Exhaust,Electrical,Transmission,Tire,Heat and AC) or (Schedule_A,Schedule_B,Schedule_C)"+
                                "\n3.Service Name");
            int service_no = Integer.parseInt(scan.nextLine());
            String service_type;
            do{System.out.println("Please enter repairs or maintenance");service_type = scan.nextLine();}
            while(!(service_type.equals("repairs") || service_type.equals("maintenance")));
            String category;
            do{System.out.println("Please enter a service category(Engine,Exhaust,Electrical,Transmission,Tire,Heat and AC) or (Schedule_A,Schedule_B,Schedule_C)");category = scan.nextLine();}
            while(!(category.equals("engine")||category.equals("exhaust")||category.equals("electrical")||category.equals("transmission")||category.equals("tire")||category.equals("heat_and_ac")||category.equals("schedule_a")||category.equals("schedule_b")||category.equals("schedule_c")));
            System.out.println("Enter the name of the service:");
            String name = scan.nextLine();
            PreparedStatement insertService = conn.prepareStatement("insert into services(service_no,name) values(?,?)");
            PreparedStatement insertRepair;
            PreparedStatement insertRepairType;
            insertService.setInt(1, service_no);
            insertService.setString(2, name);
            insertService.executeQuery();
            insertRepair = conn.prepareStatement("insert into "+service_type+"(service_no) values (?)");
            insertRepairType = conn.prepareStatement("insert into "+category+"(service_no) values (?)");
            insertRepairType.setInt(1, service_no);
            insertRepair.setInt(1, service_no);
            insertRepair.executeQuery();
            insertRepairType.executeQuery();
            System.out.println("Press any of the following options from the menu to proceed further:");
            System.out.println("1.Add Service\n2.Go Back");
            int choice = Integer.parseInt(scan.nextLine());
            if(choice == 1){{
                try {
                    ResultSet manfResults = Manufacturer.getManfNames();
                    while(manfResults.next()){                        
                        PreparedStatement manfPS = conn.prepareStatement("insert into car_needs_service(manufacturer,service_no,duration)"+
                                                    "values (?,?,?)"); 
                        manfPS.setString(1, manfResults.getString("name"));
                        manfPS.setInt(2, service_no); 
                        System.out.print("Enter duration for "+name+" for manufacturer "+manfResults.getString("name").trim()+": ");
                        int duration = Integer.parseInt(scan.nextLine());
                        manfPS.setInt(3, duration);
                        manfPS.executeUpdate();
                    }
                        System.out.println("Success! Adding New Service...");
                    }
                  catch (SQLException e) {
                    System.out.println("Could not insert. Please try again");
                    e.printStackTrace();
                }}
            }
            else if (choice == 2){
                return;
            }
            else{
                System.out.println("Invalid Input");
            }
        }
        catch(SQLException e){
            System.out.println("Failure! Could not add new service\n");
            e.printStackTrace();}
    }
}