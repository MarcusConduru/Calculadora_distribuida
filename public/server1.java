package br.com.sockets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server1 implements Runnable{
    public Socket cliente;

    public Server1(Socket cliente){
        this.cliente = cliente;
    }

    public static void main(String[] args)  throws IOException{

        //Cria um socket na porta 12346
        ServerSocket servidor = new ServerSocket(12346);
        System.out.println("Porta 12346 aberta!");

        // Aguarda alguém se conectar. A execução do servidor
        // fica bloqueada na chamada do método accept da classe
        // ServerSocket. Quando alguém se conectar ao servidor, o
        // método desbloqueia e retorna com um objeto da classe
        // Socket, que é uma porta da comunicação.
        System.out.println("Aguardando conexão do cliente...");

        while (true) {
            Socket cliente = (Socket) servidor.accept();
            // Cria uma thread do servidor para tratar a conexão
            Server1 tratamento = new Server1((java.net.Socket) cliente);
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

            if (operacao == 5) {

                opr = '^';
                total = Math.pow(num1,num2);

            } else if (operacao == 6) {

                opr = '%';
                total = (num1*num2)/100;

            } else {

                opr = '√';
                total = (Math.pow (num1, 1.0 / num2));

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