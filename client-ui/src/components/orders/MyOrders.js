import React, { Component } from 'react'
import { http } from './../../configurations/axiosConf'
import { API } from '../../helpers/constants'
import { connect } from 'react-redux'
import jwt from 'jsonwebtoken'
import Container from '../../Container'
import { Table, Button } from 'antd'
import { Link } from 'react-router-dom'
import { URI } from './../../helpers/constants'

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
        http.get(`${API.ORDERS}/user/${userAccount.id}`).then(response => {
          const orders = response.data
          this.setState({ orders })
        })
      })
      .catch(error => console.log('error', error))
  }

  actionColumnRender = record => {
    switch (record.status) {
      case 'NOT_PAID':
        return (
          <Link to={`${URI.PAYMENT}/${record.id}`}>
            <Button type="danger">Payer</Button>
          </Link>
        )
      case 'FINALIZED':
        return (
          <Link to={`${URI.PAYMENT}/${record.id}`}>
            <Button type="primary">Modifier</Button>
          </Link>
        )
      default:
        return <div>Aucune action</div>
    }
  }

  render() {
    const columns = [
      {
        title: 'Date de commande',
        dataIndex: 'orderDate',
        rowKey: 'orderDate',
      },
      {
        title: 'Statut',
        rowKey: 'status',
        render: (text, record) => (
          <div>{record.status === 'NOT_PAID' ? 'Non Payée' : 'Finalisée'}</div>
        ),
      },
      {
        title: 'Action',
        rowKey: 'action',
        render: (text, record) => <div>{this.actionColumnRender(record)}</div>,
      },
    ]
    console.log(this.state)
    return (
      <Container>
        <div>
          <Table columns={columns} dataSource={this.state.orders} rowKey="id" />
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
