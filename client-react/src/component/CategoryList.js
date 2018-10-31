import React from 'react'
import './CategoryList.css'
import axios from 'axios';
import { Link } from "react-router-dom";


class CategoryList extends React.Component {
    state= {
        categories: []
    };

    componentDidMount(){
        axios.get(`http://localhost:9000/categories`).then(res=> {
            const categories = res.data;
            this.setState({categories})
        })
    }

    render(){
        return(
            <div className="CategoryList">
                <h1>Liste des Catégories <small>Partez à l'aventure</small></h1>
                <div className="row">
                    {this.state.categories.map(category => <CategoryItem key={category.id} index={category.id} imagePath="island.jpg" title={category.title} description={category.description} /> )}
                </div>
            </div>
        )
    }
}

const CategoryItem = ({index,imagePath, title, description}) =>(
    <div className="col-lg-4 col-sm-6 portfolio-item">
        <div className="card height100">
            <a href="#">
                <img className="card-img-top" src={`/images/category/${imagePath}`} alt={description}/>
            </a>
            <div className="card-body">
                <h4 className="card-title">
                    <Link to={`/category/${index}`}>{title}</Link>
                </h4>
                <p className="card-text">{description}</p>
            </div>
        </div>
    </div>
);

export default CategoryList;