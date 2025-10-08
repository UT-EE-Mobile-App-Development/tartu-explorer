# Tartu explorer

Pervasive Game Geo-Quest: A game like geoguessr, where you see a picture and have to find the location in real life. If you visit the place, you get points (various hints if you don't know the place in the picture)

## Team members and roles
Philipp Rebeschieß (MaytrixIT Philipp) - Project Leader <br/>
Timo Schwarzbach (timoschwarzbach timoschwarzbach) - Lead Developer <br/>
Rando Roosik (Orants Rando Roosik) - Researcher <br/>
Madis Puu (madsipuu) - Editor <br/>

## Data Model and Local Storage

Tartu Explorer uses a **local Room database** to store all gameplay-related data persistently on the device.  
The database is designed around several key entities that represent the structure of the game and the player’s progress:

- **AdventureEntity** – defines each adventure with a title, description, difficulty, and estimated duration.  
- **QuestEntity** – represents a single location (step) within an adventure, including GPS coordinates and a target radius.  
- **HintEntity** – stores optional hints (text or image) for each quest.  
- **PlayerEntity** – tracks player information such as name (and some basic statistics and experience points)  
- **AdventureSessionEntity** – logs each play session, linking a player to an adventure, including start and completion times.  
- **QuestAttemptEntity** – stores attempts for each quest, tracking success and duration.  
- **HintUsageEntity** – records when and which hints were used during an attempt.  
- **PlayerAdventureProgressEntity** – saves the player’s current position within an adventure, so progress can be resumed later.

All entities are connected through **foreign key relationships**, enabling efficient queries such as:
- total hints used per player  
- completed adventures and success rates  
- average time per quest or session  


## Features

- **All core pages implemented:**  
  The app currently includes all main sections — **Main Menu**, **Statistics**, **Map Selection**, and **Game View**.  
  - The **Main Menu** and **Statistics Page** are already fully designed and functional.  
  - The **Map Selection** and **Game View** currently use placeholder graphics, serving as prototypes for navigation and gameplay flow.

- **Adventure loading from backend:**  
  The **Game View** dynamically loads available **Adventures** and their corresponding **Quests** (the steps of each Adventure) from the backend.  
  Players must first select an Adventure via the **Map Selection** screen before starting the game.

- **Hint system integrated:**  
  In the Game View, players can tap on **Hints**, which are fetched directly from the local database and displayed contextually during gameplay.

- **Complete backend database structure:**  
  The database schema (based on Room) includes all core entities such as Player, Adventure, Quest, Hint, and tracking tables for sessions, progress, and hint usage.

- **Work in progress:**  
  Dynamic updates to the **Statistics Page** (based on live database data) are currently under development on the `dev` branche.

## Challenges and Solutions

- **Data flow complexity:**  
  It was initially difficult to maintain an overview of the data flow between entities (IDs, foreign keys, and relationships).  
  Integrating the **Statistics feature** required a structural refactor of the database and data layer to improve clarity and maintainability.


## User Stories
* As a user I want to be able to select different tracks, so that I can choose the type of experience I get
* As a user i want to be able to see my location in the app, so i can use it for navigation
* As a user I want to be able to see a picture of the place that i must find
* As a user I want to be able to get hints about the location, so that I am able to find the location if I cant find it right away
* As a user I want to be able to see information/statistics about already completed tracks, so I can see the locations I have visited
* As a user I want to be able to select tracks that are close to me, so that i don't have to walk to the other side of the city to start the track
* As a user I want to be shown a radius of the location if I ask for it, so that I can find the location even if I cant find it just based on the picture

## Wireframe
![Start Screen](https://github.com/user-attachments/assets/6bdcefe9-73f1-4e19-8872-60906d57eaad)
![Stat Screen](https://github.com/user-attachments/assets/45c91b35-8fe8-45b8-999d-882b400fc05d)
![Map Select Screen](https://github.com/user-attachments/assets/90bca45c-f532-406c-a6b0-f61ab415e4c7)
![Play Screen](https://github.com/user-attachments/assets/e7b0be11-cac4-4f83-9b79-df73f69280b6)
![Hint System](https://github.com/user-attachments/assets/0fa23cf8-97db-434c-a939-ddab2fe1a118)
![End of Game Stat Screen](https://github.com/user-attachments/assets/4e62fa43-7ba3-4e87-938d-6a5e22219d67)
