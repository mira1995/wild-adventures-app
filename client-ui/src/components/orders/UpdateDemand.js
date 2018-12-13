import React, { Component } from 'react'
import { withRouter, Redirect } from 'react-router-dom'
import Container from '../../Container'
import { Input, Form, Button, message } from 'antd'
import { http } from './../../configurations/axiosConf'
import { API, URI, ORDERSTATUS } from '../../helpers/constants'
import { connect } from 'react-redux'
import jwt from 'jsonwebtoken'
import OrderRecap from './OrderRecap'
import { DEMANDSTATUS } from './../../helpers/constants'
import { strings } from '../../helpers/strings'

class UpdateDemand extends Component {
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
      .then(response => (userAccount = response.data))
      .catch(() => message.error(strings.statusCode.userInformations))
    http
      .get(`${API.ORDERS}/${this.props.match.params.orderId}`)
      .then(response => {
        let order = response.data
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
          order: { ...orderItem, status: ORDERSTATUS.UPDATE_DEMAND },
          demandStatus: DEMANDSTATUS.OPENED_DEMAND,
          orderDemandSessions: orderDemandSessions,
          demandMessage: values.message,
        }

        http
          .post(`${API.ORDERS}${API.DEMANDS}`, demand)
          .then(() => this.setState({ isSavedDemand: true }))
          .catch(() => message.error(strings.statusCode.creatingDemandError))
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

    return (
      <Container>
        <h1>{strings.orders.orderUpdateRequest}</h1>
        <h2>
          {strings.orders.orderCancellationDate}{' '}
          {orderItem && orderItem.orderDate}
        </h2>
        {orderItem &&
          orderItem.orderSessions && (
            <OrderRecap
              action={this.initiateState}
              orderSessions={orderItem.orderSessions}
              updateComponent={true}
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
                  message: strings.orders.form.messageMessageRule,
                },
              ],
            })(
              <Input
                rows={4}
                placeholder={strings.orders.form.messagePlaceholder}
              />
            )}
            <Button
              type="primary"
              htmlType="submit"
              className="login-form-button"
            >
              {strings.orders.orderModify}
            </Button>
          </FormItem>
        </Form>
      </Container>
    )
  }
}

const WrappedUpdateDemandForm = Form.create()(UpdateDemand)

const mapStateToProps = state => {
  return {
    token: state.authentication.token,
    languageCode: state.language.code,
  }
}

export default connect(mapStateToProps)(withRouter(WrappedUpdateDemandForm))
