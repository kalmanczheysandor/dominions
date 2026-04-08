<template>
    <div>
        <div ref="croppieContainer"></div>
        <button @click="cropImage">Crop Image</button>
        <img v-if="croppedImage" :src="croppedImage" alt="Cropped Image" />
    </div>
</template>

<script>
    import Croppie from 'croppie';
    import 'croppie/croppie.css';

    export default {
        data() {
            return {
                croppieInstance: null,
                croppedImage: null,
            };
        },
        mounted() {
            this.croppieInstance = new Croppie(this.$refs.croppieContainer, {
                viewport: { width: 200, height: 200, type: 'square' },
                boundary: { width: 300, height: 300 },
                showZoomer: true,
            });

            // Például itt tudsz fájlt feltölteni, hogy megjelenjen a képen:
            this.loadImage('path/to/your/image.jpg');
        },
        methods: {
            loadImage(imagePath) {
                this.croppieInstance.bind({
                    url: imagePath,
                });
            },
            async cropImage() {
                const result = await this.croppieInstance.result({
                    type: 'base64', // Vagy 'blob', 'canvas', stb.
                    size: { width: 200, height: 200 },
                });
                this.croppedImage = result;
            },
        },
        beforeUnmount() {
            if (this.croppieInstance) {
                this.croppieInstance.destroy();
            }
        },
    };
</script>

<style scoped>
    /* Itt is beállíthatsz egyéni stílusokat */
</style>
