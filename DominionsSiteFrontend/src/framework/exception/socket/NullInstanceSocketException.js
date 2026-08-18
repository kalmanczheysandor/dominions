import GeneralSocketException from "@/framework/exception/socket/GeneralSocketException";

export default class NullInstanceSocketException extends GeneralSocketException {
    constructor() {
        super('NullInstanceSocketException', {
            parameters: {}
        });
    }
}