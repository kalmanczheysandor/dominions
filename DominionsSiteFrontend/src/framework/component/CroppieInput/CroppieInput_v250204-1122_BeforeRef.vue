<template>
    <div class="Image-Upload-wrapper Image-upload">
        <p>This is an image upload wrapper component</p>

        <!-- Hidden file input -->
        <input type="file" ref="fileInput" style="display: none;" @change="eventFileSelected" accept="image/*"/>
        <div class="CroppieBox" @click.stop="eventBoxClicked">

            <div v-show="croppie || croppedImageBase64" class="Actions">
                <v-icon v-show="croppie || croppedImageBase64" class="Icon" @click.stop="eventTrashIconClicked">mdi-delete-outline</v-icon>
                <v-icon v-show="croppie && !croppedImageBase64" class="Icon" @click.stop="eventCropIconClicked">mdi-crop-free</v-icon>
                <v-icon v-show="croppie && !croppedImageBase64" class="Icon" @click.stop="eventRotateRightIconClicked">mdi-rotate-right</v-icon>
                <v-icon v-show="croppedImageBase64" class="Icon" @click.stop="eventEditIconClicked">mdi-image-edit-outline</v-icon>
            </div>
            <div v-show="croppie && !croppedImageBase64" class="Module" id="croppie" @click.stop="eventImageClicked"></div>
            <img v-show="croppedImageBase64" :src="croppedImageBase64" id="CroppedImage" alt="Cropped Image"/>


        </div>

        <p v-show="croppie ">A)</p>
        <p v-show="!croppedImageBase64 ">b)</p>

        <p v-show="croppie && !croppedImageBase64">mutasd a croppiet</p>


        <!-- Croppie container -->

        <!-- Controls for zoom, rotate, flip, and reset -->
        <div class="controls">
            <button @click="eventResetClicked">reset</button>
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
                if(!this.croppie && !this.croppedImageBase64) {
                    this.$refs.fileInput.click(); // Trigger file input
                }
            },
            eventImageClicked(event) {
                // const viewport = this.$el.querySelector('.cr-overlay'); // Get the viewport (image area)
                // if(viewport && viewport.contains(event.target)) {
                //     this.$refs.fileInput.click(); // Trigger file input
                // }
            },
            eventFileSelected(event) {
                const file = event.target.files[0];

                if(file) {

                    this.$refs.fileInput.value = "";
                    // Destroy the current Croppie instance
                    this.doFlush();

                    const reader = new FileReader();
                    reader.onload = (e) => {
                        let image = e.target.result; // Set the selected image
                        this.image=image;
                        // let imageObj = new Image();
                        // imageObj.src = e.target.result;




                        // if (this.croppie!==null) {
                        //     //this.image='http://localhost:8080/favicon.ico',
                        //     this.croppie.destroy();
                        //     this.croppie=null;
                        // }
                        // this.croppedImageBase64=null;
                        //
                        // // Clear the Croppie container
                        // const croppieEl = document.getElementById('croppie');
                        // croppieEl.innerHTML = ''; // Clear current content


                        let el = document.getElementById('croppie');
                        this.croppie = new Croppie(el, {
                            viewport: {width: 300, height: 300, type: 'square'},
                            boundary: {width: 360, height: 360},
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
                        }).then((canvas) => {
                            // // Automatikusan beállítja a zoomot, hogy a kép pontosan illeszkedjen a viewportba
                            // //this.fitImageToViewport();
                            //
                            //
                            // const viewportWidth = 300; // A vágási terület szélessége
                            // const viewportHeight = 300; // A vágási terület magassága
                            //
                            // const imgWidth = imageObj.width;
                            // const imgHeight = imageObj.height;
                            //
                            // // Kiszámítjuk a szükséges zoom mértékét (legkisebb illeszkedő érték)
                            // const zoomRatio = Math.min(viewportWidth / imgWidth, viewportHeight / imgHeight);
                            //
                            // // Beállítjuk a megfelelő zoomot
                            // alert(viewportWidth+"px/"+imgWidth+"px");
                            // alert(zoomRatio);
                            // this.croppie.setZoom(-0.5);


                        });


                        // // Figyeli az összes zoom eseményt (csúszka + görgő + touch)
                        // this.croppie.on('update', (data) => {
                        //     console.log("Zoom érték:", data.zoom);
                        // });

                    };
                    reader.readAsDataURL(file);
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
            eventRotateRightIconClicked(){
                this.croppie.rotate(-90);
            },











            eventResetClicked() {
                this.croppie.setZoom(0);
            },

            fitImageToViewport() {
                this.croppie.result({
                    type: 'rawcanvas'
                }).then((canvas) => {
                    const viewportWidth = 300; // A vágási terület szélessége
                    const viewportHeight = 300; // A vágási terület magassága

                    const imgWidth = canvas.width;
                    const imgHeight = canvas.height;

                    // Kiszámítjuk a szükséges zoom mértékét (legkisebb illeszkedő érték)
                    const zoomRatio = Math.min(viewportWidth / imgWidth, viewportHeight / imgHeight);

                    // Beállítjuk a megfelelő zoomot
                    alert(viewportWidth + "px/" + imgWidth + "px");
                    this.croppie.setZoom(zoomRatio);
                });
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
            doCropImage() {
                this.croppie.result({
                    type: 'base64', // Type of output, base64 in this case
                    size: {width: 300, height: 300} // Resize the output image if needed
                }).then((croppedImage) => {
                    this.croppedImageBase64 = croppedImage; // Store the cropped image base64
                });
            },

            doFlush() {
                // Destroy the current Croppie instance
                if(this.croppie !== null) {
                    //this.image='http://localhost:8080/favicon.ico',
                    this.croppie.destroy();
                    this.croppie = null;
                }
                this.croppedImageBase64 = null;

                // Clear the Croppie container
                const croppieEl = document.getElementById('croppie');
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

    .CroppieBox > #CroppedImage {
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

    .croppie-container .cr-slider-wrap  {
        position:absolute;
        bottom:0px;
        left:0px;
        right:0px;
        z-index: 1;
        width: 100% !important;
        margin:0px !important;
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
