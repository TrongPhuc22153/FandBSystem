import { Link } from "react-router";
import { RESERVATION_URI } from "../../constants/WebPageURI";

export default function Banner() {
  return (
    <div id="banner" className="banner full-screen-mode parallax">
      <div className="container pr">
        <div className="col-lg-12 col-md-12 col-sm-12 col-xs-12">
          <div className="banner-static">
            <div className="banner-text">
              <div className="banner-cell">
                <h1>
                  Dinner with us{" "}
                  <span
                    className="typer"
                    id="some-id"
                    data-delay="200"
                    data-delim=":"
                    data-words="Friends:Family:Officemates"
                    data-colors="red"
                  ></span>
                  <span
                    className="cursor"
                    data-cursorDisplay="_"
                    data-owner="some-id"
                  ></span>
                </h1>
                <h2>Accidental appearances </h2>
                <p>
                  Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed
                  diem nonummy nibh euismod
                </p>
                <div className="book-btn">
                  <Link
                    to={RESERVATION_URI}
                    className="table-btn hvr-underline-from-center"
                  >
                    Book my Table
                  </Link>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
