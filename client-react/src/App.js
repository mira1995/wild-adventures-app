import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";

import './App.css';

import Home from './component/Home';
import Adventure from './component/Adventure';
import Category from './component/Category';
import CategoryList from './component/CategoryList';
import Header from './component/Header';
import NoMatch from './component/NoMatch';

class App extends Component {

    render() {
        const homePath = "/";
        const adventurePath = "/adventure";
        const categoryPath = "/category";
        const categoryListPath = "/categories";

        const HomeComponent = () => <Home test="test"/>;
        const AdventureComponent = () => <Adventure/>;
        const CategoryComponent = ({ match }) => <Category catId={match.params.id}/>;
        const CategoryListComponent = () => <CategoryList/>;
        const NoMatchComponent = ()=> <NoMatch/>;

        return (
            <Router>
                <div>
                    <Header/>
                    <div className="container">
                        <Switch>
                            <Route exact path={homePath} component={HomeComponent}/>
                            <Route path={categoryListPath} component={CategoryListComponent}/>
                            <Route path={`${categoryPath}/:id`} component={CategoryComponent}/>
                            <Route path={`${adventurePath}/:id`} component={AdventureComponent}/>
                            {/* when none of the above match, <NoMatch> will be rendered */}
                            <Route component={NoMatchComponent} />
                        </Switch>
                    </div>
                </div>
            </Router>
        );
    }
}

export default App;
