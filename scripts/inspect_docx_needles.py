# -*- coding: utf-8 -*-
from docx import Document
path = r"C:\Users\DSuar\OneDrive\Escritorio\TFG\Documentacion basket aljarafe.docx"
doc = Document(path)
needles = [
    'logica de negocio se han concentrado',
    'arranque coordinado de backend y frontend',
    'reasignación de árbitros en partidos pendientes',
    'programación doble de un mismo equipo'
]
for needle in needles:
    found = False
    for i,p in enumerate(doc.paragraphs):
        if needle in p.text:
            print(i, p.text)
            found = True
    if not found:
        print('MISSING:', needle)
