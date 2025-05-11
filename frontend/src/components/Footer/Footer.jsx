import React from "react";
import Logo from "../Logo/Logo";

function Footer() {
  return (
    <footer className="py-5">
      <div className="container-fluid">
        <div className="row">
          <div className="col-lg-2 col-md-6 col-sm-6">
            <div className="footer-menu">
              <Logo />
            </div>
          </div>
        </div>
      </div>
    </footer>
  );
}

export default Footer;
