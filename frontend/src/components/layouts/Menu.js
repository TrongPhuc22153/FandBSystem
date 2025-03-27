import { useEffect, useRef, useState } from "react";
import { getProductsPerMenu } from "../../api/ProductAPI";

export function RecommendedMenu(){
    const [selectedCategory, setSelectedCategory] = useState(0)
    const [slide, setSlide] = useState(0)
    const [totalSlides, setTotalSlides] = useState(0)
    const [foodsPerCategory, setFoodsPerCategory] = useState([])
    const tabMenuRef = useRef()
    const slickRef = useRef()

    useEffect(()=>{
        const updateDimensions = () => {
            if (!tabMenuRef.current || !slickRef.current) return;

            const tabMenuWidth = tabMenuRef.current.getBoundingClientRect().width;
            const totalSlideWidth = tabMenuWidth * foodsPerCategory.length;

            slickRef.current.style.transition = "none"; // Disable transition when resizing
            slickRef.current.style.transform = `translateX(-${selectedCategory * tabMenuWidth}px)`;

            slickRef.current.style.width = `${totalSlideWidth}px`;
            setSlide(tabMenuWidth);
            setTotalSlides(foodsPerCategory.length);
        };

        fetchProductsPerMenu().then(() => {
            updateDimensions();
        });

        window.addEventListener("resize", updateDimensions);

        return () => {
            window.removeEventListener("resize", updateDimensions);
        };
    
    }, [totalSlides, slide])

    const fetchProductsPerMenu = async ()=>{
        const foods = await getProductsPerMenu()
        
        setFoodsPerCategory(foods)
    }

    const onSelectCategory = (index)=>{
        slickRef.current.style.transition = "transform 0.8s cubic-bezier(0.25, 1, 0.5, 1)";
        slickRef.current.style.transform = `translateX(-${index * slide}px)`;
        setSelectedCategory(index)
    }

    


    return(
        <div id="menu" className="menu-main pad-top-100 pad-bottom-100">
            <div className="container">
                <div className="row">
                    <div className="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                        <div className="wow fadeIn" data-wow-duration="1s" data-wow-delay="0.1s">
                            <h2 className="block-title text-center">
                                Our Menu 	
                            </h2>
                            <p className="title-caption text-center">
                                There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour, or randomised words which don't look even slightly believable. 
                            </p>
                        </div>
                        <div className="tab-menu" ref={tabMenuRef}>
                            <div className="slider slider-nav">
                                {foodsPerCategory.map((category, index)=>(
                                    <div key={index} className={`tab-title-menu flex-grow-1 cursor-pointer ${selectedCategory==index?'is-active':''}`}
                                        onClick={()=> onSelectCategory(index)}>
                                        <h2>{category.category.toUpperCase()}</h2>
                                        <p> <i className="flaticon-canape"></i> </p>
                                    </div>
                                ))}
                            </div>
                            <div className="slider slider-single">
                                <div className="slick " ref={slickRef}>
                                    {foodsPerCategory.map((foods, index)=>(
                                        <div className="row slick-slider" key={index}>
                                            {foods.foods.map((food, foodIndex)=>(
                                                <div className="col-lg-6 col-md-12 col-sm-12 col-xs-12 " key={`${food.name}:${foodIndex}`}>
                                                    <div className="offer-item">
                                                        <img src={food.image} alt="" className="img-responsive"/>
                                                        <div>
                                                            <h3>{food.name}</h3>
                                                            <p>{food.description}</p>
                                                        </div>
                                                        <span className="offer-price">${food.price}</span>
                                                    </div>
                                                </div>
                                            ))}
                                        </div>
                                    ))}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}