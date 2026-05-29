from docx import Document
path = r"C:\Users\DSuar\OneDrive\Escritorio\TFG\Documentacion basket aljarafe.docx"
doc = Document(path)
for i, p in enumerate(doc.paragraphs):
    text = p.text.strip()
    if not text:
        continue
    style = p.style.name if p.style else ''
    if style.startswith('Heading') or style.startswith('Título') or style.startswith('Title'):
        print(f"{i}: [{style}] {text}")
