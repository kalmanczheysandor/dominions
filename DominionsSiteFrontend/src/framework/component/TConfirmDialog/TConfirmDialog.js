import { createApp } from 'vue'
import { createVuetify } from 'vuetify'
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";

const TConfirmDialog = (message) => {
    return new Promise((resolve) => {
        const app = createApp({
            data: () => ({
                show: true,
                msg: message
            }),
            template: `
                <v-dialog v-model="show" persistent max-width="400px">
                <div class="TDialog" mode="Confirm">
                    <div class="Title">
                        Confirm
                    </div>
                    <div class="Body">
                        {{msg}}
                    </div>

                    <div class="Footer">
                        <button type="button" class="TButton NoButton" @click="eventClickOnNoButton">No</button>
                        <button type="button" class="TButton YesButton" @click="eventClickOnYesButton">Yes</button>
                    </div>
                </div>
                </v-dialog>
            `,
            methods: {
                eventClickOnYesButton() {
                    resolve({ok:true,no:false})
                    this.show = false
                    app.unmount()
                },
                eventClickOnNoButton() {
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

export default TConfirmDialog
