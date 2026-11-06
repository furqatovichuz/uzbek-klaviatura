# Handoff: Yangi o'zbek klaviaturasi (HeliBoard fork)

Branch: `uzbek-klaviatura`. Topshiriq: [TZ-uzbek-klaviatura.md](TZ-uzbek-klaviatura.md). Asos: HeliBoard (GPL v3), remote `upstream`.

## Qilingan ishlar
1. **Brending** — `applicationId = uz.klavish.keyboard`, nom "Yangi o'zbek klaviaturasi", versiya 1.0.0 (code 10000), APK nomi `YangiOzbekKlaviaturasi_*`, clipboard/gesture provider authority'lar `uz.klavish.keyboard.*` (asl HeliBoard bilan to'qnashmasligi uchun). `namespace` va paket nomlari o'zgarmagan.
2. **Yangi alifbo layouti** — `assets/layouts/main/uzbek_new.json`; `method.xml` da `uz-UZ-newlat` subtype (`subtypeId 0x5e0a1b3c`); `locale_key_texts/uz-UZ-newlat.txt`; `uz.txt` dan `ş ç ğ ö` popup'lari olib tashlandi; til nomi `LocaleUtils.kt` + `donottranslate.xml`.
3. **Locale qarori (spike)** — Variant A: yangi alifbo alohida variantli locale `uz-UZ-newlat` (alohida lug'at va popup fayli). Asos: kesh papkasi va `main_<tag>.dict` `toLanguageTag()` bo'yicha tanlanadi.
4. **Konvertor** — `latin/utils/UzbekAlphabetConverter.kt` + `src/test/.../UzbekAlphabetConverterTest.kt` (6 test o'tdi). Upstream `InputTest.kt` dagi bitta kompilyatsiya xatosi (`?.`) tuzatildi.
5. **Toolbar tugmasi** `UZBEK_CONVERT` — `KeyCode -10055`, `ToolbarUtils.kt`, `KeyboardIconsSet.kt`, `ic_uzbek_convert.xml`, `RichInputConnection.convertSelectionOrAllText` (bitta batch), `InputLogic.convertUzbekAlphabet`. Parol maydonida ishlamaydi; maqsad alifbo = faol locale (`newlat` -> yangi, aks holda eski).
6. **Gesture data o'chirildi** — Sozlamalardagi bo'lim va eslatma dialogi olib tashlandi, `gesture_data_mail` bo'sh. Ichki kod saqlangan (upstream merge oson bo'lishi uchun).
7. **Lug'atlar** — `assets/dicts/main_uz-UZ.dict`, `main_uz-UZ-newlat.dict` (87 412 so'z, chastota tekis). Manba: uzbek-net/uz-hunspell (GPL-3.0), kelib chiqishi manba repoda yozilmagan. Yasash: `tools/uzbek-dict/README.md`.

## Tekshirilmagan (keyingi kompda birinchi navbatda)
- Emulyator/qurilmada: layout (360dp da 3-qator), Shift `ş→Ş`, `I` hech qachon `İ` emas, til sozlamalarida ikkita o'zbek variant, toolbar tugmasi (tanlangan matn / butun maydon / undo), parol maydoni.
- Lug'atlar ilovada yuklanishi va so'z takliflari (`uz-UZ` va `uz-UZ-newlat` aralashmasligi), spellchecker va til nomi ko'rinishi.
- Test natijasi (JDK 21 bilan, 199 test, 19 xato): 14 tasi fork'dan oldingi upstream commitda (`2d9869b2`, faqat `InputTest.kt` kompilyatsiya tuzatishi bilan) ham xuddi shunday yiqiladi (`ParserTest`, `InputTest`, `CharacterIteratorTest`, `StringUtilsTest`, `InputLogicTest`). 3 ta `XLinkTest` ham upstream'da yiqiladi (tarmoq/havolalar). Yana 2 ta (`layoutsLinksInternal`, `otherLinks`) `Links.GITHUB` bizning private repoga 404 bergani uchun yiqiladi; repo public bo'lgach o'tishi kerak (`layoutsLinksInternal` `blob/main/` ishlatadi, bizning asosiy branch `uzbek-klaviatura` — keyin ko'rib chiqish kerak).

## Qilinishi kerak
- Ikonka (hozir HeliBoard ikonkasi). About: `Links.GITHUB`/`LICENSE` endi `furqatovichuz/uzbek-klaviatura`; wiki/discussions/community havolalari upstream'da qoldi (`Links.UPSTREAM_GITHUB`). Attributsiya `app_slogan` da bor.
- Lug'at sifati: affiks kengaytirish (`tools/uzbek-dict/expand_hunspell.py --depth 1+` juda sekin va xotirani ko'p oladi, optimallashtirish kerak) va haqiqiy chastotalar.
- Release imzolash (keystore repoga kirmaydi), Play Console maxfiylik siyosati.
- Repo ochiq (public) bo'lishi kerak: GPL v3 manba kodini foydalanuvchilarga ochishni talab qiladi (hozir private).

## Build
Windows (shu kompda tekshirilgan): wrapper'ning Gradle 9.7.1 yuklanmaydi (JDK sertifikat zanjirini tanimaydi) va Robolectric uchun JDK 21 kerak. Ishlagan variant: keshdagi Gradle 9.4.1, `JAVA_HOME=~/.jdks/jdk-21.0.12.1+1`, `JAVA_TOOL_OPTIONS=-Djavax.net.ssl.trustStoreType=Windows-ROOT`, `ANDROID_HOME=%LOCALAPPDATA%\Android\Sdk`. `assembleDebug` muvaffaqiyatli (APK: `app/build/outputs/apk/debug/YangiOzbekKlaviaturasi_1.0.0-debug.apk`); birlashgan debug manifestda `INTERNET` yo'q.

```bash
export ANDROID_HOME=$HOME/Library/Android/sdk   # NDK 28.0.13004108 avtomatik o'rnatiladi (~1 GB)
./gradlew assembleDebug
./gradlew testDebugUnitTest --tests '*UzbekAlphabetConverterTest*'
```
