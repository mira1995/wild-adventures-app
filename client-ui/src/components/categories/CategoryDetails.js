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
        let adventures = response.data
        adventures.map(adventure =>
          http
            .get(`${API.IMAGES}${API.ADVENTURES}/${adventure.id}`)
            .then(response => {
              let { adventuresImages } = this.state
              adventuresImages.push(response.data)
              this.setState({ adventuresImages: adventuresImages })
            })
            .catch(() =>
              message.error(strings.statusCode.gettingAdventureError)
            )
        )
        this.setState({ adventures: response.data })
      })
  }

  render() {
    const { adventures, adventuresImages } = this.state
    console.log(adventures)
    if (adventures && adventuresImages) {
      adventures.map((adventure, index) => {
        if (adventuresImages[index]) {
          adventure.image = adventuresImages[index][0]
        }
      })
    }
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
