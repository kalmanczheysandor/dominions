<template>
    <TDialog ref="Dialog" title="Modify lizNeuralConcept" :closeable="true" @whenDialogClosed="whenParentDialogClosed">
        <v-form ref="Form" class="TTabbedPanel" v-model="Form.validation.isValid">
            <v-tabs v-model="MainTabComponent.activeTabKey" align-tabs="end" color="primary">
                <v-tab value="Tab1">*</v-tab>
            </v-tabs>
            <v-tabs-window v-model="MainTabComponent.activeTabKey">
                <v-tabs-window-item value="Tab1">
                    <v-text-field v-model="Form.Fields.NameTextInput.value" label="Name"
                                  :rules="Form.Fields.NameTextInput.rules" variant="outlined" density="compact"
                                  clearable/>
                    <v-text-field v-model="Form.Fields.ConfMaxIterationsPerTurn.value" label="Max iterations per turn"
                                  :rules="Form.Fields.ConfMaxIterationsPerTurn.rules" variant="outlined"
                                  density="compact"
                                  clearable/>
                    <v-text-field v-model="Form.Fields.ConfMaxTurn.value" label="Max turn"
                                  :rules="Form.Fields.ConfMaxTurn.rules" variant="outlined" density="compact"
                                  clearable/>
                    <v-text-field v-model="Form.Fields.ConfLearningRate.value" label="Learning rate"
                                  :rules="Form.Fields.ConfLearningRate.rules" variant="outlined" density="compact"
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
import {
    PositiveNumberFieldRuleWithoutZero,
    PositiveNumberFieldRuleWithZero,
    RequiredFieldRule,
    TextFieldRule
} from "@/validation.js";
import lizNeuralConceptService from "@/services/ai/liz/concept/LizNeuralConceptService";




export default {
    name: "LizNeuralConceptEditDialog",
    components: {TDialog},
    data() {
        return {
            uuid: null,
            MainTabComponent: {
                value: null
            },
            Form: {
                Fields: {
                    NameTextInput: {
                        value: "",
                        rules: [RequiredFieldRule, TextFieldRule]
                    },
                    ConfMaxIterationsPerTurn: {
                        value: "0",
                        rules: [RequiredFieldRule, PositiveNumberFieldRuleWithoutZero]
                    },
                    ConfMaxTurn: {
                        value: "0",
                        rules: [RequiredFieldRule, PositiveNumberFieldRuleWithoutZero]
                    },
                    ConfLearningRate: {
                        value: "0",
                        rules: [RequiredFieldRule, PositiveNumberFieldRuleWithoutZero]
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
                await lizNeuralConceptService.modify(this.uuid, {
                    name: this.Form.Fields.NameTextInput.value,
                    confMaxIterationsPerTurn: this.Form.Fields.ConfMaxIterationsPerTurn.value,
                    confMaxTurn: this.Form.Fields.ConfMaxTurn.value,
                    confLearningRate: this.Form.Fields.ConfLearningRate.value,
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
        async open(uuid) {
            try {

                // Checking permission
                authService.assertEditActionGrantedOn("Ai.Liz.Concept");

                // Retrieve: primary record
                const primaryResult = await lizNeuralConceptService.accessByUuid(uuid);     // If no primary record is found at uuid than exception is thrown

                // Retrieve: meta record(s)

                // Inject: form meta values
                this.uuid = uuid;

                // Inject: form input values
                this.Form.Fields.NameTextInput.value = primaryResult.data.name;
                this.Form.Fields.ConfMaxIterationsPerTurn.value = primaryResult.data.confMaxIterationsPerTurn;
                this.Form.Fields.ConfMaxTurn.value = primaryResult.data.confMaxTurn;
                this.Form.Fields.ConfLearningRate.value = primaryResult.data.confLearningRate;
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
