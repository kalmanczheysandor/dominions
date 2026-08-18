import GeneralSocketException from "@/framework/exception/socket/GeneralSocketException";

export default class UnableToConnectSocketException extends GeneralSocketException {
    constructor() {
        super('UnableToConnectSocketException', {
            parameters: {}
        });
    }
}