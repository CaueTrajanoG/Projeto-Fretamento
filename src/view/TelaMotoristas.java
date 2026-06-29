package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import model.Motorista;
import model.Viagem;
import requisito.FachadaMotorista;
import requisito.FachadaViagem;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.SwingConstants;

public class TelaMotoristas {
	protected static final Frame Frame = null;
	private JDialog frame;
	private JScrollPane scrollPane;
	private JTable table;
	private JButton btn_refresh;
	private JTextField textField_nome;
	private JTextField textFieldCnh;
	private JLabel lblNewLabel;
	private JLabel lblCnh;
	private JTable table_historico;
	private BufferedImage buffer;
	private JLabel labelFoto = new JLabel("sem foto");
	public TelaMotoristas() {
		initialize();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {

			}
		});
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	private void initialize() {
		frame = new JDialog();
		frame.setResizable(false);
		frame.setModal(true);
		frame.setTitle("Registro de Motoristas");
		frame.setBounds(100, 100, 572, 478);
		frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		// chama o evento de listar os dados do banco de dados
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				listagem();
			}
		});

		scrollPane = new JScrollPane();
		scrollPane.setBounds(28, 34, 275, 180);
		frame.getContentPane().add(scrollPane);

		table = new JTable() {
			public boolean isCellEditable(int rowIndex, int vColIndex) {
				return false; // desabilita edicao de celulas
			}
		};

		table.setGridColor(Color.BLACK);
		table.setRequestFocusEnabled(false);
		table.setFocusable(false);
		table.setBackground(Color.WHITE);
		table.setFillsViewportHeight(true);
		table.setRowSelectionAllowed(true);
		table.setFont(new Font("Tahoma", Font.PLAIN, 14));
		scrollPane.setViewportView(table);
		table.setBorder(new LineBorder(new Color(0, 0, 0)));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		table.setShowGrid(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		JButton btnCadastrar = new JButton("Novo Motorista");
		btnCadastrar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnCadastrar.setBounds(171, 356, 132, 35);
		btnCadastrar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				TelaCadastroMotorista tela = new TelaCadastroMotorista(Frame);
				tela.setVisible(true);
			}
		});
		frame.getContentPane().add(btnCadastrar);

		JButton btnAtualizar = new JButton("Atualizar");
		btnAtualizar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnAtualizar.setBounds(28, 356, 132, 35);
		btnAtualizar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// CORRETO: Agora a chamada está dentro de um método executável!
				atualizarMotorista();
			}
		});
		frame.getContentPane().add(btnAtualizar);

		btn_refresh = new JButton("Refresh");
		btn_refresh.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btn_refresh.setBounds(316, 245, 103, 35);
		btn_refresh.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// CORRETO: Agora a chamada está dentro de um método executável!
				listagem();
			}
		});
		frame.getContentPane().add(btn_refresh);

		textField_nome = new JTextField();
		textField_nome.setBounds(28, 246, 275, 35);
		frame.getContentPane().add(textField_nome);
		textField_nome.setColumns(10);

		textFieldCnh = new JTextField();
		textFieldCnh.setColumns(10);
		textFieldCnh.setBounds(28, 303, 275, 35);
		frame.getContentPane().add(textFieldCnh);

		lblNewLabel = new JLabel("Nome");
		lblNewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblNewLabel.setBounds(28, 225, 118, 25);
		frame.getContentPane().add(lblNewLabel);

		lblCnh = new JLabel("CNH");
		lblCnh.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblCnh.setBounds(28, 285, 118, 25);
		frame.getContentPane().add(lblCnh);

		JScrollPane scrollPane_historico = new JScrollPane();
		scrollPane_historico.setBounds(316, 34, 230, 180);
		frame.getContentPane().add(scrollPane_historico);
		table_historico = new JTable() {
			public boolean isCellEditable(int rowIndex, int vColIndex) {
				return false;
			} // impedir edição
		};
		table_historico.setFillsViewportHeight(true);
		table_historico.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane_historico.setViewportView(table_historico);

		JLabel lblNewLabel_2 = new JLabel("Motoristas");
		lblNewLabel_2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblNewLabel_2.setBounds(28, 9, 103, 25);
		frame.getContentPane().add(lblNewLabel_2);

		JLabel lblNewLabel_2_1 = new JLabel("Histórico");
		lblNewLabel_2_1.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblNewLabel_2_1.setBounds(316, 9, 103, 25);
		frame.getContentPane().add(lblNewLabel_2_1);
		
		JPanel panelFoto = new JPanel();
		panelFoto.setLayout(null);
		panelFoto.setBorder(new TitledBorder("Foto"));
		panelFoto.setBounds(438, 225, 108, 118);
		frame.getContentPane().add(panelFoto);
		

		labelFoto.setHorizontalAlignment(SwingConstants.CENTER);
		labelFoto.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		labelFoto.setBounds(10, 21, 88, 86);
		panelFoto.add(labelFoto);
		
		JButton btnCarregarFoto = new JButton("+");
		btnCarregarFoto.setFont(new Font("Segoe UI", Font.PLAIN, 10));
		btnCarregarFoto.setBounds(448, 346, 39, 45);
		frame.getContentPane().add(btnCarregarFoto);
		
		JButton btnLimparFoto = new JButton("X");
		btnLimparFoto.setFont(new Font("Segoe UI", Font.PLAIN, 10));
		btnLimparFoto.setBounds(497, 346, 39, 45);
		frame.getContentPane().add(btnLimparFoto);
		
		JButton btnApagar = new JButton("Apagar");
		btnApagar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnApagar.setBounds(316, 356, 103, 35);
		frame.getContentPane().add(btnApagar);
		btnApagar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Captura a CNH atual do campo de texto antes de chamar a exclusão
				String cnhSelecionada = textFieldCnh.getText();
				apagarMotoristaSelecionado(cnhSelecionada);
			}
		});
		
		JLabel label = new JLabel("");
		label.setBounds(28, 402, 508, 26);
		frame.getContentPane().add(label);		
		
		// Selecionar linha da tabela
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					// label.setText("");
					if (table.getSelectedRow() >= 0) {
						// copiar a pessoa selecionada para formulario de edicao
						String cnh = (String) table.getValueAt(table.getSelectedRow(), 1);
						Motorista m = FachadaMotorista.localizarMotorista(cnh);
						textField_nome.setText(m.getNome());
						textFieldCnh.setText(m.getCnh());

						DefaultTableModel modelHist = new DefaultTableModel();
						modelHist.addColumn("ID");
						modelHist.addColumn("Destino");
						table_historico.setModel(modelHist);

						if (m.getListaViagem() != null) {
							for (Viagem v : m.getListaViagem()) {
								modelHist.addRow(new Object[] { v.getId(), v.getDestino() });
							}
						}
						
						if (m.getFoto() != null) {
							// converte byte[] para BufferedImage do icon do label
							InputStream in = new ByteArrayInputStream(m.getFoto());
							buffer = ImageIO.read(in);
							ImageIcon icon = new ImageIcon(buffer.getScaledInstance(buffer.getWidth(),
									buffer.getHeight(), Image.SCALE_DEFAULT));
							icon.setImage(
									icon.getImage().getScaledInstance(labelFoto.getWidth(), labelFoto.getHeight(), 1));
							labelFoto.setIcon(icon);
						} else {
							buffer = null;
							labelFoto.setText("sem foto"); // limpa a imagem
							labelFoto.setIcon(null);
						}
						
					}
				} catch (Exception erro) {
					erro.printStackTrace();
				}
			}
		});
	}

	// função que busca os dados
	public void listagem() {
		try {
			// objeto model contem todas as linhas e colunas da tabela
			DefaultTableModel model = new DefaultTableModel();
			table.setModel(model);

			// adicionar as colunas (0,1,2) do grid
			model.addColumn("Nome");
			model.addColumn("CNH");

			// adicionar as linhas do grid
			List<Motorista> lista = FachadaMotorista.listarMotoristas();
			for (Motorista m : lista) {
				model.addRow(new Object[] { m.getNome(), m.getCnh() });
			};	


		} catch (Exception erro) {
			// label.setText(erro.getMessage());
		}
	}

	public void atualizarMotorista() {
	    try {
	        String nomeDigitado = textField_nome.getText();   // Campo de cima (Nome)
	        String cnhDigitada = textFieldCnh.getText();  // Campo de baixo (CNH)

	        // 1. Primeiro validamos se a CNH realmente existe no banco
	        Motorista m = FachadaMotorista.localizarMotorista(cnhDigitada);
	        if (m == null) {
	            JOptionPane.showMessageDialog(frame, "Motorista não encontrado com a CNH: " + cnhDigitada);
	        } else { 
	            FachadaMotorista.alterarMotorista(cnhDigitada, nomeDigitado);	            
	            JOptionPane.showMessageDialog(frame, "Motorista atualizado com sucesso!");
	        }
	        listagem(); // Recarrega a tabela e limpa as seleções antigas
	        
	    } catch (Exception ex2) {
	        JOptionPane.showMessageDialog(frame, "Erro ao atualizar: " + ex2.getMessage());
	        ex2.printStackTrace();
	    }
	}
	
	public void apagarMotoristaSelecionado(String cnh) {
	    try {
	        // Validação se a CNH é válida
	        if (cnh == null || cnh.trim().isEmpty()) {
	            javax.swing.JOptionPane.showMessageDialog(
	                frame, 
	                "Motorista não selecionado. Selecione um registro na tabela.", 
	                "Aviso", 
	                javax.swing.JOptionPane.WARNING_MESSAGE
	            );
	            return;
	        }

	        // Busca os dados atuais do motorista para avaliar o histórico de viagens
	        Motorista m = FachadaMotorista.localizarMotorista(cnh); 

	        //Impede a exclusão se possuir viagens vinculadas
	        if (m != null && m.getListaViagem() != null && !m.getListaViagem().isEmpty()) {
	            javax.swing.JOptionPane.showMessageDialog(
	                frame, 
	                "Não é possível apagar este motorista porque ele possui " + m.getListaViagem().size() + " viagem(ns) vinculada(s) ao seu histórico.", 
	                "Exclusão Bloqueada", 
	                javax.swing.JOptionPane.ERROR_MESSAGE
	            );
	            return; // Aborta a exclusão
	        }

	        // Se passou pela validação, pede a confirmação do usuário
	        int resposta = javax.swing.JOptionPane.showConfirmDialog(
	            frame, 
	            "Tem certeza que deseja apagar o motorista de CNH " + cnh + "?", 
	            "Confirmar Exclusão", 
	            javax.swing.JOptionPane.YES_NO_OPTION
	        );

	        if (resposta == javax.swing.JOptionPane.YES_OPTION) {
	            // Executa a exclusão
	            FachadaMotorista.apagarMotorista(cnh);	            
	            
	            //Limpa os campos da tela
	            textFieldCnh.setText("");
	            textField_nome.setText("");
	            if (labelFoto != null) {
	                labelFoto.setIcon(null);
	                labelFoto.setText("sem foto");
	            }
	            buffer = null; // Reseta o buffer                        
	            
	            //Atualiza o grid geral
	            listagem(); 
	            
	            javax.swing.JOptionPane.showMessageDialog(frame, "Motorista excluído com sucesso!");
	        }

	    } catch (Exception erro) {
	        javax.swing.JOptionPane.showMessageDialog(
	            frame, 
	            "Erro ao tentar apagar o motorista: " + erro.getMessage(), 
	            "Erro", 
	            javax.swing.JOptionPane.ERROR_MESSAGE
	        );
	        erro.printStackTrace();
	    }
	}
}
