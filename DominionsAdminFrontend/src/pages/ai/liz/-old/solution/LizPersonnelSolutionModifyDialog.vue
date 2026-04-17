<template>
    <TDialog ref="Dialog" title="Modify lizPersonnelSolution" :closeable="true" @whenDialogClosed="whenParentDialogClosed">
        <v-form ref="Form" class="TTabbedPanel" v-model="Form.validation.isValid">
            <v-tabs v-model="MainTabComponent.activeTabKey" align-tabs="end" color="primary">
                <v-tab value="Tab1">*</v-tab>
                <v-tab value="Tab2">Runtime configurations</v-tab>
                <v-tab value="Tab3">Training configurations</v-tab>
            </v-tabs>
            <v-tabs-window v-model="MainTabComponent.activeTabKey" style="min-height: 200px;">
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
                <v-tabs-window-item value="Tab3">

                    <v-text-field v-model="Form.Fields.ConfTrainingTurnMax.value" label="Max turn"
                                  :rules="Form.Fields.ConfTrainingTurnMax.rules" variant="outlined" density="compact"
                                  clearable/>
                    <v-text-field v-model="Form.Fields.ConfTrainingTurnIteration.value" label="Iteration per turn"
                                  :rules="Form.Fields.ConfTrainingTurnIteration.rules" variant="outlined"
                                  density="compact" clearable/>
                    <v-text-field v-model="Form.Fields.ConfTrainingLearningRate.value" label="Learning rate"
                                  :rules="Form.Fields.ConfTrainingLearningRate.rules" variant="outlined"
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
import {PositiveNumberFieldRuleWithoutZero, RequiredFieldRule, TextFieldRule} from "@/validation.js";
import lizPersonnelSolutionService from "@/services/ai/liz/solution/LizPersonnelSolutionService";

export default {
    name: "LizPersonnelSolutionEditDialog",
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
                    ConfTrainingTurnMax: {
                        value: "1",
                        rules: [RequiredFieldRule, PositiveNumberFieldRuleWithoutZero]
                    },
                    ConfTrainingTurnIteration: {
                        value: "1",
                        rules: [RequiredFieldRule, PositiveNumberFieldRuleWithoutZero]
                    },
                    ConfTrainingLearningRate: {
                        value: "0.1",
                        rules: [
                            RequiredFieldRule,
                            PositiveNumberFieldRuleWithoutZero,
                            (value) => {
                                if (isNaN(value)) {
                                    return 'Field must be in number format!';
                                }
                                if (!(value > 0)) {
                                    return 'Field must be grater than 0 !';
                                }
                                if (value > 1) {
                                    return 'Field must be not grater than 1';
                                }

                                return true;
                            }
                        ]
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
                await lizPersonnelSolutionService.modify(this.parentUuid,this.uuid, {
                    title: this.Form.Fields.TitleTextInput.value,
                    confTrainingTurnMax: this.Form.Fields.ConfTrainingTurnMax.value,
                    confTrainingTurnIteration: this.Form.Fields.ConfTrainingTurnIteration.value,
                    confTrainingLearningRate: this.Form.Fields.ConfTrainingLearningRate.value,
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
                authService.assertEditActionGrantedOn("Ai.Liz.Personnel.Solution");

                // Retrieve: primary record
                const primaryResult = await lizPersonnelSolutionService.accessByUuid(parentUuid,uuid);     // If no primary record is found at uuid than exception is thrown

                // Retrieve: meta record(s)

                // Inject: form meta values
                this.uuid = uuid;
                this.parentUuid = parentUuid;


                // Inject: form input values
                this.Form.Fields.TitleTextInput.value = primaryResult.data.title;
                this.Form.Fields.ConfTrainingTurnMax.value = primaryResult.data.confTrainingTurnMax;
                this.Form.Fields.ConfTrainingTurnIteration.value = primaryResult.data.confTrainingTurnIteration;
                this.Form.Fields.ConfTrainingLearningRate.value = primaryResult.data.confTrainingLearningRate;
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
