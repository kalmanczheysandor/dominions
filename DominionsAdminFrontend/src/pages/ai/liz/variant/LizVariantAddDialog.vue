<template>
    <TDialog ref="Dialog" title="New lizVariant" :closeable="true" @whenDialogClosed="whenParentDialogClosed">
        <v-form ref="Form" class="TTabbedPanel" v-model="Form.validation.isValid">
            <v-tabs v-model="MainTabComponent.activeKey" align-tabs="end" color="primary">
                <v-tab value="Tab1">*</v-tab>
            </v-tabs>
            <v-tabs-window v-model="MainTabComponent.activeKey">
                <v-tabs-window-item value="Tab1">
                    <v-text-field v-model="Form.Fields.NameTextInput.value" label="Name"
                                  :rules="Form.Fields.NameTextInput.rules" variant="outlined" density="compact"
                                  clearable/>
                    <v-text-field v-model="Form.Fields.ConfMaxIterationsPerTurn.value" label="Max search depth"
                                  :rules="Form.Fields.ConfMaxIterationsPerTurn.rules" variant="outlined" density="compact"
                                  clearable/>
                    <v-autocomplete
                            v-model="Form.Fields.ConceptSelectInput.selectedKey"
                            :rules="Form.Fields.ConceptSelectInput.rules"
                            :items="Form.Fields.ConceptSelectInput.itemsToDisplay"
                            label="Concept" item-value="uuid" item-title="name"
                            :search-input="Form.Fields.ConceptSelectInput.searchText"
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
import lizVariantService from "@/services/ai/liz/variant/LizVariantService";
import lizCharacterService from "@/services/ai/liz/character/LizCharacterService";



export default {
    name: "LizVariantAddDialog",
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
                    ConfMaxIterationsPerTurn: {
                        value: "0",
                        rules: [RequiredFieldRule, PositiveNumberFieldRuleWithZero]
                    },
                    ConceptSelectInput: {
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
                await lizVariantService.save({
                    name: this.Form.Fields.NameTextInput.value,
                    confSearchDepth: this.Form.Fields.ConfMaxIterationsPerTurn.value,
                    conceptUuid: this.Form.Fields.ConceptSelectInput.selectedKey,
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
                authService.assertAddActionGrantedOn("Ai.Liz.Variant");

                // Retrieve: meta record(s)
                const conceptResult = await lizVariantService.atAddListConcept();

                // Inject: form meta values
                this.Form.Fields.ConceptSelectInput.itemsToDisplay = conceptResult.data;

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
