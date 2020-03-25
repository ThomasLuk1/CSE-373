package seamcarving;

import edu.princeton.cs.algs4.Picture;

public class DualGradientEnergyFunction implements EnergyFunction {
    @Override
    public double apply(Picture picture, int x, int y) {
        return getEnergy(picture, x, y);
    }
    private double getEnergy(Picture picture, int x, int y) {
        int height = picture.height();
        int width = picture.width();
        double dXSquared;
        double dYSquared;
        if (x == width - 1) {
            dXSquared = xBD(picture, x, y);
        } else if (x == 0) {
            dXSquared = xFD(picture, x, y);
        } else {
            dXSquared = xCD(picture, x, y);
        }
        if (y == height - 1) {
            dYSquared = yBD(picture, x, y);
        } else if (y == 0) {
            dYSquared = yFD(picture, x, y);
        } else {
            dYSquared = yCD(picture, x, y);
        }
        return pythagoras(dXSquared, dYSquared);
    }

    private double xCD(Picture picture, int x, int y) {
        double fXRed = picture.get(x + 1, y).getRed() - picture.get(x - 1, y).getRed();
        double fXGreen = picture.get(x + 1, y).getGreen() - picture.get(x - 1, y).getGreen();
        double fXBlue = picture.get(x + 1, y).getBlue() - picture.get(x - 1, y).getBlue();
        return squareAll(fXRed, fXGreen, fXBlue);
    }

    private double yCD(Picture picture, int x, int y) {
        double fYRed = picture.get(x, y + 1).getRed() - picture.get(x, y - 1).getRed();
        double fYGreen = picture.get(x, y + 1).getGreen() - picture.get(x, y - 1).getGreen();
        double fYBlue = picture.get(x, y + 1).getBlue() - picture.get(x, y - 1).getBlue();
        return squareAll(fYRed, fYGreen, fYBlue);
    }

    private double xFD(Picture picture, int x, int y) {
        double fXRed = -3 * picture.get(x, y).getRed() + 4 * picture.get(x + 1, y).getRed()
            - picture.get(x + 2, y).getRed();
        double fXGreen = -3 * picture.get(x, y).getGreen() + 4 * picture.get(x + 1, y).getGreen()
            - picture.get(x + 2, y).getGreen();
        double fXBlue = -3 * picture.get(x, y).getBlue() + 4 * picture.get(x + 1, y).getBlue()
            - picture.get(x + 2, y).getBlue();
        return squareAll(fXRed, fXGreen, fXBlue);
    }

    private double yFD(Picture picture, int x, int y) {
        double fYRed = -3 * picture.get(x, y).getRed() + 4 * picture.get(x, y + 1).getRed()
            - picture.get(x, y + 2).getRed();
        double fYGreen = -3 * picture.get(x, y).getGreen() + 4 * picture.get(x, y + 1).getGreen()
            - picture.get(x, y + 2).getGreen();
        double fYBlue = -3 * picture.get(x, y).getBlue() + 4 * picture.get(x, y + 1).getBlue()
            - picture.get(x, y + 2).getBlue();
        return squareAll(fYRed, fYGreen, fYBlue);
    }

    private double xBD(Picture picture, int x, int y) {
        double fXRed = -3 * picture.get(x, y).getRed() + 4 * picture.get(x - 1, y).getRed()
            - picture.get(x - 2, y).getRed();
        double fXGreen = -3 * picture.get(x, y).getGreen() + 4 * picture.get(x - 1, y).getGreen()
            - picture.get(x - 2, y).getGreen();
        double fXBlue = -3 * picture.get(x, y).getBlue() + 4 * picture.get(x - 1, y).getBlue()
            - picture.get(x - 2, y).getBlue();
        return squareAll(fXRed, fXGreen, fXBlue);
    }

    private double yBD(Picture picture, int x, int y) {
        double fYRed = -3 * picture.get(x, y).getRed() + 4 * picture.get(x, y - 1).getRed()
            - picture.get(x, y - 2).getRed();
        double fYGreen = -3 * picture.get(x, y).getGreen() + 4 * picture.get(x, y - 1).getGreen()
            - picture.get(x, y - 2).getGreen();
        double fYBlue = -3 * picture.get(x, y).getBlue() + 4 * picture.get(x, y - 1).getBlue()
            - picture.get(x, y - 2).getBlue();
        return squareAll(fYRed, fYGreen, fYBlue);
    }

    private double pythagoras(double dXSquared, double dYSquared) {
        return Math.sqrt(dXSquared + dYSquared);
    }

    private double squareAll(double red, double green, double blue) {
        return Math.pow(red, 2) + Math.pow(green, 2) + Math.pow(blue, 2);
    }
}
