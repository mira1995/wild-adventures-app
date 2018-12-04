import React, { Component } from 'react'
import { Link } from 'react-router-dom'
import { connect } from 'react-redux'
import { Menu, Icon, Button, Row, Col } from 'antd'
import { TOGGLE_AUTH, TOGGLE_MENU } from '../store/actions/types'
import { BEARER_TOKEN, URI, MENU } from '../helpers/constants'

class Header extends Component {
  handleMenu = event => {
    console.log(event, 'click')
    if (event.key === URI.LOGOUT) this.toggleAction(TOGGLE_MENU, URI.HOME)
    else this.toggleAction(TOGGLE_MENU, URI.HOME + event.key)
  }

  handleToken = () => {
    this.props.cookies.remove(BEARER_TOKEN)
    sessionStorage.clear()
    this.toggleAction(TOGGLE_AUTH, null)
  }

  toggleAction(type, value) {
    const action = { type, value }
    this.props.dispatch(action)
  }

  componentDidUpdate = (prevProps, prevState) => {
    console.log(this.props.buyingBox)
  }

  render() {
    const MenuItem = Menu.Item
    const SubMenu = Menu.SubMenu
    const auth = this.props.token
    const { buyingBox } = this.props.buyingBox
    console.log(buyingBox)
    return (
      <Menu
        onClick={this.handleMenu}
        selectedKeys={[this.props.menuKey]}
        mode="horizontal"
        theme="dark"
      >
        <MenuItem style={{ float: 'left' }} key={MENU.HOME}>
          <Link to={URI.HOME}>
            <Icon type="home" />
            Wild Adventures
          </Link>
        </MenuItem>
        <MenuItem style={{ float: 'left' }} key={MENU.CATEGORIES}>
          <Link to={URI.CATEGORIES}>
            <Icon type="heat-map" />
            Categories
          </Link>
        </MenuItem>
        <MenuItem style={{ float: 'left' }} key={MENU.ADVENTURES}>
          <Link to={URI.ADVENTURES}>
            <Icon type="heat-map" />
            Adventures
          </Link>
        </MenuItem>
        {auth ? (
          <MenuItem style={{ float: 'left' }} key={MENU.ACCOUNT}>
            <Link to={URI.ACCOUNT}>
              <Icon type="user" />
              Account
            </Link>
          </MenuItem>
        ) : (
          <MenuItem style={{ float: 'left' }} key={MENU.REGISTER}>
            <Link to={URI.REGISTER}>
              <Icon type="user-add" />
              Register
            </Link>
          </MenuItem>
        )}
        {auth ? (
          <MenuItem style={{ float: 'left' }} key={MENU.LOGOUT}>
            <Link to={URI.LOGOUT} onClick={this.handleToken}>
              <Icon type="logout" />
              Logout
            </Link>
          </MenuItem>
        ) : (
          <MenuItem style={{ float: 'left' }} key={MENU.LOGIN}>
            <Link to={URI.LOGIN}>
              <Icon type="login" />
              Login
            </Link>
          </MenuItem>
        )}
        {auth && (
          <MenuItem style={{ float: 'left' }} key={MENU.MYORDERS}>
            <Link to={URI.MYORDERS}>
              <Icon type="barcode" />
              Mes commandes
            </Link>
          </MenuItem>
        )}
        {auth && (
          <SubMenu
            style={{ float: 'right' }}
            title={
              <span className="submenu-title-wrapper">
                <Icon type="shopping-cart" />
                <span>
                  Mon panier&nbsp;
                  {buyingBox ? buyingBox.length : 0}
                </span>
              </span>
            }
          >
            {buyingBox.length > 0 &&
              buyingBox.map(item => (
                <MenuItem>
                  {item.adventureName} du {item.startDate} au {item.endDate}
                </MenuItem>
              ))}
            {buyingBox.length > 0 && (
              <MenuItem>
                <Row>
                  <Col span={12} offset={8}>
                    <Link to={`${URI.ORDER}`}>
                      <Button type="primary">Commander</Button>
                    </Link>
                  </Col>
                </Row>
              </MenuItem>
            )}
          </SubMenu>
        )}
      </Menu>
    )
  }
}

const mapStateToProps = state => {
  return {
    token: state.authentication.token,
    menuKey: state.menu.current,
    buyingBox: state.buyingBox,
  }
}

export default connect(mapStateToProps)(Header)
