# API Specifications & Data Model Descriptions

## Data Models (Room Database)

### AdventureEntity (`adventure`)
Represents a complete adventure (a collection of quests).
-   `id`: Long (PK) - Auto-generated unique identifier.
-   `title`: String - The name of the adventure.
-   `description`: String - Detailed description or objectives.
-   `difficulty`: Enum (VERY_EASY, EASY, MEDIUM, HARD, VERY_HARD) - Level of challenge.
-   `estimatedDuration`: Int - Estimated time to complete in minutes.
-   `thumbnailPath`: String - Path or URL to the cover image.
-   `completed`: Boolean - Flag indicating if the adventure has been fully completed.

### QuestEntity (`quest`)
Represents a specific step or target location within an adventure.
-   `id`: Long (PK) - Auto-generated unique identifier.
-   `adventureId`: Long (FK -> AdventureEntity) - Links the quest to a specific adventure.
-   `latitude`: Double - Target latitude coordinate.
-   `longitude`: Double - Target longitude coordinate.
-   `radius`: Double - The radius in meters within which the quest is considered "reached".

### AdventureSessionEntity (`adventure_session`)
Tracks an active playthrough of an adventure by a player.
-   `id`: Long (PK)
-   `adventureId`: Long (FK)
-   `playerId`: Long (FK)
-   `startTime`: Long - Timestamp when the session started.
-   `endTime`: Long? - Timestamp when the session ended.
-   `status`: Enum (IN_PROGRESS, COMPLETED, CANCELLED)
-   `currentQuestIndex`: Int - Tracks progress through the adventure's quests.

### Weather Response Model (API)
-   `current_weather`: Object
    -   `temperature`: Double
    -   `weathercode`: Int
    -   `windspeed`: Double

## External APIs

### Weather API (Open-Meteo)
-   **Endpoint:** `v1/forecast`
-   **Method:** GET
-   **Parameters:**
    -   `latitude`: Double (Required)
    -   `longitude`: Double (Required)
    -   `current_weather`: Boolean (Set to `true`)
-   **Usage:** Fetches current weather conditions (temperature, weather code) to display on the dashboard, helping users decide if it's good weather for an adventure.

---

# Overview of Key Modules, Services, and Workflows

## Modules

### 1. Core Module (`core`)
-   **Data Layer:** Contains the Room database setup (`AppDatabase`), Data Access Objects (DAOs), and Entities (`AdventureEntity`, `QuestEntity`, etc.). It acts as the single source of truth for app data.
-   **UI Layer:** Shared UI components and the application theme (`TartuExplorerTheme`).

### 2. Feature Modules
-   **Home (`feature/home`):** Displays the list of available adventures. It observes the database for available adventures and their completion status.
-   **Game (`feature/game`):** The core gameplay loop. It manages the active session, calculates distances to targets, and handles user interactions like guessing locations or using hints.
-   **Quest (`feature/quest`):** Details about specific quest steps.
-   **Weather (`feature/weather`):** Responsible for communicating with the external Weather API using Retrofit.
-   **Statistics (`feature/statistics`):** Aggregates and displays user progress, such as total adventures completed.

## Workflows

### Adventure Lifecycle
1.  **Selection:** The user browses the Home screen and selects an adventure.
2.  **Initialization:** A new `AdventureSession` is created in the database with status `IN_PROGRESS`.
3.  **Gameplay:**
    -   The user is shown the hint/description for the current `Quest`.
    -   The app tracks the user's location via GPS (using Google Location Services).
    -   The user tries to physically reach the coordinates.
4.  **Completion of Step:** When the user enters the `radius` of the target `QuestEntity`, the step is marked complete.
5.  **Completion of Adventure:** When all quests in an adventure are finished, the session status updates to `COMPLETED`, and the `AdventureEntity` is marked as completed.

### Weather Check
1.  On app launch or refresh, the `WeatherViewModel` requests weather data for the current user location (or a default Tartu location).
2.  The `WeatherRepository` calls the Retrofit service.
3.  The result is displayed on the Home screen (e.g., "15°C, Cloudy").

---

# User-Facing Instructions

## Step-by-Step Usage Guidance

### 1. Getting Started
-   **Permissions:** Upon first launch enter your profile name and grant the app permission to access your device's location (in the game window). This is critical for the gameplay to work.
-   **Navigation:** Use the bottom navigation or menu (if applicable) to switch between "Home" (Adventures) and "Stats".

### 2. Choosing an Adventure
-   On the **Home Screen**, scroll through the list of adventures.
-   Each card shows the **Difficulty** (e.g., Easy, Hard), **Duration**, and a **Title**.
-   Tap a card to select an Adventure and start.

### 3. Playing the Game
-   **Read the Clue:** In some quests, you will be given a description or a hint about where to go next.
-   **Find the Location:** Walk to the real-world location in Tartu.
-   **Map:** Use the integrated map to orient yourself. Your position is shown as a blue dot.
-   **Arriving:** When you are close enough (usually within 25 meters, exact radius depends on quest), the app will vibrate or notify you that you've reached the checkpoint.
-   **Next Step:** The app automatically advances to the next location in the adventure.

### 4. Viewing Progress
-   Go to the **Statistics** screen to see how many adventures you have finished and other fun stats about your journey.
