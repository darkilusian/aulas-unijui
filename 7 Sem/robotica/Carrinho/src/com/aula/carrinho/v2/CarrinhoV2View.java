package com.aula.carrinho.v2;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.*;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.SurfaceHolder;
import com.aula.carrinho.ParametrosActivity;
import com.aula.carrinho.TelaActivity;
import org.anddev.andengine.audio.music.MusicFactory;
import org.opencv.android.Utils;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

public class CarrinhoV2View extends CarrinhoV2ViewBase {

    private Mat imagemCamera;
    private final TelaActivity tela;
    MediaPlayer mediaPlayer;
    private int ultimaDirValido;
    private int ultimaEsqValido;
    int potencia = 0;
    int ignorarLinhas = 0;

    public CarrinhoV2View(Context context) {
        super(context);

        this.tela = (TelaActivity) context;
        MusicFactory.setAssetBasePath("mfx/");
        mediaPlayer = new MediaPlayer() {
        };
        try {
            mediaPlayer.reset();
            mediaPlayer.setLooping(true);
            AssetFileDescriptor assetFileDescritor = context.getAssets().openFd("mfx/acelera.mp3");
            mediaPlayer.setDataSource(assetFileDescritor.getFileDescriptor(), assetFileDescritor.getStartOffset(), assetFileDescritor.getLength());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception ex) {
            Log.e(TAG, "Erro ao Statea Musica", ex);
        }

        potencia = ParametrosActivity.potencia;
        ignorarLinhas = ParametrosActivity.ignorarLinhas;
    }

    @Override
    public void surfaceChanged(SurfaceHolder _holder, int format, int width, int height) {
        super.surfaceChanged(_holder, format, width, height);

        synchronized (this) {
            // initialize Mats before usage
            imagemCamera = new Mat(getCameraFrameWidth(), getCameraFrameHeight(), CvType.CV_8UC1);

        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
        mediaPlayer.stop();
    }

    protected Bitmap processFrame(byte[] data) {


        imagemCamera.put(0, 0, data);


        Imgproc.resize(imagemCamera, imagemCamera, new Size(getCameraFrameWidth(), getCameraFrameHeight()));
        //Monocromatico
        Imgproc.threshold(imagemCamera, imagemCamera, 100, 255d, Imgproc.THRESH_BINARY);



        ///Processa
        ////
        /*
         * Reconhecedor MATRIZ
         */

        //Numero de Divisoes Matrix
        int maxCols = 9;
        int maxRow = 9;
        int auxC = 0;//getRedFrameWidth()/maxCols;
        int auxR = 0;//getRedFrameHeight()/maxRow;
        int potenciaEsq = 80;
        int potenciaDir = 80;

        int meio = (maxCols / 2);
        String view = "";
        int[][] pontos = new int[maxCols][maxRow];
        Mat[][] matrix = new Mat[maxCols][maxRow];
        //
        int columSel = -1;
        int rowSel = -1;
        //

        for (int r = maxRow - 1; r != 0; r--) {
            for (int c = 0; c < maxCols; c++) {

                if (r < ignorarLinhas) {
                    continue;
                }
                if (((r == 2 || r == 3 || r == 4 || r == 5) && (c == 3 || c == 4 || c == 5))
                        || ((r == 6 || r == 7) && c == 5)) {//Ignorar Meios em forma de cone
//                    Log.v(TAG + "Ignorou", c + "x" + r);
                    continue;
                }
                //Quebra a Imagem em Subimagens
                auxC = (c * (imagemCamera.rows() / maxCols));
                auxR = (r * (imagemCamera.cols() / maxRow));
                int fimC = (c * (imagemCamera.rows() / maxCols)) + (imagemCamera.rows() / maxCols);
                int fimR = (r * (imagemCamera.cols() / maxRow)) + (imagemCamera.cols() / maxRow);
//                Log.i(TAG, "Submat " + "[" + c + "][" + r + "] " + auxR + "," + fimR + "," + auxC + "," + fimC);
                Mat submat = imagemCamera.submat(auxC, fimC, auxR, fimR);
                matrix[c][r] = submat;
                // FIm 

                //Varre a Subimagem para ver se é branco ou preto
                Bitmap aux = Bitmap.createBitmap(submat.cols(), submat.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(submat, aux);
                int totalPreto = 0;
                int totalBranco = 0;
                for (int i = 0; i < submat.cols(); i += 10) {//amostragem de 10 e 10
                    for (int j = 0; j < submat.rows(); j += 10) {
                        int pixel = aux.getPixel(i, j);
                        if (pixel == Color.BLACK) {
                            totalPreto++;
                        } else {
                            totalBranco++;
                        }
                    }
                }
                if (totalPreto > totalBranco) {
                    pontos[c][r] = Color.BLACK;
                } else {
                    pontos[c][r] = Color.WHITE;
                }
                //FIM


//                Log.i(TAG, "Coluna: " + c + " Branco: " + totalBranco + " Preto: " + totalPreto);

                if (pontos[c][r] == Color.BLACK && columSel < Math.abs(c - meio)) {
                    columSel = c;
                    rowSel = r;
                }
            }



        }

        /**
         * CODE OLD if (potenciaDir < 0) { potenciaDir = 0; } if (potenciaEsq <
         * 0) { potenciaEsq = 0; } if (potenciaDir > 100) { potenciaDir = 99; }
         * if (potenciaEsq > 100) { potenciaEsq = 99; }
         */
//        Log.v(TAG, columSel + "x" + rowSel);
        switch (columSel) {
            case 0://Bem no canto                 
                potenciaDir = potencia * 1;
                potenciaEsq = (int) (potencia * -1);
                break;
            case 1:
                potenciaDir = potencia * 1;
                potenciaEsq = (int) (potencia * -0.7);
                break;
            case 2:
                potenciaDir = potencia * 1;
                potenciaEsq = (int) (potencia * 0.1);
                break;
            case 3:// um pouco pra esquerda ajuste
                potenciaDir = (int) (potencia * 1);
                potenciaEsq = (int) (potencia * 0.8);
                break;
            case 4: // no meio Potencia Maxima a frente
                potenciaDir = (int) (potencia * 1);
                potenciaEsq = (int) (potencia * 1);
                break;
            case 5:
                potenciaEsq = potencia * 1;
                potenciaDir = (int) (potencia * 0.8);
                break;
            case 6:
                potenciaEsq = (int) (potencia * 1);
                potenciaDir = (int) (potencia * 0.1);
                break;
            case 7:
                potenciaEsq = potencia * 1;
                potenciaDir = (int) (potencia * -0.70);
                break;
            case 8:
                potenciaEsq = potencia * 1;
                potenciaDir = potencia * -1;
                break;
            default://Fudeu Engata RE
                potenciaEsq = (int) (potencia * -0.50);
                potenciaDir = (int) (potencia * -0.50);
                break;

        }
        if (potenciaEsq == (int) (potencia * -0.50) && potenciaDir == (int) (potencia * -0.50)) {
            /*
             * se for igual, não muda nada se esquerda é maior, estava andando
             * para a direita, logo, a ré deve tender para a direita a mesma
             * coisa (só que ao contrário) para a direita maior
             */
            if (ultimaEsqValido > ultimaDirValido) {
                potenciaEsq = (int) (potencia * -0.2);
            } else {
                potenciaDir = (int) (potencia * -0.2);
            }
        } else {
            ultimaEsqValido = potenciaEsq;
            ultimaDirValido = potenciaDir;
        }

//        if (potenciaDir == (int)(potencia*1) && potenciaEsq == (int)(potencia*1) ) {
//            tela.enviarCamera(90);
//        } else {
//            tela.enviarCamera(0);
//        }


//        Log.v(TAG + " Potencia", potenciaEsq + "x" + potenciaDir + "   \n" + view);
        tela.enviarPotencia(potenciaEsq, potenciaDir);

        if (ParametrosActivity.preview) {
            Bitmap bmp = Bitmap.createBitmap(getCameraFrameWidth(), getCameraFrameHeight(), Bitmap.Config.ARGB_8888);

            Utils.matToBitmap(imagemCamera, bmp);

            return bmp;
        } else {
            return null;
        }

    }

    @Override
    public void run() {
        super.run();

        synchronized (this) {
            // Explicitly deallocate Mats
            if (imagemCamera != null) {
                imagemCamera.release();
            }


            imagemCamera = null;

        }
    }

    public native void FindFeatures(long matAddrGr, long matAddrRgba);

    static {
        System.loadLibrary("mixed_sample");
    }
}
