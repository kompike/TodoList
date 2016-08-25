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

    var _updateTask = function (taskInfo) {

        $.ajax({
            url: serverURL + "api/tasks/?taskId=" + taskInfo.taskId + "&tokenId=" + taskInfo.tokenId,
            type: 'PUT',
            dataType: 'text',
            success: function (xhr) {

                var data = eval("(" + xhr + ")");
                eventBus.post(Events.TASK_UPDATED, data);

            }
        });
    };

    var _deleteTask = function (taskInfo) {

        $.ajax({
            url: serverURL + "api/tasks/?taskId=" + taskInfo.taskId + "&tokenId=" + taskInfo.tokenId,
            type: 'DELETE',
            dataType: 'text',
            success: function (xhr) {

                var data = eval("(" + xhr + ")");
                eventBus.post(Events.TASK_DELETED, data.taskId);

            }
        });
    };

    var _onUserAlreadyLoggedIn = function (tokenId) {

        $.get(serverURL + "api/tasks",
            {
                tokenId: tokenId.tokenId

            }, function (xhr) {

                var data = eval("(" + xhr + ")");
                eventBus.post(Events.LOGIN_SUCCESSFULL, data);

            }, 'text');
    };

    var _onTaskAdded = function (taskInfo) {
        _addTask(taskInfo);
    };

    var _onTaskUpdated = function (taskInfo) {

        _updateTask(taskInfo);
    };

    var _onTaskDeleted = function (taskInfo) {

        _deleteTask(taskInfo);
    };

    return {
        'onTaskAdded': _onTaskAdded,
        'onTaskUpdated': _onTaskUpdated,
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