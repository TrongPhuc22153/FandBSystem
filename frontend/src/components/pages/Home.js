import { Link } from "react-router-dom";
import { getAboutInset, getAboutMain, getCoffee, getFood, getGallery01, getGallery02, getGallery03, getGallery04, getGallery05, getGallery06, getGallery07, getGallery08, getGallery09, getGallery10, getLogo, getMenuItemThumbnail1, getMenuItemThumbnail2, getMenuItemThumbnail3, getMenuItemThumbnail4, getMenuItemThumbnail5, getMenuItemThumbnail6, getSpecialMenu1, getSpecialMenu2, getSpecialMenu3, getStaff, getStore } from "../../services/ImageService";
import { Footer } from "../layouts/Footer";
import Header from "../layouts/Header";
import { useState } from "react";
import { FOOD_URI } from "../../constants/WebPageURI";
import { Menu } from "../layouts/Menu";

export default function HomeComponent(){
    const [specials, setSpecials] = useState([
        {
            name: 'SALMON STEAK',
            description: 'Lorem ipsum dolor sit amet, consectetur adip aliqua. Ut enim ad minim venia.',
            image: getSpecialMenu1()
        },
        {
            name: "ITALIAN PIZZA",
            description: "Lorem ipsum dolor sit amet, consectetur adip aliqua. Ut enim ad minim venia.",
            image: getSpecialMenu2()
        },
        {
            name: "VEG. ROLL",
            description: "Lorem ipsum dolor sit amet, consectetur adip aliqua. Ut enim ad minim venia.",
            image: getSpecialMenu3()
        },
        {
            name: 'SALMON STEAK',
            description: 'Lorem ipsum dolor sit amet, consectetur adip aliqua. Ut enim ad minim venia.',
            image: getSpecialMenu1()
        },
        {
            name: "ITALIAN PIZZA",
            description: "Lorem ipsum dolor sit amet, consectetur adip aliqua. Ut enim ad minim venia.",
            image: getSpecialMenu2()
        },
    ])

    const foodsPerPage = 3;
    const totalPages = Math.ceil(specials.length/foodsPerPage)



    return(
        <>
            {/* <div id="loader">
                <div id="status"></div>
            </div> */}
            <Header/>
            <div id="banner" className="banner full-screen-mode parallax">
                <div className="container pr">
                    <div className="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                        <div className="banner-static">
                            <div className="banner-text">
                                <div className="banner-cell">
                                    <h1>Dinner with us  <span className="typer" id="some-id" data-delay="200" data-delim=":" data-words="Friends:Family:Officemates" data-colors="red"></span><span className="cursor" data-cursorDisplay="_" data-owner="some-id"></span></h1>
                                    <h2>Accidental appearances </h2>
                                    <p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diem nonummy nibh euismod</p>
                                    <div className="book-btn">
                                        <a href="#reservation" className="table-btn hvr-underline-from-center">Book my Table</a>
                                    </div>
                                    <a href="#about">
                                        <div className="mouse"></div>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div id="about" className="about-main pad-top-100 pad-bottom-100">
                <div className="container">
                    <div className="row">
                        <div className="col-lg-6 col-md-6 col-sm-12 col-xs-12">
                            <div className="wow fadeIn" data-wow-duration="1s" data-wow-delay="0.1s">
                                <h2 className="block-title"> About Us </h2>
                                <h3>IT STARTED, QUITE SIMPLY, LIKE THIS...</h3>
                                <p> Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusm incididunt ut labore et dolore magna aliqua. Ut enim ad minim venia, nostrud exercitation ullamco. </p>

                                <p> Aenean commodo ligula eget dolor aenean massa. Cum sociis nat penatibu set magnis dis parturient montes, nascetur ridiculus mus. quam felisorat, ultricies nec, Aenean commodo ligula eget dolor penatibu set magnis is parturient montes, nascetur ridiculus mus. quam felisorat, ultricies nec, pellentesque eu, pretium quis, sem. quat massa quis enim. Donec vitae sapien ut libero venenatis fauci Nullam quis ante. Etiam sit amet rci eget eros. </p>

                                <p> Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusm incididunt ut labore et dolore magna aliqua. Ut enim ad minim venia, nostrud exercitation ullamco. </p>
                            </div>
                        </div>
                        <div className="col-lg-6 col-md-6 col-sm-12 col-xs-12">
                            <div className="wow fadeIn" data-wow-duration="1s" data-wow-delay="0.1s">
                                <div className="about-images">
                                    <img className="about-main" src={getAboutMain()} alt="About Main Image"/>
                                    <img className="about-inset" src={getAboutInset()} alt="About Inset Image"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div className="special-menu pad-top-100 parallax">
                <div className="container">
                    <div className="row">
                        <div className="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                            <div className="wow fadeIn" data-wow-duration="1s" data-wow-delay="0.1s">
                                <h2 className="block-title color-white text-center"> Today's Special </h2>
                                <h5 className="title-caption text-center"> Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusm incididunt ut labore et dolore magna aliqua. Ut enim ad minim venia,nostrud exercitation ullamco. </h5>
                            </div>
                            <div className="special-box">
                                <div id="owl-demo">
                                    <div className="owl-wrapper-outer">
                                        <div className="owl-wrapper d-flex overflow-hidden">
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
                                    {/* {Array.from({ length: totalPages }, (_, page)=>{
                                        const start = page*foodsPerPage;
                                        const end = start + foodsPerPage;
                                        const foods = specials.slice(start, end);
                                        return(
                                            <div className="owl-wrapper-outer">
                                                <div className="owl-wrapper">
                                                    {foods.map((food, index)=>(
                                                        <div className="item item-type-zoom col-md-4" key={index}>
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
                                        )
                                    })} */}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <Menu/>

            {/* <div id="our_team" className="team-main pad-top-100 pad-bottom-100 parallax">
                <div className="container">
                    <div className="row">
                        <div className="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                            <div className="wow fadeIn" data-wow-duration="1s" data-wow-delay="0.1s">
                                <h2 className="block-title text-center">
                                Our Team 	
                            </h2>
                                <p className="title-caption text-center">There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour, or randomised words which don't look even slightly believable. </p>
                            </div>
                            <div className="team-box">

                                <div className="row">
                                    <div className="col-md-4 col-sm-6">
                                        <div className="sf-team">
                                            <div className="thumb">
                                                <a href="#"><img src={getStaff()} alt=""/></a>
                                            </div>
                                            <div className="text-col">
                                                <h3>John Doggett</h3>
                                                <p>Nullam quis ante. Etiam sit amet orci eget eros faucibus tincidunt. Duis leo. Sed fringilla mauris sit amet nibh. Donec sodales sagittis magna. Aenean commodo ligula.</p>
                                                <ul className="team-social">
                                                    <li><a href="#"><i className="fa fa-facebook" aria-hidden="true"></i></a></li>
                                                    <li><a href="#"><i className="fa fa-twitter" aria-hidden="true"></i></a></li>
                                                    <li><a href="#"><i className="fa fa-linkedin" aria-hidden="true"></i></a></li>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                    <div className="col-md-4 col-sm-6">
                                        <div className="sf-team">
                                            <div className="thumb">
                                                <a href="#"><img src={getStaff()} alt=""/></a>
                                            </div>
                                            <div className="text-col">
                                                <h3>Jeffrey Spender</h3>
                                                <p>Nullam quis ante. Etiam sit amet orci eget eros faucibus tincidunt. Duis leo. Sed fringilla mauris sit amet nibh. Donec sodales sagittis magna. Aenean commodo ligula.</p>
                                                <ul className="team-social">
                                                    <li><a href="#"><i className="fa fa-facebook" aria-hidden="true"></i></a></li>
                                                    <li><a href="#"><i className="fa fa-twitter" aria-hidden="true"></i></a></li>
                                                    <li><a href="#"><i className="fa fa-linkedin" aria-hidden="true"></i></a></li>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                    <div className="col-md-4 col-sm-6">
                                        <div className="sf-team">
                                            <div className="thumb">
                                                <a href="#"><img src={getStaff()} alt=""/></a>
                                            </div>
                                            <div className="text-col">
                                                <h3>Monica Reyes</h3>
                                                <p>Nullam quis ante. Etiam sit amet orci eget eros faucibus tincidunt. Duis leo. Sed fringilla mauris sit amet nibh. Donec sodales sagittis magna. Aenean commodo ligula.</p>
                                                <ul className="team-social">
                                                    <li><a href="#"><i className="fa fa-facebook" aria-hidden="true"></i></a></li>
                                                    <li><a href="#"><i className="fa fa-twitter" aria-hidden="true"></i></a></li>
                                                    <li><a href="#"><i className="fa fa-linkedin" aria-hidden="true"></i></a></li>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                            </div>

                        </div>
                    </div>
                </div>
            </div> */}

            <div id="gallery" className="gallery-main pad-top-100 pad-bottom-100">
                <div className="container">
                    <div className="row">
                        <div className="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                            <div className="wow fadeIn" data-wow-duration="1s" data-wow-delay="0.1s">
                                <h2 className="block-title text-center">
                                Our Gallery	
                            </h2>
                                <p className="title-caption text-center">There are many variations of passages of Lorem Ipsum available </p>
                            </div>
                            <div className="gal-container clearfix">
                                <div className="col-md-8 col-sm-12 co-xs-12 gal-item">
                                    <div className="box">
                                        <a href="#" data-toggle="modal" data-target="#1">
                                            <img src={getGallery01()} alt="" />
                                        </a>
                                        <div className="modal fade" id="1" tabIndex="-1" role="dialog">
                                            <div className="modal-dialog" role="document">
                                                <div className="modal-content">
                                                    <button type="button" className="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
                                                    <div className="modal-body">
                                                        <img src={getGallery01()} alt="" />
                                                    </div>
                                                    <div className="col-md-12 description">
                                                        <h4>This is the 1 one on my Gallery</h4>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-md-4 col-sm-6 co-xs-12 gal-item">
                                    <div className="box">
                                        <a href="#" data-toggle="modal" data-target="#2">
                                            <img src={getGallery02()} alt="" />
                                        </a>
                                        <div className="modal fade" id="2" tabIndex="-1" role="dialog">
                                            <div className="modal-dialog" role="document">
                                                <div className="modal-content">
                                                    <button type="button" className="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
                                                    <div className="modal-body">
                                                        <img src={getGallery02()} alt="" />
                                                    </div>
                                                    <div className="col-md-12 description">
                                                        <h4>This is the 2 one on my Gallery</h4>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-md-4 col-sm-6 co-xs-12 gal-item">
                                    <div className="box">
                                        <a href="#" data-toggle="modal" data-target="#3">
                                            <img src={getGallery03()} alt="" />
                                        </a>
                                        <div className="modal fade" id="3" tabIndex="-1" role="dialog">
                                            <div className="modal-dialog" role="document">
                                                <div className="modal-content">
                                                    <button type="button" className="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
                                                    <div className="modal-body">
                                                        <img src={getGallery03()} alt="" />
                                                    </div>
                                                    <div className="col-md-12 description">
                                                        <h4>This is the 3 one on my Gallery</h4>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-md-4 col-sm-6 co-xs-12 gal-item">
                                    <div className="box">
                                        <a href="#" data-toggle="modal" data-target="#4">
                                            <img src={getGallery04()} alt="" />
                                        </a>
                                        <div className="modal fade" id="4" tabIndex="-1" role="dialog">
                                            <div className="modal-dialog" role="document">
                                                <div className="modal-content">
                                                    <button type="button" className="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
                                                    <div className="modal-body">
                                                        <img src={getGallery04()} alt="" />
                                                    </div>
                                                    <div className="col-md-12 description">
                                                        <h4>This is the 4 one on my Gallery</h4>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-md-4 col-sm-6 co-xs-12 gal-item">
                                    <div className="box">
                                        <a href="#" data-toggle="modal" data-target="#5">
                                            <img src={getGallery05()} alt="" />
                                        </a>
                                        <div className="modal fade" id="5" tabIndex="-1" role="dialog">
                                            <div className="modal-dialog" role="document">
                                                <div className="modal-content">
                                                    <button type="button" className="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
                                                    <div className="modal-body">
                                                        <img src={getGallery05()} alt="" />
                                                    </div>
                                                    <div className="col-md-12 description">
                                                        <h4>This is the 5 one on my Gallery</h4>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-md-4 col-sm-6 co-xs-12 gal-item">
                                    <div className="box">
                                        <a href="#" data-toggle="modal" data-target="#9">
                                            <img src={getGallery06()} alt="" />
                                        </a>
                                        <div className="modal fade" id="9" tabIndex="-1" role="dialog">
                                            <div className="modal-dialog" role="document">
                                                <div className="modal-content">
                                                    <button type="button" className="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
                                                    <div className="modal-body">
                                                        <img src={getGallery06()} alt="" />
                                                    </div>
                                                    <div className="col-md-12 description">
                                                        <h4>This is the 6 one on my Gallery</h4>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-md-8 col-sm-12 co-xs-12 gal-item">
                                    <div className="box">
                                        <a href="#" data-toggle="modal" data-target="#10">
                                            <img src={getGallery07()} alt="" />
                                        </a>
                                        <div className="modal fade" id="10" tabIndex="-1" role="dialog">
                                            <div className="modal-dialog" role="document">
                                                <div className="modal-content">
                                                    <button type="button" className="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
                                                    <div className="modal-body">
                                                        <img src={getGallery07()} alt="" />
                                                    </div>
                                                    <div className="col-md-12 description">
                                                        <h4>This is the 7 one on my Gallery</h4>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-md-4 col-sm-6 co-xs-12 gal-item">
                                    <div className="box">
                                        <a href="#" data-toggle="modal" data-target="#11">
                                            <img src={getGallery08()} alt="" />
                                        </a>
                                        <div className="modal fade" id="11" tabIndex="-1" role="dialog">
                                            <div className="modal-dialog" role="document">
                                                <div className="modal-content">
                                                    <button type="button" className="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
                                                    <div className="modal-body">
                                                        <img src={getGallery08()} alt="" />
                                                    </div>
                                                    <div className="col-md-12 description">
                                                        <h4>This is the 8 one on my Gallery</h4>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-md-4 col-sm-6 co-xs-12 gal-item">
                                    <div className="box">
                                        <a href="#" data-toggle="modal" data-target="#12">
                                            <img src={getGallery09()} alt="" />
                                        </a>
                                        <div className="modal fade" id="12" tabIndex="-1" role="dialog">
                                            <div className="modal-dialog" role="document">
                                                <div className="modal-content">
                                                    <button type="button" className="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
                                                    <div className="modal-body">
                                                        <img src={getGallery09()} alt="" />
                                                    </div>
                                                    <div className="col-md-12 description">
                                                        <h4>This is the 9 one on my Gallery</h4>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-md-4 col-sm-6 co-xs-12 gal-item">
                                    <div className="box">
                                        <a href="#" data-toggle="modal" data-target="#13">
                                            <img src={getGallery10()} alt="" />
                                        </a>
                                        <div className="modal fade" id="13" tabIndex="-1" role="dialog">
                                            <div className="modal-dialog" role="document">
                                                <div className="modal-content">
                                                    <button type="button" className="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
                                                    <div className="modal-body">
                                                        <img src={getGallery10()} alt="" />
                                                    </div>
                                                    <div className="col-md-12 description">
                                                        <h4>This is the 10 one on my Gallery</h4>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            {/* <div id="blog" className="blog-main pad-top-100 pad-bottom-100 parallax">
                <div className="container">
                    <div className="row">
                        <div className="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                            <h2 className="block-title text-center">
                            Our Blog 	
                        </h2>
                            <div className="blog-box clearfix">
                                <div className="wow fadeIn" data-wow-duration="1s" data-wow-delay="0.1s">
                                    <div className="col-md-6 col-sm-6">
                                        <div className="blog-block">
                                            <div className="blog-img-box">
                                                <img src={getFeature01()} alt="" />
                                                <div className="overlay">
                                                    <a href=""><i className="fa fa-link" aria-hidden="true"></i></a>
                                                </div>
                                            </div>
                                            <div className="blog-dit">
                                                <p><span>25 NOVEMBER, 2014</span></p>
                                                <h2>LATEST RECIPES JUST IN!</h2>
                                                <h5>BY John Doggett</h5>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="wow fadeIn" data-wow-duration="1s" data-wow-delay="0.1s">
                                    <div className="col-md-6 col-sm-6">
                                        <div className="blog-block">
                                            <div className="blog-img-box">
                                                <img src={getFeature02()} alt="" />
                                                <div className="overlay">
                                                    <a href=""><i className="fa fa-link" aria-hidden="true"></i></a>
                                                </div>
                                            </div>
                                            <div className="blog-dit">
                                                <p><span>2 NOVEMBER, 2014</span></p>
                                                <h2>NEW RECRUITS HAVE ARRIVED!</h2>
                                                <h5>BY Jeffrey Spender</h5>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="wow fadeIn" data-wow-duration="1s" data-wow-delay="0.1s">
                                    <div className="col-md-6 col-sm-6">
                                        <div className="blog-block">
                                            <div className="blog-img-box">
                                                <img src={getFeature03()} alt="" />
                                                <div className="overlay">
                                                    <a href=""><i className="fa fa-link" aria-hidden="true"></i></a>
                                                </div>
                                            </div>
                                            <div className="blog-dit">
                                                <p><span>4 NOVEMBER, 2014</span></p>
                                                <h2>BAKING TIPS FROM THE PROS</h2>
                                                <h5>BY Monica Reyes</h5>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="wow fadeIn" data-wow-duration="1s" data-wow-delay="0.1s">
                                    <div className="col-md-6 col-sm-6">
                                        <div className="blog-block">
                                            <div className="blog-img-box">
                                                <img src={getFeature04()} alt="" />
                                                <div className="overlay">
                                                    <a href=""><i className="fa fa-link" aria-hidden="true"></i></a>
                                                </div>
                                            </div>
                                            <div className="blog-dit">
                                                <p><span>12 NOVEMBER, 2014</span></p>
                                                <h2>ALL YOUR EGGS BELONG TO US</h2>
                                                <h5>BY John Doggett</h5>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div className="blog-btn-v">
                                <a className="hvr-underline-from-center" href="#">View the Blog</a>
                            </div>

                        </div>
                    </div>
                </div>
            </div> */}

            {/* <div id="pricing" className="pricing-main pad-top-100 pad-bottom-100">
                <div className="container">
                    <div className="row">
                        <div className="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                            <h2 className="block-title text-center">
                                Pricing 	
                            </h2>
                            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ut orci varius, elementum lectus nec, aliquam lectus. Duis neque augue, maximus in sapien ut, porta pharetra odio.</p>
                        </div>
                        <div className="panel-pricing-in">
                            <div className="col-md-4 col-sm-4 text-center">
                                <div className="panel panel-pricing">
                                    <div className="panel-heading">
                                        <div className="pric-icon">
                                            <img src={getStore()} alt="" />
                                        </div>
                                        <h3>Basic</h3>
                                    </div>
                                    <div className="panel-body text-center">
                                        <p><strong>$30/<span>Month</span></strong></p>
                                    </div>
                                    <ul className="list-group text-center">
                                        <li className="list-group-item"><i className="fa fa-check"></i> One Website</li>
                                        <li className="list-group-item"><i className="fa fa-check"></i> One User</li>
                                        <li className="list-group-item"><i className="fa fa-check"></i> 10 GB Bandwidth</li>
                                        <li className="list-group-item"><i className="fa fa-times"></i> 2GB Storage</li>
                                        <li className="list-group-item"><i className="fa fa-times"></i> Offline work</li>
                                        <li className="list-group-item"><i className="fa fa-check"></i> 24x7 Support</li>
                                    </ul>
                                    <div className="panel-footer">
                                        <a className="btn btn-lg btn-block hvr-underline-from-center" href="#">Purchase Now!</a>
                                    </div>
                                </div>
                            </div>
                            <div className="col-md-4 col-sm-4 text-center">
                                <div className="panel panel-pricing">
                                    <div className="panel-heading">
                                        <div className="pric-icon">
                                            <img src={getFood()} alt="" />
                                        </div>
                                        <h3>Pro</h3>
                                    </div>
                                    <div className="panel-body text-center">
                                        <p><strong>$60/<span>Month</span></strong></p>
                                    </div>
                                    <ul className="list-group text-center">
                                        <li className="list-group-item"><i className="fa fa-check"></i> One Website</li>
                                        <li className="list-group-item"><i className="fa fa-check"></i> One User</li>
                                        <li className="list-group-item"><i className="fa fa-check"></i> 50 GB Bandwidth</li>
                                        <li className="list-group-item"><i className="fa fa-check"></i> 2GB Storage</li>
                                        <li className="list-group-item"><i className="fa fa-check"></i> Offline work</li>
                                        <li className="list-group-item"><i className="fa fa-check"></i> 24x7 Support</li>
                                    </ul>
                                    <div className="panel-footer">
                                        <a className="btn btn-lg btn-block hvr-underline-from-center" href="#">Purchase Now!</a>
                                    </div>
                                </div>
                            </div>
                            <div className="col-md-4 col-sm-4 text-center">
                                <div className="panel panel-pricing">
                                    <div className="panel-heading">
                                        <div className="pric-icon">
                                            <img src={getCoffee()} alt="" />
                                        </div>
                                        <h3>Platinum</h3>
                                    </div>
                                    <div className="panel-body text-center">
                                        <p><strong>$90/<span>Month</span></strong></p>
                                    </div>
                                    <ul className="list-group text-center">
                                        <li className="list-group-item"><i className="fa fa-check"></i> One Website</li>
                                        <li className="list-group-item"><i className="fa fa-check"></i> One User</li>
                                        <li className="list-group-item"><i className="fa fa-check"></i> 100 GB Bandwidth</li>
                                        <li className="list-group-item"><i className="fa fa-check"></i> 2GB Storage</li>
                                        <li className="list-group-item"><i className="fa fa-check"></i> Offline work</li>
                                        <li className="list-group-item"><i className="fa fa-check"></i> 24x7 Support</li>
                                    </ul>
                                    <div className="panel-footer">
                                        <a className="btn btn-lg btn-block hvr-underline-from-center" href="#">Purchase Now!</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div> */}

            <div id="reservation" className="reservations-main pad-top-100 pad-bottom-100">
                <div className="container">
                    <div className="row">
                        <div className="form-reservations-box">
                            <div className="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                                <div className="wow fadeIn" data-wow-duration="1s" data-wow-delay="0.1s">
                                    <h2 className="block-title text-center">
                                        Reservations			
                                    </h2>
                                </div>
                                <h4 className="form-title">BOOKING FORM</h4>
                                <p>PLEASE FILL OUT ALL REQUIRED* FIELDS. THANKS!</p>

                                <form id="contact-form" method="post" className="reservations-box" name="contactform" action="mail.php">
                                    <div className="col-lg-6 col-md-6 col-sm-6 col-xs-12 px-3">
                                        <div className="form-box">
                                            <input type="text" name="form_name" id="form_name" placeholder="Name" required="required" data-error="Firstname is required."/>
                                        </div>
                                    </div>
                                    <div className="col-lg-6 col-md-6 col-sm-6 col-xs-12 px-3">
                                        <div className="form-box">
                                            <input type="email" name="email" id="email" placeholder="E-Mail ID" required="required" data-error="E-mail id is required."/>
                                        </div>
                                    </div>
                                    <div className="col-lg-6 col-md-6 col-sm-6 col-xs-12 px-3">
                                        <div className="form-box">
                                            <input type="text" name="phone" id="phone" placeholder="contact no."/>
                                        </div>
                                    </div>
                                    <div className="col-lg-6 col-md-6 col-sm-6 col-xs-12 px-3">
                                        <div className="form-box">
                                            <select name="no_of_persons" id="no_of_persons" className="selectpicker">
                                                <option selected disabled>No. Of persons</option>
                                                <option>1</option>
                                                <option>2</option>
                                                <option>3</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div className="col-lg-6 col-md-6 col-sm-6 col-xs-12 px-3">
                                        <div className="form-box">
                                            <input type="text" name="date-picker" id="date-picker" placeholder="Date" required="required" data-error="Date is required." />
                                        </div>
                                    </div>
                                    <div className="col-lg-6 col-md-6 col-sm-6 col-xs-12 px-3">
                                        <div className="form-box">
                                            <input type="text" name="time-picker" id="time-picker" placeholder="Time" required="required" data-error="Time is required." />
                                        </div>
                                    </div>
                                    <div className="col-lg-6 col-md-6 col-sm-6 col-xs-12 px-3">
                                        <div className="form-box">
                                            <select name="preferred_food" id="preferred_food" className="selectpicker">
                                                <option selected disabled>preferred food</option>
                                                <option>Indian</option>
                                                <option>Continental</option>
                                                <option>Mexican</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div className="col-lg-6 col-md-6 col-sm-6 col-xs-12 px-3">
                                        <div className="form-box">
                                            <select name="occasion" id="occasion" className="selectpicker">
                                                <option selected disabled>Occasion</option>
                                                <option>Wedding</option>
                                                <option>Birthday</option>
                                                <option>Anniversary</option>
                                            </select>
                                        </div>
                                    </div>

                                    <div className="col-lg-12 col-md-12 col-sm-12 col-xs-12 px-3">
                                        <div className="reserve-book-btn text-center">
                                            <button className="hvr-underline-from-center" type="submit" value="SEND" id="submit">BOOK MY TABLE </button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div id="footer" className="footer-main">
                <div className="footer-news pad-top-100 pad-bottom-70 parallax">
                    <div className="container">
                        <div className="row">
                            <div className="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                                <div className="wow fadeIn" data-wow-duration="1s" data-wow-delay="0.1s">
                                    <h2 className="ft-title color-white text-center"> Newsletter </h2>
                                    <p> Lorem ipsum dolor sit amet, consectetur adipiscing elit</p>
                                </div>
                                <form>
                                    <input type="email" placeholder="Enter your e-mail id"/>
                                    <a href="#" className="orange-btn"><i className="fa fa-paper-plane-o" aria-hidden="true"></i></a>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>

                <Footer/>
            </div>
        </>
    )
}