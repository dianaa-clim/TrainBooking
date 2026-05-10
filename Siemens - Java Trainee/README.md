# Train Booking Applications

This repository contains two separate implementations of a train ticket booking system:

1. `ConsoleTrainApp` - a Java console application built with Maven, JDBC and MySQL.
2. `FullstackTrainApp` - a full-stack web application built with Spring Boot, React and MySQL.

Both applications are based on the same project idea: allowing users to search train journeys, book tickets, receive generated ticket codes and manage train-related data. However, they are implemented as separate projects and can be run independently.

The console version was developed first and focuses on core Java, JDBC, manual database access, transaction handling and a terminal-based user interface. The full-stack version was developed later as a modern web application, using a Spring Boot REST API and a React frontend.

## Repository Structure

```text
Siemens - Java Trainee/
│
├── ConsoleTrainApp/
│   ├── src/
│   ├── pom.xml
│   └── README.md
│
├── FullstackTrainApp/
│   ├── train-booking-backend/
│   │   ├── src/
│   │   ├── pom.xml
│   │   └── README.md
│   │
│   └── train-booking-frontend/
│       ├── src/
│       ├── package.json
│       └── README.md
│
├── .gitignore
└── README.md
```

## Project Versions

## 1. ConsoleTrainApp

`ConsoleTrainApp` is the first version of the train booking system. It is a Java console application that runs in the terminal and uses JDBC to communicate directly with a MySQL database.

This version was built to implement the main business logic of the train booking system in a clear and controlled way. It includes authentication, role-based menus, customer actions, admin actions, overbooking prevention, ticket generation, email confirmations and delay notifications.

### Technologies Used

- Java 23
- Maven
- JDBC
- MySQL
- Jakarta Mail
- SMTP email sending
- IntelliJ IDEA

### Main Features

The console application supports two types of users:

```text
CUSTOMER
ADMIN
```

Customers can register, log in, search for train journeys, book tickets for one or more passengers and receive email confirmations.

Admins can manage stations, trains, routes, route stops, bookings and train delays. When a delay is registered, the application finds affected bookings and sends delay notification emails to the customers.

### Customer Features

A customer can:

- create an account
- log in
- search for train journeys between two stations
- view direct journeys
- view journeys with one train change
- book tickets for one or more passengers
- receive a booking code
- receive generated ticket codes
- view full ticket details after booking
- receive a real email confirmation

Each generated ticket contains information such as:

```text
Ticket code
Passenger name
Train number
Train name
Origin station
Destination station
Departure time
Arrival time
```

### Admin Features

An administrator can:

- list, add, update and deactivate stations
- list, add, update and deactivate trains
- list, add, update and deactivate routes
- add stations to routes
- update route stops
- remove stations from routes
- view bookings for a selected train run
- register train delays
- notify affected customers by email

The default admin account is created automatically if no admin exists:

```text
Email: admin@trainapp.com
Password: admin123
```

### Architecture

The console application follows a layered architecture:

```text
CLI -> Service -> Repository -> Database
```

The main packages are:

```text
cli
config
dto
exception
model
repository
service
util
```

The `cli` package handles menus and user input.  
The `service` package contains the business logic.  
The `repository` package communicates with the MySQL database using JDBC.  
The `model` package contains the domain classes.  
The `dto` package is used to transfer structured data between layers.  
The `exception` package contains custom exceptions.  
The `config` package handles database and email configuration.

### Database

The console application uses a MySQL database named:

```text
train_booking
```

Main tables include:

```text
users
stations
trains
routes
route_stops
train_runs
train_run_stops
customers
bookings
booking_passengers
booking_legs
tickets
train_delay_events
email_outbox
```

### Overbooking Prevention

The application prevents overbooking by checking seat availability on each train segment, not only on the whole train.

For example, for this route:

```text
CLJ -> ALB -> SB -> BV -> BUC
```

A booking from `CLJ` to `BUC` occupies all route segments, while a booking from `ALB` to `BV` occupies only the segments between those two stations.

Before confirming a booking, the application checks all affected segments. If one segment exceeds the train capacity, the booking is rejected.

### Email Notifications

The console application supports real email sending using SMTP and Jakarta Mail.

Email types:

```text
BOOKING_CONFIRMATION
DELAY_NOTIFICATION
```

Every email attempt is saved in the `email_outbox` table.

Possible statuses:

```text
SENT
FAILED
SIMULATED
```

If the email cannot be sent, the booking process is not blocked. The email attempt is saved with status `FAILED`.


## 2. FullstackTrainApp

`FullstackTrainApp` is the web version of the train booking system. It contains a Spring Boot backend and a React frontend.

This version was developed as a separate full-stack application, not as a direct modification of the console application. It keeps the same main idea and business rules, but uses a modern web architecture based on REST APIs, JWT authentication and a browser-based user interface.

The full-stack application is split into two parts:

```text
train-booking-backend
train-booking-frontend
```

### Backend Technologies

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- JWT authentication
- MySQL
- Maven
- Jakarta Validation
- Jakarta Mail / Spring Mail
- Lombok

### Frontend Technologies

- React
- Vite
- TypeScript
- React Router
- Axios
- CSS

### Backend Responsibilities

The backend exposes REST APIs for:

- authentication
- customer registration
- login
- role-based authorization
- journey search
- ticket booking
- booking history
- station management
- train management
- route management
- train run management
- delay management
- email notifications
- email outbox management

The backend uses JWT tokens to secure protected endpoints. After login, the frontend stores the token and sends it in the `Authorization` header when calling secured API endpoints.

Example:

```text
Authorization: Bearer token_value
```

### Frontend Responsibilities

The frontend provides a web interface for customers and administrators.

Customers can use the interface to:

- register
- log in
- search journeys
- view direct journeys
- view journeys with one train change
- book tickets
- view generated ticket details
- view their personal bookings

Admins can use the interface to:

- manage stations
- manage trains
- manage routes
- manage train runs
- register delays
- view delay history
- view bookings for train runs
- view email notifications from the outbox

### Authentication and Roles

The full-stack application supports:

```text
CUSTOMER
ADMIN
```

Main authentication features:

- customer registration
- login
- logout
- JWT-based authentication
- role-based access control
- BCrypt password hashing
- automatic admin creation at application startup

### Customer Features

Customers can:

- search train journeys by departure station, arrival station and date
- view available direct journeys
- view journeys with one train change
- book tickets for one or more passengers
- receive a booking code
- receive generated tickets
- view personal bookings
- receive booking confirmation emails

Each generated ticket contains:

```text
Ticket code
Passenger name
Train code
Train name
Route name
Departure station
Arrival station
Departure time
Arrival time
```

### Admin Features

Admins can manage the operational data of the train booking system.

Station management:

- list stations
- add station
- update station
- activate station
- deactivate station

Train management:

- list trains
- add train
- update train
- activate train
- deactivate train

Route management:

- list routes
- add route
- update route
- activate route
- deactivate route
- add station to route
- update route stop
- remove station from route

Train run management:

- create train runs for specific dates
- generate train run stops automatically from route stops
- view train run stops
- update train runs without bookings
- activate train runs
- deactivate train runs

Delay management:

- register delays for train runs
- update actual arrival and departure times
- view delay history for a selected train run
- notify affected customers by email

Booking management:

- view bookings for a selected train run
- view generated tickets for each booking

Email management:

- view email outbox
- view booking confirmation emails
- view delay notification emails
- filter emails by type
- view email body

### Full-Stack Architecture

The full-stack application follows a client-server architecture:

```text
React Frontend -> Spring Boot REST API -> MySQL Database
```

The backend follows a layered Spring Boot architecture:

```text
Controller -> Service -> Repository -> Database
```

Typical backend packages:

```text
controller
service
repository
model
dto
mapper
security
exception
config
```

The frontend is organized around pages, reusable components, API calls, routing and styling.

Typical frontend structure:

```text
src/
├── api/
├── components/
├── pages/
├── routes/
├── context/
└── styles/
```

### Overbooking Prevention

The full-stack version also prevents overbooking by checking train capacity on every route segment used by a booking.

This means that the application does not only check how many passengers are on the train in total. It checks the exact segments used by each booking.

For example:

```text
CLJ -> ALB -> SB -> BV -> BUC
```

A passenger traveling from `CLJ` to `BUC` occupies all segments.  
A passenger traveling from `ALB` to `BV` occupies only part of the route.

This makes the booking logic more realistic and avoids accepting bookings when one part of the route is already full.

### Email Notifications

The full-stack application supports email notifications for:

```text
BOOKING_CONFIRMATION
DELAY_NOTIFICATION
```

The backend stores email attempts in an email outbox table. This makes it possible to track whether an email was sent successfully or failed.

The admin can also view emails from the web interface.

### More Details

The full documentation for this version is available in:

```text
FullstackTrainApp/README.md
```



## Main Differences Between the Two Applications

| Aspect | ConsoleTrainApp | FullstackTrainApp |
|---|---|---|
| Interface | Terminal-based menu | Web interface |
| Backend style | Java application with JDBC | Spring Boot REST API |
| Frontend | Console UI | React + Vite |
| Database access | JDBC repositories | Spring Data JPA repositories |
| Authentication | Manual login system | JWT authentication |
| Password hashing | PBKDF2 | BCrypt |
| Roles | CUSTOMER, ADMIN | CUSTOMER, ADMIN |
| Email sending | Jakarta Mail / SMTP | Spring Mail / Jakarta Mail |
| Architecture | CLI -> Service -> Repository -> DB | React -> REST API -> Service -> Repository -> DB |
| Purpose | Core Java and business logic implementation | Modern web application implementation |

## Configuration and Security

Real configuration files are not included in this repository because they contain sensitive information such as:

```text
database passwords
email credentials
JWT secrets
local environment variables
```

The following files should remain local and should not be committed to GitHub:

```text
application.properties
application-local.properties
application-secret.properties
.env
```

Instead, example configuration files should be committed:

```text
application-example.properties
.env.example
```

### Console Application Configuration

The console application expects a local configuration file:

```text
ConsoleTrainApp/src/main/resources/application.properties
```

An example file should be available at:

```text
ConsoleTrainApp/src/main/resources/application-example.properties
```

Example structure:

```properties
db.url=jdbc:mysql://localhost:3306/train_booking?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
db.username=YOUR_DB_USERNAME
db.password=YOUR_DB_PASSWORD

app.name=Console Train Booking App
app.admin.password=admin123

mail.smtp.host=smtp.gmail.com
mail.smtp.port=587
mail.smtp.auth=true
mail.smtp.starttls.enable=true

mail.sender.email=YOUR_EMAIL@gmail.com
mail.sender.password=YOUR_GMAIL_APP_PASSWORD
mail.sender.name=Console Train Booking App
```

### Full-Stack Backend Configuration

The backend expects a local configuration file:

```text
FullstackTrainApp/train-booking-backend/src/main/resources/application.properties
```

An example file should be available at:

```text
FullstackTrainApp/train-booking-backend/src/main/resources/application-example.properties
```

Example structure:

```properties
spring.application.name=train-booking-backend

spring.datasource.url=jdbc:mysql://localhost:3306/train_booking_fullstack
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.secret=YOUR_JWT_SECRET
jwt.expiration=86400000

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=YOUR_EMAIL@gmail.com
spring.mail.password=YOUR_GMAIL_APP_PASSWORD
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### Full-Stack Frontend Configuration

The frontend expects a local `.env` file:

```text
FullstackTrainApp/train-booking-frontend/.env
```

An example file should be available at:

```text
FullstackTrainApp/train-booking-frontend/.env.example
```

Example:

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

## How to Run

## Running the Console Application

Requirements:

```text
Java 23
Maven
MySQL
```

Steps:

```bash
cd ConsoleTrainApp
mvn clean compile
mvn exec:java
```

The application can also be run from IntelliJ IDEA by running:

```text
Main.java
```

Before running the application, make sure the MySQL database is created and the local `application.properties` file is configured correctly.

## Running the Full-Stack Application

Requirements:

```text
Java
Maven
Node.js
npm
MySQL
```

### Start the Backend

```bash
cd FullstackTrainApp/train-booking-backend
mvn spring-boot:run
```

The backend usually runs on:

```text
http://localhost:8080
```

### Start the Frontend

Open another terminal:

```bash
cd FullstackTrainApp/train-booking-frontend
npm install
npm run dev
```

The frontend usually runs on:

```text
http://localhost:5173
```

## Example Data

The applications use sample train data for testing and demonstration.

Example stations:

```text
CLJ - Cluj-Napoca
ALB - Alba Iulia
SB - Sibiu
BV - Brașov
PLO - Ploiești Vest
BUC - București Nord
TM - Timișoara Nord
AR - Arad
```

Example trains:

```text
IR1745 - InterRegio Cluj - București
IR1834 - InterRegio Cluj - Brașov
IR1622 - InterRegio Brașov - București
R3001 - Regio Cluj - Alba Iulia
IR1550 - InterRegio Timișoara - Cluj
```

Useful journey searches:

```text
CLJ -> BUC
CLJ -> BV
BV -> BUC
CLJ -> ALB
TM -> CLJ
```

Main test date:

```text
2026-05-10
```

## Notes About GitHub

The repository is structured to keep both applications in the same place while still keeping them independent.

The root `.gitignore` is used to avoid committing generated files, IDE files, dependencies and sensitive configuration files.

Files that should not be committed:

```text
target/
node_modules/
.idea/
.env
application.properties
application-local.properties
application-secret.properties
```

Files that should be committed:

```text
README.md
pom.xml
package.json
src/
application-example.properties
.env.example
```

## Possible Future Improvements

Possible improvements for the project include:

- booking cancellation
- customer booking history improvements
- ticket prices
- numbered seats
- PDF ticket export
- pagination for admin lists
- retry mechanism for failed emails
- advanced filtering for train runs and bookings
- improved frontend design
- Docker support
- deployment configuration
- integration tests for the backend
- end-to-end tests for the full-stack application

## Author

Developed as a Java and full-stack train booking project, containing both a console-based version and a modern web version.
