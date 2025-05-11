import React from "react";
import { Link } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faArrowRight, faArrowLeft } from "@fortawesome/free-solid-svg-icons";
import ProductCard from "../../components/ProductCard/ProductCard";
import { getPrimaryProductImage } from "../../utils/imageUtils";
import { SHOP_URI } from "../../constants/routes";
import CategoryCard from "../../components/CategoryCard/CategoryCard";
import { useCategories } from "../../hooks/categoryHooks";
import { useProducts } from "../../hooks/productHooks";
import Loading from "../../components/Loading/Loading";
import ErrorDisplay from "../../components/ErrorDisplay/ErrorDisplay";

const HomePage = () => {
  const {
    data: categoriesData,
    error: categoriesError,
    isLoading: isLoadingCategories,
  } = useCategories({});

  const {
    data: productsData,
    error: productsError,
    isLoading: isLoadingProducts,
  } = useProducts({ isFeatured: true });

  const categories = categoriesData?.content || [];

  const featuredProducts = (
    productsData?.content ||
    productsData?.items ||
    []
  ).map((product) => ({
    ...product,
    imageUrl: getPrimaryProductImage(product.images),
  }));

  if (isLoadingCategories || isLoadingProducts) return <Loading />;

  return (
    <>
      <section
        id="hero"
        className="container-fluid position-relative rounded-4"
        style={{
          height: "80vh",
          backgroundImage: `url("/images/main-bg.jpg")`,
          backgroundRepeat: "no-repeat",
          backgroundSize: "cover",
          backgroundPosition: "center",
        }}
      >
        <div className="flex justify-center h-100 hero-content-container">
          <div className="row w-100">
            <div className="ms-5 col-md-4 text-lg-start position-absolute bottom-0 mb-5 pb-5">
              <h2 className="text-white text-5xl font-bold capitalize">
                Fresh flavors
              </h2>
              <h2 className="text-white text-5xl font-bold capitalize">
                made for you
              </h2>
              <Link
                to={SHOP_URI}
                className="inline-block text-white uppercase py-2 px-4 rounded-md mt-4 fa-2x"
              >
                Shop Now
              </Link>
            </div>
          </div>
        </div>
      </section>

      {categoriesError?.message ? (
        <ErrorDisplay message={categoriesError.message} />
      ) : (
        <section
          id="products"
          className="container-fluid py-4 position-relative"
        >
          <div className="row g-4 d-block d-lg-flex">
            {categories.map((category) => (
              <div className="col-lg-4 col-md-6" key={category.categoryId}>
                <CategoryCard category={category} />
              </div>
            ))}
          </div>
        </section>
      )}

      <section
        id="best-selling"
        className="container-fluid trending-carousel overflow-hidden bg-overlay position-relative py-5"
      >
        <div className="container-fluid">
          <div className="section-header d-flex justify-content-between mb-lg-5">
            <div className="heading">
              <h2 className="display-3 fw-bold text-capitalize text-black">
                Featured Dishes
              </h2>
            </div>
            <div className="d-none d-lg-flex">
              <div>
                <FontAwesomeIcon
                  icon={faArrowLeft}
                  className="arrow-left p-0 p-lg-3 border border-2 me-3 rounded-circle"
                  size="lg"
                />
              </div>
              <div>
                <FontAwesomeIcon
                  icon={faArrowRight}
                  className="arrow-right p-0 p-lg-3 border border-2 rounded-circle"
                  size="lg"
                />
              </div>
            </div>
          </div>
          {productsError?.message ? (
            <ErrorDisplay message={productsError.message} />
          ) : (
            <div className="row">
              {featuredProducts.map((product) => (
                <div className="col-lg-3 col-md-6 mb-5" key={product.productId}>
                  <ProductCard product={product} />
                </div>
              ))}
            </div>
          )}
        </div>
      </section>
    </>
  );
};
export default HomePage;
