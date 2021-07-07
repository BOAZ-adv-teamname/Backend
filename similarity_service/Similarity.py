import pandas as pd
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity
from sklearn.feature_extraction.text import TfidfVectorizer


def read_sum_data(path):
    sum_database = pd.read_csv(f'./{path}/summary_gpu.csv')
    return sum_database.loc[:,'kobart_sum']

def vectorization(DATABASE,NEWS):
    database =  DATABASE
    news = NEWS

    corpus=[x for x in database[:10]]
    corpus.append(news)
    vectorizer = TfidfVectorizer()
    X = vectorizer.fit_transform(corpus)
    return X

def Cosine_similarity(X,DATABASE):
    news = X[-1]
    laws = X[:-1]
    database=DATABASE
    sim = cosine_similarity(news,laws)[0]
    result = pd.DataFrame({'corpus':database[:10],
                            'similarity':sim})
    final = result.sort_values('similarity').iloc[:3,0]

    return final

if __name__ == '__main__':
    DATABASE = read_sum_data('./Sum_Database')
    NEWS = list(pd.read_csv('./Sum_Database/news.txt'))[0]
    X=vectorization(DATABASE,NEWS)
    final = Cosine_similarity(X,DATABASE)

    for i,f in enumerate(final):
        text_file = open(f"./Output/output_{i+1}.txt", "w")
        n = text_file.write(f)
        text_file.close()
        






