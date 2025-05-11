import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faEye,
  faPenToSquare,
  faTrash,
  faToggleOn,
  faToggleOff,
} from "@fortawesome/free-solid-svg-icons";

const DataTable = ({
  data,
  columns,
  selectedItems,
  handleSelectItem,
  handleSelectAll,
  handleDeleteItem,
  handleUpdateItem,
  hadnleViewItem,
  handleToggleItem,
  deleteLoading,
  uniqueIdKey = "id",
  toggleAttribute = "isActive",
}) => {
  return (
    <div className="table-responsive" style={{ minHeight: "200px" }}>
      <table className="table table-hover my-0">
        <thead>
          <tr>
            <th>
              <div className="form-check">
                <input
                  className="form-check-input"
                  style={{ width: "20px", height: "20px", padding: "0" }}
                  type="checkbox"
                  onChange={handleSelectAll}
                />
              </div>
            </th>
            {columns.map((column) => (
              <th key={column.key}>{column.title}</th>
            ))}
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {data.map((item) => (
            <tr
              key={item[uniqueIdKey]}
              className={
                selectedItems.includes(item[uniqueIdKey]) ? "table-active" : ""
              }
            >
              <td>
                <div className="form-check">
                  <input
                    className="form-check-input"
                    style={{ width: "20px", height: "20px", padding: "0" }}
                    type="checkbox"
                    value={item[uniqueIdKey]}
                    checked={selectedItems.includes(item[uniqueIdKey])}
                    onChange={(e) => handleSelectItem(e, item[uniqueIdKey])}
                  />
                </div>
              </td>
              {columns.map((column) => (
                <td key={`${item[uniqueIdKey]}-${column.key}`}>
                  {column.render ? column.render(item) : item[column.key]}
                </td>
              ))}
              <td>
                <div className="d-flex gap-2">
                  {hadnleViewItem && (
                    <button
                      className="btn btn-warning"
                      onClick={() => hadnleViewItem(item[uniqueIdKey])}
                    >
                      <FontAwesomeIcon icon={faEye} />
                    </button>
                  )}
                  {handleUpdateItem && (
                    <button
                      className="btn btn-info"
                      onClick={() => handleUpdateItem(item[uniqueIdKey])}
                    >
                      <FontAwesomeIcon icon={faPenToSquare} />
                    </button>
                  )}
                  {handleDeleteItem && (
                    <button
                      className="btn btn-danger"
                      onClick={() => handleDeleteItem(item[uniqueIdKey])}
                      disabled={deleteLoading}
                    >
                      <FontAwesomeIcon icon={faTrash} />
                    </button>
                  )}
                  {handleToggleItem && (
                    <button
                      className={`btn ${
                        item[toggleAttribute] ? "btn-success" : "btn-danger"
                      }`}
                      onClick={() =>
                        handleToggleItem(
                          item[uniqueIdKey],
                          item[toggleAttribute]
                        )
                      }
                    >
                      <FontAwesomeIcon
                        icon={item[toggleAttribute] ? faToggleOn : faToggleOff}
                      />
                    </button>
                  )}
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default DataTable;
