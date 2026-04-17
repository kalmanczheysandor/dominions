<template>
    <DogAddDialog ref="DogAddDialog" @whenDialogClosed="reloadList"/>
    <DogModifyDialog ref="DogModifyDialog" @whenDialogClosed="reloadList"/>
    <DogGalleryDialog ref="DogGalleryDialog" @whenDialogClosed="reloadList"/>

    <v-card flat>
        <v-card-title class="d-flex align-center ps-0 pe-0">
            <v-text-field v-model="DogListComponent.search" density="compact" label="Search" prepend-inner-icon="mdi-magnify" variant="outlined" flat hide-details single-line/>
            <v-spacer/>
            <v-btn class="me-1" size="x-small" icon="mdi-refresh" @click="eventClickOnRefreshButton" variant="tonal"/>
            <v-btn class="me-1" size="x-small" icon="mdi-plus" @click="eventClickOnAddButton" variant="tonal"/>
            <v-btn class="me-1" size="x-small" icon="mdi-delete-outline" @click="eventClickOnDeleteMultipleButton(DogListComponent.selected)" variant="tonal" :disabled="!DogListComponent.selected.length"/>
        </v-card-title>
        <v-divider></v-divider>
        <v-data-table
            class="custom-header-table"
            v-model="DogListComponent.selected"
            v-model:search="DogListComponent.search"
            :headers="DogListComponent.headers"
            :items="DogListComponent.rows"
            :sort-by="DogListComponent.settings.sort"
            :filter-keys="DogListComponent.settings.filter"
            show-select item-value="uuid"
        >
            <template v-slot:item.image="{item}">
                <v-img :key="'row-'+item.uuid+'-'+(new Date().getTime())" :src="'http://localhost:8081/data/dog/'+item.uuid+'/image/main'" height="50" cover>
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
                                <v-icon icon="mdi-image-outline"></v-icon>
                            </template>
                            <v-list-item-title @click="eventClickOnGalleryRowButton(item.uuid)">Gallery
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
    import {dogService} from "@/services/dog/DogService.js";
    import DogAddDialog from "@/pages/dog/DogAddDialog.vue";
    import DogModifyDialog from "@/pages/dog/DogModifyDialog.vue";
    import TController from "@/framework/TController";
    import TConfirmDialog from "@/framework/component/TConfirmDialog/TConfirmDialog";
    import DogGalleryDialog from "@/pages/dog/DogGalleryDialog.vue";

    export default {
        components: {DogAddDialog, DogModifyDialog, DogGalleryDialog},
        computed: {

            currentTime() {
                const date = new Date();
                return date.getTime();
            }
        },
        data: () => ({
            DogListComponent: {
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
                        title: 'Name',
                        align: 'start',
                        sortable: true,
                        key: 'name'

                    },
                    {
                        title: 'Breed',
                        align: 'start',
                        sortable: true,
                        key: 'breedName'

                    },
                    {
                        title: 'Site',
                        align: 'start',
                        sortable: true,
                        key: 'siteName'

                    },
                    {
                        title: 'Prn',
                        align: 'start',
                        sortable: true,
                        key: 'prn'

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
                    filter: ['name', 'prn']
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
                    const result = await dogService.listAll();

                    // Inject values
                    this.DogListComponent.selected = [];
                    this.DogListComponent.rows = result.data;

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
                    await dogService.delete(uuid);

                    // Display success message
                    TController.displayDeletedToast();

                } catch(exp) {
                    await TController.displayExceptionMessages(exp);
                }

                // Refresh list content
                await this.reloadList();
            },

            eventClickOnDeleteMultipleButton: async function(uuidList) {

                try {

                    // Confirming
                    const confirmed = await TConfirmDialog('Do you wish to delete the ' + uuidList.length + ' selected item?');
                    if(!confirmed.ok) {
                        return;
                    }

                    // Delete record on backend
                    await dogService.deleteMultiple(uuidList);

                    // Display success message
                    TController.displayDeletedToast()

                } catch(exp) {
                    await TController.displayExceptionMessages(exp);
                }

                // Refresh list content
                await this.reloadList();
            },

            async eventClickOnAddButton() {
                await this.$refs.DogAddDialog.open()
            },

            async eventClickOnModifyRowButton(uuid) {
                await this.$refs.DogModifyDialog.open(uuid)
            },

            async eventClickOnGalleryRowButton(uuid) {
                await this.$refs.DogGalleryDialog.open(uuid)
            },

            async eventClickOnRefreshButton() {
                await this.reloadList();
            }
        }
    }
</script>

