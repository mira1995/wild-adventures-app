import React, { Component } from 'react'
import { Row } from 'antd'
import { http } from '../../configurations/axiosConf'
import { API } from '../../helpers/constants'
import CategoryItem from './CategoryItem'
import Container from './../../Container'
import { CONF } from './../../helpers/constants'
import { strings } from '../../helpers/strings'

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
            {strings.categories.categoriesList}{' '}
            <small>{strings.categories.goAdventure}</small>
          </h1>
          <Row type="flex" justify="center" align="middle">
            {this.state.categories.map(category => (
              <CategoryItem
                key={category.id}
                index={category.id}
                imagePath="/images/background.jpg"
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
