import { RecommendedMenu } from "../../layouts/Menu";
import { TodaySpecialDish } from "../../layouts/TodaySpecialDish";

import AboutUs from "../../layouts/AboutUs";
import Banner from "../../layouts/Banner";

export function DefaultHomeComponent(){
    return(
        <>
            <Banner/>

            <AboutUs/>

            <TodaySpecialDish/>

            <RecommendedMenu/>
        </>
    )
}