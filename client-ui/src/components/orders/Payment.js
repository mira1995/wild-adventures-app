import React, { Component } from 'react'
import { http } from './../../configurations/axiosConf'
import { API } from '../../helpers/constants'
import Container from '../../Container'
import { Redirect, withRouter } from 'react-router-dom'
import { URI } from './../../helpers/constants'
import StripeCheckout from 'react-stripe-checkout'

class Payment extends Component {
  constructor(props) {
    super(props)
    this.state = {
      order: null,
      adventures: [],
      sessions: [],
      items: [],
    }
  }

  componentWillMount() {
    let sessionsId = []
    http
      .get(`${API.ORDERS}/${this.props.match.params.orderId}`)
      .then(response => {
        let order = response.data
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
                })
                .catch(error => console.log('error', error))
            )

            order.sessions = sessions
          })
          .catch(error => console.log('error', error))
        this.setState({ order: order })
      })
      .catch(error => console.log('error', error))
  }

  payOrder = () => {
    http
      .patch(`${API.ORDERS}/pay/${this.props.match.params.orderId}`)
      .then(response => {
        this.setState({ order: response.data })
        console.log(this.state.order)
      })
      .catch(error => console.log('error', error))
  }

  onToken = token => {
    const chargeRequest = {
      stripeToken: token.id,
      amount: 10000,
      currency: 'EUR',
      stripeEmail: 'test@gmail.com',
    }
    http
      .post(`${API.ORDERS}/charge`, chargeRequest)
      .then(response => {
        alert(`We are in business,`)
      })
      .catch(error => console.log('error', error))
    this.payOrder()
  }

  render() {
    if (this.state.order && this.state.order.isPaid) {
      return <Redirect to={URI.HOME} />
    }
    const { order } = this.state
    console.log(this.state)
    return (
      <Container>
        <h1>Récapitulatif de commande</h1>
        <div>
          {order &&
            order.orderSessions.map((orderSession, index) => (
              <div key={index}>
                {order.adventures[index].title} du
                {order.sessions[index].startDate} au
                {order.session[index].endDate} pour un total de
                {orderSession.nbOrder * order.session[index][index].price}
              </div>
            ))}
        </div>

        <StripeCheckout
          token={this.onToken}
          currency="EUR"
          locale="fr"
          amount={1000}
          email="info@wild-adventures.com"
          name="Wild Adventures CIE"
          stripeKey="pk_test_iIBoWaypautkR3Zr9nZNgI9H"
        />
      </Container>
    )
  }
}

export default withRouter(Payment)
