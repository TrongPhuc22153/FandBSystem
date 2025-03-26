import { getMenuItemThumbnail1, getMenuItemThumbnail2, getMenuItemThumbnail3, getMenuItemThumbnail4, getSpecialMenu1, getSpecialMenu2, getSpecialMenu3 } from "../services/ImageService";

export const getProductsPerMenu = ()=>{
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
    ]
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