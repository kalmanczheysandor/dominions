<template>
    <TDialog ref="Dialog" title="New ottoVariant" :closeable="true" @whenDialogClosed="whenParentDialogClosed">
        <v-form ref="Form" class="TTabbedPanel" v-model="Form.validation.isValid">
            <v-tabs v-model="MainTabComponent.activeKey" align-tabs="end" color="primary">
                <v-tab value="Tab1">*</v-tab>
            </v-tabs>
            <v-tabs-window v-model="MainTabComponent.activeKey">
                <v-tabs-window-item value="Tab1">
                    <v-text-field v-model="Form.Fields.NameTextInput.value" label="Name"
                                  :rules="Form.Fields.NameTextInput.rules" variant="outlined" density="compact"
                                  clearable/>
                    <v-autocomplete
                            v-model="Form.Fields.EvaluatorSelectInput.selectedKey"
                            :rules="Form.Fields.EvaluatorSelectInput.rules"
                            :items="Form.Fields.EvaluatorSelectInput.itemsToDisplay"
                            label="Evaluator" item-value="uuid" item-title="name"
                            :search-input="Form.Fields.EvaluatorSelectInput.searchText"
                            variant="outlined" density="compact" clearable dense
                    />
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
    PositiveNumberFieldRuleWithZero,
    RequiredFieldRule,
    TextFieldRule
} from "@/validation.js";
import ottoVariantService from "@/services/ai/otto/variant/OttoVariantService";



export default {
    name: "OttoVariantAddDialog",
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
                    EvaluatorSelectInput: {
                        itemsToDisplay: [],
                        searchText: '',
                        selectedKey: null,
                        rules: [RequiredFieldRule]
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
                await ottoVariantService.save({
                    name: this.Form.Fields.NameTextInput.value,
                    evaluatorUuid: this.Form.Fields.EvaluatorSelectInput.selectedKey,
                    enabled: this.Form.Fields.EnabledCheckInput.isChecked
                });

                // Display success message
                TController.displaySavedToast();

                // Hide dialog
                this.close();
            } catch (exp) {
                await TController.handleExceptions(exp);
            }
        },
        eventClickOnCancelButton() {
            this.close();
        },
        async open() {
            try {
                // Checking permission
                authService.assertAddActionGrantedOn("Ai.Otto.Variant");

                // Retrieve: meta record(s)
                const evaluatorResult = await ottoVariantService.atAddListEvaluator();

                // Inject: form meta values
                this.Form.Fields.EvaluatorSelectInput.itemsToDisplay = evaluatorResult.data;

                // Display dialog
                this.$refs.Dialog.open();
            } catch (exp) {
                await TController.handleExceptions(exp);
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
