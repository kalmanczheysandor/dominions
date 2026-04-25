import {createApp} from 'vue'
import {createVuetify} from 'vuetify'
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";

const WarningDialog = (params) => {
    return new Promise((resolve) => {
        const app = createApp({
            data: () => ({
                isVisible: true,
                okMethodToCall: params.okEvent ? params.okEvent : null,
                title: params.title ? params.title : 'Warning',
                messageList: params.message ? (
                    Array.isArray(params.message) ? params.message : [params.message]
                ) : []
            }),

            template: `
                <v-dialog v-model="isVisible" persistent max-width="400px">
                <v-card>
                    <v-card-title style="backgroundColor:orange" >{{title}}</v-card-title>
                    <v-card-text>
                        <p v-for="(message,index) in messageList" :key="index">{{message}}</p>
                    </v-card-text>
                    <v-card-actions>
                        <v-btn style="backgroundColor:orange" @click="eventOk">OK</v-btn>
                    </v-card-actions>
                </v-card>
                </v-dialog>
            `,
            methods: {
                eventOk() {
                    if (this.okMethodToCall) {
                        this.okMethodToCall();
                    }
                    resolve(true);
                    this.isVisible = false;
                    app.unmount();
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

export default WarningDialog
