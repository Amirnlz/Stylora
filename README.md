# Stylora

Stylora is a modern Android application built with Jetpack Compose, designed to provide a seamless user experience for managing feedback, viewing history, and interacting with a dashboard.

## App Structure

The app is organized under the `app` directory with the following structure:

- **di**: General dependency injection modules.
- **navigation**: Navigation logic for the app.
- **pages**: Feature modules, including:
    - **dashboard**: Dashboard-related functionality.
    - **feedback**: Feedback-related functionality, including data sources and services.
    - **history**: History-related functionality.

Each feature module (`dashboard`, `feedback`, `history`) contains:
- **di**: Feature-specific dependency injection modules.
- **data**: Data sources and repositories.
- **domain**: Business logic and use cases.
- **ui**: UI components and ViewModels.

Note: Data sources and services are primarily tied to the `feedback` feature.

## Architecture

The app follows **Clean Architecture** combined with the **MVVM (Model-View-ViewModel)** pattern for a modular, testable, and maintainable codebase.

## Technologies Used

- **Retrofit + OkHttp**: For networking and API calls.
- **Dagger/Hilt**: For dependency injection.
- **Coil**: For loading and caching images from the network.
- **Jetpack Compose**: For declarative UI development.
- **kotlinx-serializable**: For serialization in navigation and API JSON handling.
- **navigation-compose**: For navigation within Jetpack Compose.
- **Flow and Coroutines**: For asynchronous programming.
- **Material3**: For modern UI components.

## Known Issues and Limitations

- **History API**: The "get history" API lacks pagination, which could cause performance issues with large datasets. Adding pagination to both the API and the Android app is recommended for future improvements.
- **Feedback Response JSON**: JSON keys in the feedback response are not following best practices:
    - Contain spaces (e.g., "Fit Score") instead of a standard format like `fit_score`.
    - Use camelCase (e.g., "FitScore") instead of the more conventional snake_case or kebab-case for JSON keys.