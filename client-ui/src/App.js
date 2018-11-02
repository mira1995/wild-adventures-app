import React, { Component } from 'react'
import { Redirect, Route, Switch, withRouter } from 'react-router-dom'
import { connect } from 'react-redux'
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
          <Route path={URI.CATEGORIES} component={Categories} />
          <Route path={`/category/:categoryId`} component={CategoryDetails} />
          <Route path={URI.ADVENTURES} component={Adventures} />
          <Route path={URI.ACCOUNT} component={Account} />
          <Route path={URI.REGISTER} component={Register} />
          <Route path={URI.LOGOUT} render={() => <Redirect to={URI.HOME} />} />
          <Route path={URI.LOGIN} component={Login} />
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
