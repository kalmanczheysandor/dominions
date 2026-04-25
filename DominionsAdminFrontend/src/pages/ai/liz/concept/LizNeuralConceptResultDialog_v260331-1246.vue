<template>
    <TDialog ref="Dialog" title="Result lizNeuralConcept-Training" :closeable="true"
             @whenDialogClosed="whenParentDialogClosed">
        <v-form ref="Form" class="TTabbedPanel" v-model="Form.validation.isValid">
            <v-tabs v-model="MainTabComponent.activeTabKey" align-tabs="end" color="primary">
                <v-tab value="Tab1">*</v-tab>
            </v-tabs>
            <v-tabs-window v-model="MainTabComponent.activeTabKey">

                <v-tabs-window-item value="Tab1">
                    <v-row>
                        <v-col>
                            <v-autocomplete
                                    v-model="Form.Fields.ScenarioSelectInput.selectedKey"
                                    :rules="Form.Fields.ScenarioSelectInput.rules"
                                    :items="Form.Fields.ScenarioSelectInput.itemsToDisplay"
                                    label="Scenario" item-value="uuid"
                                    item-title="caption"
                                    :search-input="Form.Fields.ScenarioSelectInput.searchText"
                                    variant="outlined" density="compact" clearable dense
                                    @update:modelValue="eventScenarioChange"
                            />
                        </v-col>
                        <v-col>
                            <v-autocomplete
                                    v-model="Form.Fields.PlayerSelectInput.selectedKey"
                                    :rules="Form.Fields.PlayerSelectInput.rules"
                                    :items="Form.Fields.PlayerSelectInput.itemsToDisplay"
                                    label="Player" item-value="uuid"
                                    :item-title="item => `${item.caption} (${item.snapshotCount})`"
                                    :search-input="Form.Fields.PlayerSelectInput.searchText"
                                    variant="outlined" density="compact" clearable dense
                       
                            />
                        </v-col>
                        <v-col>
                            <button type="button" class="TButton ShowButton" @click="eventClickOnShowButton">Show
                            </button>
                        </v-col>
                    </v-row>
                    <apexchart ref="TheChart"
                               type="line" height="350" :options="Chart.chartOptions"
                               :series="Chart.series"></apexchart>
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
                    ScenarioSelectInput: {
                        itemsToDisplay: [],
                        searchText: '',
                        selectedKey: null,
                        rules: [RequiredFieldRule]
                    },

                    PlayerSelectInput: {
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
                    plotOptions: {
                        bar: {
                            horizontal: false,
                            columnWidth: '55%',
                            borderRadius: 5,
                            borderRadiusApplication: 'end'
                        },
                    },
                    dataLabels: {
                        enabled: false
                    },
                    stroke: {
                        curve: 'straight',
                        width: 3
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
                        // padding: {
                        //     left: 0
                        // }
                    },
                    // markers: {
                    //     size: 4
                    // },
                    markers: {
                        size: 2,
                        hover: {
                            size: 6
                        }
                    },

                    xaxis: {
                        type: 'numeric',
                        labels: {
                            formatter: function (value) {
                                return Math.round(value);
                            }
                        },
                        tooltip: {
                            enabled: false,
                        },
                        title: {
                            text: "Training turns",
                            style: {
                                color: '#008FFB',
                            }
                        },
                        // tickAmount:3
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
                            // tooltip: {
                            //     enabled: true
                            // },

                            // tooltip: {
                            //     followCursor: true,
                            //     intersect: true,
                            //     shared: false,
                            //     offsetX: 10,
                            //     offsetY: -10,
                            //     y: {
                            //         formatter: (val) => val + "%"
                            //     },
                            //     x: {
                            //         formatter: function (value, opts) {
                            //             console.log(value, opts);
                            //             return "X: " + value;
                            //         }
                            //     }
                            // },
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
                                // offsetX: 40
                            }
                        }
                    ],

                    tooltip: {   // ← IDE!
                        followCursor: true,
                        intersect: true,
                        shared: false,
                        offsetX: 10,
                        offsetY: -10,

                        x: {
                            formatter: function (value, opts) {
                                console.log(value, opts);
                                return "Turn: " + value;
                            }
                        },
                        y: {
                            formatter: (val) => val + "%"
                        }
                    },
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

        async eventScenarioChange(selectedScenarioKey) {
            try {
                // Checking permission
                authService.assertEditActionGrantedOn("Ai.Liz.Concept");

                // It helps to block the model update re-invocation after window close-event
                if(selectedScenarioKey==null) {
                    return;
                }

                // Retrieve: meta record(s)
                const historyPlayerResult = await lizNeuralConceptService.atResultListHistoryPlayer(this.uuid,selectedScenarioKey);

                // Inject: form meta values
                this.Form.Fields.PlayerSelectInput.itemsToDisplay = historyPlayerResult.data;

                // Inject: form input values
                this.Form.Fields.PlayerSelectInput.selectedKey = historyPlayerResult.data.userUuid;

            } catch (exp) {
                await TController.displayExceptionMessages(exp);
            }
        },

        async eventClickOnShowButton() {
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

                const conceptUuid = this.uuid;
                const scenarioUuid = this.Form.Fields.ScenarioSelectInput.selectedKey;
                const playerUuid = this.Form.Fields.PlayerSelectInput.selectedKey;

                // Retrieve: primary record
                console.log("conceptUuid",conceptUuid);
                console.log("scenarioUuid",scenarioUuid);
                console.log("playerUuid",playerUuid);

                const primaryResult = await lizNeuralConceptService.snapshotChartData(conceptUuid,scenarioUuid,playerUuid);     // If no primary record is found at uuid than exception is thrown


                this.Chart.series[0].data = [];
                if (Array.isArray(primaryResult.data.precisions)) {
                    primaryResult.data.precisions.forEach((item, index) => {
                        this.Chart.series[0].data.push({
                            x: index + 1,
                            y: Math.round(item * 10000) / 100
                        })
                    });
                }
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

                // Retrieve: meta record(s)
                const historyScenarioResult = await lizNeuralConceptService.atResultListHistoryScenario(uuid);

                console.log("scenario:", historyScenarioResult);

                // Inject: form meta values
                this.Form.Fields.ScenarioSelectInput.itemsToDisplay = historyScenarioResult.data;

                // Inject: form meta values
                this.uuid = uuid;

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

<style scoped>
.TButton.ShowButton {
    background-color: transparent;
    border: 1px #337ab7 solid;
    color: #337ab7;
    width: 100%;
}

.TButton.ShowButton:hover {
    color: white;
}
</style>