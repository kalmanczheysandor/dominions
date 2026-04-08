<template>

    <div v-if="!isGamePlayMode" class="NavigationMenu" style="">
        <button type="button"
                title="Game"
                class="mdi-gamepad-variant-outline mdi MenuCard"
                @click="eventClickOnPlayMenuButton"
                style="left:0px;"
        ></button>

        <button type="button"
                title="Settings"
                class="mdi-cog-outline mdi MenuCard"
                @click="eventClickOnSettingsMenuButton"
                style="left:70px;"
        ></button>

        <button type="button"
                title="Charts"
                class="mdi-finance mdi MenuCard"
                @click="eventClickOnChartsMenuButton"
                style="left:140px;"
        ></button>

        <button type="button"
                title="Logout"
                class="mdi-exit-to-app mdi MenuCard"
                @click="eventClickOnLogoutMenuButton"
                style="right:0;"
        ></button>
    </div>
    <div v-else-if="isGamePlayMode" class="NavigationMenu" style="">
        <button type="button"
                title="Game"
                class="mdi-gamepad-variant-outline mdi MenuCard"
                @click="eventClickOnResignMenuButton"
                style="left:0px;"
        ></button>
    </div>
</template>
<script>
import TController from "@/framework/TController";
import TConfirmDialog from "@/framework/component/TConfirmDialog/TConfirmDialog";
import authService from "@/services/auth/AuthService";

export default {
    name: "NavigationMenu",
    emits: [
        'whenClickOnPlayMenuButton',
        'whenClickOnSettingsMenuButton',
        'whenClickOnChartsMenuButton',
        'whenClickOnLogoutMenuButton',
        'whenClickOnResignMenuButton',
    ],
    data() {
        return {};
    },
    computed: {
        currentPath() {
            return this.$route.path;
        },
        isGamePlayMode() {
            return this.currentPath.startsWith("/game/play");
        }
    },
    mounted() {

        // console.log('NavigationMenu mounted!');
        // console.log('NavigationMenu mounted!');
    },
    methods: {

        async eventClickOnPlayMenuButton() {
            try {
                this.$router.push('/game/lobby');
                this.$emit('whenClickOnPlayMenuButton');
            } catch (exp) {
                await TController.displayExceptionMessages(exp);
            }
        },
        async eventClickOnSettingsMenuButton() {
            try {
                this.$router.push('/account/settings');
                this.$emit('whenClickOnSettingsMenuButton');
            } catch (exp) {
                await TController.displayExceptionMessages(exp);
            }
        },
        async eventClickOnChartsMenuButton() {
            try {
                this.$router.push('/charts');
                this.$emit('whenClickOnChartsMenuButton');
            } catch (exp) {
                await TController.displayExceptionMessages(exp);
            }
        },
        async eventClickOnLogoutMenuButton() {
            try {
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
                this.$emit('whenClickOnLogoutMenuButton');
            } catch (exp) {
                await TController.displayExceptionMessages(exp);
            }
        },

        async eventClickOnResignMenuButton() {
            try {
                this.$emit('whenClickOnResignMenuButton');
            } catch (exp) {
                await TController.displayExceptionMessages(exp);
            }
        },
    }


};
</script>

<style scoped>
.NavigationMenu {
    position: relative;
    background-color: rgba(0, 0, 0, 0);
    height: 60px;
    width: 100%;
    margin: 0px;
}

.NavigationMenu .MenuCard {
    position: absolute;
    top: 0;
    margin: 0px !important;
    padding: 0;
    width: 60px;
    height: 60px !important;

    background-color: transparent;
    border: 5px rgba(0, 0, 0, 0.1) solid;
    border-radius: 50px;
    backdrop-filter: blur(10px);
    -webkit-backdrop-filter: blur(10px);
    box-shadow: 0px 0px 2px rgba(255, 255, 255, 0.9),
    0px 0px 8px rgba(0, 0, 0, 0.9);


    color: white;
    font-size: 20px;

    display: inline-block;
    box-sizing: border-box;

    transition: all 0.5s ease;
    transform: scale(0.95);
}


.NavigationMenu .MenuCard:hover {
    background-color: transparent;
    border: 5px rgba(255, 255, 255, 0.2) solid;
    font-size: 30px;
    transform: scale(1);
    text-shadow: 0px 0px 2px rgba(0, 0, 0, 0.5),
    0px 0px 4px rgba(0, 0, 0, 0.5),
    0px 0px 5px rgba(255, 255, 255, 0.9),
    0px 0px 10px rgba(255, 255, 255, 0.9);

    box-shadow: 0px 0px 2px rgba(255, 255, 255, 0.9),
    0px 0px 20px rgba(0, 0, 0, 0.9);
}

</style>