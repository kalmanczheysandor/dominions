import TSocketException from "@/framework/exception/socket/TSocketException";

export default class UnconnectedClientInstanceSocketException extends TSocketException {
    constructor() {
        super();
    }
}