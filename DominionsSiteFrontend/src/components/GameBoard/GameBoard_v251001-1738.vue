<template>

    <div v-if="(configuration.view !== null)">
        <div id="AvatarsPanel">
            <div class="AvatarRow" v-for="(playerItem, playerIndex) in state.players"
                 :key="'PlayersPanel-Row-'+playerIndex">
                <div class="PlayerAvatar"></div>
                <div class="AvatarInfo">
                    <p v-if="playerItem.isAlive" class="NameParagraph">{{ playerItem.name }}</p>
                    <p v-else class="NameParagraph" style="text-decoration:line-through;">{{ playerItem.name }}</p>
                    <p class="DataParagraph">
                        <span>Reserve:&nbsp;{{ playerItem.reserveSize }}</span>
                        &nbsp;|&nbsp;
                        <span>Territory:&nbsp;{{ calculateTerritory(playerIndex) }}%</span>
                        &nbsp;|&nbsp;
                        <span>{{ playerIndex == yourIndex ? '(You)' : configuration.players[playerIndex].type }}</span>
                        &nbsp;|&nbsp;
                        <span>{{ playerItem.isIntentionGiven ? 'Done' : 'Thinking' }}</span>
                    </p>


                </div>
            </div>
            <div class="AvatarRow" v-for="(playerItem, playerIndex) in state.players"
                 :key="'PlayersPanel-Row-'+playerIndex">
                <div class="PlayerAvatar"></div>
                <div class="AvatarInfo">
                    <p v-if="playerItem.isAlive" class="NameParagraph">{{ playerItem.name }}</p>
                    <p v-else class="NameParagraph" style="text-decoration:line-through;">{{ playerItem.name }}</p>
                    <p class="DataParagraph">
                        <span>Reserve:&nbsp;{{ playerItem.reserveSize }}</span>
                        &nbsp;|&nbsp;
                        <span>Territory:&nbsp;{{ calculateTerritory(playerIndex) }}%</span>
                        &nbsp;|&nbsp;
                        <span>{{ playerIndex == yourIndex ? '(You)' : configuration.players[playerIndex].type }}</span>
                        &nbsp;|&nbsp;
                        <span>{{ playerItem.isIntentionGiven ? 'Done' : 'Thinking' }}</span>
                    </p>


                </div>
            </div>
            <div class="AvatarRow" v-for="(playerItem, playerIndex) in state.players"
                 :key="'PlayersPanel-Row-'+playerIndex">
                <div class="PlayerAvatar"></div>
                <div class="AvatarInfo">
                    <p v-if="playerItem.isAlive" class="NameParagraph">{{ playerItem.name }}</p>
                    <p v-else class="NameParagraph" style="text-decoration:line-through;">{{ playerItem.name }}</p>
                    <p class="DataParagraph">
                        <span>Reserve:&nbsp;{{ playerItem.reserveSize }}</span>
                        &nbsp;|&nbsp;
                        <span>Territory:&nbsp;{{ calculateTerritory(playerIndex) }}%</span>
                        &nbsp;|&nbsp;
                        <span>{{ playerIndex == yourIndex ? '(You)' : configuration.players[playerIndex].type }}</span>
                        &nbsp;|&nbsp;
                        <span>{{ playerItem.isIntentionGiven ? 'Done' : 'Thinking' }}</span>
                    </p>


                </div>
            </div>
            <div class="AvatarRow" v-for="(playerItem, playerIndex) in state.players"
                 :key="'PlayersPanel-Row-'+playerIndex">
                <div class="PlayerAvatar"></div>
                <div class="AvatarInfo">
                    <p v-if="playerItem.isAlive" class="NameParagraph">{{ playerItem.name }}</p>
                    <p v-else class="NameParagraph" style="text-decoration:line-through;">{{ playerItem.name }}</p>
                    <p class="DataParagraph">
                        <span>Reserve:&nbsp;{{ playerItem.reserveSize }}</span>
                        &nbsp;|&nbsp;
                        <span>Territory:&nbsp;{{ calculateTerritory(playerIndex) }}%</span>
                        &nbsp;|&nbsp;
                        <span>{{ playerIndex == yourIndex ? '(You)' : configuration.players[playerIndex].type }}</span>
                        &nbsp;|&nbsp;
                        <span>{{ playerItem.isIntentionGiven ? 'Done' : 'Thinking' }}</span>
                    </p>


                </div>
            </div>
            <div class="AvatarRow" v-for="(playerItem, playerIndex) in state.players"
                 :key="'PlayersPanel-Row-'+playerIndex">
                <div class="PlayerAvatar"></div>
                <div class="AvatarInfo">
                    <p v-if="playerItem.isAlive" class="NameParagraph">{{ playerItem.name }}</p>
                    <p v-else class="NameParagraph" style="text-decoration:line-through;">{{ playerItem.name }}</p>
                    <p class="DataParagraph">
                        <span>Reserve:&nbsp;{{ playerItem.reserveSize }}</span>
                        &nbsp;|&nbsp;
                        <span>Territory:&nbsp;{{ calculateTerritory(playerIndex) }}%</span>
                        &nbsp;|&nbsp;
                        <span>{{ playerIndex == yourIndex ? '(You)' : configuration.players[playerIndex].type }}</span>
                        &nbsp;|&nbsp;
                        <span>{{ playerItem.isIntentionGiven ? 'Done' : 'Thinking' }}</span>
                    </p>


                </div>
            </div>
        </div>
        <div id="TurnLabel"></div>
        <div id="MapPanel" style="">
            <button type="button" title="Reserve" :disabled="action.targetKey!=null" id="ReserveIcon"
                    class=" mdi-piggy-bank-outline mdi"></button>
            <button type="button" title="Attack" :disabled="action.targetKey==null" id="AttackIcon"
                    class="mdi-sword mdi"></button>
            <input v-show="false" id="TargetInput" ref="TargetInput" type="text" placeholder="Target"
                   :value="action.targetKey">
            <input :disabled="action.targetKey==null" id="TroopsInput" ref="TroopsInput" type="text"
                   placeholder="Troops" v-model="action.troops">


            <button type="button" title="Resign"  id="ResignIcon"
                    class=" mdi-flag-variant-outline mdi"></button>


            <svg id="MapSvg" viewBox="0 0 1300 850" preserveAspectRatio="xMidYMid meet" style="">
                <!--CONTINENTS AND COUNTRIES-->
                <g transform="translate(0,0)">
                    <g transform="scale(1,1)">
                        <!-- CONTINENT OUTLINE -->
                        <polygon
                                v-for="(continentItem, continentIndex) in configuration.view.foreground.continents "
                                :key="'ContinentOutline-'+continentIndex" class="Continent"
                                :points="continentItem.points"/>

                        <!--                                &lt;!&ndash;LINKS&ndash;&gt;-->
                        <!--                                <line v-for="(lineItem, lineIndex) in configuration.view.foreground.jumpLines" :key="'Line-'+lineIndex" :x1="lineItem.x1" :y1="lineItem.y1" :x2="lineItem.x2" :y2="lineItem.y2" stroke="#003d99" stroke-width="5" stroke-dasharray="5"/>-->

                        <!--COUNTRIES-->
                        <polygon v-for="(countryItem, countryIndex) in configuration.view.foreground.countries"
                                 :key="'Country-'+countryIndex" ffw-type="Country"
                                 :ffw-country-key="countryItem.countryKey" :class="countryItem.classes"
                                 :points="countryItem.points" :ref="'Country-' + countryItem.countryKey"
                                 @click="eventCountryIsChosen(countryItem.countryKey)"
                                 @mouseenter="eventMouseAboveCountry(countryItem.countryKey)"/>
                        <!--                            <polygon v-for="(countryItem, countryIndex) in board.countries" :key="'Country-'+countryIndex" ffw-type="Country" :ffw-country-key="countryItem.countryKey"  :points="countryItem.points" :ref="'Country-' + countryItem.countryKey"/>-->

                        <!--COUNTRY LABELS-->
                        <text v-for="(countryLabelItem, countryLabelIndex) in configuration.view.foreground.countryLabels"
                              :key="'CountryLabel-'+countryLabelIndex" ffw-type="CountryLabel"
                              :ffw-country-key="countryLabelItem.ownerCountryKey" :x="countryLabelItem.x"
                              :y="countryLabelItem.y" class="CountryLabel"
                              :ref="'CountryLabel-' + countryLabelItem.labelKey">
                            {{ countryLabelItem.armySize }}
                        </text>
                    </g>
                </g>
            </svg>

            <div id="CellIndex"></div>
        </div>
<!--        <nav id="ActionBar">-->
<!--            [turn:{{ turn }}] [Target:#{{ action.targetKey }}][above:{{ aboveIndex }}]-->
<!--            &lt;!&ndash;            <div id="ActionBox">&ndash;&gt;-->
<!--            &lt;!&ndash;                <button :disabled="action.targetKey!=null" id="NextTurnButton" class="" type="button"&ndash;&gt;-->
<!--            &lt;!&ndash;                        @click="eventReserveAction">&ndash;&gt;-->
<!--            &lt;!&ndash;                    Reserve&ndash;&gt;-->
<!--            &lt;!&ndash;                </button>&ndash;&gt;-->

<!--            &lt;!&ndash;                <input v-show="false" id="TargetInput" ref="TargetInput" type="text" placeholder="Target"&ndash;&gt;-->
<!--            &lt;!&ndash;                       :value="action.targetKey">&ndash;&gt;-->
<!--            &lt;!&ndash;                <input :disabled="action.targetKey==null" id="TroopsInput" ref="TroopsInput" type="text"&ndash;&gt;-->
<!--            &lt;!&ndash;                       placeholder="Troops" v-model="action.troops">&ndash;&gt;-->
<!--            &lt;!&ndash;                <button :disabled="action.targetKey==null" id="AttackButton" class="btn btn-basic" type="button"&ndash;&gt;-->
<!--            &lt;!&ndash;                        @click="eventAttackAction">&ndash;&gt;-->
<!--            &lt;!&ndash;                    Attack&ndash;&gt;-->
<!--            &lt;!&ndash;                </button>&ndash;&gt;-->
<!--            &lt;!&ndash;            </div>&ndash;&gt;-->
<!--            <a id="LeaveButton" href="#">Leave</a>-->
<!--        </nav>-->
        <div>alma</div>
    </div>


    <div class="BackDropLayer" v-if="isGameRecruiting">
        <div class="TDialog GlassDialog">

            <div class="Body">
                <v-progress-linear color="white" height="1" striped indeterminate rounded></v-progress-linear>
                <p> Waiting for {{ (maxHumanPlayerCount - currentHumanPlayerCount) }} more
                    player(s) to join</p>
            </div>
            <div class="Footer">
                <button type="button" class="TButton" @click="eventClickOnLeaveTheGameButton">Leave the game
                </button>
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
                <p v-for="(playerItem, playerIndex) in listWinnerPlayers()"
                   :key="'EndDialog-Fail-WinnerPlayers-'+playerIndex">
                    {{ playerItem.name }}</p>
            </div>
        </div>
    </div>
    <div class="BackDropLayer" v-if="isGameEnded && iWonTheGame">
        <div class="TDialog GlassDialog">
            <div class="Body" style="text-align: center">
                <p style="font-size: 30px; font-weight: bold">YOU WON</p>
                <p> Congratulation! You won this match!</p>
                <p>Winner(s):</p>
                <p v-for="(playerItem, playerIndex) in listWinnerPlayers()"
                   :key="'EndDialog-Fail-WinnerPlayers-'+playerIndex">
                    {{ playerItem.name }}</p>
            </div>
        </div>
    </div>
    <div class="BackDropLayer" v-if="isIntentionGiven">
        <div class="TDialog GlassDialog">
            <div class="Body">
                <v-progress-linear class="slow-progress" color="white" height="1" striped indeterminate
                                   rounded></v-progress-linear>
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
            if (this.yourIndex != null) {
                const playerData = this.gatherPlayerData(this.yourIndex);
                if (playerData.isIntentionGiven) {
                    return true;
                }
            }
            return false;
        },
        iAmAlive() {
            if (this.yourIndex != null) {
                const playerData = this.gatherPlayerData(this.yourIndex);
                if (playerData.isAlive) {
                    return true;
                }
            }
            return false;
        },
        iLostTheGame() {
            if (this.yourIndex != null) {
                if (this.isGamePlaying && !this.iAmAlive) {
                    return true;
                } else if (this.isGameEnded && !this.winnerKeys.includes(this.yourIndex)) {
                    return true;
                }
            }
            return false;
        },
        iWonTheGame() {
            if (this.yourIndex != null && this.winnerKeys.includes(this.yourIndex)) {
                return true;
            }
            return false;
        }


    },
    data() {
        return {
            aboveIndex: null,
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
            this.aboveIndex = countryIndex;
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


            if (cellData.occupierKey == this.yourIndex) {    // When the clicked country is yours
                this.action.targetKey = null;
                this.action.troops = null;
                this.$emit('whenSelectedYourself');
            } else if (!this.areYouANeighbourOfTheCell(countryIndex)) { // When the country is not a neighbour
                this.action.targetKey = null;
                this.action.troops = null;
                this.$emit('whenSelectedOutOfNeighbourhood');
            } else {

                if (this.action.targetKey != countryIndex) { //Select
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
            if (!confirmed.ok) {
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
            if (this.isYourIntentionGiven(this.yourIndex)) {
                this.$emit('whenActionIsAlreadySent', {})
                return;
            }

            // Check: Whether the attacking troop size is above of the available
            if (playerData.reserveSize < troopsSize) {
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
            if (this.isYourIntentionGiven(this.yourIndex)) {
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
            for (const labelItem of this.configuration.view.foreground.countryLabels) {
                this.adjustCountryLabelPosition(labelItem);
            }
        }
        ,

        adjustCountryLabelPosition(labelItem) {

            const countryItem = this.configuration.view.foreground.countries.find(country => country.countryKey === labelItem.ownerCountryKey)
            if (countryItem == undefined) {
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


            for (let coordinatePairString of coordinatePairsList) {
                if (coordinatePairString.length == 0) {
                    continue;
                }

                // console.log(coordinatePairString);

                if (coordinatePairString.split(",").length != 2) {// must be only one colon presented. in case of this the array split function result in a arry containing two item.
                    throw new TGameError('Unable to detect x and y in coordinate pair string! The string is:' + coordinatePairString);
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
                if (parseInt(cellItem.occupierKey) === parseInt(playerIndex)) {
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
                if (cellItem.occupierKey !== null && cellItem.armySize > 0) {
                    countryClassAttribute['Player' + cellItem.occupierKey] = true;
                }

                if (this.action.targetKey == cellIndex) {
                    countryClassAttribute['TargetedCell'] = true;
                }


                countryData.classes = countryClassAttribute;


            });
        }
        ,

        determineCountryColor(countryIndex) {
            const cellItem = this.state.cells[countryIndex];
            if (cellItem == undefined) {
                throw new TGameError('Unable to find cell!')
            }

            let data = {'Country': true};
            if (cellItem.occupierKey !== null) {
                data['Player' + cellItem.occupierKey] = true;
            }
            return data;
        }
        ,

        findBelongingLabelDataOfCountry(countryIndex) {
            const labelData = this.configuration.view.foreground.countryLabels.find(label => label.ownerCountryKey == countryIndex)
            if (labelData == undefined) {
                throw new TGameError('Label could not be found!');
            }
            return labelData
        }
        ,


        calculateTerritory(playerIndex) {
            if (playerIndex != null) {
                const playerData = this.gatherPlayerData(parseInt(playerIndex));
                let occupiedCellCount = this.countOccupiedCells(parseInt(playerIndex));
                return Math.round((occupiedCellCount / this.getCellCount) * 100);
            }
            return 0;
        },


        listWinnerPlayers() {
            let winnerPlayersArr = [];
            for (let playerIndex of this.winnerKeys) {
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
            for (const neighbourIndex of cellData.neighbours) {
                const neighbourData = this.gatherCellData(neighbourIndex);
                if (neighbourData.occupierKey == playerIndex) {
                    return true;
                }
            }
            return false;
        }
        ,

        isCountryExistAtIndex(countryIndex) {
            const countryData = this.configuration.view.foreground.countries.find(country => country.countryKey == countryIndex)
            if (countryData == undefined) {
                return false;
            }
            return true;
        }
        ,

        isCellExistAtIndex(cellIndex) {
            if (cellIndex in this.state.cells) {
                return true;
            }
            return false;
        }
        ,

        isPlayerExistAtIndex(playerIndex) {
            if (playerIndex in this.state.players) {
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
            if (playerData.isIntentionGiven) {
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
            if (!this.isCountryExistAtIndex(countryIndex)) {
                throw new TGameError('No country data can be found at index:' + countryIndex + '!');
            }
        }
        ,
        assertPlayerExistAtIndex(playerIndex) {
            if (!this.isPlayerExistAtIndex(playerIndex)) {
                throw new TGameError('No player data can be found at index:' + playerIndex + '!');
            }
        }
        ,
        assertCellExistAtIndex(cellIndex) {
            if (!this.isCellExistAtIndex(cellIndex)) {
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

#AvatarsPanel {
    position: absolute;
    top: 20px;
    right: 10px;

    background-color: transparent;
    z-index: 1000;
}

#AvatarsPanel .AvatarRow {
    position: relative;
    height: 50px;
    width: 50px;

    padding: 0px;
    margin: 0px 10px 15px 10px;

    display: block;

    border: none;
}


#AvatarsPanel .PlayerAvatar {
    position: absolute;
    top: 0;
    left: 0;
    width: 50px;
    height: 50px;
    margin: 0px;

    background-color: transparent;
    backdrop-filter: blur(10px);
    -webkit-backdrop-filter: blur(5px);
    box-shadow: 0px 0px 15px rgba(0, 0, 0, 0.4);

    border: 5px rgba(255, 0, 0, 0.7) solid;
    border-radius: 50%;
    z-index: 1002;
}

#AvatarsPanel .AvatarRow:hover .PlayerAvatar {
    border: 5px rgba(255, 255, 255, 0.9) solid;
    box-shadow: 0px 0px 15px rgba(255, 255, 255, 0.9);
}


#AvatarsPanel .AvatarRow .AvatarInfo {
    display: none;
    position: absolute;
    top: -10px;
    right: -10px;
    padding: 0px;
    background-color: rgba(0, 0, 0, 0.7);
    border-radius: 100px 100px 100px 100px;
    border: 10px rgba(0, 0, 0, 0.1) solid;
    width: 400px;
    height: 70px;
    z-index: 1001;


}

#AvatarsPanel .AvatarRow .AvatarInfo .NameParagraph {
    color: rgba(255, 255, 255, 0.7);
    margin-left:10px;
}

#AvatarsPanel .AvatarRow .AvatarInfo .DataParagraph {
    font-size: 10px;
    color: rgba(255, 255, 255, 0.7);
    margin-left:10px;
}

#AvatarsPanel .AvatarRow:hover .AvatarInfo {
    display: block;
}


#PlayersPanel {
    position: absolute;
    top: 20px;
    right: 10px;

    min-width: 20vw;

    background-color: rgba(255, 255, 255, 0.5);
    backdrop-filter: blur(10px);
    -webkit-backdrop-filter: blur(5px);
    box-shadow: 5px 5px 15px rgba(0, 0, 0, 0.4);
    border-radius: 5px;

}

#PlayersPanel .PlayerRow {
    margin: 5px 10px 5px 10px;
    padding: 5px;
    font-size: 12px;
    color: black;
    border: 1px black solid;
    background-color: transparent;
    border-radius: 5px;
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

#MapPanel {
    position: relative;

    padding: 20px 20px 20px 100px;

    margin: 40px 20px 200px 20px;
    max-width: calc(100vw - (40px + 100px));

    background-color: rgba(255, 255, 255, 0.5);
    backdrop-filter: blur(10px);

    -webkit-backdrop-filter: blur(5px);
    border: none;
    border-radius: 5px;
    box-shadow: 5px 5px 15px rgba(0, 0, 0, 0.4);

    box-sizing: border-box;
    z-index: 6;


}

#MapPanel #ReserveIcon {
    position: absolute;
    left: 20px;
    top: 50px;
    border: 2px black solid;
    font-size: 30px;
    border-radius: 500px;
    height: 50px;
    width: 50px;
    display: block;
    transform: scale(0.95);
    transition: all 0.3s ease;
}

#MapPanel #ReserveIcon:hover {
    cursor: pointer;
    text-shadow: 0 0 5px rgba(255, 255, 255, 1), 0 0 10px rgba(255, 255, 255, 0.9);
    box-shadow: 0 0 5px rgba(255, 255, 255, 1), 0 0 20px rgba(255, 255, 255, 0.9);
    transform: scale(1);
}

#MapPanel #ReserveIcon:disabled {
    opacity: 0.2;
    cursor: not-allowed;
}

#MapPanel #AttackIcon {
    position: absolute;
    left: 20px;
    top: 120px;
    border: 2px black solid;
    font-size: 30px;
    border-radius: 500px;
    height: 50px;
    width: 50px;
    display: block;

    transform: scale(0.95);
    transition: all 0.3s ease;
}

#MapPanel #AttackIcon:hover {
    cursor: pointer;
    text-shadow: 0 0 5px rgba(255, 255, 255, 1), 0 0 10px rgba(255, 255, 255, 0.9);
    box-shadow: 0 0 5px rgba(255, 255, 255, 1), 0 0 20px rgba(255, 255, 255, 0.9);
    transform: scale(1);
}

#MapPanel #AttackIcon:disabled {
    opacity: 0.2;
    cursor: not-allowed;
}



#MapPanel #TroopsInput {
    position: absolute;
    left: 20px;
    top: 170px;

    margin: 0px;
    padding: 2px;
    width: 70px;

    border-radius: 5px;
    border: 2px black solid;
    color: black;
    background-color: transparent;

}

#MapPanel #TroopsInput:disabled {
    opacity: 0.2;
    cursor: not-allowed;
}





#MapPanel #ResignIcon {
    position: absolute;
    left: 20px;
    top: 220px;
    border: 2px black solid;
    font-size: 30px;
    border-radius: 500px;
    height: 50px;
    width: 50px;
    display: block;

    transform: scale(0.95);
    transition: all 0.3s ease;
}

#MapPanel #ResignIcon:hover {
    cursor: pointer;
    text-shadow: 0 0 5px rgba(255, 255, 255, 1), 0 0 10px rgba(255, 255, 255, 0.9);
    box-shadow: 0 0 5px rgba(255, 255, 255, 1), 0 0 20px rgba(255, 255, 255, 0.9);
    transform: scale(1);
}

#MapPanel #ResignIcon:disabled {
    opacity: 0.2;
    cursor: not-allowed;
}

#MapSvg {
    width: 100%;
    height: 100%;
    margin: 0px;
    padding: 0px;

    border-left: 1px black solid;
    background-color: transparent;
    display: flex;
}

#MapSvg .Continent {
    fill: none;
    /*stroke: #1a75ff;*/
    stroke: rgba(0, 0, 0, 0.3);
    stroke-width: 10;
    filter: drop-shadow(3px 3px 5px rgba(0, 0, 0, 0.9));
}


/* Large-tól md-ig (lgAndDown) */
@media (max-width: 1903px) {
    .div1 {
        font-size: 18px;
        padding: 20px;
    }

    .div2 {
        font-size: 16px;
        padding: 20px;
    }
}

/* Medium-tól sm-ig (mdAndDown) */
@media (max-width: 1263px) {
    .div1 {
        font-size: 16px;
        padding: 16px;
    }

    .div2 {
        font-size: 14px;
        padding: 16px;
    }
}

/* Small-tól xs-ig (smAndDown) */
@media (max-width: 959px) {


    #PlayersPanel {
        background-color: red;
    }
}

/* Extra small (xs) – mobil, legkisebb képernyő */
@media (max-width: 599px) {
    .div1 {
        font-size: 12px;
        padding: 8px;
    }

    .div2 {
        font-size: 10px;
        padding: 8px;
    }
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
</style>