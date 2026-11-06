#!/usr/bin/env python3
# SPDX-License-Identifier: GPL-3.0-only
"""Interfeys tarjimasini (tr_part*.py, eski alifbo) yangi alifboga o'tkazib,
app/src/main/res/values-uz/*.xml fayllarini yozadi.

usage: python tools/uzbek-ui/build_strings.py
Qoidalar: UzbekAlphabetConverter.oldToNew bilan bir xil (tools/uzbek-dict/build_wordlists.py dagi old_to_new).
⟨...⟩ ichidagi matn o'zgarmaydi (inglizcha atamalar); %s, %1$s, \\n va xliff teglar ham.
"""
import os, re, sys
sys.path.insert(0, os.path.join(os.path.dirname(__file__), "..", "uzbek-dict"))
from build_wordlists import old_to_new
sys.path.insert(0, os.path.dirname(__file__))
from tr_part1 import T1
from tr_part2 import T2
from tr_part3 import T3

RES = os.path.join(os.path.dirname(__file__), "..", "..", "app", "src", "main", "res")
TOKEN = re.compile(r'(<xliff:g[^>]*>|</xliff:g>|%\d\$[sd]|%[sd]|\\[nt]|⟨[^⟩]*⟩)')
PLACEHOLDER = re.compile(r'%\d\$[sd]|%[sd]')
HEADER = """<?xml version="1.0" encoding="utf-8"?>
<!--
    O'zbekcha interfeys tarjimasi (yangi alifbo). Fayl tools/uzbek-ui/build_strings.py bilan yasaladi,
    qo'lda tahrirlamang: tr_part*.py ni o'zgartirib skriptni qayta ishga tushiring.
    SPDX-License-Identifier: Apache-2.0 AND GPL-3.0-only
-->
<resources xmlns:xliff="urn:oasis:names:tc:xliff:document:1.2">
"""

def esc(s):
    return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")

def convert(text, name):
    text = text.replace("\n", "\\n")
    out = []
    for part in TOKEN.split(text):
        if not part:
            continue
        if part.startswith("⟨"):
            out.append(esc(part[1:-1]))
        elif TOKEN.fullmatch(part):
            out.append(part)  # placeholder / xliff / escape: o'zgarishsiz
        else:
            if '"' in part:
                raise SystemExit(f"{name}: ASCII qo'shtirnoq ishlatilgan, “ ” ishlating")
            conv = old_to_new(part)
            low = conv.lower()
            if "sh" in low or "ch" in low:
                print(f"  ESLATMA {name}: 'sh'/'ch' qoldi -> {conv!r}")
            out.append(esc(conv).replace("'", "\\'"))
    return "".join(out)

def base_strings(path):
    s = open(path, encoding="utf-8").read()
    res = {}
    for m in re.finditer(r'<string name="([^"]+)"([^>]*)>(.*?)</string>', s, flags=re.S):
        n, attr, t = m.groups()
        if 'translatable="false"' in attr:
            continue
        res[n] = t
    return res

def build(base_file, translations, out_file):
    base = base_strings(os.path.join(RES, "values", base_file))
    lines, problems = [], 0
    for name, raw in base.items():
        if name not in translations:
            continue
        tr = translations[name]
        if sorted(PLACEHOLDER.findall(raw)) != sorted(PLACEHOLDER.findall(tr)):
            print(f"XATO {name}: placeholderlar mos emas: {PLACEHOLDER.findall(raw)} vs {PLACEHOLDER.findall(tr)}")
            problems += 1
        lines.append(f'    <string name="{name}">{convert(tr, name)}</string>\n')
    unknown = set(translations) - set(base)
    if unknown:
        print("XATO: asl faylda yo'q nomlar:", sorted(unknown)); problems += 1
    missing = [n for n in base if n not in translations and not n.startswith(("gesture_", "background_gesture"))]
    if missing:
        print(f"{base_file}: tarjimasiz ({len(missing)}):", missing)
    if problems:
        raise SystemExit(1)
    os.makedirs(os.path.join(RES, "values-uz"), exist_ok=True)
    with open(os.path.join(RES, "values-uz", out_file), "w", encoding="utf-8", newline="\n") as f:
        f.write(HEADER + "".join(lines) + "</resources>\n")
    print(f"{out_file}: {len(lines)} qator")

if __name__ == "__main__":
    build("strings.xml", {**T1, **T2}, "strings.xml")
    build("strings-talkback-descriptions.xml", T3, "strings-talkback-descriptions.xml")
