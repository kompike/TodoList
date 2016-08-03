define(function(require) {

    var TodoList = require('./todolist');

    var EventBus = require('./eventbus');

    var Events = require('./events');

    var UserService = require('./service/clouduserservice');

    var eventBus = new EventBus();

    var baseURL = 'http://localhost:8080/';

    var todoList = new TodoList("todo", eventBus, new UserService(eventBus, baseURL));

    todoList.initialize();
});	