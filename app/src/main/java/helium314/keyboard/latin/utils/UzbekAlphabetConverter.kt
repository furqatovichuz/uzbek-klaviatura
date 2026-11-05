// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.latin.utils

/**
 * Converts between the current Uzbek Latin alphabet (oʻ gʻ sh ch) and the new one (ö ğ ş ç).
 * Pure Kotlin without Android dependencies. Case is mapped explicitly, never via Locale
 * (a Turkish locale would turn i/I into İ/ı).
 *
 * Known limitations:
 *  - oldToNew: a separately written "s"+"h" or "c"+"h" (e.g. at a compound word boundary) is merged into ş / ç.
 *    A tutuq between them ("s'h") prevents the merge.
 *  - oldToNew: an apostrophe after o/g is always read as part of oʻ / gʻ, so rare words with a tutuq there are converted wrongly.
 *  - newToOld is deterministic.
 */
object UzbekAlphabetConverter {
    private const val O_TURN = 'ʻ' // ʻ
    private const val TUTUQ = 'ʼ' // ʼ

    // ' ʻ ʼ ‘ ’ ` ´ ′ ʹ ʽ
    private val APOSTROPHES = setOf(
        '\'', 'ʻ', 'ʼ', '‘', '’', '`', '´', '′', 'ʹ', 'ʽ'
    )

    fun oldToNew(text: String): String {
        val out = StringBuilder(text.length)
        var i = 0
        val n = text.length
        while (i < n) {
            val c = text[i]
            val next = if (i + 1 < n) text[i + 1] else null
            val upper = c.isUpperCase()
            if ((c == 'o' || c == 'O') && next != null && next in APOSTROPHES) {
                out.append(if (upper) 'Ö' else 'ö')
                i += 2
                continue
            }
            if ((c == 'g' || c == 'G') && next != null && next in APOSTROPHES) {
                out.append(if (upper) 'Ğ' else 'ğ')
                i += 2
                continue
            }
            if ((c == 's' || c == 'S') && (next == 'h' || next == 'H')) {
                out.append(if (upper) 'Ş' else 'ş')
                i += 2
                continue
            }
            if ((c == 'c' || c == 'C') && (next == 'h' || next == 'H')) {
                out.append(if (upper) 'Ç' else 'ç')
                i += 2
                continue
            }
            if (c in APOSTROPHES) {
                val prev = if (i > 0) text[i - 1] else null
                if (prev != null && next != null && prev.isLetter() && next.isLetter()) {
                    out.append(TUTUQ)
                    i++
                    continue
                }
            }
            out.append(c)
            i++
        }
        return out.toString()
    }

    fun newToOld(text: String): String {
        val out = StringBuilder(text.length + 8)
        val n = text.length
        for (i in 0 until n) {
            when (val c = text[i]) {
                'ö' -> out.append('o').append(O_TURN)
                'Ö' -> out.append('O').append(O_TURN)
                'ğ' -> out.append('g').append(O_TURN)
                'Ğ' -> out.append('G').append(O_TURN)
                'ş' -> out.append("sh")
                'ç' -> out.append("ch")
                'Ş', 'Ç' -> {
                    val base = if (c == 'Ş') 'S' else 'C'
                    val prev = if (i > 0) text[i - 1] else null
                    val next = if (i + 1 < n) text[i + 1] else null
                    val allCaps = (prev != null && prev.isLetter() && prev.isUpperCase())
                        || (next != null && next.isLetter() && next.isUpperCase())
                    out.append(base).append(if (allCaps) 'H' else 'h')
                }
                else -> out.append(c)
            }
        }
        return out.toString()
    }
}
