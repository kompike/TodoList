var TodoList = function (rootDivId, eventBus, userService, taskService) {

    var _initialize = function () {

        var registrationDivId = rootDivId + '_register';
        var loginDivId = rootDivId + '_login';
        var dashboardDivId = rootDivId + '_dashboard';

        $('<div/>').appendTo('body').attr('id', rootDivId);

        var registrationComponent = new RegistrationFormComponent(registrationDivId);
        var loginFormComponent = new LoginFormComponent(loginDivId);
        var dashboardComponent = new DashboardComponent(dashboardDivId);

        eventBus.subscribe(Events.NEW_USER_ADDITION, userService.onUserAdded);
        eventBus.subscribe(Events.USER_ALREADY_REGISTERED, loginFormComponent.initialize);
        eventBus.subscribe(Events.USER_NOT_REGISTERED, registrationComponent.initialize);
        eventBus.subscribe(Events.LOGIN_SUCCESSFULL, dashboardComponent.initialize);
        eventBus.subscribe(Events.LOGIN_ATTEMPT, userService.onUserLogin);
        eventBus.subscribe(Events.NEW_TASK_ADDITION, taskService.onTaskAdded);

        registrationComponent.initialize();
    };

    var _onInputFieldEvent = function (inputDivId) {
        $(inputDivId).keydown(function (event) {
            var parent = $(this).parent();
            if (event.ctrlKey && event.which == 13) {
                parent.children('button').click();
            }
        });
    };

    var RegistrationFormComponent = function (_elementDivId) {

        var _initialize = function () {

            eventBus.subscribe(Events.REGISTRATION_FAILED, _onRegistrationFailed);
            eventBus.subscribe(Events.USER_REGISTERED, _onUserRegistered);
            eventBus.subscribe(Events.USER_ALREADY_REGISTERED, _onUserAlreadyRegistered);

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
                })));

            $('#' + _elementDivId + '_email').focus();

            $('#' + rootDivId).append($('<div/>').attr('id', rootDivId + '_registered')
                .append($('<h6/>').html('Already registered?'))
                .append($('<button/>').attr('id', buttonId + "_login").text('Login').click(function () {

                    eventBus.post(Events.USER_ALREADY_REGISTERED, {});
                })))

            _onInputFieldEvent('input');
        };

        var _onUserRegistered = function (message) {
            $('#' + _elementDivId + '_box_err').html('');
            $('#' + _elementDivId + '_box_success').html($('<span/>').text(message));
        }

        var _onUserAlreadyRegistered = function () {
            $('#' + rootDivId + '_register').remove();
            $('#' + rootDivId + '_registered').remove();
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

    var LoginFormComponent = function (_elementDivId) {

        var _initialize = function () {

            eventBus.subscribe(Events.LOGIN_FAILED, _onLoginFailed);
            eventBus.subscribe(Events.USER_NOT_REGISTERED, _onUserLoggedIn);
            eventBus.subscribe(Events.LOGIN_SUCCESSFULL, _onUserLoggedIn);

            $('#' + rootDivId).append($('<div/>').attr('id', _elementDivId));

            var loginFormBoxId = _elementDivId + '_box';
            var buttonId = loginFormBoxId + '_btn';
            var errorDivId = loginFormBoxId + '_err';

            $('#' + _elementDivId).html($('<div/>').attr('id', _elementDivId + '_box')
                .append($('<h5/>').html('Login form'))
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
                .append($('<div/>').attr('id', errorDivId)).append('<br/>')
                .append($('<button/>').attr('id', buttonId).text('Login').click(function () {
                    var user = {
                        'email': $('#' + _elementDivId + '_email').val(),
                        'password': $('#' + _elementDivId + '_password').val()
                    };
                    eventBus.post(Events.LOGIN_ATTEMPT, user);
                })));

            $('#' + _elementDivId + '_email').focus();


            $('#' + rootDivId).append($('<div/>').attr('id', rootDivId + '_to_registration')
                .append($('<h6/>').html('Not registered?'))
                .append($('<button/>').attr('id', buttonId + "_login").text('Register').click(function () {
                    eventBus.post(Events.USER_NOT_REGISTERED, {});
                })))

            _onInputFieldEvent('input');
        };

        var _onLoginFailed = function (message) {
            _loginFailed(message);
        }

        var _onUserLoggedIn = function () {
            $('#' + rootDivId + '_login').remove();
            $('#' + rootDivId + '_to_registration').remove();
        }

        var _loginFailed = function (message) {
            $('#' + _elementDivId + '_box_err').html($('<span/>').text(message));
        };

        return {
            'initialize': _initialize,
            'onUserLoggedIn': _onUserLoggedIn
        };
    };

    var DashboardComponent = function (_elementDivId) {

        var _initialize = function (userInfo) {

            eventBus.subscribe(Events.TASK_CREATED, _onTaskCreated);
            eventBus.subscribe(Events.TASK_CREATION_FAILED, _onTaskCreationFailed);

            $('#' + rootDivId).append($('<div/>').attr('id', _elementDivId));
            $('#' + _elementDivId).append($('<div/>').attr('id', _elementDivId + '_header'));

            $('#' + _elementDivId + '_header')
                .append($('<h5/>').html('Welcome to TodoList App!)'))
                .append($('<textarea/>').attr({
                    'id': _elementDivId + '_description',
                    'name': 'task_description',
                    'placeholder': 'Enter task description...'
                }))
                .append($('<button/>').attr({
                    'id': _elementDivId + '_add_task',
                    'class': 'add_task'
                }).text('Add new task').click(function () {
                    var taskInfo = {
                        'description': $('#' + _elementDivId + '_description').val(),
                        'tokenId': localStorage.getItem('tokenId')
                    };
                    eventBus.post(Events.NEW_TASK_ADDITION, taskInfo);
                }))
                .append($('<div/>').attr('id', _elementDivId + '_box_err'));

            $('#' + _elementDivId).append($('<div/>').attr('id', _elementDivId + '_list'));

            $('#' + _elementDivId + '_description').focus();

            _updateTaskList(userInfo.userTasks);

            _onInputFieldEvent('#' + _elementDivId + '_description');
        };

        var _updateTaskList = function (userTasks) {

            if (userTasks.length > 0) {
                $('#' + _elementDivId + '_list').html('').append($('<ul/>').attr({
                    'id': '_todo',
                    'class': 'todo-list'
                }));
                
                for (var i = 0; i < userTasks.length; i++) {
                    $('#_todo').append($('<li/>').attr({
                        'id': userTasks[i].userId
                    }).append($('<span/>').append($('<input/>').attr({
                        'type': 'checkbox'
                    }))).append($('<span/>').attr({
                        'class': 'taskinfo'
                    }).text(userTasks[i].description))
                        .append($('<span/>').attr({
                            'class': 'taskdate'
                        }).text("Created: " + userTasks[i].creationDate))
                        .append($('<span/>').text('x').css({
                            'float': 'right',
                            'color': 'black',
                            'cursor': 'pointer',
                            'title': 'Leave chat'
                        }).click(function () {
                        })));
                }
            } else {
                $('#' + _elementDivId + '_list').append($('<h5/>').html('No tasks yet)'));
            }
        };

        var _onTaskCreated = function (taskInfo) {
            $('#' + _elementDivId + '_box_err').html('');
            _updateTaskList(taskInfo.userTasks);
        };

        var _onTaskCreationFailed = function (message) {
            _taskCreationFailed(message);
        };

        var _taskCreationFailed = function (message) {
            $('#' + _elementDivId + '_box_err').html($('<span/>').text(message));
        };

        return {
            'initialize': _initialize
        };
    }

    return {'initialize': _initialize};
}

if (typeof define !== 'function') {
    var define = require('amdefine')(module);
}

define(function () {
    return TodoList;
});