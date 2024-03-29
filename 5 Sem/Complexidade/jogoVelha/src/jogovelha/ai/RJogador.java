/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jogovelha.ai;

import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import jogovelha.interfaces.Jogador;
import javax.swing.JOptionPane;
import jogovelha.marcacao.Ponto;
import jogovelha.tabuleiro.Tabuleiro;

/**
 *
 * @author manchini
 * Metodod Jogador Recursivo
 */
public class RJogador implements Jogador {

    private Tabuleiro tabuleiro;
    private static final byte eu = Tabuleiro.JOGADOR_COMPUTADOR;
    private int leuPontos = 0;
    private boolean primeiraRodada = true;

    public RJogador() {
        init();
    }

    private void init() {
    }

    @Override
    /**
     * Caso o Computador Comece o Jogo
     */
    public void comecar() {

        leuPontos = 0;
        primeiraRodada = false;
        pense(new Ponto(-1, -1), new Ponto(-1, -1));

    }

    /**
     * 
     * @param ponto 
     */
    public void minhaVez(Ponto ponto) {

        Ponto tp = tabuleiro.verificarPossivelVencedor(Tabuleiro.COMPUATADOR_VENCER);
        if (tp != null) {
            jogue(tp);
            return;
        }
        tp = tabuleiro.verificarPossivelVencedor(Tabuleiro.HUMANO_VENCER);
        if (tp != null) {
            jogue(tp);
            return;
        }

        tp = verificaJogadaDiagonal();
        if (tp != null) {
            jogue(tp);
            return;
        }

        leuPontos = 0;
        if (!tabuleiro.existemCasas()) {
            return;
        }
        primeiraRodada = true;
        pense(ponto, new Ponto(-1, -1));
        if (primeiraRodada) {
            primeiraRodada = false;
        }

    }

    /**
     * Metodo para encontrar o melhor ponto de jogo
     * @param ponto
     * @param melhorPonto 
     */
    public void pense(Ponto ponto, Ponto melhorPonto) {
        if (leuPontos >= 9) {
            jogue(melhorPonto);
            return;
        }

        /*
         * Se for Primeira rodada e o humano comecar
         *  em um canto
         * ele joga no meio
         * para evitar a jogada diagonal
         */
        if (primeiraRodada
                && ponto.isCanto()
                && tabuleiro.estaLivre(new Ponto(1, 1))) {

            melhorPonto = new Ponto(1, 1);
            leuPontos = 9;
            jogue(melhorPonto);
            return;

        } else if (primeiraRodada) {
            primeiraRodada = false;
        }

        /**
         * Anda pelas Casas
         */
        ponto.somar(1);


        if (melhorPonto != null
                && ponto.isCenter()
                && tabuleiro.estaLivre(ponto)) {
            melhorPonto = new Ponto(ponto.linha, ponto.coluna);

        }

        /*
         * Se não for o centro e  melhor ponto nao for o centro
         * e for um canto
         */
        if (melhorPonto != null
                && !melhorPonto.isCenter()
                && ponto.isCanto()
                && tabuleiro.estaLivre(ponto)) {
            melhorPonto = new Ponto(ponto.linha, ponto.coluna);
        }


        /***a
         * Se o melhor ponto nao for nem um canto
         */
        if (melhorPonto != null
                && !(melhorPonto.isCanto())
                && !melhorPonto.isCenter()
                && !ponto.isCanto()
                && tabuleiro.estaLivre(ponto)) {
            melhorPonto = new Ponto(ponto.linha, ponto.coluna);
        }

        leuPontos++;
        pense(ponto, melhorPonto);

    }

    private Ponto verificaJogadaDiagonal() {
        ArrayList<Ponto> listaPontosHumano = new ArrayList<Ponto>();
        int pontosHumandos = 0;
        for (int l = 0; l < tabuleiro.getTabuleiro().length; l++) {
            for (int c = 0; c < tabuleiro.getTabuleiro()[l].length; c++) {
                if ((l == 0 || l == 2) && (c == 0 || c == 2)) {
                    if (tabuleiro.getTabuleiro()[l][c] == Tabuleiro.JOGADOR_HUMANO) {
                        listaPontosHumano.add(new Ponto(l, c));
                    }
                }
                if (tabuleiro.getTabuleiro()[l][c] == Tabuleiro.JOGADOR_HUMANO) {
                    pontosHumandos++;
                }

            }
        }
        //Se Tiver 2 pontos nos Cantros 
        //Cuidado ele vai fazer a diagonal
        if (pontosHumandos == 2 && listaPontosHumano.size() == 2) {
            listaPontosHumano.get(0).somar(3);
            return listaPontosHumano.get(0);
        } else {
            return null;
        }
    }

    private void jogue(Ponto p) {
        tabuleiro.jogar(eu, p.linha, p.coluna);
        leuPontos = 9;
    }

    @Override
    public void setTabuleiro(Tabuleiro tabuleiroReal) {
        this.tabuleiro = tabuleiroReal;
    }

    public void gameIsOver(byte vencedor) {
        if (vencedor == eu) {
            JOptionPane.showMessageDialog(null, "MUHHUAHAHAHAHA!!!", "Computador diz...", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Se aproveitam de minha nobreza...", "Computador diz...", JOptionPane.INFORMATION_MESSAGE, new javax.swing.ImageIcon(getClass().getResource("/jogovelha/bitmaps/chapolin.png")));
        }
    }

    public void novoJogo() {
        primeiraRodada = true;
    }
}
