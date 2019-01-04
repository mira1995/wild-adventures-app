import React, { Component } from 'react'
import { Row, message } from 'antd'
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
    http.get(`${API.CATEGORIES}/getAll`).then(response => {
      let categoriesResponse = response.data
      categoriesResponse.map(category =>
        http
          .get(`${API.IMAGES}${API.CATEGORIES}/${category.id}`)
          .then(response => {
            let { categories } = this.state
            category.image = response.data[0]
            categories.push(category)
            this.setState({ categories: categories })
          })
          .catch(() => message.error(strings.statusCode.gettingAdventureError))
      )
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
                imagePath={
                  category.image
                    ? `/images/categories/${category.image.uri}`
                    : '/images/background.jpg'
                }
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
