import {defineStore} from "pinia";

export const useAuthStore = defineStore('auth', {
    state: () => ({
        isAuthenticated: false,
        sessionId: null,
        user: {
            uuid: "",
            identifier: null,
            permissions: []
        }
    }),
    persist: {
        enabled: true,
        strategies: [
            // {storage: localStorage, paths: ['isAuthenticated', 'user']}
            { storage: sessionStorage, paths: ['isAuthenticated', 'user'] }

        ]
    }
});
