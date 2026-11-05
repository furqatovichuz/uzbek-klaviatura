# TZ: O'zbek klaviaturasi (yangi lotin alifbosi) — HeliBoard fork, Android

Versiya: 1.0 · Sana: 2026-11-05 · Til: o'zbekcha (texnik atamalar inglizcha)

Bu hujjat Claude Code uchun to'liq topshiriq. "TEKSHIRILGAN" belgisi — faktni men kod yoki rasmiy manbadan ko'rganman. "TEKSHIRILMAGAN" belgisi — taxmin; shunga tayanib kod yozilmaydi, avval aniqlashtiriladi.

(Nusxa: asl fayl `Downloads/TZ-uzbek-klaviatura.md` yo'qolgani uchun suhbat matnidan tiklangan; 5.5-bo'limdagi Python etalon kodi va test vektorlari `UzbekAlphabetConverterTest.kt` ga ko'chirilgan.)

---

## 0. Maqsad

Android uchun bepul klaviatura ilovasi: yangi o'zbek lotin alifbosi (Ö Ğ Ş Ç) va hozirgi alifbo bilan ishlaydi, ikki alifbo o'rtasida matnni konvertatsiya qiladi, o'zbekcha so'z taklifi (lug'at) beradi. Asos — ochiq kodli HeliBoard (GPL v3).

Sabab: yangi alifbo qonuni (O'RQ-1184, 2026-yil 21-oktabrda imzolangan) taxminan 2027-yil aprelda kuchga kiradi, o'tish davrida foydalanuvchilarga ikkala alifbo kerak.

## 1. Qabul qilingan qarorlar (o'zgartirilmaydi)

- Asos: HeliBoard, https://github.com/HeliBorg/HeliBoard (fork qilinadi). Litsenziya: GNU GPL v3.
- Ilova bepul va kodi ochiq. Fork kodi GPL v3 bilan ochiq repozitoriyda saqlanadi. `LICENSE*` fayllari va HeliBoard/OpenBoard/AOSP mualliflik xabarlari o'chirilmaydi.
- Faqat Android. iOS bu TZ'ga kirmaydi.
- Glide typing kerak emas, qo'shilmaydi.
- `INTERNET` ruxsati qo'shilmaydi (HeliBoard to'liq oflayn).
- Upstream'ga PR yubormaymiz (repodagi `AI_USAGE.md` AI yordamida yaratilgan PR'larni taqiqlaydi). Mustaqil fork yuritamiz.
- Ichki paket nomlari (`helium314.keyboard...`) va `namespace = "helium314.keyboard.latin"` O'ZGARTIRILMAYDI. Faqat `applicationId`, nom, ikonka o'zgaradi.

## 2. Qonun haqida faktlar va ochiq savollar

TEKSHIRILGAN:
- Qonun: O'RQ-1184, "Lotin yozuviga asoslangan o'zbek alifbosini joriy etish to'g'risida"gi qonunga o'zgartirish. Prezident 2026-yil 21-oktabrda imzolagan.
- Yangi alifbo: 28 ta harf + 1 ta tutuq belgisi (ʼ). Eskisi: 26 harf + 3 harf birikmasi.
- To'rt o'zgarish: `oʻ → ö`, `gʻ → ğ`, `sh → ş`, `ch → ç` (bosh harflar: `Ö Ğ Ş Ç`).
- Amaldagi hujjatlar kuchida qoladi. Kuchga kirish: 2027-yil 22-aprel (infoedu.uz) / "olti oy keyin". Rasmiy matnni lex.uz'da tasdiqlash tavsiya etiladi.

TEKSHIRILMAGAN (kod ularga tayanmasin):
1. 28 harfning to'liq ro'yxati. Faraz: hozirgi 24 asosiy harf + 4 yangi = 28, `ng` alohida harf emas. Layoutda hozirgi `uzbek.json`dagi barcha lotin harflari saqlanadi.
2. Yangi imloda tutuq (ʼ) qoidalari.
3. `s`+`h` yoki `c`+`h` alohida harf sifatida yonma-yon kelgan so'zlar (eski yozuvda bir xil ko'rinadi).

## 3. Repodan TEKSHIRILGAN faktlar (clone: HEAD, 2026-11-05)

| Nima | Yo'l / qiymat |
|---|---|
| Hozirgi o'zbek layouti | `app/src/main/assets/layouts/main/uzbek.json` (3 qator: `q w e r t y u i o p oʻ` / `a s d f g h j k l gʻ ʼ` / `z x c v b n m`; `oʻ`,`gʻ` ikki belgili label, `"labelFlags": 128`) |
| Apostrof kod nuqtalari | `ʻ` = U+02BB (oʻ, gʻ), `ʼ` = U+02BC (tutuq) |
| Subtype | `app/src/main/res/xml/method.xml`, `uz_UZ`, `languageTag="uz-UZ"`, `subtypeId="0xad5cf7f6"`; 130 ta subtype, `subtypeId` takrorlanmaydi |
| Popup tugmalar | `app/src/main/assets/locale_key_texts/uz.txt`; shu tilning BARCHA layoutlariga ta'sir qiladi |
| Bundled lug'atlar | `app/src/main/assets/dicts/main_<locale>.dict` (`uz` yo'q edi) |
| Layout hujjatlari | `layouts.md` |
| Lug'at kodi | `latin/dictionary/DictionaryFactory.kt`, `DictionaryInfoUtils.kt`, `latin/DictionaryFacilitatorImpl.kt` (kesh papkasi `locale.toLanguageTag()` bo'yicha) |
| Toolbar | `latin/utils/ToolbarUtils.kt` |
| Asl `applicationId` | `helium314.keyboard` (`app/build.gradle.kts`), `versionCode = 4300`, `versionName = "4.3-beta1"` |

## 4. Ish hajmi

**P0:** (1) Brending. (2) Yangi alifbo layouti `uzbek_new` + `method.xml` subtype + string. (3) Lug'at/locale spike (kod yozishdan OLDIN). (4) Konvertor + unit testlar. (5) Konvertor toolbar tugmasi.
**P1:** (6) O'zbekcha lug'at (so'z taklifi) ikkala alifbo uchun.
**P2 (kirmaydi):** kirill ↔ lotin; avtomatik konvertatsiya rejimi; iOS; glide typing.

## 5. Talablar

### 5.1 Brending va build (P0)
- Foydalanuvchidan olinadigan kirishlar: `APP_NAME`, `APPLICATION_ID`, ikonka fayli, ochiq repo URL'i.
- `applicationId` o'zgaradi; `namespace` va paket nomlariga TEGMANG. APK nomi, `versionName = 1.0.0`, ilova nomi `strings.xml` da.
- About'da: "HeliBoard (GPL v3), OpenBoard, AOSP asosida" va ochiq repo havolasi.
- Release keystore repoga KIRMAYDI. `INTERNET` va boshqa yangi ruxsatlar qo'shilmasin.

### 5.2 Yangi alifbo layouti (P0)
Fayl: `app/src/main/assets/layouts/main/uzbek_new.json`.
```
Qator 1: q w e r t y u i o p ö
Qator 2: a s d f g h j k l ğ ş
Qator 3: ç z x c v b n m ʼ
```
Shartlar: (1) `ö ğ ş ç` va `ʼ` (U+02BC) ASOSIY qatlamda, bitta bosish bilan. (2) Eski layoutdagi barcha lotin harflari saqlanadi. (3) Shift → `Ö Ğ Ş Ç`; i/İ muammosi bo'lmasin (turk locale emas). (4) `labelFlags` ko'r-ko'rona ko'chirilmaydi. (5) 360dp kenglikda 3-qator sig'ishi tekshirilsin. (6) Eski `uzbek.json` O'ZGARTIRILMAYDI.

### 5.3 `method.xml` va stringlar (P0)
Mavjud `uz_UZ` subtype'dan keyin ikkinchi subtype: noyob 8 xonali hex `subtypeId`, `KeyboardLayoutSet=MAIN:uzbek_new,AsciiCapable,EmojiCapable`, locale 5.4 qaroriga bog'liq, label "yangi alifbo".

### 5.4 SPIKE: lug'at va locale (P0)
Muammo: lug'at kesh papkasi `toLanguageTag()` bo'yicha; ikkala layout bir xil `uz` locale'da bo'lsa bitta lug'atni bo'lishadi, harflar farq qiladi. Variantlar: A — yangi alifbo uchun alohida locale (variant/private-use subtag); B — bitta lug'at + ish vaqtida transliteratsiya; C — o'z varianti. Xulosa foydalanuvchiga ko'rsatilib tasdiq kutiladi. **Qaror: Variant A, `uz-UZ-newlat`; `uz.txt` dan `ş ç ğ ö` olib tashlansin.**

### 5.5 Konvertor (P0)
`app/src/main/java/helium314/keyboard/latin/utils/UzbekAlphabetConverter.kt` — Android'ga bog'liq bo'lmagan sof Kotlin `object`: `oldToNew(text)`, `newToOld(text)`.

Apostrof sifatida taniladigan belgilar (APOS): `'` `ʻ` `ʼ` `‘` `’` `` ` `` `´` `′` `ʹ` `ʽ`.

**oldToNew (chapdan o'ngga):** (1) `o`/`g` + APOS → `ö`/`ğ` (registr birinchi harfga ergashadi). (2) `sh` → `ş`, `ch` → `ç` (registr birinchi harfga qarab). (3) Boshqa APOS faqat ikki harf orasida kelsa → tutuq `ʼ` (U+02BC); boshqa joyda o'zgarishsiz. (4) `ng` va qolganlar o'zgarmaydi.

**newToOld:** (1) `ö→oʻ`, `ğ→gʻ` (U+02BB) va bosh harflari. (2) `ş→sh`, `ç→ch`; `Ş`/`Ç` yonida (oldingi YOKI keyingi) bosh harf bo'lsa `SH`/`CH`, aks holda `Sh`/`Ch`. (3) Tutuq `ʼ` o'zgarishsiz.

Qat'iy: registr xaritalash faqat aniq jadval bo'yicha, `Locale`-ga bog'liq `uppercase()/lowercase()` ishlatilmaydi; sof funksiyalar; bo'sh satrga bo'sh.

Ma'lum cheklovlar: alohida harf bo'lgan `s`+`h`/`c`+`h` noto'g'ri birlashishi mumkin (`s'h` birlashmaydi); `o`/`g` dan keyingi apostrof har doim harf deb olinadi.

Etalon Python realizatsiyasi va barcha test vektorlari: `app/src/test/java/helium314/keyboard/latin/UzbekAlphabetConverterTest.kt` va `tools/uzbek-dict/build_wordlists.py` (`old_to_new`). Qo'shimcha: `newToOld(oldToNew(x)) == x` — `Oʻzbekiston`, `toʻgʻri`, `Shahar`, `CHOY`, `maʼno`, `inglizcha`, `Gʻisht`, `ishchi`.

### 5.6 Konvertor tugmasi (P0)
Yangi toolbar tugmasi (`ToolbarUtils.kt` uslubida). Tap: maqsad alifbo = hozir FAOL layout alifbosi. Tanlangan matn bo'lsa faqat u, bo'lmasa maydonning butun matni (~10 000 belgigacha, oshsa xabar). Bitta batch amal (undo ishlashi uchun), `RichInputConnection` orqali. Parol maydonlarida ishlamaydi. `INTERNET` yo'q, clipboard'ga yozish yo'q.

### 5.7 Lug'at (P1)
Manba: so'zlar ro'yxati (yoki ochiq manba, litsenziyasi ko'rsatilgan holda). Skriptlar `tools/uzbek-dict/`. `.dict` formati/kompilyatsiya vositasi: https://codeberg.org/Helium314/aosp-dictionaries README (taxmin qilinmaydi). Fayllar `assets/dicts/main_<locale>.dict`. Maqsad ≥ ~50 000 so'z. Chastotasiz bo'lsa tekis chastota.

### 5.8 Build, test, chiqarish
`./gradlew assembleDebug`, `./gradlew testDebugUnitTest`. Testlarni ishga tushirmay "tayyor" demang. Release: foydalanuvchining keystore'i bilan imzolangan APK/AAB; Play Console uchun maxfiylik siyosati va Data safety (alohida vazifa).

## 6. Qabul mezonlari (qo'lda)
1. Ilova o'rnatiladi, yangi nom/ikonka, klaviatura sifatida yoqiladi.
2. Til sozlamalarida ikkita o'zbek variant (eski/yangi) ko'rinadi va almashadi.
3. Yangi layoutda `ö ğ ş ç ʼ` bitta bosishda; Shift → `Ö Ğ Ş Ç`; `I` hech qachon `İ` emas.
4. Eski layout avvalgidek ishlaydi.
5. Konvertor tugmasi: tanlangan/butun matn, 5.5 jadvallariga mos, undo ishlaydi.
6. Test vektorlari unit testda o'tadi.
7. `AndroidManifest.xml`da `INTERNET` yo'q; yangi ruxsat yo'q.
8. (P1) So'z takliflari tanlangan layout alifbosida (`şahar` yoki `shahar`, aralash emas).
9. About'da litsenziya va ochiq repo havolasi; `LICENSE*` saqlangan.

## 7. Claude Code uchun ish tartibi
1. Faylni o'zgartirishdan oldin to'liq o'qing. 2. Hisobotda fayl yo'li/qator raqamlari/oldin-keyin. 3. Branch `uzbek-klaviatura`, har qadam alohida commit. 4. Tartib: brending → layout+method.xml+string → spike tasdig'i → konvertor+testlar → toolbar tugmasi → lug'at. 5. `APP_NAME`, `APPLICATION_ID`, ikonka, locale varianti, so'zlar manbasini foydalanuvchidan so'rang. 6. Keraksiz refaktoring yo'q. 7. Yakuniy hisobotda nima qurilmada sinaldi / faqat kompilyatsiya / sinalmadi ajratilsin.

## 8. Xavflar
Lug'at/locale to'qnashuvi; 28 harf ro'yxati va tutuq qoidalari tasdiqlanmagan; eski→yangi kamyob noaniqliklar; lug'at sifati so'zlar manbasiga bog'liq; upstream HeliBoard o'zgaradi, fork qo'lda yangilanadi.

## Manbalar
- https://infoedu.uz/lotin-alifbosi-qonuni-imzolandi
- https://uza.uz/ar/posts/bir-tovush-bir-harf-tamoyili-asosida_928205
- https://gov.uz/oz/icc/news/view/239623
- https://infoedu.uz/sertifikat-imtihoni-savollari-amaldagi-alifboda
- https://github.com/HeliBorg/HeliBoard
