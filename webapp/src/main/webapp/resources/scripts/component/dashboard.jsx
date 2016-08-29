import React from 'react';
import Events from '../events';
import eventBus from '../eventbus';
import TitleComponent from './title';
import TextareaComponent from './textarea';
import MessageComponent from './message';
import Button from './button';
import TaskList from './tasklist'

class DashboardComponent extends React.Component {

    constructor() {
        super();
        this.getUserTasks = this.getUserTasks.bind(this);
        this.onTaskCreated = this.onTaskCreated.bind(this);
        this.onTaskCreationFailed = this.onTaskCreationFailed.bind(this);
        this.onTaskUpdated = this.onTaskUpdated.bind(this);
        this.onTaskRemoved = this.onTaskRemoved.bind(this);
        eventBus.subscribe(Events.TASK_CREATED, this.onTaskCreated);
        eventBus.subscribe(Events.TASK_CREATION_FAILED, this.onTaskCreationFailed);
        eventBus.subscribe(Events.TASK_UPDATED, this.onTaskUpdated);
        eventBus.subscribe(Events.TASK_DELETED, this.onTaskRemoved);
        eventBus.subscribe(Events.LOGIN_SUCCESSFULL, this.getUserTasks);
        this.state = {message: '', tasks: []};
    }

    static onTaskCreation() {
        let taskInfo = {
            'description': $('#task_description').val(),
            'tokenId': localStorage.getItem('tokenId')
        };
        eventBus.post(Events.NEW_TASK_ADDITION, taskInfo);
    }

    onTaskCreated(taskInfo) {
        $('#task_description').val('');
        this.setState({tasks: taskInfo.userTasks});
    }

    onTaskCreationFailed(msg) {
        this.setState({message: msg});
    }

    onTaskUpdated(data) {
        this.setState({tasks: data.userTasks});
    }

    onTaskRemoved(data) {
        this.setState({tasks: data.userTasks});
    }

    static onLogout() {
        eventBus.post(Events.USER_LOGOUT, {
            'tokenId': localStorage.getItem('tokenId')
        });
    }

    getUserTasks(userInfo) {
        this.setState({tasks: userInfo.userTasks});
    }

    updateTask(taskId) {
        var taskInfo = {
            'taskId': taskId,
            'tokenId': localStorage.getItem('tokenId')
        };
        eventBus.post(Events.TASK_UPDATING, taskInfo);
    }

    removeTask(taskId) {
        var taskInfo = {
            'taskId': taskId,
            'tokenId': localStorage.getItem('tokenId')
        };
        eventBus.post(Events.TASK_DELETING, taskInfo);
    }

    render() {

        return (
            <div className='_dashboard'>
                <div className='_dashboard_header'>
                    <TitleComponent text="Welcome to TodoList App!)"/>
                    <TextareaComponent id="task_description" placeholder="Enter task description..."/>
                    <Button buttonId="_add_task" buttonText="Add new task" handleClick={DashboardComponent.onTaskCreation}/>
                    <MessageComponent id="_box_err" message={this.state.message}/>
                </div>
                <div className="_dashboard_list">
                    <TaskList className="todo-list"
                              tasks={this.state.tasks}
                              updateTask={this.updateTask}
                              removeTask={this.removeTask}/>
                </div>
                <div className="_logout">
                    <Button buttonId="logout_btn" buttonText="Logout" handleClick={DashboardComponent.onLogout}/>
                </div>
            </div>
        );
    }
}

export default DashboardComponent;