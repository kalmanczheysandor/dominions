<template>
    <div>
        <input type="file" ref="fileInput" style="display: none;" @change="eventFileSelected" accept="image/*"/>
        <div class="CroppieBox" @click.stop="eventBoxClicked">

            <div v-show="croppie || croppedImageBase64" class="Actions">
                <v-icon v-show="croppie || croppedImageBase64" class="Icon" @click.stop="eventTrashIconClicked">
                    mdi-delete-outline
                </v-icon>
                <v-icon v-show="croppie && !croppedImageBase64" class="Icon" @click.stop="eventCropIconClicked">
                    mdi-crop-free
                </v-icon>
                <v-icon v-show="croppie && !croppedImageBase64" class="Icon" @click.stop="eventRotateRightIconClicked">
                    mdi-rotate-right
                </v-icon>
                <v-icon v-show="croppedImageBase64" class="Icon" @click.stop="eventEditIconClicked">
                    mdi-image-edit-outline
                </v-icon>
            </div>
            <div v-show="croppie && !croppedImageBase64" class="Module" ref="croppieContainer" @click.stop="eventImageClicked"></div>
            <img v-show="croppedImageBase64" :src="croppedImageBase64" class="CroppedImage" alt="Cropped Image"/>


        </div>
    </div>
</template>

<script>
    import Croppie from 'croppie';

    export default {
        props: {
            picture: {
                type: String,
                required: false,
                default: null
            },
            config: {
                type: Object,
                required: false,
                default: () => ({
                    cropWidth: 300,
                    cropHeight: 300
                })
            }
        },
        data() {
            return {
                firstLoad: true,
                croppie: null,
                croppedImageBase64: null // To store the cropped image in base64
            };
        },
        computed: {
            cropWidth() {
                return this.config.cropWidth;
            },
            cropHeight() {
                return this.config.cropHeight;
            },
            boundaryWidth() {
                return this.cropWidth + 60;
            },
            boundaryHeight() {
                return this.cropHeight + 60;
            }
        },
        mounted() {
            if(this.picture !== null) {
                this.preload(this.picture);
            }
        },
        methods: {

            cro(image) {
                let el = this.$refs.croppieContainer;
                this.croppie = new Croppie(el, {
                    viewport: {width: this.cropWidth, height: this.cropHeight, type: 'square'},
                    boundary: {width: this.boundaryWidth, height: this.boundaryHeight},
                    showZoomer: true,
                    enableOrientation: true,
                    enableExif: true,
                    zoom: 0
                });

                // Bind the Croppie instance to the initial image
                this.croppie.bind({
                    url: image,
                    orientation: 1,
                    enableOrientation: true,
                    zoom: 0
                });
            },


            preload(imageUrl) {

                if(imageUrl instanceof Blob) {
                    const reader = new FileReader();
                    reader.onload = (e) => {
                        let image = e.target.result; // Set the selected image

                        this.cro(image);
                        this.firstLoad = false;

                    };
                    reader.readAsDataURL(imageUrl);
                } else if(typeof imageUrl === 'string') {
                    this.cro(imageUrl);
                }

            },

            eventBoxClicked() {
                if(!this.croppie && !this.croppedImageBase64) {
                    this.$refs.fileInput.click(); // Trigger file input
                }
            },

            eventFileSelected(event) {
                const file = event.target.files[0];

                if(file) {
                    // Destroy the current Croppie instance
                    this.doFlush();
                    this.preload(file);
                    // const reader = new FileReader();
                    // reader.onload = (e) => {
                    //     let image = e.target.result; // Set the selected image
                    //
                    //     let el = this.$refs.croppieContainer;
                    //     this.croppie = new Croppie(el, {
                    //         viewport: {width: this.cropWidth, height: this.cropHeight, type: 'square'},
                    //         boundary: {width: this.boundaryWidth, height: this.boundaryHeight},
                    //         showZoomer: true,
                    //         enableOrientation: true,
                    //         enableExif: true,
                    //         zoom: 0
                    //     });
                    //
                    //     // Bind the Croppie instance to the initial image
                    //     this.croppie.bind({
                    //         url: image,
                    //         orientation: 1,
                    //         enableOrientation: true,
                    //         zoom: 0
                    //     });
                    //     this.firstLoad = false;
                    //
                    // };
                    // reader.readAsDataURL(file);
                }
            },
            eventTrashIconClicked() {
                this.doFlush();
            },
            eventCropIconClicked() {
                this.doCropImage();
            },

            eventEditIconClicked() {
                this.croppedImageBase64 = null;
            },
            eventRotateRightIconClicked() {
                this.croppie.rotate(-90);
            },


            eventResetClicked() {
                this.croppie.setZoom(0);
            },


            // Get cropped image in base64 format
            doCropImage() {
                this.croppie.result({
                    type: 'base64', // Type of output, base64 in this case
                    size: {width: this.cropWidth, height: this.cropHeight} // Resize the output image if needed
                }).then((croppedImage) => {
                    this.croppedImageBase64 = croppedImage; // Store the cropped image base64
                });
            },

            doFlush() {
                // Reset file input field
                this.$refs.fileInput.value = "";

                // Destroy the current Croppie instance
                if(this.croppie !== null) {
                    //this.image='http://localhost:8080/favicon.ico',
                    this.croppie.destroy();
                    this.croppie = null;
                }
                this.croppedImageBase64 = null;

                // Clear the Croppie container
                const croppieEl = this.$refs.croppieContainer;
                if(croppieEl) {
                    croppieEl.innerHTML = ''; // Clear current content
                }
            }
        }
    };
</script>

<style>

    .CroppieBox {
        border: 1px #cccccc solid;
        background-color: #808080;
        height: 360px;
        width: 360px;
        position: relative;
        padding: 0px;
    }

    .CroppieBox > .CroppedImage {
        margin: 30px;
    }

    .CroppieBox > .Actions {

        position: absolute;
        top: 0px;
        left: 0px;
        right: 0px;
        height: 30px;
        z-index: 2;
    }

    .CroppieBox > .Actions > .Icon {
        text-shadow: 0px 0px 2px rgba(0, 0, 0, 1);
        color: #b3b3b3;
        font-size: 30px;
    }

    .CroppieBox > .Actions > .Icon:hover {
        text-shadow: 0px 0px 5px rgba(0, 0, 0, 1);
        color: white;
        font-size: 30px;
        cursor: pointer;
    }

    .CroppieBox > .Module {
        border: 1px blue solid;
        position: absolute;
        top: 0px;
        left: 0px;
        right: 0px;
        bottom: 0px;
        z-index: 1;
    }

    .croppie-container .cr-slider-wrap {
        position: absolute;
        bottom: 0px;
        left: 0px;
        right: 0px;
        z-index: 1;
        width: 100% !important;
        margin: 0px !important;
    }


    .controls {
        display: flex;
        gap: 10px;
        margin-top: 10px;
    }

    button {
        padding: 10px;
        background-color: #4CAF50;
        color: white;
        border: none;
        border-radius: 5px;
        cursor: pointer;
    }

    button:hover {
        background-color: #45a049;
    }

    img {
        max-width: 100%;
        margin-top: 10px;
    }
</style>
