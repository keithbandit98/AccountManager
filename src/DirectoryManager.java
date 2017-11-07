package accountmanager;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Samuele
 */
public class DirectoryManager implements Runnable
{

    private String oldVersion="";
    private String currentVersion="";
    
    public DirectoryManager(String VersionSoftware,String OldVersionSoftware)
    {
     oldVersion=OldVersionSoftware;
     currentVersion=VersionSoftware;
    }
    
    @Override
    public void run()
    {
        String root = null;
        try
        {
         root = SearchPath();
         root = root.replaceAll("/AccountManager_Version_"+currentVersion,"").replaceAll("/AccountManagerSoftware_"+currentVersion,"");
         System.out.println(root);
        }
        catch (Exception ex)
        {
         System.out.println(root);
        }
       
       
       /*CANCELLO LA VECCHIA CARTELLA*/
       //File vecchiaCartella = new File("C:/AccountManagerSoftware__"+oldVersion); //individuo la vecchia versione sul pc client
       //File nuovaCartella = new File("C:/AccountManagerSoftware__"+new Installer().getNewVersion());
       //int a,b;
       //a=Integer.parseInt(currentVersion.replaceAll("\\.",""));
       //b=Integer.parseInt(oldVersion.replaceAll("\\.",""));
       
       /*if(a>b)
       {
        if(deleteDirectory(vecchiaCartella))
        {
         System.out.println("OK");
        }
        else
        {
         System.out.println("Cartella vecchia non trovata");
        } 
       }*/
        File vecchiaCartella=cercaFile(root+"/");
        System.out.println(vecchiaCartella);
        if(vecchiaCartella!=null)
        {
            if(deleteDirectory(vecchiaCartella))
            {
             System.out.println("OK");
            }
            else
            {
             System.out.println("Cartella vecchia non trovata");
            }
        }
        else
        {
         System.out.println("Nessun vecchio programma trovato");
        }
        
        File setup = null;
         if(System.getProperty("os.name").contains("Windows"))
          setup = new File(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory()+"/Setup_collegamento.exe");
         if(System.getProperty("os.name").contains("Linux")||System.getProperty("os.name").contains("Ubuntu"))
          setup = new File(findPathDesktopLinux()+"/Setup_collegamento.exe");
        
        if(setup.delete())
        {
         System.out.println("OK");
        }
        else
        {
         System.out.println("Setup non trovato");
        }
       

        /*RINOMINO LA NUOVA CARTELLA*/
        /*File inputFile = new File("C:/AccountManagerSoftware_aggiornamento");
        File outputFile = new File("C:/AccountManagerSoftware_");
        if (inputFile.renameTo(outputFile))
         System.out.println ("OK");
        else
        {
          System.out.println ("Rinominazione directory fallita");
        }*/    
    } 
    
    public File cercaFile(String root)
    {
     File f =null;
     int versione=0,versioneCorrente=Integer.parseInt(currentVersion.replaceAll("\\.",""));
     File d = new File(root);
     System.out.println("Posizione in cui cerco la vecchia cartella: "+root);
     String array[] = d.list(); //creo un array di stringhe e lo riempio con la lista dei files presenti nella directory
     System.out.println("stampo la lista dei files contenuti nella directory C:");
     for (int i=0;i<array.length;i++)
     {
      System.out.println(i+1+"."+array[i]);
      if(array[i].contains("AccountManagerSoftware_"))
      {
       versione = Integer.parseInt(array[i].replaceAll("AccountManagerSoftware_","").replaceAll("\\.","")); //verifico la versione della cartella AccounManagerSoftware trovata casualmente
       if(versione<versioneCorrente)
       {
        f = new File(root+array[i]);
        break;
       }
      }
     }
     return f;
    }
    
    public static boolean deleteDirectory(File path) 
    {
     int i;
     if(path.exists())
     {
      File[] files = path.listFiles();
        for(i=0;i<files.length;i++)
        {
          if(files[i].isDirectory())
            deleteDirectory(files[i]);
          else            
            files[i].delete();            
        }
      }
      return path.delete();
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
    
    public static String findPathDesktopLinux()
    {
     File path = null;
     
     path = new File(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory()+"/Scrivania"); 
     if(path.exists())
      return path.getAbsolutePath();
     else
     {
      path = new File(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory()+"/Desktop");
      return path.getAbsolutePath();
     }
   
     
    }
}
