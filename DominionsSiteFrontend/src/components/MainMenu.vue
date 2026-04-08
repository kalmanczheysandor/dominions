<template>

    <v-app-bar color="primary" app dense v-if="isUserAuthenticated">
        <v-app-bar-title>SITE: {{$route.meta.title}}</v-app-bar-title>
        <v-spacer></v-spacer>

        <v-btn text color="white" v-for="(menuItemLevel1, menuIndexLevel1) in MainMenuComponent.menuItems" :key="menuIndexLevel1" :to="menuItemLevel1.route ? menuItemLevel1.route : undefined">
            <template v-slot:prepend v-if="menuItemLevel1.icon">
                <v-icon :icon="menuItemLevel1.icon" size="x-small"></v-icon>
            </template>
            {{menuItemLevel1.title}}
            <v-menu activator="parent" v-if="menuItemLevel1.subItems" compact>
                <v-list>
                    <v-list-item link v-for="(menuItemLevel2, menuIndexLevel2) in menuItemLevel1.subItems" :key="menuIndexLevel2" :to="menuItemLevel2.route ? menuItemLevel2.route : undefined">
                        <template v-slot:prepend v-if="menuItemLevel2.icon">
                            <v-icon :icon="menuItemLevel2.icon" size="x-small"></v-icon>
                        </template>
                        <v-list-item-title>{{menuItemLevel2.title}}</v-list-item-title>
                        <template v-slot:append v-if="menuItemLevel2.subItems">
                            <v-icon icon="mdi-menu-right" size="x-small"></v-icon>
                        </template>

                        <v-menu :open-on-focus="false" activator="parent" open-on-hover submenu v-if="menuItemLevel2.subItems">
                            <v-list>
                                <v-list-item link v-for="(menuItemLevel3, menuIndexLevel3) in menuItemLevel2.subItems" :key="menuIndexLevel3" :to="menuItemLevel3.route ? menuItemLevel3.route : undefined">
                                    <template v-slot:prepend v-if="menuItemLevel3.icon">
                                        <v-icon :icon="menuItemLevel3.icon" size="x-small"></v-icon>
                                    </template>
                                    <v-list-item-title>{{menuItemLevel3.title}}</v-list-item-title>
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

        <v-menu c location="end">
            <template v-slot:activator="{props}">
                <v-btn icon="mdi-dots-vertical" color="white" variant="text" v-bind="props" @click="eventClickedOnTripleDot"></v-btn>
            </template>
            <v-list density="compact">
                <v-list-item :prepend-avatar="profileData.profileAvatarUrl" :title="profileData.title" :subtitle="profileData.subtitle"></v-list-item>



                <v-list-item prepend-icon="mdi-cog" title="Profile" @click="eventClickedOnProfileMenu()"/>
                <v-list-item prepend-icon="mdi-logout" title="Logout" @click="eventClickedOnLogoutMenu()"/>
            </v-list>
        </v-menu>


    </v-app-bar>

</template>

<script>
    import {authService} from "@/services/auth/AuthService";
    import TConfirmDialog from "@/framework/component/TConfirmDialog/TConfirmDialog";
    import NetworkFailureException from "@/framework/exception/failure/NetworkFailureException";
    import accountSettingsService from "@/services/account/settings/AccountSettingsService";

    export default {
        data() {
            return {
                profileData: {
                    title:'???',
                    subtitle:'????',
                    profileAvatarUrl: '',
                },
                MainMenuComponent: {
                    usersMenuOpen: false,  // Az "Users" menü állapota
                    settingsMenuOpen: false,  // A "Settings" menü állapota
                    menuItems: [
                        {
                            title: 'Breeds',
                            icon: 'mdi-view-dashboard',
                            route: '/breed'
                        },
                        {
                            title: 'Dogs',
                            icon: 'mdi-view-dashboard',
                            route: '/dog'
                        },
                        {
                            title: 'Sites',
                            icon: 'mdi-view-dashboard',
                            route: '/site'
                        },

                        {
                            title: 'Access',
                            icon: 'mdi-view-dashboard',
                            subItems: [
                                {title: 'Users', icon: 'mdi-account-group', route: '/account/user'},
                                {title: 'Permissions', icon: 'mdi-view-dashboard', route: '/account/permission'}
                            ]
                        },
                        {
                            title: 'Settings',
                            icon: 'mdi-cog',
                            subItems: [
                                {title: 'General', route: 'general-settings'},
                                {title: 'Security', route: 'security-settings'}
                            ]
                        }
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
                // Confirming
                const confirmed = await TConfirmDialog('Do you wish to logout?');
                if(!confirmed.ok) {
                    return;
                }

                await authService.logout();
                this.$router.push('/login');
            },


            async eventClickedOnProfileMenu() {
                this.$router.push('/account/settings');
            },

            async eventClickedOnTripleDot() {

                // Retrieve: record(s)
                const detailsResult = await accountSettingsService.accessDetails();

                let avatarImageUrl = await accountSettingsService.getProfilePhotoUrlIfExists();
                if(avatarImageUrl == null) {
                    avatarImageUrl = '/image/no_avatar2.png';
                }

                this.profileData = {
                    title: detailsResult.data.name,
                    subtitle: 'User',
                    profileAvatarUrl:avatarImageUrl
                }
            }
        },


    };
</script>

<style scoped>
    /* Egyedi stílusok, ha szükséges */
</style>