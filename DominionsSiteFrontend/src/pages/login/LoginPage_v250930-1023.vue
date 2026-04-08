<template>
    <v-container fill-height >
        <v-col cols="12">
            <v-row align="center" justify="center" class="fill-height">
                <v-col cols="12" sm="8" md="4">
                    <v-card class="mx-auto LoginBox" max-width="500" >
                        <v-card-title class="text-h5" primary-title>Login</v-card-title>
                        <v-card-text>
                            <v-form ref="Form" v-model="Form.validation.isValid">

                                <v-text-field v-model="Form.Fields.IdentifierEmailInput.value" label="Identifier" :rules="Form.Fields.IdentifierEmailInput.rules" variant="outlined" density="compact" type="email"/>
                                <v-text-field v-model="Form.Fields.PasswordInput.value" label="Password" :rules="Form.Fields.PasswordInput.rules" variant="outlined" density="compact" type="password"/>

                                <button type="button" class="TButton ModifyButton"
                                        :disabled="!Form.validation.isValid"
                                        @click="eventClickOnLoginButton">Login
                                </button>

                            </v-form>
                        </v-card-text>
                    </v-card>
                </v-col>
            </v-row>
            <v-row align="center" justify="center" class="fill-height">
                <v-col cols="12" sm="8" md="4">
                    <v-card class="mx-auto LoginBox" max-width="500" >
                        <v-card-title class="text-h5" primary-title>Login</v-card-title>
                        <v-card-text>
                            <v-form ref="Form" v-model="Form.validation.isValid">

                                <v-text-field v-model="Form.Fields.IdentifierEmailInput.value" label="Identifier" :rules="Form.Fields.IdentifierEmailInput.rules" variant="outlined" density="compact" type="email"/>
                                <v-text-field v-model="Form.Fields.PasswordInput.value" label="Password" :rules="Form.Fields.PasswordInput.rules" variant="outlined" density="compact" type="password"/>

                                <button type="button" class="TButton ModifyButton"
                                        :disabled="!Form.validation.isValid"
                                        @click="eventClickOnLoginButton">Login
                                </button>

                            </v-form>
                        </v-card-text>
                    </v-card>
                </v-col>
            </v-row>
        </v-col>

    </v-container>
</template>

<script>
    import * as ValidationModule from '@/validation.js';
    import {authService} from "@/services/auth/AuthService";
    import TController from "@/framework/TController";
    import {
        EmailFieldRule,
        OptionalPasswordFieldRule,
        PasswordFieldRule,
        RequiredFieldRule,
        TextFieldRule
    } from "@/validation.js";

    export default {
        data: () => ({
            show: false,
            Form: {
                Fields: {
                    IdentifierEmailInput: {
                        value: "kalmanczheysandor@gmail.com",
                        rules: [RequiredFieldRule, EmailFieldRule]
                    },
                    PasswordInput: {
                        value: "CHANGE_ME",
                        rules: [PasswordFieldRule]
                    },
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
            async eventClickOnLoginButton() {
                try {

                    // Form validation
                    await this.$refs.Form.validate();
                    if(!this.Form.validation.isValid) {
                        return;
                    }

                    // Do login
                    await authService.login({
                        identifier: this.Form.Fields.IdentifierEmailInput.value,
                        password: this.Form.Fields.PasswordInput.value
                    });

                    this.$router.push('/main');
                } catch(exp) {
                    await TController.displayExceptionMessages(exp);
                }
            }
        }
    }
</script>