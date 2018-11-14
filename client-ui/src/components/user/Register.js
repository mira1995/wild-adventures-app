import React, { Component } from 'react'
import { Link, Redirect } from 'react-router-dom'
import { connect } from 'react-redux'
import {
  Form,
  Icon,
  Input,
  Button,
  Checkbox,
  Row,
  Col,
  Tooltip,
  DatePicker,
} from 'antd'
import bcrypt from 'bcryptjs'
import { http } from '../../configurations/axiosConf'
import { URI, API, BEARER_TOKEN } from '../../helpers/constants'
import { TOGGLE_AUTH, TOGGLE_MENU } from '../../store/actions/types'

class Register extends Component {
  handleSubmit = event => {
    event.preventDefault()
    this.props.form.validateFields((error, values) => {
      if (!error) {
        const formatedValues = {
          ...values,
          birthDate: values['birthDate'].format('YYYY-MM-DD'),
        }
        const { confirm, agreement, ...userAccount } = formatedValues
        userAccount.password = bcrypt.hashSync(userAccount.password)
        http
          .post(API.USERS, { ...userAccount })
          .then(() => {
            // TODO: Envoyer le mot de passe crypté
            const user = {
              username: formatedValues.email,
              password: formatedValues.password,
            }
            http
              .post(API.AUTH, user)
              .then(response => {
                const bearerToken = response.headers.authorization
                console.log(bearerToken)
                sessionStorage.setItem(BEARER_TOKEN, bearerToken)
                this.toggleAction(
                  TOGGLE_AUTH,
                  sessionStorage.getItem(BEARER_TOKEN)
                )
                this.toggleAction(TOGGLE_MENU, URI.HOME)
              })
              .catch(error => {
                console.log('error', error)
                this.props.form.setFieldsValue({ password: null })
              })
          })
          .catch(error => console.log('error', error))
      }
    })
  }

  validateToNextPassword = (rule, value, callback) => {
    const form = this.props.form
    if (value) {
      form.validateFields(['confirm'], { force: true })
    }
    callback()
  }

  compareToFirstPassword = (rule, value, callback) => {
    const form = this.props.form
    if (value && value !== form.getFieldValue('password')) {
      callback('Two passwords that you enter is inconsistent!')
    } else {
      callback()
    }
  }

  toggleAction(type, value) {
    const action = { type, value }
    this.props.dispatch(action)
  }

  render() {
    if (this.props.token) return <Redirect to={URI.HOME} />

    const FormItem = Form.Item
    const { getFieldDecorator } = this.props.form

    const formItemLayout = {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 8 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
    }

    const tailFormItemLayout = {
      wrapperCol: {
        xs: {
          span: 24,
          offset: 0,
        },
        sm: {
          span: 16,
          offset: 8,
        },
      },
    }

    // TODO: check in real time email et pseudo unique
    return (
      <Row type="flex" justify="center" align="middle">
        <Col>
          <Form onSubmit={this.handleSubmit}>
            <FormItem {...formItemLayout} label="E-mail">
              {getFieldDecorator('email', {
                rules: [
                  {
                    type: 'email',
                    message: 'The input is not valid E-mail!',
                  },
                  { required: true, message: 'Please input your email!' },
                ],
              })(<Input />)}
            </FormItem>
            <FormItem {...formItemLayout} label="Password">
              {getFieldDecorator('password', {
                rules: [
                  {
                    required: true,
                    message: 'Please input your password!',
                  },
                  {
                    validator: this.validateToNextPassword,
                  },
                ],
              })(<Input type="password" />)}
            </FormItem>
            <FormItem {...formItemLayout} label="Confirm Password">
              {getFieldDecorator('confirm', {
                rules: [
                  {
                    required: true,
                    message: 'Please confirm your password!',
                  },
                  {
                    validator: this.compareToFirstPassword,
                  },
                ],
              })(<Input type="password" onBlur={this.handleConfirmBlur} />)}
            </FormItem>
            <FormItem
              {...formItemLayout}
              label={
                <span>
                  Pseudo&nbsp;
                  <Tooltip title="What do you want others to call you?">
                    <Icon type="question-circle-o" />
                  </Tooltip>
                </span>
              }
            >
              {getFieldDecorator('pseudo', {
                rules: [
                  {
                    required: true,
                    message: 'Please input your nickname!',
                    whitespace: true,
                  },
                ],
              })(<Input />)}
            </FormItem>
            <FormItem {...formItemLayout} label="Firstname">
              {getFieldDecorator('firstname', {
                rules: [
                  { required: true, message: 'Please input your firstname!' },
                ],
              })(<Input />)}
            </FormItem>
            <FormItem {...formItemLayout} label="Lastname">
              {getFieldDecorator('lastname', {
                rules: [
                  { required: true, message: 'Please input your lastname!' },
                ],
              })(<Input />)}
            </FormItem>
            <FormItem {...formItemLayout} label="Address">
              {getFieldDecorator('address', {
                rules: [
                  { required: true, message: 'Please input your address!' },
                ],
              })(<Input />)}
            </FormItem>
            <FormItem {...formItemLayout} label="Postal code">
              {getFieldDecorator('postalCode', {
                rules: [
                  { required: true, message: 'Please input your postal code!' },
                ],
              })(<Input />)}
            </FormItem>
            <FormItem {...formItemLayout} label="City">
              {getFieldDecorator('city', {
                rules: [{ required: true, message: 'Please input your city!' }],
              })(<Input />)}
            </FormItem>
            <FormItem {...formItemLayout} label="Country">
              {getFieldDecorator('country', {
                rules: [
                  { required: true, message: 'Please input your country!' },
                ],
              })(<Input />)}
            </FormItem>
            <FormItem {...formItemLayout} label="Phone number">
              {getFieldDecorator('phoneNumber', {
                rules: [
                  {
                    required: true,
                    message: 'Please input your phone number!',
                  },
                ],
              })(<Input />)}
            </FormItem>
            <FormItem {...formItemLayout} label="Birth date">
              {getFieldDecorator('birthDate', {
                rules: [
                  {
                    type: 'object',
                    required: true,
                    message: 'Please input your birth date!',
                  },
                ],
              })(<DatePicker format="DD MMMM YYYY" />)}
            </FormItem>
            <FormItem {...tailFormItemLayout}>
              {getFieldDecorator('agreement', {
                valuePropName: 'checked',
              })(
                <Checkbox>
                  I have read the <Link to="agreement">agreement</Link>
                </Checkbox>
              )}
            </FormItem>
            <FormItem {...tailFormItemLayout}>
              <Button type="primary" htmlType="submit">
                Register
              </Button>
            </FormItem>
          </Form>
        </Col>
      </Row>
    )
  }
}

const WrappedRegisterForm = Form.create()(Register)

const mapStateToProps = state => {
  return {
    token: state.authentication.token,
  }
}

export default connect(mapStateToProps)(WrappedRegisterForm)
