var Events = {

    NEW_USER_ADDITION : 'NEW_USER_ADDITION',
    REGISTRATION_FAILED : 'REGISTRATION_FAILED',
    USER_REGISTERED : 'USER_SUCCESSFULLY_REGISTERED',
    USER_ALREADY_REGISTERED : 'USER_ALREADY_REGISTERED',
    USER_NOT_REGISTERED : 'USER_NOT_REGISTERED',
    LOGIN_ATTEMPT : 'LOGIN',
    LOGIN_FAILED : 'LOGIN_FAILED',
    LOGIN_SUCCESSFULL : 'USER_LOGGED_IN'
}

if (typeof define !== 'function') {
    var define = require('amdefine')(module);
}

define(function() {
    return Events;
});