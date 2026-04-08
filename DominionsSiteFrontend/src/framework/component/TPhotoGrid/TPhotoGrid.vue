<template>
    <div class="TPhotoGrid">


        <div class="ImageFrame" v-for="imageItem in modelValue" :key="imageItem.key">
            <div class="Actions">
                <v-icon class="Icon" @click.stop="eventTrashIconClicked(imageItem)">mdi-delete-outline</v-icon>
            </div>
            <v-img
                :src="imageItem.imageSrc"
                class=" bg-grey-lighten-2"
                @click.stop="eventItemClicked(imageItem)"

                :aspect-ratio="1"
                cover
            >

                <template v-slot:placeholder>
                    <v-row align="center" class="fill-height ma-0" justify="center">
                        <v-progress-circular color="grey-lighten-5" indeterminate></v-progress-circular>
                    </v-row>
                </template>
            </v-img>
        </div>

    </div>


</template>

<script>
    export default {
        props: {
            modelValue: {
                type: Array,  // Changes from String to Array to allow multiple images
                default: () => [],
            },
        },
        data() {
            return {};
        },
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

<style scoped>
    .TPhotoGrid {
        display: flex;
        flex-wrap: wrap;
        gap: 10px;
    }

    .TPhotoGrid > .ImageFrame {
        position: relative;
        flex: 1 1 calc(25% - 10px);
        max-width: calc(25% - 10px);
        background-color: transparent;


        border: 1px grey solid;
        padding: 3px;
        height: auto;
    }

    .TPhotoGrid > .ImageFrame:hover {
        border: 2px grey solid;
        padding:2px;
    }




    .TPhotoGrid > .ImageFrame > .Actions {
        position: absolute;
        top: 0px;
        left: 0px;
        right: 0px;
        height: 30px;
        z-index: 2;
        text-align: left;
    }

    .TPhotoGrid > .ImageFrame > .Actions > .Icon {
        position: relative;
        display: inline;
    //text-shadow: 0px 0px 2px black; text-shadow: #000 0px 0px 1px, #000 0px 0px 1px, #000 0px 0px 1px, #000 0px 0px 1px, #000 0px 0px 1px, #000 0px 0px 1px; color: white; font-size: 30px; opacity: 0.2;
    }

    .TPhotoGrid > .ImageFrame > .Actions > .Icon:hover {
        text-shadow: 0px 0px 2px black;
        color: white;
        font-size: 30px;
        cursor: pointer;
        opacity: 1;
    }

</style>
