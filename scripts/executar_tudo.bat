@echo off
setlocal enabledelayedexpansion

REM ------------------------------------------------------------
REM  Benchmark N:M vs 1:1 (Java)
REM  Gera resultados\tempos.csv com os tempos por cenário.
REM ------------------------------------------------------------

set ITER=5000000
set POOL=8
set SIZES=10 100 500 1000

REM compilar (gera a pasta out se não existir)
if not exist out mkdir out
javac -d out src\*.java

REM preparar CSV de saída
if not exist resultados mkdir resultados
set "CSV=resultados\tempos.csv"
echo modelo,N,iteracoes,M_pool,tempo_ms > "%CSV%"

for %%N in (%SIZES%) do (
  REM --- N:M ---
  for /f "delims=" %%L in ('java -cp out ModeloNM %%N %ITER% %POOL%') do set "NM_OUT=%%L"
  echo !NM_OUT!
  REM Linha exemplo (N:M):
  REM N:M | N_tarefas=100 | M_threads_pool=8 | iteracoes_por_tarefa=5000000 | tempo_ms=86
  REM Divide por "=" e pega o 5º token (o número do tempo)
  for /f "tokens=5 delims==" %%t in ("!NM_OUT!") do set "NM_MS=%%t"
  echo NM,%%N,%ITER%,%POOL%,!NM_MS!>>"%CSV%"

  REM --- 1:1 ---
  for /f "delims=" %%L in ('java -cp out UmParaUm %%N %ITER%') do set "OTO_OUT=%%L"
  echo !OTO_OUT!
  REM Linha exemplo (1:1):
  REM 1:1 | N_tarefas=100 | iteracoes_por_tarefa=5000000 | tempo_ms=91
  REM Divide por "=" e pega o 4º token (o número do tempo)
  for /f "tokens=4 delims==" %%t in ("!OTO_OUT!") do set "OTO_MS=%%t"
  echo 1:1,%%N,%ITER%,NA,!OTO_MS!>>"%CSV%"
)

echo.
echo Pronto. Resultados em "%CSV%"
