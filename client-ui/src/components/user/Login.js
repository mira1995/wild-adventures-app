import React, { Component } from 'react'
import { Link, Redirect } from 'react-router-dom'
import { connect } from 'react-redux'
import { Form, Icon, Input, Button, Checkbox, Row, Col } from 'antd'
import './Login.css'
import { http } from '../../configurations/axiosConf'
import { TOGGLE_AUTH, TOGGLE_MENU } from '../../store/actions/types'
import { BEARER_TOKEN, URI, API } from '../../helpers/constants'

class Login extends Component {
  handleSubmit = event => {
    event.preventDefault()
    this.props.form.validateFields((error, values) => {
      if (!error) {
        console.log('Received values of form: ', values)
        http
          .post(API.AUTH, {
            username: values.email,
            password: values.password,
          })
          .then(response => {
            const bearerToken = response.headers.authorization
            console.log(bearerToken)
            sessionStorage.setItem(BEARER_TOKEN, bearerToken)
            this.toggleAction(TOGGLE_AUTH, sessionStorage.getItem(BEARER_TOKEN))
            this.toggleAction(TOGGLE_MENU, URI.HOME)
          })
          .catch(error => console.log('error', error))
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
      <div>
        <Row type="flex" justify="center" align="middle" className="container">
          <Col>
            <Form onSubmit={this.handleSubmit} className="login-form">
              <FormItem>
                {getFieldDecorator('email', {
                  rules: [
                    { required: true, message: 'Please input your email!' },
                  ],
                })(
                  <Input
                    prefix={
                      <Icon type="mail" style={{ color: 'rgba(0,0,0,.25)' }} />
                    }
                    placeholder="E-mail"
                  />
                )}
              </FormItem>
              <FormItem>
                {getFieldDecorator('password', {
                  rules: [
                    { required: true, message: 'Please input your Password!' },
                  ],
                })(
                  <Input
                    prefix={
                      <Icon type="lock" style={{ color: 'rgba(0,0,0,.25)' }} />
                    }
                    type="password"
                    placeholder="Password"
                  />
                )}
              </FormItem>
              <FormItem>
                {getFieldDecorator('remember', {
                  valuePropName: 'checked',
                  initialValue: false,
                })(<Checkbox>Remember me</Checkbox>)}
                <Link to={URI.FORGOT_PASSWORD} className="login-form-forgot">
                  Forgot password
                </Link>
                <Button
                  type="primary"
                  htmlType="submit"
                  className="login-form-button"
                >
                  Log in
                </Button>
                Or{' '}
                <Link to={URI.REGISTER} onClick={this.handleMenu}>
                  register now!
                </Link>
              </FormItem>
            </Form>
          </Col>
        </Row>
      </div>
    )
  }
}

const WrappedLoginForm = Form.create()(Login)

const mapStateToProps = state => {
  return {
    token: state.authentication.token,
  }
}

export default connect(mapStateToProps)(WrappedLoginForm)
