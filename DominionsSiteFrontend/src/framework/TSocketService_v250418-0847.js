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

                // const stompClient = Stomp.over(() => new SockJS(socketEndpointUrl));
                // stompClient.reconnect_delay = 5000; // 5 másodperces reconnect delay
                // stompClient.heartbeatIncoming = 10000; // 10 mp-enként vár heartbeat-et
                // stompClient.heartbeatOutgoing = 10000; // 10 mp-enként küld heartbeat-et

                const socket = new SockJS(socketEndpointUrl);
                const stompClient = Stomp.over(socket);
                stompClient.connect({}, (frame) => {
                    resolve(stompClient);
                }, (error) => {
                    reject(new UnableToConnectSocketException());
                });
            } else {
                resolve(stompClient);
            }
        });
    }

    static async waitForResponse(stompClient, endpointUrl, chanelUrl, requestObj, timeout = 5000) {
        // Input checking
        TParameterValidation.assertParameterIsNotNull(stompClient, 'stompClient');
        TParameterValidation.assertParameterIsNotABlankString(endpointUrl, 'endpointUrl');
        TParameterValidation.assertParameterIsNotABlankString(chanelUrl, 'chanelUrl');
        TParameterValidation.assertParameterIsNotNull(requestObj, 'requestObj');
        TParameterValidation.assertParameterIsIntegerType(timeout, 'timeout');

        if(!stompClient.connected) {
            throw new UnconnectedClientInstanceSocketException();
        }

        return new Promise((resolve, reject) => {
            // Subscription to a channel
            const subscription = stompClient.subscribe(chanelUrl, (responseObj) => {
                let contentObj = TSocketService.handleSocketResponse(responseObj);
                resolve(contentObj);
                subscription.unsubscribe();
            });

            // Send request to the channel
            stompClient.send(endpointUrl, {}, JSON.stringify(requestObj));

            // Timeout
            setTimeout(() => {
                reject(new ResponseWaitingTimedOutSocketException(endpointUrl));
                subscription.unsubscribe();
            }, timeout);
        });
    }

    static async handleSocketResponse(responseObj) {

        let contentObj;
        const contentType = responseObj.headers['content-type'];
        if(contentType != null && contentType.includes('application/json')) {
            contentObj = JSON.parse(responseObj.body);
        } else {
            contentObj = responseObj.body;
        }

        return contentObj;
    }

    static async listenOnAChanel(stompClient, chanelUrl, callback) {

        // Input checking
        TParameterValidation.assertParameterIsNotNull(stompClient, 'stompClient');
        TParameterValidation.assertParameterIsNotABlankString(chanelUrl, 'chanelUrl');
        TParameterValidation.assertParameterIsACallbackFunction(callback, 'callback');

        // Subscription to a channel
        const subscription = stompClient.subscribe(chanelUrl, (responseObj) => {
            const contentObj = TSocketService.handleSocketResponse(responseObj);
            callback(contentObj);
        });
    }


}


// Exports
export default TSocketService;
export {TSocketService};