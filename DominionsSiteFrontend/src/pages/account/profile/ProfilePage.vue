<template>
    <div class="TVerticalTabbedPanel ">
        <v-tabs v-model="MainTabComponent.activeTabKey" color="primary" direction="vertical">
            <v-tab prepend-icon="mdi-account" text="Account" value="AccountTab"></v-tab>
        </v-tabs>

        <v-tabs-window v-model="MainTabComponent.activeTabKey">
            <v-tabs-window-item value="AccountTab">

                <v-row>
                    <v-col>
                        <fieldset style="min-width:400px;">
                            <legend>Account</legend>
                            <v-form ref="PasswordFormOfMainTab" v-model="PasswordFormOfMainTab.validation.isValid" class="TSimpleForm">


                                <v-text-field v-model="PasswordFormOfMainTab.Fields.IdentifierEmailInput.value" label="Identifier" :rules="PasswordFormOfMainTab.Fields.IdentifierEmailInput.rules" variant="outlined" density="compact" clearable type="email" disabled/>

                                <v-text-field v-model="PasswordFormOfMainTab.Fields.CurrentPasswordInput.value" label="Current password" :rules="PasswordFormOfMainTab.Fields.CurrentPasswordInput.rules" variant="outlined" density="compact" clearable type="password"/>

                                <v-text-field v-model="PasswordFormOfMainTab.Fields.NewPasswordInput.value" label="New password" :rules="PasswordFormOfMainTab.Fields.NewPasswordInput.rules" variant="outlined" density="compact" clearable type="password"/>
                                <v-text-field v-model="PasswordFormOfMainTab.Fields.NewPasswordAgainInput.value" label="New password again" :rules="PasswordFormOfMainTab.Fields.NewPasswordAgainInput.rules" variant="outlined" density="compact" clearable type="password"/>

                                <div class="ActionButtonBar">
                                    <button type="button" class="TButton ModifyButton" @click="eventClickOnPasswordFormModifyButton">
                                        Modify
                                    </button>
                                </div>
                            </v-form>
                        </fieldset>
                    </v-col>
                    <v-col>
                        <fieldset style="min-width:400px;">
                            <legend>Personal</legend>
                            <v-form ref="DetailsFormOfMainTab" v-model="DetailsFormOfMainTab.validation.isValid" class="TSimpleForm">

                                <CroppieInput ref="ProfileImageCroppieInput" v-model="DetailsFormOfMainTab.Fields.ProfileImageCroppieInput.base64Content" :rules="DetailsFormOfMainTab.Fields.ProfileImageCroppieInput.rules"/>
                                <v-text-field v-model="DetailsFormOfMainTab.Fields.NameTextInput.value" label="Name" :rules="DetailsFormOfMainTab.Fields.NameTextInput.rules" variant="outlined" density="compact" clearable/>

                                <div class="ActionButtonBar">
                                    <button type="button" class="TButton ModifyButton" @click="eventClickOnPersonalFormModifyButton">
                                        Modify
                                    </button>
                                </div>
                            </v-form>
                        </fieldset>
                    </v-col>
                </v-row>

            </v-tabs-window-item>

        </v-tabs-window>
    </div>
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

    export default {
        components: {CroppieInput},

        data: () => ({
            MainTabComponent: {
                activeTabKey: "AccountTab",
            },
            PasswordFormOfMainTab: {
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


            DetailsFormOfMainTab: {
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
            async eventClickOnPasswordFormModifyButton() {
                try {
                    // Confirming
                    const confirmed = await TConfirmDialog('Do you continue?')
                    if(!confirmed.ok) {
                        return;
                    }

                    // Form validation
                    await this.$refs.PasswordFormOfMainTab.validate();
                    if(!this.PasswordFormOfMainTab.validation.isValid) {
                        return;
                    }

                    // Save form value
                    await accountSettingsService.modifyCredentialPassword({
                        currentPassword: this.PasswordFormOfMainTab.Fields.CurrentPasswordInput.value,
                        newPassword: this.PasswordFormOfMainTab.Fields.NewPasswordInput.value,
                        confirmNewPassword: this.PasswordFormOfMainTab.Fields.NewPasswordAgainInput.value
                    });


                    // Reset form
                    if(this.$refs.PasswordFormOfMainTab) {
                        const identifier = this.PasswordFormOfMainTab.Fields.IdentifierEmailInput.value;
                        this.$refs.PasswordFormOfMainTab.reset();
                        this.PasswordFormOfMainTab.Fields.IdentifierEmailInput.value = identifier;

                    }

                    // Display success message
                    TController.displayModifiedToast();
                } catch(exp) {
                    await TController.handleExceptions(exp);
                }
            },
            async eventClickOnPersonalFormModifyButton() {
                try {
                    // Confirming
                    const confirmed = await TConfirmDialog('Do you continue?')
                    if(!confirmed.ok) {
                        return;
                    }


                    // Croppie validation
                    if(!this.$refs.ProfileImageCroppieInput.validate()) {
                        return;
                    }

                    // Form validation
                    await this.$refs.DetailsFormOfMainTab.validate();
                    if(!this.DetailsFormOfMainTab.validation.isValid) {
                        return;
                    }

                    // Save form value
                    const payload = {
                        name: this.DetailsFormOfMainTab.Fields.NameTextInput.value,
                        imageBase64: this.DetailsFormOfMainTab.Fields.ProfileImageCroppieInput.base64Content
                    };
                    await accountSettingsService.modifyDetails(payload);

                    // Reset form
                    if(this.$refs.DetailsFormOfMainTab) {
                        this.$refs.DetailsFormOfMainTab.reset();
                        this.DetailsFormOfMainTab.Fields.NameTextInput.value = payload.name;

                    }

                    // Display success message
                    TController.displayModifiedToast();
                } catch(exp) {
                    await TController.handleExceptions(exp);
                }
            },

            async load() {
                try {

                    // Checking permission
                    authService.assertEditActionGrantedOn("Account.Profile");

                    // Retrieve: record(s)
                    const credentialPasswordResult = await accountSettingsService.accessCredentialPassword();
                    const detailsResult = await accountSettingsService.accessDetails();

                    // Inject: form input values
                    this.PasswordFormOfMainTab.Fields.IdentifierEmailInput.value = credentialPasswordResult.data.identifier;
                    this.PasswordFormOfMainTab.Fields.CurrentPasswordInput.value = "";
                    this.PasswordFormOfMainTab.Fields.NewPasswordInput.value = "";
                    this.PasswordFormOfMainTab.Fields.NewPasswordAgainInput.value = "";

                    // Inject: form input values
                    this.DetailsFormOfMainTab.Fields.NameTextInput.value = detailsResult.data.name;
                    await this.$refs.ProfileImageCroppieInput.openFile(backendConfiguration.BACKEND_BASE_URL+'/data/account/settings/details/image/main');

                } catch(exp) {
                    await TController.handleExceptions(exp);
                }
            },
        },
        created() {
            this.load();
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

        background-color: white !important;

    }


    .TVerticalTabbedPanel .v-tabs-window {
        padding: 10px;
        width: 100%;
        height: 100% !important;
        background-color: white;

        display: flex;
        flex-wrap: wrap; /* Ha nincs hely, új sorba tör */
        gap: 10px; /* Távolság az elemek között */
        flex-direction: row;

        align-items: flex-start;
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