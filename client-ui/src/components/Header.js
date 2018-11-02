import React, { Component } from 'react'
import { Link } from 'react-router-dom'
import { connect } from 'react-redux'
import { Menu, Icon } from 'antd'
import { TOGGLE_AUTH, TOGGLE_MENU } from '../store/actions/types'
import { URI, MENU } from '../helpers/constants'

class Header extends Component {
  handleMenu = event => {
    console.log(event, 'click')
    if (event.key === URI.LOGOUT) this.toggleAction(TOGGLE_MENU, URI.HOME)
    else this.toggleAction(TOGGLE_MENU, URI.HOME + event.key)
  }

  handleTokenSession = () => {
    sessionStorage.clear()
    this.toggleAction(TOGGLE_AUTH, null)
  }

  toggleAction(type, value) {
    const action = { type, value }
    this.props.dispatch(action)
  }

  render() {
    const MenuItem = Menu.Item
    const auth = this.props.token

    return (
      <Menu
        onClick={this.handleMenu}
        selectedKeys={[this.props.menuKey]}
        mode="horizontal"
        theme="dark"
      >
        <MenuItem key={MENU.HOME}>
          <Link to={URI.HOME}>
            <Icon type="home" />
            Wild Adventures
          </Link>
        </MenuItem>
        <MenuItem key={MENU.CATEGORIES}>
          <Link to={URI.CATEGORIES}>
            <Icon type="heat-map" />
            Categories
          </Link>
        </MenuItem>
        <MenuItem key={MENU.ADVENTURES}>
          <Link to={URI.ADVENTURES}>
            <Icon type="heat-map" />
            Adventures
          </Link>
        </MenuItem>
        {auth ? (
          <MenuItem key={MENU.ACCOUNT}>
            <Link to={URI.ACCOUNT}>
              <Icon type="user" />
              Account
            </Link>
          </MenuItem>
        ) : (
          <MenuItem key={MENU.REGISTER}>
            <Link to={URI.REGISTER}>
              <Icon type="user-add" />
              Register
            </Link>
          </MenuItem>
        )}
        {auth ? (
          <MenuItem key={MENU.LOGOUT}>
            <Link to={URI.LOGOUT} onClick={this.handleTokenSession}>
              <Icon type="logout" />
              Logout
            </Link>
          </MenuItem>
        ) : (
          <MenuItem key={MENU.LOGIN}>
            <Link to={URI.LOGIN}>
              <Icon type="login" />
              Login
            </Link>
          </MenuItem>
        )}
      </Menu>
    )
  }
}

const mapStateToProps = state => {
  return {
    token: state.authentication.token,
    menuKey: state.menu.current,
  }
}

export default connect(mapStateToProps)(Header)
