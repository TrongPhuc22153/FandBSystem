import ProductCard from "../../components/ProductCard/ProductCard";
import { useState, useEffect } from "react";
import { Link, useSearchParams } from "react-router-dom";
import { HOME_URI } from "../../constants/routes";
import { useProducts } from "../../hooks/productHooks";
import Pagination from "../../components/Pagination/Pagination";
import Loading from "../../components/Loading/Loading";
import ErrorDisplay from "../../components/ErrorDisplay/ErrorDisplay";
import background from "../../assets/images/background.jpg";

function ShopPage() {
  const [searchParams] = useSearchParams();
  const currentPageFromURL = parseInt(searchParams.get("page")) || 0;
  const categoryIdFromURL = searchParams.get("categoryId") || "";
  const searchTermFromURL = searchParams.get("search") || "";

  const [currentPage, setCurrentPage] = useState(currentPageFromURL);
  const [categoryId] = useState(categoryIdFromURL);

  useEffect(() => {
    const pageFromURL = parseInt(searchParams.get("page")) || 1;
    setCurrentPage(pageFromURL - 1);
  }, [searchParams]);

  const {
    data: productData,
    error,
    isLoading,
  } = useProducts({
    page: currentPage,
    size: 12,
    categoryId: categoryId,
    search: searchTermFromURL,
  });

  const totalPages = productData?.totalPages || 0;

  if (!productData && isLoading) {
    return <Loading />;
  }

  return (
    <>
      <section id="hero">
        <div
          className="container-fluid align-items-center d-flex rounded-4"
          style={{
            height: "40vh",
            backgroundImage: `url(${background})`,
            backgroundRepeat: "no-repeat",
            backgroundSize: "cover",
            backgroundPosition: "center",
          }}
        >
          <div className="hero-content container justify-content-center text-center">
            <h2 className="display-2 fw-bold text-body text-capitalize mb-1">
              Shop
            </h2>
            <span className="item">
              <Link to={HOME_URI} className="text-body">
                Home /
              </Link>
            </span>
            <span className="item">Shop</span>
          </div>
        </div>
      </section>
      {error?.message ? (
        <ErrorDisplay message={error.message} />
      ) : (
        <section id="shop" className="my-5 bg-overlay">
          <div className="container-fluid" style={{ minHeight: "50vh" }}>
            <div className="row">
              {productData?.content.map((product) => (
                <div className="col-lg-3 col-md-6 mb-5 d-flex" key={product.productId}>
                  <ProductCard product={product} />
                </div>
              ))}
            </div>
            {totalPages > 1 && (
              <Pagination
                currentPage={currentPage + 1}
                totalPages={totalPages}
              />
            )}
          </div>
        </section>
      )}
    </>
  );
}

export default ShopPage;