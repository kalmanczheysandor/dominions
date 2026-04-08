import TService from "@/framework/TService";
import {Stomp} from "@stomp/stompjs";
import SockJS from "sockjs-client";
class TSocketService extends TService {

    async static connectToSocketEndpoint(stompClient, socketEndpointUrl) {
        return new Promise((resolve, reject) => {
            if (stompClient === null || !stompClient.connected) {
                const socket = new SockJS(socketEndpointUrl);
                stompClient = Stomp.over(socket);

                stompClient.connect({}, (frame) => {
                    resolve(stompClient);
                }, (error) => {
                    throw "Unable to connect!";
                });
            } else {
                resolve(stompClient);
            }
        });
    }

    async static waitForResponse(stompClient, chanelUrl, requestObj, timeout = 5000) {
        if (stompClient == null) {
            throw "stompClient cannot be null!";
        }

        if (!stompClient.connected) {
            throw "Not connected to WebSocket!";
        }

        return new Promise((resolve, reject) => {
            // Subscription to a chanel
            const subscription = stompClient.subscribe(chanelUrl, (responseObj) => {
                subscription.unsubscribe();
                resolve(responseObj.body);
            });

            // Send request to the chanel
            stompClient.send(chanelUrl, {}, JSON.stringify(requestObj));

            // Timeout
            setTimeout(() => {
                subscription.unsubscribe();
                throw "Timed out!";
            }, timeout);
        });
    }
}

// Exports
export default TSocketService;
export {TSocketService};