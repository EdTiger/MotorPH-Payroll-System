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

            else if (empChoice == 2) {

            try {
            BufferedReader empInfo = new BufferedReader(new FileReader("employees.csv")); // open employees.csv file
            empInfo.readLine(); // skip header   
            String recordLine; // Variable that stores each row of the file
                    
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
            BufferedReader empInfo = new BufferedReader(new FileReader("employees.csv")); 
            empInfo.readLine();
            String recordLine; 

            boolean found = false;      
            
            while ((line = empInfo.readLine()) != null) {

                String[] empData = line.split(","); 
                int number = Integer.parseInt(empData[0]);

                // If employee number matches
                if (number == empNumber) {

                    found = true;

                    System.out.println("-----------------------------------------------");
                    System.out.println("               Employee Details                ");
                    System.out.println("===============================================");
                    System.out.println("| Emp #  | Name                | Birthday     |");
                    System.out.println("-----------------------------------------------");

                    // Display employee details
                    System.out.println("| " + num
                            + " | " + empData[2] + " " + empData[1]
                            + " | " + empData[3]);

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
        BufferedReader empInfo = new BufferedReader(new FileReader("employees.csv"));
        empInfo.readLine();    
        String recordLine; 

            // Employee information variables
            String firstName = ""; // employee first name
            String lastName = "";  // employee last name
            String birthday = "";  // employee birthday
            double hourlyRate = 0; // employee hourly salary
            boolean found = false;

            while ((line = empInfo.readLine()) != null) {
                
                String[] empData = line.split(",");
               
                if (Integer.parseInt(empData[0]) == empNumber){

                    found = true;

                    lastName = empData[1];
                    firstName = empData[2];
                    birthday = empData[3];
                    hourlyRate = Double.parseDouble(empData[5]);

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

            
            // Create a dynamic list to store all attendance records
            // Each record (row) will be stored as a String array
            ArrayList<String[]> records = new ArrayList<>();
            
            BufferedReader attendance = new BufferedReader(new FileReader("attendance.csv"));
            attendance.readLine(); // skip header row

                String recordLine2; // Variable that stores each row of the file

                while ((line2 = attendance.readLine()) != null) { // Loop through the file until there are no more lines (null means end of file)
                    records.add(line2.split(","));
                    
                }
                attendance.close();
                
                 // LOOP MONTHS
                for (int month = 6; month <= 12; month++) {

                double firstCutoff = 0;
                double secondCutoff = 0;         

                // Compute Hours
                for (String[] empAttendance : records) { // record inside the list one by one
                    
                   
                    int number = Integer.parseInt(empAttendance[0]);
                    int m = Integer.parseInt(empAttendance[1]);
                    int day = Integer.parseInt(empAttendance[2]);

                    // Check employee and month
                    if (number == empNumber && m == month) {
                        
                        // Compute total hours worked for the day
                        double hours = computeHours(empAttendance[3], empAttendance[4]);
                        
                        if (day <= 15) {
                            firstCutoff += hours;
                        } else {
                            secondCutoff += hours;
                        }
                    
                    }

                }
                // =============================
                // MONTHLY GROSS 
                // =============================
                double monthlyGross = (firstCutoff + secondCutoff) * hourlyRate;
                
                // =============================
                // FIRST CUTOFF SALARY (NO DEDUCTION)
                // =============================
                if (firstCutoff > 0) {

                    // Compute gross salary
                    double grossSalary1 = firstCutoff * hourlyRate;

                    System.out.println("\nCutoff Date: " + getMonth(month) + " 1 to 15");
                    System.out.println("Total Hours Worked: " + firstCutoff);
                    System.out.println("Gross Salary: " + grossSalary1);
                    System.out.println("Net Salary: " + grossSalary1);
                }

                // =============================
                // SECOND CUTOFF (WITH DEDUCTIONS)
                // =============================
                if (secondCutoff > 0) {
                    
                    double grossSalary2 = secondCutoff * hourlyRate; // Compute gross salary
                    
                    double sss = computeSSS(monthlyGross);
                    double philHealth = computePhilHealth(monthlyGross);
                    double pagibig = computePagibig(monthlyGross);
                    double tax = computeTax(monthlyGross, sss, philHealth, pagibig);
                                          
                    double totalDeduction = sss + philHealth + pagibig + tax; // Total deductions
                    double net = grossSalary2 - totalDeduction;                // Net salary after deductions
                    
                     // Display payroll details
                    System.out.println("\nCutoff Date: " + getMonth(month) + " 16 to end");
                    System.out.println("Total Hours Worked: " + secondCutoff);
                    System.out.println("Gross Salary: " + grossSalary2);

                    System.out.println("Each Deduction:");
                    System.out.println("SSS: " + sss);
                    System.out.println("PhilHealth: " + philHealth);
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
                    // DEDUCTIONS
                    // ============================================

                    // SSS Contribution Computation 
                public static double computeSSS(double gross) {
                    if (gross <= 3250) { // If salary is very low
                        return 135; 
                    }
                    else if (gross <= 24750) {
                        return gross * 0.045; // simplified middle range
                    }          
                    else {
                        return 1125;
                    }
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
                    // Pagibig Contribution Computation 
                public static double computePagibig(double gross) {
                    double pagibig;
                    
                    if (gross >= 1000 && gross <= 1500) // If salary is between 1,000 and 1,500
                        pagibig = gross * 0.01;         // employee contribution 1% of salary
                    else
                        pagibig = gross * 0.02;         // If salary is greater than 1,500
                                                        // employee contributes 2% of salary                     
                    if (pagibig > 100)                  // Pag-IBIG contribution hs a maximum cap of 100
                        pagibig = 100;
                    
                    return pagibig;
                }
                    
                    // Taxable Income Computation (After Deductions)
                public static double computeTax(double gross, double sss, double phil, double pagibig) {

                    double taxableIncome = gross - (sss + phil + pagibig); 

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
                                                   
                    return tax;
                    
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
                    if (timeIn <= 8.10){ 
                        timeIn = 8.0; // If employee arrives before 8:10 → considered as 8:00
                    }              
                    double hours = timeOut - timeIn;
                    
                    if (hours > 5) { 
                        hours = hours - 1; // lunch deduction
                    }
                    if (hours > 8) {
                        hours = 8; // max 8 hours
                    }               
                    return hours;
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


}
