<template>
    <v-container>
        <v-form ref="form">
            <v-text-field v-model="dog.name" label="Kutya neve" required></v-text-field>
            <v-text-field v-model="dog.breed" label="Kutya fajtája" required></v-text-field>
            <v-file-input label="Kép feltöltése" accept="image/*" @change="handleFileUpload"></v-file-input>
            <v-btn color="primary" @click="submitForm">Küldés</v-btn>
        </v-form>
    </v-container>
</template>

<script>
    export default {
        data() {
            return {
                dog: {
                    name: "",
                    breed: "",
                    imageBase64: "",
                },
            };
        },
        methods: {
            async handleFileUpload(event) {
                const file = event.target.files[0];
                if (file) {
                    const reader = new FileReader();
                    reader.readAsDataURL(file);
                    reader.onload = () => {
                        this.dog.imageBase64 = reader.result.split(",")[1]; // Base64 kód kinyerése
                    };
                }
            },
            async submitForm() {
                const payload = {
                    name: this.dog.name,
                    breed: this.dog.breed,
                    imageBase64: this.dog.imageBase64,
                };

                try {
                    const response = await fetch("http://localhost:8080/api/dogs", {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json",
                        },
                        body: JSON.stringify(payload),
                    });

                    if (response.ok) {
                        alert("Sikeresen elküldve!");
                    } else {
                        alert("Hiba történt!");
                    }
                } catch (error) {
                    console.error("Hálózati hiba:", error);
                }
            },
        },
    };
</script>
