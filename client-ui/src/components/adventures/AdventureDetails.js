import React, { Component } from 'react'
import { http } from './../../configurations/axiosConf'
import { API } from '../../helpers/constants'

class AdventureDetails extends Component {
  constructor(props) {
    super(props)
    this.state = {
      adventure: [],
    }
  }

  componentWillMount = () => {
    http
      .get(`${API.ADVENTURES}/${this.props.match.params.adventureId}`)
      .then(response => {
        this.setState({ adventure: response.data })
      })
  }

  render() {
    return (
      <div>
        <h1>{this.state.adventure.title}</h1>
        <div>
          <p>{this.state.adventure.description}</p>
          <p>Localisation : {this.state.adventure.location}</p>
        </div>
      </div>
    )
  }
}

export default AdventureDetails
