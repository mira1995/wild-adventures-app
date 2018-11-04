import React, { Component } from 'react'
import { http } from './../../configurations/axiosConf'
import { API, URI } from '../../helpers/constants'
import { Link } from 'react-router-dom'
import { Row } from 'antd'
import CommentItem from '../comments/CommentItem'

class AdventureDetails extends Component {
  constructor(props) {
    super(props)
    this.state = {
      adventure: [],
      categories: [],
      comments: [],
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
    http
      .get(`${API.COMMENTS}/${this.props.match.params.adventureId}`)
      .then(response => {
        this.setState({ comments: response.data })
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
          <ul>
            {this.state.categories.map(category => (
              <Link key={category.id} to={`${URI.CATEGORIES}/${category.id}`}>
                <li>{category.title}</li>
              </Link>
            ))}
          </ul>
        </div>
        <div>
          <h2>Commentaires</h2>
          <Row>
            {this.state.comments.map(comment => (
              <CommentItem
                key={comment.id}
                content={comment.content}
                userId={comment.userId}
                comments={comment.comments}
              />
            ))}
          </Row>
        </div>
      </div>
    )
  }
}

export default AdventureDetails
