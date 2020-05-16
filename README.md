# David Yang . Jarvis Consulting

I am a graduate from the University of Toronto with a degree in Electrical and Computer Engineering. Professionally, I have had the opportunity to take on a few months of software 
work experience writing perl, python & tcl scripts to automate and report data in human readable tables and graphs. With automation, I was able to cut time spent on analyzing 
data by more than half. In addition to work experience, I enjoyed topics related to big data in school - my favorites being deep learning and databases. Outside of work I enjoy 
playing badminton and simulation video games. One game I enjoy in particular is City Skylines as I enjoy creating custom maps as well as testing other people's mods and others 
via Steam workshop. This hobby has instilled in me an appreciation for developers who provide good documentation for others to understand how to apply their work.

## Skills

**Proficient:** Java, Bash, SQL, Agile/Scrum, Git, docker ,SpringBoot, Hadoop, Spark, Scala, Agile/Scrum

**Competent:** Python, MATLAB, Perl, SVN, perforce, Python Tensorflow

**Familiar:**  tcl, Verilog, Synopsys PrimeTime, Synopsys IC Compiler II, NIOS II Assembly

## Development Projects

Project source code: [https://github.com/jarviscanada/jarvis_data_eng_david](https://github.com/jarviscanada/jarvis_data_eng_david)

- **[Cluster Monitor](./linux_sql)**: Developed a tool to monitor cluster resources. The tool manages the connected servers and records the hardware specifications and storage details of each server and stores them in a PostgreSQL database intermittently using Crontab. This project also includes the SQL commands needed to query information from the database - For Example it can group hosts by hardware information, calculate average used memory in percentage over a 5 mins interval for each host, and detect server failure in the cluster.
- **[Core Java Apps](./core_java)**: Composed of 3 projects - a Grep project where I implemented and compared a grep application using loops against a grep application with same functionality using lambda streams. The second project was to learn to use JDBC to allow my Java application to communicate with a PostgreSQL database. The final project was a Twitter project where I used the MVC model to build an application that could write, search and delete Tweets based on ID, which was done via Twitter's REST API platform. 
- **[SpringBoot App](./springboot)**: Developed a docker deployed online trading microservice application. With this application, a trader can create their trading account, find the latest stock prices, create market orders and manage stock positions. Through use of SpringBoot, the microservice can handle dependencies between layers, and gives access to Apache Tomcat webservlet that maps HTTP requests sent by client to matching Controller methods. The SpringBoot application accesses live market data from IEXCloud API & store/retrieve local data from PostgreSQL databases. 
- **[Cloud & DevOps](./cloud_devops)**: In Progress. Currently learning AWS EC2
- **[Hadoop](./hadoop)**: Provisioned a GCP machine cluster to become familiar with the Apache Hadoop Ecosystem. To do this, I worked on business problems that would expose me to Hadoop components such as HDFS(Hadoop Distributed File System), Apache Hive, YARN (Yet Another Resource Negotiator) & MapReduce. HiveQL was the main language I used to execute Hadoop tasks to understand these components and I used Apache Zeppelin notebook to be the interpreter. These queries were operated on a large spreadsheet created by the World Bank.
- **[Spark/Scala](./spark)**: A project to learn and apply the 2 fundamental API's of Spark - RDD's (Resilient Distributed Datasets) and Structured API (which includes DataFrames, DataSets and SparkSQL). I used a Google Cloud provisioned cluster to solve business problems using RDD's, DataFrames and SparkSQL using Scala, while Apache Zeppelin was used as interpreter for this project.

## Professional Experiences

**Junior Software Developer,  Jarvis, Toronto (2019-Present):** Completed projects involving data topics to become a high quality junior engineer. Worked in groups, and made use of the Scrum framework to improve the quality of my projects. For many projects, acted in role of Scrum Master to improve team cohesion and successfully pass team code reviews.

**Physical Design Engineer, AMD, Markham (2017-2018):** Ran through Full Chip Timing Flow to obtain PrimeTime reports of violating Clock Domain Crossing paths within the full chip design. Analyzed the output of these reports to apply Engineering Change Orders (ECOs) to fix timing violations. In addition to this, I wrote perl/python/tcl scripts to help automate analysis, which significantly reduced the time needed analyze and write ECO's to correct these timing violations.

## Education & Academic Projects

**University of Toronto (2014-2019)**, Bachelor of Applied Science(B.A.Sc.) Computer Engineering

- **Hardware Netlist Diagnosis and Correction Tool (2019):** Created a flow of python scripts that could read a faulty netlist together with the RTL containing the fix, and apply the necessary corrections to the faulty netlist. Furthermore, integrated the scripts with Synopsys Design Compiler II, Yosys generate logic and Yosys SAT solver to apply fix to the faulty netlist.

- **Interactive Map of Toronto Project (2016):** Worked in group of 3 using SVN to write a program in C++ to create an interactive map of Toronto. We applied Big-O notation to pick out data structures and algorithms that minimized program running time, while maintaining the quality of the solution.

- **Verilog Game Design Project (2015):** Worked in pair to create a fun hardware-based game called HyperJump on an Altera DE1-SoC- FGPA programmed with Verilog. The game had smooth flow, multiple different levels and a scoring system. To do this, we defined a Finite State machine to apply logic that drew game graphics, including moving obstacles and a character required to jump to avoid such obstacles, on a VGA monitor based on the player's input.