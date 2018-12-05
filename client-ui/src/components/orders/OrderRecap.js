import React, { Component } from 'react'
import { http } from './../../configurations/axiosConf'
import { API } from '../../helpers/constants'
import { Table } from 'antd'
import OrderItem from './OrderItem'

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

  componentWillMount() {
    let { total } = this.state
    console.log(this.props.orderSessions)
    let orderSessions = this.props.orderSessions
    let { adventureTitles } = this.state
    let { subTotals } = this.state
    orderSessions.map(
      orderSession =>
        (orderSession = this.getDatasSessionandAdventure(
          orderSession,
          total,
          adventureTitles,
          subTotals
        ))
    )
    this.setState({
      orderSessions: orderSessions,
      total: total,
      adventureTitles: adventureTitles,
      subTotals: subTotals,
    })
  }

  getDatasSessionandAdventure = (
    orderSession,
    total,
    adventureTitles,
    subTotals
  ) => {
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
            total += orderSession.subTotal
          })
          .catch(error => console.log('error', error))
      })
      .catch(error => console.log('error', error))
  }

  render() {
    const columns = [
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
    console.log(this.props)

    const { orderSessions } = this.props
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
