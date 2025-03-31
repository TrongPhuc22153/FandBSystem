export function getNotifications(){
    const notifications = [
        {
            user: { name: "John Doe", imageUrl: "path/to/image1.jpg" },
            action: "liked your post",
            target: "Post Title",
            timestamp: 1,
            isRead: false
        },
        {
            user: { name: "Jane Smith", imageUrl: "path/to/image2.jpg" },
            action: "commented on your photo",
            target: "Photo Title",
            timestamp: 2,
            isRead: false
        },
        {
            user: { name: "Alice Brown", imageUrl: "path/to/image3.jpg" },
            action: "shared your post",
            target: "Post Title",
            isRead: true
        },
        {
            user: { name: "Bob White", imageUrl: "path/to/image4.jpg" },
            action: "mentioned you in a comment",
            target: "Comment Text",
            isRead: true
        },
    ];

    // const readNotifications = [
    //     {
    //         user: { name: "Alice Brown", imageUrl: "path/to/image3.jpg" },
    //         action: "shared your post",
    //         target: "Post Title",
    //     },
    //     {
    //         user: { name: "Bob White", imageUrl: "path/to/image4.jpg" },
    //         action: "mentioned you in a comment",
    //         target: "Comment Text",
    //     },
    // ];
    return notifications;
}