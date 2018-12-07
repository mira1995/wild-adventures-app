import React, { Component } from 'react'
import { http } from './../../configurations/axiosConf'
import { API } from '../../helpers/constants'
import { message, Table } from 'antd'
import RemoveSessionButton from './RemoveSessionButton'
import NbOrderUpdateInput from './NbOrderUpdateInput'

class OrderRecap extends Component {
  constructor(props) {
    super(props)
    this.state = {
      orderSessions: null,
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

  static getDerivedStateFromProps(nextProps, prevState) {
    console.log(nextProps.orderSessions)
    let orderSessions = nextProps.orderSessions
    let { adventureTitles } = prevState
    let { subTotals } = prevState
    orderSessions.map(orderSession => {
      let session = null
      let adventure = null

      http
        .get(`${API.ADVENTURES}/sessions/single/${orderSession.sessionId}`)
        .then(response => {
          session = response.data
          http
            .get(`${API.ADVENTURES}/${session.adventureId}`)
            .then(response => {
              adventure = response.data
              orderSession.adventureTitle = `${adventure.title} du ${
                session.startDate
              } au ${session.endDate}`
              adventureTitles.push(orderSession.adventureTitle)
              orderSession.subTotal = session.price * orderSession.nbOrder
              subTotals.push(orderSession.subTotal)
              /* nextProps.total += orderSession.subTotal */
            })
            .catch(error => console.log('error', error))
        })
        .catch(error => console.log('error', error))
      return orderSession
    })
    /* nextProps.action(nextProps.orderSessions) */
    return nextProps
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
            title: 'Nombre de participant',
            dataIndex: 'nbOrder',
            key: 'nbOrder',
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

    const { orderSessions } = this.state
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
