import React from 'react'
import { Card, Col } from 'antd'

const CommentItem = ({ key, content, userId, comments }) => (
  <Col span={24}>
    <Card title>
      <p>{content} </p>
      <div>
        {comments.map(commentChild => (
          <CommentItem
            key={commentChild.id}
            user={commentChild.user}
            content={commentChild.content}
            comments={commentChild.comments}
          />
        ))}
      </div>
    </Card>
  </Col>
)

export default CommentItem
