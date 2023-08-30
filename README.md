# Java RESTful API with Spring Boot.
## Demo project that exemplifies the interaction of three independent APIs for managing `User`, `Address` and `PhoneNumber` through a Java RESTful API.

The `pom.xml` file includes the necessary  _Spring Boot dependencies_  like Starter Data JPA, Starter Web, Starter Test, Starter Validation, DevTools and other related dependencies like H2 in-memory Database, Lombok and MapStruct.

The domain model includes the following entities:
* `User`: Represents a user in the system. The `address` attribute has a _one-to-one_ relationship with the `Address` entity and the `phones` attribute has a _one-to-many_ relationship with the `PhoneNumber` entity.
* `Address`: Represents the address of a user. The `user` attribute has a _one-to-one_ bidirectional relationship with the `User` entity.
* `PhoneNumber`: Represents a user's telephone number. The `user` attribute has a _many-to-one_ bidirectional relationship with the `User` entity.

The application provides a controller for each API that maps entities into DTOs and vice versa between the client and the server, as well as repositories for the three entities with some custom methods like `findByUser`, `findAllByUser` and `findByCpf`.

The application also implements a service layer for each controller, which interacts with the respective repositories for CRUD operations for `User`, `Address` and `PhoneNumber` entities.

Some custom exceptions like `IllegalUserStateException`, `IllegalAddressStateException` and `IllegalPhoneStateException` was implemented for handle request exceptions.

The application includes unit tests and integration tests classes with **JUnit**, **Mockito** and **Spring Test**.

The `RestApiApplication` application launcher is the main class that starts Spring Boot execution.
