package view;

import java.awt.Color;
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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import model.Viagem;
import requisito.FachadaMotorista;
import requisito.FachadaViagem;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class TelaViagens {
	protected static final Frame Frame = null;
	private JLabel label;
	private JLabel labelFoto;
	private JDialog frame;
	private JScrollPane scrollPane;
	private JTable table;
	private Viagem viagemSelecionada;
	private JTextField textFieldDestino;
	private JTextField textFieldMotorista;
	private JTextField textFieldPlaca;
	private JTextField textFieldCNH;
	private JTextField textFieldData;

	private JButton buttonCriar;
	private JButton buttonApagar;
	private JButton buttonAtualizar;
	private JButton buttonLimpar;
	private JButton btnCarregarFoto;
	private JButton buttonLimparFoto;

	private JPanel panelFoto;
	private BufferedImage buffer; // armazena a foto na mem�ria durante a edicao
	private int idSelecionada = 0;

	public TelaViagens() {
		initialize();
		frame.setVisible(true);
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	private void initialize() {
		frame = new JDialog();
		frame.setResizable(false);
		frame.setModal(true);
		frame.setTitle("Registro de Viagens");
		frame.setBounds(100, 100, 783, 484);
		frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		// chama o evento de listar os dados na tabela
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				listagem();
			}
		});	

		label = new JLabel("");
		label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		label.setForeground(Color.RED);
		label.setBounds(26, 371, 605, 29);
		frame.getContentPane().add(label);

		panelFoto = new JPanel();
		panelFoto.setLayout(null);
		panelFoto.setBorder(new TitledBorder("Foto"));
		panelFoto.setBounds(639, 245, 108, 118);
		frame.getContentPane().add(panelFoto);

		// FOTO
		labelFoto = new JLabel("sem foto");
		labelFoto.setHorizontalAlignment(SwingConstants.CENTER);
		labelFoto.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		labelFoto.setBounds(10, 21, 88, 86);
		panelFoto.add(labelFoto);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(26, 34, 715, 200);
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

		// Selecionar linha da tabela
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					label.setText("");
					labelFoto.setIcon(null); // Limpa o ícone anterior

					if (table.getSelectedRow() >= 0) {
						// Pega o ID da linha selecionada
						int id = (int) table.getValueAt(table.getSelectedRow(), 0);
						idSelecionada = (int) table.getValueAt(table.getSelectedRow(), 0);

						// Busca a viagem trazendo o motorista, por causa do lazy
						viagemSelecionada = FachadaViagem.localizarViagemComMotorista(id);

						// Preenche os campos da tela
						textFieldDestino.setText(viagemSelecionada.getDestino());

						java.time.format.DateTimeFormatter formatador = java.time.format.DateTimeFormatter
								.ofPattern("dd/MM/yyyy");
						textFieldData.setText(viagemSelecionada.getData().format(formatador));

						// Preenche os dados do motorista carregado via LAZY/FETCH
						if (viagemSelecionada.getMotorista() != null) {
							textFieldMotorista.setText(viagemSelecionada.getMotorista().getNome());
							textFieldCNH.setText(viagemSelecionada.getMotorista().getCnh());
							byte[] fotoBytes = viagemSelecionada.getMotorista().getFoto();

							if (fotoBytes != null && fotoBytes.length > 0) {
								InputStream in = new ByteArrayInputStream(fotoBytes);

								buffer = ImageIO.read(in);
								//System.out.println("-> DEBUG FOTO: O buffer gerado é: " + buffer);
								// Verifica se o ImageIO conseguiu gerar a imagem com sucesso antes de ler a
								// largura/altura
								if (buffer != null) {
									ImageIcon icon = new ImageIcon(buffer.getScaledInstance(labelFoto.getWidth(),
											labelFoto.getHeight(), Image.SCALE_SMOOTH));
									labelFoto.setIcon(icon);
									labelFoto.setText(""); // Remove o texto se houver foto
								} else {
									// Caso os bytes estejam corrompidos e o buffer resulte em null
									buffer = null;
									labelFoto.setText("erro na foto");
								}
							} else {
								// Motorista existe, mas não tem foto salva
								buffer = null;
								labelFoto.setText("sem foto");
							}
						} else {
							// Se a viagem não tiver motorista nenhum vinculado
							textFieldMotorista.setText("Sem motorista");
							textFieldCNH.setText("");
							buffer = null;
							labelFoto.setText("sem foto");
						}
						// Preenche os dados do veículo
						if (viagemSelecionada.getVeiculo() != null) {
							textFieldPlaca.setText(viagemSelecionada.getVeiculo().getPlaca());
						} else {
							textFieldPlaca.setText("");
						}
					}
					labelFoto.revalidate();
					labelFoto.repaint();
				} catch (Exception erro) {
					label.setText("Erro ao carregar dados: " + erro.getMessage());
					erro.printStackTrace();
				}
			}
		});

		textFieldDestino = new JTextField();
		textFieldDestino.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		textFieldDestino.setBounds(111, 263, 179, 25);
		frame.getContentPane().add(textFieldDestino);
		textFieldDestino.setColumns(10);

		textFieldCNH = new JTextField();
		textFieldCNH.setToolTipText("CNH");
		textFieldCNH.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		textFieldCNH.setColumns(10);
		textFieldCNH.setBounds(397, 300, 179, 25);
		frame.getContentPane().add(textFieldCNH);

		JLabel lblNewLabel = new JLabel("Destino: ");
		lblNewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblNewLabel.setBounds(26, 262, 75, 25);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Motorista:");
		lblNewLabel_1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(312, 263, 75, 25);
		frame.getContentPane().add(lblNewLabel_1);

		textFieldMotorista = new JTextField();
		textFieldMotorista.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		textFieldMotorista.setColumns(10);
		textFieldMotorista.setBounds(397, 264, 179, 25);
		frame.getContentPane().add(textFieldMotorista);

		JLabel lblNewLabel_1_1 = new JLabel("Placa:");
		lblNewLabel_1_1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblNewLabel_1_1.setBounds(312, 334, 75, 25);
		frame.getContentPane().add(lblNewLabel_1_1);

		textFieldPlaca = new JTextField();
		textFieldPlaca.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		textFieldPlaca.setColumns(10);
		textFieldPlaca.setBounds(397, 335, 179, 25);
		frame.getContentPane().add(textFieldPlaca);

		// botao para abrir tela de criação de nova viagem
		buttonCriar = new JButton("Criar");
		buttonCriar.setToolTipText("cadastrar nova pessoa");
		buttonCriar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				TelaCadastroViagem tela = new TelaCadastroViagem(Frame);
				tela.setVisible(true);
			}
		});
		buttonCriar.setBounds(21, 411, 95, 23);
		frame.getContentPane().add(buttonCriar);

		buttonAtualizar = new JButton("Atualizar");
		buttonAtualizar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (viagemSelecionada == null) {
					JOptionPane.showMessageDialog(frame, "Selecione uma viagem na tabela para atualizar.");
					return;
				}
				try {					
					TelaAtualizarViagem tela = new TelaAtualizarViagem(Frame, viagemSelecionada);
					tela.setVisible(true);
					listagem();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame, "Erro ao abrir tela de edição: " + ex.getMessage());
					ex.printStackTrace();
				}
			}
		});
		buttonAtualizar.setBounds(121, 337, 156, 23); 											
		frame.getContentPane().add(buttonAtualizar);

		buttonApagar = new JButton("Apagar");
		buttonApagar.setToolTipText("apagar pessoa e seus dados");

		buttonApagar.setBounds(129, 411, 95, 23);
		frame.getContentPane().add(buttonApagar);

		buttonLimpar = new JButton("Limpar");
		buttonLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFieldDestino.setText("");
				textFieldMotorista.setText("");
				textFieldPlaca.setText("");
				labelFoto.setText("Sem Foto");
				textFieldCNH.setText("");
				textFieldData.setText("");
			}
		});
		buttonLimpar.setBounds(234, 411, 95, 23);
		frame.getContentPane().add(buttonLimpar);

		btnCarregarFoto = new JButton("Carregar");
		btnCarregarFoto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String cnh = textFieldCNH.getText();
				if (cnh == null || cnh.trim().isEmpty()) {
					label.setText("Selecione um motorista na tabela primeiro.");
					return;
				}
				File file = selecionarArquivoFoto();
				if (file == null)
					return;

				try {
					buffer = ImageIO.read(file);

					// Converte o buffer de imagem para byte[]
					java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
					ImageIO.write(buffer, "jpg", baos);
					byte[] fotoBytes = baos.toByteArray();

					//salva a foto armazenada no buffer
					atualizarFoto();

					label.setText("Foto Atualizada");

					// Atualiza a interface gráfica com o preview
					ImageIcon icon = new ImageIcon(
							buffer.getScaledInstance(buffer.getWidth(), buffer.getHeight(), Image.SCALE_DEFAULT));
					icon.setImage(icon.getImage().getScaledInstance(labelFoto.getWidth(), labelFoto.getHeight(),
							Image.SCALE_SMOOTH));
					labelFoto.setIcon(icon);

				} catch (IOException ex) {
					label.setText("Erro de I/O: " + ex.getMessage());
				} catch (Exception ex) {
					label.setText(ex.getMessage());
					ex.printStackTrace();
				}
			}
		});
		btnCarregarFoto.setBounds(639, 374, 108, 23);
		frame.getContentPane().add(btnCarregarFoto);

		buttonLimparFoto = new JButton("Limpar foto");
		buttonLimparFoto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buffer = null;
				labelFoto.setIcon(null);
				labelFoto.setText("sem foto");
				label.setText("");
				label.setText("Precisa atualizar/criar pessoa para salvar a foto");
			}
		});
		buttonLimparFoto.setBounds(639, 408, 108, 23);
		frame.getContentPane().add(buttonLimparFoto);

		textFieldData = new JTextField();
		textFieldData.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		textFieldData.setColumns(10);
		textFieldData.setBounds(111, 299, 179, 25);
		frame.getContentPane().add(textFieldData);

		JLabel lblData = new JLabel("Data: ");
		lblData.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblData.setBounds(26, 298, 50, 25);
		frame.getContentPane().add(lblData);

		JLabel lblCnh = new JLabel("Cnh: ");
		lblCnh.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblCnh.setBounds(311, 299, 60, 25);
		frame.getContentPane().add(lblCnh);
		
		JButton btnRefresh = new JButton("Refresh");		
		btnRefresh.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnRefresh.setBounds(339, 411, 96, 23);
		btnRefresh.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// CORRETO: Agora a chamada está dentro de um método executável!
				listagem();
			}
		});
		frame.getContentPane().add(btnRefresh);
	}
	// função que busca os dados
	public void listagem() {
		try {
			DefaultTableModel model = new DefaultTableModel();
			table.setModel(model);

			// colunas do grid
			model.addColumn("Id");
			model.addColumn("Destino");
			model.addColumn("Data");

			// adicionar as linhas do grid
			List<Viagem> lista = FachadaViagem.listarViagens();
			java.time.format.DateTimeFormatter formatador = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");

			for (Viagem v : lista) {
				model.addRow(new Object[] { v.getId(), v.getDestino(), v.getData().format(formatador) });
			}
			;

			javax.swing.table.DefaultTableCellRenderer centralizado = new javax.swing.table.DefaultTableCellRenderer();
			centralizado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

			// Passa por cada coluna da tabela aplicando o alinhamento
			for (int i = 0; i < table.getColumnCount(); i++) {
				table.getColumnModel().getColumn(i).setCellRenderer(centralizado);
			}
		} catch (Exception erro) {
			label.setText(erro.getMessage());
		}
	}

	public void atualizarViagemSelecionada() {
		try {
			if (idSelecionada == 0) {
				label.setText("Selecione uma viagem na tabela primeiro.");
				return;
			}

			String destino = textFieldDestino.getText();
			String placa = textFieldPlaca.getText();
			String cnh = textFieldCNH.getText();

			//System.out.println("DEBUG: Atualizando viagem ID: " + idSelecionada);

			// Altera os dados textuais da viagem e o vínculo do motorista
			FachadaViagem.alterarViagem(idSelecionada, destino, cnh, placa, null);

			label.setText("Registro de viagem atualizado com sucesso!");
			listagem(); // Atualiza a tabela

		} catch (Exception ex2) {
			label.setText(ex2.getMessage());
			ex2.printStackTrace();
		}
	}
	
	public File selecionarArquivoFoto() {
		JFileChooser chooser = new JFileChooser();
		try {
			chooser.setCurrentDirectory(new File((new File(".").getCanonicalPath() + "\\src\\fotos")));
		} catch (IOException e) {
		}
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.showOpenDialog(null);
		File file = chooser.getSelectedFile();
		return file;
	}
	
	public void atualizarFoto() throws Exception {
		byte[] bytesfoto = null;
		if (buffer != null)
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(buffer, "jpg", baos);
				bytesfoto = baos.toByteArray();
				baos.close();
			} catch (IOException ex1) {
				label.setText("problema na convers�o da imagem em bytes");
		}
		FachadaMotorista.alterarFoto(viagemSelecionada.getMotorista().getCnh(), bytesfoto);
	}
}
