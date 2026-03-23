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
import java.io.*;                  // This library allows the program to read files such as CSV documents.
import java.util.*;                // This library allows the program to use the Scanner class for user input.
import java.time.Month;            // This library provides month name conversion using Java's built-in time API.
import java.time.YearMonth;        // This library provides month length calculation (last day of month).
import java.time.format.TextStyle; // This library provides text formatting styles for month display names.
import java.util.Locale;           // This library provides locale settings for language-specific text formatting.

public class Main {

    /**
     * Main entry point for the MotorPH Payroll System.
     * This handles login authentication and routes to the appropriate menu.
     */

    public static void main(String[] args) {

        // The program initializes a Scanner object to capture user input.
        Scanner scanner = new Scanner(System.in);

        // This block displays the system title on the console.
        System.out.println("=================================");
        System.out.println("                                 ");
        System.out.println("             MotorPH             ");
        System.out.println("         Payroll System          ");
        System.out.println("                                 ");
        System.out.println("=================================");

        // =============================
        // LOGIN SECTION
        // =============================

        // The system prompts the user to provide their login credentials.
        System.out.print("Username: ");
        String username = scanner.nextLine(); // The scanner reads the username provided by the user.

        System.out.print("Password: ");
        String password = scanner.nextLine(); // The scanner reads the password provided by the user.

        // This boolean checks if the user is authenticated as a payroll staff member.
        boolean payrollStaff = username.equals("payroll_staff") && password.equals("12345");

        // This boolean checks if the user is authenticated as a standard employee.
        boolean employee = username.equals("employee") && password.equals("12345");

        // If the login credentials do not match any records, the system displays an error message.
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

            return; // This command terminates the execution of the program.
        }
        // =============================
        // EMPLOYEE LOGIN MENU
        // =============================

        // The program loads all employee records into an ArrayList at the start of execution.
        ArrayList<String[]> employeeRecords = new ArrayList<>();
        try {
            BufferedReader employeeReader = new BufferedReader(new FileReader("employees.csv"));
            employeeReader.readLine(); // This line skips the header row of the CSV file.
            String recordLine;
            while ((recordLine = employeeReader.readLine()) != null) {
                employeeRecords.add(recordLine.split(","));
            }
            employeeReader.close();
        } catch (Exception e) {
            System.out.println("Error reading employees file.");
            return;
        }

        // The program loads all attendance records into an ArrayList at the start of execution.
        ArrayList<String[]> attendanceRecords = new ArrayList<>();
        try {
            BufferedReader attendanceReader = new BufferedReader(new FileReader("attendance.csv"));
            attendanceReader.readLine(); // skip header
            String recordLine;
            while ((recordLine = attendanceReader.readLine()) != null) {
                attendanceRecords.add(recordLine.split(","));
            }
            attendanceReader.close();
        } catch (Exception e) {
            System.out.println("Error reading attendance file.");
            return;
        }

        // =============================
        // EMPLOYEE LOGIN MENU
        // =============================
        if (employee) {

            boolean running = true;

            // The menu loop allows the employee to perform multiple actions without restarting the program.
            while (running) {

                // The system displays the available menu options for the employee.
                System.out.println("\n1 Enter employee number");
                System.out.println("2 Exit");

                // The system captures and validates the user's menu selection.
                int choice = getValidInt(scanner, "Enter choice: ");

                switch (choice) {
                    case 1:
                        // The system asks the user to input a specific employee number.
                        int employeeNumber = getValidInt(scanner, "\nEnter employee number: ");

                        // This method call executes the display of the employee information.
                        employeeInfo(employeeNumber, employeeRecords);
                        break;

                    case 2:
                        // This condition processes the user's request to exit the program.
                        running = false;
                        break;

                    default:
                        System.out.println("Invalid choice. Please select 1 or 2.");
                        break;
                }
            }
        }

        // =============================
        // PAYROLL STAFF MENU
        // =============================

        if (payrollStaff) {

            boolean running = true;

            // The menu loop allows payroll staff to perform multiple actions without restarting the program.
            while (running) {

                // The system displays the payroll processing menu.
                System.out.println("\n1 Process Payroll");
                System.out.println("2 Exit");

                // The system captures and validates the user's menu selection.
                int choice = getValidInt(scanner, "Enter choice: ");

                switch (choice) {
                    case 1:
                        // The system presents the options for payroll processing.
                        System.out.println("\n1 One Employee");
                        System.out.println("2 All Employees");
                        System.out.println("3 Back");

                        int employeeChoice = getValidInt(scanner, "Enter choice: ");

                        // =============================
                        // PROCESS ONE EMPLOYEE
                        // =============================
                        if (employeeChoice == 1) {

                            // The system requests the specific employee number from the user.
                            int employeeNumber = getValidInt(scanner, "\nEnter employee number: ");

                            // This method call triggers the payroll computation for a single employee.
                            processEmployee(employeeNumber, employeeRecords, attendanceRecords);
                        }

                        // =============================
                        // PROCESS ALL EMPLOYEES
                        // =============================
                        else if (employeeChoice == 2) {

                            // The system performs batch payroll processing by computing the salary for each employee in the records.
                            for (String[] empData : employeeRecords) {
                                int empNumber = Integer.parseInt(empData[0]);
                                processEmployee(empNumber, employeeRecords, attendanceRecords);
                            }
                        }

                        else if (employeeChoice != 3) {
                            System.out.println("Invalid choice. Please select 1, 2, or 3.");
                        }
                        break;

                    case 2:
                        // This condition processes the user's request to exit the program.
                        running = false;
                        break;

                    default:
                        System.out.println("Invalid choice. Please select 1 or 2.");
                        break;
                }
            }
        }

        scanner.close();
    }

    // ============================================
    // DISPLAY EMPLOYEE INFORMATION
    // ============================================

    // This method searches the employee records and displays the details for the specified employee number.
    public static void employeeInfo(int empNumber, ArrayList<String[]> employeeRecords) {

        boolean found = false;

        for (String[] employeeData : employeeRecords) {

            int number = Integer.parseInt(employeeData[0]);

            // This condition checks if the current record matches the requested employee number.
            if (number == empNumber) {

                found = true;

                System.out.println("-----------------------------------------------");
                System.out.println("               Employee Details                ");
                System.out.println("===============================================");
                System.out.println("| Emp #  | Name                | Birthday     |");
                System.out.println("-----------------------------------------------");

                // This block prints the formatted employee details to the console.
                System.out.println("| " + number
                        + " | " + employeeData[2] + " " + employeeData[1]
                        + " | " + employeeData[3]);

                break;
            }
        }

        if (!found) { // If the employee is not found, the system informs the user.
            System.out.println("Employee number does not exist");
        }
    }

    // ============================================
    // PROCESS PAYROLL COMPUTATION
    // ============================================

    public static void processEmployee(int employeeNumber, ArrayList<String[]> employeeRecords, ArrayList<String[]> attendanceRecords) {
        try {

            // These variables store the retrieved employee information.
            String firstName = ""; // employee first name
            String lastName = "";  // employee last name
            String birthday = "";  // employee birthday
            double hourlyRate = 0; // employee hourly salary
            boolean found = false;

            // The loop searches through the pre-loaded employee list for a match.
            for (String[] employeeData : employeeRecords) {

                if (Integer.parseInt(employeeData[0]) == employeeNumber) {

                    found = true;

                    lastName = employeeData[1];
                    firstName = employeeData[2];
                    birthday = employeeData[3];
                    hourlyRate = Double.parseDouble(employeeData[5]);

                    break;
                }
            }

            // If the employee record does not exist, the method exits early.
            if (!found) {
                System.out.println("Employee does not exist.");
                return;
            }

            // This block displays the header for the employee details.
            System.out.println("-----------------------------------------------");
            System.out.println("               Employee Details                ");
            System.out.println("===============================================");
            System.out.println("| Emp #  | Name                | Birthday     |");
            System.out.println("-----------------------------------------------");
            System.out.println("| " + employeeNumber
                    + " | " + firstName + " " + lastName
                    + " | " + birthday);
            System.out.println("-----------------------------------------------");

            // =============================
            // LOOP MONTHS (June to December)
            // =============================
            for (int month = 6; month <= 12; month++) {

                double firstCutoff = 0;  // This variable tracks the total hours worked during the first cutoff period.
                double secondCutoff = 0; // This variable tracks the total hours worked during the second cutoff period.

                // =============================
                // COMPUTE HOURS PER CUTOFF
                // =============================
                for (String[] employeeAttendance : attendanceRecords) { // The program loops through the pre-loaded attendance list.

                    int num = Integer.parseInt(employeeAttendance[0]); // This parses the employee number from the record.
                    int m = Integer.parseInt(employeeAttendance[1]);   // This parses the month of the attendance record.
                    int day = Integer.parseInt(employeeAttendance[2]); // This parses the day of the attendance record.

                    // This condition verifies if the attendance record matches the selected employee and month.
                    if (num == employeeNumber && m == month) {

                        // This method call computes the total hours worked for the specific day.
                        double hours = computeHours(employeeAttendance[3], employeeAttendance[4]);

                        if (day <= 15) {
                            firstCutoff += hours;  // This adds the computed hours to the first cutoff total.
                        } else {
                            secondCutoff += hours; // This adds the computed hours to the second cutoff total.
                        }

                    }

                }

                // =============================
                // MONTHLY GROSS
                // =============================
                double monthlyGross = (firstCutoff + secondCutoff) * hourlyRate;

                // The program skips the month calculation if no hours were recorded.
                if (monthlyGross <= 0) continue;

                // =============================
                // COMPUTE DEDUCTIONS
                // =============================
                // The system calculates deductions based on the combined monthly gross salary.
                double sss = computeSSS(monthlyGross);               // This method computes the mandatory SSS contribution.
                double philHealth = computePhilHealth(monthlyGross); // This method computes the mandatory PhilHealth contribution.
                double pagibig = computePagibig(monthlyGross);       // This method computes the mandatory Pag-IBIG contribution.

                // The taxable income is calculated by subtracting all contributions from the monthly gross salary.
                double taxableIncome = monthlyGross - (sss + philHealth + pagibig);
                double tax = computeTax(taxableIncome);             // This method computes the withholding tax based on the taxable income.

                double totalDeduction = sss + philHealth + pagibig + tax; // This calculates the total amount of all deductions.

                // =============================
                // FIRST CUTOFF SALARY (NO DEDUCTION)
                // =============================
                if (firstCutoff > 0) {

                    // This calculates the gross salary for the first cutoff period.
                    double grossSalary1 = firstCutoff * hourlyRate;

                    // This section displays the payroll breakdown for the first cutoff.
                    System.out.println("\nCutoff Date: " + Month.of(month).getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " 1 to 15 2024");
                    System.out.println("Total Hours: " + firstCutoff + " Hours");
                    System.out.println("Gross Salary: PHP " + grossSalary1);
                    System.out.println("Net Salary: PHP " + grossSalary1);
                }

                // =============================
                // SECOND CUTOFF (WITH DEDUCTIONS)
                // =============================
                if (secondCutoff > 0) {

                    // This calculates the gross salary for the second cutoff period.
                    double grossSalary2 = secondCutoff * hourlyRate;

                    // The net salary is determined by subtracting total deductions from the gross salary.
                    double net = grossSalary2 - totalDeduction;

                    // This section displays the payroll breakdown for the second cutoff.
                    System.out.println("\nCutoff Date: " + Month.of(month).getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " 16 to " + YearMonth.of(2024, month).lengthOfMonth() + " 2024");
                    System.out.println("Total Hours: " + secondCutoff + " Hours");
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

        // This block converts the time-in string format into a decimal hours value.
        String[] inTime = in.split(":"); // The string is split into hours and minutes.
        double timeIn = Integer.parseInt(inTime[0])
                + Integer.parseInt(inTime[1]) / 60.0;

        // This block converts the time-out string format into a decimal hours value.
        String[] outTime = out.split(":"); // The string is split into hours and minutes.
        double timeOut = Integer.parseInt(outTime[0])
                + Integer.parseInt(outTime[1]) / 60.0;

        // The company provides a ten-minute grace period for late arrivals.
        // If the employee arrives at or before ten minutes past eight, their start time is counted as exactly eight.
        if (timeIn <= 8.0 + 10.0 / 60.0){
            timeIn = 8.0;
        }

        // The system caps the time-out at 5:00PM to prevent unauthorized overtime.
        if (timeOut > 17.0) {
            timeOut = 17.0;
        }

        // The total working hours are calculated by subtracting the time-in from the time-out.
        double hours = timeOut - timeIn;

        // A mandatory one-hour lunch break is deducted from the working hours. (12:00 PM - 1:00 PM)
        // The system only deducts the portion of the lunch break that overlaps with the employee's actual working hours.
        double lunchOverlap = Math.max(0, Math.min(timeOut, 13.0) - Math.max(timeIn, 12.0));
        hours -= lunchOverlap;

        // This condition ensures that the calculated hours do not result in a negative value.
        if (hours < 0) {
            hours = 0;
        }

        // This enforces a maximum limit of eight regular working hours per day.
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

        // This array represents the SSS contribution table for the year.
        // Each row contains the salary ceiling and its corresponding employee contribution amount.
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

        // This loop iterates through the table to find the appropriate contribution bracket.
        for (double[] bracket : sssTable) {
            if (gross <= bracket[0]) {
                return bracket[1]; // This returns the required contribution amount for the matched bracket.
            }
        }

        return 1125.00; // The method applies the maximum contribution if the salary exceeds the highest bracket.
    }

    // PhilHealth Contribution Computation
    public static double computePhilHealth(double gross) {
        double premium;

        if (gross <= 10000) {
            premium = 300;        // This sets the minimum allowable premium.
        }
        else if (gross >= 60000) {
            premium = 1800;       // This sets the maximum allowable premium.
        }
        else {
            premium = gross * 0.03;   // This calculates the premium as 3% of the basic salary.
        }
        return premium / 2; // The method returns half of the premium, representing the employee's 50% share.
    }

    // Pag-IBIG Contribution Computation
    public static double computePagibig(double gross) {
        double pagibig;

        if (gross >= 1000 && gross <= 1500) // This condition applies if the basic salary is between one thousand and one thousand five hundred.
            pagibig = gross * 0.01;         // The employee contributes 1% of their basic salary.
        else
            pagibig = gross * 0.02;         // This condition applies if the basic salary exceeds 1,500, requiring a 2% percent contribution.
        if (pagibig > 100)                  // The Pag-IBIG contribution is capped at a maximum of one hundred pesos.
            pagibig = 100;

        return pagibig;
    }

    // Withholding Tax Computation
    public static double computeTax(double taxableIncome) {

        double tax = 0;

        if (taxableIncome <= 20832)                             // Income amounts of 20,832 and below incur no tax.
            tax = 0;
        else if (taxableIncome < 33333)                         // This calculates the tax as 20% of the excess over 20,833.
            tax = (taxableIncome - 20833) * 0.20;
        else if (taxableIncome < 66667)                         // This calculates the tax as 2500 plus 25% of the excess over 33,333, which ranges from 33,333 to 66,667.
            tax = 2500 + (taxableIncome - 33333) * 0.25;
        else if (taxableIncome < 166667)                        // This calculates the tax as 10,833 + 30% of the excess, which ranges from 66,667 to 166,667.
            tax = 10833 + (taxableIncome - 66667) * 0.30;
        else if (taxableIncome < 666667)                        // This calculates the tax as 40,833.33 + 32% of the excess, which ranges from 166,667 to 666,667.
            tax = 40833.33 + (taxableIncome - 166667) * 0.32;
        else
            tax = 200833.33 + (taxableIncome - 666667) * 0.35;  // This applies the maximum tax bracket rate (200,833.33 + 35% excess) for highest earners, which is 666,667 and above.

        return tax;

    }

    // ============================================
    // INPUT VALIDATION
    // ============================================

    /**
     * Prompts the user for an integer input and re-prompts if the input is not a valid number.
     * This prevents NumberFormatException crashes from non-numeric input.
     */
    public static int getValidInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

}
