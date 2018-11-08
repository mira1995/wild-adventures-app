import React, { Component } from 'react'
import { Form, Input, Button } from 'antd'
import { http } from '../../configurations/axiosConf'
import { API, URI } from '../../helpers/constants'

class CommentForm extends Component {
  constructor(props) {
    super(props)
    this.state = {
      adventureId: this.props.adventureId,
    }
  }
  persistComment = event => {
    event.preventDefault()
    this.props.form.validateFields((error, values) => {
      if (!error) {
        console.log('Received values of form: ', values)
        http
          .post(API.COMMENTS, {
            content: values.content,
            reported: false,
            comments: null,
            adventureId: this.state.adventureId,
            userId: 1,
          })
          .then(response => {
            const { router } = this.props
            router.push(`${URI.ADVENTURES}/${this.state.adventureId}`)
          })
          .catch(error => console.log('error', error))
      }
    })
  }

  render() {
    const { commentId } = this.props
    const FormItem = Form.Item
    const { getFieldDecorator } = this.props.form
    return (
      <Form
        id={commentId}
        className={`commentForm`}
        onSubmit={this.persistComment}
      >
        <FormItem>
          {getFieldDecorator('content', {
            rules: [
              { required: true, message: 'Merci de rentrer un commentaire' },
            ],
          })(<Input rows={4} placeholder="Ecrivez votre commentaire ici" />)}
          <Button
            type="primary"
            htmlType="submit"
            className="login-form-button"
          >
            Envoyer
          </Button>
        </FormItem>
      </Form>
    )
  }
}

const WrappedCommentForm = Form.create()(CommentForm)

export default WrappedCommentForm
