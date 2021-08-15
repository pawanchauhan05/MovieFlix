# MovieFlix Android App

### MovieFlix App Jetpack Components
App demonstrating Clean Architecture using Coroutines and Android Jetpack Components (Room, MVVM and Live Data)

- Displaying view for now playing movies
- Movie view will appear as per thier popularity
- by swiping you can delete a movie
- search movie by movie title

### ScreenShots
<img src = "https://github.com/pawanchauhan05/MovieFlix/blob/dev/screenshots/movie_listing.jpeg" width = 260 height = 550/> <img src = "https://github.com/pawanchauhan05/MovieFlix/blob/dev/screenshots/movie_details.jpeg" width = 260 height = 550/> <img src = "https://github.com/pawanchauhan05/MovieFlix/blob/dev/screenshots/movie_search.jpeg" width = 260 height = 550/> <img src = "https://github.com/pawanchauhan05/MovieFlix/blob/dev/screenshots/swipe_to_delete.jpeg" width = 260 height = 550/>

### Tech-Stack

* __Retrofit__ : For Network calls
* __Image Caching__ : Using custom image caching
* __Architecture__ : MVVM
* __Coroutines__ for background operations like fetching network response
* __Room database__ : For offline persistence
* __Live Data__ : To notify view for change
* __Hilt__ : To injecting dependancy (Google Dependency injection framework)
* __Language__ : Kotlin

### Source code & Test Cases
> **app/src/main:** Directory having **main source code** of the MovieFlix app.

> **app/src/androidTest:** Directory having **Local Database Test Case** of the MovieFlix app.

> **app/src/test:** Directory having **JUnit Test Case** for repository, ViewModel, Remote Date Source and Local Data Source of the MovieFlix app.

### Architecture Diagram
This application strictly follows the below architecture

<img src = "https://github.com/pawanchauhan05/MovieFlix/blob/dev/screenshots/Architecture.png" width = 450 />

