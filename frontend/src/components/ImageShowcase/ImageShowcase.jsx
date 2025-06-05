import { getImageSrc } from "../../utils/imageUtils";
import styles from "./ImageShowcase.module.css";

export default function ImageShowcase({ imageUrl }) {
  return (
    <div className={styles["product-imgs"]}>
      <div className={styles["img-display"]}>
        <div className={styles["img-showcase"]}>
          <img
            className="rounded"
            src={getImageSrc(imageUrl)}
          />
        </div>
      </div>
    </div>
  );
}
