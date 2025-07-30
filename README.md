* Project Overview *
This is a library management system implemented in java as DBMS (Database managment system) and database as MYSQL.
The connection between java and MYSQL is using JDBC environment
Also the project implements docker for containerization and deploying the application in an isolated setted environment

Tech Stack used
Language: Java
Database: MySQL
Database Connectivity: JDBC
Build Tool: (Maven), i personally used netbeans to handle the maven environment to build java.jar that is used in the docker image
Environment: 
  - Can run locally (the main branch)
  - inside Docker containers (the docker branch)

The project database depends on 3 different setup of the data storage either database storage using MYSQL or using file storage 
the file storage uses outputStream and inputStream using the concept of serialization

File storage concept :
It reads from 2 .dat files (users.dat and books.dat)
FOR Local deployment (main branch) , These concepts are applied based on a config.properties file which is read at the very start of the application 
FOR docker deployment , The choice is taken from .env file however , the files.dat arenot uploaded to the container , either they are uploaded manually 
or they are saved in the container during runtime
