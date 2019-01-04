import React, { Component } from 'react'
import Container from './../../Container'
import { connect } from 'react-redux'
import OrderItem from './OrderItem'
import { Row, Button, Col, message } from 'antd'
import moment from 'moment'
import jwt from 'jsonwebtoken'
import { http } from '../../configurations/axiosConf'
import { API, URI } from '../../helpers/constants'
import { Redirect } from 'react-router-dom'
import { strings } from '../../helpers/strings'

class OrderForm extends Component {
  constructor(props) {
    super(props)
    this.state = {
      total: 0,
      userAccount: null,
      order: null,
    }
  }

  componentWillMount() {
    const token = this.props.token.substring(7)
    const decoded = jwt.decode(token)
    http
      .post(`${API.USERS}/email`, decoded.sub)
      .then(response => {
        const { password, ...userAccount } = response.data
        this.setState({ userAccount })
      })
      .catch(() => message.error(strings.statusCode.userInformations))
    this.getTotal()
  }

  getTotal() {
    const { buyingBox } = this.props.buyingBox
    let total = 0
    buyingBox.map(item => (total = this.addToTotalIfNbOrderExist(item, total)))
    this.setState({ total })
  }

  addToTotalIfNbOrderExist = (item, total) => {
    if (item.nbOrder) total += item.nbOrder * item.price
    else total += item.price
    console.log(total)
    return total
  }

  onItemNbChange = (index, value) => {
    const action = {
      type: 'ADD_NB_ORDER',
      value: { index, value },
    }
    this.props.dispatch(action)
    this.getTotal()
  }

  onDeleteItem = item => {
    const action = { type: 'TOGGLE_BUYINGBOX', value: item }
    this.props.dispatch(action)
  }

  continueOrder = () => {
    let order = {
      orderDate: moment(),
      status: 'NOT_PAID',
      isPaid: false,
      userAccountId: this.state.userAccount.id,
      orderSessions: [],
    }
    const { buyingBox } = this.props.buyingBox
    console.log(buyingBox)
    buyingBox.map(item =>
      order.orderSessions.push({
        sessionId: item.id,
        nbOrder: item.nbOrder ? item.nbOrder : 1,
      })
    )

    http
      .post(API.ORDERS, order)
      .then(response => {
        order = response.data
        this.setState({ order })
        this.clearBuyingBox()
      })
      .catch(() => message.error(strings.statusCode.creatingOrderError))
  }

  clearBuyingBox = () => {
    const action = { type: 'CLEAR_BUYINGBOX', value: null }
    this.props.dispatch(action)
  }

  render() {
    const { buyingBox } = this.props.buyingBox
    const { order, total } = this.state

    if (order) return <Redirect to={`${URI.PAYMENT}/${order.id}`} />

    return (
      <Container>
        <h1>{strings.orders.makeOrder}</h1>
        {buyingBox.map((item, index) => (
          <OrderItem
            key={index}
            title={`${item.adventureName} ${strings.orders.from} ${
              item.startDate
            } ${strings.orders.to} ${item.endDate}`}
            nbOrder={item.nbOrder}
            index={index}
            item={item}
            price={item.price}
            action={this.onItemNbChange}
            actionDelete={this.onDeleteItem}
          />
        ))}
        {buyingBox.length > 0 && (
          <Row>
            <Col offest={6} span={12}>
              <h2>
                {strings.orders.total} : {total}€
              </h2>

              <Button type="primary" onClick={this.continueOrder}>
                {strings.orders.continueOrder}
              </Button>
            </Col>
          </Row>
        )}
        {!buyingBox.length > 0 && <p>{strings.orders.noSessionsCart}</p>}
      </Container>
    )
  }
}

const mapStateToProps = state => {
  return {
    buyingBox: state.buyingBox,
    token: state.authentication.token,
  }
}

export default connect(mapStateToProps)(OrderForm)
