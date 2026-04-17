<template>
    <v-row no-gutters class="ma-1" style="gap:5px; " >
        <v-col v-for="imageItem in modelValue" :key="imageItem.key" class="d-flex">
            <div class="image-wrapper">
                <v-img
                    :src="imageItem.imageSrc"
                    class="GalleryImage bg-grey-lighten-2"
                    @click.stop="eventItemClicked(imageItem)"
                    cover
                    height="50"
                >
                <div class="Actions">
                    <v-icon class="Icon" @click.stop="eventTrashIconClicked(imageItem)">mdi-delete-outline</v-icon>
                </div>
                <template v-slot:placeholder>
                    <v-row align="center" class="fill-height ma-0" justify="center">
                        <v-progress-circular color="grey-lighten-5" indeterminate></v-progress-circular>
                    </v-row>
                </template>
                </v-img>
            </div>
        </v-col>
    </v-row>
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
    /* Wrapper div around each image */
    .image-wrapper {
        position: relative;
        border: 1px grey solid;
        border-radius: 2px;
        padding: 2px;
        box-sizing: border-box;
        width: 100%;

        justify-content: center;
        align-items: center;
    }

    /* Add gap between images */
    .v-row {
        gap: 5px;
    }

    /* Control the minimum image width and auto-fill the grid */
    .v-row {
            //display: grid;
            ////grid-template-columns: repeat(auto-fill, minmax(50px, 1fr));
            //gap: 10px;
    }

    /* Optional: Styling for the image container */
    .GalleryImage {
        border-radius: 2px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

    }

    /* Add some padding for better spacing */
    .Actions {
        position: absolute;
        top: 10px;
        right: 10px;
    }
</style>
