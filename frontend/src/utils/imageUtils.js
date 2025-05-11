
export const getImageSrc = (imageUrl) => {
  return imageUrl ? imageUrl : '/images/defaultimage.png';
};

export const getPrimaryProductImage = (images) => {
  if (images && Array.isArray(images) && images.length > 0) {
    const primaryImage = images.find(img => img.isPrimary);
    return primaryImage ? primaryImage.image : images[0].image;
  }
  return '/images/defaultimage.png';
};