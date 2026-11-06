# Do'kon materiallari (Google Play / App Store uchun)

| Fayl | Nima |
|---|---|
| `play/icon-512.png` | Ilova ikonkasi 512x512 |
| `play/feature-graphic-1024x500.png` | Play banner (feature graphic) |
| `play/screenshot-1..3.png` | Telefon skrinshotlari 1080x1920 |
| `play/raw/` | Skrinshotlarning xom suratlari |
| `play/listing-uz.md` | Nom, qisqa va to'liq tavsif, Data safety javoblari, aloqa |
| `PRIVACY-uz.md` | Maxfiylik siyosati (Play'dagi URL shu faylga ishora qiladi) |
| `../assets/images/logo.png` | Logo manbasi |

Release imzolash kaliti (`upload.jks`) va `.aab` repoga kiritilmagan.

## iOS haqida
Bu loyiha HeliBoard (Android IME) asosida, shuning uchun iPhone uchun to'g'ridan-to'g'ri build qilib bo'lmaydi.
iOS'da alohida Keyboard Extension (Swift/UIKit) yozish kerak. Qayta ishlatish mumkin: logo, ikonka,
tavsif matnlari, maxfiylik siyosati va alifbo/layout ma'lumoti (`app/src/main/assets/layouts/main/uzbek_new.json`).
App Store skrinshotlari boshqa o'lchamlarda (masalan 1290x2796) talab qilinadi, ularni iOS build'dan olish kerak.
