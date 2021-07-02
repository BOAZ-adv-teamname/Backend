from flask import Flask, render_template, request, jsonify
from flask_cors import CORS, cross_origin
from flask_restful import reqparse

import json
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

def make_simtext(law_path,new_path):
    DATABASE = read_sum_data(law_path)
    NEWS = list(pd.read_csv(new_path))[0]
    X=vectorization(DATABASE,NEWS)
    final = Cosine_similarity(X,DATABASE)

    for i,f in enumerate(final):
        text_file = open(f"./Output/output_{i+1}.txt", "w")
        n = text_file.write(f)
        text_file.close()


app = Flask(__name__)
cors = CORS(app)
app.config['CORS_HEADERS'] = 'Content-Type'

@app.route('/')
@cross_origin()
def index():
  return "ok"

@app.route('/pin', methods = ['POST','OPTIONS'])
@cross_origin()
def pin():
  parser = reqparse.RequestParser()
  parser.add_argument('news', type=str)
  args = parser.parse_args()
  news = args['news']

  # save news
  with open('./Sum_Database/news.txt', 'w', encoding='utf-8') as f:
    f.write(str(news))

  law_path = './Sum_Database'
  news_path = './Sum_Database/news.txt'
  make_simtext(law_path,news_path)

  similar_precedent1 = open("./Output/output_1.txt", "r").read()
  similar_precedent2 = open("./Output/output_2.txt", "r").read()
  similar_precedent3 = open("./Output/output_3.txt", "r").read()

  print(similar_precedent1)
  print(similar_precedent2)
  print(similar_precedent3)

  data = {
    "news" : news,
    "similar_precedent1":similar_precedent1,
    "similar_precedent2":similar_precedent2,
    "similar_precedent3":similar_precedent3
  }

  return jsonify(data)

if __name__=="__main__":
    app.run(host = 'localhost', port=5000, debug=True)
