<template>

    <v-app-bar color="primary" app dense v-if="isUserAuthenticated">
        <v-app-bar-title>
            <div style="display: flex;align-items: center;justify-content: start;">
                <i class="mdi-shield-crown-outline mdi" style="font-size: 50px;padding:10px;"/>
                {{ $route.meta.title }}
            </div>
        </v-app-bar-title>

        <v-spacer></v-spacer>

        <v-btn text color="white" v-for="(menuItemLevel1, menuIndexLevel1) in MainMenuComponent.menuItems"
               :key="menuIndexLevel1" :to="menuItemLevel1.route ? menuItemLevel1.route : undefined">
            <template v-slot:prepend v-if="menuItemLevel1.icon">
                <v-icon :icon="menuItemLevel1.icon" size="x-small"></v-icon>
            </template>
            {{ menuItemLevel1.title }}
            <v-menu activator="parent" v-if="menuItemLevel1.subItems" compact>
                <v-list>
                    <v-list-item link v-for="(menuItemLevel2, menuIndexLevel2) in menuItemLevel1.subItems"
                                 :key="menuIndexLevel2" :to="menuItemLevel2.route ? menuItemLevel2.route : undefined">
                        <template v-slot:prepend v-if="menuItemLevel2.icon">
                            <v-icon :icon="menuItemLevel2.icon" size="x-small"></v-icon>
                        </template>
                        <v-list-item-title>{{ menuItemLevel2.title }}</v-list-item-title>
                        <template v-slot:append v-if="menuItemLevel2.subItems">
                            <v-icon icon="mdi-menu-right" size="x-small"></v-icon>
                        </template>

                        <v-menu :open-on-focus="false" activator="parent" open-on-hover submenu
                                v-if="menuItemLevel2.subItems">
                            <v-list>
                                <v-list-item link v-for="(menuItemLevel3, menuIndexLevel3) in menuItemLevel2.subItems"
                                             :key="menuIndexLevel3"
                                             :to="menuItemLevel3.route ? menuItemLevel3.route : undefined">
                                    <template v-slot:prepend v-if="menuItemLevel3.icon">
                                        <v-icon :icon="menuItemLevel3.icon" size="x-small"></v-icon>
                                    </template>
                                    <v-list-item-title>{{ menuItemLevel3.title }}</v-list-item-title>
                                </v-list-item>
                            </v-list>
                        </v-menu>
                    </v-list-item>
                </v-list>
            </v-menu>
        </v-btn>

        <v-spacer></v-spacer>

        <v-btn icon>
            <v-icon>mdi-bell</v-icon>
        </v-btn>
        <v-btn icon>
            <v-icon>mdi-account</v-icon>
        </v-btn>

        <v-menu location="end">
            <template v-slot:activator="{props}">
                <v-btn icon="mdi-dots-vertical" color="white" variant="text" v-bind="props"></v-btn>
            </template>
            <v-list density="compact">
                <v-list-item :prepend-avatar="profileData.profileAvatarUrl"
                             :title="profileData.title"
                >
                </v-list-item>
                <v-list-item prepend-icon="mdi-cog" title="Settings" @click="eventClickedOnSettingsMenu()"/>
                <v-list-item prepend-icon="mdi-logout" title="Logout" @click="eventClickedOnLogoutMenu()"/>
            </v-list>
        </v-menu>


    </v-app-bar>

</template>

<script>
import {authService} from "@/services/auth/AuthService";
import TConfirmDialog from "@/framework/component/TConfirmDialog/TConfirmDialog";
import NetworkFailureException from "@/framework/exception/failure/NetworkFailureException";
import adminAccountSettingsService from "@/services/account/admin/settings/AdminAccountSettingsService";
import {useAuthStore} from "@/stores/AuthStore";
import TController from "@/framework/TController";

export default {
    data() {
        return {
            authStore: useAuthStore(),
            profileData: {
                title: '???',
                subtitle: '????',
                profileAvatarUrl: '',
            },
            MainMenuComponent: {
                usersMenuOpen: false,  // Az "Users" menü állapota
                settingsMenuOpen: false,  // A "Settings" menü állapota
                menuItems: [
                    {
                        title: 'Ai',
                        icon: 'mdi-view-dashboard',
                        subItems: [
                            {
                                title: 'Hugo engine',
                                icon: 'mdi-account-outline',
                                subItems: [
                                    {
                                        title: 'Character',
                                        icon: 'mdi-account-group',
                                        route: '/ai/hugo/character'
                                    },
                                    {
                                        title: 'Variant',
                                        icon: 'mdi-account-group',
                                        route: '/ai/hugo/variant'
                                    }
                                ]
                            },
                            {
                                title: 'Liz Engine',
                                icon: 'mdi-account-outline',
                                subItems: [
                                    {
                                        title: 'Character',
                                        icon: 'mdi-account-group',
                                        route: '/ai/liz/character'
                                    },
                                    {
                                        title: 'Variant',
                                        icon: 'mdi-account-group',
                                        route: '/ai/liz/variant'
                                    },
                                    {
                                        title: 'Concept',
                                        icon: 'mdi-account-group',
                                        route: '/ai/liz/concept'
                                    }
                                ]
                            },



                        ]
                    },


                    {
                        title: 'Game',
                        icon: 'mdi-view-dashboard',
                        subItems: [
                            {title: 'Scenarios', icon: 'mdi-account-group', route: '/game/scenario'},

                        ]
                    },



                    {
                        title: 'Accounts',
                        icon: 'mdi-view-dashboard',
                        subItems: [
                            {
                                title: 'Site',
                                icon: 'mdi-account-outline',
                                subItems: [
                                    {
                                        title: 'Users',
                                        icon: 'mdi-account-group',
                                        route: '/account/site/user'
                                    },
                                    {
                                        title: 'Permissions',
                                        icon: 'mdi-view-dashboard',
                                        route: '/account/site/permission'
                                    }
                                ]
                            },
                            {
                                title: 'Admin',
                                icon: 'mdi-account-hard-hat-outline',
                                subItems: [
                                    {
                                        title: 'Users',
                                        icon: 'mdi-account-group',
                                        route: '/account/admin/user'
                                    },
                                    {
                                        title: 'Permissions',
                                        icon: 'mdi-view-dashboard',
                                        route: '/account/admin/permission'
                                    }
                                ]
                            },


                        ]
                    },
                    // {
                    //     title: 'Enrolment',
                    //     icon: 'mdi-view-dashboard',
                    //     subItems: [
                    //         {title: 'Clients', icon: 'mdi-account-group', route: '/enrollment/client'},
                    //         {title: 'Permissions', icon: 'mdi-view-dashboard', route: '/enrollment/permission'}
                    //     ]
                    // },
                    // {
                    //     title: 'Settings',
                    //     icon: 'mdi-cog',
                    //     subItems: [
                    //         {title: 'General', route: 'general-settings'},
                    //         {title: 'Security', route: 'security-settings'}
                    //     ]
                    // }
                ]
            }
        };
    },
    computed: {
        isUserAuthenticated() {
            return authService.isUserAuthenticated();
        }
    },
    methods: {

        async eventClickedOnLogoutMenu() {
            try {
                // Confirming
                const confirmed = await TConfirmDialog('Do you wish to logout?');
                if (!confirmed.ok) {
                    return;
                }
                await authService.logout();
                this.$router.push('/');
            } catch (exp) {
                await TController.handleExceptions(exp);
            }
        },


        async eventClickedOnSettingsMenu() {
            try {
                this.$router.push('/account/admin/settings');
            } catch (exp) {
                await TController.handleExceptions(exp);
            }
        },

        async init() {
            try {
                //Retrieve: record(s)
                console.log("MainMenu.Init:Q1");
                const detailsResult = await adminAccountSettingsService.accessProfileDetails();
                console.log("MainMenu.Init:Q2");
                let avatarImageUrl = await adminAccountSettingsService.getProfilePhotoUrlIfExists();
                if (avatarImageUrl == null) {
                    avatarImageUrl = '/image/no_avatar2.png';
                }
                console.log("MainMenu.Init:Q3");
                this.profileData = {
                    title: detailsResult.data.name,
                    profileAvatarUrl: avatarImageUrl
                }
            } catch (exp) {
                await TController.handleExceptions(exp);
            }
        }
    },
    watch: {
        'authStore.isAuthenticated'(newVal) {
            if (newVal) {
                this.init();
            }
        }
    },
    mounted() {
        if (this.authStore.isAuthenticated) {
            this.init();
        }
    },

};
</script>

