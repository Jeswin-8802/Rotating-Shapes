import java.lang.*;
import java.util.Scanner;
import java.io.*;
import java.util.concurrent.*;

class BOX {
    double x,y,z;
    String S;
    boolean bool;
    BOX(int x,int y,int z,String S)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.S = S;
        bool = false;
    }
    BOX(int x,int y,int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        bool = false;
    }
    void rotateJ(int deg,BOX p)
    {
        double radians = Math.toRadians((double)deg);
        x = (x-p.x)*Math.cos(radians)+(z-p.z)*Math.sin(radians) + p.x;
        z = (z-p.z)*Math.cos(radians)-(x-p.x)*Math.sin(radians) + p.z;
    }
    void rotateZ(int deg,BOX p)
    {
        double radians = Math.toRadians((double)deg);
        x = (x-p.x)*Math.cos(radians)-(y-p.y)*Math.sin(radians) + p.x;
        y = (y-p.y)*Math.cos(radians)+(x-p.x)*Math.sin(radians) + p.y;
    }
    void translate(BOX p)
    {
        x += p.x;
        y += p.y;
        z += p.z;
    }
}

public class cube {
    public static void main(String... arg) throws IOException, InterruptedException {
        int n = 17;
        String window[][] = new String[46][46];
        BOX box[][][] = new BOX[n][n][6];
        for(int i=0;i<46;i++)
        {
            for(int j=0;j<46;j++)
            {
                if((i==0 || i==45) && j>0 && j<45)
                    window[i][j] = "---";
                else if((j==0 || j==45) && i>0 && i<45)
                {
                    if(j==45)
                        window[i][j] = "  |";
                    else
                        window[i][j] = "|  ";
                }  
                else
                    window[i][j] = "   ";
            }
        }
        window[0][0] = "+--";
        window[0][45] = "--+";
        window[45][0] = "+--";
        window[45][45] = "--+";
        int a = 46/2-n/2;
        int b = 0, c = 0;
        for(int i=a;i<a + n;i++)
        {
            c = 0;
            for(int j=a;j<a + n;j++)
            {
                box[b][c][0] = new BOX(i,j,0," # ");
                box[b][c][1] = new BOX(i,a,c," O ");
                box[b][c][2] = new BOX(a,j,b," . ");
                box[b][c][3] = new BOX(i,a+n-1,c," O ");
                box[b][c][4] = new BOX(a+n-1,j,b," . ");
                box[b][c][5] = new BOX(i,j,n-1," # ");
                c++;
            }
            b++;
        }
        BOX p1 = new BOX(2*n/5,n/6,0);
        BOX p2 = new BOX(a+2*n/5,a+n/6,0);
        BOX p3 = new BOX(a+2*n/5,0,0);
        for(int k=0;k<6;k++)
            for(int i=0;i<n;i++)
                for(int j=0;j<n;j++)
                    box[i][j][k].translate(p1);
        
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
            for(int i=0;i<46;i++)
            {
                System.out.print("      ");
                for(int j=0;j<46;j++)
                    System.out.print(window[i][j]);
                System.out.println();
            }
            for(int k=0;k<6;k++)
                for(int i=0;i<n;i++)
                    for(int j=0;j<n;j++)
                        window[(int)box[i][j][k].x][(int)box[i][j][k].y] = "   ";
            for(int k=0;k<6;k++)
            {
                for(int i=0;i<n;i++)
                {
                    for(int j=0;j<n;j++)
                    {
                        box[i][j][k].rotateZ(-5,p2);
                        box[i][j][k].rotateJ(-5,p3);
                        window[(int)box[i][j][k].x][(int)box[i][j][k].y] = box[i][j][k].S;
                    }
                }
            }
        }
    }
}