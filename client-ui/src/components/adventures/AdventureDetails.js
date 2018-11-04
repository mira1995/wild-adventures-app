import React, { Component } from 'react'
import { http } from './../../configurations/axiosConf'
import { API, URI } from '../../helpers/constants'
import { Link } from 'react-router-dom'

class AdventureDetails extends Component {
  constructor(props) {
    super(props)
    this.state = {
      adventure: [],
      categories: [],
    }
  }

  componentWillMount = () => {
    http
      .get(`${API.ADVENTURES}/${this.props.match.params.adventureId}`)
      .then(response => {
        this.setState({ adventure: response.data })
      })
    http
      .get(`${API.CATEGORIES}/adventure/${this.props.match.params.adventureId}`)
      .then(response => {
        this.setState({ categories: response.data })
      })
  }

  render() {
    return (
      <div>
        <h1>{this.state.adventure.title}</h1>
        <div>
          <p>{this.state.adventure.description}</p>
          <p>Localisation : {this.state.adventure.location}</p>
          <h2>Liste des catégories de l'aventure : </h2>
          <p>
            <ul>
              {this.state.categories.map(category => (
                <Link to={`${URI.CATEGORIES}/${category.id}`}>
                  <li>{category.title}</li>
                </Link>
              ))}
            </ul>
          </p>
        </div>
      </div>
    )
  }
}

export default AdventureDetails
