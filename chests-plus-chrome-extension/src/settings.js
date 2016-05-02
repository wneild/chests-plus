var masteryServerHost = 'chests.eu-west-1.elasticbeanstalk.com';

document.addEventListener('DOMContentLoaded', function() {
    restoreOptions();
    document.getElementById('save').addEventListener('click', saveOptions);
    document.getElementById('goback').addEventListener('click', goToMain);
});

// Makes an API request to check if the summoner name exists by asking for the summoner id from our mastery server
function getSummonerId(region, summonerName, callback, errorCallback) {
    var queryUrl = 'http://' + masteryServerHost + '/getSummonerId/region/' + encodeURIComponent(region) + '/summonerName/' + encodeURIComponent(summonerName);
    var x = new XMLHttpRequest();
    x.open('GET', queryUrl);
    x.onload = function() {
        // Parse and process the response from League Mastery Server
        var response = x.response;
        if (!response) {
            errorCallback('No response from League Mastery Server!');
            return;
        }
        if(x.status != 200) {
            errorCallback(response);
            return;
        }
        var summonerId = response;
        console.assert(
            typeof summonerId == 'string',
            'Unexpected response from the League Mastery Server!');
        callback(summonerId);
    };
    x.onerror = function() {
        errorCallback('Network error.');
    };
    x.send();
}

// Redirects to main view
function goToMain() {
    window.location.href = "main.html";
}

// Saves options to chrome.storage
function saveOptions() {
    var region = document.getElementById('region').value;
    var summoner = document.getElementById('summoner').value;

    if(region == '' || summoner == '') {
        renderStatus('Summoner name and region must be provided', false);
        return;
    }

    getSummonerId(region, summoner, function (summonerId) {
        chrome.storage.sync.set({
            region: region,
            summoner: summoner
        }, function() {
            showBackBtn();
            // Update status to let user know options were saved.
            renderStatus('Options saved.', true);
        });
    }, function(msg) {
        hideBackBtn();
        renderStatus(msg, false);
    });


}

// Populates for fields with our saved settings region and summoner name
function restoreOptions() {
    chrome.storage.sync.get({
        region: 'euw',
        summoner: ''
    }, function(items) {
        if(items.summoner == '') {
            hideBackBtn();
        }
        document.getElementById('region').value = items.region;
        document.getElementById('summoner').value = items.summoner;
    });
}

// Sets the status text with the provided message, if it is a successful message then the message is set to disappear
// after a short time, otherwise the message stays
function renderStatus(statusText, success) {
    var statusElement = document.getElementById('status');
    statusElement.textContent = statusText;
    if(success) {
        setTimeout(function () {
            statusElement.textContent = '';
        }, 750);
    }
}

function showBackBtn() {
    document.getElementById('goback').style.visibility = 'visible';
}

function hideBackBtn() {
    document.getElementById('goback').style.visibility = 'hidden';
}