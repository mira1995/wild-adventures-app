import React from 'react'
import './Header.css'
import { BrowserRouter as Router, Route, Link } from "react-router-dom";

import NavLink from './subcomponent/NavLink'

const Header = () => (
    <nav className="navbar  navbar-expand-lg navbar-dark bg-dark">
        <div className="container">
            <Link className="navbar-brand" to="/">Wild Adventures</Link>
            <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarText"
                    aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
                <span className="navbar-toggler-icon"></span>
            </button>
            <div className="collapse navbar-collapse" id="navbarText">
                <ul className="navbar-nav mr-auto">
                    <NavLink to="/" text="Accueil" isCurrent />
                    <NavLink to="/categories" text="Catégories" />
                    <NavLink to="#" text="Mon compte" />
                </ul>
            </div>
        </div>
    </nav>
);
export default Header;