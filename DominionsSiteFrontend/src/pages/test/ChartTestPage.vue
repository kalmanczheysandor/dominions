<template>
    <v-form ref="Form" class="TTabbedPanel" v-model="Form.validation.isValid">
        <v-tabs v-model="TabComponent.activeTabKey" align-tabs="end" color="primary">
            <v-tab value="Tab1">*</v-tab>
        </v-tabs>
        <v-tabs-window v-model="TabComponent.activeTabKey">
            <v-tabs-window-item value="Tab1">

                <div>
                    <apexchart width="500" type="bar" :options="options" :series="series"></apexchart>
                </div>
            </v-tabs-window-item>
        </v-tabs-window>
        <div class="ActionButtonBar">
            <button type="button" class="TButton CancelButton">Cancel</button>
            <button type="button" class="TButton ModifyButton">Modify</button>
        </div>
    </v-form>
</template>

<script>


    export default {
        name: "ProfilePage",
        data: () => ({

            options: {
                chart: {
                    id: 'vuechart-example'
                },
                xaxis: {
                    categories: [1991, 1992, 1993, 1994, 1995, 1996, 1997, 1998]
                }
            },
            series: [{
                name: 'series-1',
                data: [30, 40, 45, 50, 49, 60, 70, 91]
            }],
            TabComponent: {
                value: null
            },
            Form: {
                validation: {
                    isValid: false,
                }
            }

        }),

        methods: {}
    }
</script>

