import React, { Component } from 'react'
import { Card, Col } from 'antd'
import { http } from '../../configurations/axiosConf'
import { API } from '../../helpers/constants'
import { Link } from 'react-router-dom'

class CommentItem extends Component {
  constructor(props) {
    super(props)
    this.state = {
      user: [],
    }
  }

  componentWillMount() {
    const { userId } = this.props
    http
      .get(`${API.USERS}/${userId}`)
      .then(response => this.setState({ user: response.data }))
  }

  answerComment = event => {
    event.preventDefault()
    const { answerAction, commentId } = this.props
    answerAction(commentId)
  }

  cancelAnswer = event => {
    event.preventDefault()
    const { answerAction } = this.props
    answerAction(null)
  }

  render() {
    const { content, comments, children, isActive } = this.props
    return (
      <Col span={24}>
        <Card title={`${this.state.user.pseudo} a dit`}>
          <p>{content} </p>
          <p />
          {children}
          <p>
            {!isActive && (
              <Link to="#" onClick={this.answerComment}>
                Répondre
              </Link>
            )}
            {isActive && (
              <Link to="#" onClick={this.cancelAnswer}>
                Annuler
              </Link>
            )}
          </p>
          <div>
            {comments.map(commentChild => (
              <CommentItem
                key={commentChild.id}
                userId={commentChild.userId}
                content={commentChild.content}
                comments={commentChild.comments}
              />
            ))}
          </div>
          <div />
        </Card>
      </Col>
    )
  }
}

export default CommentItem
