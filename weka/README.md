# Cluster 实验报告

## 使用方法

本次实验使用使用SimpleKMeans方法来进行聚类

### 方法简介

​		k-均值算法（英文：k-means clustering）源于信号处理中的一种向量量化方法，现在则更多地作为一种聚类分析方法流行于数据挖掘领域。k-均值聚类的目的是：把 $n$ 个点（可以是样本的一次观察或一个实例)划分到 $k$ 个聚类中，使得每个点都属于离他最近的均值（此即聚类中心）对应的聚类，以之作为聚类的标准。这个问题将归结为一个把数据空间划分为Voronoi cells的问题。

### 算法

​		已知观测集（$x_1,x_2,···,x_n$），其中每个观测都是一个$d$-维实向量，k-均值聚类就是要把这 $n$ 个观测划分到$k$ 个集合中($k\le n$)，使得组内平方和最小。换句话说，它的目标是找的使得下式满足的聚类 $S_i$，
$$
\underset{S}{argmin}\sum_{i=1}^{k}\sum_{x\in S_i}||x-\mu_i||^2
$$
其中 $\mu_i$ 是 $S_i$ 中所有点的均值。

## 数据集处理思路

本次实验使用的数据集是iris.arff

### 数据集内容

关于鸢尾花的特征数据

### 数据集标签

- sepallength
- sepalwidth
- petallength
- petalwidth
- class
  - Iris-setosa,Iris-versicolor,Iris-virginica

### 数据集处理思路

1. 将数据集加载为weka的instances
2. 设置SimpleKMeans的随机数，保存数据顺序，生成3个簇
3. 建立聚类器进行聚类

## 实验结果

### 实验代码

```java
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class Cluster {
    public static void main(String[] args) {
        try{
            //使用DataSource类的read方法来加载arff文件
            Instances data = DataSource.read(".\\dataset\\cluster\\iris.arff");
            //使用SimpleKMeans方法来进行聚类
            SimpleKMeans kMeans = new SimpleKMeans();
            kMeans.setSeed(10);                     //设置随机种子
            kMeans.setPreserveInstancesOrder(true); //保存数据的顺序
            kMeans.setNumClusters(3);               //生成3个簇
            kMeans.buildClusterer(data);            //建立聚类器
            //输出结果
            int[] assignments = kMeans.getAssignments();
            int i = 0;
            for (int clusterNum : assignments) {
                System.out.printf("Instance %d -> Cluster %d\n", i, clusterNum);
                i++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

```



### 实验结果截图

![image-20221008162416240](http://img.nebular.site/md/image-20221008162416240.png)





# Classification 实验报告

## 使用方法

本次实验使用NaiveByes（朴素贝叶斯）的方法来进行数据分类

### 方法简介

​		朴素贝叶斯是一种建分类器的简单方法。该分类器模型会给问题实例分配用特征值表示的类标签，类标签取自有限集合。它不是训练这种分类器的单一算法，而是一系列基于相同原理的算法：所有朴素贝叶斯分类器都假定样本每个特征与其他特征都不相关。举个例子，如果一种水果其具有红，圆，直径大概3英寸等特征，该水果可以被判定为是苹果。尽管这些特征相互依赖或者有些特征由其他特征决定，然而朴素贝叶斯分类器认为这些属性在判定该水果是否为苹果的概率分布上独立的。

### 数学描述

#### 分类的数学描述

从数学角度来说，分类问题可做如下定义：已知集合 $C=y_1,y_2,·····y_n$ 和 $I=x_1,x_2····x_n$，确定映射规则 $y=f()$ ，使得任意 $x_i\in I$ 有且仅有一个 $y_i\in C$，使得 $y_i\in f(x_i)$ 成立。

其中 $C$ 叫做类别集合，其中每一个元素是一个类别，而 $I$ 叫做项集合（特征集合），其中每一个元素是一个待分类项，$f$ 叫做分类器。分类算法的任务就是构造分类器 $f$。

#### 朴素贝叶斯分类算法

朴素贝叶斯分类算法的核心算法是贝叶斯公示：
$$
P(B|A)=\frac{p(A|B)P(B)}{P(A)}
$$
换个表达形式：
$$
P(类别|特征)=\frac{p(特征|类别)P(类别)}{P(特征)}
$$
我们最终求的 $P(类别|特征)$即可，就相当于完成了我们的任务

### 方法优缺点

优点

- 算法逻辑简单,易于实现
- 分类过程中时空开销小

缺点：

- 理论上，朴素贝叶斯模型与其他分类方法相比具有最小的误差率。但是实际上并非总是如此，这是因为朴素贝叶斯模型假设属性之间相互独立，这个假设在实际应用中往往是不成立的，在属性个数比较多或者属性之间相关性较大时，分类效果不好。

## 数据集处理思路

本次实验使用的数据集是adult_income_uk.arff

### 数据集内容

一个国家的成年人收入

### 数据集标签

- age

- workclass

  - Private, Self-emp-not-inc, Self-emp-inc, Federal-gov, Local-gov, State-gov, Without-pay, Never-worked

- fnlwgt

- education

  -  Bachelors, Some-college, 11th, HS-grad, Prof-school, Assoc-acdm, Assoc-voc, 9th, 7th-8th, 12th, Masters, 1st-4th, 10th, Doctorate, 5th-6th, Preschool

- education-num

- marital-status

  - Married-civ-spouse, Divorced, Never-married, Separated, Widowed, Married-spouse-absent, Married-AF-spouse

- occupation

  - Tech-support, Craft-repair, Other-service, Sales, Exec-managerial, Prof-specialty, Handlers-cleaners, Machine-op-inspct, Adm-clerical, Farming-fishing, Transport-moving, Priv-house-serv, Protective-serv, Armed-Forces

- relationship

  - Wife, Own-child, Husband, Not-in-family, Other-relative, Unmarried

- race

  - White, Asian-Pac-Islander, Amer-Indian-Eskimo, Other, Black

- sex

  - Female, Male

- capital-gain

- capital-loss

- hours-per-week

- native-country

  - United-States, Cambodia, England, Puerto-Rico, Canada, Germany, Outlying-US(Guam-USVI-etc), India, Japan, Greece, South, China, Cuba, Iran, Honduras, Philippines, Italy, Poland, Jamaica, Vietnam, Mexico, Portugal, Ireland, France, Dominican-Republic, Laos, Ecuador, Taiwan, Haiti, Columbia, Hungary, Guatemala, Nicaragua, Scotland, Thailand, Yugoslavia, El-Salvador, Trinadad&Tobago, Peru, Hong, Holand-Netherlands

- class

  - >50K, <=50K

### 数据集处理思路

1. 将数据集加载为weka的instances
2. 打乱数据集
3. 按8：2的比例划分训练集和测试集
4. 设置分类目标类别为`class`
5. 在训练集上建立朴素贝叶斯分类器
6. 使用测试集对模型预测正确率进行评价

## 实验结果

### 实验代码

```java
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

public class Classification {
    public static void main(String[] args) {
        try{
            //使用DataSource类的read方法来加载arff文件
            Instances data = ConverterUtils.DataSource.read(".\\dataset\\classification\\adult_income_uk.arff");
            //使用朴素贝叶斯实现分类
            NaiveBayes naivebayes = new NaiveBayes();
            //打乱数据集
            data.randomize(new java.util.Random(0));
            //划分训练集和测试集
            int trainDataSize = (int) Math.round(data.numInstances()*0.8);
            int testDataSize = data.numInstances() - trainDataSize;
            Instances trainData = new Instances(data, 0, trainDataSize);
            Instances testData = new Instances(data, trainDataSize, testDataSize);
            //设置目标类别
            trainData.setClassIndex(trainData.numAttributes()-1);
            testData.setClassIndex(testData.numAttributes()-1);
            //建立贝叶斯分类器
            naivebayes.buildClassifier(trainData);
            //对模型进行评价
            Evaluation evaluation = new Evaluation(trainData);
            evaluation.evaluateModel(naivebayes, testData);
            System.out.println(evaluation.toSummaryString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

```

### 实验结果截图

![image-20221008160622490](http://img.nebular.site/md/image-20221008160622490.png)

# Dimension Reduction 实验报告

## 1. 使用方法

本次实验使用的降维方法是PCA（Principal Component Analysis，主成分分析），这是一种常见的数据分析方式，常用于高维数据的降维，可用于提取数据的主要特征分量。

### 基本思想

![220px-GaussianScatterPCA](http://img.nebular.site/md/220px-GaussianScatterPCA.png)

- 将坐标轴中心移到数据的中心，然后旋转坐标轴，使得数据在C1轴上的方差最大，即全部n个数据个体在该方向上的投影最为分散。意味着更多的信息被保留下来。C1成为**第一主成分**。
- C2**第二主成分**：找一个C2，使得C2与C1的协方差（相关系数）为0，以免与C1信息重叠，并且使数据在该方向的方差尽量最大。
- 以此类推，找到第三主成分，第四主成分……第p个主成分。p个随机变量可以有p个主成分

​		主成分分析经常用于减少数据集的维数，同时保留数据集当中对方差贡献最大的特征。这是通过保留低维主成分，忽略高维主成分做到的。这样低维成分往往能够保留住数据的最重要部分。但是，这也不是一定的，要视具体应用而定。由于主成分分析依赖所给数据，所以数据的准确性对分析结果影响很大

### 数学定义

​		PCA的数学定义是：一个正交化线性变换，把数据变换到一个新的坐标系统中，使得这一数据的任何投影的第一大方差在第一个坐标（称为第一主成分）上，第二大方差在第二个坐标（第二主成分）上，依次类推。

​		定义一个 $n \times m$ 的矩阵，$X^T$ 为去平均值（以平均值为中心移动至原点）的数据，其行为数据样本，列为数据类别。则 $X$ 的奇异值分解为 $X=W\Sigma V^T$，其中 $m\times m$ 矩阵 $W$ 是 $XX^T$ 的特征向量矩阵，$\Sigma$ 是 $m\times m$ 的非负矩形对角矩阵，$V$ 是 $n\times n$ 的 $XX^T$​ 的特征向量矩阵。据此，
$$
\begin{equation}
\begin{split}
Y^T&=X^TW\\
&=V\Sigma^TW^TW\\
&=V\Sigma^T
\end{split}
\end{equation}
$$
当 $m<n-1$ 时，$V$ 在通常情况下不是唯一定义的，而 $Y$ 是唯一定义的。$W$ 是一个正交矩阵，$Y^TW=X^T$，且 $Y^T$ 的第一列由第一主成分组成，第二列由第二主成分组成，依此类推。

​		为了得到一种降低数据维度的有效办法，我们可以利用 $W_L$ 把 $X$​ 映射到一个只应用前面L个向量的低维空间中去：
$$
Y=W_L^TX=\Sigma_LV^T
$$
其中$\Sigma_L=I_{L\times m}\Sigma$，且 $I_{L\times m}$ 为 $L\times m$ 的单位矩阵。

​		$X$ 的单向量矩阵 $W$ 相当于协方差矩阵的特征向量 $C=XX^T$, $XX^T=W\Sigma\Sigma^TW^T$

## 2. 数据集处理思路

本次实验使用的数据集是titanic.arff

### 数据集内容

关于泰坦尼克号乘客的信息。

### 数据集标签

- class
  - 1st，2nd，3rd，crew
- age
  - adult，child
- sex
  - male，female
- survived
  - yes，no

### 数据集处理思路

1. 将数据集加载为weka的instances
2. 使用weka工具包中的pca进行降维

## 3. 实验结果

### 实验代码

```java
import weka.attributeSelection.PrincipalComponents;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class DimensionReduction {
    public static void main(String[] args) {
        try{
            //使用DataSource类的read方法来加载arff文件
            Instances data = DataSource.read(".\\dataset\\dimension_reduce\\titanic.arff");
            //PCA降维
            PrincipalComponents pca = new PrincipalComponents();
            pca.buildEvaluator(data);
            //输出结果
            System.out.println(pca);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}

```

### 结果截图

![image-20221008153311420](http://img.nebular.site/md/image-20221008153311420.png)