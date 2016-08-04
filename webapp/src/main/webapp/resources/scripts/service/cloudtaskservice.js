var TaskService = function (eventBus, serverURL) {

    var _onTaskAdded = function (taskInfo) {

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

    var _onTaskCompleted = function (taskInfo) {

        $.post(serverURL + "api/tasks/complete",
            {
                taskId: taskInfo.taskId,
                tokenId: taskInfo.tokenId

            }, function (xhr) {

                var data = eval("(" + xhr + ")");
                eventBus.post(Events.TASK_COMPLETED, data.taskId);

            }, 'text');
    };

    var _onTaskReopened = function (taskInfo) {

        $.post(serverURL + "api/tasks/reopen",
            {
                taskId: taskInfo.taskId,
                tokenId: taskInfo.tokenId

            }, function (xhr) {

                var data = eval("(" + xhr + ")");
                eventBus.post(Events.TASK_REOPENED, data.taskId);

            }, 'text');
    };

    var _onTaskDeleted = function (taskInfo) {

        $.post(serverURL + "api/tasks/delete",
            {
                taskId: taskInfo.taskId,
                tokenId: taskInfo.tokenId

            }, function (xhr) {

                var data = eval("(" + xhr + ")");
                eventBus.post(Events.TASK_DELETED, data.taskId);

            }, 'text');
    };

    return {
        'onTaskAdded': _onTaskAdded,
        'onTaskCompleted': _onTaskCompleted,
        'onTaskReopened': _onTaskReopened,
        'onTaskDeleted': _onTaskDeleted
    };
};

if (typeof define !== 'function') {
    var define = require('amdefine')(module);
}

define(function () {
    return TaskService;
});