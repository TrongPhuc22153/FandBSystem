import { useState, useEffect } from 'react';
import { getCategories } from '../../api/CategoryAPI';

const initialFilterData = {
    cuisines: {
        Chinese: true,
        Italian: false,
        Mexican: false,
        Thai: false,
        Gujarati: false,
        Panjabi: false,
        SouthIndian: false,
    },
    priceRange: {
        min: 100,
        max: 10000,
    },
    services: {
        Breakfast: true,
        Lunch: false,
        Donner: false,
        Cafe: false,
        Brunch: false,
        Other: false,
    },
};

export default function FilterSidebar() {
    const [filterData, setFilterData] = useState(initialFilterData);
    const [categories, setCategories] = useState([]);

    useEffect(() => {
        fetchCategories();
    }, []);

    const fetchCategories = async () => {
        try {
            const data = await getCategories();
            setCategories(data);
        } catch (error) {
            console.error("Failed to fetch categories:", error);
        }
    };

    const handleCheckboxChange = (category, key) => {
        setFilterData((prev) => ({
            ...prev,
            [category]: {
                ...prev[category],
                [key]: !prev[category][key],
            },
        }));
    };

    return (
        <div className="sidebar px-0">
            <h1 className="border-bottom filter-header d-flex d-md-none p-3 mb-0 align-items-center">
                <span className="mr-2 filter-close-btn">X</span>
                    Filters
                <span className="ml-auto text-uppercase">Reset Filters</span>
            </h1>
            <div className="sidebar__inner">
                <div className="filter-body">
                    <div>
                        <h2 className="border-bottom filter-title">Search</h2>
                        <div className="mb-3 filter-options">
                            <form className="form-inline my-2 my-lg-0 d-flex">
                                <input className="form-control mr-sm-2 me-2" type="search" placeholder="Search" aria-label="Search"/>
                                <button className="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
                            </form>
                        </div>
                        <h2 className="font-xbold body-font border-bottom filter-title">Categories</h2>
                        <div className="mb-3 filter-options" id="category-options">
                            {categories.map((category) => (
                                <div className="custom-control custom-checkbox mb-3" key={category.id}>
                                    <input
                                        type="checkbox"
                                        className="custom-control-input"
                                        id={category.name}
                                        checked={filterData.cuisines[category.name] || false}
                                        onChange={() => handleCheckboxChange('cuisines', category.name)}
                                    />
                                    <label className="custom-control-label" htmlFor={category.name}>
                                        {category.name.charAt(0).toUpperCase() + category.name.slice(1)}
                                    </label>
                                </div>
                            ))}
                        </div>
                        <h2 className="font-xbold body-font border-bottom filter-title">Price Range</h2>
                        <div className="mb-3 theme-clr xs2-font d-flex justify-content-between">
                            <span id="slider-range-value1">${filterData.priceRange.min}</span>
                            <span id="slider-range-value2">${filterData.priceRange.max}</span>
                        </div>
                        <div className="mb-30 filter-options">
                            <div>
                                <div id="slider-range">
                                    <form>
                                        <div className="form-group">
                                            <input
                                                type="range"
                                                className="form-control-range"
                                                min="100"
                                                max="10000"
                                                value={filterData.priceRange.min}
                                                onChange={(e) =>
                                                    setFilterData((prev) => ({
                                                        ...prev,
                                                        priceRange: {
                                                            ...prev.priceRange,
                                                            min: parseInt(e.target.value, 10),
                                                        },
                                                    }))
                                                }
                                            />
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                        <h2 className="border-bottom filter-title">Services</h2>
                        <div className="mb-3 filter-options" id="services-options">
                            {Object.keys(filterData.services).map((service) => (
                                <div className="custom-control custom-checkbox mb-3" key={service}>
                                    <input
                                        type="checkbox"
                                        className="custom-control-input"
                                        id={service}
                                        checked={filterData.services[service]}
                                        onChange={() => handleCheckboxChange('services', service)}
                                    />
                                    <label className="custom-control-label" htmlFor={service}>
                                        {service}
                                    </label>
                                </div>
                            ))}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
