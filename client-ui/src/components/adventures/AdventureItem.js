import React from 'react'
import { Link } from 'react-router-dom'
import { Card, Col } from 'antd'
import { URI } from './../../helpers/constants'

const { Meta } = Card

const AdventureItem = ({ index, imagePath, adventure }) => (
  <Col sm={12} lg={8}>
    <Link to={`${URI.ADVENTURES}/${index}`}>
      <Card hoverable cover={<img alt="example" src={imagePath} />}>
        <Meta title={adventure.title} description={adventure.description} />
        <div>
          <p>Localisation : {adventure.location}</p>
        </div>
      </Card>
    </Link>
  </Col>
)

export default AdventureItem
