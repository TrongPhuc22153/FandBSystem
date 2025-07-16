import { Link } from "react-router-dom";
import ProductCard from "../../../products/components/ProductCard/ProductCard";
import { getPrimaryProductImage } from "../../../../shared/utils/imageUtils";
import { SHOP_URI } from "../../../../shared/constants/routes";
import CategoryCard from "../../../categories/components/CategoryCard/CategoryCard";
import useCategories from "../../../categories/hooks/useCategories";
import useProducts from "../../../products/hooks/useProducts";
import Loading from "../../../../shared/components/Loading/Loading";
import ErrorDisplay from "../../../../shared/components/ErrorDisplay/ErrorDisplay";
import mainBg from "../../../../assets/images/main-bg.jpg";
import Slider from "react-slick";
import { useTranslation } from "react-i18next";
import {homeSetting} from "../../constants/setting";

const HomePage = () => {
  const { t } = useTranslation();

  const {
    categories,
    error: categoriesError,
    isLoading: isLoadingCategories
  } = useCategories();

  const {
    products,
    error: productsError,
    isLoading: isLoadingProducts,
  } = useProducts({ isFeatured: true });

  const featuredProducts = products.map((product) => ({
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
          backgroundImage: `url(${mainBg})`,
          backgroundRepeat: "no-repeat",
          backgroundSize: "cover",
          backgroundPosition: "center",
        }}
      >
        <div className="flex justify-center h-100 hero-content-container">
          <div className="row w-100">
            <div className="ms-5 col-md-4 text-lg-start position-absolute bottom-0 mb-5 pb-5">
              <h2 className="text-white text-5xl font-bold capitalize">
                {t("home.freshFlavors")}
              </h2>
              <h2 className="text-white text-5xl font-bold capitalize">
                {t("home.madeForYou")}
              </h2>
              <Link
                to={SHOP_URI}
                className="inline-block text-white uppercase py-2 px-4 rounded-md mt-4 fa-2x"
              >
                {t("home.shopNow")}
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
                {t("home.featuredDishes")}
              </h2>
            </div>
          </div>
          {productsError?.message ? (
            <ErrorDisplay message={productsError.message} />
          ) : (
            <Slider {...homeSetting}>
              {featuredProducts.map((product) => (
                <div key={product.productId} className="px-2">
                  <ProductCard product={product} />
                </div>
              ))}
            </Slider>
          )}
        </div>
      </section>
    </>
  );
};

export default HomePage;
