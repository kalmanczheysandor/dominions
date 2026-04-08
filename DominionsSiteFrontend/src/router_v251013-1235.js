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
        {name: 'Login', path: '/login', component: LoginPage, meta: {title: 'Login', requiresAuth: false,cssName:'LoginPage'}}, // our-domain.com/teams => TeamsList

        {name: 'Account.SignUp', path: '/account/signup', component: AccountSignUpPage, meta: {title: 'Sign up', requiresAuth: false,cssName:'AccountSignUpPage'}},
        {name: 'Account.SignUp.Verification', path: '/account/signup/verification/:verificationToken', component: AccountSignUpVerificationPage, props:true, meta: {title: 'Sign up verification', requiresAuth: false,cssName:'AccountSignUpVerificationPage'}},

        {name: 'Account.Recovery', path: '/account/recovery', component: AccountRecoveryPage, meta: {title: 'Account recovery', requiresAuth: false,cssName:'AccountRecoveryPage'}},
        {name: 'Account.Recovery.Submit', path: '/account/recovery/submit/:verificationToken', component: AccountRecoverySubmitPage, props:true, meta: {title: 'Account recovery verification', requiresAuth: false,cssName:'AccountRecoverySubmitPage'}},




        {name: 'Main', path: '/main', component: MainPage, meta: {title: 'Main page', requiresAuth: true}},
        {name: 'Account.User', path: '/account/user', component: UserPage, meta: {title: 'Users page', requiresAuth: true}},
        {name: 'Account.PermissionGroup', path: '/account/permission', component: PermissionGroupPage, meta: {title: 'Permission-groups page', requiresAuth: true}},
        {name: 'Account.Settings', path: '/account/settings', component: AccountSettingsPage, meta: {title: 'Settings page', requiresAuth: true}},

        {name: 'Game.Create', path: '/game/create', component: GameCreatePage, meta: {title: 'Game create page', requiresAuth: false,cssName:'GameCreatePage'}},
        {name: 'Game.Lobby', path: '/game/lobby', component: GameLobbyPage, meta: {title: 'Game Lobby page', requiresAuth: false}},
        {name: 'Game.Play', path: '/game/play', component: GamePlayPage, meta: {title: 'Game Play page', requiresAuth: false,cssName:'GamePlayPage'}},


        {name: 'Breed', path: '/breed', component: BreedPage, meta: {title: 'Breeds page', requiresAuth: true}},
        {name: 'Site', path: '/site', component: SitePage, meta: {title: 'Sites page', requiresAuth: true}},
        {name: 'Dog', path: '/dog', component: DogPage, meta: {title: 'Dogs page', requiresAuth: true}},

        {name: 'Test', path: '/test', component: TestPage, meta: {title: 'Title', requiresAuth: true}},
        {name: 'Test.Confirm', path: '/test/confirm', component: ConfirmTestPage, meta: {title: 'Title', requiresAuth: true}},
        {name: 'Test.Dialog', path: '/test/dialog', component: DialogTestPage, meta: {title: 'Title', requiresAuth: true}},
        {name: 'Test.Add', path: '/test/add', component: AddDialogTestPage, meta: {title: 'Title', requiresAuth: true}},
        {name: 'Test.Toaster', path: '/test/toaster', component: ToasterTestPage, meta: {title: 'Title', requiresAuth: true}},
        {name: 'Test.Snackbar', path: '/test/snackbar', component: SnackbarTestPage, meta: {title: 'Title', requiresAuth: true}},
        {name: 'Test.Image.Single', path: '/test/image/single', component: SingleImageUploadTestPage, meta: {title: 'Title', requiresAuth: false}},
        {name: 'Test.Image.Multi', path: '/test/image/multi', component: MultiImageUploadTestPage, meta: {title: 'Title', requiresAuth: false}},
        {name: 'Test.Image.Croppie', path: '/test/image/croppie', component: CroppieTestPage, meta: {title: 'Title', requiresAuth: false}},
        {name: 'Test.Websocket.First', path: '/test/websocket/first', component: WebsocketTestPage, meta: {title: 'Title', requiresAuth: false}},
        {name: 'Test.Editor.Quill', path: '/test/editor/quill', component: QuillEditorTestPage, meta: {title: 'Title', requiresAuth: false}},
        {name: 'Test.Chart.First', path: '/test/chart/first', component: ChartTestPage, meta: {title: 'Title', requiresAuth: false}},
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

    if(authService.isUserAuthenticated()) {
        if(to.path === '/login') {
            next('/main');
            return;
        } else if(to.meta.requiresAuth && !authService.isAccessActionGrantedOn(to.name)) {
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
        if(to.meta.requiresAuth) {
            next('/login');
            return;
        }
        next();
        return;
    }
});

