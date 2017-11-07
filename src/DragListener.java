package accountmanager;

import java.awt.Color;
import java.util.List;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Samuele
 */
public class DragListener implements DropTargetListener
{
    JTable TabellaPrincipale=new JTable();
    JTextField pathTable=new JTextField();
    JTextField PathFileDragDrop=null;
    JDialog FinestraDialogo=null;
    JScrollPane jScrollPane11=null;
    JTextArea AnteprimaText=null;
    JTextField ErrorMessage=null;
    JLabel GifPicture=null;
    JButton ReadButton=null;
    JButton ImportButton=null;
    JTextField TextPath=null;
    
    public DragListener(JTable file,JTextField path,JTextField pathField,JDialog Dialogo,JScrollPane ScrollPane11,JTextArea Anteprima,JTextField MessageText,JLabel gif,JButton leggiButton,JButton importaButton,JTextField PathDragDrop)
    {
     TabellaPrincipale=file;
     pathTable=path;
     PathFileDragDrop=PathDragDrop;
     FinestraDialogo=Dialogo;
     jScrollPane11=ScrollPane11;
     AnteprimaText=Anteprima;
     ErrorMessage=MessageText;
     GifPicture=gif;
     ReadButton=leggiButton;
     ImportButton=importaButton;
     TextPath=pathField;
    }
    
    @Override
    public void dragEnter(DropTargetDragEvent dtde) 
    {}
    @Override
    public void dragOver(DropTargetDragEvent dtde) 
    {}
    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) 
    {}
    @Override
    public void dragExit(DropTargetEvent dte)
    {}
    
    @Override
    public void drop(DropTargetDropEvent ev)
    {
     ev.acceptDrop(DnDConstants.ACTION_COPY);
     Transferable t=ev.getTransferable();
     
     DataFlavor[] df=t.getTransferDataFlavors();
     
     for(DataFlavor f:df)
     {
       try
       {
        if(f.isFlavorJavaFileListType())
        {
         List<File> files=(List<File>) t.getTransferData(f);
         
         for(File file : files)
         {
          displayFile(file.getPath());
         }
        }
       }
       catch(Exception ex)
       {
        JOptionPane.showMessageDialog(null,ex);
       }
     }
    }
    
    private void displayFile(String path)
    {
        ReadButton.setEnabled(true);
        ImportButton.setEnabled(true);
        FinestraDialogo.setLocationRelativeTo(null);
        if(path.contains(".csv")||path.contains(".xls")||path.contains(".txt"))
        {
            if(path!=null)
            { 
             PathFileDragDrop.setText(path);
             FinestraDialogo.setVisible(true);
             jScrollPane11.setVisible(false);
             FinestraDialogo.setSize(502,294);
             creaAnteprima(path);
            }
        }
        else
        {
         ErrorMessage.setText("Inserire un file .csv .xls .txt");
         allarmGif();
        }
    }
    
    public void creaAnteprima(String path)
    {
        BufferedReader br = null;
        String pathFile = path; //prendo il path del file scelto
        String line = null;

        AnteprimaText.setText("");
        AnteprimaText.setForeground(Color.BLACK);
        try
        {
         int controlloCicli=0;
         FileReader file = new FileReader(pathFile);
         br = new BufferedReader(file);                 
        
            /*NEL TRY E' ESEGUITA LA LETTURA PER I FILE STANDARDIZZATI*/
            try 
            {
                while ((line=br.readLine())!=null) 
                {
                    
                    AnteprimaText.append(line+"\n");
                    controlloCicli++;
                }
                /*Se il contatore di righe lette rimane a zero significa che il file è vuoto*/
                if(controlloCicli==0)
                {
                    AnteprimaText.setText("Il File è vuoto");
                    AnteprimaText.setForeground(Color.RED);
                }
            }
            catch(ArrayIndexOutOfBoundsException ex)
            {}
            AnteprimaText.setCaretPosition(0);
        }
        catch (FileNotFoundException e) 
        {
            AnteprimaText.setText("FILE NON TROVATO");
            AnteprimaText.setForeground(Color.RED);
        } 
        catch (IOException e) 
        {
            AnteprimaText.setText("Errore I/O");
            AnteprimaText.setForeground(Color.RED);
        }
        finally 
        {
            if (br != null) 
            {
                try 
                {
                    br.close();
                } 
                catch (IOException e) 
                {
                    AnteprimaText.setText("Errore I/O");
                    AnteprimaText.setForeground(Color.RED);
                }
            }
        }
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
}