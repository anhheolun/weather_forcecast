Weather Forecast

In this project I follow SOLID principle, I’m using observer, adapter, strategy pattern.

Code folder structure:

The source code follow clean architecture to develop project. Please follow below code folder to know better:

Root folder
	- app								module is presentation layer that interact with UI
		- delegate						contain delegate class
			- main						contain all class or folder relate to main activity
			- weather					contain all class or folder relate to weather feature
				- search				contain all class or folder relate to search weather feature
		- di								contain dependence injection class
		- extension					contain extension injection
		- feature 						contain all features in app
		- util							contain contants or util class
	- data								module is data layer that handle storing and get data
		- converter					contain retrofit converter factory class
	 	- interceptor					contain http interceptor class
		- mapper						contain mapper class
		- model						contain data model class
		- repository					contain repository class
		- service						contain retrofit service class
	- domain							module is domain layer that contain business logic
		- model						contain business model class
		- repository					contain repository interface
		- usecase						contain usecase class

Libraries:

- Rootbeer: check rooted device
- Retrofit: http client library
- Hilt: dependency injection library
- Navigation: fragment navigation

How to run project:

Open android studio -> select file menu -> select open -> open project folder -> after sync gradle successfully -> click run icon in toolbar
Note: We cannot run app on simulator because I applied rooted device check for simulator It won’t passed this check.

Checklist:

1. The application is a simple Android application which is written by Java/Kotlin. —> Done
2. The application is able to retrieve the weather information from OpenWeatherMaps API. —> Done
3. The application is able to allow user to input the searching term. —> Done
4. The application is able to proceed searching with a condition of the search term length must be from 3 characters or above.  —> Done
5. The application is able to render the searched results as a list of weather items. —> Done
6. The application is able to support caching mechanism so as to prevent the app from generating a bunch of API requests. —> Done
7. The application is able to manage caching mechanism & lifecycle. —> Done
8. The application is able to handle failures. —> Done
9. The application is able to support the disability to scale large text for who can't see the text clearly. —> Done
10. The application is able to support the disability to read out the text using VoiceOver controls. —> Done
