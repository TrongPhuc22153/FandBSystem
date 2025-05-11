import { getImageSrc } from "../../utils/imageUtils";
import { SHOP_URI } from "../../constants/routes";
import { Link } from "react-router-dom";

function CategoryCard({ category }) {
  return (
    <div
      className="rounded-4 img-fluid align-content-end"
      style={{
        backgroundImage: `url(${getImageSrc(category.image)})`,
        height: "60vh",
        backgroundRepeat: "no-repeat",
        backgroundSize: "cover",
        backgroundPosition: "center",
      }}
    >
      <div className="hero-content text-center mb-5 pb-4">
        <div className="detail text-center ">
          <h3 className="text-white display-3 fw-bold text-capitalize mb-2">
            {category.categoryName}
          </h3>
          <Link
            to={`${SHOP_URI}?categoryId=${category.categoryId}`}
            className="text-white text-decoration-underline text-uppercase fs-5 fw-semibold"
          >
            Shop Now
          </Link>
        </div>
      </div>
    </div>
  );
}
export default CategoryCard;
