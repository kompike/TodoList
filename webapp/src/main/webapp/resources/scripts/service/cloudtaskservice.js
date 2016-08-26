import Events from '../events';

class TaskService {

    constructor(eventBus, serverURL) {
        this.eventBus = eventBus;
        this.serverURL = serverURL;
    }

    addTask(taskInfo) {

        $.post(this.serverURL + "api/tasks",
            {
                description: taskInfo.description,
                tokenId: taskInfo.tokenId

            }, function (xhr) {

                var data = eval("(" + xhr + ")");
                this.eventBus.post(Events.TASK_CREATED, data);

            }, 'text')

            .fail(function (xhr) {

                var res = eval("(" + xhr.responseText + ")");
                this.eventBus.post(Events.TASK_CREATION_FAILED, res.errorMessage);
            });
    }

    updateTask(taskInfo) {

        $.ajax({
            url: this.serverURL + "api/tasks/?taskId=" + taskInfo.taskId + "&tokenId=" + taskInfo.tokenId,
            type: 'PUT',
            dataType: 'text',
            success: function (xhr) {

                var data = eval("(" + xhr + ")");
                this.eventBus.post(Events.TASK_UPDATED, data);

            }
        });
    }

    deleteTask(taskInfo) {

        $.ajax({
            url: this.serverURL + "api/tasks/?taskId=" + taskInfo.taskId + "&tokenId=" + taskInfo.tokenId,
            type: 'DELETE',
            dataType: 'text',
            success: function (xhr) {

                var data = eval("(" + xhr + ")");
                this.eventBus.post(Events.TASK_DELETED, data.taskId);

            }
        });
    };

    onUserAlreadyLoggedIn(tokenId) {

        $.get(this.serverURL + "api/tasks",
            {
                tokenId: tokenId.tokenId

            }, function (xhr) {

                var data = eval("(" + xhr + ")");
                this.eventBus.post(Events.LOGIN_SUCCESSFULL, data);

            }, 'text');
    };

    onTaskAdded(taskInfo) {
        this.addTask(taskInfo);
    };

    onTaskUpdated(taskInfo) {

        this.updateTask(taskInfo);
    };

    onTaskDeleted(taskInfo) {

        this.deleteTask(taskInfo);
    }
}

export default TaskService;