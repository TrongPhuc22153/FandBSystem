import React, { useState } from 'react';
import { getDefaultFood } from '../../../services/ImageService';

const menuItems = [
    {
        id: 1,
        name: "Margherita Pizza",
        price: 12.99,
        image: "https://images.unsplash.com/photo-1574071318508-1cdbab80d488?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80", // Full-size pizza image
        thumbnail: "https://images.unsplash.com/photo-1574071318508-1cdbab80d488?ixlib=rb-4.0.3&auto=format&fit=crop&w=50&h=50&q=80", // Thumbnail (cropped)
        category: "pizza",
        customizable: true,
        options: {
            size: ["Small", "Medium", "Large"],
            toppings: ["Extra Cheese", "Pepperoni", "Mushrooms", "Olives"]
        }
    },
    {
        id: 2,
        name: "Caesar Salad",
        price: 8.99,
        image: "https://images.unsplash.com/photo-1550304943-4f24f54ddde9?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80", // Full-size salad image
        thumbnail: "https://images.unsplash.com/photo-1550304943-4f24f54ddde9?ixlib=rb-4.0.3&auto=format&fit=crop&w=50&h=50&q=80", // Thumbnail (cropped)
        category: "salad",
        customizable: true,
        options: {
            dressing: ["Caesar", "Ranch", "Vinaigrette"],
            extras: ["Chicken", "Shrimp", "Croutons"]
        }
    },
    {
        id: 3,
        name: "Soft Drink",
        price: 2.99,
        image: "https://images.unsplash.com/photo-1624515833165-90fb97e41b43?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80", // Full-size soft drink image
        thumbnail: "https://images.unsplash.com/photo-1624515833165-90fb97e41b43?ixlib=rb-4.0.3&auto=format&fit=crop&w=50&h=50&q=80", // Thumbnail (cropped)
        category: "drinks",
        customizable: false
    }
];
const reservationData = {
    reservationId: "RES123456",
    customer: {
        name: "John Doe",
        phone: "+1234567890",
        email: "johndoe@example.com"
    },
    reservationDetails: {
        date: "2025-04-10",
        time: "19:30",
        numberOfGuests: 4,
        tableNumber: 12,
        specialRequests: "Window seat, birthday cake"
    },
    order: [
        {
            itemId: "FD1001",
            name: "Grilled Salmon",
            quantity: 2,
            price: 18.99
        },
        {
            itemId: "FD2003",
            name: "Caesar Salad",
            quantity: 1,
            price: 7.50
        },
        {
            itemId: "DR3002",
            name: "Lemonade",
            quantity: 4,
            price: 3.00
        }
    ],
    totalAmount: 54.48,
    payment: {
        method: "Credit Card",
        status: "Paid",
        transactionId: "TXN987654321"
    },
    status: "Confirmed"
}

const FoodReservationPage = () => {
    const [selectedItems, setSelectedItems] = useState([]);
    const [searchQuery, setSearchQuery] = useState('');
    const [categoryFilter, setCategoryFilter] = useState('all');

    // Add item to selection with default customizations
    const addItem = (item) => {
        const newItem = {
            ...item,
            quantity: 1,
            customizations: item.customizable ? {
                size: item.options.size ? item.options.size[1] : null, // Default to "Medium"
                dressing: item.options.dressing ? item.options.dressing[0] : null,
                toppings: [],
                extras: []
            } : {}
        };
        setSelectedItems([...selectedItems, newItem]);
    };

    // Update quantity
    const updateQuantity = (id, delta) => {
        setSelectedItems(selectedItems.map(item =>
            item.id === id ? { ...item, quantity: Math.max(1, item.quantity + delta) } : item
        ));
    };

    // Remove item
    const removeItem = (id) => {
        setSelectedItems(selectedItems.filter(item => item.id !== id));
    };

    // Update customization
    const updateCustomization = (id, type, value) => {
        setSelectedItems(selectedItems.map(item => {
            if (item.id === id) {
                if (type === 'toppings' || type === 'extras') {
                    const current = item.customizations[type] || [];
                    const updated = current.includes(value)
                        ? current.filter(v => v !== value)
                        : [...current, value];
                    return { ...item, customizations: { ...item.customizations, [type]: updated } };
                }
                return { ...item, customizations: { ...item.customizations, [type]: value } };
            }
            return item;
        }));
    };

    // Filter and search logic
    const filteredItems = menuItems.filter(item => {
        const matchesCategory = categoryFilter === 'all' || item.category === categoryFilter;
        const matchesSearch = item.name.toLowerCase().includes(searchQuery.toLowerCase());
        return matchesCategory && matchesSearch;
    });

    // Calculate total
    const total = selectedItems.reduce((sum, item) => sum + item.price * item.quantity, 0).toFixed(2);

    return (
        <>
            <div className="container mb-4">
                <div id="food-reservation-page">
                    <header className="mb-4">
                        <img
                            src="https://via.placeholder.com/200x100?text=Restaurant+Logo"
                            alt="Restaurant Logo"
                            className="header-img d-block mx-auto"
                        />
                        <h2 className="text-center mt-3">Customize Your Food Reservation</h2>
                    </header>

                    <div className="filter-search-section">
                        <div className="row">
                            <div className="col-md-6 mb-3">
                                <label htmlFor="search-input" className="form-label">Search:</label>
                                <input
                                    type="text"
                                    className="form-control"
                                    id="search-input"
                                    placeholder="Search menu items..."
                                    value={searchQuery}
                                    onChange={(e) => setSearchQuery(e.target.value)}
                                />
                            </div>
                            <div className="col-md-6 mb-3">
                                <label htmlFor="category-filter" className="form-label">Filter by Category:</label>
                                <select
                                    className="form-select"
                                    id="category-filter"
                                    value={categoryFilter}
                                    onChange={(e) => setCategoryFilter(e.target.value)}
                                >
                                    <option value="all">All</option>
                                    <option value="pizza">Pizza</option>
                                    <option value="salad">Salad</option>
                                    <option value="drinks">Drinks</option>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div className="row">
                        <div className="col-md-8">
                            <h3>Menu Items</h3>
                            <div className='row'>
                                {filteredItems.map((item) => (
                                    <div className='col-lg-4 d-flex' key={item.id}>
                                        <div key={item.id} className="card menu-card mb-3 w-100">
                                            <img src={item.image || getDefaultFood()} className="card-img-top" alt={item.name} />
                                            <div className="card-body d-flex flex-column justify-content-end">
                                                <h5 className="card-title">{item.name}</h5>
                                                <p className="card-text">${item.price.toFixed(2)}</p>
                                                <button
                                                    className="btn btn-primary w-100"
                                                    onClick={() => addItem(item)}
                                                >
                                                    Add to Reservation
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </div>

                        <div className="col-md-4">
                            <h3>Your Selection</h3>
                            {selectedItems.length === 0 ? (
                                <p>No items selected</p>
                            ) : (
                                selectedItems.map((item, index) => (
                                    <div key={`${item.id}-${index}`} className="card selection-card mb-3">
                                        <div className="card-body">
                                            <div className="d-flex align-items-center">
                                                <img
                                                    src={item.thumbnail}
                                                    alt={`${item.name} Thumbnail`}
                                                    className="me-3"
                                                    style={{ width: "50px", height: "50px" }}
                                                />
                                                <h5 className="card-title mb-0">{item.name}</h5>
                                            </div>
                                            <div className="d-flex align-items-center mt-2 mb-2">
                                                <button
                                                    className="btn btn-sm btn-outline-secondary"
                                                    onClick={() => updateQuantity(item.id, -1)}
                                                >-</button>
                                                <span className="mx-2">{item.quantity}</span>
                                                <button
                                                    className="btn btn-sm btn-outline-secondary"
                                                    onClick={() => updateQuantity(item.id, 1)}
                                                >+</button>
                                                <button
                                                    className="btn btn-sm btn-danger ms-auto"
                                                    onClick={() => removeItem(item.id)}
                                                >Remove</button>
                                            </div>

                                            {item.customizable && (
                                                <div className="mt-2">
                                                    {item.options.size && (
                                                        <div className="mb-2">
                                                            <label htmlFor={`size-${item.id}`}>Size:</label>
                                                            <select
                                                                className="form-select"
                                                                id={`size-${item.id}`}
                                                                value={item.customizations.size}
                                                                onChange={(e) => updateCustomization(item.id, 'size', e.target.value)}
                                                            >
                                                                {item.options.size.map(size => (
                                                                    <option key={size} value={size}>{size}</option>
                                                                ))}
                                                            </select>
                                                        </div>
                                                    )}
                                                    {item.options.dressing && (
                                                        <div className="mb-2">
                                                            <label htmlFor={`dressing-${item.id}`}>Dressing:</label>
                                                            <select
                                                                className="form-select"
                                                                id={`dressing-${item.id}`}
                                                                value={item.customizations.dressing}
                                                                onChange={(e) => updateCustomization(item.id, 'dressing', e.target.value)}
                                                            >
                                                                {item.options.dressing.map(dressing => (
                                                                    <option key={dressing} value={dressing}>{dressing}</option>
                                                                ))}
                                                            </select>
                                                        </div>
                                                    )}
                                                    {(item.options.toppings || item.options.extras) && (
                                                        <div>
                                                            <label>{item.options.toppings ? 'Toppings' : 'Extras'}:</label>
                                                            {(item.options.toppings || item.options.extras).map(option => (
                                                                <div key={option} className="form-check">
                                                                    <input
                                                                        className="form-check-input"
                                                                        type="checkbox"
                                                                        id={`${option}-${item.id}`}
                                                                        checked={item.customizations[item.options.toppings ? 'toppings' : 'extras'].includes(option)}
                                                                        onChange={() => updateCustomization(item.id, item.options.toppings ? 'toppings' : 'extras', option)}
                                                                    />
                                                                    <label className="form-check-label" htmlFor={`${option}-${item.id}`}>
                                                                        {option}
                                                                    </label>
                                                                </div>
                                                            ))}
                                                        </div>
                                                    )}
                                                </div>
                                            )}
                                        </div>
                                    </div>
                                ))
                            )}

                            {selectedItems.length > 0 && (
                                <div className="mt-3">
                                    <h4>Total: ${total}</h4>
                                    <form onSubmit={(e) => e.preventDefault() /* Add submission logic here */}>
                                        <button type="submit" className="btn btn-success w-100">Confirm Reservation</button>
                                    </form>
                                </div>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
};

export default FoodReservationPage;