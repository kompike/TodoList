import Events from '../events';
import eventBus from '../eventbus';

class UserService {

    constructor() {
    }

    addUser(user) {
        $.ajax({
            url: 'http://localhost:8080/api/register',
            method: 'post',
            dataType: 'json',
            data: {
                email: user.email,
                password: user.password,
                confirmPassword: user.confirmPassword
            },
            cache: false,
            success: function (data) {
                eventBus.post(Events.USER_REGISTERED, data.message);
            },
            error: function (xhr, status, err) {
                let error = JSON.parse(xhr.responseText);
                eventBus.post(Events.REGISTRATION_FAILED, error.errorMessage);
            }
        });
    }

    loginUser(user) {
        $.ajax({
            url: 'http://localhost:8080/api/login',
            method: 'post',
            dataType: 'json',
            data: {
                email: user.email,
                password: user.password
            },
            cache: false,
            success: function (data) {
                localStorage.setItem('tokenId', data.tokenId);
                localStorage.setItem('currentUser', data.email);
                eventBus.post(Events.LOGIN_SUCCESSFULL, data);
            },
            error: function (xhr, status, err) {
                let error = JSON.parse(xhr.responseText);
                eventBus.post(Events.LOGIN_FAILED, error.errorMessage);
            }
        });
    }

    logoutUser(data) {
        $.ajax({
            url: 'http://localhost:8080/api/logout',
            method: 'post',
            dataType: 'json',
            data: {
                tokenId: data.tokenId
            },
            cache: false,
            success: function (data) {
                localStorage.removeItem('tokenId');
                localStorage.removeItem('currentUser');
                eventBus.post(Events.USER_LOGGED_OUT, data);
            }
        });
    }
}

let userService = new UserService();
export default userService;