<template>
    boardId:{{boardUuid}}
    <button class="TButton MenuButton" @click="eventOnClickCreateButton()">Create</button>

    <v-row v-for="item in PlayListComponent.rows" :key="item.uuid">
        <v-col cols="12">
            <v-card>
                <v-row no-gutters>

                    <v-col cols="3">
                        <v-img :src="item.boardImage ? '/image/boards/'+item.boardImage : 'https://cdn.vuetifyjs.com/images/cards/sunshine.jpg'" height="100%"/>
                    </v-col>

                    <v-col cols="9">
                        <v-card-title>{{item.title}}</v-card-title>
                        <v-card-subtitle>Card Subtitle</v-card-subtitle>
                        available slots:{{item.freeSlotCount}}
                        uuid:{{item.uuid}}
                        <v-card-text>
                            Some text describing the content of the card.
                        </v-card-text>
                        <button class="TButton" @click="eventOnClickLobbyCard(item)">Join</button>
                    </v-col>
                </v-row>
            </v-card>
        </v-col>
    </v-row>

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

                } catch(exp) {
                    await TController.displayExceptionMessages(exp);
                }
            },
            async eventOnClickCreateButton() {
                try {
                    this.$router.push('/game/create');
                } catch(exp) {
                    await TController.displayExceptionMessages(exp);
                }
            },

            async eventOnClickLobbyCard(playData) {
                try {

                    // Confirming
                    const confirmed = await TConfirmDialog('Do you wish to join to the game?');
                    if(!confirmed.ok) {
                        return;
                    }

                    // Confirming
                    if(this.GlobalStore.GamePlayPage.play.state!==null) {
                        const leaveIt = await TConfirmDialog('You are already in an other game! Do you wish to leave it?');
                        if(!leaveIt.ok) {
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
                    this.GlobalStore.GamePlayPage.play.yourIndex =  gameJoinResponse.playerIndex
                    this.$router.push({
                        path: '/game/play',
                    });

                } catch(exp) {
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

</style>

