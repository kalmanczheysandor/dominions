import GeneralSocketException from "@/framework/exception/socket/GeneralSocketException";

export default class UnconnectedClientInstanceSocketException extends GeneralSocketException {
    constructor() {
        super('UnconnectedClientInstanceSocketException', {
            parameters: {}
        });
    }
}