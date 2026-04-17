<template>
    <div class="TPhotoGrid" ref='TPhotoGrid'>
        <v-row>
            <v-col v-for="imageItem in modelValue" :key="imageItem.key" class="d-flex child-flex" cols="4" style="margin:1px !important;padding:2px !important;">
                <v-img
                    :src="imageItem.imageSrc"
                    height="70" width="70"
                    class="GalleryImage bg-grey-lighten-2" style="position:relative"
                    @click.stop="eventItemClicked(imageItem)"
                    cover
                >

                    <div class="Actions">
                        <v-icon class="Icon"  @click.stop="eventTrashIconClicked(imageItem)">mdi-delete-outline</v-icon>
                    </div>
                    <template v-slot:placeholder>
                        <v-row align="center" class="fill-height ma-0" justify="center">
                            <v-progress-circular color="grey-lighten-5" indeterminate></v-progress-circular>
                        </v-row>
                    </template>
                </v-img>
            </v-col>
        </v-row>
    </div>
</template>

<script>


    export default {
        props: {
            modelValue: {
                type: String,
                default: ''
            },
        },
        data() {
            return {};
        },
        computed: {},
        methods: {
            eventItemClicked(item) {
                this.$emit('whenItemClicked', item);
            },
            eventTrashIconClicked(item) {
                this.$emit('whenItemIsSelectedToDelete', item);
            }
        }

    };
</script>

<style>

    .TPhotoGrid {
        position: relative;
        display: block;
        margin: 5px;
        padding: 0px;
        box-sizing: border-box;


        font-family: Arial;

        font-size: 14px;
        color: #657786;





    }


    .TPhotoGrid::-webkit-scrollbar {
        width: 10px;
        height: 10px;
        //background-color: #f2f2f2;
        background-color: red;
    }


    .TPhotoGrid::-webkit-scrollbar-track {
        width: 10px;
        height: 10px;
        background-color: #f2f2f2;
    }

    .TPhotoGrid::-webkit-scrollbar-thumb {
        background-color: #e6e6e6;
    }

    .TPhotoGrid::-webkit-scrollbar-thumb:hover {
        background-color: #cccccc;
    }

    .TPhotoGrid > .Actions {
        position: absolute;
        top: 0px;
        left: 0px;
        right: 0px;
        height: 30px;
        z-index: 2;
    }

    .TPhotoGrid > .Actions > .Icon {
    //text-shadow: 0px 0px 2px black; text-shadow: #000 0px 0px 1px, #000 0px 0px 1px, #000 0px 0px 1px, #000 0px 0px 1px, #000 0px 0px 1px, #000 0px 0px 1px; color: white; font-size: 30px; opacity: 0.2;
    }

    .TPhotoGrid > .Actions > .Icon:hover {
        text-shadow: 0px 0px 2px black;
        color: white;
        font-size: 30px;
        cursor: pointer;
        opacity: 1;
    }


    .TPhotoGrid .ImageFrame {
        position: relative;

        margin: 5px;
        padding: 5px;

        font-family: Arial;
        font-size: 15px;
        color: #cc0000;
        text-align: left;

        border: 1px solid #ddd;
        border-radius: 2px;

        background-color: transparent;
        box-sizing: border-box;
        float: left;

    }

    .TPhotoGrid .ImageFrame .TheImage {
        margin: 0px;
        padding: 0px;
        height: 80px;

        font-family: Arial;
        font-size: 15px;
        color: #cc0000;
        text-align: left;

        border: none;

        background-color: transparent;
        box-sizing: border-box;
        float: left

    }

    .TPhotoGrid .ImageFrame .ProgressBar {
        position: absolute;
        top: 50%;
        left: 50%;

        margin: 0;
        padding: 0;
        height: 5px;
        border: none;
        background-color: transparent;
        box-sizing: border-box;


        text-shadow: #000 0px 0px 1px, #000 0px 0px 1px, #000 0px 0px 1px, #000 0px 0px 1px, #000 0px 0px 1px, #000 0px 0px 1px;
        color: #ffffff;
        transform: translate(-50%, -50%); /* Az elem középre igazítása mindkét irányban */
    }


    .TPhotoGrid .ImageFrame .ProgressBar .ProgressScale {
        margin: 0px;
        padding: 0px;
        height: 5px;
        width: 0%;

        border: none;
        background-color: yellow;
        box-sizing: border-box;
    }


</style>

