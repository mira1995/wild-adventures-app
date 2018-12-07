import React, { Component } from 'react'
import { Link } from 'react-router-dom'
import { connect } from 'react-redux'
import { Menu, Icon, Button, Row, Col } from 'antd'
import moment from 'moment'
import { TOGGLE_AUTH, TOGGLE_MENU } from '../store/actions/types'
import { BEARER_TOKEN, URI, MENU, LANGUAGE } from '../helpers/constants'
import { strings, languagesList } from '../helpers/strings'

class Header extends Component {
  constructor(props) {
    super(props)
    this.state = {
      language: null,
    }
  }

  componentWillMount() {
    const language = this.props.cookies.get(LANGUAGE) || strings.getLanguage()
    strings.setLanguage(language)
    this.setState({ language })
  }

  handleMenu = event => {
    console.log(event, 'click')
    if (event.key === URI.LOGOUT) this.toggleAction(TOGGLE_MENU, URI.HOME)
    else if (event.key.includes(MENU.LANGUAGE)) {
      const language = event.key.substring(MENU.LANGUAGE.length + 1)
      strings.setLanguage(language)
      this.setState({ language })
      this.props.cookies.set(LANGUAGE, language, {
        path: '/',
        expires: moment()
          .add(1, 'years')
          .toDate(),
      })
    } else this.toggleAction(TOGGLE_MENU, URI.HOME + event.key)
  }

  handleToken = () => {
    this.props.cookies.remove(BEARER_TOKEN)
    this.toggleAction(TOGGLE_AUTH, null)
  }

  toggleAction(type, value) {
    const action = { type, value }
    this.props.dispatch(action)
  }

  render() {
    const MenuItem = Menu.Item
    const SubMenu = Menu.SubMenu
    const auth = this.props.token
    const { buyingBox } = this.props.buyingBox
    const currentLanguage = this.state.language

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
            {strings.routes.wildAdventures}
          </Link>
        </MenuItem>
        <MenuItem style={{ float: 'left' }} key={MENU.CATEGORIES}>
          <Link to={URI.CATEGORIES}>
            <Icon type="heat-map" />
            {strings.routes.categories}
          </Link>
        </MenuItem>
        {auth ? (
          <MenuItem style={{ float: 'left' }} key={MENU.ACCOUNT}>
            <Link to={URI.ACCOUNT}>
              <Icon type="user" />
              {strings.routes.account}
            </Link>
          </MenuItem>
        ) : (
          <MenuItem style={{ float: 'left' }} key={MENU.REGISTER}>
            <Link to={URI.REGISTER}>
              <Icon type="user-add" />
              {strings.routes.register}
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
        {auth ? (
          <MenuItem style={{ float: 'left' }} key={MENU.LOGOUT}>
            <Link to={URI.LOGOUT} onClick={this.handleToken}>
              <Icon type="logout" />
              {strings.routes.logout}
            </Link>
          </MenuItem>
        ) : (
          <MenuItem style={{ float: 'left' }} key={MENU.LOGIN}>
            <Link to={URI.LOGIN}>
              <Icon type="login" />
              {strings.routes.login}
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
        <SubMenu title={<Icon type="flag" />}>
          {strings.getAvailableLanguages().map(language => (
            <MenuItem key={`${MENU.LANGUAGE}:${language}`}>
              {currentLanguage === language ? (
                <b>{languagesList[language]}</b>
              ) : (
                languagesList[language]
              )}
            </MenuItem>
          ))}
        </SubMenu>
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
