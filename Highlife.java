package jogodavida;

import java.lang.*;

class RunThE implements Runnable {
    private static int n;
    private int [][]grid;
    private int id;
    private int maxt = 8;
    // resultado da operação
    public double valor;
    public int [][]newgrid;
    
	// construtor
    public RunThE(int [][]Tgrid, int Tid, int Tn) {
        n = Tn;
        grid = Tgrid;
        id = Tid;
    }
    public static int getNeighbours(int [][]matriz, int i, int j){
        int vizinhos = 0;
        int a1 = i-1, a2 = i , a3 = i+1, b1 = j-1, b2 = j, b3 = j+1; //a == i, b == j
         // bordas horizontais
        if(i == n-1){
            a1 = n-2;
            a2 = n-1;
            a3 = 0;
        }
        if(i == 0){
           a1 = n-1;
           a2 = 0;
           a3 = 1;
        }
        // bordas verticais
        if(j == n-1){
            b1 = n-2;
            b2 = n-1;
            b3 = 0;
        }
        if(j == 0){
            b1 = n-1;
            b2 = 0;
            b3 = 1;
        }
        vizinhos += matriz[a1][b2] + matriz[a1][b3] + matriz[a1][b1] + matriz[a2][b3] + matriz[a2][b1] + matriz[a3][b1] + matriz[a3][b2] + matriz[a3][b3];
        return vizinhos;
    }
    
    public void run() {
        int vizinhos,h,i;
        newgrid = new int[n][n];
        for(i=((id*n)/maxt); i<((id+1)*n/maxt);i++){
            for(h=0;h<n;h++){
                vizinhos = getNeighbours(grid,i,h);
                if(grid[i][h] == 1){
                    if(vizinhos < 2){
                        newgrid[i][h] = 0;
                    }
                    else if(vizinhos == 2 || vizinhos == 3){
                        newgrid[i][h] = 1;
                    }   
                    else if(vizinhos >= 4){
                        newgrid[i][h] = 0;
                    }
                }
                else{
                    if(vizinhos == 3 || vizinhos ==6){
                        newgrid[i][h] = 1;
                    }
                    else{
                        newgrid[i][h] = 0;
                    }
                }          
            }
        }
        
    }
}


public class jogodavida{
    public static int n = 2048;
    public long start, end;
    
    public int vivos(int [][]grid){
        int i,j,vivos=0;
        for(i=0;i<n;i++){
            for(j=0;j<n;j++){
                vivos= vivos+grid[i][j];
            }
        }
        return vivos;
    }

    
	public static void main(String args[]) {
        int i,j,k,quantidade;
        int lin = 1, col =  1; 
        int maxt = 8;
        Thread[] th;
        RunThE[] rh;
        th = new Thread[maxt];
        rh = new RunThE[maxt];

        jogodavida exemplo = new jogodavida();
        
        int [][]grid = new int[n][n];
        for(i = 0; i<n;i++){
            for(j=0;j<n;j++){
                grid[i][j]=0;
            }
        }
                
        // GLIDER
        grid[lin  ][col+1] = 1;
        grid[lin+1][col+2] = 1;
        grid[lin+2][col  ] = 1;
        grid[lin+2][col+1] = 1;
        grid[lin+2][col+2] = 1;
        //th[i] = new Thread(rh[i]);
        
        //R-pentomino
        lin =10; col = 30;
        grid[lin  ][col+1] = 1;
        grid[lin  ][col+2] = 1;
        grid[lin+1][col  ] = 1;
        grid[lin+1][col+1] = 1;
        grid[lin+2][col+1] = 1;
        
        
        for(k=0;k<2000;k++){
            //exemplo.next_generation(grid,newgrid);
            for(int a = 0; a<maxt;a++){
                rh[a] = new RunThE(grid, a, n);
                th[a] = new Thread(rh[a]);
                th[a].start();
            }
            try {
                for(int a = 0; a<maxt;a++){
                    th[a].join();
                }
            }
            catch (InterruptedException e) { System.out.println("Excecao"); }
            for(int a=0;a<maxt;a++) {
            	for(i=a*n/maxt; i<((a+1)*n/maxt);i++){
                	System.arraycopy(rh[a].newgrid[i],0,grid[i],0,n);
            	}
            }

            quantidade = exemplo.vivos(grid);
            System.out.printf("Geracao %d: %d\n",k,quantidade);
        }
    }
}