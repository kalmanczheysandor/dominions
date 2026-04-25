<template>
    <TDialog ref="Dialog" title="Modify lizNeuralConcept-Training" :closeable="true"
             @whenDialogClosed="whenParentDialogClosed">
        <v-form ref="Form" class="TTabbedPanel" v-model="Form.validation.isValid">
            <v-tabs v-model="MainTabComponent.activeTabKey" align-tabs="end" color="primary">
                <v-tab value="Tab1">*</v-tab>
            </v-tabs>
            <v-tabs-window v-model="MainTabComponent.activeTabKey">
                <v-tabs-window-item value="Tab1">
                    <v-progress-linear :model-value="MainTabComponent.Tab1.progress" color="amber" height="25">
                        <strong>{{MainTabComponent.Tab1.progress}}%</strong>
                    </v-progress-linear>

                    <div class="LastUpdateBar" v-if="MainTabComponent.Tab1.lastUpdatedDate">
                        Last update: {{MainTabComponent.Tab1.lastUpdatedDate.toLocaleTimeString() }}
                    </div>

                    <div class="InfoGroup">
                        <div class="GroupLabel">Status</div>
                        <span v-if="MainTabComponent.Tab1.processPhase!=null">{{ MainTabComponent.Tab1.processPhase }}</span>
                        <span v-else>UNKNOWN</span>
                    </div>
                    <div class="InfoGroup Phase">
                        <div class="GroupLabel">Tasks ({{ MainTabComponent.Tab1.taskAllCount }})</div>
                        <span>Created:{{ MainTabComponent.Tab1.taskPhaseCreatedCount }}</span> >
                        <span>Queued:{{ MainTabComponent.Tab1.taskPhaseQueuedCount }}</span> >
                        <span>Paused:{{ MainTabComponent.Tab1.taskPhasePausedCount }}</span> >
                        <span>Finished:{{ MainTabComponent.Tab1.taskPhaseFinishedCount }}</span>
                    </div>
                    <div class="InfoGroup">
                        <div class="GroupLabel">Results</div>
                        <span>Canceled:{{ MainTabComponent.Tab1.taskResultCanceledCount }}</span>
                        <span>Failed:{{ MainTabComponent.Tab1.taskResultFailedCount }}</span>
                        <span>Pending:{{ MainTabComponent.Tab1.taskResultPendingCount }}</span>
                        <span>Succeeded:{{ MainTabComponent.Tab1.taskResultSucceededCount }}</span>
                    </div>
                    <div class="ActionButtonGroup">
                        <button type="button" class="TButton ActionButton" @click="eventClickOnExecutionStartButton" :disabled="!isStartActionAllowed">
                            <v-icon icon="mdi-play-circle-outline" start></v-icon>
                            Start
                        </button>
                        <button type="button" class="TButton ActionButton" @click="eventClickOnExecutionPauseButton" :disabled="!isPauseActionAllowed">
                            <v-icon icon="mdi-pause-circle-outline" start></v-icon>
                            Pause
                        </button>
                        <button type="button" class="TButton ActionButton" @click="eventClickOnExecutionContinueButton" :disabled="!isContinueActionAllowed">
                            <v-icon icon="mdi-motion-play-outline" start></v-icon>
                            Continue
                        </button>
                        <button type="button" class="TButton ActionButton AbortButton" @click="eventClickOnExecutionCancelButton" :disabled="!isAbortActionAllowed">
                            <v-icon icon="mdi-stop-circle-outline" start></v-icon>
                            Abort
                        </button>
                    </div>
                </v-tabs-window-item>
            </v-tabs-window>
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
    name: "LizNeuralConceptExecutionDialog",
    components: {TDialog},
    data() {
        return {
            uuid: null,
            intervalId: null,
            MainTabComponent: {
                value: null,
                Tab1: {
                    progress: 0,
                    processPhase: null,
                    taskAllCount: 0,

                    taskPhaseCreatedCount: 0,
                    taskPhaseFinishedCount: 0,
                    taskPhasePausedCount: 0,
                    taskPhaseQueuedCount: 0,

                    taskResultCanceledCount: 0,
                    taskResultFailedCount: 0,
                    taskResultPendingCount: 0,
                    taskResultSucceededCount: 0,
                    lastUpdatedDate: null

                }
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
    computed: {
        isStartActionAllowed() {
            console.log("StartActionAllowed?????");

            if(this.MainTabComponent.Tab1.processPhase==null) {
                return true;
            }
            else if(this.MainTabComponent.Tab1.processPhase=='FINISHED') {
                return true;
            }
            return false;
        },
        isContinueActionAllowed() {
            if(this.MainTabComponent.Tab1.processPhase=='PAUSED') {
                return true;
            }
            return false;
        },
        isPauseActionAllowed() {
            if(this.MainTabComponent.Tab1.processPhase=='RUNNING') {
                return true;
            }
            return false;
        },
        isAbortActionAllowed() {
            if(this.MainTabComponent.Tab1.processPhase=='RUNNING') {
                return true;
            }
            else if(this.MainTabComponent.Tab1.processPhase=='PAUSED') {
                return true;
            }
            return false;
        }
    },
    emits: ["whenDialogClosed"],
    methods: {
        async reset() {
            this.$refs.Form.reset();
            this.uuid = null;
        },

        async eventClickOnExecutionStartButton() {
            try {
                // Confirming
                const confirmed = await TConfirmDialog('Do you continue?')
                if (!confirmed.ok) {
                    return;
                }

                // Save form value
                await lizNeuralConceptService.executionStart(this.uuid);

                // Display success message
                TController.displaySuccessToast("It is started!");

            } catch (exp) {
                await TController.displayExceptionMessages(exp);
            }
        },

        async eventClickOnExecutionContinueButton() {
            try {
                // Confirming
                const confirmed = await TConfirmDialog('Do you continue?')
                if (!confirmed.ok) {
                    return;
                }

                // Save form value
                await lizNeuralConceptService.executionContinue(this.uuid);

                // Display success message
                TController.displaySuccessToast("It is continued!");

            } catch (exp) {
                await TController.displayExceptionMessages(exp);
            }
        },

        async eventClickOnExecutionPauseButton() {
            try {
                // Confirming
                const confirmed = await TConfirmDialog('Do you continue?')
                if (!confirmed.ok) {
                    return;
                }

                // Save form value
                await lizNeuralConceptService.executionPause(this.uuid);

                // Display success message
                TController.displaySuccessToast("It is paused!");
            } catch (exp) {
                await TController.displayExceptionMessages(exp);
            }
        },

        async eventClickOnExecutionCancelButton() {
            try {
                // Confirming
                const confirmed = await TConfirmDialog('Do you continue?')
                if (!confirmed.ok) {
                    return;
                }

                // Save form value
                await lizNeuralConceptService.executionCancel(this.uuid);

                // Display success message
                TController.displaySuccessToast("It is cancelling!");
            } catch (exp) {
                await TController.displayExceptionMessages(exp);
            }
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

        async loadExecutionsStatus(conceptUuid) {

            try {

                const primaryResult = await lizNeuralConceptService.executionStatus(conceptUuid);     // If no primary record is found at uuid than exception is thrown
                console.log('Primary222 result :', primaryResult);
                console.log('Primary222 result:', primaryResult.data);


                this.MainTabComponent.Tab1.processPhase = primaryResult.data.processPhase;
                this.MainTabComponent.Tab1.taskAllCount = primaryResult.data.taskAllCount;

                this.MainTabComponent.Tab1.taskPhaseCreatedCount = primaryResult.data.taskPhaseCreatedCount;
                this.MainTabComponent.Tab1.taskPhaseFinishedCount = primaryResult.data.taskPhaseFinishedCount;
                this.MainTabComponent.Tab1.taskPhasePausedCount = primaryResult.data.taskPhasePausedCount;
                this.MainTabComponent.Tab1.taskPhaseQueuedCount = primaryResult.data.taskPhaseQueuedCount;

                this.MainTabComponent.Tab1.taskResultCanceledCount = primaryResult.data.taskResultCanceledCount;
                this.MainTabComponent.Tab1.taskResultFailedCount = primaryResult.data.taskResultFailedCount;
                this.MainTabComponent.Tab1.taskResultPendingCount = primaryResult.data.taskResultPendingCount;
                this.MainTabComponent.Tab1.taskResultSucceededCount = primaryResult.data.taskResultSucceededCount;


                let progress = 0;
                const allCount = this.MainTabComponent.Tab1.taskAllCount;
                const finishedCount = this.MainTabComponent.Tab1.taskPhaseFinishedCount;
                if (allCount > 0) {
                    progress = Math.round((finishedCount / allCount) * 100);
                }
                this.MainTabComponent.Tab1.progress = progress;

                //
                this.MainTabComponent.Tab1.lastUpdatedDate = new Date();


            } catch (exp) {
                await TController.displayExceptionMessages(exp);
            }
        },


        startPolling() {
            this.stopPolling();

            const poll = async () => {
                if (!this.uuid) return;
                await this.loadExecutionsStatus(this.uuid);

                this.intervalId = setTimeout(poll, 2000);
            };

            poll();
        },
        stopPolling() {
            if (this.intervalId) {
                clearTimeout(this.intervalId);
                this.intervalId = null;
            }
        },

        async open(uuid) {
            try {

                // Checking permission
                authService.assertEditActionGrantedOn("Ai.Liz.Concept");

                // Retrieve: primary record
                //const primaryResult = await lizNeuralConceptService.accessByUuid(uuid);     // If no primary record is found at uuid than exception is thrown

                // Retrieve: meta record(s)

                // Inject: form meta values
                this.uuid = uuid;

                await this.loadExecutionsStatus(uuid);
                this.startPolling();

                // Display dialog
                this.$refs.Dialog.open();
            } catch (exp) {
                await TController.displayExceptionMessages(exp);
                this.close();
            }
        },

        close() {
            this.stopPolling();

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

<style scoped>
.LastUpdateBar {
    font-size: 10px;
    color: grey;
    border-bottom: none;
    padding: 10px 0px 0px 0px;
}


.ActionButtonGroup {
    display: flex;
    align-items: center;
    margin-top: 30px;
}

.ActionButtonGroup > .TButton.ActionButton:not(:last-child) {
    margin-right: 10px;
}

.TButton.ActionButton {
    right: 0;
    height: 30px;

    border-radius: 3px;
    border: 1px #337ab7 solid;
    background-color: transparent;
    color: #337ab7;

    padding: 0px;
    margin: 0px;
    text-align: center;
    text-decoration: none;
    font-family: MainFont;
    font-size: 15px;
    box-sizing: border-box;
    cursor: pointer;
}

.TButton.ActionButton:disabled {
    color: #8c8c8c;
    border: 1px solid #8c8c8c;
}

.TButton.ActionButton:disabled:hover {
    background-color: #8c8c8c;
    color: white;
}


.TButton.ActionButton.AbortButton {
    margin-left: auto;
    background-color: #cc0000;
    border: 1px #cc0000 solid;
    color: #ffffff;
}

.TButton.ActionButton.AbortButton:hover {
    background-color: transparent;
    border: 1px #cc0000 solid;
    color: #cc0000;
}


.TButton.ActionButton.AbortButton:disabled {
    color: white;
    border: 1px solid white;
    background-color: #8c8c8c;
}

.TButton.ActionButton.AbortButton:disabled:hover {
    background-color: #8c8c8c;
    color: white;
}


.InfoGroup {
    font-size: 10px;
    color: grey;
    border-bottom: none;
    padding: 10px 0px 0px 0px;
}

.InfoGroup > .GroupLabel {
    font-weight: bold;
    display: block;
}

.InfoGroup > span {
    padding: 2px 5px 2px 5px;
    border-right: 1px grey solid;
}

.InfoGroup.Phase > span,
.InfoGroup.Phase > span:last-of-type {
    padding: 2px 5px 2px 5px;
    border: 1px grey solid;
    border-radius: 2px;
}

.InfoGroup > span:last-of-type {
    border: none;
}

</style>
