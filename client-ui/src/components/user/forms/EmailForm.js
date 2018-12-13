import React, { Component } from 'react'
import { connect } from 'react-redux'
import { Form, Input, Button } from 'antd'
import bcrypt from 'bcryptjs'
import Container from './../../../Container'
import { strings } from '../../../helpers/strings'

class EmailForm extends Component {
  handleSubmit = event => {
    event.preventDefault()
    this.props.form.validateFields((error, values) => {
      if (!error) {
        const gandalf = !bcrypt.compareSync(
          values.confirmEmailWithPassword,
          this.props.user.password
        )
        if (!gandalf) {
          const {
            currentEmail,
            // confirmEmailWithPassword,
            ...partialUser
          } = values

          this.props.form.setFieldsValue({ email: null })
          this.props.form.setFieldsValue({ confirmEmailWithPassword: null })

          this.props.action(partialUser)
        } else {
          console.log('Wrong credentials.')
          this.props.form.setFieldsValue({ confirmEmailWithPassword: null })
        }
      }
    })
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

    const { user } = this.props

    // TODO: check in real time email unique
    return (
      <Container>
        <Form onSubmit={this.handleSubmit}>
          <FormItem
            {...formItemLayout}
            label={strings.user.form.currentEmailLabel}
          >
            {getFieldDecorator('currentEmail', {
              initialValue: user.email,
            })(<Input disabled={true} />)}
          </FormItem>
          <FormItem {...formItemLayout} label={strings.user.form.newEmailLabel}>
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
          <FormItem
            {...formItemLayout}
            label={strings.user.form.currentPasswordLabel}
          >
            {getFieldDecorator('confirmEmailWithPassword', {
              rules: [
                {
                  required: true,
                  message: strings.user.form.passwordMessageRule,
                },
              ],
            })(<Input type="password" />)}
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

const WrappedEmailForm = Form.create()(EmailForm)

const mapStateToProps = state => ({
  languageCode: state.language.code,
})

export default connect(mapStateToProps)(WrappedEmailForm)
