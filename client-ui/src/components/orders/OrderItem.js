import React, { Component } from 'react'
import { InputNumber } from 'antd'

class OrderItem extends Component {
  onChange = value => {
    value = isNaN(value) ? 0 : value
    const { index } = this.props
    this.props.action(index, value)
  }

  render() {
    const { title, nbOrder, index, price } = this.props
    return (
      <div className={`orderItem${index}`}>
        <h2>{title}</h2>
        Nombre de Personnes participants à l'aventure :{' '}
        <InputNumber
          min={1}
          defaultValue={nbOrder ? nbOrder : 1}
          onChange={this.onChange}
        />
        <h3>Total pour cette session : {nbOrder ? nbOrder * price : price}</h3>
      </div>
    )
  }
}

export default OrderItem
