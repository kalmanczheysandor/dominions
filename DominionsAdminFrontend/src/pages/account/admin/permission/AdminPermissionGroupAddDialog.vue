<template>
    <TDialog ref="Dialog" title="New admin permission group" :closeable="true" @whenDialogClosed="whenParentDialogClosed">
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
                    <TCheckboxMatrixInput v-model="Form.Fields.AdminPermissionGroupCheckboxMatrixInput.selectedItems"
                                          :config="Form.Fields.AdminPermissionGroupCheckboxMatrixInput.config"/>
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
import adminPermissionGroupService from "@/services/account/admin/permission/AdminPermissionGroupService";
import TCheckboxMatrixInput from "@/framework/component/TCheckboxMatrixInput/TCheckboxMatrixInput.vue";

export default {
    name: "AdminPermissionGroupAddDialog",
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
                    AdminPermissionGroupCheckboxMatrixInput: {
                        config: {
                            columns: [
                                {Code: 'VIEW', Caption: 'Access'},
                                {Code: 'ADD', Caption: 'Add'},
                                {Code: 'EDIT', Caption: 'Edit'},
                                {Code: 'DELETE', Caption: 'Delete'}
                            ],
                            rows: [
                                {Field: 'Ai.Hugo.Character', Caption: 'AI > Hugo > Character'},
                                {Field: 'Ai.Hugo.Variant', Caption: 'AI > Hugo > Variant'},
                                {Field: 'Ai.Liz.Character', Caption: 'AI > Liz > Character'},
                                {Field: 'Ai.Liz.Variant', Caption: 'AI > Liz > Variant'},
                                {Field: 'Ai.Liz.Concept', Caption: 'AI > Liz > Concept'},
                                {Field: 'Game.Scenario', Caption: 'Game > Scenario'},
                                {Field: 'Account.Admin.User', Caption: 'Account > Admin > Users'},
                                {Field: 'Account.Admin.PermissionGroup', Caption: 'Account > Admin > PermissionGroups'},
                                {Field: 'Account.Admin.Settings', Caption: 'Account > Admin > Settings'},
                                {Field: 'Account.Site.User', Caption: 'Account > Site > Users'},
                                {Field: 'Account.Site.PermissionGroup', Caption: 'Account > Site > PermissionGroups'},
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
                const result = await adminPermissionGroupService.save({
                    name: this.Form.Fields.NameTextInput.value,
                    enabled: this.Form.Fields.EnabledCheckInput.isChecked,
                    permissions: this.Form.Fields.AdminPermissionGroupCheckboxMatrixInput.selectedItems
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
                // Checking adminPermission
                authService.assertAddActionGrantedOn("Account.Admin.PermissionGroup");

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
