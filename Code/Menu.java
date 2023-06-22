import java.util.Scanner;

public class Menu {
    public static void menuOptions() {
        Scanner scan = new Scanner(System.in);
        int choice;
        System.out.println("===============Welcome==============");
        do {
            System.out.println("Press any of the following options from the menu to proceed further:");
            System.out.println("1. Login");
            System.out.println("2. Exit");
            System.out.println("3. Queries");
            choice = Integer.parseInt(scan.nextLine());
                switch (choice) {
                case 1:
                    Login.loginPage();
                    break;
                case 2:
                    scan.close();
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                case 3:
                    Queries.runQueries();
                    break;
                default:
                    System.out.println("Selection is invalid");
                    break;
                }

            } while (choice != 2);
    }
}
