import React, { Component } from 'react'
import { Redirect } from 'react-router-dom'
import { connect } from 'react-redux'
import jwt from 'jsonwebtoken'
import moment from 'moment'
import { http } from '../../configurations/axiosConf'
import { URI, API, BEARER_TOKEN } from '../../helpers/constants'
import { TOGGLE_AUTH, TOGGLE_MENU } from '../../store/actions/types'
import WrappedEmailForm from './forms/EmailForm'
import WrappedPseudoForm from './forms/PseudoForm'
import WrappedPasswordForm from './forms/PasswordForm'
import WrappedAddressForm from './forms/AddressForm'
import WrappedDeactivateForm from './forms/DeactivateForm'
import WrappedProfileImageForm from './forms/ProfileImageForm'

class Account extends Component {
  constructor(props) {
    super(props)
    this.state = { userAccount: null }
  }

  componentWillMount() {
    const { token } = this.props
    if (!token) return <Redirect to={URI.LOGIN} />
    const decoded = jwt.decode(token.substring(7))

    http
      .post(`${API.USERS}/email`, decoded.sub)
      .then(response => {
        const { ...userAccount } = response.data
        console.log(userAccount)
        this.setState({ userAccount })
      })
      .catch(error => console.log(error))
  }

  // Use fx arrow to bind this
  handleUser = partialUser => {
    const { userAccount } = this.state
    const {
      confirmPseudoWithPassword,
      confirmNew,
      confirmEmailWithPassword,
      ...updatableUser
    } = { ...userAccount, ...partialUser }

    // If pseudo, password or email update : refresh token but user don't care
    this.updateUser(
      updatableUser,
      {
        username: updatableUser.email,
        password:
          confirmPseudoWithPassword || confirmNew || confirmEmailWithPassword,
      },
      partialUser.pseudo || partialUser.password || partialUser.email
        ? true
        : false
    )
  }

  updateUser(updatableUser, credentials, refreshToken) {
    http
      .patch(API.USERS, updatableUser)
      .then(response => {
        // console.log(response.data)
        const { active } = response.data
        if (!active) {
          this.props.cookies.remove(BEARER_TOKEN)
          this.toggleAction(TOGGLE_AUTH, null)
          this.toggleAction(TOGGLE_MENU, URI.LOGIN)
        } else {
          this.setState({ userAccount: updatableUser })
          if (refreshToken) this.refreshToken(credentials)
        }
      })
      .catch(error => console.log('error', error))
  }

  refreshToken(credentials) {
    http
      .post(API.AUTH, credentials)
      .then(response => {
        const bearerToken = response.headers.authorization
        console.log(bearerToken)
        const { cookies } = this.props
        const exp = moment().add(15, 'minutes')
        cookies.set(BEARER_TOKEN, bearerToken, {
          path: '/',
          expires: exp.toDate(),
        })
        this.toggleAction(TOGGLE_AUTH, cookies.get(BEARER_TOKEN))
      })
      .catch(error => console.log('error', error))
  }

  toggleAction(type, value) {
    const action = { type, value }
    this.props.dispatch(action)
  }

  render() {
    if (!this.props.token) return <Redirect to={URI.LOGIN} />

    const { userAccount } = this.state
    if (!userAccount) return null
    else
      return (
        <>
          <WrappedProfileImageForm
            user={userAccount}
            action={this.handleUser}
          />
          <WrappedEmailForm user={userAccount} action={this.handleUser} />
          <WrappedPseudoForm user={userAccount} action={this.handleUser} />
          <WrappedPasswordForm user={userAccount} action={this.handleUser} />
          <WrappedAddressForm user={userAccount} action={this.handleUser} />
          <WrappedDeactivateForm user={userAccount} action={this.handleUser} />
        </>
      )
  }
}

const mapStateToProps = state => {
  return {
    token: state.authentication.token,
  }
}

export default connect(mapStateToProps)(Account)
