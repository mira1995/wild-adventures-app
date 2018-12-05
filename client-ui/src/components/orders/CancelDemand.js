import React, { Component } from 'react'
import { withRouter } from 'react-router-dom'
import Container from '../../Container'
import { Input, Form, Button } from 'antd'
import { http } from './../../configurations/axiosConf'
import { API } from '../../helpers/constants'
import { connect } from 'react-redux'
import jwt from 'jsonwebtoken'
import OrderRecap from './OrderRecap'

class CancelDemand extends Component {
  constructor(props) {
    super(props)
    this.state = {
      isWrongUser: false,
    }
  }
  componentWillMount() {
    const token = this.props.token.substring(7)
    const decoded = jwt.decode(token)
    let userAccount = {}
    http
      .post(`${API.USERS}/email`, decoded.sub)
      .then(response => {
        userAccount = response.data
      })
      .catch(error => console.log('error', error))
    http.get(`${API.ORDERS}/${this.props.match.params.orderId}`).then(res => {
      let order = res.data
      console.log(order)
      this.setState({
        userAccount: userAccount,
        orderItem: order,
        isWrongUser: order.userAccountId !== userAccount.id ? true : false,
      })
    })
  }

  initiateState = (order, userAccount) => {}

  persistDemand = event => {
    event.preventDefault()
  }

  render() {
    const orderId = this.props.match.params.orderId
    const FormItem = Form.Item
    const { getFieldDecorator } = this.props.form
    const { orderItem } = this.state
    console.log(this.state)
    return (
      <Container>
        <h1>Demande d'annulation de commande</h1>
        <h2>Annulation de la commande du {orderItem && orderItem.orderDate}</h2>
        {orderItem &&
          orderItem.orderSessions && (
            <OrderRecap orderSessions={orderItem.orderSessions} />
          )}
        <Form
          id={orderId}
          className={`orderDemandForm`}
          onSubmit={this.persistComment}
        >
          <FormItem>
            {getFieldDecorator('content', {
              rules: [
                {
                  required: true,
                  message:
                    'Merci de rentrer un message pour les administrateurs',
                },
              ],
            })(
              <Input
                rows={4}
                placeholder="Ecrivez votre message pour les administrateurs"
              />
            )}
            <Button
              type="danger"
              htmlType="submit"
              className="login-form-button"
            >
              Annuler ma commande
            </Button>
          </FormItem>
        </Form>
      </Container>
    )
  }
}

const WrappedCancelDemandForm = Form.create()(CancelDemand)

const mapStateToProps = state => {
  return {
    token: state.authentication.token,
  }
}

export default connect(mapStateToProps)(withRouter(WrappedCancelDemandForm))
