import axios from "axios";
import Slider from "react-slick";
import { useEffect, useRef, useState } from "react";
import { CATEGORIES_URL, PRODUCT_URL } from "../constants/ApiEndpoints";
import { getDefaultFood } from "../services/ImageService";
import { Link } from "react-router";
import { FOOD_URI } from "../constants/WebPageURI";

export function RecommendedMenu() {
  const [categories, setCategories] = useState([]);
  const [foodsPerCategory, setFoodsPerCategory] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState(0);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const tabMenuRef = useRef(null);
  const slickRef = useRef(null);

  // Fetch categories
  useEffect(() => {
    let isMounted = true;

    const fetchCategories = async () => {
      try {
        setIsLoading(true);
        const response = await axios.get(`${CATEGORIES_URL}?pageNumber=0&pageSize=10`);
        const data = response.data;
        if (data && Array.isArray(data.content) && isMounted) {
          const validCategories = data.content.filter(
            (category) => category && typeof category.categoryId !== "undefined"
          );
          setCategories(validCategories);
          if (validCategories.length === 0) {
            setError("No valid categories found.");
          }
        } else {
          throw new Error("Invalid categories data structure");
        }
      } catch (err) {
        console.error("Categories fetch error:", err);
        if (isMounted) setError(err.message || "Failed to fetch categories");
      } finally {
        if (isMounted) setIsLoading(false);
      }
    };

    fetchCategories();
    return () => {
      isMounted = false;
    };
  }, []);

  // Fetch products for each category
  useEffect(() => {
    const fetchProducts = async () => {
      if (categories.length === 0) return;

      try {
        setIsLoading(true);
        const validCategories = categories.filter(
          (category) => category && typeof category.categoryId !== "undefined"
        );
        if (validCategories.length === 0) {
          setFoodsPerCategory([]);
          setError("No categories with valid IDs available.");
          return;
        }

        const productPromises = validCategories.map((category) =>
          axios.get(`${PRODUCT_URL}?categoryId=${category.categoryId}&pageNumber=0&pageSize=10`)
        );
        const responses = await Promise.all(productPromises);
        const productsData = responses.map((res) => res.data);

        const foods = productsData.map((page, index) => ({
          categoryId: validCategories[index]?.categoryId,
          foods: Array.isArray(page.content)
            ? page.content.map((product) => ({
              name: product.productName || "Unnamed Product",
              description: product.description || "No description",
              price: product.unitPrice || 0,
              image: product.picture || getDefaultFood(),
            }))
            : [],
        }));
        setFoodsPerCategory(foods);
      } catch (err) {
        console.error("Products fetch error:", err);
        setError(err.message || "Failed to fetch products");
      } finally {
        setIsLoading(false);
      }
    };

    fetchProducts();
  }, [categories]);

  // Synchronize slider with selected category
  useEffect(() => {
    if (slickRef.current && selectedCategory !== null) {
      try {
        slickRef.current.slickGoTo(selectedCategory);
      } catch (err) {
        console.error("Slider navigation error:", err);
      }
    }
  }, [selectedCategory]);

  // Handle category selection
  const onSelectCategory = (index) => {
    setSelectedCategory(index); // Update selected category
    // Slider will automatically sync via useEffect
  };

  // Slick Slider settings
  const sliderSettings = {
    dots: false,
    infinite: false,
    speed: 500,
    slidesToShow: 1,
    slidesToScroll: 1,
    arrows: false,
    swipe: false,
    adaptiveHeight: true,
    cssEase: "ease", // Smooth transition handling
  };

  if (error) {
    return (
      <div>
        Error loading menu: {error}{" "}
        <button onClick={() => window.location.reload()}>Retry</button>
      </div>
    );
  }

  if (isLoading) {
    return <div>Loading...</div>;
  }

  return (
    <div id="menu" className="menu-main pad-top-100 pad-bottom-100">
      <div className="container">
        <div className="row">
          <div className="col-lg-12 col-md-12 col-sm-12 col-xs-12">
            <div className="wow fadeIn" data-wow-duration="1s" data-wow-delay="0.1s">
              <h2 className="block-title text-center">Our Menu</h2>
              <p className="title-caption text-center">
                There are many variations of passages of Lorem Ipsum available, but the majority have
                suffered alteration in some form, by injected humour, or randomised words which don't
                look even slightly believable.
              </p>
            </div>
            <div className="tab-menu" ref={tabMenuRef}>
              {/* Navigation Tabs */}
              <div className="slider slider-nav">
                {categories.length > 0 ? (
                  categories.map((category, index) => (
                    <div
                      key={category.categoryId || `category-${index}`}
                      className={`tab-title-menu flex-grow-1 cursor-pointer ${selectedCategory === index ? "is-active" : ""
                        }`}
                      onClick={() => onSelectCategory(index)}
                    >
                      <h2>{category.categoryName?.toUpperCase() || "UNKNOWN"}</h2>
                      <p>
                        <i className="flaticon-canape"></i>
                      </p>
                    </div>
                  ))
                ) : (
                  <div>No categories available</div>
                )}
              </div>

              {/* Slider Content */}
              <div className="slider slider-single">
                <Slider {...sliderSettings} ref={slickRef}>
                  {foodsPerCategory.length > 0 ? (
                    foodsPerCategory.map((foods, index) => (
                      <div className="row slick-slider" key={`food-category-${index}`}>
                        {foods.foods.length > 0 ? (
                          foods.foods.map((food, foodIndex) => (
                            <div
                              className="col-lg-6 col-md-12 col-sm-12 col-xs-12"
                              key={`${food.name}-${foodIndex}`}
                            >
                              <Link to={FOOD_URI(food.name)}>
                                <div className="offer-item">
                                  <img
                                    src={food.image}
                                    alt={food.name}
                                    className="img-responsive"
                                    onError={(e) => {
                                      e.target.src = "/fallback-image.jpg";
                                    }}
                                  />
                                  <div>
                                    <h3>{food.name}</h3>
                                    <p>{food.description}</p>
                                  </div>
                                  <span className="offer-price">
                                    ${food.price?.toFixed(2) || "0.00"}
                                  </span>
                                </div>
                              </Link>
                            </div>
                          ))
                        ) : (
                          <div>No products available</div>
                        )}
                      </div>
                    ))
                  ) : (
                    <div>No products available</div>
                  )}
                </Slider>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}