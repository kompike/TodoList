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

    return {
        'onUserAdded' : _onUserAdded
    };
};

if (typeof define !== 'function') {
    var define = require('amdefine')(module);
}

define(function() {
    return UserService;
});