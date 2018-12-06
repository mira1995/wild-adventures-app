import React, { Component } from 'react'
import { http } from './../../configurations/axiosConf'
import { API } from '../../helpers/constants'
import { Table, InputNumber, Button } from 'antd'

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

  onChange = value => {
    this.props.action(this.state.orderSessions)
  }

  onClick = event => {
    this.props.action(this.state.orderSessions)
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
                <InputNumber
                  min={1}
                  defaultValue={record.nbOrder ? record.nbOrder : 1}
                  onChange={this.onChange}
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
            title: 'Nombre de participant',
            key: 'nbOrder',
            render: (text, record) => (
              <div>
                <Button type="danger" onClick={this.onClick} />
              </div>
            ),
          },
        ]
    console.log(this.state)

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
