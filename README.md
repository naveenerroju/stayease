# StayEase: A Secure Spring Boot Backend for Effortless Online Booking

## Project Overview

StayEase is a secure backend application developed using Spring Boot. It provides a robust platform for managing hotel and room bookings. The application is built with Java 17, Spring Boot 3.3.4, Spring Security 6, and JWT 0.12.5 to ensure high security and performance.

## Features

- User and Manager registration and login
- JWT-based authentication and authorization
- Secure password encryption
- Hotel and room management
- Booking management
- Validation of passwords to check if they are compromised

## Technologies Used

- **Java 17**
- **Spring Boot 3.3.4**
- **Spring Security 6**
- **JWT 0.12.5**
- **JPA/Hibernate**
- **Lombok**
- **Jakarta Persistence API**

## Database Structure

### Table Relations

- **User**: 
  - One-to-Many relationship with **Booking**
- **Hotel**: 
  - One-to-Many relationship with **Room**
- **Room**: 
  - Many-to-One relationship with **Hotel**
  - One-to-Many relationship with **Booking**
- **Booking**: 
  - Many-to-One relationship with **User**
  - Many-to-One relationship with **Room**

## API Documentation

### User Registration
- **URL**: `{{base_url}}/user/register`
- **Method**: POST
- **Request Body**:
    ```json
    {
        "firstname": "Naveen",
        "email": "naveen@gmail.com",
        "password": "naveenerroju@1997"
    }
    ```
    - If you do not provide a role, you are assigned the user role by default.
    - To register as a manager, provide the role as `MANAGER`.
- **Expected Response**:
    ```json
    {
        "firstname": "Naveen",
        "email": "naveen@gmail.com",
        "role": "USER"
    }
    ```
- **Note**: User email address will be saved to collection variables.

### Login
- **URL**: `{{base_path}}/user/login`
- **Method**: POST
- **Request Body**:
    ```json
    {
        "email": "{{user_email}}",
        "password": "naveenerroju@1997"
    }
    ```
- **Expected Response**:
    ```json
    {
        "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYXZlZW5AZ21haWwuY29tIiwiaWF0IjoxNzMwNjI4MzIyLCJleHAiOjE3MzA2MzkxMjJ9.Q2qTLsItHN1aXS-fQZL46U8NvBzWCTgnkSlizDo9ElI"
    }
    ```
    - This token will be valid for 3 hours (check `application.properties` for the updated time limit).
    - Both users and managers can log in here.
    - The token will be saved to collection variables.

### Adding Hotels
You need to be a manager to add hotels. This is a transactional call. So, if any part of the transaction falls, rest of the transaction will roll back.
- **URL**: `{{base_path}}/hotel`
- **Method**: POST
- **Request Body**:
    ```json
    {
        "name": "Grand Hotel",
        "location": [40.712776, -74.005974],
        "description": "A luxurious hotel located in the heart of the city.",
        "availability": true
    }
    ```
- **Expected Response**:
    ```json
    {
        "id": 1,
        "name": "Grand Hotel",
        "location": [40.712776, -74.005974],
        "description": "A luxurious hotel located in the heart of the city.",
        "rooms": null
    }
    ```

### Adding Rooms
You need to be a manager to add rooms. This is a transactional call. So, if any part of the transaction falls, rest of the transaction will roll back.

- **URL**: `{{base_url}}/room`
- **Method**: POST
- **Request Body**:
    ```json
    {
        "name": "Deluxe Room",
        "description": "A spacious room with a king-sized bed and a beautiful view.",
        "hotel": {{"hotel_id"}},
        "totalNumberOfRooms": 5
    }
    ```
- **Expected Response**:
    ```json
    {
        "id": 1,
        "name": "Deluxe Room",
        "description": "A spacious room with a king-sized bed and a beautiful view.",
        "totalNumberOfRooms": 5,
        "bookings": null
    }
    ```

### Getting Available Rooms
Checking availability of rooms is a complex and key features of the application. To check if the room is available on a particular date, we query the database to check the bookings table and provide the rooms has atleast 1 room available to book. Then we map the room details and hotel details to the response along with the number of available rooms.
- **URL**: `{{base_path}}/room/available-rooms?date=2025-01-27`
- **Method**: GET
- **Expected Response**:
    ```json
    [
        {
            "hotelId": 1,
            "hotelName": "Grand Hotel",
            "roomId": 1,
            "roomName": "Deluxe Room",
            "availableRooms": 5
        }
    ]
    ```

### Booking a Room
You should only be a user to book a room. Validations for checking hotel and rooms are implemented.
- **URL**: `{{base_path}}/book`
- **Method**: POST
- **Request Body**:
    ```json
    {
        "roomId": {{"room_1_id"}},
        "bookingDate": "{{tomorrowDate}}"
    }
    ```
- **Expected Response**:
    ```json
    {
        "bookingId": "b6c9e587-d73c-4a88-a817-2ca62653d2fd",
        "hotelId": 1,
        "hotelName": "Grand Hotel",
        "roomId": 1,
        "roomName": "Deluxe Room",
        "bookingDate": "2024-11-04"
    }
    ```
    - Booking ID is a UUID.
    - Variables for hotel ID and room ID will be saved after successful POST requests.
### Cancel a booking

you can cancel a booking if it is not fulfilled. If the booked date is already past, we get the validation error.

However, if it is still in future, we can cancel the booking by doing a delete API.

- **URL**: `{{base_path}}/book/{{booking_id}}`

- **Method**: DELETE

- On successfull deletion, we get 204 no content response.


### Checking Bookings
- **URL**: `{{base_path}}/book`
- **Method**: GET
- **Expected Response**:
    - **Success**:
        ```json
        [
            {
                "bookingId": "b6c9e587-d73c-4a88-a817-2ca62653d2fd",
                "hotelId": 1,
                "hotelName": "Grand Hotel",
                "roomId": 1,
                "roomName": "Deluxe Room",
                "bookingDate": "2024-11-04"
            }
        ]
        ```
    - **Failure**:
        ```json
        {
            "code": "VALIDATION_EXCEPTION",
            "description": "You do not have any bookings yet"
        }
        ```

## Security Features
This application is secured using role based authentication. We used JWT for the robust security. Few APIs like checking availability or fetching hotel details are public. APIs like adding hotels or rooms can only be done by MANAGER authority. Users can book the rooms and check their bookings.

- **Password Encryption**: Passwords are encrypted using bcrypt with a strength of 12.
- **JWT Authentication**: JWT tokens are used for securing endpoints.
- **Password Validation**: Passwords are validated to ensure they are not compromised.

## Getting Started

### Prerequisites

- Java 17
- Maven

### Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/naveenerroju/stayease.git
    ```
2. Navigate to the project directory:
    ```bash
    cd stayease
    ```
3. Build the project:
    ```bash
    mvn clean install
    ```
4. Run the application:
    ```bash
    mvn spring-boot:run
    ```

### Configuration

- Update the `application.properties` file with your database configuration and other settings.

### Running Tests

- To run tests, use the following command:
    ```bash
    mvn test
    ```

## Contributing
Default branch of this repository is protected and only owner can merge any PR. Feel free to rise a PR, so I can review it. 
1. Fork the repository.
2. Create a new branch:
    ```bash
    git checkout -b feature-branch
    ```
3. Make your changes and commit them:
    ```bash
    git commit -m 'Add some feature'
    ```
4. Push to the branch:
    ```bash
    git push origin feature-branch
    ```
5. Create a new Pull Request.

## Acknowledgments

- Thanks to the Spring Boot community for their excellent resources and support.
- Special thanks to [crio.do](https://www.crio.do/) of this project idea. 

