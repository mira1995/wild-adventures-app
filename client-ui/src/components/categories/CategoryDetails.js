import React, { Component } from 'react'
import { http } from '../../configurations/axiosConf'
import { API } from '../../helpers/constants'
import AdventureItem from '../adventures/AdventureItem'
import { Row } from 'antd'
import Container from '../../Container'
import { strings } from '../../helpers/strings'

class CategoryDetails extends Component {
  constructor(props) {
    super(props)
    this.state = {
      category: [],
      adventures: [],
    }
  }

  componentWillMount() {
    http
      .get(`${API.CATEGORIES}/getOne/${this.props.match.params.categoryId}`)
      .then(response => this.setState({ category: response.data }))
    http
      .get(`${API.ADVENTURES}/category/${this.props.match.params.categoryId}`)
      .then(response => this.setState({ adventures: response.data }))
  }

  render() {
    return (
      <Container>
        <div>
          <h1>{this.state.category.title}</h1>
          <div>
            <p>{this.state.category.description}</p>
          </div>
          <h2>{strings.categories.adventuresList}</h2>
          <div>
            <Row type="flex" align="center">
              {this.state.adventures.map(adventure => (
                <AdventureItem
                  key={adventure.id}
                  index={adventure.id}
                  imagePath="/images/background.jpg"
                  adventure={adventure}
                />
              ))}
            </Row>
          </div>
        </div>
      </Container>
    )
  }
}

export default CategoryDetails
