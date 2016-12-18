package br.superdia.se.teste;

import java.util.List;
import static javax.swing.JOptionPane.*;
import java.util.Properties;
import java.util.Scanner;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import br.superdia.modelo.Produto;
import br.superdia.sessionbean.IDAO;

public class SuperDiaSFSB {
	private static IDAO<Produto> iproduto;
	private static Produto produto;

	private static final String NOME_PROGRAMA = "Super Dia";

	public static void main(String[] args) {
		Properties props = new Properties();
		props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
		props.setProperty("org.omg.CORBA.ORBInitialHost", "localhost");
		props.setProperty("org.omg.CORBA.ORBInitialPort", "3700"); 

		InitialContext ic;
		try {
			ic = new InitialContext(props);

			iproduto = (IDAO<Produto>) ic.lookup("br.superdia.sessionbean.IDAO");

			menu();

			System.exit(0);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void menu(){
		int opcao;
		String opcoes[] = {"Adicionar", "Listar", "Sair"};

		do {
			opcao = showOptionDialog(null, "Escolha um comando abaixo.", NOME_PROGRAMA, DEFAULT_OPTION, QUESTION_MESSAGE, null, opcoes, opcoes[0]);

			if (opcao != CLOSED_OPTION && opcao != 2) {
				switch(opcao) {
				case 0: adiciona(); break;
				case 1: lista(); break;
				}
			}
		}while(opcao != CLOSED_OPTION && opcao != 2);
	}

	public static Produto adiciona() {
		produto = new Produto();

		String nome = lerString("Nome: ", "Você deve fornecer o nome!", "Adicionar Produto", false);
		String descricao = lerString("Descrição: ", "Vocẽ deve fornecer uma descrição", "Adiconar Produto", false);
		Double preco = lerNumeroReal("Preço: ", "Você deve fornecer o preço", "Adiciona Produto", false);
		String vendidoPor = lerString("Vendido Por: ", "Vocẽ deve fornecer quem vendeu o produto", "Adiconar Produto", false);
		Integer estoqueMinimo = lerNumeroInteiro("Estoque Mínimo: ", "Você deve fornecer o estoque mínimo!", "Adicionar Produto", false);
		Integer quantidadeEstoque = lerNumeroInteiro("Quantidade de Estoque: ", "Você deve fornecer a quantidade de estoque!", "Adicionar Produto", false);
		
		produto.setNome(nome);
		produto.setDescricao(descricao);
		produto.setPreco(preco);
		produto.setVendidoPor(vendidoPor);
		produto.setEstoqueMinimo(estoqueMinimo);
		produto.setQuantidadeEstoque(quantidadeEstoque);
		
		iproduto.add(produto);
		
		return produto;
	}

	public static void lista() {
		List<Produto> produtos = iproduto.getAll(Produto.class);

		for (int i = 0; i < produtos.size(); i++) {
			printProduto(produtos.get(i));
		}

	}

	public static void printProduto(Produto produto) {
		msgInfo(String.format("Nome: %s\nDescrição: %s\nPreço: %1.2f\nVendido Por: %s\nEstoque Mínimo: %d\n"
				+ "Quantidade em Estoque: %d", 
				produto.getNome(), produto.getDescricao(), produto.getPreco(), 
				produto.getVendidoPor(), produto.getEstoqueMinimo(), produto.getQuantidadeEstoque()), "Editar Produto");
	}
	
	public static String lerString(String prompt, String msgErro, String modulo, boolean vazia) {
		String string;

		do {
			string = showInputDialog(null, prompt, modulo, QUESTION_MESSAGE);

			if (string == null) break;

			if (string.equals("") && !vazia)
				showMessageDialog(null, msgErro, modulo, ERROR_MESSAGE);
		} while(string.equals("") && !vazia);
		return string;
	}

	public static Double lerNumeroReal(String prompt, String msgErro, String modulo, boolean vazio) {
		String valor;

		do {
			valor = showInputDialog(null, prompt, modulo, QUESTION_MESSAGE);
			
			if (valor == null) return null;

			if (valor.equals("") && !vazio)
				showMessageDialog(null, msgErro, modulo, ERROR_MESSAGE);
		} while (valor.equals("") && !vazio);

		// COnverte a string lida pra double.
		return Double.parseDouble(valor);
	}
	
	public static Integer lerNumeroInteiro(String prompt, String msgErro, String modulo, boolean vazio) {
		String valor;

		do {
			valor = showInputDialog(null, prompt, modulo, QUESTION_MESSAGE);
			
			if (valor == null) return null;

			if (valor.equals("") && !vazio)
				showMessageDialog(null, msgErro, modulo, ERROR_MESSAGE);
		} while (valor.equals("") && !vazio);

		// COnverte a string lida pra inteiro.
		return Integer.parseInt(valor);
	}
	
	private static void msgInfo(Object mensagem, String titulo) {
		showMessageDialog(null, mensagem, titulo, INFORMATION_MESSAGE);
	}

	private static void msgErro(Object mensagem, String titulo) {
		showMessageDialog(null, mensagem, titulo, ERROR_MESSAGE);
	}

}
