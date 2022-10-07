# HAKE: Hierarchy-Aware Knowledge Graph Embedding
## 依赖
- Python 3.6+
- [PyTorch](http://pytorch.org/) 1.0+

## 结果
### ![实验结果](http://img.bittersweet.top/markdown/%E5%AE%9E%E9%AA%8C%E7%BB%93%E6%9E%9C.png)

保存的模型和训练日志、测试日志在models目录下

## 运行

### 使用方法
```
bash runs.sh {train | valid | test} {ModE | HAKE} {wn18rr | FB15k-237 | YAGO3-10} <gpu_id> \
<save_id> <train_batch_size> <negative_sample_size> <hidden_dim> <gamma> <alpha> \
<learning_rate> <num_train_steps> <test_batch_size> [modulus_weight] [phase_weight]
```
- `{ | }`: 单选项
- `< >`: 必选项.
- `[ ]`: 可选项

**注意**: `[modulus_weight]` 和 `[phase_weight]` 只对 `HAKE` 模型可用.

为了复现HAKE和ModE的结果，请运行以下命令：

> linux系统环境下请在项目文件目录下运行以下命令
>
> windows系统环境下请使用git bash等在项目文件目录下运行以下命令

### HAKE
```bash
# WN18RR
bash runs.sh train HAKE wn18rr 0 0 512 1024 500 6.0 0.5 0.00005 80000 8 0.5 0.5

# FB15k-237
bash runs.sh train HAKE FB15k-237 0 0 1024 256 1000 9.0 1.0 0.00005 100000 16 3.5 1.0

# YAGO3-10
bash runs.sh train HAKE YAGO3-10 0 0 1024 256 500 24.0 1.0 0.0002 180000 4 1.0 0.5
```

### ModE
```bash
# WN18RR
bash runs.sh train ModE wn18rr 0 0 512 1024 500 6.0 0.5 0.0001 80000 8 --no_decay

# FB15k-237
bash runs.sh train ModE FB15k-237 0 0 1024 256 1000 9.0 1.0 0.0001 100000 16

# YAGO3-10
bash runs.sh train ModE YAGO3-10 0 0 1024 256 500 24.0 1.0 0.0002 80000 4
```

