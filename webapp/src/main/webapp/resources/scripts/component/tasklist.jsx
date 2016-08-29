import React from 'react';
import Task from './task';

class TaskListComponent extends React.Component {

    render() {
        return (
            <ul className={this.props.className}>
                {this.props.tasks.map((task) =>
                    <Task key={task.taskId} data={task}
                          updateTask={this.props.updateTask}
                          removeTask={this.props.removeTask}/>)}
            </ul>
        );
    }
}

export default TaskListComponent;