import { createApp } from 'vue'
import { createVuetify } from 'vuetify'
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";

const AlertDialog = (message) => {
    return new Promise((resolve) => {
        const app = createApp({
            data: () => ({
                show: true,
                msg: message
            }),
            template: `
                <v-dialog v-model="show" persistent max-width="400px">
                    <v-card>
                        <v-card-title class="headline">Alert</v-card-title>
                        <v-card-text>{{ msg }}</v-card-text>
                        <v-card-actions>
                            <v-btn color="primary" @click="confirm">OK</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            `,
            methods: {
                confirm() {
                    resolve(true) // Resolve a Promise-val
                    this.show = false // Bezárja a dialógust
                    app.unmount() // Kicsomagolja az alkalmazást
                }
            }
        })

        const vuetify = createVuetify({
            components,
            directives, // Directives biztosítása
        })

        app.use(vuetify)

        const container = document.createElement('div')
        document.body.appendChild(container)
        app.mount(container)
    })
}

export default AlertDialog
