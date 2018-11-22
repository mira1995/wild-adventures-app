import React, { Component } from 'react'
import { Form, Icon, Input, Button, Tooltip } from 'antd'
import bcrypt from 'bcryptjs'
import Container from './../../../Container'

class PseudoForm extends Component {
  handleSubmit = event => {
    event.preventDefault()
    this.props.form.validateFields((error, values) => {
      if (!error) {
        const gandalf = !bcrypt.compareSync(
          values.confirmPseudoWithPassword,
          this.props.user.password
        )
        if (!gandalf) {
          const {
            currentEmail,
            // confirmPseudoWithPassword,
            ...partialUser
          } = values

          this.props.form.setFieldsValue({ pseudo: null })
          this.props.form.setFieldsValue({ confirmPseudoWithPassword: null })

          this.props.action(partialUser)
        } else {
          console.log('Wrong credentials.')
          this.props.form.setFieldsValue({ confirmPseudoWithPassword: null })
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

    // TODO: check in real time pseudo unique
    return (
      <Container>
        <Form onSubmit={this.handleSubmit}>
          <FormItem {...formItemLayout} label="Current pseudo">
            {getFieldDecorator('currentPseudo', {
              initialValue: user.pseudo,
            })(<Input disabled={true} />)}
          </FormItem>
          <FormItem
            {...formItemLayout}
            label={
              <span>
                New pseudo&nbsp;
                <Tooltip title="What do you want others to call you?">
                  <Icon type="question-circle-o" />
                </Tooltip>
              </span>
            }
          >
            {getFieldDecorator('pseudo', {
              rules: [
                {
                  message: 'Please input your nickname!',
                  whitespace: true,
                },
                { required: true, message: 'Please input your pseudo!' },
              ],
            })(<Input />)}
          </FormItem>
          <FormItem {...formItemLayout} label="Current password">
            {getFieldDecorator('confirmPseudoWithPassword', {
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
      </Container>
    )
  }
}

const WrappedPseudoForm = Form.create()(PseudoForm)

export default WrappedPseudoForm
