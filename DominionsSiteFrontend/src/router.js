import {createRouter, createWebHistory} from "vue-router";

import LoginPage from "@/pages/login/LoginPage.vue";
import MainPage from "@/pages/MainPage.vue";


import {authService} from "@/services/auth/AuthService";
import TWarningDialog from "@/framework/component/TWarningDialog/TWarningDialog";


import GameLobbyPage from "@/pages/game/lobby/GameLobbyPage.vue";
import GamePlayPage from "@/pages/game/play/GamePlayPage.vue";
import GameCreatePage from "@/pages/game/create/GameCreatePage.vue";
import AccountSignUpPage from "@/pages/account/AccountSignUpPage.vue";
import AccountSignUpVerificationPage from "@/pages/account/AccountSignUpVerificationPage.vue";
import AccountRecoveryPage from "@/pages/account/AccountRecoveryPage.vue";
import AccountRecoverySubmitPage from "@/pages/account/AccountRecoverySubmitPage.vue";
import AccountSettingsPage from "@/pages/account/settings/AccountSettingsPage.vue";


export const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            name: 'Login',
            path: '/login',
            component: LoginPage,
            meta: {
                title: 'Login',
                requiresAuth: false,
                requiredPermissions: [],
                cssName: 'LoginPage'
            }
        },
        {
            name: 'Account.SignUp',
            path: '/account/signup',
            component: AccountSignUpPage,
            meta: {
                title: 'Sign up',
                requiresAuth: false,
                requiredPermissions: [],
                cssName: 'AccountSignUpPage'
            }
        },
        {
            name: 'Account.SignUp.Verification',
            path: '/account/signup/verification/:verificationToken',
            component: AccountSignUpVerificationPage,
            props: true,
            meta: {
                title: 'Sign up verification',
                requiresAuth: false,
                requiredPermissions: [],
                cssName: 'AccountSignUpVerificationPage'
            }
        },

        {
            name: 'Account.Recovery',
            path: '/account/recovery',
            component: AccountRecoveryPage,
            meta: {
                title: 'Account recovery',
                requiresAuth: false,
                requiredPermissions: [],
                cssName: 'AccountRecoveryPage'
            }
        },
        {
            name: 'Account.Recovery.Submit',
            path: '/account/recovery/submit/:verificationToken',
            component: AccountRecoverySubmitPage,
            props: true,
            meta: {
                title: 'Account recovery verification',
                requiresAuth: false,
                requiredPermissions: [],
                cssName: 'AccountRecoverySubmitPage'
            }
        },


        {
            name: 'Main',
            path: '/main',
            component: MainPage,
            meta: {
                title: 'Main page',
                requiresAuth: true,
                requiredPermissions: [authService.generateAccessPermissionOn('Main')],
            }
        },
        {
            name: 'Account.Settings',
            path: '/account/settings',
            component: AccountSettingsPage,
            meta: {
                title: 'Settings page',
                requiresAuth: true,
                requiredPermissions: [authService.generateAccessPermissionOn('Account.Settings')],
            }
        },

        {
            name: 'Game.Create',
            path: '/game/create',
            component: GameCreatePage,
            meta: {
                title: 'Game create page',
                requiresAuth: true,
                requiredPermissions: [authService.generateAccessPermissionOn('Game.Create')],
                cssName: 'GameCreatePage'
            }
        },
        {
            name: 'Game.Lobby',
            path: '/game/lobby',
            component: GameLobbyPage,
            meta: {
                title: 'Game Lobby page',
                requiresAuth: true,
                requiredPermissions: [authService.generateAccessPermissionOn('Game.Lobby')],
            }
        },
        {
            name: 'Game.Play',
            path: '/game/play',
            component: GamePlayPage,
            meta: {
                title: 'Game Play page',
                requiresAuth: true,
                requiredPermissions: [authService.generateAccessPermissionOn('Game.Play')],
                cssName: 'GamePlayPage'
            }
        },
        {
            path: '/:pathMatch(.*)*',
            redirect: (to) => {
                //
                if (to.fullPath.startsWith('/api')) {
                    console.warn("The path:",to.fullPath," is blocked, because browser tried to navigate into to /api.. scopes!");
                }

                //
                if (!authService.isUserAuthenticated()) {

                    return '/login';
                } else {
                    return '/main';
                }
            }
        }

    ]
});


router.beforeEach(async (to, from, next) => {


    if (to.path === '/') {
        if (!authService.isUserAuthenticated()) {
            next('/login');
            return;
        } else {
            next('/main');
            return;
        }
    }

    if (to.meta.requiresAuth && !authService.isUserAuthenticated() && to.path !== '/login') {
        next('/login');
        return;
    } else if (to.meta.requiresAuth && authService.isUserAuthenticated()) {
        if (to.meta.requiresAuth.requiredPermissions) {
            for (const permission of to.meta.requiresAuth.requiredPermissions) {
                if (!authService.hasCurrentUserPermission(permission)) {
                    await TWarningDialog({
                        title: 'Permission warning',
                        message: 'Sorry! You do not have the necessary permission:' + permission
                    });
                    next('/')
                    return;
                }
            }
        }
        next();
        return;
    } else if (!to.meta.requiresAuth) {
        next();
        return;
    } else {
        next();
        return;
    }
});

export default router;

