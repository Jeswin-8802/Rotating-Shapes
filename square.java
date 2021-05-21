import java.lang.*;
import java.util.Scanner;
import java.io.*;
import java.util.concurrent.*;

class BOX {
    double x,y;
    boolean bool;
    BOX(int x,int y)
    {
        this.x = x;
        this.y = y;
        bool = false;
    }
    void rotate(int deg,BOX p)
    {
        double radians = Math.toRadians((double)deg);
        x = (x-p.x)*Math.cos(radians)-(y-p.y)*Math.sin(radians) + p.x;
        y = (y-p.y)*Math.cos(radians)+(x-p.x)*Math.sin(radians) + p.y;
    }
}

public class square {
    public static void main(String... arg) throws IOException, InterruptedException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter size of the box //note that the size of the window is 40 x 40 \n inputs 18 to 28 gives best results \n also note that the terminal should be enlarged to full screen");
        int n = sc.nextInt();
        String window[][] = new String[42][42];
        BOX box[][] = new BOX[n][n];
        for(int i=0;i<42;i++)
        {
            for(int j=0;j<42;j++)
            {
                if((i==0 || i==41) && j>0 && j<41)
                    window[i][j] = "---";
                else if((j==0 || j==41) && i>0 && i<41)
                {
                    if(j==41)
                        window[i][j] = "  |";
                    else
                        window[i][j] = "|  ";
                }  
                else
                    window[i][j] = "   ";
            }
        }
        window[0][0] = "+--";
        window[0][41] = "--+";
        window[41][0] = "+--";
        window[41][41] = "--+";
        int a = 42/2-n/2;
        int b = 0, c = 0;
        for(int i=a;i<a + n;i++)
        {
            c = 0;
            for(int j=a;j<a + n;j++)
            {
                window[i][j] = " A ";
                box[b][c] = new BOX(i,j);
                c++;
            }
            b++;
        }
        BOX p = new BOX(21,21);
        while(a!=0)
        {
            try {
            	Thread.sleep((int)(0.05 * 1000));
            } catch (InterruptedException ie) {
            	Thread.currentThread().interrupt();
            }
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor(); 
            System.out.println();
            System.out.println();
            for(int i=0;i<42;i++)
            {
                System.out.print("      ");
                for(int j=0;j<42;j++)
                    System.out.print(window[i][j]);
                System.out.println();
            }
            for(int i=0;i<n;i++)
                for(int j=0;j<n;j++)
                    window[(int)box[i][j].x][(int)box[i][j].y] = "   ";
            for(int i=0;i<n;i++)
            {
                for(int j=0;j<n;j++)
                {
                    box[i][j].rotate(-5,p);
                    window[(int)box[i][j].x][(int)box[i][j].y] = " A ";
                }
            }
        }
        sc.close();
    }
}