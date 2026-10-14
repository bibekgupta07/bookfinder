# BookFinder

An Android app for browsing books by category, viewing book details and saving favourites. It's built with **Jetpack Compose** using **Clean Architecture** and data from the [Google Books API](https://developers.google.com/books).

## Features
- Browse books by category, with a "popular in technology" section
- Book detail screen
- Favourites saved on the device with **Room**
- Light and dark theme, remembered with **DataStore**

## Tech stack
| Layer | Libraries |
|---|---|
| UI | Jetpack Compose, Material 3, Navigation Compose, Coil |
| State | ViewModel, StateFlow, Kotlin Coroutines / Flow |
| DI | Hilt |
| Data | Retrofit, OkHttp, Gson, Room, DataStore |

## Architecture
```
presentation/  Compose screens + ViewModels (home, details, favourites)
domain/        Book model, BookRepository interface, GetBooksUseCase
data/          Retrofit API, Room database/DAO, repository implementations
di/            Hilt modules (network, database)
```
The UI observes `StateFlow<Resource<T>>` (Loading / Success / Error). ViewModels depend only on use cases and repository interfaces from the `domain` layer.

## Getting started
1. Get a Google Books API key from the [Google Cloud Console](https://console.cloud.google.com/apis/credentials).
2. Add it to `local.properties` in the project root (this file is not committed):
   ```properties
   GOOGLE_BOOKS_API_KEY=your_key_here
   ```
3. Open the project in Android Studio and run the `app` configuration (minSdk 28).
