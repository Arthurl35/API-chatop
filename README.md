# API-chatop

Api-chatop is the backend of a platform that connects renters and owners for seasonal rentals.

## Technologies

- Java 17
- Spring 
- Maven
- Mysql

## Installation

Prerequisites:
- Install the MySQL database using the SQL script located in `/main/ressources/script.sql`. Be sure to name your database "rentals".
- Create an S3 bucket named 'api-chatop' via the AWS interface:

Then:
- Git clone:
https://github.com/Arthurl35/API-chatop.git
- Install the dependencies in your projet /Api-chatop.
  `mvn install`
- Replace the files "example_aws.properties" and "example_database.properties" with "aws.properties" and "database.properties" using your own values.
- After launching the api, you can use the front-end application located here: https://github.com/OpenClassrooms-Student-Center/Developpez-le-back-end-en-utilisant-Java-et-Spring

## API Documentation

Link to swagger interface : http://localhost:3001/swagger-ui/index.html

## Author

Arthur
