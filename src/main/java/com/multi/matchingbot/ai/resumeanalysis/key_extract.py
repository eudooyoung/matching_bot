import pandas as pd
from konlpy.tag import Okt
from sklearn.feature_extraction.text import TfidfVectorizer
import os

# 1. 경로 설정
file_path = os.path.join("이력서 데이터셋", "occupation_intro_final.csv")

# 2. 데이터 로드
df = pd.read_csv(file_path, encoding="utf-8-sig")

# 3. Okt 형태소 분석기
okt = Okt()

# 4. 키워드 추출 함수 (명사 기반 TF-IDF 상위 5개)
def extract_keywords(text, top_k=5):
    if pd.isnull(text) or not text.strip():
        return ""
    nouns = okt.nouns(text)
    if not nouns:
        return ""
    tfidf = TfidfVectorizer(tokenizer=lambda x: x, lowercase=False)
    tfidf_matrix = tfidf.fit_transform([nouns])
    scores = tfidf.idf_
    words = tfidf.get_feature_names_out()
    ranking = sorted(zip(words, scores), key=lambda x: x[1])[:top_k]
    return ", ".join([w for w, _ in ranking])

# 5. 키워드 추출 실행
df["intro_keywords"] = df["intro_statement_all"].apply(extract_keywords)

# 6. 결과 저장
output_path = os.path.join("이력서 데이터셋", "occupation_intro_with_keywords.csv")
df.to_csv(output_path, index=False, encoding="utf-8-sig")

print("✅ 키워드 추출 완료 → occupation_intro_with_keywords.csv 저장됨")
