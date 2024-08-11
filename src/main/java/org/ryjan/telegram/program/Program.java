package org.ryjan.telegram.program;


import org.ryjan.telegram.misc.Misc;

public class Program {

    public static void main(String[] args) {
        String message = Misc.getConnectionString();
        System.out.println(message);
    }
}