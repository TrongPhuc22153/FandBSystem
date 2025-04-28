import { Link } from "react-router-dom";
import { useEffect, useRef, useState } from "react";
import Slider from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import axios from "axios";
import { PRODUCT_URL } from "../constants/ApiEndpoints";
import { FOOD_URI } from "../constants/WebPageURI";
import config from "../config/WebConfig";
import { getDefaultFood } from "../services/ImageService";

export function TodaySpecialDish() {
  const [specials, setSpecials] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const slickRef = useRef(null);

  // Fetch featured products
  useEffect(() => {
    const fetchFeaturedProducts = async () => {
      try {
        setIsLoading(true);
        const res = await axios.get(`${PRODUCT_URL}?featured=true`);
        const data = res.data?.content || [];
        const formattedSpecials = data.map((product) => ({
          name: product.productName || "Unnamed Dish",
          description: product.description || "No description available",
          image: product.picture,
          price: product.unitPrice || 0,
        }));
        setSpecials(formattedSpecials);
      } catch (err) {
        console.error("Error fetching featured products:", err);
        setError(err.message || "Failed to load special dishes");
      } finally {
        setIsLoading(false);
      }
    };

    fetchFeaturedProducts();
  }, []);

  const sliderSettings = {
    dots: true,
    infinite: specials.length > 1,
    speed: 800,
    slidesToShow: Math.min(specials.length || 1, 4),
    slidesToScroll: 1,
    autoplay: specials.length > 1,
    autoplaySpeed: config.TODAY_SPECIAL_SURFING_TIME || 3000,
    arrows: false,
    cssEase: "cubic-bezier(0.25, 1, 0.5, 1)",
    adaptiveHeight: false,
    responsive: [
      {
        breakpoint: 992,
        settings: { slidesToShow: Math.min(specials.length || 1, 2) },
      },
      {
        breakpoint: 576,
        settings: { slidesToShow: 1 },
      },
    ],
  };

  // Handle loading state
  if (isLoading) {
    return (
      <div className="special-menu pad-top-100 parallax">
        <div className="container">
          <div className="row">
            <div className="col-lg-12 col-md-12 col-sm-12 col-xs-12">
              <h2 className="block-title color-white text-center">Today's Special</h2>
              <div>Loading...</div>
            </div>
          </div>
        </div>
      </div>
    );
  }

  // Handle error state
  if (error) {
    return (
      <div className="special-menu pad-top-100 parallax">
        <div className="container">
          <div className="row">
            <div className="col-lg-12 col-md-12 col-sm-12 col-xs-12">
              <h2 className="block-title color-white text-center">Today's Special</h2>
              <div>Error loading special dishes: {error}</div>
              <button onClick={() => window.location.reload()}>Retry</button>
            </div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="special-menu pad-top-100 parallax">
      <div className="container">
        <div className="row">
          <div className="col-lg-12 col-md-12 col-sm-12 col-xs-12">
            <div className="wow fadeIn" data-wow-duration="1s" data-wow-delay="0.1s">
              <h2 className="block-title color-white text-center">Today's Special</h2>
              <h5 className="title-caption text-center">
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod
                incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam,
                nostrud exercitation ullamco.
              </h5>
            </div>
            <div className="special-box">
              {specials.length > 0 ? (
                <Slider {...sliderSettings} ref={slickRef}>
                  {specials.map((food, index) => (
                    <div className="item item-type-zoom position-relative" key={`${food.name}-${index}`}>
                      <div className="item-img position-relative">
                        <img
                          src={food.image || (getDefaultFood ? getDefaultFood() : "/default-food.jpg")}
                          alt={food.name}
                          className="img-responsive w-100"
                          onError={(e) => {
                            e.target.src = "/default-food.jpg";
                          }}
                        />
                        <Link to={FOOD_URI(food.name)} className="item-hover">
                          <div className="item-info">
                            <div className="headline">
                              {food.name}
                              <div className="line"></div>
                              <div className="dit-line">{food.description}</div>
                            </div>
                          </div>
                        </Link>
                      </div>
                    </div>
                  ))}
                </Slider>
              ) : (
                <div>No special dishes available</div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}