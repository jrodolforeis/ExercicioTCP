package tcpserver;

import pessoa.Pessoa;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPServer {
	static Socket client = null;
	
	public static void main(String[] args) {
		try {
			ServerSocket slisten = new ServerSocket(16868);
			while (true) {
				System.out.println("Aguardando Conexao.");
				client = slisten.accept();
				Connection conexao = new Connection(client);
				conexao.start();
			}
		} catch (IOException ex) {
			Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static class Connection extends Thread {
		DataInputStream in;
		ObjectInputStream inObject;
		DataOutputStream out;
		ObjectOutputStream outObject;
		Socket client;

		public Connection(Socket client) {
			this.client = client;
			try {
				this.inObject = new ObjectInputStream(client.getInputStream());
				this.in = new DataInputStream(client.getInputStream());
			} catch (IOException ex) {
				Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
			}
			try {
				this.outObject = new ObjectOutputStream(client.getOutputStream());
				this.out = new DataOutputStream(client.getOutputStream());
			} catch (IOException ex) {
				Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		@Override
		public void run() {
			String mensagem;
			Scanner input = new Scanner(System.in);
			Pessoa pessoa = null;
			try {
				System.out.println("Aguardando Mensagem...");
				mensagem = in.readUTF();
				System.out.println("Mensagem do Cliente: " + mensagem);
				System.out.println("Agudardando Objeto.....");
				pessoa = (Pessoa) inObject.readObject();
				System.out.println("Objeto Recebido do Cliente: " + pessoa);
				System.out.println("Respondendo Mensagem...");
				System.out.print("Digite Mensagem de resposta: ");
				mensagem = input.nextLine();
				System.out.println("Enviando mensagem de resposta....");
				out.writeUTF(mensagem);

				System.out.println("Enviando objeto de resposta....");
				
				outObject.writeObject(new Pessoa("Teste", "99887766"));
				outObject.flush();
			} catch (IOException ex) {
				Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
			} catch (ClassNotFoundException cnfe) {
				Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, cnfe);
			} finally {
				try {
					if (client != null)
						client.close();
				} catch (IOException ex) {
					Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
				}
			}

		}
	}

}