# Java RESTful API with Spring Boot

## Overview
This demo project highlights the interaction of three independent APIs designed to manage `User`, `Address`, and `PhoneNumber` entities through a Java RESTful API. The project utilizes Spring Boot's Starter Data JPA, Starter Web, Starter Test, Starter Validation, DevTools, and other relevant dependencies such as H2 in-memory Database, Lombok, and MapStruct.

## Getting Started
Follow these steps to run the project locally:
1. Install the required dependencies mentioned in [pom.xml](pom.xml).
2. Run the application by executing the `main` method in [RestApiApplication.java](src/main/java/com/venturasistemoj/restapi/RestApiApplication.java).

## API Documentation
The API endpoints and their usage are documented using [Swagger](https://swagger.io/) for easy reference. Access the documentation at: `http://localhost:8080/swagger-ui.html`.

## Endpoints and Usage
The project provides the following APIs:

### Users API
- `GET /rest-api/users`: Retrieve a list of all users from the database.
- `GET /rest-api/users/{id}`: Retrieve details of a specific user.
- `POST /rest-api/users`: Create a new user.
- `PUT /rest-api/users/{id}`: Update user details.
- `DELETE /rest-api/users/{id}`: Delete a user.

### Adresses API
- `GET /rest-api/adresses`: Retrieve a list of all adresses from the database.
- `GET /rest-api/adresses/{userId}`: Retrieve details of a specific address.
- `POST /rest-api/adresses/{userId}`: Create a new address for an existing user.
- `PUT /rest-api/adresses/{userId}`: Update an address for an existing user.
- `DELETE /rest-api/adresses/{userId}`: Delete an address for an existing user.

### Phone Numbers API
- `GET /rest-api/phones`: Retrieve a list of all phone numbers from the database.
- `GET /rest-api/phones/{userId}`: Retrieve details of a specific phone number.
- `POST /rest-api/phones/{userId}`: Create a phone number for an existing user.
- `PUT /rest-api/phones/{userId}`: Update a phone number for an existing user.
- `DELETE /rest-api/phones/{userId}`: Delete a phone number for an existing user.

## Testing
The project includes comprehensive unit and integration tests. You can run tests in [tests](src/test/java/com/venturasistemoj/restapi).

## License
This project is licensed under the [MIT License](LICENSE).

## Contact
For any questions or feedback, feel free to contact me at `venturamail@riseup.net` or visit my [GitHub profile](https://github.com/venturasistemoj).
