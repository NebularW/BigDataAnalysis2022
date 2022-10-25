import math
import numpy as np
import pandas as pd

def get_list_index_map(list):
    """
    将列表转为map
    :param list:输入列表
    :return:
    1. map item:index
    2. map_reverse index:item
    """
    map = {}
    map_reverse = {}
    for i in range(len(list)):
        map[list[i]] = i
        map_reverse[i] = list[i]
    return map, map_reverse


def get_rating_matrix():
    """
    构造评分矩阵
    :return: 二维数组, [i,j]表示user_i对movie_j的评分, 缺省值为0
    """
    matrix = np.zeros((len(user_map.keys()), len(movie_map.keys())))
    for row in ratings.itertuples(index=True, name='Pandas'):
        user = user_map[getattr(row, "userId")]
        movie = movie_map[getattr(row, "movieId")]
        rate = getattr(row, "rating")
        matrix[user, movie] = rate
    return matrix

def get_user_sim_matrix(input_matrix):
    """
    构造用户相似度矩阵
    :param input_matrix: 输入矩阵, 每i行代表用户i的特征向量
    :return: 对称矩阵, [i,j]=[j,i]=sim(user_i,user_j)
    """
    size = len(input_matrix)
    matrix = np.zeros((size, size))
    for i in range(size):
        for j in range(i + 1, size):
            x = np.array(input_matrix[i])
            y = np.array(input_matrix[j])
            sim = np.corrcoef(x, y)[0][1]            
            matrix[i, j] = sim
            matrix[j, i] = sim  # 对称矩阵, 对角线为0
    return matrix

def get_CFRecommend(matrix, index, k, n):
    """
    获取协同过滤推荐
    :param matrix: 相似矩阵
    :param index: 目标index
    :param k: k邻居的k
    :param n: 获取推荐的topN
    :return: list([movie_index,预测评分],...)
    """
    rate = get_predict(matrix, index, k)  # 获取预测评分
    for i in range(len(rate)):  # 如果用户已经评分过了, 把预测评分设为0, 也就是不会再推荐看过的电影
        if ratings_matrix[index][i] != 0:
            rate[i] = 0
    res = []
    for i in range(len(rate)):
        res.append([i, rate[i]])
    res.sort(key=lambda val: val[1], reverse=True)
    return res[:n]


def get_predict(matrix, index, k):
    """
    获取预测评分
    :param matrix: 相似矩阵
    :param index: 目标index
    :param k:k邻居的k
    :return: 根据KNN, 获得对第index位用户评分的预测
    """
    neighbors = k_neighbor(matrix, index, k)
    all_sim = 0
    rate = [0 for i in range(len(ratings_matrix[0]))]
    for pair in neighbors:
        neighbor_index = pair[0]
        neighbor_sim = pair[1]
        all_sim += neighbor_sim
        rate += ratings_matrix[neighbor_index] * neighbor_sim
    rate /= all_sim
    return rate


def k_neighbor(matrix, index, k):
    """
    输入相似矩阵, 读取k邻居的index
    :param matrix: 相似矩阵
    :param index: 目标index
    :param k:
    :return: list([k-index,相似度],....)
    """
    line = matrix[index]
    tmp = []
    for i in range(len(line)):
        tmp.append([i, line[i]])
    tmp.sort(key=lambda val: val[1], reverse=True)
    return tmp[:k]


def evaluation(user_sim_matrix, split=0.2):
    """
    评估推荐模型准确度
    :param user_sim_matrix: 用户相似度矩阵
    :param split: 测试集比例
    :return: RMSE计算结果
    """
    n = 0
    res = 0
    user_start = int(len(user_list) * (1 - split))
    movie_start = int(len(movie_list) * (1 - split))
    cover = {}
    # 计算RMSE
    for user_index in range(user_start, len(user_list)):
        predict = get_predict(user_sim_matrix, user_index, 10)
        for movie_index in range(movie_start, len(movie_list)):
            if ratings_matrix[user_index][movie_index] != 0:
                res += (predict[movie_index] - ratings_matrix[user_index][movie_index]) ** 2
                n += 1
    RMSE = math.sqrt(res / n) - 2
    # 计算覆盖率
    for user_index in range(len(user_list)):
        recommend = get_CFRecommend(user_sim_matrix, user_index, 10, 10)
        for movie in recommend:
            cover[movie[0]] = 1
    cover_rate = len(cover.keys()) / (len(movie_list)*split*0.3)
    # 输出评估结果
    print('RMSE={}\nCoverage={}'.format(RMSE, cover_rate))

# 读取表格数据
ratings = pd.read_csv('./dataset/ratings.csv', index_col=None)
movies = pd.read_csv('./dataset/movies.csv', index_col=None)
# 转为list
user_list = ratings['userId'].drop_duplicates().values.tolist()
movie_list = movies['movieId'].drop_duplicates().values.tolist()
# 获得map，便于后续处理
user_map, user_map_reverse = get_list_index_map(user_list)
movie_map, movie_map_reverse = get_list_index_map(movie_list)
# 获得评分矩阵
ratings_matrix = get_rating_matrix()


if __name__ == '__main__':
    k = 10 # KNN中的邻居数量
    n = 5  # 推荐给用户的电影的数量
    # 获取用户相似度矩阵
    print("Calculating···")
    print("Preparing for user similarity matrix···")
    user_sim_matrix_by_rating = get_user_sim_matrix(ratings_matrix)
    # 对每个用户进行推荐
    print("Recommending···")
    output = [['userId', 'movieId']]
    for user in user_list:
        for i in range(n):
            res = get_CFRecommend(user_sim_matrix_by_rating, user_map[user], k, n)
            output.append([user, movie_map_reverse[res[i][0]]])
            print('userId',user,"movieId",res[i][0])
    # 保存结果
    print("Saving result···")
    np.savetxt('./result/movie.csv', output, delimiter=',', fmt="%s")
    print("Result saved")
    # 评估结果
    print("Evaluating result···")
    evaluation(user_sim_matrix_by_rating)
