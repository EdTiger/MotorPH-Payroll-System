package motorph;
/**
 * MotorPH Payroll System
 *
 * This program reads employee and attendance records from CSV files
 * and computes payroll automatically.
 *
 * Features:
 * - Login system (Employee or Payroll Staff)
 * - Employee information lookup
 * - Payroll processing
 * - Automatic computation of:
 *      - Gross Salary
 *      - SSS Contribution
 *      - PhilHealth Contribution
 *      - Pag-IBIG Contribution
 *      - Income Tax
 * - Payroll summary per cutoff
 *
 * Developed for MotorPH Phase 1 Payroll System
 */
import java.io.*;      // allows reading files like CSV
import java.util.*;    // allows using Scanner for input


public class Main {

    /**
     * MotorPH Basic Payroll System
     */
    public static void main(String[] args) {

        // Scanner for user input
        Scanner scanner = new Scanner(System.in);

        // Display system title
        System.out.println("=================================");
        System.out.println("                                 ");
        System.out.println("             MotorPH             ");
        System.out.println("         Payroll System          ");
        System.out.println("                                 ");
        System.out.println("=================================");

        // =============================
        // LOGIN SECTION
        // =============================

        //Ask user for login credentials
        System.out.print("Username: ");
        String username = scanner.nextLine(); // read username

        System.out.print("Password: ");
        String password = scanner.nextLine(); // read password

        // check if user is payroll staff
        boolean payrollStaff =
                username.equals("payroll_staff") && password.equals("12345");

        // check if user is employee
        boolean employee =
                username.equals("employee") && password.equals("12345");

        // if login credentials are incorrect
        if (!payrollStaff && !employee) {

            System.out.println("------------------------------------");
            System.out.println("                                    ");
            System.out.println("            LOGIN FAILED            ");
            System.out.println("      ________________________      ");
            System.out.println("                                    ");
            System.out.println( "Incorrect username and/or password" );
            System.out.println("          Please try again.         ");
            System.out.println("                                    ");
            System.out.println("------------------------------------");

            return; // stop program
        }

        // =============================
        // EMPLOYEE LOGIN MENU
        // =============================

        if (employee) {

            // Show menu options
            System.out.println("\n1 Enter employee number");
            System.out.println("2 Exit");

            // Read user choice
            int choice = Integer.parseInt(scanner.nextLine()); // Integer.parseInt() converts the text into an integer

            // Exit option
            if (choice == 2) {
                return;
            }

            // Ask for employee number
            System.out.print("\nEnter employee number: ");
            int empNumber = Integer.parseInt(scanner.nextLine());

            // Display employee information
            employeeInfo(empNumber);
        }

        // =============================
        // PAYROLL STAFF MENU
        // =============================

        if (payrollStaff) {

            // Show payroll menu
            System.out.println("\n1 Process Payroll");
            System.out.println("2 Exit");

            int choice = Integer.parseInt(scanner.nextLine());

            if (choice == 2) {
                return;
            }

            // Payroll processing options
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

                // Ask for employee number
                System.out.print("\nEnter employee number: ");
                int empNumber = Integer.parseInt(scanner.nextLine());

                // Compute payroll for one employee
                processEmployee(empNumber);
            }

            // =============================
            // PROCESS ALL EMPLOYEES
            // =============================

            if (empChoice == 2) {

            try {
            // open employees.csv file
            File empDetails = new File("employees.csv"); 
            BufferedReader empInfo = new BufferedReader(new FileReader(empDetails));
                    
            String line; // Varible that stores each row of the file
                    
            empInfo.readLine(); // Skip the header row

                    // Read every employee in the file
                    while ((line = empInfo.readLine()) != null) {

                        String[] empdata = line.split(","); // Split the row using comma

                        int empNumber = Integer.parseInt(empdata[0]); // Convert employee number from text to integer

                        // Process payroll for each employee
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
            // Open employees.csv file
            File empDetails = new File("employees.csv"); 
            BufferedReader empInfo = new BufferedReader(new FileReader(empDetails));
            
            String line; // Varible that stores each row of the file
                    
            empInfo.readLine(); // Skip the header row

            boolean found = false;
        
            // Search employee in file 
            while ((line = empInfo.readLine()) != null) {

                String[] empdata = line.split(","); // Split row

                int num = Integer.parseInt(empdata[0]);

                // If employee number matches
                if (num == empNumber) {

                    found = true;

                    System.out.println("-----------------------------------------------");
                    System.out.println("               Employee Details                ");
                    System.out.println("===============================================");
                    System.out.println("| Emp #  | Name                | Birthday     |");
                    System.out.println("-----------------------------------------------");

                    // Display employee details
                    System.out.println("| " + num
                            + " | " + empdata[2] + " " + empdata[1]
                            + " | " + empdata[3]);

                    break;
                }
         }
            // If employee not found
            if(!found){
                    System.out.println("Employee number does not exist");
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
       // Open employees.csv file
       File empDetails = new File("employees.csv"); 
       BufferedReader empInfo = new BufferedReader(new FileReader(empDetails));
            
       String line; // Varible that stores each row of the file

            // Employee information variables
            String firstName = ""; // employee first name
            String lastName = "";  // employee last name
            String birthday = "";  // employee birthday
            double hourlyRate = 0; // employee hourly salary

            empInfo.readLine(); // Skip header

            boolean found = false;

            // Search employee in CSV file
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

            // If employee does not exist
            if (!found) {
                System.out.println("Employee does not exist.");
                return;
            }

            empInfo.close();

            // Display employee header
            System.out.println("-----------------------------------------------");
            System.out.println("               Employee Details                ");
            System.out.println("===============================================");
            System.out.println("| Emp #  | Name                | Birthday     |");
            System.out.println("-----------------------------------------------");
            System.out.println("| " + empNumber
                            + " | " + firstName + " " + lastName
                            + " | " + birthday);
            System.out.println("-----------------------------------------------");

            // Loop months from June to December
            for (int month = 6; month <= 12; month++) {

                double firstCutoff = 0;   // June 1-15
                double secondCutoff = 0;  // June 16-end
                
            // Open attendance.cvs file
            File attRecord = new File("attendance.csv"); 
            BufferedReader attendance = new BufferedReader(new FileReader(attRecord));
            attendance.readLine(); // skip header row

                String line2; // Varible that stores each row of the file

                // Read attendance records
                while ((line2 = attendance.readLine()) != null) {

                    String[] empAttendance = line2.split(",");

                    int num = Integer.parseInt(empAttendance[0]);
                    int m = Integer.parseInt(empAttendance[1]);
                    int day = Integer.parseInt(empAttendance[2]);

                    // Check employee and month
                    if (num == empNumber && m == month) {
                        
                        // Compute total hours worked for the day
                        double hours = computeHours(empAttendance[3], empAttendance[4]);

                        // Separate hours by cutoff
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

                    // Compute gross salary
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
                    
                    // Compute gross salary
                    double gross = secondCutoff * hourlyRate;

                    //_____________________________
                    // SSS Contribution Computation 
                    double sss = 0;

                    // If the employee's gross salary falls within a range,
                    // asssign the corressponding SSS contribution.
                    if (gross <= 3250) sss = 135;
                    else if (gross <= 3750) sss = 157.5;
                    else if (gross <= 4250) sss = 180;
                    else if (gross <= 4750) sss = 202.5;
                    else if (gross <= 5250) sss = 225;
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
                    else if (gross >= 24750) sss = 1125;
                   
                    //____________________________________
                    // PhilHealth Contribution Computation 
                    double monthlyPremium;

                    if (gross <= 10000) {
                    monthlyPremium = 300;        // minimum premium
                    }
                    else if (gross >= 60000) {
                    monthlyPremium = 1800;       // maximum premium
                    }
                    else {
                    monthlyPremium = gross * 0.03;   // 3% of salary
                    }
                    double phil = monthlyPremium / 2; // employee share is 50%

                    //_________________________________
                    // Pagibig Contribution Computation 
                    double pagibig; 

                    if (gross >= 1000 && gross <= 1500) // If salary is between 1,000 and 1,500
                        pagibig = gross * 0.01;         // employee contribution 1% of salary
                    else
                        pagibig = gross * 0.02;         // If salary is greater than 1,500
                                                        // employee contributes 2% of salary
                     
                    if (pagibig > 100)                  // Pag-IBIG contribution hs a maximum cap of 100
                        pagibig = 100;

                    
                    // Taxable Income Computation (After Deductions)
                    double taxableIncome = gross - (sss + phil + pagibig); 

                    //_________________________________
                    // Tax Contribution Computation 
                    double tax = 0;

                    if (taxableIncome <= 20832)                             // 20,832 and below = no tax
                        tax = 0;                                           
                    else if (taxableIncome < 33333)                         // 20,833 - 33,333 = 20% of excess over 20,833
                        tax = (taxableIncome - 20833) * 0.20;
                    else if (taxableIncome < 66667)                         // 33,333 - 66,667 = 2500 + 25& excess over 33,333
                        tax = 2500 + (taxableIncome - 33333) * 0.25; 
                    else if (taxableIncome < 166667)                        // 66,667 - 166,667 = 10,833 + 30% excess over 66,667
                        tax = 10833 + (taxableIncome - 66667) * 0.30;
                    else if (taxableIncome < 666667)                        // 166,667 - 666,667 = 40,833.33 + 32% excess
                        tax = 40833.33 + (taxableIncome - 166667) * 0.32;
                    else
                        tax = 200833.33 + (taxableIncome - 666667) * 0.35;  // 666,667 and above = 200,833.33 + 35% excess

                    double totalDeduction = sss + phil + pagibig + tax; // Total deductions
                    double net = gross - totalDeduction;                // Net salary after deductions

                    // Display payroll details
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

                    // Convert time-in (HH:MM) to decimal hours
                    String[] inTime = in.split(":"); // Split time-in
                    int inHour = Integer.parseInt(inTime[0]);
                    int inMin = Integer.parseInt(inTime[1]);

                    // Convert time-in into decimal hours
                    double timeIn = inHour + (inMin / 60.0); // 08:30 -> 8.5

                    // Convert time-out (HH:MM) to decimal hours
                    String[] outTime = out.split(":"); // Split time-out
                    int outHour = Integer.parseInt(outTime[0]);
                    int outMin = Integer.parseInt(outTime[1]);

                    // Convert time-out into decimal hours
                    double timeOut = outHour + (outMin / 60.0); // 17:00 -> 17.0

                    // Compute total hours worked
                    double hoursWorked = timeOut - timeIn; // Work hours = Ending time − Starting time

                    return hoursWorked;
    }

    // ============================================
    // GET MONTH NAME
    // ============================================

    public static String getMonth(int m) {

        // Convert month number to month name
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
