<template>
        <MenuBar/>
        <h3 class="SectionTitle">
            Available scenarios
        </h3>
        <v-row dense>
            <v-col v-for="item in scenarioListComponent.rows" :key="item.uuid" lg="3" sm="6" cols="12" class="pa-3">
                <v-card :class="{'mx-auto':true,'GameCard':true,'Disabled':false}">

                    <v-img
                            height="150"
                            cover
                            :src="backendConfiguration.BACKEND_BASE_URL+'/game/scenario/'+item.uuid+'/image/main'"
                            class="position-relative CardImage"
                    >
                        <v-icon
                                icon="mdi-play"
                                size="100"
                                class="JoinIcon"
                                style="position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%);"
                                @click="eventOnClickGameScenarioCard(item)"
                        />
                    </v-img>
                    <v-card-text style="padding:10px;">
                        <p class="Title">{{ item.title }}</p>
                        <table class="Details">
                            <tr>
                                <td>Difficulty</td>
                                <td>{{ item.difficulty }}</td>
                            </tr>
                            <tr>
                                <td>Human players</td>
                                <td>{{ item.playerHumanCount }}</td>
                            </tr>
                            <tr>
                                <td>AI players</td>
                                <td>{{ item.playerAiCount }}</td>
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
import MenuBar from "@/components/MenuBar.vue";
import backendConfiguration from "@/configurations/backendConfiguration";

export default {
    computed: {
        backendConfiguration() {
            return backendConfiguration
        }
    },
    components: {MenuBar},
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

            } catch (exp) {
                await TController.handleExceptions(exp);
            }
        },


        async eventOnClickGameScenarioCard(scenarioData) {

            try {
                // Confirming
                const confirmed = await TConfirmDialog('Do you wish to CREATE a new game?')
                if (!confirmed.ok) {
                    return;
                }

                // TODO: mapcode?
                // Communicate with backend
                const gameCreateResponse = await gameService.create({
                    scenarioUuid: scenarioData.uuid,
                    endpointKey: authService.getCurrentSessionId(),
                    userUuid: authService.getCurrentUserUuid(),
                    playerName: authService.getCurrentUserIdentifier()
                });

                // Navigate
                this.GlobalStore.GamePlayPage.gameSessionUuid = gameCreateResponse.gameSessionUuid;
                // this.GlobalStore.GamePlayPage.play.configuration = gameCreateResponse.gameMapConfiguration;
                // this.GlobalStore.GamePlayPage.play.state = gameCreateResponse.gameMapState;
                // this.GlobalStore.GamePlayPage.play.statusCode = gameCreateResponse.statusCode;
                // this.GlobalStore.GamePlayPage.play.maxHumanPlayerCount = gameCreateResponse.maxHumanPlayerCount;
                // this.GlobalStore.GamePlayPage.play.currentHumanPlayerCount = gameCreateResponse.currentHumanPlayerCount;
                this.GlobalStore.GamePlayPage.play.yourIndex = gameCreateResponse.playerIndex
                this.$router.push({
                    path: '/game/play',
                });

            } catch (exp) {
                await TController.handleExceptions(exp);
            }
        },


    }
}
</script>
<style scoped>
.GameCard {
    background-color: rgba(0, 0, 0, 0.5);
    color: white;
    border: 10px rgba(0, 0, 0, 0.1) solid;
    box-shadow: 0px 0px 5px rgba(255, 255, 255, 0.3);
    transform: scale(0.95);
    transition: all 0.5s ease;
    border-radius: 5px;
}

.GameCard:hover {
    background-color: rgba(0, 0, 0, 0.6);
    border: 10px rgba(255, 255, 255, 0.2) solid;
    transform: scale(1);

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


.GameCard .CardImage {
    opacity: 1;
}

.GameCard:hover .CardImage {
    opacity: 1;
}


.GameCard .JoinIcon {
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

.GameCard .Title {
    font-size: 16px;
    font-weight: bold;
    color: #ffffff;
}

.GameCard .Details  td {
    font-size: 10px;
    color: #c0c0c0;
    padding-right: 5px;
}




</style>

