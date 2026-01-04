# 📖 Kindle Desktop Reader (JavaFX)

A comprehensive desktop application designed to mimic the E-Book reading experience. This project demonstrates proficiency in **Object-Oriented Programming (OOP)**, **Database Management (SQLite)**, and **GUI Design** using JavaFX.

The application allows users to read text files as paged books, rate them, listen to background audio, and receive daily inspirational quotes via a live API connection.

## 🚀 Key Features

### 🛠️ Core Functionality
* **Dynamic Book Import:** Automatically detects and imports `.txt` files from a local directory into the library.
* **Smart Pagination:** Custom logic to split raw text into readable pages (600 characters per page).
* **Rating System:** Persists user ratings for each book using a local **SQLite Database**.
* **Audio Player:** Built-in media player to toggle background music/ambiance while reading.

### 🎨 User Interface (UI)
* **Modern Design:** Custom CSS styling to mimic the "Amazon Kindle" aesthetic (Georgia font, clean layout).
* **Interactive Controls:** Smooth navigation (Next/Prev), dropdown book selection, and real-time page counters.

### 🔥 Advanced & Bonus Features (API Integration)
* **Multithreading:** Utilizes background threads to prevent UI freezing during heavy operations.
* **REST API Client:**
    * *Implemented by Abdallah Salah:* Integrated a live connection to the `kanye.rest` API.
    * Fetches and parses JSON data asynchronously to display a unique quote on the dashboard every time the app launches.

## 👥 Team & Contributions

This project was a collaborative effort to build the core desktop application (UI, Database, and Logic). However, the **Live API Integration** feature was developed independently as an advanced enhancement.

| Team Member | Role |
| :--- | :--- |
| **Abdallah Salah Elsheikh** | **Core Developer & API Specialist:** Co-developed the main application and solely implemented the multithreaded REST API client for fetching live quotes. |
| **Emad-Eldeen Essam** | **Core Developer:** Co-developed the main application, focusing on system architecture, object-oriented design, and media handling. |

## 🛠️ Tech Stack
* **Language:** Java (JDK 17+)
* **Framework:** JavaFX (FXML & CSS)
* **Database:** SQLite (JDBC Driver)
* **Networking:** `HttpURLConnection` for API calls
* **Concepts:** MVC Pattern, DAO Pattern, Multithreading, File I/O.

## 💻 Code Highlight (API Integration)
The application fetches data asynchronously to ensure a smooth user experience. Below is the logic used for the REST API call:

```java
// Logic by Abdallah Salah
new Thread(() -> {
    try {
        URL url = new URL("[https://api.kanye.rest/](https://api.kanye.rest/)");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        
        Scanner scanner = new Scanner(url.openStream());
        String response = scanner.useDelimiter("\\A").next();
        scanner.close();
        
        // Update UI on JavaFX Application Thread
        Platform.runLater(() -> quoteLabel.setText(parseJson(response)));
        
    } catch (Exception e) {
        e.printStackTrace();
    }
}).start();
