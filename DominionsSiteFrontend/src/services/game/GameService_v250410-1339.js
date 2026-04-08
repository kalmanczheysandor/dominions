import TService from "@/framework/TService";
import {authService} from "@/services/auth/AuthService";
import backendConfiguration from "@/configurations/backendConfiguration";
import SockJS from "sockjs-client";
import {Stomp} from "@stomp/stompjs";

class GameService extends TService {
    #baseUrl = super.extendWithBaseUrl('/game');
    stompClient=null;
    isConnected=false;
    //#baseUrl = "http://localhost:15000/game"
    #permissionResource = 'Game.Launch';

    // async accessByUuid(uuid) {
    //     // Checking permission
    //     authService.assertAccessActionGrantedOn(this.#permissionResource);
    //
    //     // Communicate with backend
    //     const url = this.#baseUrl + '/' + uuid;
    //     return await this.accessRecordByUuid(url);
    // }



    async create(data) {
        const socket = new SockJS('http://localhost:15000/site/ws/connect');
        this.stompClient = Stomp.over(socket);
        this.stompClient.connect({}, (frame) => {
            console.log('Connected: ' + frame);
            this.isConnected = true; // Kapcsolódás sikeres, a kapcsolati állapotot beállítjuk

            // // Minden csatlakozott felhasználónak üzenetküldés
            // this.stompClient.subscribe('/topic/chat', (msg) => {
            //     this.messages.push(msg.body);
            // });
            //
            // // Csak a saját felhasználónak üzenetküldés
            // this.stompClient.subscribe('/user/queue/private', (msg) => {
            //     this.messages.push("Saját üzenet: " + msg.body);
            // });
            //
            //
            // this.stompClient.subscribe("/user/queue/errors", function(message) {
            //
            //     alert("Error " + message.body);
            // });
        }, (error) => {
            console.error('WebSocket hiba: ', error);
            this.isConnected = false;
        });
    }


    async join(data) {
        // Checking permission
        //authService.assertAddActionGrantedOn(this.#permissionResource);


        const socket = new SockJS('http://localhost:15000/site/ws/connect');
        this.stompClient = Stomp.over(socket);
        this.stompClient.connect({}, (frame) => {
            console.log('Connected: ' + frame);
            this.isConnected = true; // Kapcsolódás sikeres, a kapcsolati állapotot beállítjuk

            // // Minden csatlakozott felhasználónak üzenetküldés
            // this.stompClient.subscribe('/topic/chat', (msg) => {
            //     this.messages.push(msg.body);
            // });
            //
            // // Csak a saját felhasználónak üzenetküldés
            // this.stompClient.subscribe('/user/queue/private', (msg) => {
            //     this.messages.push("Saját üzenet: " + msg.body);
            // });
            //
            //
            // this.stompClient.subscribe("/user/queue/errors", function(message) {
            //
            //     alert("Error " + message.body);
            // });






        }, (error) => {
            console.error('WebSocket hiba: ', error);
            this.isConnected = false;
        });






    }


    // async join(data) {
    //     // Checking permission
    //     //authService.assertAddActionGrantedOn(this.#permissionResource);
    //
    //     // Communicate with backend
    //     const url = this.#baseUrl + '/join';
    //     return await this.sendPostRequest(url, data);
    // }




    async listAllBoards() {
        // Checking permission
        //authService.assertAccessActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        // const url = this.#baseUrl + '/list';
        // return await this.listAllRecord(url);

        return {
            data:[
                {
                    uuid:'jakdhajsdhasjkdhasjkd',
                    boardImage:'board-1.jpg',
                    title:'World map',
                    subtitle:'some text',
                    description:'Here are some description',
                    difficulty:'Beginner',
                    playerAiCount:3,
                    playerNormalCount:1,
                    isOpen:true,
                },
                {
                    uuid:'jakdhajsdhasjkdhasjkd2',
                    boardImage:'board-2.jpg',
                    title:'Europe map',
                    subtitle:'some text',
                    description:'Here are some description',
                    difficulty:'Moderate',
                    playerAiCount:3,
                    playerNormalCount:1,
                    isOpen:false,
                },
                {
                    uuid:'jakdhajsdhasjkdhasjkd3',
                    boardImage:'board-3.jpg',
                    title:'Africa map',
                    subtitle:'some text3',
                    description:'Here are some description',
                    difficulty:'Hard',
                    playerAiCount:3,
                    playerNormalCount:1,
                    isOpen:false,
                },
                {
                    uuid:'jakdhajsdhasjkdhasjkd4',
                    title:'test13',
                    subtitle:'some text3',
                    description:'Here are some description',
                    difficulty:'Beginner',
                    playerAiCount:3,
                    playerNormalCount:1,
                    isOpen:false,
                },
                {
                    uuid:'jakdhajsdhasjkdhasjkd5',
                    title:'test13',
                    subtitle:'some text3',
                    description:'Here are some description',
                    difficulty:'Beginner',
                    playerAiCount:3,
                    playerNormalCount:1,
                    isOpen:false,
                },

            ]
        }
    }


}

// Create singleton instance
const gameService = new GameService();

// Exports
export default gameService;
export {GameService, gameService};
