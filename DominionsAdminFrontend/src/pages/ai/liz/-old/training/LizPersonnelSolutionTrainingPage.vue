<template>
    <LizPersonnelSolutionTrainingAddDialog ref="LizPersonnelSolutionTrainingAddDialog" @whenDialogClosed="reloadList"/>
    <LizPersonnelSolutionTrainingModifyDialog ref="LizPersonnelSolutionTrainingModifyDialog" @whenDialogClosed="reloadList"/>
    <v-card flat class="TPanel">
        <v-card-title class="d-flex align-center ps-0 pe-0">
            <v-text-field v-model="LizPersonnelSolutionTrainingListComponent.search" density="compact" label="Search" prepend-inner-icon="mdi-magnify" variant="outlined" flat hide-details single-line/>
            <v-spacer/>
            <v-btn class="me-1" size="x-small" icon="mdi-refresh" @click="eventClickOnRefreshButton" variant="tonal"/>
            <v-btn class="me-1" size="x-small" icon="mdi-plus" @click="eventClickOnAddButton" variant="tonal"/>
            <v-btn class="me-1" size="x-small" icon="mdi-delete-outline" @click="eventClickOnDeleteMultipleButton(LizPersonnelSolutionTrainingListComponent.selected)" variant="tonal" :disabled="!LizPersonnelSolutionTrainingListComponent.selected.length"/>
        </v-card-title>

        <v-divider></v-divider>
        <v-data-table
            class="custom-header-table"
            v-model="LizPersonnelSolutionTrainingListComponent.selected"
            v-model:search="LizPersonnelSolutionTrainingListComponent.search"
            :headers="LizPersonnelSolutionTrainingListComponent.headers"
            :items="LizPersonnelSolutionTrainingListComponent.rows"
            :sort-by="LizPersonnelSolutionTrainingListComponent.settings.sort"
            :filter-keys="LizPersonnelSolutionTrainingListComponent.settings.filter"
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
                                <v-icon icon="mdi-pencil"></v-icon>
                            </template>
                            <v-list-item-title @click="eventClickOnModifyRowButton(item.uuid)">Modify
                            </v-list-item-title>
                        </v-list-item>
                        <v-list-item>
                            <template v-slot:prepend>
                                <v-icon icon="mdi-delete-outline"></v-icon>
                            </template>
                            <v-list-item-title @click="eventClickOnDeleteRowButton(item.uuid)">Delete
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
    import LizPersonnelSolutionTrainingAddDialog from "@/pages/ai/liz/-old/training/LizPersonnelSolutionTrainingAddDialog.vue";
    import LizPersonnelSolutionTrainingModifyDialog from "@/pages/ai/liz/-old/training/LizPersonnelSolutionTrainingModifyDialog.vue";
    import lizPersonnelSolutionTrainingService from "@/services/ai/liz/training/LizPersonnelSolutionTrainingService";
   export default {
        components: {LizPersonnelSolutionTrainingAddDialog, LizPersonnelSolutionTrainingModifyDialog},
        props: {
            solutionUuid: {
                type: String,
                required: true
            }
        },
        data: () => ({
            LizPersonnelSolutionTrainingListComponent: {
                search: '',
                selected: [],
                headers: [
                    {
                        title: 'Title',
                        align: 'start',
                        sortable: true,
                        key: 'title'
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
                    sort: [{key: 'title', order: 'asc'}],
                    filter: ['title']
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
                    const result = await lizPersonnelSolutionTrainingService.listAll(this.solutionUuid);

                    // Inject values
                    this.LizPersonnelSolutionTrainingListComponent.selected = [];
                    this.LizPersonnelSolutionTrainingListComponent.rows = result.data;

                } catch(exp) {
                    await TController.displayExceptionMessages(exp);
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
                    await lizPersonnelSolutionTrainingService.delete(this.solutionUuid,uuid);

                    // Display success message
                    TController.displayDeletedToast();

                } catch(exp) {
                    await TController.displayExceptionMessages(exp);
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
                    await lizPersonnelSolutionTrainingService.deleteMultiple(this.solutionUuid,uuidList);

                    // Display success message
                    TController.displayDeletedToast()

                } catch(exp) {
                    await TController.displayExceptionMessages(exp);
                }

                // Refresh list content
                await this.reloadList();
            }
            ,

            async eventClickOnAddButton() {
                await this.$refs.LizPersonnelSolutionTrainingAddDialog.open(this.solutionUuid)
            },

            async eventClickOnModifyRowButton(uuid) {
                await this.$refs.LizPersonnelSolutionTrainingModifyDialog.open(this.solutionUuid,uuid)
            },

            async eventClickOnRefreshButton() {
                await this.reloadList();
            }
        }
    }
</script>

