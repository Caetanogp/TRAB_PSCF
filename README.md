Integrantes: Caetano Padoin e Gastão Borges
Disciplina: Performance em Sistemas Ciber-Físicos
Instituição: Pontifícia Universidade Católica do Paraná
Período: Quarto A

Comparação de Desempenho: Threads N:M vs 1:1 (Java)

Objetivo: Comparar N:M e 1:1 medindo o tempo total para a mesma carga.

Como executar

Requisitos: Java 17+.

Compilar
mkdir -p out
javac -d out src/*.java

Executar manualmente (exemplos)
# N:M
java -cp out ModeloNM 100 5000000 8

# 1:1
java -cp out UmParaUm 100 5000000

Executar tudo (gera resultados/tempos.csv)

Windows (CMD):

scripts\executar_tudo.bat

Metodologia 

Carga: UnidadeDeTrabalho realiza operações inteiras simples em loop (CPU-bound) por um número fixo de iterações.

N:M: uso de ExecutorService com pool fixo (M threads) executando N tarefas.

1:1: criação de uma Thread por tarefa e join() no final.

Métrica: tempo total com System.nanoTime(), reportado em milissegundos.

Parâmetros do experimento

Iterações por tarefa (ITER): 5_000_000

Pool (M) no N:M: 8

Quantidades de tarefas (N): 10, 100, 500, 1000

Resultados

Arquivo gerado: resultados/tempos.csv

N	N:M (ms)	1:1 (ms)
10	 23	         24
100	 81	         86
500	 316	     191
1000 618	     351

Bruto (do CSV):

modelo,N,iteracoes,M_pool,tempo_ms
NM,10,5000000,8,23
1:1,10,5000000,NA,24
NM,100,5000000,8,81
1:1,100,5000000,NA,86
NM,500,5000000,8,316
1:1,500,5000000,NA,191
NM,1000,5000000,8,618
1:1,1000,5000000,NA,351

Análise

Nos cenários menores (N = 10 e 100), o N:M ficou um pouquinho melhor ou empatou com o 1:1. Nossa leitura é que o pool ajuda a segurar o overhead de criação/gerenciamento de threads quando a carga é pequena, então os dois acabam próximos.

Quando aumentamos para N = 500 e 1000, o 1:1 passou o N:M com folga. Mesmo criando muitas threads, o Sistema Operacional (SO) conseguiu paralelizar melhor e evitou a fila do pool (no N:M, só 8 threads trabalham de cada vez e o resto espera). Na nossa máquina, com POOL=8, o “ponto de virada” ficou entre 100 e 500 tarefas. Ou seja: para volumes maiores, o 1:1 foi mais vantajoso aqui.

Resumo:

N pequeno: N:M ≈ 1:1 (ligeira vantagem do N:M).

N grande: 1:1 ganha, provavelmente por reduzir espera em fila e aproveitar melhor o paralelismo disponível.

Esse “ponto de virada” depende do hardware e do M do pool. Se testarmos POOL=4 ou 16, pode mudar.

Observações rápidas

Se o teste ficar muito lento/rápido, ajuste ITER nos scripts.

Para resultados mais estáveis, dá para rodar cada cenário 3x e tirar média.



Estrutura do projeto
mapeamento-threads-benchmark/
├─ src/
│  ├─ UnidadeDeTrabalho.java
│  ├─ ModeloNM.java
│  └─ UmParaUm.java
├─ scripts/
│  └─ executar_tudo.bat
├─ resultados/
│  └─ tempos.csv      # gerado pelos scripts
└─ README.md (este arquivo)