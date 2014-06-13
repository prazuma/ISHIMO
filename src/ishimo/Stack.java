/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ishimo;
import java.util.*;
/**
 *
 * @author g12908hm
 */
public class Stack {//数字を入力したときに一時的に一桁一桁保存する場所
    int []stack;//数字入れる用
    int count;//桁カウント用

    public Stack(){
        stack=new int[9];
        count=0;
    }
    
    public void clear(){//全部消去→0にする
        for(int i=0;i<9;i++){
            stack[i]=0;
        }
        count=0;
    }
    
    public void push(int x){//数字を入れる
        stack[count++]=x;
    }
    
    public int pop(){//きちんとした数字にして返す
        int f=0,y=1;
        for(int i=count-1;i>=0;i--){
            f+=stack[i]*y;
            y*=10;
        }
        return f;
    }
    
    public int pop_every(int n){//右からn桁目を取り出す
        return stack[n-1];
    }
    
    public int floor(){
        return count;
    }

    
}
