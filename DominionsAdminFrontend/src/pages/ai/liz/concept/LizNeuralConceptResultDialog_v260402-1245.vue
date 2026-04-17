<template>
    <TDialog ref="Dialog" title="Result lizNeuralConcept-Training" :closeable="true"
             @whenDialogClosed="whenParentDialogClosed">
        <v-form ref="Form" class="TTabbedPanel" v-model="Form.validation.isValid">
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
                            @update:modelValue="eventPlayerChange"
                    />
                </v-col>
            </v-row>
            <v-tabs v-model="MainTabComponent.activeTabKey" align-tabs="end" color="primary">
                <v-tab value="Tab1">Snapshots</v-tab>
                <v-tab value="Tab2">Executions</v-tab>
            </v-tabs>
            <v-tabs-window v-model="MainTabComponent.activeTabKey">

                <v-tabs-window-item value="Tab1">
                    <div id="InfoBar">
                        <span v-if="Form.turnCount!=null">Turn count:{{ Form.turnCount }}</span>
                        <span v-else>Turn count: unknown </span>

                        <span v-if="Form.turnBest!=null">Best turn:{{ Form.turnBest }}</span>
                        <span v-else>Best turn: unknown </span>

                        <span v-if="Form.bestPrecision!=null">Best precision:{{ Form.bestPrecision * 100 }}%</span>
                        <span v-else>Best precision: unknown </span>
                    </div>
                    <apexchart ref="SnapshotsChart"
                               type="line" height="350"
                               :options="SnapshotsChart.chartOptions"
                               :series="SnapshotsChart.series">
                    </apexchart>
                </v-tabs-window-item>
                <v-tabs-window-item value="Tab2">
                    <apexchart ref="ExecutionsChart"
                               type="bar"
                               height="350"
                               :options="ExecutionsChart.chartOptions"
                               :series="ExecutionsChart.series">
                    </apexchart>
                </v-tabs-window-item>
            </v-tabs-window>
        </v-form>
    </TDialog>
</template>

<script>
import TDialog from "@/framework/component/TDialog/TDialog.vue";

import {authService} from "@/services/auth/AuthService";
import TController from "@/framework/TController";
import {
    RequiredFieldRule
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
                turnCount: null,
                turnBest: null,
                bestPrecision: null,
                validation: {
                    isValid: false,
                }
            },

            SnapshotsChart: {
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
                        text: 'Progress',
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
            },
            ExecutionsChart: {
                series: [
                    {
                        name: 'Net Profit',
                        data: [44, 55, 57, 56, 61, 58, 63, 60, 66]
                    }, {
                        name: 'Revenue',
                        data: [76, 85, 101, 98, 87, 105, 91, 114, 94]
                    }, {
                        name: 'Free Cash Flow',
                        data: [35, 41, 36, 26, 45, 48, 52, 53, 41]
                    }],
                chartOptions: {
                    chart: {
                        type: 'bar',
                        height: 350
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
                        show: true,
                        width: 2,
                        colors: ['transparent']
                    },
                    xaxis: {
                        categories: ['Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct'],
                    },
                    yaxis: {
                        title: {
                            text: '$ (thousands)'
                        }
                    },
                    fill: {
                        opacity: 1
                    },
                    tooltip: {
                        y: {
                            formatter: function (val) {
                                return "$ " + val + " thousands"
                            }
                        }
                    }
                },
            },
        }
    },
    emits: ["whenDialogClosed"],
    methods:
            {
                async reset() {
                    this.$refs.Form.reset();
                    this.uuid = null;
                }
                ,

                async eventScenarioChange(selectedScenarioKey) {
                    try {
                        // Checking permission
                        authService.assertEditActionGrantedOn("Ai.Liz.Concept");

                        // It helps to block the model update re-invocation after window close-event
                        if (selectedScenarioKey == null) {
                            await this.flushSnapshotsChartContent();
                            return;
                        }

                        // Retrieve: meta record(s)
                        const historyPlayerResult = await lizNeuralConceptService.atResultListHistoryPlayer(this.uuid, selectedScenarioKey);

                        // Inject: form meta values
                        this.Form.Fields.PlayerSelectInput.itemsToDisplay = historyPlayerResult.data;

                        // Inject: form input values
                        this.Form.Fields.PlayerSelectInput.selectedKey = historyPlayerResult.data.userUuid;

                    } catch (exp) {
                        await TController.displayExceptionMessages(exp);
                    }
                }
                ,

                async eventPlayerChange(selectedPlayerKey) {
                    try {
                        // Checking permission
                        authService.assertEditActionGrantedOn("Ai.Liz.Concept");

                        // It helps to block the model update re-invocation after window close-event
                        if (selectedPlayerKey == null) {
                            await this.flushSnapshotsChartContent();
                            return;
                        }

                        // Form validation
                        await this.$refs.Form.validate();
                        if (!this.Form.validation.isValid) {
                            await this.flushSnapshotsChartContent();
                            return;
                        }

                        //
                        const conceptUuid = this.uuid;
                        const scenarioUuid = this.Form.Fields.ScenarioSelectInput.selectedKey;
                        const playerUuid = this.Form.Fields.PlayerSelectInput.selectedKey;

                        //
                        await this.loadSnapshotsChartContent(conceptUuid, scenarioUuid, playerUuid)

                    } catch (exp) {
                        await TController.displayExceptionMessages(exp);
                    }
                }
                ,


                async loadSnapshotsChartContent(conceptUuid, scenarioUuid, playerUuid) {

                    try {
                        await this.flushSnapshotsChartContent();

                        const primaryResult = await lizNeuralConceptService.snapshotChartData(conceptUuid, scenarioUuid, playerUuid);     // If no primary record is found at uuid than exception is thrown
                        console.log('PrimaryResult', primaryResult.data);
                        this.Form.turnCount = primaryResult.data.turnCount;
                        this.Form.turnBest = primaryResult.data.turnBest;
                        this.Form.bestPrecision = primaryResult.data.bestPrecision;

                        if (Array.isArray(primaryResult.data.precisions)) {
                            primaryResult.data.precisions.forEach((item, index) => {
                                this.SnapshotsChart.series[0].data.push({
                                    x: index + 1,
                                    y: Math.round(item * 10000) / 100
                                })
                            });
                        }
                    } catch (exp) {
                        await TController.displayExceptionMessages(exp);
                    }
                }
                ,
                async flushSnapshotsChartContent() {
                    try {
                        this.SnapshotsChart.series[0].data = [];
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
}
;
</script>

<style scoped>
#InfoBar {
    font-size: 10px;
    color: grey;
    border-bottom: 1px grey solid;
    padding: 3px;
}

#InfoBar > span {
    padding: 2px;
    border-right: 1px grey solid;
}

#InfoBar > span:last-of-type {
    border: none;
}

</style>