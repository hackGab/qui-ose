import React, { Component } from 'react';

class AdminBlog extends Component {
    constructor(props) {
        super(props);
        this.state = {
            articles: [],
            newArticleTitle: '',
            newArticleContent: '',
            searchQuery: '',
            currentPage: 1,
            articlesPerPage: 5,
            totalArticles: 0,
            loading: false,
        };
    }

    componentDidMount() {
        this.fetchArticles();
    }

    // Méthode pour récupérer les articles
    fetchArticles = () => {
        this.setState({ loading: true });
        // Simuler une requête API pour récupérer les articles
        setTimeout(() => {
            const { currentPage, articlesPerPage, searchQuery } = this.state;
            const allArticles = this.generateFakeArticles();
            const filteredArticles = allArticles.filter(article => article.title.toLowerCase().includes(searchQuery.toLowerCase()));
            const totalArticles = filteredArticles.length;
            const currentArticles = filteredArticles.slice(
                (currentPage - 1) * articlesPerPage,
                currentPage * articlesPerPage
            );
            this.setState({
                articles: currentArticles,
                totalArticles,
                loading: false,
            });
        }, 1000);
    };

    // Générer des articles fictifs pour la démonstration
    generateFakeArticles = () => {
        const fakeArticles = [];
        for (let i = 1; i <= 100; i++) {
            fakeArticles.push({
                id: i,
                title: `Article ${i}`,
                content: `This is the content of article ${i}. It can be quite long or short, depending on the content.`,
            });
        }
        return fakeArticles;
    };

    // Gérer la recherche
    handleSearchChange = (event) => {
        this.setState({ searchQuery: event.target.value, currentPage: 1 }, this.fetchArticles);
    };

    // Gérer la pagination
    handlePageChange = (page) => {
        this.setState({ currentPage: page }, this.fetchArticles);
    };

    // Gérer la soumission d'un nouvel article
    handleNewArticleSubmit = (event) => {
        event.preventDefault();
        const { newArticleTitle, newArticleContent, articles } = this.state;
        if (newArticleTitle && newArticleContent) {
            const newArticle = {
                id: articles.length + 1,
                title: newArticleTitle,
                content: newArticleContent,
            };
            this.setState((prevState) => ({
                articles: [newArticle, ...prevState.articles],
                newArticleTitle: '',
                newArticleContent: '',
            }));
        }
    };

    // Gérer les changements dans le titre ou le contenu du nouvel article
    handleNewArticleChange = (event) => {
        const { name, value } = event.target;
        this.setState({ [name]: value });
    };

    // Calculer le nombre total de pages pour la pagination
    getTotalPages = () => {
        const { totalArticles, articlesPerPage } = this.state;
        return Math.ceil(totalArticles / articlesPerPage);
    };

    getSnapshotBeforeUpdate(prevProps, prevState) {
        if (prevState.searchQuery !== this.state.searchQuery) {
            console.log('Search query changed!');
        }
        return null;
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (prevState.searchQuery !== this.state.searchQuery) {
            this.fetchArticles();
        }

        if (prevState.currentPage !== this.state.currentPage) {
            this.fetchArticles();

        }

        if (prevState.articlesPerPage !== this.state.articlesPerPage) {
            this.fetchArticles();
        }

    }

    render() {
        const {
            articles,
            newArticleTitle,
            newArticleContent,
            searchQuery,
            currentPage,
            loading,
        } = this.state;
        const totalPages = this.getTotalPages();

        return (
            <div className="admin-blog">
                <h1>Admin Blog</h1>
                <div className="search-bar">
                    <input
                        type="text"
                        placeholder="Search articles"
                        value={searchQuery}
                        onChange={this.handleSearchChange}
                    />
                </div>

                <div className="articles-per-page">
                    <label htmlFor="articlesPerPage">Articles per page:</label>
                    <select
                        id="articlesPerPage"
                        name="articlesPerPage"
                        value={this.state.articlesPerPage}
                        onChange={(event) => this.setState({ articlesPerPage: parseInt(event.target.value), currentPage: 1 })}
                    >
                        <option value="5">5</option>
                        <option value="10">10</option>
                        <option value="20">20</option>
                    </select>
                </div>


                <div className="article-list">
                    {loading ? (
                        <p>Loading...</p>
                    ) : (
                        articles.map((article) => (
                            <div key={article.id} className="article-item">
                                <h2>{article.title}</h2>
                                <p>{article.content}</p>
                            </div>
                        ))
                    )}
                </div>

                <div className="pagination">
                    <button
                        onClick={() => this.handlePageChange(1)}
                        disabled={currentPage === 1}
                    >
                        First
                    </button>
                    <button
                        onClick={() => this.handlePageChange(currentPage - 1)}
                        disabled={currentPage === 1}
                    >
                        Prev
                    </button>
                    <span>
            Page {currentPage} of {totalPages}
          </span>
                    <button
                        onClick={() => this.handlePageChange(currentPage + 1)}
                        disabled={currentPage === totalPages}
                    >
                        Next
                    </button>
                    <button
                        onClick={() => this.handlePageChange(totalPages)}
                        disabled={currentPage === totalPages}
                    >
                        Last
                    </button>
                </div>

                <div className="new-article-form">
                    <h2>Create New Article</h2>
                    <form onSubmit={this.handleNewArticleSubmit}>
                        <div>
                            <label htmlFor="newArticleTitle">Title</label>
                            <input
                                type="text"
                                id="newArticleTitle"
                                name="newArticleTitle"
                                value={newArticleTitle}
                                onChange={this.handleNewArticleChange}
                                required
                            />
                        </div>
                        <div>
                            <label htmlFor="newArticleContent">Content</label>
                            <textarea
                                id="newArticleContent"
                                name="newArticleContent"
                                value={newArticleContent}
                                onChange={this.handleNewArticleChange}
                                required
                            />
                        </div>
                        <button type="submit">Submit</button>
                    </form>
                </div>
            </div>
        );
    }
}

export default AdminBlog;
