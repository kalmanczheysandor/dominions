<template>
    <v-dialog v-model="isVisible" persistent max-width="600px">

        <div className="TDialog">
            <div className="Title">
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
                    this.$emit("closed");
                }
            },
        },
        emits: ["closed"],
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
        },
    };
</script>
<style>
    .TDialog {
        position: fixed;
        top: 50%;
        left: 50%;

        width: 100%;
        max-height: 80vh;

        background-color: white;
        box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
        border-radius: 3px;

        transform: translate(-50%, -50%);
        border:1px green solid;
        display: inline-block;
    }

    .TDialog > .Title {
        position: absolute;
        top:0px;
        left:0px;
        right:0px;

        margin: 0px;
        padding: 5px 10px 5px 10px;

        box-sizing: border-box;
        height: 30px;

        font-family: MainFont;
        font-size: 18px;
        color: white;
        vertical-align: middle; /* függőleges középre igazítás */
        line-height: 20px;

        border: none;
        background: rgb(var(--v-theme-primary)) !important;
        border-radius: 3px 3px 0px 0px;
    }

    .TDialog > .Title > .CloseButton {
        position: absolute;
        top: 0px;
        right: 0px;

        margin: 0px;
        padding: 0px;

        height: 30px;
        width: 30px;
        box-sizing: border-box;
        border-radius: 0px 3px 0px 0px;
        border: none;

        background-color: #cc2900;
    }

    .TDialog > .Title > .CloseButton:hover {
        background-color: #ff3300;
    }


    .TDialog > .Body {
        position: absolute;
        top:30px;
        left:0px;
        right:0px;
        bottom: 0px;

        margin: 0px;
        padding: 10px;
        border-radius: 0px 0px 3px 3px;

        border: 1px red solid;
    }

    .TDialog > .Body::-webkit-scrollbar {
        width: 10px;
        height: 10px;
        background-color: #f2f2f2;
    }

    .TDialog > .Body::-webkit-scrollbar-track {
        width: 10px;
        height: 10px;
        background-color: #f2f2f2;
    }

    .TDialog > .Body::-webkit-scrollbar-thumb {
        background-color: #e6e6e6;
    }

    .TDialog > .Body::-webkit-scrollbar-thumb:hover {
        background-color: #cccccc;
    }


</style>

