<template>
    <v-dialog v-model="isVisible" persistent max-width="600px">
        <v-card>
            <v-card-title class="d-flex justify-space-between align-center">
                <div>
                    <slot name="title">{{ title }}</slot>
                </div>
                <!-- Close Button in Title -->
                <v-btn v-if="closeable" icon @click="close">
                    <v-icon>mdi-close</v-icon>
                </v-btn>
            </v-card-title>
            <v-card-text>
                <slot>
                    {{ content }}
                </slot>
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
            content: {
                type: String,
                default: "",
            },
            value: {
                type: Boolean,
                required: true,
            },
            closeable: {
                type: Boolean,
                default: true, // Alapértelmezett: bezárható
            },
        },
        emits: ["update:value", "closed"],
        data() {
            return {
                isVisible: this.value,
            };
        },
        watch: {
            value(val) {
                this.isVisible = val;
            },
            isVisible(val) {
                this.$emit("update:value", val);
                if (!val) {
                    this.$emit("closed");
                }
            },
        },
        methods: {
            close() {
                this.isVisible = false;
            },
        },
    };
</script>
