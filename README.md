# Car Parking - Spring Boot Application
## Overview
The Car Parking Spring Boot Application is designed to manage cars and parking spaces efficiently, ensuring proper validation when adding or removing cars from parkings. It handles various scenarios such as parking space availability, car size constraints, fuel type restrictions (like LPG cars), and charging station availability for electric vehicles.

## Features
### Car Management:

- Create, update, retrieve, and delete car entities.
- Manage car attributes like brand, model, width, and fuel type (e.g., LPG, Electric, etc.).

### Parking Management:
- Create, update, retrieve, and delete parking entities.
- Add or remove cars from parking spaces with proper validations (e.g., space availability, fuel type restrictions).
- Manage parking attributes like total spaces, width, address, and support for LPG or electric car chargers.

### Validations:
- Ensure cars adhere to parking width constraints.
- Restrict LPG cars from non-LPG-friendly parking.
- Ensure electric vehicles only park where chargers are available.
- Verify parking space availability before adding cars.
## Technologies Used
- Spring Boot: Backend framework for developing RESTful services.
- Spring Data JPA: To handle the persistence of CarEntity and ParkingEntity.
- Hibernate: As the ORM tool for interacting with the database.
- SLF4J with Logback: For logging application actions and errors.