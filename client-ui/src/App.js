import React, { Component } from 'react'
import { Redirect, Route, Switch, withRouter } from 'react-router-dom'
import { connect } from 'react-redux'
import jwt from 'jsonwebtoken'
import moment from 'moment'
import { withCookies } from 'react-cookie'
import './App.css'
import Header from './components/Header'
import Home from './components/Home'
import Categories from './components/categories/Categories'
import CategoryDetails from './components/categories/CategoryDetails'
import Adventures from './components/adventures/Adventures'
import Account from './components/user/Account'
import Register from './components/user/Register'
import Login from './components/user/Login'
import OrderForm from './components/orders/OrderForm'
import NoMatch from './components/NoMatch'
import { TOGGLE_AUTH, TOGGLE_MENU } from './store/actions/types'
import { BEARER_TOKEN, URI } from './helpers/constants'
import AdventureDetails from './components/adventures/AdventureDetails'

class App extends Component {
  constructor(props) {
    super(props)
    this.state = { path: this.props.location.pathname }
  }

  componentWillMount() {
    const tokenSession = sessionStorage.getItem(BEARER_TOKEN)
    const tokenCookie = this.props.cookies.get(BEARER_TOKEN)
    let token

    if (!tokenSession) {
      if (tokenCookie) {
        // Si cookie, vérifier token cookie puis créer session
        token = this.tokenIsFine(tokenCookie)
        if (token) {
          sessionStorage.setItem(BEARER_TOKEN, token)
          this.toggleAction(TOGGLE_AUTH, token)
        }
      }
    } else {
      // Si session, vérifier token session
      token = this.tokenIsFine(tokenSession)
    }

    //const token = this.tokenIsFine(this.state.token)
    const { path } = this.state
    console.log(path, 'path')

    this.toggleAction(TOGGLE_AUTH, token)
    this.toggleAction(TOGGLE_MENU, path)

    if (token && path === URI.LOGIN) this.toggleAction(TOGGLE_MENU, URI.HOME)
    if (!token && path === URI.ACCOUNT)
      this.toggleAction(TOGGLE_MENU, URI.LOGIN)
  }

  // Delete token in sessionStorage if exp is exceeded
  tokenIsFine(token) {
    if (token) {
      const decoded = jwt.decode(token.substring(7))
      const format = 'DD MMMM YYYY hh:mm:ss'
      const now = moment()
      const iat = moment.unix(decoded.iat)
      const exp = moment.unix(decoded.exp)
      console.log(decoded, 'decoded')
      console.log(iat.format(format), 'iat')
      console.log(exp.format(format), 'exp')
      console.log(exp.diff(now), 'diff')
      if (exp.diff(now) <= 0) {
        token = null
        this.props.cookies.remove(BEARER_TOKEN)
        sessionStorage.clear()
      }
    }

    return token
  }

  toggleAction(type, value) {
    const action = { type, value }
    this.props.dispatch(action)
  }

  render() {
    const { cookies } = this.props

    return (
      <div>
        <Header cookies={cookies} />

        <Switch>
          <Route exact path={URI.HOME} component={Home} />
          <Route exact path={URI.CATEGORIES} component={Categories} />
          <Route
            path={`${URI.CATEGORIES}/:categoryId`}
            component={CategoryDetails}
          />
          <Route exact path={URI.ADVENTURES} component={Adventures} />
          <Route
            path={`${URI.ADVENTURES}/:adventureId`}
            component={AdventureDetails}
          />
          <Route path={`${URI.ORDER}/:sessionId`} component={OrderForm} />
          <Route path={URI.ACCOUNT} component={Account} />
          <Route path={URI.REGISTER} component={Register} />
          <Route path={URI.LOGOUT} render={() => <Redirect to={URI.HOME} />} />
          <Route path={URI.LOGIN} render={() => <Login cookies={cookies} />} />
          <Route component={NoMatch} />
        </Switch>
      </div>
    )
  }
}

const mapStateToProps = state => {
  return {
    token: state.authentication.token, // Needed to refresh App component when token update in Login and Header components
  }
}

export default withCookies(withRouter(connect(mapStateToProps)(App)))
