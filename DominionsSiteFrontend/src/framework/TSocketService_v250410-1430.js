import TService from "@/framework/TService";
import {Stomp} from "@stomp/stompjs";
import SockJS from "sockjs-client";

class TSocketService extends TService {

    async static connectToSocketEndpoint( stompClient,socketEndpointUrl {
        return new Promise((resolve, reject) => {
            if(stompClient === null || !stompClient.connected) {
                const socket = new SockJS(socketEndpointUrl);
                stompClient = Stomp.over(socket);

                this.stompClient.connect({}, (frame) => {
                    resolve(stompClient);
                }, (error) => {
                    resolve(null);
                });
            } else {
                resolve(stompClient);
            }
        });
    }

    async static waitForResponse(stompClient,subscribeUrl,requestObj) {
        if(stompClient==null) {
            throw "Must not be null!";
        }

        if(!stompClient.connected) {
            throw "Not connected";
        }

        return new Promise((resolve, reject) => {
            stompClient.subscribe(subscribeUrl, (responseObj) => {
                resolve(responseObj.body);
            });
            stompClient.send(subscribeUrl, {}, JSON.stringify(requestObj));
        });
    }
}

// Exports
export default TSocketService;
export {TSocketService};