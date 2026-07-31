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
                    <div class="InfoBar">
                        <span v-if="MainTabComponent.SnapshotsTab.turnCount!=null">Turn count:{{
                                MainTabComponent.SnapshotsTab.turnCount
                            }}</span>
                        <span v-else>Turn count: unknown </span>

                        <span v-if="MainTabComponent.SnapshotsTab.turnBest!=null">Best turn:{{
                                MainTabComponent.SnapshotsTab.turnBest
                            }}</span>
                        <span v-else>Best turn: unknown </span>

                        <span v-if="MainTabComponent.SnapshotsTab.bestPrecision!=null">Best precision:{{
                                MainTabComponent.SnapshotsTab.bestPrecision * 100
                            }}%</span>
                        <span v-else>Best precision: unknown </span>
                    </div>
                    <apexchart ref="SnapshotsChartComponent"
                               type="line" height="350"
                               :options="SnapshotsChartComponent.chartOptions"
                               :series="SnapshotsChartComponent.series">
                    </apexchart>
                </v-tabs-window-item>
                <v-tabs-window-item value="Tab2">
                    <div class="InfoBar">
                        <span v-if="MainTabComponent.ExecutionsTab.executionCount!=null">Execution count:{{
                                MainTabComponent.ExecutionsTab.executionCount
                            }}</span>
                        <span v-else>Execution count: unknown </span>

                        <span v-if="MainTabComponent.ExecutionsTab.executionKeyBest!=null">Best execution key:{{
                                MainTabComponent.ExecutionsTab.executionKeyBest
                            }}</span>
                        <span v-else>Best execution key: unknown </span>

                        <span v-if="MainTabComponent.ExecutionsTab.bestPrecision!=null">Best execution precision:{{
                                MainTabComponent.ExecutionsTab.bestPrecision * 100
                            }}%</span>
                        <span v-else>Best execution precision: unknown </span>
                    </div>
                    <apexchart ref="ExecutionsChartComponent"
                               type="bar"
                               height="350"
                               :options="ExecutionsChartComponent.chartOptions"
                               :series="ExecutionsChartComponent.series">
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
                value: null,
                SnapshotsTab: {
                    turnCount: null,
                    turnBest: null,
                    bestPrecision: null,
                },
                ExecutionsTab: {
                    executionCount: null,
                    executionKeyBest: null,
                    bestPrecision: null
                },
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
            SnapshotsChartComponent: {
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
                        width: 2
                    },
                    title: {
                        text: 'Progress',
                        align: 'left'
                    },
                    grid: {
                        row: {
                            colors: ['#f3f3f3', 'transparent'], // takes an array which will be repeated on columns
                            opacity: 0.5
                        }
                    },
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
                    },
                    yaxis: [
                        {
                            min: 0,
                            max: 100,
                            seriesName: 'Snapshot',
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

                            legend: {
                                horizontalAlign: 'right',
                                // offsetX: 40
                            }
                        }
                    ],

                    tooltip: {
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
            ExecutionsChartComponent: {
                chartOptions: {
                    chart: {
                        type: 'bar',
                        height: 350,
                        zoom: {
                            enabled: true,
                            type: 'x',
                            autoScaleYaxis: true
                        },
                    },
                    plotOptions: {
                        bar: {
                            horizontal: false,
                            columnWidth: 10,
                            borderRadius: 1,
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
                    title: {
                        text: 'All executions',
                        align: 'left'
                    },
                    grid: {
                        row: {
                            colors: ['#f3f3f3', 'transparent'], // takes an array which will be repeated on columns
                            opacity: 0.5
                        }
                    },
                    xaxis: {
                        type: 'numeric',
                        labels: {
                            formatter: function (value) {
                                return Math.round(value);
                            }
                        },
                    },
                    yaxis: {
                        min: 0,
                        max: 100,
                        seriesName: 'Executions',
                        title: {
                            text: 'Precision (%)'
                        }
                    },
                    fill: {
                        opacity: 1
                    },
                    tooltip: {
                        show: true,
                        tools: {
                            zoom: true,
                            zoomin: true,
                            zoomout: true,
                            pan: true,
                            reset: true
                        },
                        y: {
                            formatter: function (val) {
                                return val + " %"
                            }
                        }
                    }
                },
                series: [
                    {
                        name: 'Executions',
                        data: [],
                    }],
            },
        }
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
                if (selectedScenarioKey == null) {
                    await this.flushSnapshotsChartComponentContent();
                    return;
                }

                // Retrieve: meta record(s)
                const historyPlayerResult = await lizNeuralConceptService.atResultListHistoryPlayer(this.uuid, selectedScenarioKey);

                // Inject: form meta values
                this.Form.Fields.PlayerSelectInput.itemsToDisplay = historyPlayerResult.data;

                // Inject: form input values
                this.Form.Fields.PlayerSelectInput.selectedKey = historyPlayerResult.data.userUuid;

            } catch (exp) {
                await TController.handleExceptions(exp);
            }
        },

        async eventPlayerChange(selectedPlayerKey) {
            try {
                // Checking permission
                authService.assertEditActionGrantedOn("Ai.Liz.Concept");

                // It helps to block the model update re-invocation after window close-event
                if (selectedPlayerKey == null) {
                    await this.flushSnapshotsChartComponentContent();
                    return;
                }

                // Form validation
                await this.$refs.Form.validate();
                if (!this.Form.validation.isValid) {
                    await this.flushSnapshotsChartComponentContent();
                    return;
                }

                //
                const conceptUuid = this.uuid;
                const scenarioUuid = this.Form.Fields.ScenarioSelectInput.selectedKey;
                const playerUuid = this.Form.Fields.PlayerSelectInput.selectedKey;

                //
                await this.loadSnapshotsChartComponentContent(conceptUuid, scenarioUuid, playerUuid);
                await this.loadExecutionsChartComponentContent(conceptUuid, scenarioUuid, playerUuid);

            } catch (exp) {
                await TController.handleExceptions(exp);
            }
        },

        async loadSnapshotsChartComponentContent(conceptUuid, scenarioUuid, playerUuid) {

            try {
                await this.flushSnapshotsChartComponentContent();

                const primaryResult = await lizNeuralConceptService.snapshotChartData(conceptUuid, scenarioUuid, playerUuid);     // If no primary record is found at uuid than exception is thrown
                console.log('PrimaryResult', primaryResult.data);
                this.MainTabComponent.SnapshotsTab.turnCount = primaryResult.data.turnCount;
                this.MainTabComponent.SnapshotsTab.turnBest = primaryResult.data.turnBest;
                this.MainTabComponent.SnapshotsTab.bestPrecision = primaryResult.data.bestPrecision;

                if (Array.isArray(primaryResult.data.precisions)) {
                    primaryResult.data.precisions.forEach((item, index) => {
                        this.SnapshotsChartComponent.series[0].data.push({
                            x: index + 1,
                            y: Math.round(item * 10000) / 100
                        })
                    });
                }
            } catch (exp) {
                await TController.handleExceptions(exp);
            }
        },

        async loadExecutionsChartComponentContent(conceptUuid, scenarioUuid, playerUuid) {

            try {
                await this.flushExecutionsChartComponentContent();

                const primaryResult = await lizNeuralConceptService.executionChartData(conceptUuid, scenarioUuid, playerUuid);     // If no primary record is found at uuid than exception is thrown
                console.log('PrimaryResult', primaryResult.data);

                this.MainTabComponent.ExecutionsTab.executionCount = primaryResult.data.executionCount;
                this.MainTabComponent.ExecutionsTab.executionKeyBest = primaryResult.data.executionKeyBest;
                this.MainTabComponent.ExecutionsTab.bestPrecision = primaryResult.data.bestPrecision;

                if (Array.isArray(primaryResult.data.precisions)) {
                    primaryResult.data.precisions.forEach((item, index) => {
                        this.ExecutionsChartComponent.series[0].data.push({
                            x: index + 1,
                            y: Math.round(item * 10000) / 100
                        })
                    });
                }
            } catch (exp) {
                await TController.handleExceptions(exp);
            }
        },

        async flushSnapshotsChartComponentContent() {
            try {
                this.MainTabComponent.SnapshotsTab.turnCount = null;
                this.MainTabComponent.SnapshotsTab.turnBest = null;
                this.MainTabComponent.SnapshotsTab.bestPrecision = null;

                this.SnapshotsChartComponent.series[0].data = [];
            } catch (exp) {
                await TController.handleExceptions(exp);
            }
        },

        async flushExecutionsChartComponentContent() {
            try {
                this.MainTabComponent.ExecutionsTab.executionCount = null;
                this.MainTabComponent.ExecutionsTab.executionKeyBest = null;
                this.MainTabComponent.ExecutionsTab.bestPrecision = null;

                this.ExecutionsChartComponent.series[0].data = [];
            } catch (exp) {
                await TController.handleExceptions(exp);
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

                //
                await this.flushSnapshotsChartComponentContent();
                await this.flushExecutionsChartComponentContent();

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
        },

    },
};
</script>

<style scoped>
.InfoBar {
    font-size: 10px;
    color: grey;
    border-bottom: 1px grey solid;
    padding: 3px;
}

.InfoBar > span {
    padding: 2px;
    border-right: 1px grey solid;
}

.InfoBar > span:last-of-type {
    border: none;
}

</style>