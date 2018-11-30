import React, { Component } from 'react'
import { Elements, StripeProvider } from 'react-stripe-elements'
import Payment from './Payment'
import { withRouter } from 'react-router-dom'
import StripeCheckout from 'react-stripe-checkout'
import { API } from '../../helpers/constants'
import { http } from '../../configurations/axiosConf'

class PaymentForm extends Component {
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
  }

  render() {
    return (
      <div>
        <StripeProvider apiKey="pk_test_iIBoWaypautkR3Zr9nZNgI9H">
          <Elements>
            <Payment />
          </Elements>
        </StripeProvider>

        <StripeCheckout
          token={this.onToken}
          currency="EUR"
          locale="fr"
          amount={1000}
          email="info@wild-adventures.com"
          name="Wild Adventures CIE"
          stripeKey="pk_test_iIBoWaypautkR3Zr9nZNgI9H"
        />
      </div>
    )
  }
}

export default withRouter(PaymentForm)
