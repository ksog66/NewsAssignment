# NewsAssignment

## Description
A functional news app that fetch latest news articles from NewsApi and displays them in a user-friendly manner, and allows users to save articles locally for offline
access. The project follows Android best practices and leverages Jetpack Compose for modern UI
development.

## Project Setup

1. Clone the repository:
   ```sh
   git clone git@github.com:ksog66/NewsAssignment.git
   ```
2. Navigate to NewsAssignment/app in terminal
3. Then create a file named keys.properties using 
   ```sh
   touch keys.properties
   ```
4. Then add two variables in the file(keys.properties) baseUrl=https://newsapi.org/ and apiKey=YOUR_NEWS_API_KEY
5. You can get your own apikey from https://newsapi.org
6. Then open the project in **Android Studio**

## Libraries Used

- **Retrofit**: Used for making network requests to perform any data of rest api operation.
- **Moshi**: A JSON library for parsing API responses efficiently.
- **Hilt**: Dependency injection framework to manage dependencies in the application.
- **Room**: Provides a local database for caching articles data and also store the saved articles from user.
- **Coil**: Async Image loading library to load image from internet.
- **Paging3**: Pagination library to load large chunk of data in efficient manner in offset of pages.

## Architecture Used
The project is built using Clean Architecture, which basically means the code is well-structured and
easier to manage. There are different layers like Presentation for UI stuff, Domain for all the main
logic, and Data for handling API calls and local storage using Retrofit and Room. Hilt is used for
dependency injection.

**Note**: Usecase can be used in this project to isolate each and every action which helps in separation of concern and helps to easily debug and test certain feature without breaking thing, but i didn't used it because of time constraint and it might have been a overkill for the scope of this project