import TPermissionFailureException from '@/framework/exception/permission/TPermissionFailureException.js';

export default class AccessActionNotGrantedFailureException extends TPermissionFailureException {
    constructor() {
        super('AccessActionNotGrantedFailureException','ACCESS');
    }
}