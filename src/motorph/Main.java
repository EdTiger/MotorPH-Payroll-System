package motorph;
/**
 * MotorPH Payroll System
 *
 * @author H1101_Group 2 ByteBeans
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
import java.util.*;    // allows using Scanner

public class Main {

    /**
     * Main entry point for the MotorPH Payroll System.
     * Handles login authentication and routes to the appropriate menu.
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
        boolean payrollStaff = username.equals("payroll_staff") && password.equals("12345");

        // check if user is employee
        boolean employee = username.equals("employee") && password.equals("12345");

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

            int employeeChoice = Integer.parseInt(scanner.nextLine());

            if (employeeChoice == 3) {
                return;
            }

            // =============================
            // PROCESS ONE EMPLOYEE
            // =============================
            if (employeeChoice == 1) {

                // Ask for employee number
                System.out.print("\nEnter employee number: ");
                int employeeNumber = Integer.parseInt(scanner.nextLine());

                // Compute payroll for one employee
                processEmployee(employeeNumber);
            }

            // =============================
            // PROCESS ALL EMPLOYEES
            // =============================

            else if (employeeChoice == 2) {

                try {
                    BufferedReader employeeInfo = new BufferedReader(new FileReader("employees.csv")); // open employees.csv file
                    employeeInfo.readLine(); // skip header
                    String recordLine; // Variable that stores each row of the file

                    // Read every employee in the file
                    while ((recordLine = employeeInfo.readLine()) != null) {

                        String[] empdata = recordLine.split(","); // Split the row using comma
                        int empNumber = Integer.parseInt(empdata[0]); // Convert employee number from text to integer

                        // Process payroll for each employee
                        processEmployee(empNumber);
                    }

                    employeeInfo.close();

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
            BufferedReader empInfo = new BufferedReader(new FileReader("employees.csv"));
            empInfo.readLine();
            String recordLine;

            boolean found = false;

            while ((recordLine = empInfo.readLine()) != null) {

                String[] employeeData = recordLine.split(",");
                int number = Integer.parseInt(employeeData[0]);

                // If employee number matches
                if (number == empNumber) {

                    found = true;

                    System.out.println("-----------------------------------------------");
                    System.out.println("               Employee Details                ");
                    System.out.println("===============================================");
                    System.out.println("| Emp #  | Name                | Birthday     |");
                    System.out.println("-----------------------------------------------");

                    // Display employee details
                    System.out.println("| " + number
                            + " | " + employeeData[2] + " " + employeeData[1]
                            + " | " + employeeData[3]);

                    break;
                }
            }
            if(!found){ // If employee not found
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
            BufferedReader employeeInfo = new BufferedReader(new FileReader("employees.csv"));
            employeeInfo.readLine();
            String recordLine;

            // Employee information variables
            String firstName = ""; // employee first name
            String lastName = "";  // employee last name
            String birthday = "";  // employee birthday
            double hourlyRate = 0; // employee hourly salary
            boolean found = false;

            while ((recordLine = employeeInfo.readLine()) != null) {

                String[] employeeData = recordLine.split(",");

                if (Integer.parseInt(employeeData[0]) == empNumber){

                    found = true;

                    lastName = employeeData[1];
                    firstName = employeeData[2];
                    birthday = employeeData[3];
                    hourlyRate = Double.parseDouble(employeeData[5]);

                    break;
                }
            }
            // If employee does not exist
            if (!found) {
                System.out.println("Employee does not exist.");
                return;
            }

            employeeInfo.close();

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


            // Create a dynamic list to store all attendance records
            // Each record (row) will be stored as a String array
            ArrayList<String[]> records = new ArrayList<>();

            BufferedReader attendance = new BufferedReader(new FileReader("attendance.csv"));
            attendance.readLine(); // skip header row

            String recordLine2; // Variable that stores each row of the file

            while ((recordLine2 = attendance.readLine()) != null) { // Loop through the file until there are no more lines (null means end of file)
                records.add(recordLine2.split(","));

            }
            attendance.close();

            // =============================
            // LOOP MONTHS (June to December)
            // =============================
            for (int month = 6; month <= 12; month++) {

                double firstCutoff = 0;  // total hours for 1st cutoff (days 1-15)
                double secondCutoff = 0; // total hours for 2nd cutoff (days 16-end)

                // =============================
                // COMPUTE HOURS PER CUTOFF
                // =============================
                for (String[] employeeAttendance : records) { // loop through each attendance record

                    int num = Integer.parseInt(employeeAttendance[0]); // employee number
                    int m = Integer.parseInt(employeeAttendance[1]);   // month of the record
                    int day = Integer.parseInt(employeeAttendance[2]); // day of the record

                    // Check if attendance record matches this employee and month
                    if (num == empNumber && m == month) {

                        // Compute total hours worked for the day
                        double hours = computeHours(employeeAttendance[3], employeeAttendance[4]);

                        if (day <= 15) {
                            firstCutoff += hours;  // add to 1st cutoff hours
                        } else {
                            secondCutoff += hours; // add to 2nd cutoff hours
                        }

                    }

                }

                // =============================
                // MONTHLY GROSS 
                // =============================
                double monthlyGross = (firstCutoff + secondCutoff) * hourlyRate;

                // Skip month if no hours recorded
                if (monthlyGross <= 0) continue;

                // =============================
                // COMPUTE DEDUCTIONS
                // =============================
                // Deductions are based on the combined monthly gross (1st + 2nd cutoff)
                double sss = computeSSS(monthlyGross);             // SSS contribution
                double philHealth = computePhilHealth(monthlyGross); // PhilHealth contribution
                double pagibig = computePagibig(monthlyGross);     // Pag-IBIG contribution

                // Taxable income = monthly gross minus all contributions
                double taxableIncome = monthlyGross - (sss + philHealth + pagibig);
                double tax = computeTax(taxableIncome);            // Withholding tax

                double totalDeduction = sss + philHealth + pagibig + tax; // Total deductions

                // =============================
                // FIRST CUTOFF SALARY (NO DEDUCTION)
                // =============================
                if (firstCutoff > 0) {

                    // Compute gross salary for 1st cutoff
                    double grossSalary1 = firstCutoff * hourlyRate;

                    // Display 1st cutoff payroll
                    System.out.println("\nCutoff Date: " + getMonth(month) + " 1 to 15 2024");
                    System.out.println("Total Hours: " + firstCutoff + " hours");
                    System.out.println("Gross Salary: PHP " + grossSalary1);
                    System.out.println("Net Salary: PHP " + grossSalary1);
                }

                // =============================
                // SECOND CUTOFF (WITH DEDUCTIONS)
                // =============================
                if (secondCutoff > 0) {

                    // Compute gross salary for 2nd cutoff
                    double grossSalary2 = secondCutoff * hourlyRate;

                    // Net salary after deductions
                    double net = grossSalary2 - totalDeduction;

                    // Display 2nd cutoff payroll
                    System.out.println("\nCutoff Date: " + getMonth(month) + " 16 to " + getLastDay(month) + " 2024");
                    System.out.println("Total Hours: " + secondCutoff + " hours");
                    System.out.println("Gross Salary: PHP " + grossSalary2);

                    System.out.println("Each Deduction:");
                    System.out.println("    SSS: PHP " + sss);
                    System.out.println("    PhilHealth: PHP " + philHealth);
                    System.out.println("    Pag-IBIG: PHP " + pagibig);
                    System.out.println("    Tax: PHP " + tax);

                    System.out.println("Total Deductions: PHP " + totalDeduction);
                    System.out.println("Net Salary: PHP " + net);
                }
            }

        } catch (Exception e) {
            System.out.println("Error processing payroll.");
        }
    }

    // ============================================
    // HOURS COMPUTATION
    // ============================================

    public static double computeHours(String in, String out) {

        // Convert time-in (HH:MM) into decimal hours
        String[] inTime = in.split(":"); // Split time-in
        double timeIn = Integer.parseInt(inTime[0])
                + Integer.parseInt(inTime[1]) / 60.0;

        // Convert time-out (HH:MM) into decimal hours
        String[] outTime = out.split(":"); // Split time-out
        double timeOut = Integer.parseInt(outTime[0])
                + Integer.parseInt(outTime[1]) / 60.0;

        // Grace period (10 mins)
        // If employee arrives at or before 8:10 AM, count as 8:00 AM
        if (timeIn <= 8.0 + 10.0 / 60.0){
            timeIn = 8.0;
        }

        // Cap time-out at 5:00 PM (no overtime credit)
        if (timeOut > 17.0) {
            timeOut = 17.0;
        }

        // Total hours = time-out minus time-in
        double hours = timeOut - timeIn;

        // Lunch deduction (12:00 PM - 1:00 PM)
        // Only deduct the portion of lunch that overlaps with actual work time
        double lunchOverlap = Math.max(0, Math.min(timeOut, 13.0) - Math.max(timeIn, 12.0));
        hours -= lunchOverlap;

        // Hours cannot be negative
        if (hours < 0) {
            hours = 0;
        }

        // Maximum 8 working hours per day
        if (hours > 8) {
            hours = 8;
        }
        return hours;
    }

    // ============================================
    // DEDUCTIONS
    // ============================================

    // SSS Contribution Computation
    public static double computeSSS(double gross) {

        // SSS 2024 contribution table
        // Each row: {salary ceiling, employee contribution}
        double[][] sssTable = {
                {3250, 135.00},   {3750, 157.50},   {4250, 180.00},
                {4750, 202.50},   {5250, 225.00},   {5750, 247.50},
                {6250, 270.00},   {6750, 292.50},   {7250, 315.00},
                {7750, 337.50},   {8250, 360.00},   {8750, 382.50},
                {9250, 405.00},   {9750, 427.50},   {10250, 450.00},
                {10750, 472.50},  {11250, 495.00},  {11750, 517.50},
                {12250, 540.00},  {12750, 562.50},  {13250, 585.00},
                {13750, 607.50},  {14250, 630.00},  {14750, 652.50},
                {15250, 675.00},  {15750, 697.50},  {16250, 720.00},
                {16750, 742.50},  {17250, 765.00},  {17750, 787.50},
                {18250, 810.00},  {18750, 832.50},  {19250, 855.00},
                {19750, 877.50},  {20250, 900.00},  {20750, 922.50},
                {21250, 945.00},  {21750, 967.50},  {22250, 990.00},
                {22750, 1012.50}, {23250, 1035.00}, {23750, 1057.50},
                {24250, 1080.00}, {24750, 1102.50}
        };

        // Find the matching bracket
        for (double[] bracket : sssTable) {
            if (gross <= bracket[0]) {
                return bracket[1]; // return the contribution for this bracket
            }
        }

        return 1125.00; // maximum contribution if salary exceeds all brackets
    }

    // PhilHealth Contribution Computation
    public static double computePhilHealth(double gross) {
        double premium;

        if (gross <= 10000) {
            premium = 300;        // minimum premium
        }
        else if (gross >= 60000) {
            premium = 1800;       // maximum premium
        }
        else {
            premium = gross * 0.03;   // 3% of salary
        }
        return premium / 2; // employee share is 50%
    }

    // Pag-IBIG Contribution Computation
    public static double computePagibig(double gross) {
        double pagibig;

        if (gross >= 1000 && gross <= 1500) // If salary is between 1,000 and 1,500
            pagibig = gross * 0.01;         // employee contribution 1% of salary
        else
            pagibig = gross * 0.02;         // If salary is greater than 1,500
        // employee contributes 2% of salary
        if (pagibig > 100)                  // Pag-IBIG contribution has a maximum cap of 100
            pagibig = 100;

        return pagibig;
    }

    // Withholding Tax Computation
    public static double computeTax(double taxableIncome) {

        double tax = 0;

        if (taxableIncome <= 20832)                             // 20,832 and below = no tax
            tax = 0;
        else if (taxableIncome < 33333)                         // 20,833 - 33,333 = 20% of excess over 20,833
            tax = (taxableIncome - 20833) * 0.20;
        else if (taxableIncome < 66667)                         // 33,333 - 66,667 = 2500 + 25% excess over 33,333
            tax = 2500 + (taxableIncome - 33333) * 0.25;
        else if (taxableIncome < 166667)                        // 66,667 - 166,667 = 10,833 + 30% excess over 66,667
            tax = 10833 + (taxableIncome - 66667) * 0.30;
        else if (taxableIncome < 666667)                        // 166,667 - 666,667 = 40,833.33 + 32% excess
            tax = 40833.33 + (taxableIncome - 166667) * 0.32;
        else
            tax = 200833.33 + (taxableIncome - 666667) * 0.35;  // 666,667 and above = 200,833.33 + 35% excess

        return tax;

    }

    // ============================================
    // GET MONTH NAME
    // ============================================

    // Convert month number to month name
    public static String getMonth(int m) {
        if (m == 1) return "January";
        if (m == 2) return "February";
        if (m == 3) return "March";
        if (m == 4) return "April";
        if (m == 5) return "May";
        if (m == 6) return "June";
        if (m == 7) return "July";
        if (m == 8) return "August";
        if (m == 9) return "September";
        if (m == 10) return "October";
        if (m == 11) return "November";
        if (m == 12) return "December";
        return "";
    }

    // ============================================
    // GET LAST DAY OF MONTH
    // ============================================

    // Returns the last day of a given month
    public static int getLastDay(int month) {
        if (month == 1) return 31;
        if (month == 2) return 28;
        if (month == 3) return 31;
        if (month == 4) return 30;
        if (month == 5) return 31;
        if (month == 6) return 30;
        if (month == 7) return 31;
        if (month == 8) return 31;
        if (month == 9) return 30;
        if (month == 10) return 31;
        if (month == 11) return 30;
        if (month == 12) return 31;
        return 30;
    }

}
