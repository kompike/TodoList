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

    return {
        'onTaskAdded': _onTaskAdded
    };
};

if (typeof define !== 'function') {
    var define = require('amdefine')(module);
}

define(function () {
    return TaskService;
});