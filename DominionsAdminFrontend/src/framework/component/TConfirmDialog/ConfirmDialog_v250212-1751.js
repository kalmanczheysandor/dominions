import { createApp } from 'vue'
import { createVuetify } from 'vuetify'
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";

const ConfirmDialog = (message) => {
    return new Promise((resolve) => {
        const app = createApp({
            data: () => ({
                show: true,
                msg: message
            }),
            template: `
                <v-dialog v-model="show" persistent max-width="400px">
                    <v-card>
                        <v-card-title class="headline">Confirm</v-card-title>
                        <v-card-text>{{ msg }}</v-card-text>
                        <v-card-actions>
                            <v-btn text @click="cancel">No</v-btn>
                            <v-btn color="primary" @click="confirm">Yes</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            `,
            methods: {
                confirm() {
                    resolve({ok:true,no:false})
                    this.show = false
                    app.unmount()
                },
                cancel() {
                    resolve({ok:false,no:true})
                    this.show = false
                    app.unmount()
                }
            },
            components: {
                // VDialog,
                // VCard,
                // VCardTitle,
                // VCardText,
                // VCardActions,
                // VBtn
            }
        })

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

        app.use(vuetify)

        const container = document.createElement('div')
        document.body.appendChild(container)
        app.mount(container)
    })
}

export default ConfirmDialog
