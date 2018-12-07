import React, { Component } from 'react'
import { http } from './../../configurations/axiosConf'
import { API, URI } from '../../helpers/constants'
import { Link } from 'react-router-dom'
import { Row } from 'antd'
import CommentItem from '../comments/CommentItem'
import CommentForm from './../comments/CommentForm'
import { BEARER_TOKEN } from './../../helpers/constants'
import { withRouter } from 'react-router-dom'
import Container from './../../Container'
import { strings } from '../../helpers/strings'

class AdventureDetails extends Component {
  constructor(props) {
    super(props)
    this.handleSubmit = this.handleSubmit.bind(this)
    this.state = {
      adventure: [],
      categories: [],
      comments: [],
      isAnonymous: this.checkIfAnonymous(),
      activeComment: null,
    }
  }

  componentWillMount = () => {
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
    return this.props.cookies.get(BEARER_TOKEN) === null
  }

  // Use fx arrow to bind this
  handleSubmit = comment => {
    let { comments } = this.state
    if (typeof this.getIndex(comments, comment.id) !== 'undefined') {
      comments[this.getIndex(comments, comment.id)].comments = comment.comments
    } else {
      comments.push(comment)
    }
    this.setState({ comments })
    console.log(this.state.comments)
  }

  getIndex(comments, id) {
    for (var i = 0; i < comments.length; i++) {
      if (comments[i].id === id) {
        return i
      }
    }
  }

  // Use fx arrow to bind this
  handleAnswerClick = activeComment => {
    this.setState({ activeComment })
  }

  render() {
    const { adventure } = this.state
    console.log(adventure)

    return (
      <Container>
        <div>
          <h1>{adventure.title}</h1>
          <div>
            <p>{adventure.description}</p>
            <p>
              {strings.adventures.location} : {adventure.location}
            </p>
            <h2>{strings.adventures.categoriesListAdventure}</h2>
            <ul>
              {this.state.categories.map(category => (
                <Link key={category.id} to={`${URI.CATEGORIES}/${category.id}`}>
                  <li>{category.title}</li>
                </Link>
              ))}
            </ul>
          </div>
          <div>
            <h2>{strings.comments.commentsList}</h2>
            {!this.state.isAnonymous &&
              this.state.activeComment === null && (
                <Row>
                  <CommentForm
                    adventureId={adventure.id}
                    action={this.handleSubmit}
                  />
                </Row>
              )}
            <Row>
              {this.state.comments.map(comment => (
                <CommentItem
                  key={comment.id}
                  commentId={comment.id}
                  content={comment.content}
                  userId={comment.userId}
                  comments={comment.comments}
                  answerAction={this.handleAnswerClick}
                  isActive={this.state.activeComment === comment.id}
                >
                  {!this.state.isAnonymous &&
                    this.state.activeComment === comment.id && (
                      <CommentForm
                        adventureId={adventure.id}
                        action={this.handleSubmit}
                        parent={comment}
                      />
                    )}
                </CommentItem>
              ))}
            </Row>
          </div>
        </div>
      </Container>
    )
  }
}

export default withRouter(AdventureDetails)
