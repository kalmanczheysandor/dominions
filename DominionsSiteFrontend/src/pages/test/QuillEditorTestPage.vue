<template>
    <QuillEditor :options="options" ref="QuillEditor"/>
    <button @click.stop="eventClickOnSaveButton">SAVE</button>
</template>

<script>
    import { QuillEditor } from '@vueup/vue-quill'
    import '@vueup/vue-quill/dist/vue-quill.snow.css';

    export default {
        components: {
            QuillEditor
        },
        data() {
            return {
                options: {
                    debug: 'info',
                    modules: {
                        toolbar: [
                            [{ header: [1, 2, false] }],
                            ['bold', 'italic', 'underline'],
                            ['image', 'code-block'],
                        ]
                    },
                    placeholder: 'Compose an epic...',
                    readOnly: false,
                    theme: 'snow'
                }
            }
        },
        methods: {
            eventClickOnSaveButton(){
                alert(this.$refs.QuillEditor.getHTML());
            }
        }
    }
</script>
