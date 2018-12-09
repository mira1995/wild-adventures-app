import React, { Component } from 'react'
import { withRouter, Redirect } from 'react-router-dom'
import Container from '../../Container'
import { Input, Form, Button } from 'antd'
import { http } from './../../configurations/axiosConf'
import { API, URI, ORDERSTATUS } from '../../helpers/constants'
import { connect } from 'react-redux'
import jwt from 'jsonwebtoken'
import OrderRecap from './OrderRecap'
import { DEMANDSTATUS } from './../../helpers/constants'

class CancelDemand extends Component {
  constructor(props) {
    super(props)
    this.state = {
      isWrongUser: false,
      isSavedDemand: false,
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

  initiateState = orderSessions => {
    const { orderItem } = this.state
    orderItem.orderSessions = orderSessions
    this.setState({ orderItem })
  }

  persistDemand = event => {
    event.preventDefault()
    this.props.form.validateFields((error, values) => {
      if (!error) {
        const { orderItem } = this.state
        let orderDemandSessions = []
        orderItem.orderSessions.map(orderSession =>
          orderDemandSessions.push({
            nbOrder: orderSession.nbOrder,
            sessionId: orderSession.sessionId,
          })
        )
        const demand = {
          orderDate: orderItem.orderDate,
          status: orderItem.status,
          isPaid: orderItem.isPaid,
          userAccountId: orderItem.userAccountId,
          order: { ...orderItem, status: ORDERSTATUS.DELETE_DEMAND },
          demandStatus: DEMANDSTATUS.OPENED_DEMAND,
          orderDemandSessions: orderDemandSessions,
          demandMessage: values.message,
        }

        http
          .post(`${API.ORDERS}${API.DEMANDS}`, demand)
          .then(response => {
            this.setState({ isSavedDemand: true })
          })
          .catch(error => console.log('error', error))
      }
    })
  }

  render() {
    if (this.state.isSavedDemand) {
      return <Redirect to={URI.MYORDERS} />
    }
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
            <OrderRecap
              action={this.initiateState}
              orderSessions={orderItem.orderSessions}
            />
          )}
        <Form
          id={orderId}
          className={`orderDemandForm`}
          onSubmit={this.persistDemand}
        >
          <FormItem>
            {getFieldDecorator('message', {
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
    languageCode: state.language.code,
  }
}

export default connect(mapStateToProps)(withRouter(WrappedCancelDemandForm))
