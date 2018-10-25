import * as React from 'react';
import axios from 'axios';

interface Category {
    id: number;
    title: string;
    description: string;
}

interface CategoriesProps {
}

interface CategoriesState {
    categories: Array<Category>;
    isLoading: boolean;
}

class Categories extends React.Component<CategoriesProps, CategoriesState> {
    constructor(props: CategoriesProps) {
        super(props);

        this.state = {
            categories: [],
            isLoading: false
        };
    }

    componentDidMount() {
        this.setState({isLoading: true});

        axios.get('http://localhost:9000/categories').then(response =>
            this.setState({categories: response.data, isLoading: false}));
    }

    render() {
        const {categories, isLoading} = this.state;

        if (isLoading) {
            return <p>Loading...</p>;
        }

        return (
            <div>
                <h2>Categories</h2>
                {categories.map((category: Category) =>
                    <div key={category.id}>{category.title}<br/>
                        <p>{category.description}</p>
                    </div>
                )}
            </div>
        );
    }
}

export default Categories;
