var masteryServerHost = 'chests.eu-west-1.elasticbeanstalk.com';

document.addEventListener('DOMContentLoaded', function() {
    renderNextChampion();
    document.getElementById('goback').addEventListener('click', goToSettings);
});

// Gets and renders champion information and image if the API request is succesful, otherwise shows an error
// describing what went wrong
function renderNextChampion() {
    getOptions(function (items) {
        var region = items.region;
        var summoner = items.summoner;
        if(summoner == '') {
            goToSettings();
            return;
        }
        getNextChampion(region, summoner, function(championName, championImageUrl) {
            setChampion(championName, championImageUrl);
        }, function(msg) {
            showError(msg);
        });
    })
}

// Makes an API request to our mastery server using the provided region and summoner name to determine the next best champion to get a chest with
function getNextChampion(region, summonerName, callback, errorCallback) {
    var queryUrl = 'http://' + masteryServerHost + '/getNextChampionChest/region/' + encodeURIComponent(region) + '/summonerName/' + encodeURIComponent(summonerName);
    var x = new XMLHttpRequest();
    x.open('GET', queryUrl);
    x.responseType = 'json';
    x.onload = function() {
        // Parse and process the response from League Mastery Server
        var response = x.response;
        if (!response) {
            errorCallback('No response from League Mastery Server!');
            return;
        }
        if(x.status != 200) {
            errorCallback("Configured summoner not eligible to earn chests currently");
            return;
        }
        var championName = response.name;
        var championImageUrl = response.imageUrl;
        console.assert(
            typeof championName == 'string' && typeof championImageUrl == 'string',
            'Unexpected response from the League Mastery Server!');
        callback(championName, championImageUrl);
    };
    x.onerror = function() {
        errorCallback('Network error.');
    };
    x.send();
}

// Gets our saved settings region and summoner name and calls the callback parameter with them as arguments
function getOptions(callback) {
    chrome.storage.sync.get({
        region: 'euw',
        summoner: ''
    }, callback);
}

// Redirects to settings view
function goToSettings() {
    window.location.href = "settings.html";
}

// Populates elements with champion information
function setChampion(championName, imageUrl, championHighestGrade) {
    var championImageElement = document.getElementById('championImg');
    var championNameElement = document.getElementById('championName');
    championImageElement.src = imageUrl;
    championNameElement.textContent = championName;
    showChampionInfo();
}

// Hides the spinner if it is shown and shows the champion information
function showChampionInfo() {
    hideSpinner();
    document.getElementById('champion').style.display = 'block';
}

// Hides the spinner if it is shown and shows a messsage in the error message element
function showError(msg) {
    hideSpinner();
    var errorMsgElement = document.getElementById('errorMsg');
    errorMsgElement.style.display = 'block';
    errorMsgElement.textContent = msg;
}

// Hides the spinner if it is shown
function hideSpinner() {
    document.getElementById('spinner').style.display = 'none';
}