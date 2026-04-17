<template>
    <TDialog ref="Dialog" title="New lizPersonnel" :closeable="true" @whenDialogClosed="whenParentDialogClosed">
        <v-form ref="Form" class="TTabbedPanel" v-model="Form.validation.isValid">
            <v-tabs v-model="MainTabComponent.activeKey" align-tabs="end" color="primary">
                <v-tab value="Tab1">*</v-tab>
            </v-tabs>
            <v-tabs-window v-model="MainTabComponent.activeKey">
                <v-tabs-window-item value="Tab1">
                    <v-text-field v-model="Form.Fields.NameTextInput.value" label="Name"
                                  :rules="Form.Fields.NameTextInput.rules" variant="outlined" density="compact"
                                  clearable/>
                    <v-text-field v-model="Form.Fields.CodeTextInput.value" label="Code"
                                  :rules="Form.Fields.CodeTextInput.rules" variant="outlined" density="compact"
                                  clearable/>
                    <v-checkbox v-model="Form.Fields.EnabledCheckInput.isChecked" label="Enabled" variant="outlined"
                                density="compact" clearable/>
                </v-tabs-window-item>
            </v-tabs-window>

            <div class="ActionButtonBar">
                <button type="button" class="TButton CancelButton" @click="eventClickOnCancelButton">Cancel</button>
                <button type="button" class="TButton ModifyButton" @click="eventClickOnSaveButton">Save</button>
            </div>
        </v-form>
    </TDialog>
</template>

<script>
import TDialog from "@/framework/component/TDialog/TDialog.vue";
import TConfirmDialog from "@/framework/component/TConfirmDialog/TConfirmDialog";
import {authService} from "@/services/auth/AuthService";
import TController from "@/framework/TController";
import {
    RequiredFieldRule,
    TextFieldRule
} from "@/validation.js";
import lizPersonnelService from "@/services/ai/liz/personnel/LizPersonnelService";


export default {
    name: "LizPersonnelAddDialog",
    components: {TDialog},
    data() {
        return {
            MainTabComponent: {
                activeTabKey: null,
            },
            Form: {
                Fields: {
                    NameTextInput: {
                        value: "",
                        rules: [RequiredFieldRule, TextFieldRule]
                    },
                    CodeTextInput: {
                        value: "",
                        rules: [RequiredFieldRule, TextFieldRule]
                    },
                    EnabledCheckInput: {
                        isChecked: true,
                        rules: []
                    },
                },
                validation: {
                    isValid: false,
                }
            },
        };
    },
    emits: ["whenDialogClosed"],
    methods: {
        async eventClickOnSaveButton() {
            try {
                // Confirming
                const confirmed = await TConfirmDialog('Do you continue?')
                if (!confirmed.ok) {
                    return;
                }

                // Form validation
                await this.$refs.Form.validate();
                if (!this.Form.validation.isValid) {
                    return;
                }

                // Save form value
                await lizPersonnelService.save({
                    name: this.Form.Fields.NameTextInput.value,
                    code: this.Form.Fields.CodeTextInput.value,
                    enabled: this.Form.Fields.EnabledCheckInput.isChecked
                });

                // Display success message
                TController.displaySavedToast();

                // Hide dialog
                this.close();
            } catch (exp) {
                await TController.displayExceptionMessages(exp);
            }
        },
        eventClickOnCancelButton() {
            this.close();
        },
        async open() {
            try {
                // Checking permission
                authService.assertAddActionGrantedOn("Ai.Liz.Personnel");

                // Display dialog
                this.$refs.Dialog.open();
            } catch (exp) {
                await TController.displayExceptionMessages(exp);
                this.close();
            }
        },
        close() {
            this.$refs.Dialog.close();
            if (this.$refs.Form) {
                this.$refs.Form.reset();
            }

            // Propagate event
            this.$emit("whenDialogClosed");
        },
        whenParentDialogClosed() {
            this.close();
        }
    },
};
</script>
