package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import requisito.FachadaMotorista;

import java.awt.*;

public class TelaCadastroMotorista extends JDialog {
	private JTextField textField_Motorista;
	private JButton btnSalvar;
	private JButton btnCancelar;
	private JTextField textField_cnh;

	public TelaCadastroMotorista(Frame parent) {
		super(parent, "Cadastrar novo motorista", true);
		setSize(380, 320);
		setLocationRelativeTo(parent);
		setResizable(false);

		// Painel central que vai conter o formulário empilhado verticalmente
		JPanel painelFormulario = new JPanel();
		painelFormulario.setBounds(0, 0, 364, 238);

		// bordas da tela
		painelFormulario.setBorder(new EmptyBorder(15, 15, 15, 15));
		painelFormulario.setLayout(null);

		// --- Campo: Destino ---
		JLabel lblNome = new JLabel("Nome do motorista:");
		lblNome.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblNome.setBounds(10, 50, 165, 16);
		painelFormulario.add(lblNome);
		textField_Motorista = new JTextField();
		textField_Motorista.setBounds(10, 69, 334, 25);
		textField_Motorista.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
		painelFormulario.add(textField_Motorista);
		getContentPane().setLayout(null);

		// Adiciona o formulário no centro da tela
		getContentPane().add(painelFormulario);

		JLabel lblNumeroDaCnh = new JLabel("Numero da CNH:");
		lblNumeroDaCnh.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblNumeroDaCnh.setBounds(10, 105, 150, 16);
		painelFormulario.add(lblNumeroDaCnh);

		textField_cnh = new JTextField();
		textField_cnh.setMaximumSize(new Dimension(2147483647, 25));
		textField_cnh.setBounds(10, 124, 334, 25);
		painelFormulario.add(textField_cnh);

		JLabel Label_feedback = new JLabel("");
		Label_feedback.setHorizontalAlignment(SwingConstants.CENTER);
		Label_feedback.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		Label_feedback.setBounds(10, 173, 334, 31);
		painelFormulario.add(Label_feedback);

		// --- Rodapé: Botões ---
		JPanel painelBotoes = new JPanel();
		painelBotoes.setBounds(0, 238, 364, 43);
		painelBotoes.setBorder(new EmptyBorder(0, 0, 10, 10)); // Margem nos botões
		btnCancelar = new JButton("Cancelar");
		btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnCancelar.setBounds(251, 4, 103, 23);
		painelBotoes.setLayout(null);
		btnSalvar = new JButton("Cadastrar");
		btnSalvar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnSalvar.setBounds(23, 4, 97, 23);
		painelBotoes.add(btnSalvar);
		btnSalvar.addActionListener(e -> {
			try {
				salvarMotorista();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		painelBotoes.add(btnCancelar);

		// Adiciona os botões na parte de baixo da tela
		getContentPane().add(painelBotoes);

		// --- Eventos dos Botões ---
		btnCancelar.addActionListener(e -> dispose());
	}

	private void salvarMotorista() throws Exception {
		try {
			// cadastrar novo motorista
			String nome = textField_Motorista.getText();
			String cnh = textField_cnh.getText();
			if(FachadaMotorista.localizarMotorista(cnh)== null) {
				FachadaMotorista.criarMotorista(cnh, nome);
				System.out.println("Novo motorista cadastrado com sucesso...");
				dispose(); 
			}else {
				System.out.println("Cnh já cadastrada no sistema");
			}

		} catch (java.time.format.DateTimeParseException ex) {
			JOptionPane.showMessageDialog(this, "Formato de data inválido! Use DD/MM/AAAA.");
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage());
		}
	}
}