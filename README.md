# MotorPH Basic Payroll System (Phase 1)

## Project Overview

The **MotorPH Basic Payroll System** is a console-based Java application developed as part of the Phase 1 milestone requirement. It reads employee records and attendance data from CSV files, then computes payroll values including gross salary, government-mandated deductions, and net salary for each cutoff period.

---

## Program Details

The system begins with a **login screen** that determines the user's role. Depending on the credentials entered, the user is directed to either the Employee menu or the Payroll Staff menu.

### Employee Login

When logged in as an employee, the user can look up their personal information by entering their employee number. The system searches the employee CSV file and displays:

- Employee Number
- Full Name
- Birthday

If the employee number is not found in the records, the system displays an appropriate message.

### Payroll Staff Login

When logged in as payroll staff, the user can process payroll through two options:

**Process One Employee** -- The staff enters a specific employee number. The system retrieves that employee's details and attendance records, then computes payroll for each month from June to December.

**Process All Employees** -- The system reads through the entire employee CSV file and automatically processes payroll for every employee in the database.

### Payroll Computation

For each employee, payroll is computed per **semi-monthly cutoff**:

- **First Cutoff (Day 1-15):** Hours worked are totaled and multiplied by the hourly rate to produce the gross salary. No deductions are applied during this cutoff.
- **Second Cutoff (Day 16-end):** Hours worked are totaled and the gross salary is calculated. Government deductions are then applied:
  - **SSS** -- Based on the SSS contribution table
  - **PhilHealth** -- 3% of salary (split between employee and employer), with a floor of 10,000 and ceiling of 60,000
  - **Pag-IBIG** -- 1% if salary is between 1,000-1,500, otherwise 2%, capped at 100
  - **Withholding Tax** -- Computed from taxable income (gross minus SSS, PhilHealth, and Pag-IBIG)

The **net salary** is the gross salary minus all deductions.

### Hours Worked Calculation

Daily hours worked are calculated by converting the time-in and time-out values from the attendance CSV into decimal hours and computing the difference.

---

## Technologies Used

| Component       | Details                          |
|-----------------|----------------------------------|
| Language        | Java                             |
| Runtime         | Java 21.0.10 2026-01-20 LTS     |
| JVM             | Java HotSpot(TM) 64-Bit Server  |
| Data Source     | CSV files (employees, attendance)|
| Version Control | Git & GitHub                     |

---

## Team Details

### ByteBeans Development Team

| Team Member              | Contribution |
|--------------------------|--------------|
| **Expeditus Yntig**      | *(fill in)*  |
| **Rey Manuel Oljol**     | *(fill in)*  |
| **Angel Rhyne Hangad**   | *(fill in)*  |
| **Jeana Karyll Esteron** | *(fill in)*  |

---

## Project Plan Link

[View Project Plan on Google Sheets](https://docs.google.com/spreadsheets/d/1FF1jRVCnI0Zv32z_0VOuM7BB6sGdVNDUTtjyJr_Z7lg/edit?gid=2134013708#gid=2134013708)

---

## How to Run

1. Clone the repository
2. Open the project in your IDE (IntelliJ IDEA or NetBeans)
3. Make sure `employees.csv` and `attendance.csv` are in the project root directory
4. Run `motorph.Main`

### Login Credentials

| Role           | Username         | Password |
|----------------|------------------|----------|
| Employee       | `employee`       | `12345`  |
| Payroll Staff  | `payroll_staff`  | `12345`  |
