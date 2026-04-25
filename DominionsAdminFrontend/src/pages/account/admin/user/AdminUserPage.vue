<template>
    <AdminUserAddDialog ref="AdminUserAddDialog" @whenDialogClosed="reloadList"/>
    <AdminUserModifyDialog ref="AdminUserModifyDialog" @whenDialogClosed="reloadList"/>

    <v-card flat class="TPanel">
        <v-card-title class="d-flex align-center ps-0 pe-0">
            <v-text-field v-model="AdminUserListComponent.search" density="compact" label="Search" prepend-inner-icon="mdi-magnify" variant="outlined" flat hide-details single-line/>
            <v-spacer/>
            <v-btn class="me-1" size="x-small" icon="mdi-refresh" @click="eventClickOnRefreshButton" variant="tonal"/>
            <v-btn class="me-1" size="x-small" icon="mdi-plus" @click="eventClickOnAddButton" variant="tonal"/>
            <v-btn class="me-1" size="x-small" icon="mdi-delete-outline" @click="eventClickOnDeleteMultipleButton(AdminUserListComponent.selected)" variant="tonal" :disabled="!AdminUserListComponent.selected.length"/>
        </v-card-title>
        <v-divider></v-divider>
        <v-data-table
            class="custom-header-table"
            v-model="AdminUserListComponent.selected"
            v-model:search="AdminUserListComponent.search"
            :headers="AdminUserListComponent.headers"
            :items="AdminUserListComponent.rows"
            :sort-by="AdminUserListComponent.settings.sort"
            :filter-keys="AdminUserListComponent.settings.filter"
            show-select item-value="uuid"
        >
            <template v-slot:item.image="{item}">
                <v-img :key="'row-'+item.uuid+'-'+(new Date().getTime())" :src="backendConfiguration.BACKEND_BASE_URL+'/account/admin/user/'+item.uuid+'/photo'" height="50" cover>
                    <template v-slot:error>
                        <v-img src="/image/no_image.png" height="50" cover></v-img>
                    </template>
                    <template v-slot:placeholder>
                        <div class="d-flex align-center justify-center fill-height">
                            <v-progress-circular
                                    color="grey-lighten-4"
                                    indeterminate
                            ></v-progress-circular>
                        </div>
                    </template>
                </v-img>
            </template>
            <template v-slot:item.finalised="{item}">
                <v-checkbox-btn v-model="item.finalised" readonly/>
            </template>
            <template v-slot:item.enabled="{item}">
                <v-checkbox-btn v-model="item.enabled" readonly/>
            </template>
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
    import AdminUserAddDialog from "@/pages/account/admin/user/AdminUserAddDialog.vue";
    import AdminUserModifyDialog from "@/pages/account/admin/user/AdminUserModifyDialog.vue";
    import adminAdminUserService from "@/services/account/admin/user/AdminUserService";
    import backendConfiguration from "@/configurations/backendConfiguration";


    export default {
        components: {AdminUserAddDialog: AdminUserAddDialog, AdminUserModifyDialog: AdminUserModifyDialog},
        computed: {
            backendConfiguration() {
                return backendConfiguration
            },
            currentTime() {
                const date = new Date();
                return date.getTime();
            }
        },
        data: () => ({
            AdminUserListComponent: {
                search: '',
                selected: [],
                headers: [
                    {
                        title: 'Image',
                        align: 'start',
                        sortable: false,
                        key: 'image'

                    },
                    {
                        title: 'Identifier',
                        align: 'start',
                        sortable: true,
                        key: 'identifier'

                    },
                    {
                        title: 'Name',
                        align: 'start',
                        sortable: true,
                        key: 'name'
                    },
                    {
                        title: 'Finalised',
                        align: 'start',
                        sortable: true,
                        key: 'finalised'
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
                    sort: [{key: 'identifier', order: 'asc'}],
                    filter: ['identifier', 'name']
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
                    const result = await adminAdminUserService.listAll();

                    // Inject values
                    this.AdminUserListComponent.selected = [];
                    this.AdminUserListComponent.rows = result.data;

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
                    await adminAdminUserService.delete(uuid);

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
                    await adminAdminUserService.deleteMultiple(uuidList);

                    // Display success message
                    TController.displayDeletedToast()

                } catch(exp) {
                    await TController.displayExceptionMessages(exp);
                }

                // Refresh list content
                await this.reloadList();
            },

            async eventClickOnAddButton() {
                await this.$refs.AdminUserAddDialog.open()
            },

            async eventClickOnModifyRowButton(uuid) {
                await this.$refs.AdminUserModifyDialog.open(uuid)
            },

            async eventClickOnRefreshButton() {
                await this.reloadList();
            }
        }
    }
</script>

