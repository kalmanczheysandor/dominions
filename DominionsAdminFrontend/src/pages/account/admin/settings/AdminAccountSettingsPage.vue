<template>
    <v-card flat class="TPanel">
        <div class="TVerticalTabbedPanel ">
            <v-tabs v-model="MainTabComponent.activeTabKey" color="primary" direction="vertical">
                <v-tab prepend-icon="mdi-form-textbox-password" text="Credentials" value="CredentialsTab"></v-tab>
                <v-tab prepend-icon="mdi-account" text="Profile" @click.once="initProfileTab" value="ProfileTab"></v-tab>
            </v-tabs>
            <v-tabs-window v-model="MainTabComponent.activeTabKey">
                <v-tabs-window-item value="CredentialsTab">
                    <v-row>
                        <v-col>

                                <v-form ref="CredentialsForm" v-model="CredentialsForm.validation.isValid"
                                        class="TSimpleForm" style="min-width:400px;">


                                    <v-text-field v-model="CredentialsForm.Fields.IdentifierEmailInput.value"
                                                  label="Identifier"
                                                  :rules="CredentialsForm.Fields.IdentifierEmailInput.rules"
                                                  variant="outlined" density="compact" clearable type="email" disabled/>

                                    <v-text-field v-model="CredentialsForm.Fields.CurrentPasswordInput.value"
                                                  label="Current password"
                                                  :rules="CredentialsForm.Fields.CurrentPasswordInput.rules"
                                                  variant="outlined" density="compact" clearable type="password"/>

                                    <v-text-field v-model="CredentialsForm.Fields.NewPasswordInput.value"
                                                  label="New password"
                                                  :rules="CredentialsForm.Fields.NewPasswordInput.rules"
                                                  variant="outlined" density="compact" clearable type="password"/>
                                    <v-text-field v-model="CredentialsForm.Fields.NewPasswordAgainInput.value"
                                                  label="New password again"
                                                  :rules="CredentialsForm.Fields.NewPasswordAgainInput.rules"
                                                  variant="outlined" density="compact" clearable type="password"/>

                                    <div class="ActionButtonBar">
                                        <button type="button" class="TButton ModifyButton"
                                                @click="eventClickOnCredentialsFormModifyButton">
                                            Modify
                                        </button>
                                    </div>
                                </v-form>
                        </v-col>

                    </v-row>

                </v-tabs-window-item>
                <v-tabs-window-item value="ProfileTab">
                    <v-row>
                        <v-col>
                                <v-form ref="ProfileForm" v-model="ProfileForm.validation.isValid"
                                        class="TSimpleForm" style="min-width:400px;">

                                    <CroppieInput ref="ProfileImageCroppieInput"
                                                  v-model="ProfileForm.Fields.ProfileImageCroppieInput.base64Content"
                                                  :rules="ProfileForm.Fields.ProfileImageCroppieInput.rules"/>
                                    <v-text-field v-model="ProfileForm.Fields.NameTextInput.value" label="Name"
                                                  :rules="ProfileForm.Fields.NameTextInput.rules"
                                                  variant="outlined" density="compact" clearable/>
                                    <div class="ActionButtonBar">
                                        <button type="button"
                                                class="TButton ModifyButton"
                                                @click="eventClickOnProfileFormModifyButton">
                                            Modify
                                        </button>
                                    </div>
                                </v-form>
                        </v-col>
                    </v-row>

                </v-tabs-window-item>
            </v-tabs-window>
        </div>
    </v-card>
</template>

<script>

import {
    EmailFieldRule,
    PasswordFieldRule,
    RequiredFieldRule,
    TextFieldRule
} from "@/validation";
import TController from "@/framework/TController";
import authService from "@/services/auth/AuthService";
import adminAccountSettingsService from "@/services/account/admin/settings/AdminAccountSettingsService";
import TConfirmDialog from "@/framework/component/TConfirmDialog/TConfirmDialog";
import CroppieInput from "@/framework/component/CroppieInput/CroppieInput.vue";
import backendConfiguration from "@/configurations/backendConfiguration";

export default {

    components: {CroppieInput},

    data: () => ({
        MainTabComponent: {
            activeTabKey: "CredentialsTab",
        },
        CredentialsForm: {
            Fields: {
                IdentifierEmailInput: {
                    value: "",
                    rules: [RequiredFieldRule, EmailFieldRule]
                },
                CurrentPasswordInput: {
                    value: "",
                    rules: [PasswordFieldRule]
                },
                NewPasswordInput: {
                    value: "",
                    rules: [PasswordFieldRule]
                },
                NewPasswordAgainInput: {
                    value: "",
                    rules: [PasswordFieldRule]
                },
            },
            validation: {
                isValid: false,
            }
        },
        ProfileForm: {
            Fields: {
                ProfileImageCroppieInput: {
                    base64Content: "",
                    rules: []
                },
                NameTextInput: {
                    value: "",
                    rules: [RequiredFieldRule, TextFieldRule]
                },


            },
            validation: {
                isValid: false,
            }
        },
    }),
    methods: {
        async eventClickOnCredentialsFormModifyButton() {
            try {
                // Confirming
                const confirmed = await TConfirmDialog('Do you continue?')
                if (!confirmed.ok) {
                    return;
                }

                // Form validation
                await this.$refs.CredentialsForm.validate();
                if (!this.CredentialsForm.validation.isValid) {
                    return;
                }

                // Save form value
                await adminAccountSettingsService.modifyCredentialPassword({
                    currentPassword: this.CredentialsForm.Fields.CurrentPasswordInput.value,
                    newPassword: this.CredentialsForm.Fields.NewPasswordInput.value,
                    confirmNewPassword: this.CredentialsForm.Fields.NewPasswordAgainInput.value
                });


                // Reset form
                if (this.$refs.CredentialsForm) {
                    const identifier = this.CredentialsForm.Fields.IdentifierEmailInput.value;
                    this.$refs.CredentialsForm.reset();
                    this.CredentialsForm.Fields.IdentifierEmailInput.value = identifier;
                }

                // Display the success message
                TController.displayModifiedToast();
            } catch (exp) {
                await TController.handleExceptions(exp);
            }
        },
        async eventClickOnProfileFormModifyButton() {
            try {
                // Confirming
                const confirmed = await TConfirmDialog('Do you continue?')
                if (!confirmed.ok) {
                    return;
                }


                // Croppie validation
                if (!this.$refs.ProfileImageCroppieInput.validate()) {
                    return;
                }

                // Form validation
                await this.$refs.ProfileForm.validate();
                if (!this.ProfileForm.validation.isValid) {
                    return;
                }

                // Save form value
                const payload = {
                    name: this.ProfileForm.Fields.NameTextInput.value,
                    imageBase64: this.ProfileForm.Fields.ProfileImageCroppieInput.base64Content
                };
                await adminAccountSettingsService.modifyProfileDetails(payload);

                // Reset form
                if (this.$refs.ProfileForm) {
                    this.$refs.ProfileForm.reset();
                    this.ProfileForm.Fields.NameTextInput.value = payload.name;

                }

                // Display success message
                TController.displayModifiedToast();
            } catch (exp) {
                await TController.handleExceptions(exp);
            }
        },
        async initCredentialsTab() {
            try {

                // Checking permission
                authService.assertEditActionGrantedOn("Account.Admin.Settings");

                // Retrieve: record(s)
                const credentialPasswordResult = await adminAccountSettingsService.accessCredentialPassword();

                // Inject: form input values
                this.CredentialsForm.Fields.IdentifierEmailInput.value = credentialPasswordResult.data.identifier;
                this.CredentialsForm.Fields.CurrentPasswordInput.value = "";
                this.CredentialsForm.Fields.NewPasswordInput.value = "";
                this.CredentialsForm.Fields.NewPasswordAgainInput.value = "";

            } catch (exp) {
                await TController.handleExceptions(exp);
            }
        },
        async initProfileTab() {
            try {

                // Checking permission
                authService.assertEditActionGrantedOn("Account.Admin.Settings");

                // Retrieve: record(s)
                const profileDetailsResult = await adminAccountSettingsService.accessProfileDetails();

                // Inject: form input values
                this.ProfileForm.Fields.NameTextInput.value = profileDetailsResult.data.name;
                await this.$refs.ProfileImageCroppieInput.openFile(backendConfiguration.BACKEND_BASE_URL + '/account/admin/settings/profile/photo');

            } catch (exp) {
                await TController.handleExceptions(exp);
            }
        },
    },
    async created() {
        await this.initCredentialsTab();
    },

}
</script>
<style>

fieldset {
    padding: 10px 10px 10px 10px;
    border-radius: 5px;
    border: 1px solid #66b3ff;
    background-color: white;
}

.TVerticalTabbedPanel {
    position: relative;
    display: flex;
    border: 0px red solid;
}


.TVerticalTabbedPanel .v-slide-group {
    background-color: transparent;

}


.TVerticalTabbedPanel .v-slide-group__content > button {

    background-color: rgba(255,255,255,1) !important;
    margin-bottom: 5px;
    box-shadow: 2px 2px 5px rgba(0,0,0,0.3);
    border-radius: 5px 0px 0px 5px !important;
}


.TVerticalTabbedPanel .v-slide-group__content > button[aria-selected="false"] {
    background-color: rgba(242,242,242,0.8) !important;
    box-shadow: 2px 2px 2px rgba(0,0,0,0.3);
}


.TVerticalTabbedPanel .v-slide-group__content > button[aria-selected="false"]:hover {
    background-color: rgba(255,255,255,0.7) !important;
    box-shadow: 2px 2px 5px rgba(0,0,0,0.3);
    background-color: rgba(255,255,255,1) !important;
}

.TVerticalTabbedPanel .v-tabs-window {
    padding: 10px;
    width: 100%;
    height: 100% !important;
    background-color: white;

    display: flex;
    flex-wrap: wrap;
    gap: 10px;
    flex-direction: row;

    align-items: flex-start;
    box-shadow: 2px 2px 5px rgba(0,0,0,0.3);
    border-radius: 0px 5px 5px 5px;
}


.TSimpleForm {

}

.TSimpleForm > .ActionButtonBar {
    display: flex;
    justify-content: flex-end;
    padding: 0px 0px 0px 10px;
    gap: 5px;
}


</style>