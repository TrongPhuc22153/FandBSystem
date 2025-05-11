import { getImageSrc } from "../../utils/imageUtils";
import { useEffect, useState, useRef } from "react";
import styles from "./ImagesShowcase.module.css";

export default function ImagesShowcase({ uploadedImages, images }) {
  const [currentImageIndex, setCurrentImageIndex] = useState(0);
  const imgShowcaseRef = useRef(null);

  useEffect(() => {
    if (imgShowcaseRef.current && uploadedImages.length > 0) {
      const translateValue = -currentImageIndex * 100 + "%";
      imgShowcaseRef.current.style.transform = `translateX(${translateValue})`;
    }
  }, [currentImageIndex]);

  return (
    <div className={styles["product-imgs"]}>
      <div className={styles["img-display"]}>
        <div className={styles["img-showcase"]} ref={imgShowcaseRef}>
          {uploadedImages.map((image, index) => (
            <img
              key={index}
              src={image.previewURL}
              alt={`product image ${index + 1}`}
            />
          ))}
          {uploadedImages.length === 0 && images && images.length > 0 ? (
            images.map((image, index) => (
              <img
                key={`existing-${index}`}
                src={image.image}
                alt={`existing product image ${index + 1}`}
              />
            ))
          ) : (
            <img src={getImageSrc()} alt="default image" />
          )}
        </div>
      </div>
      {uploadedImages.length > 0 && (
        <div className={styles["img-select"]}>
          {uploadedImages.map((image, index) => (
            <div
              className={`${styles["img-item"]} ${
                currentImageIndex === index ? styles.active : ""
              }`}
              key={index}
              onClick={() => setCurrentImageIndex(index)}
            >
              <img
                src={image.previewURL}
                alt={`product thumbnail ${index + 1}`}
              />
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
