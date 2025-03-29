import { useState } from "react";
import FilterSidebar from "../../layouts/FilterSidebar";



export default function MenuComponent() {
    const [products, setProducts] = useState([])

    return (
        <>
            <section id="home" className="welcome-hero">
                <div className="container">
                    <div className="welcome-hero-txt">
                        <h2>best place to find and explore <br /> that all you need </h2>
                    </div>
                </div>
            </section>

             <div className="overlay d-none"></div>
            <div className="search-section py-5">
                <div className="container-fluid container-xl">
                    <div className="row main-content ml-md-0">
                        <div className="col-md-3">
                            <FilterSidebar />
                        </div>

                        <div className="content col-md-9">
                            <div className="d-flex justify-content-between border-bottom align-items-center">
                                <h2 className="title">Products</h2>
                            </div>
                            <div className="row row-grid" style={{minHeight: "80vh"}}>
                                {products.map((product, index) => (
                                    <div key={index} className="col-md-6 col-lg-4 col-xl-4 mb-4">
                                        <div className="card" style={{ width: "18rem" }}>
                                            <img className="card-img-top" src={product.image} alt="Card image cap" />
                                            <div className="card-body">
                                                <h5 className="card-title">{product.name}</h5>
                                                <p className="card-text">
                                                    {product.description}
                                                </p>
                                                <p className="card-text">
                                                    {product.price}
                                                </p>
                                                <a href="#" className="btn btn-primary">Order</a>
                                            </div>
                                        </div>
                                    </div>
                                ))}
                            </div>
                            <ul className="pagination d-flex justify-content-center">
                                <li className="page-item">
                                    <a className="page-link" href="#" aria-label="Previous">
                                        <span aria-hidden="true">&laquo;</span>
                                        <span className="sr-only">Previous</span>
                                    </a>
                                </li>
                                <li className="page-item active"><a className="page-link" href="#">1</a></li>
                                <li className="page-item"><a className="page-link" href="#">2</a></li>
                                <li className="page-item"><a className="page-link" href="#">3</a></li>
                                <li className="page-item">
                                    <a className="page-link" href="#" aria-label="Next">
                                        <span aria-hidden="true">&raquo;</span>
                                        <span className="sr-only">Next</span>
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </div>
                    
                </div>
            </div>
           
        </>
    );
}
