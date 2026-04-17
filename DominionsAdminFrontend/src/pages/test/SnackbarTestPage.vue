<template>
    <!-- Confirm Snackbar -->
    <v-snackbar v-model="snackbar" timeout="-1"  timer="red" :color="snackbarColor" multi-line>
        {{ snackbarMessage }}

        <template v-slot:actions>
            <v-btn text @click="handleConfirm">OK</v-btn>
            <v-btn text @click="handleCancel">Mégse</v-btn>
        </template>
    </v-snackbar>

    <!-- Gomb, amely megjeleníti a Confirm Snackbart -->
    <v-btn @click="openConfirm">Megerősítés kérése</v-btn>
</template>

<script>
    export default {
        data() {
            return {
                snackbar: false,  // A snackbar láthatóságának kezelése
                snackbarMessage: 'Biztosan szeretnéd végrehajtani ezt a műveletet?',
                snackbarColor: 'warning',  // Szín a figyelmeztető snackbarhoz
            };
        },
        methods: {
            // A confirm (OK) gomb kattintása
            handleConfirm() {
                this.snackbar = false; // Bezárja a snackbart
                // További művelet végrehajtása (pl. törlés, mentés)
                console.log('Művelet megerősítve');
            },

            // A cancel (Mégse) gomb kattintása
            handleCancel() {
                this.snackbar = false; // Bezárja a snackbart
                console.log('Művelet visszavonva');
            },

            // SnackBar megjelenítése
            openConfirm() {
                this.snackbar = true;
            }
        },
    };
</script>

<style>
    /* Stílusok opcionálisan */
</style>
