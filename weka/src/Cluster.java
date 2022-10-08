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
