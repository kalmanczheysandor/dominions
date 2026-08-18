
import TBackendException from "@/framework/exception/backend/TBackendException";

export default class GeneralBackendException extends TBackendException {

    constructor(code,parameters={}){
        super(code,parameters);
    }
}