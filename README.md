# Tartu Explorer

## Team members and roles
Philipp Rebeschieß (MaytrixIT Philipp) - Project Leader <br/>
Timo Schwarzbach (timoschwarzbach timoschwarzbach) - Lead Developer <br/>
Rando Roosik (Orants Rando Roosik) - Researcher <br/>
Madis Puu (madsipuu) - Editor <br/>

## Project Overview

Tartu Explorer is an Android application designed to help users explore the city of Tartu through interactive quests and adventures. Find places on photos in real life. The app encourages outdoor activity by guiding players to specific locations, offering challenges to find well-known places in Tartu, and tracking the players progress.

### Goals
- **Promote Exploration:** Encourage users to discover new places in Tartu.
- **Interactive Gameplay:** Combine real-world location tracking with in-app quests and hints.
- **Progress Tracking:** Allow users to monitor their achievements and statistics.
- **User-Friendly Experience:** Provide a seamless and engaging mobile interface.

### Main Features
- **Adventure Mode:** Select from various adventures with different difficulty levels.
- **Quest System:** Navigate to specific target locations using hints and map guidance.
- **Real-time Location:** Uses GPS to track user position and verify quest completion.
- **Weather Integration:** Checks current weather conditions to suggest appropriate times for exploration.
- **Statistics:** View completed adventures, hints used, and overall progress.

## Installation Steps & Build Instructions

### Dependencies
The project uses the following key libraries and frameworks:
- **Kotlin:** Primary programming language.
- **Jetpack Compose:** For building the native UI.
- **Android Architecture Components:** ViewModel, Lifecycle, Room Database.
- **Google Maps SDK:** For map integration and location services.
- **Retrofit:** For network requests (Weather API).
- **Coil:** For image loading.

### Build Instructions
1.  **Clone the Repository:**
    ```bash
    git clone <repository-url>
    ```
2.  **Open in Android Studio:**
    - Launch Android Studio.
    - Select "Open" and navigate to the project root directory.
3.  **Sync Gradle:**
    - Allow Android Studio to sync the project with Gradle files.
    - Ensure you have the required Android SDKs installed (Target SDK 36, Min SDK 29).
4.  **API Keys:**
    - **Google Maps:** You will need a valid Google Maps API key defined in your `local.properties` or manifest.
5.  **Build and Run:**
    - Connect an Android device or start an Emulator.
    - Click the "Run" button (Green Arrow) in Android Studio.

## Usage Guide

1.  **Launch:** Open the app on your Android device.
2.  **Permissions:** Grant location permissions when prompted to enable the core gameplay features.
3.  **Select Adventure:** Browse the list of available adventures on the home screen and tap one to start.
4.  **Play:** Follow the on-screen hints and map to find the target locations in the city.

## Project Structure

The project follows a feature-based Clean Architecture approach:

-   `app/src/main/java/ee/ut/cs/tartu_explorer/`
    -   `core/`: Contains core components shared across the app.
        -   `data/`: Database entities (Room), repositories, and data sources.
        -   `ui/`: Common UI components and theme definitions.
        -   `util/`: Utility classes.
    -   `feature/`: Contains feature-specific code.
        -   `game/`: Logic for the active gameplay session.
        -   `quest/`: Management of quest steps and states.
        -   `home/`: Main landing screen and adventure list.
        -   `weather/`: Weather data fetching and display.
        -   `statistics/`: User stats and history.
    -   `navigation/`: Navigation graph and logic.
    -   `MainActivity.kt`: The entry point of the application.


## User Stories and paper Prototyping

see [step1_report.md](https://github.com/UT-EE-Mobile-App-Development/tartu-explorer/blob/main/step1_report.md)


## API Documentation ##

see [step3_report.md](https://github.com/UT-EE-Mobile-App-Development/tartu-explorer/blob/main/step3_report.md)


## Polishing + Testing + APK Documentation ##

see [step4_report.md](https://github.com/UT-EE-Mobile-App-Development/tartu-explorer/blob/main/step4_report.md)

