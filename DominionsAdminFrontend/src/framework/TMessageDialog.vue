<template>
    <v-dialog v-model="isVisible" max-width="400px">
        <v-card>
            <v-card-title :style="backgroundPrimaryStyle">{{title}}</v-card-title>
            <v-card-text>
               <p v-for="(message,index) in messages" :key="index">{{message}}</p>
            </v-card-text>
            <v-card-actions>
                <v-btn :style="backgroundPrimaryStyle" @click="eventOk">Ok</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>

    export default {

        props: {
            type: {type: String, default: "warn"}
        },
        data() {
            return {
                isVisible: false,
                okMethodToCall: null,
                title: "Message",
                messages: ""
            };
        },
        computed: {
            backgroundPrimaryStyle() {
                console.log("The color:");
                console.log(this.$vuetify.theme.current.colors.error);
                if (this.type === "info") {
                    return {backgroundColor: this.$vuetify.theme.current.colors.primary};
                } else if (this.type === "success") {
                    return {backgroundColor: this.$vuetify.theme.current.colors.success};
                } else if (this.type === "warn") {
                    return {backgroundColor: this.$vuetify.theme.current.colors.warning};
                } else if (this.type === "error") {
                    return {backgroundColor: this.$vuetify.theme.current.colors.error};
                }
                return {backgroundColor: 'pink'};
            },

            textPrimaryStyle() {
                if (this.type === "info") {
                    return {'color': this.$vuetify.theme.themes.light.primary};
                } else if (this.type === "success") {
                    return {'color': this.$vuetify.theme.themes.light.success};
                } else if (this.type === "warn") {
                    return {'color': this.$vuetify.theme.themes.light.warning};
                } else if (this.type === "error") {
                    return {'color': this.$vuetify.theme.themes.light.error};
                }
                return {'color': 'pink'};
            }

        },
        methods: {
            eventOk() {
                this.isVisible = false;
                if (this.okMethodToCall) {
                    this.okMethodToCall(); // Lokális confirm esemény hívása
                }
            },
            open(okEvent = null, title = null, messages = null) {
                if (okEvent !== null) {
                    this.okMethodToCall = okEvent;
                }
                if (title !== null) {
                    this.title = title;
                }
                if (messages !== null) {
                    if (!Array.isArray(messages)) {
                        messages = [messages];
                    }
                    this.messages = messages;
                }

                this.isVisible = true;
            }
        }
    };
</script>
