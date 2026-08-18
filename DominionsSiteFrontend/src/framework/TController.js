import TPermissionFailureException from "@/framework/exception/permission/TPermissionFailureException";
import TWarningDialog from "@/framework/component/TWarningDialog/TWarningDialog";
import GeneralBackendException from "@/framework/exception/backend/GeneralBackendException";
import AuthenticationBackendException from "@/framework/exception/backend/AuthenticationBackendException";
import TFailureException from "@/framework/exception/failure/TFailureException";
import TAlertDialog from "@/framework/component/TAlertDialog/TAlertDialog";
import TErrorException from "@/framework/exception/error/TErrorException";
import TException from "@/framework/exception/TException";
import SecurityBackendException from "@/framework/exception/backend/SecurityBackendException";
import {useToast} from "vue-toastification";
import authService from "@/services/auth/AuthService";
import router from "@/router";
import HttpBackendException from "@/framework/exception/backend/HttpBackendException";
import CustomErrorException from "@/framework/exception/error/CustomlErrorException";
import TBackendException from "@/framework/exception/backend/TBackendException";

export default class TController {

    static getTime() {
        let date = new Date();
        return date.getTime();

    }

    static async handleExceptions(exp) {
        //
        if (exp instanceof SecurityBackendException) {
            if (exp.getExceptionCode() === 'SessionExpiredException') {
                authService.invalidateAuthentication();
                router.push('/');
            } else if (exp.getExceptionCode() === 'InsufficientAuthenticationException') {
                authService.invalidateAuthentication();
                router.push('/');
            }

        }

        //
        const dialogData =  TController.generateMessageDialogData(exp);

        // Show the message dialog
        if (dialogData.type === 'Warning') {
            await TWarningDialog(dialogData);
        } else if (dialogData.type === 'Error') {
            await TAlertDialog(dialogData);
        } else {
            throw new Error('Unsupported message type "' + dialogData.type + '" is found!');
        }
    }


    static generateMessageDialogData(exp) {

        if (exp instanceof TBackendException) {
            if (exp instanceof HttpBackendException) {

                return TController.determineDialogData(exp, new Map([
                        ['Http400BackendException', {type: 'Error', title: exp.getStatusCode() + ' - Bad Request', message: 'The request is invalid.'}],
                        ['Http401BackendException', {type: 'Error', title: exp.getStatusCode() + ' - Unauthorized', message: 'Please sign in first.'}],
                        ['Http403BackendException', {type: 'Error', title: exp.getStatusCode() + ' - Forbidden', message: 'Access is not allowed.'}],
                        ['Http404BackendException', {type: 'Error', title: exp.getStatusCode() + ' - Not Found', message: 'The page was not found.'}],
                        ['Http405BackendException', {type: 'Error', title: exp.getStatusCode() + ' - Method Not Allowed', message: 'This action is not allowed.'}],
                        ['Http408BackendException', {type: 'Error', title: exp.getStatusCode() + ' - Request Timeout', message: 'The request timed out.'}],
                        ['Http409BackendException', {type: 'Error', title: exp.getStatusCode() + ' - Conflict', message: 'The request conflicts with existing data.'}],
                        ['Http429BackendException', {type: 'Error', title: exp.getStatusCode() + ' - Too Many Requests', message: 'Too many requests. Try again later.'}],

                        ['Http500BackendException', {type: 'Error', title: exp.getStatusCode() + ' - Internal Server Error', message: 'An unexpected error occurred.'}],
                        ['Http501BackendException', {type: 'Error', title: exp.getStatusCode() + ' - Not Implemented', message: 'This feature is not available.'}],
                        ['Http502BackendException', {type: 'Error', title: exp.getStatusCode() + ' - Bad Gateway', message: 'The server received an invalid response.'}],
                        ['Http503BackendException', {type: 'Error', title: exp.getStatusCode() + ' - Service Unavailable', message: 'The service is temporarily unavailable.'}],
                        ['Http504BackendException', {type: 'Error', title: exp.getStatusCode() + ' - Gateway Timeout', message: 'The server took too long to respond.'}]

                    ])
                );
            } else if (exp instanceof GeneralBackendException) {

                //
                let parameters = exp.getParameters();
                return TController.determineDialogData(exp, new Map([
                        //
                        ['NotParseableResponseProxyException', {type: 'Error', message: 'Inner module communication error due to parsing problem!', title: 'Error'}],
                        ['HttpStatusResponseProxyException', {type: 'Error', message: 'Inner module communication error with code:' + parameters.statusCode, title: 'Error'}],


                    // SiteAccountSignUp
                    ['SiteAccountSignUpVerificationTokenNotFoundException', {type: 'Warning', message: 'Sign up verification token is not found!'}],
                    ['SiteAccountSignUpUserDetailsNotFoundException', {type: 'Warning', message: 'Sign up user details is not found!'}],
                    ['SiteAccountSignUpUserUuidDuplicationException', {type: 'Warning', message: 'User UUID already exists!'}],
                    ['SiteAccountSignUpReservedUserIdentifierException', {type: 'Warning', message: 'User identifier already exists!'}],

                    // SiteAccountRecovery
                    ['SiteAccountRecoveryVerificationTokenNotFoundException', {type: 'Warning', message: 'Recovery verification token is not found!'}],
                    ['SiteAccountRecoveryUserDetailsNotFoundException', {type: 'Warning', message: 'Recovery user details not found!'}],
                    ['SiteAccountRecoveryUserUuidDuplicationException', {type: 'Warning', message: 'User UUID already exists!'}],
                    ['SiteAccountRecoveryIdentifierNotFoundException', {type: 'Warning', message: 'User identifier not found!'}],
                    ['SiteAccountRecoveryUserNotFinalisedException', {type: 'Warning', message: 'User is not finalised!'}],
                    ['SiteAccountRecoveryUserNotEnabledException', {type: 'Warning', message: 'User is not enabled!'}],
                    ['SiteAccountRecoveryConfirmPasswordMismatchException', {type: 'Warning', message: 'Confirm password mismatch!'}],

                    // AccountSettings
                    ['SiteAccountSettingsUserNotFoundException', {type: 'Warning', message: 'Couldn\'t find the user record!'}],
                    ['SiteAccountSettingsUserNotFoundByUuidException', {type: 'Warning', message: 'Couldn\'t find the user record by uuid:' + parameters.uuid}],
                    ['SiteAccountSettingsIdentifierIsReservedException', {type: 'Warning', message: 'The identifier "' + parameters.identifier + '" is already in use!'}],
                    ['SiteAccountSettingsCredentialCurrentPasswordMismatchException', {type: 'Warning', message: 'The given password is not matching with the current one!'}],
                    ['SiteAccountSettingsCredentialConfirmPasswordMismatchException', {type: 'Warning', message: 'The confirm password is not matching!'}],

                    // GamePlaySession
                    ['GameSessionNotRecruitingException', {type: 'Warning', message: 'No more free slot!'}],
                    ['PlayerEndpointKeyAlreadyRegisteredSessionException', {type: 'Warning', message: 'This endpoint is already attached to this gameplay!'}],
                    ['UserUuidAlreadyRegisteredSessionException', {type: 'Warning', message: 'You are already in!'}],

                    // GameScenario
                    ['GameScenarioNotFoundException', {type: 'Warning', message: 'Couldn\'t find the game-scenario record!'}],
                    ['GameScenarioNotFoundByUuidException', {type: 'Warning', message: 'Couldn\'t find the game-scenario record by uuid:' + parameters.uuid}],
                    ['GameScenarioAssociationRestrictedException', {type: 'Warning', message: 'The "' + parameters.title + '" game-scenario record association is restricted!'}],
                    ['GameScenarioReferencedElsewhereException', {type: 'Warning', message: 'The "' + parameters.title + '" game-scenario record is still referenced elsewhere!'}],
                    ['GameScenarioTitleIsReservedException', {type: 'Warning', message: 'The title "' + parameters.title + '" is already in use!'}],

                    // HugoCharacter
                    ['HugoCharacterNotFoundException', {type: 'Warning', message: 'Couldn\'t find the character record!'}],
                    ['HugoCharacterNotFoundByUuidException', {type: 'Warning', message: 'Couldn\'t find the character record by uuid:' + parameters.uuid}],
                    ['HugoCharacterNotFoundByCodeException', {type: 'Warning', message: 'Couldn\'t find the character record by code:' + parameters.code}],
                    ['HugoCharacterAssociationRestrictedException', {type: 'Warning', message: 'The "' + parameters.name + '" character record association is restricted!'}],
                    ['HugoCharacterReferencedElsewhereException', {type: 'Warning', message: 'The "' + parameters.name + '" character record is still referenced elsewhere!'}],
                    ['HugoCharacterNameIsReservedException', {type: 'Warning', message: 'The name "' + parameters.name + '" is already in use!'}],
                    ['HugoCharacterCodeIsReservedException', {type: 'Warning', message: 'The code "' + parameters.code + '" is already in use!'}],

                    // HugoVariant
                    ['HugoVariantNotFoundException', {type: 'Warning', message: 'Couldn\'t find the variant record!'}],
                    ['HugoVariantNotFoundByUuidException', {type: 'Warning', message: 'Couldn\'t find the variant record by uuid:' + parameters.uuid}],
                    ['HugoVariantAssociationRestrictedException', {type: 'Warning', message: 'The "' + parameters.name + '" variant record association is restricted!'}],
                    ['HugoVariantReferencedElsewhereException', {type: 'Warning', message: 'The "' + parameters.name + '" variant record is still referenced elsewhere!'}],
                    ['HugoVariantNameIsReservedException', {type: 'Warning', message: 'The name "' + parameters.name + '" is already in use!'}],


                    // HugoHeuristic
                    ['HugoHeuristicNotFoundException', {type: 'Warning', message: 'Couldn\'t find the heuristic record!'}],
                    ['HugoHeuristicNotFoundByUuidException', {type: 'Warning', message: 'Couldn\'t find the heuristic record by uuid:' + parameters.uuid}],
                    ['HugoHeuristicNotFoundByCodeException.java', {type: 'Warning', message: 'Couldn\'t find the heuristic record by code:' + parameters.uuid}],
                    ['HugoHeuristicAssociationRestrictedException', {type: 'Warning', message: 'The "' + parameters.name + '" heuristic record association is restricted!'}],
                    ['HugoHeuristicReferencedElsewhereException', {type: 'Warning', message: 'The "' + parameters.name + '" heuristic record is still referenced elsewhere!'}],
                    ['HugoHeuristicNameIsReservedException', {type: 'Warning', message: 'The name "' + parameters.name + '" is already in use!'}],
                    ['HugoUnexpectedHeuristicEvaluatorTypeCodeException.java', {type: 'Warning', message: 'Unexpected heuristic typeCode:' + parameters.typeCode}],


                    // LizCharacter
                    ['LizCharacterNotFoundException', {type: 'Warning', message: 'Couldn\'t find the character record!'}],
                    ['LizCharacterNotFoundByUuidException', {type: 'Warning', message: 'Couldn\'t find the character record by uuid:' + parameters.uuid}],
                    ['LizCharacterNotFoundByCodeException', {type: 'Warning', message: 'Couldn\'t find the character record by code:' + parameters.code}],
                    ['LizCharacterAssociationRestrictedException', {type: 'Warning', message: 'The "' + parameters.name + '" character record association is restricted!'}],
                    ['LizCharacterReferencedElsewhereException', {type: 'Warning', message: 'The "' + parameters.name + '" character record is still referenced elsewhere!'}],
                    ['LizCharacterNameIsReservedException', {type: 'Warning', message: 'The name "' + parameters.name + '" is already in use!'}],
                    ['LizCharacterCodeIsReservedException', {type: 'Warning', message: 'The code "' + parameters.code + '" is already in use!'}],


                    // LizVariant
                    ['LizVariantNotFoundException', {type: 'Warning', message: 'Couldn\'t find the variant record!'}],
                    ['LizVariantNotFoundByUuidException', {type: 'Warning', message: 'Couldn\'t find the variant record by uuid:' + parameters.uuid}],
                    ['LizVariantAssociationRestrictedException', {type: 'Warning', message: 'The "' + parameters.name + '" variant record association is restricted!'}],
                    ['LizVariantReferencedElsewhereException', {type: 'Warning', message: 'The "' + parameters.name + '" variant record is still referenced elsewhere!'}],
                    ['LizVariantNameIsReservedException', {type: 'Warning', message: 'The name "' + parameters.name + '" is already in use!'}],

                    // LizNeuralConcept
                    ['LizNeuralConceptNotFoundException', {type: 'Warning', message: 'Couldn\'t find the concept record!'}],
                    ['LizNeuralConceptNotFoundByUuidException', {type: 'Warning', message: 'Couldn\'t find the concept record by uuid:' + parameters.uuid}],
                    ['LizNeuralConceptAssociationRestrictedException', {type: 'Warning', message: 'The "' + parameters.name + '" concept record association is restricted!'}],
                    ['LizNeuralConceptReferencedElsewhereException', {type: 'Warning', message: 'The "' + parameters.name + '" concept record is still referenced elsewhere!'}],
                    ['LizNeuralConceptNameIsReservedException', {type: 'Warning', message: 'The name "' + parameters.name + '" is already in use!'}],
                    ['LizNeuralConceptHistoryPlayerNotFoundByUuidException', {type: 'Warning', message: 'Couldn\'t find the history-player for concept record by uuid:' + parameters.uuid}],
                    ['LizNeuralConceptHistoryScenarioNotFoundByUuidException', {type: 'Warning', message: 'Couldn\'t find the history-scenario for concept record by uuid:' + parameters.uuid}],
                    ['LizNeuralConceptDirectoryDeletionFailedException', {type: 'Warning', message: 'Couldn\'t delete file structure of concept'}],
                    ['LizNeuralConceptDeletionBlockedByActiveExecutionException', {type: 'Warning', message: 'Couldn\'t delete concept while there is any unfinished execution!'}],

                    ])
                );
            } else if (exp instanceof SecurityBackendException) {
                let parameters = exp.getParameters();
                return TController.determineDialogData(exp, new Map([
                        ['AccessActionNotGrantedException', {message: 'Access action is not granted!', title: 'Permission warning'}],
                        ['AddActionNotGrantedException', {message: 'Add action is not granted!', title: 'Permission warning'}],
                        ['EditActionNotGrantedException', {message: 'Edit action is not granted!', title: 'Permission warning'}],
                        ['DeleteActionNotGrantedException', {message: 'Delete action is not granted!', title: 'Permission warning'}],
                        ['AccessDeniedException', {message: 'Access denied at:"' + parameters.uri + '"!', title: 'Security warning'}],
                        ['NoResourceFoundException', {message: 'The requested endpoint does not exist!', title: 'Security warning'}],
                        ['SessionExpiredException', {message: 'The Session is expired!', title: 'Security warning'}],
                        ['InsufficientAuthenticationException', {message: 'Insufficient authentication exception!', title: 'Security warning'}],
                        ['UserIsNotAuthenticatedException', {message: 'The user is not authenticated!', title: 'Security warning'}]
                    ])
                );

            } else if (exp instanceof AuthenticationBackendException) {

                return TController.determineDialogData(exp, new Map([
                        ['BadCredentialsException', {message: 'Invalid username or password', title: 'Authentication warning'}],
                        ['LockedException', {message: 'Your account is locked', title: 'Authentication warning'}],
                        ['DisabledException', {message: 'Your account is disabled', title: 'Authentication warning'}],
                        ['AccountExpiredException', {message: 'Your account has expired', title: 'Authentication warning'}],
                        ['CredentialsExpiredException', {message: 'Your password has expired', title: 'Authentication warning'}]
                    ])
                );
            } else {
                throw new Error('Unsupported child class of TBackendException exception: ' + exp.constructor.name);
            }
        } else if (exp instanceof TFailureException) {
            if (exp instanceof TPermissionFailureException) {
                return TController.determineDialogData(exp, new Map([
                        ['AccessActionNotGrantedFailureException', {message: 'Sorry! You do not have the necessary "ACCESS" permission!', title: 'Permission warning'}],
                        ['AddActionNotGrantedFailureException', {message: 'Sorry! You do not have the necessary "ADD" permission!', title: 'Permission warning'}],
                        ['DeleteActionNotGrantedFailureException', {message: 'Sorry! You do not have the necessary "DELETE" permission!', title: 'Permission warning'}],
                        ['EditActionNotGrantedFailureException', {message: 'Sorry! You do not have the necessary "EDIT" permission!', title: 'Permission warning'}],
                    ])
                );

            } else if (exp instanceof TFailureException) {

                return TController.determineDialogData(exp, new Map([
                        ['NetworkFailureException', {message: 'Couldn\'t communicate with endpoint!', title: 'Network warning'}],
                    ])
                );
            }
        } else if (exp instanceof TErrorException) {

            if (exp instanceof CustomErrorException) {
                return {
                    type: 'Error',
                    title: 'Error',
                    message: exp.getMessage()
                }
            } else {
                return {
                    type: 'Error',
                    title: 'Error',
                    message: 'Uncategorised TErrorException is found!'
                }
            }
        } else if (exp instanceof TException) {
            return {
                type: 'Error',
                title: 'Error',
                message: 'Uncategorised TException is found!'
            }
        } else {
            return {
                type: 'Error',
                title: 'Error',
                message: 'Uncategorised Exception is found!'
            }
        }
    }



    static determineDialogData(exp, messageList = {}) {

        //
        if (!messageList.has(exp.getExceptionCode())) {
            return {
                type: 'Error',
                title: "",
                message: 'Unsupported exception occurred! The code is:' + exp.getExceptionCode()
            }
        }


        //
        const item = messageList.get(exp.getExceptionCode());

        let type = 'Warning';
        if (Object.hasOwn(item, 'type')) {
            type = item.type;
        }

        let title = 'Warning';
        if (type === 'Error') {
            title = 'Error';
        }
        if (Object.hasOwn(item, 'title')) {
            title = item.title;
        }

        //
        return {
            type: type,
            title: title,
            message: item.message
        }
    }

    static displaySavedToast() {
        const toast = useToast();
        toast.success('Saved!');
    }

    static displayModifiedToast() {
        const toast = useToast();
        toast.success('Modified!');
    }

    static displayDeletedToast() {
        const toast = useToast();
        toast.success('Deleted!');
    }
}