<template>
    <p>hello</p>
    <button @click="eventClickOnTestButton"> Test 1</button>
    <p>hello</p>
    <button @click="eventClickOnTestButton2"> Test 2</button>

</template>

<script>

    import TConfirmDialog from "@/framework/component/TConfirmDialog/TConfirmDialog";
    import TController from "@/framework/TController";
    import testService from "@/services/test/TestService";

    export default {
        components: {},
        data() {
            return {
                usersMenuOpen: false,  // Az "Users" menü állapota
                settingsMenuOpen: false,  // A "Settings" menü állapota
                menuItems: [
                    {
                        title: 'Dashboard',
                        icon: 'mdi-view-dashboard',
                        subItems: [
                            {title: 'Overview', route: 'overview'},
                            {title: 'Stats', route: 'stats'}
                        ]
                    },
                    {
                        title: 'Users',
                        icon: 'mdi-account-group',
                        subItems: [
                            {title: 'Manage Users', route: 'manage-user'},
                            {title: 'Add User', route: 'add-user'}
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
            };
        },
        methods: {
            async eventClickOnTestButton() {
                try {

                    // Save form value
                    await testService.accessByUuid("45");
                    alert("done");
                    // Display success message
                    TController.displayModifiedToast();

                } catch(exp) {
                    await TController.displayExceptionMessages(exp);
                }
            },
            async eventClickOnTestButton2() {
                try {

                    // Save form value
                    await testService.accessByUuid2("76");
                    alert("done2");
                    // Display success message
                    TController.displayModifiedToast();

                } catch(exp) {
                    await TController.displayExceptionMessages(exp);
                }
            },
            navigate(route) {
                console.log(`Navigating to: ${route}`);
                // Itt például Vue Router logikát adhatsz hozzá:
                // this.$router.push(route);
            },
        },
    };
</script>

<style scoped>
    /* Egyedi stílusok, ha szükséges */
</style>
