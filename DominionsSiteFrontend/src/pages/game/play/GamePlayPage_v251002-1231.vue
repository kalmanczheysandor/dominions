<template>
    <GameBoard ref="GameBoardComponent"
               @whenSelectedYourself="whenSelectedYourself"
               @whenSelectedOutOfNeighbourhood="whenSelectedOutOfNeighbourhood"
               @whenAttackButTroopSizeIsOverTheAvailable="whenAttackButTroopSizeIsOverTheAvailable"
               @whenActionIsAttack="whenActionIsAttack"
               @whenActionIsReserve="whenActionIsReserve"
               @whenLeaveGame="whenLeaveGame"
               @whenActionIsAlreadySent="whenActionIsAlreadySent"
    />
</template>

<script>
    import TController from "@/framework/TController";
    import {useGlobalStore} from "@/stores/UseGlobalStore";
    import gameLobbyService from "@/services/game/lobby/GameLobbyService";
    import TConfirmDialog from "@/framework/component/TConfirmDialog/TConfirmDialog";
    import TString from "@/framework/utils/TString";
    import GameBoard from "@/components/GameBoard/GameBoard.vue";
    import TPhotoGrid from "@/framework/component/TPhotoGrid/TPhotoGrid.vue";
    import {useToast} from "vue-toastification";
    import gameService from "@/services/game/GameService";
    import authService from "@/services/auth/AuthService";
    import TParameterValidation from "@/framework/utils/TParameterValidation";

    export default {
        components: {GameBoard},
        data: () => ({
            GlobalStore: useGlobalStore(),
        }),
        computed: {
            gameSessionUuid() {
                return this.GlobalStore.GamePlayPage.gameSessionUuid;
            }
        },
        methods: {
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            /// [ Event methods ] //////////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            async eventInit() {
                try {
                    await this.loadGame();
                } catch(exp) {
                    await TController.displayExceptionMessages(exp);
                }
            },
            async eventOnClickLobbyCard(playData) {
                // Confirming
                const confirmed = await TConfirmDialog('Do you wish to continue?');
                if(!confirmed.ok) {
                    return;
                }
                this.GlobalStore.GameLobbyPage.playUuid = playData.uuid;
                this.$router.push({
                    path: '/game/play',
                });
            },


            async eventAttack(actionData) {
                // Input checking
                TParameterValidation.assertParameterIsNotNull(actionData, 'actionData');

                try {

                    // Communicate with backend
                    const gameAttackResponse = await gameService.sendAttackAction({
                        gameSessionUuid: this.GlobalStore.GamePlayPage.gameSessionUuid,
                        endpointKey: authService.getCurrentSessionId(),
                        userUuid: authService.getCurrentUserUuid(),
                        targetCellKey: actionData.targetKey,
                        attackingTroopSize: actionData.troopsSize,
                        playerIndex: actionData.playerIndex
                    });

                } catch(exp) {
                    await TController.displayExceptionMessages(exp);
                }

            },


            async eventReserve(actionData) {
                // Input checking
                TParameterValidation.assertParameterIsNotNull(actionData, 'actionData');

                try {

                    // Communicate with backend
                    const gameReserveResponse = await gameService.sendReserveAction({
                        gameSessionUuid: this.GlobalStore.GamePlayPage.gameSessionUuid,
                        endpointKey: authService.getCurrentSessionId(),
                        userUuid: authService.getCurrentUserUuid(),
                        playerIndex: actionData.playerIndex
                    });

                } catch(exp) {
                    await TController.displayExceptionMessages(exp);
                }

            },


            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            /// [ ???? methods ] //////////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            async loadGame() {

                // Communicate with backend
                const gameStateResponse = await gameService.state({
                    gameSessionUuid: this.GlobalStore.GamePlayPage.gameSessionUuid,
                    endpointKey: authService.getCurrentSessionId(),
                    userUuid: authService.getCurrentUserUuid(),
                    playerName: authService.getCurrentUserIdentifier()
                });
                console.log("XXINIT")
                console.log(gameStateResponse)

                // Load play state
                await this.$refs.GameBoardComponent.loadPlayState({
                    settings: {
                        playerColors: {
                            0: 'red',
                            2: 'green',
                            3: 'blue',
                            4: 'purple',
                            5: 'pink',
                            6: 'yellow',
                        },
                        emptyCountryColor: 'brown'
                    },
                    configuration: gameStateResponse.gameMapConfiguration,
                    state: gameStateResponse.gameMapState,
                    statusCode: gameStateResponse.statusCode,
                    maxHumanPlayerCount: gameStateResponse.maxHumanPlayerCount,
                    currentHumanPlayerCount: gameStateResponse.currentHumanPlayerCount,
                    yourIndex: this.GlobalStore.GamePlayPage.play.yourIndex,
                    turn: gameStateResponse.turn,
                    winnerKeys:gameStateResponse.winnerKeys
                });

                console.log("HELLO" + this.GlobalStore.GamePlayPage.gameSessionUuid)
                // Subscribe

                const gameBoardComponent = this.$refs.GameBoardComponent;
                await gameService.listenOnStateChanel(this.GlobalStore.GamePlayPage.gameSessionUuid, (gameMapStateResponse) => {
                    console.log("---REFRESH---")
                    console.log(gameMapStateResponse)

                    // Refresh values
                    gameBoardComponent.refreshPlayState({
                        state: gameMapStateResponse.gameMapState,
                        statusCode: gameMapStateResponse.statusCode,
                        maxHumanPlayerCount: gameMapStateResponse.maxHumanPlayerCount,
                        currentHumanPlayerCount: gameMapStateResponse.currentHumanPlayerCount,
                        turn: gameMapStateResponse.turn,
                        winnerKeys:gameMapStateResponse.winnerKeys
                    });
                })
            },

            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            /// [ When methods ] //////////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            whenSelectedYourself() {
                const toast = useToast();
                toast.warning('You selected yourself!');
            },
            whenSelectedOutOfNeighbourhood() {
                const toast = useToast();
                toast.warning('Selected country is not in your neighbourhood!');
            },
            whenAttackButTroopSizeIsOverTheAvailable(data) {
                const toast = useToast();
                toast.warning('The expected ' + data.expectedTroopSize + ' troops exceed the available ' + data.reserveSize);
            },
            whenActionIsAlreadySent(data) {
                const toast = useToast();
                toast.warning('You already made your step!');
            },
            whenLeaveGame(data) {
                const toast = useToast();
                toast.warning('The expected ' + data.expectedTroopSize + ' troops exceed the available ' + data.reserveSize);
            },
            whenActionIsAttack(data) {
                this.eventAttack(data);
            },
            whenActionIsReserve(data) {
                this.eventReserve(data);
            }

        },
        async mounted() {
            await this.eventInit();
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

    input {
        border-radius: 3px;
        padding: 2px;
    }


    #ExitButton {
        position: absolute;
        top: 0px;
        right: 0px;
        bottom: 0px;
        width: 150px;
        box-sizing: border-box;
        padding: 10px;
        font-size: 20px;
        font-weight: bold;
        text-align: center;

    }

    #TargetInput, #TroopsInput {

        width: 150px;
        box-sizing: border-box;
        padding: 10px;

        color: white;
        font-size: 20px;
        font-weight: bold;

        background-color: transparent;
        border: 1px white solid;
        border-radius: 4px;
    }


    #TargetInput:focus, #TroopsInput:focus {
        outline: none;
    }


    #AttackButton, #ReserveButton {
        width: 150px;
        box-sizing: border-box;
        padding: 10px;

        color: white;
        font-size: 20px;
        font-weight: bold;
    }

    #AttackButton {
        background-color: #ff0000;
    }

    #AttackButton:hover {
        background-color: #cc0000;
    }


    #ReserveButton {
        background-color: #00cc44;
    }

    #ReserveButton:hover {
        background-color: #00802b;
    }

    #ActionBox {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
    }

    #PlayersPanel {
        position: absolute;
        top: 10px;
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

