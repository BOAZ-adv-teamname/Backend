import pandas as pd
import numpy as np
import argparse
import torch
import time

from sklearn.metrics.pairwise import cosine_similarity
from sklearn.feature_extraction.text import TfidfVectorizer
from sentence_transformers import SentenceTransformer, util



def read_sum_data(path):
    sum_database = pd.read_csv(path)
    sum_database=sum_database[sum_database.kobart_sum.notna()]
    return sum_database.kobart_sum.values.tolist()

def vectorization(DATABASE,NEWS,type):
    database =  DATABASE
    news = NEWS

    if type =='tfidf':
        database.append(news)
        vectorizer = TfidfVectorizer()
        X = vectorizer.fit_transform(database)
        
    if type =='bert':
        model_path = 'paraphrase-multilingual-mpnet-base-v2'
        embedder = SentenceTransformer(model_path)
        query = NEWS

        corpus_embeddings = torch.load('./Sum_Database/law_encoder.pt')
        query_embedding = embedder.encode(query, convert_to_tensor=True)
        X = (corpus_embeddings,query_embedding)
    
        # corpus.append(news)

        # device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

        # model = ElectraModel.from_pretrained("monologg/koelectra-small-v3-discriminator")
        # tokenizer = ElectraTokenizer.from_pretrained("monologg/koelectra-small-v3-discriminator")
        
        # model = model.to(device)
        
        # tokens = {'input_ids': [], 'attention_mask': []}
        # MAX_LENGTH = 256
        # for sentence in corpus:
        #     # encode each sentence and append to dictionary
        #     ts = tokenizer.tokenize(sentence)
        #     input_ids = tokenizer.convert_tokens_to_ids(ts)

        #     if len(input_ids) >= MAX_LENGTH:
        #         input_ids = input_ids[:MAX_LENGTH]
        #         attention_mask = [1] * MAX_LENGTH
        #     else:
        #         n_to_pad = MAX_LENGTH - len(input_ids)
        #         attention_mask = ([1] * len(input_ids)) + ([0]* n_to_pad)
        #         input_ids = input_ids + ([0] * n_to_pad)
        
        #     input_ids= torch.as_tensor(input_ids)
        #     attention_mask = torch.as_tensor(attention_mask)

        #     tokens['input_ids'].append(input_ids)
        #     tokens['attention_mask'].append(attention_mask)
        # # reformat list of tensors into single tensor
        # tokens['input_ids'] = torch.stack(tokens['input_ids'])
        # tokens['attention_mask'] = torch.stack(tokens['attention_mask'])

        # outputs = model(**tokens)

        # embeddings = outputs[0]
        # attention_mask = tokens['attention_mask']
        # mask = attention_mask.unsqueeze(-1).expand(embeddings.size()).float()
        # masked_embeddings = embeddings * mask
        # summed = torch.sum(masked_embeddings, 1)
        # summed_mask = torch.clamp(mask.sum(1), min=1e-9)
        # mean_pooled = summed / summed_mask
        # X = mean_pooled.detach().numpy()

    return X


def Cosine_similarity(X,DATABASE,NEWS,type,top_k):
    if type == 'tfidf':
        news = X[-1]
        laws = X[:-1]
        cos_scores = cosine_similarity(news,laws)[0]
    
    if type =='bert':
        news = X[1]
        laws = X[0]
        cos_scores = util.pytorch_cos_sim(news,laws)[0]
        cos_scores = cos_scores.cpu()
    
    top_results = np.argpartition(-cos_scores, range(top_k))[0:top_k]

    print("\n\n======================\n\n")
    print("Query:", NEWS)
    print(f"\nTop {top_k} most similar sentences in corpus:")

    final=[]
    for idx in top_results[0:top_k]:
        print(DATABASE[idx].strip(), "(Score: %.4f)" % (cos_scores[idx]))
        final.append(DATABASE[idx].strip())

    return final


def make_simtext(law_path,new_path,type,top_k):
    DATABASE = read_sum_data(law_path)
    NEWS = list(pd.read_csv(new_path))[0]
    X=vectorization(DATABASE,NEWS,type)
    final = Cosine_similarity(X,DATABASE,NEWS,type,top_k)

    for i,f in enumerate(final):
        text_file = open(f"./Output/output_{i+1}.txt", "w")
        n = text_file.write(f)
        text_file.close()

if __name__ == '__main__':
    #start = time.time()  # 시작 시간 저장
    parser = argparse.ArgumentParser()
    # Directory paths
    parser.add_argument('--law-dir', default='./Sum_Database/0706_KoBart512.csv')
    parser.add_argument('--news-dir', default='./Sum_Database/news.txt')
    parser.add_argument('--emb-type', required=True, choices=['tfidf','fasttext','bert'])
    parser.add_argument('--top-k',default = 3)
    args = parser.parse_args()

    make_simtext(args.law_dir,args.news_dir,args.emb_type,args.top_k)
         
    #print("time :", time.time() - start)