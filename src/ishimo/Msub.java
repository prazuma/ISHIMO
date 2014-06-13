/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ishimo;

/**
 *
 * @author g12908hm
 */
public class Msub extends M{
    public int push2(int x){    
        x = -x;
    M.List.add(x);       
    /*
        for(int i=0;i<M.List.size();i++)
        System.out.println(M.List.get(i));
        * */
    return x;
}
}
