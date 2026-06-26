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
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import controller.ControllerMotorista;
import controller.ControllerViagem;
import model.Viagem;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.ScrollPaneConstants;

public class TelaViagens {
	protected static final Frame Frame = null;
	private JLabel label;
	private JLabel label_1;
	private JDialog frame;
	private JScrollPane scrollPane;
	private JTable table;
	private JTextField textFieldDestino;
	private JTextField textFieldMotorista;
	private JTextField textFieldPlaca;
	private JButton buttonCriar;
	private JButton buttonApagar;
	private JButton buttonAtualizar;
	private JButton buttonLimpar;
	private JButton btnCarregarFoto;
	private JButton buttonLimparFoto;
	private JPanel panel;
	private BufferedImage buffer; // armazena a foto na mem�ria durante a edicao
	private int idSelecionada = 0;
	private JTextField textFieldCNH;
	private JTextField textFieldData;


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
		label.setBounds(26, 371, 436, 29);
		frame.getContentPane().add(label);

		panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(new TitledBorder("Foto"));
		panel.setBounds(639, 245, 108, 118);
		frame.getContentPane().add(panel);

		// FOTO
		label_1 = new JLabel("sem foto");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		// label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setBounds(10, 21, 78, 73);
		panel.add(label_1);

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
					if (table.getSelectedRow() >= 0) {
						// 1. Pega o ID da linha selecionada
						int id = (int) table.getValueAt(table.getSelectedRow(), 0);
						
						// 2. Busca a viagem completa trazendo o motorista pelo FETCH
						Viagem v = ControllerViagem.localizarViagemComMotorista(id);
						
						// 3. Preenche os campos da tela usando a mesma variável 'v'
						textFieldDestino.setText(v.getDestino());
						
						// Se a viagem tiver data, descomente a linha abaixo para preencher o campo data também:
						// java.time.format.DateTimeFormatter formatador = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
						// textFieldData.setText(v.getData().format(formatador));

						// Preenche os dados do motorista carregado via LAZY/FETCH com segurança
						if (v.getMotorista() != null) {
							textFieldMotorista.setText(v.getMotorista().getNome());
							textFieldCNH.setText(v.getMotorista().getCnh());
						} else {
							textFieldMotorista.setText("Sem motorista");
							textFieldCNH.setText("");
						}

						// Preenche os dados do veículo
						if (v.getVeiculo() != null) {
							textFieldPlaca.setText(v.getVeiculo().getPlaca());
						} else {
							textFieldPlaca.setText("");
						}
						// carregar foto
						if (v.getMotorista().getFoto() != null) {
							// converte byte[] para BufferedImage do icon do label
							InputStream in = new ByteArrayInputStream(v.getMotorista().getFoto());
							buffer = ImageIO.read(in);
							ImageIcon icon = new ImageIcon(buffer.getScaledInstance(buffer.getWidth(),
									buffer.getHeight(), Image.SCALE_DEFAULT));
							icon.setImage(
									icon.getImage().getScaledInstance(label_1.getWidth(), label_1.getHeight(), 1));
							label_1.setIcon(icon);
						} else {
							buffer = null;
							label_1.setText("sem foto"); // limpa a imagem
							label_1.setIcon(null);
						}

					}
				} catch (Exception erro) {
					label.setText(erro.getMessage());
				}
			}
		});

		textFieldDestino = new JTextField();
		textFieldDestino.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		textFieldDestino.setBounds(111, 263, 179, 25);
		frame.getContentPane().add(textFieldDestino);
		textFieldDestino.setColumns(10);

		JLabel lblNewLabel = new JLabel("Destino: ");
		lblNewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblNewLabel.setBounds(26, 262, 75, 25);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Motorista:");
		lblNewLabel_1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(26, 299, 75, 25);
		frame.getContentPane().add(lblNewLabel_1);

		textFieldMotorista = new JTextField();
		textFieldMotorista.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		textFieldMotorista.setColumns(10);
		textFieldMotorista.setBounds(111, 300, 179, 25);
		frame.getContentPane().add(textFieldMotorista);

		JLabel lblNewLabel_1_1 = new JLabel("Placa:");
		lblNewLabel_1_1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblNewLabel_1_1.setBounds(26, 335, 75, 25);
		frame.getContentPane().add(lblNewLabel_1_1);

		textFieldPlaca = new JTextField();
		textFieldPlaca.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		textFieldPlaca.setColumns(10);
		textFieldPlaca.setBounds(111, 336, 179, 25);
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
		buttonAtualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (textFieldMotorista.getText().isEmpty())
					label.setText("nome vazio");
				else
					atualizarViagemSelecionada();
				label.setText("Registro de viagem atualizado.");
			}
		});
		buttonAtualizar.setBounds(126, 411, 95, 23);
		frame.getContentPane().add(buttonAtualizar);

		buttonApagar = new JButton("Apagar");
		buttonApagar.setToolTipText("apagar pessoa e seus dados");

		buttonApagar.setBounds(231, 411, 95, 23);
		frame.getContentPane().add(buttonApagar);

		buttonLimpar = new JButton("Limpar");
		buttonLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFieldDestino.setText("");
				textFieldMotorista.setText("");
				textFieldPlaca.setText("");

			}
		});
		buttonLimpar.setBounds(336, 411, 95, 23);
		frame.getContentPane().add(buttonLimpar);

		btnCarregarFoto = new JButton("Carregar foto");
		btnCarregarFoto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (textFieldPlaca.getText().isEmpty()) {
					label.setText("selecione uma pessoa");
					return;
				}
				File file = selecionarArquivoFoto();
				if (file == null)
					return; // arquivo nao foi selecionado

				try {
					buffer = ImageIO.read(file); 
					System.out.println("-> FOTO CARREGADA NO BOTÃO: " + buffer);
					ImageIcon icon = new ImageIcon(
					buffer.getScaledInstance(buffer.getWidth(), buffer.getHeight(), Image.SCALE_DEFAULT));
					icon.setImage(icon.getImage().getScaledInstance(label_1.getWidth(), label_1.getHeight(), 1));
					label_1.setIcon(icon);
					label.setText("Precisa atualizar/criar pessoa para salvar a foto");
				} catch (IOException ex) {
					label.setText(ex.getMessage());
				}
			}
		});
		btnCarregarFoto.setBounds(639, 374, 108, 23);
		frame.getContentPane().add(btnCarregarFoto);

		buttonLimparFoto = new JButton("Limpar foto");
		buttonLimparFoto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buffer = null;
				label_1.setIcon(null);
				label_1.setText("sem foto");
				label.setText("");
				label.setText("Precisa atualizar/criar pessoa para salvar a foto");
			}
		});
		buttonLimparFoto.setBounds(639, 408, 108, 23);
		frame.getContentPane().add(buttonLimparFoto);

		textFieldCNH = new JTextField();
		textFieldCNH.setToolTipText("CNH");
		textFieldCNH.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		textFieldCNH.setColumns(10);
		textFieldCNH.setBounds(341, 300, 179, 25);
		frame.getContentPane().add(textFieldCNH);

		textFieldData = new JTextField();
		textFieldData.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		textFieldData.setColumns(10);
		textFieldData.setBounds(340, 263, 179, 25);
		frame.getContentPane().add(textFieldData);

		JLabel lblData = new JLabel("Data: ");
		lblData.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblData.setBounds(300, 262, 50, 25);
		frame.getContentPane().add(lblData);

		JLabel lblCnh = new JLabel("Cnh: ");
		lblCnh.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblCnh.setBounds(300, 299, 60, 25);
		frame.getContentPane().add(lblCnh);

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
			List<Viagem> lista = ControllerViagem.listarViagens();
			java.time.format.DateTimeFormatter formatador = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
			
			for (Viagem v : lista) {
				model.addRow(new Object[] { v.getId(), v.getDestino(), v.getData().format(formatador)});
			};
			
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

	public File selecionarArquivoFoto() {
	    JFileChooser chooser = new JFileChooser();	    
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("Imagens (*.jpg, *.jpeg, *.gif)", "jpg", "jpeg", "gif");
	    chooser.setFileFilter(filter);
	    
	    try {
	        File pastaFotos = new File("." + File.separator + "src" + File.separator + "fotos");	        
	        if (pastaFotos.exists() && pastaFotos.isDirectory()) {
	            chooser.setCurrentDirectory(pastaFotos);
	        }
	    } catch (Exception e) {
	        // Registra o erro no console se a busca pela pasta falhar
	        System.err.println("Erro ao definir diretório inicial: " + e.getMessage());
	    }
	    
	    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    
	    // Verifica se o usuário de fato clicou em "Abrir"
	    int retorno = chooser.showOpenDialog(null);	    
	    if (retorno == JFileChooser.APPROVE_OPTION) {
	        return chooser.getSelectedFile();
	    }	    
	    // Retorna null se o usuário cancelou ou fechou a janela
	    return null; 
	}

	public void atualizarViagemSelecionada() {
		try {
			String destino = textFieldDestino.getText();
			String motorista = textFieldMotorista.getText();
			String placa = textFieldPlaca.getText();
			String cnh = textFieldCNH.getText();	


			// parte da foto
			System.out.println("-> FOTO CARREGADA NO BOTÃO: " + buffer);
			byte[] bytesfoto = null;
			if (buffer != null)
				try {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(buffer, "jpg", baos);
					bytesfoto = baos.toByteArray();
					System.out.println(bytesfoto);
					baos.close();
				} catch (IOException ex1) {
					label.setText("problema na conversao da imagem em bytes");
				}
			System.out.println("DEBUG: Executando o primeiro controller...");
	        ControllerMotorista.salvarFoto(cnh, bytesfoto);
	        System.out.println("DEBUG: Primeiro controller finalizou com sucesso!");

	        System.out.println("DEBUG: Executando o segundo controller...");
	        ControllerViagem.alterarViagem(idSelecionada, destino, cnh, placa, null);
	        System.out.println("DEBUG: Segundo controller finalizou com sucesso!");
			label.setText("Registro de viagem atualizado");
			listagem();
		} catch (Exception ex2) {
			label.setText(ex2.getMessage());
		}
	}
}
