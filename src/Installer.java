package accountmanager;

import static accountmanager.DirectoryManager.findPathDesktopLinux;
import static accountmanager.JForm.deleteDirectory;
import java.awt.Color;
import java.io.File;
import java.io.UnsupportedEncodingException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

/**
 *
 * @author Samuele
 */
public class Installer implements Runnable
{
    
    private JButton ButtonInstalla=null;
    private JProgressBar BarDownload=null;
    private JTextField TextDownload=null;
    private JFrame FrameAggiornamento=null;
    private String versioneFileZip=null;
    private String oldVersion="";
    private String currentVersion="";
    private String root="";
    
    public Installer(JButton button,JProgressBar bar,JTextField text,JFrame frame,String VersionSoftware,String OldVersionSoftware)
    {
     ButtonInstalla=button;
     BarDownload=bar;
     TextDownload=text;
     FrameAggiornamento=frame;
     oldVersion=OldVersionSoftware;
     currentVersion=VersionSoftware;
    }


    @Override
    public void run()
    {
        try
        {
         root = SearchPath();
         root = root.replaceAll("/AccountManager_Version_"+currentVersion,"").replaceAll("/AccountManagerSoftware_"+currentVersion,""); 
            
          ButtonInstalla.setText("Installa");
          ButtonInstalla.setEnabled(false);
          BarDownload.setValue(0);
          BarDownload.setForeground(Color.green);
          BarDownload.setVisible(true);

          String nomeFileZip=cercaFileZip().getName();
          versioneFileZip=nomeFileZip.replaceAll(".zip","").replaceAll("AccountManager_Version_","");

          /*CREO DIRECTORY IN CUI ANDRANNO I NUOVI FILES SCARICATI*/
          boolean successo1 = (new File(root+"/AccountManagerSoftware_"+versioneFileZip)).mkdir();

          /*ESEGUO SPOSTAMENTO DEI FILES SCARICATI DAL DESKTOP ALLA NUOVA DIRECTORY*/
          BarDownload.setValue(25);
          
          File inputFile = null;
          if(System.getProperty("os.name").contains("Windows"))
           inputFile = new File(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory()+"/"+nomeFileZip.replaceAll(".zip","")+".zip");
          if(System.getProperty("os.name").contains("Linux")||System.getProperty("os.name").contains("Ubuntu"))
           inputFile = new File(findPathDesktopLinux()+"/"+nomeFileZip.replaceAll(".zip","")+".zip");
          
          File outputFile = new File(root+"/AccountManagerSoftware_"+versioneFileZip+"/"+nomeFileZip.replaceAll(".zip","")+".zip");
          if (inputFile.renameTo(outputFile))
           System.out.println ("OK");
          else
          {
            System.out.println ("ERRORE COPIATURA");
            throw new Exception();
          }

          /*CREO DIRECTORY CON IL NOME DEL FILE ZIP PER CORREGGERE UN BUG DI PERCORSO PATH*/
          BarDownload.setValue(50);
          boolean successo2 = (new File(root+"/AccountManagerSoftware_"+versioneFileZip+"/"+nomeFileZip.replaceAll(".zip",""))).mkdir();
          BarDownload.setValue(75);

          /*ESEGUO UNZIP FILE*/
          try
          {
           System.out.println(root);
           //UnzipFile fileZip = new UnzipFile(root+"/AccountManagerSoftware_"+versioneFileZip+"/"+nomeFileZip.replaceAll(".zip","")+".zip",root+"/AccountManagerSoftware_"+versioneFileZip+"");
           UnzipFile fileZip = new UnzipFile(root+"/"+nomeFileZip.replaceAll(".zip","")+".zip",root+"/AccountManagerSoftware_"+versioneFileZip+"");
           System.out.println("UNZIP: "+root);
           fileZip.unzip();
           BarDownload.setValue(85);
          }
          catch (Exception ex)
          {
           System.out.println ("ERRORE ESTRAZIONE");
           throw new Exception();
          }            

          /*CANCELLO IL VECCHIO COLLEGAMENTO*/
          File f = null;
          if(System.getProperty("os.name").contains("Windows"))
           f = new File(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory()+"/accountmanager.lnk");
          if(System.getProperty("os.name").contains("Linux")||System.getProperty("os.name").contains("Ubuntu"))
           f = new File(findPathDesktopLinux()+"/accountmanager.lnk");
          
          if(f.delete())
          {
           System.out.println("OK");
          }
          else
          {
           System.out.println("Collegamento non trovato");
          } 

          /*CREO UN NUOVO COLLEGAMENTO*/
          BarDownload.setValue(100);

          /*inputFile = new File("C:/accountmanagerSoftware_"+versioneFileZip+"/"+nomeFileZip.replaceAll(".zip","")+"/Setup_collegamento.exe");
          outputFile = new File(System.getProperty("user.home")+"/Desktop/Setup_collegamento.exe");
          if (inputFile.renameTo(outputFile))
           System.out.println ("OK");
          else
           System.out.println ("Collegamento al desktop fallito");
          */
        try 
        {
        Process p = Runtime.getRuntime().exec("cmd /c "+root+"/AccountManagerSoftware_"+versioneFileZip+"/"+nomeFileZip.replaceAll(".zip","")+"/Setup_collegamento.exe");
        }
        catch (Exception e)
        {
            System.out.println("Collegamento al software fallito");
        }
        
          FrameAggiornamento.setVisible(false);
          System.exit(0);
        }
        catch (Exception ex)
        {
          deleteDirectory(new File(root+"/AccountManagerSoftware_"+versioneFileZip+""));
          BarDownload.setForeground(Color.red);
          ButtonInstalla.setEnabled(true);
          ButtonInstalla.setText("Riprova");
          TextDownload.setText("ERRORE");
        } 
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
     for (int i=0;i<array.length;i++)
     {
      System.out.println(i+1+"."+array[i]);
      if(array[i].contains("AccountManager_Version_"))
      {
       verioneFileZip=Integer.parseInt(array[i].replaceAll("AccountManager_Version_","").replaceAll(".zip","").replaceAll("\\.",""));
       versioneSoftwareCorrente=Integer.parseInt(currentVersion.replaceAll("\\.",""));
       if(verioneFileZip>versioneSoftwareCorrente)
       {
        f = new File(array[i]);
        break;
       }
      }
     }
     System.out.println(f.getName());
     return f;
    }
    
    public String getNewVersion()
    {
        return versioneFileZip;
    }
    
    public String SearchPath() throws UnsupportedEncodingException
    {
      String absolutePath = null;
      if(System.getProperty("os.name").contains("Windows"))
      {
       absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
       absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf("/"));
       absolutePath = absolutePath.replaceAll("%20"," ");
       
       System.out.println("Windows");
       System.out.println(absolutePath);
      }
      if(System.getProperty("os.name").contains("Linux")||System.getProperty("os.name").contains("Ubuntu"))
      {  
       absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
       absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf("/"));
       absolutePath = absolutePath.replaceAll("%20"," "); // Surely need to do this here
       System.out.println("UBUNTU");
       System.out.println(absolutePath);
      }
      return absolutePath;
    }
}
