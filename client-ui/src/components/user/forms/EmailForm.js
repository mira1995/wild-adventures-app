import React, { Component } from 'react'
import { Form, Input, Button, Row, Col } from 'antd'
import bcrypt from 'bcryptjs'

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
      <Row type="flex" justify="center" align="middle">
        <Col>
          <Form onSubmit={this.handleSubmit}>
            <FormItem {...formItemLayout} label="Current e-mail">
              {getFieldDecorator('currentEmail', {
                initialValue: user.email,
              })(<Input disabled={true} />)}
            </FormItem>
            <FormItem {...formItemLayout} label="New e-mail">
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
            <FormItem {...formItemLayout} label="Current password">
              {getFieldDecorator('confirmEmailWithPassword', {
                rules: [
                  {
                    required: true,
                    message: 'Please input your current password!',
                  },
                ],
              })(<Input type="password" />)}
            </FormItem>
            <FormItem {...tailFormItemLayout}>
              <Button type="primary" htmlType="submit">
                Send
              </Button>
            </FormItem>
          </Form>
        </Col>
      </Row>
    )
  }
}

const WrappedEmailForm = Form.create()(EmailForm)

export default WrappedEmailForm
