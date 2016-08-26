import React from 'react';

class MessageComponent extends React.Component {

    render() {
        return (
            <div id={this.props.id}>{this.props.message}</div>
        );
    }

}

export default MessageComponent;