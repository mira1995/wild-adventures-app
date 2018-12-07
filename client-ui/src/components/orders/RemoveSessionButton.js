import React, { Component } from 'react'
import { Button } from 'antd'

class RemoveSessionButton extends Component {
  onClick = event => {
    this.props.actionOnClick(this.props.record)
  }

  render() {
    return (
      <Button type="danger" onClick={this.onClick}>
        Supprimer
      </Button>
    )
  }
}

export default RemoveSessionButton
