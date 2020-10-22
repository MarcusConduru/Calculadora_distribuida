package br.com.sockets;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente implements Runnable{

    static int operacao = 0;
    private Socket cliente;
    double num1;
    double num2;
    char opr;

    public Cliente(Socket cliente){
        this.cliente = cliente;
    }

    public static void main(String args[]) throws UnknownHostException, IOException {
        while(operacao <= 0 || operacao > 7) {
            operacao = Integer.parseInt(JOptionPane.showInputDialog("Qual operação desejada? 1= +, 2= -,3= X,4= /,5= ^,6= %,7= √"));
            if (operacao <= 0 || operacao > 7){
                System.out.println("Você digitou uma operação inválida.");
            }
        }

        int local;
        // para se conectar ao servidor, cria-se objeto Socket.
        // O primeiro parâmetro é o IP ou endereço da máquina que
        // se quer conectar e o segundo é a porta da aplicação.
        // Neste caso, usa-se o IP da máquina local (127.0.0.1)
        // e a porta da aplicação ServidorDeEco (12345).
        if (operacao <= 4) {
            local = 12345;
        } else {
            local = 12346;
        }
        Socket socket = new Socket("127.0.0.1", local);
        /*Cria um novo objeto Cliente com a conexão socket para que seja executado em um novo processo.
        Permitindo assim a conexão de vário clientes com o servidor.*/
        Cliente c = new Cliente(socket);
        Thread t = new Thread(c);
        t.start();
    }

    public void run() {
        try {
            PrintStream saida;
            System.out.println("O cliente conectou ao servidor");

            ObjectInputStream resultado = new ObjectInputStream(cliente.getInputStream());
            ObjectOutputStream dados = new ObjectOutputStream(cliente.getOutputStream());

            num1 = Double.parseDouble(JOptionPane.showInputDialog("Digite o primeiro número"));
            num2 = Double.parseDouble(JOptionPane.showInputDialog("Digite o segundo número"));

            dados.writeInt(operacao);
            dados.writeDouble(num1);
            dados.writeDouble(num2);
            dados.flush();

            double total = resultado.readDouble();
            opr = resultado.readChar();
            System.out.println("Total de " + num1 + opr + num2 + " = " + total);

            resultado.close();
            dados.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}