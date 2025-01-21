package com.bcit.myminiapp.data

enum class Endpoints(val url: String) {
    BASE_URL("https://x-colors.yurace.pro/api"),
    GENERATE_RANDOM_COLOR("${BASE_URL.url}/random"),
    GENERATE_COLORS("${GENERATE_RANDOM_COLOR.url}?number=%s"),

    CONVERT_CODE("${BASE_URL.url}/%s2%s?value=%s");

    fun format(str: String?): String {
        if (str == null) return ""
        return url.format(str)
    }
}