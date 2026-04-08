<template>


    <TUploadZone :config="config" :target="'http://localhost:8081/data/dog/0040db20-e739-4ac3-8a01-cd9abf0b0ce6/gallery/upload'"/>

</template>

<script>

    import TUploadZone from "@/framework/component/TUploadZone/TUploadZone.vue";

    export default {
        components: {TUploadZone},
        data() {
            return {
                config: {
                    width: 500,
                    height: 250
                }
            };
        }
    }
</script>
<style>

</style>
