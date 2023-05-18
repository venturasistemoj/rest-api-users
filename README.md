# Java RESTful API with Spring Boot.
## Demo project that exemplifies the management of a `User` through a RESTful Java API.

The `POM.xml` file includes the necessary dependencies like **Spring Boot Starter Data JPA**, **Spring Boot Starter Web**, **H2 in-memory Database**, **Lombok** and other Spring Boot related dependencies.

The `application.properties` file defines some settings such as the path matching strategy and the display of SQL queries.

The domain model includes the following entities:
* `User`: Represents a user in the system. The `address` attribute is a _one-to-one_ relationship with the `Address` entity and the `phones` attribute is a _one-to-many_ relationship with the `PhoneNumber` entity.
* `Address`: Represents the address of a user.
* `PhoneNumber`: Represents a user's telephone number. The `user` attribute is a _many-to-one_ bidirectional relationship with the `User` entity.

The application also provide the `UserRepository` repository and the `UserController` controller, which implement the CRUD operations for the `User` entity.

The application includes a `RestApiTests` test class for `UserController` class with **JUnit** and **Mockito**.

The `RestApiApplication` application launcher is the main class that starts Spring Boot execution.
