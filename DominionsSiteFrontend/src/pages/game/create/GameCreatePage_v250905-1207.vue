<template>
    <v-row dense>
        <v-col v-for="item in BoardListComponent.rows" :key="item.uuid" cols="3">
            <v-card :class="{'mx-auto':true,'GameCard':true,'Disabled':!item.isOpen}" @click="eventOnClickGameCard(item)">
                <v-card-title>
                    {{item.title}}
                </v-card-title>
                <v-img height="200px" :src="item.boardImage ? '/image/boards/'+item.boardImage : 'https://cdn.vuetifyjs.com/images/cards/sunshine.jpg'" cover :style="{filter: item.isOpen ? 'grayscale(0%)' : 'grayscale(90%)'}"></v-img>
                <v-card-text>
                    <table>
                        <tr>
                            <td>Difficulty</td>
                            <td>{{item.difficulty}}</td>
                        </tr>
                        <tr>
                            <td>Human players</td>
                            <td>{{item.playerHumanCount}}</td>
                        </tr>
                        <tr>
                            <td>AI players</td>
                            <td>{{item.playerAiCount}}</td>
                        </tr>

                    </table>
                </v-card-text>


            </v-card>
        </v-col>
    </v-row>


</template>

<script>
    import TController from "@/framework/TController";
    import TConfirmDialog from "@/framework/component/TConfirmDialog/TConfirmDialog";
    import gameService from "@/services/game/GameService";
    import {useGlobalStore} from "@/stores/UseGlobalStore";
    import authService from "@/services/auth/AuthService";

    export default {
        components: {},
        data: () => ({
            GlobalStore: useGlobalStore(),
            BoardListComponent: {
                rows: []
            }
        }),
        created() {
            this.init();
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

                    //List all rows
                    const listRows = await gameService.listAllPublishedScenarios();

                    // Inject values
                    this.BoardListComponent.selected = [];
                    this.BoardListComponent.rows = listRows;

                } catch(exp) {
                    await TController.displayExceptionMessages(exp);
                }
            },


            async eventOnClickGameCard(boardData) {

                try {
                    // Confirming
                    const confirmed = await TConfirmDialog('Do you wish to CREATE a new game?')
                    if(!confirmed.ok) {
                        return;
                    }

                    // TODO: mapcode?
                    // Communicate with backend
                    const gameCreateResponse = await gameService.create({
                        mapCode: "www",
                        endpointKey:authService.getCurrentSessionId(),
                        userUuid:authService.getCurrentUserUuid(),
                        playerName:authService.getCurrentUserIdentifier()
                    });

                    // Navigate
                    this.GlobalStore.GamePlayPage.gameSessionUuid = gameCreateResponse.gameSessionUuid;
                    // this.GlobalStore.GamePlayPage.play.configuration = gameCreateResponse.gameMapConfiguration;
                    // this.GlobalStore.GamePlayPage.play.state = gameCreateResponse.gameMapState;
                    // this.GlobalStore.GamePlayPage.play.statusCode = gameCreateResponse.statusCode;
                    // this.GlobalStore.GamePlayPage.play.maxHumanPlayerCount = gameCreateResponse.maxHumanPlayerCount;
                    // this.GlobalStore.GamePlayPage.play.currentHumanPlayerCount = gameCreateResponse.currentHumanPlayerCount;
                    this.GlobalStore.GamePlayPage.play.yourIndex =  gameCreateResponse.playerIndex
                    this.$router.push({
                        path: '/game/play',
                    });

                } catch(exp) {
                    await TController.displayExceptionMessages(exp);
                }
            },


        }
    }
</script>
<style scoped>
    .GameCard {

    }

    .GameCard.Disabled {

    }

    .GameCard.Disabled img {
        filter: grayscale(90%)
    }

    .GameCard:hover {
        cursor: pointer;
    }

    .GameCard.Disabled:hover {
        cursor: default;
    }
</style>

