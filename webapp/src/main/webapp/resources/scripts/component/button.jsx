import React from 'react';

class Button extends React.Component {

    render() {
        return (
            <button id={this.props.buttonId} onClick={this.props.handleClick}>{this.props.buttonText}</button>
        );
    }

}

export default Button;