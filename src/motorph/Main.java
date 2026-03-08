package motorph;


import java.io.*;      // allows reading files like CSV
import java.util.*;    // allows using Scanner


public class MotorPH_Payroll_System_Group2_ByteBeans {

    /**
     * MotorPH Simple Payroll System
     */
    public static void main(String[] args) {

        // Scanner for user input
        Scanner scanner = new Scanner(System.in);

        // System header
        System.out.println("=================================");
        System.out.println("                                 ");
        System.out.println("             MotorPH             ");
        System.out.println("         Payroll System          ");
        System.out.println("                                 ");
        System.out.println("=================================");

        // =============================
        // LOGIN SECTION
        // =============================

        System.out.print("Username: ");
        String username = scanner.nextLine(); // read username

        System.out.print("Password: ");
        String password = scanner.nextLine(); // read password

        // check if login is payroll staff
        boolean payrollStaff =
                username.equals("payroll_staff") && password.equals("12345");

        // check if login is employee
        boolean employee =
                username.equals("employee") && password.equals("12345");

        // if login credentials are incorrect
        if (!payrollStaff && !employee) {

            System.out.println("---------------------------------");
            System.out.println("                                 ");
            System.out.println("           LOGIN FAILED          ");
            System.out.println("      ______________________     ");
            System.out.println("                                 ");
            System.out.println("   Invalid username or password  ");
            System.out.println("         Please try again.       ");
            System.out.println("                                 ");
            System.out.println("---------------------------------");

            return; // stop program
        }

        // =============================
        // EMPLOYEE LOGIN MENU
        // =============================

        if (employee) {

            // employee menu
            System.out.println("\n1 Enter employee number");
            System.out.println("2 Exit");

            int choice = Integer.parseInt(scanner.nextLine());
            
//             scanner.nextLine() reads user input as TEXT (String)
//             but we need a NUMBER for menu selection.
//
//             Integer.parseInt() converts the text into an integer.
//
//             Example:
//             user types: "1"
//             scanner.nextLine() -> "1"
//             Integer.parseInt("1") -> 1

            // exit option
            if (choice == 2) {
                return;
            }

            // ask for employee number
            System.out.print("\nEnter employee number: ");
            int empNumber = Integer.parseInt(scanner.nextLine());

            // show employee information
            employeeInfo(empNumber);
        }

        // =============================
        // PAYROLL STAFF MENU
        // =============================

        if (payrollStaff) {

            System.out.println("\n1 Process Payroll");
            System.out.println("2 Exit");

            int choice = Integer.parseInt(scanner.nextLine());

            if (choice == 2) {
                return;
            }

            // payroll sub-options
            System.out.println("\n1 One Employee");
            System.out.println("2 All Employees");
            System.out.println("3 Exit");

            int empChoice = Integer.parseInt(scanner.nextLine());

            if (empChoice == 3) {
                return;
            }

            // =============================
            // PROCESS ONE EMPLOYEE
            // =============================

            if (empChoice == 1) {

                System.out.print("\nEnter employee number: ");
                int empNumber = Integer.parseInt(scanner.nextLine());

                processEmployee(empNumber);
            }

            // =============================
            // PROCESS ALL EMPLOYEES
            // =============================

            if (empChoice == 2) {

                try {

                    // open employees.csv file
                    
                    File file = new File("employees.csv"); // Make sure this is plain text
                    BufferedReader empInfo = new BufferedReader(new FileReader(file));
                    
                    empInfo.readLine(); // skip header row

                    String line;

                    // read every employee in the file
                    while ((line = empInfo.readLine()) != null) {

                        String[] empdata = line.split(",");

                        int empNumber = Integer.parseInt(empdata[0]);

                        // process payroll for each employee
                        processEmployee(empNumber);
                    }

                    empInfo.close();

                } catch (Exception e) {
                    System.out.println("Error reading employees file.");
                }
            }
        }

        scanner.close();
    }

    // ============================================
    // DISPLAY EMPLOYEE INFORMATION
    // ============================================

    public static void employeeInfo(int empNumber) {

        try {
            
            File file = new File("employees.csv"); // Make sure this is plain text
            BufferedReader empInfo = new BufferedReader(new FileReader(file));
            String line;
                    

            empInfo.readLine(); // skip header

            while ((line = empInfo.readLine()) != null) {

                String[] empdata = line.split(",");

                int num = Integer.parseInt(empdata[0]);

                // if employee number matches
                if (num == empNumber) {

                    System.out.println("-----------------------------------------------");
                    System.out.println("               Employee Details                ");
                    System.out.println("===============================================");
                    System.out.println("| Emp #  | Name                | Birthday     |");
                    System.out.println("-----------------------------------------------");

                    // display employee details
                    System.out.println("| " + num
                            + " | " + empdata[2] + " " + empdata[1]
                            + " | " + empdata[3]);

                    break;
                }
            }

            empInfo.close();

        } catch (Exception e) {
            System.out.println("Error reading file.");
        }
    }

    // ============================================
    // PROCESS PAYROLL COMPUTATION
    // ============================================

    public static void processEmployee(int empNumber) {

        try {

            BufferedReader empInfo = new BufferedReader(new FileReader("employees.csv"));
            String line;

            // employee information variables
            String firstName = "";
            String lastName = "";
            String birthday = "";
            double hourlyRate = 0;

            empInfo.readLine(); // skip header

            boolean found = false;

            // search employee in CSV file
            while ((line = empInfo.readLine()) != null) {

                String[] empdata = line.split(",");

                int num = Integer.parseInt(empdata[0]);

                if (num == empNumber) {

                    found = true;

                    lastName = empdata[1];
                    firstName = empdata[2];
                    birthday = empdata[3];
                    hourlyRate = Double.parseDouble(empdata[5]);

                    break;
                }
            }

            // if employee does not exist
            if (!found) {
                System.out.println("Employee does not exist.");
                return;
            }

            empInfo.close();

            // display employee header
            System.out.println("-----------------------------------------------");
            System.out.println("               Employee Details                ");
            System.out.println("===============================================");
            System.out.println("| Emp #  | Name                | Birthday     |");
            System.out.println("-----------------------------------------------");
            System.out.println("| " + empNumber
                            + " | " + firstName + " " + lastName
                            + " | " + birthday);
            System.out.println("-----------------------------------------------");

            // loop months from June to December
            for (int month = 6; month <= 12; month++) {

                double firstCutoff = 0;   // June 1-15
                double secondCutoff = 0;  // June 16-end

                BufferedReader attendance = new BufferedReader(new FileReader("attendance.csv"));
                attendance.readLine();

                String line2;

                // read attendance records
                while ((line2 = attendance.readLine()) != null) {

                    String[] empAttendance = line2.split(",");

                    int num = Integer.parseInt(empAttendance[0]);
                    int m = Integer.parseInt(empAttendance[1]);
                    int day = Integer.parseInt(empAttendance[2]);

                    // check employee and month
                    if (num == empNumber && m == month) {

                        double hours = computeHours(empAttendance[3], empAttendance[4]);

                        // separate hours by cutoff
                        if (day <= 15) {
                            firstCutoff += hours;
                        } else {
                            secondCutoff += hours;
                        }
                    }
                }

                attendance.close();

                // =============================
                // FIRST CUTOFF SALARY
                // =============================

                if (firstCutoff > 0) {

                    double grossSalary = firstCutoff * hourlyRate;

                    System.out.println("\nCutoff Date: " + getMonth(month) + " 1 to 15");
                    System.out.println("Total Hours Worked: " + firstCutoff);
                    System.out.println("Gross Salary: " + grossSalary);
                    System.out.println("Net Salary: " + grossSalary);
                }

                // =============================
                // SECOND CUTOFF WITH DEDUCTIONS
                // =============================

                if (secondCutoff > 0) {

                    double gross = secondCutoff * hourlyRate;

                    // SSS contribution based on salary bracket
                    double sss = 0;

                    if (gross <= 3250) sss = 135;
                    else if (gross <= 3750) sss = 157.5;
                    else if (gross <= 4250) sss = 180;
                    else if (gross <= 4750) sss = 202.5;
                    else if (gross <= 5250) sss = 224;
                    else if (gross <= 5750) sss = 247.5;
                    else if (gross <= 6250) sss = 270;
                    else if (gross <= 6750) sss = 292.5;
                    else if (gross <= 7250) sss = 315;
                    else if (gross <= 7750) sss = 337.5;
                    else if (gross <= 8250) sss = 360;
                    else if (gross <= 8750) sss = 382.5;
                    else if (gross <= 9250) sss = 405;
                    else if (gross <= 9750) sss = 427.5;
                    else if (gross <= 10250) sss = 450;
                    else if (gross <= 10750) sss = 472.5;
                    else if (gross <= 11250) sss = 495;
                    else if (gross <= 11750) sss = 517.5;
                    else if (gross <= 12250) sss = 540;
                    else if (gross <= 12750) sss = 562.5;
                    else if (gross <= 13250) sss = 585;
                    else if (gross <= 13750) sss = 607.5;
                    else if (gross <= 14250) sss = 630;
                    else if (gross <= 14750) sss = 652.5;
                    else if (gross <= 15250) sss = 675;
                    else if (gross <= 15750) sss = 697.5;
                    else if (gross <= 16250) sss = 720;
                    else if (gross <= 16750) sss = 742.5;
                    else if (gross <= 17250) sss = 765;
                    else if (gross <= 17750) sss = 787.5;
                    else if (gross <= 18250) sss = 810;
                    else if (gross <= 18750) sss = 832.5;
                    else if (gross <= 19250) sss = 855;
                    else if (gross <= 19750) sss = 877.5;
                    else if (gross <= 20250) sss = 900;
                    else if (gross <= 20750) sss = 922.5;
                    else if (gross <= 21250) sss = 945;
                    else if (gross <= 21750) sss = 967.5;
                    else if (gross <= 22250) sss = 990;
                    else if (gross <= 22750) sss = 1012.5;
                    else if (gross <= 23250) sss = 1035;
                    else if (gross <= 23750) sss = 1057.5;
                    else if (gross <= 24250) sss = 1080;
                    else if (gross <= 24750) sss = 1102.5;
                    else sss = 1125;

                    // PhilHealth computation
                    double philHealth = gross;

                    if (philHealth < 10000) philHealth = 10000;
                    if (philHealth > 60000) philHealth = 60000;

                    double monthlyPremiumRate = philHealth * 0.03;
                    double phil = monthlyPremiumRate / 2;

                    // Pag-IBIG contribution
                    double pagibig;

                    if (gross >= 1000 && gross <= 1500)
                        pagibig = gross * 0.01;
                    else
                        pagibig = gross * 0.02;

                    if (pagibig > 100)
                        pagibig = 100;

                    // Taxable income after deductions
                    double taxableIncome = gross - (sss + phil + pagibig);

                    double tax = 0;

                    if (taxableIncome <= 20832)
                        tax = 0;
                    else if (taxableIncome < 33333)
                        tax = (taxableIncome - 20833) * 0.20;
                    else if (taxableIncome < 66667)
                        tax = 2500 + (taxableIncome - 33333) * 0.25;
                    else if (taxableIncome < 166667)
                        tax = 10833 + (taxableIncome - 66667) * 0.30;
                    else if (taxableIncome < 666667)
                        tax = 40833.33 + (taxableIncome - 166667) * 0.32;
                    else
                        tax = 200833.33 + (taxableIncome - 666667) * 0.35;

                    double totalDeduction = sss + phil + pagibig + tax;
                    double net = gross - totalDeduction;

                    // display payroll details
                    System.out.println("\nCutoff Date: " + getMonth(month) + " 16 to end");
                    System.out.println("Total Hours Worked: " + secondCutoff);
                    System.out.println("Gross Salary: " + gross);

                    System.out.println("Each Deduction:");
                    System.out.println("SSS: " + sss);
                    System.out.println("PhilHealth: " + phil);
                    System.out.println("Pag-IBIG: " + pagibig);
                    System.out.println("Tax: " + tax);

                    System.out.println("Total Deductions: " + totalDeduction);
                    System.out.println("Net Salary: " + net);
                }
            }

        } catch (Exception e) {
            System.out.println("Error processing payroll.");
        }
    }

    // ============================================
    // COMPUTE WORK HOURS
    // ============================================

    public static double computeHours(String in, String out) {

        // split time format HH:MM
        String[] logIn = in.split(":");
        String[] logOut = out.split(":");

        int inHour = Integer.parseInt(logIn[0]);
        int inMin = Integer.parseInt(logIn[1]);

        int outHour = Integer.parseInt(logOut[0]);
        int outMin = Integer.parseInt(logOut[1]);

        // convert to decimal hours
        double start = inHour + (inMin / 60.0);
        double end = outHour + (outMin / 60.0);

        // handle overnight shift
        if (end < start) {
            end += 24;
        }

        // return total hours worked
        return end - start;
    }

    // ============================================
    // GET MONTH NAME
    // ============================================

    public static String getMonth(int m) {

        if (m == 6) return "June";
        if (m == 7) return "July";
        if (m == 8) return "August";
        if (m == 9) return "September";
        if (m == 10) return "October";
        if (m == 11) return "November";
        if (m == 12) return "December";

        return "";
    }
}
