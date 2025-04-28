import { RecommendedMenu } from "../components/Menu";
import { TodaySpecialDish } from "../components/TodaySpecialDish";

import AboutUs from "../components/AboutUs";
import Banner from "../components/Banner";

export function DefaultHomePage() {
  
  return (
    <>
      <Banner />

      <AboutUs />

      <TodaySpecialDish />

      <RecommendedMenu />
    </>
  );
}
