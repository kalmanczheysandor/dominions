<template>
    <UserAddDialog ref="UserAddDialog"/>
    <v-btn @click="doShow">New user</v-btn>
</template>

<script>
    import AdminUserAddDialog from "@/pages/account/admin/user/AdminUserAddDialog.vue";
    export default {
        components: {UserAddDialog: AdminUserAddDialog},
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
