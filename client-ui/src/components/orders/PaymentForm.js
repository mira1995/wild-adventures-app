import React, { Component } from 'react'
import Payment from './Payment'
import { withRouter } from 'react-router-dom'
import { API } from '../../helpers/constants'
import { http } from '../../configurations/axiosConf'
import { message } from 'antd'
import { strings } from '../../helpers/strings'

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
      .then(() => message.warning(`We are in business.`))
      .catch(() => message.error(strings.statusCode.orderChargeError))
  }

  render() {
    return (
      <div>
        <Payment />
      </div>
    )
  }
}

export default withRouter(PaymentForm)
