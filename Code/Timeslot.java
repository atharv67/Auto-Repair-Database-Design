import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Timeslot {

    public static Connection conn = dbConnect.conn;
    
    public static void getTimesSlots(int c_id,int service_center_no,int duration){
        ArrayList<ArrayList<Integer>> result_available=new ArrayList<ArrayList<Integer>>();
        try{
            ArrayList<Integer> all_mechanics=new ArrayList<Integer>();
            PreparedStatement ps_get_mechanics = conn.prepareStatement("select m.e_Id from mechanics m, employee e where m.e_id=e.e_id and e.id = ?");
            ps_get_mechanics.setInt(1,service_center_no);
            ResultSet rs=ps_get_mechanics.executeQuery();
            // PreparedStatement ps= conn.prepareStatement("select sat_open from service_centre where id=?");
            // ps.setInt(1,service_center_no);
            //ResultSet psResultSet = ps.executeQuery();psResultSet.next();
            //String sat_open = psResultSet.getString("sat_open");
            while(rs.next()){
                int mech=rs.getInt("e_id");
                all_mechanics.add(mech);
            }
            for (int mechanic_id:all_mechanics){
                for (int i=1; i<=5; i++){
                    for (int j=1;j<=6;j++){
                        if(i==5 && j>3)
                            break;
                        //if(sat_open.equals("N") && j>5)
                        //  break;
                        PreparedStatement ps_get_available = conn.prepareStatement("((select t.id, m.e_Id from mechanics m cross join time_slot t, employee e where e.e_id=m.e_id and t.daysofweek=? and t.week=? and m.e_id=? and  e.id=(select e1.Id from employee e1 where e1.e_id=?)) minus (select t_id, m_id from bookings)) minus (select t_id,m_id from mech_time_off)");
                        ps_get_available.setInt(1, j);
                        ps_get_available.setInt(2, i);
                        ps_get_available.setInt(3, mechanic_id);
                        ps_get_available.setInt(4, mechanic_id);
                        ResultSet rs_get_available=ps_get_available.executeQuery();
                            int previous; int current;int start;
                            if(rs_get_available.next()==false)
                                continue;
                            start=rs_get_available.getInt("id");
                            previous=rs_get_available.getInt("id");
                            while(rs_get_available.next()){
                                current=rs_get_available.getInt("id");
                                if (current!=previous+1){
                                    if (duration<=previous-start){
                                        ArrayList<Integer> slots = new ArrayList<Integer>();
                                        slots.add(mechanic_id);
                                        slots.add(start);
                                        slots.add(previous);
                                        slots.add(i);
                                        slots.add(j);
                                        result_available.add(slots);
                                    }
                                    start=current;
                                    previous=current;
                                }
                                else
                                    previous=current;
                                
                            }
                            if (duration<=previous-start){
                                ArrayList<Integer> slots = new ArrayList<Integer>();
                                slots.add(mechanic_id);
                                slots.add(start);
                                slots.add(previous);
                                slots.add(i);
                                slots.add(j);
                                result_available.add(slots);
                            }
                            ps_get_available.close();
                            rs_get_available.close();
                        }
                    }
                }
            HashMap<Integer,String> map =new HashMap<Integer,String>();
            map.put(1, "Mechanic ID:    ");
            map.put(4, "Week:    ");
            map.put(5, "Day:    ");
            map.put(2, "Start Time:    ");
            map.put(3, "End Time:    ");
            for (ArrayList<Integer> i:result_available){
                int x = 1;
                for(int j:i){
                    System.out.print(map.get(x++)+padLeft(""+j,3)+"\t\t");
                }
                System.out.println();
            }
        }catch(SQLException e){e.printStackTrace();}
    }
    public static String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);  
   }
}