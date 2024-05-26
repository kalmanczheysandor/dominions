'use strict';

function removeDisallowedCharacters(input, allowedSet) {
    // Create a regex pattern that matches any character not in the allowed set
    // The `^` inside the character class `[^...]` means "not any of these characters"
    let pattern = new RegExp(`[^${allowedSet}]`, 'g');

    // Use the replace method to remove all disallowed characters
    return input.replace(pattern, '');
}

function removeMultiplicatedSpaces(input) {
    // Create a regex pattern that matches two or more consecutive spaces
    let pattern = /\s{2,}/g;

    // Replace all occurrences of the pattern with a single space
    return input.replace(pattern, ' ');
}


function adjustCountryLabelPosition(countryIndex) {
    let countryNode = document.body.querySelector('[ffw-type="Country"][ffw-country-key="' + countryIndex + '"]');
    if (countryNode == null) {
        throw "node is null!";
    }

    let textNode = document.body.querySelector('[ffw-type="CountryLabel"][ffw-country-key="' + countryIndex + '"]');
    if (textNode == null) {
        throw "node is null!";
    }

    let newCoordinateX = null;
    let newCoordinateY = null;
    let turn = 0;

    let coordinatesString = countryNode.getAttribute("points")
    coordinatesString = removeDisallowedCharacters(coordinatesString, "0123456789 .,");
    coordinatesString = removeMultiplicatedSpaces(coordinatesString);

    let coordinatePairsList = coordinatesString.split(" ");
    console.log(coordinatePairsList);


    for (let coordinatePairString of coordinatePairsList) {
        if (coordinatePairString.length == 0) {
            continue;
        }

        console.log(coordinatePairString);

        if (coordinatePairString.split(",").length != 2) {// must be only one colon presented. in case of this the array split function result in a arry containing two item.
            throw "unable to detect x and y in coordinate pair string! The string is:" + coordinatePairString;
        }
        let coordinatePairArr = coordinatePairString.split(",");
        let coordinateX = coordinatePairArr[0];
        let coordinateY = coordinatePairArr[1];

        if (newCoordinateX != null && newCoordinateY != null) {
            newCoordinateX = parseInt(newCoordinateX) + parseInt(coordinateX);
            newCoordinateY = parseInt(newCoordinateY) + parseInt(coordinateY);
        } else {
            newCoordinateX = parseInt(coordinateX);
            newCoordinateY = parseInt(coordinateY);
        }

        turn++;
    }

    if (newCoordinateX != null && newCoordinateY != null) {

        console.log("Adjusted (" + turn + ")[" + newCoordinateX + "][" + newCoordinateY + "]");

        newCoordinateX = newCoordinateX / turn;
        newCoordinateY = newCoordinateY / turn;

        textNode.setAttribute("x", newCoordinateX);
        textNode.setAttribute("y", newCoordinateY);

    } else {
        console.log("not adjusted");
    }

}

function adjustCountryLabels() {
    for (let i = 0; i <= 41; i++) {
        adjustCountryLabelPosition(i);
    }
}


function joinToAGameAndLoad(sessionKey) {
    $.ajax({
        type: 'POST',
        url: '/web/map/' + sessionKey + '/join',
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify({}),
        beforeSend: function (xhr) {
            let csrfToken = $("meta[name='_csrf']").attr("content");
            let csrfHeader = $("meta[name='_csrf_header']").attr("content");
            xhr.setRequestHeader(csrfHeader, csrfToken); // Include CSRF token in request headers
        },
        success: function (response) {
            let url = '/web/map/' + sessionKey + '/play/' + response.playerIndex;
            window.location.href = url;
        },
        error: function (xhr, status, error) {
            // Handle error
            console.error("Error:", error);
        }
    });
}


function setCountryState(countryIndex, playerIndex, troopsSize) {
    let countryNode = document.body.querySelector('[ffw-type="Country"][ffw-country-key="' + countryIndex + '"]');
    if (countryNode == null) {
        throw "node is null!";
    }
    let countryLabelNode = document.body.querySelector('[ffw-type="CountryLabel"][ffw-country-key="' + countryIndex + '"]');
    if (countryLabelNode == null) {
        throw "node is null!";
    }

    if (playerIndex == null) {
        countryLabelNode.innerHTML = "";
        countryNode.style.fill = emptyCellData.color;
    } else {
        countryNode.style.fill = playerDataList[playerIndex].color;
        countryLabelNode.innerHTML = troopsSize;
    }
}

function refreshState(response) {
    console.log("refreshState:" + response.currentTurn);
    let players = response.players;
    for (const playerIndex in players) {
        players[playerIndex].occupiedCellCount = 0;
    }

    let cells = response.cells;
    let cellCount = Object.keys(cells).length

    for (const countryIndex in cells) {
        let cell = cells[countryIndex];
        if (cell.playerKey != null) {
            players[cell.playerKey].occupiedCellCount++;
        }

        setCountryState(countryIndex, cell.playerKey, cell.armySize);
    }

    // Draw players panel
    let playersTableStr = '';
    let rowsStr = '';
    for (const playerIndex in players) {
        let player = players[playerIndex];
        let occupiedCellCount = player.occupiedCellCount;
        if (occupiedCellCount == null || occupiedCellCount == undefined) {
            occupiedCellCount = 0;
        }

        rowsStr += '' +
            '<tr>' +
            '   <td style="border-left: 5px solid ' + playerDataList[playerIndex].color + ';">Player' + (parseInt(playerIndex) + 1) + '</td>' +
            '   <td>' + player.type + '</td>' +
            '   <td>' + player.reserveSize + '</td>' +
            '   <td>' + parseFloat(((occupiedCellCount / cellCount) * 100).toFixed()) + '%</td>' +
            '</tr>';
    }
    if (rowsStr.length > 0) {
        playersTableStr = '' +
            '<table>' +
            rowsStr +
            '</table>';
    }
    document.getElementById('PlayersPanel').innerHTML = playersTableStr;

    document.getElementById('TurnLabel').innerHTML = '#' + response.currentTurn;

}



const emptyCellData = {
    color: "grey"
}
const playerDataList = {
    0: {
        color: '#cc0000'
    },
    1: {
        color: '#009900'
    },
    2: {
        color: '#e67300'
    },
    3: {
        color: '#0099e6'
    }
}





function loadState(sessionKey) {
    $.ajax({
        type: 'GET',
        url: '/web/map/'+sessionKey+'/current',
        dataType: 'json',
        beforeSend: function (xhr) {
        },
        success: function (response) {
            refreshState(response)
        },
        error: function (error, message) {
            console.error(error);
            console.error(message);
        }
    });
};


function stopTimer() {
    document.getElementById('gfg')
        .innerHTML = " Timer stopped ";
    clearInterval(timer);
}


function emptyAllCountry() {
    let countryNodesList = document.body.querySelectorAll('[ffw-type="Country"]');
    countryNodesList.forEach(function (node) {
        node.style.fill = "grey";
    });
};


function occupyACountry(countryIndex) {
    let countryNode = document.body.querySelector('[ffw-type="Country"][ffw-country-key="' + countryIndex + '"]');
    if (countryNode == null) {
        throw "node is null!";
    }
    countryNode.style.fill = "red";
};

function selectCountryAsTarget(countryIndex) {
    document.getElementById('TargetInput').value = countryIndex;
}





