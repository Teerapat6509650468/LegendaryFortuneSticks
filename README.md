# Legendary Fortune Sticks

## Overview
Legendary Fortune Sticks is an Android application that utilizes various device sensors to generate random fortune predictions when the device is shaken. The app listens to multiple sensors, including accelerometer, gyroscope, gravity, and more, to detect movement and display a random fortune message.

## Features
- Uses **Accelerometer** to detect shakes and trigger fortune messages.
- Displays random fortune predictions from a predefined list.
- Supports various sensors such as **Gyroscope, Gravity, Light, Proximity, Magnetic Field, and Rotation Vector**.
- Monitors **Battery Information**, including health, level, charging status, and temperature.
- Keeps the screen awake using a **WakeLock**.
- Displays an animated **GIF** using Glide library.

## Requirements
- Android 6.0 (API Level 23) or higher
- Java Development Kit (JDK)
- Android Studio
- Glide Library for GIF support

## Installation
1. Clone the repository:
   ```sh
   git clone https://github.com/yourusername/LegendaryFortuneSticks.git
   ```
2. Open the project in **Android Studio**.
3. Build and run the app on an **Android device or emulator**.

## How It Works
### Shake Detection & Fortune Display
- The app continuously listens to accelerometer readings.
- If the device experiences a shake greater than the defined threshold (`shake_threshold = 15`), an **AlertDialog** pops up displaying a random fortune.
- The fortune number and message are fetched from `res/values/strings.xml`.

### Sensor Data Collection
- The app registers listeners for multiple sensors and updates the `SensorInfo` object with real-time sensor values.
- Supported sensors:
  - **Accelerometer** (Motion detection)
  - **Gyroscope** (Rotation tracking)
  - **Gravity** (Gravity force measurement)
  - **Magnetic Field** (Compass-based direction tracking)
  - **Light Sensor** (Ambient light detection)
  - **Proximity Sensor** (Object detection near the device)
  - **Linear Acceleration** (Acceleration excluding gravity)
  - **Rotation Vector** (Device orientation tracking)

### Battery Status Monitoring
- Uses a `BroadcastReceiver` to collect battery information such as:
  - **Health** (Good/Not Good)
  - **Level** (Percentage)
  - **Charging Type** (USB, AC, Unplugged)
  - **Status** (Charging, Discharging, Full)
  - **Temperature** (in °C)
  - **Voltage**

## Dependencies
```gradle
dependencies {
    implementation 'com.github.bumptech.glide:glide:4.11.0'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.11.0'
}
```

## Preview
https://github.com/user-attachments/assets/884f61de-974d-4b59-995d-f55ce0c79d21

