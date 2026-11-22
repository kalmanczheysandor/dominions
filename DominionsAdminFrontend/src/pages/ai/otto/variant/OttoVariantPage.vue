<template>
    <OttoVariantAddDialog ref="OttoVariantAddDialog" @whenDialogClosed="reloadList"/>
    <OttoVariantModifyDialog ref="OttoVariantModifyDialog" @whenDialogClosed="reloadList"/>
    <v-card flat class="TPanel">
        <v-card-title class="d-flex align-center ps-0 pe-0">
            <v-text-field v-model="OttoVariantListComponent.search" density="compact" label="Search" prepend-inner-icon="mdi-magnify" variant="outlined" flat hide-details single-line/>
            <v-spacer/>
            <v-btn class="me-1" size="x-small" icon="mdi-refresh" @click="eventClickOnRefreshButton" variant="tonal"/>
            <v-btn class="me-1" size="x-small" icon="mdi-plus" @click="eventClickOnAddButton" variant="tonal"/>
            <v-btn class="me-1" size="x-small" icon="mdi-delete-outline" @click="eventClickOnDeleteMultipleButton(OttoVariantListComponent.selected)" variant="tonal" :disabled="!OttoVariantListComponent.selected.length"/>
        </v-card-title>

        <v-divider></v-divider>
        <v-data-table
            class="custom-header-table"
            v-model="OttoVariantListComponent.selected"
            v-model:search="OttoVariantListComponent.search"
            :headers="OttoVariantListComponent.headers"
            :items="OttoVariantListComponent.rows"
            :sort-by="OttoVariantListComponent.settings.sort"
            :filter-keys="OttoVariantListComponent.settings.filter"
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
    import OttoVariantAddDialog from "@/pages/ai/otto/variant/OttoVariantAddDialog.vue";
    import OttoVariantModifyDialog from "@/pages/ai/otto/variant/OttoVariantModifyDialog.vue";
    import ottoVariantService from "@/services/ai/otto/variant/OttoVariantService";



    export default {
        components: {OttoVariantAddDialog, OttoVariantModifyDialog},
        data: () => ({
            OttoVariantListComponent: {
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
                        title: 'Evaluator',
                        align: 'start',
                        sortable: true,
                        key: 'evaluatorName'
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
                    filter: ['name','code','evaluatorName','dateCreated','dateModified']
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
                    const result = await ottoVariantService.listAll();

                    // Inject values
                    this.OttoVariantListComponent.selected = [];
                    this.OttoVariantListComponent.rows = result.data;

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
                    await ottoVariantService.delete(uuid);

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
                    await ottoVariantService.deleteMultiple(uuidList);

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
                await this.$refs.OttoVariantAddDialog.open()
            },

            async eventClickOnModifyRowButton(uuid) {
                await this.$refs.OttoVariantModifyDialog.open(uuid)
            },

            async eventClickOnRefreshButton() {
                await this.reloadList();
            }
        }
    }
</script>

