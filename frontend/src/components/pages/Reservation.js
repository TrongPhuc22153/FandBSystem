import { ReservationForm } from '../layouts/ReservationForm'

export default function ReservationComponent(){
    return(
        <>
            <section id="home" className="welcome-hero">
                <div className="container">
                    <div className="welcome-hero-txt">
                        <h2 className="block-title text-center">Reservations</h2>
                        <h4 className="form-title">BOOKING FORM</h4>
                        <p className='m-0'>PLEASE FILL OUT ALL REQUIRED* FIELDS. THANKS!</p>
                    </div>
                </div>
            </section>

            <ReservationForm/>
        </>
    )
}