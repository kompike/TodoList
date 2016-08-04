var TaskService = function(eventBus, serverURL) {

    var _addTask = function(taskInfo) {
    };

    var _onTaskAdded = function(taskInfo) {
        return _addTask(taskInfo);
    };

    return {
        'onTaskAdded' : _onTaskAdded
    };
};

if (typeof define !== 'function') {
    var define = require('amdefine')(module);
}

define(function() {
    return TaskService;
});