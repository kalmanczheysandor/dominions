<template>
    <!-- Gomb, ami megjeleníti a dialógust -->
    <v-btn @click="openDialog">Open Dialog</v-btn>
    <v-btn @click="$refs.ClientAddDialog.open()">9</v-btn>
    <!-- Teleportáljuk a dialógust a body-ba vagy bárhová -->
    <Teleport to="body">
        <TDialog ref="ClientAddDialog" title="Add New User" :closeable="true" @closed="onClose">
            <v-form ref="form" v-model="formValid">
                <v-text-field v-model="formData.name" label="Name" required></v-text-field>
                <v-text-field v-model="formData.email" label="Email" required type="email"></v-text-field>
                <v-btn color="primary" @click="doSave(119)">Save</v-btn>
                <v-btn color="secondary" @click="closeDialog">Cancel</v-btn>
            </v-form>
        </TDialog>
    </Teleport>

    <UserAddDialog refs="UserAddDialog"/>


    <v-btn @click="$refs.UserAddDialog.open()">New user</v-btn>
</template>

<script>
    import TDialog from "@/framework/component/TDialog/TDialog.vue";

    export default {
        components: { TDialog },
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
            // A dialógus megnyitása programból
            openDialog() {
                this.$refs.ClientAddDialog.open(); // A dialógus megnyitása ref-en keresztül
            },

            // A dialógus bezárása programból
            closeDialog() {
                this.$refs.ClientAddDialog.close(); // A dialógus bezárása ref-en keresztül
            },

            // Mentés művelet
            doSave(index) {
                alert("save " + index);

                // Mentés logika (például form validálás itt)
            },

            // Dialógus bezárása
            onClose() {
                console.log("Dialog closed.");
                this.resetForm();
            },

            resetForm() {
                this.formData = { name: "", email: "" };
            },
        },
    };
</script>
