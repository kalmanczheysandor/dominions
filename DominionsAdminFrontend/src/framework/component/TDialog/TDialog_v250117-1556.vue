<template>
    <v-dialog v-model="isVisible" persistent max-width="600px">
        <v-card>
            <v-card-title class="d-flex justify-space-between align-center">
                <div>
                    <slot name="title">{{ title }}</slot>
                </div>
                <v-btn v-if="closeable" icon @click="close">
                    <v-icon>mdi-close</v-icon>
                </v-btn>
            </v-card-title>
            <v-card-text>
                <slot>{{ content }}</slot>
            </v-card-text>
        </v-card>
    </v-dialog>
</template>
<script>
    export default {
        name: "TDialog",
        props: {
            title: {
                type: String,
                default: "Default Title",
            },
            closeable: {
                type: Boolean,
                default: true,
            },
        },

        data() {
            return {
                isVisible: false,
            };
        },
        watch: {
            isVisible(val) {
                if (!val) {
                    this.$emit("closed");
                }
            },
        },
        emits: ["closed"],
        methods: {
            close() {
                this.isVisible=false;
            },
            open() {
                this.isVisible=true;
            },
        },
    };
</script>

