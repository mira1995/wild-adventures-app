import React, { Component } from 'react'
import { Row, Col } from 'antd'

class Container extends Component {
  render() {
    const { children } = this.props
    return (
      <Row type="flex" justify="center" align="top">
        <Col lg={15} md={18} sm={21} xs={22} className="customContainer">
          {children}
        </Col>
      </Row>
    )
  }
}

export default Container
