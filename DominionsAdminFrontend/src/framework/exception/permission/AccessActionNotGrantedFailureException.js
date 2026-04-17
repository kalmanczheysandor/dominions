import ActionNotGrantedFailureException from '@/framework/exception/permission/ActionNotGrantedFailureException.js';

export default class AccessActionNotGrantedFailureException extends ActionNotGrantedFailureException {
    constructor() {
        super('ACCESS');
    }
}