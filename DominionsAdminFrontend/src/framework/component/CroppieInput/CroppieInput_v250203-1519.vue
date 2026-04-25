<template>
    <div className="Image-Upload-wrapper Image-upload">
        <p>
            This is image upload wrapper component
        </p>
        <div id="croppie"></div>
    </div>
</template>


<script>
    //  uitleg over Croppie::https://www.youtube.com/watch?v=kvNozA8M1HM
    import Croppie from 'croppie';

    export default {
        // props: [
        //     'imgUrl'
        // ],


        mounted() {
            this.image = this.imgUrl   // als component is mounted, this.image definieren, en daartmee croppie in..
            this.setUpCroppie()
        },
        data() {
            return {
                image: null,
                croppie: null,
                imgUrl:'http://localhost:8080/favicon.ico'
            }
        },
        methods: {
            setUpCroppie() {
                let el = document.getElementById('croppie');
                this.croppie = new Croppie(el, {
                    viewport: {width: 200, height: 200, type: 'circle'},
                    boundary: {width: 220, height: 220},
                    showZoomer: true,
                    enableOrientation: true
                });

                this.croppie.bind({
                    url: this.image
                });
            }
        }
    }
</script>
