
package com.example.kursovaaaa;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

public class KDTree {
    private long startTime;
    private long endTime;
    KDNode root = null;
    int k;

    public KDTree(int k) {
        this.k = k;
    }

    public void insert(double[] point) {
        this.root = this.insertRec(this.root, point, 0);
    }

    private KDNode insertRec(KDNode root, double[] point, int depth) {
        if (root == null) {
            return new KDNode(point);
        } else {
            int cd = depth % this.k;
            if (point[cd] < root.point[cd]) {
                root.left = this.insertRec(root.left, point, depth + 1);
            } else {
                root.right = this.insertRec(root.right, point, depth + 1);
            }

            return root;
        }
    }

    public double[] nearestNeighbor(double[] point) {
        return this.nearestNeighborRec(this.root, point, 0, (double[])null, Double.MAX_VALUE);
    }

    private double[] nearestNeighborRec(KDNode root, double[] point, int depth, double[] bestPoint, double bestDist) {
        if (root == null) {
            return bestPoint;
        } else {
            double dist = this.euclideanDistance(root.point, point);
            if (dist < bestDist) {
                bestDist = dist;
                bestPoint = root.point;
            }

            int cd = depth % this.k;
            double d = point[cd] - root.point[cd];
            KDNode goodSide = d < 0.0 ? root.left : root.right;
            KDNode badSide = d < 0.0 ? root.right : root.left;
            bestPoint = this.nearestNeighborRec(goodSide, point, depth + 1, bestPoint, bestDist);
            if (d * d < bestDist) {
                bestPoint = this.nearestNeighborRec(badSide, point, depth + 1, bestPoint, bestDist);
            }

            return bestPoint;
        }
    }

    private double euclideanDistance(double[] a, double[] b) {
        double sum = 0.0;

        for(int i = 0; i < this.k; ++i) {
            sum += (a[i] - b[i]) * (a[i] - b[i]);
        }

        return Math.sqrt(sum);
    }
    public void startTimer() {
        startTime = System.currentTimeMillis();
    }

    public void stopTimer() {
        endTime = System.currentTimeMillis();
    }

    public long getExecutionTime() {
        return endTime - startTime;
    }
}

