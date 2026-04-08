import {createRouter, createWebHistory} from "vue-router";

import LoginPage from "@/pages/login/LoginPage.vue";
import MainPage from "@/pages/MainPage.vue";
import BreedPage from "@/pages/breed/BreedPage.vue";
import DogPage from "@/pages/dog/DogPage.vue";
import TestPage from "@/pages/TestPage.vue";
import ConfirmTestPage from "@/pages/test/ConfirmTestPage.vue";
import DialogTestPage from "@/pages/test/DialogTestPage.vue";
import AddDialogTestPage from "@/pages/test/AddDialogTestPage.vue";
import ToasterTestPage from "@/pages/test/ToasterTestPage.vue";
import SnackbarTestPage from "@/pages/test/SnackbarTestPage.vue";

import {authService} from "@/services/auth/AuthService";
import TWarningDialog from "@/framework/component/TWarningDialog/TWarningDialog";
import SingleImageUploadTestPage from "@/pages/test/SingleImageUploadTestPage.vue";
import MultiImageUploadTestPage from "@/pages/test/MultiImageUploadTestPage.vue";
import CroppieTestPage from "@/pages/test/CroppieTestPage.vue";
import WebsocketTestPage from "@/pages/test/WebsocketTestPage.vue";
import QuillEditorTestPage from "@/pages/test/QuillEditorTestPage.vue";
import PermissionGroupPage from "@/pages/account/permission/PermissionGroupPage.vue";
import UserPage from "@/pages/account/user/UserPage.vue";
import ProfilePage from "@/pages/account/profile/ProfilePage.vue";
import ChartTestPage from "@/pages/test/ChartTestPage.vue";
import SitePage from "@/pages/site/SitePage.vue";

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

    ]
});

//
// router.beforeEach(async (to, from, next) => {
// alert('Before each');
//     if(authService.isUserAuthenticated()) {
//         alert('A1');
//         if(to.path=='/login') {
//             next('/main');
//             return;
//         }
//         else if(!authService.isAccessActionGrantedOn(to.name)) {
//             await TWarningDialog({
//                 title: 'Permission warning',
//                 message: 'Sorry! You do not have the necessary permission!'
//             });
//             next(from.path);
//             return;
//         }
//
//         next();
//         return;
//     } else { // When user is not authenticated
//         alert('B1');
//         if(to.meta.requiresAuth) {
//             next('/login');
//             return;
//         }
//         next();
//         return;
//     }
// });

router.beforeEach(async (to, from, next) => {
    //const authStore = useAuthStore();
    // console.log("AuthStore State:", authStore);

    if (authService.isUserAuthenticated()) {
        if (to.path === '/login') {
            next('/main');
            return;
        } else if (to.meta.requiresAuth && !authService.isAccessActionGrantedOn(to.name)) {
            await TWarningDialog({
                title: 'Permission warning',
                message: 'Sorry! You do not have the necessary permission on "' + to.meta.title + '"!'
            });
            // if(from.path==to.path) {
            //     next('/main');
            // }
            // else {
            //     next(from.path);
            // }

            next('/main')
            return;
        }
        next();
        return;
    } else {
        if (to.meta.requiresAuth) {
            next('/login');
            return;
        }
        next();
        return;
    }
});

