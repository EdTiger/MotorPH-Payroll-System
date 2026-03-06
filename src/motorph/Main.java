package motorph;

/*
 * ============================================================
 * MotorPH Basic Payroll System (Milestone 2: Initial Code Submission)
 * Developed by ByteBeans
 * ============================================================
 *
 * Purpose:
 *   Complete payroll management system for MotorPH that handles
 *   employee information, attendance tracking, and semi-monthly
 *   payroll computation with government-mandated deductions.
 *
 * Structure:
 *   - Procedural design using static methods and parallel arrays
 *   - Employee and attendance data loaded from CSV files at runtime
 *   - Government contribution tables (SSS, PhilHealth, Pag-IBIG,
 *     BIR Withholding Tax) hardcoded per Philippine law
 *   - Interactive menu-driven console interface with 5 options
 *
 * Input Files (located in project root, same level as src/):
 *   1. MotorPH_Employee Data - Employee Details.csv
 *      - 34 employees, 19 fields per row, header on line 1
 *   2. MotorPH_Employee Data - Attendance Record.csv
 *      - 5,167 records (June 3 – December 31, 2024), 6 fields, header on line 1
 *
 * Payroll Logic:
 *   - Work hours counted between 8:00 AM and 5:00 PM only
 *   - Grace period: login at 8:10 AM or earlier treated as on time
 *   - Lunch period (12:00–1:00 PM) excluded from billable hours
 *   - Two cutoff periods per month: 1st (days 1–15), 2nd (days 16–end)
 *   - Government deductions computed on combined monthly gross pay
 *   - Late arrivals reduce hours worked (no separate penalty amount)
 * ============================================================
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    // ==================== SYSTEM CONSTANTS ====================

    static final int MAX_EMPLOYEES = 50;
    static final int MAX_ATTENDANCE = 6000;
    static final String EMPLOYEE_FILE = "MotorPH_Employee Data - Employee Details.csv";
    static final String ATTENDANCE_FILE = "MotorPH_Employee Data - Attendance Record.csv";
    static final int EMPLOYEE_FIELD_COUNT = 19;
    static final int ATTENDANCE_FIELD_COUNT = 6;

    // ==================== TIME CONSTANTS (minutes from midnight) ====================

    static final int WORK_START = 480;       // 8:00 AM
    static final int WORK_END = 1020;        // 5:00 PM
    static final int GRACE_LIMIT = 490;      // 8:10 AM
    static final int LUNCH_START = 720;      // 12:00 PM
    static final int LUNCH_END = 780;        // 1:00 PM

    // ==================== DISPLAY CONSTANTS ====================

    static final String[] MONTH_NAMES = {
        "", "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    };

    // ==================== EMPLOYEE DATA (parallel arrays) ====================

    static int employeeCount = 0;
    static int[] empNumbers = new int[MAX_EMPLOYEES];
    static String[] lastNames = new String[MAX_EMPLOYEES];
    static String[] firstNames = new String[MAX_EMPLOYEES];
    static String[] birthdays = new String[MAX_EMPLOYEES];
    static String[] addresses = new String[MAX_EMPLOYEES];
    static String[] phoneNumbers = new String[MAX_EMPLOYEES];
    static String[] sssNumbers = new String[MAX_EMPLOYEES];
    static String[] philhealthNumbers = new String[MAX_EMPLOYEES];
    static String[] tinNumbers = new String[MAX_EMPLOYEES];
    static String[] pagibigNumbers = new String[MAX_EMPLOYEES];
    static String[] empStatuses = new String[MAX_EMPLOYEES];
    static String[] positions = new String[MAX_EMPLOYEES];
    static String[] supervisors = new String[MAX_EMPLOYEES];
    static double[] basicSalaries = new double[MAX_EMPLOYEES];
    static double[] riceSubsidies = new double[MAX_EMPLOYEES];
    static double[] phoneAllowances = new double[MAX_EMPLOYEES];
    static double[] clothingAllowances = new double[MAX_EMPLOYEES];
    static double[] grossSemiMonthlyRates = new double[MAX_EMPLOYEES];
    static double[] hourlyRates = new double[MAX_EMPLOYEES];

    // ==================== ATTENDANCE DATA (parallel arrays) ====================

    static int attendanceCount = 0;
    static int[] attEmpNumbers = new int[MAX_ATTENDANCE];
    static String[] attLastNames = new String[MAX_ATTENDANCE];
    static String[] attFirstNames = new String[MAX_ATTENDANCE];
    static String[] attDates = new String[MAX_ATTENDANCE];
    static String[] attLogIns = new String[MAX_ATTENDANCE];
    static String[] attLogOuts = new String[MAX_ATTENDANCE];

    // ==================== MAIN ====================

    /**
     * Entry point. Loads CSV data from disk and runs the interactive
     * menu loop until the user chooses to exit.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        loadEmployeeData();
        loadAttendanceData();

        if (employeeCount == 0) {
            System.out.println("Error: No employee data loaded. Ensure '"
                + EMPLOYEE_FILE + "' is in the project root directory.");
            return;
        }
        if (attendanceCount == 0) {
            System.out.println("Warning: No attendance data loaded. Ensure '"
                + ATTENDANCE_FILE + "' is in the project root directory.");
        }

        System.out.println("Loaded " + employeeCount + " employees and "
            + attendanceCount + " attendance records.\n");

        Scanner scanner = new Scanner(System.in);
        int choice = 0;

        do {
            printMenu();
            System.out.print("Enter your choice: ");
            String input = scanner.nextLine().trim();

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input. Please enter a number between 1 and 5.");
                continue;
            }

            switch (choice) {
                case 1: viewAllEmployees();             break;
                case 2: viewEmployeeDetails(scanner);   break;
                case 3: viewAttendanceRecords(scanner);  break;
                case 4: calculatePayroll(scanner);       break;
                case 5:
                    System.out.println("\nThank you for using the MotorPH Payroll System. Goodbye!");
                    break;
                default:
                    System.out.println("\nInvalid choice. Please enter a number between 1 and 5.");
            }
        } while (choice != 5);

        scanner.close();
    }

    // ==================== MENU ====================

    /**
     * Prints the main menu to the console.
     */
    static void printMenu() {
        System.out.println("\n========================================");
        System.out.println("       MOTORPH PAYROLL SYSTEM");
        System.out.println("========================================");
        System.out.println("1. View All Employees");
        System.out.println("2. View Employee Details");
        System.out.println("3. View Attendance Records");
        System.out.println("4. Calculate Payroll for a Month");
        System.out.println("5. Exit");
        System.out.println("========================================");
    }
/// mic check mic check 
    // ==================== DATA LOADING ====================

    /**
     * Reads the Employee Details CSV into the parallel employee arrays.
     * Handles quoted fields (addresses, salaries, supervisor names that
     * contain commas). Skips the header row and any malformed lines.
     */
    static void loadEmployeeData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(EMPLOYEE_FILE))) {
            String line;
            boolean headerSkipped = false;

            while ((line = reader.readLine()) != null && employeeCount < MAX_EMPLOYEES) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }

                try {
                    String[] f = parseCsvLine(line, EMPLOYEE_FIELD_COUNT);

                    int i = employeeCount;
                    empNumbers[i]           = Integer.parseInt(f[0]);
                    lastNames[i]            = f[1];
                    firstNames[i]           = f[2];
                    birthdays[i]            = f[3];
                    addresses[i]            = f[4];
                    phoneNumbers[i]         = f[5];
                    sssNumbers[i]           = f[6];
                    philhealthNumbers[i]    = f[7];
                    tinNumbers[i]           = f[8];
                    pagibigNumbers[i]       = f[9];
                    empStatuses[i]          = f[10];
                    positions[i]            = f[11];
                    supervisors[i]          = f[12];
                    basicSalaries[i]        = parseSalary(f[13]);
                    riceSubsidies[i]        = parseSalary(f[14]);
                    phoneAllowances[i]      = parseSalary(f[15]);
                    clothingAllowances[i]   = parseSalary(f[16]);
                    grossSemiMonthlyRates[i]= parseSalary(f[17]);
                    hourlyRates[i]          = parseSalary(f[18]);

                    employeeCount++;
                } catch (NumberFormatException e) {
                    System.out.println("Warning: Skipped malformed employee row – " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading employee file: " + e.getMessage());
        }
    }

    /**
     * Reads the Attendance Record CSV into the parallel attendance arrays.
     * Skips the header row and any malformed lines.
     */
    static void loadAttendanceData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ATTENDANCE_FILE))) {
            String line;
            boolean headerSkipped = false;

            while ((line = reader.readLine()) != null && attendanceCount < MAX_ATTENDANCE) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }

                try {
                    String[] f = parseCsvLine(line, ATTENDANCE_FIELD_COUNT);

                    int i = attendanceCount;
                    attEmpNumbers[i]  = Integer.parseInt(f[0]);
                    attLastNames[i]   = f[1];
                    attFirstNames[i]  = f[2];
                    attDates[i]       = f[3];
                    attLogIns[i]      = f[4];
                    attLogOuts[i]     = f[5];

                    attendanceCount++;
                } catch (NumberFormatException e) {
                    System.out.println("Warning: Skipped malformed attendance row – " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading attendance file: " + e.getMessage());
        }
    }

    // ==================== CSV / PARSING HELPERS ====================

    /**
     * Parses a single CSV line while respecting quoted fields that may
     * contain commas (e.g. addresses, salary values, supervisor names).
     *
     * @param line           the raw CSV line
     * @param expectedFields number of columns expected
     * @return array of trimmed field values
     */
    static String[] parseCsvLine(String line, int expectedFields) {
        String[] result = new String[expectedFields];
        int fieldIndex = 0;
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                if (fieldIndex < expectedFields) {
                    result[fieldIndex] = current.toString().trim();
                    fieldIndex++;
                    current.setLength(0);
                }
            } else {
                current.append(c);
            }
        }

        // Capture the last field
        if (fieldIndex < expectedFields) {
            result[fieldIndex] = current.toString().trim();
        }

        // Fill any remaining slots with empty strings to avoid nulls
        for (int i = 0; i < expectedFields; i++) {
            if (result[i] == null) result[i] = "";
        }

        return result;
    }

    /**
     * Strips commas from a salary/currency string and parses it as double.
     *
     * @param value string like "90,000" or "535.71"
     * @return the numeric value
     */
    static double parseSalary(String value) {
        return Double.parseDouble(value.replace(",", ""));
    }

    /**
     * Converts a time string (H:mm or HH:mm, 24-hour) to minutes since midnight.
     *
     * @param time e.g. "8:05" or "17:30"
     * @return total minutes from 00:00
     */
    static int parseTimeToMinutes(String time) {
        String[] parts = time.split(":");
        int hours   = Integer.parseInt(parts[0].trim());
        int minutes = Integer.parseInt(parts[1].trim());
        return hours * 60 + minutes;
    }

    /**
     * Extracts the month (1–12) from a date in MM/dd/yyyy format.
     *
     * @param date the date string
     * @return month number
     */
    static int getMonthFromDate(String date) {
        return Integer.parseInt(date.split("/")[0]);
    }

    /**
     * Extracts the day of month (1–31) from a date in MM/dd/yyyy format.
     *
     * @param date the date string
     * @return day of month
     */
    static int getDayFromDate(String date) {
        return Integer.parseInt(date.split("/")[1]);
    }

    // ==================== LOOKUP ====================

    /**
     * Finds the array index of the given employee number.
     *
     * @param empNumber the employee number to locate
     * @return index in the employee arrays, or -1 if not found
     */
    static int findEmployeeIndex(int empNumber) {
        for (int i = 0; i < employeeCount; i++) {
            if (empNumbers[i] == empNumber) return i;
        }
        return -1;
    }

    // ==================== HOURS WORKED ====================

    /**
     * Computes billable hours for a single day given login/logout times.
     * <ul>
     *   <li>Only counts time within the 8:00 AM – 5:00 PM window</li>
     *   <li>Grace period: login at 8:10 AM or earlier → effective start 8:00 AM</li>
     *   <li>Lunch period (12:00–1:00 PM) excluded from billable time</li>
     *   <li>Overtime (logout after 5:00 PM) not counted</li>
     * </ul>
     *
     * @param logIn  login time (H:mm)
     * @param logOut logout time (H:mm)
     * @return hours worked
     */
    static double computeHoursWorked(String logIn, String logOut) {
        int loginMin  = parseTimeToMinutes(logIn);
        int logoutMin = parseTimeToMinutes(logOut);

        // Grace period: 8:10 or earlier counts as 8:00
        int effectiveStart = (loginMin <= GRACE_LIMIT) ? WORK_START : loginMin;

        // Cap at the 5:00 PM boundary
        int effectiveEnd = Math.min(logoutMin, WORK_END);

        if (effectiveEnd <= effectiveStart) return 0.0;

        double rawMinutes = effectiveEnd - effectiveStart;

        // Exclude any overlap with the 12:00–1:00 PM lunch window
        int overlapStart = Math.max(effectiveStart, LUNCH_START);
        int overlapEnd   = Math.min(effectiveEnd, LUNCH_END);
        if (overlapEnd > overlapStart) {
            rawMinutes -= (overlapEnd - overlapStart);
        }

        double hours = rawMinutes / 60.0;
        return (hours < 0) ? 0.0 : hours;
    }

    /**
     * Determines if an employee was late based on login time.
     * Login at 8:10 AM or earlier is considered ON TIME.
     *
     * @param logIn login time (H:mm)
     * @return true if late
     */
    static boolean isLate(String logIn) {
        return parseTimeToMinutes(logIn) > GRACE_LIMIT;
    }

    // ==================== OPTION 1 – VIEW ALL EMPLOYEES ====================

    /**
     * Displays a compact table of every employee: number, full name,
     * position, status, and basic salary.
     */
    static void viewAllEmployees() {
        System.out.println("\n==========================================================================================");
        System.out.println("                                    EMPLOYEE LIST");
        System.out.println("==========================================================================================");
        System.out.printf("%-10s %-25s %-35s %-14s %14s%n",
            "Emp #", "Full Name", "Position", "Status", "Basic Salary");
        System.out.println("------------------------------------------------------------------------------------------");

        for (int i = 0; i < employeeCount; i++) {
            String fullName = firstNames[i] + " " + lastNames[i];
            System.out.printf("%-10d %-25s %-35s %-14s %,14.2f%n",
                empNumbers[i], fullName, positions[i], empStatuses[i], basicSalaries[i]);
        }

        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("Total Employees: " + employeeCount);
        System.out.println("==========================================================================================");
    }

    // ==================== OPTION 2 – VIEW EMPLOYEE DETAILS ====================

    /**
     * Prompts for an employee number and prints every field from the
     * Employee Details CSV for that employee.
     *
     * @param scanner the Scanner for user input
     */
    static void viewEmployeeDetails(Scanner scanner) {
        System.out.print("\nEnter Employee Number: ");
        int empNumber = readInt(scanner);
        if (empNumber == -1) return;

        int idx = findEmployeeIndex(empNumber);
        if (idx == -1) {
            System.out.println("Employee #" + empNumber + " not found.");
            return;
        }

        System.out.println("\n========================================");
        System.out.println("         EMPLOYEE DETAILS");
        System.out.println("========================================");
        System.out.printf("Employee #              : %d%n",       empNumbers[idx]);
        System.out.printf("Last Name               : %s%n",       lastNames[idx]);
        System.out.printf("First Name              : %s%n",       firstNames[idx]);
        System.out.printf("Birthday                : %s%n",       birthdays[idx]);
        System.out.printf("Address                 : %s%n",       addresses[idx]);
        System.out.printf("Phone Number            : %s%n",       phoneNumbers[idx]);
        System.out.printf("SSS #                   : %s%n",       sssNumbers[idx]);
        System.out.printf("PhilHealth #            : %s%n",       philhealthNumbers[idx]);
        System.out.printf("TIN #                   : %s%n",       tinNumbers[idx]);
        System.out.printf("Pag-IBIG #              : %s%n",       pagibigNumbers[idx]);
        System.out.printf("Status                  : %s%n",       empStatuses[idx]);
        System.out.printf("Position                : %s%n",       positions[idx]);
        System.out.printf("Immediate Supervisor    : %s%n",       supervisors[idx]);
        System.out.printf("Basic Salary            : PHP %,.2f%n", basicSalaries[idx]);
        System.out.printf("Rice Subsidy            : PHP %,.2f%n", riceSubsidies[idx]);
        System.out.printf("Phone Allowance         : PHP %,.2f%n", phoneAllowances[idx]);
        System.out.printf("Clothing Allowance      : PHP %,.2f%n", clothingAllowances[idx]);
        System.out.printf("Gross Semi-monthly Rate : PHP %,.2f%n", grossSemiMonthlyRates[idx]);
        System.out.printf("Hourly Rate             : PHP %,.2f%n", hourlyRates[idx]);
        System.out.println("========================================");
    }

    // ==================== OPTION 3 – VIEW ATTENDANCE RECORDS ====================

    /**
     * Prompts for an employee number, then lists every attendance record
     * for that employee with computed hours worked and late/on-time status.
     *
     * @param scanner the Scanner for user input
     */
    static void viewAttendanceRecords(Scanner scanner) {
        System.out.print("\nEnter Employee Number: ");
        int empNumber = readInt(scanner);
        if (empNumber == -1) return;

        int empIdx = findEmployeeIndex(empNumber);
        if (empIdx == -1) {
            System.out.println("Employee #" + empNumber + " not found.");
            return;
        }

        String fullName = firstNames[empIdx] + " " + lastNames[empIdx];

        System.out.println("\n========================================================================");
        System.out.println("                       ATTENDANCE RECORDS");
        System.out.println("========================================================================");
        System.out.printf("Employee: %s (#%d)%n", fullName, empNumber);
        System.out.println("------------------------------------------------------------------------");
        System.out.printf("%-14s %-10s %-10s %14s  %-10s%n",
            "Date", "Log In", "Log Out", "Hours Worked", "Status");
        System.out.println("------------------------------------------------------------------------");

        int recordCount = 0;
        double totalHours = 0;

        for (int i = 0; i < attendanceCount; i++) {
            if (attEmpNumbers[i] == empNumber) {
                double hw = computeHoursWorked(attLogIns[i], attLogOuts[i]);
                String status = isLate(attLogIns[i]) ? "LATE" : "ON TIME";

                System.out.printf("%-14s %-10s %-10s %14.2f  %-10s%n",
                    attDates[i], attLogIns[i], attLogOuts[i], hw, status);

                totalHours += hw;
                recordCount++;
            }
        }

        System.out.println("------------------------------------------------------------------------");
        System.out.printf("Total Records: %d  |  Total Hours Worked: %.2f%n", recordCount, totalHours);
        System.out.println("========================================================================");
    }

    // ==================== OPTION 4 – CALCULATE PAYROLL ====================

    /**
     * Prompts for Employee # and Month (1–12 for 2024), then:
     * <ol>
     *   <li>Lists attendance grouped by 1st cutoff (days 1–15) and 2nd cutoff (days 16–end)</li>
     *   <li>Computes gross pay per cutoff (hours worked × hourly rate)</li>
     *   <li>Combines both cutoff gross pays into a monthly total</li>
     *   <li>Computes SSS, PhilHealth, Pag-IBIG, income tax on the monthly total</li>
     *   <li>Displays the full payroll summary with net pay</li>
     * </ol>
     *
     * @param scanner the Scanner for user input
     */
    static void calculatePayroll(Scanner scanner) {
        // --- Get and validate employee number ---
        System.out.print("\nEnter Employee Number: ");
        int empNumber = readInt(scanner);
        if (empNumber == -1) return;

        int empIdx = findEmployeeIndex(empNumber);
        if (empIdx == -1) {
            System.out.println("Employee #" + empNumber + " not found.");
            return;
        }

        // --- Get and validate month ---
        System.out.print("Enter Month (1-12 for 2024): ");
        int month = readInt(scanner);
        if (month == -1) return;
        if (month < 1 || month > 12) {
            System.out.println("Invalid month. Please enter a number between 1 and 12.");
            return;
        }

        String fullName = firstNames[empIdx] + " " + lastNames[empIdx];
        double rate = hourlyRates[empIdx];

        System.out.println("\n========================================================================");
        System.out.println("                       PAYROLL CALCULATION");
        System.out.println("========================================================================");
        System.out.printf("Employee   : %s (#%d)%n", fullName, empNumber);
        System.out.printf("Position   : %s%n", positions[empIdx]);
        System.out.printf("Month      : %s 2024%n", MONTH_NAMES[month]);
        System.out.printf("Hourly Rate: PHP %,.2f%n", rate);
        System.out.println("========================================================================");

        // ---- 1ST CUTOFF (Days 1–15) ----
        System.out.println("\n--- 1ST CUTOFF (Days 1-15) ---");
        System.out.printf("%-14s %-10s %-10s %14s  %-10s%n",
            "Date", "Log In", "Log Out", "Hours Worked", "Status");
        System.out.println("--------------------------------------------------------------");

        double firstCutoffHours = 0;
        int firstCutoffDays = 0;

        for (int i = 0; i < attendanceCount; i++) {
            if (attEmpNumbers[i] == empNumber && getMonthFromDate(attDates[i]) == month) {
                int day = getDayFromDate(attDates[i]);
                if (day >= 1 && day <= 15) {
                    double hw = computeHoursWorked(attLogIns[i], attLogOuts[i]);
                    String status = isLate(attLogIns[i]) ? "LATE" : "ON TIME";
                    System.out.printf("%-14s %-10s %-10s %14.2f  %-10s%n",
                        attDates[i], attLogIns[i], attLogOuts[i], hw, status);
                    firstCutoffHours += hw;
                    firstCutoffDays++;
                }
            }
        }

        double firstCutoffGross = firstCutoffHours * rate;
        System.out.println("--------------------------------------------------------------");
        System.out.printf("Days Worked: %d  |  Total Hours: %.2f%n", firstCutoffDays, firstCutoffHours);
        System.out.printf("1st Cutoff Gross Pay: PHP %,.2f%n", firstCutoffGross);

        // ---- 2ND CUTOFF (Days 16–end of month) ----
        System.out.println("\n--- 2ND CUTOFF (Days 16-End) ---");
        System.out.printf("%-14s %-10s %-10s %14s  %-10s%n",
            "Date", "Log In", "Log Out", "Hours Worked", "Status");
        System.out.println("--------------------------------------------------------------");

        double secondCutoffHours = 0;
        int secondCutoffDays = 0;

        for (int i = 0; i < attendanceCount; i++) {
            if (attEmpNumbers[i] == empNumber && getMonthFromDate(attDates[i]) == month) {
                int day = getDayFromDate(attDates[i]);
                if (day >= 16) {
                    double hw = computeHoursWorked(attLogIns[i], attLogOuts[i]);
                    String status = isLate(attLogIns[i]) ? "LATE" : "ON TIME";
                    System.out.printf("%-14s %-10s %-10s %14.2f  %-10s%n",
                        attDates[i], attLogIns[i], attLogOuts[i], hw, status);
                    secondCutoffHours += hw;
                    secondCutoffDays++;
                }
            }
        }

        double secondCutoffGross = secondCutoffHours * rate;
        System.out.println("--------------------------------------------------------------");
        System.out.printf("Days Worked: %d  |  Total Hours: %.2f%n", secondCutoffDays, secondCutoffHours);
        System.out.printf("2nd Cutoff Gross Pay: PHP %,.2f%n", secondCutoffGross);

        // ---- Combine cutoffs and compute deductions ----
        double totalMonthlyGross = firstCutoffGross + secondCutoffGross;

        double sss       = computeSSS(totalMonthlyGross);
        double philHealth = computePhilHealth(totalMonthlyGross);
        double pagIbig   = computePagIbig(totalMonthlyGross);

        double preTaxDeductions = sss + philHealth + pagIbig;
        double taxableIncome    = totalMonthlyGross - preTaxDeductions;
        double incomeTax        = computeIncomeTax(taxableIncome);
        double totalDeductions  = preTaxDeductions + incomeTax;
        double netPay           = computeNetPay(totalMonthlyGross, sss, philHealth, pagIbig, incomeTax);

        // ---- Payroll Summary ----
        System.out.println("\n========================================================================");
        System.out.println("                         PAYROLL SUMMARY");
        System.out.println("========================================================================");
        System.out.printf("Employee Name        : %s%n", fullName);
        System.out.printf("Month                : %s 2024%n", MONTH_NAMES[month]);
        System.out.println("------------------------------------------------------------------------");
        System.out.printf("1st Cutoff Gross Pay : PHP %,18.2f%n", firstCutoffGross);
        System.out.printf("2nd Cutoff Gross Pay : PHP %,18.2f%n", secondCutoffGross);
        System.out.printf("Total Monthly Gross  : PHP %,18.2f%n", totalMonthlyGross);
        System.out.println("------------------------------------------------------------------------");
        System.out.println("DEDUCTIONS:");
        System.out.printf("  SSS                : PHP %,18.2f%n", sss);
        System.out.printf("  PhilHealth         : PHP %,18.2f%n", philHealth);
        System.out.printf("  Pag-IBIG           : PHP %,18.2f%n", pagIbig);
        System.out.println("                       ------------------");
        System.out.printf("  Subtotal           : PHP %,18.2f%n", preTaxDeductions);
        System.out.println("------------------------------------------------------------------------");
        System.out.printf("Taxable Income       : PHP %,18.2f%n", taxableIncome);
        System.out.printf("Income Tax           : PHP %,18.2f%n", incomeTax);
        System.out.println("------------------------------------------------------------------------");
        System.out.printf("Total Deductions     : PHP %,18.2f%n", totalDeductions);
        System.out.printf("NET PAY              : PHP %,18.2f%n", netPay);
        System.out.println("========================================================================");
    }

    // ==================== GOVERNMENT DEDUCTION METHODS ====================

    /**
     * Computes the SSS employee contribution using the official bracket table.
     * Brackets range from below 3,250 (PHP 135) to 24,750+ (PHP 1,125),
     * in steps of 500 pesos with contributions increasing by 22.50 each step.
     *
     * @param monthlySalary the employee's monthly gross pay
     * @return SSS employee contribution
     */
    static double computeSSS(double monthlySalary) {
        // Lower bounds for each SSS bracket (3,250 to 24,750 in 500-step increments)
        double[] floors = {
             3250,  3750,  4250,  4750,  5250,  5750,  6250,  6750,  7250,  7750,
             8250,  8750,  9250,  9750, 10250, 10750, 11250, 11750, 12250, 12750,
            13250, 13750, 14250, 14750, 15250, 15750, 16250, 16750, 17250, 17750,
            18250, 18750, 19250, 19750, 20250, 20750, 21250, 21750, 22250, 22750,
            23250, 23750, 24250, 24750
        };

        // Employee share for each bracket (157.50 to 1,125.00 in 22.50 steps)
        double[] contributions = {
              157.50,  180.00,  202.50,  225.00,  247.50,  270.00,  292.50,  315.00,  337.50,  360.00,
              382.50,  405.00,  427.50,  450.00,  472.50,  495.00,  517.50,  540.00,  562.50,  585.00,
              607.50,  630.00,  652.50,  675.00,  697.50,  720.00,  742.50,  765.00,  787.50,  810.00,
              832.50,  855.00,  877.50,  900.00,  922.50,  945.00,  967.50,  990.00, 1012.50, 1035.00,
             1057.50, 1080.00, 1102.50, 1125.00
        };

        if (monthlySalary < 3250) return 135.00;

        // Search from highest bracket downward to find the first match
        for (int i = floors.length - 1; i >= 0; i--) {
            if (monthlySalary >= floors[i]) {
                return contributions[i];
            }
        }

        return 135.00;
    }

    /**
     * Computes the PhilHealth employee share.
     * <ul>
     *   <li>Floor: salary &le; 10,000 → employee share 150.00</li>
     *   <li>Ceiling: salary &ge; 60,000 → employee share 900.00</li>
     *   <li>Between: total premium = salary × 3%, employee pays half</li>
     * </ul>
     *
     * @param monthlySalary the employee's monthly gross pay
     * @return PhilHealth employee share
     */
    static double computePhilHealth(double monthlySalary) {
        if (monthlySalary <= 10000.00) {
            return 150.00;
        } else if (monthlySalary >= 60000.00) {
            return 900.00;
        } else {
            double totalPremium = monthlySalary * 0.03;
            return totalPremium / 2.0;
        }
    }

    /**
     * Computes the Pag-IBIG employee contribution.
     * <ul>
     *   <li>Salary 1,000–1,500 → 1% of salary</li>
     *   <li>Salary above 1,500 → 2% of salary</li>
     *   <li>Maximum contribution capped at PHP 100.00</li>
     * </ul>
     *
     * @param monthlySalary the employee's monthly gross pay
     * @return Pag-IBIG employee contribution
     */
    static double computePagIbig(double monthlySalary) {
        if (monthlySalary < 1000.00) {
            return 0.00;
        }

        double contribution;
        if (monthlySalary <= 1500.00) {
            contribution = monthlySalary * 0.01;
        } else {
            contribution = monthlySalary * 0.02;
        }

        return Math.min(contribution, 100.00);
    }

    /**
     * Computes the BIR withholding tax using the official tax bracket table.
     * <pre>
     *   20,832 and below         → 0
     *   20,833 – 33,332          → (income − 20,833) × 20%
     *   33,333 – 66,666          → 2,500 + (income − 33,333) × 25%
     *   66,667 – 166,666         → 10,833 + (income − 66,667) × 30%
     *   166,667 – 666,666        → 40,833.33 + (income − 166,667) × 32%
     *   666,667 and above        → 200,833.33 + (income − 666,667) × 35%
     * </pre>
     *
     * @param taxableIncome monthly gross minus SSS, PhilHealth, Pag-IBIG
     * @return withholding tax amount
     */
    static double computeIncomeTax(double taxableIncome) {
        if (taxableIncome <= 20832.00) {
            return 0.00;
        } else if (taxableIncome <= 33332.00) {
            return (taxableIncome - 20833.00) * 0.20;
        } else if (taxableIncome <= 66666.00) {
            return 2500.00 + (taxableIncome - 33333.00) * 0.25;
        } else if (taxableIncome <= 166666.00) {
            return 10833.00 + (taxableIncome - 66667.00) * 0.30;
        } else if (taxableIncome <= 666666.00) {
            return 40833.33 + (taxableIncome - 166667.00) * 0.32;
        } else {
            return 200833.33 + (taxableIncome - 666667.00) * 0.35;
        }
    }

    /**
     * Computes net pay by subtracting all deductions from gross pay.
     *
     * @param grossPay  total monthly gross pay
     * @param sss       SSS deduction
     * @param philHealth PhilHealth deduction
     * @param pagIbig   Pag-IBIG deduction
     * @param incomeTax withholding tax
     * @return net pay after all deductions
     */
    static double computeNetPay(double grossPay, double sss, double philHealth,
                                double pagIbig, double incomeTax) {
        return grossPay - sss - philHealth - pagIbig - incomeTax;
    }

    // ==================== INPUT HELPER ====================

    /**
     * Reads and validates an integer from the scanner.
     * Returns -1 and prints an error message if the input is not a valid integer.
     *
     * @param scanner the Scanner for user input
     * @return the parsed integer, or -1 on invalid input
     */
    static int readInt(Scanner scanner) {
        String input = scanner.nextLine().trim();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return -1;
        }
    }
}
