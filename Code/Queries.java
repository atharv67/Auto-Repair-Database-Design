import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Queries {
    public static Connection conn = dbConnect.conn;
    public static Scanner scan = new Scanner(System.in);
    public static void runQueries(){
        try{
            System.out.println("Choose the queries you want to execute:(1-6)\n7.Exit");
            int choice;
            PreparedStatement ps;
            ResultSet rs;
            do{
                choice = Integer.parseInt(scan.nextLine());
                switch(choice){
                    case 1:
                        ps = conn.prepareStatement("select S_id,count(*) as count from customer group by S_id having count(*)=(select max(count1) from  (select S_id,count(*) as count1 from customer group by S_id) temp)");
                        rs = ps.executeQuery();
                        while(rs.next()){
                            System.out.println("Service No: "+rs.getInt("s_id"));
                            System.out.println("Count: "+rs.getInt(2));
                        }
                        break;
                    case 2:
                        ps = conn.prepareStatement("select avg(cost) from car_has_cost_of_service where manufacturer='Honda' and service_no=(select service_no from services where name='Evaporator Repair')");
                        rs = ps.executeQuery();
                        while(rs.next()){
                            System.out.println("Average Count: "+rs.getInt(1));
                        }
                        break;
                    case 3:
                        ps = conn.prepareStatement("select cust_id from invoice where paid=0 and S_id=30003");
                        rs = ps.executeQuery();
                        while(rs.next()){
                            System.out.println("Customer ID(s): "+rs.getInt("cust_id"));
                        }
                        break;
                    case 4:
                        ps = conn.prepareStatement("select s.* from (select service_no from maintenance m intersect select service_no from repairs r) temp, services s where temp.service_no=s.service_no");
                        rs = ps.executeQuery();
                        while(rs.next()){
                            System.out.println("Service ID(s): "+rs.getInt(1));
                            System.out.println("Service Name(s): "+rs.getString(2));
                        }
                        break;                    
                    case 5:
                        ps = conn.prepareStatement("select abs(count1-count2) as diff"+
                        " from (select sum(c1.cost) as count1,c1.manufacturer  from car_has_cost_of_service c1 where c1.id=30001 and c1.manufacturer='Toyota' and c1.service_no in(select s1.service_no from services s1 where s1.name='A' or s1.name='Belt Replacement') group by manufacturer) table1,"+
                        " (select sum(c2.cost) as count2,c2.manufacturer  from car_has_cost_of_service c2 where c2.id=30002 and c2.manufacturer='Toyota' and c2.service_no in(select s2.service_no from services s2 where s2.name='A' or s2.name='Belt Replacement')group by manufacturer) table2 "+
                        " where table1.manufacturer=table2.manufacturer");
                        rs = ps.executeQuery();
                        while(rs.next()){
                            System.out.println("Difference: "+rs.getInt(1));
                        }
                        break;                    
                    case 6:
                        ps = conn.prepareStatement("SELECT CASE c.last_schedule WHEN 'A' THEN 'B' WHEN 'B' THEN 'C' ELSE 'A' END FROM car c WHERE c.vin = '34KLE19D'");
                        rs = ps.executeQuery();
                        while(rs.next()){
                            System.out.println("Schedule: "+rs.getString(1));
                        }
                        break;                    
                    case 7:
                    return;
                    default:
                    System.out.println("Invalid Input");
                }
            }while(choice!=7);
        }catch(SQLException e){e.printStackTrace();}
    }
}
