<template>
    <GameScenarioAddDialog ref="GameScenarioAddDialog" @whenDialogClosed="reloadList"/>
    <GameScenarioModifyDialog ref="GameScenarioModifyDialog" @whenDialogClosed="reloadList"/>

    <v-card flat class="TPanel">
        <v-card-title class="d-flex align-center ps-0 pe-0">
            <v-text-field v-model="ScenarioListComponent.search" density="compact" label="Search" prepend-inner-icon="mdi-magnify" variant="outlined" flat hide-details single-line/>
            <v-spacer/>
            <v-btn class="me-1" size="x-small" icon="mdi-refresh" @click="eventClickOnRefreshButton" variant="tonal"/>
            <v-btn class="me-1" size="x-small" icon="mdi-plus" @click="eventClickOnAddButton" variant="tonal"/>
            <v-btn class="me-1" size="x-small" icon="mdi-delete-outline" @click="eventClickOnDeleteMultipleButton(ScenarioListComponent.selected)" variant="tonal" :disabled="!ScenarioListComponent.selected.length"/>
        </v-card-title>
        <v-divider></v-divider>
        <v-data-table
            class="custom-header-table"
            v-model="ScenarioListComponent.selected"
            v-model:search="ScenarioListComponent.search"
            :headers="ScenarioListComponent.headers"
            :items="ScenarioListComponent.rows"
            :sort-by="ScenarioListComponent.settings.sort"
            :filter-keys="ScenarioListComponent.settings.filter"
            show-select item-value="uuid"
        >
            <template v-slot:item.image="{item}">
                <v-img :key="'row-'+item.uuid+'-'+(new Date().getTime())" :src="backendConfiguration.BACKEND_BASE_URL+'/game/scenario/'+item.uuid+'/image/main'" height="50" cover>
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
            <template v-slot:item.published="{item}">
                <v-checkbox-btn v-model="item.published" readonly/>
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
    import {gameScenarioService} from "@/services/game/scenario/GameScenarioService.js";
    import GameScenarioAddDialog from "@/pages/game/scenario/GameScenarioAddDialog.vue";
    import GameScenarioModifyDialog from "@/pages/game/scenario/GameScenarioModifyDialog.vue";
    import TController from "@/framework/TController";
    import TConfirmDialog from "@/framework/component/TConfirmDialog/TConfirmDialog";
    import TService from "@/framework/TService";
    import backendConfiguration from "@/configurations/backendConfiguration";

    export default {
        components: {GameScenarioAddDialog, GameScenarioModifyDialog},
        computed: {
            backendConfiguration() {
                return backendConfiguration
            },
            TService() {
                return TService
            },

            currentTime() {
                const date = new Date();
                return date.getTime();
            }
        },
        data: () => ({
            ScenarioListComponent: {
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
                        title: 'Title',
                        align: 'start',
                        sortable: true,
                        key: 'title'

                    },
                    {
                        title: 'Difficulty',
                        align: 'start',
                        sortable: true,
                        key: 'difficulty'

                    },

                    {
                        title: 'Enabled',
                        align: 'start',
                        sortable: true,
                        key: 'enabled'
                    },
                    {
                        title: 'Published',
                        align: 'start',
                        sortable: true,
                        key: 'published'
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
                    filter: ['title', 'prn']
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
                    const result = await gameScenarioService.listAll();

                    // Inject values
                    this.ScenarioListComponent.selected = [];
                    this.ScenarioListComponent.rows = result.data;

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
                    await gameScenarioService.delete(uuid);

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
                    await gameScenarioService.deleteMultiple(uuidList);

                    // Display success message
                    TController.displayDeletedToast()

                } catch(exp) {
                    await TController.displayExceptionMessages(exp);
                }

                // Refresh list content
                await this.reloadList();
            },

            async eventClickOnAddButton() {
                await this.$refs.GameScenarioAddDialog.open()
            },

            async eventClickOnModifyRowButton(uuid) {
                await this.$refs.GameScenarioModifyDialog.open(uuid)
            },



            async eventClickOnRefreshButton() {
                await this.reloadList();
            }
        }
    }
</script>

