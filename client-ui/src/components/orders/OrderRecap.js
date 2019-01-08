import React, { Component } from 'react'
import { message, Table } from 'antd'
import RemoveSessionButton from './RemoveSessionButton'
import NbOrderUpdateInput from './NbOrderUpdateInput'

class OrderRecap extends Component {
  constructor(props) {
    super(props)
    this.state = {
      orderSessions: this.props.orderSessions,
      adventureTitles: [],
      subTotals: [],
      total: 0,
    }
  }

  actionOnChange = record => {
    const { orderSessions } = this.state
    const sessionIndex = orderSessions.findIndex(
      item =>
        item.sessionId === record.sessionId && item.orderId === record.orderId
    )
    console.log(sessionIndex)
    orderSessions[sessionIndex].nbOrder = record.nbOrder
    this.props.action(orderSessions)
  }

  actionOnClick = record => {
    console.log(
      this.state.orderSessions.filter(
        (item, index) =>
          item.sessionId === record.sessionId && item.orderId === record.orderId
      )
    )
    if (this.props.orderSessions.length > 1) {
      this.props.action(
        this.state.orderSessions.filter(
          (item, index) =>
            item.sessionId === record.sessionId &&
            item.orderId === record.orderId
        )
      )
    } else {
      message.error('Votre commande doit au moins contenir une aventure')
    }
  }

  _toggleOrderSessions(record) {
    const action = { type: 'UPDATE_ORDERSESSIONS', value: record }
    this.props.dispatch(action)
  }

  render() {
    const columns = !this.props.updateComponent
      ? [
          {
            title: 'Aventure',
            dataIndex: 'adventureTitle',
            key: 'adventureTitle',
          },
          {
            title: 'Total en euros',
            dataIndex: 'subTotal',
            rowKey: 'subTotal',
          },
        ]
      : [
          {
            title: 'Aventure',
            dataIndex: 'adventureTitle',
            key: 'adventureTitle',
          },
          {
            title: 'Nombre de participant',
            key: 'nbOrder',
            render: (text, record) => (
              <div>
                <NbOrderUpdateInput
                  record={record}
                  actionOnChange={this.actionOnChange}
                />
              </div>
            ),
          },
          {
            title: 'Total en euros',
            dataIndex: 'subTotal',
            rowKey: 'subTotal',
          },
          {
            title: 'Suppression',
            key: 'delete',
            render: (text, record) => (
              <div>
                <RemoveSessionButton
                  actionOnClick={this.actionOnClick}
                  record={record}
                />
              </div>
            ),
          },
        ]
    console.log(this.props)

    let { orderSessions } = this.props
    console.log(orderSessions)
    if (orderSessions) {
      console.log(orderSessions)
      orderSessions.map((orderSession, index) =>
        this.props.formalizeSession(orderSession, index)
      )
    }
    console.log(orderSessions)
    return (
      <div className="orderRecap">
        {orderSessions &&
          orderSessions.length > 0 && (
            <Table
              columns={columns}
              dataSource={orderSessions}
              rowKey="sessionId"
            />
          )}
      </div>
    )
  }
}

export default OrderRecap
