import Events from '../events';
import eventBus from '../eventbus';

class TaskService {

    constructor() {
    }

    addTask(taskInfo) {
        $.ajax({
            url: 'http://localhost:8080/api/tasks',
            method: 'post',
            dataType: 'json',
            data: {
                description: taskInfo.description,
                tokenId: taskInfo.tokenId
            },
            cache: false,
            success: function (data) {
                eventBus.post(Events.TASK_CREATED, data);
            },
            error: function (xhr) {
                let error = JSON.parse(xhr.responseText);
                eventBus.post(Events.TASK_CREATION_FAILED, error.errorMessage);
            }
        });
    }

    updateTask(taskInfo) {
        $.ajax({
            url: 'http://localhost:8080/api/tasks?taskId=' + taskInfo.taskId + '&tokenId=' + taskInfo.tokenId,
            method: 'put',
            dataType: 'json',
            cache: false,
            success: function (data) {
                eventBus.post(Events.TASK_UPDATED, data);
            }
        });
    }

    deleteTask(taskInfo) {
        $.ajax({
            url: 'http://localhost:8080/api/tasks?taskId=' + taskInfo.taskId + '&tokenId=' + taskInfo.tokenId,
            method: 'delete',
            dataType: 'json',
            cache: false,
            success: function (data) {
                eventBus.post(Events.TASK_DELETED, data);
            }
        });
    };

    onUserAlreadyLoggedIn(tokenId) {
        $.ajax({
            url: 'http://localhost:8080/api/tasks',
            method: 'get',
            dataType: 'json',
            data: {
                tokenId: tokenId.tokenId
            },
            cache: false,
            success: function (data) {
                eventBus.post(Events.LOGIN_SUCCESSFULL, data);
            },
            error: function () {
                eventBus.post(Events.USER_NOT_REGISTERED, {});
            }
        });
    };
}

let taskService = new TaskService();
export default taskService;