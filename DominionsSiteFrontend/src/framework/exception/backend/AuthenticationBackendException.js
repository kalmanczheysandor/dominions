import TBackendException from "@/framework/exception/backend/TBackendException";

export default class AuthenticationBackendException extends TBackendException {
    constructor(code,parameters={}){
        super(code,parameters);
    }
}