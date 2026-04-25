<template>
    <TDialog ref="Dialog" title="Modify dog" :closeable="true" @whenDialogClosed="whenParentDialogClosed">


        <v-tabs v-model="MainTabComponent.value" align-tabs="end" color="primary">
            <v-tab value="zero">Test</v-tab>
            <v-tab value="one">Uploader</v-tab>
            <v-tab value="two">Content</v-tab>
        </v-tabs>

        <v-tabs-window v-model="MainTabComponent.value">
            <v-tabs-window-item value="zero">
                <div style="height:500px;width:300px; background-color: #007bff;">
                    hello
                </div>
            </v-tabs-window-item>
            <v-tabs-window-item value="one">
                <TUploadZone :config="config"
                             :target="'http://localhost:8081/data/dog/'+uuid+'/gallery/upload'"
                             @whenUploadFinished="eventUploadFinished"
                />
            </v-tabs-window-item>
            <v-tabs-window-item value="two">
                <TPhotoGrid v-model="PhotoGridComponent.items"
                            @whenItemClicked="eventPhotoItemClicked"
                            @whenItemIsSelectedToDelete="eventPhotoItemDeleteClicked"

                />
            </v-tabs-window-item>
        </v-tabs-window>


    </TDialog>
</template>

<script>
    import TDialog from "@/framework/component/TDialog/TDialog.vue";
    import * as ValidationModule from '@/validation.js';
    import {dogService} from "@/services/dog/DogService.js";
    import {authService} from "@/services/auth/AuthService";
    import TConfirmDialog from "@/framework/component/TConfirmDialog/TConfirmDialog";
    import {useToast} from "vue-toastification";
    import TController from "@/framework/TController";
    import CroppieInput from "@/framework/component/CroppieInput/CroppieInput.vue";
    import TUploadZone from "@/framework/component/TUploadZone/TUploadZone.vue";
    import TPhotoGrid from "@/framework/component/TPhotoGrid/TPhotoGrid.vue";

    export default {
        name: "DogEditDialog",
        components: {TDialog, TUploadZone, TPhotoGrid},
        data() {
            return {
                uuid: null,
                MainTabComponent: {
                    value: null
                },
                config: {
                    width: 500,
                    height: 250
                },
                PhotoGridComponent: {
                    items: []
                }

            };
        },
        emits: ["whenDialogClosed"],
        methods: {
            async loadImageList() {
                try {
                    // List all items of gallery
                    const result = await dogService.listGallery(this.uuid);

                    // Transform items
                    let items = [];
                    if(result.data) {
                        items = result.data.map(item => {
                            return {
                                key: item.uuid + '-' + item.filename,
                                imageSrc: 'http://localhost:8081/data/dog/' + item.uuid + '/gallery/' + item.filename,
                                uuid: item.uuid,
                                filename: item.filename
                            };
                        });
                    }

                    // Inject values
                    this.PhotoGridComponent.items = items;
                } catch(exp) {
                    await TController.displayExceptionMessages(exp);
                }
            },

            eventPhotoItemClicked(photoItem) {
            },

            eventUploadFinished() {
                this.loadImageList();
            },

            async eventPhotoItemDeleteClicked(photoItem) {
                try {

                    // Confirming
                    const confirmed = await TConfirmDialog('Do you wish to delete the selected item?');
                    if(!confirmed.ok) {
                        return;
                    }

                    // Delete record on backend
                    const result = await dogService.deleteGalleryItem(photoItem.uuid, photoItem.filename);

                    // Display success message
                    const toast = useToast();
                    toast.success('Deleted!');

                } catch(exp) {
                    await TController.displayExceptionMessages(exp);
                }

                // Refresh list content
                await this.loadImageList();
            },

            async open(uuid) {
                try {

                    // Checking permission
                    authService.assertEditActionGrantedOn("Dog");

                    // Attempt to retrieve data
                    const result = await dogService.accessByUuid(uuid);

                    // Inject data
                    this.uuid = uuid;

                    // Display dialog
                    await this.$refs.Dialog.open();

                    // Load content
                    await this.loadImageList();
                } catch(exp) {
                    await TController.displayExceptionMessages(exp);
                    this.close();
                }
            },
            close() {
                this.$refs.Dialog.close();

                // Propagate event
                this.$emit("whenDialogClosed");
            },
            whenParentDialogClosed() {
                this.close();
            },

        },
    };
</script>

<!--<style>-->
<!--    .GalleryImage {-->
<!--        border: 1px red solid;-->
<!--    }-->

<!--    .GalleryImage .Actions {-->
<!--        position: absolute;-->
<!--        top: 0px;-->
<!--        left: 0px;-->
<!--        right: 0px;-->
<!--        height: 30px;-->
<!--        z-index: 2;-->
<!--    }-->

<!--    .GalleryImage .Actions > .Icon {-->
<!--        text-shadow: 0px 0px 2px black;-->
<!--        text-shadow: #000 0px 0px 1px, #000 0px 0px 1px, #000 0px 0px 1px, #000 0px 0px 1px, #000 0px 0px 1px, #000 0px 0px 1px;-->
<!--        color: white;-->
<!--        font-size: 30px;-->
<!--        opacity: 0.2;-->
<!--    }-->

<!--    .GalleryImage .Actions > .Icon:hover {-->
<!--        text-shadow: 0px 0px 2px black;-->
<!--        color: white;-->
<!--        font-size: 30px;-->
<!--        cursor: pointer;-->
<!--        opacity: 1;-->
<!--    }-->
<!--</style>-->
