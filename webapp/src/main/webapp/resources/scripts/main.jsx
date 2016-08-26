import React from 'react';
import ReactDOM from 'react-dom';
import RegistrationComponent from './component/registration';
import LoginComponent from './component/login';
import Events from './events';
import eventBus from './eventbus';
import userService from './service/clouduserservice'

class Main {
    constructor() {
        eventBus.subscribe(Events.NEW_USER_ADDITION, userService.addUser);
        eventBus.subscribe(Events.LOGIN_ATTEMPT, userService.loginUser);
        eventBus.subscribe(Events.USER_LOGOUT, userService.logoutUser);
        eventBus.subscribe(Events.USER_ALREADY_REGISTERED, this.renderLoginComponent);
        eventBus.subscribe(Events.USER_LOGGED_OUT, this.renderRegistrationComponent);
        eventBus.subscribe(Events.USER_NOT_REGISTERED, this.renderRegistrationComponent);
        eventBus.subscribe(Events.LOGIN_SUCCESSFULL, this.renderRegistrationComponent);
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
}

let main = new Main();
main.renderRegistrationComponent();