import React from 'react'
import './Category.css'
import axios from "axios/index";
import { withRouter, Link } from "react-router-dom";

class Category extends React.Component{
    constructor(props){
        super(props);
        this.state= {
            category: [],
            adventures: []
        }
    }


    componentDidMount(props){
        console.log(this);
        axios.get(`http://localhost:9000/categories/`+this.props.match.params.id).then(res=> {
            const category = res.data;
            this.setState({category})
        });
        axios.get(`http://localhost:9000/adventure/`+this.props.match.params.id).then(res=> {
            const adventures = res.data;
            this.setState({adventures})
        });
    }

    render (){
        return (
            <div className="Category">
                <h1>{this.state.category.title}</h1>
                <div className="row">
                    <p>{this.state.category.description}</p>
                </div>
                <h2>Liste des aventures</h2>
                <div className="row">
                    {this.state.adventures.map(adventure => <AdventureItem key={adventure.id} index={adventure.id} imagePath="island.jpg"  adventure={adventure}  /> )}
                </div>
            </div>
        )
    }
}

const AdventureItem = ({index,imagePath, adventure}) =>(
    <div className="col-lg-4 col-sm-6 portfolio-item">
        <div className="card height100">
            <Link to={`/adventure/${index}`}>
                <img className="card-img-top" src={`/images/category/${imagePath}`} alt={adventure.description}/>
            </Link>
            <div className="card-body">
                <h4 className="card-title">
                    <Link to={`/adventure/${index}`}>{adventure.title}</Link>
                </h4>
                <p className="card-text">{adventure.description}</p>
            </div>
            <div className="card-footer">
                <p>Localisation : {adventure.location}</p>
            </div>
        </div>
    </div>
);

export default withRouter(Category);