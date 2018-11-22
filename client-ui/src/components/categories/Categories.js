import React, { Component } from 'react'
import { Row } from 'antd'
import { http } from '../../configurations/axiosConf'
import { API } from '../../helpers/constants'
import CategoryItem from './CategoryItem'

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

  render() {
    return (
      <div>
        <h2>
          Liste des catégories <small>Partez à l'aventure</small>
        </h2>
        <Row type="flex" justify="center" align="middle">
          {this.state.categories.map(category => (
            <CategoryItem
              key={category.id}
              index={category.id}
              imagePath="https://www.riu.com/fr/binaris/new-slide-destino-paradise-island_tcm57-138470.jpg"
              title={category.title}
              description={category.description}
            />
          ))}
        </Row>
      </div>
    )
  }
}

export default Categories
