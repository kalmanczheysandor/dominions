<template>
    <v-container>



        <label>Kép</label>
        <div class="panel panel-default profile-container">
            <div id="DogAddDialog_Input_MainImageElement_Container" class="croppie-container" style="position:relative;">
                <div class="loader"></div>
                <img src="/image/noimage.png" class="profileimage" id="DogAddDialog_Input_MainImageElement_Img" style="width:200px;">
                <div class="cr-actions">
                    <button type="button" class="btn btn-default btn-sm rotateimage norightradius disabled" data-deg="90" data-toggle="tooltip" title="Rotate">
                        <i class="fas fa-redo-alt" style="color:#007bff"></i>
                    </button>
                    <button type="button" class="btn btn-default btn-sm defaultimage noleftradius norightradius disabled" data-toggle="tooltip" title="Empty">
                        <i class="fas fa-trash-alt" style="color:red"></i>
                    </button>
                    <label class="btn btn-default btn-sm noleftradius norightradius browsebtn" data-toggle="tooltip" title="Browse (.jpg, .jpeg)">
                        <i class="fas fa-folder" style="color:yellow"></i>
                        <input type="file" id="DogAddDialog_Input_MainImageElement_UploadButton" class="uploadbutton" name="profile_picture" accept=".jpg,.jpeg,.png">
                        <input type="hidden" id="DogAddDialog_Input_MainImageElement_HiddenImage" name="user[image]" value="">
                    </label>
                    <button type="button" class="btn btn-default btn-sm setimage noleftradius disabled" data-toggle="Finish" title="Finish">
                        <i class="fas fa-check" style="color:green;"></i>
                    </button>
                </div>
            </div>
        </div>







        <v-row>
            <v-col>
                <input type="file" @change="onFileChange" />
            </v-col>
            <v-col>
                <div ref="croppie" style="width: 300px; height: 300px;"></div>
            </v-col>
        </v-row>
        <v-btn @click="getCroppedImage">Get Cropped Image</v-btn>
        <v-img :src="croppedImage" v-if="croppedImage" />
    </v-container>
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
        methods: {
            onFileChange(event) {
                const file = event.target.files[0];
                if (file) {
                    const reader = new FileReader();
                    reader.onload = () => {
                        this.initializeCroppie(reader.result);
                    };
                    reader.readAsDataURL(file);
                }
            },
            initializeCroppie(imageUrl) {
                if (this.croppieInstance) {
                    this.croppieInstance.destroy();
                }
                this.croppieInstance = new Croppie(this.$refs.croppie, {
                    url: imageUrl,
                    viewport: { width: 200, height: 200, type: 'square' },
                    boundary: { width: 300, height: 300 },
                });
            },
            getCroppedImage() {
                this.croppieInstance
                .result({
                    type: 'base64',
                    size: 'viewport',
                })
                .then((croppedImage) => {
                    this.croppedImage = croppedImage;
                });
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

</style>
