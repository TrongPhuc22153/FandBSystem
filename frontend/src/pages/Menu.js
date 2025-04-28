import { useState, useEffect } from "react";
import FilterSidebar from "../components/FilterSidebar";
import axios from "axios";
import { PRODUCT_URL } from "../constants/ApiEndpoints";
import { getDefaultFood } from "../services/ImageService";
import { Pagination } from "../components/Pagination";
import { Link, useSearchParams } from "react-router-dom";
import { FOOD_URI } from "../constants/WebPageURI";

export default function MenuPage() {
  const [products, setProducts] = useState([]);
  const [pageable, setPageable] = useState({
    pageNumber: 0,
    totalPages: 0,
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchParams] = useSearchParams();
  const currentPage = parseInt(searchParams.get('page') || '0');

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const response = await axios.get(`${PRODUCT_URL}?page=${currentPage}`);
        const data = response.data;
        setProducts(data.content);
        setPageable({
          pageNumber: data.pageable.pageNumber,
          totalPages: data.totalPages,
        });
        setLoading(false);
      } catch (e) {
        setError(e.message);
        setLoading(false);
        console.error("Error fetching products:", e);
      }
    };
    fetchProducts();
  }, [currentPage]);

  if (loading) {
    return <div>Loading products...</div>;
  }

  if (error) {
    return <div>Error loading products: {error}</div>;
  }

  return (
    <>
      <section id="home" className="welcome-hero">
        {/* ... your welcome hero content ... */}
      </section>

      <div className="overlay d-none"></div>
      <div className="search-section py-5">
        <div className="container-fluid container-xl">
          <div className="row main-content ml-md-0">
            {/* <div className="col-md-3">
              <FilterSidebar />
            </div> */}

            <div className="content col-md-12">
              <div className="row row-grid" style={{ minHeight: "80vh" }}>
                {products.map((product, index) => (
                  <div key={index} className="col-md-6 col-lg-4 col-xl-3 mb-4 d-flex">
                    <div className="card">
                      <img
                        className="card-img-top"
                        src={product.image || getDefaultFood()}
                        alt={product.productName} // Added alt attribute for accessibility
                      />
                      <div className="card-body">
                        <h5 className="card-title">{product.productName}</h5>
                        <p className="card-text">{product.description}</p>
                        <p className="card-text">${product.unitPrice}</p>
                        <Link to={`${FOOD_URI(product.productName)}?id=${product.productId}`} className="btn btn-primary">
                          Order
                        </Link>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
              <div className="d-flex justify-content-center"> {/* Changed ul to div for layout */}
                <Pagination
                  totalPages={pageable.totalPages}
                  currentPage={currentPage}
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}