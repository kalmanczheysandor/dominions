import TService from "@/framework/TService";
import {Stomp} from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import UnableToConnectSocketException from "@/framework/exception/socket/UnableToConnectSocketException";
import TParameterValidation from "@/framework/utils/TParameterValidation";
import UnconnectedClientInstanceSocketException from "@/framework/exception/socket/UnconnectedClientInstanceSocketException";
import ResponseWaitingTimedOutSocketException from "@/framework/exception/socket/ResponseWaitingTimedOutSocketException";

class TSocketService extends TService {

    static async connectToSocketEndpoint(stompClient, socketEndpointUrl) {
        return new Promise((resolve, reject) => {
            if(stompClient === null || !stompClient.connected) {
                const socket = new SockJS(socketEndpointUrl);
                stompClient = Stomp.over(socket);

                alert(0);
                stompClient.connect({}, (frame) => {
                    console.log("CONNECT");
                    resolve(stompClient);
                }, (error) => {
                    console.log("REJECT");
                    reject(new UnableToConnectSocketException());
                });
            } else {
                resolve(stompClient);
            }
        });
    }

    static async waitForResponse(stompClient, chanelUrl, requestObj, timeout = 5000) {
        // Input checking
        TParameterValidation.assertParameterIsNotNull(stompClient, 'stompClient');
        TParameterValidation.assertParameterIsNotABlankString(chanelUrl, 'chanelUrl');
        TParameterValidation.assertParameterIsNotNull(requestObj, 'requestObj');
        TParameterValidation.assertParameterIsIntegerType(timeout, 'timeout');

        if(!stompClient.connected) {
            throw new UnconnectedClientInstanceSocketException();
        }

        return new Promise((resolve, reject) => {
            // Subscription to a channel
            const subscription = stompClient.subscribe(chanelUrl, (responseObj) => {
                subscription.unsubscribe();
                resolve(responseObj.body);
            });

            // Send request to the channel
            stompClient.send(chanelUrl, {}, JSON.stringify(requestObj));

            // Timeout
            setTimeout(() => {
                subscription.unsubscribe();
                reject(new ResponseWaitingTimedOutSocketException());
            }, timeout);
        });
    }
}


// Exports
export default TSocketService;
export {TSocketService};