
import TBackendException from "@/framework/exception/backend/TBackendException";

export default class SecurityBackendException extends TBackendException {
    constructor(code,parameters={}){
        super(code,parameters);
    }
}