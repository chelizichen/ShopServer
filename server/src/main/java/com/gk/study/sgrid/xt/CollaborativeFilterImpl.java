package com.gk.study.sgrid.xt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CollaborativeFilterImpl implements CollaborativeFilter {
    // 赋权列表
    public ArrayList<CollaborativeFactor> factories;
    public Integer factorySize;
    // 基础因子，每种类型的默认赋权值
    public double BaseFactory;
    // 最大因子值，保证不会出现只推荐一种的情况
    public double MaxWeight;

    // 加权
    @Override
    public void addFactoryWeight(CollaborativeFactor factory) {
        // 查询最大加权因子
        CollaborativeFactor MaxWeightFactory = factories.get(0);
        for (CollaborativeFactor f : factories) {
            if (f.getWeight() >= MaxWeightFactory.getWeight()) {
                MaxWeightFactory = f;
            }
        }
        // 需要加权的不是当前最大加权的因子，则减权
        if (!factory.getType().equals(MaxWeightFactory.getType()) && !MaxWeightFactory.getWeight().equals(BaseFactory)) {
            MaxWeightFactory.subWeight(this.getBaseFactory());
        }
        // 加权
        for (CollaborativeFactor f : factories) {
            // 查找相同权重并添加
            if (f.getType().equals(factory.getType())) {
                // 如果当前赋权因子超过最大值，则不添加
                if (f.getWeight() >= MaxWeight) {
                    break;
                }
                f.addWeight(this.getBaseFactory());
            }
        }
    }

    // 拿到当前加权列表
    @Override
    public List<CollaborativeFactor> getCurrentFactories() {
        // 基础因子随机排序
        List<CollaborativeFactor> baseList = factories.stream().
                filter(factory -> factory.getWeight().equals(BaseFactory)).
                collect(Collectors.toList());
        Collections.shuffle(baseList);

        List<CollaborativeFactor> weightList = factories.stream().filter(factory -> !factory.getWeight().equals(BaseFactory))
                .sorted(Comparator.comparingDouble(CollaborativeFactor::getWeight).reversed())
                .collect(Collectors.toList());
        int count = 0;
        double currentTotalWeight = 0.0;
        for (CollaborativeFactor collaborativeFactor : weightList) {
            double templateWeight = currentTotalWeight;
            currentTotalWeight += collaborativeFactor.getWeight();
            if (currentTotalWeight >= 100) {
                collaborativeFactor.setWeight(100 - templateWeight);
                return weightList;
            }
        }
        System.out.println("加权因子权重总值 | " + currentTotalWeight);
        while (weightList.size() < factorySize) {
            if (currentTotalWeight >= 100) {
                break;
            }
            currentTotalWeight += baseList.get(count).getWeight();
            weightList.add(baseList.get(count));
            count++;
        }
        return weightList;
    }

    @Override
    public double getBaseFactory() {
        return this.BaseFactory;
    }

    @Override
    public void WithDefaultLength(Integer size) {
        this.factorySize = size;
        this.BaseFactory = (100.0 / size);
    }

    @Override
    public void WithDefaultFactories(ArrayList<CollaborativeFactor> factories) {
        this.factories = factories;
        for (CollaborativeFactor factory : this.factories) {
            factory.setWeight(this.getBaseFactory());
        }
    }

    @Override
    public void WithLimitMaxFactoryWeight(Double maxWeight) {
        this.MaxWeight = maxWeight;
    }
}
