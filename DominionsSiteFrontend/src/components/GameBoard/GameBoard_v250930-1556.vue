<template>

    <div v-if="(configuration.view !== null)">
        <div id="PlayersPanel">
            <div class="PlayerRow" v-for="(playerItem, playerIndex) in state.players" :key="'PlayersPanel-Row-'+playerIndex">
                <p v-if="playerItem.isAlive" class="NameParagraph">{{playerItem.name}}</p>
                <p v-else class="NameParagraph" style="text-decoration:line-through;">{{playerItem.name}}</p>
                <p class="DataParagraph">
                    <span>Reserve:&nbsp;{{playerItem.reserveSize}}</span>
                    &nbsp;|&nbsp;
                    <span>Territory:&nbsp;{{calculateTerritory(playerIndex)}}%</span>
                    &nbsp;|&nbsp;
                    <span>{{playerIndex == yourIndex ? '(You)' : configuration.players[playerIndex].type}}</span>
                    &nbsp;|&nbsp;
                    <span>{{playerItem.isIntentionGiven ? 'Done' : 'Thinking'}}</span>
                </p>
            </div>
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
                                <line v-for="(meridianItem, meridianIndex) in configuration.view.background.meridians" :key="'Background-MeridianLine-'+meridianIndex" :x1="meridianItem.x1" :y1="meridianItem.y1" :x2="meridianItem.x2" :y2="meridianItem.y2" style="stroke:#0066ff;stroke-width:1"/>
                                <!--PARALLEL LINES-->
                                <line v-for="(parallelItem, parallelIndex) in configuration.view.background.parallels" :key="'Background-ParallelsLine-'+parallelIndex" :x1="parallelItem.x1" :y1="parallelItem.y1" :x2="parallelItem.x2" :y2="parallelItem.y2" style="stroke:#0066ff;stroke-width:1"/>
                            </g>
                        </g>

                        <!--CONTINENTS AND COUNTRIES-->
                        <g transform="translate(+200,0)">
                            <g transform="scale(0.8,0.8)">
                                <!-- CONTINENT OUTLINE -->
                                <polygon v-for="(continentItem, continentIndex) in configuration.view.foreground.continents " :key="'ContinentOutline-'+continentIndex" class="Continent" :points="continentItem.points"/>

                                <!--LINKS-->
                                <line v-for="(lineItem, lineIndex) in configuration.view.foreground.jumpLines" :key="'Line-'+lineIndex" :x1="lineItem.x1" :y1="lineItem.y1" :x2="lineItem.x2" :y2="lineItem.y2" stroke="#003d99" stroke-width="5" stroke-dasharray="5"/>

                                <!--COUNTRIES-->
                                <polygon v-for="(countryItem, countryIndex) in configuration.view.foreground.countries" :key="'Country-'+countryIndex" ffw-type="Country" :ffw-country-key="countryItem.countryKey" :class="countryItem.classes" :points="countryItem.points" :ref="'Country-' + countryItem.countryKey" @click="eventCountryIsChosen(countryItem.countryKey)" @mouseenter="eventMouseAboveCountry(countryItem.countryKey)"/>
                                <!--                            <polygon v-for="(countryItem, countryIndex) in board.countries" :key="'Country-'+countryIndex" ffw-type="Country" :ffw-country-key="countryItem.countryKey"  :points="countryItem.points" :ref="'Country-' + countryItem.countryKey"/>-->

                                <!--COUNTRY LABELS-->
                                <text v-for="(countryLabelItem, countryLabelIndex) in configuration.view.foreground.countryLabels" :key="'CountryLabel-'+countryLabelIndex" ffw-type="CountryLabel" :ffw-country-key="countryLabelItem.ownerCountryKey" :x="countryLabelItem.x" :y="countryLabelItem.y" class="CountryLabel" :ref="'CountryLabel-' + countryLabelItem.labelKey">
                                    {{countryLabelItem.armySize}}
                                </text>
                            </g>
                        </g>

                    </g>
                </g>
            </svg>

            <div id="CellIndex"></div>
        </div>
        <nav id="ActionBar">
            [turn:{{turn}}] [Target:#{{action.targetKey}}][above:{{aboveIndex}}]
            <div id="ActionBox">
                <button :disabled="action.targetKey!=null" id="NextTurnButton" class="" type="button" @click="eventReserveAction">
                    Reserve
                </button>

                <input v-show="false" id="TargetInput" ref="TargetInput" type="text" placeholder="Target" :value="action.targetKey">
                <input :disabled="action.targetKey==null" id="TroopsInput" ref="TroopsInput" type="text" placeholder="Troops" v-model="action.troops">
                <button :disabled="action.targetKey==null" id="AttackButton" class="btn btn-basic" type="button" @click="eventAttackAction">
                    Attack
                </button>
            </div>
            <a id="LeaveButton" href="#">Leave</a>
        </nav>
    </div>


    <div class="BackDropLayer" v-if="isGameRecruiting">
        <div class="TDialog GlassDialog">

            <div class="Body">
                <v-progress-linear color="white" height="1" striped indeterminate rounded></v-progress-linear>
                <p> Waiting for {{(maxHumanPlayerCount - currentHumanPlayerCount)}} more
                    player(s) to join</p>
            </div>
            <div class="Footer">
                <button type="button" class="TButton" @click="eventClickOnLeaveTheGameButton">Leave the game</button>
            </div>
        </div>
    </div>

    <div class="BackDropLayer" v-if="(isGamePlaying && iLostTheGame)">
        <div class="TDialog GlassDialog">
            <div class="Body" style="text-align: center">
                <p style="font-size: 30px; font-weight: bold">GAME OVER</p>
                <p>Sorry! You lost the game!</p>
                <p>You are dead.<br>You can observe the game play.</p>
            </div>
        </div>
    </div>
    <div class="BackDropLayer" v-if="(isGameEnded && iLostTheGame)">
        <div class="TDialog GlassDialog">
            <div class="Body" style="text-align: center">
                <p style="font-size: 30px; font-weight: bold">GAME OVER</p>
                <p>Sorry! You lost the game!</p>

                <p>Winner(s):</p>
                <p v-for="(playerItem, playerIndex) in listWinnerPlayers()" :key="'EndDialog-Fail-WinnerPlayers-'+playerIndex">
                    {{playerItem.name}}</p>
            </div>
        </div>
    </div>
    <div class="BackDropLayer" v-if="isGameEnded && iWonTheGame">
        <div class="TDialog GlassDialog">
            <div class="Body" style="text-align: center">
                <p style="font-size: 30px; font-weight: bold">YOU WON</p>
                <p> Congratulation! You won this match!</p>
                <p>Winner(s):</p>
                <p v-for="(playerItem, playerIndex) in listWinnerPlayers()" :key="'EndDialog-Fail-WinnerPlayers-'+playerIndex">
                    {{playerItem.name}}</p>
            </div>
        </div>
    </div>
    <div class="BackDropLayer" v-if="isIntentionGiven">
        <div class="TDialog GlassDialog">
            <div class="Body">
                <v-progress-linear class="slow-progress" color="white" height="1" striped indeterminate rounded></v-progress-linear>
                <p>Waiting for other players!</p>
            </div>
        </div>
    </div>


</template>

<script>

    import TConfirmDialog from "@/framework/component/TConfirmDialog/TConfirmDialog";
    import TString from "@/framework/utils/TString";
    import TGameError from "@/components/GameBoard/TGameError";
    import TParameterValidation from "@/framework/utils/TParameterValidation";

    export default {
        emits: [
            'whenSelectedYourself',
            'whenSelectedOutOfNeighbourhood',
            'whenAttackButTroopSizeIsOverTheAvailable',
            'whenActionIsAttack',
            'whenActionIsReserve',
            'whenLeaveGame',
            'whenActionIsAlreadySent'
        ],
        computed: {

            getCellCount() {
                return Object.keys(this.state.cells).length
            },
            isGameRecruiting() {
                return this.statusCode == 'RECRUITING' ? true : false;
            },

            isGamePlaying() {
                return this.statusCode == 'PLAYING' ? true : false;
            },

            isGameEnded() {
                return this.statusCode == 'ENDED' ? true : false;
            },
            isIntentionGiven() {
                if(this.yourIndex != null) {
                    const playerData = this.gatherPlayerData(this.yourIndex);
                    if(playerData.isIntentionGiven) {
                        return true;
                    }
                }
                return false;
            },
            iAmAlive() {
                if(this.yourIndex != null) {
                    const playerData = this.gatherPlayerData(this.yourIndex);
                    if(playerData.isAlive) {
                        return true;
                    }
                }
                return false;
            },
            iLostTheGame() {
                if(this.yourIndex != null) {
                    if(this.isGamePlaying && !this.iAmAlive) {
                        return true;
                    }
                    else if(this.isGameEnded && !this.winnerKeys.includes(this.yourIndex)) {
                        return true;
                    }
                }
                return false;
            },
            iWonTheGame() {
                if(this.yourIndex != null && this.winnerKeys.includes(this.yourIndex)) {
                    return true;
                }
                return false;
            }


        },
        data() {
            return {
                aboveIndex:null,
                action: {
                    targetKey: null,
                    troops: null
                },
                yourIndex: null,
                settings: {},
                configuration: {
                    view: null,
                },
                state: {
                    players: {},
                    cells: {},
                },
                statusCode: null,
                maxHumanPlayerCount: null,
                currentHumanPlayerCount: null,
                turn: null,
                winnerKeys: []
            };
        },
        methods: {


            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            /// [ Event methods ] //////////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            eventInit() {
            },


            eventMouseAboveCountry(countryIndex) {
                this.aboveIndex=countryIndex;
            },
            eventCountryIsChosen(countryIndex) {
                // Input checking
                TParameterValidation.assertParameterIsIntegerType(countryIndex, 'countryIndex');
                this.assertCountryExistAtIndex(countryIndex);
                this.assertCellExistAtIndex(countryIndex);

                // Collect data
                const countryData = this.gatherCountryData(countryIndex);
                const cellData = this.gatherCellData(countryIndex);
                const playerData = this.gatherPlayerData(this.yourIndex);


                if(cellData.occupierKey == this.yourIndex) {    // When the clicked country is yours
                    this.action.targetKey = null;
                    this.action.troops = null;
                    this.$emit('whenSelectedYourself');
                } else if(!this.areYouANeighbourOfTheCell(countryIndex)) { // When the country is not a neighbour
                    this.action.targetKey = null;
                    this.action.troops = null;
                    this.$emit('whenSelectedOutOfNeighbourhood');
                } else {

                    if(this.action.targetKey != countryIndex) { //Select
                        this.action.targetKey = countryIndex;
                        this.action.troops = playerData.reserveSize;
                    } else { // Deselect
                        this.action.targetKey = null;
                        this.action.troops = null;
                    }
                }
                this.refreshAllCountryState();
            },

            async eventLeaveGame() {
                // Confirming
                const confirmed = await TConfirmDialog('Do you wish to continue?');
                if(!confirmed.ok) {
                    return;
                }

                this.$emit('whenLeaveGame', {});
            },


            eventAttackAction() {

                // Initialisation
                const countryIndex = this.$refs.TargetInput.value;
                const troopsSize = this.$refs.TroopsInput.value;

                // Validation
                this.assertCountryExistAtIndex(countryIndex);
                this.assertCellExistAtIndex(countryIndex);

                // Gather current player data
                const playerData = this.gatherPlayerData(this.yourIndex);

                // Check: Whether the intention is already sent for this turn
                if(this.isYourIntentionGiven(this.yourIndex)) {
                    this.$emit('whenActionIsAlreadySent', {})
                    return;
                }

                // Check: Whether the attacking troop size is above of the available
                if(playerData.reserveSize < troopsSize) {
                    this.$emit('whenAttackButTroopSizeIsOverTheAvailable', {
                        expectedTroopSize: troopsSize,
                        reserveSize: playerData.reserveSize
                    });
                    return;
                }

                // Execute
                this.$emit('whenActionIsAttack', {
                    targetKey: countryIndex,
                    troopsSize: troopsSize,
                    playerIndex: this.yourIndex
                });

                this.action.targetKey = null;
                this.action.troops = null;
            },
            eventReserveAction() {

                // Initialisation
                const countryIndex = 0;
                const troopsSize = 0;

                // Gather current player data
                const playerData = this.gatherPlayerData(this.yourIndex);

                // Check: Whether the intention is already sent for this turn
                if(this.isYourIntentionGiven(this.yourIndex)) {
                    this.$emit('whenActionIsAlreadySent', {})
                    return;
                }

                // Execute
                this.$emit('whenActionIsReserve', {
                    playerIndex: this.yourIndex
                });

                this.action.targetKey = null;
                this.action.troops = null;
            },
            //
            // eventNextTurnAction() {
            //     this.$emit('whenStepMade', {
            //         actionType: 'RESERVE',
            //         actionData: {},
            //         playerIndex: this.yourIndex
            //     });
            // }
            // ,

            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            /// [ Public methods ] //////////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            async loadPlayState(loadData) {

                this.settings = loadData.settings;
                this.configuration = loadData.configuration;
                this.state = loadData.state;
                this.statusCode = loadData.statusCode;
                this.maxHumanPlayerCount = loadData.maxHumanPlayerCount;
                this.currentHumanPlayerCount = loadData.currentHumanPlayerCount;
                this.yourIndex = loadData.yourIndex;
                this.turn = loadData.turn;
                this.winnerKeys = loadData.winnerKeys;


                this.action.targetKey = null;
                this.action.troops = null;

                this.adjustCountryLabels();
                this.refreshBoardState();
            },

            async refreshPlayState(refreshData) {

                this.state = refreshData.state;
                this.statusCode = refreshData.statusCode;
                this.maxHumanPlayerCount = refreshData.maxHumanPlayerCount;
                this.currentHumanPlayerCount = refreshData.currentHumanPlayerCount;
                this.turn = refreshData.turn;
                this.winnerKeys = refreshData.winnerKeys;

                this.adjustCountryLabels();
                this.refreshBoardState();
            }
            ,

            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            /// [ ETC methods ] //////////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            adjustCountryLabels() {
                for(const labelItem of this.configuration.view.foreground.countryLabels) {
                    this.adjustCountryLabelPosition(labelItem);
                }
            }
            ,

            adjustCountryLabelPosition(labelItem) {

                const countryItem = this.configuration.view.foreground.countries.find(country => country.countryKey === labelItem.ownerCountryKey)
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
                // console.log(coordinatePairsList);


                for(let coordinatePairString of coordinatePairsList) {
                    if(coordinatePairString.length == 0) {
                        continue;
                    }

                    // console.log(coordinatePairString);

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

                    //console.log("Adjusted (" + turn + ")[" + newCoordinateX + "][" + newCoordinateY + "]");

                    newCoordinateX = newCoordinateX / turn;
                    newCoordinateY = newCoordinateY / turn;

                    labelItem.x = newCoordinateX;
                    labelItem.y = newCoordinateY;

                } else {
                    //console.log("not adjusted");
                }

            }
            ,

            countOccupiedCells(playerIndex) {
                let count = 0;

                Object.entries(this.state.cells).forEach(([cellIndex, cellItem]) => {
                    if(parseInt(cellItem.occupierKey) === parseInt(playerIndex)) {
                        count++;
                    }
                });
                return count;
            }
            ,

            refreshBoardState() {
                console.log('refreshBoardState');
                console.log(this.state);
                // Counting occupied cells
                Object.entries(this.state.players).forEach(([playerIndex, playerItem]) => {
                    playerItem.occupiedCellCount = this.countOccupiedCells(playerIndex);
                    console.log(playerIndex + "occupiedCellCount:" + playerItem.occupiedCellCount);
                });

                //
                console.log('refreshAllCountryState');
                this.refreshAllCountryState();

                // // Draw players panel
                // let totalCellCount = Object.keys(response.cells).length
                // refreshPlayersTable(players, totalCellCount)
                //
                // // Refresh turn label
                // document.getElementById('TurnLabel').innerHTML = '#' + response.currentTurn;

            }
            ,

            refreshAllCountryState() {
                Object.entries(this.state.cells).forEach(([cellIndex, cellItem]) => {
                    const countryIndex = cellIndex;
                    const labelData = this.findBelongingLabelDataOfCountry(countryIndex)
                    const countryData = this.gatherCountryData(countryIndex)
                    labelData.armySize = cellItem.armySize;

                    let countryClassAttribute = {'Country': true};
                    if(cellItem.occupierKey !== null && cellItem.armySize > 0) {
                        countryClassAttribute['Player' + cellItem.occupierKey] = true;
                    }

                    if(this.action.targetKey == cellIndex) {
                        countryClassAttribute['TargetedCell'] = true;
                    }


                    countryData.classes = countryClassAttribute;


                });
            }
            ,

            determineCountryColor(countryIndex) {
                const cellItem = this.state.cells[countryIndex];
                if(cellItem == undefined) {
                    throw new TGameError('Unable to find cell!')
                }

                let data = {'Country': true};
                if(cellItem.occupierKey !== null) {
                    data['Player' + cellItem.occupierKey] = true;
                }
                return data;
            }
            ,

            findBelongingLabelDataOfCountry(countryIndex) {
                const labelData = this.configuration.view.foreground.countryLabels.find(label => label.ownerCountryKey == countryIndex)
                if(labelData == undefined) {
                    throw new TGameError('Label could not be found!');
                }
                return labelData
            }
            ,


            calculateTerritory(playerIndex) {
                if(playerIndex != null) {
                    const playerData = this.gatherPlayerData(parseInt(playerIndex));
                    let occupiedCellCount = this.countOccupiedCells(parseInt(playerIndex));
                    return Math.round((occupiedCellCount / this.getCellCount) * 100);
                }
                return 0;
            },


            listWinnerPlayers() {
                let winnerPlayersArr = [];
                for(let playerIndex of this.winnerKeys) {
                    let playerData = this.gatherPlayerData(parseInt(playerIndex));
                    winnerPlayersArr.push(playerData);
                }
                return winnerPlayersArr;
            },


            // findWinnerPlayerIndex() {
            //     for(const [playerIndex, playerItem] of Object.entries(this.state.players)) {
            //         if(playerItem.isAlive == true) {
            //             return playerIndex;
            //         }
            //     }
            //     return null;
            // },

            // findWinnerPlayerData() {
            //     const winnerIndex = this.findWinnerPlayerIndex();
            //     if(winnerIndex === null) {
            //         return null;
            //     }
            //
            //     return this.gatherPlayerData(parseInt(winnerIndex));
            // },

            // findWinnerPlayerName() {
            //     const winnerPlayer = this.findWinnerPlayerData();
            //     if(winnerPlayer === null) {
            //         return null;
            //     }
            //
            //     return winnerPlayer.name
            // },


            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            /// [ Is... methods ] //////////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            areYouANeighbourOfTheCell(cellIndex) {
                this.assertCellExistAtIndex(cellIndex);
                return this.isCellANeighbourOfPlayer(cellIndex, this.yourIndex);
            }
            ,

            isCellANeighbourOfPlayer(cellIndex, playerIndex) {
                this.assertCellExistAtIndex(cellIndex);
                this.assertPlayerExistAtIndex(playerIndex);

                const cellData = this.gatherCellData(cellIndex);
                // console.log('playerIndex:' + playerIndex);
                // console.log('cellIndex:' + cellIndex);
                // console.log(cellData.neighbours);
                // console.log('---------------');
                for(const neighbourIndex of cellData.neighbours) {
                    const neighbourData = this.gatherCellData(neighbourIndex);
                    if(neighbourData.occupierKey == playerIndex) {
                        return true;
                    }
                }
                return false;
            }
            ,

            isCountryExistAtIndex(countryIndex) {
                const countryData = this.configuration.view.foreground.countries.find(country => country.countryKey == countryIndex)
                if(countryData == undefined) {
                    return false;
                }
                return true;
            }
            ,

            isCellExistAtIndex(cellIndex) {
                if(cellIndex in this.state.cells) {
                    return true;
                }
                return false;
            }
            ,

            isPlayerExistAtIndex(playerIndex) {
                if(playerIndex in this.state.players) {
                    return true;
                }
                return false;
            }
            ,

            isYourIntentionGiven(playerIndex) {
                // Input checking
                TParameterValidation.assertParameterIsIntegerType(playerIndex);

                // Validation
                this.assertPlayerExistAtIndex(playerIndex);

                const playerData = this.gatherPlayerData(this.yourIndex);
                if(playerData.isIntentionGiven) {
                    return true;
                }
                return false;
            }
            ,
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            /// [ Gather-data methods ] //////////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            gatherCellData(cellIndex) {
                // Input checking
                TParameterValidation.assertParameterIsIntegerType(cellIndex, 'cellIndex');
                this.assertCellExistAtIndex(cellIndex);

                // Generate return
                return {
                    caption: this.configuration.boardCells[cellIndex].caption,
                    neighbours: this.configuration.boardCells[cellIndex].neighbours,
                    occupierKey: this.state.cells[cellIndex].occupierKey,
                    armySize: this.state.cells[cellIndex].armySize,
                }
            }
            ,
            gatherPlayerData(playerIndex) {
                // Input checking
                TParameterValidation.assertParameterIsIntegerType(playerIndex, 'playerIndex');
                this.assertPlayerExistAtIndex(playerIndex);

                // Execution
                return {
                    type: this.configuration.players[playerIndex].type,
                    engine: this.configuration.players[playerIndex].engine,
                    name: this.state.players[playerIndex].name,
                    reserveSize: this.state.players[playerIndex].reserveSize,
                    isAlive: this.state.players[playerIndex].isAlive,
                    isIntentionGiven: this.state.players[playerIndex].isIntentionGiven,
                }
            }
            ,
            gatherCountryData(countryIndex) {
                this.assertCountryExistAtIndex(countryIndex);
                return this.configuration.view.foreground.countries.find(country => country.countryKey == countryIndex);
            }
            ,

            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            /// [ Assert methods ] //////////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            assertCountryExistAtIndex(countryIndex) {
                if(!this.isCountryExistAtIndex(countryIndex)) {
                    throw new TGameError('No country data can be found at index:' + countryIndex + '!');
                }
            }
            ,
            assertPlayerExistAtIndex(playerIndex) {
                if(!this.isPlayerExistAtIndex(playerIndex)) {
                    throw new TGameError('No player data can be found at index:' + playerIndex + '!');
                }
            }
            ,
            assertCellExistAtIndex(cellIndex) {
                if(!this.isCellExistAtIndex(cellIndex)) {
                    throw new TGameError('No cell data can be found at index:' + cellIndex + '!');
                }
            }
            ,

        }
        ,
        mounted() {
            this.eventInit();
        }
    }
</script>
<style scoped>

    .BackDropLayer {
        position: absolute;
        top: 0px;
        left: 0px;
        right: 0px;
        bottom: 0px;
        background-color: rgba(0, 0, 0, 0.5);
    }


    .GlassDialog {
        background-color: rgba(0, 0, 0, 0.5);
        min-height: auto;
        max-width: 30vw;
    }


    .GlassDialog .Body {
        background-color: transparent;
        overflow: hidden;
        padding: 10px;
        color: white;
    }

    .GlassDialog .Footer {
        background-color: transparent;
    }


    .GlassDialog .Footer .TButton {
        background-color: transparent;
        border-color: white;
        color: white;
        width: auto;
    }


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
        cursor: pointer;
    }

    .TargetedCell.Country {
        stroke: white;
        stroke-width: 5;
        cursor: pointer;
    }


    .Player0 {
        fill: red;
        color: red;
    }

    .Player1 {
        fill: blue;
        color: blue;
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
        fill: indigo;
        color: indigo;
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
        position: relative;
        height: 60px;

        background-color: #666666;
        box-shadow: 0px 10px 10px 0px rgba(0, 0, 0, 0.5), 0 6px 20px 0 rgba(0, 0, 0, 0.19);

    }


    #ActionBar input {
        border-radius: 0px;
        padding: 2px;
        margin: 5px;
    }


    #ActionBar #LeaveButton {
        position: absolute;
        top: 0px;
        right: 0px;
        bottom: 0px;

        margin: 5px;

        width: 150px;
        box-sizing: border-box;
        padding: 10px;
        font-size: 20px;
        font-weight: bold;
        text-align: center;
        text-decoration: none;

        border: 1px #8c8c8c solid;
        color: #8c8c8c;
    }

    #ActionBar #LeaveButton:hover {
        border: 1px #e6e6e6 solid;
        color: #e6e6e6;
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
    #ActionBar #NextTurnButton {
        width: 150px;
        box-sizing: border-box;
        padding: 10px;

        color: white;
        font-size: 20px;
        font-weight: bold;
    }


    #ActionBar #NextTurnButton:disabled {
        opacity: 0.2;
        cursor: not-allowed;
    }

    #ActionBar #AttackButton {
        background-color: #ff0000;
    }

    #ActionBar #AttackButton:hover {
        background-color: #cc0000;
    }

    #ActionBar #AttackButton:disabled {
        opacity: 0.2;
        cursor: not-allowed;
    }

    #ActionBar #NextTurnButton {
        background-color: #00b33c;
    }

    #ActionBar #NextTurnButton:hover {
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

    #PlayersPanel .PlayerRow {
        margin: 5px;
        padding: 5px;
        font-size: 12px;
        color: white;
        border: 1px blue solid;
        background-color: rgba(0, 0, 0, 0.5);
    }

    #PlayersPanel .PlayerRow .DataParagraph {

        font-size: 10px;
        color: grey;

    }


    #PlayersPanel table {
        border-collapse: separate;
        border-spacing: 0 5px;
    }

    #PlayersPanel td {
        color: white;
        padding: 2px 10px 2px 10px;
        backdrop-filter: blur(100px);
        font-size: 12px;
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