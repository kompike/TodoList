var TaskService = function (eventBus, serverURL) {

    var _addTask = function (taskInfo) {

        $.post(serverURL + "api/tasks",
            {
                description: taskInfo.description,
                tokenId: taskInfo.tokenId

            }, function (xhr) {

                var data = eval("(" + xhr + ")");
                eventBus.post(Events.TASK_CREATED, data);

            }, 'text')

            .fail(function (xhr) {

                var res = eval("(" + xhr.responseText + ")");
                eventBus.post(Events.TASK_CREATION_FAILED, res.errorMessage);
            });
    };

    var _completeTask = function (taskInfo) {

        $.post(serverURL + "api/tasks/complete",
            {
                taskId: taskInfo.taskId,
                tokenId: taskInfo.tokenId

            }, function (xhr) {

                var data = eval("(" + xhr + ")");
                eventBus.post(Events.TASK_COMPLETED, data.taskId);

            }, 'text');
    };

    var _reopenTask = function (taskInfo) {

        $.post(serverURL + "api/tasks/reopen",
            {
                taskId: taskInfo.taskId,
                tokenId: taskInfo.tokenId

            }, function (xhr) {

                var data = eval("(" + xhr + ")");
                eventBus.post(Events.TASK_REOPENED, data.taskId);

            }, 'text');
    };

    var _deleteTask = function (taskInfo) {

        $.post(serverURL + "api/tasks/delete",
            {
                taskId: taskInfo.taskId,
                tokenId: taskInfo.tokenId

            }, function (xhr) {

                var data = eval("(" + xhr + ")");
                eventBus.post(Events.TASK_DELETED, data.taskId);

            }, 'text');
    };

    var _onUserAlreadyLoggedIn = function (tokenId) {

        $.get(serverURL + "api/tasks",
            {
                tokenId: tokenId

            }, function (xhr) {

                var data = eval("(" + xhr + ")");
                eventBus.post(Events.LOGIN_SUCCESSFULL, data);

            }, 'text');
    };

    var _onTaskAdded = function (taskInfo) {
        _addTask(taskInfo);
    };

    var _onTaskCompleted = function (taskInfo) {

        _completeTask(taskInfo);
    };

    var _onTaskReopened = function (taskInfo) {

        _reopenTask(taskInfo);
    };

    var _onTaskDeleted = function (taskInfo) {

        _deleteTask(taskInfo);
    };

    return {
        'onTaskAdded': _onTaskAdded,
        'onTaskCompleted': _onTaskCompleted,
        'onTaskReopened': _onTaskReopened,
        'onTaskDeleted': _onTaskDeleted,
        'onUserAlreadyLoggedIn': _onUserAlreadyLoggedIn
    };
};

if (typeof define !== 'function') {
    var define = require('amdefine')(module);
}

define(function () {
    return TaskService;
});