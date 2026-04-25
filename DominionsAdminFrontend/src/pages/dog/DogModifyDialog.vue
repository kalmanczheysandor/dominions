<template>
    <TDialog ref="Dialog" title="Modify dog" :closeable="true" @whenDialogClosed="whenParentDialogClosed">
        <v-form ref="Form" class="TTabbedPanel" v-model="Form.validation.isValid">
            <v-tabs v-model="MainTabComponent.activeTabKey" align-tabs="end" color="primary">
                <v-tab value="Tab1">*</v-tab>
                <v-tab value="Tab2">Details</v-tab>
            </v-tabs>
            <v-tabs-window v-model="MainTabComponent.activeTabKey">
                <v-tabs-window-item value="Tab1">

                    <v-row>
                        <v-col>
                            <CroppieInput ref="DogImage" v-model="Form.Fields.DogImageCroppieInput.base64Content" :rules="Form.Fields.DogImageCroppieInput.rules"/>
                        </v-col>
                        <v-col>
                            <v-text-field v-model="Form.Fields.NameTextInput.value" label="Name" :rules="Form.Fields.NameTextInput.rules" variant="outlined" density="compact" clearable/>
                            <v-text-field v-model="Form.Fields.PrnTextInput.value" label="Prn" :rules="Form.Fields.PrnTextInput.rules" variant="outlined" density="compact" clearable/>
                            <v-checkbox v-model="Form.Fields.EnabledCheckInput.isChecked" label="Enabled" variant="outlined" density="compact" clearable/>
                            <v-autocomplete
                                v-model="Form.Fields.BreedSelectInput.selectedKey"
                                :rules="Form.Fields.BreedSelectInput.rules"
                                :items="Form.Fields.BreedSelectInput.itemsToDisplay"
                                label="Select Item" item-value="uuid" item-title="name"
                                :search-input="Form.Fields.BreedSelectInput.searchText"
                                variant="outlined" density="compact" clearable dense
                            />
                            <v-autocomplete
                                v-model="Form.Fields.SiteSelectInput.selectedKey"
                                :rules="Form.Fields.SiteSelectInput.rules"
                                :items="Form.Fields.SiteSelectInput.itemsToDisplay"
                                label="Select Item" item-value="uuid" item-title="name"
                                :search-input="Form.Fields.SiteSelectInput.searchText"
                                variant="outlined" density="compact" clearable dense
                            />
                        </v-col>
                    </v-row>
                </v-tabs-window-item>
                <v-tabs-window-item value="Tab2">
                    <v-row>
                        <v-col>
                            <QuillEditor v-model:content="Form.Fields.NoteEditorInput.content" contentType="html" style="height: 250px"/>
                        </v-col>
                    </v-row>
                </v-tabs-window-item>
            </v-tabs-window>
            <div class="ActionButtonBar">
                <button type="button" class="TButton CancelButton" @click="eventClickOnCancelButton">Cancel</button>
                <button type="button" class="TButton ModifyButton" @click="eventClickOnModifyButton">Modify</button>
            </div>
        </v-form>
    </TDialog>
</template>

<script>
    import TDialog from "@/framework/component/TDialog/TDialog.vue";
    import {dogService} from "@/services/dog/DogService.js";
    import {authService} from "@/services/auth/AuthService";
    import TConfirmDialog from "@/framework/component/TConfirmDialog/TConfirmDialog";
    import TController from "@/framework/TController";
    import CroppieInput from "@/framework/component/CroppieInput/CroppieInput.vue";
    import {QuillEditor} from "@vueup/vue-quill";
    import {RequiredFieldRule, TextFieldRule} from "@/validation.js";
    import siteService from "@/services/site/SiteService";


    export default {
        name: "DogEditDialog",
        components: {CroppieInput, TDialog, QuillEditor},
        data() {
            return {
                uuid: null,
                MainTabComponent: {
                    value: null
                },
                Form: {
                    Fields: {
                        DogImageCroppieInput: {
                            base64Content: "",
                            rules: [RequiredFieldRule]
                        },
                        NameTextInput: {
                            value: "",
                            rules: [RequiredFieldRule, TextFieldRule]
                        },
                        PrnTextInput: {
                            value: "",
                            rules: [RequiredFieldRule, TextFieldRule]
                        },
                        EnabledCheckInput: {
                            isChecked: true,
                            rules: []
                        },
                        BreedSelectInput: {
                            itemsToDisplay: [],
                            searchText: '',
                            selectedKey: null,
                            rules: [RequiredFieldRule]
                        },
                        SiteSelectInput: {
                            itemsToDisplay: [],
                            searchText: '',
                            selectedKey: null,
                            rules: [RequiredFieldRule]
                        },
                        NoteEditorInput: {
                            configuration: {
                                debug: 'info',
                                modules: {
                                    toolbar: [
                                        [{header: [1, 2, false]}],
                                        ['bold', 'italic', 'underline'],
                                        ['image', 'code-block'],
                                    ]
                                },
                                placeholder: 'Content...',
                                readOnly: false,
                                theme: 'snow'
                            },
                            content: '',
                            rules: []
                        }
                    },
                    validation: {
                        isValid: false,
                    }
                },
            };
        },
        emits: ["whenDialogClosed"],
        methods: {
            async reset() {
                this.$refs.Form.reset();
                this.uuid = null;
            },
            async eventClickOnModifyButton() {
                try {

                    // Confirming
                    const confirmed = await TConfirmDialog('Do you continue?')
                    if(!confirmed.ok) {
                        return;
                    }

                    // Croppie validation
                    if(!this.$refs.DogImage.validate()) {
                        return;
                    }

                    // Form validation
                    await this.$refs.Form.validate();
                    if(!this.Form.validation.isValid) {
                        return;
                    }

                    // Save form value
                    await dogService.modify(this.uuid, {
                        name: this.Form.Fields.NameTextInput.value,
                        prn: this.Form.Fields.PrnTextInput.value,
                        enabled: this.Form.Fields.EnabledCheckInput.isChecked,
                        breedUuid: this.Form.Fields.BreedSelectInput.selectedKey,
                        siteUuid: this.Form.Fields.SiteSelectInput.selectedKey,
                        note: this.Form.Fields.NoteEditorInput.content,
                        imageBase64: this.Form.Fields.DogImageCroppieInput.base64Content
                    });

                    // Display success message
                    TController.displayModifiedToast();

                    // Hide dialog
                    this.close();
                } catch(exp) {
                    await TController.displayExceptionMessages(exp);
                }
            },
            async eventClickOnCancelButton() {
                this.close();
            },
            async open(uuid) {
                try {

                    // Checking permission
                    authService.assertEditActionGrantedOn("Dog");

                    // Retrieve: primary record
                    const primaryResult = await dogService.accessByUuid(uuid);     // If no primary record is found at uuid than exception is thrown

                    // Retrieve: meta record(s)
                    const breedResult = await dogService.atEditListBreed(uuid);
                    const siteResult = await dogService.atEditListSite(uuid);

                    // Inject: form meta values
                    this.uuid = uuid;
                    this.Form.Fields.BreedSelectInput.itemsToDisplay = breedResult.data;
                    this.Form.Fields.SiteSelectInput.itemsToDisplay = siteResult.data;

                    // Inject: form input values
                    this.Form.Fields.NameTextInput.value = primaryResult.data.name;
                    this.Form.Fields.PrnTextInput.value = primaryResult.data.prn;
                    this.Form.Fields.EnabledCheckInput.isChecked = primaryResult.data.enabled;
                    this.Form.Fields.NoteEditorInput.content = primaryResult.data.note;
                    this.Form.Fields.BreedSelectInput.selectedKey = primaryResult.data.breedUuid;
                    this.Form.Fields.SiteSelectInput.selectedKey = primaryResult.data.siteUuid;

                    // Display dialog
                    await this.$refs.Dialog.open();
                    await this.$refs.DogImage.openFile('http://localhost:8081/data/dog/' + uuid + '/image/main');
                } catch(exp) {
                    await TController.displayExceptionMessages(exp);
                    this.close();
                }
            },
            close() {
                this.$refs.Dialog.close();
                if(this.$refs.Form) {
                    this.$refs.Form.reset();
                }

                // Propagate event
                this.$emit("whenDialogClosed");
            },
            whenParentDialogClosed() {
                this.close();
            },
        },
    };
</script>
