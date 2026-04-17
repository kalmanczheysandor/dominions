<template>
    <v-dialog v-model="isVisible" persistent max-width="600px">
        <div class="TDialog">
            <div class="Title">
                {{title}}
                <button type="button" class="CloseButton" v-if="closeable" @click="eventCloseButtonClicked">
                    <v-icon>mdi-close</v-icon>
                </button>
            </div>
            <div class="Body">
                <slot>{{content}}</slot>
            </div>
        </div>
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
                if(!val) {
                    this.$emit("whenDialogClosed");
                }
            },
        },
        emits: ['whenDialogClosed'],
        methods: {
            eventCloseButtonClicked() {
                this.close();
            },
            close() {
                this.isVisible = false;
            },
            open() {
                this.isVisible = true;
            },
        }
    };
</script>


