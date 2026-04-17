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
import AdminPermissionGroupPage from "@/pages/account/admin/permission/AdminPermissionGroupPage.vue";
import AdminUserPage from "@/pages/account/admin/user/AdminUserPage.vue";
import AdminAccountSettingsPage from "@/pages/account/admin/settings/AdminAccountSettingsPage.vue";
import ChartTestPage from "@/pages/test/ChartTestPage.vue";
import SitePage from "@/pages/site/SitePage.vue";
import GameScenarioPage from "@/pages/game/scenario/GameScenarioPage.vue";

import SiteUserPage from "@/pages/account/site/user/SiteUserPage.vue";
import SitePermissionGroupPage from "@/pages/account/site/permission/SitePermissionGroupPage.vue";


export const router = createRouter({
    history: createWebHistory(),
    routes: [
        {name: 'Login', path: '/login', component: LoginPage, meta: {title:'Login',requiresAuth: false}}, // our-domain.com/teams => TeamsList
        {name: 'Main', path: '/main', component: MainPage, meta: {title:'Main page',requiresAuth: true}},

        {name: 'Account.Admin.User', path: '/account/admin/user', component: AdminUserPage, meta: {title:'Admin Users page',requiresAuth: true}},
        {name: 'Account.Admin.PermissionGroup', path: '/account/admin/permission', component: AdminPermissionGroupPage, meta: {title:'Admin-permission-groups page',requiresAuth: true}},
        {name: 'Account.Admin.Profile', path: '/account/admin/settings', component: AdminAccountSettingsPage, meta: {title:'Profile page',requiresAuth: true}},

        {name: 'Account.Site.User', path: '/account/site/user', component: SiteUserPage, meta: {title:'Site users page',requiresAuth: true}},
        {name: 'Account.Site.PermissionGroup', path: '/account/site/permission', component: SitePermissionGroupPage, meta: {title:'Site-permission-groups page',requiresAuth: true}},


        {name: 'Game.Scenario', path: '/game/scenario', component: GameScenarioPage, meta: {title:'Scenarios page',requiresAuth: true}},

        {name: 'Breed', path: '/breed', component: BreedPage, meta: {title:'Breeds page',requiresAuth: true}},
        {name: 'Site', path: '/site', component: SitePage, meta: {title:'Sites page',requiresAuth: true}},
        {name: 'Dog', path: '/dog', component: DogPage, meta: {title:'Dogs page',requiresAuth: true}},

        {name: 'Test', path: '/test', component: TestPage, meta: {title:'Title',requiresAuth: true}},
        {name: 'Test.Confirm', path: '/test/confirm', component: ConfirmTestPage, meta: {title:'Title',requiresAuth: true}},
        {name: 'Test.Dialog', path: '/test/dialog', component: DialogTestPage, meta: {title:'Title',requiresAuth: true}},
        {name: 'Test.Add', path: '/test/add', component: AddDialogTestPage, meta: {title:'Title',requiresAuth: true}},
        {name: 'Test.Toaster', path: '/test/toaster', component: ToasterTestPage, meta: {title:'Title',requiresAuth: true}},
        {name: 'Test.Snackbar', path: '/test/snackbar', component: SnackbarTestPage, meta: {title:'Title',requiresAuth: true}},
        {name: 'Test.Image.Single', path: '/test/image/single', component: SingleImageUploadTestPage, meta: {title:'Title',requiresAuth: false}},
        {name: 'Test.Image.Multi', path: '/test/image/multi', component: MultiImageUploadTestPage, meta: {title:'Title',requiresAuth: false}},
        {name: 'Test.Image.Croppie', path: '/test/image/croppie', component: CroppieTestPage, meta: {title:'Title',requiresAuth: false}},
        {name: 'Test.Websocket.First', path: '/test/websocket/first', component: WebsocketTestPage, meta: {title:'Title',requiresAuth: false}},
        {name: 'Test.Editor.Quill', path: '/test/editor/quill', component: QuillEditorTestPage, meta: {title:'Title',requiresAuth: false}},
        {name: 'Test.Chart.First', path: '/test/chart/first', component: ChartTestPage, meta: {title:'Title',requiresAuth: false}},
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
                message: 'Sorry! You do not have the necessary permission on "'+to.meta.title+'"!'
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

