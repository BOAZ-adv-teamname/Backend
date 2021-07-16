from sklearn.metrics.pairwise import cosine_similarity
from sklearn.feature_extraction.text import TfidfVectorizer

from model.precedent_dao import *

def vectorization(DATABASE,NEWS):
    database =  DATABASE
    news = NEWS

    corpus=[x for x in database]
    corpus.append(news)
    vectorizer = TfidfVectorizer()
    X = vectorizer.fit_transform(corpus)
    return X

def Cosine_similarity(X,DATABASE):
    news = X[-1]
    laws = X[:-1]
    database=DATABASE
    sim = cosine_similarity(news,laws)[0]
    result = pd.DataFrame({'corpus':database,
                            'similarity':sim})
    final = result.sort_values('similarity').iloc[:3,0]

    return final

def make_simtext(news):
    DATABASE = get_precedent_data(local=True)
    NEWS = news
    X=vectorization(DATABASE,NEWS)
    final = Cosine_similarity(X,DATABASE)

    output = []
    for i,f in enumerate(final):
        output.append(f)

        text_file = open(f"./service/Output/output_{i+1}.txt", "w")
        n = text_file.write(f)
        text_file.close()
    
    return output