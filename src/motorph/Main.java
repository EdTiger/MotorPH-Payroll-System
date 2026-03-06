package motorph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    static final int MAX_EMPLOYEES = 50;
    static final int MAX_ATTENDANCE = 6000;

    static final String EMPLOYEE_FILE = "MotorPH_Employee Data - Employee Details.csv";
    static final String ATTENDANCE_FILE = "MotorPH_Employee Data - Attendance Record.csv";

    // Time constants in minutes since midnight
    static final int WORK_START  = 8 * 60;      // 8:00 AM = 480
    static final int WORK_END    = 17 * 60;     // 5:00 PM = 1020
    static final int GRACE_LIMIT = 8 * 60 + 10; // 8:10 AM = 490
    static final int LUNCH_START = 12 * 60;     // 12:00 PM = 720
    static final int LUNCH_END   = 13 * 60;     // 1:00 PM = 780

    static final String[] MONTH_NAMES = {
            "", "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    };

    // ==================== Employee parallel arrays ====================

    static int employeeCount = 0;

    static int[]    empNumbers            = new int[MAX_EMPLOYEES];
    static String[] lastNames             = new String[MAX_EMPLOYEES];
    static String[] firstNames            = new String[MAX_EMPLOYEES];
    static String[] birthdays             = new String[MAX_EMPLOYEES];
    static String[] addresses             = new String[MAX_EMPLOYEES];
    static String[] phoneNumbers          = new String[MAX_EMPLOYEES];
    static String[] sssNumbers            = new String[MAX_EMPLOYEES];
    static String[] philhealthNumbers     = new String[MAX_EMPLOYEES];
    static String[] tinNumbers            = new String[MAX_EMPLOYEES];
    static String[] pagibigNumbers        = new String[MAX_EMPLOYEES];
    static String[] empStatuses           = new String[MAX_EMPLOYEES];
    static String[] positions             = new String[MAX_EMPLOYEES];
    static String[] supervisors           = new String[MAX_EMPLOYEES];
    static double[] basicSalaries         = new double[MAX_EMPLOYEES];
    static double[] riceSubsidies         = new double[MAX_EMPLOYEES];
    static double[] phoneAllowances       = new double[MAX_EMPLOYEES];
    static double[] clothingAllowances    = new double[MAX_EMPLOYEES];
    static double[] grossSemiMonthlyRates = new double[MAX_EMPLOYEES];
    static double[] hourlyRates           = new double[MAX_EMPLOYEES];

    // ==================== Attendance parallel arrays ====================

    static int attendanceCount = 0;

    static int[]    attEmpNumbers  = new int[MAX_ATTENDANCE];
    static String[] attLastNames   = new String[MAX_ATTENDANCE];
    static String[] attFirstNames  = new String[MAX_ATTENDANCE];
    static String[] attDates       = new String[MAX_ATTENDANCE];
    static String[] attLogIns      = new String[MAX_ATTENDANCE];
    static String[] attLogOuts     = new String[MAX_ATTENDANCE];

    // SSS bracket table: {lowerBound, upperBound, employeeContribution}
    static double[][] sssTable = {
            {0, 3249.99, 135.00},
            {3250, 3749.99, 157.50},    {3750, 4249.99, 180.00},
            {4250, 4749.99, 202.50},    {4750, 5249.99, 225.00},
            {5250, 5749.99, 247.50},    {5750, 6249.99, 270.00},
            {6250, 6749.99, 292.50},    {6750, 7249.99, 315.00},
            {7250, 7749.99, 337.50},    {7750, 8249.99, 360.00},
            {8250, 8749.99, 382.50},    {8750, 9249.99, 405.00},
            {9250, 9749.99, 427.50},    {9750, 10249.99, 450.00},
            {10250, 10749.99, 472.50},  {10750, 11249.99, 495.00},
            {11250, 11749.99, 517.50},  {11750, 12249.99, 540.00},
            {12250, 12749.99, 562.50},  {12750, 13249.99, 585.00},
            {13250, 13749.99, 607.50},  {13750, 14249.99, 630.00},
            {14250, 14749.99, 652.50},  {14750, 15249.99, 675.00},
            {15250, 15749.99, 697.50},  {15750, 16249.99, 720.00},
            {16250, 16749.99, 742.50},  {16750, 17249.99, 765.00},
            {17250, 17749.99, 787.50},  {17750, 18249.99, 810.00},
            {18250, 18749.99, 832.50},  {18750, 19249.99, 855.00},
            {19250, 19749.99, 877.50},  {19750, 20249.99, 900.00},
            {20250, 20749.99, 922.50},  {20750, 21249.99, 945.00},
            {21250, 21749.99, 967.50},  {21750, 22249.99, 990.00},
            {22250, 22749.99, 1012.50}, {22750, 23249.99, 1035.00},
            {23250, 23749.99, 1057.50}, {23750, 24249.99, 1080.00},
            {24250, 24749.99, 1102.50}, {24750, 999999.99, 1125.00}
    };

    // ==================== MAIN ====================

    public static void main(String[] args) {
        loadEmployeeData();
        loadAttendanceData();

        if (employeeCount == 0) {
            System.out.println("Error: Could not load employee data.");
            System.out.println("Make sure '" + EMPLOYEE_FILE + "' exists in the project root.");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        System.out.println("=================================");
        System.out.println("                                 ");
        System.out.println("            MotorPH              ");
        System.out.println("        Payroll System           ");
        System.out.println("                                 ");
        System.out.println("=================================");

        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        boolean validUser = username.equals("employee") || username.equals("payroll_staff");
        if (!validUser || !password.equals("12345")) {
            System.out.println("Invalid username or password.");
            scanner.close();
            return;
        }

        if (username.equals("employee")) {
            employeeMenu(scanner);
        } else {
            payrollStaffMenu(scanner);
        }

        scanner.close();
    }

    // ==================== EMPLOYEE MENU ====================

    static void employeeMenu(Scanner scanner) {
        int choice = 0;
        do {
            System.out.println("\n1. Enter Employee Number");
            System.out.println("2. Exit");
            System.out.print("Enter your choice: ");
            choice = readInt(scanner);

            if (choice == 1) {
                System.out.print("Enter Employee Number: ");
                int empNo = readInt(scanner);
                int idx = findEmployee(empNo);
                if (idx == -1) {
                    System.out.println("Employee number does not exist.");
                } else {
                    System.out.println("\nEmployee Number  : " + empNumbers[idx]);
                    System.out.println("Employee Name    : " + firstNames[idx] + " " + lastNames[idx]);
                    System.out.println("Birthday         : " + birthdays[idx]);
                }
            }
        } while (choice != 2);
    }

    // ==================== PAYROLL STAFF MENUS ====================

    static void payrollStaffMenu(Scanner scanner) {
        int choice = 0;
        do {
            System.out.println("\n1. Process Payroll");
            System.out.println("2. Exit");
            System.out.print("Enter your choice: ");
            choice = readInt(scanner);

            if (choice == 1) {
                processPayrollMenu(scanner);
            }
        } while (choice != 2);
    }

    static void processPayrollMenu(Scanner scanner) {
        int sub = 0;
        do {
            System.out.println("\n1. One Employee");
            System.out.println("2. All Employees");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            sub = readInt(scanner);

            if (sub == 1) {
                System.out.print("Enter Employee Number: ");
                int empNo = readInt(scanner);
                int idx = findEmployee(empNo);
                if (idx == -1) {
                    System.out.println("Employee number does not exist.");
                } else {
                    displayPayroll(idx);
                }
            } else if (sub == 2) {
                for (int i = 0; i < employeeCount; i++) {
                    displayPayroll(i);
                }
            }
        } while (sub != 3);
    }

    // ==================== PAYROLL DISPLAY ====================
    //
    // For each month (June–December), two cutoff periods are shown:
    //   1st cutoff (days 1–15): no deductions, Net = Gross
    //   2nd cutoff (days 16–end): all government deductions applied
    // Deductions use the combined monthly gross (1st + 2nd) but are
    // subtracted only from the 2nd cutoff gross.

    static void displayPayroll(int idx) {
        System.out.println("\n============================================================");
        System.out.println("Employee Number  : " + empNumbers[idx]);
        System.out.println("Employee Name    : " + firstNames[idx] + " " + lastNames[idx]);
        System.out.println("Birthday         : " + birthdays[idx]);

        for (int month = 6; month <= 12; month++) {
            String name = MONTH_NAMES[month];
            int lastDay = lastDayOfMonth(month);

            double hours1 = getHoursForCutoff(empNumbers[idx], month, 1);
            double gross1 = hours1 * hourlyRates[idx];

            System.out.println("\nCutoff Period    : " + name + " 1 to " + name + " 15");
            System.out.printf("Hours Worked     : %.2f%n", hours1);
            System.out.printf("Gross Salary     : PHP %,.2f%n", gross1);
            System.out.printf("Net Salary       : PHP %,.2f%n", gross1);

            double hours2 = getHoursForCutoff(empNumbers[idx], month, 2);
            double gross2 = hours2 * hourlyRates[idx];

            double monthlyGross = gross1 + gross2;

            double sss  = computeSSS(monthlyGross);
            double phil = computePhilHealth(monthlyGross);
            double pag  = computePagIbig(monthlyGross);

            double taxable = monthlyGross - sss - phil - pag;
            double tax     = computeIncomeTax(taxable);

            double totalDed = sss + phil + pag + tax;
            double net2     = gross2 - totalDed;

            System.out.println("\nCutoff Period    : " + name + " 16 to " + name + " " + lastDay);
            System.out.printf("Hours Worked     : %.2f%n", hours2);
            System.out.printf("Gross Salary     : PHP %,.2f%n", gross2);
            System.out.printf("SSS              : PHP %,.2f%n", sss);
            System.out.printf("PhilHealth       : PHP %,.2f%n", phil);
            System.out.printf("Pag-IBIG         : PHP %,.2f%n", pag);
            System.out.printf("Income Tax       : PHP %,.2f%n", tax);
            System.out.printf("Total Deductions : PHP %,.2f%n", totalDed);
            System.out.printf("Net Salary       : PHP %,.2f%n", net2);
        }

        System.out.println("============================================================");
    }

    // ==================== HOURS WORKED CALCULATION ====================
    //
    // Sums hours for one employee within a specific cutoff period.
    // cutoff=1 → days 1–15, cutoff=2 → days 16+

    static double getHoursForCutoff(int empNumber, int month, int cutoff) {
        double total = 0;
        for (int i = 0; i < attendanceCount; i++) {
            if (attEmpNumbers[i] == empNumber && getMonth(attDates[i]) == month) {
                int day = getDay(attDates[i]);
                boolean inCutoff = (cutoff == 1) ? (day >= 1 && day <= 15) : (day >= 16);
                if (inCutoff) {
                    total += computeHours(attLogIns[i], attLogOuts[i]);
                }
            }
        }
        return total;
    }

    // Computes billable hours for a single attendance entry.
    // Rules: work window 8:00–17:00, grace period ≤8:10 counts as 8:00,
    // lunch 12:00–13:00 excluded, no overtime past 17:00.
    static double computeHours(String logIn, String logOut) {
        int loginMin  = parseTime(logIn);
        int logoutMin = parseTime(logOut);

        int start = (loginMin <= GRACE_LIMIT) ? WORK_START : loginMin;
        int end   = Math.min(logoutMin, WORK_END);

        if (end <= start) return 0.0;

        double minutes = end - start;

        int lunchOverlapStart = Math.max(start, LUNCH_START);
        int lunchOverlapEnd   = Math.min(end, LUNCH_END);
        if (lunchOverlapEnd > lunchOverlapStart) {
            minutes -= (lunchOverlapEnd - lunchOverlapStart);
        }

        return (minutes < 0) ? 0.0 : minutes / 60.0;
    }

    // ==================== GOVERNMENT DEDUCTIONS ====================

    static double computeSSS(double salary) {
        for (int i = 0; i < sssTable.length; i++) {
            if (salary >= sssTable[i][0] && salary <= sssTable[i][1]) {
                return sssTable[i][2];
            }
        }
        return 1125.00;
    }

    static double computePhilHealth(double salary) {
        if (salary <= 10000.00) return 150.00;
        if (salary >= 60000.00) return 900.00;
        return (salary * 0.03) / 2.0;
    }

    static double computePagIbig(double salary) {
        if (salary < 1000.00) return 0.00;
        if (salary <= 1500.00) return salary * 0.01;
        return Math.min(salary * 0.02, 100.00);
    }

    // BIR withholding tax brackets
    static double computeIncomeTax(double taxable) {
        if (taxable <= 20832.00) return 0.00;
        if (taxable <= 33332.00) return (taxable - 20833.00) * 0.20;
        if (taxable <= 66666.00) return 2500.00 + (taxable - 33333.00) * 0.25;
        if (taxable <= 166666.00) return 10833.00 + (taxable - 66667.00) * 0.30;
        if (taxable <= 666666.00) return 40833.33 + (taxable - 166667.00) * 0.32;
        return 200833.33 + (taxable - 666667.00) * 0.35;
    }

    // ==================== CSV DATA LOADING ====================

    static void loadEmployeeData() {
        try (BufferedReader br = new BufferedReader(new FileReader(EMPLOYEE_FILE))) {
            String line;
            boolean headerSkipped = false;

            while ((line = br.readLine()) != null && employeeCount < MAX_EMPLOYEES) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (!headerSkipped) { headerSkipped = true; continue; }

                try {
                    String[] f = parseCsvLine(line, 19);
                    int i = employeeCount;

                    empNumbers[i]            = Integer.parseInt(f[0]);
                    lastNames[i]             = f[1];
                    firstNames[i]            = f[2];
                    birthdays[i]             = f[3];
                    addresses[i]             = f[4];
                    phoneNumbers[i]          = f[5];
                    sssNumbers[i]            = f[6];
                    philhealthNumbers[i]     = f[7];
                    tinNumbers[i]            = f[8];
                    pagibigNumbers[i]        = f[9];
                    empStatuses[i]           = f[10];
                    positions[i]             = f[11];
                    supervisors[i]           = f[12];
                    basicSalaries[i]         = parseMoney(f[13]);
                    riceSubsidies[i]         = parseMoney(f[14]);
                    phoneAllowances[i]       = parseMoney(f[15]);
                    clothingAllowances[i]    = parseMoney(f[16]);
                    grossSemiMonthlyRates[i] = parseMoney(f[17]);
                    hourlyRates[i]           = parseMoney(f[18]);

                    employeeCount++;
                } catch (NumberFormatException e) {
                    System.out.println("Warning: Skipped bad employee row.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading " + EMPLOYEE_FILE + ": " + e.getMessage());
        }
    }

    static void loadAttendanceData() {
        try (BufferedReader br = new BufferedReader(new FileReader(ATTENDANCE_FILE))) {
            String line;
            boolean headerSkipped = false;

            while ((line = br.readLine()) != null && attendanceCount < MAX_ATTENDANCE) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (!headerSkipped) { headerSkipped = true; continue; }

                try {
                    String[] f = parseCsvLine(line, 6);
                    int i = attendanceCount;

                    attEmpNumbers[i] = Integer.parseInt(f[0]);
                    attLastNames[i]  = f[1];
                    attFirstNames[i] = f[2];
                    attDates[i]      = f[3];
                    attLogIns[i]     = f[4];
                    attLogOuts[i]    = f[5];

                    attendanceCount++;
                } catch (NumberFormatException e) {
                    System.out.println("Warning: Skipped bad attendance row.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading " + ATTENDANCE_FILE + ": " + e.getMessage());
        }
    }

    // ==================== CSV / PARSING HELPERS ====================

    // Handles quoted fields containing commas (e.g. addresses like
    // "Valero Carpark Building Valero Street 1227, Makati City")
    static String[] parseCsvLine(String line, int fieldCount) {
        String[] result = new String[fieldCount];
        int idx = 0;
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                if (idx < fieldCount) {
                    result[idx] = sb.toString().trim();
                    idx++;
                    sb.setLength(0);
                }
            } else {
                sb.append(c);
            }
        }

        if (idx < fieldCount) {
            result[idx] = sb.toString().trim();
        }

        for (int i = 0; i < fieldCount; i++) {
            if (result[i] == null) result[i] = "";
        }

        return result;
    }

    // Strips commas from salary strings like "90,000" before parsing
    static double parseMoney(String value) {
        return Double.parseDouble(value.replace(",", ""));
    }

    // Converts "H:mm" time string to minutes since midnight
    static int parseTime(String time) {
        String[] parts = time.split(":");
        return Integer.parseInt(parts[0].trim()) * 60 + Integer.parseInt(parts[1].trim());
    }

    // Date format: MM/dd/yyyy
    static int getMonth(String date) {
        return Integer.parseInt(date.split("/")[0].trim());
    }

    static int getDay(String date) {
        return Integer.parseInt(date.split("/")[1].trim());
    }

    static int findEmployee(int empNo) {
        for (int i = 0; i < employeeCount; i++) {
            if (empNumbers[i] == empNo) return i;
        }
        return -1;
    }

    static int lastDayOfMonth(int month) {
        if (month == 4 || month == 6 || month == 9 || month == 11) return 30;
        if (month == 2) return 29;
        return 31;
    }

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
