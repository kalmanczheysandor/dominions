<template>

    <div class="TGameBoard" v-if="(configuration.view !== null)">
        <div id="AvatarsPanel">
            <div class="AvatarRow" v-for="(playerItem, playerIndex) in state.players"
                 :key="'PlayersPanel-Row-'+playerIndex">
                <div v-if="playerItem.isAlive" :class="['PlayerAvatar','Player'+playerIndex]">
                    <div class="AvatarLabel">{{ calculateTerritory(playerIndex) }}%</div>
                </div>
                <div v-else class="PlayerAvatar DeadPlayer ">
                    <div class="AvatarLabel">{{ calculateTerritory(playerIndex) }}%</div>
                </div>
                <div class="AvatarInfoTray">
                    <div class="AvatarInfoBox">
                        <p v-if="playerItem.isAlive" class="NameParagraph">{{ playerItem.name }}</p>
                        <p v-else class="NameParagraph" style="text-decoration:line-through;">{{ playerItem.name }}</p>
                        <p class="DataParagraph">
                            <span>Reserve:&nbsp;{{ playerItem.reserveSize }}</span>
                            &nbsp;|&nbsp;
                            <span>Territory:&nbsp;{{ calculateTerritory(playerIndex) }}%</span>
                            &nbsp;|&nbsp;
                            <span>{{
                                    playerIndex == yourIndex ? '(You)' : configuration.players[playerIndex].type
                                }}</span>
                            &nbsp;|&nbsp;
                            <span>{{ playerItem.isIntentionGiven ? 'Done' : 'Thinking' }}</span>
                        </p>

                    </div>
                </div>
            </div>
        </div>
        <div id="TurnLabel"></div>
        <div id="MapPanel" style="">
            <div id="ActionBar">
                <div id="Section1">
                    <button type="button" title="Reserve" :disabled="action.targetKey!=null"
                            id="ReserveIcon"
                            class=" mdi-piggy-bank-outline mdi"
                            @click="eventReserveAction"
                    />
                    <button type="button" title="Attack" :disabled="action.targetKey==null" id="AttackIcon"
                            class="mdi-sword mdi" @click="eventAttackAction"/>
                    <input v-show="false" id="TargetInput" ref="TargetInput" type="text" placeholder="Target"
                           :value="action.targetKey"/>
                    <input :disabled="action.targetKey==null" id="TroopsInput" ref="TroopsInput" type="text"
                           placeholder="Troops" v-model="action.troops"/>
                </div>
                <div id="Section2">

                </div>
                <div id="Section3">
                    <button type="button" title="Resign" id="ResignIcon"
                            class=" mdi-exit-run mdi"
                            @click="eventAttemptResign"
                    />
                </div>
            </div>
            <div id="DeveloperInfo">[turn:{{ turn }}] [Target:#{{ action.targetKey }}][above:{{ aboveIndex }}]</div>
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
    <div class="BackDropLayer" v-if="isAttemptingToResign">
        <div class="TDialog GlassDialog">
            <div class="Body">
                <p> Do you wish to leave the game?</p>
            </div>
            <div class="Footer">
                <button type="button" class="TButton" @click="eventResignAction">Yes</button>
                <button type="button" class="TButton" @click="this.attemptingToResign=false">No</button>
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
        'whenActionIsResign',
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
        },

        isAttemptingToResign() {
            return this.attemptingToResign;
        },


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
            winnerKeys: [],
            attemptingToResign: false
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

        // async eventLeaveGame() {
        //     // Confirming
        //     const confirmed = await TConfirmDialog('Do you wish to continue?');
        //     if (!confirmed.ok) {
        //         return;
        //     }
        //
        //     this.$emit('whenActionIsResign', {});
        // },
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
        eventResignAction() {
            // Execute
            this.$emit('whenActionIsResign', {
                playerIndex: this.yourIndex
            });

            this.attemptingToResign = false;
            this.action.targetKey = null;
            this.action.troops = null;
        },
        eventAttemptResign() {
            this.attemptingToResign = true;
        },


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
        },

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /// [ ETC methods ] //////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        adjustCountryLabels() {
            for (const labelItem of this.configuration.view.foreground.countryLabels) {
                this.adjustCountryLabelPosition(labelItem);
            }
        },

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

        },

        countOccupiedCells(playerIndex) {
            let count = 0;

            Object.entries(this.state.cells).forEach(([cellIndex, cellItem]) => {
                if (parseInt(cellItem.occupierKey) === parseInt(playerIndex)) {
                    count++;
                }
            });
            return count;
        },

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

        },

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
        },

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
        },

        findBelongingLabelDataOfCountry(countryIndex) {
            const labelData = this.configuration.view.foreground.countryLabels.find(label => label.ownerCountryKey == countryIndex)
            if (labelData == undefined) {
                throw new TGameError('Label could not be found!');
            }
            return labelData
        },

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


        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /// [ Is... methods ] //////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        areYouANeighbourOfTheCell(cellIndex) {
            this.assertCellExistAtIndex(cellIndex);
            return this.isCellANeighbourOfPlayer(cellIndex, this.yourIndex);
        },
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
        },
        isCountryExistAtIndex(countryIndex) {
            const countryData = this.configuration.view.foreground.countries.find(country => country.countryKey == countryIndex)
            if (countryData == undefined) {
                return false;
            }
            return true;
        },
        isCellExistAtIndex(cellIndex) {
            if (cellIndex in this.state.cells) {
                return true;
            }
            return false;
        },
        isPlayerExistAtIndex(playerIndex) {
            if (playerIndex in this.state.players) {
                return true;
            }
            return false;
        },
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
        },
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
        },
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
        },
        gatherCountryData(countryIndex) {
            this.assertCountryExistAtIndex(countryIndex);
            return this.configuration.view.foreground.countries.find(country => country.countryKey == countryIndex);
        },

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /// [ Assert methods ] //////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        assertCountryExistAtIndex(countryIndex) {
            if (!this.isCountryExistAtIndex(countryIndex)) {
                throw new TGameError('No country data can be found at index:' + countryIndex + '!');
            }
        },
        assertPlayerExistAtIndex(playerIndex) {
            if (!this.isPlayerExistAtIndex(playerIndex)) {
                throw new TGameError('No player data can be found at index:' + playerIndex + '!');
            }
        },
        assertCellExistAtIndex(cellIndex) {
            if (!this.isCellExistAtIndex(cellIndex)) {
                throw new TGameError('No cell data can be found at index:' + cellIndex + '!');
            }
        },

    }
    ,
    mounted() {
        this.eventInit();
    }
}
</script>
<style scoped>

.TGameBoard {
    border: none;
    margin: 0px;
    padding: 0px;
    position: relative;
}

#AvatarsPanel {
    position: absolute;
    top: 0px;
    right: 0px;
    border: none;
    background-color: transparent;
    z-index: 1000;
}


#AvatarsPanel .AvatarRow {
    position: relative;
    height: 50px;
    width: 50px;

    padding: 0px;
    margin: 0px 0px 25px 0px;

    display: block;

    border: none;
}


#AvatarsPanel .PlayerAvatar {
    position: absolute;
    top: 0px;
    right: 0px;

    width: 60px;
    height: 60px;
    margin: 0px;

    background-color: rgba(204, 0, 0, 1);
    backdrop-filter: blur(10px);
    -webkit-backdrop-filter: blur(5px);
    box-shadow: 0px 0px 15px rgba(0, 0, 0, 0.4),
    8px 8px 15px rgba(255, 255, 255, 0.3) inset,
    -8px -8px 15px rgba(0, 0, 0, 0.3) inset;

    border: 5px rgba(204, 0, 0, 0.7) solid;

    border-radius: 50%;
    z-index: 1002;

    overflow: hidden;


}


#AvatarsPanel .PlayerAvatar::before {
    content: "";
    position: absolute;
    top: 0;
    left: -220%;
    width: 220%;
    height: 100%;
    background: linear-gradient(
            120deg,
            rgba(255, 255, 255, 0) 0%,
            rgba(255, 255, 255, 0.3) 50%,
            rgba(255, 255, 255, 0) 100%
    );
    transform: skewX(-25deg);
}

#AvatarsPanel .PlayerAvatar:hover::before {
    animation: shine 2s ease;
}

@keyframes shine {
    0% {
        left: -75%;
    }
    100% {
        left: 125%;
    }
}

#AvatarsPanel .PlayerAvatar > .AvatarLabel {
    position: absolute;
    left: 0px;
    top: 13px;
    right: 0px;
    font-size: 17px;
    font-weight: bold;
    text-align: center;
    color: white;
    border: 0px white solid;
}


#AvatarsPanel .AvatarRow:hover .PlayerAvatar {
    border: 5px rgba(255, 255, 255, 0.9) solid;
    box-shadow: 0px 0px 15px rgba(255, 255, 255, 0.0);
}


#AvatarsPanel .AvatarRow .AvatarInfoTray {
    display: block;
    position: absolute;
    top: 0px;
    right: 0px;
    padding: 10px;
    background-color: rgba(0, 0, 0, 0.7);
    border-radius: 100px 100px 100px 100px;
    border-right: none;
    width: 60px;
    height: 60px;
    z-index: 1001;

    transition: width 0.3s ease; /* idő és görbe */
    overflow: hidden;
    max-height: 60px;

    box-shadow: 5px 5px 10px rgba(0, 0, 0, 0.9);

}

#AvatarsPanel .AvatarRow .AvatarInfoTray .AvatarInfoBox {

    opacity: 0;
    transition: opacity 1s cubic-bezier(0.9, 0.1, 0.3, 1);
    visibility: hidden;
}

#AvatarsPanel .AvatarRow .AvatarInfoTray .AvatarInfoBox .NameParagraph {
    color: rgba(255, 255, 255, 0.7);
    margin-left: 10px;
}

#AvatarsPanel .AvatarRow .AvatarInfoTray .AvatarInfoBox .DataParagraph {
    font-size: 10px;
    color: rgba(255, 255, 255, 0.7);
    margin-left: 10px;
}

#AvatarsPanel .AvatarRow:hover .AvatarInfoTray {
    width: 400px;
}

#AvatarsPanel .AvatarRow:hover .AvatarInfoTray .AvatarInfoBox {

    opacity: 1;
    visibility: visible;

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
    position: fixed;
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


.DeadPlayer {
    fill: grey !important;
    color: grey !important;
    background-color: grey !important;
    border-color: grey !important;
}


.Player0 {
    fill: red !important;
    color: red !important;
    background-color: red !important;
    border-color: red !important;
}

.Player1 {
    fill: blue !important;
    color: blue !important;
    background-color: blue !important;
    border-color: blue !important;
}


.Player2 {
    fill: orange !important;
    color: orange !important;
    background-color: orange !important;
    border-color: orange !important;
}

.Player4 {
    fill: green !important;
    color: green !important;
    background-color: green !important;
    border-color: green !important;
}


.Player5 {
    fill: indigo !important;
    color: indigo !important;
    background-color: indigo !important;
    border-color: indigo !important;
}

.Player6 {
    fill: blueviolet !important;
    color: blueviolet !important;
    background-color: blueviolet !important;
    border-color: blueviolet !important;
}


#MapPanel {
    position: relative;

    padding: 20px 20px 20px 20px;

    margin: 0px 0px 0px 0px;
    max-width: calc(100% - (0px + 80px));

    background-color: rgba(255, 255, 255, 0.5);
    backdrop-filter: blur(10px);

    -webkit-backdrop-filter: blur(5px);
    border: none;
    border-radius: 5px;
    box-shadow: 5px 5px 15px rgba(0, 0, 0, 0.4);

    box-sizing: border-box;


}

#MapPanel > #DeveloperInfo {
    position: absolute;
    bottom: 5px;
    left: 5px;

    height: 20px;
    width: 200px;
    padding: 0px;
    margin: 0px;

    color: rgba(0, 0, 0, 0.2);
    font-size: 10px;
    font-weight: normal;

    border: 0px red solid;
}

#MapPanel > #ActionBar {
    position: relative;

    border-bottom: 1px black solid;
    height: 60px;
}

#MapPanel > #ActionBar > #Section1 {
    position: absolute;
    top: 0px;
    left: 0px;
    border-right: 1px black solid;
    height: 50px;
    width: 210px;
}

#MapPanel > #ActionBar > #Section2 {
    position: absolute;
    top: 0px;
    left: 220px;
    right: 70px;
    border: none;
    height: 50px;
    color: rgba(0, 0, 0, 0.1);
    font-size: 20px;
    font-weight: normal;
}

#MapPanel > #ActionBar > #Section3 {
    position: absolute;
    top: 0px;
    right: 0px;
    border-left: 1px black solid;
    height: 50px;
    width: 60px;
}

#MapPanel > #ActionBar > #Section1 > #ReserveIcon {
    position: absolute;
    top: 0px;
    left: 0px;
    border: 2px black solid;
    font-size: 30px;
    border-radius: 500px;
    height: 50px;
    width: 50px;
    display: block;
    transform: scale(0.95);
    transition: all 0.3s ease;
}

#MapPanel > #ActionBar > #Section1 > #ReserveIcon:hover {
    cursor: pointer;
    text-shadow: 0 0 5px rgba(255, 255, 255, 1), 0 0 10px rgba(255, 255, 255, 0.9);
    box-shadow: 0 0 5px rgba(255, 255, 255, 1), 0 0 20px rgba(255, 255, 255, 0.9);
    transform: scale(1);
}

#MapPanel > #ActionBar > #Section1 > #ReserveIcon:disabled {
    opacity: 0.2;
    cursor: not-allowed;
}

#MapPanel > #ActionBar > #Section1 > #AttackIcon {
    position: absolute;
    left: 60px;
    top: 0px;
    border: 2px black solid;
    font-size: 30px;
    border-radius: 500px;
    height: 50px;
    width: 50px;
    display: block;

    transform: scale(0.95);
    transition: all 0.3s ease;
}

#MapPanel > #ActionBar > #Section1 > #AttackIcon:hover {
    cursor: pointer;
    text-shadow: 0 0 5px rgba(255, 255, 255, 1), 0 0 10px rgba(255, 255, 255, 0.9);
    box-shadow: 0 0 5px rgba(255, 255, 255, 1), 0 0 20px rgba(255, 255, 255, 0.9);
    transform: scale(1);
}

#MapPanel > #ActionBar > #Section1 > #AttackIcon:disabled {
    opacity: 0.2;
    cursor: not-allowed;
}


#MapPanel > #ActionBar > #Section1 > #TroopsInput {
    position: absolute;
    left: 120px;
    top: 10px;

    margin: 0px;
    padding: 2px;
    width: 70px;

    border-radius: 5px;
    border: 2px black solid;
    color: black;
    background-color: transparent;

}

#MapPanel > #ActionBar > #Section1 > #TroopsInput:disabled {
    opacity: 0.2;
    cursor: not-allowed;
}


#MapPanel > #ActionBar > #Section3 > #ResignIcon {
    position: absolute;
    top: 0px;
    right: 0px;
    border: 2px black solid;
    font-size: 30px;
    border-radius: 500px;
    height: 50px;
    width: 50px;
    display: block;

    transform: scale(0.95);
    transition: all 0.3s ease;
}

#MapPanel > #ActionBar > #Section3 > #ResignIcon:hover {
    cursor: pointer;
    text-shadow: 0 0 5px rgba(255, 255, 255, 1), 0 0 10px rgba(255, 255, 255, 0.9);
    box-shadow: 0 0 5px rgba(255, 255, 255, 1), 0 0 20px rgba(255, 255, 255, 0.9);
    transform: scale(1);
}

#MapPanel > #ActionBar > #Section3 > #ResignIcon:disabled {
    opacity: 0.2;
    cursor: not-allowed;
}

#MapSvg {
    width: 100%;
    height: 100%;
    margin: 0px;
    padding: 0px;


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


@media (max-width: 1903px) {

}

@media (max-width: 1263px) {

}

@media (max-width: 959px) {

}

@media (max-width: 599px) {

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