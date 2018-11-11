import React, { Component } from 'react'
import { Form, Input, Button } from 'antd'
import { http } from '../../configurations/axiosConf'
import { API } from '../../helpers/constants'
import jwt from 'jsonwebtoken'
import { connect } from 'react-redux'

class CommentForm extends Component {
  constructor(props) {
    super(props)
    this.state = {
      adventureId: this.props.adventureId,
      userAccount: null,
    }
  }

  componentWillMount() {
    const token = this.props.token.substring(7)
    const decoded = jwt.decode(token)
    http
      .get(`${API.USERS}/email/${decoded.sub}`)
      .then(response => {
        const { password, ...userAccount } = response.data
        this.setState({ userAccount })
      })
      .catch(error => console.log('error', error))
  }

  persistComment = event => {
    event.preventDefault()
    this.props.form.validateFields((error, values) => {
      if (!error) {
        console.log('Received values of form: ', values)
        let commentToAdd = {
          content: values.content,
          reported: false,
          comments: [],
          adventureId: this.props.adventureId,
          userId: this.state.userAccount.id,
        }
        console.log(commentToAdd)
        if (this.props.parent !== null) {
          let { parent } = this.props
          parent.comments.push(commentToAdd)
          console.log(parent)
          http
            .patch(API.COMMENTS, { parent })
            .then(response => {
              this.props.action(response.data)
            })
            .catch(error => console.log('error', error))
        } else {
          http
            .post(API.COMMENTS, { commentToAdd })
            .then(response => {
              this.props.action(response.data)
            })
            .catch(error => console.log('error', error))
        }
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

CommentForm.defaultProps = {
  parent: null,
  adventureId: 0,
}

const WrappedCommentForm = Form.create()(CommentForm)

const mapStateToProps = state => {
  return {
    token: state.authentication.token,
  }
}

export default connect(mapStateToProps)(WrappedCommentForm)
