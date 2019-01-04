import React, { Component } from 'react'
import { InputNumber, Button } from 'antd'
import { strings } from '../../helpers/strings'

class OrderItem extends Component {
  onChange = value => {
    value = isNaN(value) ? 0 : value
    const { index } = this.props
    this.props.action(index, value)
  }

  onClickDeleteFromBuyingBox = () => {
    this.props.actionDelete(this.props.item)
  }

  render() {
    const { title, nbOrder, index, price } = this.props
    return (
      <div className={`orderItem${index}`}>
        <h2>{title}</h2>
        {strings.orders.peopleAdventure} :{' '}
        <InputNumber
          min={1}
          defaultValue={nbOrder ? nbOrder : 1}
          onChange={this.onChange}
        />
        <Button type="danger" onClick={this.onClickDeleteFromBuyingBox}>
          Supprimer
        </Button>
        <h3>
          {strings.orders.totalSession} : {nbOrder ? nbOrder * price : price}
        </h3>
      </div>
    )
  }
}

export default OrderItem
