<template>
    <v-dialog v-model="isVisible" persistent max-width="600px">

        <div class="TDialog">
            <div class="Title">
                {{title}}
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
                    this.$emit("closed");
                }
            },
        },
        emits: ["closed"],
        methods: {
            close() {
                this.isVisible = false;
            },
            open() {
                this.isVisible = true;
            },
        },
    };
</script>
<style>
    .TDialog {
        position: absolute;
        top: 50%;
        left: 50%;
        border: 1px red solid;
        background-color: white;
        transform: translate(-50%, -50%);

    }

    .TDialog > .Title {
        border: 1px green solid;
        background: blue;
    }
    .TDialog > .Body {
        border: 1px green solid;
        background: green;

        flex: 1 1 auto;
        font-size: 0.875rem;
        font-weight: 400;
        letter-spacing: 0.0178571429em;
        padding: 1rem;
        text-transform: none;

    }
</style>

