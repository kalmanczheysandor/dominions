<template>
    <v-container fill-height>
        <v-row align="center" justify="center" class="fill-height">
            <v-col cols="12" sm="8" md="4">
                <v-card class="mx-auto" max-width="344">
                    <v-card-title class="text-h5" primary-title>Login</v-card-title>
                    <v-card-text>
                        <v-form ref="Form" v-model="Form.validation.isValid">
                            <v-text-field v-model="Form.data.username" label="Email" :rules="[Form.validation.rules.email]" required outlined/>
                            <v-text-field v-model="Form.data.password" label="Password" :rules="[Form.validation.rules.password]" type="password" required outlined/>
                            <v-btn :disabled="!Form.validation.isValid" color="primary" block @click="eventClickOnLoginButton">Login</v-btn>
                        </v-form>
                    </v-card-text>
                </v-card>
            </v-col>
        </v-row>
    </v-container>
</template>

<script>
    import * as ValidationModule from '@/validation.js';
    import {authService} from "@/services/auth/AuthService";
    import TController from "@/framework/TController";

    export default {
        data: () => ({
            show: false,
            Form: {
                data: {
                    username: "kalmanczheysandor@gmail.com",
                    password: "CHANGE_ME",
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




                    await authService.login({username: this.Form.data.username, password: this.Form.data.password})
                    this.$router.push('/main');

                } catch(exp) {
                    await TController.displayExceptionMessages(exp);
                }
            }
        }
    }
</script>