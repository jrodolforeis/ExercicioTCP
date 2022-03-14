package tcpclient;

import pessoa.Pessoa;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPClient {

	public static void main(String[] args) {
		Scanner ler = new Scanner(System.in);
		String mensagem, nome, cel;
		DataInputStream in;
		ObjectInputStream inO;
		ObjectOutputStream outO;
		DataOutputStream out;
		Socket s = null;
		try {

			int port = 16868;
			InetAddress server = InetAddress.getLocalHost();
			s = new Socket(server, port);
			System.out.println("Conectado......");

			outO = new ObjectOutputStream(s.getOutputStream());
			out = new DataOutputStream(s.getOutputStream());
			inO = new ObjectInputStream(s.getInputStream());
			in = new DataInputStream(s.getInputStream());

			System.out.print("Digite Mensagem: ");
			mensagem = ler.nextLine();
			out.writeUTF(mensagem);
			System.out.println("Mensagem enviada para o servidor....");

			System.out.print("Digite Nome: ");
			nome = ler.nextLine();
			System.out.print("Digite cel: ");
			cel = ler.nextLine();
			Pessoa p = new Pessoa(nome, cel);
			outO.writeObject(p);
			System.out.println("Objeto enviado para o servidor....");

			System.out.println("Aguardando resposta do Servidor....");

			mensagem = in.readUTF();
			System.out.println("Mensagem recebida do Servidor:" + mensagem);

			System.out.println("Aguardando objeto do Servidor.....");
			Pessoa resposta = (Pessoa) inO.readObject();
			System.out.println("Objeto recebido do Servidor: " + resposta);
		} catch (UnknownHostException ex) {
			Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			if (s != null)
				try {
					s.close();
				} catch (IOException ex) {
					Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
				}
		}

	}

}