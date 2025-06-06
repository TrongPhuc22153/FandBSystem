import styles from "./Button.module.css"

const Button = ({
  variant = "primary",
  size = "medium",
  children,
  className = "",
  ...props
}) => {
  return (
    <button className={`${styles.button} ${styles[variant]} ${styles[size]} ${className}`} {...props}>
      {children}
    </button>
  )
}

export default Button
