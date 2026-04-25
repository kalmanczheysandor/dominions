<template>
    <v-dialog v-model="visible" max-width="400px">
        <v-card>
            <v-card-title class="headline">{{title}}</v-card-title>
            <v-card-text>
                {{message}}
            </v-card-text>
            <v-card-actions>
                <v-btn text @click="cancel">Cancel</v-btn>
                <v-btn color="primary" @click="confirm">Confirm</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>
    export default {
        data() {
            return {
                visible: false,
                cancelMethod: null,
                confirmMethod: null,
                title: "Confirm",
                message: "Do you wish to continue?"
            };
        },
        methods: {
            cancel() {
                this.visible = false;
                if (this.cancelMethod) {
                    this.cancelMethod(); // Lokális cancel esemény hívása
                }
            },
            confirm() {
                this.visible = false;
                if (this.confirmMethod) {
                    this.confirmMethod(); // Lokális confirm esemény hívása
                }
            },
            showDialog(confirmEvent = null, cancelEvent = null, title = null, message = null) {
                if (confirmEvent !== null) {
                    this.confirmMethod = confirmEvent;
                }
                if (cancelEvent !== null) {
                    this.cancelMethod = cancelEvent;
                }
                if (title !== null) {
                    this.title = title;
                }
                if (message !== null) {
                    this.message = message;
                }

                this.visible = true;
            }
        }
    };
</script>
