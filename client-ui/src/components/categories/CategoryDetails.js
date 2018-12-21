import React, { Component } from 'react'
import { http } from '../../configurations/axiosConf'
import { API } from '../../helpers/constants'
import AdventureItem from '../adventures/AdventureItem'
import { Row, message } from 'antd'
import Container from '../../Container'
import { strings } from '../../helpers/strings'

class CategoryDetails extends Component {
  constructor(props) {
    super(props)
    this.state = {
      category: [],
      adventures: [],
      adventuresImages: [],
    }
  }

  componentWillMount() {
    http
      .get(`${API.CATEGORIES}/getOne/${this.props.match.params.categoryId}`)
      .then(response => this.setState({ category: response.data }))
    http
      .get(`${API.ADVENTURES}/category/${this.props.match.params.categoryId}`)
      .then(response => {
        let adventuresResponse = response.data
        adventuresResponse.map(adventure =>
          http
            .get(`${API.IMAGES}${API.ADVENTURES}/${adventure.id}`)
            .then(response => {
              let { adventures } = this.state
              adventure.image = response.data[0]
              adventures.push(adventure)
              this.setState({ adventures: adventures })
            })
            .catch(() =>
              message.error(strings.statusCode.gettingAdventureError)
            )
        )
      })
  }

  render() {
    const { adventures } = this.state
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
              {adventures.map(adventure => (
                <AdventureItem
                  key={adventure.id}
                  index={adventure.id}
                  imagePath={
                    adventure.image
                      ? `/images/adventures/${adventure.image.uri}`
                      : '/images/background.jpg'
                  }
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
