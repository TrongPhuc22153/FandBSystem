import { getMenuItemThumbnail1, getMenuItemThumbnail2, getMenuItemThumbnail3, getMenuItemThumbnail4, getSpecialMenu1, getSpecialMenu2, getSpecialMenu3 } from "../services/ImageService";

export const getProducts = ()=>{
    const productsdata = [
        {
            image: "https://dummyimage.com/300X400/000/fff",
            name: "Product A",
            description: "This is a great product.",
            price: "$10.99"
        },
        {
            image: "https://dummyimage.com/300X400/000/fff",
            name: "Product B",
            description: "High quality and reliable.",
            price: "$15.49"
        },
        {
            image: "https://dummyimage.com/300X400/000/fff",
            name: "Product C",
            description: "Perfect for everyday use.",
            price: "$8.99"
        },
        {
            image: "https://dummyimage.com/300X400/000/fff",
            name: "Product D",
            description: "A must-have item.",
            price: "$12.99"
        },
        {
            image: "https://dummyimage.com/300X400/000/fff",
            name: "Product E",
            description: "Affordable and durable.",
            price: "$9.49"
        },
        {
            image: "https://dummyimage.com/300X400/000/fff",
            name: "Product F",
            description: "Stylish and functional.",
            price: "$14.99"
        },
        {
            image: "https://dummyimage.com/300X400/000/fff",
            name: "Product G",
            description: "Top-rated by customers.",
            price: "$11.99"
        },
        {
            image: "https://dummyimage.com/300X400/000/fff",
            name: "Product H",
            description: "Innovative and unique.",
            price: "$13.49"
        },
        {
            image: "https://dummyimage.com/300X400/000/fff",
            name: "Product I",
            description: "Compact and lightweight.",
            price: "$7.99"
        },
        {
            image: "https://dummyimage.com/300X400/000/fff",
            name: "Product J",
            description: "Best value for money.",
            price: "$16.99"
        }
    ];
    return productsdata
}

export const getProductsPerMenu = ()=> {
    return [
        {
            category: "starters",
            foods: [
                {
                    name: "CHOCOLATE FUDGECAKE",
                    description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc mollis eleifend dapibus.",
                    image: getMenuItemThumbnail1(),
                    price: 8.5
                },
                {
                    name: "MIXED SALAD",
                    description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc mollis eleifend dapibus.",
                    image: getMenuItemThumbnail2(),
                    price: 8.5
                },
                {
                    name: "BBQ CHICKEN WINGS",
                    description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc mollis eleifend dapibus.",
                    image: getMenuItemThumbnail3(),
                    price: 8.5
                }
            ]
        },
        {
            category: "main dishes",
            foods: [
                {
                    name: "MEAT FEAST PIZZA",
                    description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc mollis eleifend dapibus.",
                    image: getMenuItemThumbnail4(),
                    price: 8.5
                },
                {
                    name: "CHICKEN TIKKA MASALA",
                    description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc mollis eleifend dapibus.",
                    image: getMenuItemThumbnail2(),
                    price: 8.5
                },
                {
                    name: "SPICY MEATBALLS",
                    description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc mollis eleifend dapibus.",
                    image: getMenuItemThumbnail3(),
                    price: 8.5
                }
            ]
        },
        {
            category: "deserts",
            foods: [
                {
                    name: "CHOCOLATE FUDGECAKE",
                    description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc mollis eleifend dapibus.",
                    image: getMenuItemThumbnail4(),
                    price: 8.5
                },
                {
                    name: "CHICKEN TIKKA MASALA",
                    description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc mollis eleifend dapibus.",
                    image: getMenuItemThumbnail2(),
                    price: 8.5
                },
                {
                    name: "SPICY MEATBALLS",
                    description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc mollis eleifend dapibus.",
                    image: getMenuItemThumbnail3(),
                    price: 8.5
                }
            ]
        },
        {
            category: "drinks",
            foods: [
                {
                    name: "CHOCOLATE FUDGECAKE",
                    description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc mollis eleifend dapibus.",
                    image: getMenuItemThumbnail4(),
                    price: 8.5
                },
                {
                    name: "CHICKEN TIKKA MASALA",
                    description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc mollis eleifend dapibus.",
                    image: getMenuItemThumbnail2(),
                    price: 8.5
                },
                {
                    name: "SPICY MEATBALLS",
                    description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc mollis eleifend dapibus.",
                    image: getMenuItemThumbnail3(),
                    price: 8.5
                }
            ]
        }
    ];
}

export const getSpecialDishes = ()=>{
    return [
        {
            name: 'SALMON STEAK',
            description: 'Lorem ipsum dolor sit amet, consectetur adip aliqua. Ut enim ad minim venia.',
            image: getSpecialMenu1()
        },
        {
            name: "ITALIAN PIZZA",
            description: "Lorem ipsum dolor sit amet, consectetur adip aliqua. Ut enim ad minim venia.",
            image: getSpecialMenu2()
        },
        {
            name: "VEG. ROLL",
            description: "Lorem ipsum dolor sit amet, consectetur adip aliqua. Ut enim ad minim venia.",
            image: getSpecialMenu3()
        },
        {
            name: 'SALMON STEAK',
            description: 'Lorem ipsum dolor sit amet, consectetur adip aliqua. Ut enim ad minim venia.',
            image: getSpecialMenu1()
        },
        {
            name: "ITALIAN PIZZA",
            description: "Lorem ipsum dolor sit amet, consectetur adip aliqua. Ut enim ad minim venia.",
            image: getSpecialMenu2()
        },
    ]
    
}