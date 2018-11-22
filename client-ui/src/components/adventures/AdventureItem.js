import React from 'react'
import { Link } from 'react-router-dom'
import { Card, Col } from 'antd'
import { URI, CONF } from './../../helpers/constants'

const { Meta } = Card

const gridStyle = {
  width: '100%',
}

const gridBodyStyle = {
  width: '100%',
}

let formatContent = content => {
  if (content.length > CONF.CARD_CONTENT_SIZE) {
    return content.substring(0, CONF.CARD_CONTENT_SIZE - 1) + '...'
  } else {
    return content
  }
}

const AdventureItem = ({ index, imagePath, adventure }) => (
  <Col sm={24} xl={12} className="cardCol">
    <Link to={`${URI.ADVENTURES}/${index}`}>
      <Card hoverable cover={<img alt="example" src={imagePath} />}>
        <Card.Grid style={gridStyle}>
          <Meta title={adventure.title} />
        </Card.Grid>
        <Card.Grid style={gridBodyStyle}>
          {formatContent(adventure.description)}
        </Card.Grid>
        <Card.Grid style={gridStyle}>
          <div>
            <p>Localisation : {adventure.location}</p>
          </div>
        </Card.Grid>
      </Card>
    </Link>
  </Col>
)

export default AdventureItem
