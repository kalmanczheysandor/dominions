<template>
    <v-btn  color="primary" block @click="$root.showConfirmDialog(()=>doConfirm(5))">Confirm</v-btn>
</template>

<script>
    export default {
        methods: {
            doConfirm(index) {
                alert('confirmad'+index);
            }
        },
    }
</script>