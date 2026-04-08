<template>
    <div class="CroppieInput">
        <input type="file" ref="fileInput" style="display: none;" @change="eventFileSelected" accept="image/*"/>
        <div class="CroppieBox" :style="styleCroppieBox" @click.stop="eventBoxClicked">

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
        <div v-if="validationError" class="ValidationErrorMessage">
            {{validationError}}
        </div>
    </div>
</template>

<script>
    import Croppie from 'croppie';

    export default {
        props: {
            modelValue: {
                type: String,
                default: ''
            },
            image: {
                type: String,
                required: false,
                default: null
            },
            config: {
                type: Object,
                required: false,
                default: () => ({
                    cropWidth: 150,
                    cropHeight: 150
                })
            },
            rules: {
                type: Array,
                default: () => []
            }
        },
        watch: {
            result(newValue) {
                this.validate();
                this.$emit('update:modelValue', newValue);
            }
        },
        data() {
            return {
                firstLoad: true,
                croppie: null,
                croppedImageBase64: null,
                result: this.modelValue, // To store the cropped image in base64
                validationError: null,
                isValid: true
            };
        },
        computed: {
            styleCroppieInput() {
                return {
                    border: '10px rgb(var(--v-theme-error)) solid',
                };
            },


            styleCroppieBox() {
                return {
                    width: this.boundaryWidth + 'px',
                    height: this.boundaryHeight + 'px',
                };
            },

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
            if(this.image !== null) {
                this.openFile(this.image);
            }
        },
        created() {
            // this.validate();
        },
        methods: {


            validate() {
                this.validationError = null;
                let isValid = true;
                for(let rule of this.rules) {
                    const error = rule(this.result);
                    if(error !== true) {

                        this.validationError = error;
                        isValid = false;
                        break;
                    }
                }
                return isValid;
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
                    this.openFile(file);

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
                this.result = '';
            },
            eventRotateRightIconClicked() {
                this.croppie.rotate(-90);
            },


            eventResetClicked() {
                this.croppie.setZoom(0);
            },


            // bindCroppie(imageUrl) {
            //     let el = this.$refs.croppieContainer;
            //
            //     // Create a new Croppie instance
            //     this.croppie = new Croppie(el, {
            //         viewport: {width: this.cropWidth, height: this.cropHeight, type: 'square'},
            //         boundary: {width: this.boundaryWidth, height: this.boundaryHeight},
            //         showZoomer: true,
            //         enableOrientation: true,
            //         enableExif: true,
            //         zoom: 0
            //     });
            //
            //     // Fetch the image with credentials: 'include'
            //     fetch(imageUrl, {
            //         method: 'GET',
            //         credentials: 'include' // Ensure credentials (cookies, etc.) are included
            //     })
            //     .then(response => response.blob())
            //     .then(blob => {
            //         const reader = new FileReader();
            //         reader.onloadend = () => {
            //             let image = reader.result; // Convert the image blob to base64
            //             this.croppie.bind({
            //                 url: image,  // Use the base64 image
            //                 orientation: 1,
            //                 enableOrientation: true,
            //                 zoom: 0
            //             });
            //         };
            //         reader.readAsDataURL(blob); // Read the image blob as base64
            //     })
            //     .catch(error => {
            //         console.error('Error loading image:', error);
            //     });
            // },


            bindCroppie(image) {
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

            async openFile(imageUrl) {
                if(imageUrl instanceof Blob) {
                    const reader = new FileReader();
                    reader.onload = (e) => {
                        let image = e.target.result; // Set the selected image
                        this.bindCroppie(image);
                        this.firstLoad = false;

                    };
                    reader.readAsDataURL(imageUrl);
                } else if(typeof imageUrl === 'string') {
                    try {
                        // Communicate to backend
                        const response = await fetch(imageUrl, {
                            method: 'GET',
                            credentials: 'include' // Ensure credentials (cookies, etc.) are included
                        });

                        // Checking: Backend response status
                        if(!response.ok) {
                            return false;
                        }

                        // Checking: Type checking
                        const contentType = response.headers.get("content-type");
                        if (!contentType || !contentType.startsWith("image/")) {
                            console.log("NO IMAGE (not an image content-type)");
                            return false;
                        }

                        // Checking: Content size checking
                        const blob = await response.blob();
                        if (!blob || blob.size === 0) {
                            console.log("NO IMAGE (empty blob)");
                            return false;
                        }

                        //
                        const reader = new FileReader();
                        reader.onloadend = () => {
                            const image = reader.result; // This will contain the base64 string
                            this.bindCroppie(image);
                        };
                        reader.readAsDataURL(blob);  // Read the blob as base64
                    } catch(error) {
                        console.error('Error loading image:', error);
                    }

                } else {
                    console.error('Unsupported croppie case has been found!')
                }

            }
            ,
            doCropImage() {
                this.croppie.result({
                    type: 'base64', // Type of output, base64 in this case
                    size: {width: this.cropWidth, height: this.cropHeight} // Resize the output image if needed
                }).then((croppedImage) => {
                    this.croppedImageBase64 = croppedImage; // Store the cropped image base64
                    this.result = croppedImage.split(",")[1];
                });
            }
            ,
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
                this.result = '';

                // Clear the Croppie container
                const croppieEl = this.$refs.croppieContainer;
                if(croppieEl) {
                    croppieEl.innerHTML = ''; // Clear current content
                }
            }
            ,
            getResultAsBase64() {

                if(this.croppedImageBase64 === null) {
                    return null;
                }

                return this.croppedImageBase64.split(",")[1]
            }
        }
    };
</script>

<style>


    .CroppieInput {
        display: inline-block;
    }

    .CroppieInput > .ValidationErrorMessage {
        border-top: 1px rgb(var(--v-theme-error)) solid;
        color: rgb(var(--v-theme-error));
        font-size: 12px;
        padding-left: 20px;

    }


    .CroppieBox {
        position: relative;

        height: 360px;
        width: 360px;
        padding: 0px;

        border: 1px #cccccc solid;
        background-color: #808080;
    }

    .CroppieBox > .CroppedImage {
        margin: 30px;
    }

    .CroppieBox > .Actions {

        position: absolute;
        top: 30px;
        left: 0px;
        bottom: 30px;
        width: 30px;
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
        left: 30px;
        right: 30px;
        z-index: 1;
        width: auto !important;
        margin: 0px !important;
    }


    .controls {
        display: flex;
        gap: 10px;
        margin-top: 10px;
    }


</style>
