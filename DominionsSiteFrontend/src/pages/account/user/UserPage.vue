<template>
    <UserAddDialog ref="UserAddDialog" @whenDialogClosed="reloadList"/>
    <UserModifyDialog ref="UserModifyDialog" @whenDialogClosed="reloadList"/>

    <v-card flat>
        <v-card-title class="d-flex align-center ps-0 pe-0">
            <v-text-field v-model="UserListComponent.search" density="compact" label="Search" prepend-inner-icon="mdi-magnify" variant="outlined" flat hide-details single-line/>
            <v-spacer/>
            <v-btn class="me-1" size="x-small" icon="mdi-refresh" @click="eventClickOnRefreshButton" variant="tonal"/>
            <v-btn class="me-1" size="x-small" icon="mdi-plus" @click="eventClickOnAddButton" variant="tonal"/>
            <v-btn class="me-1" size="x-small" icon="mdi-delete-outline" @click="eventClickOnDeleteMultipleButton(UserListComponent.selected)" variant="tonal" :disabled="!UserListComponent.selected.length"/>
        </v-card-title>
        <v-divider></v-divider>
        <v-data-table
            class="custom-header-table"
            v-model="UserListComponent.selected"
            v-model:search="UserListComponent.search"
            :headers="UserListComponent.headers"
            :items="UserListComponent.rows"
            :sort-by="UserListComponent.settings.sort"
            :filter-keys="UserListComponent.settings.filter"
            show-select item-value="uuid"
        >
            <template v-slot:item.image="{item}">
                <v-img :key="'row-'+item.uuid+'-'+(new Date().getTime())" :src="backendConfiguration.BACKEND_BASE_URL+'/data/user/'+item.uuid+'/image/main'" height="50" cover>
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
    import UserAddDialog from "@/pages/account/user/UserAddDialog.vue";
    import UserModifyDialog from "@/pages/account/user/UserModifyDialog.vue";
    import userService from "@/services/account/user/UserService";
    import backendConfiguration from "@/configurations/backendConfiguration";


    export default {
        components: {UserAddDialog, UserModifyDialog},
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
            UserListComponent: {
                search: '',
                selected: [],
                headers: [
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
                    const result = await userService.listAll();

                    // Inject values
                    this.UserListComponent.selected = [];
                    this.UserListComponent.rows = result.data;

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
                    await userService.delete(uuid);

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
                    await userService.deleteMultiple(uuidList);

                    // Display success message
                    TController.displayDeletedToast()

                } catch(exp) {
                    await TController.handleExceptions(exp);
                }

                // Refresh list content
                await this.reloadList();
            },

            async eventClickOnAddButton() {
                await this.$refs.UserAddDialog.open()
            },

            async eventClickOnModifyRowButton(uuid) {
                await this.$refs.UserModifyDialog.open(uuid)
            },

            async eventClickOnRefreshButton() {
                await this.reloadList();
            }
        }
    }
</script>

