<template>
    <CroppieInput :config="config" image="http://localhost:8080/image/WP_20230815_23_38_43_Pro.jpg"/>
    <CroppieInput/>
</template>

<script>
    import CroppieInput from "@/framework/component/CroppieInput/CroppieInput.vue";

    export default {
        components: {CroppieInput},
        data() {
            return {
                config: {
                    cropWidth: 500,
                    cropHeight: 250
                }
            };
        }

    }

</script>

<style>

</style>
