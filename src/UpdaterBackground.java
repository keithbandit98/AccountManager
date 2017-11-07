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
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
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

/**
 *
 * @author Samuele
 */
public class UpdaterBackground implements Runnable
{

    private String SAVE_DIR = ""+javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory()+"/";
    private final Socket socketClient = null;
    private String Version = null;
    private File inFile = null;
    private JFrame frame = null;
    private JLabel Picture = null;
    private JButton buttonInstallazione = null;
    private JProgressBar BarDownload = null;
    private JTextField TextDownload = null;
    private JMenuItem Update = null;
    private boolean statoThread = true;

    public UpdaterBackground(JFrame FrameAggiornamento,String VersionSoftware,JLabel PictureDownload,JButton buttonInstall,JProgressBar bar,JTextField text,JMenuItem updateCheck)
    {
     Version=VersionSoftware;
     frame=FrameAggiornamento;
     Picture=PictureDownload;
     buttonInstallazione=buttonInstall;
     BarDownload=bar;
     TextDownload=text;
     Update=updateCheck;
    }

    @Override
    public void run()
    {
     FileOutputStream fos=null;
     Boolean controllo=false;
     ImageIcon icona = null;
     Image immagine = null;
     Image newimg = null;
     double progress=0;
     int bar=0;
     
     //in base al sistema operativo salvo il file scaricato dal server sul desktop relativo
     if(System.getProperty("os.name").contains("Windows"))
      SAVE_DIR = javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory()+"/";
     if(System.getProperty("os.name").contains("Linux")||System.getProperty("os.name").contains("Ubuntu"))
      SAVE_DIR = findPathDesktopLinux()+"/";
     
     /*
        System.setProperty("javax.net.ssl.trustStore", "myTrustStore");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
        System.setProperty("javax.net.ssl.keyStoreType", "pkcs12");
        System.setProperty("javax.net.ssl.keyStore", "new.p12");
        System.setProperty("javax.net.ssl.keyStorePassword", "newpasswd");*/
        // Fornisco le chiavi ed il certificato con le relative password
        /*System.setProperty("javax.net.ssl.keyStore", "C:\\_Certificati\\keystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "password");
        System.setProperty("javax.net.ssl.trustStore", "C:\\_Certificati\\truststore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "password");*/

     if(cercaFileZip()!=null)
     {
        System.out.println("TROVATO");
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
        TextDownload.setText("Installare aggiornamento?");
        frame.setVisible(true);
        controllo=true; // se trovo zip non eseguo collegamento al server
     }

     while(controllo==false)
     {
        try
        {
         InetAddress address = InetAddress.getByName("kronoserver.ddns.net");
         Socket socketClient = new Socket(address.getHostAddress(),1337);
         
         //SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
         //SSLSocket socketClient = (SSLSocket) factory.createSocket(address.getHostAddress(),1337);
         
         PrintWriter socketOut =new PrintWriter(socketClient.getOutputStream()); //invio stringa al server
         ObjectInputStream oin = new ObjectInputStream(socketClient.getInputStream()); //intercetto il file in arrivo

         while(true)
         {
          String inputLine = Version; //scrivo stringa
          socketOut.println(inputLine); //invio la stringa
          socketOut.flush();

          try
          {
           File nameNewFile= (File) oin.readObject(); //ricevo il path del file spedito dal server
           Update.setEnabled(false);
           //frame.setUndecorated(true);
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

           inFile = new File(SAVE_DIR+nameNewFile.getName());

           bar=oin.readInt(); //ricevo la dimensione del file

           //FileInputStream fis=new FileInputStream(nameNewFile);
           fos=new FileOutputStream(inFile);
           int bufferMAX=8196;
           byte[] buf = new byte[bufferMAX];
           int i=0,c=0,x=0;
           double datiBuffer=0;
           //riga per riga leggo il file originale per scriverlo nello stream del file destinazione
           while((x=x+i)<bar)
           {
            i=oin.read(buf);
            datiBuffer=i;
            progress=progress+(100*(datiBuffer/bar));
            BarDownload.setValue((int) progress);
            fos.write(buf,0,i);
            c++;
            System.out.println("ciclo: "+c+" i="+i);
            if(statoThread==false)
            {
              fos.close();
              socketClient.close();
              throw new InterruptedException();
            }
           }
           try
           {
             //chiudo gli streams
            fos.close();
            socketClient.close();
            BarDownload.setValue(100);
            System.out.println(" --ricezione completata");

            controllo=true;
            buttonInstallazione.setEnabled(true);
            TextDownload.setText("Installare aggiornamento?");
            frame.setVisible(true);
           }
           catch(NullPointerException ex)
           {System.out.println("Nessun aggiornamento disponibile");}
          }
          catch (InterruptedException ex)
          {
           System.out.println("STOP");
           String pathzip=cercaFileZip().getName();
           /*try
           {
            Process p = Runtime.getRuntime().exec(System.getProperty("user.home")+"/Desktop/"+pathzip);
            p.destroy();
           }
           catch (IOException e)
           {System.out.println("NON riesco a fermare il processo");}*/
           
          if(System.getProperty("os.name").contains("Windows"))
            new File(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory()+"/"+pathzip).delete();
          if(System.getProperty("os.name").contains("Linux")||System.getProperty("os.name").contains("Ubuntu"))
            new File(findPathDesktopLinux()+"/"+pathzip).delete();
          
          }
          break;
         }
         Update.setEnabled(true);
        }
        catch(ConnectException e)
        {
         TextDownload.setText("ERRORE: Il server ha smesso di funzionare");
         BarDownload.setForeground(Color.red);
        }
        catch(NoSuchElementException e)
        {
         TextDownload.setText("ERRORE: NoSuchElement");
         BarDownload.setForeground(Color.red);
        }
        catch(UnknownHostException e)
        {
         TextDownload.setText("ERRORE: Server contattato sconosciuto");
         BarDownload.setForeground(Color.red);
        }
        catch (IOException ex)
        {
         TextDownload.setText("ERRORE: I/O sul file");
         BarDownload.setForeground(Color.red);
        }
        catch (ClassNotFoundException ex)
        {
         TextDownload.setText("ERRORE: Oggetto sconosciuto");
         BarDownload.setForeground(Color.red);
        }
        finally
        {
         try
         {
          socketClient.close();
          Thread.sleep(4000);
          frame.dispose();
         }
         catch(NullPointerException e)
         {}
         catch (IOException ex)
         {

         }
         catch (InterruptedException ex)
         {
          System.out.println("STOP");
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
        }
         try
         {
            Thread.sleep(100000);
            System.out.println("OK");
         }
         catch (InterruptedException ex)
         {}
     }
    }


    public void setStatoThread(Boolean stato)
    {
     statoThread=stato;
    }
    
    public boolean getStatoThread()
    {
     return statoThread;
    }

    private void save(File inFile, File saveFile)throws IOException
    {
      FileInputStream fis=new FileInputStream(inFile); //apro uno stream sul file che e' stato inviato
      FileOutputStream fos=new FileOutputStream(saveFile); //scrivo uno stream per il salvataggio del nuovo file

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
    }

    public File cercaFileZip()
    {
     File f = null;
     File d = null;
     int verioneFileZip=0,versioneSoftwareCorrente=0;
     if(System.getProperty("os.name").contains("Windows"))
      d = new File(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath());
     if(System.getProperty("os.name").contains("Linux")||System.getProperty("os.name").contains("Ubuntu"))
      d = new File(findPathDesktopLinux());
     
     String array[] = d.list(); //creo un array di stringhe e lo riempio con la lista dei files presenti nella directory
     System.out.println("stampo la lista dei files contenuti nella directory:");
     if(d.exists()==true)
     {
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
     }
     return f;
    }
}