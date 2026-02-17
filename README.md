# OWeather (Owl Weather)

This project is a Kotlin Android app (Jetpack Compose) demonstrating a weather forecast app using Open-Meteo APIs.

Features implemented:
- Kotlin + AndroidX + Material3
- UI: Jetpack Compose
- Architecture: MVVM + Repository
- Coroutines + Flow
- DI: Hilt
- Network: Retrofit + OkHttp + Moshi
- Local storage: Room + DataStore (Room used for CRUD)
- Location: FusedLocationProviderClient
- Maps: Maps Compose stub (requires API key)
- Offline-first: caches last successful forecast in Room, with asset fallback `assets/weather_fallback.json`
- CRUD for cities (Room) — basic DAO and UI placeholders
- Logging: Timber

Where to add Google Maps API key:
- Add the key to `app/src/main/AndroidManifest.xml` as a `<meta-data android:name="com.google.android.geo.API_KEY" android:value="YOUR_KEY"/>` inside the `<application>` tag.

How to run:
- Open in Android Studio
- Build & run on device or emulator with Google Play services for location

Permissions:
- INTERNET
- ACCESS_COARSE_LOCATION
- ACCESS_FINE_LOCATION

Notes & next steps:
- `CitiesScreen`, `MapScreen` and full CRUD UI are placeholders; repository & DB are implemented and ready for UI wiring.
- Add Google Maps API key to enable map screen.
- Implement better error handling, animations and weather icon mapping.

*** End of README
