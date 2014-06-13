/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ishimo;

import java.awt.Color;
import java.awt.List;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author g12908hm
 */
public class Mrecall extends M {
    public int recall(int x){
    for(int i =0;i<M.List.size();i++){
            x += (Integer)M.List.get(i);
    System.out.println(x);
    }
    keepData(M.List);    
        return x;
    }
    
    void keepData(ArrayList list){
        try{
            PrintWriter pw=new PrintWriter(FILE_NAME,"UTF-8");
            for(int i=0;i<list.size();i++){
                pw.print(list.get(i)+"\n");
            }
            pw.close();
        }catch(IOException ex){
            ex.printStackTrace();
        }      
    }
    
    void keepData2(ArrayList list){
        int x = 0;
        try{
            PrintWriter pw=new PrintWriter(FILE_NAME,"UTF-8");
            pw.print("==============================\n");
            pw.print(list.get(0)+"\n");
            x = (Integer)M.List.get(0);
            for(int i=1;i<list.size();i++){
                pw.print("と"+list.get(i)+"\n");
                x += (Integer)M.List.get(i);
            }
            pw.print("を足した結果は");
            pw.print(x+"になりました。\n");
            pw.print("==============================");
            pw.close();
        }catch(IOException ex){
            ex.printStackTrace();
        }      
    }
}
