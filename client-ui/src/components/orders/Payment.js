import React, { Component } from 'react'
import { http } from './../../configurations/axiosConf'
import { API } from '../../helpers/constants'
import Container from '../../Container'
import { Button } from 'antd/lib/radio'
import { Redirect, withRouter } from 'react-router-dom'
import { URI } from './../../helpers/constants'
import { CardElement, injectStripe } from 'react-stripe-elements'

class Payment extends Component {
  constructor(props) {
    super(props)
    this.submit = this.submit.bind(this)
    this.state = {
      order: null,
    }
  }

  componentWillMount = () => {
    http
      .get(`${API.ORDERS}/${this.props.match.params.orderId}`)
      .then(response => {
        this.setState({ order: response.data })
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

  async submit(ev) {
    // User clicked submit
    let { token } = await this.props.stripe.createToken({ name: 'Name' })
    let response = await fetch(`${API.ORDERS}/charge`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: {
        stripeToken: token.id,
        amount: 10000,
        currency: 'EUR',
        stripeEmail: 'test@gmail.com',
      },
    })

    if (response.ok) this.payOrder()
  }

  render() {
    if (this.state.order && this.state.order.isPaid) {
      return <Redirect to={URI.HOME} />
    }
    const { order } = this.state
    console.log(order)
    return (
      <Container>
        <CardElement />
        <Button onClick={this.payOrder}>Payer</Button>
      </Container>
    )
  }
}

export default injectStripe(withRouter(Payment))
