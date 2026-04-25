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

                <td class="RowFieldCaption" :key="'row-' + aRow.Field + '-Caption'">
                    {{aRow.Caption}}
                </td>


                <td v-for="column in config.columns" :key="'Cell-' + aRow.Field + '-' + column.Code">
                    <v-checkbox
                        v-model="internalModel"
                        :value="aRow.Field + ':' + column.Code"
                        density="compact"
                        hide-details
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
                rules: {
                    type: Array,
                    default: () => [],
                }
            },


        },

        data() {
            return {
                internalErrors: []
            };
        },
        computed: {
            internalModel: {
                get() {
                    return this.modelValue;
                },
                set(value) {
                    if(value === null) {
                        // value = this.modelValue;
                        value = [];
                    }

                    this.$emit("update:modelValue", value);
                }
            },
            errorMessages() {
                return this.internalErrors.length > 0 ? this.internalErrors : null;
            }
        },
        methods: {
            validate() {
                this.internalErrors = this.rules.map(rule => rule(this.internalModel)).filter(error => error);
                return this.internalErrors.length === 0;
            },
            reset() {
                this.internalModel = this.modelValue.length > 0 ? [...this.modelValue] : [];
                this.internalErrors = [];
            },


        },
    };
</script>




<style>

    .TCheckboxMatrixInput {
        padding: 0px;
        margin: 0px;

        border: 1px #ccc solid;
        border-radius: 4px;
    }

    .TCheckboxMatrixInput > table {
        margin: 0px;
        padding: 0px;
        width: 100%;

        border-collapse: collapse;
    }

    .TCheckboxMatrixInput > table tbody > tr:hover {
        background-color: #f4f4f4;
    }

    .TCheckboxMatrixInput > table th {
        padding: 10px;

        text-align: center;


        background-color: transparent;
    }

    .TCheckboxMatrixInput > table td {
        padding: 0px;

        justify-content: center; /* Vízszintes középre igazítás */
        text-align: center;
        text-overflow: ellipsis;
        align-items: center; /* Függőleges középre igazítás */
        white-space: nowrap;

        background-color: transparent;
        border: none;

        overflow: hidden;
    }



    .TCheckboxMatrixInput > table th {

    }



    .TCheckboxMatrixInput > table .RowFieldCaption {
        text-align: left;
        padding-left: 10px;
    }

    .TCheckboxMatrixInput > table td {
        vertical-align: middle;
    }

</style>
