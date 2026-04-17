<template>
    <div class="Image-Upload-wrapper Image-upload">
        <p>
            This is an image upload wrapper component
        </p>
        <!-- Hidden file input -->
        <input
            type="file"
            ref="fileInput"
            style="display: none;"
            @change="onFileChange"
            accept="image/*"
        />

        <!-- Croppie container -->
        <div
            id="croppie"
            @click="onImageClick"
        ></div>

        <div class="controls">

            <button @click="rotateLeft">Rotate Left</button>
            <button @click="rotateRight">Rotate Right</button>
            <button @click="flipHorizontal">Flip Horizontal</button>
            <button @click="flipVertical">Flip Vertical</button>
            <button @click="flush">Flush</button>
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
                imgUrl: 'http://localhost:8080/favicon.ico' // default image
            };
        },
        methods: {
            setUpCroppie() {
                let el = document.getElementById('croppie');
                this.croppie = new Croppie(el, {
                    boundary: { width: 350, height: 350 },
                    showZoomer: true,
                    enableOrientation: true,
                    viewport: {
                        width: 300,
                        height: 300,
                        type: 'square'
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
                const viewport = this.$el.querySelector('.cr-overlay'); // Get the viewport (image area)

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
                            url: this.image // Update Croppie with new image
                        });
                    };
                    reader.readAsDataURL(file);
                }
            }
            ,
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

            // Flush (update the Croppie viewport)
            flush() {
                this.croppie.bind({
                    url: '' // Clear the current image by passing an empty URL
                });
            }
        }
    };
</script>
