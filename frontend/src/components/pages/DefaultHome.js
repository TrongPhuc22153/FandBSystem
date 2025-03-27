import { RecommendedMenu } from "../layouts/Menu";
import { TodaySpecialDish } from "../layouts/TodaySpecialDish";

import AboutUs from "../layouts/AboutUs";
import { ReservationForm } from "../layouts/ReservationForm";
import { Gallery } from "../layouts/Gallery";
import Banner from "../layouts/Banner";

export function DefaultHomeComponent(){
    return(
        <>
            <Banner/>

            <AboutUs/>

            <TodaySpecialDish/>

            <RecommendedMenu/>

            <Gallery/>

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

            <ReservationForm/>
        
        </>
    )
}