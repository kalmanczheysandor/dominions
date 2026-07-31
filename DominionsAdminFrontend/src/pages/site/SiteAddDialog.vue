<template>
    <TDialog ref="Dialog" title="New site" :closeable="true" @whenDialogClosed="whenParentDialogClosed">
        <v-form ref="Form" class="TTabbedPanel" v-model="Form.validation.isValid">
            <v-tabs v-model="MainTabComponent.activeKey" align-tabs="end" color="primary">
                <v-tab value="Tab1">*</v-tab>
            </v-tabs>
            <v-tabs-window v-model="MainTabComponent.activeKey">
                <v-tabs-window-item value="Tab1">
                    <v-text-field v-model="Form.Fields.NameTextInput.value" label="Name" :rules="Form.Fields.NameTextInput.rules"  variant="outlined" density="compact" clearable />
                    <v-text-field v-model="Form.Fields.AddressTextInput.value" label="Address" :rules="Form.Fields.AddressTextInput.rules"  variant="outlined" density="compact" clearable />
                    <QuillEditor v-model:content="Form.Fields.NoteEditorInput.content" contentType="html" style="height: 250px" :options="Form.Fields.NoteEditorInput.configuration"/>
                    <v-checkbox v-model="Form.Fields.EnabledCheckInput.isChecked" label="Enabled" variant="outlined" density="compact" clearable />

                </v-tabs-window-item>
            </v-tabs-window>

            <div class="ActionButtonBar">
                <button type="button" class="TButton CancelButton" @click="eventClickOnCancelButton">Cancel</button>
                <button type="button" class="TButton ModifyButton" @click="eventClickOnSaveButton">Save</button>
            </div>
        </v-form>
    </TDialog>
</template>

<script>
    import TDialog from "@/framework/component/TDialog/TDialog.vue";
    import {siteService} from "@/services/site/SiteService.js";
    import TConfirmDialog from "@/framework/component/TConfirmDialog/TConfirmDialog";
    import {authService} from "@/services/auth/AuthService";
    import TController from "@/framework/TController";
    import {RequiredFieldRule, TextFieldRule} from "@/validation.js";
    import {QuillEditor} from "@vueup/vue-quill";

    export default {
        name: "SiteAddDialog",
        components: {TDialog,QuillEditor},
        data() {
            return {
                MainTabComponent: {
                    activeTabKey: null,
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
            async eventClickOnSaveButton() {
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
                    await siteService.save({
                        name: this.Form.Fields.NameTextInput.value,
                        address: this.Form.Fields.AddressTextInput.value,
                        note: this.Form.Fields.NoteEditorInput.content,
                        enabled: this.Form.Fields.EnabledCheckInput.isChecked
                    });

                    // Display success message
                    TController.displaySavedToast();

                    // Hide dialog
                    this.close();
                } catch(exp) {
                    await TController.handleExceptions(exp);
                }
            },
            eventClickOnCancelButton() {
                this.close();
            },
            async open() {
                try {
                    // Checking permission
                    authService.assertAddActionGrantedOn("Site");

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
            }
        },
    };
</script>
