import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Scanner;



public class Mechanic {

    public static Connection conn = dbConnect.conn;
    public static int m_id;
    public static Scanner scan = new Scanner(System.in);
    public static void mechanicMenu(int mch_id){
        m_id = mch_id;
        int choice;
        System.out.println("===============Welcome to the Mechanic Panel==============");
        do{
        System.out.println("Press any of the following options from the menu to proceed further:");
        System.out.println("1. View Schedule");
        System.out.println("2. Request Time Off");
        System.out.println("3. Request Swap");
        System.out.println("4. Accept/Reject Swap");
        System.out.println("5. Logout");
        choice = Integer.parseInt(scan.nextLine());
            switch (choice) {
            case 1:
                viewSchedule();
                break;
            case 2:
                requestTimeOff();
                break;
            case 3:
                requestTimeSwap();
                break;
            case 4:
                getSwapRequests();
                break;
            case 5:
                return;
            default:
                System.out.println("Selection is invalid");
                break;
            }
        } while (choice !=5);

    }
    public static void viewSchedule(){
            try{
                int choice;
            
                System.out.println("The list of timeslots for which the mechanic is booked are:");
                PreparedStatement ps = conn.prepareStatement("select t_id from bookings where m_id=?");
                ps.setInt(1, m_id);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    System.out.println(rs.getInt("t_id"));
                }
                do{
                System.out.println("1. Go Back");
                choice = Integer.parseInt(scan.nextLine());
                if(choice==1)
                    return;
                else
                    System.out.println("Invalid Input");
                }while(choice!=1);
            }
            catch(SQLException e){e.printStackTrace();}
    }
    public static void requestTimeOff(){
        int choice;
        int start_day;
        int start_week;
        int end_day;
        int end_week;
        int start_slot;
        int end_slot;
        try{
            System.out.println("Please enter the following details to proceed");
            System.out.println("1.Start Day\n2.Start Week\n3.Start Slot\n4.End Day\n5.End Week\n6.End Slot");
            start_day = Integer.parseInt(scan.nextLine());
            start_week = Integer.parseInt(scan.nextLine());
            start_slot = Integer.parseInt(scan.nextLine());
            end_day = Integer.parseInt(scan.nextLine());
            end_week = Integer.parseInt(scan.nextLine());
            end_slot = Integer.parseInt(scan.nextLine());
            do{
            System.out.println("1.Send Request");
            System.out.println("2.Go Back");
            choice = Integer.parseInt(scan.nextLine());
                    switch (choice) {
                    case 1:
                        int start_time_slot;
                        int end_time_slot;
                        PreparedStatement ps_start_time_slot = conn.prepareStatement("select id from time_slot where daysofweek=? and week=? and slot=?");
                        ps_start_time_slot.setInt(1, start_day);
                        ps_start_time_slot.setInt(2, start_week);
                        ps_start_time_slot.setInt(3, start_slot);
                        ResultSet rs=ps_start_time_slot.executeQuery();rs.next();
                        start_time_slot = rs.getInt("Id");
                        PreparedStatement ps_end_time_slot = conn.prepareStatement("select id from time_slot where daysofweek=? and week=? and slot=?");
                        ps_end_time_slot.setInt(1, end_day);
                        ps_end_time_slot.setInt(2, end_week);
                        ps_end_time_slot.setInt(3, end_slot);
                        rs=ps_end_time_slot.executeQuery();rs.next();
                        end_time_slot = rs.getInt("Id");
                        PreparedStatement ps_totalmechanics = conn.prepareStatement("select count(*) as count1 from employee e where e.Id=(select e1.Id from employee E1 where e1.E_Id=?)");
                        ps_totalmechanics.setInt(1, m_id);
                        rs=ps_totalmechanics.executeQuery();rs.next();
                        int total_mechanics = rs.getInt("count1");
                        int flag=0;
                        int booked;
                        PreparedStatement get_booked_slots1 = conn.prepareStatement("select t_id from bookings where m_id=?");
                        get_booked_slots1.setInt(1, m_id);
                        rs=get_booked_slots1.executeQuery();
                        HashSet<Integer> set=new HashSet<Integer>();
                        while(rs.next()){
                            booked=rs.getInt("t_id");
                            set.add(booked);
                        }
                        for(int i = start_time_slot;i<=end_time_slot;i++){
                            PreparedStatement ps_unavailable = conn.prepareStatement("select count(*) as count2 from mech_time_off mto, employee e2 where mto.M_Id=e2.E_Id and mto.T_ID=? and e2.Id=(select e3.Id from employee e3 where e3.E_Id=?) ");
                            ps_unavailable.setInt(1,i);
                            ps_unavailable.setInt(2, m_id);
                            rs=ps_unavailable.executeQuery();rs.next();
                            int unavailable = rs.getInt("count2");
                            if (total_mechanics-unavailable==3 || set.contains(i)){
                                flag=1;
                                break;
                            }
                        }
                        if (flag==1){
                            System.out.println("Cannot give time off");
                        }
                        else{
                            for(int i=start_time_slot;i<=end_time_slot;i++){
                                PreparedStatement insert_time_off = conn.prepareStatement("INSERT INTO mech_time_off(M_Id,t_Id) VALUES (?,?)");
                                insert_time_off.setInt(1, m_id);
                                insert_time_off.setInt(2,i);
                                insert_time_off.executeUpdate();
                            }
                        }
                        System.out.println("Paid time has been granted!");
                        break;
                    case 2:
                        return;
                    default:
                        System.out.println("Selection is invalid");
                        break;
                    }
                }while(choice!=2);
        }catch(SQLException e){e.printStackTrace();}
    }
    public static void requestTimeSwap(){
        try{
        System.out.println("Enter the followings details for offered times");
        System.out.println("1.Start TimeslotID\n2.End TimeslotID");
        System.out.println("Enter the followings details for requested times");
        System.out.println("1.MechanicID\n2.Start TimeslotID\n3.End TimeslotID");
        int start_time_slot1 = Integer.parseInt(scan.nextLine());
        int end_time_slot1 = Integer.parseInt(scan.nextLine());
        int m_id2=Integer.parseInt(scan.nextLine());
        int start_time_slot2 = Integer.parseInt(scan.nextLine());
        int end_time_slot2 = Integer.parseInt(scan.nextLine());
        //check if swap will overbook the mechanic
        PreparedStatement ps_week1 = conn.prepareStatement("select week from time_slot where id=?");
        ps_week1.setInt(1, start_time_slot2);
        ResultSet rs=ps_week1.executeQuery();rs.next();
        int week1 = rs.getInt("week");
        PreparedStatement ps_count1 = conn.prepareStatement("select count(*) as count1 from time_slot t, bookings b where t.id=b.t_id and t.week=? and b.m_id=?");
        ps_count1.setInt(1, week1);
        ps_count1.setInt(2, m_id);
        rs=ps_count1.executeQuery();rs.next();
        int count1 = rs.getInt("count1");
        if (count1+end_time_slot2-start_time_slot2>50){
            System.out.println("Not possible");
            return;
        }
        //Check if other mech is overbooked
        PreparedStatement ps_week2 = conn.prepareStatement("select week from time_slot where id=?");
        ps_week2.setInt(1, start_time_slot1);
        rs=ps_week2.executeQuery();rs.next();
        int week2 = rs.getInt("week");
        PreparedStatement ps_count2 = conn.prepareStatement("select count(*) as count1 from time_slot t, bookings b where t.id=b.t_id and t.week=? and b.m_id=?");
        ps_count2.setInt(1, week2);
        ps_count2.setInt(2, m_id2);
        rs=ps_count2.executeQuery();rs.next();
        int count2 = rs.getInt("count1");
        if (count2+end_time_slot1-start_time_slot1>50){
            System.out.println("Not possible");
            return;
        }
        // Check if mechanic gets double booked
        PreparedStatement get_booked_slots1 = conn.prepareStatement("select t_id from bookings where m_id=?");
        get_booked_slots1.setInt(1, m_id);
        rs=get_booked_slots1.executeQuery();
        int booked;
        HashSet<Integer> set=new HashSet<Integer>();  
        while(rs.next()){
            booked=rs.getInt("t_id");
            set.add(booked);
        }
        for(int i=start_time_slot2;i<=end_time_slot2;i++){
            if(set.contains(i)){
                System.out.println("Not possible");
                return;
            }
        }
        //Check if other mechanic gets double booked
        PreparedStatement get_booked_slots2 = conn.prepareStatement("select t_id from bookings where m_id=?");
        get_booked_slots2.setInt(1, m_id2);
        rs=get_booked_slots2.executeQuery();
        HashSet<Integer> set2=new HashSet<Integer>();  
        while(rs.next()){
            booked=rs.getInt("t_id");
            set2.add(booked);
        }
        for(int i=start_time_slot1;i<=end_time_slot1;i++){
            if(set.contains(i)){
                System.out.println("Not possible");
                return;
            }
        }
        PreparedStatement insert_time_swap = conn.prepareStatement("INSERT INTO swap_request(request_id,M_Id1,M_Id2,start_time_slot1,end_time_slot1,start_time_slot2,end_time_slot2) VALUES (?,?,?,?,?,?,?)");
        int request_id = (int)(Math.random()*100000);
        insert_time_swap.setInt(1, request_id);
        insert_time_swap.setInt(2, m_id);
        insert_time_swap.setInt(3,m_id2);
        insert_time_swap.setInt(4, start_time_slot1);
        insert_time_swap.setInt(5,end_time_slot1);
        insert_time_swap.setInt(6, start_time_slot2);
        insert_time_swap.setInt(7,end_time_slot2);
        insert_time_swap.executeUpdate();
        System.out.println("Successfully added request");
    }catch(SQLException e){e.printStackTrace();}
    }
    public static void getSwapRequests(){
        try{
            PreparedStatement get_swap_requests = conn.prepareStatement("select * from swap_request where m_id2=?");
            get_swap_requests.setInt(1, m_id);
            ResultSet rs = get_swap_requests.executeQuery();
            while(rs.next()){
            System.out.println("Swaps have been requested by:");
            System.out.println("Swap RequestID: " + rs.getString("request_id"));
            System.out.println("Mechanic ID: " + rs.getString("m_id1"));
            System.out.println("Offered Start Time: "+ rs.getString("start_time_slot1"));
            System.out.println("Offered End Time: "+ rs.getString("end_time_slot1"));
            System.out.println("Requested Start Time: "+rs.getString("start_time_slot2"));
            System.out.println("Requested End Time: "+rs.getString("end_time_slot2"));
            }
            int choice;
            do{
            System.out.println("Press any of the following options from the menu to proceed further:");
            System.out.println("1.Manage Invoices\n2.Go Back");
            choice = Integer.parseInt(scan.nextLine());
            if(choice == 1){
                try {
                    System.out.println("Enter the RequestID for the swap request:");
                    int request_id = Integer.parseInt(scan.nextLine());
                    PreparedStatement curr_swap_request = conn.prepareStatement("select * from swap_request where request_id=?");
                    curr_swap_request.setInt(1, request_id);
                    rs = curr_swap_request.executeQuery();rs.next();
                    int choice2;
                    System.out.println("Press any of the following options from the menu to proceed further:");
                    do{
                    System.out.println("1.Approve Request\n2.Deny Request\n3.Go Back");
                    choice2 = Integer.parseInt(scan.nextLine());
                    if(choice2 == 1){
                        PreparedStatement ps = conn.prepareStatement("update bookings set m_id=? where m_id=? and t_id>=? and t_id<=?");
                        ps.setInt(1, m_id);
                        ps.setInt(2, rs.getInt("m_id1"));
                        ps.setInt(3, rs.getInt("start_time_slot1"));
                        ps.setInt(4, rs.getInt("end_time_slot1"));
                        ps.executeUpdate();
                        PreparedStatement ps2 = conn.prepareStatement("update bookings set m_id=? where m_id=? and t_id>=? and t_id<=?");
                        ps2.setInt(1, rs.getInt("m_id1"));
                        ps2.setInt(2, m_id);
                        ps2.setInt(3, rs.getInt("start_time_slot2"));
                        ps2.setInt(4, rs.getInt("end_time_slot2"));
                        ps2.executeUpdate();
                        System.out.println("Swap completed successfully!");
                        PreparedStatement ps_del = conn.prepareStatement("delete from swap_request where request_id=?");
                        ps_del.setInt(1,request_id);
                        ps_del.executeUpdate();
                    }
                    else if (choice2 == 2){
                        PreparedStatement ps = conn.prepareStatement("delete from swap_request where request_id=?");
                        ps.setInt(1,request_id);
                        ps.executeUpdate();
                        System.out.println("Swap has been denied.");
                    }
                    else if (choice2 == 3){
                        return;
                    }
                    else{
                        System.out.println("Invalid Input");
                    }} while(choice2!=3);
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
            }while(choice!=2);
        }catch(SQLException e){e.printStackTrace();}
    }
}