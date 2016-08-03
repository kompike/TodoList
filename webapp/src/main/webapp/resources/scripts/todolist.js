var TodoList = function(rootDivId, eventBus, userService) {

    var _initialize = function() {

        var registrationDivId = rootDivId + '_register';

        $('<div/>').appendTo('body').attr('id', rootDivId);

        var registrationComponent = new RegistrationFormComponent(registrationDivId);
        
        eventBus.subscribe(Events.NEW_USER_ADDITION, userService.onUserAdded);

        registrationComponent.initialize();
    };

    var RegistrationFormComponent = function (_elementDivId) {

        var _initialize = function () {

            eventBus.subscribe(Events.REGISTRATION_FAILED, _onRegistrationFailed);
            eventBus.subscribe(Events.USER_REGISTERED, _onUserRegistered);

            $('#' + rootDivId).append($('<div/>').attr('id', _elementDivId));

            var registrationFormBoxId = _elementDivId + '_box';
            var buttonId = registrationFormBoxId + '_btn';
            var errorDivId = registrationFormBoxId + '_err';

            $('#' + _elementDivId).html($('<div/>').attr('id', _elementDivId + '_box')
                .append($('<h5/>').html('Registration form'))
                .append($('<label/>').attr('for', 'email').text('Email'))
                .append($('<input/>').attr({
                    'id': _elementDivId + '_email',
                    'name': 'email',
                    'type': 'text'
                })).append('<br/>')
                .append($('<label/>').attr('for', 'password').text('Password'))
                .append($('<input/>').attr({
                    'id': _elementDivId + '_password',
                    'name': 'password',
                    'type': 'password'
                })).append('<br/>')
                .append($('<label/>').attr('for', 'confirm_password').text('Confirm password'))
                .append($('<input/>').attr({
                    'id': _elementDivId + '_confirm_password',
                    'name': 'confirm_password',
                    'type': 'password'
                })).append('<br/>')
                .append($('<div/>').attr('id', errorDivId)).append('<br/>')
                .append($('<div/>').attr('id', _elementDivId + '_box_success')).append('<br/>')
                .append($('<button/>').attr('id', buttonId).text('Register').click(function () {
                    var user = {
                        'email': $('#' + _elementDivId + '_email').val(),
                        'password': $('#' + _elementDivId + '_password').val(),
                        'confirmPassword': $('#' + _elementDivId + '_confirm_password').val()
                    };
                    eventBus.post(Events.NEW_USER_ADDITION, user);
                })))
            $('#' + rootDivId).append($('<div/>').attr('id', rootDivId + '_registered')
                .append($('<h6/>').html('Already registered?'))
                .append($('<button/>').attr('id', buttonId + "_login").text('Login').click(function () {

                    eventBus.post(Events.USER_REGISTERED, {});
                })))
        };

        var _onUserRegistered = function (message) {
            $('#' + _elementDivId + '_box_err').html('');
            $('#' + _elementDivId + '_box_success').html($('<span/>').text(message));
        }

        var _onRegistrationFailed = function (message) {
            _registrationFailed(message);
        }

        var _registrationFailed = function (message) {
            $('#' + _elementDivId + '_box_success').html('');
            $('#' + _elementDivId + '_box_err').html($('<span/>').text(message));
        };

        return {
            'initialize': _initialize
        };
    };

    return {'initialize' : _initialize};
}

if (typeof define !== 'function') {
    var define = require('amdefine')(module);
}

define(function() {
    return TodoList;
});