# Step 4: Polishing + Testing + APK

## Testing strategy

We opted for regression tests to ensure that some of our core logic remains unchanged when
implementing new features or changing existing features. The idea is that we will quickly recognize,
if we break the existing user-flow and are able to identify the faulty code-change.

On the UI side we chose to test the happy path when opening the app (for the first time). Where the
user should be able to select a quest from the Quests-Screen and click on play on the Home-Screen
once the quest has been successfully loaded.

For unit tests, we decided to test our leveling service, which calculates the current level of a
user based on XP and how much XP should be gained from a Quest in regard to the amount of hints that
were used.

## Build process for APK (Build Configuration & Developer Mode)

The application supports a **Developer Mode** that enables additional debugging features, such as a "Dev-Panel" in the Home Screen and a "Continue Anyway" button in the game dialogs (useful for skipping quests without being at the physical location).

The Developer Mode is controlled via a boolean flag in the `build.gradle.kts` file.

- **Debug Builds:** Developer mode is enabled by default (`true`).
- **Release Builds:** Developer mode is disabled by default (`false`).

To manually toggle the Developer Mode for a specific build type, you can modify the `DEV_MODE` flag in `app/build.gradle.kts`:

```kotlin
buildTypes {
    release {
        // ...
        buildConfigField("boolean", "DEV_MODE", "false") // Set to true to enable in release
    }
    getByName("debug") {
        buildConfigField("boolean", "DEV_MODE", "true") // Set to false to disable in debug
    }
}
```
## Generating a Signed Release APK

To build a release version of the application (Signed APK) that can be installed on devices, follow these steps:

1. **Open the Build Menu**:   In Android Studio, navigate to the top menu bar and select **Build** > **Generate Signed Bundle / APK...**.

2. **Select APK**:
   Choose **APK** (instead of Android App Bundle) and click **Next**.

3. **Keystore Configuration**:
   You need a cryptographic key to sign the app.
   - **Existing Key**: If you already have a `.jks` file, select "Choose existing...", enter the path, and type in the Key Store Password and Key Password.
   - **New Key**: If this is your first time, click **Create new...**.
     - Choose a location to save the keystore file.
     - Set a strong password.
     - Fill in the "Certificate" details (at least one field like First/Last Name).
     - Click **OK**.

4. **Select Build Variant**:
   - Select the **release** build variant.
   - (Optional) Check the "V2 (Full APK Signature)" box if available.

5. **Finish**:
   Click **Create** or **Finish**. Android Studio will start building the APK.

6. **Locate the APK**:
   Once the build is complete, a notification will appear. Click **locate** or navigate to:
   `tartu-explorer/app/release/app-release.apk`

> **Note regarding Dev-Mode:**
> The status of the "Dev-Panel" depends on the `DEV_MODE` flag in `app/build.gradle.kts`.
> Ensure the `release` block is set to `false` (or `true` if you specifically want a Dev-Release) before generating the signed APK.

## Known bugs or limitations

- Whenever we add new adventures and quests, the user currently has to manually wipe their local
  data
  for the new quests to be loaded. Ideally, we would want them to be automatic as long as they are
  non-destructive.
- On smaller devices, it is possible that the quest screen becomes so large that it blocks the back
  button. And the user has to use swipe gestures or touch controls.
- Image fetching in offline mode fast-fails giving the false illusion of an ready-to-play adventure
- Weather info is displayed in offline-mode prompting an fetch error instead of just being hidden in
  the first place
