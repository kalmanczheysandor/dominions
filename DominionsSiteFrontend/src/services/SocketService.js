class WebSocketService {
    constructor() {
        // A WebSocket kapcsolat és a feliratkozás állapotát tároló változók
        this.stompClient = null;
        this.isSubscribed = false;
    }

    // WebSocket kapcsolat létrehozása
    connect() {
        return new Promise((resolve, reject) => {
            // WebSocket kapcsolat létrehozása
            this.stompClient = Stomp.client("ws://localhost:8080/ws");

            // Kapcsolódás a szerverhez
            this.stompClient.connect({}, (frame) => {
                console.log("Kapcsolódva:", frame);
                resolve(frame);
                this.subscribeToResponse(); // Feliratkozás csak egyszer
            }, (error) => {
                reject(new Error("WebSocket kapcsolat hiba: " + error));
            });
        });
    }

    // Feliratkozás a válasz csatornára
    subscribeToResponse() {
        if(this.isSubscribed) return;  // Ha már feliratkoztunk, nem csinálunk semmit

        // Feliratkozunk a válasz csatornára
        this.stompClient.subscribe('/user/queue/response', (message) => {
            console.log("Válasz a szervertől: " + message.body);
            // További válasz feldolgozása itt, pl. UI frissítése, állapot módosítása stb.
        });

        // Beállítjuk a flag-et, hogy feliratkoztunk
        this.isSubscribed = true;
    }

    // Üzenet küldése a szervernek
    sendMessage(message) {
        return new Promise((resolve, reject) => {
            const timeout = setTimeout(() => {
                reject(new Error("Válasz nem érkezett időben!"));
            }, 5000);  // 5000 ms = 5 másodperc

            // Üzenet küldése a szervernek
            this.stompClient.send("/app/chat", {}, message);

            // Feliratkozás a válasz csatornára
            this.stompClient.subscribe('/user/queue/response', (responseMessage) => {
                clearTimeout(timeout);  // Időzítő törlése, ha válasz érkezett
                resolve(responseMessage.body);  // Válasz megérkezett, teljesítjük a Promise-t
            });
        });
    }
}

// Az osztály példányosítása (singleton-szerűen)
const webSocketService = new WebSocketService();
