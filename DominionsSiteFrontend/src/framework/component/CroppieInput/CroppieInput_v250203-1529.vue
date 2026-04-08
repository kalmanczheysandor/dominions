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
            @click="triggerFileInput"
        ></div>
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

                    boundary: { width: 220, height: 220 },
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

            // Trigger file input click
            triggerFileInput() {
                this.$refs.fileInput.click();
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
        }
    };
</script>
