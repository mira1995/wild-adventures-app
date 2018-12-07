import React, { Component } from 'react'
import { Form, Input, Button, DatePicker } from 'antd'
import moment from 'moment'
import Container from './../../../Container'
import { strings } from '../../../helpers/strings'

class AddressForm extends Component {
  handleSubmit = event => {
    event.preventDefault()
    this.props.form.validateFields((error, values) => {
      if (!error) {
        this.props.action(values)
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

    return (
      <Container>
        <Form onSubmit={this.handleSubmit}>
          <FormItem
            {...formItemLayout}
            label={strings.user.form.firstnameLabel}
          >
            {getFieldDecorator('firstname', {
              initialValue: user.firstname,
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
              initialValue: user.lastname,
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
              initialValue: user.address,
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
              initialValue: user.postalCode,
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
              initialValue: user.city,
              rules: [
                { required: true, message: strings.user.form.cityMessageRule },
              ],
            })(<Input />)}
          </FormItem>
          <FormItem {...formItemLayout} label={strings.user.form.countryLabel}>
            {getFieldDecorator('country', {
              initialValue: user.country,
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
              initialValue: user.phoneNumber,
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
              initialValue: moment(user.birthDate),
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
              {strings.user.send}
            </Button>
          </FormItem>
        </Form>
      </Container>
    )
  }
}

const WrappedAddressForm = Form.create()(AddressForm)

export default WrappedAddressForm
