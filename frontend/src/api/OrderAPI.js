export function getPurchasedOrder(){
    const orderData = {
        orderNumber: "546924",
        title: "Thank you for your order!",
        paymentSummary: [
            {
                name: "iPhone XR",
                description: "128GB White",
                price: "$599",
                image: "https://i.imgur.com/qSnCFIS.png",
            },
            {
                name: "Airpods",
                description: "With charging case",
                price: "$199",
                image: "https://i.imgur.com/WuJwAJD.jpg",
            },
            {
                name: "Belkin Boost Up",
                description: "Wireless charging pad",
                price: "$49.95",
                image: "https://i.imgur.com/hOsIes2.png",
            },
        ],
        shipping: "$20.00",
        total: "$867.95", // Updated total to include shipping
    };
    return orderData; 
}