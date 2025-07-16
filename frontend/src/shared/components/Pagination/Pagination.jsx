import { useNavigate, useLocation } from "react-router-dom";

function Pagination({ currentPage, totalPages, onPageChange }) {
  const pageNumbers = Array.from(
    { length: totalPages },
    (_, index) => index + 1
  );
  const navigate = useNavigate();
  const location = useLocation();

  const handlePageClick = (pageNumber) => {
    const searchParams = new URLSearchParams(location.search);
    searchParams.set("page", pageNumber);
    navigate(`?${searchParams.toString()}`);
  };

  const handlePreviousClick = () => {
    if (currentPage > 1) {
      const newPage = currentPage - 1;
      const searchParams = new URLSearchParams(location.search);
      searchParams.set("page", newPage);
      navigate(`?${searchParams.toString()}`);
    }
  };

  const handleNextClick = () => {
    if (currentPage < totalPages) {
      const newPage = currentPage + 1;
      const searchParams = new URLSearchParams(location.search);
      searchParams.set("page", newPage);
      navigate(`?${searchParams.toString()}`);
    }
  };

  return (
    <nav aria-label="Page navigation">
      <ul className="pagination justify-content-center mt-5">
        <li
          className={`page-item mx-md-2 ${currentPage === 1 ? "disabled" : ""}`}
        >
          <button className="page-link" onClick={handlePreviousClick}>
            Previous
          </button>
        </li>
        {pageNumbers.map((pageNumber) => (
          <li
            key={pageNumber}
            className={`page-item mx-md-2 ${
              currentPage === pageNumber ? "active" : ""
            }`}
          >
            <button
              className="page-link"
              onClick={() => handlePageClick(pageNumber)}
            >
              {pageNumber}
            </button>
          </li>
        ))}
        <li
          className={`page-item mx-md-2 ${
            currentPage === totalPages ? "disabled" : ""
          }`}
        >
          <button className="page-link" onClick={handleNextClick}>
            Next
          </button>
        </li>
      </ul>
    </nav>
  );
}

export default Pagination;
