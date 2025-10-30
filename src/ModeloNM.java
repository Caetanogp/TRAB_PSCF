import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*
 * Simulação do modelo N:M:
 * - N tarefas "de usuário"
 * - M threads de SO (pool fixo) executando as tarefas de forma multiplexada
 *
 * Uso:
 *   javac src/*.java
 *   java ModeloNM <N_TAREFAS> <ITERACOES_POR_TAREFA> <M_THREADS_POOL>
 *
 * Exemplo:
 *   java ModeloNM 100 5000000 8
 */
public class ModeloNM {
    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.out.println("Uso: java ModeloNM <N_TAREFAS> <ITERACOES_POR_TAREFA> <M_THREADS_POOL>");
            return;
        }

        final int nTarefas = Integer.parseInt(args[0]);
        final int iteracoesPorTarefa = Integer.parseInt(args[1]);
        final int mThreads = Integer.parseInt(args[2]);

        ExecutorService pool = Executors.newFixedThreadPool(mThreads);
        List<Future<?>> futuros = new ArrayList<>(nTarefas);

        long t0 = System.nanoTime();

        for (int i = 0; i < nTarefas; i++) {
            futuros.add(pool.submit(new UnidadeDeTrabalho(iteracoesPorTarefa)));
        }

        for (Future<?> f : futuros) {
            f.get(); // espera cada tarefa terminar
        }

        long t1 = System.nanoTime();
        pool.shutdown();

        long ms = (t1 - t0) / 1_000_000;
        System.out.println("N:M | N_tarefas=" + nTarefas +
                " | M_threads_pool=" + mThreads +
                " | iteracoes_por_tarefa=" + iteracoesPorTarefa +
                " | tempo_ms=" + ms);
    }
}
