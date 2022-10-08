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
