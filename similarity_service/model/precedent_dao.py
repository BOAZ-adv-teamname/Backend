import pymongo
from pymongo import MongoClient

import json
import pandas as pd
import numpy as np

import os

def get_precedent_data(local):
    # local csv 파일
    if local:
        sum_database = pd.read_csv('./model/Sum_Database/0706_KoBart512.csv')
    # mongo db
    else:
        try:
            client = MongoClient("localhost:27017")
            db = client.precedent
            collection = db.precedent
            sum_database = collection.find({})
        except:
            print('mongo db connect error')
            sum_database = pd.read_csv(path)

    sum_database=sum_database[sum_database.kobart_sum.notna()]
    return sum_database.kobart_sum.values.tolist()
