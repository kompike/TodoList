import React from 'react';
import ReactDOM from 'react-dom';
import RegistrationComponent from './component/registration';
import LoginComponent from './component/login';
import DashboardComponent from './component/dashboard';
import Events from './events';
import eventBus from './eventbus';
import userService from './service/clouduserservice'
import taskService from './service/cloudtaskservice'

class Main {

    constructor() {
        eventBus.subscribe(Events.NEW_USER_ADDITION, userService.addUser);
        eventBus.subscribe(Events.LOGIN_ATTEMPT, userService.loginUser);
        eventBus.subscribe(Events.USER_LOGOUT, userService.logoutUser);
        eventBus.subscribe(Events.USER_ALREADY_REGISTERED, this.renderLoginComponent);
        eventBus.subscribe(Events.USER_LOGGED_OUT, this.renderRegistrationComponent);
        eventBus.subscribe(Events.USER_NOT_REGISTERED, this.renderRegistrationComponent);
        eventBus.subscribe(Events.LOGIN_SUCCESSFULL, this.renderDashboardComponent);
        eventBus.subscribe(Events.USER_ALREADY_LOGGED_IN, taskService.onUserAlreadyLoggedIn);
        eventBus.subscribe(Events.NEW_TASK_ADDITION, taskService.addTask);
        eventBus.subscribe(Events.TASK_UPDATING, taskService.updateTask);
        eventBus.subscribe(Events.TASK_DELETING, taskService.deleteTask);
    }

    renderRegistrationComponent() {
        ReactDOM.render(
            <RegistrationComponent/>,
            document.getElementById("container")
        );
    }

    renderLoginComponent() {
        ReactDOM.render(
            <LoginComponent/>,
            document.getElementById("container")
        );
    }

    renderDashboardComponent() {
        ReactDOM.render(
            <DashboardComponent/>,
            document.getElementById("container")
        );
    }
}

let main = new Main();
let tokenId = localStorage.getItem('tokenId');

if (tokenId !== null) {
    eventBus.post(Events.USER_ALREADY_LOGGED_IN, {'tokenId': tokenId});
} else {
    main.renderRegistrationComponent();
}