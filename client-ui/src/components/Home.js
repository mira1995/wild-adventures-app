import React, { Component } from 'react'
import { strings } from '../helpers/strings'
import Container from '../Container'
import { http } from '../configurations/axiosConf'
import { API, CONF } from './../helpers/constants'
import { Row } from 'antd'
import AdventureItem from './adventures/AdventureItem'
import CategoryItem from './categories/CategoryItem'

class Home extends Component {
  constructor(props) {
    super(props)
    this.state = {
      lastFiveAdventures: [],
      lastFiveCategories: [],
    }
  }

  componentWillMount() {
    http
      .get(`${API.ADVENTURES}/last5ById`)
      .then(response => this.setState({ lastFiveAdventures: response.data }))
      .catch(error => console.log(error))
    http
      .get(`${API.CATEGORIES}/lastFiveCategories`)
      .then(response => this.setState({ lastFiveCategories: response.data }))
      .catch(error => console.log(error))
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
        <h1>{strings.routes.home}</h1>
        <p>{strings.home.welcomeText}</p>
        <div className="lastFiveCategories">
          <h2>{strings.home.categoryTitle}</h2>
          <Row type="flex" justify="center" align="middle">
            {this.state.lastFiveCategories.map(category => (
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
        <div className="lastFiveAdventures">
          <h2>{strings.home.adventureTitle}</h2>
          <Row type="flex" align="center">
            {this.state.lastFiveAdventures.map(adventure => (
              <AdventureItem
                key={adventure.id}
                index={adventure.id}
                imagePath="/images/background.jpg"
                adventure={adventure}
              />
            ))}
          </Row>
        </div>
      </Container>
    )
  }
}

export default Home
