export function getProductsCart(){
    const initialCartItems = [
        {
            id: 1,
            name: "Burger",
            category: "Fast Food",
            quantity: 1,
            price: 5.99,
            image: "https://i.imgur.com/2DsA49b.jpg",
        },
        {
            id: 2,
            name: "Pizza",
            category: "Italian",
            quantity: 1,
            price: 8.50,
            image: "https://i.imgur.com/Oj1iQUX.jpg",
        },
    ];
    return initialCartItems;
}