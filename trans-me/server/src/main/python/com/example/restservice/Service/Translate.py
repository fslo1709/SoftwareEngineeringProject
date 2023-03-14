import nltk
import re
import sys
import googletrans
translator = googletrans.Translator()


#msg = sys.argv[1]
article_text = sys.argv[1]
language = sys.argv[2]
src = translator.detect(article_text)

results = translator.translate(article_text,src=src.lang,dest=language).text
print(results)