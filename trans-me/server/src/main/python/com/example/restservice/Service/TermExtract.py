import sys
import spacy
from pyate.term_extraction_pipeline import TermExtractionPipeline

string = sys.argv[1]

nlp = spacy.load("en_core_web_sm")
nlp.add_pipe("combo_basic")
doc = nlp(string)

result = doc._.combo_basic
for res in (result[result > 1].sort_values(ascending=False).keys()):
    print(res)
