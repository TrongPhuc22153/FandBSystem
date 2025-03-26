import { useEffect, useRef, useState } from "react";
import { getMenuItemThumbnail1, getMenuItemThumbnail2, getMenuItemThumbnail3, getMenuItemThumbnail4, getMenuItemThumbnail5, getMenuItemThumbnail6 } from "../../services/ImageService";

export function Menu(){
    const [categorySelector, setCategorySelect] = useState()
    const [selectedCategory, setSelectedCategory] = useState(0)
    const [foodsPerCategory, setFoodsPerCategory] = useState([
        {
            category: "starters",
            foods: [
                {
                    name: "CHOCOLATE FUDGECAKE",
                    description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc mollis eleifend dapibus.",
                    image: getMenuItemThumbnail1()
                },
                {
                    name: "MIXED SALAD",
                    description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc mollis eleifend dapibus.",
                    image: getMenuItemThumbnail2()
                },
                {
                    name: "BBQ CHICKEN WINGS",
                    description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc mollis eleifend dapibus.",
                    image: getMenuItemThumbnail3()
                }
            ]
        },
        {
            category: "main dishes",
            foods: [
                {
                    name: "MEAT FEAST PIZZA",
                    description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc mollis eleifend dapibus.",
                    image: getMenuItemThumbnail4()
                },
                {
                    name: "CHICKEN TIKKA MASALA",
                    description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc mollis eleifend dapibus.",
                    image: getMenuItemThumbnail2()
                },
                {
                    name: "SPICY MEATBALLS",
                    description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc mollis eleifend dapibus.",
                    image: getMenuItemThumbnail3()
                }
            ]
        },
        {
            category: "deserts",
            foods: [
                {
                    name: "CHOCOLATE FUDGECAKE",
                    description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc mollis eleifend dapibus.",
                    image: getMenuItemThumbnail4()
                },
                {
                    name: "CHICKEN TIKKA MASALA",
                    description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc mollis eleifend dapibus.",
                    image: getMenuItemThumbnail2()
                },
                {
                    name: "SPICY MEATBALLS",
                    description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc mollis eleifend dapibus.",
                    image: getMenuItemThumbnail3()
                }
            ]
        },
        {
            category: "drinks",
            foods: [
                {
                    name: "CHOCOLATE FUDGECAKE",
                    description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc mollis eleifend dapibus.",
                    image: getMenuItemThumbnail4()
                },
                {
                    name: "CHICKEN TIKKA MASALA",
                    description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc mollis eleifend dapibus.",
                    image: getMenuItemThumbnail2()
                },
                {
                    name: "SPICY MEATBALLS",
                    description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc mollis eleifend dapibus.",
                    image: getMenuItemThumbnail3()
                }
            ]
        }
    ])
    const tabMenuRef = useRef()
    const slickRef = useRef()

    useEffect(()=>{
        if(tabMenuRef.current){
            const tabMenuWidth = tabMenuRef.current.getBoundingClientRect().width
            const totalSlideWidth = tabMenuWidth*foodsPerCategory.length;
            console.log(tabMenuWidth*foodsPerCategory.length)
        }
    }, [])

    


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
                                        onClick={()=> setSelectedCategory(index)}>
                                        <h2>{category.category.toUpperCase()}</h2>
                                        <p> <i className="flaticon-canape"></i> </p>
                                    </div>
                                ))}
                            </div>
                            <div className="slider slider-single">
                                <div className="slick" ref={slickRef}>
                                    {foodsPerCategory.map((foods, index)=>(
                                        <div className="row" key={index}>
                                            {foods.foods.map((food, foodIndex)=>(
                                                <div className="col-lg-6 col-md-6 col-sm-12 col-xs-12 " key={`${food.name}:${foodIndex}`}>
                                                    <div className="offer-item">
                                                        <img src={food.image} alt="" className="img-responsive"/>
                                                        <div>
                                                            <h3>{food.name}</h3>
                                                            <p>
                                                                {food.description}
                                                            </p>
                                                        </div>
                                                        <span className="offer-price">$8.5</span>
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