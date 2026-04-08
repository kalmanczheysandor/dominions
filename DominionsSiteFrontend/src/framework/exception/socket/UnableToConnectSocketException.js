import TSocketException from "@/framework/exception/socket/TSocketException";

export default class UnableToConnectSocketException extends TSocketException {
    constructor() {
        super();
    }
}