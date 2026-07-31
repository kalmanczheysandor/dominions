<template>
    <TDialog ref="Dialog" title="New site permission group" :closeable="true" @whenDialogClosed="whenParentDialogClosed">
        <v-form ref="Form" class="TTabbedPanel" v-model="Form.validation.isValid">
            <v-tabs v-model="MainTabComponent.activeKey" align-tabs="end" color="primary">
                <v-tab value="Tab1">*</v-tab>
            </v-tabs>
            <v-tabs-window v-model="MainTabComponent.activeKey">
                <v-tabs-window-item value="Tab1">
                    <v-text-field v-model="Form.Fields.NameTextInput.value" label="Name"
                                  :rules="Form.Fields.NameTextInput.rules"
                                  variant="outlined" density="compact" clearable/>
                    <v-checkbox v-model="Form.Fields.EnabledCheckInput.isChecked" label="Enabled" variant="outlined"
                                density="compact" clearable/>
                    <TCheckboxMatrixInput v-model="Form.Fields.SitePermissionGroupCheckboxMatrixInput.selectedItems"
                                          :config="Form.Fields.SitePermissionGroupCheckboxMatrixInput.config"/>
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
import TConfirmDialog from "@/framework/component/TConfirmDialog/TConfirmDialog";
import {authService} from "@/services/auth/AuthService";
import TController from "@/framework/TController";
import {RequiredFieldRule, TextFieldRule} from "@/validation.js";
import sitePermissionGroupService from "@/services/account/site/permission/SitePermissionGroupService";
import TCheckboxMatrixInput from "@/framework/component/TCheckboxMatrixInput/TCheckboxMatrixInput.vue";

export default {
    name: "SitePermissionGroupAddDialog",
    components: {TCheckboxMatrixInput, TDialog},
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
                    EnabledCheckInput: {
                        isChecked: true,
                        rules: []
                    },
                    SitePermissionGroupCheckboxMatrixInput: {
                        config: {
                            columns: [
                                {Code: 'VIEW', Caption: 'Access'},
                                {Code: 'ADD', Caption: 'Add'},
                                {Code: 'EDIT', Caption: 'Edit'},
                                {Code: 'DELETE', Caption: 'Delete'}
                            ],
                            rows: [
                                {Field: 'Main', Caption: 'Main'},
                                {Field: 'Game.Create', Caption: 'Game > Create'},
                                {Field: 'Game.Lobby', Caption: 'Game > Lobby'},
                                {Field: 'Game.Play', Caption: 'Game > Play'},
                                {Field: 'Account.Settings', Caption: 'Account > Settings'},
                            ],
                        },
                        selectedItems: [],
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
        async eventClickOnSaveButton() {
            try {
                // Confirming
                const confirmed = await TConfirmDialog('Do you continue?')
                if (!confirmed.ok) {
                    return;
                }

                // Form validation
                await this.$refs.Form.validate();
                if (!this.Form.validation.isValid) {
                    return;
                }

                // Save form value
                const result = await sitePermissionGroupService.save({
                    name: this.Form.Fields.NameTextInput.value,
                    enabled: this.Form.Fields.EnabledCheckInput.isChecked,
                    permissions: this.Form.Fields.SitePermissionGroupCheckboxMatrixInput.selectedItems
                });

                // Display success message
                TController.displaySavedToast();

                // Hide dialog
                this.close();
            } catch (exp) {
                await TController.handleExceptions(exp);
            }
        },
        eventClickOnCancelButton() {
            this.close();
        },
        async open() {
            try {
                // Checking sitePermission
                authService.assertAddActionGrantedOn("Account.Site.PermissionGroup");

                // Display dialog
                await this.$refs.Dialog.open();
            } catch (exp) {
                await TController.handleExceptions(exp);
                this.close();
            }
        },
        close() {
            this.$refs.Dialog.close();
            if (this.$refs.Form) {
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
