<template>


    <v-container>

            <v-row align="start" justify="start" class="fill-height NavigationMenu" style="border:1px blue solid;">
                <v-col cols="1" class="justify-center NavColumn" >
                    <button type="button"
                            title="Game"
                            class="mdi-gamepad-variant-outline mdi MenuCard"
                            @click="eventClickOnPlayMenuButton"
                    />
                </v-col>
                <v-col cols="1" class="justify-center NavColumn">
                    <button type="button"
                            title="Settings"
                            class="mdi-cog-outline mdi MenuCard"
                            @click="eventClickOnSettingsMenuButton"
                    />
                </v-col>
                <v-col cols="1" class="justify-center NavColumn">
                    <button type="button"
                            title="Charts"
                            class="mdi-finance mdi MenuCard"
                            @click="eventClickOnChartsMenuButton"
                    />
                </v-col>
                <v-col cols="1" class="justify-center NavColumn">
                    <button type="button"
                            title="Logout"
                            class="mdi-exit-to-app mdi MenuCard"
                            @click="eventClickOnLogoutMenuButton"
                    />
                </v-col>

            </v-row>



        <h3 class="SectionTitle">
            Already existing
        </h3>

        <v-row>
            <v-col cols="12" lg="6" md="6" sm="12">
                <v-card dense class="d-flex flex-no-wrap justify-space-between NewCard">
                    <v-avatar class="ma-0 " rounded="0" size="250" title="New">
                        <div class="d-flex align-center justify-center">
                            <v-icon icon="mdi-plus" size="100" class="NewIcon"
                                    @click="eventOnClickCreateButton()"></v-icon>
                        </div>
                    </v-avatar>
                </v-card>
            </v-col>
            <v-col v-for="item in PlayListComponent.rows" :key="item.uuid" cols="12" lg="6" md="6" sm="12">


                <v-card dense class="d-flex flex-no-wrap justify-space-between LobbyCard">
                    <v-avatar class="ma-0" rounded="0" size="250" title="Join">
                        <v-img
                                :src="'http://localhost:15000/site/game/scenario/'+item.scenarioUuid+'/image/main'"
                                class="d-flex align-center justify-center"
                        >

                            <v-icon icon="mdi-play" size="100" class="JoinIcon"
                                    @click="eventOnClickLobbyCard(item)"></v-icon>
                        </v-img>
                    </v-avatar>
                    <div>

                        <v-card-title>{{ item.title }}</v-card-title>
                        <v-card-text>
                            available slots:{{ item.freeSlotCount }}
                            Some text describing the content of the card.
                            Some text describing the content of the card.
                            Some text describing the content of the card.
                            Some text describing the content of the card.
                            Some text describing the content of the card.
                        </v-card-text>
                    </div>
                </v-card>
            </v-col>
        </v-row>
    </v-container>


</template>

<script>
import TController from "@/framework/TController";
import {useGlobalStore} from "@/stores/UseGlobalStore";
import gameLobbyService from "@/services/game/lobby/GameLobbyService";
import TConfirmDialog from "@/framework/component/TConfirmDialog/TConfirmDialog";
import gameService from "@/services/game/GameService";
import authService from "@/services/auth/AuthService";

export default {
    // props: {
    //     boardUuid: String,
    // },
    components: {},
    data: () => ({

        GlobalStore: useGlobalStore(),
        PlayListComponent: {
            rows: []
        }
    }),
    computed: {
        boardUuid() {
            return this.GlobalStore.GameLobbyPage.boardUuid;
        }
    },
    methods: {
        async init() {
            await this.loadList();
        },

        async reloadList() {
            await this.loadList();
        },

        async loadList() {
            try {

                // List all rows
                const listRows = await gameService.listAllRecruiting();

                // Inject values
                this.PlayListComponent.selected = [];
                this.PlayListComponent.rows = listRows;

            } catch (exp) {
                await TController.displayExceptionMessages(exp);
            }
        },
        async eventOnClickCreateButton() {
            try {
                this.$router.push('/game/create');
            } catch (exp) {
                await TController.displayExceptionMessages(exp);
            }
        },

        async eventOnClickLobbyCard(playData) {
            try {

                // Confirming
                const confirmed = await TConfirmDialog('Do you wish to join to the game?');
                if (!confirmed.ok) {
                    return;
                }

                // Confirming
                if (this.GlobalStore.GamePlayPage.play.state !== null) {
                    const leaveIt = await TConfirmDialog('You are already in an other game! Do you wish to leave it?');
                    if (!leaveIt.ok) {
                        return;
                    }
                }

                // Communicate with backend
                const gameJoinResponse = await gameService.join({
                    gameSessionUuid: playData.uuid,
                    endpointKey: authService.getCurrentSessionId(),
                    userUuid: authService.getCurrentUserUuid(),
                    playerName: authService.getCurrentUserIdentifier()
                });

                // Navigate
                this.GlobalStore.GamePlayPage.gameSessionUuid = gameJoinResponse.gameSessionUuid;
                this.GlobalStore.GamePlayPage.play.configuration = gameJoinResponse.gameMapConfiguration;
                this.GlobalStore.GamePlayPage.play.state = gameJoinResponse.gameMapState;
                this.GlobalStore.GamePlayPage.play.statusCode = gameJoinResponse.statusCode;
                this.GlobalStore.GamePlayPage.play.maxHumanPlayerCount = gameJoinResponse.maxHumanPlayerCount;
                this.GlobalStore.GamePlayPage.play.currentHumanPlayerCount = gameJoinResponse.currentHumanPlayerCount;
                this.GlobalStore.GamePlayPage.play.yourIndex = gameJoinResponse.playerIndex
                this.$router.push({
                    path: '/game/play',
                });

            } catch (exp) {
                await TController.displayExceptionMessages(exp);
            }
        },
    },
    mounted() {
        this.init();
    }
}
</script>
<style scoped>
.NavigationMenu {
    position: relative;
    background-color: rgba(0, 0, 0, 0.5);
    height:50px;
    width: 100%;
    margin:0px;
}
.NavigationMenu .NavColumn {
    border:1px red solid;
    padding:0px;
    margin:0px;
}

.NavigationMenu  .MenuCard {
    position: relative;
    margin: 0px !important;
    padding: 0;
    width: 60px;
    height: 60px !important;

    background-color: rgba(0, 0, 0, 0.5);
    border: 5px rgba(0, 0, 0, 0.1) solid;
    border-radius: 500px;
    box-shadow:
            0px 0px 2px rgba(255, 255, 255, 0.9),
            0px 0px 8px rgba(0, 0, 0, 0.9);


    color: white;
    font-size: 20px;

    display: inline-block;
    box-sizing: border-box;

    transition: all 0.5s ease;
    transform: scale(0.95);
}


.NavigationMenu  .MenuCard:hover {
    background-color: rgba(0, 0, 0, 0.6);
    border: 5px rgba(255, 255, 255, 0.2) solid;
    font-size: 30px;
    transform: scale(1);
    text-shadow: 0px 0px 2px rgba(0, 0, 0, 0.5),
    0px 0px 4px rgba(0, 0, 0, 0.5),
    0px 0px 5px rgba(255, 255, 255, 0.9),
    0px 0px 10px rgba(255, 255, 255, 0.9);

    box-shadow:
            0px 0px 2px rgba(255, 255, 255, 0.9),
            0px 0px 20px rgba(0, 0, 0, 0.9);
}












.NewCard {
    background-color: rgba(0, 0, 0, 0.5);
    color: white;
    border: 10px rgba(0, 0, 0, 0.1) solid;
    box-shadow: 0px 0px 5px rgba(255, 255, 255, 0.3);
    transition: all 0.5s ease;
}

.NewCard:hover {
    background-color: rgba(0, 0, 0, 0.6);
    border: 10px rgba(255, 255, 255, 0.2) solid;
}

.NewIcon {
    transform: scale(0.8);
    transition: all 0.3s ease;
    border: 3px solid rgba(255, 255, 255, 0.5) !important;
    border-radius: 500px;
    color: rgba(255, 255, 255, 0.5) !important;
}

.NewCard:hover .NewIcon {
    transform: scale(1);
    border: 3px solid rgba(255, 255, 255, 0.7) !important;
    color: rgba(255, 255, 255, 0.7) !important;

    cursor: pointer;
    filter: drop-shadow(0 0 20px rgba(0, 0, 0, 1)) drop-shadow(0 0 40px rgba(0, 0, 0, 0.9));
}


.LobbyCard {
    background-color: rgba(0, 0, 0, 0.5);
    color: white;
    border: 10px rgba(0, 0, 0, 0.1) solid;
    box-shadow: 0px 0px 5px rgba(255, 255, 255, 0.3);
    transition: all 0.5s ease;
}

.LobbyCard:hover {
    background-color: rgba(0, 0, 0, 0.6);
    border: 10px rgba(255, 255, 255, 0.2) solid;
}


.JoinIcon {
    opacity: 1;
    transform: scale(0.8);
    transition: all 0.3s ease;
    border: 3px solid rgba(255, 255, 255, 0.0) !important;
    color: rgba(255, 255, 255, 0.0) !important;
}

.LobbyCard:hover .JoinIcon {
    opacity: 1;
    transform: scale(1);
    border: 3px solid rgba(255, 255, 255, 0.7) !important;
    color: rgba(255, 255, 255, 0.7) !important;
    border-radius: 500px;
    cursor: pointer;
    filter: drop-shadow(0 0 20px rgba(0, 0, 0, 1)) drop-shadow(0 0 40px rgba(0, 0, 0, 0.9));
}
</style>

