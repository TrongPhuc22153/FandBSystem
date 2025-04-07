import React, { useState } from "react";
import DatePicker from "react-datepicker";

export default function ReservationPage() {
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    phone: "",
    noOfPersons: "",
    date: "",
    time: new Date(),
    preferredFood: "",
    table: "",
  });
  const today = new Date();
  const currentYear = today.getFullYear();

  // Set max date to 3 months ahead
  const maxDate = new Date();
  maxDate.setMonth(maxDate.getMonth() + 3);

  // Set max date to end of current year if it's sooner
  const endOfYear = new Date(currentYear, 11, 31); // Dec 31
  if (maxDate > endOfYear) {
    maxDate.setTime(endOfYear.getTime());
  }


  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleTimeChange = (date) => {
    setFormData({ ...formData, time: date });
  }


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
                      <DatePicker
                        selected={formData.time}
                        onChange={handleTimeChange}
                        showTimeSelect
                        dateFormat="Pp"
                        minDate={today}
                        maxDate={maxDate}
                        placeholderText="Pick a date within 3 months"
                        wrapperClassName="w-100"
                        className="w-100"
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
                        name="table"
                        id="table"
                        className="selectpicker"
                        value={formData.table}
                        onChange={handleChange}
                      >
                        <option value="" disabled>
                          Table
                        </option>
                        <option value="Any">Any</option>
                        <option value="1">1</option>
                        <option value="2">2</option>
                        <option value="3">3</option>
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
