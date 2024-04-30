package com.gk.study.sgrid.xt;

import java.util.ArrayList;
import java.util.List;

public class CollaborativeFilterImplTest {
    public static void main(String[] args) {
        CollaborativeFilterImpl collaborativeFilter = new CollaborativeFilterImpl();
        collaborativeFilter.WithDefaultLength(10);
        // 默认最大加权到75
        collaborativeFilter.WithLimitMaxFactoryWeight(70.0);
        ArrayList<CollaborativeFactor> collaborativeFactories = test();
        collaborativeFilter.WithDefaultFactories(collaborativeFactories);

        List<CollaborativeFactor> currentFactories = collaborativeFilter.getCurrentFactories();
        for (CollaborativeFactor collaborativeFactor : currentFactories) {
            System.out.println("测试 | "+ collaborativeFactor);
        }
    }

    public static ArrayList<CollaborativeFactor> test() {
        CollaborativeFactor a = new CollaborativeFactor(0.0,0,"冰箱");
        CollaborativeFactor b = new CollaborativeFactor(0.0,1,"彩电");
        CollaborativeFactor c = new CollaborativeFactor(0.0,2,"洗衣机");
        CollaborativeFactor d = new CollaborativeFactor(0.0,3,"空调");
        CollaborativeFactor e = new CollaborativeFactor(0.0,4,"热水器");
        CollaborativeFactor f = new CollaborativeFactor(0.0,5,"摩托车");
        CollaborativeFactor g = new CollaborativeFactor(0.0,6,"电脑");
        CollaborativeFactor h = new CollaborativeFactor(0.0,7,"电吹风");
        CollaborativeFactor i = new CollaborativeFactor(0.0,8,"平板");
        CollaborativeFactor j = new CollaborativeFactor(0.0,9,"衬衫");
        CollaborativeFactor k = new CollaborativeFactor(0.0,10,"鞋子");
        CollaborativeFactor l = new CollaborativeFactor(0.0,11,"杯子");
        CollaborativeFactor m = new CollaborativeFactor(0.0,12,"电动车");
        ArrayList<CollaborativeFactor> collaborativeFactories = new ArrayList<>();
        collaborativeFactories.add(a);
        collaborativeFactories.add(b);
        collaborativeFactories.add(c);
        collaborativeFactories.add(d);
        collaborativeFactories.add(e);
        collaborativeFactories.add(f);
        collaborativeFactories.add(g);
        collaborativeFactories.add(h);
        collaborativeFactories.add(i);
        collaborativeFactories.add(j);
        collaborativeFactories.add(k);
        collaborativeFactories.add(l);
        collaborativeFactories.add(m);
        return collaborativeFactories;
    }
}
