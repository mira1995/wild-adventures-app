import React, { Component } from 'react'
import { Redirect } from 'react-router-dom'
import { connect } from 'react-redux'
import { Form, Icon, Input, Button, Tooltip, DatePicker, message } from 'antd'
import bcrypt from 'bcryptjs'
import moment from 'moment'
import { http } from '../../configurations/axiosConf'
import { URI, API, BEARER_TOKEN } from '../../helpers/constants'
import { TOGGLE_AUTH, TOGGLE_MENU } from '../../store/actions/types'
import Container from '../../Container'
import { strings } from '../../helpers/strings'

class Register extends Component {
  constructor(props) {
    super(props)
    this.state = { confirmDirty: false }
  }
  handleSubmit = event => {
    event.preventDefault()
    this.props.form.validateFields((error, values) => {
      if (!error) {
        const formatedValues = {
          ...values,
          birthDate: values['birthDate'].format('YYYY-MM-DD'),
        }
        const { confirm, ...userAccount } = formatedValues
        userAccount.password = bcrypt.hashSync(userAccount.password)
        http
          .post(API.USERS, { ...userAccount })
          .then(() => {
            const user = {
              username: formatedValues.email,
              password: formatedValues.password,
            }
            http
              .post(API.AUTH, user)
              .then(response => {
                const bearerToken = response.headers.authorization
                console.log(bearerToken)
                const { cookies } = this.props
                const exp = moment().add(15, 'minutes')
                cookies.set(BEARER_TOKEN, bearerToken, {
                  path: '/',
                  expires: exp.toDate(),
                })
                this.toggleAction(TOGGLE_AUTH, cookies.get(BEARER_TOKEN))
                this.toggleAction(TOGGLE_MENU, URI.HOME)
              })
              .catch(error => {
                if (error.response.status === 401)
                  message.error(strings.statusCode.wrongCredentials)
                else message.error(strings.statusCode.serverNotFound)
                return <Redirect to={URI.LOGIN} />
              })
          })
          .catch(() => message.error(strings.statusCode.accountCreation))
      }
    })
  }

  validateToNextPassword = (rule, value, callback) => {
    const form = this.props.form
    if (value && this.state.confirmDirty) {
      form.validateFields(['confirm'], { force: true })
    }
    callback()
  }

  compareToFirstPassword = (rule, value, callback) => {
    const form = this.props.form
    if (value && value !== form.getFieldValue('password')) {
      callback(strings.user.form.compareToFirstPassword)
    } else {
      callback()
    }
  }

  handleConfirmBlur = e => {
    const value = e.target.value
    this.setState({ confirmDirty: this.state.confirmDirty || !!value })
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
      <Container>
        <Form onSubmit={this.handleSubmit}>
          <FormItem {...formItemLayout} label={strings.user.form.emailLabel}>
            {getFieldDecorator('email', {
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
            })(<Input />)}
          </FormItem>
          <FormItem {...formItemLayout} label={strings.user.form.passwordLabel}>
            {getFieldDecorator('password', {
              rules: [
                {
                  required: true,
                  message: strings.user.form.passwordMessageRule,
                },
                {
                  validator: this.validateToNextPassword,
                },
              ],
            })(<Input type="password" />)}
          </FormItem>
          <FormItem
            {...formItemLayout}
            label={strings.user.form.confirmPasswordLabel}
          >
            {getFieldDecorator('confirm', {
              rules: [
                {
                  required: true,
                  message: strings.user.form.confirmPasswordMessageRule,
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
                {strings.user.form.pseudoLabel}
                &nbsp;
                <Tooltip title={strings.user.form.pseudoTooltipTitle}>
                  <Icon type="question-circle-o" />
                </Tooltip>
              </span>
            }
          >
            {getFieldDecorator('pseudo', {
              rules: [
                {
                  required: true,
                  message: strings.user.form.pseudoMessageRule,
                  whitespace: true,
                },
              ],
            })(<Input />)}
          </FormItem>
          <FormItem
            {...formItemLayout}
            label={strings.user.form.firstnameLabel}
          >
            {getFieldDecorator('firstname', {
              rules: [
                {
                  required: true,
                  message: strings.user.form.firstnameMessageRule,
                },
              ],
            })(<Input />)}
          </FormItem>
          <FormItem {...formItemLayout} label={strings.user.form.lastnameLabel}>
            {getFieldDecorator('lastname', {
              rules: [
                {
                  required: true,
                  message: strings.user.form.lastnameMessageRule,
                },
              ],
            })(<Input />)}
          </FormItem>
          <FormItem {...formItemLayout} label={strings.user.form.addressLabel}>
            {getFieldDecorator('address', {
              rules: [
                {
                  required: true,
                  message: strings.user.form.addressMessageRule,
                },
              ],
            })(<Input />)}
          </FormItem>
          <FormItem
            {...formItemLayout}
            label={strings.user.form.postalCodeLabel}
          >
            {getFieldDecorator('postalCode', {
              rules: [
                {
                  required: true,
                  message: strings.user.form.postalCodeMessageRule,
                },
              ],
            })(<Input />)}
          </FormItem>
          <FormItem {...formItemLayout} label={strings.user.form.cityLabel}>
            {getFieldDecorator('city', {
              rules: [
                { required: true, message: strings.user.form.cityMessageRule },
              ],
            })(<Input />)}
          </FormItem>
          <FormItem {...formItemLayout} label={strings.user.form.countryLabel}>
            {getFieldDecorator('country', {
              rules: [
                {
                  required: true,
                  message: strings.user.form.countryMessageRule,
                },
              ],
            })(<Input />)}
          </FormItem>
          <FormItem
            {...formItemLayout}
            label={strings.user.form.phoneNumberLabel}
          >
            {getFieldDecorator('phoneNumber', {
              rules: [
                {
                  required: true,
                  message: strings.user.form.phoneNumberMessageRule,
                },
              ],
            })(<Input />)}
          </FormItem>
          <FormItem
            {...formItemLayout}
            label={strings.user.form.birthDateLabel}
          >
            {getFieldDecorator('birthDate', {
              rules: [
                {
                  type: 'object',
                  required: true,
                  message: strings.user.form.birthDateMessageRule,
                },
              ],
            })(
              <DatePicker
                placeholder={strings.user.form.birthDatePlaceholder}
                format={strings.user.form.birthDateFormat}
                showToday={false}
              />
            )}
          </FormItem>
          <FormItem {...tailFormItemLayout}>
            <Button type="primary" htmlType="submit">
              {strings.user.register}
            </Button>
          </FormItem>
        </Form>
      </Container>
    )
  }
}

const WrappedRegisterForm = Form.create()(Register)

const mapStateToProps = state => {
  return {
    token: state.authentication.token,
    languageCode: state.language.code,
  }
}

export default connect(mapStateToProps)(WrappedRegisterForm)
