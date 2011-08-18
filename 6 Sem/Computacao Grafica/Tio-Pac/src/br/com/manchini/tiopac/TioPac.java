/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TioPac.java
 *
 * Created on 12/08/2011, 21:23:10
 */
package br.com.manchini.tiopac;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author manchini
 */
public class TioPac extends javax.swing.JFrame {

    boolean estaMovendo;
    private int goX;
    private int goY;

    /** Creates new form TioPac */
    public TioPac() {
        initComponents();
        estaMovendo = false;
        goX=tioPacman1.getX();
        goY=tioPacman1.getY();
        moveRun();
    }

    private void loop() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(200);
                        tioPacman1.paint(tioPacman1.getGraphics());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }).start();

    }

    private void vaiPara(int x, int y) {
        if (estaMovendo) {
            return;
        }
        estaMovendo = true;
        goX = x;
        goY = y;

//        tioPacman1.getGraphics().setColor(Color.black);
//        jPanel1.getGraphics().clearRect(0, 0, this.getWidth(), this.getHeight());
//        int xAnt = tioPacman1.getX();
//        int yAnt = tioPacman1.getY();
////        while (x != xAnt && y != yAnt) {
//        try {
////                Thread.sleep(100);
//            tioPacman1.move(x - tioPacman1.getHeight() / 2, y - tioPacman1.getWidth() / 2);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        }
    }

    private void moveRun() {
        new Thread(new Runnable() {

            @Override
            @SuppressWarnings("SleepWhileInLoop")
            public void run() {
                try {
                    while (true) {
                        int ax = -2;
                        int ay = -2;
                        if (tioPacman1.getX() <= goX) {
                            ax = 2;
                        }
                        if (tioPacman1.getY() <= goY) {
                            ay = 2;
                        }
                        for (int i = tioPacman1.getX(); i != goX; i += ax) {
                            for (int j = tioPacman1.getY(); j != goY; j += ay) {
                                if ((tioPacman1.getY() - goY) * ay > 0) {
                                    break;
                                }
                                Thread.sleep(10);
                                tioPacman1.setLocation(i, j);
                            }
                            tioPacman1.setLocation(i, tioPacman1.getY());
                            if ((tioPacman1.getX() - goX) * ax > 0) {
                                break;
                            }
                            Thread.sleep(10);
                        }
                        Thread.sleep(50);
                        estaMovendo = false;
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(TioPac.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        tioPacman1 = new br.com.manchini.tiopac.TioPacman();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(1, 1, 1));
        setForeground(java.awt.Color.black);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(1, 1, 1));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
        });
        jPanel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jPanel1MouseMoved(evt);
            }
        });
        jPanel1.setLayout(null);

        tioPacman1.setBackground(new java.awt.Color(1, 1, 1));

        javax.swing.GroupLayout tioPacman1Layout = new javax.swing.GroupLayout(tioPacman1);
        tioPacman1.setLayout(tioPacman1Layout);
        tioPacman1Layout.setHorizontalGroup(
            tioPacman1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 70, Short.MAX_VALUE)
        );
        tioPacman1Layout.setVerticalGroup(
            tioPacman1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );

        jPanel1.add(tioPacman1);
        tioPacman1.setBounds(353, 187, 70, 60);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-682)/2, (screenSize.height-480)/2, 682, 480);
    }// </editor-fold>//GEN-END:initComponents

private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
}//GEN-LAST:event_formMouseClicked

private void jPanel1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseMoved
}//GEN-LAST:event_jPanel1MouseMoved

private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked
    vaiPara(evt.getX(), evt.getY());
}//GEN-LAST:event_jPanel1MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TioPac.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TioPac.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TioPac.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TioPac.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                TioPac tioPac = new TioPac();
                tioPac.setVisible(true);
                tioPac.loop();

            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private br.com.manchini.tiopac.TioPacman tioPacman1;
    // End of variables declaration//GEN-END:variables
}