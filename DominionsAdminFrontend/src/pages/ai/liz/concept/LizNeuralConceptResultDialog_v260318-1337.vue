<template>
    <TDialog ref="Dialog" title="Result lizNeuralConcept-Training" :closeable="true" @whenDialogClosed="whenParentDialogClosed">
        <v-form ref="Form" class="TTabbedPanel" v-model="Form.validation.isValid">
            <v-tabs v-model="MainTabComponent.activeTabKey" align-tabs="end" color="primary">
                <v-tab value="Tab1">*</v-tab>
            </v-tabs>
            <v-tabs-window v-model="MainTabComponent.activeTabKey">
                <v-tabs-window-item value="Tab1">
                    <apexchart type="line" height="350" :options="Chart.chartOptions" :series="Chart.series"></apexchart>
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
    name: "LizNeuralConceptResultDialog",
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

            Chart: {
                chartOptions: {
                    chart: {
                        height: 350,
                        type: 'line',
                        zoom: {
                            enabled: true
                        },
                        offsetX: 0
                    },
                    dataLabels: {
                        enabled: false
                    },
                    stroke: {
                        curve: 'straight'
                    },
                    title: {
                        text: 'Product Trends by Month',
                        align: 'left'
                    },
                    grid: {
                        row: {
                            colors: ['#f3f3f3', 'transparent'], // takes an array which will be repeated on columns
                            opacity: 0.5
                        },
                        padding: {
                            left: 20
                        }
                    },
                    xaxis: {
                        categories: [],
                        title: {
                            text: "Training turns",
                            style: {
                                color: '#008FFB',
                            }
                        },
                    },
                    yaxis: [
                        {
                            min: 0,
                            max: 100,
                            seriesName: 'Income',
                            axisTicks: {
                                show: true,
                            },
                            axisBorder: {
                                show: true,
                                color: '#008FFB'
                            },
                            labels: {
                                minWidth: 50,
                                formatter: function (value) {
                                    return value + "%";
                                },
                                style: {
                                    colors: '#008FFB',
                                    border: '1px solid #008FFB',
                                }
                            },
                            title: {
                                text: "Precision (%)",
                                style: {
                                    color: '#008FFB',
                                }
                            },
                            tooltip: {
                                enabled: true
                            },

                            // tooltip: {
                            //     fixed: {
                            //         enabled: true,
                            //         position: 'topLeft', // topRight, topLeft, bottomRight, bottomLeft
                            //         offsetY: 30,
                            //         offsetX: 60
                            //     },
                            // },
                            legend: {
                                horizontalAlign: 'right',
                                offsetX: 40
                            }
                        }
                    ],

                },
                series: [{
                    name: "Precision",
                    data: []
                }],
            }

        };
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

                // Hide dialog
                this.close();
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

                // Hide dialog
                this.close();
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

                // Hide dialog
                this.close();
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

                // Hide dialog
                this.close();
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
        async open(uuid) {
            try {

                // Checking permission
                authService.assertEditActionGrantedOn("Ai.Liz.Concept");

                // Retrieve: primary record
                const primaryResult = await lizNeuralConceptService.snapshotChartData(uuid);     // If no primary record is found at uuid than exception is thrown
                console.log("RESULT",primaryResult);

                // Retrieve: meta record(s)

                // Inject: form meta values
                this.uuid = uuid;

                // Inject: form input values
                // this.Form.Fields.NameTextInput.value = primaryResult.data.name;
                // this.Form.Fields.ConfMaxIterationsPerTurn.value = primaryResult.data.confMaxIterationsPerTurn;
                // this.Form.Fields.ConfMaxTurn.value = primaryResult.data.confMaxTurn;
                // this.Form.Fields.ConfLearningRate.value = primaryResult.data.confLearningRate;
                // this.Form.Fields.EnabledCheckInput.isChecked = primaryResult.data.enabled;

                this.Chart.chartOptions.xaxis.categories=[];
                this.Chart.series[0].data=[];
                if (Array.isArray(primaryResult.data.precisions)) {
                    primaryResult.data.precisions.forEach((item, index) => {
                        this.Chart.chartOptions.xaxis.categories.push((index+1));
                        this.Chart.series[0].data.push((item*100).toFixed(2));
                    });
                }

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
