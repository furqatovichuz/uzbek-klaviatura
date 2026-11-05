#!/usr/bin/env python3
# SPDX-License-Identifier: GPL-3.0-only
"""Expands a Hunspell dictionary (uz_UZ.dic + uz_UZ.aff, suffix-only) into a plain word list.

Source: https://github.com/uzbek-net/uz-hunspell (GPL-3.0). Suffix rules are applied to the stem
and, through the continuation flags, to the result once more (depth 2).

usage: expand_hunspell.py uz_UZ.dic uz_UZ.aff out_wordlist.txt [--depth N]
"""
import re
import sys
from collections import defaultdict

def load_rules(aff_path):
    """flag -> {condition: [(strip, add, cont_flags)]}"""
    rules = defaultdict(lambda: defaultdict(list))
    for line in open(aff_path, encoding="utf-8"):
        p = line.split()
        if len(p) < 5 or p[0] != "SFX":
            continue
        flag, strip, add, cond = p[1], p[2], p[3], p[4]
        cont = ""
        if "/" in add:
            add, cont = add.split("/", 1)
        strip = "" if strip == "0" else strip
        add = "" if add == "0" else add
        rules[flag][re.compile("(?:" + cond + ")$")].append((strip, add, cont))
    return rules

def apply(word, flags, rules):
    for f in flags:
        for cond, items in rules.get(f, {}).items():
            if not cond.search(word):
                continue
            for strip, add, cont in items:
                if strip and not word.endswith(strip):
                    continue
                yield (word[: len(word) - len(strip)] + add), cont

def main():
    dic, aff, out = sys.argv[1:4]
    depth = int(sys.argv[sys.argv.index("--depth") + 1]) if "--depth" in sys.argv else 2
    rules = load_rules(aff)
    words = set()
    lines = open(dic, encoding="utf-8").read().splitlines()[1:]
    for line in lines:
        stem, _, flags = line.partition("/")
        if not stem:
            continue
        words.add(stem)
        frontier = [(stem, flags)]
        for _ in range(depth):
            nxt = []
            for w, fl in frontier:
                for new, cont in apply(w, fl, rules):
                    if new not in words:
                        words.add(new)
                        if cont:
                            nxt.append((new, cont))
            frontier = nxt
    with open(out, "w", encoding="utf-8") as f:
        for w in sorted(words):
            f.write(w + "\n")
    print(f"{len(lines)} stems -> {len(words)} words")

if __name__ == "__main__":
    main()
