/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ishimo;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author g12908hm
 */
public class Isimo extends javax.swing.JFrame {
    /**
     * Creates new form Isimo
     */
    static final String FILE_NAME="U:\\NetBeansProjects\\ISIMO\\outfile.txt";
    JLabel[]kurai=new JLabel[9];//電卓の液晶部
    int number_1=0;//記号を打つ前の数字
    int number_2=0;//記号を打った後の数字
    int figure=0;//記号を表す用（1…＋、2…ー、3…×、4…÷、5…±）
    int pm=0;//正負を表わす用(0…正、0以外…負)
    int count=1;//0のときは1回も記号が押されえていない状態
    int[] numnum=new int[3];
    Stack s=new Stack();//計算する数字を保存しておく
    Add add=new Add();
    Sub sub=new Sub();
    Mul mul=new Mul();
    Div div=new Div();
    //
    Madd ma=new Madd();
    Msub ms=new Msub();
    Mclear mc = new Mclear();
    Mrecall mr = new Mrecall();
    //
    public Isimo() {
        initComponents();
        
        kurai[8]=taOutput9;
        kurai[7]=taOutput8;
        kurai[6]=taOutput7;
        kurai[5]=taOutput6;
        kurai[4]=taOutput5;
        kurai[3]=taOutput4;
        kurai[2]=taOutput3;
        kurai[1]=taOutput2;
        kurai[0]=taOutput1;
        paint_white();
        paintIsimo();
    }
    
    void putMessage(String str,SimpleAttributeSet attr){
        StyledDocument doc=taOutput.getStyledDocument();
        try{
            doc.insertString(doc.getLength(), str, attr);
        }catch(BadLocationException ex){
            ex.printStackTrace();
        }
        
    }
    

    
    void putout(){
            File dir=new File("point.txt").getParentFile();
            JFileChooser fc=new JFileChooser(dir);
            FileNameExtensionFilter ff=new FileNameExtensionFilter("テキストファイル","txt");
            fc.setFileFilter(ff);
            int retval=fc.showSaveDialog(this);
            if(retval!=JFileChooser.APPROVE_OPTION)return;
            File f=fc.getSelectedFile();
            try{
                PrintWriter pp=new PrintWriter(f,"UTF-8");
                pp.println("かきこみテキスト");
                pp.println("---");
                pp.println(taOutput.getText());
                pp.println("---");
                pp.println("終わり");
                pp.close();
    
            }   catch(IOException ex){
                ex.printStackTrace();
            }
    }
    
    public void paintIsimo(){
        File f=new File("Number\\isimo.jpg");
JLabel j=logo;
            BufferedImage img=new BufferedImage(j.getWidth(),j.getHeight(),BufferedImage.TYPE_INT_RGB);
        Graphics g=img.getGraphics();
        g.setColor(Color.WHITE);
        try{
            Image number=ImageIO.read(f);
            g.drawImage(number,0,0,this);
            
        }catch(IOException ex){
            g.drawString(ex.getMessage(),28,40);
        }
        g.dispose();
        ImageIcon icon=new ImageIcon(img);
        j.setIcon(icon);
               
    }
    
    public void paint_white(){//液晶全部を消す
        File f=new File("Number\\none.jpg");
        for(int i=0;i<9;i++){
            JLabel j=kurai[i];
            BufferedImage img=new BufferedImage(j.getWidth(),j.getHeight(),BufferedImage.TYPE_INT_RGB);
        Graphics g=img.getGraphics();
        g.setColor(Color.WHITE);
        try{
            Image number=ImageIO.read(f);
            g.drawImage(number,0,0,this);
            
        }catch(IOException ex){
            g.drawString(ex.getMessage(),28,40);
        }
        g.dispose();
        ImageIcon icon=new ImageIcon(img);
        j.setIcon(icon);
        }
    }

    public void paint(int n,JLabel j){//数字ｎ（一桁、n=10のときは負のときに使用）を電卓の液晶のとある場所に出力させる
        File f;
        pm=0;//
        if(n==1) {
            f=new File("Number\\1.jpg");
        }
        else if(n==2) {
            f=new File("Number\\2.jpg");
        }
        else if(n==3) {
            f=new File("Number\\3.jpg");
        }
        else if(n==4) {
            f=new File("Number\\4.jpg");
        }
        else if(n==5) {
            f=new File("Number\\5.jpg");
        }
        else if(n==6) {
            f=new File("Number\\6.jpg");
        }
        else if(n==7) {
            f=new File("Number\\7.jpg");
        }
        else if(n==8) {
            f=new File("Number\\8.jpg");
        }
        else if(n==9){
            f=new File("Number\\9.jpg");
        }
        else if(n==0){
            f=new File("Number\\0.jpg");
        }
        else if(n==10){
            f=new File("Number\\-.jpg");
        }
        else {
            f=new File("Number\\none.jpg");
        }
        BufferedImage img=new BufferedImage(j.getWidth(),j.getHeight(),BufferedImage.TYPE_INT_RGB);
        Graphics g=img.getGraphics();
        g.setColor(Color.WHITE);
        try{
            Image number=ImageIO.read(f);
            g.drawImage(number,0,0,this);
            
        }catch(IOException ex){
            g.drawString(ex.getMessage(),28,40);
        }
        g.dispose();
        ImageIcon icon=new ImageIcon(img);
        j.setIcon(icon);
    }
    
    public int keta_cal(int n){//数字ｎの桁数を調べる
        int keta=0,x=1;
        while(n%x!=n){
            x*=10;
            keta++;
        }
        if(n==0)return 1;
        return keta;
    }
 
    public void show(int n,int pm){//電卓の液晶に数字n（計算結果など）を表示する
        int keta=keta_cal(n);
        int y=1,x=1,p=0;
        while(y!=keta){
            x*=10;
            y++;
        }
        paint_white();
        if(n<0)n=-n;
        for(int i=keta-1;i>=0;i--){
            int m=n/x;
            paint(m,kurai[i]);
            n%=x;
            x/=10;
        }
        if(pm==-1){
            paint(10,kurai[keta]);
            pm=0;
        }
     }

    public int calculate(int figure,int n1,int n2){//figureで四則演算を判断し、n1,n2の計算をする
        int a=0;
        if(figure==1){
            a=add.cal(n1,n2);
        }
        else if(figure==2){
            a=sub.cal(n1,n2);
        }
        else if(figure==3){
            a=mul.cal(n1,n2);
        }
        else{
            a=div.cal(n1,n2);
        }
        return a;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSlider1 = new javax.swing.JSlider();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        btn0 = new javax.swing.JButton();
        btn1 = new javax.swing.JButton();
        btnMC = new javax.swing.JButton();
        btnDiv = new javax.swing.JButton();
        btnReverse = new javax.swing.JButton();
        btnC = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnM_Add = new javax.swing.JButton();
        btnSub = new javax.swing.JButton();
        btn9 = new javax.swing.JButton();
        btnMul = new javax.swing.JButton();
        btn7 = new javax.swing.JButton();
        btn8 = new javax.swing.JButton();
        btn5 = new javax.swing.JButton();
        btn6 = new javax.swing.JButton();
        btn3 = new javax.swing.JButton();
        btn2 = new javax.swing.JButton();
        btnEqual = new javax.swing.JButton();
        btn4 = new javax.swing.JButton();
        btnM_Sub = new javax.swing.JButton();
        btnMR = new javax.swing.JButton();
        taOutput8 = new javax.swing.JLabel();
        taOutput9 = new javax.swing.JLabel();
        taOutput6 = new javax.swing.JLabel();
        taOutput7 = new javax.swing.JLabel();
        taOutput5 = new javax.swing.JLabel();
        taOutput4 = new javax.swing.JLabel();
        taOutput3 = new javax.swing.JLabel();
        taOutput2 = new javax.swing.JLabel();
        taOutput1 = new javax.swing.JLabel();
        logo = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        taOutput = new javax.swing.JTextPane();

        jLabel8.setText("jLabel1");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btn0.setText("0");
        btn0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn0ActionPerformed(evt);
            }
        });

        btn1.setText("1");
        btn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn1ActionPerformed(evt);
            }
        });

        btnMC.setText("MC");
        btnMC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMCActionPerformed(evt);
            }
        });

        btnDiv.setText("÷");
        btnDiv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDivActionPerformed(evt);
            }
        });

        btnReverse.setText("+/-");
        btnReverse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReverseActionPerformed(evt);
            }
        });

        btnC.setText("C");
        btnC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCActionPerformed(evt);
            }
        });

        btnAdd.setText("+");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnM_Add.setText("M+");
        btnM_Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnM_AddActionPerformed(evt);
            }
        });

        btnSub.setText("-");
        btnSub.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubActionPerformed(evt);
            }
        });

        btn9.setText("9");
        btn9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn9ActionPerformed(evt);
            }
        });

        btnMul.setText("×");
        btnMul.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMulActionPerformed(evt);
            }
        });

        btn7.setText("7");
        btn7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn7ActionPerformed(evt);
            }
        });

        btn8.setText("8");
        btn8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn8ActionPerformed(evt);
            }
        });

        btn5.setText("5");
        btn5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn5ActionPerformed(evt);
            }
        });

        btn6.setText("6");
        btn6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn6ActionPerformed(evt);
            }
        });

        btn3.setText("3");
        btn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn3ActionPerformed(evt);
            }
        });

        btn2.setText("2");
        btn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn2ActionPerformed(evt);
            }
        });

        btnEqual.setText("=");
        btnEqual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEqualActionPerformed(evt);
            }
        });

        btn4.setText("4");
        btn4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn4ActionPerformed(evt);
            }
        });

        btnM_Sub.setText("M-");
        btnM_Sub.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnM_SubActionPerformed(evt);
            }
        });

        btnMR.setText("MR");
        btnMR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMRActionPerformed(evt);
            }
        });

        jScrollPane3.setViewportView(taOutput);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnMC, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnM_Add, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(btnM_Sub, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(btn7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(btn8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btn9, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(btnC, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(btnReverse, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(24, 24, 24)
                                    .addComponent(btnDiv, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(24, 24, 24)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(btnMR, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnMul, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(248, 248, 248)
                                .addComponent(btnSub, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(btn4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btn5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btn3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btn6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(24, 24, 24)
                                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(taOutput9, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(taOutput8, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(taOutput7, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(taOutput6, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(taOutput5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGap(1, 1, 1)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(btn0, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(btnEqual, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(btn1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(btn2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(174, 174, 174)
                                        .addComponent(taOutput4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(taOutput3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(taOutput2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(taOutput1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(logo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, Short.MAX_VALUE))
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(taOutput8, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(taOutput9, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(taOutput5, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(taOutput7, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(taOutput6, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(taOutput3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(taOutput4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(taOutput2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(taOutput1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnMC, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnM_Add, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnM_Sub, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMR, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnC, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReverse, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDiv, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMul, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSub, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(btnEqual, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(btn0, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMCActionPerformed
        //Listの計算結果をすべて削除する
        mc.clear();
    }//GEN-LAST:event_btnMCActionPerformed

    private void btnDivActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDivActionPerformed
        if(figure==0){
            number_1=s.pop();
        }
        else{
            number_2=s.pop();
            number_1=calculate(figure,number_1,number_2);
        }
        figure=4;//
        s.clear();
        count++;//
    }//GEN-LAST:event_btnDivActionPerformed

    private void btnReverseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReverseActionPerformed
        int f=s.pop();
        System.out.println(f+"の正負を反転させます。");
        f=-1*f;
        pm=-1;
        int keta=keta_cal(f),y=1,x=1;
        s.clear();
        while(x!=keta){
            y*=10;
            x++;
        }
        if(number_1>999999999)number_1=999999999;
        else show(f,pm);
        int n=f;//number_1がこの後１桁ずつに分解されるのでnとして保存しておく。
        number_2=0;
        System.out.println(f);
        for(int i=keta;i>0;i--){
            f=f/y;
            y/=10;
            s.push(f);
        }
        number_1=n;
    }//GEN-LAST:event_btnReverseActionPerformed

    private void btnCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCActionPerformed
        paint_white();
        s.clear();
        number_1=0;
        number_2=0;
        figure=0;
    }//GEN-LAST:event_btnCActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        if(figure==0){
            number_1=s.pop();
        }
        else{
            number_2=s.pop();
            number_1=calculate(figure,number_1,number_2);
        }
        figure=1;
        s.clear();
        count++;
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnM_AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnM_AddActionPerformed
        //Listに計算結果を追加する
        if(figure!=0){
        number_2=s.pop();
        number_1=calculate(figure,number_1,number_2);
        }
        s.clear();
        count++;
        ma.push2(number_1);
        number_1=0;
    }//GEN-LAST:event_btnM_AddActionPerformed

    private void btnSubActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubActionPerformed
        if(figure==0){
            number_1=s.pop();
        }
        else{
            number_2=s.pop();
            number_1=calculate(figure,number_1,number_2);
        }
        figure=2;
        s.clear();
        count++;
    }//GEN-LAST:event_btnSubActionPerformed

    private void btn9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn9ActionPerformed
        if(count==0){
            paint_white();                                                            
        }
        s.push(9);
        show(s.pop(),pm);
    }//GEN-LAST:event_btn9ActionPerformed

    private void btnMulActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMulActionPerformed
        if(figure==0){//
            number_1=s.pop();
        }
        else{
            number_2=s.pop();
            number_1=calculate(figure,number_1,number_2);
        }
        s.clear();
        figure=3;
        count++;
    }//GEN-LAST:event_btnMulActionPerformed

    private void btn7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn7ActionPerformed
        if(count==0){
            paint_white();        
        }  
        s.push(7);
        show(s.pop(),pm);
    }//GEN-LAST:event_btn7ActionPerformed

    private void btn8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn8ActionPerformed
        if(count==0){
            paint_white();
        }
        s.push(8);
        show(s.pop(),pm);
    }//GEN-LAST:event_btn8ActionPerformed

    private void btn5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn5ActionPerformed
        if(count==0){
            paint_white();
        }
        s.push(5);
        show(s.pop(),pm);       
    }//GEN-LAST:event_btn5ActionPerformed

    private void btn6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn6ActionPerformed
        if(count==0){
            paint_white();
        } 
        s.push(6);
        show(s.pop(),pm);       
    }//GEN-LAST:event_btn6ActionPerformed

    private void btn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn3ActionPerformed
        if(count==0){
            paint_white();
        }        
        s.push(3);
        show(s.pop(),pm);        
    }//GEN-LAST:event_btn3ActionPerformed

    private void btn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn2ActionPerformed
        if(count==0){
            paint_white();
        }
        s.push(2);
        show(s.pop(),pm);
    }//GEN-LAST:event_btn2ActionPerformed

    private void btnEqualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEqualActionPerformed
        int keta=keta_cal(number_1),y=1,x=1;
        number_2=s.pop();
        s.clear();
        System.out.println("1つめの数字は"+number_1);
        if(figure!=0){
            number_1=calculate(figure,number_1,number_2);
            figure=0;
        }
        while(x!=keta){
            y*=10;
            x++;
        }
        if(number_1<0)pm=-1;
        else pm=0;
        if(number_1>999999999)number_1=999999999;
        if(count==0){show(number_2,pm);number_1=number_2;}
        else show(number_1,pm);
        int n=number_1;//number_1がこの後１桁ずつに分解されるのでnとして保存しておく。
        System.out.println("2つ目の数字は"+number_2);
        number_2=0;
        System.out.println("1つ目の数字と2つ目の数字の計算結果は"+number_1);
        for(int i=keta;i>0;i--){
            number_1=number_1/y;
            y/=10;
            s.push(number_1);
        }
        count=0;
        number_1=n;
    }//GEN-LAST:event_btnEqualActionPerformed

    private void btn4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn4ActionPerformed
        if(count==0){
            paint_white();
        }
        s.push(4);
        show(s.pop(),pm);
    }//GEN-LAST:event_btn4ActionPerformed

    private void btnM_SubActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnM_SubActionPerformed
        //Listに計算結果をマイナスにして追加する
        if(figure!=0){
        number_2=s.pop();
        number_1=calculate(figure,number_1,number_2);
        }
        s.clear();
        count++;
        ms.push2(number_1);
        number_1=0;
    }//GEN-LAST:event_btnM_SubActionPerformed

    private void btnMRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMRActionPerformed
        //Listの計算結果をすべて足して表示
        int call = mr.recall(0);
        System.out.println(call);
        int keta=keta_cal(call),y=1,x=1;
        while(x!=keta){
            y*=10;
            x++;
        }
        if(call<0)pm=-1;
        else pm=0;
        if(call>999999999)call=999999999;
        if(count==0){show(number_2,pm);number_1=number_2;}
        else show(call,pm);
        for(int i=keta;i>0;i--){
            call=call/y;
            y/=10;
            s.push(call);
        }
            SimpleAttributeSet attr=new SimpleAttributeSet();
        try{
            FileInputStream fis=new FileInputStream("outfile.txt");
            InputStreamReader isr=new InputStreamReader(fis,"UTF-8");
            BufferedReader bur=new BufferedReader(isr);
            StyledDocument doc = taOutput.getStyledDocument();
            for(String line;(line=bur.readLine())!=null;){
                putMessage(line+",",attr);
            }          
            bur.close();
            isr.close();
            fis.close();
        }catch(IOException ex){
            ex.printStackTrace();
        }
        putMessage("の合計",attr);
        mr.keepData2(M.List);
    }//GEN-LAST:event_btnMRActionPerformed

    private void btn0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn0ActionPerformed
        if(count==0){
            paint_white();
        }
        s.push(0);
        show(s.pop(),pm);        
    }//GEN-LAST:event_btn0ActionPerformed

    private void btn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn1ActionPerformed
        if(count==0){
            paint_white();
        }
        s.push(1);
        show(s.pop(),pm);
    }//GEN-LAST:event_btn1ActionPerformed

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
            java.util.logging.Logger.getLogger(Isimo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Isimo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Isimo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Isimo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Isimo().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn0;
    private javax.swing.JButton btn1;
    private javax.swing.JButton btn2;
    private javax.swing.JButton btn3;
    private javax.swing.JButton btn4;
    private javax.swing.JButton btn5;
    private javax.swing.JButton btn6;
    private javax.swing.JButton btn7;
    private javax.swing.JButton btn8;
    private javax.swing.JButton btn9;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnC;
    private javax.swing.JButton btnDiv;
    private javax.swing.JButton btnEqual;
    private javax.swing.JButton btnMC;
    private javax.swing.JButton btnMR;
    private javax.swing.JButton btnM_Add;
    private javax.swing.JButton btnM_Sub;
    private javax.swing.JButton btnMul;
    private javax.swing.JButton btnReverse;
    private javax.swing.JButton btnSub;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JLabel logo;
    private javax.swing.JTextPane taOutput;
    private javax.swing.JLabel taOutput1;
    private javax.swing.JLabel taOutput2;
    private javax.swing.JLabel taOutput3;
    private javax.swing.JLabel taOutput4;
    private javax.swing.JLabel taOutput5;
    private javax.swing.JLabel taOutput6;
    private javax.swing.JLabel taOutput7;
    private javax.swing.JLabel taOutput8;
    private javax.swing.JLabel taOutput9;
    // End of variables declaration//GEN-END:variables
}
