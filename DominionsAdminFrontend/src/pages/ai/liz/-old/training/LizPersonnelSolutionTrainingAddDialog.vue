<template>
    <TDialog ref="Dialog" title="New lizPersonnelSolution" :closeable="true" @whenDialogClosed="whenParentDialogClosed">
        <v-form ref="Form" class="TTabbedPanel"  v-model="Form.validation.isValid">
            <v-tabs v-model="MainTabComponent.activeKey" align-tabs="end" color="primary">
                <v-tab value="Tab1">*</v-tab>
                <v-tab value="Tab2">Batch configurations</v-tab>
            </v-tabs>
            <v-tabs-window v-model="MainTabComponent.activeKey" style="min-height: 200px;">
                <v-tabs-window-item value="Tab1">
                    <v-text-field v-model="Form.Fields.TitleTextInput.value" label="Title"
                                  :rules="Form.Fields.TitleTextInput.rules" variant="outlined" density="compact"
                                  clearable/>
                    <v-checkbox v-model="Form.Fields.EnabledCheckInput.isChecked" label="Enabled" variant="outlined"
                                density="compact" clearable/>
                </v-tabs-window-item>
                <v-tabs-window-item value="Tab2">
                    hello
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
    PositiveNumberFieldRuleWithoutZero,
    RequiredFieldRule,
    TextFieldRule
} from "@/validation.js";
import lizPersonnelSolutionTrainingService from "@/services/ai/liz/training/LizPersonnelSolutionTrainingService";

export default {
    name: "LizPersonnelSolutionTrainingAddDialog",
    components: {TDialog},
    data() {
        return {
            parentUuid: null,
            MainTabComponent: {
                activeTabKey: null,
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
                await lizPersonnelSolutionTrainingService.save(this.parentUuid,{
                    title: this.Form.Fields.TitleTextInput.value,
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
        async open(parentUuid) {
            try {
                // Checking permission
                authService.assertAddActionGrantedOn("Ai.Liz.Personnel.Solution.Training");

                // Inject: form meta values
                this.parentUuid = parentUuid;

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
            this.parentUuid = null;

            // Propagate event
            this.$emit("whenDialogClosed");
        },
        whenParentDialogClosed() {
            this.close();
        }
    },
};
</script>
