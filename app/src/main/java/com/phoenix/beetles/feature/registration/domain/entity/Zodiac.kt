package com.phoenix.beetles.feature.registration.domain.entity

enum class Zodiac(val title: String) {
    ARIES("Овен"),
    TAURUS("Телец"),
    GEMINI("Близнецы"),
    CANCER("Рак"),
    LEO("Лев"),
    VIRGO("Дева"),
    LIBRA("Весы"),
    SCORPIO("Скорпион"),
    SAGITTARIUS("Стрелец"),
    CAPRICORN("Козерог"),
    AQUARIUS("Водолей"),
    PISCES("Рыбы");

    companion object {
        fun ZodiacCalculator(day: Int, month: Int): Zodiac {
            return when (month) {
                1 -> if (day < 20) Zodiac.CAPRICORN else Zodiac.AQUARIUS
                2 -> if (day < 19) Zodiac.AQUARIUS else Zodiac.PISCES
                3 -> if (day < 21) Zodiac.PISCES else Zodiac.ARIES
                4 -> if (day < 20) Zodiac.ARIES else Zodiac.TAURUS
                5 -> if (day < 21) Zodiac.TAURUS else Zodiac.GEMINI
                6 -> if (day < 22) Zodiac.GEMINI else Zodiac.CANCER
                7 -> if (day < 23) Zodiac.CANCER else Zodiac.LEO
                8 -> if (day < 23) Zodiac.LEO else Zodiac.VIRGO
                9 -> if (day < 23) Zodiac.VIRGO else Zodiac.LIBRA
                10 -> if (day < 24) Zodiac.LIBRA else Zodiac.SCORPIO
                11 -> if (day < 23) Zodiac.SCORPIO else Zodiac.SAGITTARIUS
                12 -> if (day < 22) Zodiac.SAGITTARIUS else Zodiac.CAPRICORN
                else -> Zodiac.ARIES
            }
        }
    }
}