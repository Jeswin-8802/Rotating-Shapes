import java.io.IOException;

public class RotatingCube {
    static class Point {
        double x, y, z;

        Point(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    static class Cube {
        Point point;

        Cube(int x, int y, int z) {
            point = new Point(x, y, z);
        }

        void rotateX(int deg) {
            double radians = Math.toRadians(deg);
            point.y = point.y * Math.cos(radians) - point.z * Math.sin(radians);
            point.z = point.y * Math.sin(radians) + point.z * Math.cos(radians);
        }

        void rotateY(int deg) {
            double radians = Math.toRadians(deg);
            point.x = point.x * Math.cos(radians) + point.z * Math.sin(radians);
            point.z = point.z * Math.cos(radians) - point.x * Math.sin(radians);
        }

        void rotateZ(int deg) {
            double radians = Math.toRadians(deg);
            point.x = point.x * Math.cos(radians) - point.y * Math.sin(radians);
            point.y = point.x * Math.sin(radians) + point.y * Math.cos(radians);
        }

        void translate(Point p) {
            point.x += p.x;
            point.y += p.y;
            point.z += p.z;
        }
    }

    private static final int windowSize = 40;

    public static void main(String... arg) throws IOException, InterruptedException {
        String[][] window = new String[windowSize][windowSize];

        int side = (int) ((windowSize - 2) / Math.sqrt(3)) - windowSize / 10;

//           + Z
//           /
//          /
//         /
//        #----------------------- + X
//        |
//        |
//        |
//        |
//        |
//        |
//       + Y

        Cube[][][] cube = new Cube[side][side][6]; // 6 sides of a cube
        for (int i = 0; i < windowSize; i++) {
            for (int j = 0; j < windowSize; j++) {
                if ((i == 0 || i == windowSize - 1) && j > 0 && j < windowSize - 1)
                    window[i][j] = "---";
                else if ((j == 0 || j == windowSize - 1) && i > 0 && i < windowSize - 1) {
                    if (j == windowSize - 1)
                        window[i][j] = "  |";
                    else
                        window[i][j] = "|  ";
                } else
                    window[i][j] = "   ";
            }
        }
        window[0][0] = "+--";
        window[0][windowSize - 1] = "--+";
        window[windowSize - 1][0] = "+--";
        window[windowSize - 1][windowSize - 1] = "--+";

        for (int i = 1; i < windowSize - 1; i++) {
            for (int j = 1; j < windowSize - 1; j++)
                window[i][j] = "   ";
        }

        int a = (windowSize / 2) - (side / 2) + 1; // cube start pos
        int b = 0, c; // array iterator
        for (int i = a; i < a + side; i++) {
            c = 0;
            for (int j = a; j < a + side; j++) {
                // front face
                cube[b][c][0] = new Cube(i, j, side / 2);
                // top face
                cube[b][c][1] = new Cube(i, a, c - (side / 2));
                // left face
                cube[b][c][2] = new Cube(a, j, b - (side / 2));
                // bottom face
                cube[b][c][3] = new Cube(i, a + side - 1, c - (side / 2));
                // right face
                cube[b][c][4] = new Cube(a + side - 1, j, b - (side / 2));
                // back face
                cube[b][c][5] = new Cube(i, j, (side / 2) - 1);
                c++;
            }
            b++;
        }

        // to be used for translation and rotation
        Point temp = new Point((double) windowSize / 2.0, (double) windowSize / 2.0, 0);

        // clips faces that do not appear in view
        Double[][] clipWindow = new Double[windowSize][windowSize];

        while (a != 0) {
            try {
                Thread.sleep((int) (0.05 * 1000));
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }

            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }

            System.out.println();
            System.out.println();
            for (int i = 0; i < windowSize; i++) {
                System.out.print("      ");
                for (int j = 0; j < windowSize; j++)
                    System.out.print(window[i][j]);
                System.out.println();
            }

            // clear window
            for (int i = 1; i < windowSize - 1; i++)
                for (int j = 1; j < windowSize - 1; j++)
                    window[i][j] = "   ";

            for (int k = 0; k < 6; k++) {
                for (int i = 0; i < side; i++) {
                    for (int j = 0; j < side; j++) {
                        // move to origin
                        temp.x = -temp.x;
                        temp.y = -temp.y;
                        cube[i][j][k].translate(temp);

                        // rotate about origin
                        cube[i][j][k].rotateX(4);
                        cube[i][j][k].rotateY(4);
                        cube[i][j][k].rotateZ(4);

                        // translate back
                        temp.x = -temp.x;
                        temp.y = -temp.y;
                        cube[i][j][k].translate(temp);

                        // clipping
                        if (window[(int) cube[i][j][k].point.y][(int) cube[i][j][k].point.x].equals("   ")) {
                            clipWindow[(int) cube[i][j][k].point.y][(int) cube[i][j][k].point.x] = cube[i][j][k].point.z;
                            window[(int) cube[i][j][k].point.y][(int) cube[i][j][k].point.x] = getPatternForFace(k);
                        } else {
                            if (cube[i][j][k].point.z >= clipWindow[(int) cube[i][j][k].point.y][(int) cube[i][j][k].point.x])
                                window[(int) cube[i][j][k].point.y][(int) cube[i][j][k].point.x] = getPatternForFace(k);
                        }
                    }
                }
            }
        }
    }

    private static String getPatternForFace(int x) {
        switch (x) {
            case 0, 5 -> {
                return " # ";
            }
            case 1, 3 -> {
                return " O ";
            }
            case 2, 4 -> {
                return " . ";
            }
        }
        return "   ";
    }
}