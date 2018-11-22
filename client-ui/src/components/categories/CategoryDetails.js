import React, { Component } from 'react'
import { http } from '../../configurations/axiosConf'
import { API } from '../../helpers/constants'
import AdventureItem from '../adventures/AdventureItem'
import { Row, Col } from 'antd'

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
      .get(`${API.CATEGORIES}/${this.props.match.params.categoryId}`)
      .then(response => this.setState({ category: response.data }))
    http
      .get(`${API.ADVENTURES}/category/${this.props.match.params.categoryId}`)
      .then(response => this.setState({ adventures: response.data }))
  }

  render() {
    return (
      <Row type="flex" justify="center" align="top">
        <Col lg={15} md={18} sm={21} xs={22} className="customContainer">
          <div>
            <h1>{this.state.category.title}</h1>
            <div>
              <p>{this.state.category.description}</p>
            </div>
            <h2>Liste des aventures</h2>
            <div>
              <Row type="flex" align="center">
                {this.state.adventures.map(adventure => (
                  <AdventureItem
                    key={adventure.id}
                    index={adventure.id}
                    imagePath="https://www.riu.com/fr/binaris/new-slide-destino-paradise-island_tcm57-138470.jpg"
                    adventure={adventure}
                  />
                ))}
              </Row>
            </div>
          </div>
        </Col>
      </Row>
    )
  }
}

export default CategoryDetails
