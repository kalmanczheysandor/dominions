<template>
    <TDialog ref="Dialog" title="Modify sitePermissionGroup" :closeable="true" @whenDialogClosed="whenParentDialogClosed">
        <v-form ref="Form" class="TTabbedPanel" v-model="Form.validation.isValid">
            <v-tabs v-model="MainTabComponent.activeTabKey" align-tabs="end" color="primary">
                <v-tab value="Tab1">*</v-tab>
            </v-tabs>
            <v-tabs-window v-model="MainTabComponent.activeTabKey">
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
                <button type="button" class="TButton ModifyButton" @click="eventClickOnModifyButton">Modify</button>
            </div>
        </v-form>
    </TDialog>
</template>

<script>
import TDialog from "@/framework/component/TDialog/TDialog.vue";
import {authService} from "@/services/auth/AuthService";
import TConfirmDialog from "@/framework/component/TConfirmDialog/TConfirmDialog";
import TController from "@/framework/TController";
import {RequiredFieldRule, TextFieldRule} from "@/validation.js";
import sitePermissionGroupService from "@/services/account/site/permission/SitePermissionGroupService";
import TCheckboxMatrixInput from "@/framework/component/TCheckboxMatrixInput/TCheckboxMatrixInput.vue";


export default {
    name: "SitePermissionGroupEditDialog",
    components: {TCheckboxMatrixInput, TDialog},
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
                                {Field: 'Profile', Caption: 'Profile'},
                                {Field: 'Game.Scenario', Caption: 'Game > Scenario'},
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
        async reset() {
            this.$refs.Form.reset();
            this.uuid = null;
        },
        async eventClickOnModifyButton() {
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
                const result = await sitePermissionGroupService.modify(this.uuid, {
                    name: this.Form.Fields.NameTextInput.value,
                    enabled: this.Form.Fields.EnabledCheckInput.isChecked,
                    permissions: this.Form.Fields.SitePermissionGroupCheckboxMatrixInput.selectedItems
                });

                // Display success message
                TController.displayModifiedToast();

                // Hide dialog
                this.close();
            } catch (exp) {
                await TController.displayExceptionMessages(exp);
            }
        },
        async eventClickOnCancelButton() {
            this.close();
        },
        async open(uuid) {
            try {

                // Checking sitePermission
                authService.assertEditActionGrantedOn("Account.Site.PermissionGroup");

                // Retrieve: primary record
                const primaryResult = await sitePermissionGroupService.accessByUuid(uuid);     // If no primary record is found at uuid than exception is thrown

                // Retrieve: meta record(s)

                // Inject: form meta values
                this.uuid = uuid;

                // Inject: form input values
                this.Form.Fields.NameTextInput.value = primaryResult.data.name;
                this.Form.Fields.EnabledCheckInput.isChecked = primaryResult.data.enabled;
                this.Form.Fields.SitePermissionGroupCheckboxMatrixInput.selectedItems = primaryResult.data.permissions;

                // Display dialog
                this.$refs.Dialog.open();
            } catch (exp) {
                await TController.displayExceptionMessages(exp);
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
        },

    },
};
</script>
