import ActionNotGrantedFailureException from "@/framework/exception/permission/ActionNotGrantedFailureException";
import TWarningDialog from "@/framework/component/TWarningDialog/TWarningDialog";
import GeneralFailureException from "@/framework/exception/failure/GeneralFailureException";
import GeneralErrorException from "@/framework/exception/error/GeneralErrorException";
import AuthenticationFailureException from "@/framework/exception/failure/AuthenticationFailureException";
import TFailure from "@/framework/exception/failure/TFailure";
import TAlertDialog from "@/framework/component/TAlertDialog/TAlertDialog";
import TError from "@/framework/exception/error/TError";
import TException from "@/framework/exception/TException";
import NetworkFailureException from "@/framework/exception/failure/NetworkFailureException";
import SecurityFailureException from "@/framework/exception/failure/SecurityFailureException";
import {useToast} from "vue-toastification";
import authService from "@/services/auth/AuthService";
import router from "@/router";
import backendConfiguration from "@/configurations/backendConfiguration";
import HttpFailureException from "@/framework/exception/failure/HttpFailureException";
import CustomErrorException from "@/framework/exception/error/CustomlErrorException";

export default class TController {

    static getTime() {
        let date = new Date();
        return date.getTime();

    }


    static async generateMessages(exp) {
        if (exp instanceof TFailure) {
            if (exp instanceof HttpFailureException) {
                const messageList = new Map([

                    [400, {title: "Bad Request", message: "The request is invalid."}],
                    [401, {title: "Unauthorized", message: "Please sign in first."}],
                    [403, {title: "Forbidden", message: "Access is not allowed."}],
                    [404, {title: "Not Found", message: "The page was not found."}],
                    [405, {title: "Method Not Allowed", message: "This action is not allowed."}],
                    [408, {title: "Request Timeout", message: "The request timed out."}],
                    [409, {title: "Conflict", message: "The request conflicts with existing data."}],
                    [429, {title: "Too Many Requests", message: "Too many requests. Try again later."}],

                    [500, {title: "Internal Server Error", message: "An unexpected error occurred."}],
                    [501, {title: "Not Implemented", message: "This feature is not available."}],
                    [502, {title: "Bad Gateway", message: "The server received an invalid response."}],
                    [503, {title: "Service Unavailable", message: "The service is temporarily unavailable."}],
                    [504, {title: "Gateway Timeout", message: "The server took too long to respond."}]

                ]);

                //
                if (messageList.has(exp.getCode())) {
                    const item = messageList.get(exp.getCode());

                    await TAlertDialog({
                        title: "Http: " + exp.getCode() + " - " + item.title,
                        message: item.message
                    });
                } else {
                    await TAlertDialog({
                        message: 'Unsupported HttpFailureException occurred! code:' + exp.getCode()
                    });
                }
            } else if (exp instanceof GeneralFailureException) {
                console.log("GeneralFailureException:", exp.getCode());

                //
                let data = exp.getData();
                const messageList = new Map([
                    // Admin-User
                    ['AdminUserNotFoundException', {message: 'Couldn\'t find the user record!'}],
                    ['AdminUserNotFoundByUuidException', {message: 'Couldn\'t find the user record by uuid:' + data.parameters.uuid}],
                    ['AdminUserAssociationRestrictedException', {message: 'The "' + data.parameters.identifier + '" user record association is restricted!'}],
                    ['AdminUserReferencedElsewhereException', {message: 'The "' + data.parameters.identifier + '" user record is still referenced elsewhere!'}],
                    ['AdminUserSelfDeleteException', {message: 'The "' + data.parameters.identifier + '" user record not deletable in its account!'}],
                    ['AdminUserConfirmPasswordMismatchException', {message: 'The confirm password is not matching!'}],
                    ['AdminUserEnabledButNotFinalisedException', {message: 'The user is enabled but not finalised!'}],
                    ['AdminUserIdentifierIsReservedException', {message: 'The identifier "' + data.parameters.identifier + '" is already in use!'}],

                    // Admin-Profile
                    ['AdminProfileNotFoundException', {message: 'Couldn\'t find the settings record!'}],
                    ['AdminProfileNotFoundByUuidException', {message: 'Couldn\'t find the settings record by uuid:' + data.parameters.uuid}],
                    ['AdminProfileAssociationRestrictedException', {message: 'The "' + data.parameters.identifier + '" settings record association is restricted!'}],
                    ['AdminProfileReferencedElsewhereException', {message: 'The "' + data.parameters.identifier + '" settings record is still referenced elsewhere!'}],
                    ['AdminProfileSelfDeleteException', {message: 'The "' + data.parameters.identifier + '" settings record not deletable in its account!'}],
                    ['AdminProfileConfirmPasswordMismatchException', {message: 'The confirm password is not matching!'}],
                    ['AdminProfileCurrentPasswordMismatchException', {message: 'The given password is not matching with the current one!'}],
                    ['AdminProfileIdentifierIsReservedException', {message: 'The identifier "' + data.parameters.identifier + '" is already in use!'}],

                    // Admin-PermissionGroup
                    ['AdminPermissionGroupNotFoundException', {message: 'Couldn\'t find the permission-group record!'}],
                    ['AdminPermissionGroupNotFoundByUuidException', {message: 'Couldn\'t find the permission-group record by uuid:' + data.parameters.uuid}],
                    ['AdminPermissionGroupAssociationRestrictedException', {message: 'The "' + data.parameters.name + '" permission-group record association is restricted!'}],
                    ['AdminPermissionGroupReferencedElsewhereException', {message: 'The "' + data.parameters.name + '" permission-group record is still referenced elsewhere!'}],
                    ['AdminPermissionGroupNameIsReservedException', {message: 'The name "' + data.parameters.name + '" is already in use!'}],


                    // Site-User
                    ['SiteUserNotFoundException', {message: 'Couldn\'t find the user record!'}],
                    ['SiteUserNotFoundByUuidException', {message: 'Couldn\'t find the user record by uuid:' + data.parameters.uuid}],
                    ['SiteUserAssociationRestrictedException', {message: 'The "' + data.parameters.identifier + '" user record association is restricted!'}],
                    ['SiteUserReferencedElsewhereException', {message: 'The "' + data.parameters.identifier + '" user record is still referenced elsewhere!'}],
                    ['SiteUserSelfDeleteException', {message: 'The "' + data.parameters.identifier + '" user record not deletable in its account!'}],
                    ['SiteUserConfirmPasswordMismatchException', {message: 'The confirm password is not matching!'}],
                    ['SiteUserEnabledButNotFinalisedException', {message: 'The user is enabled but not finalised!'}],
                    ['SiteUserIdentifierIsReservedException', {message: 'The identifier "' + data.parameters.identifier + '" is already in use!'}],


                    // Site-PermissionGroup
                    ['SitePermissionGroupNotFoundException', {message: 'Couldn\'t find the permission-group record!'}],
                    ['SitePermissionGroupNotFoundByUuidException', {message: 'Couldn\'t find the permission-group record by uuid:' + data.parameters.uuid}],
                    ['SitePermissionGroupAssociationRestrictedException', {message: 'The "' + data.parameters.name + '" permission-group record association is restricted!'}],
                    ['SitePermissionGroupReferencedElsewhereException', {message: 'The "' + data.parameters.name + '" permission-group record is still referenced elsewhere!'}],
                    ['SitePermissionGroupNameIsReservedException', {message: 'The name "' + data.parameters.name + '" is already in use!'}],


                    // GameScenario
                    ['GameScenarioNotFoundException', {message: 'Couldn\'t find the game-scenario record!'}],
                    ['GameScenarioNotFoundByUuidException', {message: 'Couldn\'t find the game-scenario record by uuid:' + data.parameters.uuid}],
                    ['GameScenarioAssociationRestrictedException', {message: 'The "' + data.parameters.title + '" game-scenario record association is restricted!'}],
                    ['GameScenarioReferencedElsewhereException', {message: 'The "' + data.parameters.title + '" game-scenario record is still referenced elsewhere!'}],
                    ['GameScenarioTitleIsReservedException', {message: 'The title "' + data.parameters.title + '" is already in use!'}],


                    // HugoCharacter
                    ['HugoCharacterNotFoundException', {message: 'Couldn\'t find the character record!'}],
                    ['HugoCharacterNotFoundByUuidException', {message: 'Couldn\'t find the character record by uuid:' + data.parameters.uuid}],
                    ['HugoCharacterNotFoundByCodeException', {message: 'Couldn\'t find the character record by code:' + data.parameters.code}],
                    ['HugoCharacterAssociationRestrictedException', {message: 'The "' + data.parameters.name + '" character record association is restricted!'}],
                    ['HugoCharacterReferencedElsewhereException', {message: 'The "' + data.parameters.name + '" character record is still referenced elsewhere!'}],
                    ['HugoCharacterNameIsReservedException', {message: 'The name "' + data.parameters.name + '" is already in use!'}],
                    ['HugoCharacterCodeIsReservedException', {message: 'The code "' + data.parameters.code + '" is already in use!'}],


                    // HugoVariant
                    ['HugoVariantNotFoundException', {message: 'Couldn\'t find the variant record!'}],
                    ['HugoVariantNotFoundByUuidException', {message: 'Couldn\'t find the variant record by uuid:' + data.parameters.uuid}],
                    ['HugoVariantAssociationRestrictedException', {message: 'The "' + data.parameters.name + '" variant record association is restricted!'}],
                    ['HugoVariantReferencedElsewhereException', {message: 'The "' + data.parameters.name + '" variant record is still referenced elsewhere!'}],
                    ['HugoVariantNameIsReservedException', {message: 'The name "' + data.parameters.name + '" is already in use!'}],


                    // HugoHeuristic
                    ['HugoHeuristicNotFoundException', {message: 'Couldn\'t find the heuristic record!'}],
                    ['HugoHeuristicNotFoundByUuidException', {message: 'Couldn\'t find the heuristic record by uuid:' + data.parameters.uuid}],
                    ['HugoHeuristicNotFoundByCodeException.java', {message: 'Couldn\'t find the heuristic record by code:' + data.parameters.uuid}],
                    ['HugoHeuristicAssociationRestrictedException', {message: 'The "' + data.parameters.name + '" heuristic record association is restricted!'}],
                    ['HugoHeuristicReferencedElsewhereException', {message: 'The "' + data.parameters.name + '" heuristic record is still referenced elsewhere!'}],
                    ['HugoHeuristicNameIsReservedException', {message: 'The name "' + data.parameters.name + '" is already in use!'}],
                    ['HugoUnexpectedHeuristicEvaluatorTypeCodeException.java', {message: 'Unexpected heuristic typeCode:' + data.parameters.typeCode}],


                    // LizCharacter
                    ['LizCharacterNotFoundException', {message: 'Couldn\'t find the character record!'}],
                    ['LizCharacterNotFoundByUuidException', {message: 'Couldn\'t find the character record by uuid:' + data.parameters.uuid}],
                    ['LizCharacterNotFoundByCodeException', {message: 'Couldn\'t find the character record by code:' + data.parameters.code}],

                    ['LizCharacterAssociationRestrictedException', {message: 'The "' + data.parameters.name + '" character record association is restricted!'}],
                    ['LizCharacterReferencedElsewhereException', {message: 'The "' + data.parameters.name + '" character record is still referenced elsewhere!'}],
                    ['LizCharacterNameIsReservedException', {message: 'The name "' + data.parameters.name + '" is already in use!'}],
                    ['LizCharacterCodeIsReservedException', {message: 'The code "' + data.parameters.code + '" is already in use!'}],


                    // LizVariant
                    ['LizVariantNotFoundException', {message: 'Couldn\'t find the variant record!'}],
                    ['LizVariantNotFoundByUuidException', {message: 'Couldn\'t find the variant record by uuid:' + data.parameters.uuid}],
                    ['LizVariantAssociationRestrictedException', {message: 'The "' + data.parameters.name + '" variant record association is restricted!'}],
                    ['LizVariantReferencedElsewhereException', {message: 'The "' + data.parameters.name + '" variant record is still referenced elsewhere!'}],
                    ['LizVariantNameIsReservedException', {message: 'The name "' + data.parameters.name + '" is already in use!'}],


                    // LizNeuralConcept
                    ['LizNeuralConceptNotFoundException', {message: 'Couldn\'t find the concept record!'}],
                    ['LizNeuralConceptNotFoundByUuidException', {message: 'Couldn\'t find the concept record by uuid:' + data.parameters.uuid}],
                    ['LizNeuralConceptAssociationRestrictedException', {message: 'The "' + data.parameters.name + '" concept record association is restricted!'}],
                    ['LizNeuralConceptReferencedElsewhereException', {message: 'The "' + data.parameters.name + '" concept record is still referenced elsewhere!'}],
                    ['LizNeuralConceptNameIsReservedException', {message: 'The name "' + data.parameters.name + '" is already in use!'}],
                    ['LizNeuralConceptHistoryPlayerNotFoundByUuidException', {message: 'Couldn\'t find the history-player for concept record by uuid:' + data.parameters.uuid}],
                    ['LizNeuralConceptHistoryScenarioNotFoundByUuidException', {message: 'Couldn\'t find the history-scenario for concept record by uuid:' + data.parameters.uuid}],
                    ['LizNeuralConceptDirectoryDeletionFailedException', {message: 'Couldn\'t delete file structure of concept'}],
                    ['LizNeuralConceptDeletionBlockedByActiveExecutionException', {message: 'Couldn\'t delete concept while there is any unfinished execution!'}]


                ]);

                //
                if (messageList.has(exp.getCode())) {
                    const item = messageList.get(exp.getCode());

                    await TWarningDialog({
                        message: item.message
                    });
                } else {
                    await TWarningDialog({
                        message: 'Unsupported GeneralFailureException occurred! code:' + exp.getCode()
                    });
                }

            } else if (exp instanceof SecurityFailureException) {
                let data = exp.getData();
                const messageList = new Map([
                    ['AccessActionNotGrantedException', {message: 'Access action is not granted!', title: 'Permission warning'}],
                    ['AddActionNotGrantedException', {message: 'Add action is not granted!', title: 'Permission warning'}],
                    ['EditActionNotGrantedException', {message: 'Edit action is not granted!', title: 'Permission warning'}],
                    ['DeleteActionNotGrantedException', {message: 'Delete action is not granted!', title: 'Permission warning'}],
                    ['AccessDeniedException', {message: 'Access denied at:"' + data.parameters.uri + '"!', title: 'Security warning'}],
                    ['NoResourceFoundException', {message: 'The requested endpoint does not exist!', title: 'Security warning'}],
                    ['SessionExpiredException', {message: 'The Session is expired!', title: 'Security warning'}],
                    ['InsufficientAuthenticationException', {message: 'Insufficient authentication exception!', title: 'Security warning'}],
                    ['UserIsNotAuthenticatedException', {message: 'The user is not authenticated!', title: 'Security warning'}]
                ]);

                //
                if (messageList.has(exp.getCode())) {
                    const item = messageList.get(exp.getCode());

                    await TWarningDialog({
                        title: item.title,
                        message: item.message
                    });
                } else {
                    await TWarningDialog({
                        message: 'Unsupported SecurityFailureException occurred! code:' + exp.getCode()
                    });
                }

            } else if (exp instanceof AuthenticationFailureException) {

                const messageList = new Map([
                    ['BadCredentialsException', {message: 'Invalid username or password'}],
                    ['LockedException', {message: 'Your account is locked'}],
                    ['DisabledException', {message: 'Your account is disabled'}],
                    ['AccountExpiredException', {message: 'Your account has expired'}],
                    ['CredentialsExpiredException', {message: 'Your password has expired'}]
                ]);

                //
                if (messageList.has(exp.getCode())) {
                    const item = messageList.get(exp.getCode());

                    await TWarningDialog({
                        title: item.title,
                        message: item.message
                    });
                } else {
                    await TWarningDialog({
                        message: 'Unsupported AuthenticationFailureException occurred! code:' + exp.getCode()
                    });
                }
            } else if (exp instanceof ActionNotGrantedFailureException) {
                await TWarningDialog({
                    title: 'Permission warning',
                    message: 'Sorry! You do not have the necessary "' + exp.getAction() + '" permission!'
                });
            } else if (exp instanceof NetworkFailureException) {
                await TWarningDialog({
                    title: 'Network warning',
                    message: 'Couldn\'t communicate with endpoint!'
                });
            } else {
                await TWarningDialog({
                    message: ['Uncategorised TFailure is found!', exp]
                });
            }
        } else if (exp instanceof TError) {

            if (exp instanceof CustomErrorException) {
                await TAlertDialog({
                    message: exp.getMessage()
                });
            } else if (exp instanceof GeneralErrorException) {

                const messageList = new Map([
                    ['InternalServerError', {message: 'InternalServerError'}],
                    ['NotParseableResponseProxyException', {message: 'Inner Module communication error due to parsing problem!'}],
                    ['HttpStatusResponseProxyException', {message: 'Inner Module communication error with code:' + exp.getData().parameters.statusCode}],
                ]);

                //
                if (messageList.has(exp.getCode())) {
                    const item = messageList.get(exp.getCode());

                    await TAlertDialog({
                        title: item.title,
                        message: item.message
                    });
                } else {
                    await TAlertDialog({
                        message: 'Unsupported GeneralErrorException occurred! code:' + exp.getCode()
                    });
                }
            } else {
                await TAlertDialog({
                    message: ['Uncategorised TError is found!', exp]
                });
            }
        } else if (exp instanceof TException) {
            await TAlertDialog({
                message: ['Uncategorised TException is found!', exp]
            });
        } else {
            await TAlertDialog({
                message: ['Uncategorised exception is found!', exp]
            });
        }
    }

    static async handleExceptions(exp) {


        //
        if (exp instanceof SecurityFailureException) {
            if (exp.getCode() == 'SessionExpiredException') {
                authService.invalidateAuthentication();
                router.push('/');
            } else if (exp.getCode() == 'InsufficientAuthenticationException') {
                authService.invalidateAuthentication();
                router.push('/');
            }

        }

        //
        await TController.generateMessages(exp);

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

    static displaySuccessToast(message) {
        const toast = useToast();
        toast.success(message);
    }


}