<template>
    <TDialog ref="Dialog" title="Modify scenario" :closeable="true" @whenDialogClosed="whenParentDialogClosed">
        <v-form ref="Form" class="TTabbedPanel" v-model="Form.validation.isValid">
            <v-tabs v-model="MainTabComponent.activeTabKey" align-tabs="end" color="primary">
                <v-tab value="Tab1">*</v-tab>
                <v-tab value="Tab2">Map</v-tab>
            </v-tabs>
            <v-tabs-window v-model="MainTabComponent.activeTabKey">
                <v-tabs-window-item value="Tab1">

                    <v-row>
                        <v-col>
                            <CroppieInput ref="ScenarioImage"
                                          v-model="Form.Fields.ScenarioImageCroppieInput.base64Content"
                                          :rules="Form.Fields.ScenarioImageCroppieInput.rules"/>
                        </v-col>
                        <v-col>


                            <v-text-field v-model="Form.Fields.TitleTextInput.value" label="Title"
                                          :rules="Form.Fields.TitleTextInput.rules" variant="outlined" density="compact"
                                          clearable/>
                            <v-autocomplete
                                    v-model="Form.Fields.DifficultySelectInput.selectedKey"
                                    :rules="Form.Fields.DifficultySelectInput.rules"
                                    :items="Form.Fields.DifficultySelectInput.itemsToDisplay"
                                    label="Difficulty" item-value="key" item-title="title"
                                    :search-input="Form.Fields.DifficultySelectInput.searchText"
                                    variant="outlined" density="compact" clearable dense
                            />
                            <v-checkbox v-model="Form.Fields.EnabledCheckInput.isChecked" label="Enabled"
                                        variant="outlined" density="compact" clearable/>
                            <v-checkbox v-model="Form.Fields.PublishedCheckInput.isChecked" label="Published"
                                        variant="outlined" density="compact" clearable/>
                        </v-col>
                    </v-row>
                </v-tabs-window-item>
                <v-tabs-window-item value="Tab2">
                    <v-row>
                        <v-col>
                            <v-textarea v-model="Form.Fields.MapTextareaInput.value" label="Map json content"
                                        :rules="Form.Fields.MapTextareaInput.rules" variant="outlined"
                                        density="compact"/>
                        </v-col>
                    </v-row>
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
import {gameScenarioService} from "@/services/game/scenario/GameScenarioService.js";
import {authService} from "@/services/auth/AuthService";
import TConfirmDialog from "@/framework/component/TConfirmDialog/TConfirmDialog";
import TController from "@/framework/TController";
import CroppieInput from "@/framework/component/CroppieInput/CroppieInput.vue";
import {RequiredFieldRule, TextareaFieldRule, TextFieldRule} from "@/validation.js";
import backendConfiguration from "@/configurations/backendConfiguration";


export default {
    name: "ScenarioEditDialog",
    components: {CroppieInput, TDialog},
    emits:["whenDialogClosed"],
    data() {
        return {
            uuid: null,
            MainTabComponent: {
                value: null
            },
            Form: {
                Fields: {
                    ScenarioImageCroppieInput: {
                        base64Content: "",
                        rules: [RequiredFieldRule]
                    },
                    TitleTextInput: {
                        value: "",
                        rules: [RequiredFieldRule, TextFieldRule]
                    },

                    EnabledCheckInput: {
                        isChecked: true,
                        rules: []
                    },
                    PublishedCheckInput: {
                        isChecked: true,
                        rules: []
                    },

                    MapTextareaInput: {
                        value: "",
                        rules: [RequiredFieldRule, TextareaFieldRule]
                    },

                    DifficultySelectInput: {
                        itemsToDisplay: [],
                        searchText: '',
                        selectedKey: null,
                        rules: [RequiredFieldRule]
                    }
                },
                validation: {
                    isValid: false,
                }
            },
        };
    },
    methods:
            {
                async reset() {
                    this.$refs.Form.reset();
                    this.uuid = null;
                }
                ,
                async eventClickOnModifyButton() {
                    try {

                        // Confirming
                        const confirmed = await TConfirmDialog('Do you continue?')
                        if (!confirmed.ok) {
                            return;
                        }

                        // Croppie validation
                        if (!this.$refs.ScenarioImage.validate()) {
                            return;
                        }

                        // Form validation
                        await this.$refs.Form.validate();
                        if (!this.Form.validation.isValid) {
                            return;
                        }

                        // Save form value
                        await gameScenarioService.modify(this.uuid, {
                            title: this.Form.Fields.TitleTextInput.value,
                            difficulty: this.Form.Fields.DifficultySelectInput.selectedKey,
                            enabled: this.Form.Fields.EnabledCheckInput.isChecked,
                            published: this.Form.Fields.PublishedCheckInput.isChecked,
                            gameMap:this.Form.Fields.MapTextareaInput.value,
                            description: "",
                            imageBase64: this.Form.Fields.ScenarioImageCroppieInput.base64Content
                        });

                        // Display success message
                        TController.displayModifiedToast();

                        // Hide dialog
                        this.close();
                    } catch (exp) {
                        await TController.displayExceptionMessages(exp);
                    }
                }
                ,
                async eventClickOnCancelButton() {
                    this.close();
                }
                ,
                async open(uuid) {
                    try {

                        // Checking permission
                        authService.assertEditActionGrantedOn("Game.Scenario");

                        // Retrieve: primary record
                        const primaryResult = await gameScenarioService.accessByUuid(uuid);     // If no primary record is found at uuid than exception is thrown

                        // Retrieve: meta record(s)
                        const difficultyResult = await gameScenarioService.atEditListDifficulty();

                        // Inject: form meta values
                        this.uuid = uuid;
                        this.Form.Fields.DifficultySelectInput.itemsToDisplay = difficultyResult.data;

                        // Inject: form input values
                        this.Form.Fields.TitleTextInput.value = primaryResult.data.title;
                        this.Form.Fields.DifficultySelectInput.selectedKey = primaryResult.data.difficulty;
                        this.Form.Fields.EnabledCheckInput.isChecked = primaryResult.data.enabled;
                        this.Form.Fields.PublishedCheckInput.isChecked = primaryResult.data.published;
                        this.Form.Fields.MapTextareaInput.value = primaryResult.data.gameMap;

                        // Display dialog
                        await this.$refs.Dialog.open();
                        await this.$refs.ScenarioImage.openFile(backendConfiguration.BACKEND_BASE_URL+'/game/scenario/'+uuid+'/image/main');

                    } catch (exp) {
                        await TController.displayExceptionMessages(exp);
                        this.close();
                    }
                }
                ,
                close() {
                    this.$refs.Dialog.close();
                    if (this.$refs.Form) {
                        this.$refs.Form.reset();
                    }

                    // Propagate event
                    this.$emit("whenDialogClosed");
                }
                ,
                whenParentDialogClosed() {
                    this.close();
                }
                ,
            }
    ,
};
</script>
