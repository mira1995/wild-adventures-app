import React from 'react'
import { withRouter, Link } from "react-router-dom";
import PropTypes from 'prop-types';


class NavLink extends React.Component{

     render () {
         const  isActive = this.props.location.pathname === this.props.to;
         console.log(this.context.router);
         return(
             <li className={`nav-item ${isActive && "active"}`}>
                 <Link className="nav-link" to={this.props.to}>{this.props.text}</Link>
             </li>
         )
     }
}

NavLink.propTypes = {
    to: PropTypes.string.isRequired,
    text: PropTypes.string.isRequired,
};

export default withRouter(NavLink);