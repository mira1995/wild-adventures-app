import React, { Component } from 'react'
import { Redirect } from 'react-router-dom'
import { connect } from 'react-redux'
import { URI } from '../../helpers/constants'

class Account extends Component {
  render() {
    if (this.props.token) return <Redirect to={URI.HOME} />
    return <h2>Register</h2>
  }
}

const mapStateToProps = state => {
  return {
    token: state.authentication.token,
  }
}

export default connect(mapStateToProps)(Account)
