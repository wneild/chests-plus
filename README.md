# Chests+ Chrome Extension and League of Legends Champion Mastery Server #
==============================================================

## Chests+ Chrome Extension ##
A chrome extension to assist League of Legends players choosing their next champion to play in order to have the best chance of getting a Hextech Crafting chest.

### Chrome Web Store Installation ###
Visit [the chrome webstore](https://chrome.google.com/webstore/detail/chests%20/mdcgedghcdekikfiipnlgniigddifpnf) and click the "Add To Chrome" button.

### Manual Installation ###
If you are running the Master Server yourself, you will first need to navigate to the files:

{project.directory}\chests-plus-chrome-extension\src\main.js

{project.directory}\chests-plus-chrome-extension\src\settings.js

Change the variable named "masteryServerHost" to the domain you are running the server (typically this will be localhost:8080) in both files.

Using Google Chrome navigate to chrome://extensions and make sure "Developer mode" is checked in the top right of the page.

Click the "Load unpacked extension..." button in the top left of the page and select the following folder:

{project.directory}/chests-plus-chrome-extension/src

### Usage ####
Whilst in Google Chrome and having installed the extension, click the chest icon in the top right of your window (if it isn't there, find it in the options menu).

Fill in your summoner name and the region you are registered to with that name and click the "Save" button.

Click "Go Back" and after a short time you should see the champion that is recommended you play next to have the best chance of getting a chest.

## League of Legends Champion Mastery Server ##
A server written in Java with Spring Boot that makes requests to Riot Games APIs on behalf of the chrome extension to determine summoner and champion mastery information.

### Available Endpoints ###
Below are the endpoints available to call through the server:

#### /getSummonerId/region/{region}/summonerName/{summonerName} ####
This will return the summoner id for the summoner name in the provided region in plain text
Example: 

	Calling /getSummonerId/region/euw/summonerName/Minion
	
	Returns 19596625
	
#### /getNextChampionChest/region/{region}/summonerName/{summonerName} ####
This will return a recommended champion specific to the summoner provided and based on their highest mastery points and chest availability
Example:

	Calling /getNextChampionChest/region/euw/summonerName/minion
	
	Returns
	
	```
	{
		"name": "Blitzcrank",
		"imageUrl": "https://ddragon.leagueoflegends.com/cdn/6.9.1/img/champion/Blitzcrank.png",
		"highestGrade": "A+"
	}
	```

### Hosted Solution ###
The server is deployed and hosted on Amazon webservices at the following domain:

http://chests.eu-west-1.elasticbeanstalk.com/

### Manual Deployment ###
The server is built using Maven. To build the server, navigate to the root of the server module {project.directory}/league-mastery-server and execute:
mvn clean package

To run the server, locate the built .jar inside {project.directory}/league-mastery-server/target and run from the command line like so, replacing values surrounded by {}:

java -jar -Driot.api-key={your-riot-api-key} league-mastery-server-{artifact-version}.jar

Note that the server will not function correctly if you do not provide a legitimate, working Riot API key

