## 一、处理思路

### 数据集

模型目标是为每个用户进行电影推荐，数据集是MovieLens数据集，数据被分为movies.csv，ratings.csv和tags.csv三张表格。

模型使用了movies.csv，ratings.csv中的数据。

### 总体思路

流程图如下：

![处理思路](http://img.bittersweet.top/markdown/%E5%A4%84%E7%90%86%E6%80%9D%E8%B7%AF.jpg)

## 二、使用算法

我使用了基于用户的最近邻协同过滤，可以有效的预测用户的评分，从而为用户进行推荐。

步骤如下：

1. 由数据集中的数据构建用户-电影评分矩阵
2. 使用皮尔逊相关系数来计算用户之间的相似度关系，构建用户相似度矩阵
3. 使用KNN，对于目标用户，根据相似度矩阵，选择k个与其相似度最高的用户
4. 根据KNN的结果，预测目标用户对所有电影的评分。这里默认选择k=10
5. 根据评分，排序推荐得分最高的n部电影

### 1. 用户-电影评分矩阵

根据ratings.csv下的内容，构建一个如下所示的二维数组

![image-20221025005403755](http://img.bittersweet.top/markdown/image-20221025005403755.png)

### 2. 用户相似度矩阵

使用皮尔逊相关系数来计算用户之间的相似度关系

公式如下：
$$
sim(x,y) = \frac{\sum\limits_{s \in S_{xy}}(r_{xs} - \overline{r_x})(r_{ys} - \overline{r_y})}{\sqrt{\sum\limits_{s \in S_{xy}}(r_{xs} - \overline{r_x})^2 }\sqrt{\sum\limits_{s \in S_{xy}}(r_{ys} - \overline{r_y})^2}}
$$
其中，$S_{xy}$指的是物品和用户x、y的相似性：$\overline{r_x}$和$\overline{r_y}$是x和y的均值评分

### 3. KNN

对于目标用户，根据相似度矩阵，选择k个与其相似度最高的用户。这里默认选择k=10

### 4. 预测评分

根据knn的结果，预测目标用户对所有电影的评分

计算公式如下：
$$
p(u,i)=\sum_{v\in S(u,k)\cap N(i)}w_{uv}r_{vi}
$$

### 5. 排序推荐高分电影

根据上一步得到的预测评分，对预测评分进行排序，向用户推荐预测评分最靠前的电影

需要注意的是，如果用户已经对某个电影评过分了，将不再重复推荐，因此需要在这里对将其预测评分修改为0

最后将结果保存到result文件夹下的movie.csv中

## 三、模型评估

### 测试集划分

数据集中，后20%的用户对后20%的电影评分为测试集

![1958143-20201018191119403-1042974421](http://img.bittersweet.top/markdown/1958143-20201018191119403-1042974421.png)

### 评估指标

#### RMSE

RMSE（Root Mean Square Error）均方根误差：真实值与差值的平方然后求和再平均，最后开根号。

公式如下：
$$
RMSE = \sqrt[]{\frac{1}{m}\sum_{i=1}^m(y_i-f(x_i))^2}
$$

#### Coverage

Coverage 覆盖率，表示为每位用户推荐预测评分top10的电影的并集与电影总集合的比例

### 评估结果

结果如图所示：

![image-20221025150403415](http://img.bittersweet.top/markdown/image-20221025150403415.png)

RMSE=0.88，说明模型对目标用户对电影的预测评分与真实评分误差较小，故推荐电影也比较符合用户口味

Coverage=0.92，说明模型推荐的电影覆盖范围广

综上所述：模型在预测用户的电影评分、推荐各种电影上表现优秀。

## 四、推荐结果

推荐结果保存在result文件夹下的movie.csv中，格式如下

![image-20221025143211154](http://img.bittersweet.top/markdown/image-20221025143211154.png)
