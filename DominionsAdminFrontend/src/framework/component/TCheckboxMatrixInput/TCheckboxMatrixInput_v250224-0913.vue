<template>
    <div class="TCheckboxMatrixInput">
        <table>
            <thead>
            <tr>
                <th></th>
                <th v-for="column in config.columns" :key="'Header-' + column.Code">
                    {{column.Caption}}
                </th>
            </tr>
            </thead>

            <tbody>
            <tr v-for="aRow in config.rows" :key="'Row-' + aRow.Field">

                <td :key="'row-' + aRow.Field + '-Caption'">
                    {{aRow.Field}}
                </td>


                <td v-for="column in config.columns" :key="'Cell-' + aRow.Field + '-' + column.Code">
                    <v-checkbox
                        v-model="localModelValue"
                        :value="aRow.Field + ':' + column.Code"
                        density="compact"
                        hide-details
                        @change="eventCheckboxValueChanged"
                        style="display:inline-block;min-height:inherit;padding-top:3px;"
                    />
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</template>

<script>
    export default {
        props: {
            modelValue: {
                type: Array,
                default: () => [],
            },
            config: {
                type: Object,
                required: true,
                default: () => ({
                    columns: [],
                    rows: [],
                }),
            },
        },
        data() {
            return {
                localModelValue: [...this.modelValue],
            };
        },
        methods: {
            eventCheckboxValueChanged() {
                this.$emit('update:modelValue', this.localModelValue);
            },
        },
    };
</script>

<style scoped>
    /* A táblázat és a checkboxok stílusának finomhangolása */
    table {
        width: 100%;
        border-collapse: collapse;
        margin: 20px 0;
    }

    th {
        padding: 10px;
        text-align: left;
        border: 1px solid #ccc;
    }

    td {
        padding: 1px;
        text-align: center;
        border: 1px solid #ccc;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        padding: 0px;

        justify-content: center; /* Vízszintes középre igazítás */
        align-items: center;     /* Függőleges középre igazítás */


    }


    th {
        background-color: #f4f4f4;
    }

    td {
        vertical-align: middle;
    }


    /* Ha túl hosszúak az oszlopok, csökkenthetjük a táblázat szélességét */
    table td {

    }
</style>
