export const checkPasswordStrength = (password) => {
    if (!password) return "";
    if (password.length < 6) return "Weak";
    if (password.length < 10) return "Medium";
    return "Strong";
};