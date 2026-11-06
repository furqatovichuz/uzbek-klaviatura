// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.latin.utils

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

/**
 * Ilova interfeysi tizim tilidan qat'i nazar har doim o'zbekcha (values-uz) bo'ladi.
 * Klaviatura tillari (subtype) bunga ta'sir qilmaydi.
 */
object UiLocale {
    val UZBEK: Locale = Locale.forLanguageTag("uz")

    /** Activity uchun: attachBaseContext ichida chaqiring. */
    @JvmStatic
    fun wrap(base: Context): Context {
        val config = Configuration(base.resources.configuration)
        config.setLocale(UZBEK)
        return base.createConfigurationContext(config)
    }

    /** Service va Application uchun: resurslarni o'zbek tiliga o'tkazadi (kontekst almashmaydi). */
    @JvmStatic
    @Suppress("DEPRECATION")
    fun applyTo(context: Context) {
        val resources = context.resources
        val config = Configuration(resources.configuration)
        config.setLocale(UZBEK)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}
