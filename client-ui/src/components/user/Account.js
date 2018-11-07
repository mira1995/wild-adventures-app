import React, { Component } from 'react'
import { Redirect } from 'react-router-dom'
import { connect } from 'react-redux'
import { Form, Icon, Input, Button, Row, Col, Tooltip, DatePicker } from 'antd'
import moment from 'moment'
import jwt from 'jsonwebtoken'
import bcrypt from 'bcryptjs'
import { http } from '../../configurations/axiosConf'
import { URI, API, BEARER_TOKEN } from '../../helpers/constants'
import { TOGGLE_AUTH, TOGGLE_MENU } from '../../store/actions/types'

class Account extends Component {
  constructor(props) {
    super(props)
    this.state = { userAccount: null }
  }

  componentWillMount() {
    const token = this.props.token.substring(7)
    const decoded = jwt.decode(token)

    http
      .get(`/users/email/${decoded.sub}`)
      .then(response => {
        const { ...userAccount } = response.data
        console.log(userAccount)
        this.setState({ userAccount })
      })
      .catch(error => console.log(error))
  }

  handleSubmit = event => {
    event.preventDefault()
    const { userAccount } = this.state
    this.props.form.validateFields((error, values) => {
      if (!error) {
        const gandalf = !bcrypt.compareSync(
          values.password,
          userAccount.password
        )
        const updatedUser = {
          ...userAccount,
          ...values,
          password: bcrypt.hashSync(values.password),
        }
        console.log(updatedUser)
        if (!gandalf) {
          http
            .patch(API.USERS, updatedUser)
            .then(response => {
              console.log(response.data)
            })
            .catch(error => console.log('error', error))
        } else console.log('Wrong credentials.')
      }
    })
  }

  toggleAction(type, value) {
    const action = { type, value }
    this.props.dispatch(action)
  }

  render() {
    if (!this.props.token) return <Redirect to={URI.LOGIN} />

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

    const { userAccount } = this.state
    if (!userAccount) return null
    else
      return (
        <Row type="flex" justify="center" align="middle">
          <Col>
            <Form onSubmit={this.handleSubmit}>
              <FormItem {...formItemLayout} label="E-mail">
                {getFieldDecorator('email', {
                  initialValue: userAccount.email,
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
                  ],
                })(<Input type="password" />)}
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
                  initialValue: userAccount.pseudo,
                  rules: [
                    {
                      message: 'Please input your nickname!',
                      whitespace: true,
                    },
                  ],
                })(<Input disabled={true} />)}
              </FormItem>
              <FormItem {...formItemLayout} label="Firstname">
                {getFieldDecorator('firstname', {
                  initialValue: userAccount.firstname,
                  rules: [
                    { required: true, message: 'Please input your firstname!' },
                  ],
                })(<Input />)}
              </FormItem>
              <FormItem {...formItemLayout} label="Lastname">
                {getFieldDecorator('lastname', {
                  initialValue: userAccount.lastname,
                  rules: [
                    { required: true, message: 'Please input your lastname!' },
                  ],
                })(<Input />)}
              </FormItem>
              <FormItem {...formItemLayout} label="Address">
                {getFieldDecorator('address', {
                  initialValue: userAccount.address,
                  rules: [
                    { required: true, message: 'Please input your address!' },
                  ],
                })(<Input />)}
              </FormItem>
              <FormItem {...formItemLayout} label="Postal code">
                {getFieldDecorator('postalCode', {
                  initialValue: userAccount.postalCode,
                  rules: [
                    {
                      required: true,
                      message: 'Please input your postal code!',
                    },
                  ],
                })(<Input />)}
              </FormItem>
              <FormItem {...formItemLayout} label="City">
                {getFieldDecorator('city', {
                  initialValue: userAccount.city,
                  rules: [
                    { required: true, message: 'Please input your city!' },
                  ],
                })(<Input />)}
              </FormItem>
              <FormItem {...formItemLayout} label="Country">
                {getFieldDecorator('country', {
                  initialValue: userAccount.country,
                  rules: [
                    { required: true, message: 'Please input your country!' },
                  ],
                })(<Input />)}
              </FormItem>
              <FormItem {...formItemLayout} label="Phone number">
                {getFieldDecorator('phoneNumber', {
                  initialValue: userAccount.phoneNumber,
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
                  initialValue: moment(userAccount.birthDate),
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

const WrappedAccountForm = Form.create()(Account)

const mapStateToProps = state => {
  return {
    token: state.authentication.token,
  }
}

export default connect(mapStateToProps)(WrappedAccountForm)
