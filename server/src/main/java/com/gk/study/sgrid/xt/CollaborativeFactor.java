package com.gk.study.sgrid.xt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CollaborativeFactor {
    private Double Weight; // 权重
    private Integer Type; // 商品类型ID
    private String TypeName; // 类型名称

    @Override
    public String toString() {
        return "CollaborativeFactory{" +
                "Weight=" + Weight +
                ", Type=" + Type +
                ", TypeName='" + TypeName + '\'' +
                '}';
    }

    public void addWeight(Double weight){
        this.Weight += weight;
    }

    public void subWeight(Double weight){
        this.Weight -= weight;
    }
}
