import React, { Component } from 'react'
import { http } from '../../configurations/axiosConf'
import { API } from '../../helpers/constants'
import AdventureItem from '../adventures/AdventureItem'

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
      .get(`${API.CATEGORIES}/${this.props.match.params.id}`)
      .then(response => {
        const category = response.data
        this.setState({ category })
      })
    http
      .get(`${API.ADVENTURES}/${this.props.match.params.id}`)
      .then(response => {
        const adventures = response.data
        this.setState({ adventures })
      })
  }

  render() {
    return (
      <div>
        <h1>{this.state.category.title}</h1>
        <div>
          <p>{this.state.category.description}</p>
        </div>
        <h2>Liste des aventures</h2>
        <div>
          {this.state.adventures.map(adventure => (
            <AdventureItem
              key={adventure.id}
              index={adventure.id}
              imagePath="https://i.kym-cdn.com/photos/images/original/001/384/541/1d8.jpg"
              adventure={adventure}
            />
          ))}
        </div>
      </div>
    )
  }
}

export default CategoryDetails
