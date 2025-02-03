# Kindly App

## Overview
Kindly is a user-centric application designed to help individuals track and manage promises of assistance to others. Whether it's financial support, completing a task, or helping a community, Kindly ensures users remain organized and disciplined. The app offers features for tracking promises, rewarding fulfilled promises, and backing up user data securely using Firebase.

---

## Features
- **User Authentication**: Sign in or sign up securely using Firebase Authentication.
- **Track Promises**: Add, view, and manage promises to help individuals or communities.
- **Fulfillment Rewards**: Mark promises as fulfilled and earn rewards or badges.
- **Categories & Notes**: Categorize promises and add notes for additional context.
- **Reminders**: Set reminders for pending promises.
- **Data Backup**: Backup and restore data across devices with Firebase Firestore integration.
- **Notifications**: Receive updates for reminders, backups, or app updates.

---

## Tech Stack
- **Frontend**: Jetpack Compose for a modern and dynamic UI.
- **Architecture**: Clean Architecture with MVVM for separation of concerns.
- **Local Storage**: Room Database for offline storage.
- **Remote Storage**: Firebase Firestore for backups and synchronization.
- **Authentication**: Firebase Authentication for user sign-in/sign-up.
- **Dependency Injection**: Hilt for managing dependencies.
- **Other Libraries**:
  - Retrofit for API calls (if needed in future expansions).
  - Kotlin Coroutines for asynchronous tasks.
  - Glide or Coil for image loading.

---

## Folder Structure
```plaintext
📂 app
 ├── 📂 data
 │    ├── 📂 local (Room Database)
 │    ├── 📂 remote (Firestore)
 │    ├── 📂 repository (Repositories for data handling)
 │
 ├── 📂 domain
 │    ├── 📂 model (Data models)
 │    ├── 📂 usecase (Use cases for business logic)
 │
 ├── 📂 presentation
 │    ├── 📂 screens (Composable screens for UI)
 │    ├── 📂 viewmodel (ViewModels for state management)
 │
 ├── 📂 utils (Utility classes and helpers)
```

---

## Getting Started

### Prerequisites
- Android Studio Flamingo or later
- Minimum SDK: 21
- Firebase project setup with Authentication and Firestore enabled

### Setup Instructions
1. Clone the repository:
   ```bash
   git clone https://github.com/aliapochi/kindly.git
   cd kindly
   ```

2. Open the project in Android Studio.

3. Add your Firebase configuration file:
   - Place `google-services.json` in the `app/` directory.

4. Sync Gradle files and build the project.

5. Run the app on an emulator or a physical device.

---

## Usage
1. **Authentication**: Log in or sign up with an email and password.
2. **Add Promises**:
   - Navigate to the dashboard.
   - Click the "+" button to add a new promise.
   - Fill in the details and save.
3. **Fulfill Promises**: Mark a promise as fulfilled and view rewards.
4. **Backup Data**: Go to settings and manually trigger a backup or enable automatic backups.
5. **Restore Data**: Sign in on a new device to retrieve your saved data.

---

## Future Enhancements
- Add social features to share promises or collaborate on tasks.
- Introduce analytics to track user progress.
- Integrate in-app notifications for reminders and updates.

---

## License
This project is licensed under the [MIT License](LICENSE).

---

## Contribution
We welcome contributions to Kindly! If you'd like to contribute, please fork the repository, create a feature branch, and submit a pull request.

---

## Contact
For queries or feedback, please contact:
- **Email**: aliapochi@gmail.com
- **GitHub**: [aliapochi](https://github.com/yourusernamealiapochi)
