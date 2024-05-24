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


$(document).ready(function () {
    'use strict';

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

    let adjustCountryLabelPosition = function (countryIndex) {
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

    let setCountryState = function (countryIndex, playerIndex, troopsSize) {
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


    let refreshState = function (response) {
        console.log("refreshState:" + response.currentTurn);
        let players = response.players;
        for (const playerIndex in players) {
            players[playerIndex].occupiedCellCount=0;
        }

        let cells = response.cells;
        let cellCount =  Object.keys(cells).length

        for (const countryIndex in cells) {
            let cell = cells[countryIndex];
            if(cell.playerKey!=null) {
                players[cell.playerKey].occupiedCellCount++;
            }

            setCountryState(countryIndex, cell.playerKey, cell.armySize);
        }

        let playersTableStr = '';
        let rowsStr = '';
        for (const playerIndex in players) {
            let player = players[playerIndex];
            let occupiedCellCount = player.occupiedCellCount;
            if(occupiedCellCount==null || occupiedCellCount==undefined) {
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

        document.getElementById('PlayersPanel').innerHTML = playersTableStr

    }

    let current = function () {
        console.log("Call");
        $.ajax({
            type: 'GET',
            url: '/web/map/AAA-1/current',
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

    let timer;

    timer = setInterval(current, 5000);

    function stopTimer() {
        document.getElementById('gfg')
            .innerHTML = " Timer stopped ";
        clearInterval(timer);
    }


    let emptyAllCountry = function () {
        let countryNodesList = document.body.querySelectorAll('[ffw-type="Country"]');
        countryNodesList.forEach(function (node) {
            node.style.fill = "grey";
        });
    };


    let occupyACountry = function (countryIndex) {
        let countryNode = document.body.querySelector('[ffw-type="Country"][ffw-country-key="' + countryIndex + '"]');
        if (countryNode == null) {
            throw "node is null!";
        }
        countryNode.style.fill = "red";
    };

    for (let i = 1; i <= 42; i++) {
        adjustCountryLabelPosition(i);
    }
    emptyAllCountry();


    occupyACountry(39);


    let selectACountry = function(countryIndex) {
        document.getElementById('Target').value = countryIndex;
    }

    $(document).on('click', '[ffw-type="Country"]', function (e) {
            e.preventDefault();
            let self = this;
            try {
                let ffwCountryKey = self.getAttribute("ffw-country-key");
                if (ffwCountryKey == null) {
                    throw "The ffw-country-key is not defined!";
                }


                selectACountry(ffwCountryKey);

            } catch (exp) {
                console.error(exp)
            }
        }
    );

    $(document).on('mouseover', '[ffw-type="Country"]', function (e) {
            e.preventDefault();
            let self = this;
            try {
                let ffwCountryKey = self.getAttribute("ffw-country-key");
                if (ffwCountryKey == null) {
                    throw "The ffw-country-key is not defined!";
                }
                self.style.backgroundColor = "green";
                document.getElementById('PointedCountry').innerHTML = 'Pointed country key:' + ffwCountryKey;
            } catch (exp) {
                console.error(exp)
            }
        }
    );

    $(document).on('mouseover', '#AttackButton', function (e) {
            e.preventDefault();
            let self = this;
            try {
                let ffwCountryKey = self.getAttribute("ffw-country-key");
                if (ffwCountryKey == null) {
                    throw "The ffw-country-key is not defined!";
                }
                self.style.backgroundColor = "green";
                document.getElementById('PointedCountry').innerHTML = 'Pointed country key:' + ffwCountryKey;
            } catch (exp) {
                console.error(exp)
            }
        }
    );
});








