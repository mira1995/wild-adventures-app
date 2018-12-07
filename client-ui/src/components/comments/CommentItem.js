import React, { Component } from 'react'
import { Card, Col } from 'antd'
import { http } from '../../configurations/axiosConf'
import { API } from '../../helpers/constants'
import { Link } from 'react-router-dom'
import { strings } from '../../helpers/strings'

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
    const { content, comments, children, isActive, isChild } = this.props
    return (
      <Col span={24}>
        <Card title={`${this.state.user.pseudo} ${strings.comments.said}`}>
          <p>{content} </p>
          <p />
          {children}
          <p>
            {!isActive &&
              !isChild && (
                <Link to="#" onClick={this.answerComment}>
                  {strings.comments.reply}
                </Link>
              )}
            {isActive &&
              !isChild && (
                <Link to="#" onClick={this.cancelAnswer}>
                  {strings.comments.cancel}
                </Link>
              )}
          </p>
          <div>
            {comments.map(commentChild => (
              <CommentItem
                isChild={true}
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
