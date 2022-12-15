/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilidades;

import javax.swing.JOptionPane;

/**
 *
 * @author Santiago
 */
public class MensajeSimple {
    public static void mostrar(String txt){
        JOptionPane.showMessageDialog(null, txt, "RegisteredMoney", JOptionPane.INFORMATION_MESSAGE, null);
    }
}
