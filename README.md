## Project Overview:
- This is a library management system implemented in java as DBMS (Database managment system) and database as MYSQL.
- The connection between java and MYSQL is using JDBC environment
- The project implements docker for containerization and deploying the application in an isolated setted environment

Tech Stack used :
- Language: Java
- Database: MySQL
- Database Connectivity: JDBC
- Build Tool: (Maven), i personally used netbeans to handle the maven environment to build java.jar that is used in the docker image
- Environment: 
   - Can run locally (the main branch)
   - inside Docker containers (the docker branch)


Data Storage Options:

The project database depends on 2 different setup of the data storage 
- Database storage using MYSQL
- File based storage , using outputStream and inputStream using the concept of serialization

File storage concept :
It reads from 2 .dat files (users.dat and books.dat)
- FOR Local deployment (main branch) -> These concepts are applied based on a config.properties file which is read at the very start of the application 
- FOR docker deployment -> The choice is taken from .env file however , the files.dat arenot uploaded to the container , either they are uploaded manually 
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
- Creating .env file `see Environment Variables below`

##  Setup Instructions

To build and start the application using Docker and Docker Compose:

```bash
docker-compose build
docker-compose up -d mysql_db adminer
docker-compose run --rm -it app

```
- This will build all containers
- start adminer and mysql in the background "detached mode"
- run app container , the main app , in an interactive user mode in the terminal
- --rm is to delete the container instance once it stopped ,preferred for clean closeup
- `see Access Instructions Below` 

## Environment Variables
- A file must be created before building anything to set uo the environment variables the project needed , otherwise it will give an error.

| Variable       | Description                                                     |
| -------------- | ----------------------------------------------------------------|
| `DB_HOST`      | MySQL container hostname (typically `db`)                       |
| `DB_PORT`      | MySQL exposed port (default: `3306`)                            |
| `DB_NAME`      | Name of the database used by the application                    |
| `DB_USER`      | Username for MySQL connection                                   |
| `DB_PASSWORD`  | Password for the database user                          	   |
| `STORAGE_MODE` | Mode of data storage: `database` for MySQL or `file` for .dat   | 
| `USERS_FILE`   | Users filename, To save serialization into if `file` was chosen |
| `BOOKS_FILE`   | Books filename, To save serialization into if `file` was chosen |


## Access Instrusctions:
- Once containers are built , adminer is accessed using `localhost:8080` from any browser
- when `localhost:8080` starts , it will display window to enter MYSQL login requirements
  > log the same info from the .env file to login successfully
- If the login was for the first time , create a database
  1) with the same database name in the .env
  2) create all the tables in the schema above
  3) insert one user to be the first in the database , which has the access to register users later (other admins or regular users)
     otherwise , when app container runs , the application will not be able to login as admins or regular users as the project contain authentican required of the "user id and name "
     - Insertion example : `insert into Users(id,name,role) Values ("admin1","admin",'admin')`

## Troubleshooting Tips:

- To see specfic container problem
  `docker-compose logs` or `docker-compose <container nane>`
  - it will show the logs and problems occured if exits , container is either adminer , mysql_db , app
  `docker-compose ps`
  - this must show both adminer and mysql_db containers are running , if not check problems and solutions below

- Database not connecting?
   1) Ensure db is the correct hostname (Docker Compose sets this internally).
   2) Check that your .env variables match the Docker Compose configuration.
   3) Confirm that STORAGE_MODE is set to database.

- File-based storage not working?
  1) Verify that .dat files (users.dat, books.dat) are mounted or accessible inside the container.
  2) Confirm that STORAGE_MODE is set to file.

- Ports already in use?
  1) Make sure nothing else is running on ports 3306 (MySQL) or 8080 (Adminer).
   `netstat -aon | findstr : 3306
    netstat -aon | findstr : 8080
   ` 

- Changes not applying?
  1) Rebuild containers to reflect changes
`	docker-compose down
	docker-compose build
`

- if app container not working correctly
  1) try rebuilding the jar file of the java project and ensure the path is quivalent to the one in the docker file
  2) rename the jar file or the docker path if neccessary
     build using
     `mvn clean package`
     if not working correctly , check
     `mvn -v`
     if not maven version exists , install it first
     `sudo apt install maven`
 
