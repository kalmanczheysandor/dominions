import { defineStore } from "pinia";

export const useAuthStore = defineStore('auth', {
    state: () => ({
        isAuthenticated: false,
        user: {
            uuid:"",
            identifier: null,
            permissions: []
        }
    }),
    persist: {
        enabled: true,
        strategies: [
            { storage: localStorage, paths: ['isAuthenticated', 'user'] }
        ]
    }
});
