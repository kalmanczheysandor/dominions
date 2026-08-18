<template>
    <h3 class="SectionTitle">
        Account recovery
    </h3>
    <v-row align="center" justify="center" class="fill-height">
        <v-col cols="8" sm="6" md="4" class="justify-center">
            <v-card class="mx-auto RecoveryPanel">
                <v-card-title class="text-h5" primary-title>Recovery request</v-card-title>
                <v-card-text>
                    <v-form ref="Form" v-model="Form.validation.isValid">
                        <v-text-field v-model="Form.Fields.IdentifierEmailInput.value" label="Identifier"
                                      :rules="Form.Fields.IdentifierEmailInput.rules" variant="outlined"
                                      density="compact" type="email"/>
                        <button type="button" class="TButton PanelActionButton" :disabled="!Form.validation.isValid"
                                @click="eventClickOnRequestButton">Request
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
                IdentifierEmailInput: {
                    value: "",
                    rules: [RequiredFieldRule, EmailFieldRule]
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
        async eventClickOnRequestButton() {
            try {

                // Form validation
                await this.$refs.Form.validate();
                if (!this.Form.validation.isValid) {
                    return;
                }

                // Communicate to backend
                await accountService.recovery({
                    identifier: this.Form.Fields.IdentifierEmailInput.value,
                });

                await TInfoDialog({
                    title: 'Recovery',
                    message: 'A verification email is sent to: ' + this.Form.Fields.IdentifierEmailInput.value
                });

                if (this.$refs.Form) {
                    this.$refs.Form.reset();
                }

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
.RecoveryPanel {
    background-color: rgba(255, 255, 255, 0.2);
    backdrop-filter: blur(10px);
    -webkit-backdrop-filter: blur(5px);
    box-shadow: 5px 5px 15px rgba(0, 0, 0, 0.4);
}
</style>

