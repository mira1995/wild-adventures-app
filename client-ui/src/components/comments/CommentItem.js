import React, { Component } from 'react'
import { Card, Col } from 'antd'
import { http } from '../../configurations/axiosConf'
import { API } from '../../helpers/constants'

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

  render() {
    const { content, comments } = this.props
    return (
      <Col span={24}>
        <Card title={`${this.state.user.pseudo} a dit`}>
          <p>{content} </p>
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
