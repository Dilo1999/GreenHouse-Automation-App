# 🌿 GreenHouse Automation App

A comprehensive mobile solution for smart greenhouse management. This Android app connects with IoT devices and Firebase to automate and monitor greenhouse operations. It includes predictive analytics to forecast the market trends of vanilla plants cultivated inside the greenhouse.

---

## 📱 Overview

The GreenHouse Automation App is designed to help users maintain and manage greenhouses both before and after construction. It facilitates real-time sensor monitoring, environmental control, and intelligent insights through:

- **IoT Integration:** Controls and monitors hardware inside the greenhouse (e.g., temperature, humidity).
- **Firebase Integration:** Stores additional data and user authentication.
- **Market Forecasting:** Uses machine learning to predict future market trends for vanilla plants.

---

## 🧩 Project Structure

The project consists of **three core components**:

1. **IoT Device**
   - Collects real-time environmental sensor data (temperature, humidity, soil moisture, etc.)
   - Sends data to the mobile app via a REST API
   - Receives control commands (e.g., turning fans on/off)

2. **Mobile App (Android, Java)**
   - Built using Android Studio with Java
   - Displays sensor data from the IoT device
   - Provides manual/automatic control over the greenhouse environment
   - Includes user interface for maintenance planning (pre- and post-construction)
   - Predicts vanilla market trends using integrated ML model

3. **Model**
   - Machine learning model for vanilla market forecasting
   - Can be deployed locally or using cloud platforms
   - Integrated via API or local inference

---

## 🔧 Features

- 🌡️ Real-time sensor monitoring
- 💡 IoT device control (Fan, Sprinkler, Light)
- ☁️ Firebase-based data storage and user management
- 🧠 Vanilla market prediction engine
- 🏗️ Pre and post-construction maintenance planner
- 📊 Visual dashboards for analysis

---

## 🛠️ Technologies Used

- **Android Studio (Java)**
- **Firebase**
- **RESTful APIs**
- **IoT (Arduino/ESP32, etc.)**
- **ML Model (Python/Cloud API)**
- **JSON/XML for data exchange**

---

## 🚀 Getting Started

### Prerequisites

- Android Studio installed
- Firebase project configured
- Access to the IoT device (or simulated environment)
- API endpoints set up to receive/send sensor data

### Installation

1. Clone the repository:
```bash
git clone https://github.com/Dilo1999/GreenHouse-Automation-App.git
