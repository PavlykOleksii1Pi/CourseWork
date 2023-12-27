package com.example.kursovaaaa;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class KDTreeApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("KD Tree Application");

        Label dimensionLabel = new Label("Введіть розмірність простору (k):");
        TextField dimensionField = new TextField();

        Label pointsLabel = new Label("Введіть кількість точок:");
        TextField pointsField = new TextField();

        Button buildTreeButton = new Button("Побудувати KD-дерево");
        TextArea outputArea = new TextArea();

        // Стилі для кольору
        dimensionLabel.setStyle("-fx-text-fill: #336699; -fx-font-weight: bold;");
        pointsLabel.setStyle("-fx-text-fill: #336699; -fx-font-weight: bold;");
        buildTreeButton.setStyle("-fx-text-fill: #336699; -fx-font-weight: bold;");
        outputArea.setStyle("-fx-border-color: #333333; -fx-border-width: 7px;");
        outputArea.setStyle("-fx-control-inner-background: #e0e0e0; -fx-text-fill: #FF0000; -fx-font-weight: bold;");

        VBox root = new VBox();
        root.setSpacing(10);
        root.setPadding(new Insets(10, 10, 10, 10));
        root.getChildren().addAll(dimensionLabel, dimensionField, pointsLabel, pointsField, buildTreeButton, outputArea);

        // Підрозділ для введення координат точок
        VBox coordinatesInput = new VBox();
        coordinatesInput.setSpacing(10);

        // Підрозділ для введення координат точки для пошуку найближчого сусіда
        VBox queryPointInput = new VBox();
        queryPointInput.setSpacing(10);

        buildTreeButton.setOnAction(e -> {
            int k = Integer.parseInt(dimensionField.getText());
            KDTree tree = new KDTree(k);

            int pointsCount = Integer.parseInt(pointsField.getText());
            double[][] pointsArray = new double[pointsCount][k];

            for (int i = 0; i < pointsCount; ++i) {
                double[] point = new double[k];
                String input = getInput("Введіть координати точки " + (i + 1) + " (через пробіл):");
                String[] coordinates = input.split(" ");

                for (int j = 0; j < k; ++j) {
                    point[j] = Double.parseDouble(coordinates[j]);
                }

                pointsArray[i] = point;
                tree.insert(point);
            }

            // Заповнення підпункту для введення координат точки для пошуку найближчого сусіда
            String queryInput = getInput("Введіть координати точки для пошуку найближчого сусіда (через пробіл):");
            String[] queryCoordinates = queryInput.split(" ");
            double[] queryPoint = new double[k];

            for (int j = 0; j < k; ++j) {
                queryPoint[j] = Double.parseDouble(queryCoordinates[j]);
            }

            // Алгоритм для KD-дерева
            interface KDTreeAlgorithm {
                double[] findNearestNeighbor(KDTree tree, double[] queryPoint);
            }

            KDTreeAlgorithm kdTreeAlgorithm = new KDTreeAlgorithm() {
                @Override
                public double[] findNearestNeighbor(KDTree tree, double[] queryPoint) {
                    return tree.nearestNeighbor(queryPoint);
                }
            };

            long kdTreeStartTime = System.currentTimeMillis();
            double[] nearestKDTree = kdTreeAlgorithm.findNearestNeighbor(tree, queryPoint);
            long kdTreeEndTime = System.currentTimeMillis();
            System.out.println("Час виконання для KD-дерева: " + (kdTreeEndTime - kdTreeStartTime) + " мс");
            // Алгоритм наївного пошуку найближчого сусіда
            class NaiveAlgorithm {
                public static double[] findNearestNeighbor(double[][] points, double[] queryPoint) {
                    if (points.length == 0 || points[0].length != queryPoint.length) {
                        throw new IllegalArgumentException("Invalid input");
                    }

                    double minDistance = Double.MAX_VALUE;
                    double[] nearestNeighbor = null;

                    for (double[] point : points) {
                        double distance = calculateEuclideanDistance(point, queryPoint);

                        if (distance < minDistance) {
                            minDistance = distance;
                            nearestNeighbor = point.clone();
                        }
                    }

                    return nearestNeighbor;
                }

                private static double calculateEuclideanDistance(double[] point1, double[] point2) {
                    if (point1.length != point2.length) {
                        throw new IllegalArgumentException("Points must have the same dimension");
                    }

                    double sum = 0.0;

                    for (int i = 0; i < point1.length; i++) {
                        sum += Math.pow(point1[i] - point2[i], 2);
                    }

                    return Math.sqrt(sum);
                }
            }

            long naiveStartTime = System.currentTimeMillis();
            double[] nearestNaive = NaiveAlgorithm.findNearestNeighbor(pointsArray, queryPoint);
            long naiveEndTime = System.currentTimeMillis();
            System.out.println("Час виконання для наївного алгоритму: " + (naiveEndTime - naiveStartTime) + " мс");

            StringBuilder result = new StringBuilder("Найближчий сусід (KD-дерево): (");

            for (int i = 0; i < k; ++i) {
                result.append(nearestKDTree[i]);
                if (i < k - 1) {
                    result.append(", ");
                }
            }

            result.append(")");
            result.append("\nЧас виконання для KD-дерева: ").append(kdTreeEndTime - kdTreeStartTime).append(" мс\n");

            result.append("Найближчий сусід (Наївний алгоритм): (");
            for (int i = 0; i < k; ++i) {
                result.append(nearestNaive[i]);
                if (i < k - 1) {
                    result.append(", ");
                }
            }

            result.append(")");
            result.append("\nЧас виконання для наївного алгоритму: ").append(naiveEndTime - naiveStartTime).append(" мс");

            outputArea.setText(result.toString());
        });

        root.getChildren().addAll(coordinatesInput, queryPointInput);

        Scene scene = new Scene(root, 800, 800);

        // Стилі для кольору фону сцени
        scene.getRoot().setStyle("-fx-background-color: rgba(255, 0, 0, 0.5);");

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private String getInput(String prompt) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(null);
        dialog.setContentText(prompt);
        return dialog.showAndWait().orElse("");
    }
}