<template>
    <UserAddDialog ref="UserAddDialog"/>
    <v-btn @click="doShow">New user</v-btn>
</template>

<script>
    import UserAddDialog from "@/pages/account/user/UserAddDialog.vue";
    export default {
        components: {UserAddDialog},
        data() {
            return {
                formData: {
                    name: "",
                    email: "",
                },
                formValid: false,
            };
        },
        methods: {
            doShow(){
                this.$refs.UserAddDialog.open()
            }
        },
    };
</script>
