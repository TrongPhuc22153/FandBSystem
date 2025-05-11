import { IMAGE_ENDPOINT } from "../constants/api";

export const uploadImage = async ({ token, files }) => {
    const formData = new FormData();
    for (let i = 0; i < files.length; i++) {
        formData.append("files", files[i]); // Append each file with the same field name
    }

    const response = await fetch(IMAGE_ENDPOINT, {
        method: "POST",
        headers: {
            Authorization: `Bearer ${token}`,
        },
        body: formData,
    });

    if (!response.ok) {
        throw await response.json();
    }

    return response.json();
};