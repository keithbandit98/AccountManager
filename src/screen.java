package accountmanager;

import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.ImageIcon;

/**
 *
 * @author Samuele
 */
public class screen extends javax.swing.JFrame implements Runnable
{
    @SuppressWarnings("unchecked")
    public screen() 
    {
      initComponents();
      try
      {
        ImageIcon icona = null;
        Image immagine = null;
        Image newimg = null;  
        Image icon = Toolkit.getDefaultToolkit().getImage("./src/accountmanager/img/CSV.png");
        this.setIconImage(icon);
        icona = new ImageIcon("./src/accountmanager/img/Green_csv_splash.jpg");
        immagine = icona.getImage();
        newimg = immagine.getScaledInstance(760, 410,  java.awt.Image.SCALE_SMOOTH);
        icona = new ImageIcon(newimg);
        LogoCSVPicture.setIcon(icona);
      }
      catch(NullPointerException ex)
      {
       System.out.println("ERRORE");
      }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        LogoCSVPicture = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(400, 300));
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(184, 211, 185));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        LogoCSVPicture.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LogoCSVPicture.setMinimumSize(new java.awt.Dimension(0, 0));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(LogoCSVPicture, javax.swing.GroupLayout.DEFAULT_SIZE, 694, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(LogoCSVPicture, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    @SuppressWarnings("unchecked")
    @Override
    public void run() 
    {
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        try 
        {
            Thread.sleep(2000);
        } 
        catch (InterruptedException ex) 
        {}
        this.setVisible(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LogoCSVPicture;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
