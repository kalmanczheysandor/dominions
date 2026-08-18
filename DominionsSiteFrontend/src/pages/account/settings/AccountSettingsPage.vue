<template>
    <MenuBar/>
    <h3 class="SectionTitle">
        Settings
    </h3>
    <v-row align="center" justify="center" class="fill-height">
        <v-col cols="8" sm="6" md="4" class="justify-center">
            <v-card class="mx-auto SettingsPanel">
                <div class="SettingsTabs">
                    <v-tabs v-model="MainTabComponent.activeTabKey" color="primary">
                        <v-tab text="Credential" value="CredentialTab" >Credential</v-tab>
                        <v-tab text="Profile" value="ProfileDetailsTab" @click.once="initProfileTab">Profile</v-tab>
                    </v-tabs>

                    <v-tabs-window v-model="MainTabComponent.activeTabKey" >

                        <v-tabs-window-item value="CredentialTab">
                            <v-form ref="CredentialsForm"
                                    v-model="CredentialsForm.validation.isValid" class="">

                                <v-text-field
                                        v-model="CredentialsForm.Fields.IdentifierEmailInput.value"
                                        label="Identifier"
                                        :rules="CredentialsForm.Fields.IdentifierEmailInput.rules"
                                        variant="outlined" density="compact" clearable type="email"
                                        disabled/>

                                <v-text-field
                                        v-model="CredentialsForm.Fields.CurrentPasswordInput.value"
                                        label="Current password"
                                        :rules="CredentialsForm.Fields.CurrentPasswordInput.rules"
                                        variant="outlined" density="compact" clearable type="password"/>

                                <v-text-field v-model="CredentialsForm.Fields.NewPasswordInput.value"
                                              label="New password"
                                              :rules="CredentialsForm.Fields.NewPasswordInput.rules"
                                              variant="outlined" density="compact" clearable
                                              type="password"/>
                                <v-text-field
                                        v-model="CredentialsForm.Fields.NewPasswordAgainInput.value"
                                        label="New password again"
                                        :rules="CredentialsForm.Fields.NewPasswordAgainInput.rules"
                                        variant="outlined" density="compact" clearable type="password"/>

                                <div class="ActionButtonBar">

                                    <button type="button" class="TButton ModifyButton" @click="eventClickOnCredentialsFormModifyButton">
                                        Modify
                                    </button>
                                </div>
                            </v-form>


                        </v-tabs-window-item>


                        <v-tabs-window-item value="ProfileDetailsTab">

                                <v-form ref="ProfileDetailsForm" v-model="ProfileDetailsForm.validation.isValid" class="TSimpleForm">

                                    <CroppieInput ref="ProfileImageCroppieInput"
                                                  v-model="ProfileDetailsForm.Fields.ProfileImageCroppieInput.base64Content"
                                                  :rules="ProfileDetailsForm.Fields.ProfileImageCroppieInput.rules"
                                    />
                                    <v-text-field v-model="ProfileDetailsForm.Fields.NameTextInput.value"
                                                  label="Name"
                                                  :rules="ProfileDetailsForm.Fields.NameTextInput.rules"
                                                  variant="outlined" density="compact" clearable/>

                                    <div class="ActionButtonBar">
                                        <button type="button" class="TButton ModifyButton"
                                                @click="eventClickOnProfileFormModifyButton">
                                            Modify
                                        </button>
                                    </div>
                                </v-form>

                        </v-tabs-window-item>

                    </v-tabs-window>
                </div>

            </v-card>
        </v-col>
    </v-row>

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
import accountSettingsService from "@/services/account/settings/AccountSettingsService";
import TConfirmDialog from "@/framework/component/TConfirmDialog/TConfirmDialog";
import CroppieInput from "@/framework/component/CroppieInput/CroppieInput.vue";
import backendConfiguration from "@/configurations/backendConfiguration";
import NavigationMenu from "@/components/NavigationMenu.vue";
import MenuBar from "@/components/MenuBar.vue";

console.log("NavigationMenu importált:", NavigationMenu);
console.log("NavigationMenu CroppieInput:", CroppieInput);



export default {
    components: {MenuBar, CroppieInput},

    data: () => ({
        MainTabComponent: {
            activeTabKey: "CredentialTab",
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
        ProfileDetailsForm: {
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
                await accountSettingsService.modifyCredentialPassword({
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
                await this.$refs.ProfileDetailsForm.validate();
                if (!this.ProfileDetailsForm.validation.isValid) {
                    return;
                }

                // Save form value
                const payload = {
                    name: this.ProfileDetailsForm.Fields.NameTextInput.value,
                    imageBase64: this.ProfileDetailsForm.Fields.ProfileImageCroppieInput.base64Content
                };
                await accountSettingsService.modifyProfileDetails(payload);

                // Reset form
                if (this.$refs.ProfileDetailsForm) {
                    this.$refs.ProfileDetailsForm.reset();
                    this.ProfileDetailsForm.Fields.NameTextInput.value = payload.name;

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
                authService.assertEditActionGrantedOn("Account.Settings");

                // Retrieve: record(s)
                const credentialPasswordResult = await accountSettingsService.accessCredentialPassword();

                // Inject: form input values
                this.CredentialsForm.Fields.IdentifierEmailInput.value = credentialPasswordResult.identifier;
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
                authService.assertEditActionGrantedOn("Account.Settings");

                // Retrieve: record(s)
                const profileDetailsResult = await accountSettingsService.accessProfileDetails();
                //
                // Inject: form input values
                // this.ProfileDetailsForm.Fields.NameTextInput.value = profileDetailsResult.name;
                this.ProfileDetailsForm.Fields.NameTextInput.value = "Ica";
               await this.$refs.ProfileImageCroppieInput.openFile(backendConfiguration.BACKEND_BASE_URL + '/account/settings/profile/photo');

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
.SettingsPanel {
    background-color: rgba(255, 255, 255, 0.2);
    backdrop-filter: blur(10px);
    -webkit-backdrop-filter: blur(5px);
    box-shadow: 5px 5px 15px rgba(0, 0, 0, 0.4);
}

fieldset {
    padding: 10px 10px 10px 10px;
    border-radius: 5px;
    border: 1px solid #66b3ff;
    background-color: transparent;
}


.SettingsTabs {
    margin: 20px;
}

.SettingsTabs .v-slide-group {
    position: relative;
    height: 30px !important;
    overflow: visible;
    border-bottom: none;
}

.SettingsTabs .v-slide-group__container {
    position: absolute;
    top: 1px;
    z-index: 200 !important;
}


.SettingsTabs .v-tab {

    Height: 25px !important;
    Margin: 5px 8px 0px 0px;
    Padding: 3px 10px 3px 10px;

    font-family: MainFont;
    Font-size: 15px;
    Color: #224f77;
    Cursor: pointer;
    Border: 0px #224f77 solid;
    Display: block;
    Box-sizing: border-box;
    Text-decoration: none;
    text-transform: none;
    border-bottom: none;

    min-width: 2px;

    background-color: rgba(0, 0, 0, 0.2) !important;
    border-radius: 5px 5px 0px 0px !important;
}

.SettingsTabs .v-tab:hover {
    margin-top: 0px;
    height: 30px !important;
    Border-bottom: none;
    background-color: rgba(0, 0, 0, 0.3) !important;
}


.SettingsTabs .v-tab-item--selected {
    margin-top: 0px;
    height: 30px !important;
    background-color: rgba(255, 255, 255, 0.2) !important;
    Border: none !important;
    color: #224f77 !important;
}


.SettingsTabs .v-tab-item--selected:hover {
    background-color: rgba(255, 255, 255, 0.2) !important;
}

.SettingsTabs .v-tab__slider {
    background-color: transparent;
    border: none;
}


.SettingsTabs .v-tabs-window {
    padding: 5px;
    margin: 0px !important;
    margin-top: 1px !important;
    background-color: rgba(255, 255, 255, 0.2) !important;
    border-radius: 0px 0px 5px 5px;

}


.TSimpleForm {

}

.TSimpleForm > .ActionButtonBar {

    padding: 0px 0px 0px 10px;
}


</style>