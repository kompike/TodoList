import React from 'react';
import ReactDOM from 'react-dom';
import Events from '../events';
import eventBus from '../eventbus';
import TitleComponent from './title';
import InputFieldComponent from './input';
import MessageComponent from './message';
import Button from './button';

class LoginComponent extends React.Component {

    constructor() {
        super();
        this.loginFailed = this.loginFailed.bind(this);
        eventBus.subscribe(Events.LOGIN_FAILED, this.loginFailed);
        this.state = {message: ''};
    }

    static login() {
        var user = {
            'email': $('#_email').val(),
            'password': $('#_password').val()
        };
        eventBus.post(Events.LOGIN_ATTEMPT, user);
    }

    loginFailed(msg) {
        this.setState = {message: msg};
    }

    onNotRegistered() {
        eventBus.post(Events.USER_NOT_REGISTERED, {});
    }

    render() {

        return (
            <div>
                <div className='_register'>
                    <TitleComponent text="Login"/>
                    <InputFieldComponent for="email" text="Email" id="_email" type="text" name="email"/>
                    <InputFieldComponent for="password" text="Password" id="_password" type="password" name="password"/>
                    <MessageComponent id="_box_err" message={this.state.message}/>
                    <Button buttonId="_register_btn" buttonText="Login" handleClick={LoginComponent.login}/>
                </div>
                <div id='_registered'>
                    <Button buttonId="_register_btn_login" buttonText="Register" handleClick={this.onNotRegistered}/>
                </div>
            </div>
        );
    }
}

export default LoginComponent;