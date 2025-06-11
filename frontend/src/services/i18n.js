import i18n from "i18next";
import { initReactI18next } from "react-i18next";
import HttpBackend from "i18next-http-backend";
import LanguageDetector from "i18next-browser-languagedetector";

i18n
  .use(HttpBackend) // Load translations from public folder
  .use(LanguageDetector) // Detect user language from browser/localStorage/cookie
  .use(initReactI18next) // Pass to react-i18next
  .init({
    fallbackLng: "en", // Default language if none is detected or translation is missing
    supportedLngs: ["en", "vi"],
    debug: false, // Set to true for dev debugging
    interpolation: {
      escapeValue: false, // React already escapes
    },
    backend: {
      loadPath: "/locales/{{lng}}/translation.json", // Path to translation files
    },
    detection: {
      order: ["localStorage", "cookie", "navigator"],
      caches: ["localStorage"],
    },
  });

export default i18n;
