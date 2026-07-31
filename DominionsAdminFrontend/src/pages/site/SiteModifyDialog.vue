<template>
    <TDialog ref="Dialog" title="Modify site" :closeable="true" @whenDialogClosed="whenParentDialogClosed">
        <v-form ref="Form" class="TTabbedPanel" v-model="Form.validation.isValid">
            <v-tabs v-model="MainTabComponent.activeTabKey" align-tabs="end" color="primary">
                <v-tab value="Tab1">*</v-tab>
            </v-tabs>
            <v-tabs-window v-model="MainTabComponent.activeTabKey">
                <v-tabs-window-item value="Tab1">
                    <v-text-field v-model="Form.Fields.NameTextInput.value" label="Name" :rules="Form.Fields.NameTextInput.rules"  variant="outlined" density="compact" clearable />
                    <v-text-field v-model="Form.Fields.AddressTextInput.value" label="Address" :rules="Form.Fields.AddressTextInput.rules"  variant="outlined" density="compact" clearable />
                    <QuillEditor v-model:content="Form.Fields.NoteEditorInput.content" contentType="html" style="height: 250px" :options="Form.Fields.NoteEditorInput.configuration"/>
                    <v-checkbox v-model="Form.Fields.EnabledCheckInput.isChecked" label="Enabled" variant="outlined" density="compact" clearable />

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
    import {siteService} from "@/services/site/SiteService.js";
    import {authService} from "@/services/auth/AuthService";
    import TConfirmDialog from "@/framework/component/TConfirmDialog/TConfirmDialog";
    import TController from "@/framework/TController";
    import {RequiredFieldRule, TextFieldRule} from "@/validation.js";
    import {QuillEditor} from "@vueup/vue-quill";


    export default {
        name: "SiteEditDialog",
        components: {TDialog,QuillEditor},
        data() {
            return {
                uuid: null,
                MainTabComponent: {
                    value: null
                },
                Form: {
                    Fields: {
                        NameTextInput: {
                            value: "",
                            rules: [RequiredFieldRule, TextFieldRule]
                        },
                        AddressTextInput: {
                            value: "",
                            rules: [RequiredFieldRule, TextFieldRule]
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
                            content: "",
                            rules: []
                        },
                        EnabledCheckInput: {
                            isChecked: true,
                            rules: []
                        },
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

                    // Form validation
                    await this.$refs.Form.validate();
                    if(!this.Form.validation.isValid) {
                        return;
                    }

                    // Save form value
                    await siteService.modify(this.uuid, {
                        name: this.Form.Fields.NameTextInput.value,
                        address: this.Form.Fields.AddressTextInput.value,
                        note: this.Form.Fields.NoteEditorInput.content,
                        enabled: this.Form.Fields.EnabledCheckInput.isChecked
                    });

                    // Display success message
                    TController.displayModifiedToast();

                    // Hide dialog
                    this.close();
                } catch(exp) {
                    await TController.handleExceptions(exp);
                }
            },
            async eventClickOnCancelButton() {
                this.close();
            },
            async open(uuid) {
                try {

                    // Checking permission
                    authService.assertEditActionGrantedOn("Site");

                    // Retrieve: primary record
                    const primaryResult = await siteService.accessByUuid(uuid);     // If no primary record is found at uuid than exception is thrown

                    // Retrieve: meta record(s)

                    // Inject: form meta values
                    this.uuid = uuid;

                    // Inject: form input values
                    this.Form.Fields.NameTextInput.value = primaryResult.data.name;
                    this.Form.Fields.AddressTextInput.value = primaryResult.data.address;
                    this.Form.Fields.NoteEditorInput.content = primaryResult.data.note;
                    this.Form.Fields.EnabledCheckInput.isChecked = primaryResult.data.enabled;

                    // Display dialog
                    this.$refs.Dialog.open();
                } catch(exp) {
                    await TController.handleExceptions(exp);
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
