import TException from '@/framework/exception/TException';

class TWarning extends TException {
    constructor(message) {
        super(message);
        this.name = "CustomError";  // Egyedi hibanevet adunk meg
    }
}