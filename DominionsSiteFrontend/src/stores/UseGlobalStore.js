import {defineStore} from "pinia";

export const useGlobalStore = defineStore('GlobalStore', {
    state: () => ({
        GameLaunchPage: {},
        GameLobbyPage: {
            boardUuid: null
        },
        GamePlayPage: {
            gameSessionUuid: null,
            play: {
                configuration:{
                    view: null,
                },
                state: null,
                statusCode:null,
                maxHumanPlayerCount:null,
                currentHumanPlayerCount:null,
                yourIndex:null
            }
        }
    }),
    persist: {
        enabled: true,
        strategies: [
            { storage: sessionStorage, paths: ['GamePlayPage'] }
        ]
    }
});
