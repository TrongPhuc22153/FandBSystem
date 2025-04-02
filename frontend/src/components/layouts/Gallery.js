import {
  getGallery01,
  getGallery02,
  getGallery03,
  getGallery04,
  getGallery05,
  getGallery06,
  getGallery07,
  getGallery08,
  getGallery09,
  getGallery10,
} from "../../services/ImageService";

export function Gallery() {
  return (
    <div id="gallery" className="gallery-main pad-top-100 pad-bottom-100">
      <div className="container">
        <div className="row">
          <div className="col-lg-12 col-md-12 col-sm-12 col-xs-12">
            <div
              className="wow fadeIn"
              data-wow-duration="1s"
              data-wow-delay="0.1s"
            >
              <h2 className="block-title text-center">Our Gallery</h2>
              <p className="title-caption text-center">
                There are many variations of passages of Lorem Ipsum available{" "}
              </p>
            </div>
            <div className="gal-container clearfix">
              <div className="col-md-8 col-sm-12 co-xs-12 gal-item">
                <div className="box">
                  <a href="#" data-toggle="modal" data-target="#1">
                    <img src={getGallery01()} alt="" />
                  </a>
                  <div
                    className="modal fade"
                    id="1"
                    tabIndex="-1"
                    role="dialog"
                  >
                    <div className="modal-dialog" role="document">
                      <div className="modal-content">
                        <button
                          type="button"
                          className="close"
                          data-dismiss="modal"
                          aria-label="Close"
                        >
                          <span aria-hidden="true">×</span>
                        </button>
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
                  <div
                    className="modal fade"
                    id="2"
                    tabIndex="-1"
                    role="dialog"
                  >
                    <div className="modal-dialog" role="document">
                      <div className="modal-content">
                        <button
                          type="button"
                          className="close"
                          data-dismiss="modal"
                          aria-label="Close"
                        >
                          <span aria-hidden="true">×</span>
                        </button>
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
                  <div
                    className="modal fade"
                    id="3"
                    tabIndex="-1"
                    role="dialog"
                  >
                    <div className="modal-dialog" role="document">
                      <div className="modal-content">
                        <button
                          type="button"
                          className="close"
                          data-dismiss="modal"
                          aria-label="Close"
                        >
                          <span aria-hidden="true">×</span>
                        </button>
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
                  <div
                    className="modal fade"
                    id="4"
                    tabIndex="-1"
                    role="dialog"
                  >
                    <div className="modal-dialog" role="document">
                      <div className="modal-content">
                        <button
                          type="button"
                          className="close"
                          data-dismiss="modal"
                          aria-label="Close"
                        >
                          <span aria-hidden="true">×</span>
                        </button>
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
                  <div
                    className="modal fade"
                    id="5"
                    tabIndex="-1"
                    role="dialog"
                  >
                    <div className="modal-dialog" role="document">
                      <div className="modal-content">
                        <button
                          type="button"
                          className="close"
                          data-dismiss="modal"
                          aria-label="Close"
                        >
                          <span aria-hidden="true">×</span>
                        </button>
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
                  <div
                    className="modal fade"
                    id="9"
                    tabIndex="-1"
                    role="dialog"
                  >
                    <div className="modal-dialog" role="document">
                      <div className="modal-content">
                        <button
                          type="button"
                          className="close"
                          data-dismiss="modal"
                          aria-label="Close"
                        >
                          <span aria-hidden="true">×</span>
                        </button>
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
                  <div
                    className="modal fade"
                    id="10"
                    tabIndex="-1"
                    role="dialog"
                  >
                    <div className="modal-dialog" role="document">
                      <div className="modal-content">
                        <button
                          type="button"
                          className="close"
                          data-dismiss="modal"
                          aria-label="Close"
                        >
                          <span aria-hidden="true">×</span>
                        </button>
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
                  <div
                    className="modal fade"
                    id="11"
                    tabIndex="-1"
                    role="dialog"
                  >
                    <div className="modal-dialog" role="document">
                      <div className="modal-content">
                        <button
                          type="button"
                          className="close"
                          data-dismiss="modal"
                          aria-label="Close"
                        >
                          <span aria-hidden="true">×</span>
                        </button>
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
                  <div
                    className="modal fade"
                    id="12"
                    tabIndex="-1"
                    role="dialog"
                  >
                    <div className="modal-dialog" role="document">
                      <div className="modal-content">
                        <button
                          type="button"
                          className="close"
                          data-dismiss="modal"
                          aria-label="Close"
                        >
                          <span aria-hidden="true">×</span>
                        </button>
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
                  <div
                    className="modal fade"
                    id="13"
                    tabIndex="-1"
                    role="dialog"
                  >
                    <div className="modal-dialog" role="document">
                      <div className="modal-content">
                        <button
                          type="button"
                          className="close"
                          data-dismiss="modal"
                          aria-label="Close"
                        >
                          <span aria-hidden="true">×</span>
                        </button>
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
  );
}
