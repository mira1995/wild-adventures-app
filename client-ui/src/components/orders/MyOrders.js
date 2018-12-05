import React, { Component } from 'react'
import { http } from './../../configurations/axiosConf'
import { API, ORDERSTATUS } from '../../helpers/constants'
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
    const buttonStyle = {
      marginRight: '10px',
    }
    switch (record.status) {
      case ORDERSTATUS.NOT_PAID:
        return (
          <div>
            <Link to={`${URI.PAYMENT}/${record.id}`}>
              <Button style={buttonStyle} type="danger">
                Payer
              </Button>
            </Link>
            <Link to={`${URI.MYORDERS}${URI.CANCELDEMAND}/${record.id}`}>
              <Button type="danger">Annuler</Button>
            </Link>
          </div>
        )
      case ORDERSTATUS.FINALIZED:
        return (
          <div>
            <Link to={`${URI.MYORDERS}${URI.CANCELDEMAND}/${record.id}`}>
              <Button style={buttonStyle} type="primary">
                Modifier
              </Button>
            </Link>
            <Link to={`${URI.MYORDERS}${URI.CANCELDEMAND}/${record.id}`}>
              <Button type="danger">Annuler</Button>
            </Link>
          </div>
        )
      case ORDERSTATUS.DELETE_DEMAND:
        return <div>Demande de suppression en cours de traitement</div>
      case ORDERSTATUS.UPDATE_DEMAND:
        return (
          <div>
            Demande de mise à jour de la commande en cours de traitement
          </div>
        )
      default:
        return <div>Aucune action disponible</div>
    }
  }

  statusColumnRender = record => {
    switch (record.status) {
      case ORDERSTATUS.NOT_PAID:
        return <div>Non Payée</div>
      case ORDERSTATUS.FINALIZED:
        return <div>Finalisée</div>
      case ORDERSTATUS.DELETE_DEMAND:
        return <div>Demande de suppression</div>
      case ORDERSTATUS.UPDATE_DEMAND:
        return <div>Demande de mise à jour</div>
      default:
        return <div>Statut non reconnu</div>
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
        render: (text, record) => <div>{this.statusColumnRender(record)}</div>,
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
        <h1>Liste de mes commandes</h1>
        <div>
          {this.state.orders && this.state.orders.length > 0 ? (
            <Table
              columns={columns}
              dataSource={this.state.orders}
              rowKey="id"
            />
          ) : (
            <p>Vous n'avez réalisé aucune commande</p>
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
