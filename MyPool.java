/* Código Responsável pela atividade 1 usado como base a atividade feita em laboratório com os códigos disponibilizados no cods-lab11 */

import java.util.LinkedList;


class FilaTarefas {
    private final int nThreads;
    private final MyPoolThreads[] threads;
    private final LinkedList<Runnable> queue;
    private boolean shutdown;

    public FilaTarefas(int nThreads) {
        this.shutdown = false;
        this.nThreads = nThreads;
        queue = new LinkedList<Runnable>();
        threads = new MyPoolThreads[nThreads];
        for (int i=0; i<nThreads; i++) {
            threads[i] = new MyPoolThreads();
            threads[i].start();
        } 
    }

    public void execute(Runnable r) {
        synchronized(queue) {
            if (this.shutdown) return;
            queue.addLast(r);
            queue.notify();
        }
    }
    
    public void shutdown() {
        synchronized(queue) {
            this.shutdown=true;
            queue.notifyAll();
        }
        for (int i=0; i<nThreads; i++) {
          try { threads[i].join(); } catch (InterruptedException e) { return; }
        }
    }

    private class MyPoolThreads extends Thread {
       public void run() {
         Runnable r;
         while (true) {
           synchronized(queue) {
             while (queue.isEmpty() && (!shutdown)) {
               try { queue.wait(); }
               catch (InterruptedException ignored){}
             }
             if (queue.isEmpty()) return;   
             r = (Runnable) queue.removeFirst();
           }
           try { r.run(); }
           catch (RuntimeException e) {}
         } 
       } 
    } 
}

class Hello implements Runnable {
   String msg;
   public Hello(String m) { msg = m; }

   //--metodo executado pela thread
   public void run() {
      System.out.println(msg);
   }
}

class Primo implements Runnable {
   Integer num;
   int i;
   public Primo(Integer n){num = n;}
   public void run() {
    if((num <= 1)){
      System.out.println("numero " +num+" nao eh primo");
      return;
    }
    if (num == 2) {
      System.out.println("numero " +num+" eh primo");
      return;
    }
    if (num%2 == 0){
      System.out.println("numero " +num+" nao eh primo");
      return;
    }
    boolean ehPrimo = true;
    for (int i = 3; i <= Math.sqrt(num); i += 2) {
        if (num % i == 0) {
            ehPrimo = false;
            break;
        }
    }

    if (ehPrimo) {
        System.out.println("numero " + num + " eh primo");
    } else {
        System.out.println("numero " + num + " nao eh primo");
    }
   }
}


class MyPool {
  private static final int NTHREADS = 10;
  public static void main (String[] args) {
      Integer N = 100;// Ate que N quer analisar se é primo ou não
    //--PASSO 2: cria o pool de threads
      FilaTarefas pool = new FilaTarefas(NTHREADS); 
      
      //--PASSO 3: dispara a execução dos objetos runnable usando o pool de threads
      for (int i = 0; i < N; i++) {
        //final String m = "Hello da tarefa " + i;
        //Runnable hello = new Hello(m);
        //pool.execute(hello);
        Runnable primo = new Primo(i);
        pool.execute(primo);
      }

      
      pool.shutdown();
      System.out.println("Terminou");
   }
}
