// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.latin

import helium314.keyboard.latin.utils.UzbekAlphabetConverter
import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals

class UzbekAlphabetConverterTest {
    private val oldToNewCases = listOf(
        "Oʻzbekiston" to "Özbekiston",
        "OʻZBEKISTON" to "ÖZBEKISTON",
        "O'zbekiston" to "Özbekiston",
        "O‘zbekiston" to "Özbekiston",
        "toʻgʻri" to "töğri",
        "bogʻ" to "boğ",
        "Gʻisht" to "Ğişt",
        "o'g'il" to "öğil",
        "shahar" to "şahar",
        "Shahar" to "Şahar",
        "SHAHAR" to "ŞAHAR",
        "choy" to "çoy",
        "Chiroyli" to "Çiroyli",
        "CHOY" to "ÇOY",
        "ishchi" to "işçi",
        "Ishchi" to "Işçi",
        "ISHCHI" to "IŞÇI",
        "Ko'chalar" to "Köçalar",
        "Toshkent shahri" to "Toşkent şahri",
        "TOSHKENT" to "TOŞKENT",
        "inglizcha" to "inglizça",
        "maʼno" to "maʼno",
        "ma'no" to "maʼno",
        "san’at" to "sanʼat",
        "mas'ul" to "masʼul",
        "s'h" to "sʼh",
        "'salom'" to "'salom'",
        "‘salom’" to "‘salom’",
        "ng" to "ng",
        "o'" to "ö",
        "G'" to "Ğ",
    )

    private val newToOldCases = listOf(
        "Özbekiston" to "Oʻzbekiston",
        "ÖZBEKISTON" to "OʻZBEKISTON",
        "töğri" to "toʻgʻri",
        "Ğişt" to "Gʻisht",
        "şahar" to "shahar",
        "Şahar" to "Shahar",
        "ŞAHAR" to "SHAHAR",
        "çoy" to "choy",
        "Çiroyli" to "Chiroyli",
        "ÇOY" to "CHOY",
        "işçi" to "ishchi",
        "Işçi" to "Ishchi",
        "IŞÇI" to "ISHCHI",
        "Toşkent şahri" to "Toshkent shahri",
        "Ş." to "Sh.",
        "maʼno" to "maʼno",
    )

    @Test fun oldToNewVectors() {
        oldToNewCases.forEach { (input, expected) -> assertEquals(expected, UzbekAlphabetConverter.oldToNew(input), "oldToNew($input)") }
    }

    @Test fun newToOldVectors() {
        newToOldCases.forEach { (input, expected) -> assertEquals(expected, UzbekAlphabetConverter.newToOld(input), "newToOld($input)") }
    }

    @Test fun roundTrip() {
        listOf(
            "Oʻzbekiston", "toʻgʻri", "Shahar", "CHOY", "maʼno", "inglizcha", "Gʻisht", "ishchi"
        ).forEach { assertEquals(it, UzbekAlphabetConverter.newToOld(UzbekAlphabetConverter.oldToNew(it)), "roundTrip($it)") }
    }

    @Test fun emptyString() {
        assertEquals("", UzbekAlphabetConverter.oldToNew(""))
        assertEquals("", UzbekAlphabetConverter.newToOld(""))
    }

    @Test fun oldTextUnchangedByNewToOld() {
        val text = "Salom, Oʻzbekiston! Shahar choy ishchi"
        assertEquals(text, UzbekAlphabetConverter.newToOld(text))
    }

    @Test fun noTurkishCasing() {
        val previous = Locale.getDefault()
        try {
            Locale.setDefault(Locale.forLanguageTag("tr-TR"))
            val result = UzbekAlphabetConverter.oldToNew("Isiq ISIQ iii Ishchi")
            assertEquals("Isiq ISIQ iii Işçi", result)
            assertEquals("Isiq ISIQ iii Ishchi", UzbekAlphabetConverter.newToOld(result))
        } finally {
            Locale.setDefault(previous)
        }
    }
}
