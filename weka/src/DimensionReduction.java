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
