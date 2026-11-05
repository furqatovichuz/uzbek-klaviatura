# Uzbek dictionaries

`app/src/main/assets/dicts/main_uz-UZ.dict` (current alphabet) and `main_uz-UZ-newlat.dict` (new alphabet).

* Source words: https://github.com/uzbek-net/uz-hunspell (`uz_UZ.dic`, GPL-3.0). Only the stems are used (no affix expansion), words with hyphens, digits or Cyrillic letters are skipped: 87 412 words. The origin of the word list is not documented in that repository.
* Frequencies are flat (`f=100`), so suggestions are not ranked by real usage yet.
* The new-alphabet list is produced with the same rules as `UzbekAlphabetConverter.oldToNew`.
* Build: `python3 build_wordlists.py words.txt out/` (words.txt from `expand_hunspell.py <dic> <aff> words.txt --depth 0`), then
  `java -jar dicttool_aosp.jar makedict -s main_<locale>.combined -d main_<locale>.dict`
  (`dicttool_aosp.jar` from https://codeberg.org/Helium314/aosp-dictionaries, not stored in this repo).
