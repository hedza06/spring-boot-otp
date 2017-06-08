# Spring Boot Starter Project

This project is a starter project for Spring Boot Developers. It contains technologies as Spring Boot, Hibernate,
Liquibase, JPA and JWT Token Authorization.

## Running the project

1. Create database with name **test**
2. Open terminal and navigate to your project
3. Type command **mvn**

## Authentication route

Link: http://localhost:8080/auth/authenticate <br />
Method: __POST__ <br />
Data: ```{ username: "admin", password: "admin" }```

## Check application running state

Link: http://localhost:8080/

## Packaging

Command: ```mvn -P{profile} package```

{profile}: <br><br>
    - dev <br>
    - prod


_Written by Heril Muratović._