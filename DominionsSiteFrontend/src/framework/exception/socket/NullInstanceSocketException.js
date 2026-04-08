import TSocketException from "@/framework/exception/socket/TSocketException";

export default class NullInstanceSocketException extends TSocketException {
    constructor() {
        super();
    }
}