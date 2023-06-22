import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class RepairServices {
    public static Connection conn = dbConnect.conn;
    public static Scanner scan = new Scanner(System.in);
    public static HashSet<Integer> getEngineServices(){
        HashSet<Integer> services = new HashSet<Integer>();
        try{
            HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
            PreparedStatement ps = conn.prepareStatement("select name,service_no from services where service_no in (select service_no from engine)");
            ResultSet rs;
            int choice,i;
            do{
            rs = ps.executeQuery();    
            i = 1;
            while(rs.next()){
                map.put(i, rs.getInt("service_no"));
                System.out.println(i+"."+rs.getString("name"));
                i++;
            }
            System.out.println(i+".Go Back");
            System.out.println("Enter the number for the service you want to select:");
            choice = Integer.parseInt(scan.nextLine());
            if(choice != i)
                services.add(map.get(choice));
            System.out.println("Added service to cart");
            }
            while(choice !=i);
        }catch(SQLException e){e.printStackTrace();}
        return services;
    }

    public static HashSet<Integer> getExhaustServices(){
        HashSet<Integer> services = new HashSet<Integer>();
        try{
            HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
            PreparedStatement ps = conn.prepareStatement("select name,service_no from services where service_no in (select service_no from exhaust)");
            ResultSet rs;
            int choice,i;
            do{
            rs = ps.executeQuery();
            i = 1;
            while(rs.next()){
                map.put(i, rs.getInt("service_no"));
                System.out.println(i+"."+rs.getString("name"));
                i++;
            }
            System.out.println(i+".Go Back");
            System.out.println("Enter the number for the service you want to select:");
            choice = Integer.parseInt(scan.nextLine());
            if(choice != i)
                services.add(map.get(choice));
            System.out.println("Added service to cart");
            }
            while(choice !=i);
        }catch(SQLException e){e.printStackTrace();}
        return services;
    }

    public static HashSet<Integer> getElectricalServices(){
        HashSet<Integer> services = new HashSet<Integer>();
        try{
            HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
            PreparedStatement ps = conn.prepareStatement("select name,service_no from services where service_no in (select service_no from electrical)");
            ResultSet rs;
            int choice,i;
            do{
            rs = ps.executeQuery();
            i = 1;
            while(rs.next()){
                map.put(i, rs.getInt("service_no"));
                System.out.println(i+"."+rs.getString("name"));
                i++;
            }
            System.out.println(i+".Go Back");
            System.out.println("Enter the number for the service you want to select:");
            choice = Integer.parseInt(scan.nextLine());
            if(choice != i)
                services.add(map.get(choice));
            System.out.println("Added service to cart");
            }
            while(choice !=i);
        }catch(SQLException e){e.printStackTrace();}
        return services;
    }

    public static HashSet<Integer> getTransmissionServices(){
        HashSet<Integer> services = new HashSet<Integer>();
        try{
            HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
            PreparedStatement ps = conn.prepareStatement("select name,service_no from services where service_no in (select service_no from transmission)");
            ResultSet rs;
            int choice,i;
            do{
            rs = ps.executeQuery();
            i = 1;
            while(rs.next()){
                map.put(i, rs.getInt("service_no"));
                System.out.println(i+"."+rs.getString("name"));
                i++;
            }
            System.out.println(i+".Go Back");
            System.out.println("Enter the number for the service you want to select:");
            choice = Integer.parseInt(scan.nextLine());
            if(choice != i)
                services.add(map.get(choice));
            System.out.println("Added service to cart");
            }
            while(choice !=i);
        }catch(SQLException e){e.printStackTrace();}
        return services;
    }

    public static HashSet<Integer> getTireServices(){
        HashSet<Integer> services = new HashSet<Integer>();
        try{
            HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
            PreparedStatement ps = conn.prepareStatement("select name,service_no from services where service_no in (select service_no from tire)");
            ResultSet rs;
            int choice,i;
            do{
            rs = ps.executeQuery();
            i = 1;
            while(rs.next()){
                map.put(i, rs.getInt("service_no"));
                System.out.println(i+"."+rs.getString("name"));
                i++;
            }
            System.out.println(i+".Go Back");
            System.out.println("Enter the number for the service you want to select:");
            choice = Integer.parseInt(scan.nextLine());
            if(choice != i)
                services.add(map.get(choice));
            System.out.println("Added service to cart");
            }
            while(choice !=i);
        }catch(SQLException e){e.printStackTrace();}
        return services;
    }

    public static HashSet<Integer> getHeatingServices(){
        HashSet<Integer> services = new HashSet<Integer>();
        try{
            HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
            PreparedStatement ps = conn.prepareStatement("select name,service_no from services where service_no in (select service_no from heat_and_ac)");
            ResultSet rs;
            int choice,i;
            do{
            rs = ps.executeQuery();
            i = 1;
            while(rs.next()){
                map.put(i, rs.getInt("service_no"));
                System.out.println(i+"."+rs.getString("name"));
                i++;
            }
            System.out.println(i+".Go Back");
            System.out.println("Enter the number for the service you want to select:");
            choice = Integer.parseInt(scan.nextLine());
            if(choice != i)
                services.add(map.get(choice));
            System.out.println("Added service to cart");
            }
            while(choice !=i);
        }catch(SQLException e){e.printStackTrace();}
        return services;
    }
}
