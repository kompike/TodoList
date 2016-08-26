import React from 'react';

class InputFieldComponent extends React.Component {

    render() {
        return (
            <div>
                <label htmlFor={this.props.for}>{this.props.text}</label>
                <input id={this.props.id} type={this.props.type} name={this.props.name}/>
                <br/>
            </div>
        );
    }

}

export default InputFieldComponent;