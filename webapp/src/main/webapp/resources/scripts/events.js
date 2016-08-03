var Events = {

    NEW_USER_ADDITION : 'NEW_USER_ADDITION',
    REGISTRATION_FAILED : 'REGISTRATION_FAILED',
    USER_REGISTERED : 'USER_SUCCESSFULLY_REGISTERED'
}

if (typeof define !== 'function') {
    var define = require('amdefine')(module);
}

define(function() {
    return Events;
});