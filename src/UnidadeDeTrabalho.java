// Tarefa simples e repetitiva para ocupar CPU por um tempinho.
// Evitamos usar sleep para não depender do relógio do SO.
public class UnidadeDeTrabalho implements Runnable {

    private final int iteracoes;

    public UnidadeDeTrabalho(int iteracoes) {
        this.iteracoes = iteracoes;
    }

    @Override
    public void run() {
        long acc = 0L;
        for (int i = 1; i <= iteracoes; i++) {
            acc += i;
            acc ^= (acc << 1);
            acc += (i % 97);
        }
        // Usa o resultado para evitar eliminação pelo JIT
        if (acc == 42L) {
            System.out.print("");
        }
    }
}
