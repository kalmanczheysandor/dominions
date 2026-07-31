import {createRouter, createWebHistory} from "vue-router";

import LoginPage from "@/pages/login/LoginPage.vue";
import MainPage from "@/pages/MainPage.vue";


import {authService} from "@/services/auth/AuthService";
import TWarningDialog from "@/framework/component/TWarningDialog/TWarningDialog";

import AdminPermissionGroupPage from "@/pages/account/admin/permission/AdminPermissionGroupPage.vue";
import AdminUserPage from "@/pages/account/admin/user/AdminUserPage.vue";
import AdminAccountSettingsPage from "@/pages/account/admin/settings/AdminAccountSettingsPage.vue";

import GameScenarioPage from "@/pages/game/scenario/GameScenarioPage.vue";

import SiteUserPage from "@/pages/account/site/user/SiteUserPage.vue";
import SitePermissionGroupPage from "@/pages/account/site/permission/SitePermissionGroupPage.vue";

import LizCharacterPage from "@/pages/ai/liz/character/LizCharacterPage.vue";
import LizVariantPage from "@/pages/ai/liz/variant/LizVariantPage.vue";
import LizNeuralConceptPage from "@/pages/ai/liz/concept/LizNeuralConceptPage.vue";
import HugoVariantPage from "@/pages/ai/hugo/variant/HugoVariantPage.vue";
import HugoCharacterPage from "@/pages/ai/hugo/character/HugoCharacterPage.vue";


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
                cssName: 'DefaultPageStyle'
            }
        },
        {
            name: 'Main',
            path: '/main',
            component: MainPage,
            meta: {
                title: 'Main',
                requiresAuth: true,
                requiredPermissions: [authService.generateAccessPermissionOn('Main')],
                cssName: 'DefaultPageStyle'
            }
        },

        {
            name: 'Account.Admin.User',
            path: '/account/admin/user',
            component: AdminUserPage,
            meta: {
                title: 'Admin users',
                requiresAuth: true,
                requiredPermissions: [authService.generateAccessPermissionOn('Account.Admin.User')],
                cssName: 'DefaultPageStyle'
            }
        },
        {
            name: 'Account.Admin.PermissionGroup',
            path: '/account/admin/permission',
            component: AdminPermissionGroupPage,
            meta: {
                title: 'Admin permission-groups',
                requiresAuth: true,
                requiredPermissions: [authService.generateAccessPermissionOn('Account.Admin.PermissionGroup')],
                cssName: 'DefaultPageStyle'
            }
        },
        {
            name: 'Account.Admin.Settings',
            path: '/account/admin/settings',
            component: AdminAccountSettingsPage,
            meta: {
                title: 'Settings',
                requiresAuth: true,
                requiredPermissions: [authService.generateAccessPermissionOn('Account.Admin.Settings')],
                cssName: 'DefaultPageStyle'
            }
        },

        {
            name: 'Account.Site.User',
            path: '/account/site/user',
            component: SiteUserPage,
            meta: {
                title: 'Site users',
                requiresAuth: true,
                requiredPermissions: [authService.generateAccessPermissionOn('Account.Site.User')],
                cssName: 'DefaultPageStyle'
            }
        },
        {
            name: 'Account.Site.PermissionGroup',
            path: '/account/site/permission',
            component: SitePermissionGroupPage,
            meta: {
                title: 'Site permission-groups',
                requiresAuth: true,
                requiredPermissions: [authService.generateAccessPermissionOn('Account.Site.PermissionGroup')],
                cssName: 'DefaultPageStyle'
            }
        },
        {
            name: 'Game.Scenario',
            path: '/game/scenario',
            component: GameScenarioPage,
            meta: {
                title: 'Scenarios',
                requiresAuth: true,
                requiredPermissions: [authService.generateAccessPermissionOn('Game.Scenario')],
                cssName: 'DefaultPageStyle'
            }
        },
        // {
        //     name: 'Ai.Liz.Solution',
        //     path: '/ai/liz/ss',
        //     component: LizSolutionPage,
        //     meta: {
        //         title: 'Liz Solutions',
        //         requiresAuth: true,
        //         requiredPermissions: [authService.generateAccessPermissionOn('Ai.Liz.Solution')],
        //         cssName: 'DefaultPageStyle'
        //     }
        // },


        // {
        //     name: 'Ai.Liz.Personnel.Solution.Training',
        //     path: '/ai/liz/personnel/0/solution/:solutionUuid/training',
        //     component: LizPersonnelSolutionTrainingPage,
        //     props: true,
        //     meta: {
        //         title: 'Liz - Training',
        //         requiresAuth: true,
        //         requiredPermissions: [authService.generateAccessPermissionOn('Ai.Liz.Personnel.Solution')],
        //         cssName: 'DefaultPageStyle'
        //     }
        // },

        // {
        //     name: 'Ai.Liz.Personnel.Solution',
        //     path: '/ai/liz/personnel/:personnelUuid/solution',
        //     component: LizPersonnelSolutionPage,
        //     props: true,
        //     meta: {
        //         title: 'Liz - Solution',
        //         requiresAuth: true,
        //         requiredPermissions: [authService.generateAccessPermissionOn('Ai.Liz.Personnel.Solution')],
        //         cssName: 'DefaultPageStyle'
        //     }
        // },


        {
            name: 'Ai.Hugo.Character',
            path: '/ai/hugo/character',
            component: HugoCharacterPage,
            meta: {
                title: 'Hugo-Characters',
                requiresAuth: true,
                requiredPermissions: [authService.generateAccessPermissionOn('Ai.Hugo.Character')],
                cssName: 'DefaultPageStyle'
            }
        },
        {
            name: 'Ai.Hugo.Variant',
            path: '/ai/hugo/variant',
            component: HugoVariantPage,
            meta: {
                title: 'Hugo-Variants',
                requiresAuth: true,
                requiredPermissions: [authService.generateAccessPermissionOn('Ai.Hugo.Variant')],
                cssName: 'DefaultPageStyle'
            }
        },


        {
            name: 'Ai.Liz.Character',
            path: '/ai/liz/character',
            component: LizCharacterPage,
            meta: {
                title: 'Liz-Characters',
                requiresAuth: true,
                requiredPermissions: [authService.generateAccessPermissionOn('Ai.Liz.Character')],
                cssName: 'DefaultPageStyle'
            }
        },
        {
            name: 'Ai.Liz.Variant',
            path: '/ai/liz/variant',
            component: LizVariantPage,
            meta: {
                title: 'Liz-Variants',
                requiresAuth: true,
                requiredPermissions: [authService.generateAccessPermissionOn('Ai.Liz.Variant')],
                cssName: 'DefaultPageStyle'
            }
        },
        {
            name: 'Ai.Liz.Concept',
            path: '/ai/liz/concept',
            component: LizNeuralConceptPage,
            meta: {
                title: 'Liz-Concepts',
                requiresAuth: true,
                requiredPermissions: [authService.generateAccessPermissionOn('Ai.Liz.Concept')],
                cssName: 'DefaultPageStyle'
            }
        },
        {
            path: '/:pathMatch(.*)*',
            redirect: (to) => {
                console.log("REDIRECT from:",to.fullPath)

                //
                if (to.fullPath.startsWith('/api')) {
                    console.warn("The path:", to.fullPath, " is blocked, because browser tried to navigate into to /api.. scopes!");
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