import React, { Component } from 'react'
import { Link } from 'react-router-dom'
import { connect } from 'react-redux'
import { Menu, Icon } from 'antd'
import { TOGGLE_AUTH } from '../store/actions/types'

class Header extends Component {
  constructor(props) {
    super(props)
    this.state = {
      current: this.props.path,
    }
  }

  handleMenu = event => {
    console.log('click ', event)
    this.setState({
      current: event.key,
    })
  }

  handleTokenSession = () => {
    sessionStorage.clear()
    this.toggleAuthentication()
  }

  toggleAuthentication() {
    const action = {
      type: TOGGLE_AUTH,
      value: null,
    }
    this.props.dispatch(action)
  }

  render() {
    const MenuItem = Menu.Item
    const auth = this.props.token

    return (
      <Menu
        onClick={this.handleMenu}
        selectedKeys={[this.state.current]}
        mode="horizontal"
        theme="dark"
      >
        <MenuItem key="/">
          <Link to="/">
            <Icon type="home" />
            Wild Adventures
          </Link>
        </MenuItem>
        <MenuItem key="/adventures">
          <Link to="/adventures">
            <Icon type="heat-map" />
            Adventures
          </Link>
        </MenuItem>
        {auth ? (
          <MenuItem key="/account">
            <Link to="/account">
              <Icon type="user" />
              Account
            </Link>
          </MenuItem>
        ) : (
          <MenuItem key="/register">
            <Link to="/register">
              <Icon type="user-add" />
              Register
            </Link>
          </MenuItem>
        )}
        {auth ? (
          <MenuItem key="/logout">
            <Link to="/logout" onClick={this.handleTokenSession}>
              <Icon type="logout" />
              Logout
            </Link>
          </MenuItem>
        ) : (
          <MenuItem key="/login">
            <Link to="/login">
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
    token: state.token,
  }
}

export default connect(mapStateToProps)(Header)
