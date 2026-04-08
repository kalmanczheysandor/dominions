<template>

    <h3 class="SectionTitle">
        Main menu
    </h3>
    <v-row align="center" justify="center" class="fill-height">
        <v-col cols="2" class="justify-center">
            <button type="button"
                    title="Game"
                    class="mdi-gamepad-variant-outline mdi MenuCard"
                    @click="eventClickOnPlayMenuButton"
            />
        </v-col>
        <v-col cols="2" class="justify-center">
            <button type="button"
                    title="Settings"
                    class="mdi-cog-outline mdi MenuCard"
                    @click="eventClickOnSettingsMenuButton"
            />
        </v-col>
        <v-col cols="2" class="justify-center">
            <button type="button"
                    title="Charts"
                    class="mdi-finance mdi MenuCard"
                    @click="eventClickOnChartsMenuButton"
            />
        </v-col>
        <v-col cols="2" class="justify-center">
            <button type="button"
                    title="Logout"
                    class="mdi-exit-to-app mdi MenuCard"
                    @click="eventClickOnLogoutMenuButton"
            />
        </v-col>

    </v-row>
</template>

<script>

import TController from "@/framework/TController";
import TConfirmDialog from "@/framework/component/TConfirmDialog/TConfirmDialog";
import authService from "@/services/auth/AuthService";

export default {
    components: {},
    data() {
        return {};
    },
    methods: {
        async eventClickOnPlayMenuButton() {
            try {
                this.$router.push('/game/lobby');
            } catch (exp) {
                await TController.displayExceptionMessages(exp);
            }
        },
        async eventClickOnSettingsMenuButton() {
            try {
                this.$router.push('/account/settings');
            } catch (exp) {
                await TController.displayExceptionMessages(exp);
            }
        },
        async eventClickOnChartsMenuButton() {
            try {
                this.$router.push('/charts');
            } catch (exp) {
                await TController.displayExceptionMessages(exp);
            }
        },
        async eventClickOnLogoutMenuButton() {
            try {
                // Confirming
                const confirmed = await TConfirmDialog('Do you wish to logout?');
                if (!confirmed.ok) {
                    return;
                }
                await authService.logout();
                this.$router.push('/');
            } catch (exp) {
                await TController.displayExceptionMessages(exp);
            }
        },


        // async eventOnClickProfileMenuButton() {
        //     try {
        //         this.$router.push('/main');
        //     } catch(exp) {
        //         await TController.displayExceptionMessages(exp);
        //     }
        // },
        // async eventOnClickLogoutMenuButton() {
        //     // Confirming
        //     const confirmed = await TConfirmDialog('Do you wish to logout?');
        //     if(!confirmed.ok) {
        //         return;
        //     }
        //
        //     await authService.logout();
        //     this.$router.push('/login');
        // },

    },
};
</script>

<style scoped>


.MenuCard {
    position: relative;
    margin: 0px !important;
    padding: 0;
    width: 100%;
    height: 250px !important;

    background-color: rgba(0, 0, 0, 0.5);
    border: 10px rgba(0, 0, 0, 0.1) solid;
    border-radius: 5px;
    box-shadow: 0px 0px 2px rgba(255, 255, 255, 0.9),
    0px 0px 8px rgba(0, 0, 0, 0.9);


    color: white;
    font-size: 80px;

    display: inline-block;
    box-sizing: border-box;

    transition: all 0.5s ease;
    transform: scale(0.95);
}


.MenuCard:hover {
    background-color: rgba(0, 0, 0, 0.6);
    border: 10px rgba(255, 255, 255, 0.2) solid;
    font-size: 100px;
    transform: scale(1);
    text-shadow: 0px 0px 2px rgba(0, 0, 0, 0.5),
    0px 0px 4px rgba(0, 0, 0, 0.5),
    0px 0px 5px rgba(255, 255, 255, 0.9),
    0px 0px 10px rgba(255, 255, 255, 0.9);

    box-shadow: 0px 0px 2px rgba(255, 255, 255, 0.9),
    0px 0px 20px rgba(0, 0, 0, 0.9);
}


#MenuPanel {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);

    background-color: rgba(255, 255, 255, 0.2);
    backdrop-filter: blur(10px);
    -webkit-backdrop-filter: blur(5px);
    box-shadow: 5px 5px 15px rgba(0, 0, 0, 0.4);

}

#MenuPanel #ReserveIcon {

    border: 2px black solid;
    font-size: 70px;
    border-radius: 500px;
    height: 100px;
    width: 100px;
    display: block;
    transform: scale(0.95);
    transition: all 0.3s ease;
}

#MenuPanel #ReserveIcon:hover {
    cursor: pointer;
    text-shadow: 0 0 5px rgba(255, 255, 255, 1), 0 0 10px rgba(255, 255, 255, 0.9);
    box-shadow: 0 0 5px rgba(255, 255, 255, 1), 0 0 20px rgba(255, 255, 255, 0.9);
    transform: scale(1);
}

#MenuPanel #ReserveIcon:disabled {
    opacity: 0.2;
    cursor: not-allowed;
}


</style>
