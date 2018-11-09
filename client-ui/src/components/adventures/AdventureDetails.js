import React, { Component } from 'react'
import { http } from './../../configurations/axiosConf'
import { API, URI } from '../../helpers/constants'
import { Link } from 'react-router-dom'
import { Row } from 'antd'
import CommentItem from '../comments/CommentItem'
import CommentForm from './../comments/CommentForm'
import { BEARER_TOKEN } from './../../helpers/constants'
import { withRouter } from 'react-router-dom'

class AdventureDetails extends Component {
  constructor(props) {
    super(props)
    this.handleSubmit = this.handleSubmit.bind(this)
    this.state = {
      adventure: [],
      categories: [],
      comments: [],
      isAnonymous: this.checkIfAnonymous(),
    }
  }

  componentWillMount = () => {
    this.getDatas()
  }

  componountDidUpdate = () => {
    this.getDatas()
  }

  getDatas() {
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

  checkIfAnonymous() {
    return sessionStorage.getItem(BEARER_TOKEN) === null
  }

  handleSubmit(adventureId) {
    window.location.reload()
  }

  render() {
    console.log(this.state.isAnonymous)
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
          {!this.state.isAnonymous && (
            <Row>
              <CommentForm
                adventureId={this.props.match.params.adventureId}
                action={this.handleSubmit}
              />
            </Row>
          )}
        </div>
      </div>
    )
  }
}

export default withRouter(AdventureDetails)
