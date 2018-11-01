import * as React from 'react';
import Categories from './components/Categories';
import axios from 'axios';
import './App.css';

class App extends React.Component {
    componentDidMount() {
        axios.defaults.baseURL = 'http://localhost:9000';
        axios.defaults.headers.post['Content-Type'] = 'application/json';

        axios.post('/auth', {
            "username": "admin@gmail.com",
            "password": "12345"
        }).then(response => {
            const bearerToken = response.headers.authorization;
            axios.post('/categories/admin', {
                "title": "Je suis nouvelle, merci l'admin",
                "description": "Envoyée via le client React !!"
            }, {
                headers: {"Authorization": bearerToken}
            })
        });
    }

    render() {
        return (
            <div className="App">
                <Categories />
            </div>
        );
    }
}

export default App;
