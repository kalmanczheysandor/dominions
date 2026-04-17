<template>
    <v-app>

        <v-main class="d-flex flex-column fill-height " style="margin:0px;padding:0px;">
            <v-container fluid :class="['flex-grow-1','GeneralPageStyle',currentCssClass]" style="">
                <MainMenu/>
                <v-container class="PageContainer">
                    <router-view/>
                </v-container>
                <footer id="Footer" class="">
                    <h1 class="LogoBox">
                        <i class="mdi-shield-crown-outline mdi"/>
                        DOMINIONS
                    </h1>
                    <div class="InfoBox">
                        <p>
                            Developed at the <strong>University of Debrecen, Faculty of Informatics</strong>, as part of
                            a BSc
                            thesis by <strong>Sándor Kálmánchey</strong>, <br>under the academic supervision of <strong>Tamás
                            Kádek</strong>.
                        </p>
                        <p>
                            © 2025 – Version 1.0
                        </p>
                        <p>
                            All rights reserved.
                        </p>


                    </div>
                </footer>
            </v-container>
        </v-main>

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
    computed: {
        currentCssClass() {
            // return this.$route.meta.cssName || 'DefaultPageStyle'
            return 'DefaultPageStyle'
        }
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


#Footer {
    position: relative;
    background-color: rgba(0, 0, 0, 0.5);
    margin: 150px 0px 0px 0px;
    width: 100%;
    height: auto;
    padding: 5px;

    display: flex;
    align-items: center;
    justify-content: start;


    backdrop-filter: blur(0px);
    -webkit-backdrop-filter: blur(0px);
    box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.4), 0px 0px 10px rgba(0, 0, 0, 0.4), 0px 0px 15px rgba(0, 0, 0, 0.4);

    transition: all 0.5s ease;
}

#Footer:hover {
    backdrop-filter: blur(5px);
    -webkit-backdrop-filter: blur(5px);
}

#Footer .LogoBox {
    display: flex;
    align-items: center;
    justify-content: start;
    color: rgba(255, 255, 255, 0.2);
    text-align: center;
    font-size: 30px;
    border-right: 2px rgba(255, 255, 255, 0.2) solid;
    padding: 5px;

}

#Footer .LogoBox i {
    font-size: 50px !important;
    font-weight: bold;
}

#Footer .InfoBox {
    padding: 5px;
    color: rgba(255, 255, 255, 0.2);
    font-size: 14px;
    text-align: center;
    border: 0px red solid;
    width: 100%;
}
.PageContainer {
    margin-top:70px;
    min-height: 100%;
}
</style>