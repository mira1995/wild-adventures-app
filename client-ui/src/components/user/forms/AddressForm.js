import React, { Component } from 'react'
import { Form, Input, Button, Row, Col, DatePicker } from 'antd'
import moment from 'moment'

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
      <Row type="flex" justify="center" align="middle">
        <Col>
          <Form onSubmit={this.handleSubmit}>
            <FormItem {...formItemLayout} label="Firstname">
              {getFieldDecorator('firstname', {
                initialValue: user.firstname,
                rules: [
                  {
                    required: true,
                    message: 'Please input your firstname!',
                  },
                ],
              })(<Input />)}
            </FormItem>
            <FormItem {...formItemLayout} label="Lastname">
              {getFieldDecorator('lastname', {
                initialValue: user.lastname,
                rules: [
                  {
                    required: true,
                    message: 'Please input your lastname!',
                  },
                ],
              })(<Input />)}
            </FormItem>
            <FormItem {...formItemLayout} label="Address">
              {getFieldDecorator('address', {
                initialValue: user.address,
                rules: [
                  { required: true, message: 'Please input your address!' },
                ],
              })(<Input />)}
            </FormItem>
            <FormItem {...formItemLayout} label="Postal code">
              {getFieldDecorator('postalCode', {
                initialValue: user.postalCode,
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
                initialValue: user.city,
                rules: [{ required: true, message: 'Please input your city!' }],
              })(<Input />)}
            </FormItem>
            <FormItem {...formItemLayout} label="Country">
              {getFieldDecorator('country', {
                initialValue: user.country,
                rules: [
                  { required: true, message: 'Please input your country!' },
                ],
              })(<Input />)}
            </FormItem>
            <FormItem {...formItemLayout} label="Phone number">
              {getFieldDecorator('phoneNumber', {
                initialValue: user.phoneNumber,
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
                initialValue: moment(user.birthDate),
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
                Send
              </Button>
            </FormItem>
          </Form>
        </Col>
      </Row>
    )
  }
}

const WrappedAddressForm = Form.create()(AddressForm)

export default WrappedAddressForm
