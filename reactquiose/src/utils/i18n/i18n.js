import i18n from "i18next";
import { initReactI18next } from "react-i18next";
import enLang  from './locales/en/en.json';
import frLang  from './locales/fr/fr.json';
import Backend from 'i18next-http-backend';
import LanguageDetector from 'i18next-browser-languagedetector';

const resources = {
    en: {
        translation: enLang
    },
    fr: {
        translation: frLang
    }
};

i18n
    .use(LanguageDetector)
    .use(initReactI18next)// passes i18n down to react-i18next
    .use(Backend)
    .init({
        resources,
        fallbackLng: 'en',
        interpolation: {
            escapeValue: false // react already safes from xss
        },
        detection: {
            order: ['navigator', 'htmlTag', 'path', 'subdomain'],
            caches: ['localStorage', 'cookie']
        }
    });

export default i18n;