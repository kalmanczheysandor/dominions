<template>
    <h3 class="SectionTitle">
        Sign up
    </h3>
    <v-row align="center" justify="center" class="fill-height">
        <v-col cols="8" sm="6" md="4" class="justify-center">
            <v-card class="mx-auto RecoveryPanel"  >
                <v-card-title class="text-h5" primary-title>Account details</v-card-title>
                <v-card-text>
                    <v-form ref="Form" v-model="Form.validation.isValid">
                        <v-text-field v-model="Form.Fields.IdentifierEmailInput.value" label="Identifier" :rules="Form.Fields.IdentifierEmailInput.rules" variant="outlined" density="compact" type="email"/>
                        <v-text-field v-model="Form.Fields.PasswordInput.value" label="Password" :rules="Form.Fields.PasswordInput.rules" variant="outlined" density="compact" type="password"/>
                        <v-text-field v-model="Form.Fields.PasswordAgainInput.value" label="Password again" :rules="Form.Fields.PasswordAgainInput.rules" variant="outlined" density="compact" clearable type="password" />
                        <v-text-field v-model="Form.Fields.NameTextInput.value" label="Name" :rules="Form.Fields.NameTextInput.rules" variant="outlined" density="compact" clearable/>
                        <button type="button" class="TButton PanelActionButton"  :disabled="!Form.validation.isValid" @click="eventClickOnSignUpButton">Sign up</button>
                    </v-form>
                </v-card-text>
            </v-card>
        </v-col>
    </v-row>
</template>

<script>
    import * as ValidationModule from '@/validation.js';
    import TController from "@/framework/TController";
    import {
        EmailFieldRule,
        PasswordFieldRule,
        RequiredFieldRule,
        TextFieldRule
    } from "@/validation.js";
    import accountService from "@/services/account/AccountService";
    import TInfoDialog from "@/framework/component/TInfoDialog/TInfoDialog";
    import TWarningDialog from "@/framework/component/TWarningDialog/TWarningDialog";
    import TAlertDialog from "@/framework/component/TAlertDialog/TAlertDialog";
    import TConfirmDialog from "@/framework/component/TConfirmDialog/TConfirmDialog";

    export default {
        data: () => ({
            show: false,
            Form: {
                Fields: {
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
                        rules: [PasswordFieldRule]
                    },
                    PasswordAgainInput: {
                        value: "",
                        rules: [RequiredFieldRule, PasswordFieldRule]
                    }
                },

                validation: {
                    isValid: false,
                    rules: {
                        ...ValidationModule.globalRules
                    }
                }
            }
        }),
        methods: {
            async eventClickOnSignUpButton() {
                try {

                    // Form validation
                    await this.$refs.Form.validate();
                    if(!this.Form.validation.isValid) {
                        return;
                    }


                    // Communicate to backend
                    await accountService.signUp({
                        identifier: this.Form.Fields.IdentifierEmailInput.value,
                        password: this.Form.Fields.PasswordInput.value,
                        confirmPassword: this.Form.Fields.PasswordAgainInput.value,
                        name: this.Form.Fields.NameTextInput.value,
                    });

                    await TInfoDialog({
                        title: 'Sign up',
                        message: 'A verification email is sent to: '+this.Form.Fields.IdentifierEmailInput.value
                    });

                    if(this.$refs.Form) {
                        this.$refs.Form.reset();
                    }

                    this.$router.push('/main');
                } catch(exp) {
                    await TController.displayExceptionMessages(exp);
                }
            }
        }
    }
</script>

<style scoped>
.PanelActionButton {
    display: inline;
    width: 100%;
}
.RecoveryPanel {
    background-color: rgba(255, 255, 255, 0.2);
    backdrop-filter: blur(10px);
    -webkit-backdrop-filter: blur(5px);
    box-shadow: 5px 5px 15px rgba(0,0,0,0.4);
}
</style>

