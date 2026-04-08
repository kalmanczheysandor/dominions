<template>
    <div class="Image-Upload-wrapper Image-upload">
        <p>This is an image upload wrapper component</p>

        <!-- Hidden file input -->
        <input
            type="file"
            ref="fileInput"
            style="display: none;"
            @change="onFileChange"
            accept="image/*"
        />

        <!-- Croppie container -->
        <div id="croppie" @click="onImageClick"></div>

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
        mounted() {
            this.image = this.imgUrl; // initial image URL if provided
            this.setUpCroppie();
        },
        data() {
            return {
                image: null,
                croppie: null,
                imgUrl: 'http://localhost:8080/favicon.ico', // default image
                croppedImageBase64: null // To store the cropped image in base64
            };
        },
        methods: {
            setUpCroppie() {
                let el = document.getElementById('croppie');
                this.croppie = new Croppie(el, {
                    boundary: { width: 220, height: 220 },
                    showZoomer: true,
                    enableOrientation: true,
                    viewport: {
                        width: 300,
                        height: 300,
                        type: 'square' // You can also change this to 'circle' or other types
                    },
                    enableExif: true,
                    zoom: 0
                });

                // Bind the Croppie instance to the initial image
                this.croppie.bind({
                    url: this.image
                });
            },

            // Handle click only on the image area (viewport)
            onImageClick(event) {
                const viewport = this.$el.querySelector('.cr-viewport'); // Get the viewport (image area)

                // Check if the click is within the viewport (image area)
                if (viewport && viewport.contains(event.target)) {
                    this.$refs.fileInput.click(); // Trigger file input
                }
            },

            // Handle file selection and update Croppie
            onFileChange(event) {
                const file = event.target.files[0];
                if (file) {
                    const reader = new FileReader();
                    reader.onload = (e) => {
                        this.image = e.target.result; // Set the selected image
                        this.croppie.bind({
                            url: this.image // Bind the new image to Croppie
                        });
                    };
                    reader.readAsDataURL(file);
                }
            },

            // Zoom in
            zoomIn() {
                this.croppie.setZoom(this.croppie.getZoom() + 0.1); // Increase zoom by 10%
            },

            // Zoom out
            zoomOut() {
                this.croppie.setZoom(this.croppie.getZoom() - 0.1); // Decrease zoom by 10%
            },

            // Rotate left (90 degrees)
            rotateLeft() {
                this.croppie.rotate(-90); // Rotate left by 90 degrees
            },

            // Rotate right (90 degrees)
            rotateRight() {
                this.croppie.rotate(90); // Rotate right by 90 degrees
            },

            // Flip horizontally
            flipHorizontal() {
                this.croppie.flip('horizontal'); // Flip image horizontally
            },

            // Flip vertically
            flipVertical() {
                this.croppie.flip('vertical'); // Flip image vertically
            },

            // Clear the image (unbind the current image)
            clearImage() {
                this.croppie.bind({
                    url: '' // Clear the current image by passing an empty URL
                });
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
