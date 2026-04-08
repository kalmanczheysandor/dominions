<template>
    <nav id="ActionBar" style="height:60px;position:relative;">
        <div id="ActionBox">
            <button id="ReserveButton" class="btn btn-basic" type="button">
                Reserve
            </button>

            <input id="TargetInput" type="text" placeholder="Target">
            <input id="TroopsInput" type="text" placeholder="Troops">

            <button id="AttackButton" class="btn btn-basic" type="button">
                Attack
            </button>
        </div>
        <a id="ExitButton" href="#">EXIT</a>
    </nav>
    <div id="PlayersPanel">
        <table>
            <tr>
                <td>Player1</td>
                <td>Human</td>
                <td>50</td>
                <td>60%</td>
            </tr>
            <tr>
                <td>Player1</td>
                <td>Human</td>
                <td>50</td>
                <td>60%</td>
            </tr>
            <tr>
                <td>Player1</td>
                <td>AI</td>
                <td>50</td>
                <td>60%</td>
            </tr>
        </table>

    </div>


    <div id="TurnLabel"></div>
    <div style="border:2px red solid;display:table-row;padding:0px;margin:0px;">
        <svg style="">

            <g transform="translate(0,0)">
                <g transform="scale(1.0,1.0)">
                    <!--LINES IN THE BACKGROUND-->
                    <g transform="translate(0,0)">
                        <g transform="scale(1.0,1.0)">
                            <!--MERIDIAN LINES-->
                            <line v-for="(meridianItem, meridianIndex) in modelValue.background.meridians" :key="'Background-MeridianLine-'+meridianIndex" :x1="meridianItem.x1" :y1="meridianItem.y1" :x2="meridianItem.x2" :y2="meridianItem.y2" style="stroke:#0066ff;stroke-width:1"/>
                            <!--PARALLEL LINES-->
                            <line v-for="(parallelItem, parallelIndex) in modelValue.background.parallels" :key="'Background-ParallelsLine-'+parallelIndex" :x1="parallelItem.x1" :y1="parallelItem.y1" :x2="parallelItem.x2" :y2="parallelItem.y2" style="stroke:#0066ff;stroke-width:1"/>
                        </g>
                    </g>

                    <!--CONTINENTS AND COUNTRIES-->
                    <g transform="translate(+250,0)">
                        <g transform="scale(0.8,0.8)">
                            <!-- CONTINENT OUTLINE -->
                            <polygon v-for="(continentItem, continentIndex) in modelValue.continents " :key="'ContinentOutline-'+continentIndex" class="Continent" :points="continentItem.points"/>

                            <!--LINKS-->
                            <line v-for="(lineItem, lineIndex) in modelValue.lines" :key="'Line-'+lineIndex" :x1="lineItem.x1" :y1="lineItem.y1" :x2="lineItem.x2" :y2="lineItem.y2" stroke="#003d99" stroke-width="5" stroke-dasharray="5"/>

                            <!--COUNTRIES-->
                            <polygon v-for="(countryItem, countryIndex) in modelValue.countries" :key="'Country-'+countryIndex" ffw-type="Country" :ffw-country-key="countryItem.countryKey" :class="countryItem.classes" :points="countryItem.points" :ref="'Country-' + countryItem.countryKey"/>
                            <!--                            <polygon v-for="(countryItem, countryIndex) in modelValue.countries" :key="'Country-'+countryIndex" ffw-type="Country" :ffw-country-key="countryItem.countryKey"  :points="countryItem.points" :ref="'Country-' + countryItem.countryKey"/>-->

                            <!--COUNTRY LABELS-->
                            <text v-for="(countryLabelItem, countryLabelIndex) in modelValue.countryLabels" :key="'CountryLabel-'+countryLabelIndex" ffw-type="CountryLabel" :ffw-country-key="countryLabelItem.ownerCountryKey" :x="countryLabelItem.x" :y="countryLabelItem.y" class="CountryLabel" :ref="'CountryLabel-' + countryLabelItem.labelKey">
                                {{countryLabelItem.armySize}}
                            </text>
                        </g>
                    </g>

                </g>
            </g>
        </svg>


        <div id="CellIndex"></div>

    </div>

</template>

<script>
    import gameLobbyService from "@/services/game/lobby/GameLobbyService";
    import TController from "@/framework/TController";
    import TConfirmDialog from "@/framework/component/TConfirmDialog/TConfirmDialog";
    import TString from "@/framework/utils/TString";
    import TGameError from "@/components/GameBoard/TGameError";

    export default {
        props: {
            modelValue: {
                type: Array,
                default: () => [],
            },
        },
        computed: {},
        data() {
            return {};
        },
        methods: {
            init() {
                this.adjustCountryLabels();
                this.refreshBoardState();
            },


            adjustCountryLabels() {
                for(const labelItem of this.modelValue.countryLabels) {
                    this.adjustCountryLabelPosition(labelItem);
                }
            },
            adjustCountryLabelPosition(labelItem) {

                const countryItem = this.modelValue.countries.find(country => country.countryKey === labelItem.ownerCountryKey)
                if(countryItem == undefined) {
                    throw new TGameError('Country could not be found!');
                }

                let newCoordinateX = null;
                let newCoordinateY = null;
                let turn = 0;

                let coordinatesString = countryItem.points;
                coordinatesString = TString.removeDisallowedCharacters(coordinatesString, "0123456789 .,");
                coordinatesString = TString.removeMultiplicatedSpaces(coordinatesString);

                let coordinatePairsList = coordinatesString.split(" ");
                console.log(coordinatePairsList);


                for(let coordinatePairString of coordinatePairsList) {
                    if(coordinatePairString.length == 0) {
                        continue;
                    }

                    console.log(coordinatePairString);

                    if(coordinatePairString.split(",").length != 2) {// must be only one colon presented. in case of this the array split function result in a arry containing two item.
                        throw new TGameError('Unable to detect x and y in coordinate pair string! The string is:' + coordinatePairString);
                    }
                    let coordinatePairArr = coordinatePairString.split(",");
                    let coordinateX = coordinatePairArr[0];
                    let coordinateY = coordinatePairArr[1];

                    if(newCoordinateX != null && newCoordinateY != null) {
                        newCoordinateX = parseInt(newCoordinateX) + parseInt(coordinateX);
                        newCoordinateY = parseInt(newCoordinateY) + parseInt(coordinateY);
                    } else {
                        newCoordinateX = parseInt(coordinateX);
                        newCoordinateY = parseInt(coordinateY);
                    }

                    turn++;
                }

                if(newCoordinateX != null && newCoordinateY != null) {

                    console.log("Adjusted (" + turn + ")[" + newCoordinateX + "][" + newCoordinateY + "]");

                    newCoordinateX = newCoordinateX / turn;
                    newCoordinateY = newCoordinateY / turn;

                    labelItem.x = newCoordinateX;
                    labelItem.y = newCoordinateY;

                } else {
                    console.log("not adjusted");
                }

            },

            countOccupiedCells(playerIndex) {
                let count = 0;

                Object.entries(this.modelValue.cells).forEach((cellItem) => {
                    if(cellItem.playerKey === playerIndex) {
                        count++;
                    }
                });
                return count;
            },

            refreshBoardState() {

                // Counting occupied cells
                Object.entries(this.modelValue.players).forEach(([playerIndex, playerItem]) => {
                    playerItem.occupiedCellCount = this.countOccupiedCells(playerIndex);
                });

                //
                this.refreshAllCountryState();

                // // Draw players panel
                // let totalCellCount = Object.keys(response.cells).length
                // refreshPlayersTable(players, totalCellCount)
                //
                // // Refresh turn label
                // document.getElementById('TurnLabel').innerHTML = '#' + response.currentTurn;

            },

            refreshAllCountryState() {
                Object.entries(this.modelValue.cells).forEach(([cellIndex, cellItem]) => {
                    const countryIndex = cellIndex;
                    const labelData = this.findBelongingLabelDataOfCountry(countryIndex)
                    const countryData = this.findCountryData(countryIndex)
                    labelData.armySize = cellItem.armySize;

                    let countryClassAttribute = {'Country': true};
                    if(cellItem.playerKey !== null && cellItem.armySize > 0) {
                        countryClassAttribute['Player' + cellItem.playerKey] = true;
                    }
                    countryData.classes = countryClassAttribute;


                });
            },

            // refreshPlayersTable(players, totalCellCount) {
            //     let playersTableStr = '';
            //     let rowsStr = '';
            //     for(const playerIndex in players) {
            //         let player = players[playerIndex];
            //         let occupiedCellCount = player.occupiedCellCount;
            //         if(occupiedCellCount == null || occupiedCellCount == undefined) {
            //             occupiedCellCount = 0;
            //         }
            //
            //         let style1Str = 'border-left: 5px solid ' + playerDataList[playerIndex].color + ';';
            //         if(player.alive === false) {
            //             style1Str += 'text-decoration:line-through;';
            //         }
            //
            //
            //         rowsStr += '' +
            //             '<tr>' +
            //             '   <td style="' + style1Str + '">Player' + (parseInt(playerIndex) + 1) + '</td>' +
            //             '   <td>' + player.type + '</td>' +
            //             '   <td>' + player.reserveSize + '</td>' +
            //             '   <td>' + parseFloat(((occupiedCellCount / totalCellCount) * 100).toFixed()) + '%</td>' +
            //             '</tr>';
            //     }
            //     if(rowsStr.length > 0) {
            //         playersTableStr = '' +
            //             '<table>' +
            //             rowsStr +
            //             '</table>';
            //     }
            //     document.getElementById('PlayersPanel').innerHTML = playersTableStr;
            // },


            determineCountryColor(countryIndex) {
                const cellItem = this.modelValue.cells[countryIndex];
                if(cellItem == undefined) {
                    throw new TGameError('Unable to find cell!')
                }

                let data = {'Country': true};
                if(cellItem.playerKey !== null) {
                    data['Player' + cellItem.playerKey] = true;
                }
                return data;
            },

            findBelongingLabelDataOfCountry(countryIndex) {
                const labelData = this.modelValue.countryLabels.find(label => label.ownerCountryKey == countryIndex)
                if(labelData == undefined) {
                    throw new TGameError('Label could not be found!');
                }
                return labelData
            },
            findCountryData(countryIndex) {
                const countryData = this.modelValue.countries.find(country => country.countryKey == countryIndex)
                if(countryData == undefined) {
                    throw new TGameError('Country could not be found!');
                }
                return countryData
            }

        },
        created() {
            this.init();
        }
    }
</script>
<style scoped>

    .Continent {
        fill: none;
        stroke: #1a75ff;
        stroke-width: 20;
    }

    .Country {
        fill: grey;
        stroke: black;
        stroke-width: 2;
    }

    .Country:hover {
        stroke: black;
        stroke-width: 5;
    }


    .Player1 {
        fill: indigo;
        color: indigo;
    }

    .Player2 {
        fill: orange;
        color: orange;
    }

    .Player3 {
        fill: green;
        color: green;
    }

    .Player4 {
        fill: red;
        color: red;
    }


    #Box {
        display: block;
        padding: 10px;
        margin: 10px;
        width: 200px;
        heght: 100px;
        background-color: red;
        border: 1px red solid;
    }


    #Box > header > h1 {
        font-size: 26px;
    }


    .container-fluid {
        padding: 0px;
    }

    svg {
        height: 738px;
        /*width: 1597px;*/
        width: 1620px;
        display: flex;

        background-color: #005ce6;
        margin: 0px;
        padding: 0px;
    }


    #ActionBar {
        position: absolute;
        top: 0px;
        left: 0px;
        right: 0px;

        background-color: #666666;
        box-shadow: 0px 10px 10px 0px rgba(0, 0, 0, 0.5), 0 6px 20px 0 rgba(0, 0, 0, 0.19);

    }


    #ActionBar input {
        border-radius: 0px;
        padding: 2px;
        margin:5px;
    }


    #ActionBar #ExitButton {
        position: absolute;
        top: 0px;
        right: 0px;
        bottom: 0px;

        margin:5px;

        width: 150px;
        box-sizing: border-box;
        padding: 10px;
        font-size: 20px;
        font-weight: bold;
        text-align: center;
        text-decoration: none;

        border:1px #8c8c8c solid;
        color:#8c8c8c;
    }

    #ActionBar #ExitButton:hover {
        border:1px #e6e6e6 solid;
        color:#e6e6e6;
    }


    #ActionBar #TargetInput,
    #ActionBar #TroopsInput {

        width: 150px;
        box-sizing: border-box;
        padding: 10px;

        color: white;
        font-size: 20px;
        font-weight: bold;

        background-color: transparent;
        border: 1px white solid;
        border-radius: 0px;
    }


    #ActionBar #TargetInput:focus,
    #ActionBar #TroopsInput:focus {
        outline: none;
    }


    #ActionBar #AttackButton,
    #ActionBar #ReserveButton {
        width: 150px;
        box-sizing: border-box;
        padding: 10px;

        color: white;
        font-size: 20px;
        font-weight: bold;
    }

    #ActionBar #AttackButton {
        background-color: #ff0000;
    }

    #ActionBar #AttackButton:hover {
        background-color: #cc0000;
    }


    #ActionBar #ReserveButton {
        background-color: #00b33c;
    }

    #ActionBar #ReserveButton:hover {
        background-color: #00802b;
    }

    #ActionBar #ActionBox {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
    }

    #PlayersPanel {
        position: absolute;
        top: 70px;
        right: 10px;
    }

    #PlayersPanel table {
        border-collapse: separate;
        border-spacing: 0 5px;
    }

    #PlayersPanel td {
        color: white;
        padding: 2px 10px 2px 10px;
        backdrop-filter: blur(100px);
    }

    .CountryLabel {
        color: white;
        font-size: 15px;
        font-weight: bold;
    }

    #CellIndex {
        position: absolute;
        left: 5px;
        top: 5px;
        color: white;
        font-size: 25px;
        font-weight: bold;
    }

    #TurnLabel {
        position: absolute;
        left: 5px;
        top: 5px;
        color: white;
        font-size: 25px;
        font-weight: bold;
    }

</style>