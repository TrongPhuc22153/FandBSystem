import { useState } from "react";

const product = {
    category: "Women",
    title: "Black dress for Women",
    oldPrice: 100,
    discount: 20,
    newPrice: 80,
    description: "Lorem ipsum dolor sit amet consectetur adipisicing elit. Placeat excepturi odio recusandae aliquid ad impedit autem commodi earum voluptatem laboriosam?",
    mainImage: "https://cdn.pixabay.com/photo/2015/07/24/18/40/model-858753_960_720.jpg",
    previewImages: [
        "https://cdn.pixabay.com/photo/2015/07/24/18/40/model-858754_960_720.jpg",
        "https://cdn.pixabay.com/photo/2015/07/24/18/38/model-858749_960_720.jpg",
        "https://cdn.pixabay.com/photo/2015/07/24/18/37/model-858748_960_720.jpg",
        "https://cdn.pixabay.com/photo/2015/07/24/18/39/model-858751_960_720.jpg"
    ]
};

const similarProducts = [
    {
        title: "Lovely black dress",
        price: 100,
        image: "https://source.unsplash.com/gsKdPcIyeGg"
    },
    {
        title: "Lovely Dress with patterns",
        price: 85,
        image: "https://source.unsplash.com/sg_gRhbYXhc"
    },
    {
        title: "Lovely fashion dress",
        price: 200,
        image: "https://source.unsplash.com/gJZQcirK8aw"
    },
    {
        title: "Lovely red dress",
        price: 120,
        image: "https://source.unsplash.com/qbB_Z2pXLEU"
    }
];

export default function ProductComponent() {
    const [quantity, setQuantity] = useState(1);

    const handleQuantityChange = (event) => {
        const value = Math.max(0, Math.min(5, Number(event.target.value))); // Ensure value is between 0 and 5
        setQuantity(value);
    };

    return (
        <>
            <div className="container mb-5" style={{marginTop: "8rem"}}>
                <div className="row">
                    <div className="col-md-5">
                        <div className="main-img">
                            <img className="img-fluid" src={product.mainImage} alt="Product" />
                            <div className="row my-3 previews">
                                {product.previewImages.map((image, index) => (
                                    <div className="col-md-3" key={index}>
                                        <img className="w-100" src={image} alt="Preview" />
                                    </div>
                                ))}
                            </div>
                        </div>
                    </div>

                    <div className="col-md-7">
                        <div className="main-description px-2">
                            <div className="category text-bold">
                                Category: {product.category}
                            </div>

                            <div className="product-title text-bold my-3">
                                {product.title}
                            </div>

                            <div className="price-area my-4">
                                <p className="old-price mb-1">
                                    <del>${product.oldPrice}</del> <span className="old-price-discount text-danger">({product.discount}% off)</span>
                                </p>
                                <p className="new-price text-bold mb-1">${product.newPrice}</p>
                                <p className="text-secondary mb-1">(Additional tax may apply on checkout)</p>
                            </div>

                            <div className="buttons d-flex my-5">
                                <div className="block">
                                    <a href="#" className="shadow btn custom-btn">Wishlist</a>
                                </div>
                                <div className="block">
                                    <button className="shadow btn custom-btn">Add to cart</button>
                                </div>

                                <div className="block quantity">
                                    <input
                                        type="number"
                                        className="form-control"
                                        id="cart_quantity"
                                        value={quantity}
                                        onChange={handleQuantityChange}
                                        min="0"
                                        max="5"
                                        placeholder="Enter quantity"
                                        name="cart_quantity"
                                    />
                                </div>
                            </div>

                            <div className="product-details my-4">
                                <p className="details-title text-color mb-1">Product Details</p>
                                <p className="description">{product.description}</p>
                            </div>

                            <div className="row questions bg-light p-3">
                                <div className="col-md-1 icon">
                                    <i className="fa-brands fa-rocketchat questions-icon"></i>
                                </div>
                                <div className="col-md-11 text">
                                    Have a question about our products at E-Store? Feel free to contact our representatives via live chat or email.
                                </div>
                            </div>

                            <div className="delivery my-4">
                                <p className="font-weight-bold mb-0"><span><i className="fa-solid fa-truck"></i></span> <b>Delivery done in 3 days from date of purchase</b></p>
                                <p className="text-secondary">Order now to get this product delivery</p>
                            </div>
                            <div className="delivery-options my-4">
                                <p className="font-weight-bold mb-0"><span><i className="fa-solid fa-filter"></i></span> <b>Delivery options</b></p>
                                <p className="text-secondary">View delivery options here</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div className="container similar-products my-4">
                <hr />
                <p className="display-5">Similar Products</p>

                <div className="row">
                    {similarProducts.map((product, index) => (
                        <div className="col-md-3" key={index}>
                            <div className="similar-product">
                                <img className="w-100" src={product.image} alt="Preview" />
                                <p className="title">{product.title}</p>
                                <p className="price">${product.price}</p>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </>
    );
}
