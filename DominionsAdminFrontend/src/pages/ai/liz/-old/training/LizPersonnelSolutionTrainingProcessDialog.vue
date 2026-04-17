<template>
    <TDialog ref="Dialog" title="Process" :closeable="true" @whenDialogClosed="whenParentDialogClosed">
        <v-form ref="Form" class="TTabbedPanel" v-model="Form.validation.isValid">
            <v-tabs v-model="MainTabComponent.activeTabKey" align-tabs="end" color="primary">
                <v-tab value="Tab1">*</v-tab>
            </v-tabs>
            <v-tabs-window v-model="MainTabComponent.activeTabKey" style="min-height: 200px;">
                <v-tabs-window-item value="Tab1">
                    <v-text-field v-model="Form.Fields.TitleTextInput.value" label="Title"
                                  :rules="Form.Fields.TitleTextInput.rules" variant="outlined" density="compact"
                                  clearable/>
                    <v-checkbox v-model="Form.Fields.EnabledCheckInput.isChecked" label="Enabled" variant="outlined"
                                density="compact" clearable/>
                </v-tabs-window-item>
               
            </v-tabs-window>
            <div class="ActionButtonBar">
                <button type="button" class="TButton CancelButton" @click="eventClickOnCancelButton">Cancel</button>
                <button type="button" class="TButton ModifyButton" @click="eventClickOnModifyButton">Modify</button>
            </div>
        </v-form>
    </TDialog>
</template>

<script>
import TDialog from "@/framework/component/TDialog/TDialog.vue";

import {authService} from "@/services/auth/AuthService";
import TConfirmDialog from "@/framework/component/TConfirmDialog/TConfirmDialog";
import TController from "@/framework/TController";
import { RequiredFieldRule, TextFieldRule} from "@/validation.js";
import lizPersonnelSolutionTrainingService from "@/services/ai/liz/training/LizPersonnelSolutionTrainingService";


export default {
    name: "LizPersonnelSolutionTrainingProcessDialog",
    components: {TDialog},
    data() {
        return {
            uuid: null,
            parentUuid: null,
            MainTabComponent: {
                value: null
            },
            Form: {
                Fields: {
                    TitleTextInput: {
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
        async reset() {
            this.$refs.Form.reset();
            this.uuid = null;
            this.parentUuid = null;
        },
        async eventClickOnModifyButton() {
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
                await lizPersonnelSolutionTrainingService.modify(this.parentUuid,this.uuid, {
                    title: this.Form.Fields.TitleTextInput.value,
                    enabled: this.Form.Fields.EnabledCheckInput.isChecked
                });

                // Display success message
                TController.displayModifiedToast();

                // Hide dialog
                this.close();
            } catch (exp) {
                await TController.displayExceptionMessages(exp);
            }
        },
        async eventClickOnCancelButton() {
            this.close();
        },
        async open(parentUuid,uuid) {
            try {

                // Checking permission
                authService.assertEditActionGrantedOn("Ai.Liz.Personnel.Solution.Training");

                // Retrieve: primary record
                const primaryResult = await lizPersonnelSolutionTrainingService.accessByUuid(parentUuid,uuid);     // If no primary record is found at uuid than exception is thrown

                // Retrieve: meta record(s)

                // Inject: form meta values
                this.uuid = uuid;
                this.parentUuid = parentUuid;


                // Inject: form input values
                this.Form.Fields.TitleTextInput.value = primaryResult.data.title;
                this.Form.Fields.EnabledCheckInput.isChecked = primaryResult.data.enabled;

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
        },

    },
};
</script>
