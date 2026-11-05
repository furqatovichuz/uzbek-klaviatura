#!/usr/bin/env python3
# SPDX-License-Identifier: GPL-3.0-only
"""Builds AOSP `.combined` word lists (old and new Uzbek alphabet) from a plain word list.

usage: build_wordlists.py words.txt out_dir
The new alphabet list is produced with the same rules as UzbekAlphabetConverter.oldToNew.
Words are expected in the current alphabet (oʻ gʻ with U+02BB, tutuq U+02BC).
Flat frequency, see README.md.
"""
import sys, time, os

APOS = set("'ʻʼ‘’`´′ʹʽ")

def old_to_new(s):
    out = []; i = 0; n = len(s)
    while i < n:
        c = s[i]; low = c.lower(); nxt = s[i + 1] if i + 1 < n else ""
        if low in ("o", "g") and nxt and nxt in APOS:
            m = {"o": "ö", "g": "ğ"}[low]
            out.append(m.upper() if c.isupper() else m); i += 2; continue
        if low == "s" and nxt.lower() == "h" and nxt:
            out.append("Ş" if c.isupper() else "ş"); i += 2; continue
        if low == "c" and nxt.lower() == "h" and nxt:
            out.append("Ç" if c.isupper() else "ç"); i += 2; continue
        if c in APOS:
            prev = s[i - 1] if i > 0 else ""
            if prev and nxt and prev.isalpha() and nxt.isalpha():
                out.append("ʼ"); i += 1; continue
        out.append(c); i += 1
    return "".join(out)

def valid(w):
    return w and all((ch.isalpha() and ch.isascii()) or ch in "ʻʼ" for ch in w)

def main():
    src, out_dir = sys.argv[1:3]
    words = sorted({w.strip() for w in open(src, encoding="utf-8") if valid(w.strip())})
    date = int(time.time())
    for locale, conv in (("uz-UZ", lambda w: w), ("uz-UZ-newlat", old_to_new)):
        path = os.path.join(out_dir, f"main_{locale}.combined")
        with open(path, "w", encoding="utf-8") as f:
            f.write(f"dictionary=main:{locale},locale={locale},description=Uzbek ({locale}),date={date},version=54\n")
            for w in sorted({conv(w) for w in words}):
                f.write(f" word={w},f=100\n")
        print(path, len(words))

if __name__ == "__main__":
    main()
