import React, { Component } from 'react'
import { http } from './../../configurations/axiosConf'
import { API } from '../../helpers/constants'
import Container from '../../Container'
import { Redirect, withRouter } from 'react-router-dom'
import { URI } from './../../helpers/constants'
import StripeCheckout from 'react-stripe-checkout'
import OrderRecap from './OrderRecap'
import { strings } from '../../helpers/strings'
import { message } from 'antd'
class Payment extends Component {
  constructor(props) {
    super(props)
    this.state = {
      order: null,
      adventures: [],
      sessions: [],
      items: [],
      total: 0,
    }
  }

  componentWillMount() {
    let sessionsId = []
    let total = 0
    http
      .get(`${API.ORDERS}/${this.props.match.params.orderId}`)
      .then(response => {
        const order = response.data
        order.orderSessions.map(session => sessionsId.push(session.sessionId))
        console.log(sessionsId)
        http
          .post(`${API.ADVENTURES}/sessions`, sessionsId)
          .then(response => {
            const sessions = response.data
            let adventures = []
            sessions.map(session =>
              http
                .get(`${API.ADVENTURES}/${session.adventureId}`)
                .then(response => {
                  adventures.push(response.data)
                  const orderSession = order.orderSessions.filter(
                    (item, index) => item.sessionId === session.id
                  )[0]
                  total = total + session.price * orderSession.nbOrder
                  console.log(total)
                  this.setState({ total: total })
                })
                .catch(() =>
                  message.error(strings.statusCode.gettingAdventureError)
                )
            )

            order.sessions = sessions
          })
          .catch(() => message.error(strings.statusCode.sessionUpdateError))
        this.setState({ order: order })
      })
      .catch(() => message.error(strings.statusCode.getOrderError))
  }

  payOrder = () => {
    http
      .patch(`${API.ORDERS}/pay/${this.props.match.params.orderId}`)
      .then(response => this.setState({ order: response.data }))
      .catch(() => message.error(strings.statusCode.paymentError))
  }

  initiateState = orderSessions => {
    const { order } = this.state
    order.orderSessions = orderSessions
    this.setState({ order })
  }

  onToken = token => {
    const { total } = this.state
    const chargeRequest = {
      stripeToken: token.id,
      amount: total * 100,
      currency: 'EUR',
      stripeEmail: 'test@gmail.com',
    }
    http
      .post(`${API.ORDERS}/charge`, chargeRequest)
      .then(() => message.warning(`We are in business.`))
      .catch(() => message.error(strings.statusCode.orderChargeError))
    this.payOrder()
  }

  calculateTotalOrder = order => {
    let total = 0
    order.orderSessions(item => (total += item.subTotal))
    return total
  }

  render() {
    if (this.state.order && this.state.order.isPaid) {
      return <Redirect to={URI.HOME} />
    }

    const { order, total } = this.state
    console.log(this.state)
    return (
      <Container>
        <h1>{strings.orders.orderSummary}</h1>
        <div>
          {order &&
            order.orderSessions && (
              <div>
                <OrderRecap
                  action={this.initiateState}
                  orderSessions={order.orderSessions}
                />
                <StripeCheckout
                  token={this.onToken}
                  currency="EUR"
                  locale="fr"
                  amount={total * 100}
                  email="info@wild-adventures.com"
                  name="Wild Adventures CIE"
                  stripeKey="pk_test_iIBoWaypautkR3Zr9nZNgI9H"
                />
              </div>
            )}
        </div>
      </Container>
    )
  }
}

export default withRouter(Payment)
