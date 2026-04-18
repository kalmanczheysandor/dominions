import { createApp } from 'vue'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'

const BasicDialog = async (message, filePath, callback) => {
    // Dynamically import the component based on the file path provided
    console.log(import.meta.url);
    filePath = "./XDialog.vue";
    console.log(window.location.origin + '/components/MyComponent.vue');
    const imported = await import(filePath);

    imported.then((component) => {
        const app = createApp({
            components: {
                DynamicDialog: component.default // The dynamically loaded component
            },
            data() {
                return {
                    show: true,
                    msg: message
                }
            },
            template: `
                <DynamicDialog :message="msg" v-model="show" @confirmed="confirm"/>
            `,
            methods: {
                confirm() {
                    this.show = false
                    callback(true) // Call the callback with 'true' when OK is clicked
                    app.unmount() // Unmount the app instance
                }
            }
        })

        // Vuetify instance setup
        const vuetify = createVuetify({
            components,
            directives
        })

        app.use(vuetify)

        const container = document.createElement('div')
        document.body.appendChild(container)
        app.mount(container)
    }).catch((error) => {
        console.error('Error loading component:', error)
    })
}

export default BasicDialog
