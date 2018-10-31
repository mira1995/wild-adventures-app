import React from 'react'
import './Header.css'
import { BrowserRouter as Router, Route, Link } from "react-router-dom";

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
                    <li className="nav-item active">
                        <Link className="nav-link" to="/">Accueil <span className="sr-only">(current)</span></Link>
                    </li>
                    <li className="nav-item">
                        <Link className="nav-link" to="/category">Catégories</Link>
                    </li>
                    <li className="nav-item">
                        <Link className="nav-link" to="#">Mon Compte</Link>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
);

export default Header;