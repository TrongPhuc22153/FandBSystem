import { getImageSrc } from "../../utils/imageUtils";
import styles from "./SingleImageDisplay.module.css";

export default function SingleImageDisplay({ imageUrl, altText }) {
  return (
    <div className={styles["img-display"]}>
      <div className={styles["img-preview"]}>
        {imageUrl ? (
          <img src={imageUrl} alt={altText} />
        ) : (
          <img src={getImageSrc()} alt="default preview" />
        )}
      </div>
    </div>
  );
}
