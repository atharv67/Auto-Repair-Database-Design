import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Scanner;

public class Customer {

    public static Connection conn = dbConnect.conn;
    public static int cust_id;
    public static int service_centre_no;
    public static String vin;
    public static Scanner scan = new Scanner(System.in);
    public static HashSet<Integer> cart = new HashSet<Integer>();

    public static void customerMenu(int c_id){
        cust_id = c_id;
        try{
        PreparedStatement getSc = conn.prepareStatement("select s_id from customer where id=?");
        getSc.setInt(1,cust_id);
        ResultSet getScresult = getSc.executeQuery();getScresult.next();
        service_centre_no = getScresult.getInt("s_id");
        }catch(SQLException e){e.printStackTrace();}
        int choice;
        System.out.println("===============Welcome to the Customer Panel==============");
        do {
        System.out.println("Press any of the following options from the menu to proceed further:");
        System.out.println("1. View and Update Profile");
        System.out.println("2. View and Schedule Services");
        System.out.println("3. Invoices");
        System.out.println("4. Logout");
        choice = Integer.parseInt(scan.nextLine());
            switch (choice) {
            case 1:
                customerProfileMenu();
                break;
            case 2:
                scheduleServiceMenu();
                break;
            case 3:
                invoicesMenu();
                break;
            case 4:
                return;
            default:
                System.out.println("Selection is invalid");
                break;
            }
        } while (choice != 4);

    }

    public static void customerProfileMenu(){
        int choice;
        do {
        System.out.println("Press any of the following options from the menu to proceed further:");
        System.out.println("1. View Profile");
        System.out.println("2. Add Car");
        System.out.println("3. Delete Car");
        System.out.println("4. Go Back");
        choice = Integer.parseInt(scan.nextLine());
            switch (choice) {
            case 1:
                viewCustomerProfile();
                break;
            case 2:
                addCar();
                break;
            case 3:
                deleteCar();
                break;
            case 4:
                return;
            default:
                System.out.println("Selection is invalid");
                break;
            }
        } while (choice != 4);

    }
    public static void viewCustomerProfile(){
        try{
            System.out.println("-----------Customer Details-------------------");
            PreparedStatement ps = conn.prepareStatement("select * from Customer where id = ?");
            ps.setInt(1, cust_id);
            ResultSet rs = ps.executeQuery();rs.next();
            System.out.println("Customer ID: "+rs.getInt(1));
            System.out.println("Service Center ID: "+rs.getInt(2));
            System.out.println("First Name: "+rs.getString(3));
            System.out.println("Last Name: "+rs.getString(4));
            System.out.println("Address: "+rs.getInt(7));
            System.out.println("Phone: "+rs.getInt(9));
            System.out.println("Email: "+rs.getInt(8));
            System.out.println("Status: "+rs.getInt(5));
            System.out.println("Standing: "+rs.getInt(6));
            System.out.println("-----------Customer Car Details----------------");
            PreparedStatement ps_car = conn.prepareStatement("select car.vin,car.mileage,car.manufacturer,"+
                                    "car.last_schedule,car.year from customer, car, cust_has_car chc "
                                    +"where customer.id=? and customer.S_id=? and customer.Id=chc.Id and customer.S_Id=chc.S_Id and chc.vin=car.vin");
            ps_car.setInt(1, cust_id);
            ps_car.setInt(2, service_centre_no);
            ResultSet rs_car = ps_car.executeQuery();
            while(rs_car.next()){ 
            System.out.println("VIN: "+rs_car.getString("vin"));
            System.out.println("Mileage: "+rs_car.getInt("mileage"));
            System.out.println("Manufacturer: "+rs_car.getString("manufacturer"));
            System.out.println("Last Schedule: "+rs_car.getString("last_schedule"));
            System.out.println("Year: "+rs_car.getString("year"));
            System.out.println("-----------------------------------------------");
            }
            System.out.println("1.Go Back");
            int choice;
            do{
            choice = Integer.parseInt(scan.nextLine());
            if(choice == 1)
                return;
            else
                System.out.println("Invalid Input");
            }
            while(choice!=1);
        }catch(SQLException e){e.printStackTrace();}
    }

    public static void addCar(){
        try{
            System.out.println("Please enter the following details to add the car.");
            System.out.println("1.VIN\n2.Car Manufacturer\n3.Mileage\n4.Year");
            String vin = scan.nextLine();
            String manf = scan.nextLine();
            int mileage = Integer.parseInt(scan.nextLine());
            int year = Integer.parseInt(scan.nextLine());
            PreparedStatement ps = conn.prepareStatement("select s_id from customer where id=?");
            ps.setInt(1, cust_id);
            ResultSet rs = ps.executeQuery();rs.next();
            int service_centre_no = rs.getInt("s_id");
            String insertCar = "insert into car(Vin,mileage,manufacturer,last_schedule,year)"+
                                "VALUES (?,?,?,'C',?)";
            String insertCustHasCar = "insert into cust_has_car(vin,Id,S_id)"+
                                        "VALUES (?,?,?)";
            PreparedStatement ps_car = conn.prepareStatement(insertCar);
            PreparedStatement ps_custhascar = conn.prepareStatement(insertCustHasCar);
            ps_car.setString(1, vin);
            ps_car.setInt(2, mileage);
            ps_car.setString(3, manf);
            ps_car.setInt(4, year);
            ps_custhascar.setString(1,vin);
            ps_custhascar.setInt(2,cust_id);
            ps_custhascar.setInt(3,service_centre_no);
            System.out.println("Press any of the following options from the menu to proceed further:");
            System.out.println("1.Add Car\n2.Go Back");
            int choice = Integer.parseInt(scan.nextLine());
            if(choice == 1){
                try {
                    ps_car.executeUpdate();
                    ps_custhascar.executeUpdate();
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
            System.out.println("Success! Adding New Car...");
        }catch(SQLException e){e.printStackTrace();}
    }

    public static void deleteCar(){
        try{
            System.out.println("Please select the car to delete.");
            System.out.println("-----------------------------------------");
            PreparedStatement ps_car = conn.prepareStatement("select car.vin,car.mileage,car.manufacturer,"+
                                    "car.last_schedule,car.year from customer, car, cust_has_car chc "
                                    +"where customer.id=? and customer.S_id=? and customer.Id=chc.Id and customer.S_Id=chc.S_Id and chc.vin=car.vin");
            ps_car.setInt(1, cust_id);
            ps_car.setInt(2, service_centre_no);                                    
            ResultSet rs_car = ps_car.executeQuery();
            while(rs_car.next()){ 
            System.out.println("VIN: "+rs_car.getString("vin"));
            System.out.println("Mileage: "+rs_car.getInt("mileage"));
            System.out.println("Manufacturer: "+rs_car.getString("manufacturer"));
            System.out.println("Last Schedule: "+rs_car.getString("last_schedule"));
            System.out.println("Year: "+rs_car.getString("year"));
            System.out.println("-----------------------------------------");
            }
            System.out.println("Press any of the following options from the menu to proceed further:");
            System.out.println("1.Delete Car\n2.Go Back");
            int choice = Integer.parseInt(scan.nextLine());
            if(choice == 1){
                try {
                    System.out.println("Enter the VIN of the car you want to delete:");
                    String deleteCar = scan.nextLine();
                    PreparedStatement delCar = conn.prepareStatement("Delete from car where vin=?");
                    delCar.setString(1, padRight(deleteCar,100));
                    delCar.executeUpdate();
                    System.out.println("Success! Deleted Car...");
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
        }catch(SQLException e){e.printStackTrace();}
    }

    public static void scheduleServiceMenu(){
        
            System.out.println("Press any of the following options from the menu to proceed further:");
            int choice;
            do{
            System.out.println("1. View Service History");
            System.out.println("2. Schedule Service");
            System.out.println("3. Go Back");
            choice = Integer.parseInt(scan.nextLine());
                switch (choice) {
                case 1:
                    viewServicesHistory();
                    break;
                case 2:
                    scheduleNewService();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Selection is invalid");
                    break;
                }
            } while (choice != 3);
    }
    public static void viewServicesHistory(){
        try{
            ResultSet getCarResults;
            String vin;
            String manf;
            int service_centre_no;
            int invoice_no;
            boolean run = false;
            do{
                if(run){System.out.println("Not a valid VIN.");}
                System.out.println("Press enter the VIN of the car whos service history you want to view:");
                vin = scan.nextLine();
                PreparedStatement getCar = conn.prepareStatement("select manufacturer from car where vin=?");
                getCar.setString(1, padRight(vin,100));
                getCarResults = getCar.executeQuery();
                run = true;
                } while(getCarResults.next()==false);
                manf = getCarResults.getString("manufacturer");
                PreparedStatement ps;
                ResultSet rs;
                System.out.println("Press any of the following options from the menu to proceed further:");
                System.out.println("1.Show History\n2.Go Back");
                int choice = Integer.parseInt(scan.nextLine());
                if(choice == 1){
                    try {
                        ps = conn.prepareStatement("select invoice_id,s_id,vin from invoice where vin=?");
                        ps.setString(1, padRight(vin,100));
                        rs = ps.executeQuery();
                        while(rs.next()){
                        service_centre_no = rs.getInt("s_id");
                        invoice_no = rs.getInt("invoice_id");
                        PreparedStatement getServices = conn.prepareStatement("select service_no from invoice_has_service where invoice_id=?");
                        getServices.setInt(1, invoice_no);
                        ResultSet getServicesResults = getServices.executeQuery();
                        System.out.println("Invoice Number: "+invoice_no);
                        System.out.println("Service Center Number: "+service_centre_no);
                        System.out.println("VIN Number: "+vin);
                        while(getServicesResults.next()){
                            PreparedStatement getServiceDetails = conn.prepareStatement("select cost from car_has_cost_of_service where service_no=? and manufacturer=?");
                            getServiceDetails.setInt(1, getServicesResults.getInt("service_no"));
                            getServiceDetails.setString(2, padRight(manf,100));
                            ResultSet getServiceDetailsResults = getServiceDetails.executeQuery();getServiceDetailsResults.next();
                            System.out.println("Service ID: "+getServicesResults.getInt("service_no"));
                            System.out.println("Cost: "+getServiceDetailsResults.getInt("cost"));
                        }}
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                else if (choice == 2){
                    return;
                }
                else{
                    System.out.println("Invalid Input");
                } 
        }catch(SQLException e){e.printStackTrace();}   
    }
    public static void scheduleNewService(){
        try{
            ResultSet getCarResults;
            boolean run = false;
            do{
                if(run){System.out.println("Not a valid VIN.");}
                System.out.println("Press enter the VIN of the car whos maintenance or repair you want to schedule:");
                vin = scan.nextLine();
                PreparedStatement getCar = conn.prepareStatement("select manufacturer from car where vin=?");
                getCar.setString(1, padRight(vin,100));
                getCarResults = getCar.executeQuery();
                run = true;
            }
                while(getCarResults.next()==false);
            System.out.println("Press any of the following options from the menu to proceed further:");
            int choice;
            do{
            System.out.println("1. Add Schedule Maintenance");
            System.out.println("2. Add Schedule Repair");
            System.out.println("3. View Cart and Schedule Time Slot");
            System.out.println("4. Go Back");
            choice = Integer.parseInt(scan.nextLine());
                switch (choice) {
                case 1:
                    addMaintenanceService(vin);
                    break;
                case 2:
                    addRepairService();
                    break;
                case 3:
                    viewCart();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Selection is invalid");
                    break;
                }
            } while (choice != 4);
        }catch(SQLException e){e.printStackTrace();}
    }
    public static void addMaintenanceService(String vin){
        try{
            System.out.println("The service you are currently eligible for is:");
            PreparedStatement ps = conn.prepareStatement("select last_schedule from car where vin=?");
            ps.setString(1, padRight(vin,100));
            ResultSet rs = ps.executeQuery();rs.next();
            String next_schedule="";
            String last_schedule = rs.getString("last_schedule");
            if(last_schedule.equals("C"))
                next_schedule = "A";
            else if (last_schedule.equals("A"))
                next_schedule = "B";
            else if(last_schedule.equals("B"))
                next_schedule = "C";
            System.out.println("Schedule "+ next_schedule);
            System.out.println("Press any of the following options from the menu to proceed further:");
            System.out.println("1.Accept and add to cart\n2.Go Back");
            int choice = Integer.parseInt(scan.nextLine());
            if(choice == 1){
                try {
                    ps = conn.prepareStatement("select service_no from services where name=?");
                    ps.setString(1, padRight(next_schedule,20));
                    rs = ps.executeQuery();rs.next();
                    int service_no = rs.getInt("service_no");
                    cart.add(service_no);
                    System.out.println("Successfully added to cart");
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
        }catch(SQLException e){e.printStackTrace();}
    }

    public static void addRepairService(){
            System.out.println("The service you are currently eligible for are:");
            int choice;
            HashSet<Integer> services = new HashSet<Integer>();
            do{
                System.out.println("1.Engine Services\n2.Exhaust Services\n3.Electrical Services\n4.Transmission Services"+
                                "\n5.Tire Services\n6.Heating and AC Services\n7.Go Back");
                choice = Integer.parseInt(scan.nextLine());
                    switch (choice) {
                    case 1:
                        services = RepairServices.getEngineServices();
                        cart.addAll(services);
                        break;
                    case 2:
                        services = RepairServices.getExhaustServices();
                        cart.addAll(services);
                        break;
                    case 3:
                        services = RepairServices.getElectricalServices();
                        cart.addAll(services);
                        break;
                    case 4:
                        services = RepairServices.getTransmissionServices();
                        cart.addAll(services);
                        break;
                    case 5:
                        services = RepairServices.getTireServices();
                        cart.addAll(services);
                        break;
                    case 6:
                        services = RepairServices.getHeatingServices();
                        cart.addAll(services);
                        break;
                    case 7:
                        return;
                    default:
                        System.out.println("Selection is invalid");
                        break;
                    }
                    
            } while (choice != 7); 
    }
        public static void viewCart(){
        try{
            System.out.println("The services added to your cart are:");
            PreparedStatement ps;
            ResultSet rs;
            for(int service_no: cart){
                ps = conn.prepareStatement("select name from services where service_no=?");
                ps.setInt(1,service_no);
                rs = ps.executeQuery();
                if(!rs.next())
                    System.out.println("Cart is empty");
                else{
                    System.out.println("Service Number: "+service_no +", Service Name: "+rs.getString("name"));
                }
            }
            System.out.println("Press any of the following options from the menu to proceed further:");
            int choice;
            do{
            System.out.println("1. Proceed with Scheduling");
            System.out.println("2. Go Back");
            choice = Integer.parseInt(scan.nextLine());
                switch (choice) {
                case 1:
                    scheduleTime();
                    break;
                case 2:
                    return;
                default:
                    System.out.println("Selection is invalid");
                    break;
                }
            } while (choice != 2);
        }catch(SQLException e){e.printStackTrace();}
    }
    public static void scheduleTime(){
        try{
            int duration=0;
            int invId = (int)(Math.random() * 10000);
            for(int service_no:cart){
                PreparedStatement ps = conn.prepareStatement("select manufacturer from car where vin=?");
                ps.setString(1, padRight(vin,100));
                ResultSet rs =ps.executeQuery();rs.next();
                String manf = rs.getString("manufacturer");
                ps = conn.prepareStatement("select duration from car_needs_service where manufacturer = ? and service_no =?");
                ps.setString(1, manf);
                ps.setInt(2, service_no);
                rs = ps.executeQuery();rs.next();
                duration+=rs.getInt("duration");
            }
            System.out.println("The total duration of the services is: "+duration);
            System.out.println("The time slots available are:");
            Timeslot.getTimesSlots(cust_id,service_centre_no,duration);
            System.out.println("Enter the following details to proceed to scheduling services");
            System.out.println("1.Mechanic ID\n2.Week\n3.Day\n4.Start Time");
            int mech_id = Integer.parseInt(scan.nextLine());
            int week = Integer.parseInt(scan.nextLine());
            int day = Integer.parseInt(scan.nextLine());
            int start_time = Integer.parseInt(scan.nextLine());
            int start_time_copy = start_time;
            while(duration>0){
                PreparedStatement insertBookings= conn.prepareStatement("insert into bookings values(?,?)");
                insertBookings.setInt(1, mech_id);
                insertBookings.setInt(2, start_time);
                insertBookings.executeQuery();
                start_time++;
                duration--;
            }
            if(cart.contains(113) ||cart.contains(114)||cart.contains(115)){
                String x = "A";
                if(cart.contains(113))
                    x ="B";
                else if (cart.contains(114))
                    x="C";
                PreparedStatement inps = conn.prepareStatement("update car set last_schedule = ?");
                inps.setString(1, padRight(x,1));
                inps.executeUpdate();
            }
            PreparedStatement insertInvoice = conn.prepareStatement("insert into Invoice(invoice_id,m_id,cust_id,s_id,vin,start_time_slot,paid)"+
                                            "values(?,?,?,?,?,?,?)");
            insertInvoice.setInt(1, invId);
            insertInvoice.setInt(2, mech_id);
            insertInvoice.setInt(3, cust_id);
            insertInvoice.setInt(4, service_centre_no);
            insertInvoice.setString(5, padRight(vin,100));
            insertInvoice.setInt(6, start_time_copy);
            insertInvoice.setInt(7,0);
            insertInvoice.executeQuery();
            System.out.println("Successfully booked slots");
            System.out.println("Successfully generated invoice");
            for(int service_no:cart){
            PreparedStatement invoiceServices = conn.prepareStatement("insert into invoice_has_service values(?,?)");
            invoiceServices.setInt(1, invId);
            invoiceServices.setInt(2, service_no);
            invoiceServices.executeQuery();
            }
        }catch(SQLException e){e.printStackTrace();}
    }
    public static void invoicesMenu(){
        try{
        int choice;
        PreparedStatement ps = conn.prepareStatement("select invoice_id,paid from invoice where cust_id=?");
        ps.setInt(1, cust_id);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            System.out.println("Invoice Number: "+rs.getInt("invoice_id")+", Invoice Status: "+(rs.getInt("paid")==1?"Paid":"Unpaid"));
        }
        do {
        System.out.println("Press any of the following options from the menu to proceed further:");
        System.out.println("1. View Invoice Details");
        System.out.println("2. Pay Invoice");
        System.out.println("3. Go Back");
        choice = Integer.parseInt(scan.nextLine());
            switch (choice) {
            case 1:
                viewInvoiceDetails();
                break;
            case 2:
                payInvoice();
                break;
            case 3:
                return;
            default:
                System.out.println("Selection is invalid");
                break;
            }
        } while (choice != 3);
        }catch(SQLException e){e.printStackTrace();}
    }
    public static void viewInvoiceDetails(){
        try{
            ResultSet getInvoiceResults;
            int invoice_id;
            int total_cost=0;
            boolean run = false;
            PreparedStatement getInvoice;
            String manf;
            do{
                if(run){System.out.println("Not a valid Invoice ID.");}
                System.out.println("Press enter the Invoice ID for the invoice you want to retrieve:");
                invoice_id = Integer.parseInt(scan.nextLine());
                getInvoice = conn.prepareStatement("select * from invoice where invoice_id=?");
                getInvoice.setInt(1, invoice_id);
                getInvoiceResults = getInvoice.executeQuery();
                run = true;
            }
            while(getInvoiceResults.next()==false);
            PreparedStatement getManf = conn.prepareStatement("select manufacturer from car where vin=?");
            getManf.setString(1, getInvoiceResults.getString("vin"));
            ResultSet getManfResults = getManf.executeQuery();getManfResults.next();
            manf = getManfResults.getString("manufacturer");
            System.out.println("Press any of the following options from the menu to proceed further:");
            System.out.println("1.View Invoice\n2.Go Back");
            int choice = Integer.parseInt(scan.nextLine());
            if(choice == 1){
                try {
                    System.out.println("Invoice ID:"+getInvoiceResults.getString("invoice_id"));
                    System.out.println("Customer ID:"+getInvoiceResults.getString("cust_id"));
                    System.out.println("VIN:"+getInvoiceResults.getString("vin"));
                    System.out.println("Invoice Status:"+getInvoiceResults.getInt("paid"));
                    System.out.println("Mechanic ID:"+getInvoiceResults.getInt("m_id"));
                    PreparedStatement invoiceServices = conn.prepareStatement("select service_no from invoice_has_service where invoice_id=?");
                    invoiceServices.setInt(1, invoice_id);
                    ResultSet rs = invoiceServices.executeQuery();
                    while(rs.next()){
                        System.out.println("Service ID(s):"+rs.getInt("service_no"));
                        PreparedStatement serviceCost = conn.prepareStatement("select cost from car_has_cost_of_service where service_no=? and manufacturer=?");
                        serviceCost.setInt(1,rs.getInt("service_no"));
                        serviceCost.setString(2,padRight(manf,100));
                        ResultSet serviceCostResult = serviceCost.executeQuery();serviceCostResult.next();
                        System.out.println("Cost of Service:"+serviceCostResult.getInt("cost"));
                        total_cost+=serviceCostResult.getInt("cost");
                    }
                    System.out.println("Total cost of Services:"+total_cost);
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
        }catch(SQLException e){e.printStackTrace();}
    }
    public static void payInvoice(){
        try{
            int invoice_id;
            boolean run = false;
            PreparedStatement getInvoice;
            ResultSet getInvoiceResults;
            do{
                if(run){System.out.println("Not a valid Invoice ID.");}
                System.out.println("Press enter the Invoice ID for the invoice you want to pay:");
                invoice_id = Integer.parseInt(scan.nextLine());
                getInvoice = conn.prepareStatement("select * from invoice where invoice_id=?");
                getInvoice.setInt(1, invoice_id);
                getInvoiceResults = getInvoice.executeQuery();
                run = true;
            }
                while(getInvoiceResults.next()==false);
            System.out.println("Press any of the following options from the menu to proceed further:");
            System.out.println("1.Pay Invoice\n2.Go Back");
            int choice = Integer.parseInt(scan.nextLine());
            if(choice == 1){
                try {
                    PreparedStatement payInvoice = conn.prepareStatement("update invoice set paid=1 where invoice_id=?");
                    payInvoice.setInt(1,invoice_id);
                    payInvoice.executeUpdate();
                    System.out.println("Invoice had been paid!");
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
        }catch(SQLException e){e.printStackTrace();}
    }
    public static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);  
   }
}