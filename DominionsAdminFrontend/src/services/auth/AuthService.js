
import {useAuthStore} from "@/stores/AuthStore";
import AccessActionNotGrantedFailureException from "@/framework/exception/permission/AccessActionNotGrantedFailureException";
import AddActionNotGrantedFailureException from "@/framework/exception/permission/AddActionNotGrantedFailureException";
import EditActionNotGrantedFailureException from "@/framework/exception/permission/EditActionNotGrantedFailureException";
import DeleteActionNotGrantedFailureException from "@/framework/exception/permission/DeleteActionNotGrantedFailureException";
import TService from "@/framework/TService";
import NetworkFailureException from "@/framework/exception/failure/NetworkFailureException";

class AuthService extends TService {
    getAuthStore() {
        const authStore = useAuthStore();
        return authStore;
    }

    getCurrentUsername() {
        return this.getAuthStore().user.username;
    }

    getCurrentUserPermissions() {
        return this.getAuthStore().user.permissions;
    }

    hasCurrentUserPermission(permission) {
        const permissions = this.getCurrentUserPermissions();

        // console.log(permissions);
        return permissions.includes(permission);
    }

    // isRoutingPermitted(to, from) {
    //     if(to.path == '/breed' && this.hasCurrentUserPermission('Breed:VIEW')) {
    //         return true;
    //     }
    //     if(from.path == '') {
    //         console.log('');
    //     }
    //
    //     return false;
    // }

    isAccessActionGrantedOn(resource) {
        return this.hasCurrentUserPermission(resource + ':VIEW');
    }

    isAddActionGrantedOn(resource) {
        return this.hasCurrentUserPermission(resource + ':ADD');
    }

    isEditActionGrantedOn(resource) {
        return this.hasCurrentUserPermission(resource + ':EDIT');
    }

    isDeleteActionGrantedOn(resource) {
        return this.hasCurrentUserPermission(resource + ':DELETE');
    }


    assertAccessActionGrantedOn(resource) {
        if(!this.isAccessActionGrantedOn(resource)) {
            throw new AccessActionNotGrantedFailureException();
        }
    }

    assertAddActionGrantedOn(resource) {
        if(!this.isAddActionGrantedOn(resource)) {
            throw new AddActionNotGrantedFailureException();
        }
    }

    assertEditActionGrantedOn(resource) {
        if(!this.isEditActionGrantedOn(resource)) {
            throw new EditActionNotGrantedFailureException();
        }
    }

    assertDeleteActionGrantedOn(resource) {
        if(!this.isDeleteActionGrantedOn(resource)) {
            throw new DeleteActionNotGrantedFailureException();
        }
    }


    isUserAuthenticated() {
        return this.getAuthStore().isAuthenticated;
    }

    async login(userData) {

        console.log("login", userData);
        try {
            const url = super.extendWithHost('/auth/admin/login');
            // Communicate with server
            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                credentials: 'include',
                body: new URLSearchParams({
                    identifier: userData.identifier,
                    password: userData.password
                })
            });


            // Handling response
            console.log("X1");
            const answer = await TService.handleHttpResponse(response);
            console.log("X2");

            // Save data of authenticated user
            const authStore = this.getAuthStore();
            console.log("X3");
            // console.log("ANSWER:",answer.data);
            authStore.isAuthenticated = true;
            authStore.user.identifier = answer.data.identifier;
            authStore.user.permissions = answer.data.permissions;

            console.log("X4");
            console.log("answer",answer);
            return answer;

        } catch(exp) {
            console.log("X5");
            if(exp instanceof TypeError) {
                console.log(exp);
                throw new NetworkFailureException();
            }
            throw exp;
        }
    }



    async logout() {

        try {
            const url = super.extendWithHost('/auth/admin/logout');
            console.log("A1",url);
            // Communicate with server
            console.log("url",url);
            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                credentials: 'include'
            });
            console.log("A2",url);
            // Handling response
            const answer = await TService.handleHttpResponse(response);
            console.log("A3",url);

            this.invalidateAuthentication();
            console.log("A4",url);

            //window.location.href = "/login"; // Átirányítás


            //
            // // Save data of authenticated user
            // const authStore = this.getAuthStore();
            // authStore.isAuthenticated = true;
            // authStore.user.identifier = answer.identifier;
            // authStore.user.permissions = answer.permissions;

            return answer;

        } catch(exp) {
            if(exp instanceof TypeError) {
                console.log(exp);
                throw new NetworkFailureException();
            }
            throw exp;
        }
    }

    invalidateAuthentication() {
        const authStore = this.getAuthStore();
        authStore.isAuthenticated = false;
        authStore.user = {identifier: null, permissions: []};
        localStorage.removeItem('auth');
        localStorage.clear();
    }


    generateAccessPermissionOn(resource) {
        return resource + ':VIEW';
    }
    generateAddPermissionOn(resource) {
        return resource + ':ADD';
    }
    generateEditPermissionOn(resource) {
        return resource + ':EDIT';
    }
    generateDeletePermissionOn(resource) {
        return resource + ':DELETE';
    }
}

// Create singleton instance
const authService = new AuthService();

// Exports
export default authService;
export {AuthService, authService};
