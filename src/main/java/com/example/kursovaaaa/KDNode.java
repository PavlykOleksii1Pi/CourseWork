package com.example.kursovaaaa;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

public class KDNode {
    double[] point;
    KDNode left;
    KDNode right;

    public KDNode(double[] point) {
        this.point = point;
        this.left = this.right = null;
    }
}
