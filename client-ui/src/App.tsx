import * as React from 'react';
import Categories from './components/Categories';
import './App.css';

class App extends React.Component {
    componentDidMount() {
        this.setState({isLoading: true});

        fetch('http://localhost:9000/auth', {
            method: "POST",
            body: JSON.stringify({"username": "admin@gmail.com", "password": "12345"}),
            headers: {"Content-Type": "application/json"}
        })
            .then(response => {
                const bearerToken = response.headers.get("Authorization");
                console.log(bearerToken);
                if (bearerToken) {
                  fetch('http://localhost:9000/categories/admin', {
                      method: "POST",
                      body: JSON.stringify({"title": "Je suis nouvelle, merci l'admin", "description": "Envoyée via le client React !!"}),
                      headers: {
                        "Authorization": bearerToken.toString(),
                        "Content-Type": "application/json"
                      }
                  })
                      .then(responseCat => console.log(responseCat));
                }
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
