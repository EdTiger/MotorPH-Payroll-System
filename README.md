<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Basic Payroll System (Phase 1)</title>

<link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700&display=swap" rel="stylesheet">

<style>

body{
    font-family: "Inter", sans-serif;
    background:#f5f7fb;
    color:#2d2d2d;
    line-height:1.6;
    margin:0;
    padding:0;
}

/* Container */
.container{
    max-width:900px;
    margin:60px auto;
    background:white;
    padding:40px;
    border-radius:12px;
    box-shadow:0 6px 20px rgba(0,0,0,0.06);
}

/* Title */
.title{
    text-align:center;
    margin-bottom:30px;
}

.title h1{
    font-size:38px;
    color:#1f3c88;
    margin:0;
}

/* Headings */
h2{
    font-size:24px;
    margin-top:35px;
    border-left:5px solid #4a6cf7;
    padding-left:12px;
}

/* Lists */
ul{
    padding-left:20px;
}

li{
    margin:8px 0;
}

/* Table */
table{
    width:100%;
    border-collapse:collapse;
    margin-top:15px;
    border-radius:8px;
    overflow:hidden;
}

th{
    background:#4a6cf7;
    color:white;
    padding:12px;
    text-align:left;
}

td{
    padding:12px;
    border-bottom:1px solid #eee;
}

tr:hover{
    background:#f2f6ff;
}

/* Links */
a{
    color:#4a6cf7;
    text-decoration:none;
    font-weight:500;
}

a:hover{
    text-decoration:underline;
}

/* Footer */
.footer{
    margin-top:40px;
    font-weight:600;
    color:#1f3c88;
}

</style>
</head>

<body>

<div class="container">

<div class="title">
<h1>Basic Payroll System (Phase 1)</h1>
</div>

<h2>Project Overview</h2>
<p>
This repository contains the Phase 1 implementation of the MotorPH Basic Payroll System.
The initial requirement focuses on the presentation of employee details and the automatic
calculation of salaries through code, utilizing the number of hours worked and basic deductions.
</p>

<h2>Phase 1 Tasks and Objectives</h2>

<ul>
<li><b>Employee Information:</b> Present employee details in the prescribed format, specifically the employee number, name, and birthday.</li>

<li><b>Hours Worked Calculation:</b> Calculate the total weekly hours worked by an employee.</li>

<li><b>Gross Salary Calculation:</b> Compute the gross weekly salary based on hours worked.</li>

<li><b>Net Salary Calculation:</b> Compute the net weekly salary after applying generic deductions.</li>
</ul>

<h2>Development Environment</h2>

<table>
<tr>
<th>Component</th>
<th>Specification</th>
</tr>

<tr>
<td><b>Language</b></td>
<td>Java</td>
</tr>

<tr>
<td><b>Runtime</b></td>
<td>java 21.0.10 2026-01-20 LTS</td>
</tr>

<tr>
<td><b>JVM</b></td>
<td>Java HotSpot(TM) 64-Bit Server VM (build 21.0.10+8-LTS-217, mixed mode, sharing)</td>
</tr>

<tr>
<td><b>Compiler</b></td>
<td>javac 21.0.10</td>
</tr>
</table>

<h2>Documentation</h2>

<p>
<b>Project Management:</b>
<a href="https://docs.google.com/spreadsheets/d/1FF1jRVCnI0Zv32z_0VOuM7BB6sGdVNDUTtjyJr_Z7lg/edit?gid=2134013708#gid=2134013708" target="_blank">
View Project Plan on Google Sheets
</a>
</p>

<h2>Development Team</h2>

<p class="footer">
This phase of the MotorPH Basic Payroll System was developed by <b>ByteBeans</b>.
</p>

</div>

</body>
</html>
