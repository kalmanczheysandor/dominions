<template>
    <TDialog ref="Dialog" title="Modify lizCharacter" :closeable="true" @whenDialogClosed="whenParentDialogClosed">
        <v-form ref="Form" class="TTabbedPanel" v-model="Form.validation.isValid">
            <v-tabs v-model="MainTabComponent.activeTabKey" align-tabs="end" color="primary">
                <v-tab value="Tab1">*</v-tab>
            </v-tabs>
            <v-tabs-window v-model="MainTabComponent.activeTabKey">
                <v-tabs-window-item value="Tab1">
                    <v-text-field v-model="Form.Fields.NameTextInput.value" label="Name"
                                  :rules="Form.Fields.NameTextInput.rules" variant="outlined" density="compact"
                                  clearable/>
                    <v-text-field v-model="Form.Fields.CodeTextInput.value" label="Code"
                                  :rules="Form.Fields.CodeTextInput.rules" variant="outlined" density="compact"
                                  clearable/>
                    <v-autocomplete
                            v-model="Form.Fields.VariantSelectInput.selectedKey"
                            :rules="Form.Fields.VariantSelectInput.rules"
                            :items="Form.Fields.VariantSelectInput.itemsToDisplay"
                            label="Variant" item-value="uuid" item-title="name"
                            :search-input="Form.Fields.VariantSelectInput.searchText"
                            variant="outlined" density="compact" clearable dense
                    />
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
import lizCharacterService from "@/services/ai/liz/character/LizCharacterService";



export default {
    name: "LizCharacterEditDialog",
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
                    CodeTextInput: {
                        value: "",
                        rules: [RequiredFieldRule, TextFieldRule]
                    },
                    VariantSelectInput: {
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
                await lizCharacterService.modify(this.uuid, {
                    name: this.Form.Fields.NameTextInput.value,
                    code: this.Form.Fields.CodeTextInput.value,
                    variantUuid: this.Form.Fields.VariantSelectInput.selectedKey,
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
                authService.assertEditActionGrantedOn("Ai.Liz.Character");

                // Retrieve: primary record
                const primaryResult = await lizCharacterService.accessByUuid(uuid);     // If no primary record is found at uuid than exception is thrown

                // Retrieve: meta record(s)
                const variantResult = await lizCharacterService.atEditListVariant(uuid);

                // Inject: form meta values
                this.uuid = uuid;
                this.Form.Fields.VariantSelectInput.itemsToDisplay = variantResult.data;

                // Inject: form input values
                this.Form.Fields.NameTextInput.value = primaryResult.data.name;
                this.Form.Fields.CodeTextInput.value = primaryResult.data.code;
                this.Form.Fields.VariantSelectInput.selectedKey = primaryResult.data.variantUuid;
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
