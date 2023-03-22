# Car Service Garage
Application is designed to book appointments for repairs or services of customers cars at automotive workshops in various cities. 
Spring Security login is implemented in the application, and once logged in, users can add their cars and book a service or services at a selected workshop. 
They will be informed about the estimated repair time and cost. They can always manage their incoming bookings as well as cars.

## Here is only frontend, link to backend

This repository contains only the frontend of Car Service Garage. To function properly, it must be run in conjunction with the backend version, which can be found here: [**GitHub**](https://github.com/viepovsky/Final-App-Backend/tree/release-1.0).

## Usage of external API

At the moment, the application is using one external Car API to provide car details such as the year, make, model, and type.

## How to run

To run this application, you need to first run the backend. Once that is done, to run the frontend, simply type `mvnw` (Windows) or `./mvnw` (Mac & Linux) in terminal IDE. Then, open http://localhost:8081 in your browser to access the application. 

In case of problems on Mac with the error message `zsh: permission denied: ./mvnw`, simply type `chmod +x ./mvnw` in terminal IDE to make the file executable, and then type `./mvnw` again to start the application.

## Logging to site, initial data

The backend of Car Service Garage contains initial data to demonstrate the application's capabilities. After testing with the provided user account, you can add your own to test the application with your own data. 

To log in, use the username: `testuser` and the password: `testpassword`
