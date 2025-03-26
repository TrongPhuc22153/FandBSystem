import { Link } from "react-router";
import { FOOD_URI } from "../../constants/WebPageURI";
import { getSpecialDishes } from "../../api/ProductAPI";
import { useEffect, useRef, useState } from "react";

export function TodaySpecialDish(){
    const [specials, setSpecials] = useState([])
    const [slide, setSlide] = useState(0)
    const [totalSlides, setTotalSlides]  = useState(0)
    const foodsPerPage = 3;

    const owlWrapperRef = useRef()
    const owlWrapperOuterRef = useRef();

    useEffect(()=>{
        fetchSpecialDishes();

        // const updateDimensions = () => {
        //     if (!owlWrapperOuterRef.current || !owlWrapperRef.current) return;
    
        //     const totalPages = Math.ceil(specials.length/foodsPerPage)

        //     const owlWrapperOuterWidth = owlWrapperOuterRef.current.getBoundingClientRect().width;

        //     const totalSlideWidth = owlWrapperOuterWidth * totalPages;
    
        //     owlWrapperRef.current.style.transition = "none"; // Disable transition when resizing
        //     owlWrapperRef.current.style.transform = `translateX(-${slideIndex * owlWrapperOuterWidth}px)`;
    
        //     owlWrapperRef.current.style.width = `${totalSlideWidth}px`;
        //     setSlide(owlWrapperOuterWidth);
        //     setTotalSlides(totalPages);
        // };
        // let slideIndex = 0;
    
        // fetchSpecialDishes();
        // updateDimensions();
    
        // const interval = setInterval(() => {
        //     slideIndex++;
    
        //     if (slideIndex >= totalSlides) {
        //         setTimeout(() => {
        //             owlWrapperRef.current.style.transition = "none";
        //             owlWrapperRef.current.style.transform = `translateX(0px)`;
        //         }, 800);
        //         slideIndex = 0;
        //     }
    
        //     owlWrapperRef.current.style.transition = "transform 0.8s cubic-bezier(0.25, 1, 0.5, 1)";
        //     owlWrapperRef.current.style.transform = `translateX(-${slideIndex * slide}px)`;
        // }, 2000);
    
        // window.addEventListener("resize", updateDimensions);
    
        // return () => {
        //     clearInterval(interval);
        //     window.removeEventListener("resize", updateDimensions);
        // };

    }, [slide])

    const fetchSpecialDishes = ()=>{
        setSpecials(getSpecialDishes())
    }

    return(
        <div className="special-menu pad-top-100 parallax">
            <div className="container">
                <div className="row">
                    <div className="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                        <div className="wow fadeIn" data-wow-duration="1s" data-wow-delay="0.1s">
                            <h2 className="block-title color-white text-center"> Today's Special </h2>
                            <h5 className="title-caption text-center"> 
                                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusm incididunt ut labore et dolore magna aliqua. Ut enim ad minim venia,nostrud exercitation ullamco. 
                            </h5>
                        </div>
                        <div className="special-box">
                            <div id="owl-demo">
                                <div className="owl-wrapper-outer overflow-hidden" ref={owlWrapperOuterRef}>
                                    <div className="owl-wrapper d-flex" ref={owlWrapperRef}>
                                        {specials.map((food, index)=>(
                                            <div className="item item-type-zoom" key={index}>
                                                <Link to={FOOD_URI(food.name)} className="item-hover">
                                                    <div className="item-info">
                                                        <div className="headline">
                                                            {food.name}
                                                            <div className="line"></div>
                                                            <div className="dit-line">{food.description}</div>
                                                        </div>
                                                    </div>
                                                </Link>
                                                <div className="item-img">
                                                    <img src={food.image} alt="sp-menu"/>
                                                </div>
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}