import React, { Component } from 'react'
import { http } from './../../configurations/axiosConf'
import { API, ORDERSTATUS } from '../../helpers/constants'
import { connect } from 'react-redux'
import jwt from 'jsonwebtoken'
import Container from '../../Container'
import { Table, Button, message } from 'antd'
import { Link } from 'react-router-dom'
import { URI } from './../../helpers/constants'
import { strings } from '../../helpers/strings'
import moment from 'moment'

class MyOrders extends Component {
  constructor(props) {
    super(props)
    this.state = {
      userAccount: null,
      orders: [],
    }
  }

  componentWillMount() {
    const token = this.props.token.substring(7)
    const decoded = jwt.decode(token)
    http
      .post(`${API.USERS}/email`, decoded.sub)
      .then(response => {
        const { password, ...userAccount } = response.data
        this.setState({ userAccount })
        http
          .get(`${API.ORDERS}/user/${userAccount.id}`)
          .then(response => this.setState({ orders: response.data }))
      })
      .catch(() => message.error(strings.statusCode.userInformations))
  }

  actionColumnRender = record => {
    const buttonStyle = {
      marginRight: '10px',
    }
    switch (record.status) {
      case ORDERSTATUS.NOT_PAID:
        return (
          <div>
            <Link to={`${URI.PAYMENT}/${record.id}`}>
              <Button style={buttonStyle} type="danger">
                {strings.orders.pay}
              </Button>
            </Link>
            <Link to={`${URI.MYORDERS}${URI.CANCELDEMAND}/${record.id}`}>
              <Button type="danger">{strings.orders.cancel}</Button>
            </Link>
          </div>
        )
      case ORDERSTATUS.FINALIZED:
        return (
          <div>
            <Link to={`${URI.MYORDERS}${URI.UPDATEDEMAND}/${record.id}`}>
              <Button style={buttonStyle} type="primary">
                {strings.orders.edit}
              </Button>
            </Link>
            <Link to={`${URI.MYORDERS}${URI.CANCELDEMAND}/${record.id}`}>
              <Button type="danger">{strings.orders.cancel}</Button>
            </Link>
          </div>
        )
      case ORDERSTATUS.DELETE_DEMAND:
        return <div>{strings.orders.requestDeletionTreatment}</div>
      case ORDERSTATUS.UPDATE_DEMAND:
        return <div>{strings.orders.requestUpdateProcessed}</div>
      default:
        return <div>{strings.orders.noActionAvailable}</div>
    }
  }

  orderDateColumnRender = record => {
    const format = 'DD-MM-YYYY'
    const orderDate = moment(record.orderDate).format(format)
    return <div>{orderDate}</div>
  }

  statusColumnRender = record => {
    switch (record.status) {
      case ORDERSTATUS.NOT_PAID:
        return <div>{strings.orders.unpaid}</div>
      case ORDERSTATUS.FINALIZED:
        return <div>{strings.orders.finalized}</div>
      case ORDERSTATUS.DELETE_DEMAND:
        return <div>{strings.orders.deletionRequest}</div>
      case ORDERSTATUS.UPDATE_DEMAND:
        return <div>{strings.orders.updateRequest}</div>
      default:
        return <div>{strings.orders.unrecognizedStatus}</div>
    }
  }

  render() {
    const columns = [
      {
        title: strings.orders.orderDate,
        rowKey: 'orderDate',
        render: (text, record) => (
          <div>{this.orderDateColumnRender(record)}</div>
        ),
      },
      {
        title: strings.orders.status,
        rowKey: 'status',
        render: (text, record) => <div>{this.statusColumnRender(record)}</div>,
      },
      {
        title: strings.orders.action,
        rowKey: 'action',
        render: (text, record) => <div>{this.actionColumnRender(record)}</div>,
      },
    ]
    console.log(this.state)
    return (
      <Container>
        <h1>{strings.orders.ordersList}</h1>
        <div>
          {this.state.orders && this.state.orders.length > 0 ? (
            <Table
              columns={columns}
              dataSource={this.state.orders}
              rowKey="id"
            />
          ) : (
            <p>{strings.orders.emptyOrdersList}</p>
          )}
        </div>
      </Container>
    )
  }
}
const mapStateToProps = state => {
  return {
    token: state.authentication.token,
  }
}

export default connect(mapStateToProps)(MyOrders)
