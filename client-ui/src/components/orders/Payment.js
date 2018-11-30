import React, { Component } from 'react'
import { http } from './../../configurations/axiosConf'
import { API } from '../../helpers/constants'
import Container from '../../Container'
import { Button } from 'antd/lib/radio'
import { Redirect } from 'react-router-dom'
import { URI } from './../../helpers/constants'

class Payment extends Component {
  constructor(props) {
    super(props)
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

  render() {
    if (this.state.order && this.state.order.isPaid) {
      return <Redirect to={URI.HOME} />
    }
    const { order } = this.state
    console.log(order)
    return (
      <Container>
        <Button onClick={this.payOrder}>Payer</Button>
      </Container>
    )
  }
}

export default Payment
