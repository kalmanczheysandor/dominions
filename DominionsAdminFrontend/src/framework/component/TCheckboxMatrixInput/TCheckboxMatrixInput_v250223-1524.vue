<template>
    <div class="TCheckboxMatrixInput">


        <table>
            <tr>
                <th></th>
                <th v-for="column in config.columns" :key="'Header-'+column.Code">{{column.Caption}}</th>
            </tr>

            <tr v-for="aRow in config.rows" :key="'Row-' + aRow.Field" >
                <td :key="'row'+aRow.Field-'Caption'">
                    {{ aRow.Field }}
                </td>
                <td v-for="columns in config.columns" :key="'Cell-' + aRow.Field-columns.Code">
                    <v-checkbox v-model="modelValue" :value="aRow.Field + ':' + columns.Code" density="compact" hide-details class="small-checkbox"/>
                </td>
            </tr>
        </table>

    </div>


</template>

<script>
    export default {
        props: {
            modelValue: {
                type: Array,  // Changes from String to Array to allow multiple images
                default: () => [],
            },
            config: {
                type: Object,
                required: true,
                default: () => ({
                    columns:[],
                    rows:[]
                })
            },
        },
        data() {
            return {};
        },
        methods: {
            eventItemClicked(item) {
                this.$emit('whenItemClicked', item);
            },
            eventTrashIconClicked(item) {
                this.$emit('whenItemIsSelectedToDelete', item);
            }
        }
    };
</script>

<style scoped>
    

</style>
