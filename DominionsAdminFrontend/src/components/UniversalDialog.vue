<template>
    <TDialog title="Add Somebody" :closeable="true" @closed="onClose">
        <v-form ref="form" v-model="formValid">
            <v-text-field v-model="formData.name" label="Name" required></v-text-field>
            <v-text-field v-model="formData.email" label="Email" required type="email"></v-text-field>
            <v-btn color="primary" @click="onSave">Save</v-btn>
            <v-btn color="secondary" @click="dialogVisible = false">Cancel</v-btn>
        </v-form>
    </TDialog>
</template>

<script>
    import TDialog from "@/framework/component/TDialog/TDialog.vue";

    export default {
        name: "UserAddDialog",
        components: { TDialog },
        data() {
            return {
                dialogVisible: false,
                formData: {
                    name: "",
                    email: "",
                },
                formValid: false,
            };
        },
        methods: {
            open() {
                this.dialogVisible = true;
            },
            onSave() {
                if (this.$refs.form.validate()) {
                    this.$emit("save", { ...this.formData }); // Emitálás szülő komponensnek
                    this.resetForm();
                    this.dialogVisible = false; // Dialógus bezárása
                } else {
                    console.log("Form validation failed.");
                }
            },
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
