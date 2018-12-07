import React, { Component } from 'react'
import { Redirect, Route, Switch, withRouter } from 'react-router-dom'
import { connect } from 'react-redux'
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
    const token = this.props.cookies.get(BEARER_TOKEN)

    const { path } = this.state
    console.log(path, 'path')

    this.toggleAction(TOGGLE_AUTH, token)
    this.toggleAction(TOGGLE_MENU, path)

    if (token && path === URI.LOGIN) this.toggleAction(TOGGLE_MENU, URI.HOME)
    if (!token && path === URI.ACCOUNT)
      this.toggleAction(TOGGLE_MENU, URI.LOGIN)
  }

  // Delete token in sessionStorage if exp is exceeded
  // tokenIsFine(token) {
  //   if (token) {
  //     const decoded = jwt.decode(token.substring(7))
  //     const format = 'DD MMMM YYYY hh:mm:ss'
  //     const now = moment()
  //     const exp = moment.unix(decoded.exp)
  //     console.log(decoded, 'decoded')
  //     console.log(exp.format(format), 'exp')
  //     console.log(exp.diff(now), 'diff')
  //     if (exp.diff(now) <= 0) {
  //       token = null
  //       this.props.cookies.remove(BEARER_TOKEN)
  //     }
  //   }

  //   return token
  // }

  toggleAction(type, value) {
    const action = { type, value }
    this.props.dispatch(action)
  }

  render() {
    const { cookies } = this.props

    return (
      <div>
        <Header cookies={cookies} />

        {window.location.pathname.includes('index.html') && <Redirect to="/" />}
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
            render={() => <AdventureDetails cookies={cookies} />}
          />
          <Route
            exact
            path={URI.ACCOUNT}
            render={() => <Account cookies={cookies} />}
          />
          <Route
            exact
            path={URI.REGISTER}
            render={() => <Register cookies={cookies} />}
          />
          <Route
            exact
            path={URI.LOGOUT}
            render={() => <Redirect to={URI.HOME} />}
          />
          <Route
            exact
            path={URI.LOGIN}
            render={() => <Login cookies={cookies} />}
          />
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
