package com.buggy.lunga.data.models

data class Language(
    val code: String,
    val name: String,
    val nativeName: String,
    val flag: String
) {
    companion object {
        fun getSupportedLanguages(): List<Language> = listOf(
            Language("en", "English", "English", "🇺🇸"),
            Language("es", "Spanish", "Español", "🇪🇸"),
            Language("lt", "Lithuanian", "Lietuvių", "🇱🇹"),
            Language("hi", "Hindi", "हिन्दी", "🇮🇳"),
            Language("fr", "French", "Français", "🇫🇷"),
            Language("de", "German", "Deutsch", "🇩🇪"),
            Language("it", "Italian", "Italiano", "🇮🇹"),
            Language("pt", "Portuguese", "Português", "🇵🇹"),
            Language("ru", "Russian", "Русский", "🇷🇺"),
            Language("ja", "Japanese", "日本語", "🇯🇵"),
            Language("ko", "Korean", "한국어", "🇰🇷"),
            Language("zh", "Chinese", "中文", "🇨🇳"),
            Language("ar", "Arabic", "العربية", "🇸🇦"),
            Language("bn", "Bengali", "বাংলা", "🇧🇩"),
            Language("ur", "Urdu", "اردو", "🇵🇰"),
            Language("ta", "Tamil", "தமிழ்", "🇮🇳"),
            Language("te", "Telugu", "తెలుగు", "🇮🇳"),
            Language("mr", "Marathi", "मराठी", "🇮🇳"),
            Language("gu", "Gujarati", "ગુજરાતી", "🇮🇳"),
            Language("kn", "Kannada", "ಕನ್ನಡ", "🇮🇳"),
            Language("ml", "Malayalam", "മലയാളം", "🇮🇳")
        )

        fun getLanguageByCode(code: String): Language? {
            return getSupportedLanguages().find { it.code == code }
        }

        fun getLanguageByName(name: String): Language? {
            return getSupportedLanguages().find {
                it.name.equals(name, ignoreCase = true) ||
                        it.nativeName.equals(name, ignoreCase = true)
            }
        }
    }
}
