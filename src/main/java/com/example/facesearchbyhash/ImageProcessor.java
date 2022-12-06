package com.example.facesearchbyhash;

import com.example.facesearchbyhash.Service.Impl.imageHashCodeService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ImageProcessor {
    public ImageProcessor() {
    }

    public void batchProcess(String path, IMAGE[] images) throws IOException {
        // 创建子目录 /Temp
        String dir = "Temp";
        Path dirPath = Paths.get(path + "/" + dir);
        Files.createDirectories(dirPath);

        // 转换为灰度图
        String destPath = path + "/" + dir;
        for (int i = 0; i < images.length; ++i) {
            convert2gray(path, destPath, images[i].getImagePath());
        }

        // 缩小图片尺寸
        resizeImage(destPath);

        // DCT变换
        DCT(path, images);
    }

    private int colorToRGB(int alpha, int red, int green, int blue) {
        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red;
        newPixel = newPixel << 8;
        newPixel += green;
        newPixel = newPixel << 8;
        newPixel += blue;

        return newPixel;
    }

    public void convert2gray(String sourcePath, String destPath, String imageName) throws IOException {
        BufferedImage bufferedImage
                = ImageIO.read(new File(sourcePath + "/" + imageName));
        BufferedImage grayImage =
                new BufferedImage(bufferedImage.getWidth(),
                        bufferedImage.getHeight(),
                        bufferedImage.getType());

        for (int i = 0; i < bufferedImage.getWidth(); i++) {
            for (int j = 0; j < bufferedImage.getHeight(); j++) {
                final int color = bufferedImage.getRGB(i, j);
                final int r = (color >> 16) & 0xff;
                final int g = (color >> 8) & 0xff;
                final int b = color & 0xff;
                int gray = (int) (0.3 * r + 0.59 * g + 0.11 * b);

                int newPixel = colorToRGB(255, gray, gray, gray);
                grayImage.setRGB(i, j, newPixel);
            }
        }
        File newFile = new File(destPath + "/" + imageName + ".png");
        ImageIO.write(grayImage, "png", newFile);
    }

    public void resizeImage(String path) throws IOException {
        ArrayList imagesName = new ArrayList();

        imageHashCodeService imageHashCodeService01=new imageHashCodeService();

        imageHashCodeService01.getImages(path, imagesName);

        for (int i = 0; i < imagesName.size(); ++i) {
            BufferedImage bufferedImage = ImageIO.read(new File(path + "/" + imagesName.get(i).toString()));
            Image tempImage = bufferedImage.getScaledInstance(8, 8, Image.SCALE_SMOOTH);
            BufferedImage resizeImage = new BufferedImage(8, 8, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = resizeImage.createGraphics();
            g2d.drawImage(tempImage, 0, 0, null);
            g2d.dispose();

            String newFileName = imagesName.get(i).toString().substring(0, imagesName.get(i).toString().indexOf(".png"));
            File newFile = new File(path + "/" + newFileName + ".png");
            ImageIO.write(resizeImage, "png", newFile);
        }
    }

    public static void DCT(String path, IMAGE[] images) throws IOException {
        ArrayList imagesName = new ArrayList();
        imageHashCodeService imageHashCodeService01=new imageHashCodeService();

        imageHashCodeService01.getImages(path + "/Temp/", imagesName);

        for (int i = 0; i < imagesName.size(); ++i) {
            BufferedImage bufferedImage
                    = ImageIO.read(new File(path + "/Temp/" + imagesName.get(i).toString()));

            int[][] RGBArray = convertImage2Array(bufferedImage);
            int[][] newRGBArray = DctTranform(RGBArray, 8, 8);

            // 计算均值
            double mean = 0;
            for (int j = 0; j < newRGBArray.length; ++j) {
                for (int k = 0; k < newRGBArray[0].length; ++k) {
                    mean += newRGBArray[j][k]/64.0;
                }
            }

            // 计算Hash
            StringBuffer hash = new StringBuffer();
            for (int j = 0; j < newRGBArray.length; ++j) {
                for (int k = 0; k < newRGBArray[0].length; ++k) {
                    if (newRGBArray[j][k] >= mean)
                        hash.append("1");
                    else
                        hash.append("0");
                }
            }

            images[i].setHashcode_64(hash.toString());
        }

    }

    private static double[][] coefficient(int n) {
        double[][] coeff = new double[n][n];
        double sqrt = 1.0 / Math.sqrt(n);
        for (int i = 0; i < n; i++) {
            coeff[0][i] = sqrt;
        }
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < n; j++) {
                coeff[i][j] = Math.sqrt(2.0 / n) * Math.cos(i * Math.PI * (j + 0.5) / (double) n);
            }
        }
        return coeff;
    }

    private static double[][] transposingMatrix(double[][] matrix, int n) {
        double nMatrix[][] = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                nMatrix[i][j] = matrix[j][i];
            }
        }
        return nMatrix;
    }

    private static double[][] matrixMultiply(double[][] A, double[][] B, int n) {
        double nMatrix[][] = new double[n][n];
        int t = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                t = 0;
                for (int k = 0; k < n; k++) {
                    t += A[i][k] * B[k][j];
                }
                nMatrix[i][j] = t;
            }
        }
        return nMatrix;
    }

    public static int[][] DctTranform(int[][] img, int n, int m) {
        double[][] iMatrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                iMatrix[i][j] = (double) (img[i][j]);
            }
        }
        double[][] quotient = coefficient(n); // 求系数矩阵
        double[][] quotientT = transposingMatrix(quotient, n); // 转置系数矩阵

        double[][] temp = new double[n][n];
        temp = matrixMultiply(quotient, iMatrix, n);
        iMatrix = matrixMultiply(temp, quotientT, n);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                img[i][j] = (int) (iMatrix[i][j]);
            }
        }
        return img;
    }

    public static int[][] convertImage2Array(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int[] color = new int[width * height];
        bufferedImage.getRGB(0, 0, width, height, color, 0, width);

        int[][] rgbArray = new int[height][width];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                rgbArray[i][j] = color[i * width + j];
        return rgbArray;
    }


}
