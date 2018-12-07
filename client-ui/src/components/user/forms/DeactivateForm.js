import React, { Component } from 'react'
import { Form, Input, Button } from 'antd'
import bcrypt from 'bcryptjs'
import Container from './../../../Container'
import { strings } from '../../../helpers/strings'

class DeactivateForm extends Component {
  handleSubmit = event => {
    event.preventDefault()
    this.props.form.validateFields((error, values) => {
      if (!error) {
        const gandalf = !bcrypt.compareSync(
          values.selfDestruction,
          this.props.user.password
        )
        if (!gandalf) {
          const partialUser = { active: false }
          this.props.form.setFieldsValue({ selfDestruction: null })
          this.props.action(partialUser)
        } else {
          console.log('Wrong credentials.')
          this.props.form.setFieldsValue({ selfDestruction: null })
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

    return (
      <Container>
        <Form onSubmit={this.handleSubmit}>
          <FormItem {...formItemLayout} label={strings.user.form.passwordLabel}>
            {getFieldDecorator('selfDestruction', {
              rules: [
                {
                  required: true,
                  message: strings.user.form.passwordMessageRule,
                },
              ],
            })(<Input type="password" />)}
          </FormItem>
          <FormItem {...tailFormItemLayout}>
            <Button type="danger" htmlType="submit">
              {strings.user.deactivateAccount}
            </Button>
          </FormItem>
        </Form>
      </Container>
    )
  }
}

const WrappedDeactivateForm = Form.create()(DeactivateForm)

export default WrappedDeactivateForm
