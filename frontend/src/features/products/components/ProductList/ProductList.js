import ProductCard from "../ProductCard/ProductCard";
import Pagination from "../../../../shared/components/Pagination/Pagination";

export default function ProductList({ products, totalPages, currentPage }) {
    return (
        <section className="my-5 bg-overlay">
            <div className="container-fluid" style={{ minHeight: "50vh" }}>
                <div className="row">
                    {products.map((product) => (
                        <div className="col-lg-3 col-md-6 mb-5 d-flex" key={product.productId}>
                            <ProductCard product={product} />
                        </div>
                    ))}
                </div>
                {totalPages > 1 && (
                    <Pagination
                        currentPage={currentPage}
                        totalPages={totalPages}
                    />
                )}
            </div>
        </section>
    )
}