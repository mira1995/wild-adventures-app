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
import { TOGGLE_AUTH } from './store/actions/types'

class App extends Component {
  toggleAuthentication() {
    const action = {
      type: TOGGLE_AUTH,
      value: sessionStorage.getItem('bearerToken'),
    }
    this.props.dispatch(action)
  }

  componentDidMount() {
    this.toggleAuthentication()
  }

  render() {
    const auth = this.props.token
    const path = this.props.location.pathname

    return (
      <div>
        <Header path={path} />

        <Switch>
          <Route exact path="/" component={Home} />
          <Route path="/adventures" component={Adventures} />
          {auth ? (
            <Redirect from="/login" to="/" />
          ) : (
            <Route path="/login" component={Login} />
          )}
          {auth ? (
            <Route path="/account" component={Account} />
          ) : (
            <Redirect from="/account" to="/login" />
          )}
          <Route path="/logout" render={() => <Redirect to="/" />} />
          <Route component={NoMatch} />
        </Switch>
      </div>
    )
  }
}

const mapStateToProps = state => {
  return {
    token: state.token,
  }
}

export default withRouter(connect(mapStateToProps)(App))
