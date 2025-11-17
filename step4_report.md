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

## Build process for APK

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