import java.util.ArrayList;
import java.util.List;

/*
 * Simulação do modelo 1:1:
 * - Cada tarefa "de usuário" vira uma Thread real do SO.
 *
 * Uso:
 *   javac src/*.java
 *   java UmParaUm <N_TAREFAS> <ITERACOES_POR_TAREFA>
 *
 * Exemplo:
 *   java UmParaUm 100 5000000
 */
public class UmParaUm {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Uso: java UmParaUm <N_TAREFAS> <ITERACOES_POR_TAREFA>");
            return;
        }

        final int nTarefas = Integer.parseInt(args[0]);
        final int iteracoesPorTarefa = Integer.parseInt(args[1]);

        List<Thread> threads = new ArrayList<>(nTarefas);

        long t0 = System.nanoTime();

        for (int i = 0; i < nTarefas; i++) {
            Thread t = new Thread(new UnidadeDeTrabalho(iteracoesPorTarefa));
            threads.add(t);
            t.start();
        }

        for (Thread t : threads) {
            t.join();
        }

        long t1 = System.nanoTime();
        long ms = (t1 - t0) / 1_000_000;
        System.out.println("1:1 | N_tarefas=" + nTarefas +
                " | iteracoes_por_tarefa=" + iteracoesPorTarefa +
                " | tempo_ms=" + ms);
    }
}
