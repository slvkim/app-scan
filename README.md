# UI and Sensor Data Tracking Library

This library provides a comprehensive solution for tracking user interactions and gathering sensor data within an Android application. It includes the following core functionalities:

1. **UI Scanning**: The library scans the UI of the application to obtain a detailed View Tree map, capturing information such as element sizes, positions, IDs, types, and more.
2. **Sensor Data Gathering**: It collects data from various sensors, such as the accelerometer and gyroscope, to track device movement and orientation.
3. **User Interaction Tracking**: The library tracks user clicks across the screen, capturing interactions with any supported UI element.
4. **Data Serialization**: Upon each user interaction, all gathered information (from UI scanning, sensor data, and user clicks) is serialized into a JSON format for further processing or analysis.

## Key Features

- **SOLID Principles & Clean Code**:
    - The library is designed with maintainability and extensibility in mind.
    - It adheres to the SOLID principles, ensuring that each feature has a single responsibility.
    - Abstractions are separated from implementations using small, focused interfaces.
    - For instance, recognizing the differences between Android View and Compose, the library is structured to potentially support both, with separate paths for UI scanning.

- **Modular Structure**:
    - Each feature is encapsulated within its own entity:
        - `ScannerRepository`: Handles the UI scanning process.
        - `SensorRepository`: Gathers and processes sensor data.
        - `UserActionRepository`: Sets up listeners on UI elements to track user interactions.
        - `SerializerRepository`: Converts the collected data into a serialized JSON format.
    - This modularity allows for easy expansion and customization.

## Potential Improvements

The current implementation is a solid foundation, but there is room for enhancement in the following areas:

1. **Dependency Injection (DI)**:
    - Introduce a DI framework to better manage dependencies across the library.
    - Consider creating a custom DI framework tailored to the library's needs.

2. **Optimized Sensor Data Gathering**:
    - Further research into mathematical formulas for calculating optimized alpha values and normalization techniques to improve sensor data accuracy and efficiency.

3. **Enhanced Architecture**:
    - Refine the architecture to promote even more encapsulation and secure code.
    - Break down features into independent modules to facilitate better code management and reuse.

4. **Efficient View Tree Traversal**:
    - Explore more efficient algorithms or methods to traverse the view tree, reducing overhead and improving performance.

## Time Investment

This project took approximately one full working day to develop. Given the limited time, there is significant room for further optimization and improvement.

## Getting Started

To use this library, simply initialize the `AppScanner` wrapper by passing a reference to the current application. The rest of the process is handled automatically under the hood.

### Example Usage

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppScannerBuilder.inject(this)
    }
}
