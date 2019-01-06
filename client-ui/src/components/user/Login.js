import React, { Component } from 'react'
import { Link, Redirect } from 'react-router-dom'
import { connect } from 'react-redux'
import { Form, Icon, Input, Button, Checkbox, message, Row, Col } from 'antd'
import jwt from 'jsonwebtoken'
import moment from 'moment'
import './Login.css'
import { http } from '../../configurations/axiosConf'
import { TOGGLE_AUTH, TOGGLE_MENU } from '../../store/actions/types'
import { BEARER_TOKEN, URI, API } from '../../helpers/constants'
import { strings } from '../../helpers/strings'
import Container from '../../Container'

class Login extends Component {
  handleSubmit = event => {
    event.preventDefault()
    this.props.form.validateFields((error, values) => {
      if (!error) {
        const { remember, ...userAccount } = values
        http
          .post(API.AUTH, userAccount)
          .then(response => {
            const bearerToken = response.headers.authorization
            console.log(bearerToken)
            const { cookies } = this.props

            if (values.remember) {
              const decoded = jwt.decode(bearerToken.substring(7))
              const exp = moment.unix(decoded.exp)

              cookies.set(BEARER_TOKEN, bearerToken, {
                path: '/',
                expires: exp.toDate(),
              })
            } else {
              const exp = moment().add(15, 'minutes')
              cookies.set(BEARER_TOKEN, bearerToken, {
                path: '/',
                expires: exp.toDate(),
              })
            }

            this.toggleAction(TOGGLE_AUTH, bearerToken)
            this.toggleAction(TOGGLE_MENU, URI.HOME)
          })
          .catch(error => {
            if (error.response.status === 401)
              message.error(strings.statusCode.wrongCredentials)
            else message.error(strings.statusCode.serverNotFound)
            this.props.form.setFieldsValue({ password: null })
          })
      }
    })
  }

  handleMenu = () => {
    this.toggleAction(TOGGLE_MENU, URI.REGISTER)
  }

  toggleAction(type, value) {
    const action = { type, value }
    this.props.dispatch(action)
  }

  render() {
    if (this.props.token) return <Redirect to={URI.HOME} />

    const FormItem = Form.Item
    const { getFieldDecorator } = this.props.form

    return (
      <Row>
        <Col span={12} offset={6} type="flex" justify="center" align="center">
          <Form onSubmit={this.handleSubmit} className="login-form">
            <FormItem>
              {getFieldDecorator('username', {
                rules: [
                  {
                    type: 'email',
                    message: strings.user.form.usernameValidEmail,
                  },
                  {
                    required: true,
                    message: strings.user.form.usernameMessageRule,
                  },
                ],
              })(
                <Input
                  prefix={
                    <Icon type="mail" style={{ color: 'rgba(0,0,0,.25)' }} />
                  }
                  placeholder={strings.user.form.usernamePlaceholder}
                />
              )}
            </FormItem>
            <FormItem>
              {getFieldDecorator('password', {
                rules: [
                  {
                    required: true,
                    message: strings.user.form.passwordMessageRule,
                  },
                ],
              })(
                <Input
                  prefix={
                    <Icon type="lock" style={{ color: 'rgba(0,0,0,.25)' }} />
                  }
                  type="password"
                  placeholder={strings.user.form.passwordPlaceholder}
                />
              )}
            </FormItem>
            <FormItem>
              {getFieldDecorator('remember', {
                valuePropName: 'checked',
                initialValue: false,
              })(<Checkbox>{strings.user.rememberMe}</Checkbox>)}
              {/* <Link to={URI.FORGOT_PASSWORD} className="login-form-forgot">
                {strings.user.forgotPassword}
              </Link> */}
              <Button
                type="primary"
                htmlType="submit"
                className="login-form-button"
              >
                {strings.user.logIn}
              </Button>
              {`${strings.user.or} `}
              <Link to={URI.REGISTER} onClick={this.handleMenu}>
                {strings.user.registerNow}
              </Link>
            </FormItem>
          </Form>
        </Col>
      </Row>
    )
  }
}

const WrappedLoginForm = Form.create()(Login)

const mapStateToProps = state => {
  return {
    token: state.authentication.token,
    languageCode: state.language.code,
  }
}

export default connect(mapStateToProps)(WrappedLoginForm)
