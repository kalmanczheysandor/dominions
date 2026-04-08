<template>
    <v-row align="center" justify="center" class="fill-height">
        <v-col cols="8" sm="6" md="4" class="justify-center">
            <v-card class="mx-auto RecoveryPanel">
                <v-card-title class="text-h5" primary-title>Sign up - Verification</v-card-title>
                <v-card-text>
                    Verification token: {{verificationToken}}
                </v-card-text>
            </v-card>
        </v-col>
    </v-row>
</template>

<script>

import accountService from "@/services/account/AccountService";
import TController from "@/framework/TController";
import TInfoDialog from "@/framework/component/TInfoDialog/TInfoDialog";

export default {
    data: () => ({
        show: true
    }),
    props: {
        verificationToken: {
            type: String,
            required: true
        }
    },

    // async beforeRouteEnter(to, from, next) {
    //
    //     next()
    //     await this.doVerification()
    // },
    methods: {
        async doVerification() {
            try {
                await accountService.signUpVerification({
                    verificationToken: this.verificationToken
                });

                await TInfoDialog({
                    title: 'Sign up',
                    message: 'Your registration is successfully finalised!'
                });
                this.$router.push('/main');
            } catch (exp) {
                await TController.displayExceptionMessages(exp);
            }
        },
        // async eventClickOnRegistrationButton() {
        //     try {
        //
        //         // Form validation
        //         await this.$refs.Form.validate();
        //         if(!this.Form.validation.isValid) {
        //             return;
        //         }
        //
        //         // // Do login
        //         // await accountService.signUp({
        //         //     identifier: this.Form.Fields.IdentifierEmailInput.value,
        //         //     password: this.Form.Fields.PasswordInput.value,
        //         //     confirmPassword: this.Form.Fields.PasswordAgainInput.value,
        //         //     name: this.Form.Fields.NameTextInput.value,
        //         // });
        //         //
        //         // await TInfoDialog({
        //         //     title: 'Registration',
        //         //     message: 'A verification email was sent to: '+this.Form.Fields.IdentifierEmailInput.value
        //         // });
        //
        //         if(this.$refs.Form) {
        //             this.$refs.Form.reset();
        //         }
        //
        //         this.$router.push('/main');
        //     } catch(exp) {
        //         await TController.displayExceptionMessages(exp);
        //     }
        // }
    },


    watch: {
        '$route.params.verificationToken': {
            immediate: true,
            handler(newVal, oldVal) {
                this.doVerification();
            }
        }
    }
}
</script>

<style scoped>
.RecoveryPanel {
    background-color: rgba(255, 255, 255, 0.2);
    backdrop-filter: blur(10px);
    -webkit-backdrop-filter: blur(5px);
    box-shadow: 5px 5px 15px rgba(0, 0, 0, 0.4);
}
</style>

