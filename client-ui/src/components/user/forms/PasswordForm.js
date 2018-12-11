import React, { Component } from 'react'
import { connect } from 'react-redux'
import { Form, Input, Button } from 'antd'
import bcrypt from 'bcryptjs'
import Container from './../../../Container'
import { strings } from '../../../helpers/strings'

class PasswordForm extends Component {
  constructor(props) {
    super(props)
    this.state = { confirmDirty: false }
  }

  handleSubmit = event => {
    event.preventDefault()
    this.props.form.validateFields((error, values) => {
      if (!error) {
        const gandalf = !bcrypt.compareSync(
          values.confirmPasswordWithPassword,
          this.props.user.password
        )
        if (!gandalf) {
          const {
            confirmPasswordWithPassword,
            // confirmNew,
            ...partialUser
          } = { ...values, password: bcrypt.hashSync(values.password) }

          this.props.form.setFieldsValue({ password: null })
          this.props.form.setFieldsValue({ confirmNew: null })
          this.props.form.setFieldsValue({ confirmPasswordWithPassword: null })

          this.props.action(partialUser)
        } else {
          console.log('Wrong credentials.')
          this.props.form.setFieldsValue({ confirmPasswordWithPassword: null })
        }
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

  render() {
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

    return (
      <Container>
        <Form onSubmit={this.handleSubmit}>
          <FormItem
            {...formItemLayout}
            label={strings.user.form.currentPasswordLabel}
          >
            {getFieldDecorator('confirmPasswordWithPassword', {
              rules: [
                {
                  required: true,
                  message: strings.user.form.passwordMessageRule,
                },
              ],
            })(<Input type="password" />)}
          </FormItem>
          <FormItem
            {...formItemLayout}
            label={strings.user.form.newPasswordLabel}
          >
            {getFieldDecorator('password', {
              rules: [
                {
                  required: true,
                  message: strings.user.form.newPasswordMessageRule,
                },
                {
                  validator: this.validateToNextPassword,
                },
              ],
            })(<Input type="password" />)}
          </FormItem>
          <FormItem
            {...formItemLayout}
            label={strings.user.form.confirmNewPasswordLabel}
          >
            {getFieldDecorator('confirmNew', {
              rules: [
                {
                  required: true,
                  message: strings.user.form.confirmNewPasswordMessageRule,
                },
                {
                  validator: this.compareToFirstPassword,
                },
              ],
            })(<Input type="password" onBlur={this.handleConfirmBlur} />)}
          </FormItem>
          <FormItem {...tailFormItemLayout}>
            <Button type="primary" htmlType="submit">
              {strings.user.send}
            </Button>
          </FormItem>
        </Form>
      </Container>
    )
  }
}

const WrappedPasswordForm = Form.create()(PasswordForm)

const mapStateToProps = state => ({
  languageCode: state.language.code,
})

export default connect(mapStateToProps)(WrappedPasswordForm)
