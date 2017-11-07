package accountmanager;

import static accountmanager.DirectoryManager.findPathDesktopLinux;
import java.awt.Color;
import java.awt.Image;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/**
 *
 * @author Samuele
 */
public class Updater implements Runnable
{

    private String SAVE_DIR = javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory()+"/";
    private Socket socketClient = null;
    private JTextField Message = null;
    private PrintWriter socketOut = null;
    private String Version = null;
    private File inFile = null;
    private JLabel GifPicture = null;
    private JMenuItem Update = null;
    private JFrame frame = null;
    private JLabel Picture = null;
    private JButton buttonInstallazione = null;
    private JProgressBar BarDownload = null;
    private JTextField TextDownload = null;
    private JProgressBar BarUpdater = null;
    private boolean statoAggiornamento = false;
    private boolean statoThread = true;

    public Updater(JTextField MessageText,String VersionSoftware,JLabel gif,JMenuItem updateCheck,JFrame FrameAggiornamento,JLabel PictureDownload,JButton buttonInstall,JProgressBar bar,JTextField text,JProgressBar barGround)
    {
     Message=MessageText;
     Version=VersionSoftware;
     GifPicture=gif;
     Update=updateCheck;
     frame=FrameAggiornamento;
     Picture=PictureDownload;
     buttonInstallazione=buttonInstall;
     BarDownload=bar;
     TextDownload=text;
     BarUpdater=barGround;
    }

    @Override
    public void run()
    {
        try
        {
         ImageIcon icona = null;
         Image immagine = null;
         Image newimg = null;
         double progress=0;
         int bar=0;
         double datiBuffer=0;
         
         //in base al sistema operativo salvo il file scaricato dal server sul desktop relativo
         if(System.getProperty("os.name").contains("Windows"))
          SAVE_DIR = javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory()+"/";
         if(System.getProperty("os.name").contains("Linux")||System.getProperty("os.name").contains("Ubuntu"))
          SAVE_DIR = findPathDesktopLinux()+"/";
         
        System.out.println(SAVE_DIR);
         
        /*System.setProperty("javax.net.ssl.trustStore", "myTrustStore");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
        System.setProperty("javax.net.ssl.keyStoreType", "pkcs12");
        System.setProperty("javax.net.ssl.keyStore", "new.p12");
        System.setProperty("javax.net.ssl.keyStorePassword", "newpasswd");*/

        // Fornisco le chiavi ed il certificato con le relative password
        /*System.setProperty("javax.net.ssl.keyStore", "C:\\_Certificati\\keystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "password");
        System.setProperty("javax.net.ssl.trustStore", "C:\\_Certificati\\truststore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "password");*/
         
         Message.setText("Mi sto connettendo al server...");
         searchGif();
         InetAddress address = InetAddress.getByName("kronoserver.ddns.net");
         System.out.println(address.getHostAddress());
         System.out.println(address.getHostName());
         
         //SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
         //SSLSocket socketClient = (SSLSocket) factory.createSocket(address.getHostAddress(),1337);
         
         Socket socketClient = new Socket(address.getHostAddress(),1337);

         Message.setText("Connesione stabilita...");
         PrintWriter socketOut =new PrintWriter(socketClient.getOutputStream()); //invio stringa al server
         ObjectInputStream oin = new ObjectInputStream(socketClient.getInputStream()); //intercetto il file in arrivo

          String inputLine = Version; //scrivo stringa
          socketOut.println(inputLine); //invio la stringa
          socketOut.flush();

          try
          {
           Update.setEnabled(false);
           BarUpdater.setValue(0);
           File nameNewFile= (File) oin.readObject();
           inFile = new File(SAVE_DIR+nameNewFile.getName());

           Message.setText("Download in corso...");
           FileOutputStream fos=new FileOutputStream(inFile);
           byte[] buf = new byte[8196];
           int i=0,c=0,x=0;
           //riga per riga leggo il file originale per scriverlo nello stream del file destinazione
           bar=oin.readInt(); //ricevo la dimensione del file

           while((x=x+i)<bar)
           {
            BarUpdater.setVisible(true);
            i=oin.read(buf);
            datiBuffer=i;
            progress=progress+(100*(datiBuffer/bar));
            BarUpdater.setValue((int) progress); 
            fos.write(buf,0,i);
            c++;
            System.out.println("ciclo: "+c+" i="+i);
            if(statoThread==false)
            {
              fos.close();
              socketClient.close();
              throw new InterruptedException();
            }
            statoAggiornamento=true;
           }
           //chiudo gli streams
           fos.close();
           BarUpdater.setVisible(false);
           System.out.println(" --ricezione completata");
           statoAggiornamento=false;
           TextDownload.setText("Installare aggiornamento?");

           frame.setSize(425,495);
           frame.setLocationRelativeTo(null);
           frame.setVisible(true);
           buttonInstallazione.setEnabled(false);
           BarDownload.setValue(0);
           BarDownload.setForeground(Color.green);
           BarDownload.setVisible(true);
           icona = new ImageIcon("./src/accountmanager/img/update.png");
           immagine = icona.getImage();
           newimg = immagine.getScaledInstance(Picture.getWidth(),Picture.getHeight(),java.awt.Image.SCALE_SMOOTH);
           icona = new ImageIcon(newimg);
           Picture.setIcon(icona);
           buttonInstallazione.setEnabled(true);
           Message.setText("Ricezione aggiornamento completata: file.zip in desktop");
           TickGif();
           //File saveFile = new File(SAVE_DIR+"/"+inFile.getName()); //imposto il nuovo file che dovro' salvare prendendone il nome originale
           //save(fos,saveFile); //salvo il file
          }
          catch(EOFException e)
          {
            BarUpdater.setVisible(false);
            Message.setText("Nessun aggiornamento disponibile");
            allarmGif();
          }
          catch (ClassNotFoundException ex)
          {
            BarUpdater.setVisible(false);
            Message.setText("Oggetto sconosciuto");
            allarmGif();
          }
          Thread.sleep(1000);
          Update.setEnabled(true);
        }
        /*catch(SocketException e)
        {
         newIcon = new ImageIcon("./src/accountmanager/attenzione.gif");
         Gif.setIcon(newIcon);
         Message.setText("Connessione fallita");
        }*/
        catch(ConnectException e)
        {
         BarUpdater.setVisible(false);
         Message.setText("Connessione fallita! Server non disponibile");
         allarmGif();
        }
        catch(NoSuchElementException e)
        {
         BarUpdater.setVisible(false);
         Message.setText("Connessione persa");
         allarmGif();
        }
        catch(UnknownHostException e)
        {
         BarUpdater.setVisible(false);
         Message.setText("Non esistono host/server associati a questo dominio");
         allarmGif();
        }
        catch (IOException ex)
        {
         BarUpdater.setVisible(false);
         Message.setText("Errore I/O");
         allarmGif();
        }
        catch (InterruptedException ex)
        {
         System.out.println("STOP");
         BarUpdater.setVisible(false);
         String pathzip=cercaFileZip().getName();
         /*try
         {
          Process p = Runtime.getRuntime().exec(System.getProperty("user.home")+"/Desktop/"+pathzip);
          p.destroy();
         }
         catch (IOException e)
         {}*/
         if(System.getProperty("os.name").contains("Windows"))
            new File(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory()+"/"+pathzip).delete();
         if(System.getProperty("os.name").contains("Linux")||System.getProperty("os.name").contains("Ubuntu"))
            new File(findPathDesktopLinux()+"/"+pathzip).delete();
         
        } 
        catch (InvocationTargetException ex)
        {
            Logger.getLogger(Updater.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
         try
         {
          socketClient.close();
         }
         catch(NullPointerException e)
         {}
         catch (IOException ex)
         {
          BarUpdater.setVisible(false);
         }
        }
    }

    public void setStatoThread(Boolean stato)
    {
     statoThread=stato;
    }
    
    public File cercaFileZip()
    {
     File f =null;
     File d = null;
     int verioneFileZip=0,versioneSoftwareCorrente=0;
     if(System.getProperty("os.name").contains("Windows"))
       d = new File(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath());
     if(System.getProperty("os.name").contains("Linux")||System.getProperty("os.name").contains("Ubuntu"))
       d = new File(findPathDesktopLinux());
     
     String array[] = d.list(); //creo un array di stringhe e lo riempio con la lista dei files presenti nella directory
     System.out.println("stampo la lista dei files contenuti nella directory:");
     for (int i=0;i<array.length;i++)
     {
      System.out.println(i+1+"."+array[i]);
      if(array[i].contains("AccountManager_Version_"))
      {
       verioneFileZip=Integer.parseInt(array[i].replaceAll("AccountManager_Version_","").replaceAll(".zip","").replaceAll("\\.",""));
       versioneSoftwareCorrente=Integer.parseInt(Version.replaceAll("\\.",""));
       if(verioneFileZip>versioneSoftwareCorrente)
       {
        f = new File(array[i]);
        break;
       }
      }
     }
     return f;
    }

    private void save(FileOutputStream fos, File saveFile)throws IOException
    {
      FileInputStream fis=new FileInputStream(inFile); //apro uno stream sul file che e' stato inviato
      //FileOutputStream fos=new FileOutputStream(saveFile); //scrivo uno stream per il salvataggio del nuovo file

      byte[] buf = new byte[1024];
      int i=0;
      //riga per riga leggo il file originale per scriverlo nello stream del file destinazione
      while((i=fis.read(buf))!=-1)
      {
       fos.write(buf, 0, i);
      }
      // chiudo gli strams
      fis.close();
      fos.close();
      System.out.println(" --ricezione completata");
    }

    public void allarmGif()
    {
     ImageIcon newIcon = new ImageIcon("./src/accountmanager/img/attenzione.gif");
     GifPicture.setIcon(newIcon);
     GifPicture.setVisible(true);
     TimerTask mt= new TimerTask()
     {
      @Override
       public void run()
       {
        GifPicture.setVisible(false);
       }
     };
     java.util.Timer timer = new java.util.Timer();
     timer.schedule(mt,1500);
    }

    public void TickGif()
    {
     ImageIcon newIcon = new ImageIcon("./src/accountmanager/img/tick.gif");
     GifPicture.setIcon(newIcon);
     GifPicture.setVisible(true);
     TimerTask mt= new TimerTask()
     {
      @Override
       public void run()
       {
        GifPicture.setVisible(false);
       }
     };
     java.util.Timer timer = new java.util.Timer();
     timer.schedule(mt,1500);
    }

    public void downloadGif()
    {
     ImageIcon newIcon = new ImageIcon("./src/accountmanager/img/download_gif.gif");
     GifPicture.setIcon(newIcon);
     SwingUtilities.invokeLater(new Runnable()
     {
        @Override
        public void run()
        {
          // Here, we can safely update the GUI
          // because we'll be called from the
          // event dispatch thread
          GifPicture.setVisible(true);
        }
     });
    }

    public void searchGif() throws InterruptedException, InvocationTargetException
    {
     ImageIcon newIcon = new ImageIcon("./src/accountmanager/img/mondo.gif");
     GifPicture.setIcon(newIcon);
     if(GifPicture.isVisible()==false)
     {
         SwingUtilities.invokeLater(new Runnable()
         {
            @Override
            public void run()
            {
              // Here, we can safely update the GUI
              // because we'll be called from the
              // event dispatch thread
              GifPicture.setVisible(true);
            }
         });
     }
    }

    public void downloadGif2() throws InterruptedException, InvocationTargetException
    {
     ImageIcon newIcon = new ImageIcon("./src/accountmanager/img/download_gif.gif");
     GifPicture.setIcon(newIcon);
     SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>()
     {
        @Override
            protected Boolean doInBackground() throws Exception
            {
                return true;
            }
        @Override
            protected void done()
            {
                try
                {
                    GifPicture.setVisible(true);
                }
                catch (Exception e)
                {
                    // This is thrown if we throw an exception
                    // from doInBackground.
                }
            }
       };
       worker.execute();
    }

    public void searchGif2() throws InterruptedException, InvocationTargetException
    {
     ImageIcon newIcon = new ImageIcon("./src/accountmanager/img/mondo.gif");
     GifPicture.setIcon(newIcon);
     SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
        @Override
            protected Boolean doInBackground() throws Exception
            {
                return true;
            }
        @Override
            protected void done()
            {
                try
                {
                    GifPicture.setVisible(true);
                }
                catch (Exception e)
                {
                    // This is thrown if we throw an exception
                    // from doInBackground.
                }
            }
       };
       worker.execute();
    }

}