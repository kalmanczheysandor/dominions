<template>
    <v-app>
        <MainMenu/>
        <v-main class="d-flex flex-column fill-height">
            <v-container fluid class="flex-grow-1" style="background-color: #f2f2f2">
                <v-row>
                    <v-col cols="12">
                        <router-view/>
                    </v-col>
                </v-row>
            </v-container>
        </v-main>
        <v-footer class="bg-grey-lighten-2 text-center d-flex flex-column">
            <div>
                <v-btn v-for="icon in icons" :key="icon" :icon="icon" class="mx-4" variant="text"></v-btn>
            </div>

            <div class="pt-0">
                Phasellus feugiat arcu sapien, et iaculis ipsum elementum sit amet. Mauris cursus commodo interdum.
                Praesent ut risus eget metus luctus accumsan id ultrices nunc. Sed at orci sed massa consectetur
                dignissim a sit amet dui. Duis commodo vitae velit et faucibus. Morbi vehicula lacinia malesuada. Nulla
            </div>
            <v-divider></v-divider>

            <div>
                {{new Date().getFullYear()}} — <strong>Vuetify</strong>
            </div>
        </v-footer>
    </v-app>
</template>


<script>
    import TConfirmDialog from '@/framework/TConfirmDialog.vue';
    import MessageDialog from '@/framework/TMessageDialog.vue';
    import MainMenu from "@/components/MainMenu.vue";

    export default {
        name: 'App',
        components: {
            MainMenu
        },

        methods: {
            // showConfirmDialog(okMethod = null, cancelMethod = null, title = null, message = null) {
            //     this.$refs.ConfirmDialog.showDialog(okMethod, cancelMethod, title, message);
            // },
            // showInfoDialog(okMethod = null, title = null, message = null) {
            //     this.$refs.InfoDialog.open(okMethod, title, message);
            // },
            // showSuccessDialog(okMethod = null, title = null, message = null) {
            //     this.$refs.SuccessDialog.open(okMethod, title, message);
            // },
            // showWarnDialog(okMethod = null, title = null, message = null) {
            //     this.$refs.WarnDialog.open(okMethod, title, message);
            // },
            // showErrorDialog(okMethod = null, title = null, message = null) {
            //     this.$refs.ErrorDialog.open(okMethod, title, message);
            // }
        }
    };
</script>

<style>
    html, body, #app {
        height: 100%;
        margin: 0;
    }

    .v-data-table-header__content span {
        font-weight: bold;
    }
</style>