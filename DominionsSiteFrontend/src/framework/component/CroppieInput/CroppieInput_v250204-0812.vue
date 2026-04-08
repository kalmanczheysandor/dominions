<template>
    <div class="Image-Upload-wrapper Image-upload">
        <p>This is an image upload wrapper component</p>

        <!-- Hidden file input -->
        <input type="file" ref="fileInput" style="display: none;" @change="eventFileSelected" accept="image/*"/>
        <div class="CroppieBox" @click.stop="eventBoxClicked">

            <div class="Actions">
                <v-icon class="FirstIcon">mdi-trash-can</v-icon>
            </div>
            <div class="Module" id="croppie" @click.stop="eventImageClicked"></div>

        </div>


        <!-- Croppie container -->

        <!-- Controls for zoom, rotate, flip, and reset -->
        <div class="controls">
            
            <button @click="zoomIn">Zoom In</button>
            <button @click="zoomOut">Zoom Out</button>
            <button @click="rotateLeft">Rotate Left</button>
            <button @click="rotateRight">Rotate Right</button>
            <button @click="flipHorizontal">Flip Horizontal</button>
            <button @click="flipVertical">Flip Vertical</button>
            <button @click="clearImage">Clear Image</button> <!-- Button to clear the selected image -->
            <button @click="getCroppedImage">Get Cropped Image</button> <!-- Button to get cropped image -->
        </div>

        <!-- To show the result base64 string -->
        <div v-if="croppedImageBase64">
            <p>Cropped Image in Base64:</p>
            <img :src="croppedImageBase64" alt="Cropped Image" />
        </div>
    </div>
</template>

<script>
    import Croppie from 'croppie';

    export default {
        data() {
            return {
                image: null,
                croppie: null,
                croppedImageBase64: null // To store the cropped image in base64
            };
        },
        methods: {
            eventBoxClicked() {
                this.$refs.fileInput.click(); // Trigger file input
            },
            eventImageClicked(event) {
                console.log("eventImageClicked");
                const viewport = this.$el.querySelector('.cr-viewport'); // Get the viewport (image area)
                this.$refs.fileInput.click(); // Trigger file input
                // Check if the click is within the viewport (image area)
                if (viewport && viewport.contains(event.target)) {
                    // this.$refs.fileInput.click(); // Trigger file input
                }
            },
            eventFileSelected(event) {
                const file = event.target.files[0];
                if (file) {
                    const reader = new FileReader();
                    reader.onload = (e) => {
                        let image = e.target.result; // Set the selected image

                        // Destroy the current Croppie instance
                        if (this.croppie!==null) {
                            //this.image='http://localhost:8080/favicon.ico',
                            this.croppie.destroy();
                            this.croppie=null;
                        }

                        // Clear the Croppie container
                        const croppieEl = document.getElementById('croppie');
                        croppieEl.innerHTML = ''; // Clear current content


                        let el = document.getElementById('croppie');
                        this.croppie = new Croppie(el, {
                            viewport: { width: 300, height: 300, type: 'square'},
                            boundary: { width: 360 , height: 360 },
                            showZoomer: true,
                            enableOrientation: true,
                            enableExif: true,
                            zoom: 0
                        });

                        // Bind the Croppie instance to the initial image
                        this.croppie.bind({
                            url: image
                        });


                        // Figyeli az összes zoom eseményt (csúszka + görgő + touch)
                        this.croppie.on('update', (data) => {
                            console.log("Zoom érték:", data.zoom);
                        });

                    };
                    reader.readAsDataURL(file);
                }
            },



            // Rotate left (90 degrees)
            rotateLeft() {
                this.croppie.rotate(-90); // Rotate left by 90 degrees
            },

            // Clear the image (unbind the current image)
            clearImage() {
                this.destroyCroppie();
            },

            // Get cropped image in base64 format
            getCroppedImage() {
                this.croppie.result({
                    type: 'base64', // Type of output, base64 in this case
                    size: { width: 300, height: 300 } // Resize the output image if needed
                }).then((croppedImage) => {
                    this.croppedImageBase64 = croppedImage; // Store the cropped image base64
                });
            }
        }
    };
</script>

<style scoped>

    .CroppieBox {
        border:1px #cccccc solid;
        background-color: #f2f2f2;
        height:350px;
        width:350px;
        position:relative;
    }

    .CroppieBox > .Actions {
        border:1px red solid;
        position:absolute;
        top:0px;
        left:0px;
        right:0px;
        height:30px;
        z-index: 2;
    }

    .CroppieBox > .Actions > .FirstIcon {
        text-shadow: 0px 0px 5px rgba(0, 0, 0, 1);
        color:white;
        font-size: 30px;
    }

    .CroppieBox > .Module {
        border:1px blue solid;
        position:absolute;
        top:0px;
        left:0px;
        right:0px;
        bottom:0px;
        z-index: 1;
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
