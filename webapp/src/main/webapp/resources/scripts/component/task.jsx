import React from 'react';

class TaskComponent extends React.Component {

    constructor(props) {
        super();
        this.onUpdate = this.onUpdate.bind(this);
        this.onDelete = this.onDelete.bind(this);
        this.onChange = this.onChange.bind(this);
        let status = props.data.status == 'true';
        this.state = {isChecked: status};
    }

    onChange() {
        this.setState({isChecked: !this.state.isChecked});
        this.onUpdate();
    }

    onUpdate() {
        var taskId = this.props.data.taskId;
        this.props.updateTask(taskId);
    }

    onDelete() {
        var taskId = this.props.data.taskId;
        this.props.removeTask(taskId);
    }

    render() {

        let className = (this.state.isChecked) ? 'todo-item-done' : 'todo-item-active';
        let textClassName = (this.state.isChecked) ? 'taskDescription-done' : 'taskDescription';

        return (
            <li id={this.props.data.taskId} className={className}>
                <input type="checkbox" checked={this.state.isChecked} onChange={this.onChange}/>
                <span className={textClassName}>{this.props.data.description}</span>
                <span className="taskDate">Created: {this.props.data.creationDate}</span>
                <span className="closeButton" onClick={this.onDelete}>x</span>
            </li>
        );
    }

}

export default TaskComponent;