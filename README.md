# WealthTrack - Financial Dashboard Backend

This project is a RESTful API backend for a financial dashboard application built with Kotlin and Spring Boot. It allows users to track investment portfolios, simulate investment strategies, and analyze financial data.

## Features

- User authentication with JWT
- Portfolio management
- Investment tracking
- Financial asset information
- Investment simulation with customizable parameters
- Comprehensive API documentation with Swagger

## Technology Stack

- **Language**: Kotlin 1.8
- **Framework**: Spring Boot 3.1
- **Database**: PostgreSQL
- **Security**: Spring Security with JWT
- **ORM**: Spring Data JPA
- **Database Migrations**: Flyway
- **API Documentation**: Springdoc OpenAPI (Swagger)
- **Build Tool**: Gradle with Kotlin DSL

## Prerequisites

- JDK 17 or higher
- PostgreSQL 14 or higher
- Gradle 8.0+ (or use the included Gradle wrapper)

## Getting Started

### Setting Up the Database

1. Create a PostgreSQL database:
   ```sql
   CREATE DATABASE wealthtrack_db;
   ```

2. Configure your database connection in `application-dev.yml`

### Building and Running the Application

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/wealthtrack-backend.git
   cd wealthtrack-backend
   ```

2. Build the application:
   ```bash
   ./gradlew build
   ```

3. Run the application:
   ```bash
   ./gradlew bootRun
   ```

The application will start on port 8080 by default and will be accessible at `http://localhost:8080/api`.

## API Documentation

Once the application is running, you can access the Swagger UI documentation at:
```
http://localhost:8080/api/swagger-ui.html
```

The OpenAPI specification is available at:
```
http://localhost:8080/api/api-docs
```

## Project Structure

The project follows a standard layered architecture:

- **Controller Layer**: Handles HTTP requests and responses
- **Service Layer**: Contains business logic
- **Repository Layer**: Handles data access
- **Model Layer**: Defines the data structures
- **DTO Layer**: Defines the data transfer objects for requests and responses
- **Security**: JWT-based authentication and authorization

## Main Endpoints

### Authentication

- `POST /api/auth/signup`: Register a new user
- `POST /api/auth/signin`: Authenticate a user and get tokens
- `POST /api/auth/refresh-token`: Refresh the access token

### User Management

- `GET /api/users/me`: Get current user profile
- `PUT /api/users/me`: Update current user profile

### Portfolio Management

- `GET /api/portfolios`: Get all portfolios for the current user
- `GET /api/portfolios/{id}`: Get a specific portfolio
- `POST /api/portfolios`: Create a new portfolio
- `PUT /api/portfolios/{id}`: Update a portfolio
- `DELETE /api/portfolios/{id}`: Delete a portfolio

### Investment Simulation

- `GET /api/simulations`: Get all simulations for the current user
- `GET /api/simulations/{id}`: Get a specific simulation
- `POST /api/simulations`: Create a new investment simulation
- `DELETE /api/simulations/{id}`: Delete a simulation

## Development

### Running Tests

```bash
./gradlew test
```

### Running with Different Profiles

The application supports different profiles:

- **dev**: For development (default)
- **prod**: For production environment

To run with a specific profile:

```bash
./gradlew bootRun --args='--spring.profiles.active=prod'
```

### Environment Variables

For production deployment, you should set the following environment variables:

- `DB_HOST`: Database host
- `DB_PORT`: Database port
- `DB_NAME`: Database name
- `DB_USERNAME`: Database username
- `DB_PASSWORD`: Database password
- `JWT_SECRET`: Secret key for JWT signing
- `ALLOWED_ORIGINS`: Comma-separated list of allowed origins for CORS

## Contributing

1. Create a feature branch
2. Commit your changes
3. Push to the branch
4. Open a pull request

## License

This project is proprietary and confidential.

## Contact

For any inquiries, please contact your-email@example.com
