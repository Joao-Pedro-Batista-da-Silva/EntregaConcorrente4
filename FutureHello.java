/* Código Responsável pela atividade 3 usado como base a atividade feita em laboratório com os códigos disponibilizados no cods-lab11 */

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import java.util.ArrayList;
import java.util.List;



class MyCallable implements Callable<Long> {
  //construtor
  MyCallable() {
  }
 
  
  public Long call() throws Exception {
    long s = 0;
    for (long i=1; i<=100; i++) {
      s++;
    }
    return s;
  }
}

class ehPrimo implements Callable<Boolean>{
  private final long number;
  public ehPrimo(int number){
    this.number = number;
  }

  public Boolean call(){
    if(number<=1) return false;
    if(number == 2) return true;
    if(number%2 == 0)return false;
    for(int i = 3; i<Math.sqrt(number)+1;i+=2){
      if(number%i == 0)return false;
    }
    return true;

  }

}


public class FutureHello  {
  //private static final int N = 3;
  private static final int NTHREADS = 1;

  public static void main(String[] args) {
    //cria um pool de threads (NTHREADS)
    Integer N = 1000;// Ate que N quer analisar se é primo ou não
    ExecutorService executor = Executors.newFixedThreadPool(NTHREADS);
    //cria uma lista para armazenar referencias de chamadas assincronas
    List<Future<Long>> list = new ArrayList<Future<Long>>();
    List<Future<Boolean>> results = new ArrayList<>();
    List<Integer> nums = new ArrayList<>();
    for (Integer i = 0; i < N; i++) {
      Callable<Boolean> checker = new ehPrimo(i);
      Future<Boolean> result = executor.submit(checker);

      results.add(result);
      nums.add(i);
    }
    int sum = 0;
    for(int i = 0; i<results.size();i++){
      try{
        if(results.get(i).get()){
          System.out.println(nums.get(i) + " eh primo");
          sum+=1;
        }
        //else System.out.println(nums.get(i) + " nao eh primo");
      }
      catch(InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    }
    executor.shutdown();
    System.out.println("numero total de primos de 1 ate a "+ N + " eh de: "+ sum);
    //System.out.println(list.size());
    //pode fazer outras tarefas...

    //recupera os resultados e faz o somatório final
    //long sum = 0;
    ///for (Future<Long> future : list) {
      //try {
      //  sum += future.get(); //bloqueia se a computação nao tiver terminado
      //} catch (InterruptedException e) {
      // e.printStackTrace();
      //} catch (ExecutionException e) {
      //  e.printStackTrace();
      //}
    //}
    //System.out.println(sum);
    //executor.shutdown();
  }
}
