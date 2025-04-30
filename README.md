# ⏱️ Focus+ — A Pomodoro & Productivity Timer App
![image](https://github.com/user-attachments/assets/e93077eb-6e16-4cda-b1f1-97feab2fa585)

![image](https://github.com/user-attachments/assets/9c80cf36-387b-488a-8873-ce442e18bae1)

**Focus+** is a backend-powered web application built using **Scala with Play Framework** and **MongoDB**.  
It allows users to track and manage focused work sessions using popular productivity techniques like **Pomodoro**, **Time Boxing**, and **Feynman Method**.

Sessions are recorded, stored, and displayed with details including **duration**, **laps**, and **streaks**.

---

## 🧠 Features

- 🕐 **Session Tracking:** Start, stop, and record focus sessions with timestamps
- 🔁 **Lap & Streak Counters:** Built-in productivity feedback system
- 🎯 **Modes Supported:** Pomodoro, Time Boxing, Feynman
- 🧾 **Session History:** Displays all previous sessions with full stats
- 🍪 **User Tracking via Cookies:** Keeps data per user across sessions
- 🧰 **MongoDB Integration:** All sessions stored in a NoSQL database

---

## ⚙️ Technologies Used

| Layer | Tech |
|--|--|
| Language | Scala |
| Framework | Play Framework |
| Database | MongoDB (via MongoDB Scala Driver) |
| Frontend | HTML + Twirl Templates |
| JSON Handling | `play.api.libs.json._` |
| DateTime | Java Time API |

---



![image](https://github.com/user-attachments/assets/ca79d357-a4ba-4118-a66e-35a07800e996)

![image](https://github.com/user-attachments/assets/00801b55-c1f7-4b60-9060-e796870a9cfd)

## ▶️ How to Run

1. **Ensure MongoDB is running** on your local machine.
2. Set your `application.conf` to include:
   ```hocon
   mongodb.uri = "mongodb://localhost:27017"
   mongodb.database = "focusplus

Run the app using sbt:
sbt run
