var UserService = function(eventBus, serverURL) {

    var _addUser = function(user) {
        var email = user.email;
        var password = user.password;
        var confirmPassword = user.confirmPassword;
        $.post(serverURL + "api/register",{
            email: email,
            password: password,
            confirmPassword: confirmPassword
        }, function(xhr) {
            var data = JSON.parse(xhr);
            eventBus.post(Events.USER_REGISTERED, data.message);
        }, 'text')
            .fail(function(xhr, status, error) {
                var err = JSON.parse(xhr.responseText);
                eventBus.post(Events.REGISTRATION_FAILED, err.errorMessage);
            });
    };

    var _onUserAdded = function(user) {
        return _addUser(user);
    };

    var _loginUser = function(user) {
        var email = user.email;
        var password = user.password;
        $.post(
            serverURL + "api/login",
            {
                email: email,
                password: password
            },
            function(xhr) {
                var data = JSON.parse(xhr);
                localStorage.setItem('tokenId', data.tokenId);
                localStorage.setItem('currentUser', data.email);
                eventBus.post(Events.LOGIN_SUCCESSFULL, data);
            }, 'text')
            .fail(function(xhr, status, error) {
                var err = JSON.parse(xhr.responseText);
                eventBus.post(Events.LOGIN_FAILED, err.errorMessage);
            });
    };

    var _onUserLogin = function(user) {
        _loginUser(user);
    };

    var _logoutUser = function(data) {
        $.post(
            serverURL + "api/logout",
            {
                tokenId: data.tokenId
            },
            function(xhr) {
                var data = JSON.parse(xhr);
                localStorage.removeItem('tokenId');
                localStorage.removeItem('currentUser');
                eventBus.post(Events.USER_LOGGED_OUT, data);
            }, 'text');
    };

    var _onUserLogout = function(data) {
        _logoutUser(data);
    };

    return {
        'onUserAdded' : _onUserAdded,
        'onUserLogin' : _onUserLogin,
        'onUserLogout' : _onUserLogout
    };
};

if (typeof define !== 'function') {
    var define = require('amdefine')(module);
}

define(function() {
    return UserService;
});