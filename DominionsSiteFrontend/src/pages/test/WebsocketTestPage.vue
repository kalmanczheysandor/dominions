<template>
    <div>
        <h2>Chat alkalmazás</h2>
        <div>
            <input v-model="message" placeholder="Írj üzenetet..." />
            <button @click="sendMessage" :disabled="!isConnected">Üzenet küldése mindenkihez</button>
            <button @click="sendPrivateMessage" :disabled="!isConnected">Üzenet küldése magamnak</button>
            <button @click="sendDirectMessage" :disabled="!isConnected">Üzenet küldése másnak</button>
        </div>
        <div>
            <h3>Üzenetek:</h3>
            <ul>
                <li v-for="(msg, index) in messages" :key="index">{{ msg }}</li>
            </ul>
        </div>
    </div>
</template>

<script>
    import { Stomp } from '@stomp/stompjs';
    import SockJS from 'sockjs-client';

    export default {
        data() {
            return {
                message: '', // Üzenet szövege
                messages: [], // Üzenetek listája
                stompClient: null, // WebSocket kapcsolathoz szükséges kliens
                isConnected: false // Kapcsolat állapota
            };
        },
        methods: {
            connect() {
                const socket = new SockJS('http://localhost:15000/test/spring-boot-chat');
                // const socket = new SockJS('http://localhost:15010/spring-boot-chat');
                this.stompClient = Stomp.over(socket);
                this.stompClient.connect({}, (frame) => {
                    console.log('Connected: ' + frame);
                    this.isConnected = true; // Kapcsolódás sikeres, a kapcsolati állapotot beállítjuk

                    // Minden csatlakozott felhasználónak üzenetküldés
                    this.stompClient.subscribe('/topic/chat', (msg) => {
                        this.messages.push(msg.body);
                    });

                    // Csak a saját felhasználónak üzenetküldés
                    this.stompClient.subscribe('/user/queue/private', (msg) => {
                        this.messages.push("Saját üzenet: " + msg.body);
                    });


                    this.stompClient.subscribe("/user/queue/errors", function(message) {

                        alert("Error " + message.body);
                    });






                }, (error) => {
                    console.error('WebSocket hiba: ', error);
                    this.isConnected = false; // Ha a kapcsolat nem sikerült, beállítjuk a kapcsolat állapotát
                });
            },
            sendMessage() {
                if (this.message && this.isConnected) {
                    console.log("Attemot")
                    this.stompClient.send("/app/chat", {}, 'H1');
                    this.message = ''; // Üzenet elküldése után üresítjük a mezőt
                }
            },
            sendPrivateMessage() {
                if (this.message && this.isConnected) {
                    this.stompClient.send("/app/private-message", {}, "hello2");
                    this.message = ''; // Üzenet elküldése után üresítjük a mezőt
                }
            },
            // sendDirectMessage() {
            //     const recipient = prompt("Kinek küldöd az üzenetet?");
            //     if (this.message && recipient && this.isConnected) {
            //         this.stompClient.send("http://localhost:8081/app/direct-message", {}, JSON.stringify({ recipient, message: this.message }));
            //         this.message = ''; // Üzenet elküldése után üresítjük a mezőt
            //     }
            // },

            sendDirectMessage() {
                if(this.isConnected) {
                    this.stompClient.send("/app/direct-message", {}, 'Juci');
                    this.message = ''; // Üzenet elküldése után üresítjük a mezőt
                }
            },
        },
        mounted() {
            this.connect();
        },
        beforeUnmount() {
            if (this.stompClient) {
                this.stompClient.disconnect();
            }
        }
    };
</script>

<style scoped>
    /* CSS stílusok a chat alkalmazáshoz */
    button {
        border:1px red solid;
        margin:2px;
    }
</style>
