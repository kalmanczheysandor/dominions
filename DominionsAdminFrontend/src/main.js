
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
    // theme: {
    //     themes: {
    //         light: {
    //             colors: {
    //                 primary: '#1976D2',
    //                 secondary: '#424242',
    //                 accent: '#8C9EFF',
    //                 error: '#F44336',
    //                 info: '#2196F3',
    //                 success: '#4CAF50',
    //                 warning: '#FF9800',
    //             },
    //         },
    //     },
    // }
})







const pinia = createPinia();
// pinia.use(createPersistedState );
pinia.use(piniaPersist);

// Pinia persist plugin beállítása
// pinia.use(({ store }) => {
//     store.$persistedState = store.$state;
// });

//useAuthStore(pinia);

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
