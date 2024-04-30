package com.gk.study.sgrid.xt;

import java.util.ArrayList;
import java.util.List;

// 默认不加权实现
public interface CollaborativeFilter {
    // 过滤因子加权
    void addFactoryWeight(CollaborativeFactor factory);
    // 获取当前因子
    List<CollaborativeFactor> getCurrentFactories();
    // 提取基础赋权权重
    double getBaseFactory();
    // 限制因子列表返回长度
    void WithDefaultLength(Integer size);
    // 初始化过滤因子
    void WithDefaultFactories(ArrayList<CollaborativeFactor> factories);
    // 初始化最大因子值
    void WithLimitMaxFactoryWeight(Double maxWeight);
}
