import React from 'react';
import Events from '../events';
import eventBus from '../eventbus';
import TitleComponent from './title';
import InputFieldComponent from './input';
import MessageComponent from './message';
import Button from './button';

class RegistrationComponent extends React.Component {

    constructor() {
        super();
        this.registrationFailed = this.registrationFailed.bind(this);
        this.registrationSucceeded = this.registrationSucceeded.bind(this);
        eventBus.subscribe(Events.REGISTRATION_FAILED, this.registrationFailed);
        eventBus.subscribe(Events.USER_REGISTERED, this.registrationSucceeded);
        this.state = {message: '', messageBoxId: '_box_err'};
    }

    static register() {
        let user = {
            'email': $('#_email').val(),
            'password': $('#_password').val(),
            'confirmPassword': $('#_confirm_password').val()
        };
        eventBus.post(Events.NEW_USER_ADDITION, user);
    }

    static onUserAlreadyRegistered() {
        eventBus.post(Events.USER_ALREADY_REGISTERED, {});
    }

    registrationSucceeded(msg) {
        this.setState({message: msg, messageBoxId: '_box_success'});
    }

    registrationFailed(msg) {
        this.setState({message: msg, messageBoxId: '_box_err'});
    }

    render() {

        return (
            <div>
                <div className='_register'>
                    <TitleComponent text="Registration"/>
                    <InputFieldComponent ref="email" for="email" text="Email" id="_email" type="text" name="email"/>
                    <InputFieldComponent for="password" text="Password" id="_password" type="password" name="password"/>
                    <InputFieldComponent for="confirm_password" text="Confirm password" id="_confirm_password"
                                         type="password" name="confirm_password"/>
                    <MessageComponent id={this.state.messageBoxId} message={this.state.message}/>
                    <Button buttonId="_register_btn" buttonText="Register"
                            handleClick={RegistrationComponent.register}/>
                </div>
                <div className='_to_login'>
                    <Button buttonId="_to_login" buttonText="Login"
                            handleClick={RegistrationComponent.onUserAlreadyRegistered}/>
                </div>
            </div>
        );
    }
}

export default RegistrationComponent;