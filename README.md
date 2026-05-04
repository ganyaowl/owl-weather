# Weather Forecast App (OWeather)

Android-приложение прогноза погоды на Kotlin + Jetpack Compose.

## 1. Выполнение требований

### Базовые требования
- Kotlin + AndroidX + Material3: выполнено.
- UI: Jetpack Compose: выполнено.
- MVVM + Repository + Coroutines + Flow: выполнено.
- DI: Hilt: выполнено.
- Network: Retrofit + OkHttp + Gson: выполнено.
- Local storage: Room + DataStore: выполнено.
- Location: FusedLocationProviderClient: выполнено.
- Maps: OpenStreetMap через osmdroid: выполнено.
- Weather API: Open-Meteo (без ключа): выполнено.

### Экраны
- Splash -> Main: реализовано.
- Main: текущая погода, 7-дневный прогноз, кнопки обновления/города/карта: реализовано.
- Cities: CRUD, поиск через geocoding, сохранение в Room: реализовано.
- City details: погода выбранного города: реализовано.
- Map: текущая позиция + маркеры городов на OpenStreetMap: реализовано.
- Анимации: fade/scale на Splash, анимированное появление прогноза: реализовано.

### CRUD (Room)
- Таблица `cities`: `id, name, latitude, longitude, note, createdAt`.
- Таблица `weather_cache`: кэш прогноза по `cityKey` (включая `current_location`).
- Create/Read/Update/Delete для городов: реализовано.

### Offline-first
- При запуске и просмотре городов читается локальный кэш из Room.
- После отображения кэша выполняется обновление из сети.
- При сетевой ошибке используется локальный кэш Room, если он уже был сохранен.

### Permissions
- `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`, `INTERNET` в `AndroidManifest.xml`.
- Runtime permission для локации реализован.
- При отказе приложение работает через ручной выбор города.

## 2. Архитектура

Слои:
- `presentation`: Compose экраны + ViewModel.
- `domain`: интерфейсы репозиториев + контракт локации.
- `data`: Room DAO/entities, Retrofit API, репозитории, DataStore.
- `di`: Hilt-модули.

Паттерны:
- MVVM
- Repository
- Dependency Injection
- Offline-first cache strategy

## 3. Использованные темы (8+)
- Jetpack Compose
- Navigation Compose
- Material3
- Hilt DI
- Retrofit + OkHttp
- Room
- DataStore Preferences
- Coroutines + Flow
- Fused Location Provider
- OpenStreetMap / osmdroid
- JSON parsing (Gson)
- Open-Meteo JSON parsing

## 4. Как запустить

1. Откройте проект в Android Studio.
2. Убедитесь, что установлен Android SDK для `compileSdk 36`.
3. Sync Gradle.
4. Соберите проект:
   ```bash
   ./gradlew :app:assembleDebug
   ```
5. Запустите на эмуляторе/устройстве (Android 13+, `minSdk 33`).

## 5. Карта

Карта работает через OpenStreetMap/osmdroid и не требует Google Maps API Key.

## 6. Важные файлы

- `app/src/main/java/com/example/oweather/navigation/WeatherApp.kt`
- `app/src/main/java/com/example/oweather/presentation/main/MainScreen.kt`
- `app/src/main/java/com/example/oweather/presentation/cities/CitiesScreen.kt`
- `app/src/main/java/com/example/oweather/presentation/citydetails/CityDetailsScreen.kt`
- `app/src/main/java/com/example/oweather/presentation/map/MapScreen.kt`
- `app/src/main/java/com/example/oweather/data/repository/WeatherRepositoryImpl.kt`
- `app/src/main/java/com/example/oweather/data/local/AppDatabase.kt`
