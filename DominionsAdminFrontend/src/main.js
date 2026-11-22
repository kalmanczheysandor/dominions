
import { createApp } from 'vue';
import '@mdi/font/css/materialdesignicons.css'; // Material Design Icons importálása
import { createVuetify } from 'vuetify'

import {createPinia} from 'pinia';
import piniaPersist from 'pinia-plugin-persistedstate';



import App from './App.vue';

import Toast from 'vue-toastification';
import 'vue-toastification/dist/index.css';


import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import 'vuetify/styles' // Import Vuetify styles

import DropZone from 'dropzone-vue';

// optionally import default styles
import 'dropzone-vue/dist/dropzone-vue.common.css';

import {router} from "@/router.js";
import './assets/croppie.css';
import './assets/style/vuetify.css';


const vuetify = createVuetify({
    components,
    directives, // Ensure directives are also imported
    defaults: {
        VBtn: {
            color: 'primary',
            variant: 'outlined',
            rounded: true,
        },
        VDataTableHeaders: {
            color:'red'
        }
    },

})







const pinia = createPinia();
pinia.use(piniaPersist);
import VueApexCharts from "vue3-apexcharts";


const app = createApp(App);
app.use(pinia);
app.use(vuetify)


app.use(router);

app.use(Toast, {
    duration: 5000,
    maxToasts: 5,
    newestOnTop: true,
    position: 'top-right',
    closeOnClick: true,
    pauseOnHover: true,
    draggable: true,
    draggablePercent: 0.6,
    showCloseButtonOnHover: true,
    hideProgressBar: false,
    closeButton: 'button',
    rtl: false,
    icon: true,
    toastClassName: 'my-toast',
    bodyClassName: 'my-toast-body',
    progressClassName: 'my-toast-progress',
    transition: 'fade',
});

app.use(DropZone);
app.use(VueApexCharts);
app.mount('#app');
