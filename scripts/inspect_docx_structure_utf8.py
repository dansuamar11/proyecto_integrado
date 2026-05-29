# -*- coding: utf-8 -*-
from docx import Document

path = r"C:\Users\DSuar\OneDrive\Escritorio\TFG\Documentacion basket aljarafe.docx"
doc = Document(path)

for i, paragraph in enumerate(doc.paragraphs):
    text = paragraph.text.strip()
    if not text:
        continue

    style_name = paragraph.style.name if paragraph.style else ""
    if style_name.startswith("Heading") or style_name.startswith("Title") or "Titulo" in style_name:
        print(f"{i}: [{style_name}] {text}")
