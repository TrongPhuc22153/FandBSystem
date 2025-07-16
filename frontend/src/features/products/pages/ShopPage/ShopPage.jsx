import { useState, useEffect } from "react";
import { Link, useSearchParams } from "react-router-dom";
import { HOME_URI } from "../../../../shared/constants/routes";
import useProducts from "../../hooks/useProducts";
import Loading from "../../../../shared/components/Loading/Loading";
import ErrorDisplay from "../../../../shared/components/ErrorDisplay/ErrorDisplay";
import background from "../../../../assets/images/background.jpg";
import ProductList from "../../components/ProductList/ProductList";

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
    products,
    totalPages,
    error,
    isLoading,
  } = useProducts({
    page: currentPage,
    size: 12,
    categoryId: categoryId,
    search: searchTermFromURL,
  });

  if (!products && isLoading) {
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
        <ProductList products={products} totalPages={totalPages} currentPage={currentPage + 1}/>
      )}
    </>
  );
}

export default ShopPage;