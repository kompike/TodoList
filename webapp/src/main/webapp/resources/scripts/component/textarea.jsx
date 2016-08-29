import React from 'react';

class TextareaComponent extends React.Component {

    render() {
        return (
            <textarea id={this.props.id} placeholder={this.props.placeholder}/>
        );
    }

}

export default TextareaComponent;