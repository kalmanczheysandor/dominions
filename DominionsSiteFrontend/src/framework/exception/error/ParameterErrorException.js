import GeneralErrorException from "@/framework/exception/error/GeneralErrorException";

export default class ParameterErrorException extends GeneralErrorException {
    constructor(message) {
        super(message);
    }
}