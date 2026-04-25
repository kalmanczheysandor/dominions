import {createApp} from 'vue'
import {createVuetify} from 'vuetify'
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";

const TWarningDialog = (params) => {
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
                <div class="TDialog" mode="Warning">
                    <div class="Title">
                        {{title}}
                    </div>
                    <div class="Body">
                        <p v-for="(message,index) in messageList" :key="index">{{message}}</p>
                    </div>
                    <div class="Footer">
                        <button type="button" class="TButton OkButton" @click="eventClickOnOkButton">OK</button>
                    </div>
                </div>
                </v-dialog>
            `,
            methods: {
                eventClickOnOkButton() {
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

export default TWarningDialog
