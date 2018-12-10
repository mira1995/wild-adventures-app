import React, { Component } from 'react'
import { Button } from 'antd'
import { strings } from '../../helpers/strings'

class RemoveSessionButton extends Component {
  onClick = event => {
    this.props.actionOnClick(this.props.record)
  }

  render() {
    return (
      <Button type="danger" onClick={this.onClick}>
        {strings.orders.remove}
      </Button>
    )
  }
}

export default RemoveSessionButton
