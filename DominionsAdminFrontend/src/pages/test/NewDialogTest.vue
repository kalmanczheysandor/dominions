<template>
    <TDialog ref="Dialog" title="New breed" :closeable="true" @closed="whenParentClose">
        <v-form ref="Form" v-model="Form.validation.isValid">
            <v-text-field v-model="Form.data.name" label="Name" :rules="[Form.validation.rules.required,Form.validation.rules.text]"></v-text-field>
            <v-checkbox v-model="Form.data.enabled" label="Checkbox"></v-checkbox>
            <v-btn color="primary" @click="eventSave">Save</v-btn>
            <v-btn color="secondary" @click="eventCancel">Cancel</v-btn>
        </v-form>
    </TDialog>
</template>

<script>
    import TDialog from "@/framework/component/TDialog/TDialog.vue";
    import * as ValidationModule from '@/validation.js';
    import {BreedService} from "@/services/breed/BreedService.js";


    export default {
        name: "NewDialogTest",
        components: {TDialog},
        data() {
            return {
                Form: {
                    data: {
                        name: "",
                        enabled: true,
                    },
                    validation: {
                        isValid: false,
                        rules: {
                            ...ValidationModule.globalRules
                        }
                    }
                },
            };
        },
        emits: ["closed"],
        methods: {
            async eventSave() {
                try {
                    // Form validation
                    await this.$refs.Form.validate();
                    if (!this.Form.validation.isValid) {
                        return;
                    }

                    // Save form value
                    const result = await BreedService.save(this.Form.data);
                    if (result.success) {
                        this.$root.showSuccessDialog(null, "Success", "Done!");
                        this.close();
                    } else {
                        this.$root.showWarnDialog(null, "Warning", result.messages);
                    }
                } catch (e) {
                    alert(e);
                }
            },
            eventCancel() {
                this.close();
            },
            open() {
                this.$refs.Dialog.open();
            },
            close() {
                this.$refs.Dialog.close();
                this.$refs.Form.reset();
                this.$emit("closed");
            },
            whenParentClose() {
                this.$refs.Form.reset();
            }
        },
    };
</script>
