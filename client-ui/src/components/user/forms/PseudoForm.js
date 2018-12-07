import React, { Component } from 'react'
import { Form, Icon, Input, Button, Tooltip } from 'antd'
import bcrypt from 'bcryptjs'
import Container from './../../../Container'
import { strings } from '../../../helpers/strings'

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
          <FormItem
            {...formItemLayout}
            label={strings.user.form.currentPseudoLabel}
          >
            {getFieldDecorator('currentPseudo', {
              initialValue: user.pseudo,
            })(<Input disabled={true} />)}
          </FormItem>
          <FormItem
            {...formItemLayout}
            label={
              <span>
                {strings.user.form.newPseudoLabel}
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
                  message: strings.user.form.pseudoMessageRule,
                  whitespace: true,
                },
                {
                  required: true,
                  message: strings.user.form.pseudoMessageRule,
                },
              ],
            })(<Input />)}
          </FormItem>
          <FormItem
            {...formItemLayout}
            label={strings.user.form.currentPasswordLabel}
          >
            {getFieldDecorator('confirmPseudoWithPassword', {
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

const WrappedPseudoForm = Form.create()(PseudoForm)

export default WrappedPseudoForm
