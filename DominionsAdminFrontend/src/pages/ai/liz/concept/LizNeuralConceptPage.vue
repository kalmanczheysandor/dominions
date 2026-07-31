<template>
    <LizNeuralConceptAddDialog ref="LizNeuralConceptAddDialog" @whenDialogClosed="reloadList"/>
    <LizNeuralConceptModifyDialog ref="LizNeuralConceptModifyDialog" @whenDialogClosed="reloadList"/>
    <LizNeuralConceptExecutionDialog ref="LizNeuralConceptExecutionDialog" @whenDialogClosed="reloadList"/>
    <LizNeuralConceptResultDialog ref="LizNeuralConceptResultDialog" @whenDialogClosed="reloadList"/>

    <v-card flat class="TPanel">
        <v-card-title class="d-flex align-center ps-0 pe-0">
            <v-text-field v-model="LizNeuralConceptListComponent.search" density="compact" label="Search" prepend-inner-icon="mdi-magnify" variant="outlined" flat hide-details single-line/>
            <v-spacer/>
            <v-btn class="me-1" size="x-small" icon="mdi-refresh" @click="eventClickOnRefreshButton" variant="tonal"/>
            <v-btn class="me-1" size="x-small" icon="mdi-plus" @click="eventClickOnAddButton" variant="tonal"/>
            <v-btn class="me-1" size="x-small" icon="mdi-delete-outline" @click="eventClickOnDeleteMultipleButton(LizNeuralConceptListComponent.selected)" variant="tonal" :disabled="!LizNeuralConceptListComponent.selected.length"/>
        </v-card-title>

        <v-divider></v-divider>
        <v-data-table
            class="custom-header-table"
            v-model="LizNeuralConceptListComponent.selected"
            v-model:search="LizNeuralConceptListComponent.search"
            :headers="LizNeuralConceptListComponent.headers"
            :items="LizNeuralConceptListComponent.rows"
            :sort-by="LizNeuralConceptListComponent.settings.sort"
            :filter-keys="LizNeuralConceptListComponent.settings.filter"
            show-select item-value="uuid"
        >

            <template v-slot:item.actions="{item}">
                <v-menu>
                    <template v-slot:activator="{props}">
                        <v-btn icon="mdi-dots-vertical" variant="text" v-bind="props"></v-btn>
                    </template>
                    <v-list density="compact">
                        <v-list-item>
                            <template v-slot:prepend>
                                <v-icon icon="mdi-square-root"></v-icon>
                            </template>
                            <v-list-item-title @click="eventClickOnExecutionRowButton(item.uuid)">
                                Training
                            </v-list-item-title>
                            <template v-slot:append>
                                <v-icon icon="mdi-menu-right" size="x-small"></v-icon>
                            </template>
                            <v-menu :open-on-focus="false" activator="parent" open-on-hover submenu>
                                <v-list>
                                    <v-list-item>
                                        <template v-slot:prepend>
                                            <v-icon icon="mdi-brain"></v-icon>
                                        </template>
                                        <v-list-item-title @click="eventClickOnExecutionRowButton(item.uuid)">
                                           Execution
                                        </v-list-item-title>
                                    </v-list-item>
                                    <v-list-item>
                                        <template v-slot:prepend>
                                            <v-icon icon="mdi-sigma"></v-icon>
                                        </template>
                                        <v-list-item-title @click="eventClickOnResultRowButton(item.uuid)">
                                           Result
                                        </v-list-item-title>
                                    </v-list-item>
                                </v-list>
                            </v-menu>
                        </v-list-item>



                        <v-list-item>
                            <template v-slot:prepend>
                                <v-icon icon="mdi-pencil"></v-icon>
                            </template>
                            <v-list-item-title @click="eventClickOnModifyRowButton(item.uuid)">
                                Modify
                            </v-list-item-title>
                        </v-list-item>
                        <v-list-item>
                            <template v-slot:prepend>
                                <v-icon icon="mdi-delete-outline"></v-icon>
                            </template>
                            <v-list-item-title @click="eventClickOnDeleteRowButton(item.uuid)">
                                Delete
                            </v-list-item-title>
                        </v-list-item>
                    </v-list>
                </v-menu>
            </template>
            <template v-slot:item.enabled="{item}">
                <v-checkbox-btn v-model="item.enabled" readonly/>
            </template>

            <template v-slot:loading>
                <v-skeleton-loader type="table-row@10"></v-skeleton-loader>
            </template>

            <template v-slot:no-data>
                Empty
            </template>
        </v-data-table>
    </v-card>
</template>

<script>


    import TController from "@/framework/TController";
    import TConfirmDialog from "@/framework/component/TConfirmDialog/TConfirmDialog";

    import LizNeuralConceptAddDialog from "@/pages/ai/liz/concept/LizNeuralConceptAddDialog.vue";
    import LizNeuralConceptModifyDialog from "@/pages/ai/liz/concept/LizNeuralConceptModifyDialog.vue";
    import lizNeuralConceptService from "@/services/ai/liz/concept/LizNeuralConceptService";
    import LizNeuralConceptExecutionDialog from "@/pages/ai/liz/concept/LizNeuralConceptExecutionDialog.vue";
    import LizNeuralConceptResultDialog from "@/pages/ai/liz/concept/LizNeuralConceptResultDialog.vue";



    export default {
        components: {
            LizNeuralConceptResultDialog,
            LizNeuralConceptAddDialog,
            LizNeuralConceptModifyDialog,
            LizNeuralConceptExecutionDialog
        },
        data: () => ({
            LizNeuralConceptListComponent: {
                search: '',
                selected: [],
                headers: [
                    {
                        title: 'Name',
                        align: 'start',
                        sortable: true,
                        key: 'name'
                    },
                    {
                        title: 'Created',
                        align: 'start',
                        sortable: true,
                        key: 'dateCreated'
                    },
                    {
                        title: 'Modified',
                        align: 'start',
                        sortable: true,
                        key: 'dateModified'
                    },
                    {
                        title: 'Enabled',
                        align: 'start',
                        sortable: true,
                        key: 'enabled'
                    },
                    {
                        title: '',
                        key: 'actions',
                        align: 'end',
                        sortable: false
                    },
                ],
                rows: [],
                settings: {
                    sort: [{key: 'name', order: 'asc'}],
                    filter: ['name','dateCreated','dateModified']
                }
            }
        }),
        created() {
            this.init();
        },
        methods: {
            async init() {
                await this.loadList();
            },

            async reloadList() {
                await this.loadList();
            },

            async loadList() {
                try {

                    // List all rows
                    const result = await lizNeuralConceptService.listAll();

                    // Inject values
                    this.LizNeuralConceptListComponent.selected = [];
                    this.LizNeuralConceptListComponent.rows = result.data;

                } catch(exp) {
                    await TController.handleExceptions(exp);
                }
            },

            async eventClickOnDeleteRowButton(uuid) {
                try {

                    // Confirming
                    const confirmed = await TConfirmDialog('Do you wish to delete the selected item?');
                    if(!confirmed.ok) {
                        return;
                    }

                    // Delete record on backend
                    await lizNeuralConceptService.delete(uuid);

                    // Display success message
                    TController.displayDeletedToast();

                } catch(exp) {
                    await TController.handleExceptions(exp);
                }

                // Refresh list content
                await this.reloadList();
            },
            async eventClickOnDeleteMultipleButton(uuidList) {

                try {

                    // Confirming
                    const confirmed = await TConfirmDialog('Do you wish to delete the ' + uuidList.length + ' selected item?');
                    if(!confirmed.ok) {
                        return;
                    }

                    // Delete record on backend
                    await lizNeuralConceptService.deleteMultiple(uuidList);

                    // Display success message
                    TController.displayDeletedToast()

                } catch(exp) {
                    await TController.handleExceptions(exp);
                }

                // Refresh list content
                await this.reloadList();
            }
            ,

            async eventClickOnAddButton() {
                await this.$refs.LizNeuralConceptAddDialog.open()
            },

            async eventClickOnModifyRowButton(uuid) {
                await this.$refs.LizNeuralConceptModifyDialog.open(uuid)
            },

            async eventClickOnExecutionRowButton(uuid) {
                await this.$refs.LizNeuralConceptExecutionDialog.open(uuid)
            },

            async eventClickOnResultRowButton(uuid) {
                await this.$refs.LizNeuralConceptResultDialog.open(uuid)
            },

            async eventClickOnRefreshButton() {
                await this.reloadList();
            }
        }
    }
</script>

