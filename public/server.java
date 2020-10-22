package br.com.sockets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{
    public Socket cliente;

    public Server(Socket cliente){
        this.cliente = cliente;
    }

    public static void main(String[] args)  throws IOException{

        //Cria um socket na porta 12345
        ServerSocket servidor = new ServerSocket(12345);
        System.out.println("Porta 12345 aberta!");

        // Aguarda alguém se conectar. A execução do servidor
        System.out.println("Aguardando conexão do cliente...");

        while (true) {
            Socket cliente = (Socket) servidor.accept();
            // Cria uma thread do servidor para tratar a conexão
            Server tratamento = new Server((java.net.Socket) cliente);
            Thread t = new Thread(tratamento);
            // Inicia a thread para o cliente conectado
            t.start();
        }
    }


    public void run(){
        System.out.println("Nova conexao com o cliente " + this.cliente.getInetAddress().getHostAddress());
        double num1, num2, total = 0.0;
        int operacao;
        char opr = '\n';

        try {

            ObjectOutputStream resultado = new ObjectOutputStream(cliente.getOutputStream());
            ObjectInputStream dados = new ObjectInputStream(cliente.getInputStream());

            operacao = dados.readInt();
            num1 = dados.readDouble();
            num2 = dados.readDouble();

            if (operacao == 1) {

                opr = '+';
                total = (num1 + num2);

            } else if (operacao == 2) {

                opr = '-';
                total = (num1 - num2);

            } else if (operacao == 3) {

                opr = 'x';
                total = (num1 * num2);

            } else {

                opr = '/';
                total = (num1 / num2);

            }

            resultado.writeDouble(total);
            resultado.writeChar(opr);
            resultado.flush();

            resultado.close();
            dados.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}