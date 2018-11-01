import React, { Component } from 'react'
import { Redirect, Route, Switch, withRouter } from 'react-router-dom'
import { connect } from 'react-redux'
import './App.css'
import Header from './components/Header'
import Home from './components/Home'
import Adventures from './components/Adventures'
import Account from './components/Account'
import NoMatch from './components/NoMatch'
import Login from './components/Login'
import { TOGGLE_AUTH, TOGGLE_MENU } from './store/actions/types'
import { BEARER_TOKEN, URI } from './helpers/constants'

class App extends Component {
  constructor(props) {
    super(props)
    this.state = {
      path: this.props.location.pathname,
      token: sessionStorage.getItem(BEARER_TOKEN),
    }
  }

  componentWillMount() {
    const { path, token } = this.state
    console.log(path, 'path')
    this.toggleAction(TOGGLE_AUTH, token)
    this.toggleAction(TOGGLE_MENU, path)

    if (token && path === URI.LOGIN) this.toggleAction(TOGGLE_MENU, URI.HOME)
    if (!token && path === URI.ACCOUNT)
      this.toggleAction(TOGGLE_MENU, URI.LOGIN)
  }

  toggleAction(type, value) {
    const action = { type, value }
    this.props.dispatch(action)
  }

  render() {
    return (
      <div>
        <Header />

        <Switch>
          <Route exact path={URI.HOME} component={Home} />
          <Route path={URI.ADVENTURES} component={Adventures} />
          <Route path={URI.LOGIN} component={Login} />
          <Route path={URI.ACCOUNT} component={Account} />
          <Route path={URI.LOGOUT} render={() => <Redirect to={URI.HOME} />} />
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

export default withRouter(connect(mapStateToProps)(App))
