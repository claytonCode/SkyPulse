# Fix SerializationException for 'Home' class

The application is crashing with a `SerializationException` because the `Home` class, used as a navigation destination, is not being correctly serialized. This is due to the `kotlinx-serialization` compiler plugin not being applied and a wrong import in the `AppNavHost.kt` file.

## User Review Required

> [!IMPORTANT]
> I will be adding the `kotlinx-serialization` plugin and its JSON library dependency. This is required for Type Safe Navigation in Compose.

## Proposed Changes

### Build Configuration

#### [MODIFY] [libs.versions.toml](file:///C:/Users/Clayton/AndroidStudioProjects/SkyPulse/gradle/libs.versions.toml)
- Add version for `kotlinxSerialization`.
- Add library definition for `kotlinx-serialization-json`.
- Add plugin definition for `kotlin-serialization`.

#### [MODIFY] [build.gradle.kts (root)](file:///C:/Users/Clayton/AndroidStudioProjects/SkyPulse/build.gradle.kts)
- Register the `kotlin-serialization` plugin.

#### [MODIFY] [build.gradle.kts (app)](file:///C:/Users/Clayton/AndroidStudioProjects/SkyPulse/app/build.gradle.kts)
- Apply the `kotlin-serialization` plugin.
- Add `kotlinx-serialization-json` dependency.

### Navigation

#### [MODIFY] [AppNavHost.kt](file:///C:/Users/Clayton/AndroidStudioProjects/SkyPulse/app/src/main/java/com/example/skypulse/view/navigation/AppNavHost.kt)
- Correct the import for `Home` to point to the `Destinations.kt` definition instead of `Key.Companion.Home`.

## Verification Plan

### Automated Tests
- I will run `./gradlew :app:assembleDebug` to ensure the project builds correctly with the new plugin.

### Manual Verification
- The user can run the app and verify that the `HomeScreen` is displayed without the `SerializationException`.
