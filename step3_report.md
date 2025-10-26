# Step 3: API Integration

## Which API was chosen and why
We are using the Open-Meteo Weather API. Since our app is mainly used outdoors while players walk around, it is helpful to provide a quick way to check the weather at guest's location, allowing them to prepare for the conditions. Open-Meteo is free, requires no sign-in or API key, and provides real-time weather data based on latitude and longitude, making it an ideal choice for our location-based gameplay.

## Example API endpoint used
The app fetches weather data for the current quest’s location using a URL in the following format: <br>
https://api.open-meteo.com/v1/forecast?latitude={lat}&longitude={lon}&current_weather=true <br>
Where: <br>
{lat} -> latitude of the current quest location <br>
{lon} -> longitude of the current quest location <br>
current_weather=true -> ensures only the current weather is returned <br>

For example API request for Tartu Delta would be something like this: <br>
https://api.open-meteo.com/v1/forecast?latitude=58.377&longitude=26.729&current_weather=true

## Error handling strategy
**Loading State:** While fetching data, a loading spinner is shown in the weather popup.  
**Network/API Errors:** If the API call fails, a toast message notifies the player.  
**Fallback:** If weather information cannot be retrieved, a default “unknown” weather icon is displayed.  

This ensures that players can continue playing the game even if the weather data is temporarily unavailable.
