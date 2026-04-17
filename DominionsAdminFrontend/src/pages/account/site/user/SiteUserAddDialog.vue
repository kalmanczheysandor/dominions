<template>
    <TDialog ref="Dialog" title="New Site-User" :closeable="true" @whenDialogClosed="whenParentDialogClosed">
        <v-form ref="Form" class="TTabbedPanel" v-model="Form.validation.isValid">
            <v-tabs v-model="MainTabComponent.activeKey" align-tabs="end" color="primary">
                <v-tab value="Tab1">*</v-tab>
            </v-tabs>
            <v-tabs-window v-model="MainTabComponent.activeKey">
                <v-tabs-window-item value="Tab1">
                    <v-row>
                        <v-col>
                            <CroppieInput ref="PhotoCroppieInput" v-model="Form.Fields.PhotoCroppieInput.base64Content"
                                          :rules="Form.Fields.PhotoCroppieInput.rules"
                            />
                        </v-col>
                        <v-col>
                            <v-text-field v-model="Form.Fields.IdentifierEmailInput.value" label="Identifier"
                                          :rules="Form.Fields.IdentifierEmailInput.rules" variant="outlined"
                                          density="compact"
                                          clearable type="email"
                                          style="width:320px;"
                            />

                            <v-text-field v-model="Form.Fields.PasswordInput.value" label="Password"
                                          :rules="Form.Fields.PasswordInput.rules" variant="outlined" density="compact"
                                          clearable type="password"
                                          style="width:320px;"
                            />
                            <v-text-field v-model="Form.Fields.PasswordAgainInput.value" label="Password again"
                                          :rules="Form.Fields.PasswordAgainInput.rules" variant="outlined"
                                          density="compact" clearable type="password"
                                          style="width:320px;"
                            />
                            <v-row>
                                <v-col>
                                    <v-checkbox v-model="Form.Fields.FinalisedCheckInput.isChecked" label="Finalised"
                                                variant="outlined"
                                                density="compact" clearable
                                    />
                                </v-col>
                                <v-col>
                                    <v-checkbox v-model="Form.Fields.EnabledCheckInput.isChecked" label="Enabled"
                                                variant="outlined"
                                                density="compact" clearable
                                    />
                                </v-col>
                            </v-row>
                        </v-col>
                    </v-row>
                    <v-text-field v-model="Form.Fields.NameTextInput.value" label="Name"
                                  :rules="Form.Fields.NameTextInput.rules" variant="outlined" density="compact"
                                  clearable/>

                    <v-autocomplete
                            v-model="Form.Fields.PermissionGroupSelectInput.selectedKeys"
                            :rules="Form.Fields.PermissionGroupSelectInput.rules"
                            :items="Form.Fields.PermissionGroupSelectInput.itemsToDisplay"
                            label="Select Item" item-value="uuid" item-title="name"
                            :search-input="Form.Fields.PermissionGroupSelectInput.searchText"
                            variant="outlined" density="compact"
                            clearable
                            dense
                            chips
                            multiple
                    />

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
import {EmailFieldRule, PasswordFieldRule, RequiredFieldRule, TextFieldRule} from "@/validation.js";
import siteUserService from "@/services/account/site/user/SiteUserService";
import CroppieInput from "@/framework/component/CroppieInput/CroppieInput.vue";

export default {
    name: "SiteUserAddDialog",
    components: {CroppieInput, TDialog},
    data() {
        return {

            MainTabComponent: {
                activeTabKey: null,
            },
            Form: {
                Fields: {
                    PhotoCroppieInput: {
                        base64Content: "",
                        rules: []
                    },
                    IdentifierEmailInput: {
                        value: "",
                        rules: [RequiredFieldRule, EmailFieldRule]
                    },
                    NameTextInput: {
                        value: "",
                        rules: [RequiredFieldRule, TextFieldRule]
                    },

                    PasswordInput: {
                        value: "",
                        rules: [RequiredFieldRule, PasswordFieldRule]
                    },
                    PasswordAgainInput: {
                        value: "",
                        rules: [RequiredFieldRule, PasswordFieldRule]
                    },
                    PermissionGroupSelectInput: {
                        itemsToDisplay: [],
                        searchText: '',
                        selectedKeys: [],
                        rules: [RequiredFieldRule]
                    },
                    FinalisedCheckInput: {
                        isChecked: true,
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
                if (!confirmed.ok) {
                    return;
                }

                // Form validation
                await this.$refs.Form.validate();
                if (!this.Form.validation.isValid) {
                    return;
                }

                // Save form value
                await siteUserService.save({
                    identifier: this.Form.Fields.IdentifierEmailInput.value,
                    password: this.Form.Fields.PasswordInput.value,
                    confirmPassword: this.Form.Fields.PasswordAgainInput.value,
                    name: this.Form.Fields.NameTextInput.value,
                    finalised: this.Form.Fields.FinalisedCheckInput.isChecked,
                    enabled: this.Form.Fields.EnabledCheckInput.isChecked,
                    permissionGroups: this.Form.Fields.PermissionGroupSelectInput.selectedKeys,
                    imageBase64: this.Form.Fields.PhotoCroppieInput.base64Content
                });

                // Display success message
                TController.displaySavedToast();

                // Hide dialog
                this.close();
            } catch (exp) {
                await TController.displayExceptionMessages(exp);
            }
        },
        eventClickOnCancelButton() {
            this.close();
        },
        async open() {
            try {
                // Checking permission
                authService.assertAddActionGrantedOn("Account.Site.User");

                // Attempt to retrieve data
                const permissionGroupResult = await siteUserService.atAddListPermissionGroup();
                this.Form.Fields.PermissionGroupSelectInput.itemsToDisplay = permissionGroupResult.data;

                // Display dialog
                await this.$refs.Dialog.open();
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
        }
    },
};
</script>
