import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Link } from "react-router-dom";

import './App.css';

import Home from './component/Home';
import Adventure from './component/Adventure';
import Category from './component/Category';
import Header from './component/Header';


class App extends Component {

    render() {
        const homePath = "/";
        const adventurePath = "/adventure";
        const categoryPath = "/category";

        const HomeComponent = () => <Home test="test"/>;
        const AdventureComponent = () => <Adventure/>;
        const CategoryComponent = () => <Category/>;

        return (
            <Router>
                <div>
                    <Header/>
                    <div className="container">
                        <Route exact path={homePath} component={HomeComponent}/>
                        <Route path={adventurePath} component={AdventureComponent}/>
                        <Route path={categoryPath} component={CategoryComponent}/>
                    </div>
                </div>
            </Router>
        );
    }
}

export default App;
