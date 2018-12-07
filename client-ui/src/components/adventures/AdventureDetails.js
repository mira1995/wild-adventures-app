import React, { Component } from 'react'
import { http } from './../../configurations/axiosConf'
import { API, URI } from '../../helpers/constants'
import { Link } from 'react-router-dom'
import { Row, Table, Button } from 'antd'
import CommentItem from '../comments/CommentItem'
import CommentForm from './../comments/CommentForm'
import { BEARER_TOKEN } from './../../helpers/constants'
import Container from './../../Container'
import moment from 'moment'
import { connect } from 'react-redux'
import { withRouter } from 'react-router-dom'

import { strings } from '../../helpers/strings'

class AdventureDetails extends Component {
  constructor(props) {
    super(props)
    this.handleSubmit = this.handleSubmit.bind(this)
    this.state = {
      adventure: [],
      categories: [],
      sessions: [],
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
    http
      .get(`${API.SESSIONS}/${this.props.match.params.adventureId}`)
      .then(response => {
        this.setState({ sessions: response.data })
      })
  }

  checkIfAnonymous() {
    return !this.props.cookies.get(BEARER_TOKEN)
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

  handleReservationClick = record => {
    const { cookies } = this.props
    let buyingBox = cookies.get('buyingBox')
    if (cookies.get('buyingBox')) {
      buyingBox = buyingBox.filter(function(item) {
        return (
          item.record.adventureId !== record.adventureId &&
          item.record.startDate !== record.startDate &&
          item.record.endDate !== record.endDate
        )
      })
      record.adventureName = this.state.adventure.title
      console.log(record)
      buyingBox.push({ record })
      cookies.set('buyingBox', buyingBox)
    } else {
      buyingBox = []
      record.adventureName = this.state.adventure.title
      console.log(record)
      buyingBox.push({ record })
      cookies.set('buyingBox', buyingBox)
    }
    this._toggleBuyingBox(record)
  }

  _toggleBuyingBox(record) {
    const action = { type: 'TOGGLE_BUYINGBOX', value: record }
    this.props.dispatch(action)
  }

  formatSessionsDates = sessions => {
    sessions.map(session => this.formatEndAndStartDate(session))
  }

  formatEndAndStartDate = session => {
    /* const format = 'L' */
    const format = 'L'
    session.startDate = moment(session.startDate).format(format)
    session.endDate = moment(session.endDate).format(format)
  }

  isInBuyingBox = record => {
    const { buyingBox } = this.props.buyingBox
    const sessionIndex = buyingBox.findIndex(
      item =>
        item.adventureId === record.adventureId &&
        item.startDate === record.startDate &&
        item.endDate === record.endDate
    )
    console.log(sessionIndex)
    return sessionIndex !== -1
  }

  render() {
    const { adventure, sessions } = this.state
    console.log(adventure)
    this.formatSessionsDates(sessions)

    const columns = [
      {
        title: 'Du',
        dataIndex: 'startDate',
        key: 'startDate',
      },
      {
        title: 'Au',
        dataIndex: 'endDate',
        key: 'endDate',
      },
      {
        title: 'Prix',
        dataIndex: 'price',
        key: 'price',
      },
      {
        title: 'Action',
        key: 'action',
        render: (text, record) => (
          <div>
            {!this.state.isAnonymous &&
              !this.isInBuyingBox(record) && <Button>Réserver</Button>}
            {!this.state.isAnonymous &&
              this.isInBuyingBox(record) && (
                <Button type="danger">Supprimer du panier</Button>
              )}
            {this.state.isAnonymous && (
              <p>
                Pour réserver <Link to={URI.REGISTER}>s'inscrire</Link> ou&nbsp;
                <Link to={URI.LOGIN}> se connecter</Link>
              </p>
            )}
          </div>
        ),
      },
    ]

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
            <h2>Réserver cette aventure</h2>
            {sessions.length > 0 && (
              <Table
                onRow={record => {
                  return {
                    onClick: () => {
                      this.handleReservationClick(record)
                    },
                  }
                }}
                columns={columns}
                dataSource={sessions}
              />
            )}
            {!sessions.length > 0 && (
              <p>Aucune session de programmée pour cette aventure</p>
            )}
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

const mapStateToProps = state => {
  return {
    buyingBox: state.buyingBox,
  }
}

export default connect(mapStateToProps)(withRouter(AdventureDetails))
