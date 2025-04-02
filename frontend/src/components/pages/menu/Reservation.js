import React, { useState } from "react";

export default function ReservationComponent() {
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    phone: "",
    noOfPersons: "",
    date: "",
    time: "",
    preferredFood: "",
    occasion: "",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  return (
    <>
      <section id="home" className="welcome-hero">
        <div className="container">
          <div className="welcome-hero-txt">
            <h2 className="block-title text-center">Reservations</h2>
          </div>
        </div>
      </section>

      <div
        id="reservation"
        className="reservations-main container pad-top-100 pad-bottom-100"
      >
        <div className="container">
          <div className="row">
            <div className="form-reservations-box">
              <div className="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                <h4 className="form-title">BOOKING FORM</h4>
                <p>PLEASE FILL OUT ALL REQUIRED* FIELDS. THANKS!</p>

                <form
                  id="contact-form"
                  method="post"
                  className="reservations-box"
                  name="contactform"
                  action="mail.php"
                >
                  <div className="col-lg-6 col-md-6 col-sm-6 col-xs-12 px-3">
                    <div className="form-box">
                      <input
                        type="text"
                        name="name"
                        id="form_name"
                        placeholder="Name"
                        required="required"
                        data-error="Firstname is required."
                        value={formData.name}
                        onChange={handleChange}
                      />
                    </div>
                  </div>
                  <div className="col-lg-6 col-md-6 col-sm-6 col-xs-12 px-3">
                    <div className="form-box">
                      <input
                        type="email"
                        name="email"
                        id="email"
                        placeholder="E-Mail ID"
                        required="required"
                        data-error="E-mail id is required."
                        value={formData.email}
                        onChange={handleChange}
                      />
                    </div>
                  </div>
                  <div className="col-lg-6 col-md-6 col-sm-6 col-xs-12 px-3">
                    <div className="form-box">
                      <input
                        type="text"
                        name="phone"
                        id="phone"
                        placeholder="contact no."
                        value={formData.phone}
                        onChange={handleChange}
                      />
                    </div>
                  </div>
                  <div className="col-lg-6 col-md-6 col-sm-6 col-xs-12 px-3">
                    <div className="form-box">
                      <select
                        name="noOfPersons"
                        id="no_of_persons"
                        className="selectpicker"
                        value={formData.noOfPersons}
                        onChange={handleChange}
                      >
                        <option value="" disabled>
                          No. Of persons
                        </option>
                        <option value="1">1</option>
                        <option value="2">2</option>
                        <option value="3">3</option>
                      </select>
                    </div>
                  </div>
                  <div className="col-lg-6 col-md-6 col-sm-6 col-xs-12 px-3">
                    <div className="form-box">
                      <input
                        type="text"
                        name="date"
                        id="date-picker"
                        placeholder="Date"
                        required="required"
                        data-error="Date is required."
                        value={formData.date}
                        onChange={handleChange}
                      />
                    </div>
                  </div>
                  <div className="col-lg-6 col-md-6 col-sm-6 col-xs-12 px-3">
                    <div className="form-box">
                      <input
                        type="text"
                        name="time"
                        id="time-picker"
                        placeholder="Time"
                        required="required"
                        data-error="Time is required."
                        value={formData.time}
                        onChange={handleChange}
                      />
                    </div>
                  </div>
                  <div className="col-lg-6 col-md-6 col-sm-6 col-xs-12 px-3">
                    <div className="form-box">
                      <select
                        name="preferredFood"
                        id="preferred_food"
                        className="selectpicker"
                        value={formData.preferredFood}
                        onChange={handleChange}
                      >
                        <option value="" disabled>
                          preferred food
                        </option>
                        <option value="Indian">Indian</option>
                        <option value="Continental">Continental</option>
                        <option value="Mexican">Mexican</option>
                      </select>
                    </div>
                  </div>
                  <div className="col-lg-6 col-md-6 col-sm-6 col-xs-12 px-3">
                    <div className="form-box">
                      <select
                        name="occasion"
                        id="occasion"
                        className="selectpicker"
                        value={formData.occasion}
                        onChange={handleChange}
                      >
                        <option value="" disabled>
                          Occasion
                        </option>
                        <option value="Wedding">Wedding</option>
                        <option value="Birthday">Birthday</option>
                        <option value="Anniversary">Anniversary</option>
                      </select>
                    </div>
                  </div>

                  <div className="col-lg-12 col-md-12 col-sm-12 col-xs-12 px-3">
                    <div className="reserve-book-btn text-center">
                      <button
                        className="hvr-underline-from-center"
                        type="submit"
                        value="SEND"
                        id="submit"
                      >
                        BOOK MY TABLE{" "}
                      </button>
                    </div>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
