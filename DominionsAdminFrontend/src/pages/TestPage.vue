<template>
    <div>
        <v-container>
            <v-list>
                <v-list-item v-for="user in users" :key="user.id" @click="openDialog(user)">
                    <v-list-item-content>{{user.name}}</v-list-item-content>
                </v-list-item>
            </v-list>
        </v-container>
        <UniversalDialog v-model:value="dialogVisible" :title="dialogTitle" :content="dialogContent" @saved="handleSave" @closed="refreshData"/>
    </div>
</template>

<script>
    import UniversalDialog from "@/components/UniversalDialog.vue";

    export default {
        components: {UniversalDialog},
        data() {
            return {
                users: [
                    {id: 1, name: "John Doe"},
                    {id: 2, name: "Jane Smith"},
                ],
                dialogVisible: false,
                dialogTitle: "",
                dialogContent: "",
                currentUser: null,
            };
        },
        methods: {
            openDialog(user) {
                this.currentUser = user;
                this.dialogTitle = `Editing ${user.name}`;
                this.dialogContent = `Details of ${user.name}`;
                this.dialogVisible = true;
            },
            handleSave() {
                console.log("Saved data for user:", this.currentUser);
                // Itt lehet adatmentési logikát megvalósítani
                this.refreshData();
            },
            refreshData() {
                console.log("Refreshing data...");
                // Itt újradőltheted az adatokat, pl. API hívással
            },
        },
    };
</script>
