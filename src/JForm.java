package accountmanager;


import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterAbortException;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import static java.lang.System.exit;
import javax.swing.JFileChooser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoundedRangeModel;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/*  DOCSTRING:
   -autore: Samuele Todesca 5IC
   -titolo: AccountManager
   -nome file: Samuele_Todesca_AccountManager_5IC_versione 3.78.60
   -scopo del programma: Gestire un file.csv visualizzandolo,modificandolo e salvando le modifiche
   -istituto: i.t.t.s. Fedi Fermi
   -dati input: da tastiera,da mouse, scelta tra inserimento manuale/automatico
   -dati output: tabelle dati file.csv
   -tipo dato input: stringhe, eventi
   -tipo dato output: stringhe, file.csv
   -metodo d'inserimento dati: da utente, automatico (random)
   -data inizio: 24/09/2016
   -data ultima modifica: 14/09/2017
   */


 /**
 * @author Samuele Todesca
 * @version 3.78.60
 */
public class JForm extends javax.swing.JFrame implements Printable
{
    //RICORDA: quando sposti il file zip su htdocs per rendere pubblico l'aggiornamento, nominare il file zip AccountManager_Version_x.x.x.zip il file zip creerà automaticamente una sottocartella con il medesimo nome
    //NON PIU NECESSARIO(farlo comunque):questi attributi devono essere modificati manualmente dal programmatore quando decide di rilasciare un aggiornamento (cambiare valori anche nel setup.exe e setup_collegamento.exe e guida(dettagli_versione))
    private final String VersionSoftware="3.78.60"; //versione corrente localizzata sul server (indico la versione che voglio rilasciare e che sostituirà la vecchia)
    private final String OldVersionSoftware="3.77.60"; //versione vecchia localizzata sul client (localizzo la versione vecchia e la elimino)

    private Thread IDThreadUpdaterBackGround=null;
    private Thread IDThreadUpdaterManuale=null;
    private Thread IDThreadReader=null;
    private Thread IDThreadDirectoryManager=null;
    private Thread IDThreadAnteprima=null;
    private Thread IDThreadSalvaNome=null;
    private Thread IDThreadCancellaRighe=null;
    private Thread IDThreadCancellaTabella=null;
    private Thread IDThreadGeneraPassword=null;
    private Thread IDThreadImportaFile=null;
    private Thread IDThreadComparaFile=null;
    private Thread IDThreadInstallazione=null;
    private Thread IDThreadCorrettore=null;

    private Updater GlobalClassUpdater=null;
    private UpdaterBackground GlobalClassUpdaterBackGround=null;

    private int posX=0,posY=0; //frame information location for drag and drop
    private String PathFileImport=null;
    private int PosizioneUltimaRiga=-1;
    private JLabel SfondoGuida=null;
    private boolean eventoChangeItem = false;
    private boolean CTRL = false;
    private int NumeroSlide=0;
    private int Sezione=0;
    private int contaImportatiOmonimi=0;
    private int contaFileImportati=0;
    private int contaSenzaDipartimento=0;
    private int contaMembriImportati=0;
    private int Design=0;
    private boolean StatoThreadReader=true;
    private int rigaStampa=0;

    public JForm()
    {
        initComponents();
        /*try{list = COMRuntime.newInstance(ITaskbarList3.class);}catch (ClassNotFoundException ex){}
        list.Release();
        long hwndVal = JAWTUtils.getNativePeerHandle(this);
        hwnd = Pointer.pointerToAddress(hwndVal);
        list.SetProgressValue((Pointer)hwnd,150,300);*/

        IDThreadDirectoryManager = new Thread(new DirectoryManager(VersionSoftware,OldVersionSoftware));
        IDThreadDirectoryManager.start();
        connectToDragDrop();
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.setExtendedState(JForm.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);
        TabellaAggiungi.setVisible(false);
        FinestraDialogo.setAlwaysOnTop(true);
        jScrollPane11.setVisible(false);
        TextFieldAltro.setEnabled(false);
        EliminaModifiche.setEnabled(false);
        SalvaButton.setEnabled(false);
        CheckRadioEmail.setEnabled(false);
        CheckRadioPasswordRandom.setEnabled(false);
        TextFieldAltro.requestFocus(false);
        GifPicture.setVisible(false);
        BarUpdater.setVisible(false);
        MessageText.setText("");
        contaAggiuntiManuale.setText("0");
        contaAggiunti.setText("0");
        contaEsistenti.setText("0");
        contaMembri.setText("0");
        AboutText.setText(" \n"+"                                    DETTAGLI\n"+"\n"+" Produzione: Samuele_Todesca_5IC\n"+" Product Version: "+VersionSoftware+"\n"+" Aggiornamenti: 6° aggiornamento\n"+" Java: "+System.getProperty("java.version")+"\n"+" Runtime: Java(TM) SE Runtime Environment\n"+" System: "+System.getProperty("os.name")+"\n"+"");
        Image icon = Toolkit.getDefaultToolkit().getImage("./src/accountmanager/img/CSV.png");
        this.setIconImage(icon);
        ContattaFrame.setIconImage(icon);
        FinestraDialogo.setIconImage(icon);
        FrameLoad.setIconImage(icon);
        AboutFrame.setIconImage(icon);
        FrameAggiornamento.setIconImage(icon);
        /*Sincronizzo le due scroll di entrambe le tabelle*/
        BoundedRangeModel modelScrollBar = ScrollPane_TabellaFlags.getVerticalScrollBar().getModel();
        ScrollPane_TabellaPrincipale.getVerticalScrollBar().setModel(modelScrollBar);
        ScrollPane_TabellaNumerazione.getVerticalScrollBar().setModel(modelScrollBar);
        /*Disattivo le seguenti scrollbar*/
        ScrollPane_TabellaNumerazione.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        ScrollPane_TabellaRicerca.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        ScrollPane_TabellaAggiungi.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        TabellaNumerazione.setRowSelectionAllowed(false);
        TabellaFlags.setRowSelectionAllowed(false);
        GlobalClassUpdaterBackGround = new UpdaterBackground(FrameAggiornamento,VersionSoftware,PictureDownload,ButtonInstalla,BarDownload,TextDownload,UpdateSoftware);
        IDThreadUpdaterBackGround = new Thread(GlobalClassUpdaterBackGround);
        IDThreadUpdaterBackGround.start();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        FileChooserGenerale = new javax.swing.JFileChooser();
        ScrollPane_TabellaEsterna = new javax.swing.JScrollPane();
        TabellaEsterna = new javax.swing.JTable();
        AboutFrame = new javax.swing.JFrame();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        AboutText = new javax.swing.JTextArea();
        logoGuidaPicture2 = new javax.swing.JLabel();
        FinestraDialogo = new javax.swing.JDialog();
        Panel_SceltaSplit = new javax.swing.JPanel();
        SceltaSplit = new javax.swing.JTextField();
        Panel_Split = new javax.swing.JPanel();
        CheckTabulazione = new javax.swing.JCheckBox();
        CheckVirgola = new javax.swing.JCheckBox();
        CheckPuntoVirgola = new javax.swing.JCheckBox();
        CheckHash = new javax.swing.JCheckBox();
        CheckSlash = new javax.swing.JCheckBox();
        CheckUnderscore = new javax.swing.JCheckBox();
        CheckSpazio = new javax.swing.JCheckBox();
        CheckAltro = new javax.swing.JCheckBox();
        TextFieldAltro = new javax.swing.JTextField();
        LeggiFileButton = new javax.swing.JButton();
        jScrollPane11 = new javax.swing.JScrollPane();
        AnteprimaText = new javax.swing.JTextArea();
        visualizzaAnteprima = new javax.swing.JTextField();
        ButtonImporta = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        FrameFileExist = new javax.swing.JDialog();
        jPanel9 = new javax.swing.JPanel();
        AnnullaButtonFrameFileExist = new javax.swing.JButton();
        SostiuisciButtonFrameFileExist = new javax.swing.JButton();
        TextFieldFrameFileExist = new javax.swing.JTextField();
        FrameLoad = new javax.swing.JFrame();
        jPanel10 = new javax.swing.JPanel();
        ProgressBar = new javax.swing.JProgressBar();
        TextLoad = new javax.swing.JTextField();
        TextInfo = new javax.swing.JTextField();
        ScrollPane_TabellaFlagsSupport = new javax.swing.JScrollPane();
        TabellaFlagsSupport = new javax.swing.JTable();
        ContattaFrame = new javax.swing.JFrame();
        PannelloContatta = new javax.swing.JPanel();
        ScrollPaneContatta = new javax.swing.JScrollPane();
        TextAreaContatta = new javax.swing.JTextArea();
        logoGuidaPicture3 = new javax.swing.JLabel();
        PopupMenu = new javax.swing.JPopupMenu();
        eliminaPassword = new javax.swing.JMenuItem();
        FrameAggiornamento = new javax.swing.JFrame();
        Pannelllo_FrameAggiornamento = new javax.swing.JPanel();
        TextDownload = new javax.swing.JTextField();
        PictureDownload = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        ButtonInstalla = new javax.swing.JButton();
        BarDownload = new javax.swing.JProgressBar();
        PannelloInformazioni = new javax.swing.JFrame();
        Pannello_PannelloInformazioni = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        ClosePanelloInformazioni = new javax.swing.JButton();
        Pannello_InformazioniGenerali = new javax.swing.JPanel();
        CampoTotaleOmonimi = new javax.swing.JTextField();
        CampoBaseOmonimi = new javax.swing.JTextField();
        CampoImportatiOmonimi = new javax.swing.JTextField();
        CampoNumeroDipartimenti = new javax.swing.JTextField();
        CampoNoDipartimento = new javax.swing.JTextField();
        CampoMembriImportati = new javax.swing.JTextField();
        CampoNumeroFiles = new javax.swing.JTextField();
        TextFileBase = new javax.swing.JTextField();
        ScrollPane_TabellaFiles = new javax.swing.JScrollPane();
        TabellaFile = new javax.swing.JTable();
        Pannello_InformazioniFileImportato = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        AnteprimaFileImportato = new javax.swing.JTextArea();
        SceltaSalvataggio = new javax.swing.JFrame();
        Pannello_SceltaSalvataggio = new javax.swing.JPanel();
        JPane123 = new javax.swing.JPanel();
        Pannello_EstrazioneDati = new javax.swing.JPanel();
        CheckBoxSalvaTutto = new javax.swing.JCheckBox();
        CheckBoxSalvaNuovi = new javax.swing.JCheckBox();
        CheckBoxSalvaVecchi = new javax.swing.JCheckBox();
        CloseSceltaSalvataggio = new javax.swing.JButton();
        Help_Salvataggio = new javax.swing.JLabel();
        FileChooserSalvaNome = new javax.swing.JFileChooser();
        CorrettoreOmonimi = new javax.swing.JFrame();
        Pannello_CorrettoreOminimi = new javax.swing.JPanel();
        JPane124 = new javax.swing.JPanel();
        Pannello_EstrazioneDati1 = new javax.swing.JPanel();
        ButtonCancellaRiga = new javax.swing.JButton();
        ButtonRinominaTutto = new javax.swing.JButton();
        ButtonCorreggi = new javax.swing.JButton();
        ButtonRinominaSelezionati = new javax.swing.JButton();
        ButtonRipristina = new javax.swing.JButton();
        Icon_CorrettoreOmonimi = new javax.swing.JLabel();
        CloseCorrettoreOmonimi = new javax.swing.JButton();
        Help_CorrettoreOmonimi = new javax.swing.JLabel();
        Panel_CorrettoreOminimi = new javax.swing.JPanel();
        ScrollPane_TabellaOmonimi = new javax.swing.JScrollPane();
        TabellaOmonimi = new javax.swing.JTable();
        PathFileDragDrop = new javax.swing.JTextField();
        Dialog_Chiusura = new javax.swing.JDialog();
        Panel_Chiusura = new javax.swing.JPanel();
        Panel2_Chiusura = new javax.swing.JPanel();
        TextField_Dialog = new javax.swing.JTextField();
        Button_Riavvia = new javax.swing.JButton();
        Button_Annulla = new javax.swing.JButton();
        Guida = new javax.swing.JFrame();
        Panel_Guida = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        Indice = new javax.swing.JTree();
        jScrollPane5 = new javax.swing.JScrollPane();
        TextGuida = new javax.swing.JTextArea();
        OptionSlide = new javax.swing.JTextField();
        SlidePanel = new javax.swing.JLabel();
        ButtonSlideLeft = new javax.swing.JButton();
        ButtonSlideRight = new javax.swing.JButton();
        NumberSlide = new javax.swing.JTextField();
        Guida_veloce = new javax.swing.JFrame();
        Guida_veloce_text = new javax.swing.JTextArea();
        Comparatore = new javax.swing.JFrame();
        Pannello_comparatore = new javax.swing.JPanel();
        Pannello_comparatore2 = new javax.swing.JPanel();
        CloseComparatore = new javax.swing.JButton();
        Text_File_Comparato = new javax.swing.JTextField();
        TextFileComparato = new javax.swing.JTextField();
        ScrollaPane_TabellaComparatore = new javax.swing.JScrollPane();
        TabellaComparatore = new javax.swing.JTable();
        Contatore_doppioni = new javax.swing.JTextField();
        Contatore_omonimi = new javax.swing.JTextField();
        TextOmonimi = new javax.swing.JTextField();
        TextDoppioni = new javax.swing.JTextField();
        TabellaFlagsComparatore = new javax.swing.JTable();
        ScrollPane_JForm = new javax.swing.JScrollPane();
        Pannello_JForm = new javax.swing.JPanel();
        Pannello_StrumentiLaterale = new javax.swing.JPanel();
        CancellaRigaButton = new javax.swing.JButton();
        EsportaModificheButton = new javax.swing.JButton();
        GeneraPasswordButton = new javax.swing.JButton();
        PulisciTabellaButton = new javax.swing.JButton();
        LogoCSVPicture = new javax.swing.JLabel();
        CheckRadioPasswordRandom = new javax.swing.JRadioButton();
        AggiungiMembroButton = new javax.swing.JButton();
        contaEsistenti = new javax.swing.JTextField();
        contaAggiunti = new javax.swing.JTextField();
        contaMembri = new javax.swing.JTextField();
        EliminaModifiche = new javax.swing.JButton();
        CheckRadioEmail = new javax.swing.JRadioButton();
        contaAggiuntiManuale = new javax.swing.JTextField();
        ScrollPane_TabellaAggiungi = new javax.swing.JScrollPane();
        TabellaAggiungi = new javax.swing.JTable();
        GuidaIcon = new javax.swing.JLabel();
        SfogliaButton = new javax.swing.JButton();
        PathField = new javax.swing.JTextField();
        MessageText = new javax.swing.JTextField();
        Pannello_Tabelle = new javax.swing.JPanel();
        ScrollPane_TabellaPrincipale = new javax.swing.JScrollPane();
        TabellaPrincipale = new javax.swing.JTable();
        ScrollPane_TabellaFlags = new javax.swing.JScrollPane();
        TabellaFlags = new javax.swing.JTable();
        ScrollPane_TabellaNumerazione = new javax.swing.JScrollPane();
        TabellaNumerazione = new javax.swing.JTable();
        Pannello_Gif = new javax.swing.JPanel();
        Pannello_GifApertura = new javax.swing.JPanel();
        GifPicture = new javax.swing.JLabel();
        SceltaDipartimento = new javax.swing.JComboBox<>();
        DipartimentoIcon = new javax.swing.JLabel();
        ScrollPane_TabellaRicerca = new javax.swing.JScrollPane();
        TabellaRicerca = new javax.swing.JTable();
        SearchButton = new javax.swing.JButton();
        BarUpdater = new javax.swing.JProgressBar();
        InformationFileIcon = new javax.swing.JLabel();
        MenuBar = new javax.swing.JMenuBar();
        FileMenu = new javax.swing.JMenu();
        ComparaFileButton = new javax.swing.JMenu();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        CorreggiOmonimi = new javax.swing.JMenu();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        SalvaButton = new javax.swing.JMenu();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        ExportFile = new javax.swing.JMenu();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        ImportFile = new javax.swing.JMenu();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        StampaButton = new javax.swing.JMenu();
        GuidaMenu = new javax.swing.JMenu();
        GuidaShortCut = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        Contattami = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        UpdateSoftware = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JPopupMenu.Separator();
        Menu_Design = new javax.swing.JMenu();
        Design_Metal = new javax.swing.JMenuItem();
        Design_Windows = new javax.swing.JMenuItem();
        jSeparator10 = new javax.swing.JPopupMenu.Separator();
        Altro = new javax.swing.JMenuItem();

        FileChooserGenerale.setAcceptAllFileFilterUsed(false);
        FileChooserGenerale.setDialogTitle("");

        TabellaEsterna.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "null", "null", "null", "null", "null", "null", "null", "null", "Title 13", "Title 14", "Title 15", "null"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, true, true, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ScrollPane_TabellaEsterna.setViewportView(TabellaEsterna);

        AboutFrame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        AboutFrame.setMinimumSize(new java.awt.Dimension(505, 210));
        AboutFrame.setResizable(false);

        jPanel11.setBackground(new java.awt.Color(184, 211, 185));
        jPanel11.setMaximumSize(new java.awt.Dimension(505, 210));
        jPanel11.setMinimumSize(new java.awt.Dimension(505, 210));
        jPanel11.setPreferredSize(new java.awt.Dimension(505, 210));

        AboutText.setBackground(new java.awt.Color(255, 255, 204));
        AboutText.setColumns(20);
        AboutText.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        AboutText.setRows(5);
        AboutText.setText(" \n                                    DETTAGLI\n\n Produzione: Samuele_Todesca_5IC\n Product Version: x.x.x\n Aggiornamenti: 6° aggiornamento\n Java: 1.8.0_101-b13\n Runtime: Java(TM) SE Runtime Environment\n System: Windows 10\n");
        AboutText.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        AboutText.setEnabled(false);
        jScrollPane6.setViewportView(AboutText);

        logoGuidaPicture2.setBackground(new java.awt.Color(0, 0, 0));
        logoGuidaPicture2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logoGuidaPicture2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/accountmanager/img/csv2.png"))); // NOI18N
        logoGuidaPicture2.setToolTipText("");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(logoGuidaPicture2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane6)
                    .addComponent(logoGuidaPicture2, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout AboutFrameLayout = new javax.swing.GroupLayout(AboutFrame.getContentPane());
        AboutFrame.getContentPane().setLayout(AboutFrameLayout);
        AboutFrameLayout.setHorizontalGroup(
            AboutFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        AboutFrameLayout.setVerticalGroup(
            AboutFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        FinestraDialogo.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        FinestraDialogo.setMinimumSize(new java.awt.Dimension(502, 330));
        FinestraDialogo.setResizable(false);
        FinestraDialogo.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                FinestraDialogoWindowClosing(evt);
            }
        });

        Panel_SceltaSplit.setBackground(new java.awt.Color(102, 102, 102));
        Panel_SceltaSplit.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        SceltaSplit.setEditable(false);
        SceltaSplit.setBackground(new java.awt.Color(255, 255, 204));
        SceltaSplit.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        SceltaSplit.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        SceltaSplit.setText("Scegliere il carattere separatore");
        SceltaSplit.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout Panel_SceltaSplitLayout = new javax.swing.GroupLayout(Panel_SceltaSplit);
        Panel_SceltaSplit.setLayout(Panel_SceltaSplitLayout);
        Panel_SceltaSplitLayout.setHorizontalGroup(
            Panel_SceltaSplitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_SceltaSplitLayout.createSequentialGroup()
                .addGap(83, 83, 83)
                .addComponent(SceltaSplit, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        Panel_SceltaSplitLayout.setVerticalGroup(
            Panel_SceltaSplitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_SceltaSplitLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(SceltaSplit, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        Panel_Split.setBackground(new java.awt.Color(184, 211, 185));

        CheckTabulazione.setBackground(new java.awt.Color(184, 211, 185));
        CheckTabulazione.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        CheckTabulazione.setText("Tabulazione");
        CheckTabulazione.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckTabulazioneActionPerformed(evt);
            }
        });

        CheckVirgola.setBackground(new java.awt.Color(184, 211, 185));
        CheckVirgola.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        CheckVirgola.setText("Virgola");
        CheckVirgola.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckVirgolaActionPerformed(evt);
            }
        });

        CheckPuntoVirgola.setBackground(new java.awt.Color(184, 211, 185));
        CheckPuntoVirgola.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        CheckPuntoVirgola.setText("Punto e virgola");
        CheckPuntoVirgola.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckPuntoVirgolaActionPerformed(evt);
            }
        });

        CheckHash.setBackground(new java.awt.Color(184, 211, 185));
        CheckHash.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        CheckHash.setText("Hash");
        CheckHash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckHashActionPerformed(evt);
            }
        });

        CheckSlash.setBackground(new java.awt.Color(184, 211, 185));
        CheckSlash.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        CheckSlash.setText("Slash");
        CheckSlash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckSlashActionPerformed(evt);
            }
        });

        CheckUnderscore.setBackground(new java.awt.Color(184, 211, 185));
        CheckUnderscore.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        CheckUnderscore.setText("Underscore");
        CheckUnderscore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckUnderscoreActionPerformed(evt);
            }
        });

        CheckSpazio.setBackground(new java.awt.Color(184, 211, 185));
        CheckSpazio.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        CheckSpazio.setText("Spazio");
        CheckSpazio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckSpazioActionPerformed(evt);
            }
        });

        CheckAltro.setBackground(new java.awt.Color(184, 211, 185));
        CheckAltro.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        CheckAltro.setText("Altro");
        CheckAltro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckAltroActionPerformed(evt);
            }
        });

        TextFieldAltro.setBackground(new java.awt.Color(255, 255, 204));
        TextFieldAltro.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        LeggiFileButton.setText("Leggi File");
        LeggiFileButton.setToolTipText("Esegui la lettura di un file .csv");
        LeggiFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LeggiFileButtonActionPerformed(evt);
            }
        });

        AnteprimaText.setEditable(false);
        AnteprimaText.setBackground(new java.awt.Color(255, 255, 204));
        AnteprimaText.setColumns(20);
        AnteprimaText.setRows(5);
        AnteprimaText.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        AnteprimaText.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPane11.setViewportView(AnteprimaText);

        visualizzaAnteprima.setEditable(false);
        visualizzaAnteprima.setBackground(new java.awt.Color(184, 211, 185));
        visualizzaAnteprima.setForeground(new java.awt.Color(0, 102, 255));
        visualizzaAnteprima.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        visualizzaAnteprima.setText("Visualizza anteprima");
        visualizzaAnteprima.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        visualizzaAnteprima.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                visualizzaAnteprimaMouseClicked(evt);
            }
        });

        ButtonImporta.setText("Importa File");
        ButtonImporta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonImportaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Panel_SplitLayout = new javax.swing.GroupLayout(Panel_Split);
        Panel_Split.setLayout(Panel_SplitLayout);
        Panel_SplitLayout.setHorizontalGroup(
            Panel_SplitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_SplitLayout.createSequentialGroup()
                .addGroup(Panel_SplitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel_SplitLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane11))
                    .addGroup(Panel_SplitLayout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addGroup(Panel_SplitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(Panel_SplitLayout.createSequentialGroup()
                                .addComponent(LeggiFileButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(ButtonImporta, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(36, 36, 36))
                            .addGroup(Panel_SplitLayout.createSequentialGroup()
                                .addGroup(Panel_SplitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(CheckTabulazione, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(CheckVirgola, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(CheckPuntoVirgola, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(Panel_SplitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(CheckUnderscore, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                                    .addComponent(CheckHash, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(CheckSlash, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(Panel_SplitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(CheckAltro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(CheckSpazio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(Panel_SplitLayout.createSequentialGroup()
                                        .addComponent(TextFieldAltro, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 36, Short.MAX_VALUE)))))))
                .addContainerGap())
            .addGroup(Panel_SplitLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(visualizzaAnteprima, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        Panel_SplitLayout.setVerticalGroup(
            Panel_SplitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_SplitLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel_SplitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CheckTabulazione)
                    .addComponent(CheckSlash)
                    .addComponent(CheckSpazio))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(Panel_SplitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CheckVirgola)
                    .addComponent(CheckUnderscore)
                    .addComponent(CheckAltro))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(Panel_SplitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CheckPuntoVirgola)
                    .addComponent(CheckHash)
                    .addComponent(TextFieldAltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(Panel_SplitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ButtonImporta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(LeggiFileButton, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(visualizzaAnteprima, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout FinestraDialogoLayout = new javax.swing.GroupLayout(FinestraDialogo.getContentPane());
        FinestraDialogo.getContentPane().setLayout(FinestraDialogoLayout);
        FinestraDialogoLayout.setHorizontalGroup(
            FinestraDialogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Panel_SceltaSplit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Panel_Split, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        FinestraDialogoLayout.setVerticalGroup(
            FinestraDialogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FinestraDialogoLayout.createSequentialGroup()
                .addComponent(Panel_SceltaSplit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Panel_Split, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "null", "null", "null", "null", "null", "null", "null", "null", "Title 13", "Title 14", "Title 15", "null"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, true, true, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane7.setViewportView(jTable4);

        FrameFileExist.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        FrameFileExist.setAlwaysOnTop(true);
        FrameFileExist.setMinimumSize(new java.awt.Dimension(649, 159));
        FrameFileExist.setResizable(false);
        FrameFileExist.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                FrameFileExistWindowClosing(evt);
            }
        });

        jPanel9.setBackground(new java.awt.Color(184, 211, 185));

        AnnullaButtonFrameFileExist.setText("Annulla");
        AnnullaButtonFrameFileExist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnnullaButtonFrameFileExistActionPerformed(evt);
            }
        });

        SostiuisciButtonFrameFileExist.setText("Sostituisci");
        SostiuisciButtonFrameFileExist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SostiuisciButtonFrameFileExistActionPerformed(evt);
            }
        });

        TextFieldFrameFileExist.setEditable(false);
        TextFieldFrameFileExist.setBackground(new java.awt.Color(255, 255, 204));
        TextFieldFrameFileExist.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        TextFieldFrameFileExist.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TextFieldFrameFileExist.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(AnnullaButtonFrameFileExist, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(SostiuisciButtonFrameFileExist, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE))
                    .addComponent(TextFieldFrameFileExist))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TextFieldFrameFileExist, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AnnullaButtonFrameFileExist, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SostiuisciButtonFrameFileExist, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28))
        );

        javax.swing.GroupLayout FrameFileExistLayout = new javax.swing.GroupLayout(FrameFileExist.getContentPane());
        FrameFileExist.getContentPane().setLayout(FrameFileExistLayout);
        FrameFileExistLayout.setHorizontalGroup(
            FrameFileExistLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        FrameFileExistLayout.setVerticalGroup(
            FrameFileExistLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        FrameLoad.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        FrameLoad.setAlwaysOnTop(true);
        FrameLoad.setMinimumSize(new java.awt.Dimension(493, 150));
        FrameLoad.setResizable(false);
        FrameLoad.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                FrameLoadWindowClosed(evt);
            }
        });

        jPanel10.setBackground(new java.awt.Color(184, 211, 185));
        jPanel10.setMaximumSize(new java.awt.Dimension(493, 163));
        jPanel10.setMinimumSize(new java.awt.Dimension(493, 163));
        jPanel10.setPreferredSize(new java.awt.Dimension(493, 110));

        ProgressBar.setStringPainted(true);

        TextLoad.setEditable(false);
        TextLoad.setBackground(new java.awt.Color(184, 211, 185));
        TextLoad.setText("Load:");

        TextInfo.setEditable(false);
        TextInfo.setBackground(new java.awt.Color(184, 211, 185));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TextLoad)
                    .addComponent(ProgressBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
                    .addComponent(TextInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TextInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextLoad, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout FrameLoadLayout = new javax.swing.GroupLayout(FrameLoad.getContentPane());
        FrameLoad.getContentPane().setLayout(FrameLoadLayout);
        FrameLoadLayout.setHorizontalGroup(
            FrameLoadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        FrameLoadLayout.setVerticalGroup(
            FrameLoadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
        );

        TabellaFlagsSupport.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Flags"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TabellaFlagsSupport.setMaximumSize(new java.awt.Dimension(45, 16));
        TabellaFlagsSupport.getTableHeader().setReorderingAllowed(false);
        ScrollPane_TabellaFlagsSupport.setViewportView(TabellaFlagsSupport);
        if (TabellaFlagsSupport.getColumnModel().getColumnCount() > 0) {
            TabellaFlagsSupport.getColumnModel().getColumn(0).setResizable(false);
        }

        ContattaFrame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        ContattaFrame.setMinimumSize(new java.awt.Dimension(670, 238));
        ContattaFrame.setResizable(false);

        PannelloContatta.setBackground(new java.awt.Color(184, 211, 185));
        PannelloContatta.setMaximumSize(new java.awt.Dimension(505, 210));
        PannelloContatta.setMinimumSize(new java.awt.Dimension(505, 210));

        TextAreaContatta.setBackground(new java.awt.Color(255, 255, 204));
        TextAreaContatta.setColumns(20);
        TextAreaContatta.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        TextAreaContatta.setRows(5);
        TextAreaContatta.setText("\t               \n\t              CONTATTA\n\n - Segnala eventuali bug\n - Richiedi migliorie sul programma\n - Proponi nuove idee\n - Contatta in caso di aiuto o chiarimenti\n\n     todesca.samuele@studenti-ittfedifermi.gov.it");
        TextAreaContatta.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        TextAreaContatta.setEnabled(false);
        ScrollPaneContatta.setViewportView(TextAreaContatta);

        logoGuidaPicture3.setBackground(new java.awt.Color(0, 0, 0));
        logoGuidaPicture3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logoGuidaPicture3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/accountmanager/img/new-message-icon.png"))); // NOI18N
        logoGuidaPicture3.setToolTipText("");

        javax.swing.GroupLayout PannelloContattaLayout = new javax.swing.GroupLayout(PannelloContatta);
        PannelloContatta.setLayout(PannelloContattaLayout);
        PannelloContattaLayout.setHorizontalGroup(
            PannelloContattaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PannelloContattaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(logoGuidaPicture3, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ScrollPaneContatta, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
                .addGap(22, 22, 22))
        );
        PannelloContattaLayout.setVerticalGroup(
            PannelloContattaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PannelloContattaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PannelloContattaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ScrollPaneContatta, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                    .addComponent(logoGuidaPicture3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout ContattaFrameLayout = new javax.swing.GroupLayout(ContattaFrame.getContentPane());
        ContattaFrame.getContentPane().setLayout(ContattaFrameLayout);
        ContattaFrameLayout.setHorizontalGroup(
            ContattaFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PannelloContatta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        ContattaFrameLayout.setVerticalGroup(
            ContattaFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PannelloContatta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        eliminaPassword.setText("cancella  password selezionate");
        eliminaPassword.setFocusPainted(true);
        eliminaPassword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                eliminaPasswordMouseClicked(evt);
            }
        });
        PopupMenu.add(eliminaPassword);

        FrameAggiornamento.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        FrameAggiornamento.setAlwaysOnTop(true);
        FrameAggiornamento.setResizable(false);
        FrameAggiornamento.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                FrameAggiornamentoWindowClosed(evt);
            }
        });

        Pannelllo_FrameAggiornamento.setBackground(new java.awt.Color(184, 211, 185));

        TextDownload.setEditable(false);
        TextDownload.setBackground(new java.awt.Color(255, 255, 204));
        TextDownload.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        TextDownload.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TextDownload.setText("NUOVO AGGIORNAMENTO");

        PictureDownload.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        PictureDownload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/accountmanager/img/update.png"))); // NOI18N

        jPanel14.setBackground(new java.awt.Color(102, 102, 102));
        jPanel14.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        ButtonInstalla.setText("Installa");
        ButtonInstalla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonInstallaActionPerformed(evt);
            }
        });

        BarDownload.setForeground(new java.awt.Color(102, 204, 0));
        BarDownload.setStringPainted(true);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(BarDownload, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(127, 127, 127)
                .addComponent(ButtonInstalla, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(BarDownload, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ButtonInstalla, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout Pannelllo_FrameAggiornamentoLayout = new javax.swing.GroupLayout(Pannelllo_FrameAggiornamento);
        Pannelllo_FrameAggiornamento.setLayout(Pannelllo_FrameAggiornamentoLayout);
        Pannelllo_FrameAggiornamentoLayout.setHorizontalGroup(
            Pannelllo_FrameAggiornamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pannelllo_FrameAggiornamentoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TextDownload, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pannelllo_FrameAggiornamentoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(PictureDownload, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(74, 74, 74))
        );
        Pannelllo_FrameAggiornamentoLayout.setVerticalGroup(
            Pannelllo_FrameAggiornamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pannelllo_FrameAggiornamentoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TextDownload, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PictureDownload, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout FrameAggiornamentoLayout = new javax.swing.GroupLayout(FrameAggiornamento.getContentPane());
        FrameAggiornamento.getContentPane().setLayout(FrameAggiornamentoLayout);
        FrameAggiornamentoLayout.setHorizontalGroup(
            FrameAggiornamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Pannelllo_FrameAggiornamento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        FrameAggiornamentoLayout.setVerticalGroup(
            FrameAggiornamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Pannelllo_FrameAggiornamento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        PannelloInformazioni.setAlwaysOnTop(true);
        PannelloInformazioni.setMinimumSize(new java.awt.Dimension(817, 516));
        PannelloInformazioni.setUndecorated(true);

        Pannello_PannelloInformazioni.setBackground(new java.awt.Color(184, 211, 185));
        Pannello_PannelloInformazioni.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(102, 102, 102), new java.awt.Color(102, 102, 102), new java.awt.Color(102, 102, 102), new java.awt.Color(102, 102, 102)));
        Pannello_PannelloInformazioni.setForeground(new java.awt.Color(255, 255, 255));
        Pannello_PannelloInformazioni.setMaximumSize(new java.awt.Dimension(817, 516));
        Pannello_PannelloInformazioni.setMinimumSize(new java.awt.Dimension(817, 516));
        Pannello_PannelloInformazioni.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                Pannello_PannelloInformazioniMouseDragged(evt);
            }
        });
        Pannello_PannelloInformazioni.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Pannello_PannelloInformazioniMousePressed(evt);
            }
        });

        jPanel16.setBackground(new java.awt.Color(102, 102, 102));
        jPanel16.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        ClosePanelloInformazioni.setBackground(new java.awt.Color(102, 102, 102));
        ClosePanelloInformazioni.setIcon(new javax.swing.ImageIcon(getClass().getResource("/accountmanager/img/exit.png"))); // NOI18N
        ClosePanelloInformazioni.setToolTipText("Chiudi finestra");
        ClosePanelloInformazioni.setFocusPainted(false);
        ClosePanelloInformazioni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClosePanelloInformazioniActionPerformed(evt);
            }
        });

        Pannello_InformazioniGenerali.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Informazioni generali", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(255, 255, 255))); // NOI18N
        Pannello_InformazioniGenerali.setForeground(new java.awt.Color(255, 255, 255));
        Pannello_InformazioniGenerali.setOpaque(false);

        CampoTotaleOmonimi.setEditable(false);
        CampoTotaleOmonimi.setBackground(new java.awt.Color(255, 255, 204));
        CampoTotaleOmonimi.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        CampoTotaleOmonimi.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        CampoTotaleOmonimi.setText("0");
        CampoTotaleOmonimi.setBorder(javax.swing.BorderFactory.createTitledBorder("Totale omonimi"));

        CampoBaseOmonimi.setEditable(false);
        CampoBaseOmonimi.setBackground(new java.awt.Color(255, 255, 204));
        CampoBaseOmonimi.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        CampoBaseOmonimi.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        CampoBaseOmonimi.setText("0");
        CampoBaseOmonimi.setBorder(javax.swing.BorderFactory.createTitledBorder("Omonimi in file base"));

        CampoImportatiOmonimi.setEditable(false);
        CampoImportatiOmonimi.setBackground(new java.awt.Color(255, 255, 204));
        CampoImportatiOmonimi.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        CampoImportatiOmonimi.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        CampoImportatiOmonimi.setText("0");
        CampoImportatiOmonimi.setBorder(javax.swing.BorderFactory.createTitledBorder("Omonimi in files importati"));

        CampoNumeroDipartimenti.setEditable(false);
        CampoNumeroDipartimenti.setBackground(new java.awt.Color(255, 255, 204));
        CampoNumeroDipartimenti.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        CampoNumeroDipartimenti.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        CampoNumeroDipartimenti.setText("\\");
            CampoNumeroDipartimenti.setBorder(javax.swing.BorderFactory.createTitledBorder("Numero dipartimenti"));

            CampoNoDipartimento.setEditable(false);
            CampoNoDipartimento.setBackground(new java.awt.Color(255, 255, 204));
            CampoNoDipartimento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            CampoNoDipartimento.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            CampoNoDipartimento.setText("0");
            CampoNoDipartimento.setBorder(javax.swing.BorderFactory.createTitledBorder("Membri senza dipartimento"));

            CampoMembriImportati.setEditable(false);
            CampoMembriImportati.setBackground(new java.awt.Color(255, 255, 204));
            CampoMembriImportati.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            CampoMembriImportati.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            CampoMembriImportati.setText("0");
            CampoMembriImportati.setBorder(javax.swing.BorderFactory.createTitledBorder("Totale membri importati"));

            CampoNumeroFiles.setEditable(false);
            CampoNumeroFiles.setBackground(new java.awt.Color(255, 255, 204));
            CampoNumeroFiles.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            CampoNumeroFiles.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            CampoNumeroFiles.setText("0");
            CampoNumeroFiles.setBorder(javax.swing.BorderFactory.createTitledBorder("Numero files importati"));

            javax.swing.GroupLayout Pannello_InformazioniGeneraliLayout = new javax.swing.GroupLayout(Pannello_InformazioniGenerali);
            Pannello_InformazioniGenerali.setLayout(Pannello_InformazioniGeneraliLayout);
            Pannello_InformazioniGeneraliLayout.setHorizontalGroup(
                Pannello_InformazioniGeneraliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pannello_InformazioniGeneraliLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(Pannello_InformazioniGeneraliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(CampoBaseOmonimi, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(CampoTotaleOmonimi, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                        .addComponent(CampoImportatiOmonimi)
                        .addComponent(CampoNumeroDipartimenti)
                        .addComponent(CampoNumeroFiles)
                        .addComponent(CampoNoDipartimento)
                        .addComponent(CampoMembriImportati))
                    .addContainerGap())
            );
            Pannello_InformazioniGeneraliLayout.setVerticalGroup(
                Pannello_InformazioniGeneraliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pannello_InformazioniGeneraliLayout.createSequentialGroup()
                    .addComponent(CampoTotaleOmonimi, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(CampoBaseOmonimi, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(CampoImportatiOmonimi, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(CampoMembriImportati, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(CampoNoDipartimento, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(11, 11, 11)
                    .addComponent(CampoNumeroDipartimenti, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(CampoNumeroFiles, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(45, Short.MAX_VALUE))
            );

            javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
            jPanel16.setLayout(jPanel16Layout);
            jPanel16Layout.setHorizontalGroup(
                jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel16Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(Pannello_InformazioniGenerali, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
                .addGroup(jPanel16Layout.createSequentialGroup()
                    .addComponent(ClosePanelloInformazioni, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
            );
            jPanel16Layout.setVerticalGroup(
                jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel16Layout.createSequentialGroup()
                    .addComponent(ClosePanelloInformazioni, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(Pannello_InformazioniGenerali, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );

            TextFileBase.setEditable(false);
            TextFileBase.setBackground(new java.awt.Color(255, 255, 204));
            TextFileBase.setBorder(javax.swing.BorderFactory.createTitledBorder("File base"));

            ScrollPane_TabellaFiles.setBackground(new java.awt.Color(184, 211, 185));
            ScrollPane_TabellaFiles.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true), "Files importati"));

            TabellaFile.setBackground(new java.awt.Color(255, 255, 204));
            TabellaFile.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            TabellaFile.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "Nome file", "Path file", "Dimensione", "Membri"
                }
            ) {
                boolean[] canEdit = new boolean [] {
                    false, true, false, false
                };

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            TabellaFile.setSelectionForeground(new java.awt.Color(0, 0, 0));
            TabellaFile.getTableHeader().setReorderingAllowed(false);
            TabellaFile.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    TabellaFileMouseClicked(evt);
                }
            });
            ScrollPane_TabellaFiles.setViewportView(TabellaFile);

            Pannello_InformazioniFileImportato.setBackground(new java.awt.Color(184, 211, 185));
            Pannello_InformazioniFileImportato.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true), "Anteprima"));

            AnteprimaFileImportato.setEditable(false);
            AnteprimaFileImportato.setBackground(new java.awt.Color(255, 255, 204));
            AnteprimaFileImportato.setColumns(50);
            AnteprimaFileImportato.setRows(5);
            jScrollPane1.setViewportView(AnteprimaFileImportato);

            javax.swing.GroupLayout Pannello_InformazioniFileImportatoLayout = new javax.swing.GroupLayout(Pannello_InformazioniFileImportato);
            Pannello_InformazioniFileImportato.setLayout(Pannello_InformazioniFileImportatoLayout);
            Pannello_InformazioniFileImportatoLayout.setHorizontalGroup(
                Pannello_InformazioniFileImportatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1)
            );
            Pannello_InformazioniFileImportatoLayout.setVerticalGroup(
                Pannello_InformazioniFileImportatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1)
            );

            javax.swing.GroupLayout Pannello_PannelloInformazioniLayout = new javax.swing.GroupLayout(Pannello_PannelloInformazioni);
            Pannello_PannelloInformazioni.setLayout(Pannello_PannelloInformazioniLayout);
            Pannello_PannelloInformazioniLayout.setHorizontalGroup(
                Pannello_PannelloInformazioniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pannello_PannelloInformazioniLayout.createSequentialGroup()
                    .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(Pannello_PannelloInformazioniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(ScrollPane_TabellaFiles)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pannello_PannelloInformazioniLayout.createSequentialGroup()
                            .addGap(0, 1, Short.MAX_VALUE)
                            .addComponent(TextFileBase, javax.swing.GroupLayout.PREFERRED_SIZE, 544, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(Pannello_InformazioniFileImportato, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap())
            );
            Pannello_PannelloInformazioniLayout.setVerticalGroup(
                Pannello_PannelloInformazioniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pannello_PannelloInformazioniLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(TextFileBase, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(ScrollPane_TabellaFiles, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(Pannello_InformazioniFileImportato, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
                .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );

            TextFileBase.getAccessibleContext().setAccessibleName("");

            javax.swing.GroupLayout PannelloInformazioniLayout = new javax.swing.GroupLayout(PannelloInformazioni.getContentPane());
            PannelloInformazioni.getContentPane().setLayout(PannelloInformazioniLayout);
            PannelloInformazioniLayout.setHorizontalGroup(
                PannelloInformazioniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(Pannello_PannelloInformazioni, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            PannelloInformazioniLayout.setVerticalGroup(
                PannelloInformazioniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(Pannello_PannelloInformazioni, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );

            SceltaSalvataggio.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            SceltaSalvataggio.setMinimumSize(new java.awt.Dimension(835, 503));
            SceltaSalvataggio.setUndecorated(true);
            SceltaSalvataggio.setResizable(false);

            Pannello_SceltaSalvataggio.setBackground(new java.awt.Color(184, 211, 185));
            Pannello_SceltaSalvataggio.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.gray, java.awt.Color.gray, java.awt.Color.gray, java.awt.Color.gray));
            Pannello_SceltaSalvataggio.setMaximumSize(new java.awt.Dimension(835, 503));
            Pannello_SceltaSalvataggio.setMinimumSize(new java.awt.Dimension(835, 503));
            Pannello_SceltaSalvataggio.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
                public void mouseDragged(java.awt.event.MouseEvent evt) {
                    Pannello_SceltaSalvataggioMouseDragged(evt);
                }
            });
            Pannello_SceltaSalvataggio.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    Pannello_SceltaSalvataggioMousePressed(evt);
                }
            });

            JPane123.setBackground(new java.awt.Color(102, 102, 102));
            JPane123.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            Pannello_EstrazioneDati.setBackground(new java.awt.Color(102, 102, 102));
            Pannello_EstrazioneDati.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Estrazione dati", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(255, 255, 255))); // NOI18N

            CheckBoxSalvaTutto.setBackground(new java.awt.Color(102, 102, 102));
            CheckBoxSalvaTutto.setForeground(new java.awt.Color(255, 255, 255));
            CheckBoxSalvaTutto.setText("Salva tutto");
            CheckBoxSalvaTutto.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    CheckBoxSalvaTuttoActionPerformed(evt);
                }
            });

            CheckBoxSalvaNuovi.setBackground(new java.awt.Color(102, 102, 102));
            CheckBoxSalvaNuovi.setForeground(new java.awt.Color(255, 255, 255));
            CheckBoxSalvaNuovi.setText("Salva solo nuovi");
            CheckBoxSalvaNuovi.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    CheckBoxSalvaNuoviActionPerformed(evt);
                }
            });

            CheckBoxSalvaVecchi.setBackground(new java.awt.Color(102, 102, 102));
            CheckBoxSalvaVecchi.setForeground(new java.awt.Color(255, 255, 255));
            CheckBoxSalvaVecchi.setText("Salva solo vecchi");
            CheckBoxSalvaVecchi.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    CheckBoxSalvaVecchiActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout Pannello_EstrazioneDatiLayout = new javax.swing.GroupLayout(Pannello_EstrazioneDati);
            Pannello_EstrazioneDati.setLayout(Pannello_EstrazioneDatiLayout);
            Pannello_EstrazioneDatiLayout.setHorizontalGroup(
                Pannello_EstrazioneDatiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(CheckBoxSalvaTutto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(CheckBoxSalvaNuovi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(CheckBoxSalvaVecchi, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
            );
            Pannello_EstrazioneDatiLayout.setVerticalGroup(
                Pannello_EstrazioneDatiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pannello_EstrazioneDatiLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(CheckBoxSalvaTutto)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(CheckBoxSalvaNuovi)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(CheckBoxSalvaVecchi)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            CloseSceltaSalvataggio.setBackground(new java.awt.Color(102, 102, 102));
            CloseSceltaSalvataggio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/accountmanager/img/exit.png"))); // NOI18N
            CloseSceltaSalvataggio.setToolTipText("Chiudi finestra");
            CloseSceltaSalvataggio.setFocusPainted(false);
            CloseSceltaSalvataggio.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    CloseSceltaSalvataggioActionPerformed(evt);
                }
            });

            Help_Salvataggio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/accountmanager/img/help.png"))); // NOI18N
            Help_Salvataggio.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    Help_SalvataggioMouseClicked(evt);
                }
            });

            javax.swing.GroupLayout JPane123Layout = new javax.swing.GroupLayout(JPane123);
            JPane123.setLayout(JPane123Layout);
            JPane123Layout.setHorizontalGroup(
                JPane123Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(JPane123Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(Pannello_EstrazioneDati, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
                .addGroup(JPane123Layout.createSequentialGroup()
                    .addComponent(CloseSceltaSalvataggio, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Help_Salvataggio, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
            );
            JPane123Layout.setVerticalGroup(
                JPane123Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(JPane123Layout.createSequentialGroup()
                    .addGroup(JPane123Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(CloseSceltaSalvataggio, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                        .addComponent(Help_Salvataggio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(Pannello_EstrazioneDati, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );

            FileChooserSalvaNome.setApproveButtonText("Salva con nome");
            FileChooserSalvaNome.setDragEnabled(true);
            FileChooserSalvaNome.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    FileChooserSalvaNomeActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout Pannello_SceltaSalvataggioLayout = new javax.swing.GroupLayout(Pannello_SceltaSalvataggio);
            Pannello_SceltaSalvataggio.setLayout(Pannello_SceltaSalvataggioLayout);
            Pannello_SceltaSalvataggioLayout.setHorizontalGroup(
                Pannello_SceltaSalvataggioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pannello_SceltaSalvataggioLayout.createSequentialGroup()
                    .addComponent(JPane123, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(FileChooserSalvaNome, javax.swing.GroupLayout.DEFAULT_SIZE, 611, Short.MAX_VALUE)
                    .addContainerGap())
            );
            Pannello_SceltaSalvataggioLayout.setVerticalGroup(
                Pannello_SceltaSalvataggioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(JPane123, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(Pannello_SceltaSalvataggioLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(FileChooserSalvaNome, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)
                    .addContainerGap())
            );

            javax.swing.GroupLayout SceltaSalvataggioLayout = new javax.swing.GroupLayout(SceltaSalvataggio.getContentPane());
            SceltaSalvataggio.getContentPane().setLayout(SceltaSalvataggioLayout);
            SceltaSalvataggioLayout.setHorizontalGroup(
                SceltaSalvataggioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 835, Short.MAX_VALUE)
                .addGroup(SceltaSalvataggioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Pannello_SceltaSalvataggio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            SceltaSalvataggioLayout.setVerticalGroup(
                SceltaSalvataggioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 503, Short.MAX_VALUE)
                .addGroup(SceltaSalvataggioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Pannello_SceltaSalvataggio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            CorrettoreOmonimi.setAlwaysOnTop(true);
            CorrettoreOmonimi.setMinimumSize(new java.awt.Dimension(982, 549));
            CorrettoreOmonimi.setUndecorated(true);

            Pannello_CorrettoreOminimi.setBackground(new java.awt.Color(184, 211, 185));
            Pannello_CorrettoreOminimi.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(102, 102, 102), new java.awt.Color(102, 102, 102), new java.awt.Color(102, 102, 102), new java.awt.Color(102, 102, 102)));
            Pannello_CorrettoreOminimi.setPreferredSize(new java.awt.Dimension(950, 549));
            Pannello_CorrettoreOminimi.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
                public void mouseDragged(java.awt.event.MouseEvent evt) {
                    Pannello_CorrettoreOminimiMouseDragged(evt);
                }
            });
            Pannello_CorrettoreOminimi.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    Pannello_CorrettoreOminimiMousePressed(evt);
                }
            });

            JPane124.setBackground(new java.awt.Color(102, 102, 102));
            JPane124.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            Pannello_EstrazioneDati1.setBackground(new java.awt.Color(102, 102, 102));
            Pannello_EstrazioneDati1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Correttore omonimi", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(255, 255, 255))); // NOI18N

            ButtonCancellaRiga.setText("Cancella riga / righe");
            ButtonCancellaRiga.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    ButtonCancellaRigaActionPerformed(evt);
                }
            });

            ButtonRinominaTutto.setText("Rinomina tutti");
            ButtonRinominaTutto.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    ButtonRinominaTuttoActionPerformed(evt);
                }
            });

            ButtonCorreggi.setText("Correggi");
            ButtonCorreggi.setToolTipText("Applica i comandi assegnati");
            ButtonCorreggi.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    ButtonCorreggiActionPerformed(evt);
                }
            });

            ButtonRinominaSelezionati.setText("Rinomina selezionati");
            ButtonRinominaSelezionati.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    ButtonRinominaSelezionatiActionPerformed(evt);
                }
            });

            ButtonRipristina.setText("Ripristina eliminati selezionati");
            ButtonRipristina.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    ButtonRipristinaActionPerformed(evt);
                }
            });

            Icon_CorrettoreOmonimi.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            Icon_CorrettoreOmonimi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/accountmanager/img/omonimi.png"))); // NOI18N

            javax.swing.GroupLayout Pannello_EstrazioneDati1Layout = new javax.swing.GroupLayout(Pannello_EstrazioneDati1);
            Pannello_EstrazioneDati1.setLayout(Pannello_EstrazioneDati1Layout);
            Pannello_EstrazioneDati1Layout.setHorizontalGroup(
                Pannello_EstrazioneDati1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pannello_EstrazioneDati1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(Pannello_EstrazioneDati1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(Icon_CorrettoreOmonimi, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ButtonRinominaTutto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ButtonCorreggi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ButtonCancellaRiga, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ButtonRinominaSelezionati, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ButtonRipristina, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE))
                    .addContainerGap())
            );
            Pannello_EstrazioneDati1Layout.setVerticalGroup(
                Pannello_EstrazioneDati1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pannello_EstrazioneDati1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(ButtonCancellaRiga)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(ButtonRinominaTutto)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(ButtonRinominaSelezionati)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(ButtonRipristina)
                    .addGap(18, 18, 18)
                    .addComponent(Icon_CorrettoreOmonimi, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(ButtonCorreggi, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );

            CloseCorrettoreOmonimi.setBackground(new java.awt.Color(102, 102, 102));
            CloseCorrettoreOmonimi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/accountmanager/img/exit.png"))); // NOI18N
            CloseCorrettoreOmonimi.setToolTipText("Chiudi finestra");
            CloseCorrettoreOmonimi.setFocusPainted(false);
            CloseCorrettoreOmonimi.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    CloseCorrettoreOmonimiActionPerformed(evt);
                }
            });

            Help_CorrettoreOmonimi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/accountmanager/img/help.png"))); // NOI18N
            Help_CorrettoreOmonimi.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    Help_CorrettoreOmonimiMouseClicked(evt);
                }
            });

            javax.swing.GroupLayout JPane124Layout = new javax.swing.GroupLayout(JPane124);
            JPane124.setLayout(JPane124Layout);
            JPane124Layout.setHorizontalGroup(
                JPane124Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(JPane124Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(Pannello_EstrazioneDati1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
                .addGroup(JPane124Layout.createSequentialGroup()
                    .addComponent(CloseCorrettoreOmonimi, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Help_CorrettoreOmonimi, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
            );
            JPane124Layout.setVerticalGroup(
                JPane124Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(JPane124Layout.createSequentialGroup()
                    .addGroup(JPane124Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(CloseCorrettoreOmonimi, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                        .addComponent(Help_CorrettoreOmonimi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                    .addComponent(Pannello_EstrazioneDati1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );

            Panel_CorrettoreOminimi.setBackground(new java.awt.Color(184, 211, 185));
            Panel_CorrettoreOminimi.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), "Individua e correggi omonimi"));

            ScrollPane_TabellaOmonimi.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    ScrollPane_TabellaOmonimiMousePressed(evt);
                }
            });

            TabellaOmonimi.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            TabellaOmonimi.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "Numero", "Cognome", "Nome", "Email governativa", "Dipartimento", "Stato/Azione"
                }
            ) {
                boolean[] canEdit = new boolean [] {
                    false, true, true, false, false, false
                };

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            TabellaOmonimi.getTableHeader().setReorderingAllowed(false);
            TabellaOmonimi.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    TabellaOmonimiMousePressed(evt);
                }
            });
            ScrollPane_TabellaOmonimi.setViewportView(TabellaOmonimi);

            javax.swing.GroupLayout Panel_CorrettoreOminimiLayout = new javax.swing.GroupLayout(Panel_CorrettoreOminimi);
            Panel_CorrettoreOminimi.setLayout(Panel_CorrettoreOminimiLayout);
            Panel_CorrettoreOminimiLayout.setHorizontalGroup(
                Panel_CorrettoreOminimiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(ScrollPane_TabellaOmonimi, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 677, Short.MAX_VALUE)
            );
            Panel_CorrettoreOminimiLayout.setVerticalGroup(
                Panel_CorrettoreOminimiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(ScrollPane_TabellaOmonimi)
            );

            javax.swing.GroupLayout Pannello_CorrettoreOminimiLayout = new javax.swing.GroupLayout(Pannello_CorrettoreOminimi);
            Pannello_CorrettoreOminimi.setLayout(Pannello_CorrettoreOminimiLayout);
            Pannello_CorrettoreOminimiLayout.setHorizontalGroup(
                Pannello_CorrettoreOminimiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pannello_CorrettoreOminimiLayout.createSequentialGroup()
                    .addComponent(JPane124, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(Panel_CorrettoreOminimi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );
            Pannello_CorrettoreOminimiLayout.setVerticalGroup(
                Pannello_CorrettoreOminimiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(JPane124, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(Pannello_CorrettoreOminimiLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(Panel_CorrettoreOminimi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );

            javax.swing.GroupLayout CorrettoreOmonimiLayout = new javax.swing.GroupLayout(CorrettoreOmonimi.getContentPane());
            CorrettoreOmonimi.getContentPane().setLayout(CorrettoreOmonimiLayout);
            CorrettoreOmonimiLayout.setHorizontalGroup(
                CorrettoreOmonimiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(Pannello_CorrettoreOminimi, javax.swing.GroupLayout.DEFAULT_SIZE, 982, Short.MAX_VALUE)
            );
            CorrettoreOmonimiLayout.setVerticalGroup(
                CorrettoreOmonimiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(Pannello_CorrettoreOminimi, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE)
            );

            PathFileDragDrop.setText("jTextField2");

            Dialog_Chiusura.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            Dialog_Chiusura.setAlwaysOnTop(true);
            Dialog_Chiusura.setMinimumSize(new java.awt.Dimension(450, 185));
            Dialog_Chiusura.setUndecorated(true);

            Panel_Chiusura.setBackground(new java.awt.Color(184, 211, 185));
            Panel_Chiusura.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            Panel_Chiusura.setMinimumSize(new java.awt.Dimension(450, 185));
            Panel_Chiusura.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
                public void mouseDragged(java.awt.event.MouseEvent evt) {
                    Panel_ChiusuraMouseDragged(evt);
                }
            });
            Panel_Chiusura.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    Panel_ChiusuraMousePressed(evt);
                }
            });

            Panel2_Chiusura.setBackground(new java.awt.Color(102, 102, 102));
            Panel2_Chiusura.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

            TextField_Dialog.setEditable(false);
            TextField_Dialog.setBackground(new java.awt.Color(255, 255, 204));
            TextField_Dialog.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            TextField_Dialog.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            TextField_Dialog.setText("Il programma sarà riavviato, continuare?");

            javax.swing.GroupLayout Panel2_ChiusuraLayout = new javax.swing.GroupLayout(Panel2_Chiusura);
            Panel2_Chiusura.setLayout(Panel2_ChiusuraLayout);
            Panel2_ChiusuraLayout.setHorizontalGroup(
                Panel2_ChiusuraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Panel2_ChiusuraLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(TextField_Dialog)
                    .addContainerGap())
            );
            Panel2_ChiusuraLayout.setVerticalGroup(
                Panel2_ChiusuraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Panel2_ChiusuraLayout.createSequentialGroup()
                    .addGap(20, 20, 20)
                    .addComponent(TextField_Dialog, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(26, Short.MAX_VALUE))
            );

            Button_Riavvia.setText("Riavvia");
            Button_Riavvia.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    Button_RiavviaActionPerformed(evt);
                }
            });

            Button_Annulla.setText("Annulla");
            Button_Annulla.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    Button_AnnullaActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout Panel_ChiusuraLayout = new javax.swing.GroupLayout(Panel_Chiusura);
            Panel_Chiusura.setLayout(Panel_ChiusuraLayout);
            Panel_ChiusuraLayout.setHorizontalGroup(
                Panel_ChiusuraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(Panel2_Chiusura, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_ChiusuraLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(Button_Annulla, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(Button_Riavvia, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );
            Panel_ChiusuraLayout.setVerticalGroup(
                Panel_ChiusuraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Panel_ChiusuraLayout.createSequentialGroup()
                    .addComponent(Panel2_Chiusura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(28, 28, 28)
                    .addGroup(Panel_ChiusuraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(Button_Riavvia, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                        .addComponent(Button_Annulla, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            javax.swing.GroupLayout Dialog_ChiusuraLayout = new javax.swing.GroupLayout(Dialog_Chiusura.getContentPane());
            Dialog_Chiusura.getContentPane().setLayout(Dialog_ChiusuraLayout);
            Dialog_ChiusuraLayout.setHorizontalGroup(
                Dialog_ChiusuraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(Panel_Chiusura, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            Dialog_ChiusuraLayout.setVerticalGroup(
                Dialog_ChiusuraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(Panel_Chiusura, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );

            Guida.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

            Panel_Guida.setBackground(new java.awt.Color(184, 211, 185));
            Panel_Guida.setOpaque(false);
            Panel_Guida.setPreferredSize(new java.awt.Dimension(990, 790));

            Indice.setBackground(new java.awt.Color(255, 255, 204));
            javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("Guida");
            javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Manuale");
            javax.swing.tree.DefaultMutableTreeNode treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Passaggi_di_lavorazione");
            treeNode2.add(treeNode3);
            treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Requisiti_lettura_file");
            treeNode2.add(treeNode3);
            treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Aggiungere_membro");
            treeNode2.add(treeNode3);
            treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Importazione");
            treeNode2.add(treeNode3);
            treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Esportazione");
            treeNode2.add(treeNode3);
            treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Capire_la_tabella_Flags");
            treeNode2.add(treeNode3);
            treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Verifica_presenza_omonimi");
            treeNode2.add(treeNode3);
            treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Strumento_Anti_Omonimi");
            treeNode2.add(treeNode3);
            treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Rinominazione_email");
            treeNode2.add(treeNode3);
            treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Installazione_software");
            treeNode2.add(treeNode3);
            treeNode1.add(treeNode2);
            treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Risoluzione_Problemi");
            treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("File_non_leggibile");
            treeNode2.add(treeNode3);
            treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("immodificabilita");
            treeNode2.add(treeNode3);
            treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Server_Problemi");
            treeNode2.add(treeNode3);
            treeNode1.add(treeNode2);
            Indice.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
            Indice.setMinimumSize(new java.awt.Dimension(72, 64));
            Indice.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
                public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                    IndiceValueChanged(evt);
                }
            });
            jScrollPane2.setViewportView(Indice);

            TextGuida.setBackground(new java.awt.Color(255, 255, 204));
            TextGuida.setColumns(20);
            TextGuida.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
            TextGuida.setRows(5);
            TextGuida.setText("\t\t\tBENVENUTI NELLA GUIDA DI AccountManager\n\n\t- Il Software AccountManager è un programma sviluppato con NetBeans\n\t  allo scopo di risolvere il problema delle omonimie in liste di nominativi\n\t- La guida è ancora in fase di manutenzione, alcuni aspetti potrebbero\n\t  non essere di aiuto.");
            TextGuida.setDisabledTextColor(new java.awt.Color(0, 0, 0));
            TextGuida.setEnabled(false);
            TextGuida.setMinimumSize(new java.awt.Dimension(0, 0));
            jScrollPane5.setViewportView(TextGuida);

            OptionSlide.setEditable(false);
            OptionSlide.setBackground(new java.awt.Color(0, 0, 0));
            OptionSlide.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            OptionSlide.setForeground(new java.awt.Color(0, 255, 204));
            OptionSlide.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            OptionSlide.setText("Visualizza slide Manuale");
            OptionSlide.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
            OptionSlide.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            OptionSlide.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    OptionSlideMouseClicked(evt);
                }
            });

            SlidePanel.setBackground(new java.awt.Color(0, 0, 0));
            SlidePanel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

            ButtonSlideLeft.setBackground(new java.awt.Color(184, 211, 185));
            ButtonSlideLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/accountmanager/img/Small-arrow-left-icon.png"))); // NOI18N
            ButtonSlideLeft.setBorderPainted(false);
            ButtonSlideLeft.setContentAreaFilled(false);
            ButtonSlideLeft.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    ButtonSlideLeftMousePressed(evt);
                }
            });
            ButtonSlideLeft.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    ButtonSlideLeftActionPerformed(evt);
                }
            });

            ButtonSlideRight.setBackground(new java.awt.Color(184, 211, 185));
            ButtonSlideRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/accountmanager/img/Small-arrow-right-icon.png"))); // NOI18N
            ButtonSlideRight.setText("jButton1");
            ButtonSlideRight.setBorderPainted(false);
            ButtonSlideRight.setContentAreaFilled(false);
            ButtonSlideRight.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    ButtonSlideRightMousePressed(evt);
                }
            });
            ButtonSlideRight.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    ButtonSlideRightActionPerformed(evt);
                }
            });

            NumberSlide.setEditable(false);
            NumberSlide.setBackground(new java.awt.Color(0, 0, 0));
            NumberSlide.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            NumberSlide.setForeground(new java.awt.Color(0, 255, 204));
            NumberSlide.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            NumberSlide.setText("Slide: 1/3");

            javax.swing.GroupLayout Panel_GuidaLayout = new javax.swing.GroupLayout(Panel_Guida);
            Panel_Guida.setLayout(Panel_GuidaLayout);
            Panel_GuidaLayout.setHorizontalGroup(
                Panel_GuidaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Panel_GuidaLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(Panel_GuidaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(Panel_GuidaLayout.createSequentialGroup()
                            .addComponent(ButtonSlideLeft, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(SlidePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(ButtonSlideRight, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(Panel_GuidaLayout.createSequentialGroup()
                            .addGroup(Panel_GuidaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(OptionSlide, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(Panel_GuidaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 729, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(NumberSlide, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addContainerGap(18, Short.MAX_VALUE))
            );
            Panel_GuidaLayout.setVerticalGroup(
                Panel_GuidaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Panel_GuidaLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(Panel_GuidaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                        .addComponent(jScrollPane5))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(Panel_GuidaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(OptionSlide, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(NumberSlide, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(Panel_GuidaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(SlidePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ButtonSlideRight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ButtonSlideLeft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap())
            );

            javax.swing.GroupLayout GuidaLayout = new javax.swing.GroupLayout(Guida.getContentPane());
            Guida.getContentPane().setLayout(GuidaLayout);
            GuidaLayout.setHorizontalGroup(
                GuidaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(Panel_Guida, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            GuidaLayout.setVerticalGroup(
                GuidaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(Panel_Guida, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );

            Guida_veloce.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            Guida_veloce.setAlwaysOnTop(true);
            Guida_veloce.setMinimumSize(new java.awt.Dimension(550, 300));
            Guida_veloce.setUndecorated(true);
            Guida_veloce.setResizable(false);

            Guida_veloce_text.setEditable(false);
            Guida_veloce_text.setBackground(new java.awt.Color(255, 255, 204));
            Guida_veloce_text.setColumns(20);
            Guida_veloce_text.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
            Guida_veloce_text.setRows(5);
            Guida_veloce_text.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
            Guida_veloce_text.setMinimumSize(new java.awt.Dimension(164, 200));
            Guida_veloce_text.setPreferredSize(new java.awt.Dimension(164, 200));

            javax.swing.GroupLayout Guida_veloceLayout = new javax.swing.GroupLayout(Guida_veloce.getContentPane());
            Guida_veloce.getContentPane().setLayout(Guida_veloceLayout);
            Guida_veloceLayout.setHorizontalGroup(
                Guida_veloceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(Guida_veloce_text, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
            );
            Guida_veloceLayout.setVerticalGroup(
                Guida_veloceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(Guida_veloce_text, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
            );

            Comparatore.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            Comparatore.setAlwaysOnTop(true);
            Comparatore.setMinimumSize(new java.awt.Dimension(666, 418));
            Comparatore.setUndecorated(true);

            Pannello_comparatore.setBackground(new java.awt.Color(184, 211, 185));
            Pannello_comparatore.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.gray, java.awt.Color.gray, java.awt.Color.gray, java.awt.Color.gray));
            Pannello_comparatore.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
            Pannello_comparatore.setMinimumSize(new java.awt.Dimension(666, 418));
            Pannello_comparatore.setPreferredSize(new java.awt.Dimension(666, 418));
            Pannello_comparatore.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
                public void mouseDragged(java.awt.event.MouseEvent evt) {
                    Pannello_comparatoreMouseDragged(evt);
                }
            });
            Pannello_comparatore.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    Pannello_comparatoreMousePressed(evt);
                }
            });

            Pannello_comparatore2.setBackground(new java.awt.Color(102, 102, 102));
            Pannello_comparatore2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            Pannello_comparatore2.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
            Pannello_comparatore2.setPreferredSize(new java.awt.Dimension(666, 330));

            CloseComparatore.setBackground(new java.awt.Color(102, 102, 102));
            CloseComparatore.setIcon(new javax.swing.ImageIcon(getClass().getResource("/accountmanager/img/exit.png"))); // NOI18N
            CloseComparatore.setToolTipText("Chiudi finestra");
            CloseComparatore.setFocusPainted(false);
            CloseComparatore.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    CloseComparatoreActionPerformed(evt);
                }
            });

            Text_File_Comparato.setEditable(false);
            Text_File_Comparato.setBackground(new java.awt.Color(255, 255, 204));
            Text_File_Comparato.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            Text_File_Comparato.setHorizontalAlignment(javax.swing.JTextField.LEFT);
            Text_File_Comparato.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            TextFileComparato.setEditable(false);
            TextFileComparato.setBackground(new java.awt.Color(102, 102, 102));
            TextFileComparato.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            TextFileComparato.setForeground(new java.awt.Color(255, 255, 255));
            TextFileComparato.setText("Stai comparando il contenuto della tabella con il seguente file:");
            TextFileComparato.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

            TabellaComparatore.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "Numero", "Cognome", "Nome", "Email governativa", "Dipartimento", "Stato"
                }
            ) {
                boolean[] canEdit = new boolean [] {
                    false, false, false, false, false, false
                };

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            TabellaComparatore.getTableHeader().setReorderingAllowed(false);
            ScrollaPane_TabellaComparatore.setViewportView(TabellaComparatore);

            javax.swing.GroupLayout Pannello_comparatore2Layout = new javax.swing.GroupLayout(Pannello_comparatore2);
            Pannello_comparatore2.setLayout(Pannello_comparatore2Layout);
            Pannello_comparatore2Layout.setHorizontalGroup(
                Pannello_comparatore2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pannello_comparatore2Layout.createSequentialGroup()
                    .addComponent(CloseComparatore, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(TextFileComparato, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pannello_comparatore2Layout.createSequentialGroup()
                    .addGroup(Pannello_comparatore2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(Pannello_comparatore2Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(ScrollaPane_TabellaComparatore))
                        .addGroup(Pannello_comparatore2Layout.createSequentialGroup()
                            .addGap(70, 70, 70)
                            .addComponent(Text_File_Comparato)))
                    .addContainerGap())
            );
            Pannello_comparatore2Layout.setVerticalGroup(
                Pannello_comparatore2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pannello_comparatore2Layout.createSequentialGroup()
                    .addGroup(Pannello_comparatore2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(Pannello_comparatore2Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(TextFileComparato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(Text_File_Comparato, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(CloseComparatore, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addComponent(ScrollaPane_TabellaComparatore, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
                    .addContainerGap())
            );

            Contatore_doppioni.setEditable(false);
            Contatore_doppioni.setBackground(new java.awt.Color(255, 255, 204));
            Contatore_doppioni.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            Contatore_doppioni.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            Contatore_doppioni.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            Contatore_omonimi.setEditable(false);
            Contatore_omonimi.setBackground(new java.awt.Color(255, 255, 204));
            Contatore_omonimi.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            Contatore_omonimi.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            Contatore_omonimi.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            TextOmonimi.setEditable(false);
            TextOmonimi.setBackground(new java.awt.Color(184, 211, 185));
            TextOmonimi.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            TextOmonimi.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            TextOmonimi.setText("Numero omonimi");
            TextOmonimi.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

            TextDoppioni.setEditable(false);
            TextDoppioni.setBackground(new java.awt.Color(184, 211, 185));
            TextDoppioni.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            TextDoppioni.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            TextDoppioni.setText("Numero doppioni");
            TextDoppioni.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

            javax.swing.GroupLayout Pannello_comparatoreLayout = new javax.swing.GroupLayout(Pannello_comparatore);
            Pannello_comparatore.setLayout(Pannello_comparatoreLayout);
            Pannello_comparatoreLayout.setHorizontalGroup(
                Pannello_comparatoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(Pannello_comparatore2, javax.swing.GroupLayout.DEFAULT_SIZE, 662, Short.MAX_VALUE)
                .addGroup(Pannello_comparatoreLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(Pannello_comparatoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(TextDoppioni, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(Pannello_comparatoreLayout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(Contatore_doppioni, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(126, 126, 126)
                    .addGroup(Pannello_comparatoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(Contatore_omonimi)
                        .addComponent(TextOmonimi, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(16, Short.MAX_VALUE))
            );
            Pannello_comparatoreLayout.setVerticalGroup(
                Pannello_comparatoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pannello_comparatoreLayout.createSequentialGroup()
                    .addComponent(Pannello_comparatore2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(Pannello_comparatoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(TextOmonimi, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(TextDoppioni, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(Pannello_comparatoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Contatore_doppioni, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Contatore_omonimi, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(69, 69, 69))
            );

            javax.swing.GroupLayout ComparatoreLayout = new javax.swing.GroupLayout(Comparatore.getContentPane());
            Comparatore.getContentPane().setLayout(ComparatoreLayout);
            ComparatoreLayout.setHorizontalGroup(
                ComparatoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 666, Short.MAX_VALUE)
                .addGroup(ComparatoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Pannello_comparatore, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            ComparatoreLayout.setVerticalGroup(
                ComparatoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 418, Short.MAX_VALUE)
                .addGroup(ComparatoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Pannello_comparatore, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            TabellaFlagsComparatore.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "Stato"
                }
            ) {
                boolean[] canEdit = new boolean [] {
                    false
                };

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });

            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
            setTitle("AccountManager CSV");
            setBackground(new java.awt.Color(153, 255, 153));
            setForeground(java.awt.Color.black);
            setIconImages(null);
            addComponentListener(new java.awt.event.ComponentAdapter() {
                public void componentResized(java.awt.event.ComponentEvent evt) {
                    formComponentResized(evt);
                }
            });
            addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosing(java.awt.event.WindowEvent evt) {
                    formWindowClosing(evt);
                }
                public void windowIconified(java.awt.event.WindowEvent evt) {
                    formWindowIconified(evt);
                }
                public void windowOpened(java.awt.event.WindowEvent evt) {
                    formWindowOpened(evt);
                }
            });

            ScrollPane_JForm.setPreferredSize(new java.awt.Dimension(1394, 861));

            Pannello_JForm.setBackground(new java.awt.Color(184, 211, 185));
            Pannello_JForm.setMinimumSize(new java.awt.Dimension(1300, 598));
            Pannello_JForm.setPreferredSize(new java.awt.Dimension(1200, 588));

            Pannello_StrumentiLaterale.setBackground(new java.awt.Color(102, 102, 102));
            Pannello_StrumentiLaterale.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            Pannello_StrumentiLaterale.setPreferredSize(new java.awt.Dimension(176, 626));

            CancellaRigaButton.setText("Cancella riga / righe");
            CancellaRigaButton.setToolTipText("Selezione una o più righe da eliminare");
            CancellaRigaButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    CancellaRigaButtonActionPerformed(evt);
                }
            });

            EsportaModificheButton.setText("Esporta modifiche");
            EsportaModificheButton.setToolTipText("Salva con nome le modifiche");
            EsportaModificheButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    EsportaModificheButtonActionPerformed(evt);
                }
            });

            GeneraPasswordButton.setText("Genera password");
            GeneraPasswordButton.setToolTipText("Genera password per le righe selezionate");
            GeneraPasswordButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    GeneraPasswordButtonActionPerformed(evt);
                }
            });

            PulisciTabellaButton.setText("Pulisci tabella");
            PulisciTabellaButton.setToolTipText("Pulisce tutta la tabella");
            PulisciTabellaButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    PulisciTabellaButtonActionPerformed(evt);
                }
            });

            LogoCSVPicture.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            LogoCSVPicture.setIcon(new javax.swing.ImageIcon(getClass().getResource("/accountmanager/img/csv2.png"))); // NOI18N
            LogoCSVPicture.setDoubleBuffered(true);
            LogoCSVPicture.setMinimumSize(new java.awt.Dimension(0, 0));
            LogoCSVPicture.setPreferredSize(new java.awt.Dimension(150, 130));

            CheckRadioPasswordRandom.setBackground(new java.awt.Color(102, 102, 102));
            CheckRadioPasswordRandom.setForeground(new java.awt.Color(255, 255, 255));
            CheckRadioPasswordRandom.setText("Password random/manuale");
            CheckRadioPasswordRandom.setToolTipText("Scegli tra inserimento manuale o automatico della password");
            CheckRadioPasswordRandom.setMargin(new java.awt.Insets(3, 3, 3, 3));
            CheckRadioPasswordRandom.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    CheckRadioPasswordRandomActionPerformed(evt);
                }
            });

            AggiungiMembroButton.setText("Aggiungi membro");
            AggiungiMembroButton.setToolTipText("Clicca per aggiungere un nuovo membro");
            AggiungiMembroButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    AggiungiMembroButtonActionPerformed(evt);
                }
            });

            contaEsistenti.setEditable(false);
            contaEsistenti.setBackground(new java.awt.Color(255, 255, 204));
            contaEsistenti.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            contaEsistenti.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            contaEsistenti.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Omonimi"));

            contaAggiunti.setEditable(false);
            contaAggiunti.setBackground(new java.awt.Color(255, 255, 204));
            contaAggiunti.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            contaAggiunti.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            contaAggiunti.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Importati"));

            contaMembri.setEditable(false);
            contaMembri.setBackground(new java.awt.Color(255, 255, 204));
            contaMembri.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            contaMembri.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            contaMembri.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Totale"));

            EliminaModifiche.setText("Elimina modifiche");
            EliminaModifiche.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    EliminaModificheActionPerformed(evt);
                }
            });

            CheckRadioEmail.setBackground(new java.awt.Color(102, 102, 102));
            CheckRadioEmail.setForeground(new java.awt.Color(255, 255, 255));
            CheckRadioEmail.setText("Aggiorna Email primaria");
            CheckRadioEmail.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    CheckRadioEmailActionPerformed(evt);
                }
            });

            contaAggiuntiManuale.setEditable(false);
            contaAggiuntiManuale.setBackground(new java.awt.Color(255, 255, 204));
            contaAggiuntiManuale.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            contaAggiuntiManuale.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            contaAggiuntiManuale.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Aggiunti"));

            javax.swing.GroupLayout Pannello_StrumentiLateraleLayout = new javax.swing.GroupLayout(Pannello_StrumentiLaterale);
            Pannello_StrumentiLaterale.setLayout(Pannello_StrumentiLateraleLayout);
            Pannello_StrumentiLateraleLayout.setHorizontalGroup(
                Pannello_StrumentiLateraleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pannello_StrumentiLateraleLayout.createSequentialGroup()
                    .addGap(10, 10, 10)
                    .addComponent(CheckRadioPasswordRandom)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(Pannello_StrumentiLateraleLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(Pannello_StrumentiLateraleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(contaEsistenti, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(CancellaRigaButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(EsportaModificheButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(GeneraPasswordButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(PulisciTabellaButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(LogoCSVPicture, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(AggiungiMembroButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(contaMembri)
                        .addComponent(contaAggiunti)
                        .addComponent(EliminaModifiche, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(CheckRadioEmail, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(contaAggiuntiManuale))
                    .addContainerGap())
            );
            Pannello_StrumentiLateraleLayout.setVerticalGroup(
                Pannello_StrumentiLateraleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pannello_StrumentiLateraleLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(AggiungiMembroButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(CheckRadioPasswordRandom)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(CheckRadioEmail)
                    .addGap(7, 7, 7)
                    .addComponent(PulisciTabellaButton)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(CancellaRigaButton)
                    .addGap(7, 7, 7)
                    .addComponent(GeneraPasswordButton)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(EsportaModificheButton)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(EliminaModifiche)
                    .addGap(18, 18, 18)
                    .addComponent(contaAggiuntiManuale, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(contaAggiunti, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(contaEsistenti, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(contaMembri, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(LogoCSVPicture, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                    .addContainerGap())
            );

            TabellaAggiungi.setBackground(new java.awt.Color(255, 255, 204));
            TabellaAggiungi.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}
                },
                new String [] {
                    "Cognome", "Nome", "Email primaria", "Password", "Email secondaria", "Telefono lavoro", "Telefono casa", "Telefono cellulare", "Indirizzo lavoro", "Indirizzo casa", "Employee ID", "Employee tipse", "Employee title", "Manager", "Dipartimento", "Cost center"
                }
            ) {
                boolean[] canEdit = new boolean [] {
                    true, true, false, true, true, true, true, true, true, true, true, true, true, true, true, true
                };

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            TabellaAggiungi.setMaximumSize(new java.awt.Dimension(2147483647, 15));
            TabellaAggiungi.setMinimumSize(new java.awt.Dimension(0, 0));
            TabellaAggiungi.setPreferredSize(new java.awt.Dimension(1200, 15));
            TabellaAggiungi.getTableHeader().setReorderingAllowed(false);
            ScrollPane_TabellaAggiungi.setViewportView(TabellaAggiungi);

            GuidaIcon.setBackground(new java.awt.Color(0, 0, 0));
            GuidaIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            GuidaIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/accountmanager/img/Guida_Icona.png"))); // NOI18N
            GuidaIcon.setToolTipText("Guida");
            GuidaIcon.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    GuidaIconMouseClicked(evt);
                }
            });

            SfogliaButton.setText("Sfoglia");
            SfogliaButton.setToolTipText("Scegli un file .csv da visualizzare");
            SfogliaButton.setInheritsPopupMenu(true);
            SfogliaButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    SfogliaButtonActionPerformed(evt);
                }
            });

            PathField.setEditable(false);
            PathField.setBackground(new java.awt.Color(255, 255, 204));
            PathField.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            PathField.setMinimumSize(new java.awt.Dimension(0, 0));

            MessageText.setBackground(new java.awt.Color(255, 255, 204));
            MessageText.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            MessageText.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            MessageText.setBorder(javax.swing.BorderFactory.createTitledBorder("Messaggi"));
            MessageText.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
            MessageText.setDisabledTextColor(new java.awt.Color(0, 0, 0));
            MessageText.setEnabled(false);
            MessageText.setMinimumSize(new java.awt.Dimension(0, 0));

            Pannello_Tabelle.setBackground(new java.awt.Color(184, 211, 185));

            ScrollPane_TabellaPrincipale.setBackground(new java.awt.Color(204, 204, 204));
            ScrollPane_TabellaPrincipale.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            ScrollPane_TabellaPrincipale.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            ScrollPane_TabellaPrincipale.setAutoscrolls(true);
            ScrollPane_TabellaPrincipale.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            ScrollPane_TabellaPrincipale.setDoubleBuffered(true);
            ScrollPane_TabellaPrincipale.setHorizontalScrollBar(null);
            ScrollPane_TabellaPrincipale.setMinimumSize(new java.awt.Dimension(0, 0));
            ScrollPane_TabellaPrincipale.setPreferredSize(new java.awt.Dimension(454, 376));

            TabellaPrincipale.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "Cognome", "Nome", "Email primaria", "Password", "Email secondaria", "Telefono lavoro", "Telefono casa", "Telefono cellulare", "Indirizzo lavoro", "Indirizzo casa", "Employee ID", "Employee tipse", "Employee title", "Manager", "Dipartimento", "Cost center"
                }
            ) {
                boolean[] canEdit = new boolean [] {
                    true, true, false, true, true, true, true, true, true, true, true, true, true, true, false, true
                };

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            TabellaPrincipale.setDoubleBuffered(true);
            TabellaPrincipale.getTableHeader().setReorderingAllowed(false);
            TabellaPrincipale.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    TabellaPrincipaleMousePressed(evt);
                }
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    TabellaPrincipaleMouseReleased(evt);
                }
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    TabellaPrincipaleMouseClicked(evt);
                }
            });
            TabellaPrincipale.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    TabellaPrincipaleKeyPressed(evt);
                }
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    TabellaPrincipaleKeyReleased(evt);
                }
            });
            ScrollPane_TabellaPrincipale.setViewportView(TabellaPrincipale);

            TabellaFlags.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            TabellaFlags.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "Flags"
                }
            ) {
                boolean[] canEdit = new boolean [] {
                    false
                };

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            TabellaFlags.setMaximumSize(new java.awt.Dimension(45, 16));
            TabellaFlags.getTableHeader().setReorderingAllowed(false);
            ScrollPane_TabellaFlags.setViewportView(TabellaFlags);

            TabellaNumerazione.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            TabellaNumerazione.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "Numero"
                }
            ) {
                boolean[] canEdit = new boolean [] {
                    false
                };

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            TabellaNumerazione.setMaximumSize(new java.awt.Dimension(45, 16));
            TabellaNumerazione.getTableHeader().setReorderingAllowed(false);
            ScrollPane_TabellaNumerazione.setViewportView(TabellaNumerazione);

            javax.swing.GroupLayout Pannello_TabelleLayout = new javax.swing.GroupLayout(Pannello_Tabelle);
            Pannello_Tabelle.setLayout(Pannello_TabelleLayout);
            Pannello_TabelleLayout.setHorizontalGroup(
                Pannello_TabelleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pannello_TabelleLayout.createSequentialGroup()
                    .addComponent(ScrollPane_TabellaNumerazione, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(ScrollPane_TabellaPrincipale, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(ScrollPane_TabellaFlags, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
            );
            Pannello_TabelleLayout.setVerticalGroup(
                Pannello_TabelleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pannello_TabelleLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(Pannello_TabelleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(ScrollPane_TabellaPrincipale, javax.swing.GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)
                        .addComponent(ScrollPane_TabellaFlags, javax.swing.GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)
                        .addComponent(ScrollPane_TabellaNumerazione, javax.swing.GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)))
            );

            Pannello_Gif.setBackground(new java.awt.Color(102, 102, 102));
            Pannello_Gif.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            Pannello_GifApertura.setBackground(new java.awt.Color(102, 102, 102));
            Pannello_GifApertura.setMinimumSize(new java.awt.Dimension(117, 75));
            Pannello_GifApertura.setOpaque(false);

            GifPicture.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

            javax.swing.GroupLayout Pannello_GifAperturaLayout = new javax.swing.GroupLayout(Pannello_GifApertura);
            Pannello_GifApertura.setLayout(Pannello_GifAperturaLayout);
            Pannello_GifAperturaLayout.setHorizontalGroup(
                Pannello_GifAperturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pannello_GifAperturaLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(GifPicture, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );
            Pannello_GifAperturaLayout.setVerticalGroup(
                Pannello_GifAperturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(GifPicture, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );

            javax.swing.GroupLayout Pannello_GifLayout = new javax.swing.GroupLayout(Pannello_Gif);
            Pannello_Gif.setLayout(Pannello_GifLayout);
            Pannello_GifLayout.setHorizontalGroup(
                Pannello_GifLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(Pannello_GifLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pannello_GifLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(Pannello_GifApertura, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap()))
            );
            Pannello_GifLayout.setVerticalGroup(
                Pannello_GifLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 100, Short.MAX_VALUE)
                .addGroup(Pannello_GifLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pannello_GifLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(Pannello_GifApertura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
            );

            SceltaDipartimento.setMaximumRowCount(15);
            SceltaDipartimento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tutto" }));
            SceltaDipartimento.setToolTipText("Seleziona filtro");
            SceltaDipartimento.setAutoscrolls(true);
            SceltaDipartimento.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            SceltaDipartimento.setInheritsPopupMenu(true);
            SceltaDipartimento.setMaximumSize(new java.awt.Dimension(53, 22));
            SceltaDipartimento.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent evt) {
                    SceltaDipartimentoItemStateChanged(evt);
                }
            });

            DipartimentoIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/accountmanager/img/Filter-List-icon.png"))); // NOI18N

            ScrollPane_TabellaRicerca.setBackground(new java.awt.Color(0, 0, 0));
            ScrollPane_TabellaRicerca.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
            ScrollPane_TabellaRicerca.setHorizontalScrollBar(null);
            ScrollPane_TabellaRicerca.setMaximumSize(new java.awt.Dimension(250, 100));
            ScrollPane_TabellaRicerca.setMinimumSize(new java.awt.Dimension(100, 24));

            TabellaRicerca.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null, null}
                },
                new String [] {
                    "Cognome", "Nome"
                }
            ));
            TabellaRicerca.setMaximumSize(new java.awt.Dimension(250, 100));
            TabellaRicerca.setMinimumSize(new java.awt.Dimension(0, 24));
            TabellaRicerca.setPreferredSize(new java.awt.Dimension(250, 24));
            TabellaRicerca.getTableHeader().setReorderingAllowed(false);
            ScrollPane_TabellaRicerca.setViewportView(TabellaRicerca);
            if (TabellaRicerca.getColumnModel().getColumnCount() > 0) {
                TabellaRicerca.getColumnModel().getColumn(0).setHeaderValue("Cognome");
                TabellaRicerca.getColumnModel().getColumn(1).setHeaderValue("Nome");
            }

            SearchButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/accountmanager/img/lente.png"))); // NOI18N
            SearchButton.setToolTipText("Cerca un membro");
            SearchButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    SearchButtonActionPerformed(evt);
                }
            });

            BarUpdater.setForeground(new java.awt.Color(102, 255, 102));
            BarUpdater.setMinimumSize(new java.awt.Dimension(0, 0));

            InformationFileIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/accountmanager/img/file_information.png"))); // NOI18N
            InformationFileIcon.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    InformationFileIconMouseClicked(evt);
                }
            });

            javax.swing.GroupLayout Pannello_JFormLayout = new javax.swing.GroupLayout(Pannello_JForm);
            Pannello_JForm.setLayout(Pannello_JFormLayout);
            Pannello_JFormLayout.setHorizontalGroup(
                Pannello_JFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pannello_JFormLayout.createSequentialGroup()
                    .addGap(17, 17, 17)
                    .addGroup(Pannello_JFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(Pannello_StrumentiLaterale, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Pannello_Gif, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(Pannello_JFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(Pannello_Tabelle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ScrollPane_TabellaAggiungi)
                        .addComponent(PathField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(Pannello_JFormLayout.createSequentialGroup()
                            .addComponent(SfogliaButton, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(InformationFileIcon)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(GuidaIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(Pannello_JFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(BarUpdater, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(MessageText, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE))
                            .addGap(30, 30, 30)
                            .addComponent(SearchButton)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(ScrollPane_TabellaRicerca, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(31, 31, 31)
                            .addComponent(DipartimentoIcon)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(SceltaDipartimento, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap())
            );
            Pannello_JFormLayout.setVerticalGroup(
                Pannello_JFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pannello_JFormLayout.createSequentialGroup()
                    .addGroup(Pannello_JFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(Pannello_Gif, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(Pannello_JFormLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(PathField, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(Pannello_JFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(Pannello_JFormLayout.createSequentialGroup()
                                    .addGap(15, 15, 15)
                                    .addGroup(Pannello_JFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(InformationFileIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(Pannello_JFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(MessageText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(GuidaIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(SfogliaButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(Pannello_JFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(SceltaDipartimento, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(DipartimentoIcon, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pannello_JFormLayout.createSequentialGroup()
                                    .addGap(14, 14, 14)
                                    .addGroup(Pannello_JFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(ScrollPane_TabellaRicerca, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(SearchButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(BarUpdater, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(6, 6, 6)
                    .addGroup(Pannello_JFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(Pannello_JFormLayout.createSequentialGroup()
                            .addComponent(ScrollPane_TabellaAggiungi, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(Pannello_Tabelle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(Pannello_StrumentiLaterale, javax.swing.GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE))
                    .addContainerGap())
            );

            ScrollPane_JForm.setViewportView(Pannello_JForm);

            MenuBar.setBorder(null);

            FileMenu.setText("File");
            FileMenu.setToolTipText("");

            ComparaFileButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/accountmanager/img/compara_file.png"))); // NOI18N
            ComparaFileButton.setText("Compara File");
            ComparaFileButton.setToolTipText("Cerca eventuali omonimi");
            ComparaFileButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    ComparaFileButtonMouseClicked(evt);
                }
            });
            FileMenu.add(ComparaFileButton);
            FileMenu.add(jSeparator3);

            CorreggiOmonimi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/accountmanager/img/Icon_Omonimo.png"))); // NOI18N
            CorreggiOmonimi.setText("Correggi Omonimi");
            CorreggiOmonimi.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    CorreggiOmonimiMouseClicked(evt);
                }
            });
            FileMenu.add(CorreggiOmonimi);
            FileMenu.add(jSeparator6);

            SalvaButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/accountmanager/img/salva32.png"))); // NOI18N
            SalvaButton.setText("Salva");
            SalvaButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    SalvaButtonMouseClicked(evt);
                }
            });
            FileMenu.add(SalvaButton);
            FileMenu.add(jSeparator4);

            ExportFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/accountmanager/img/export-to-file-icon.png"))); // NOI18N
            ExportFile.setText("Salva con nome");
            ExportFile.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    ExportFileMouseClicked(evt);
                }
            });
            FileMenu.add(ExportFile);
            FileMenu.add(jSeparator1);

            ImportFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/accountmanager/img/Import-icon.png"))); // NOI18N
            ImportFile.setText("Importa File");
            ImportFile.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    ImportFileMouseClicked(evt);
                }
            });
            FileMenu.add(ImportFile);
            FileMenu.add(jSeparator2);

            StampaButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/accountmanager/img/icona_stampa.jpg"))); // NOI18N
            StampaButton.setText("Stampa File");
            StampaButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    StampaButtonMouseClicked(evt);
                }
            });
            FileMenu.add(StampaButton);

            MenuBar.add(FileMenu);

            GuidaMenu.setText("?");

            GuidaShortCut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/accountmanager/img/help.png"))); // NOI18N
            GuidaShortCut.setText("Guida");
            GuidaShortCut.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    GuidaShortCutActionPerformed(evt);
                }
            });
            GuidaMenu.add(GuidaShortCut);
            GuidaMenu.add(jSeparator7);

            Contattami.setIcon(new javax.swing.ImageIcon(getClass().getResource("/accountmanager/img/email_icon.png"))); // NOI18N
            Contattami.setText("Contattami");
            Contattami.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    ContattamiActionPerformed(evt);
                }
            });
            GuidaMenu.add(Contattami);
            GuidaMenu.add(jSeparator8);

            UpdateSoftware.setIcon(new javax.swing.ImageIcon(getClass().getResource("/accountmanager/img/aggiorna_icon.png"))); // NOI18N
            UpdateSoftware.setText("Cerca Aggiornamenti");
            UpdateSoftware.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    UpdateSoftwareActionPerformed(evt);
                }
            });
            GuidaMenu.add(UpdateSoftware);
            GuidaMenu.add(jSeparator9);

            Menu_Design.setIcon(new javax.swing.ImageIcon(getClass().getResource("/accountmanager/img/design_icon.png"))); // NOI18N
            Menu_Design.setText("Design");

            Design_Metal.setText("Metal");
            Design_Metal.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    Design_MetalActionPerformed(evt);
                }
            });
            Menu_Design.add(Design_Metal);

            Design_Windows.setText("Windows");
            Design_Windows.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    Design_WindowsActionPerformed(evt);
                }
            });
            Menu_Design.add(Design_Windows);

            GuidaMenu.add(Menu_Design);
            GuidaMenu.add(jSeparator10);

            Altro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/accountmanager/img/about_icon.png"))); // NOI18N
            Altro.setText("About");
            Altro.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    AltroActionPerformed(evt);
                }
            });
            GuidaMenu.add(Altro);

            MenuBar.add(GuidaMenu);

            setJMenuBar(MenuBar);

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(ScrollPane_JForm, javax.swing.GroupLayout.DEFAULT_SIZE, 1300, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(ScrollPane_JForm, javax.swing.GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE)
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void SfogliaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SfogliaButtonActionPerformed

        /*try
        {
            Desktop.getDesktop().open(new File("C:\\Users\\Samuele\\Desktop\\Samuele_Todesca_AccountManager_5IC\\dist\\accountmanager.jar"));
        }
        catch (IOException ex)
        {
            System.out.println("ERROREEEE");
        }*/

        /*String commandA = "cmd /c start C:\\Users\\Samuele\\Desktop\\collegamento.bat";
        try
        {
        Process p = Runtime.getRuntime().exec(commandA);
        }
        catch (Exception e)
        {
            System.out.println("Qualcosa è andato storto: " + e.toString());
        }*/
        Guida_veloce.dispose();
        LeggiFileButton.setEnabled(true);
        ButtonImporta.setEnabled(false);
        PannelloInformazioni.dispose();
        SceltaSalvataggio.dispose();
        CorrettoreOmonimi.dispose();
        PathFileDragDrop.setText("");
        String returnPath=fileChooser();
        FinestraDialogo.setLocationRelativeTo(null);
        if(returnPath!=null)
        {
         PathField.setText(returnPath);
         FinestraDialogo.setVisible(true);
         jScrollPane11.setVisible(false);
         FinestraDialogo.setSize(502,330);
         visualizzaAnteprima(PathField.getText(),AnteprimaText);
        }


    }//GEN-LAST:event_SfogliaButtonActionPerformed

    private void EsportaModificheButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EsportaModificheButtonActionPerformed


     IDThreadSalvaNome = new Thread()
     {
       @Override
       public void run()
       {
          /*AccountManager CHOOSER SALVA CON NOME*/
          MessageText.setText("");
          FileChooserSalvaNome.setDialogTitle("Salva con nome");
          FileChooserSalvaNome.setApproveButtonText("Salva con nome");
          FileChooserSalvaNome.setAcceptAllFileFilterUsed(false);
          FileChooserSalvaNome.setCurrentDirectory(new java.io.File(System.getProperty("user.home")+"/Desktop"));
          SceltaSalvataggio.setLocationRelativeTo(null);
          SceltaSalvataggio.setAlwaysOnTop(true);
          SceltaSalvataggio.setVisible(true);
       }
     };
     IDThreadSalvaNome.start();

    }//GEN-LAST:event_EsportaModificheButtonActionPerformed

    private void AggiungiMembroButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AggiungiMembroButtonActionPerformed

        TabellaAggiungi.setVisible(true);
        CheckRadioPasswordRandom.setEnabled(true);
        CheckRadioEmail.setEnabled(true);

        if((SceltaDipartimento.getSelectedItem().equals(" Tutto")||SceltaDipartimento.getSelectedItem().equals("Tutto"))&&((!PathField.getText().equals(""))))
         EliminaModifiche.setEnabled(true);
        else
         EliminaModifiche.setEnabled(false);

        MessageText.setText("");
        DefaultTableModel table1 = (DefaultTableModel) TabellaPrincipale.getModel();
        DefaultTableModel table2 = (DefaultTableModel) TabellaAggiungi.getModel();
        DefaultTableModel tableFlags = (DefaultTableModel) TabellaFlags.getModel();
        int riga=0,posizione=0,ricordaPosizione=0,ricordaPosizione2=0,contatoreAggiunti=0,righeTable=0,rigaCancellata=0,verifica=0,i; //coordinate dove la nuova riga andrà
        Boolean controllo=false,controllo2=false;
        String nome=null,cognomi=null,classe=null,item=null;
        ArrayList listaCognomi = new ArrayList(0);

        righeTable=table1.getRowCount();

        nome = (String) table2.getValueAt(0,1);
        cognomi = (String) table2.getValueAt(0,0);
        classe = (String) table2.getValueAt(0,14);
        item = (String) SceltaDipartimento.getSelectedItem();


        if((""!=table2.getValueAt(0,0))||(""!=table2.getValueAt(0,1))||(""!=table2.getValueAt(0,2))||(""!=table2.getValueAt(0,3))||(""!=table2.getValueAt(0,4))||
           (""!=table2.getValueAt(0,5))||(""!=table2.getValueAt(0,6))||(""!=table2.getValueAt(0,7))||(""!=table2.getValueAt(0,8))||(""!=table2.getValueAt(0,9))||
           (""!=table2.getValueAt(0,10))||(""!=table2.getValueAt(0,11))||(""!=table2.getValueAt(0,12))||(""!=table2.getValueAt(0,13))||(""!=table2.getValueAt(0,14))||
           (""!=table2.getValueAt(0,15)))
        {

        try
        {
         nome=nome.trim().replaceAll(" {2,}", " ");
         cognomi=cognomi.trim().replaceAll(" {2,}", " ");
         classe=classe.trim().replaceAll(" {2,}", " ");

         if(CheckRadioPasswordRandom.isSelected())
            table2.setValueAt(randomString(8),0,3);

         /*VERIFICO SE IL MEMBRO AGGIUNTO FA PARTE DI UNA CLASSE DIVERSA CON FILTRI ATTIVATI*/
         for(riga=0;table1.getRowCount()!=riga;riga++)
         {
          if(!((classe.toUpperCase()+"   ").equals(table1.getValueAt(riga,14)))&&!((classe.toUpperCase()).equals(table1.getValueAt(riga,14))))
          {
            controllo2=true; //il membro aggiunto fa parte di una classe diversa da quella filtrata
            break;
          }
         }



         if(((!(item.equals(" Tutto")))&&controllo2==false)||(item.equals(" Tutto")))
         {

            /*CONTROLLO DOPPIONI TABLE1 TABLE2*/
            for(i=0;i!=tableFlags.getRowCount();i++)
             if(tableFlags.getValueAt(i,0).equals("ESISTE"))
              {
                tableFlags.setValueAt("AGGIUNTO",i,0);
              }

            for(i=0;i!=table1.getRowCount();i++)
             if((table1.getValueAt(i,0).equals(cognomi.toUpperCase()))&&(table1.getValueAt(i,1).equals(nome.toUpperCase())))
              {
                tableFlags.setValueAt("ESISTE",i,0);
                TabellaPrincipale.setRowSelectionInterval(i, i);
                TabellaPrincipale.scrollRectToVisible(TabellaPrincipale.getCellRect(i,0,true));
                controllo=true;
              }


            if(controllo==false) //se esiste un doppione non eseguo l'operazione
            {
                if((0==cognomi.length())||(0==nome.length()))  //verifico se i campi citati hanno contenuto vuoto nel caso si dasse invio senza inserire nulla
                {
                 throw new NullPointerException();
                }

                /*AGGIUNGO LA NUOVA RIGA IN FONDO ALLA TABELLA PRINCIPALE*/
                 table1.addRow(new Object[]{
                    cognomi.toUpperCase(),nome.toUpperCase(),cognomi.toLowerCase().replace(" ","").replace("'","")+""+"."+nome.toLowerCase().replace(" ","").replace("'","")+"@studenti-ittfedifermi.gov.it",table2.getValueAt(0,3),table2.getValueAt(0,4),table2.getValueAt(0,5),
                    table2.getValueAt(0,6),table2.getValueAt(0,7),table2.getValueAt(0,8),table2.getValueAt(0,9),table2.getValueAt(0,10),table2.getValueAt(0,11),
                    table2.getValueAt(0,12),table2.getValueAt(0,13),(classe.toUpperCase()),table2.getValueAt(0,15)
                 }); //aggiungo in fondo alla tabella la riga compilata


                /*SEGNALO L'AGGIUNTA DELLA NUOVA RIGA*/
                tableFlags.addRow(new Object[]{"AGGIUNTO"});//il flag come la nuova riga si trovano inizialmente in fondo alla tabella

                /*AGGIORNO IL CONTATORE DEGLI ELEMENTI AGGIUNTI*/
                for(i=0;i!=table1.getRowCount();i++)
                {
                 if(tableFlags.getValueAt(i,0).equals("AGGIUNTO"))
                 {
                  contatoreAggiunti++;
                  contaAggiuntiManuale.setText(""+contatoreAggiunti);
                 }
                }

                /*CERCO LA CLASSE*/
                for(riga=0;riga!=table1.getRowCount()-1;riga++) //visito tutte le righe relativamente al campo classe
                {
                    if(table1.getValueAt(riga,14).equals((classe.toUpperCase()))) //cerco la classe
                    {
                     table1.moveRow(table1.getRowCount()-1,table1.getRowCount()-1,riga); //inserisco la nuova riga compilata alla classe corretta in cima alla lista dei nomi
                     tableFlags.moveRow(table1.getRowCount()-1,table1.getRowCount()-1,riga);
                     break;
                    }
                }// se arrivo in fondo al ciclo senza aver mosso la riga significa che non vi è nessuna classe corrispondente

                /*ORDINO IL NOME RISPETTO ALLA CLASSE*/
                if(riga!=table1.getRowCount()-1) //non eseguo le operazioni sottostanti nel caso non ci fosse la classe inserita nella lista e la nuova riga rimane in fondo alla lista
                {
                 try
                 {
                    ricordaPosizione=riga; //prima riga in cui si presenta la classe trovata
                    ricordaPosizione2=riga;//prima riga in cui si presenta la classe trovata
                    for(posizione=riga;table1.getValueAt(posizione,14).equals(classe.toUpperCase())||table1.getValueAt(posizione,14).equals(classe.toUpperCase());posizione++)
                    {
                        listaCognomi.add(table1.getValueAt(posizione,0)); //inserisco tuti i cognomi della classe scelta in ArrayList cosi da poter determinare il numero di alunni (compreso quello nuovo)
                        if(table1.getRowCount()==posizione+1)
                          break;
                    }
                    String[] cognome= new String[listaCognomi.size()]; //ora che so quanti alunni ci sono nella classe determino la dimensione dell'array che conterrà tutti i cognomi della classe

                    for(i=0;i!=listaCognomi.size();i++)
                    {
                        cognome[i]=(String) table1.getValueAt(ricordaPosizione,0); //trascrivo tutti i cognomi dentro l'array
                        ricordaPosizione++;
                    }

                    Arrays.sort(cognome); //ordino l'array contenente il campo cognomi presi da ArrayList
                    for(i=0;i!=listaCognomi.size();i++)
                    {
                        if(cognome[i].equals(cognomi.toUpperCase())) //cerco la posizione, dentro l'array ordinato, del nuovo alunno e sommo la posizione dell'array con quello della posizione della prima riga della classe scelta
                        {
                         ricordaPosizione2=ricordaPosizione2+i;
                         break;
                        }
                    }
                    table1.moveRow(riga,riga,ricordaPosizione2); //inserisco i campi nel giusto ordine alfabetico dei nomi della classe scelta
                    tableFlags.moveRow(riga,riga,ricordaPosizione2);
                    TabellaPrincipale.scrollRectToVisible(TabellaPrincipale.getCellRect(ricordaPosizione2,0,true));
                    ordinaNomiDoppioni(table1,ricordaPosizione2);
                 }
                 catch(ArrayIndexOutOfBoundsException e)
                 {}
                }
                TickGif();
                if(item.equals("Tutto"))
                 verifica=inizializzazioneTabellaSupporto(righeTable,rigaCancellata,0); //con zero vado nella parte di aggiornamento senza filtri
                else
                 verifica=inizializzazioneTabellaSupporto(righeTable,rigaCancellata,1); //con uno vado nella parte di aggiornamento per i filtri

                if(verifica==0) //mantiene gli items se si aggiunge o si elimina un membro in una classe filtrata
                    creaItems();
                contaMembri();

                if(PathField.getText().equals("")||PathField.getText().equals(null))
                  SalvaButton.setEnabled(false);
                else
                  SalvaButton.setEnabled(true);
             }
             else
             {
                MessageText.setText("Questo nominativo è gia esistente");
                allarmGif();
             }
        }
        else
        {
            MessageText.setText("Non è possibile aggiungere membri appartenenti ad altri dipartimenti");
            allarmGif();
        }
        }
        catch(NullPointerException e)
        {
           MessageText.setText("Inserire almeno nome e cognome");
           allarmGif();
        }
        }


    }//GEN-LAST:event_AggiungiMembroButtonActionPerformed

    private void CancellaRigaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancellaRigaButtonActionPerformed

     IDThreadCancellaRighe = new Thread()
     {
       @Override
       public void run()
       {
        DefaultTableModel table = null;
        DefaultTableModel tableFlags = null;
        DefaultTableModel tableNumerazione = null;
        int righeTable=0,controllo=0,riga=0,tipoRimozione=0,valore=0,i,c;
        int rowsSelected[] = null;
        boolean rigaSelezionata=true;
        try
        {
            MessageText.setText("");
            table = (DefaultTableModel) TabellaPrincipale.getModel();
            tableFlags = (DefaultTableModel) TabellaFlags.getModel();
            tableNumerazione = (DefaultTableModel) TabellaNumerazione.getModel();
            DefaultTableModel tableSupport = (DefaultTableModel) jTable4.getModel();
            righeTable=table.getRowCount();

            rowsSelected = TabellaPrincipale.getSelectedRows();
            if(rowsSelected.length==0)
                throw new ArrayIndexOutOfBoundsException();

            while(rigaSelezionata==true)
            {
                  rigaSelezionata=false;
                /*ESECUZIONE TEST PER CAPIRE LA TIPOLOGIA DI RIMOZIONE*/
                try
                {
                 for(i=0;i!=rowsSelected.length;i++)
                  if(rowsSelected[i+1]-rowsSelected[i]!=1) //verifico se la selezione delle righe è contigua, se falso utilizzo la rimozione singola, se vero uso la rimozione di gruppo
                  {
                    tipoRimozione=1;
                    break;
                  }
                }
                catch(ArrayIndexOutOfBoundsException e)
                {}

                /*RIMOZIONE SINGOLA*/
                if(tipoRimozione==1)
                {

                     if(tableFlags.getValueAt(TabellaPrincipale.getSelectedRow(),0).equals("NEW")||tableFlags.getValueAt(TabellaPrincipale.getSelectedRow(),0).equals("NEW(omonimo)"))
                     {
                       valore=Integer.parseInt(contaAggiunti.getText())-1;
                       contaAggiunti.setText(""+valore);
                       CampoMembriImportati.setText(""+valore);
                     }
                     if(tableFlags.getValueAt(TabellaPrincipale.getSelectedRow(),0).equals("AGGIUNTO")||tableFlags.getValueAt(TabellaPrincipale.getSelectedRow(),0).equals("ESISTE"))
                     {
                       valore=Integer.parseInt(contaAggiuntiManuale.getText())-1;
                       contaAggiuntiManuale.setText(""+valore);
                     }
                     if(tableFlags.getValueAt(TabellaPrincipale.getSelectedRow(),0).equals("OLD")||tableFlags.getValueAt(TabellaPrincipale.getSelectedRow(),0).equals("OLD(omonimo)"))
                     {
                       valore=Integer.parseInt(contaMembri.getText())-1;
                       contaMembri.setText(""+valore);
                     }
                     if(tableFlags.getValueAt(TabellaPrincipale.getSelectedRow(),0).equals("OLD(omonimo)")||tableFlags.getValueAt(TabellaPrincipale.getSelectedRow(),0).equals("NEW(omonimo)"))
                     {
                       valore=Integer.parseInt(contaEsistenti.getText())-1;
                       contaEsistenti.setText(""+valore);
                       CampoTotaleOmonimi.setText(""+valore);
                     }
                     if(tableFlags.getValueAt(TabellaPrincipale.getSelectedRow(),0).equals("OLD(omonimo)"))
                     {
                       valore=Integer.parseInt(CampoBaseOmonimi.getText())-1;
                       CampoBaseOmonimi.setText(""+valore);
                     }
                     if(tableFlags.getValueAt(TabellaPrincipale.getSelectedRow(),0).equals("NEW(omonimo)"))
                     {
                       valore=Integer.parseInt(CampoImportatiOmonimi.getText())-1;
                       CampoImportatiOmonimi.setText(""+valore);
                     }
                     if(table.getValueAt(TabellaPrincipale.getSelectedRow(),14).equals(""))
                     {
                       valore=Integer.parseInt(CampoNoDipartimento.getText())-1;
                       CampoNoDipartimento.setText(""+valore);
                     }
                     tableFlags.removeRow(TabellaPrincipale.getSelectedRow());
                     tableNumerazione.removeRow(TabellaPrincipale.getSelectedRow());

                    /*Operazione che consente di cercare sulla tabella di supporto la riga da eliminare scelta nella tabella principale*/
                    for(riga=0;riga!=tableSupport.getRowCount();riga++)
                     if(table.getValueAt(TabellaPrincipale.getSelectedRow(),14).equals(tableSupport.getValueAt(riga,14))&&
                        table.getValueAt(TabellaPrincipale.getSelectedRow(),0).equals(tableSupport.getValueAt(riga,0))&&
                        table.getValueAt(TabellaPrincipale.getSelectedRow(),1).equals(tableSupport.getValueAt(riga,1)))
                         break;


                     table.removeRow(TabellaPrincipale.getSelectedRow());
                     contaMembri();
                     controllo=inizializzazioneTabellaSupporto(righeTable,riga,1);

                }
                /*RIMOZIONE DI GRUPPO*/
                if(tipoRimozione==0)
                {
                    for(i=0;i!=rowsSelected.length;i++)
                    {
                     if(tableFlags.getValueAt(rowsSelected[0],0).equals("NEW")||tableFlags.getValueAt(TabellaPrincipale.getSelectedRow(),0).equals("NEW(omonimo)"))
                     {
                       valore=Integer.parseInt(contaAggiunti.getText())-1;
                       contaAggiunti.setText(""+valore);
                       CampoMembriImportati.setText(""+valore);
                     }
                     if(tableFlags.getValueAt(rowsSelected[0],0).equals("AGGIUNTO")||tableFlags.getValueAt(TabellaPrincipale.getSelectedRow(),0).equals("ESISTE"))
                     {
                       valore=Integer.parseInt(contaAggiuntiManuale.getText())-1;
                       contaAggiuntiManuale.setText(""+valore);
                     }
                     if(tableFlags.getValueAt(rowsSelected[0],0).equals("OLD")||tableFlags.getValueAt(TabellaPrincipale.getSelectedRow(),0).equals("OLD(omonimo)"))
                     {
                       valore=Integer.parseInt(contaMembri.getText())-1;
                       contaMembri.setText(""+valore);
                     }
                     if(tableFlags.getValueAt(rowsSelected[0],0).equals("OLD(omonimo)")||tableFlags.getValueAt(TabellaPrincipale.getSelectedRow(),0).equals("NEW(omonimo)"))
                     {
                       valore=Integer.parseInt(contaEsistenti.getText())-1;
                       contaEsistenti.setText(""+valore);
                       CampoTotaleOmonimi.setText(""+valore);
                     }
                     if(tableFlags.getValueAt(rowsSelected[0],0).equals("OLD(omonimo)"))
                     {
                       valore=Integer.parseInt(CampoBaseOmonimi.getText())-1;
                       CampoBaseOmonimi.setText(""+valore);
                     }
                     if(tableFlags.getValueAt(rowsSelected[0],0).equals("NEW(omonimo)"))
                     {
                       valore=Integer.parseInt(CampoImportatiOmonimi.getText())-1;
                       CampoImportatiOmonimi.setText(""+valore);
                     }
                     if(table.getValueAt(rowsSelected[0],14).equals("")&&Integer.parseInt(CampoNoDipartimento.getText())>0)
                     {
                       valore=Integer.parseInt(CampoNoDipartimento.getText())-1;
                       CampoNoDipartimento.setText(""+valore);
                     }

                     tableFlags.removeRow(rowsSelected[0]);
                     tableNumerazione.removeRow(rowsSelected[0]);
                    }

                    /*Operazione che consente di cercare sulla tabella di supporto la riga da eliminare scelta nella tabella principale*/
                    for(riga=0;riga!=tableSupport.getRowCount();riga++)
                     if(table.getValueAt(TabellaPrincipale.getSelectedRow(),14).equals(tableSupport.getValueAt(riga,14))&&
                        table.getValueAt(TabellaPrincipale.getSelectedRow(),0).equals(tableSupport.getValueAt(riga,0))&&
                        table.getValueAt(TabellaPrincipale.getSelectedRow(),1).equals(tableSupport.getValueAt(riga,1)))
                         break;

                    /*Se il filtro è su tutto allora posso cancellare partendo dalla prima riga selezionata*/
                    if(SceltaDipartimento.getSelectedItem().equals(" Tutto")||SceltaDipartimento.getSelectedItem().equals("Tutto"))
                    {
                        for(i=0;i!=rowsSelected.length;i++)
                        {
                         table.removeRow(rowsSelected[0]);
                         controllo=inizializzazioneTabellaSupporto(righeTable,rowsSelected[0],1);
                        }
                    }
                    /*Se il filtro non è su tutto allora cancello partendo dalla prima riga in cui si presenta la classe filtrata + la posizione della riga selezionata*/
                    else
                    {
                        for(c=0;tableSupport.getRowCount()!=c;c++)
                          if(table.getValueAt(TabellaPrincipale.getSelectedRow(),14).equals(tableSupport.getValueAt(c,14)))
                            break;

                        for(i=0;i!=rowsSelected.length;i++)
                        {
                         table.removeRow(rowsSelected[0]);
                         controllo=inizializzazioneTabellaSupporto(righeTable,rowsSelected[0]+c,1);
                        }
                    }
                    contaMembri();
                }

                for(i=0;table.getRowCount()!=i;i++)
                {
                    if(TabellaPrincipale.isRowSelected(i))
                     rigaSelezionata=true;
                }
            }

            SalvaButton.setEnabled(true);
             if(controllo==0&&table.getRowCount()!=0) //mantiene gli items se la tabella diventa vuota cancellando le righe
              creaItems();
             if(table.getRowCount()==0)
              CheckRadioEmail.setEnabled(false);
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
         MessageText.setText("Selezionare una o più righe che si vuole eliminare");
         allarmGif();
        }
      }
     };
     IDThreadCancellaRighe.start();
    }//GEN-LAST:event_CancellaRigaButtonActionPerformed

    private void PulisciTabellaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PulisciTabellaButtonActionPerformed

     IDThreadCancellaTabella = new Thread()
     {
       @Override
       public void run()
       {
        DefaultTableModel table = (DefaultTableModel) TabellaPrincipale.getModel();
        DefaultTableModel tableFlags = (DefaultTableModel) TabellaFlags.getModel();
        DefaultTableModel tabellaFile = (DefaultTableModel) TabellaFile.getModel();
        DefaultTableModel tableOmonimi = (DefaultTableModel) TabellaOmonimi.getModel();
        DefaultTableModel tableNumerazione = (DefaultTableModel) TabellaNumerazione.getModel();

        EliminaModifiche.setEnabled(false);
        CheckRadioEmail.setSelected(false);
        CheckRadioEmail.setEnabled(false);
        MessageText.setText("");
        PathField.setText("");
        contaAggiunti.setText("0");
        contaEsistenti.setText("0");
        CampoBaseOmonimi.setText("0");
        CampoImportatiOmonimi.setText("0");
        CampoMembriImportati.setText("0");
        CampoNumeroFiles.setText("0");
        CampoNoDipartimento.setText("0");
        CampoNumeroDipartimenti.setText("0");
        contaAggiuntiManuale.setText("0");
        contaSenzaDipartimento=0;
        AnteprimaFileImportato.setText("");
        TextFileBase.setText("");
        PathFileDragDrop.setText("");
        int righeTable=0,rigaCancellata=0;

        if(table.getRowCount()!=0)
        {
         while (table.getRowCount()>0)
            table.removeRow(0);

         while (tableOmonimi.getRowCount()>0)
            tableOmonimi.removeRow(0);

         while (tableNumerazione.getRowCount()>0)
            tableNumerazione.removeRow(0);

         while (tableFlags.getRowCount()>0)
           tableFlags.removeRow(0);

         while(tabellaFile.getRowCount()>0)
           tabellaFile.removeRow(0);

         contaMembri();
         inizializzazioneTabellaSupporto(righeTable,rigaCancellata,0);
         creaItems();
         SalvaButton.setEnabled(false);
        }
        else
        {
         MessageText.setText("La tabella è vuota");
         allarmGif();
        }
       }
     };
     IDThreadCancellaTabella.start();
    }//GEN-LAST:event_PulisciTabellaButtonActionPerformed

    private void GeneraPasswordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GeneraPasswordButtonActionPerformed

     IDThreadGeneraPassword = new Thread()
     {
       @Override
       public void run()
       {
        DefaultTableModel table = null;
        int rigaCancellata=0;
        boolean rigaSelezionata=false;
        try
        {
            SalvaButton.setEnabled(true);
            table = (DefaultTableModel) TabellaPrincipale.getModel();
            int rowsSelected[] = null,i;
            MessageText.setText("");
            TabellaAggiungi.clearSelection();
            rowsSelected = TabellaPrincipale.getSelectedRows();
            for(i=0;i!=rowsSelected.length;i++)
            {
             table.setValueAt(randomString(8),rowsSelected[i],3);
             //TabellaPrincipale.clearSelection();
            }
            inizializzazioneTabellaSupporto(table.getRowCount(),rigaCancellata,1);

            for(i=0;table.getRowCount()!=i;i++)
            {
             if(TabellaPrincipale.isRowSelected(i))
               rigaSelezionata=true;
            }
            TabellaPrincipale.clearSelection();
            if(rigaSelezionata==false)
            {
                throw new NullPointerException();
            }
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
         rigaCancellata=0;
         inizializzazioneTabellaSupporto(table.getRowCount(),rigaCancellata,1);
        }
        catch(NullPointerException e)
        {
         MessageText.setText("Selezionare la riga o gruppo di righe a cui si vuole assegnare una password");
         allarmGif();
        }
       }
     };
     IDThreadGeneraPassword.start();
    }//GEN-LAST:event_GeneraPasswordButtonActionPerformed

    private void GuidaIconMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_GuidaIconMouseClicked

        Guida.dispose();
        initUI();
        ButtonSlideRight.setVisible(false);
        ButtonSlideLeft.setVisible(false);
        SlidePanel.setVisible(false);
        Guida.setSize(990,370);
        Panel_Guida.setSize(990,370);
        NumberSlide.setVisible(false);
        OptionSlide.setVisible(false);
    }//GEN-LAST:event_GuidaIconMouseClicked

    private void ImportFileMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ImportFileMouseClicked

    Guida_veloce.dispose();
    LeggiFileButton.setEnabled(false);
    ButtonImporta.setEnabled(true);
    FileMenu.setPopupMenuVisible(false);
    if(SceltaDipartimento.getSelectedItem().equals(" Tutto")||SceltaDipartimento.getSelectedItem().equals("Tutto")) //non è possibile importare file su ua tabella con filtri attivati
    {
     if(TabellaPrincipale.getRowCount()!=0)//non è possibile importare file su ua tabella vuota
     {
     PannelloInformazioni.dispose();
     SceltaSalvataggio.dispose();
     CorrettoreOmonimi.dispose();
     PathFileDragDrop.setText("");
     PathFileImport=fileChooser();
     FinestraDialogo.setLocationRelativeTo(null);
        if(PathFileImport!=null)
        {
         FinestraDialogo.setVisible(true);
         jScrollPane11.setVisible(false);
         FinestraDialogo.setSize(502,330);
         visualizzaAnteprima(PathFileImport,AnteprimaText);
        }
     }
     else
     {
      MessageText.setText("Un file non può essere importato su una tabella vuota");
      allarmGif();
     }
    }
    else
    {
      MessageText.setText("Un file non può essere importato su una tabella con filtri attivati");
      allarmGif();
    }
    }//GEN-LAST:event_ImportFileMouseClicked

    private void ExportFileMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ExportFileMouseClicked
      FileMenu.setPopupMenuVisible(false);
      EsportaModificheButton.doClick();
    }//GEN-LAST:event_ExportFileMouseClicked

    private void AltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AltroActionPerformed
        GuidaMenu.setPopupMenuVisible(false);
        AboutFrame.setLocationRelativeTo(null);
        AboutFrame.setVisible(true);
    }//GEN-LAST:event_AltroActionPerformed

    private void GuidaShortCutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GuidaShortCutActionPerformed
        GuidaMenu.setPopupMenuVisible(false);
        initUI();
        ButtonSlideRight.setVisible(false);
        ButtonSlideLeft.setVisible(false);
        SlidePanel.setVisible(false);
        Guida.setSize(990,370);
        Panel_Guida.setSize(990,370);
    }//GEN-LAST:event_GuidaShortCutActionPerformed

    private void CheckTabulazioneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckTabulazioneActionPerformed

     if(CheckTabulazione.isSelected())
     {
        CheckTabulazione.setSelected(true);
        CheckSpazio.setSelected(false);
        CheckPuntoVirgola.setSelected(false);
        CheckVirgola.setSelected(false);
        CheckSlash.setSelected(false);
        CheckUnderscore.setSelected(false);
        CheckHash.setSelected(false);
        CheckAltro.setSelected(false);
        TextFieldAltro.setText("\t");
        TextFieldAltro.setEnabled(false);
        TextFieldAltro.requestFocus(false);
     }
    }//GEN-LAST:event_CheckTabulazioneActionPerformed

    private void CheckVirgolaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckVirgolaActionPerformed

     if(CheckVirgola.isSelected())
     {
         CheckVirgola.setSelected(true);
         CheckSpazio.setSelected(false);
         CheckPuntoVirgola.setSelected(false);
         CheckTabulazione.setSelected(false);
         CheckSlash.setSelected(false);
         CheckUnderscore.setSelected(false);
         CheckHash.setSelected(false);
         CheckSpazio.setSelected(false);
         CheckAltro.setSelected(false);
         TextFieldAltro.setText(",");
         TextFieldAltro.setEnabled(false);
         TextFieldAltro.requestFocus(false);
     }
    }//GEN-LAST:event_CheckVirgolaActionPerformed

    private void CheckPuntoVirgolaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckPuntoVirgolaActionPerformed

     if(CheckPuntoVirgola.isSelected())
     {
        CheckPuntoVirgola.setSelected(true);
        CheckSpazio.setSelected(false);
        CheckTabulazione.setSelected(false);
        CheckVirgola.setSelected(false);
        CheckSlash.setSelected(false);
        CheckUnderscore.setSelected(false);
        CheckHash.setSelected(false);
        CheckSpazio.setSelected(false);
        CheckAltro.setSelected(false);
        TextFieldAltro.setText(";");
        TextFieldAltro.setEnabled(false);
        TextFieldAltro.requestFocus(false);
     }
    }//GEN-LAST:event_CheckPuntoVirgolaActionPerformed

    private void CheckSlashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckSlashActionPerformed

     if(CheckSlash.isSelected())
     {
        CheckSlash.setSelected(true);
        CheckSpazio.setSelected(false);
        CheckPuntoVirgola.setSelected(false);
        CheckVirgola.setSelected(false);
        CheckTabulazione.setSelected(false);
        CheckUnderscore.setSelected(false);
        CheckHash.setSelected(false);
        CheckSpazio.setSelected(false);
        CheckAltro.setSelected(false);
        TextFieldAltro.setText("/");
        TextFieldAltro.setEnabled(false);
        TextFieldAltro.requestFocus(false);
     }
    }//GEN-LAST:event_CheckSlashActionPerformed

    private void CheckUnderscoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckUnderscoreActionPerformed

     if(CheckUnderscore.isSelected())
     {
        CheckUnderscore.setSelected(true);
        CheckSpazio.setSelected(false);
        CheckPuntoVirgola.setSelected(false);
        CheckVirgola.setSelected(false);
        CheckSlash.setSelected(false);
        CheckTabulazione.setSelected(false);
        CheckHash.setSelected(false);
        CheckSpazio.setSelected(false);
        CheckAltro.setSelected(false);
        TextFieldAltro.setText("_");
        TextFieldAltro.setEnabled(false);
        TextFieldAltro.requestFocus(false);
     }
    }//GEN-LAST:event_CheckUnderscoreActionPerformed

    private void CheckHashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckHashActionPerformed

     if(CheckHash.isSelected())
     {
        CheckHash.setSelected(true);
        CheckSpazio.setSelected(false);
        CheckPuntoVirgola.setSelected(false);
        CheckVirgola.setSelected(false);
        CheckSlash.setSelected(false);
        CheckUnderscore.setSelected(false);
        CheckTabulazione.setSelected(false);
        CheckSpazio.setSelected(false);
        CheckAltro.setSelected(false);
        TextFieldAltro.setText("#");
        TextFieldAltro.setEnabled(false);
        TextFieldAltro.requestFocus(false);
     }
    }//GEN-LAST:event_CheckHashActionPerformed

    private void CheckSpazioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckSpazioActionPerformed

     if(CheckSpazio.isSelected())
     {
        CheckSpazio.setSelected(true);
        CheckTabulazione.setSelected(false);
        CheckPuntoVirgola.setSelected(false);
        CheckVirgola.setSelected(false);
        CheckSlash.setSelected(false);
        CheckUnderscore.setSelected(false);
        CheckHash.setSelected(false);
        CheckAltro.setSelected(false);
        TextFieldAltro.setText(" ");
        TextFieldAltro.setEnabled(false);
        TextFieldAltro.requestFocus(false);
     }
    }//GEN-LAST:event_CheckSpazioActionPerformed

    private void CheckAltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckAltroActionPerformed

     if(CheckAltro.isSelected())
     {
        CheckAltro.setSelected(true);
        CheckSpazio.setSelected(false);
        CheckPuntoVirgola.setSelected(false);
        CheckVirgola.setSelected(false);
        CheckSlash.setSelected(false);
        CheckUnderscore.setSelected(false);
        CheckTabulazione.setSelected(false);
        CheckSpazio.setSelected(false);
        CheckHash.setSelected(false);
        TextFieldAltro.setText("");
        TextFieldAltro.setEnabled(true);
        TextFieldAltro.requestFocus(true);
     }
    }//GEN-LAST:event_CheckAltroActionPerformed

    private void LeggiFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LeggiFileButtonActionPerformed

      this.fileReader();
    }//GEN-LAST:event_LeggiFileButtonActionPerformed

    private void TabellaPrincipaleMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabellaPrincipaleMousePressed

     Thread x = new Thread()
     {
       @Override
       public void run()
       {
        DefaultTableModel table = (DefaultTableModel) TabellaPrincipale.getModel();
        String nome=null,cognomi=null,flag=null,flagPrima;
        int righeTable=table.getRowCount(),i,controllo=0;
        inizializzazioneTabellaSupporto(righeTable,0,1);
        int rowsSelected[] = TabellaPrincipale.getSelectedRows();
        SalvaButton.setEnabled(true);


         /*if (CTRL==true||rowsSelected.length>1)
         {
          for(i=0;rowsSelected.length!=i;i++)
          {
            TabellaFlags.addRowSelectionInterval(rowsSelected[i],rowsSelected[i]);
            TabellaNumerazione.addRowSelectionInterval(rowsSelected[i],rowsSelected[i]);
          }
         }
         else
         {
           TabellaFlags.clearSelection();
           TabellaNumerazione.clearSelection();
         }*/
        /*VERIFICO EMAIL CORRENTE SELEZIONATA*/
        if(CheckRadioEmail.isSelected())
        {
            //if(TabellaPrincipale.isColumnSelected(0)||TabellaPrincipale.isColumnSelected(1))
                nome = (String) table.getValueAt(TabellaPrincipale.getSelectedRow(),1);
                cognomi = (String) table.getValueAt(TabellaPrincipale.getSelectedRow(),0);
                table.setValueAt(cognomi.toLowerCase().replace(" ",".").replace("'","")+"."+nome.toLowerCase().replace(" ",".").replace("'","")+"@studenti-ittfedifermi.gov.it",TabellaPrincipale.getSelectedRow(),2);
        }

        /*VERIFICO CORRENTE RIGA SELEZIONATA*/
        for(i=0;TabellaPrincipale.getRowCount()!=i;i++)
            if(i!=TabellaPrincipale.getSelectedRow())
            {
             if(TabellaPrincipale.getValueAt(i,0).equals(TabellaPrincipale.getValueAt(TabellaPrincipale.getSelectedRow(),0))&&TabellaPrincipale.getValueAt(i,1).equals(TabellaPrincipale.getValueAt(TabellaPrincipale.getSelectedRow(),1)))
             {
              controllo=1;
              break;
             }
            }
        if(controllo==0)
        {
         flag=(String) TabellaFlags.getValueAt(TabellaPrincipale.getSelectedRow(),0);
         flagPrima=flag;
         flag=flag.replace("(omonimo)","");
         TabellaFlags.setValueAt(flag,TabellaPrincipale.getSelectedRow(),0);

         /*contatori aggiornamento*/
         if(flagPrima!=flag)
         {
          contaEsistenti.setText(""+(Integer.parseInt(contaEsistenti.getText())-1));
          CampoTotaleOmonimi.setText(contaEsistenti.getText());
          if(flag.contains("OLD"))
           CampoBaseOmonimi.setText(""+(Integer.parseInt(CampoBaseOmonimi.getText())-1));
          if(flag.contains("NEW"))
           CampoImportatiOmonimi.setText(""+(Integer.parseInt(CampoImportatiOmonimi.getText())-1));
         }
        }
        else
        {
         flag=(String) TabellaFlags.getValueAt(i,0);
         if(!flag.contains("(omonimo)"))
          TabellaFlags.setValueAt(flag+"(omonimo)",TabellaPrincipale.getSelectedRow(),0);
        }

        /*VERIFICO EMAIL PRECEDENTEMENTE SELEZIONATA*/
        if(CheckRadioEmail.isSelected())
        {
            //if(TabellaPrincipale.isColumnSelected(0)||TabellaPrincipale.isColumnSelected(1))
            nome = (String) table.getValueAt(PosizioneUltimaRiga,1);
            cognomi = (String) table.getValueAt(PosizioneUltimaRiga,0);
            table.setValueAt(cognomi.toLowerCase().replace(" ",".").replace("'","")+"."+nome.toLowerCase().replace(" ",".").replace("'","")+"@studenti-ittfedifermi.gov.it",PosizioneUltimaRiga,2);
        }

        /*VERIFICO PRECEDENTE RIGA SELEZIONATA*/
        if(PosizioneUltimaRiga!=-1)
        {
            controllo=0;
            for(i=0;TabellaPrincipale.getRowCount()!=i;i++)
                if(i!=PosizioneUltimaRiga)
                {
                 if(TabellaPrincipale.getValueAt(i,0).equals(TabellaPrincipale.getValueAt(PosizioneUltimaRiga,0))&&TabellaPrincipale.getValueAt(i,1).equals(TabellaPrincipale.getValueAt(PosizioneUltimaRiga,1)))
                 {
                  controllo=1;
                  break;
                 }
                }
            if(controllo==0)
            {
             flag=(String) TabellaFlags.getValueAt(PosizioneUltimaRiga,0);
             flagPrima=flag;
             flag=flag.replace("(omonimo)","");
             TabellaFlags.setValueAt(flag.replace("(omonimo)",""),PosizioneUltimaRiga,0);

             /*contatori aggiornamento*/
             if(flagPrima!=flag)
             {
              contaEsistenti.setText(""+(Integer.parseInt(contaEsistenti.getText())-1));
              CampoTotaleOmonimi.setText(contaEsistenti.getText());
              if(flag.contains("OLD"))
               CampoBaseOmonimi.setText(""+(Integer.parseInt(CampoBaseOmonimi.getText())-1));
              if(flag.contains("NEW"))
               CampoImportatiOmonimi.setText(""+(Integer.parseInt(CampoImportatiOmonimi.getText())-1));
             }
            }
            else
            {
             flag=(String) TabellaFlags.getValueAt(i,0);
             if(!flag.contains("(omonimo)"))
                TabellaFlags.setValueAt(flag+"(omonimo)",PosizioneUltimaRiga,0);
            }
        }
        PosizioneUltimaRiga=TabellaPrincipale.getSelectedRow();
       }
     };
     x.start();
    }//GEN-LAST:event_TabellaPrincipaleMousePressed

    private void EliminaModificheActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EliminaModificheActionPerformed

      this.fileReader();
    }//GEN-LAST:event_EliminaModificheActionPerformed

    private void SearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchButtonActionPerformed

     DefaultTableModel table = (DefaultTableModel) TabellaPrincipale.getModel();
     DefaultTableModel tableSearch = (DefaultTableModel) TabellaRicerca.getModel();

     String nome,cognome;
     boolean controllo=false;
     int i;

     nome = (String) tableSearch.getValueAt(0,1);
     cognome = (String) tableSearch.getValueAt(0,0);
     if(nome!=null&&cognome!=null)
     {
         for(i=0;i!=table.getRowCount();i++)
         {
             if((table.getValueAt(i,0).equals(cognome.toUpperCase().trim().replaceAll(" {2,}", " ")))&&(table.getValueAt(i,1).equals(nome.toUpperCase().trim().replaceAll(" {2,}", " "))))
              {
                controllo=true;
                MessageText.setText("Elemento trovato");
                TabellaPrincipale.setRowSelectionInterval(i, i);
                TabellaPrincipale.scrollRectToVisible(TabellaPrincipale.getCellRect(i,0,true));
                TickGif();
              }
         }
         if(controllo!=true||table.getRowCount()==0)
         {
           MessageText.setText("Nessuno elemento trovato");
           allarmGif();
         }
     }
     else
     {
       MessageText.setText("Inserire nome e cognome");
       allarmGif();
     }
    }//GEN-LAST:event_SearchButtonActionPerformed

    private void SostiuisciButtonFrameFileExistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SostiuisciButtonFrameFileExistActionPerformed
      TextFieldFrameFileExist.setText("Sostituisci");
      FrameFileExist.setVisible(false);
    }//GEN-LAST:event_SostiuisciButtonFrameFileExistActionPerformed

    private void AnnullaButtonFrameFileExistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AnnullaButtonFrameFileExistActionPerformed
      TextFieldFrameFileExist.setText("Annulla");
      FrameFileExist.setVisible(false);
    }//GEN-LAST:event_AnnullaButtonFrameFileExistActionPerformed

    private void FrameFileExistWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_FrameFileExistWindowClosing
      TextFieldFrameFileExist.setText("Annulla");
      FrameFileExist.setVisible(false);
    }//GEN-LAST:event_FrameFileExistWindowClosing

    private void CheckRadioEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckRadioEmailActionPerformed

       DefaultTableModel table = (DefaultTableModel) TabellaPrincipale.getModel();
       String nome=null,cognome=null;
       int rowsSelected[] = null;
       int i;

       try
       {
          /* if(TabellaPrincipale.getSelectedRowCount()!=0)
           {
              if(CheckRadioEmail.isSelected())
              {
               TabellaAggiungi.clearSelection();
               rowsSelected = TabellaPrincipale.getSelectedRows();
               for(i=0;i!=table.getRowCount();i++)
               {
                nome = (String) table.getValueAt(rowsSelected[i],1);
                cognome = (String) table.getValueAt(rowsSelected[i],0);
                table.setValueAt(cognome.toLowerCase().replace(" ",".").replace("'","")+""+"."+nome.toLowerCase().replace(" ",".").replace("'","")+"@studenti-ittfedifermi.gov.it",rowsSelected[i],2);
                TabellaPrincipale.clearSelection();
               }
              }
              else
              {
               TabellaAggiungi.clearSelection();
               rowsSelected = TabellaPrincipale.getSelectedRows();
               for(i=0;i!=table.getRowCount();i++)
               {
                table.setValueAt("",rowsSelected[i],2);
                TabellaPrincipale.clearSelection();
               }
              }
           }*/
           //else
           //{
            if(CheckRadioEmail.isSelected())
               for(i=0;i!=table.getRowCount();i++)
               {
                nome = (String) table.getValueAt(i,1);
                cognome = (String) table.getValueAt(i,0);
                table.setValueAt(cognome.toLowerCase().replace(" ","").replace("'","")+""+"."+nome.toLowerCase().replace(" ","").replace("'","")+"@studenti-ittfedifermi.gov.it",i,2);
               }
            else
               for(i=0;i!=table.getRowCount();i++)
               {
                table.setValueAt("",i,2);
               }
           //}
       }
       catch(ArrayIndexOutOfBoundsException e)
       {}
       inizializzazioneTabellaSupporto(table.getRowCount(),0,1);

    }//GEN-LAST:event_CheckRadioEmailActionPerformed

    private void CheckRadioPasswordRandomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckRadioPasswordRandomActionPerformed

        DefaultTableModel tableAggiungi = (DefaultTableModel) TabellaAggiungi.getModel();
        if(CheckRadioPasswordRandom.isSelected()) //determino se usare la funzione randomString alla riga 0 colonna 3 table2
        {
           tableAggiungi.setValueAt(randomString(8),0,3);
        }
        else
        {
           tableAggiungi.setValueAt("",0,3);
        }
    }//GEN-LAST:event_CheckRadioPasswordRandomActionPerformed

    private void StampaButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_StampaButtonMouseClicked

    FileMenu.setPopupMenuVisible(false);
    DefaultTableModel table = (DefaultTableModel) TabellaPrincipale.getModel();
    if(!java.awt.Desktop.isDesktopSupported())
    {
      System.out.println("Funzione non supportata!");
      return;
    }

    try
    {
       rigaStampa=0;
       Desktop desk = java.awt.Desktop.getDesktop();
       // mando in stampa il file
       desk.print(new File(PathField.getText()));
       PrinterJob pj = PrinterJob.getPrinterJob();
       pj.setJobName("TestStampa");
       // apriamo la finestra di dialogo della stampante
       StampaGif();
       if(pj.printDialog())
       {
        Book x =new Book();
        x.append(this,pj.defaultPage(),table.getRowCount()/25);
        pj.setPageable(x);
        pj.print();
       }
    }
    catch (NullPointerException npe)
    {
     System.out.println (" Il valore specificato del parametro file è null! ");
    }
    catch (IllegalArgumentException iae)
    {
     MessageText.setText(" Il file specificato non esiste! ");
     allarmGif();
    }
    catch (UnsupportedOperationException uoe)
    {
     MessageText.setText(" La piattaforma non supporta l'azione di stampa! ");
     allarmGif();
    }
    catch (SecurityException se)
    {
     MessageText.setText(" Nessun servizio disponibile");
     allarmGif();
    }
    catch (IOException ex)
    {
     MessageText.setText(" Errore I/O");
     allarmGif();
    }
    catch (PrinterAbortException ex)
    {
     MessageText.setText(" Stampa annullata");
     allarmGif();
    }
    catch (PrinterException ex)
    {
     MessageText.setText(" Qualcosa è andato storto");
     allarmGif();
    }


    }//GEN-LAST:event_StampaButtonMouseClicked

    private void SalvaButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SalvaButtonMouseClicked

      int riga=0;
      DefaultTableModel table = null;
      PrintWriter stream = null;
       if(SalvaButton.isEnabled()==true)
       {
        try
        {
           if(SceltaDipartimento.getSelectedItem().equals(" Tutto")) //non è possibile importare file su ua tabella con filtri attivati
           {
            FileMenu.setPopupMenuVisible(false);

            /*AccountManager CREAZIONE E SCRITTURA SU FILE*/
            table = (DefaultTableModel) TabellaPrincipale.getModel();

            //File creazione = new File(PathField.getText().substring(PathField.getText().lastIndexOf('\\')));
            stream = new PrintWriter(new FileWriter(PathField.getText()));

            for (riga=0;riga!=table.getRowCount();riga++)
            {
               stream.println(table.getValueAt(riga,0)+"\t"+table.getValueAt(riga,1)+"\t"+table.getValueAt(riga,2)+"\t"+table.getValueAt(riga,3)+"\t"+table.getValueAt(riga,4)+"\t"+
                              table.getValueAt(riga,5)+"\t"+table.getValueAt(riga,6)+"\t"+table.getValueAt(riga,7)+"\t"+table.getValueAt(riga,8)+"\t"+table.getValueAt(riga,9)+"\t"+
                              table.getValueAt(riga,10)+"\t"+table.getValueAt(riga,11)+"\t"+table.getValueAt(riga,12)+"\t"+table.getValueAt(riga,13)+"\t"+table.getValueAt(riga,14)+"\t"+
                              table.getValueAt(riga,15)+" ");
            }
            stream.close();
            MessageText.setText("Salvato");
            TickGif();
           }
           else
           {
            MessageText.setText("Impostare il filtro su tutto per salvare");
            allarmGif();
           }
        }
        catch (IOException ex)
        {
          MessageText.setText("Il file è in utilizzo da un altro programma");
          allarmGif();
        }
       }

    }//GEN-LAST:event_SalvaButtonMouseClicked

    private void SceltaDipartimentoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SceltaDipartimentoItemStateChanged

        if(eventoChangeItem==false)
        {
            if (evt.getStateChange() == ItemEvent.SELECTED)
             selezionaDipartimento();
        }
    }//GEN-LAST:event_SceltaDipartimentoItemStateChanged

    private void visualizzaAnteprimaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_visualizzaAnteprimaMouseClicked

        if(jScrollPane11.isVisible()==false)
        {
         FinestraDialogo.setSize(502,679);
         jScrollPane11.setVisible(true);
         visualizzaAnteprima.setText("Chiudi anteprima");
        }
        else
        {
         jScrollPane11.setVisible(false);
         FinestraDialogo.setSize(502,330);
         visualizzaAnteprima.setText("Visualizza anteprima");
        }
    }//GEN-LAST:event_visualizzaAnteprimaMouseClicked

    private void TabellaPrincipaleKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TabellaPrincipaleKeyPressed

       if(evt.getKeyCode()==KeyEvent.VK_DELETE)
        CancellaRigaButton.doClick();
       if((evt.getModifiers() & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK)
        CTRL=true;
    }//GEN-LAST:event_TabellaPrincipaleKeyPressed

    private void FinestraDialogoWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_FinestraDialogoWindowClosing
        PathField.setText("");
    }//GEN-LAST:event_FinestraDialogoWindowClosing

    private void ComparaFileButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ComparaFileButtonMouseClicked

     IDThreadComparaFile = new Thread()
     {
       @Override
       public void run()
       {
        MessageText.setText("");
        contaAggiunti.setText("0");
        contaEsistenti.setText("0");
        CampoBaseOmonimi.setText("0");
        CampoImportatiOmonimi.setText("0");
        CampoMembriImportati.setText("0");
        CampoNumeroFiles.setText("0");
        Contatore_omonimi.setText("0");
        Contatore_doppioni.setText("0");

        if((SceltaDipartimento.getSelectedItem().equals(" Tutto")||SceltaDipartimento.getSelectedItem().equals("Tutto"))&&((!PathField.getText().equals(""))))
         EliminaModifiche.setEnabled(true);
        else
         EliminaModifiche.setEnabled(false);

        DefaultTableModel table = (DefaultTableModel) TabellaPrincipale.getModel();
        DefaultTableModel tableSupportImport = (DefaultTableModel) TabellaEsterna.getModel();
        DefaultTableModel tableNumerazione = (DefaultTableModel) TabellaNumerazione.getModel();
        DefaultTableModel tableComparatore = (DefaultTableModel) TabellaComparatore.getModel();
        DefaultTableModel tableFlagsComparatore = (DefaultTableModel) TabellaFlagsComparatore.getModel();

        BufferedReader br = null;
        String line = null;
        String csvSplitBy = "\t";
        String fileName = null;
        String nome = null;
        String cognome = null;
        String classe = null;
        Boolean controllo = false;
        Boolean avviso = false;
        double progress=0,riga=0;
        int bar=0,conta_doppioni=0,conta_omonimi=0;
        String pathFile = null;

        try
        {
            FileMenu.setPopupMenuVisible(false);
            if(SceltaDipartimento.getSelectedItem().equals(" Tutto")||SceltaDipartimento.getSelectedItem().equals("Tutto")) //non è possibile importare file su ua tabella con filtri attivati
            {
             if(table.getRowCount()!=0)//non è possibile importare file su ua tabella vuota
             {
             /*SCELTA FILE DA COMPARARE*/
             MessageText.setText("");
             fileName=fileChooser();
             pathFile=fileName; //prendo il path del file scelto

             /*LETTURA DATI*/
             File file2 = new File(pathFile);
             bar= (int) file2.length();
             TextInfo.setText("Sto comparando: "+file2.getName());
             Text_File_Comparato.setText(file2.getName());
             ProgressBar.setValue(0);
             FrameLoad.setVisible(true);
             FrameLoad.setLocationRelativeTo(null);
             File file = new File(fileName);
             br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"ISO-8859-1"));
             String[] campi = null;
             int i=0,y=0,controlloCicli=0,righeTable=0,rigaCancellata=0;//variabile per verificare se il file è vuoto
             righeTable=table.getRowCount();

             /*CONTROLLO IMPORTAZIONE DEL FILE*/
             if(TextFileBase.getText().equals(file2.getName()))
               throw new IllegalArgumentException();

             /*INIZIALIZZAZIONE TABELLE*/
             while (tableSupportImport.getRowCount()>0) //cancello i dati contenuti nella tabella di supporto esterna al form
                tableSupportImport.removeRow(0);

             while (tableFlagsComparatore.getRowCount()>0)
              tableFlagsComparatore.removeRow(0);

             while (table.getRowCount()!=i)
             {
              tableFlagsComparatore.addRow(new Object[]{""});
              i++;
             }
                    /*LETTURA FILE*/
                    while ((line=br.readLine())!=null)
                    {
                       riga=line.length();
                       progress=progress+(100*(riga/bar));
                       ProgressBar.setValue((int)progress);
                       TextLoad.setText("Load: "+line);
                       controlloCicli++;
                       campi = line.split(csvSplitBy,16); //questo metodo separa una stringa in celle in un array facendo riferimento ad un carattere separatore scelto
                       System.out.println(campi[15]); //verifico se il file in questione è di tipo standard standard nuovo[16 colonne], va in eccezione se non trova la 16° colonna
                       try
                       {
                         tableSupportImport.addRow(new Object[]{campi[0],campi[1],campi[2],campi[3],campi[4],campi[5],campi[6],campi[7],campi[8],campi[9],campi[10],campi[11],campi[12],campi[13],campi[14],campi[15]});
                         nome = (String) tableSupportImport.getValueAt(y,1);
                         cognome = (String) tableSupportImport.getValueAt(y,0);
                         classe = (String) tableSupportImport.getValueAt(y,14);

                         if(campi[0].equals(null)||campi[1].equals(null)||
                            campi[0].equals("")||campi[1].equals("")) //verifico se al campo nome,cognome e classe vi è contenuto, se falso salto la riga e lo segnalo
                             throw new IllegalArgumentException();

                           /*CONTROLLO DOPPIONI TABLE1 FILE NUOVO*/
                            for(i=0;i!=table.getRowCount();i++)
                            {
                              if((table.getValueAt(i,0).equals(cognome.toUpperCase()))&&(table.getValueAt(i,1).equals(nome.toUpperCase()))&&(!tableFlagsComparatore.getValueAt(i,0).equals("OMONIMO")))
                              {
                               if((table.getValueAt(i,0).equals(cognome.toUpperCase()))&&(table.getValueAt(i,1).equals(nome.toUpperCase()))&&(table.getValueAt(i,14).equals(classe.toUpperCase()))&&(!tableFlagsComparatore.getValueAt(i,0).equals("DOPPIONE")))
                               {
                                TabellaPrincipale.setRowSelectionInterval(i,i); //trova il nominativo doppione
                                tableFlagsComparatore.setValueAt("DOPPIONE",i,0);
                                tableComparatore.addRow(new Object[]{tableNumerazione.getValueAt(i,0),table.getValueAt(i,0),table.getValueAt(i,1),table.getValueAt(i,2),table.getValueAt(i,14),"DOPPIONE"});
                                conta_doppioni++;
                                Contatore_doppioni.setText(""+conta_doppioni);
                                controllo=true;
                               }
                               else
                               {
                                TabellaPrincipale.setRowSelectionInterval(i,i); //trova il nominativo doppione
                                tableFlagsComparatore.setValueAt("OMONIMO",i,0);
                                tableComparatore.addRow(new Object[]{tableNumerazione.getValueAt(i,0),table.getValueAt(i,0),table.getValueAt(i,1),table.getValueAt(i,2),table.getValueAt(i,14),"OMONIMO"});
                                Contatore_omonimi.setText(""+conta_omonimi);
                                conta_omonimi++;
                                controllo=true;
                               }
                              }
                            }
                       }
                       catch(ArrayIndexOutOfBoundsException e)
                       {
                         System.out.println("ECCEZIONE");//qui si va in eccezione (per il file lista_studenti.csv) solo nelle prime sette righe e quando si superano le quattro colonne del file csv
                       }
                       catch(IllegalArgumentException e)
                       {}
                     y++; //prossima riga di jTable3 tableSupportImport
                    }

                    if(controllo==false) //se esistono nominativi già esistenti vengono segnalati dai flags, l'esecuzione continua per i nominativi nuovi
                      MessageText.setText("Non sono presenti omonimi/doppioni");
                    else
                    {
                      MessageText.setText("Sono presenti uno o più omonimi/doppioni");
                      Comparatore.setLocationRelativeTo(null);
                      Comparatore.setVisible(true);
                    }

                    if(controlloCicli==0)
                    {
                        MessageText.setText("Il File è vuoto");
                        allarmGif();
                        avviso=true;
                    }

                if(avviso==false) //risoluzione problema grafico
                {
                 ordinaElencoClassi();
                 contaMembri();
                 inizializzazioneTabellaSupporto(righeTable,rigaCancellata,1);
                 creaItems();
                 SalvaButton.setEnabled(true);
                 if(controllo==false)
                  TickGif();
                 else
                  allarmGif();
                }
             }
             else
             {
                MessageText.setText("Un file non può essere comparato su una tabella vuota");
                allarmGif();
             }
            }
            else
            {
                MessageText.setText("Un file non può essere comparato su una tabella con filtri attivati");
                allarmGif();
            }
            FrameLoad.setVisible(false);
        }
        catch (FileNotFoundException e)
        {
            MessageText.setText("FILE NON TROVATO");
            allarmGif();
        }
        catch (IllegalArgumentException e)
        {
            MessageText.setText("Impossibile comparare lo stesso file");
            allarmGif();
        }
        catch (IOException e)
        {
            MessageText.setText("Errore I/O");
            allarmGif();
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            MessageText.setText("Standardizzare il file prima di compararlo");
            allarmGif();
        }
        catch(NullPointerException e)
        {}
        catch (InterruptedException ex)
        {

        }
        finally
        {
            if (br != null)
            {
                try
                {
                    FrameLoad.setVisible(false);
                    br.close();
                }
                catch (IOException e)
                {
                    MessageText.setText("Errore I/O");
                    allarmGif();
                }
            }
        }
      }
     };
     IDThreadComparaFile.start();
    }//GEN-LAST:event_ComparaFileButtonMouseClicked

    private void TabellaPrincipaleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabellaPrincipaleMouseClicked

        if(evt.getButton()==3)
        {
          PopupMenu.setVisible(true);
          PopupMenu.setLocation(evt.getXOnScreen(),evt.getYOnScreen());
          PopupMenu.setFocusCycleRoot(true);
        }
        else
          PopupMenu.setVisible(false);
    }//GEN-LAST:event_TabellaPrincipaleMouseClicked

    private void eliminaPasswordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eliminaPasswordMouseClicked

        DefaultTableModel table = null;
        int rigaCancellata=0;
        boolean rigaSelezionata=false;
        try
        {
            PopupMenu.setVisible(false);
            SalvaButton.setEnabled(true);
            table = (DefaultTableModel) TabellaPrincipale.getModel();
            int rowsSelected[] = null,i;
            MessageText.setText("");
            TabellaAggiungi.clearSelection();
            rowsSelected = TabellaPrincipale.getSelectedRows();
            for(i=0;i!=rowsSelected.length;i++)
            {
             table.setValueAt("",rowsSelected[i],3);
            }
            inizializzazioneTabellaSupporto(table.getRowCount(),rigaCancellata,1);

            for(i=0;table.getRowCount()!=i;i++)
            {
             if(TabellaPrincipale.isRowSelected(i))
               rigaSelezionata=true;
            }
            TabellaPrincipale.clearSelection();
            if(rigaSelezionata==false)
                throw new NullPointerException();
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
         rigaCancellata=0;
         inizializzazioneTabellaSupporto(table.getRowCount(),rigaCancellata,1);
        }
        catch(NullPointerException e)
        {
         MessageText.setText("Selezionare la riga o gruppo di righe a cui si vuole assegnare una password");
         allarmGif();
        }
    }//GEN-LAST:event_eliminaPasswordMouseClicked

    private void formWindowIconified(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowIconified

        PopupMenu.setVisible(false);
        FrameLoad.setState(JFrame.ICONIFIED);
        PannelloInformazioni.setState(JFrame.ICONIFIED);
        SceltaSalvataggio.setState(JFrame.ICONIFIED);
        FrameAggiornamento.setState(JFrame.ICONIFIED);
        CorrettoreOmonimi.setState(JFrame.ICONIFIED);
        Comparatore.setState(JFrame.ICONIFIED);
        Guida.setState(JFrame.ICONIFIED);
    }//GEN-LAST:event_formWindowIconified

    private void ContattamiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ContattamiActionPerformed

       ContattaFrame.setLocationRelativeTo(null);
       ContattaFrame.setVisible(true);
    }//GEN-LAST:event_ContattamiActionPerformed

    private void UpdateSoftwareActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateSoftwareActionPerformed

      IDThreadUpdaterBackGround.suspend(); //non toccare
      GlobalClassUpdater = new Updater(MessageText,VersionSoftware,GifPicture,UpdateSoftware,FrameAggiornamento,PictureDownload,ButtonInstalla,BarDownload,TextDownload,BarUpdater);
      IDThreadUpdaterManuale = new Thread(GlobalClassUpdater);
      IDThreadUpdaterManuale.start();
    }//GEN-LAST:event_UpdateSoftwareActionPerformed

    private void ButtonInstallaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonInstallaActionPerformed

        IDThreadInstallazione= new Thread(new Installer(ButtonInstalla,BarDownload,TextDownload,FrameAggiornamento,VersionSoftware,OldVersionSoftware));
        IDThreadInstallazione.start();
    }//GEN-LAST:event_ButtonInstallaActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

    this.setDefaultCloseOperation (JFrame.DO_NOTHING_ON_CLOSE);
      try
      {
        if(BarUpdater.isVisible()||FrameAggiornamento.isVisible())
        {
         GlobalClassUpdater.setStatoThread(false);
         GlobalClassUpdaterBackGround.setStatoThread(false);
         Thread.sleep(2000);
         exit(0);
        }
        else
        {
         exit(0);
        }
      }
      catch(NullPointerException e)
      {}
      catch (InterruptedException ex)
      {}
    }//GEN-LAST:event_formWindowClosing

    private void FrameLoadWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_FrameLoadWindowClosed

        StatoThreadReader=false;
        MessageText.setText("Lettura interrotta");
        allarmGif();
        DefaultTableModel table = (DefaultTableModel) TabellaPrincipale.getModel();
        DefaultTableModel tableFlags = (DefaultTableModel) TabellaFlags.getModel();
        if(table.getRowCount()!=0)
        {
         while (table.getRowCount()>0)
            table.removeRow(0);
         while (tableFlags.getRowCount()>0)
            tableFlags.removeRow(0);
        }
        TextFileBase.setText("");

    }//GEN-LAST:event_FrameLoadWindowClosed

    private void Pannello_PannelloInformazioniMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Pannello_PannelloInformazioniMouseDragged

     PannelloInformazioni.setLocation(evt.getXOnScreen()-posX,evt.getYOnScreen()-posY);
    }//GEN-LAST:event_Pannello_PannelloInformazioniMouseDragged

    private void Pannello_PannelloInformazioniMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Pannello_PannelloInformazioniMousePressed

     posX=evt.getX();
     posY=evt.getY();
    }//GEN-LAST:event_Pannello_PannelloInformazioniMousePressed

    private void ClosePanelloInformazioniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClosePanelloInformazioniActionPerformed
     PannelloInformazioni.dispose();
    }//GEN-LAST:event_ClosePanelloInformazioniActionPerformed

    private void InformationFileIconMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_InformationFileIconMouseClicked

     Thread x = new Thread()
     {
      @Override
      public void run()
      {
        PannelloInformazioni.setLocationRelativeTo(null);
        PannelloInformazioni.setAlwaysOnTop(true);
        PannelloInformazioni.setVisible(true);
      }
     };
     x.start();

    }//GEN-LAST:event_InformationFileIconMouseClicked

    private void Pannello_SceltaSalvataggioMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Pannello_SceltaSalvataggioMouseDragged
     SceltaSalvataggio.setLocation(evt.getXOnScreen()-posX,evt.getYOnScreen()-posY);
    }//GEN-LAST:event_Pannello_SceltaSalvataggioMouseDragged

    private void Pannello_SceltaSalvataggioMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Pannello_SceltaSalvataggioMousePressed
     posX=evt.getX();
     posY=evt.getY();
     Guida_veloce.dispose();
    }//GEN-LAST:event_Pannello_SceltaSalvataggioMousePressed

    private void CloseSceltaSalvataggioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CloseSceltaSalvataggioActionPerformed
     SceltaSalvataggio.dispose();
     Guida_veloce.dispose();
    }//GEN-LAST:event_CloseSceltaSalvataggioActionPerformed

    private void FileChooserSalvaNomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FileChooserSalvaNomeActionPerformed

        try
        {
          if(SceltaSalvataggio.isVisible()) //se premo x sul frame chooser non faccio nulla
          {
             int riga=0;
             Boolean controllo=false;
             DefaultTableModel table = null;
             DefaultTableModel tableFlags = null;
             PrintWriter stream = null;

              /*AccountManager CREAZIONE E SCRITTURA SU FILE*/
              table = (DefaultTableModel) TabellaPrincipale.getModel();
              tableFlags = (DefaultTableModel) TabellaFlags.getModel();
              File creazione = new File(FileChooserSalvaNome.getSelectedFile()+".csv");

             /*NEL CASO SI PREMA ANNULLA*/
             if(!creazione.getName().equals("null.csv"))
             {
              /*CONTROLLO ESISTENZA FILE E DIALOG*/
              FrameFileExist.setModal(true);
              if(creazione.exists())
              {
                allarmGif();
                TextFieldFrameFileExist.setText("Il file: "+FileChooserSalvaNome.getSelectedFile()+".csv esiste! sostituirlo?");
                FrameFileExist.setLocationRelativeTo(null);
                FrameFileExist.setVisible(true);
              }
              if(!(TextFieldFrameFileExist.getText().equals("Annulla")))
              {
                stream = new PrintWriter(creazione);
                stream.println("First Name,Last Name,Email Address,Password,Secondary Email,Work Phone 1,Home Phone 1,Mobile Phone 1,Work address 1,Home address 1,Employee Id,Employee Type,Employee Title,Manager,Department,Cost Center");
                    if(CheckBoxSalvaTutto.isSelected())
                    {
                        /*SALVO TUTTO*/
                        for (riga=0;riga!=table.getRowCount();riga++)
                        {
                           stream.println(table.getValueAt(riga,0)+","+table.getValueAt(riga,1)+","+table.getValueAt(riga,2)+","+table.getValueAt(riga,3)+","+table.getValueAt(riga,4)+","+
                                          table.getValueAt(riga,5)+","+table.getValueAt(riga,6)+","+table.getValueAt(riga,7)+","+table.getValueAt(riga,8)+","+table.getValueAt(riga,9)+","+
                                          table.getValueAt(riga,10)+","+table.getValueAt(riga,11)+","+table.getValueAt(riga,12)+","+table.getValueAt(riga,13)+","+table.getValueAt(riga,14)+","+
                                          table.getValueAt(riga,15)+" ");
                           controllo=true;
                        }
                        stream.close();
                        MessageText.setText("Estrazione di tutte le righe nel file specificato completato");
                        TickGif();
                    }
                    if(CheckBoxSalvaNuovi.isSelected())
                    {
                        /*SALVO NUOVI*/
                        for (riga=0;riga!=table.getRowCount();riga++)
                        {
                           if(tableFlags.getValueAt(riga,0).equals("NEW")||tableFlags.getValueAt(riga,0).equals("ESISTE")||tableFlags.getValueAt(riga,0).equals("AGGIUNTO"))
                           {
                           stream.println(table.getValueAt(riga,0)+","+table.getValueAt(riga,1)+","+table.getValueAt(riga,2)+","+table.getValueAt(riga,3)+","+table.getValueAt(riga,4)+","+
                                          table.getValueAt(riga,5)+","+table.getValueAt(riga,6)+","+table.getValueAt(riga,7)+","+table.getValueAt(riga,8)+","+table.getValueAt(riga,9)+","+
                                          table.getValueAt(riga,10)+","+table.getValueAt(riga,11)+","+table.getValueAt(riga,12)+","+table.getValueAt(riga,13)+","+table.getValueAt(riga,14)+","+
                                          table.getValueAt(riga,15)+" ");
                            controllo=true;
                           }
                        }
                        stream.close();
                        MessageText.setText("Estrazione righe NEW nel file specificato completato");
                        TickGif();
                    }
                    if(CheckBoxSalvaVecchi.isSelected())
                    {
                        /*SALVO VECCHI*/
                        for (riga=0;riga!=table.getRowCount();riga++)
                        {
                           if(tableFlags.getValueAt(riga,0).equals("OLD"))
                           {
                           stream.println(table.getValueAt(riga,0)+","+table.getValueAt(riga,1)+","+table.getValueAt(riga,2)+","+table.getValueAt(riga,3)+","+table.getValueAt(riga,4)+","+
                                          table.getValueAt(riga,5)+","+table.getValueAt(riga,6)+","+table.getValueAt(riga,7)+","+table.getValueAt(riga,8)+","+table.getValueAt(riga,9)+","+
                                          table.getValueAt(riga,10)+","+table.getValueAt(riga,11)+","+table.getValueAt(riga,12)+","+table.getValueAt(riga,13)+","+table.getValueAt(riga,14)+","+
                                          table.getValueAt(riga,15)+" ");
                            controllo=true;
                           }
                        }
                        stream.close();
                        MessageText.setText("Estrazione righe OLD nel file specificato completato");
                        TickGif();
                    }
                    if(CheckBoxSalvaTutto.isSelected()==false&&CheckBoxSalvaNuovi.isSelected()==false&&CheckBoxSalvaVecchi.isSelected()==false)
                    {
                        stream.close();
                        creazione.delete();
                        MessageText.setText("Selezionare un opzione");
                        allarmGif();
                    }
                    else
                    {
                     if(controllo==false)
                     {
                        creazione.delete();
                        MessageText.setText("Non sono presenti righe estraibili");
                        allarmGif();
                     }
                    }
              }
              FrameFileExist.dispose();
              SceltaSalvataggio.dispose();
              SalvaButton.setEnabled(true);
             }
             else
             {
              SceltaSalvataggio.dispose();
             }
         }
        }
        catch (IOException ex)
        {
          MessageText.setText("Il file è in utilizzo da un altro programma");
          allarmGif();
        }
    }//GEN-LAST:event_FileChooserSalvaNomeActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened

      FrameLoad.setState(JFrame.NORMAL);
      PannelloInformazioni.setState(JFrame.NORMAL);
      SceltaSalvataggio.setState(JFrame.NORMAL);
    }//GEN-LAST:event_formWindowOpened

    private void CheckBoxSalvaTuttoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckBoxSalvaTuttoActionPerformed
        CheckBoxSalvaNuovi.setSelected(false);
        CheckBoxSalvaVecchi.setSelected(false);
    }//GEN-LAST:event_CheckBoxSalvaTuttoActionPerformed

    private void CheckBoxSalvaNuoviActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckBoxSalvaNuoviActionPerformed
        CheckBoxSalvaTutto.setSelected(false);
        CheckBoxSalvaVecchi.setSelected(false);
    }//GEN-LAST:event_CheckBoxSalvaNuoviActionPerformed

    private void CheckBoxSalvaVecchiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckBoxSalvaVecchiActionPerformed
        CheckBoxSalvaTutto.setSelected(false);
        CheckBoxSalvaNuovi.setSelected(false);
    }//GEN-LAST:event_CheckBoxSalvaVecchiActionPerformed

    private void CloseCorrettoreOmonimiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CloseCorrettoreOmonimiActionPerformed
     CorrettoreOmonimi.dispose();
     Guida_veloce.dispose();
    }//GEN-LAST:event_CloseCorrettoreOmonimiActionPerformed

    private void Pannello_CorrettoreOminimiMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Pannello_CorrettoreOminimiMousePressed
     posX=evt.getX();
     posY=evt.getY();
     Guida_veloce.dispose();
    }//GEN-LAST:event_Pannello_CorrettoreOminimiMousePressed

    private void Pannello_CorrettoreOminimiMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Pannello_CorrettoreOminimiMouseDragged
     CorrettoreOmonimi.setLocation(evt.getXOnScreen()-posX,evt.getYOnScreen()-posY);
    }//GEN-LAST:event_Pannello_CorrettoreOminimiMouseDragged

    private void CorreggiOmonimiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CorreggiOmonimiMouseClicked
      CorrettoreOmonimi.setLocationRelativeTo(null);
      CorrettoreOmonimi.setVisible(true);
    }//GEN-LAST:event_CorreggiOmonimiMouseClicked

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized

      ImageIcon icona = null;
      Image immagine = null;
      Image newimg = null;
      icona = new ImageIcon("./src/accountmanager/img/CSV.png");
      immagine = icona.getImage();
      if(this.getHeight()<this.getPreferredSize().getHeight()+150)//dinamico
       LogoCSVPicture.setIcon(null);
      if(this.getHeight()>this.getPreferredSize().getHeight()+150)//fisso
      {
       newimg = immagine.getScaledInstance(150,155,java.awt.Image.SCALE_SMOOTH);
       icona = new ImageIcon(newimg);
       LogoCSVPicture.setIcon(icona);
      }
    }//GEN-LAST:event_formComponentResized

    private void TabellaFileMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabellaFileMouseClicked

       visualizzaAnteprima((String) TabellaFile.getValueAt(TabellaFile.getSelectedRow(),1),AnteprimaFileImportato);
    }//GEN-LAST:event_TabellaFileMouseClicked

    private void ButtonCancellaRigaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonCancellaRigaActionPerformed

      int rowsSelected[] = null,i;

      rowsSelected = TabellaOmonimi.getSelectedRows();
      for(i=0;rowsSelected.length!=i;i++)
      {
       TabellaOmonimi.setValueAt("ELIMINA",rowsSelected[i],5);
      }
    }//GEN-LAST:event_ButtonCancellaRigaActionPerformed

    private void ButtonRinominaTuttoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonRinominaTuttoActionPerformed

      int i,c,x;
      String cognome = null;

      for(i=0;TabellaOmonimi.getRowCount()!=i;i++)
      {
       cognome=(String) TabellaOmonimi.getValueAt(i,1);
       if(!TabellaOmonimi.getValueAt(i,5).equals("ELIMINA")&&!cognome.contains("_"))
       {
        x=2;
        for(c=i+1;TabellaOmonimi.getRowCount()!=c;c++)
         if(TabellaOmonimi.getValueAt(c,1).equals(TabellaOmonimi.getValueAt(i,1))&&TabellaOmonimi.getValueAt(c,2).equals(TabellaOmonimi.getValueAt(i,2)))
           x++;
        TabellaOmonimi.setValueAt(TabellaOmonimi.getValueAt(i,1)+"_"+x,i,1);
        TabellaOmonimi.setValueAt("RINOMINA",i,5);
       }
      }
    }//GEN-LAST:event_ButtonRinominaTuttoActionPerformed

    private void ButtonRinominaSelezionatiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonRinominaSelezionatiActionPerformed

      int rowsSelected[] = null,i,c,x;
      String cognome=null;

      rowsSelected = TabellaOmonimi.getSelectedRows();
      for(i=0;rowsSelected.length!=i;i++)
      {
       cognome=(String) TabellaOmonimi.getValueAt(rowsSelected[i],1);
       if(!cognome.contains("_"))
       {
        x=2;
        for(c=i+1;TabellaOmonimi.getRowCount()!=c;c++)
         if(TabellaOmonimi.getValueAt(c,1).equals(TabellaOmonimi.getValueAt(rowsSelected[i],1))&&TabellaOmonimi.getValueAt(c,2).equals(TabellaOmonimi.getValueAt(rowsSelected[i],2)))
           x++;
        TabellaOmonimi.setValueAt(TabellaOmonimi.getValueAt(rowsSelected[i],1)+"_"+x,rowsSelected[i],1);
        TabellaOmonimi.setValueAt("RINOMINA",rowsSelected[i],5);
       }
      }
    }//GEN-LAST:event_ButtonRinominaSelezionatiActionPerformed

    private void ButtonRipristinaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonRipristinaActionPerformed

      int rowsSelected[] = null,i;

      rowsSelected = TabellaOmonimi.getSelectedRows();
      for(i=0;rowsSelected.length!=i;i++)
      {
        if(TabellaOmonimi.getValueAt(rowsSelected[i],5).equals("ELIMINA"))
           TabellaOmonimi.setValueAt("",rowsSelected[i],5);
      }
    }//GEN-LAST:event_ButtonRipristinaActionPerformed

    private void ButtonCorreggiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonCorreggiActionPerformed

     IDThreadCorrettore = new Thread()
     {
      @Override
      public void run()
      {
        DefaultTableModel table = (DefaultTableModel) TabellaPrincipale.getModel();
        DefaultTableModel tableFlags = (DefaultTableModel) TabellaFlags.getModel();
        DefaultTableModel tableOmonimi = (DefaultTableModel) TabellaOmonimi.getModel();
        DefaultTableModel tableNumerazione = (DefaultTableModel) TabellaNumerazione.getModel();
        Boolean controllo=false,verifica=false;
        String nome,cognome;
        int eliminati=0,i,c,u;

        if(tableOmonimi.getRowCount()!=0)
        {
        if(table.getRowCount()!=0)
        {
            MessageText.setText("");
            SalvaButton.setEnabled(true);

            for(i=0;tableOmonimi.getRowCount()!=i;i++)
            {
             if(tableOmonimi.getValueAt(i,5).equals("OLD(omonimo)")||tableOmonimi.getValueAt(i,5).equals("NEW(omonimo)")||tableOmonimi.getValueAt(i,5).equals("omonimo")||tableOmonimi.getValueAt(i,5).equals(""))
             {
                verifica=true;
                break;
             }
            }
            for(c=0;tableOmonimi.getRowCount()!=c&&verifica==false;c++) //utile per le modifiche manuali, non troverà mai omonimi se si utilizza la correzione automatica
            {
                for(u=0;table.getRowCount()!=u;u++)
                {
                 if(!tableOmonimi.getValueAt(c,5).equals("ELIMINA"))
                 {
                    if(table.getValueAt(u,0).equals(tableOmonimi.getValueAt(c,1))&&table.getValueAt(u,1).equals(tableOmonimi.getValueAt(c,2)))
                    {
                     tableOmonimi.setValueAt("omonimo",c,5);
                     controllo=true;
                    }
                 }
                }
            }
            if(tableOmonimi.getRowCount()==0)
              verifica=true;

            if(verifica==true||controllo==true)
            {
                MessageText.setText("Applicare le correzioni per tutti gli omonimi");
                allarmGif();
            }

            if(verifica==false&&controllo==false)
            {
                for(i=0;tableOmonimi.getRowCount()!=i;i++)
                {
                  controllo=false;
                  if(tableOmonimi.getValueAt(i,5).equals("ELIMINA"))
                  {
                   tableOmonimi.setValueAt("ELIMINATO",i,5);
                   table.removeRow((int)tableOmonimi.getValueAt(i,0)-1-eliminati);
                   tableFlags.removeRow((int)tableOmonimi.getValueAt(i,0)-1-eliminati);
                   tableNumerazione.removeRow((int)tableOmonimi.getValueAt(i,0)-1-eliminati);
                   eliminati++;
                  }
                  if(tableOmonimi.getValueAt(i,5).equals("RINOMINA"))
                  {
                   tableOmonimi.setValueAt("CORRETTO",i,5);
                    if(!tableOmonimi.getValueAt(i,5).equals("ELIMINA"))
                    {
                      table.setValueAt(tableOmonimi.getValueAt(i,1),(int)tableOmonimi.getValueAt(i,0)-1-eliminati,0);
                      table.setValueAt(tableOmonimi.getValueAt(i,2),(int)tableOmonimi.getValueAt(i,0)-1-eliminati,1);
                      if(tableFlags.getValueAt((int)tableOmonimi.getValueAt(i,0)-1-eliminati,0).equals("OLD(omonimo)"))
                       tableFlags.setValueAt("OLD",(int)tableOmonimi.getValueAt(i,0)-1-eliminati,0);
                      else
                       tableFlags.setValueAt("NEW",(int)tableOmonimi.getValueAt(i,0)-1-eliminati,0);
                    }
                  }
                }
                for(i=0;i!=table.getRowCount();i++)
                {
                 nome = (String) table.getValueAt(i,1);
                 cognome = (String) table.getValueAt(i,0);
                 table.setValueAt(cognome.toLowerCase().replace(" ","").replace("'","")+""+"."+nome.toLowerCase().replace(" ","").replace("'","")+"@studenti-ittfedifermi.gov.it",i,2);
                }
                contaMembri();
                MessageText.setText("Operazione terminata con successo");
                TickGif();
            }
        }
        }
        else
        {
            MessageText.setText("Nessun omonimo da correggere");
            allarmGif();
        }
       }
     };
     IDThreadCorrettore.start();
    }//GEN-LAST:event_ButtonCorreggiActionPerformed

    private void ButtonImportaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonImportaActionPerformed
      importFile();
    }//GEN-LAST:event_ButtonImportaActionPerformed

    private void TabellaOmonimiMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabellaOmonimiMousePressed
     Guida_veloce.dispose();
     Thread x = new Thread()
     {
       @Override
       public void run()
       {
          int i,controllo=0;
          for(i=0;TabellaPrincipale.getRowCount()!=i;i++)
            if(TabellaOmonimi.getValueAt(TabellaOmonimi.getSelectedRow(),1).equals(TabellaPrincipale.getValueAt(i,0))&&TabellaOmonimi.getValueAt(TabellaOmonimi.getSelectedRow(),2).equals(TabellaPrincipale.getValueAt(i,1)))
            {
              controllo=1;
              break;
            }
          if(controllo==0)
           TabellaOmonimi.setValueAt("RINOMINA",TabellaOmonimi.getSelectedRow(),5);

          try
          {
            /*VERIFICO PRECEDENTE RIGA SELEZIONATA*/
            if(PosizioneUltimaRiga!=-1)
            {
              controllo=0;
              for(i=0;TabellaPrincipale.getRowCount()!=i;i++)
                if(TabellaOmonimi.getValueAt(PosizioneUltimaRiga,1).equals(TabellaPrincipale.getValueAt(i,0))&&TabellaOmonimi.getValueAt(PosizioneUltimaRiga,2).equals(TabellaPrincipale.getValueAt(i,1)))
                {
                  controllo=1;
                  break;
                }
                if(controllo==0)
                 TabellaOmonimi.setValueAt("RINOMINA",PosizioneUltimaRiga,5);
            }
            PosizioneUltimaRiga=TabellaOmonimi.getSelectedRow();
          }
          catch(ArrayIndexOutOfBoundsException e)
          {
           System.out.println("Ho selezionato una riga da una tabella ed eseguito il controllo per un'altra tabella");
          }
       }
     };
     x.start();
    }//GEN-LAST:event_TabellaOmonimiMousePressed

    private void Design_MetalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Design_MetalActionPerformed

        Design=1;
        Dialog_Chiusura.setLocationRelativeTo(null);
        Dialog_Chiusura.setVisible(true);
    }//GEN-LAST:event_Design_MetalActionPerformed

    private void Design_WindowsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Design_WindowsActionPerformed

        Design=0;
        Dialog_Chiusura.setLocationRelativeTo(null);
        Dialog_Chiusura.setVisible(true);
    }//GEN-LAST:event_Design_WindowsActionPerformed

    private void Panel_ChiusuraMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Panel_ChiusuraMousePressed
        posX=evt.getX();
        posY=evt.getY();
    }//GEN-LAST:event_Panel_ChiusuraMousePressed

    private void Panel_ChiusuraMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Panel_ChiusuraMouseDragged
        Dialog_Chiusura.setLocation(evt.getXOnScreen()-posX,evt.getYOnScreen()-posY);
    }//GEN-LAST:event_Panel_ChiusuraMouseDragged

    private void Button_AnnullaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_AnnullaActionPerformed
        Dialog_Chiusura.dispose();
    }//GEN-LAST:event_Button_AnnullaActionPerformed

    private void Button_RiavviaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_RiavviaActionPerformed

        if(BarUpdater.isVisible()||FrameAggiornamento.isVisible())
        {
         MessageText.setText("Terminare il processo di aggiornamento!!!");
         allarmGif();
        }
        else
        {
        Dialog_Chiusura.dispose();
        if(Design==1)
        {
            try
            {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
                {
                    if ("Metal".equals(info.getName()))
                    {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (ClassNotFoundException ex) {
                java.util.logging.Logger.getLogger(JForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                java.util.logging.Logger.getLogger(JForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(JForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(JForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
        if(Design==0)
        {
            try
            {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
                {
                    if ("Windows".equals(info.getName()))
                    {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (ClassNotFoundException ex) {
                java.util.logging.Logger.getLogger(JForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                java.util.logging.Logger.getLogger(JForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(JForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(JForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
        FrameLoad.setState(JFrame.EXIT_ON_CLOSE);
        PannelloInformazioni.setState(JFrame.EXIT_ON_CLOSE);
        SceltaSalvataggio.setState(JFrame.EXIT_ON_CLOSE);
        FrameAggiornamento.setState(JFrame.EXIT_ON_CLOSE);
        Guida.setState(JFrame.EXIT_ON_CLOSE);
        if(IDThreadReader!=null)
         if(IDThreadReader.isAlive())
          IDThreadReader.stop();
        new JForm().setVisible(true);
        this.dispose();
        }
    }//GEN-LAST:event_Button_RiavviaActionPerformed

    private void IndiceValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_IndiceValueChanged

        String NameFile = evt.getPath().toString();
        if(NameFile.equals("[Guida, Manuale]")||NameFile.equals("[Guida]")||NameFile.equals("[Guida, Risoluzione_Problemi]"))
        {}
        else
        {
            OptionSlide.setVisible(true);
            SlidePanel.setVisible(true);
            NumberSlide.setVisible(true);
            visualizzaAnteprima("./src/accountmanager/TestoGuida/"+NameFile.replaceAll(",","").replaceAll("Guida","").replaceAll("Manuale","").replaceAll("Risoluzione_Problemi","").replaceAll(" ","").replace("[","").replace("]","")+".txt",TextGuida);
            NameFile=NameFile.replaceAll(",","").replaceAll("Guida","").replaceAll("Manuale","").replaceAll("Risoluzione_Problemi","").replaceAll(" ","").replace("[","").replace("]","");
            switch(NameFile)
            {
                case "Aggiungere_membro":
                {NumeroSlide=0;
                 Sezione=0;
                 NumberSlide.setText("Slide: 1/3");
                 NumberSlide.setVisible(true);
                 ButtonSlideLeft.setVisible(true);
                 ButtonSlideRight.setVisible(true);
                 visualizzaSlide();}
                break;
                case "Capire_la_tabella_Flags":
                {NumeroSlide=0;
                 Sezione=1;
                 NumberSlide.setVisible(false);
                 ButtonSlideLeft.setVisible(false);
                 ButtonSlideRight.setVisible(false);
                 visualizzaSlide();}
                break;
                case "Importazione":
                {NumeroSlide=0;
                 Sezione=2;
                 NumberSlide.setVisible(false);
                 ButtonSlideLeft.setVisible(false);
                 ButtonSlideRight.setVisible(false);
                 visualizzaSlide();}
                break;
                case "Esportazione":
                {NumeroSlide=0;
                 Sezione=3;
                 NumberSlide.setVisible(false);
                 ButtonSlideLeft.setVisible(false);
                 ButtonSlideRight.setVisible(false);
                 visualizzaSlide();}
                break;
                case "Strumento_Anti_Omonimi":
                {NumeroSlide=0;
                 Sezione=4;
                 NumberSlide.setText("Slide: 1/2");
                 NumberSlide.setVisible(true);
                 ButtonSlideLeft.setVisible(true);
                 ButtonSlideRight.setVisible(true);
                 visualizzaSlide();}
                break;
                case "Server_Problemi":
                {NumeroSlide=0;
                 Sezione=5;
                 NumberSlide.setText("Slide: 1/2");
                 NumberSlide.setVisible(true);
                 ButtonSlideLeft.setVisible(true);
                 ButtonSlideRight.setVisible(true);
                 visualizzaSlide();}
                break;
                case "File_non_leggibile":
                {NumeroSlide=0;
                 Sezione=6;
                 NumberSlide.setText("Slide: 1/2");
                 NumberSlide.setVisible(false);
                 ButtonSlideLeft.setVisible(false);
                 ButtonSlideRight.setVisible(false);
                 visualizzaSlide();}
                break;
                default:
                {OptionSlide.setVisible(false);
                 NumberSlide.setVisible(false);
                 ButtonSlideRight.setVisible(false);
                 ButtonSlideLeft.setVisible(false);
                 SlidePanel.setVisible(false);
                 Guida.setSize(990,370);
                 Panel_Guida.setSize(990,370);
                 OptionSlide.setText("Visualizza slide");}
                break;
            }
        }
    }//GEN-LAST:event_IndiceValueChanged

    private void OptionSlideMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OptionSlideMouseClicked

     Thread x = new Thread()
     {
      @Override
      public void run()
      {
        if(SlidePanel.isVisible()==false)
        {
         OptionSlide.setText("Chiudi slide");
         Guida.setSize(990,800);
         Panel_Guida.setSize(990,800);
         ButtonSlideRight.setVisible(true);
         ButtonSlideLeft.setVisible(true);
         SlidePanel.setVisible(true);
        }
        else
        {
         OptionSlide.setText("Visualizza slide");
         ButtonSlideRight.setVisible(false);
         ButtonSlideLeft.setVisible(false);
         SlidePanel.setVisible(false);
         Guida.setSize(990,370);
         Panel_Guida.setSize(990,370);
      }
      }
     };
     x.start();
    }//GEN-LAST:event_OptionSlideMouseClicked

    private void ButtonSlideRightMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonSlideRightMousePressed

        ImageIcon icona2 = new ImageIcon("./src/accountmanager/img/Small-arrow-right-icon2.png");
        ImageIcon icona = new ImageIcon("./src/accountmanager/img/Small-arrow-right-icon.png");
        ButtonSlideRight.setIcon(icona2);
        TimerTask mt= new TimerTask()
        {
          @Override
           public void run()
           {
            ButtonSlideRight.setIcon(icona);
           }
        };
        java.util.Timer timer = new java.util.Timer();
        timer.schedule(mt,100);
    }//GEN-LAST:event_ButtonSlideRightMousePressed

    private void ButtonSlideLeftMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonSlideLeftMousePressed

        ImageIcon icona2 = new ImageIcon("./src/accountmanager/img/Small-arrow-left-icon2.png");
        ImageIcon icona = new ImageIcon("./src/accountmanager/img/Small-arrow-left-icon.png");
        ButtonSlideLeft.setIcon(icona2);
        TimerTask mt= new TimerTask()
        {
          @Override
           public void run()
           {
            ButtonSlideLeft.setIcon(icona);
           }
        };
        java.util.Timer timer = new java.util.Timer();
        timer.schedule(mt,100);        // TODO add your handling code here:
    }//GEN-LAST:event_ButtonSlideLeftMousePressed

    private void ButtonSlideLeftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonSlideLeftActionPerformed
       NumeroSlide--;
       visualizzaSlide();
    }//GEN-LAST:event_ButtonSlideLeftActionPerformed

    private void ButtonSlideRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonSlideRightActionPerformed
       NumeroSlide++;
       visualizzaSlide();
    }//GEN-LAST:event_ButtonSlideRightActionPerformed

    private void TabellaPrincipaleMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabellaPrincipaleMouseReleased

    /* int i,rowsSelected[] = TabellaPrincipale.getSelectedRows();
     if(CTRL==true||rowsSelected.length<=1)
     {
      for(i=0;rowsSelected.length!=i;i++)
      {
        TabellaFlags.addRowSelectionInterval(rowsSelected[i],rowsSelected[i]);
        TabellaNumerazione.addRowSelectionInterval(rowsSelected[i],rowsSelected[i]);
      }
     }
     else
     {
       TabellaFlags.clearSelection();
       TabellaNumerazione.clearSelection();
     }*/
    }//GEN-LAST:event_TabellaPrincipaleMouseReleased

    private void TabellaPrincipaleKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TabellaPrincipaleKeyReleased

      if(CTRL==true)
       CTRL=false;
    }//GEN-LAST:event_TabellaPrincipaleKeyReleased

    private void Help_CorrettoreOmonimiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Help_CorrettoreOmonimiMouseClicked

       Guida_veloce.setLocationRelativeTo(Help_CorrettoreOmonimi);
       visualizzaAnteprima("./src/accountmanager/TestoGuida/GuidaVeloce_Omonimi.txt",Guida_veloce_text);
       Guida_veloce.setVisible(true);

    }//GEN-LAST:event_Help_CorrettoreOmonimiMouseClicked

    private void Help_SalvataggioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Help_SalvataggioMouseClicked

       Guida_veloce.setLocationRelativeTo(Help_Salvataggio);
       visualizzaAnteprima("./src/accountmanager/TestoGuida/GuidaVeloce_Salvataggio.txt",Guida_veloce_text);
       Guida_veloce.setVisible(true);

    }//GEN-LAST:event_Help_SalvataggioMouseClicked

    private void ScrollPane_TabellaOmonimiMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ScrollPane_TabellaOmonimiMousePressed
       Guida_veloce.dispose();
    }//GEN-LAST:event_ScrollPane_TabellaOmonimiMousePressed

    private void FrameAggiornamentoWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_FrameAggiornamentoWindowClosed

      GlobalClassUpdaterBackGround.setStatoThread(false);
      MessageText.setText("Aggiornamento interrotto");
      allarmGif();
    }//GEN-LAST:event_FrameAggiornamentoWindowClosed

    private void CloseComparatoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CloseComparatoreActionPerformed
     Comparatore.dispose();
    }//GEN-LAST:event_CloseComparatoreActionPerformed

    private void Pannello_comparatoreMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Pannello_comparatoreMouseDragged
     Comparatore.setLocation(evt.getXOnScreen()-posX,evt.getYOnScreen()-posY);
    }//GEN-LAST:event_Pannello_comparatoreMouseDragged

    private void Pannello_comparatoreMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Pannello_comparatoreMousePressed
     posX=evt.getX();
     posY=evt.getY();
    }//GEN-LAST:event_Pannello_comparatoreMousePressed


    public File cercaFileZip()
    {
     File f =null;
     File d = new File(System.getProperty("user.home")+"/Desktop");
     String array[] = d.list(); //creo un array di stringhe e lo riempio con la lista dei files presenti nella directory
     System.out.println("stampo la lista dei files contenuti nella directory:");
     for (int i=0;i<array.length;i++)
     {
      System.out.println(i+1+"."+array[i]);
      if(array[i].contains("AccountManager_Version_"))
      {
       f = new File(array[i]);
       break;
      }
     }
     return f;
    }

    public String getCurrentVersion()
    {
     return VersionSoftware;
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

    public void setColorRow(int riga, int reset)
    {
        for(int c=0;c!=16;c++)
        {
         TabellaFlags.getColumnModel().getColumn(c).setCellRenderer(new DefaultTableCellRenderer()
         {
          @Override
          public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
          {
           Component cell = super.getTableCellRendererComponent (table, value, false, false, row, column);

           switch(reset)
           {
               case 1:
                 if(row==riga||row==riga)
                  cell.setBackground( Color.orange );
                 else
                  cell.setBackground( Color.white );
                 return cell;
               case 2:
                 if(row==riga||row==riga+2)
                  cell.setBackground( Color.red );
                 else
                  cell.setBackground( Color.white );
                 return cell;
               case 3:
                 if(row==riga||row==riga)
                  cell.setBackground( Color.green );
                 else
                  cell.setBackground( Color.white );
                 return cell;
               default:
                 if(row==riga||row==riga)
                  cell.setBackground( Color.white );
                 else
                  cell.setBackground( Color.white );
                 return cell;
           }
          }});
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

    public void downloadGif()
    {
     ImageIcon newIcon = new ImageIcon("./src/accountmanager/img/download_gif.gif");
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
     timer.schedule(mt,4000);
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

    public void StampaGif()
    {
     ImageIcon newIcon = new ImageIcon("./src/accountmanager/img/stampa.gif");
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
     timer.schedule(mt,3000);
    }

    public String randomString(int length)
    {
     Random rand = new Random();
     StringBuffer tempStr = new StringBuffer();
     tempStr.append("");
     for (int i=0;i<length;i++)
     {
        int c = rand.nextInt(122-48)+48;
        if((c>=58&&c<=64)||(c>=91&&c<=96))
        {
         i--; //dovendo saltare l'iterazione corrente tonro indietro nel conteggio dei cicli
         continue; //salto l'iterazione
        }
      tempStr.append((char)c);
     }
    return tempStr.toString();
    }

    public int ordinaStringaDati(int y)
    {

         DefaultTableModel tableFlags = (DefaultTableModel) TabellaFlags.getModel();
         DefaultTableModel table1 = (DefaultTableModel) TabellaPrincipale.getModel();
         DefaultTableModel table = (DefaultTableModel) TabellaEsterna.getModel();
         DefaultTableModel tableNumerazione = (DefaultTableModel) TabellaNumerazione.getModel();

         String nome = (String) table.getValueAt(y,1);
         String cognome = (String) table.getValueAt(y,0);
         String classe = (String) table.getValueAt(y,14);
         ArrayList listaCognomi = new ArrayList(0);
         int riga=0,posizione=0,ricordaPosizione=0,ricordaPosizione2=0,contaMembriImportati=0,i=0;
         Boolean controllo=false;

         /*CONTROLLO DOPPIONI TABLE1 FILE NUOVO*/
         for(i=0;i!=table1.getRowCount();i++)
         {
          if((table1.getValueAt(i,0).equals(cognome.toUpperCase()))&&(table1.getValueAt(i,1).equals(nome.toUpperCase()))&&(!tableFlags.getValueAt(i,0).equals("OLD(omonimo)")&&!tableFlags.getValueAt(i,0).equals("NEW(omonimo)")))
          {
            TabellaPrincipale.setRowSelectionInterval(i,i); //trova il nominativo doppione
            tableFlags.addRow(new Object[]{"NEW(omonimo)"});
            tableNumerazione.addRow(new Object[]{""});
            contaImportatiOmonimi++;
            contaMembriImportati++;
            //contaEsistenti.setText(""+contatoreEsistenti);
            controllo=true;
          }
         }

         table1.addRow(new Object[]
         {
            cognome.toUpperCase(),nome.toUpperCase(),cognome.toLowerCase().replace(" ",".").replace("'","")+"."+nome.toLowerCase().replace(" ",".").replace("'","")+"@studenti-ittfedifermi.gov.it",table.getValueAt(y,3),table.getValueAt(y,4),table.getValueAt(y,5),
            table.getValueAt(y,6),table.getValueAt(y,7),table.getValueAt(y,8),table.getValueAt(y,9),table.getValueAt(y,10),table.getValueAt(y,11),
            table.getValueAt(y,12),table.getValueAt(y,13),(classe.toUpperCase()),table.getValueAt(y,15)
         }); //aggiungo in fondo alla tabella la riga compilata


        /**/
        if(controllo==true) //se esistono nominativi già esistenti
        {
          MessageText.setText("Sono presenti uno o più omonimi");
        }
        else
        {
         tableFlags.addRow(new Object[]{"NEW"});
         tableNumerazione.addRow(new Object[]{""});
         contaMembriImportati++;
        }
        /**/


        /*INIZIO L'ORDINAMENTO DELLA STRINGA*/
        for(riga=0;riga!=table1.getRowCount();riga++) //visito tutte le righe relativamente al campo classe
        {
            if(table1.getValueAt(riga,14).equals((classe.toUpperCase()))) //cerco la classe
            {
             table1.moveRow(table1.getRowCount()-1,table1.getRowCount()-1,riga); //inserisco la nuova riga compilata alla classe corretta in cima alla lista dei nomi
             tableFlags.moveRow(table1.getRowCount()-1,table1.getRowCount()-1,riga);
             break;
            }
        }// se arrivo in fondo al ciclo senza aver mosso la riga significa che non vi è nessuna classe corrispondente

        ricordaPosizione2=riga;

        if(riga!=table1.getRowCount()) //non eseguo le operazioni sottostanti nel caso non ci fosse la classe inserita nella lista e la nuova riga rimane in fondo alla lista
        {
         try
         {
            ricordaPosizione=riga; //prima riga in cui si presenta la classe trovata
            ricordaPosizione2=riga;
            for(posizione=riga;table1.getValueAt(posizione,14).equals(classe.toUpperCase()+"   ")||table1.getValueAt(posizione,14).equals(classe.toUpperCase());posizione++)
            {
                listaCognomi.add(table1.getValueAt(posizione,0)); //inserisco tuti i cognomi della classe scelta in ArrayList cosi da poter determinare il numero di alunni (compreso quello nuovo)
                if(table1.getRowCount()==posizione+1)
                    break;
            }
            String[] arrayCognome= new String[listaCognomi.size()]; //ora che so quanti alunni ci sono nella classe determino la dimensione dell'array che conterrà tutti i cognomi della classe
            for(i=0;i!=listaCognomi.size();i++)
            {
                arrayCognome[i]=(String) table1.getValueAt(ricordaPosizione,0); //trascrivo tutti i cognomi dentro l'array
                ricordaPosizione++;
            }

            Arrays.sort(arrayCognome); //ordino l'array contenente il campo cognomi presi da ArrayList
            for(i=0;i!=listaCognomi.size();i++)
            {
                if(arrayCognome[i].equals(cognome.toUpperCase())) //cerco la posizione, dentro l'array ordinato, del nuovo alunno e sommo la posizione dell'array con quello della posizione della prima riga della classe scelta
                {
                 ricordaPosizione2=ricordaPosizione2+i;
                 break;
                }
            }
            table1.moveRow(riga,riga,ricordaPosizione2); //inserisco i campi nel giusto ordine alfabetico dei nomi della classe scelta
            tableFlags.moveRow(riga,riga,ricordaPosizione2);
            TabellaPrincipale.setRowSelectionInterval(ricordaPosizione2,ricordaPosizione2);
         }
         catch(ArrayIndexOutOfBoundsException e)
         {
          System.out.println("ERRORE");
         }
        }
        ordinaNomiDoppioni(table1,ricordaPosizione2);
        return contaMembriImportati;
    }

    public void ordinaCampi(String campi[],String generiCampi[],DefaultTableModel table,int colonna,int contaRighe)
    {
     switch(generiCampi[colonna].toUpperCase().trim().replaceAll(" {2,}", " "))
     {
         case "COGNOME":
            table.setValueAt(campi[colonna].toUpperCase().trim().replaceAll(" {2,}"," "),contaRighe,0);
          break;
         case "NOME":
            table.setValueAt(campi[colonna].toUpperCase().trim().replaceAll(" {2,}"," "),contaRighe,1);
          break;
         case "EMAIL PRIMARIA":
            table.setValueAt(campi[colonna],contaRighe,2);
          break;
         case "PASSWORD":
            table.setValueAt(campi[colonna],contaRighe,3);
          break;
         case "EMAIL SECONDARIA":
            table.setValueAt(campi[colonna],contaRighe,4);
          break;
         case "TELEFONO LAVORO":
            table.setValueAt(campi[colonna],contaRighe,5);
          break;
         case "TELEFONO CASA":
            table.setValueAt(campi[colonna],contaRighe,6);
          break;
         case "TELEFONO CELLULARE":
            table.setValueAt(campi[colonna],contaRighe,7);
          break;
         case "INDIRIZZO LAVORO":
            table.setValueAt(campi[colonna],contaRighe,8);
          break;
         case "INDIRIZZO CASA":
            table.setValueAt(campi[colonna],contaRighe,9);
          break;
         case "EMPLOYEE ID":
            table.setValueAt(campi[colonna],contaRighe,10);
          break;
         case "EMPLOYEE TIPS":
            table.setValueAt(campi[colonna],contaRighe,11);
          break;
         case "EMPLOYE TITLE":
            table.setValueAt(campi[colonna],contaRighe,12);
          break;
         case "MANAGER":
            table.setValueAt(campi[colonna],contaRighe,13);
          break;
         case "CLASSE":
            table.setValueAt(campi[colonna].toUpperCase().trim().replaceAll(" {2,}"," "),contaRighe,14);
          break;
         case "COST CENTER":
            table.setValueAt(campi[colonna],contaRighe,15);
          break;
         case "FIRST NAME":
            table.setValueAt(campi[colonna].toUpperCase().trim().replaceAll(" {2,}"," "),contaRighe,0);
          break;
         case "LAST NAME":
            table.setValueAt(campi[colonna].toUpperCase().trim().replaceAll(" {2,}"," "),contaRighe,1);
          break;
         case "EMAIL ADDRESS":
            table.setValueAt(campi[colonna],contaRighe,2);
          break;
         case "SECONDARY EMAIL":
            table.setValueAt(campi[colonna],contaRighe,4);
          break;
         case "WORK PHONE 1":
            table.setValueAt(campi[colonna],contaRighe,5);
          break;
         case "HOME PHONE 1":
            table.setValueAt(campi[colonna],contaRighe,6);
          break;
         case "HOME ADDRESS 1":
            table.setValueAt(campi[colonna],contaRighe,9);
          break;
         case "Employee Type":
            table.setValueAt(campi[colonna],contaRighe,11);
          break;
         case "Employee Title":
            table.setValueAt(campi[colonna],contaRighe,12);
          break;
         case "DEPARTMENT":
            table.setValueAt(campi[colonna].toUpperCase().trim().replaceAll(" {2,}"," "),contaRighe,14);
          break;
         case "EMPLOYEE TITLE":
            table.setValueAt(campi[colonna],contaRighe,12);
          break;
          case "DIPARTIMENTO":
            table.setValueAt(campi[colonna].toUpperCase().trim().replaceAll(" {2,}"," "),contaRighe,14);
          break;
         default:
          break;
     }
    }

    public void ragruppaClassi() throws InterruptedException
    {
     DefaultTableModel table = (DefaultTableModel) TabellaPrincipale.getModel();
     DefaultTableModel tableFlags = (DefaultTableModel) TabellaFlags.getModel();
     String classe = null;
     int posizione=0,riga=0;

     for(posizione=0;posizione!=table.getRowCount()-1;)
     {
        if(StatoThreadReader==false)
          throw new InterruptedException();
        classe=(String) table.getValueAt(posizione,14);
        for(riga=posizione+1;riga!=table.getRowCount();)
        {
         if(table.getValueAt(riga,14).equals(classe.toUpperCase()))
         {
            posizione++;
            table.moveRow(riga,riga,posizione);
            tableFlags.moveRow(riga,riga,posizione);
         }
         riga++;
        }
        posizione++;
     }
    }

    public void creaItems()
    {
     DefaultTableModel table = (DefaultTableModel) TabellaPrincipale.getModel();
     int i,sottraggo=0;

     SceltaDipartimento.removeAllItems();
     /*Attenzione: questa variabile globale è utilizzata per risolvere un complicato bug dovuto al lancio dell' evento quando un item cambia lo stato della combobox
                   la soluzione è stata quella di evitare che l'istruzione addItem lanciasse l'evento*/
     eventoChangeItem = true;
     SceltaDipartimento.addItem(" Tutto");
     eventoChangeItem = false;
     for(i=0;i!=table.getRowCount();i++)
     {
        if(table.getRowCount()-1!=i)
        {
          if(!(table.getValueAt(i,14).equals(table.getValueAt(i+1,14))))
            SceltaDipartimento.addItem((String) table.getValueAt(i,14));
        }
        else
        {
         if(!(table.getValueAt(i,14).equals(null)))
            SceltaDipartimento.addItem((String) table.getValueAt(i,14));
        }
     }
     sottraggo=SceltaDipartimento.getItemCount()-1;
     try
     {
      if(SceltaDipartimento.getItemAt(1).equals(""))
       sottraggo=sottraggo-1;
     }
     catch(NullPointerException e){}
     if(sottraggo>0)
      CampoNumeroDipartimenti.setText(""+sottraggo);
     else
      CampoNumeroDipartimenti.setText("0");

    }

    public int inizializzazioneTabellaSupporto(int righeTable,int posizione,int controlloRighe)
    {
        DefaultTableModel table = null;
        DefaultTableModel tableFlags = null;
        DefaultTableModel tableSupport = null;
        DefaultTableModel tableFlagsSupport = null;

        table = (DefaultTableModel) TabellaPrincipale.getModel();
        tableFlags = (DefaultTableModel) TabellaFlags.getModel();
        tableSupport = (DefaultTableModel) jTable4.getModel();
        tableFlagsSupport = (DefaultTableModel) TabellaFlagsSupport.getModel();

        int riga,i=0,riga2=0,controllo=0;
        boolean checkPassage=false;

        /*Eseguo la sostituzione totale delle modifiche sulla tabella di supporto*/
        /*Se il filtro è impostato su "tutto" l'aggiunta di elementi è gestita normalmente aggiungendo a tableSupport la nuova riga*/
        if(SceltaDipartimento.getSelectedItem().equals(" Tutto")||controlloRighe==0)
        {
            while(tableSupport.getRowCount()>0) //cancello i dati contenuti nella tabella di supporto esterna al form
                tableSupport.removeRow(0);

            while(tableFlagsSupport.getRowCount()>0) //cancello i dati contenuti nella tabella di supporto flags esterna al form
                tableFlagsSupport.removeRow(0);

            for(riga=0;table.getRowCount()!=riga;riga++) //scrivo i dati della tabella principale sulla tabella di supporto
            {
             tableSupport.addRow(new Object[]
             {
                table.getValueAt(riga,0),table.getValueAt(riga,1),table.getValueAt(riga,2),table.getValueAt(riga,3),table.getValueAt(riga,4),table.getValueAt(riga,5),
                table.getValueAt(riga,6),table.getValueAt(riga,7),table.getValueAt(riga,8),table.getValueAt(riga,9),table.getValueAt(riga,10),table.getValueAt(riga,11),
                table.getValueAt(riga,12),table.getValueAt(riga,13),table.getValueAt(riga,14),table.getValueAt(riga,15)
             });
            }
            for(riga=0;tableFlags.getRowCount()!=riga;riga++) //scrivo i dati della tabella principale sulla tabella di supporto
            {
             tableFlagsSupport.addRow(new Object[]
             {
                tableFlags.getValueAt(riga,0)
             });
            }
        }
        /*Eseguo la sostituzione parziale delle modifiche di una classe dentro la tabella di supporto*/
        else
        {
            /*Se la tabella di supporto è maggiore della tabella principale allora si deve eliminare la riga anche nella tabella di supporto*/
            if(righeTable>table.getRowCount())
            {
                tableSupport.removeRow(posizione);
                tableFlagsSupport.removeRow(posizione);
                controllo=1;
            }

            /*Se la tabella di supporto è uguale in dimensioni alla tabella principale si aggiornano i cambiamenti*/
            if(righeTable==table.getRowCount())
            {
                for(riga=0;tableSupport.getRowCount()!=riga;)
                {
                  if(table.getValueAt(0,14).equals(tableSupport.getValueAt(riga,14)))
                    while(table.getRowCount()!=riga2)
                    {
                     for(i=0;16!=i;i++)
                      tableSupport.setValueAt(table.getValueAt(riga2,i),riga,i);
                     tableFlagsSupport.setValueAt(tableFlags.getValueAt(riga2,0),riga,0);
                     riga2++;
                     riga++;
                    }
                  else
                    riga++;
                }
            }
            /*Se la tabella di supporto è minore della tabella principale allora si deve aggiungere la riga anche nella tabella di supporto
              (la funzione inizializzazioneTabellaSupporto è richiamata su AggiungiMembro)*/
            if(righeTable<table.getRowCount())
            {
                tableSupport.addRow(new Object[]{"","","","","","","","","","","","","","","",""}); //necessario se si aggiunge una nuova riga in una classe filtrata
                tableFlagsSupport.addRow(new Object[]{""});

                for(riga=0;tableSupport.getRowCount()!=riga;)
                {
                  if(table.getValueAt(0,14).equals(tableSupport.getValueAt(riga,14)))
                  {
                    if(checkPassage==false)
                    {
                        tableSupport.moveRow(tableSupport.getRowCount()-1,tableSupport.getRowCount()-1,riga);
                        tableFlagsSupport.moveRow(tableSupport.getRowCount()-1,tableSupport.getRowCount()-1,riga);
                        checkPassage=true;
                    }
                    while(table.getRowCount()!=riga2)
                    {
                     for(i=0;16!=i;i++)
                      tableSupport.setValueAt(table.getValueAt(riga2,i),riga,i);
                     tableFlagsSupport.setValueAt(tableFlags.getValueAt(riga2,0),riga,0);
                     riga2++;
                     riga++;
                    }
                  }
                  else
                    riga++;
                }
                controllo=1;
            }
        }
        return controllo;
    }

    public void contaMembri()
    {
     DefaultTableModel table = null;
     DefaultTableModel tableFlags = null;
     DefaultTableModel tableNumerazione = null;
     DefaultTableModel tableOmonimi = null;

     table = (DefaultTableModel) TabellaPrincipale.getModel();
     tableFlags = (DefaultTableModel) TabellaFlags.getModel();
     tableNumerazione = (DefaultTableModel) TabellaNumerazione.getModel();
     tableOmonimi = (DefaultTableModel) TabellaOmonimi.getModel();
     int i,c=0,contaTotaleOmonimi=0,contaImportatiOmonimi=0,contaBaseOmonimi=0;

     contaMembri.setText("0");
     contaEsistenti.setText("0");
     CampoBaseOmonimi.setText("0");
     CampoImportatiOmonimi.setText("0");
     CampoTotaleOmonimi.setText("0");
     while(tableOmonimi.getRowCount()>0)
      tableOmonimi.removeRow(0);
     while(tableNumerazione.getRowCount()>0)
      tableNumerazione.removeRow(0);


     for(i=0;table.getRowCount()!=i;i++)
     {
      contaMembri.setText(""+i);
      tableNumerazione.addRow(new Object[]{i+1});
       if(tableFlags.getValueAt(i,0).equals("OLD(omonimo)")||tableFlags.getValueAt(i,0).equals("NEW(omonimo)"))
       {
        if(tableFlags.getValueAt(i,0).equals("OLD(omonimo)"))
         contaBaseOmonimi++;
        if(tableFlags.getValueAt(i,0).equals("NEW(omonimo)"))
         contaImportatiOmonimi++;

        tableOmonimi.addRow(new Object[]{"",table.getValueAt(i,0),table.getValueAt(i,1),table.getValueAt(i,2),table.getValueAt(i,14),tableFlags.getValueAt(i,0)});
        if(tableOmonimi.getRowCount()!=c)
        {
         tableOmonimi.setValueAt(i+1,c,0);
         c++;
        }
        CampoTotaleOmonimi.setText(""+contaTotaleOmonimi++);
       }
     }
     contaEsistenti.setText(""+contaTotaleOmonimi);
     CampoBaseOmonimi.setText(""+contaBaseOmonimi);
     CampoImportatiOmonimi.setText(""+contaImportatiOmonimi);
     CampoTotaleOmonimi.setText(""+contaTotaleOmonimi);
     contaMembri.setText(""+i);
    }

    public String fileChooser()
    {
        File f = null;
        String ricordaPath=null;
        try
        {
         MessageText.setText("");
         ricordaPath=PathField.getText();
         // impostazione della directory di partenza
         JFileChooser fileScelto = new JFileChooser(System.getProperty("user.home")+"/Desktop");
         // returnValore = fileScelto.showOpenDialog(null);


            // creazione di tre filtri
            FileFilter filtroCsv = new FileNameExtensionFilter("CSV file", "csv");
            FileFilter filtroTxt = new FileNameExtensionFilter("TXT file", "txt");
            FileFilter filtroXls = new FileNameExtensionFilter("XLS file", "xls");

            fileScelto.addChoosableFileFilter(filtroTxt); //fileScelto.addChoosableFileFilter(filtroTxt);
            fileScelto.setFileFilter(filtroXls); //di default filtro solo i file Csv (lo aggiunge in automatico alla combo sottostante)
            fileScelto.setFileFilter(filtroCsv); //di default filtro solo i file Csv (lo aggiunge in automatico alla combo sottostante)
            fileScelto.setSelectedFile(new File("")); //imposto di default il nome del file da cercare

            int selezione = fileScelto.showDialog(null, "Apri");
         if (selezione == JFileChooser.APPROVE_OPTION)
         {
            f=fileScelto.getSelectedFile();
         }
         else
         {
           FinestraDialogo.setVisible(false);
           FinestraDialogo.dispose();
           return null;
         }
        }
        catch(NullPointerException e)
        {
         FinestraDialogo.setVisible(false);
         FinestraDialogo.dispose();
        }
        return f.getAbsolutePath();
    }

    public void ordinaNomiDoppioni(DefaultTableModel tableRiga, int riga)
    {
     DefaultTableModel table = (DefaultTableModel) TabellaPrincipale.getModel();
     DefaultTableModel tableFlags = (DefaultTableModel) TabellaFlags.getModel();
     ArrayList listaNomi = new ArrayList(0);
     int i,posizione=0,c,conta=0;
     boolean ordinato=false;
     for(i=riga;table.getValueAt(i,14).equals(table.getValueAt(riga,14))&&table.getRowCount()-1!=i;i++)
     {
        if((table.getValueAt(i,0).equals(table.getValueAt(riga,0)))&&(table.getValueAt(i,14).equals(table.getValueAt(riga,14))))
         listaNomi.add(table.getValueAt(i,1));
     }
     /*Sono arrivato al termine della sequenza di nomi uguali ora eseguo l'ordinamento*/
     String[] nomi = new String[listaNomi.size()];
     for(i=0;i!=listaNomi.size();i++)
      nomi[i]= (String) listaNomi.get(i);
     Arrays.sort(nomi);

     c=0;
     posizione=riga;
     for(i=posizione;c!=listaNomi.size();i++)
     {
      if(!(nomi[c].equals(table.getValueAt(i,1))))
      {
        ordinato=false;
        conta=0;
        while(ordinato!=true)
        {
         if(nomi[c].equals(table.getValueAt(i+conta,1)))
         {
           table.moveRow(i+conta,i+conta,posizione+c);
           tableFlags.moveRow(i+conta,i+conta,posizione+c);
           TabellaPrincipale.scrollRectToVisible(TabellaPrincipale.getCellRect(posizione+c+1,0,true));
           ordinato=true;
         }
         conta++;
        }
      }
      c++;
     }
    }

    public void visualizzaSlide()
    {
      String[] SlideAggiuntaMembro = new String[2];
      String[] SlideCapireTabella = new String[1];
      String[] SlideEsportazione = new String[2];
      String[] SlideImportazione = new String[1];
      String[] SlideServerProblemi = new String[2];
      String[] SlideStrumentoOmonimi = new String[2];
      String[] SlideNonLeggibile = new String[1];

      ImageIcon icona = null;
      Image immagine = null;
      Image newimg = null;

      SlideAggiuntaMembro[0]="./src/accountmanager/SlideGuida/Slide_1.png";
      SlideAggiuntaMembro[1]="./src/accountmanager/SlideGuida/Slide_2.png";
      SlideCapireTabella[0]="./src/accountmanager/SlideGuida/Slide_3.png";
      SlideEsportazione[0]="./src/accountmanager/SlideGuida/Slide_11.png";
      SlideEsportazione[1]="./src/accountmanager/SlideGuida/Slide_4.png";
      SlideImportazione[0]="./src/accountmanager/SlideGuida/Slide_6.png";
      SlideStrumentoOmonimi[0]="./src/accountmanager/SlideGuida/Slide_8.png";
      SlideStrumentoOmonimi[1]="./src/accountmanager/SlideGuida/Slide_7.png";
      SlideServerProblemi[0]="./src/accountmanager/SlideGuida/Slide_9.png";
      SlideServerProblemi[1]="./src/accountmanager/SlideGuida/Slide_10.png";
      SlideNonLeggibile[0]="./src/accountmanager/SlideGuida/Slide_5.png";

      try
      {
        if(Sezione==0)
         icona = new ImageIcon(SlideAggiuntaMembro[NumeroSlide]);
        if(Sezione==1)
         icona = new ImageIcon(SlideCapireTabella[NumeroSlide]);
        if(Sezione==2)
         icona = new ImageIcon(SlideEsportazione[NumeroSlide]);
        if(Sezione==3)
         icona = new ImageIcon(SlideImportazione[NumeroSlide]);
        if(Sezione==4)
         icona = new ImageIcon(SlideStrumentoOmonimi[NumeroSlide]);
        if(Sezione==5)
         icona = new ImageIcon(SlideServerProblemi[NumeroSlide]);
        if(Sezione==6)
         icona = new ImageIcon(SlideNonLeggibile[NumeroSlide]);

        immagine = icona.getImage();
        newimg = immagine.getScaledInstance(760,410,java.awt.Image.SCALE_SMOOTH);
        icona = new ImageIcon(newimg);
        SlidePanel.setIcon(icona);
      }
      catch(ArrayIndexOutOfBoundsException e)
      {
       if(NumeroSlide<0)
        NumeroSlide++;
       else
        NumeroSlide--;
      }
      catch(NullPointerException e)
      {
       System.out.println("Nessuna immagine è presente in questa posizione: "+NumeroSlide+"");
      }
    }

    public void selezionaDipartimento()
    {
        MessageText.setText("");
        DefaultTableModel table = null;
        DefaultTableModel tableFlags = null;
        DefaultTableModel tableSupport = null;
        DefaultTableModel tableFlagsSupport = null;
        String classe=" Tutto";
        int riga,i=0,righeTable,contatoreAggiunti=0,contatoreEsistenti=0,contatoreOldOmonimi=0,contatoreNewOmonimi=0,contatoreAggiuntiManuale=0;

        table = (DefaultTableModel) TabellaPrincipale.getModel();
        tableSupport = (DefaultTableModel) jTable4.getModel();
        tableFlags = (DefaultTableModel) TabellaFlags.getModel();
        tableFlagsSupport = (DefaultTableModel) TabellaFlagsSupport.getModel();
        classe=(String) SceltaDipartimento.getSelectedItem();
        righeTable=table.getRowCount();

        try
        {
           //entro se scelgo un item diverso da " Tutto"
           /*VISUALIZZO APPLICANDO FILTRI SECONDO LA CLASSE*/
           if(!(classe.equals(" Tutto")))
           {
            PulisciTabellaButton.setEnabled(false);
            EliminaModifiche.setEnabled(false);
            while(tableFlags.getRowCount()>0)
             tableFlags.removeRow(0);

            while(table.getRowCount()>0)
             table.removeRow(0);

            for(riga=0;tableSupport.getRowCount()!=riga;riga++) //scrivo sulla tabella principale il contenuto della tabella di supporto
            {
             table.addRow(new Object[]
             {
                tableSupport.getValueAt(riga,0),tableSupport.getValueAt(riga,1),tableSupport.getValueAt(riga,2),tableSupport.getValueAt(riga,3),tableSupport.getValueAt(riga,4),tableSupport.getValueAt(riga,5),
                tableSupport.getValueAt(riga,6),tableSupport.getValueAt(riga,7),tableSupport.getValueAt(riga,8),tableSupport.getValueAt(riga,9),tableSupport.getValueAt(riga,10),tableSupport.getValueAt(riga,11),
                tableSupport.getValueAt(riga,12),tableSupport.getValueAt(riga,13),tableSupport.getValueAt(riga,14),tableSupport.getValueAt(riga,15)
             });
            }

            /*aggiungo la colonna dei flags*/
            for(riga=0;riga!=table.getRowCount();riga++)
              tableFlags.addRow(new Object[]{tableFlagsSupport.getValueAt(riga,0)});

            /*Eseguo operazione di filtraggio classi per la classe scelta*/
            for(riga=0;table.getRowCount()>0;)
            {
                 if(!classe.equals(table.getValueAt(riga,14))) //quando ha finito entra in eccezione
                 {
                    table.removeRow(i);
                    tableFlags.removeRow(i);
                 }
                 else
                 {
                    i++;
                    riga++;
                 }
            }
           }
           /*VISUALIZZO TUTTO SENZA FILTRI*/
           else
           {
            if((SceltaDipartimento.getSelectedItem().equals(" Tutto")||SceltaDipartimento.getSelectedItem().equals("Tutto"))&&((!PathField.getText().equals(""))))
             EliminaModifiche.setEnabled(true);
            else
             EliminaModifiche.setEnabled(false);
            PulisciTabellaButton.setEnabled(true);
            while(tableFlags.getRowCount()>0) //cancello la colonna dei flags
             tableFlags.removeRow(0);

            while(table.getRowCount()>0) //cancello i dati contenuti nella tabella di supporto esterna al form
             table.removeRow(0);

            for(riga=0;tableSupport.getRowCount()!=riga;riga++) //scrivo sulla tabella principale il contenuto della tabella di supporto
            {
             table.addRow(new Object[]
             {
                tableSupport.getValueAt(riga,0),tableSupport.getValueAt(riga,1),tableSupport.getValueAt(riga,2),tableSupport.getValueAt(riga,3),tableSupport.getValueAt(riga,4),tableSupport.getValueAt(riga,5),
                tableSupport.getValueAt(riga,6),tableSupport.getValueAt(riga,7),tableSupport.getValueAt(riga,8),tableSupport.getValueAt(riga,9),tableSupport.getValueAt(riga,10),tableSupport.getValueAt(riga,11),
                tableSupport.getValueAt(riga,12),tableSupport.getValueAt(riga,13),tableSupport.getValueAt(riga,14),tableSupport.getValueAt(riga,15)
             });
            }

            /*aggiungo la colonna dei flags*/
            for(riga=0;riga!=table.getRowCount();riga++)
              tableFlags.addRow(new Object[]{tableFlagsSupport.getValueAt(riga,0)});

            contaMembri();
           }

         for(riga=0;tableFlags.getRowCount()!=riga;riga++)
         {
            if(tableFlags.getValueAt(riga,0).equals("NEW")||tableFlags.getValueAt(riga,0).equals("NEW(omonimo)"))
             contatoreAggiunti++;
            if(tableFlags.getValueAt(riga,0).equals("OLD(omonimo)"))
             contatoreOldOmonimi++;
            if(tableFlags.getValueAt(riga,0).equals("NEW(omonimo)"))
             contatoreNewOmonimi++;
            if(tableFlags.getValueAt(riga,0).equals("NEW(omonimo)")||tableFlags.getValueAt(riga,0).equals("OLD(omonimo)"))
             contatoreEsistenti++;
            if(tableFlags.getValueAt(riga,0).equals("AGGIUNTO")||tableFlags.getValueAt(riga,0).equals("ESISTE"))
             contatoreAggiuntiManuale++;

         }
         contaAggiunti.setText(""+contatoreAggiunti);
         contaEsistenti.setText(""+contatoreEsistenti);
         CampoTotaleOmonimi.setText(""+contatoreEsistenti);
         CampoBaseOmonimi.setText(""+contatoreOldOmonimi);
         CampoImportatiOmonimi.setText(""+contatoreNewOmonimi);
         contaAggiuntiManuale.setText(""+contatoreAggiuntiManuale);

        inizializzazioneTabellaSupporto(righeTable,0,1);
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
         contaMembri();
         for(riga=0;tableFlags.getRowCount()!=riga;riga++)
         {
            if(tableFlags.getValueAt(riga,0).equals("NEW")||tableFlags.getValueAt(riga,0).equals("NEW(omonimo)"))
             contatoreAggiunti++;
            if(tableFlags.getValueAt(riga,0).equals("OLD(omonimo)"))
             contatoreOldOmonimi++;
            if(tableFlags.getValueAt(riga,0).equals("NEW(omonimo)"))
             contatoreNewOmonimi++;
            if(tableFlags.getValueAt(riga,0).equals("NEW(omonimo)")||tableFlags.getValueAt(riga,0).equals("OLD(omonimo)"))
             contatoreEsistenti++;
            if(tableFlags.getValueAt(riga,0).equals("AGGIUNTO")||tableFlags.getValueAt(riga,0).equals("ESISTE"))
             contatoreAggiuntiManuale++;
         }
         contaAggiunti.setText(""+contatoreAggiunti);
         contaEsistenti.setText(""+contatoreEsistenti);
         CampoTotaleOmonimi.setText(""+contatoreEsistenti);
         CampoBaseOmonimi.setText(""+contatoreOldOmonimi);
         CampoImportatiOmonimi.setText(""+contatoreNewOmonimi);
         contaAggiuntiManuale.setText(""+contatoreAggiuntiManuale);
        }
    }

    private void connectToDragDrop()
    {
     DragListener d=new DragListener(TabellaPrincipale,PathField,PathField,FinestraDialogo,jScrollPane11,AnteprimaText,MessageText,GifPicture,LeggiFileButton,ButtonImporta,PathFileDragDrop);
     new DropTarget(this,d); //listener
    }

    public void ordinaElencoClassi() throws InterruptedException
    {
     DefaultTableModel table = (DefaultTableModel) TabellaPrincipale.getModel();
     DefaultTableModel tableFlags = (DefaultTableModel) TabellaFlags.getModel();
     int i=0,c=0,conta=0,ampiezzaClassiIniziale=0,ampiezzaClassiFinale=0,contaLista,posizione,statoTabellaComparazione,dimensioneTabellaIniziale=table.getRowCount();
     List <String> listaMembriClasse=null;
     ArrayList dimensione= new ArrayList(0);

     /*DETERMINO LA DIMENSIONE DEI DUE ARRAY TRAMITE UN ARRAYLIST IL QUALE RICEVE UNA CLASSE PER CATEGORIA*/
     try
     {
      for(i=0;table.getRowCount()!=i;i++)
        if(!(table.getValueAt(i,14).equals(table.getValueAt(i+1,14))))
            dimensione.add(null);
     }
     catch(ArrayIndexOutOfBoundsException ex)
     {}

     /*CREO DUE ARRAY NOTARE LA DIMENSIONE*/
     String[] posizioneClassi = new String[dimensione.size()+1];
     int[] intervalliClassi= new int[(dimensione.size()+1)*2];

     /*ASSEGNO LA POSIZIONE DELLA PRIMA CLASSE PER OGNI CATEGORIA E GLI INTERVALLI*/
     try
     {
        i=0;
        posizione=0;
        while(table.getRowCount()!=i)
        {
            posizioneClassi[posizione]=(String) table.getValueAt(i,14); //segno nome prima classe
            intervalliClassi[c]=i; //segno posizione prima classe
            for(;table.getRowCount()!=i;i++)
            {
                if(!(table.getValueAt(i,14).equals(table.getValueAt(i+1,14))))
                {
                 c++;
                 intervalliClassi[c]=i+1; //segno posizione ultima classe
                 c++;
                 break;
                }
            }
            i++;
            posizione++;
        }
     }
     catch(ArrayIndexOutOfBoundsException ex)
     {
      c++;
      intervalliClassi[c]=i+1; //segno posizione ultima classe
     }
     finally
     {
        /*ESEGUO ORDINAMENTO POSIZIONECLASSI CONTIENE SOLO I NOMI DELLE CLASSI*/
        Arrays.sort(posizioneClassi);

        /*AGGIUNGO IN FONDO ALLA TABELLA TUTTE LE CLASSI IN ORDINE*/
        for(posizione=0;posizioneClassi.length>posizione;posizione++) //leggo la prima classe contenuta nella prima cella dell'array
        {
            if(StatoThreadReader==false)
                throw new InterruptedException();
            conta=0;
            listaMembriClasse = new ArrayList <String>();//determino dimensione lista membri di un dipartimento singolo alla volta

            for(i=0;intervalliClassi.length>i;i=i+2) //leggo la prima classe contenuta nella prima cella dell'array (celle pari=prima classe, celle dispari=ultima classe)
            {
              if(posizioneClassi[posizione].equals(table.getValueAt(intervalliClassi[i],14))) //comparo la classe dentro l'array ordinato con le classi della tabella a partire dalla posizione del primo intervallo
              {
                /*ORDINO CLASSI*/
                for(c=intervalliClassi[i];intervalliClassi[i+1]!=c;c++) //aggiungo le righe ordinatamente, il c serve per scalare i membri del dipartimento
                {
                   conta++;
                   listaMembriClasse.add((String)table.getValueAt(c,0));
                   //table.moveRow(table.getRowCount()-1,c,c);
                   //tableFlags.moveRow(tableFlags.getRowCount()-1,c,c);
                   table.addRow(new Object[]{table.getValueAt(c,0),table.getValueAt(c,1),table.getValueAt(c,2),table.getValueAt(c,3),table.getValueAt(c,4),table.getValueAt(c,5),table.getValueAt(c,6),table.getValueAt(c,7),table.getValueAt(c,8),table.getValueAt(c,9),table.getValueAt(c,10),table.getValueAt(c,11),table.getValueAt(c,12),table.getValueAt(c,13),table.getValueAt(c,14),table.getValueAt(c,15)});
                   tableFlags.addRow(new Object[]{tableFlags.getValueAt(c,0)});
                }

                /*ORDINO COGNOMI*/
                Collections.sort(listaMembriClasse);
                contaLista=0;

                for(c=intervalliClassi[i];intervalliClassi[i+1]!=c;c++) //aggiungo le righe ordinatamente, il c serve per scalare i membri del dipartimento
                {
                   ampiezzaClassiFinale=listaMembriClasse.size();
                   statoTabellaComparazione=dimensioneTabellaIniziale+ampiezzaClassiFinale;

                   for(conta=dimensioneTabellaIniziale+ampiezzaClassiIniziale;statoTabellaComparazione!=conta;conta++) //scandisco la tabella
                   {
                    if(listaMembriClasse.get(contaLista).equals(table.getValueAt(conta,0)))
                    {
                       contaLista++;
                       table.addRow(new Object[]{table.getValueAt(conta,0),table.getValueAt(conta,1),table.getValueAt(conta,2),table.getValueAt(conta,3),table.getValueAt(conta,4),table.getValueAt(conta,5),table.getValueAt(conta,6),table.getValueAt(conta,7),table.getValueAt(conta,8),table.getValueAt(conta,9),table.getValueAt(conta,10),table.getValueAt(conta,11),table.getValueAt(conta,12),table.getValueAt(conta,13),table.getValueAt(conta,14),table.getValueAt(conta,15)});
                       tableFlags.addRow(new Object[]{tableFlags.getValueAt(conta,0)});
                       table.removeRow(conta);
                       tableFlags.removeRow(conta);
                       statoTabellaComparazione--;
                       break;
                    }
                   }
                }
                ampiezzaClassiIniziale=ampiezzaClassiIniziale+listaMembriClasse.size();
                break;
              }
              else
              {}
            }
        }

        /*ELIMINO TUTTE LE RIGHE NON ORDINATE AL DI SOPRA DELLE RIGHE ORDINATE*/
        for(i=0;dimensioneTabellaIniziale!=i;i++)
        {
          table.removeRow(0);
          tableFlags.removeRow(0);
        }
     }
    }

    public void visualizzaAnteprima(String path,JTextArea component)
    {
     IDThreadAnteprima = new Thread()
     {
      @Override
      public void run()
      {
        BufferedReader br = null;
        String pathFile = path; //prendo il path del file scelto
        String line = null;
        JTextArea textArea = component;

        textArea.setText("");
        textArea.setForeground(Color.BLACK);
        try
        {
         int controlloCicli=0;
         File file = new File(pathFile);
         br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
            /*NEL TRY E' ESEGUITA LA LETTURA PER I FILE STANDARDIZZATI*/
            try
            {
                while ((line=br.readLine())!=null)
                {

                    textArea.append(line+"\n");
                    controlloCicli++;
                }
                /*Se il contatore di righe lette rimane a zero significa che il file è vuoto*/
                if(controlloCicli==0)
                {
                    textArea.setText("Il File è vuoto");
                    textArea.setForeground(Color.RED);
                }
            }
            catch(ArrayIndexOutOfBoundsException ex)
            {}
         textArea.setCaretPosition(0);
        }
        catch (FileNotFoundException e)
        {
            textArea.setText("FILE NON TROVATO");
            textArea.setForeground(Color.RED);
        }
        catch (IOException e)
        {
            textArea.setText("Errore I/O");
            textArea.setForeground(Color.RED);
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
                    textArea.setText("Errore I/O");
                    textArea.setForeground(Color.RED);
                }
            }
        }
      }
     };
     IDThreadAnteprima.start();
    }

    protected void initUI()
    {
        Image icon = Toolkit.getDefaultToolkit().getImage("./src/accountmanager/img/CSV.png");
        Guida = new JFrame("Guida");
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon icona = null;
        Image immagine = null;
        Image newimg = null;

        icona = new ImageIcon("./src/accountmanager/img/sfondo_guida1.jpg");
        immagine = icona.getImage();
        newimg = immagine.getScaledInstance(990,800,java.awt.Image.SCALE_SMOOTH);
        icona = new ImageIcon(newimg);
        //final ImageIcon backgroundImage = new ImageIcon("./src/accountmanager/sfondo.jpg");
        SfondoGuida= new JLabel(icona)
        {
            @Override
            public Dimension getPreferredSize()
            {
                Dimension size = super.getPreferredSize();
                Dimension lmPrefSize = getLayout().preferredLayoutSize(this);
                size.width = Math.max(size.width, lmPrefSize.width);
                size.height = Math.max(size.height, lmPrefSize.height);
                return size;
            }
        };
        SfondoGuida.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0,0,0,0);
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        SfondoGuida.add(Panel_Guida,gbc);
        // Let's put a filler bottom component that will push the rest to the top
        gbc.weighty = 1.0;
        SfondoGuida.add(Box.createGlue(),gbc);

        icona = new ImageIcon("./src/accountmanager/SlideGuida/Slide1.png");
        immagine = icona.getImage();
        newimg = immagine.getScaledInstance(760,410,java.awt.Image.SCALE_SMOOTH);
        icona = new ImageIcon(newimg);
        SlidePanel.setIcon(icona);
        OptionSlide.setText("Visualizza slide");
        Guida.setSize(990,370);
        Panel_Guida.setSize(990,370);
        Guida.setResizable(false);
        Guida.add(SfondoGuida);
        Guida.pack();
        Guida.setIconImage(icon);
        Guida.setLocationRelativeTo(null);
        Guida.setVisible(true);
    }

    public void fileReader()
    {
     Thread x = new Thread()
     {
      @Override
      public void run()
      {
        CancellaRigaButton.setEnabled(true);
        EsportaModificheButton.setEnabled(true);
        EliminaModifiche.setEnabled(true);
        PulisciTabellaButton.setEnabled(true);
        CheckRadioEmail.setEnabled(true);
        ButtonImporta.setEnabled(false);
        MessageText.setText("");
        contaAggiunti.setText("0");
        contaEsistenti.setText("0");
        CampoBaseOmonimi.setText("0");
        CampoImportatiOmonimi.setText("0");
        CampoMembriImportati.setText("0");
        CampoNumeroFiles.setText("0");
        CampoNoDipartimento.setText("0");
        CampoNumeroDipartimenti.setText("0");
        contaAggiuntiManuale.setText("0");
        contaSenzaDipartimento=0;
        StatoThreadReader=true;

        BufferedReader br = null;
        DefaultTableModel table = null;
        DefaultTableModel tableFlags = null;
        DefaultTableModel tableFile = null;
        DefaultTableModel tableOmonimi = null;
        DefaultTableModel tableNumerazione = null;

        String pathFile = PathField.getText(); //prendo il path del file scelto
        if(!PathFileDragDrop.getText().equals(""))
        {
         pathFile=PathFileDragDrop.getText();
         PathField.setText(pathFile);
        }

        String line = null;
        String csvSplitBy = TextFieldAltro.getText();
        int colonna=0,i=0,righeTable=0,numeroCampi=0,righeNonInserite=0,bar=0,contaBaseOmonimi=0,omonimiOld=0;
        double progress=0,riga=0;
        boolean rigaSaltata=false;

        if(TextFieldAltro.getText().contains("+")||TextFieldAltro.getText().contains("|")||TextFieldAltro.getText().contains("?")||TextFieldAltro.getText().contains("^")||TextFieldAltro.getText().contains("*")
         ||TextFieldAltro.getText().contains(".")||TextFieldAltro.getText().contains("$")||TextFieldAltro.getText().contains("(")||TextFieldAltro.getText().contains(")")||TextFieldAltro.getText().contains("{")
         ||TextFieldAltro.getText().contains("}")||TextFieldAltro.getText().contains("[")||TextFieldAltro.getText().contains("]"))
        {
         TextFieldAltro.setText("");
         MessageText.setText("+|?^*.$()[]{} non sono utilizzabili come split");
         allarmGif();
         return;
        }

        try
        {
         FinestraDialogo.setVisible(false);
         FinestraDialogo.dispose();

        if(CheckTabulazione.isSelected()||CheckVirgola.isSelected()||CheckPuntoVirgola.isSelected()||CheckSlash.isSelected()||CheckUnderscore.isSelected()||CheckHash.isSelected()||CheckSpazio.isSelected()||CheckAltro.isSelected())
        {
         FrameLoad.setVisible(true);
         FrameLoad.setLocationRelativeTo(null);

         table = (DefaultTableModel) TabellaPrincipale.getModel();
         tableFlags = (DefaultTableModel) TabellaFlags.getModel();
         tableFile = (DefaultTableModel) TabellaFile.getModel();
         tableOmonimi = (DefaultTableModel) TabellaOmonimi.getModel();
         tableNumerazione = (DefaultTableModel) TabellaNumerazione.getModel();

         righeTable=table.getRowCount();//indico lo stato iniziale della tabella principale prima che venga scancellata

         /*Reinizializzo le tabelle ripulendole per una nuova lettura*/
          while (table.getRowCount()>0)
          {
           table.removeRow(0);
           tableNumerazione.removeRow(0);
           tableFlags.removeRow(0);
          }
          while(tableFile.getRowCount()>0)
           tableFile.removeRow(0);

        File file = new File(pathFile);
        TextInfo.setText("Sto leggendo: "+file.getName());
        TextFileBase.setText(file.getName());
        bar= (int) file.length();
        br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"ISO-8859-1"));
        String[] campi = null;
        String[] generiCampi = null;
        int controlloCicli=0,contaRighe=0,rigaCancellata=0;//variabile per verificare se il file è vuoto

            /*NEL TRY E' ESEGUITA LA LETTURA PER I FILE STANDARDIZZATI*/
            try
            {
                while ((line=br.readLine())!=null)
                {
                   if(StatoThreadReader==false)
                     throw new InterruptedException();
                   try
                   {
                    riga=line.length();
                    progress=progress+(100*(riga/bar));
                    ProgressBar.setValue((int)progress);
                    TextLoad.setText("Load: "+line);
                    controlloCicli++;
                    campi = line.split(csvSplitBy,16); //questo metodo separa una stringa in celle in un array facendo riferimento ad un carattere separatore scelto
                    numeroCampi=campi.length;
                        if(numeroCampi<2) //verifico se ci sono intestazioni inutili o righe vuote
                         throw new NullPointerException();
                        if(numeroCampi<16) //verifico se il file in questione è di tipo standard nuovo[16 colonne]
                         throw new ArrayIndexOutOfBoundsException();

                    campi[0]=campi[0].toUpperCase().trim().replaceAll(" {2,}", " ");//le operazioni di MAIUSC e eliminazione degli spazi si effettua qui perchè devo eliminare l'intestazione

                    if("NATO IL".equals(campi[0])||"COGNOME".equals(campi[0])||"NOME".equals(campi[0])||"CLASSE".equals(campi[0])||"LAST NAME".equals(campi[0])||"FIRST NAME".equals(campi[0])||"DEPARTMENT".equals(campi[0]))
                    {}
                    else
                    {
                      if(campi[0].equals(null)||campi[1].equals(null)||campi[0].equals("")||campi[1].equals("")) //verifico se al campo nome,cognome vi è contenuto, se falso salto la riga e lo segnalo
                      throw new IllegalArgumentException();

                     tableFlags.addRow(new Object[]{"OLD"});

                     for(i=0;table.getRowCount()!=i;i++)
                     {
                        if(campi[0].equals(table.getValueAt(i,0))&&campi[1].equals(table.getValueAt(i,1))&&(!tableFlags.getValueAt(i,0).equals("OLD(omonimo)")&&!tableFlags.getValueAt(i,0).equals("NEW(omonimo)")))
                        {
                          tableFlags.setValueAt("OLD(omonimo)",i,0);
                          contaBaseOmonimi++;
                          omonimiOld++;
                        }
                     }

                     table.addRow(new Object[]{campi[0].toUpperCase().trim().replaceAll(" {2,}", " "),campi[1].toUpperCase().trim().replaceAll(" {2,}", " "),campi[2],campi[3],campi[4],campi[5],campi[6],campi[7],campi[8],campi[9],campi[10],campi[11],campi[12],campi[13],campi[14].toUpperCase().trim(),campi[15]});

                     if(campi[14].equals("")||campi[14].equals(null))
                     {
                      contaSenzaDipartimento++;
                      CampoNoDipartimento.setText(""+contaSenzaDipartimento);
                     }
                    }
                   }
                   catch(NullPointerException e)
                   {}
                   catch(IllegalArgumentException e)
                   {
                    righeNonInserite++;
                    rigaSaltata=true;
                   }
                }

                if(rigaSaltata==true)
                {
                    MessageText.setText("Saltate "+righeNonInserite+" righe che non hanno nome/cognome oppure sono omonimi");
                }
                /*Se il contatore di righe lette rimane a zero significa che il file è vuoto*/
                if(controlloCicli==0)
                {
                    EliminaModifiche.setEnabled(false);
                    MessageText.setText("Il File è vuoto");
                    allarmGif();
                }

                try
                {
                 ragruppaClassi(); //raggruppo eventuali classi sparse
                }
                catch(ArrayIndexOutOfBoundsException ex) //se non esistono classi l'operazione si interrompe
                {}

                /*Inserisco per ogni nuova riga creata nella tabella principale una riga corrispettiva per la tabella dei flags*/
                /*for(i=0;i!=table.getRowCount();i++)
                    tableFlags.addRow(new Object[]{""});*/
            }
            /*NEL CATCH E' ESEGUITA LA LETTURA PER I FILE NON STANDARDIZZATI*/
            /*Se entro in questa eccezione significa che il file letto è di una forma standardizzata conosciuta e specifica per il caso in questione*/
            catch(ArrayIndexOutOfBoundsException ex)
            {
                String cognome,nome,dipartimento;

                while(line!=null) //qui non c'è il comando per leggere la riga in quanto una riga è gia stata letta precedentemente
                {
                 if(StatoThreadReader==false)
                    throw new InterruptedException();
                 try
                 {
                  if(line.substring(0,1).equals(csvSplitBy))
                   line=line.replaceFirst(csvSplitBy,"");
                 }
                 catch(Exception e) //nel caso fossero riscontrate righe vuote si salta questo controllo
                 {}
                   riga = line.length();
                   progress=progress+(100*(riga/bar));
                   ProgressBar.setValue((int)progress);
                   TextLoad.setText("Load: "+line);
                   campi = line.split(csvSplitBy); //questo metodo separa una stringa in celle in un array facendo riferimento ad un carattere separatore scelto
                   try
                   {
                    numeroCampi=campi.length;
                        if(numeroCampi<2)
                            throw new ArrayIndexOutOfBoundsException();

                    campi[0]=campi[0].toUpperCase().trim().replaceAll(" {2,}", " ");//le operazioni di MAIUSC e eliminazione degli spazi si effettua qui perchè devo eliminare l'intestazione
                    if("NATO IL".equals(campi[0])||"COGNOME".equals(campi[0])||"NOME".equals(campi[0])||"CLASSE".equals(campi[0])||"LAST NAME".equals(campi[0])||"FIRST NAME".equals(campi[0])||"DEPARTMENT".equals(campi[0]))
                    {
                        generiCampi = line.split(csvSplitBy,300);
                        line=br.readLine();
                    }
                    else
                    {
                        numeroCampi=campi.length;
                        if(numeroCampi<2)
                         throw new ArrayIndexOutOfBoundsException();

                        table.addRow(new Object[]{"","","","","","","","","","","","","","","",""});
                        campi = line.split(csvSplitBy,300); //questo metodo separa una stringa in celle in un array facendo riferimento ad un carattere separatore scelto

                        for(colonna=0;generiCampi.length!=colonna||generiCampi==null;colonna++)
                          ordinaCampi(campi,generiCampi,table,colonna,contaRighe);

                        if(table.getValueAt(table.getRowCount()-1,0).equals(null)||table.getValueAt(table.getRowCount()-1,1).equals(null)||
                           table.getValueAt(table.getRowCount()-1,0).equals("")||table.getValueAt(table.getRowCount()-1,1).equals("")) //verifico se al campo nome,cognome vi è contenuto, se falso salto la riga e lo segnalo
                            throw new IllegalArgumentException();

                        tableFlags.addRow(new Object[]{"OLD"});

                        for(i=0;table.getRowCount()-1!=i;i++)
                        {
                         if(table.getValueAt(table.getRowCount()-1,0).equals(table.getValueAt(i,0))&&table.getValueAt(table.getRowCount()-1,1).equals(table.getValueAt(i,1))&&(!tableFlags.getValueAt(i,0).equals("OLD(omonimo)")&&!tableFlags.getValueAt(i,0).equals("NEW(omonimo)")))
                         {
                          tableFlags.setValueAt("OLD(omonimo)",i,0);
                          cognome=(String) table.getValueAt(table.getRowCount()-1,0);
                          nome=(String) table.getValueAt(table.getRowCount()-1,1);
                          dipartimento=(String) table.getValueAt(table.getRowCount()-1,14);
                          contaBaseOmonimi++;
                          omonimiOld++;
                         }
                        }
                        if(table.getValueAt(table.getRowCount()-1,14).equals("")||table.getValueAt(table.getRowCount()-1,14).equals(null))
                        {
                         contaSenzaDipartimento++;
                         CampoNoDipartimento.setText(""+contaSenzaDipartimento);
                        }
                        contaRighe++;
                        line=br.readLine(); //un readLine è stato eseguito nel try per questo questa istruzione si trova in fondo
                    }
                   }
                   catch(ArrayIndexOutOfBoundsException e)
                   {
                       line=br.readLine(); //un readLine è stato eseguito nel try per questo questa istruzione si trova in fondo [correzione]
                   }
                   catch(NullPointerException e)
                   {
                       //contaRighe++;
                       line=br.readLine(); //un readLine è stato eseguito nel try per questo questa istruzione si trova in fondo [correzione]
                   }
                   catch(IllegalArgumentException e)
                   {
                    table.removeRow(table.getRowCount()-1); //la riga errata è stata ormai inserita quindi va eliminata, si trova in fondo alla tabella
                    righeNonInserite++;
                    rigaSaltata=true;
                    line=br.readLine();
                   }
                    //un readLine è stato eseguito nel try per questo questa istruzione si trova in fondo [correzione]
                    //eseguito nel caso di split errati
                }
                if(rigaSaltata==true)
                {
                 MessageText.setText("Saltate "+righeNonInserite+" righe che non hanno nome/cognome");
                }
                try
                {
                 ragruppaClassi(); //raggruppo eventuali classi sparse
                }
                catch(ArrayIndexOutOfBoundsException e) //se non esistono classi l'operazione si interrompe
                {}

                /*Creo lo spazio per i flags*/
                /*for(i=0;i!=table.getRowCount();i++)
                  tableFlags.addRow(new Object[]{""});*/
            }


            try
            {
             ordinaElencoClassi();
            }
            catch(ArrayIndexOutOfBoundsException ex) //esiste per risolvere quel problema relativo allo split errato
            {
              while (table.getRowCount()>0)  //eseguo questa istruzione per entrare nell'if sotto
                table.removeRow(0);
            }
            contaMembri();
            creaItems();
            inizializzazioneTabellaSupporto(righeTable,rigaCancellata,1);
            CampoBaseOmonimi.setText(""+contaBaseOmonimi);
            contaEsistenti.setText(""+omonimiOld);
            TickGif();
            ProgressBar.setValue(100-(int) ProgressBar.getPercentComplete());
            FrameLoad.setVisible(false);
            if(controlloCicli==0) //risoluzione bug grafico
             allarmGif();
            if(table.getRowCount()==0||table.getValueAt(0,0)==""||table.getValueAt(0,0)==null||
               table.getValueAt(0,1)==""||table.getValueAt(0,1)==null||table.getValueAt(0,14)==""||table.getValueAt(0,14)==null)
            {
               while (table.getRowCount()>0)
                table.removeRow(0);
               while (tableFlags.getRowCount()>0)
                tableFlags.removeRow(0);
               contaMembri();
               EliminaModifiche.setEnabled(false);
               if(controlloCicli!=0) //risoluzione bug grafico
               {
                FinestraDialogo.setVisible(true);
                if(!PathFileDragDrop.getText().equals(""))
                    ButtonImporta.setEnabled(true);
                MessageText.setText("Il file non è leggibile! CAUSE: split errato /insufficienti parametri /assenza intestazione");
                allarmGif();
               }
            }
        }
        else
        {
            EliminaModifiche.setEnabled(false);
            MessageText.setText("Nessun separatore è stato scelto");
            allarmGif();
        }
        }
        catch (InterruptedException ex)
        {

        }
        catch (FileNotFoundException e)
        {
            EliminaModifiche.setEnabled(false);
            MessageText.setText("FILE NON TROVATO");
            allarmGif();
        }
        catch (IOException e)
        {
            EliminaModifiche.setEnabled(false);
            MessageText.setText("Errore I/O");
            allarmGif();
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
                    EliminaModifiche.setEnabled(false);
                    MessageText.setText("Errore I/O");
                    allarmGif();
                }
            }
        }
      }
     };
     x.start();
     IDThreadReader=x;
    }

    public void importFile()
    {
     IDThreadImportaFile = new Thread()
     {
       @Override
       public void run()
       {
        MessageText.setText("");
        LeggiFileButton.setEnabled(false);
        if((SceltaDipartimento.getSelectedItem().equals(" Tutto")||SceltaDipartimento.getSelectedItem().equals("Tutto"))&&((!PathField.getText().equals(""))))
         EliminaModifiche.setEnabled(true);
        else
         EliminaModifiche.setEnabled(false);

        DefaultTableModel table = (DefaultTableModel) TabellaPrincipale.getModel();
        DefaultTableModel tableSupportImport = (DefaultTableModel) TabellaEsterna.getModel();
        DefaultTableModel tableFlags = (DefaultTableModel) TabellaFlags.getModel();
        DefaultTableModel tableFile = (DefaultTableModel) TabellaFile.getModel();

        BufferedReader br = null;
        String line = null;
        String csvSplitBy =  TextFieldAltro.getText();
        String fileName = null;
        String nome = null;
        String cognome = null;
        String classe = null;
        String dipartimento = null;
        String[] generiCampi = null;
        Boolean controllo = false;
        Boolean avviso = false;
        Boolean rigaSaltata=false;
        int righeNonInserite=0,bar=0,contaTotaleOmonimi=0,i=0,y=0,controlloCicli=0,contaMembriSingoloFile=0,numeroCampi=0,colonna=0,contaRighe=0;

        double progress=0,riga=0;
        MessageText.setText("");

        String pathFile = PathFileImport; //prendo il path del file scelto
        if(!PathFileDragDrop.getText().equals(""))
        {
         pathFile=PathFileDragDrop.getText();
        }

        if(TextFieldAltro.getText().contains("+")||TextFieldAltro.getText().contains("|")||TextFieldAltro.getText().contains("?")||TextFieldAltro.getText().contains("^")||TextFieldAltro.getText().contains("*")
         ||TextFieldAltro.getText().contains(".")||TextFieldAltro.getText().contains("$")||TextFieldAltro.getText().contains("(")||TextFieldAltro.getText().contains(")")||TextFieldAltro.getText().contains("{")
         ||TextFieldAltro.getText().contains("}")||TextFieldAltro.getText().contains("[")||TextFieldAltro.getText().contains("]"))
        {
         TextFieldAltro.setText("");
         MessageText.setText("+|?^*.$()[]{} non sono utilizzabili come split");
         allarmGif();
         return;
        }

        try
        {
            FileMenu.setPopupMenuVisible(false);
            FinestraDialogo.setVisible(false);
            FinestraDialogo.dispose();

            if(TabellaPrincipale.getRowCount()!=0)//non è possibile importare file su ua tabella vuota
            {
            if(SceltaDipartimento.getSelectedItem().equals(" Tutto")||SceltaDipartimento.getSelectedItem().equals("Tutto")) //non è possibile importare file su ua tabella con filtri attivati
            {
            if(CheckTabulazione.isSelected()||CheckVirgola.isSelected()||CheckPuntoVirgola.isSelected()||CheckSlash.isSelected()||CheckUnderscore.isSelected()||CheckHash.isSelected()||CheckSpazio.isSelected()||CheckAltro.isSelected())
            {

             /*CONTROLLO IMPORTAZIONE DEL FILE*/
             File file2 = new File(pathFile);
             if(TextFileBase.getText().equals(file2.getName()))
               throw new IllegalArgumentException();

             for(i=0;tableFile.getRowCount()!=i;i++)
              if(pathFile.equals(tableFile.getValueAt(i,1)))
               throw new IllegalArgumentException();

             /*LETTURA DATI*/
             bar=(int) file2.length();
             ProgressBar.setValue(0);
             FrameLoad.setVisible(true);
             FrameLoad.setLocationRelativeTo(null);
             File file = new File(pathFile);
             TextInfo.setText("Sto importando: "+file.getName());
             br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"ISO-8859-1"));
             String[] campi = null;
             i=0;
             int righeTable=0,rigaCancellata=0;//variabile per verificare se il file è vuoto
             righeTable=table.getRowCount();

             /*INIZIALIZZAZIONE TABELLE*/
                while(tableSupportImport.getRowCount()>0) //cancello i dati contenuti nella tabella di supporto esterna al form
                    tableSupportImport.removeRow(0);

                contaMembriSingoloFile=contaMembriImportati; //impiegato per ricavare il numero esatto di membri del singolo file importato
                try
                {
                    /*LETTURA FILE*/
                    while((line=br.readLine())!=null)
                    {
                       riga=line.length();
                       progress=progress+(100*(riga/bar));
                       ProgressBar.setValue((int)progress);
                       TextLoad.setText("Load: "+line);
                       controlloCicli++;
                       controllo=false;
                       campi = line.split(csvSplitBy,16); //questo metodo separa una stringa in celle in un array facendo riferimento ad un carattere separatore scelto
                       numeroCampi=campi.length; //verifico se il file in questione è di tipo standard standard nuovo[16 colonne], va in eccezione se non trova la 16° colonna
                        if(numeroCampi<16||numeroCampi>16) //verifico se il file in questione è di tipo standard nuovo[16 colonne]
                         throw new ArrayIndexOutOfBoundsException();

                       try
                       {
                         tableSupportImport.addRow(new Object[]{campi[0],campi[1],campi[2],campi[3],campi[4],campi[5],campi[6],campi[7],campi[8],campi[9],campi[10],campi[11],campi[12],campi[13],campi[14].trim(),campi[15]});
                         nome = (String) tableSupportImport.getValueAt(y,1);
                         cognome = (String) tableSupportImport.getValueAt(y,0);
                         classe = (String) tableSupportImport.getValueAt(y,14);

                         if(campi[0].equals(null)||campi[1].equals(null)||campi[14].equals(null)||campi[0].equals("")||campi[1].equals("")||campi[14].equals("")) //verifico se al campo nome,cognome e classe vi è contenuto, se falso salto la riga e lo segnalo
                             throw new IllegalArgumentException();

                         contaMembriImportati=contaMembriImportati+ordinaStringaDati(y); //permette di contare solo i NEW esclusi omonimi
                       }
                       catch(ArrayIndexOutOfBoundsException e)
                       {
                         System.out.println("ECCEZIONE");//qui si va in eccezione (per il file lista_studenti.csv) solo nelle prime sette righe e quando si superano le quattro colonne del file csv
                       }
                       catch(IllegalArgumentException e)
                       {
                        righeNonInserite++;
                        rigaSaltata=true;
                       }
                     y++; //prossima riga di TabellaEsterna tableSupportImport
                    }
                    if(rigaSaltata==true)
                    {
                        MessageText.setText("Saltate "+righeNonInserite+" righe che non hanno nome/cognome");
                    }
                    if(controlloCicli==0)
                    {
                        MessageText.setText("Il File è vuoto");
                        allarmGif();
                        avviso=true;
                    }
                }

            /*SEZIONE PER L'IMPORTAZIONE DI FILE NON STANDARDIZZATI*/
            /**************************************************************************************************************************************************************/

                /*NEL CATCH E' ESEGUITA LA LETTURA PER I FILE NON STANDARDIZZATI*/
                /*Se entro in questa eccezione significa che il file letto è di una forma standardizzata conosciuta e specifica per il caso in questione*/
                catch(ArrayIndexOutOfBoundsException ex)
                {
                    controlloCicli=0;
                    while(line!=null) //qui non c'è il comando per leggere la riga in quanto una riga è gia stata letta precedentemente
                    {
                     try
                     {
                      if(line.substring(0,1).equals(csvSplitBy))
                       line=line.replaceFirst(csvSplitBy,"");
                     }
                     catch(Exception e) //nel caso fossero riscontrate righe vuote si salta questo controllo
                     {}
                       riga = line.length();
                       progress=progress+(100*(riga/bar));
                       ProgressBar.setValue((int)progress);
                       TextLoad.setText("Load: "+line);
                       campi = line.split(csvSplitBy); //questo metodo separa una stringa in celle in un array facendo riferimento ad un carattere separatore scelto
                       try
                       {
                        numeroCampi=campi.length;
                            if(numeroCampi<2)
                                throw new ArrayIndexOutOfBoundsException();

                        campi[0]=campi[0].toUpperCase().trim().replaceAll(" {2,}", " ");//le operazioni di MAIUSC e eliminazione degli spazi si effettua qui perchè devo eliminare l'intestazione
                        if("NATO IL".equals(campi[0])||"COGNOME".equals(campi[0])||"NOME".equals(campi[0])||"CLASSE".equals(campi[0])||"LAST NAME".equals(campi[0])||"FIRST NAME".equals(campi[0])||"DEPARTMENT".equals(campi[0]))
                        {
                            generiCampi = line.split(csvSplitBy,300);
                            line=br.readLine();
                        }
                        else
                        {
                            numeroCampi=campi.length;
                            if(numeroCampi<2)
                             throw new ArrayIndexOutOfBoundsException();

                            tableSupportImport.addRow(new Object[]{"","","","","","","","","","","","","","","",""});
                            campi = line.split(csvSplitBy,300); //questo metodo separa una stringa in celle in un array facendo riferimento ad un carattere separatore scelto

                            for(colonna=0;generiCampi.length!=colonna||generiCampi==null;colonna++)
                              ordinaCampi(campi,generiCampi,tableSupportImport,colonna,contaRighe);

                            if(tableSupportImport.getValueAt(tableSupportImport.getRowCount()-1,0).equals(null)||tableSupportImport.getValueAt(tableSupportImport.getRowCount()-1,1).equals(null)||
                               tableSupportImport.getValueAt(tableSupportImport.getRowCount()-1,0).equals("")||tableSupportImport.getValueAt(tableSupportImport.getRowCount()-1,1).equals("")) //verifico se al campo nome,cognome vi è contenuto, se falso salto la riga e lo segnalo
                                throw new IllegalArgumentException();

                            contaMembriImportati=contaMembriImportati+ordinaStringaDati(y); //permette di contare solo i NEW esclusi omonimi
                            controlloCicli++;

                            if(tableSupportImport.getValueAt(tableSupportImport.getRowCount()-1,14).equals("")||tableSupportImport.getValueAt(tableSupportImport.getRowCount()-1,14).equals(null))
                            {
                             contaSenzaDipartimento++;
                             CampoNoDipartimento.setText(""+contaSenzaDipartimento);
                            }
                            contaRighe++;
                            line=br.readLine(); //un readLine è stato eseguito nel try per questo questa istruzione si trova in fondo
                            y++; //prossima riga di TabellaEsterna tableSupportImport
                        }
                       }
                       catch(ArrayIndexOutOfBoundsException e)
                       {
                           line=br.readLine(); //un readLine è stato eseguito nel try per questo questa istruzione si trova in fondo [correzione]
                       }
                       catch(NullPointerException e)
                       {
                           //contaRighe++;
                           line=br.readLine(); //un readLine è stato eseguito nel try per questo questa istruzione si trova in fondo [correzione]
                       }
                       catch(IllegalArgumentException e)
                       {
                        tableSupportImport.removeRow(tableSupportImport.getRowCount()-1); //la riga errata è stata ormai inserita quindi va eliminata, si trova in fondo alla tabella
                        righeNonInserite++;
                        rigaSaltata=true;
                        line=br.readLine();
                       }
                        //un readLine è stato eseguito nel try per questo questa istruzione si trova in fondo [correzione]
                        //eseguito nel caso di split errati
                    }
                    if(controlloCicli==0)
                    {
                     avviso=true;
                    }
                    if(rigaSaltata==true)
                    {
                     MessageText.setText("Saltate "+righeNonInserite+" righe che non hanno nome/cognome");
                    }
                }

             /********************************************************************************************************************************************/

                if(avviso==false) //risoluzione problema grafico
                {
                 ordinaElencoClassi();
                 contaMembri();
                 CampoImportatiOmonimi.setText(""+contaImportatiOmonimi); //il conteggio è eseguito dentro la funzione ordinaStringaDati
                 CampoMembriImportati.setText(""+contaMembriImportati); //il conteggio è effettuato mediante il return della funzione ordinaStringaDati
                 contaAggiunti.setText(""+contaMembriImportati);
                 inizializzazioneTabellaSupporto(righeTable,rigaCancellata,1);
                 creaItems();
                 SalvaButton.setEnabled(true);

                 CampoTotaleOmonimi.setText("0");
                 for(i=0;table.getRowCount()!=i;i++)
                 {
                  String x = (String) tableFlags.getValueAt(i,0);
                  if(tableFlags.getValueAt(i,0).equals("OLD(omonimo)")||tableFlags.getValueAt(i,0).equals("NEW(omonimo)"))
                   CampoTotaleOmonimi.setText(""+contaTotaleOmonimi++);
                 }
                 CampoTotaleOmonimi.setText(""+contaTotaleOmonimi);
                 contaEsistenti.setText(""+contaTotaleOmonimi);
                 tableFile.addRow(new Object[]{file2.getName(),pathFile,file2.length()+" Byte",contaMembriImportati-contaMembriSingoloFile});
                 contaFileImportati++;
                 CampoNumeroFiles.setText(""+contaFileImportati);
                 TickGif();
                }
                if(controlloCicli==0)
                {
                  FinestraDialogo.setVisible(true);
                  if(!PathFileDragDrop.getText().equals(""))
                    LeggiFileButton.setEnabled(true);
                  MessageText.setText("Il file non è leggibile! CAUSE: split errato /insufficienti parametri /assenza intestazione");
                  allarmGif();
                }
            FrameLoad.setVisible(false);
            }
            else
            {
                MessageText.setText("Nessun separatore è stato scelto");
                allarmGif();
            }
        }
        else
        {
          MessageText.setText("Un file non può essere importato su una tabella con filtri attivati");
          allarmGif();
        }
        }
        else
        {
          MessageText.setText("Un file non può essere importato su una tabella vuota");
          allarmGif();
        }
        }
        catch (FileNotFoundException e)
        {
            MessageText.setText("FILE NON TROVATO");
            allarmGif();
        }
        catch (IOException e)
        {
            MessageText.setText("Errore I/O");
            allarmGif();
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            MessageText.setText("Il file non è leggibile! CAUSE: split errato /insufficienti parametri /assenza intestazione");
            allarmGif();
        }
        catch (IllegalArgumentException e)
        {
            MessageText.setText("Questo file è stato gia importato");
            allarmGif();
        }
        catch(NullPointerException e)
        {}
        catch (InterruptedException ex)
        {

        }
        finally
        {
            if (br != null)
            {
                try
                {
                    FrameLoad.setVisible(false);
                    br.close();
                }
                catch (IOException e)
                {
                    MessageText.setText("Errore I/O");
                    allarmGif();
                }
            }
        }
       }
     };
     IDThreadImportaFile.start();
    }

    @Override
    public int print(Graphics g, PageFormat pf, int page) throws PrinterException
    {
       System.out.println("numero pagine: "+page);
       int spazioY=10;
       DefaultTableModel table = (DefaultTableModel) TabellaPrincipale.getModel();

       // Posizioniamo correttamente le coordinate
       // da dove cominciare a disegnare
       if(table.getRowCount()!=rigaStampa)
       {
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());

        g2d.drawString("COGNOMI",50,spazioY);
        g2d.drawString("NOMI",200,spazioY);
        g2d.drawString("CLASSE",400,spazioY);
        g2d.drawString("___________________________________________________________________",0,spazioY+8);

        // Rendering
        for(int i=0;22!=i&&table.getRowCount()!=rigaStampa;i++)
        {
         spazioY=spazioY+30;
         g2d.drawString((String) table.getValueAt(rigaStampa,0),50,spazioY);
         g2d.drawString((String) table.getValueAt(rigaStampa,1),200,spazioY);
         g2d.drawString((String) table.getValueAt(rigaStampa,14),400,spazioY);
         rigaStampa++;
        }
       }

       // comunica che questa pagina è
       // parte del documento stampato
       return PAGE_EXISTS;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFrame AboutFrame;
    private javax.swing.JTextArea AboutText;
    private javax.swing.JButton AggiungiMembroButton;
    private javax.swing.JMenuItem Altro;
    private javax.swing.JButton AnnullaButtonFrameFileExist;
    private javax.swing.JTextArea AnteprimaFileImportato;
    private javax.swing.JTextArea AnteprimaText;
    private javax.swing.JProgressBar BarDownload;
    private javax.swing.JProgressBar BarUpdater;
    private javax.swing.JButton ButtonCancellaRiga;
    private javax.swing.JButton ButtonCorreggi;
    private javax.swing.JButton ButtonImporta;
    private javax.swing.JButton ButtonInstalla;
    private javax.swing.JButton ButtonRinominaSelezionati;
    private javax.swing.JButton ButtonRinominaTutto;
    private javax.swing.JButton ButtonRipristina;
    private javax.swing.JButton ButtonSlideLeft;
    private javax.swing.JButton ButtonSlideRight;
    private javax.swing.JButton Button_Annulla;
    private javax.swing.JButton Button_Riavvia;
    private javax.swing.JTextField CampoBaseOmonimi;
    private javax.swing.JTextField CampoImportatiOmonimi;
    private javax.swing.JTextField CampoMembriImportati;
    private javax.swing.JTextField CampoNoDipartimento;
    private javax.swing.JTextField CampoNumeroDipartimenti;
    private javax.swing.JTextField CampoNumeroFiles;
    private javax.swing.JTextField CampoTotaleOmonimi;
    private javax.swing.JButton CancellaRigaButton;
    private javax.swing.JCheckBox CheckAltro;
    private javax.swing.JCheckBox CheckBoxSalvaNuovi;
    private javax.swing.JCheckBox CheckBoxSalvaTutto;
    private javax.swing.JCheckBox CheckBoxSalvaVecchi;
    private javax.swing.JCheckBox CheckHash;
    private javax.swing.JCheckBox CheckPuntoVirgola;
    private javax.swing.JRadioButton CheckRadioEmail;
    private javax.swing.JRadioButton CheckRadioPasswordRandom;
    private javax.swing.JCheckBox CheckSlash;
    private javax.swing.JCheckBox CheckSpazio;
    private javax.swing.JCheckBox CheckTabulazione;
    private javax.swing.JCheckBox CheckUnderscore;
    private javax.swing.JCheckBox CheckVirgola;
    private javax.swing.JButton CloseComparatore;
    private javax.swing.JButton CloseCorrettoreOmonimi;
    private javax.swing.JButton ClosePanelloInformazioni;
    private javax.swing.JButton CloseSceltaSalvataggio;
    private javax.swing.JMenu ComparaFileButton;
    private javax.swing.JFrame Comparatore;
    private javax.swing.JTextField Contatore_doppioni;
    private javax.swing.JTextField Contatore_omonimi;
    private javax.swing.JFrame ContattaFrame;
    private javax.swing.JMenuItem Contattami;
    private javax.swing.JMenu CorreggiOmonimi;
    private javax.swing.JFrame CorrettoreOmonimi;
    private javax.swing.JMenuItem Design_Metal;
    private javax.swing.JMenuItem Design_Windows;
    private javax.swing.JDialog Dialog_Chiusura;
    private javax.swing.JLabel DipartimentoIcon;
    private javax.swing.JButton EliminaModifiche;
    private javax.swing.JButton EsportaModificheButton;
    private javax.swing.JMenu ExportFile;
    private javax.swing.JFileChooser FileChooserGenerale;
    private javax.swing.JFileChooser FileChooserSalvaNome;
    private javax.swing.JMenu FileMenu;
    private javax.swing.JDialog FinestraDialogo;
    private javax.swing.JFrame FrameAggiornamento;
    private javax.swing.JDialog FrameFileExist;
    private javax.swing.JFrame FrameLoad;
    private javax.swing.JButton GeneraPasswordButton;
    private javax.swing.JLabel GifPicture;
    private javax.swing.JFrame Guida;
    private javax.swing.JLabel GuidaIcon;
    private javax.swing.JMenu GuidaMenu;
    private javax.swing.JMenuItem GuidaShortCut;
    private javax.swing.JFrame Guida_veloce;
    private javax.swing.JTextArea Guida_veloce_text;
    private javax.swing.JLabel Help_CorrettoreOmonimi;
    private javax.swing.JLabel Help_Salvataggio;
    private javax.swing.JLabel Icon_CorrettoreOmonimi;
    private javax.swing.JMenu ImportFile;
    private javax.swing.JTree Indice;
    private javax.swing.JLabel InformationFileIcon;
    private javax.swing.JPanel JPane123;
    private javax.swing.JPanel JPane124;
    private javax.swing.JButton LeggiFileButton;
    private javax.swing.JLabel LogoCSVPicture;
    private javax.swing.JMenuBar MenuBar;
    private javax.swing.JMenu Menu_Design;
    private javax.swing.JTextField MessageText;
    private javax.swing.JTextField NumberSlide;
    private javax.swing.JTextField OptionSlide;
    private javax.swing.JPanel Panel2_Chiusura;
    private javax.swing.JPanel Panel_Chiusura;
    private javax.swing.JPanel Panel_CorrettoreOminimi;
    private javax.swing.JPanel Panel_Guida;
    private javax.swing.JPanel Panel_SceltaSplit;
    private javax.swing.JPanel Panel_Split;
    private javax.swing.JPanel Pannelllo_FrameAggiornamento;
    private javax.swing.JPanel PannelloContatta;
    private javax.swing.JFrame PannelloInformazioni;
    private javax.swing.JPanel Pannello_CorrettoreOminimi;
    private javax.swing.JPanel Pannello_EstrazioneDati;
    private javax.swing.JPanel Pannello_EstrazioneDati1;
    private javax.swing.JPanel Pannello_Gif;
    private javax.swing.JPanel Pannello_GifApertura;
    private javax.swing.JPanel Pannello_InformazioniFileImportato;
    private javax.swing.JPanel Pannello_InformazioniGenerali;
    private javax.swing.JPanel Pannello_JForm;
    private javax.swing.JPanel Pannello_PannelloInformazioni;
    private javax.swing.JPanel Pannello_SceltaSalvataggio;
    private javax.swing.JPanel Pannello_StrumentiLaterale;
    private javax.swing.JPanel Pannello_Tabelle;
    private javax.swing.JPanel Pannello_comparatore;
    private javax.swing.JPanel Pannello_comparatore2;
    private javax.swing.JTextField PathField;
    private javax.swing.JTextField PathFileDragDrop;
    private javax.swing.JLabel PictureDownload;
    private javax.swing.JPopupMenu PopupMenu;
    private javax.swing.JProgressBar ProgressBar;
    private javax.swing.JButton PulisciTabellaButton;
    private javax.swing.JMenu SalvaButton;
    private javax.swing.JComboBox<String> SceltaDipartimento;
    private javax.swing.JFrame SceltaSalvataggio;
    private javax.swing.JTextField SceltaSplit;
    private javax.swing.JScrollPane ScrollPaneContatta;
    private javax.swing.JScrollPane ScrollPane_JForm;
    private javax.swing.JScrollPane ScrollPane_TabellaAggiungi;
    private javax.swing.JScrollPane ScrollPane_TabellaEsterna;
    private javax.swing.JScrollPane ScrollPane_TabellaFiles;
    private javax.swing.JScrollPane ScrollPane_TabellaFlags;
    private javax.swing.JScrollPane ScrollPane_TabellaFlagsSupport;
    private javax.swing.JScrollPane ScrollPane_TabellaNumerazione;
    private javax.swing.JScrollPane ScrollPane_TabellaOmonimi;
    private javax.swing.JScrollPane ScrollPane_TabellaPrincipale;
    private javax.swing.JScrollPane ScrollPane_TabellaRicerca;
    private javax.swing.JScrollPane ScrollaPane_TabellaComparatore;
    private javax.swing.JButton SearchButton;
    private javax.swing.JButton SfogliaButton;
    private javax.swing.JLabel SlidePanel;
    private javax.swing.JButton SostiuisciButtonFrameFileExist;
    private javax.swing.JMenu StampaButton;
    private javax.swing.JTable TabellaAggiungi;
    private javax.swing.JTable TabellaComparatore;
    private javax.swing.JTable TabellaEsterna;
    private javax.swing.JTable TabellaFile;
    private javax.swing.JTable TabellaFlags;
    private javax.swing.JTable TabellaFlagsComparatore;
    private javax.swing.JTable TabellaFlagsSupport;
    private javax.swing.JTable TabellaNumerazione;
    private javax.swing.JTable TabellaOmonimi;
    private javax.swing.JTable TabellaPrincipale;
    private javax.swing.JTable TabellaRicerca;
    private javax.swing.JTextArea TextAreaContatta;
    private javax.swing.JTextField TextDoppioni;
    private javax.swing.JTextField TextDownload;
    private javax.swing.JTextField TextFieldAltro;
    private javax.swing.JTextField TextFieldFrameFileExist;
    private javax.swing.JTextField TextField_Dialog;
    private javax.swing.JTextField TextFileBase;
    private javax.swing.JTextField TextFileComparato;
    private javax.swing.JTextArea TextGuida;
    private javax.swing.JTextField TextInfo;
    private javax.swing.JTextField TextLoad;
    private javax.swing.JTextField TextOmonimi;
    private javax.swing.JTextField Text_File_Comparato;
    private javax.swing.JMenuItem UpdateSoftware;
    private javax.swing.JTextField contaAggiunti;
    private javax.swing.JTextField contaAggiuntiManuale;
    private javax.swing.JTextField contaEsistenti;
    private javax.swing.JTextField contaMembri;
    private javax.swing.JMenuItem eliminaPassword;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator10;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JPopupMenu.Separator jSeparator9;
    private javax.swing.JTable jTable4;
    private javax.swing.JLabel logoGuidaPicture2;
    private javax.swing.JLabel logoGuidaPicture3;
    private javax.swing.JTextField visualizzaAnteprima;
    // End of variables declaration//GEN-END:variables
}
