import { getLogo } from "../../services/ImageService";
import { useEffect, useRef } from "react";
import { Link } from "react-router-dom";
import { HOME_URI } from "../../constants/WebPageURI";

export default function Header(){
    const headerRef = useRef();

    useEffect(()=>{
        document.addEventListener('scroll', onScroll)

        return(()=>{
            document.removeEventListener('scroll', onScroll);
        })
    }, [])

    const onScroll = ()=>{
        if(headerRef.current){
            if (window.scrollY > 0) {
                headerRef.current.classList.add("fixed-menu");
            } else {
                headerRef.current.classList.remove("fixed-menu");
            }
        }
    }



    return(
        <div id="site-header">
            <header id="header" class="header-block-top" ref={headerRef}>
                <div class="container">
                    <div class="row">
                        <div class="main-menu">
                            <nav class="navbar navbar-default" id="mainNav">
                                <div id="navbar" class="navbar-collapse collapse show">
                                    <div class="navbar-header">
                                        {/* <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                                            <span class="sr-only">Toggle navigation</span>
                                            <span class="icon-bar"></span>
                                            <span class="icon-bar"></span>
                                            <span class="icon-bar"></span>
                                        </button> */}
                                        <div class="logo">
                                            <Link class="navbar-brand js-scroll-trigger logo-header" to={HOME_URI}>
                                                <img src={getLogo()} alt=""/>
                                            </Link>
                                        </div>
                                    </div>
                                    <ul class="nav navbar-nav navbar-right">
                                        <li class="active"><Link to={HOME_URI}>Home</Link></li>
                                        <li><Link>About us</Link></li>
                                        <li><Link>Menu</Link></li>
                                        <li><Link>Team</Link></li>
                                        <li><Link>Gallery</Link></li>
                                        <li><Link>Reservaion</Link></li>
                                        <li><Link>Contact us</Link></li>
                                    </ul>
                                </div>
                            </nav>
                        </div>
                    </div>
                </div>
            </header>
        </div>
    )
}