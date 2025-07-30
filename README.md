## Project Overview:
- This is a library management system implemented in java as DBMS (Database managment system) and database as MYSQL.
- The connection between java and MYSQL is using JDBC environment
- The project implements docker for containerization and deploying the application in an isolated setted environment

Tech Stack used :
Language: Java
Database: MySQL
Database Connectivity: JDBC
Build Tool: (Maven), i personally used netbeans to handle the maven environment to build java.jar that is used in the docker image
Environment: 
  - Can run locally (the main branch)
  - inside Docker containers (the docker branch)


Data Storage Options:

The project database depends on 2 different setup of the data storage 
- Database storage using MYSQL
- File based storage , using outputStream and inputStream using the concept of serialization

File storage concept :
It reads from 2 .dat files (users.dat and books.dat)
FOR Local deployment (main branch) -> These concepts are applied based on a config.properties file which is read at the very start of the application 
FOR docker deployment -> The choice is taken from .env file however , the files.dat arenot uploaded to the container , either they are uploaded manually 
or they are saved in the container during runtime

Database Storage concept :
The project reads from MySQL version 8 database.
* The schema used :
```
        create table Books (id varchar(100) primary Key,
      				title varchar(100) unique not null,
                         author varchar(100) not null,
                         genre varchar(100) ,
                         avaliableCopies int not null default 1
      					);
       create table Users(id varchar(100) primary Key,
      					name varchar(100) not null,
      					role ENUM ('admin','regUser') not null
      				);
       create table BorrowedBooks(userId varchar(100) ,
              bookId varchar(100),
              primary key (userId,bookId),
							FOREIGN KEY (userId) REFERENCES Users(id)
							ON DELETE RESTRICT
							ON UPDATE CASCADE,
							FOREIGN KEY (bookId) REFERENCES Books(id)
							ON DELETE RESTRICT
							ON UPDATE CASCADE
							);
```
Java uses JDBC to interact with MYSQL using SQL
it takes the MYSQL login info (host ,port , username,password)
  - From config file (main branch version)
  - From .env file (docker branch version)


## Prerequisites

Before running the application in a Dockerized setup, ensure the following are installed:

- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)

##  Setup Instructions

To build and start the application using Docker and Docker Compose:

```bash
docker-compose build
docker-compose up -d mysql_db adminer
docker-compose run --rm -it app

```


