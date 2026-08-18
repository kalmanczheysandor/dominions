import TPermissionFailureException from '@/framework/exception/permission/TPermissionFailureException.js';

export default class DeleteActionNotGrantedFailureException extends TPermissionFailureException {
    constructor() {
        super('DeleteActionNotGrantedFailureException','DELETE');
    }
}