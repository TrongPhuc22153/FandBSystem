import { useNavigate, useSearchParams } from "react-router-dom"; // Import useNavigate and useSearchParams

export function Pagination({ totalPages, currentPage }) {
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useSearchParams(); // Use setSearchParams to update

  const handlePageChange = (newPage) => {
    searchParams.set('page', newPage-1);
    navigate(`?${searchParams.toString()}`);
  };

  const getPageNumbers = () => {
    const pageNumbers = [];
    const visiblePageCount = 5; // Number of visible page links (adjust as needed)
    let startPage = Math.max(1, currentPage - Math.floor(visiblePageCount / 2));
    let endPage = Math.min(totalPages, startPage + visiblePageCount - 1);

    // Adjust start page if end page is too close to the end
    if (endPage - startPage + 1 < visiblePageCount && endPage < totalPages) {
      startPage = Math.max(1, endPage - visiblePageCount + 1);
    }

    for (let i = startPage; i <= endPage; i++) {
      pageNumbers.push(i);
    }
    return pageNumbers;
  };

  const pageNumbers = getPageNumbers();

  return (
    <nav className="mt-3">
      <ul className="pagination float-end">
        <li className={`page-item ${currentPage === 1 ? 'disabled' : ''}`}>
          <button // Changed Link to button for onClick functionality
            className="page-link"
            aria-label="Previous"
            onClick={() => handlePageChange(currentPage - 1)}
            disabled={currentPage === 1}
          >
            <span aria-hidden="true">&laquo;</span>
            <span className="sr-only">Previous</span>
          </button>
        </li>

        {pageNumbers.map((pageNumber) => (
          <li
            key={pageNumber}
            className={`page-item ${currentPage === pageNumber ? 'active' : ''}`}
          >
            <button // Changed Link to button for onClick functionality
              className="page-link"
              onClick={() => handlePageChange(pageNumber)}
            >
              {pageNumber}
            </button>
          </li>
        ))}

        <li className={`page-item ${currentPage === totalPages ? 'disabled' : ''}`}>
          <button // Changed Link to button for onClick functionality
            className="page-link"
            aria-label="Next"
            onClick={() => handlePageChange(currentPage + 1)}
            disabled={currentPage === totalPages}
          >
            <span aria-hidden="true">&raquo;</span>
            <span className="sr-only">Next</span>
          </button>
        </li>
      </ul>
    </nav>
  );
}