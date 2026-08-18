<template>
    <h3 class="SectionTitle">
        Account recovery
    </h3>
    <v-row align="center" justify="center" class="fill-height">
        <v-col cols="8" sm="6" md="4" class="justify-center">
            <v-card class="mx-auto SubmitPanel">
                <v-card-title class="text-h5" primary-title>New password submit</v-card-title>
                <v-card-text>
                    <v-form ref="Form" v-model="Form.validation.isValid">
                        <v-text-field v-model="Form.Fields.NewPasswordInput.value" label="New password" :rules="Form.Fields.NewPasswordInput.rules" variant="outlined" density="compact" type="password"/>
                        <v-text-field v-model="Form.Fields.NewPasswordAgainInput.value" label="New password again" :rules="Form.Fields.NewPasswordAgainInput.rules" variant="outlined" density="compact" clearable type="password" />

                        <button type="button" class="TButton PanelActionButton" :disabled="!Form.validation.isValid"
                                @click="eventClickOnSubmitButton">Submit
                        </button>
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

export default {
    data: () => ({
        show: false,
        Form: {
            Fields: {
                NewPasswordInput: {
                    value: "",
                    rules: [PasswordFieldRule]
                },
                NewPasswordAgainInput: {
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
    props: {
        verificationToken: {
            type: String,
            required: true
        }
    },
    methods: {
        async eventClickOnSubmitButton() {
            try {

                // Form validation
                await this.$refs.Form.validate();
                if (!this.Form.validation.isValid) {
                    return;
                }

                // Communicate to backend
                await accountService.recoverySubmit({
                    verificationToken: this.verificationToken,
                    password: this.Form.Fields.NewPasswordInput.value,
                    confirmPassword: this.Form.Fields.NewPasswordAgainInput.value,
                });

                await TInfoDialog({
                    title: 'Recovery submitted successfully.',
                    message: 'Your new password is active!'
                });

                if (this.$refs.Form) {
                    this.$refs.Form.reset();
                }

                this.$router.push('/main');
            } catch (exp) {
                await TController.handleExceptions(exp);
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
.SubmitPanel {
    background-color: rgba(255, 255, 255, 0.2);
    backdrop-filter: blur(10px);
    -webkit-backdrop-filter: blur(5px);
    box-shadow: 5px 5px 15px rgba(0, 0, 0, 0.4);
}
</style>

