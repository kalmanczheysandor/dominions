export const ServerResponseManager = {

    async generateReturn(response) {
        let data = null;

        const contentType = response.headers.get('content-type');
        if(contentType && contentType.includes('application/json')) {
            data = await response.json();
        }

        // console.log("data");
        // console.log(data);

        if(response.ok) {
            return {success: true, data: data};
        } else {
            let errorMessages = [];

            if(data.type && data.type == "GeneralFailureResponse") {

                if(data.code == "BreedNameIsReservedException") {
                    errorMessages.push('The name "' + data.parameters.name + '" is already in use!')
                } else if(data.code == "NoResourceFoundException") {
                    errorMessages.push('The requested endpoint does not exist!')
                } else if(data.code == "BreedNotFoundByUuidException") {
                    errorMessages.push('The requested endpoint does not exist!')
                } else {
                    errorMessages.push("??[ErrorResponse]:" + data.code + "??");
                }
            }
            else if(data.type && data.type == "AuthenticationFailureResponse") {
                errorMessages.push('Login is rejected!')
            }
            else {
                alert(data);
            }

            return {success: false, messages: errorMessages};
        }
    }





}