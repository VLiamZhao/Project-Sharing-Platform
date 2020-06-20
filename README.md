# Project Shared Platform by Can Zhao
## Description 
The goal of this product is to help students (junior job seekers) to join a good fit industry project and to accumulate 
industry experience. Also, in the meantime, a small size company (typically startup company) could find the engineers 
to complete their project with minimal cost (the cost: mentorship from industry).
## Use Case & Scenario
1. Users can register on the website
2. Users can choose to register a student account or a company account
3. Users can log in by their username and password
4. Company users can upload projects after admin approves it, it will appear on the website
5. Company users can check students who have chosen their projects and their information, but they cannot view the information of all student users.
6. Company users can issue recommendation letters for students
7. Student users can upload their resumes(docx.doc, pdf, jpg)
8. Student users can view project lists of all companies
9. Student users can apply for a project (Limitation: most 3 projects at the same time)
10. Student users can view the information of all company users
11. Student users can rate 1 - 5 stars for companies
## Architecture Design   
![Image of signUp](https://github.com/VLiamZhao/Car-Maintenance-Service/blob/master/web/src/main/resources/car-test.png?raw=true)
1. scalability: Service can scale up/down on demand (cloud deployment)
2. latency: 1s
3. MVP (V0):
4. Tech stacks:
    1. Front-end: React, Ant-design, Axios, JWT parser, React-Router
    2. Back-end: Spring Boot, Hibernate, JWT Token stateless authentication, Restful API, Docker, JUnit
    3. Deployment: Azure Web Service: Blob File Storage, PostgreSQL Database,  Virtual Machine, Nginx
## Assumption
1. user <10K
2. MVP: front end, login, students, companies, DB & Storage

## Database Design
![Image of signUp](https://github.com/VLiamZhao/Car-Maintenance-Service/blob/master/web/src/main/resources/car-test.png?raw=true)
1. Object: User, Resume, Project, RecommendationLetter, StudentProject, RateStar, UserInfo, Role
* Project Approach:

    1. Create Models.
    2. Used Hibernate to do the database schema migration.
    3. Used JDBC to connect project with Postgres.
    4. Configured Spring Security for Authentication.
    5. Created repository, service and did test.
    6. Created Controllers and Restful APIs.
    7. Used mockito library to test AWS S3 Storage service.
    8. Integrated third-party application AWS SQS and tested by Mockito.
 
    

## Build Project
1. Clone the project.
```
git clone https://github.com/VLiamZhao/Car-Maintenance-Service.git
```
2. Install Docker if necessary.
3. Use command window to spin up the PostgreSQL database server using Postgres docker image.
```
docker pull postgres

docker run --name ${PostgresContainerName} -e POSTGRES_USER=${username} -e POSTGRES_PASSWORD=${password} -e POSTGRES_DB=${databaseName} -p ${hostport}:${containerport} -d postgres
```
4. Create Unit database on PGAdmin for unit testing
```
create database Maintenance_Service;
```
5. Environment properties configuration
```
location:./src/main/resources/META-INF/env
   
Template:
database.driver=${driver}
database.url=${url}
database.port=${port}
database.name=${name}
database.username=${username}
database.password=${password}
   
mvn compile -Dspring.profiles.active=${env}
```
6. Schema Flyway migration for creating tables in database
```
mvn compile flyway:migrate -P unit -Ddb_username=${username} -Ddb_url=localhost:${containerport}/${databasename} -Ddb_password=${password} 
```

## Test
- Package and install the folder before unit test 
```
mvn clean compile install -DskipTests=true
```
-Run the test with the command. All the Test are done using JUnit and Mockito
```
mvn compile test -Dspring.profiles.active=${unit} -Daws.region=${region} -Ddb_url=${localhost:5432/pigge_unit} -Ddb.username=${username} -Ddb.password=${password} 
```

## Packaging
```
mvn clean compile package -DskipTests=true
```
## API guideline and Reference DEMO
- You need to sign up for authority to get access.<br />
Make a get request in this address to create a new account.<br />
     
```
GET - http://localhost:8080/auth/registration
```
Put the request body.
```
{
	"name": "Test",
	"email": "test@gmail.com",
	"password": "123"
}

``` 
Then you can get the response like:
```
{
    "Email": "test@gmail.com",
    "Id": "15",
    "Name": "Test"
}

```
DEMO screen shoot:
![Image of signUp](https://github.com/VLiamZhao/Car-Maintenance-Service/blob/master/web/src/main/resources/car-test.png?raw=true)
- You need to login.<br />
Make a post request in this address.
```
POST - http://localhost:8080/auth
```
Put the request body.（ You can chose login with username or email)
```
{
    "email": "test@gmail.com",
    "password": "123"
}
``` 
Then you can get the response like:
```
{
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxNSIsInN1YiI6IlRlc3QiLCJpYXQiOjE1OTI2NDM4NzAsImlzcyI6Im9yZy5teW5vZGUiLCJleHAiOjE1OTI3MzAyNzAsImFsbG93ZWRSZXNvdXJjZSI6Ii9jYXIsIC9tYWludGVuYW5jZSIsImFsbG93ZWRSZWFkUmVzb3VyY2VzIjoiL2NhciwgL21haW50ZW5hbmNlIiwiYWxsb3dlZENyZWF0ZVJlc291cmNlcyI6IiIsImFsbG93ZWRVcGRhdGVSZXNvdXJjZXMiOiIiLCJhbGxvd2VkRGVsZXRlUmVzb3VyY2VzIjoiIn0.Ky6EX7h4gz9yw1DdJhIqijFKlyACzJCJvhgcfkhvjYU"    
}
```
This token is need for the future access other api. So you don't need to login to the every API.
# CI/CD

You should have completed the following stages before you work with DevOps engineer.

  1. Upload source code to GitHub repository
  2. Fulfill unit test stage in docker container
  3. Package **.war** file in docker container
  4. Build Docker image with **.war** file and Dockerfile
  5. Launch containerized application successfully

## GitHub

Make sure the source code in the github is the latest(runnable) version.   

***IMPORTANT: DO NOT INCLUDE ANY CREDENTIAL IN THE CODE.***

## Unit Test
>Use `Docker` to pull `Maven` image and run an interactive container.
>
    docker pull maven:3.6.0-jdk-8
    docker run -it maven:3.6.0-jdk-8 /bin/bash

>Use `Git` to retrieve source code from `GitHub`.
>
    git clone <repository_url>
    
>Get into the project's folder, then use `Flyway` to migrate data.
>
    mvn clean compile flyway:migrate -Ddatabase.url=jdbc:postgresql://${database_url}:5432/${database_name} 
    -Ddatabase.user=${user_name} -Ddatabase.password=${password}
    
Notice: Cause we are currently run in the container. Thus, the database connection is no longer localhost:5432.
You should inspect `postgreSQL` server container to find the IP address. Find the internal IP address of the container by using:
    
    docker inspect ${container_id} | grep "IPAddress"
    
>Run unit tests in the container.
>
    mvn test -Ddatabase.url=jdbc:postgresql://${database_url}:5432/${database_name} -Dspring.profiles.active=unit -Ddatabase.user=${user_name} 
    -Ddatabase.password=${password} -Daws.accessKeyId=${access_key} -Daws.secretKey=${secret_key} 
    -Ddatabase.dialect=org.hibernate.dialect.PostgreSQL9Dialect -Ddatabase.driver=org.postgresql.Driver
