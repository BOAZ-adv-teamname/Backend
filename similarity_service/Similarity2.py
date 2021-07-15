import pandas as pd
import numpy as np
import torch
from sklearn.metrics.pairwise import cosine_similarity
from sklearn.feature_extraction.text import TfidfVectorizer
from sentence_transformers import SentenceTransformer, util
import time

def read_sum_data(path):
    sum_database = pd.read_csv(path)
    sum_database=sum_database[sum_database.kobart_sum.notna()]
    return sum_database.kobart_sum.values.tolist()


def vectorization(DATABASE,NEWS):
    model_path = 'paraphrase-multilingual-mpnet-base-v2'
    embedder = SentenceTransformer(model_path)
    query = NEWS

    corpus_embeddings = torch.load('./Sum_Database/law_encoder.pt')
    query_embedding = embedder.encode(query, convert_to_tensor=True)
    X = (corpus_embeddings,query_embedding)
    return X

def Cosine_similarity(X,DATABASE):
    top_k = 3
    news = X[1]
    laws = X[0]
    cos_scores = util.pytorch_cos_sim(news, laws)[0]
    cos_scores = cos_scores.cpu()

    top_results = np.argpartition(-cos_scores, range(top_k))[0:top_k]

    final=[]
    for idx in top_results[0:top_k]:
        final.append(DATABASE[idx].strip())
    return final

def make_simtext(law_path,new_path):
    DATABASE = read_sum_data(law_path)
    NEWS = list(pd.read_csv(new_path))[0]
    X=vectorization(DATABASE,NEWS)
    final = Cosine_similarity(X,DATABASE)


    for i,f in enumerate(final):
        text_file = open(f"./Output/output_{i+1}.txt", "w")
        n = text_file.write(f)
        text_file.close()

if __name__ == '__main__':
    #start = time.time()  # 시작 시간 저장
    law_path = './Sum_Database/0706_KoBart512.csv'
    news_path = './Sum_Database/news.txt'
    make_simtext(law_path,news_path)
         
    #print("time :", time.time() - start)