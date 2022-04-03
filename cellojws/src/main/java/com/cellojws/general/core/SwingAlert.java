package com.cellojws.general.core;

import javax.swing.JOptionPane;

public class SwingAlert
{
    public static void infoBox(String infoMessage)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "Alert", JOptionPane.INFORMATION_MESSAGE);
    }
}

