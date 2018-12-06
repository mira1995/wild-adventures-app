import React, { Component } from 'react'
import { InputNumber } from 'antd'

class NbOrderUpdateInput extends Component {
  onChange = value => {
    const { record } = this.props
    record.nbOrder = value
    this.props.actionOnChange(record)
  }

  render() {
    const { record } = this.props
    return (
      <InputNumber
        min={1}
        defaultValue={record.nbOrder ? record.nbOrder : 1}
        onChange={this.onChange}
      />
    )
  }
}

export default NbOrderUpdateInput
