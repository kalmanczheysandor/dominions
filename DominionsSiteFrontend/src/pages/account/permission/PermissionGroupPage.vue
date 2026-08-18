<template>
    <PermissionGroupAddDialog ref="PermissionGroupAddDialog" @whenDialogClosed="reloadList"/>
    <PermissionGroupModifyDialog ref="PermissionGroupModifyDialog" @whenDialogClosed="reloadList"/>
    <v-card flat>
        <v-card-title class="d-flex align-center ps-0 pe-0">
            <v-text-field v-model="PermissionGroupListComponent.search" density="compact" label="Search" prepend-inner-icon="mdi-magnify" variant="outlined" flat hide-details single-line/>
            <v-spacer/>
            <v-btn class="me-1" size="x-small" icon="mdi-refresh" @click="eventClickOnRefreshButton" variant="tonal"/>
            <v-btn class="me-1" size="x-small" icon="mdi-plus" @click="eventClickOnAddButton" variant="tonal"/>
            <v-btn class="me-1" size="x-small" icon="mdi-delete-outline" @click="eventClickOnDeleteMultipleButton(PermissionGroupListComponent.selected)" variant="tonal" :disabled="!PermissionGroupListComponent.selected.length"/>
        </v-card-title>

        <v-divider></v-divider>
        <v-data-table
            class="custom-header-table"
            v-model="PermissionGroupListComponent.selected"
            v-model:search="PermissionGroupListComponent.search"
            :headers="PermissionGroupListComponent.headers"
            :items="PermissionGroupListComponent.rows"
            :sort-by="PermissionGroupListComponent.settings.sort"
            :filter-keys="PermissionGroupListComponent.settings.filter"
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
    import PermissionGroupAddDialog from "@/pages/account/permission/PermissionGroupAddDialog.vue";
    import PermissionGroupModifyDialog from "@/pages/account/permission/PermissionGroupModifyDialog.vue";
    import permissionGroupService from "@/services/account/permission/PermissionGroupService";

    export default {
        components: {PermissionGroupAddDialog, PermissionGroupModifyDialog},
        data: () => ({
            PermissionGroupListComponent: {
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
                    filter: ['name']
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
                    const result = await permissionGroupService.listAll();

                    // Inject values
                    this.PermissionGroupListComponent.selected = [];
                    this.PermissionGroupListComponent.rows = result.data;

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
                    await permissionGroupService.delete(uuid);

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
                    await permissionGroupService.deleteMultiple(uuidList);

                    // Display success message
                    TController.displayDeletedToast()

                } catch(exp) {
                    await TController.handleExceptions(exp);
                }

                // Refresh list content
                await this.reloadList();
            },

            async eventClickOnAddButton() {
                await this.$refs.PermissionGroupAddDialog.open()
            },

            async eventClickOnModifyRowButton(uuid) {
                await this.$refs.PermissionGroupModifyDialog.open(uuid)
            },

            async eventClickOnRefreshButton() {
                await this.reloadList();
            }
        }
    }
</script>

