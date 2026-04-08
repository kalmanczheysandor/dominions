<template>
    <v-container>
    <v-row dense >
        <v-col v-for="item in scenarioListComponent.rows" :key="item.uuid" lg="3"  sm="6" cols="12" class="pa-3">
            <v-card :class="{'mx-auto':true,'GameCard':true,'Disabled':false}" @click="eventOnClickGameScenarioCard(item)">
                    <v-img
                            class="align-end text-white"
                            height="150"
                            :src="'http://localhost:15000/site/game/scenario/'+item.uuid+'/image/main'"
                            cover
                            :style="{filter: true ? 'grayscale(0%)' : 'grayscale(90%)'}"
                    >
                        <v-card-title> {{item.title}}</v-card-title>
                    </v-img>









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
        </v-container>


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
            scenarioListComponent: {
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
                    this.scenarioListComponent.selected = [];
                    this.scenarioListComponent.rows = listRows;

                } catch(exp) {
                    await TController.displayExceptionMessages(exp);
                }
            },


            async eventOnClickGameScenarioCard(scenarioData) {

                try {
                    // Confirming
                    const confirmed = await TConfirmDialog('Do you wish to CREATE a new game?')
                    if(!confirmed.ok) {
                        return;
                    }

                    // TODO: mapcode?
                    // Communicate with backend
                    const gameCreateResponse = await gameService.create({
                        scenarioUuid: scenarioData.uuid,
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
        background-color: rgba(255, 255, 255, 0.7);
    }

    .GameCard:hover {
        background-color: rgba(255, 255, 255, 0.9);
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

    .JoinIcon {
        opacity: 1;
        transform: scale(0.8);
        transition: all 0.3s ease;
        border: 3px solid rgba(255, 255, 255, 0.0) !important;
        color: rgba(255, 255, 255, 0.0) !important;
    }

    .GameCard:hover .JoinIcon {
        opacity: 1;
        transform: scale(1);
        border: 3px solid rgba(255, 255, 255, 0.7) !important;
        color: rgba(255, 255, 255, 0.7) !important;
        border-radius: 500px;
        cursor: pointer;
        filter: drop-shadow(0 0 20px rgba(0, 0, 0, 1)) drop-shadow(0 0 40px rgba(0, 0, 0, 0.9));
    }

</style>

