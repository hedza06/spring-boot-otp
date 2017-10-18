# Spring Boot Starter Project

This is a starter project for Spring Boot Developers. It contains technologies as Spring Boot, Hibernate,
Liquibase, JPA and Spring Security with JWT Token Authorization.


## Running the project

1. Create database with name **test**
2. Open terminal and navigate to your project
3. Type command **mvn install**
4. Type command **mvn spring-boot:run**


## Check application running state

Route: **http://localhost:8080/**


## Available profiles

- Development profile (dev)
- Production profile (prod)


## Authentication

Route: **/auth/authenticate**  
Method: **POST**  
Content-Type: **application/json**  
Request payload: `{ username: "admin", password: "admin" }`  
Response: `{ id_token: "token_hash" }`


## Packaging for production

1. **mvn clean**  
2. **mvn -Pprod package**


## Author

Heril Muratović  
Software Engineer  
<br>
**Mobile**: +38269657962  
**E-mail**: hedzaprog@gmail.com  
**Skype**: hedza06  
**Twitter**: hedzakirk  
**LinkedIn**: https://www.linkedin.com/in/heril-muratovi%C4%87-021097132/  
**StackOverflow**: https://stackoverflow.com/users/4078505/heril-muratovic