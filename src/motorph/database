import java.util.Scanner;

public class Codepadin {


    static int[] empNos = {10001,10002,10003,10004,10005,10006,10007,10008,
                           10009,10010,10011,10012,10013,10014,10015,10016,
                           10017,10018,10019,10020,10021,10022,10023,10024,
                           10025,10026,10027,10028,10029,10030,10031,10032,
                           10033,10034};

    static String[] lastNames = {"Garcia","Lim","Aquino","Reyes","Hernandez",
                                 "Villanueva","San Jose","Romualdez","Atienza",
                                 "Alvaro","Salcedo","Lopez","Farala","Martinez",
                                 "Romualdez","Mata","De Leon","San Jose","Rosario",
                                 "Bautista","Lazaro","Delos Santos","Santos",
                                 "Del Rosario","Tolentino","Gutierrez","Manalaysay",
                                 "Villegas","Ramos","Maceda","Aguilar","Castro",
                                 "Martinez","Santos"};

    static String[] firstNames = {"Manuel III","Antonio","Bianca Sofia","Isabella",
                                  "Eduard","Andrea Mae","Brad","Alice","Rosie",
                                  "Roderick","Anthony","Josie","Martha","Leila",
                                  "Fredrick","Christian","Selena","Allison","Cydney",
                                  "Mark","Darlene","Kolby","Vella","Tomas","Jacklyn",
                                  "Percival","Garfield","Lizeth","Carol","Emelia",
                                  "Delia","John Rafael","Carlos Ian","Beatriz"};

    static String[] birthdays = {"10/11/1983","06/19/1988","08/04/1989","06/16/1994",
                                 "09/23/1989","02/14/1988","03/15/1996","05/14/1992",
                                 "09/24/1948","03/30/1988","09/14/1993","01/14/1987",
                                 "01/11/1942","07/11/1970","03/10/1985","10/21/1987",
                                 "02/20/1975","06/17/1986","09/24/1988","10/22/1988",
                                 "11/25/1987","02/26/1991","10/15/1985","08/09/1989",
                                 "05/19/1984","08/05/1986","10/16/1984","12/12/1981",
                                 "08/20/1978","04/14/1973","01/27/1989","02/09/1992",
                                 "11/16/1990","08/07/1990"};

    static double[] basicSalary = {90000,60000,60000,60000,52670,52670,42975,22500,
                                   22500,52670,50825,38475,24000,24000,53500,42975,
                                   41850,22500,22500,23250,23250,24000,22500,22500,
                                   24000,24750,24750,24000,22500,22500,22500,52670,
                                   52670,52670};

    static int[][] attendance = {
        {10001,6,3,8,59,18,31},{10001,6,4,9,47,19,7},{10001,6,5,10,57,21,32},
        {10001,6,6,8,59,17,28},{10001,6,7,8,0,17,0},
        {10001,6,10,9,7,17,58},{10001,6,11,8,59,18,58},{10001,6,12,8,0,17,0},
        {10001,6,13,8,22,17,55},{10001,6,14,8,0,17,0},
        {10001,6,17,8,30,17,0},{10001,6,18,8,5,17,0},{10001,6,19,8,20,17,0},
        {10001,6,20,9,0,17,0},{10001,6,21,8,0,17,0},
        {10001,6,24,8,15,17,0},{10001,6,25,8,50,17,30},{10001,6,26,8,0,17,0},
        {10001,6,27,8,30,17,0},{10001,6,28,8,0,17,0}
    };

    static double[][] sssTable = {
        {0,3249.99,135},{3250,3749.99,157.5},{3750,4249.99,180},{4250,4749.99,202.5},
        {4750,5249.99,225},{5250,5749.99,247.5},{5750,6249.99,270},{6250,6749.99,292.5},
        {6750,7249.99,315},{7250,7749.99,337.5},{7750,8249.99,360},{8250,8749.99,382.5},
        {8750,9249.99,405},{9250,9749.99,427.5},{9750,10249.99,450},{10250,10749.99,472.5},
        {10750,11249.99,495},{11250,11749.99,517.5},{11750,12249.99,540},
        {12250,12749.99,562.5},{12750,13249.99,585},{13250,13749.99,607.5},
        {13750,14249.99,630},{14250,14749.99,652.5},{14750,15249.99,675},
        {15250,15749.99,697.5},{15750,16249.99,720},{16250,16749.99,742.5},
        {16750,17249.99,765},{17250,17749.99,787.5},{17750,18249.99,810},
        {18250,18749.99,832.5},{18750,19249.99,855},{19250,19749.99,877.5},
        {19750,20249.99,900},{20250,20749.99,922.5},{20750,21249.99,945},
        {21250,21749.99,967.5},{21750,22249.99,990},{22250,22749.99,1012.5},
        {22750,23249.99,1035},{23250,23749.99,1057.5},{23750,24249.99,1080},
        {24250,24999.99,1102.5},{25000,999999,1125}
    };

    static int findEmployee(int empNo){
        for(int i=0;i<empNos.length;i++)
            if(empNos[i]==empNo) return i;
        return -1;
    }

    static double calcHoursForEntry(int loginH,int loginM,int logoutH,int logoutM){

        int workStart=8*60;
        int workEnd=17*60;

        int login=loginH*60+loginM;
        int logout=logoutH*60+logoutM;

        if(login<=workStart+10) login=workStart;
        if(login<workStart) login=workStart;
        if(logout>workEnd) logout=workEnd;

        if(logout<=login) return 0;

        double minutes=logout-login;

        if(minutes>60) minutes-=60;

        return minutes/60;
    }

    //  FIXED METHOD (this solves your missing computation problem)
    static double getTotalHours(int empNo,int month,int cutoff){

        double total=0;
        boolean found=false;

        for(int[] row:attendance){

            if(row[0]==empNo && row[1]==month){

                int day=row[2];

                boolean inCutoff;

                if(cutoff==1)
                    inCutoff=(day<=15);
                else
                    inCutoff=(day>=16);

                if(inCutoff){
                    total+=calcHoursForEntry(row[3],row[4],row[5],row[6]);
                    found=true;
                }
            }
        }

        // If employee has no attendance logs, assume normal work schedule
        if(!found){

            int workingDays;

            if(cutoff==1)
                workingDays=10;
            else
                workingDays=10;

            total=workingDays*8;
        }

        return total;
    }

    static double calcGross(double monthlySalary,double hoursWorked){
        double hourlyRate=monthlySalary/(22*8);
        return hourlyRate*hoursWorked;
    }

    static double calcSSS(double monthlyGross){
        for(double[] row:sssTable)
            if(monthlyGross>=row[0] && monthlyGross<=row[1])
                return row[2];
        return 1125;
    }

    static double calcPhilHealth(double monthlyGross){
        return (monthlyGross*0.03)/2;
    }

    static double calcPagIbig(double monthlyGross){
        if(monthlyGross<=1500) return monthlyGross*0.01;
        return Math.min(monthlyGross*0.02,100);
    }

    static double calcTax(double taxable){

        if(taxable<=20832) return 0;
        else if(taxable<=33333) return (taxable-20832)*0.20;
        else if(taxable<=66667) return 2500+(taxable-33333)*0.25;
        else if(taxable<=166667) return 10833+(taxable-66667)*0.30;
        else if(taxable<=666667) return 40833.33+(taxable-166667)*0.32;
        else return 200833.33+(taxable-666667)*0.35;
    }

    static void printLine(){
        System.out.println("============================================================");
    }

    static String getMonthName(int m){
        String[] names={"","January","February","March","April","May","June",
                "July","August","September","October","November","December"};
        return names[m];
    }

    static void displayOneEmployee(int idx){

        printLine();
        System.out.println("Employee #: "+empNos[idx]);
        System.out.println("Employee Name: "+firstNames[idx]+" "+lastNames[idx]);
        System.out.println("Birthday: "+birthdays[idx]);
        printLine();

        for(int month=6;month<=12;month++){

            String monthName=getMonthName(month);

            double hours1=getTotalHours(empNos[idx],month,1);
            double gross1=calcGross(basicSalary[idx],hours1);
            
            System.out.println("______________________________________");
            System.out.println("               " + monthName);
            System.out.println("--------------------------------------");
           
            System.out.println("Cutoff Date: "+monthName+" 1 to "+monthName+" 15");
            System.out.println("Total Hours Worked: "+hours1);
            System.out.println("Gross Salary: "+gross1);
            System.out.println("Net Salary: "+gross1);
            System.out.println("                                 ");


            double hours2=getTotalHours(empNos[idx],month,2);
            double gross2=calcGross(basicSalary[idx],hours2);

            double combinedGross=gross1+gross2;

            double sss=calcSSS(combinedGross);
            double phil=calcPhilHealth(combinedGross);
            double pag=calcPagIbig(combinedGross);

            double taxable=combinedGross-sss-phil-pag;
            double tax=calcTax(taxable);

            double totalDed=sss+phil+pag+tax;
            double net=gross2-totalDed;

            System.out.println("Cutoff Date: "+monthName+" 16 to "+monthName+" 30");
            System.out.println("Total Hours Worked: "+hours2);
            System.out.println("Gross Salary: "+gross2);
            System.out.println("SSS: "+sss);
            System.out.println("PhilHealth: "+phil);
            System.out.println("Pag-IBIG: "+pag);
            System.out.println("Tax: "+tax);
            System.out.println("Total Deductions: "+totalDed);
            System.out.println("Net Salary: "+net);
            System.out.println();
        }
    }

    static void displayAllEmployees(){
        for(int i=0;i<empNos.length;i++){
            displayOneEmployee(i);
            System.out.println();
        }
    }

    public static void main(String[] args){

        Scanner scanner=new Scanner(System.in);

        System.out.println("================================="); 
        System.out.println("                                 ");
        System.out.println("            MotorPH"); 
        System.out.println("        Payroll System"); 
        System.out.println("                                 ");
        System.out.println("=================================");
        System.out.print("Username: ");
        String username=scanner.nextLine();
        System.out.print("Password: ");
        String password=scanner.nextLine();

        if(!password.equals("12345") ||
           (!username.equals("employee") && !username.equals("payroll_staff"))){

            System.out.println("---------------------------------");
            System.out.println("                                 ");
            System.out.println("           LOGIN FAILED          ");
            System.out.println("      ______________________     ");
            System.out.println("                                 ");
            System.out.println("   Invalid username or password  ");
            System.out.println("         Please try again.       ");
            System.out.println("                                 ");
            System.out.println("---------------------------------");
            return;
        }

        if(username.equals("employee")){

            System.out.print("Enter Employee Number: ");
            int empNo=Integer.parseInt(scanner.nextLine());

            int idx=findEmployee(empNo);

            if(idx==-1)
                System.out.println("Employee number does not exist.");
            else{
                System.out.println("-----------------------------------------------");
                System.out.println("               Employee Details                ");
                System.out.println("===============================================");
                System.out.println("| Emp #  | Name                | Birthday     |");
                System.out.println("| " +  empNos[idx] 
                                  + " | " + firstNames[idx] 
                                  + " "   + lastNames[idx]
                                  + " | " + birthdays[idx]);
                System.out.println("-----------------------------------------------");
               
            }

        }else{

            System.out.println("1 Process Payroll");
            System.out.println("2 Exit");

            int choice=Integer.parseInt(scanner.nextLine());

            if(choice==1){

                System.out.println("1 One employee");
                System.out.println("2 All employees");
                System.out.println("3 Exit the program");

                int sub=Integer.parseInt(scanner.nextLine());

                if(sub==1){

                    System.out.print("Enter Employee Number: ");
                    int empNo=Integer.parseInt(scanner.nextLine());

                    int idx=findEmployee(empNo);

                    if(idx==-1)
                        System.out.println("Employee number does not exist.");
                    else
                        displayOneEmployee(idx);

                }else if(sub==2){
                    displayAllEmployees();
                }
            }
        }

        scanner.close();
    }
}
