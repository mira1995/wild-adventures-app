import React, { Component } from 'react'
import { Row } from 'antd'
import { http } from '../../configurations/axiosConf'
import { API } from '../../helpers/constants'
import CategoryItem from './CategoryItem'
import Container from './../../Container'
import { CONF } from './../../helpers/constants'

class Categories extends Component {
  state = {
    categories: [],
  }

  componentWillMount() {
    http.get(API.CATEGORIES).then(response => {
      const categories = response.data
      this.setState({ categories })
    })
  }

  formatContent(content) {
    if (content.length > CONF.CARD_CONTENT_SIZE) {
      return content.substring(0, CONF.CARD_CONTENT_SIZE - 1) + '...'
    } else {
      return content
    }
  }

  render() {
    return (
      <Container>
        <div>
          <h1>
            Liste des catégories <small>Partez à l'aventure</small>
          </h1>
          <Row type="flex" justify="center" align="middle">
            {this.state.categories.map(category => (
              <CategoryItem
                key={category.id}
                index={category.id}
                imagePath="https://www.riu.com/fr/binaris/new-slide-destino-paradise-island_tcm57-138470.jpg"
                title={category.title}
                description={this.formatContent(category.description)}
              />
            ))}
          </Row>
        </div>
      </Container>
    )
  }
}

export default Categories
