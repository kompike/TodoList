define(function(require) {

    var TodoList = require('./todolist');

    var EventBus = require('./eventbus');

    var Events = require('./events');

    var UserService = require('./service/clouduserservice');

    var TaskService = require('./service/cloudtaskservice');

    var eventBus = new EventBus();

    var baseURL = 'http://localhost:8080/';

    var todoList = new TodoList("todo", eventBus, new UserService(eventBus, baseURL), new TaskService(eventBus, baseURL));

    todoList.initialize();
});	