package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
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

import controller.ControllerViagem;
import model.Viagem;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class TelaViagens {
	private JLabel label;
	private JLabel label_1;
	private JDialog frame;
	private JScrollPane scrollPane;
	private JTable table;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JButton button_1;
	private JButton button_3;
	private JButton button_2;
	private JButton button_4;
	private JButton btnCarregarFoto;
	private JButton button_6;
	private JPanel panel;
	private BufferedImage buffer; // armazena a foto na mem�ria durante a edicao

	public TelaViagens() {
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
		label.setBounds(26, 371, 600, 29);
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
						// copiar a pessoa selecionada para formulario de edicao
						int id = (int) table.getValueAt(table.getSelectedRow(), 0);
						Viagem v = ControllerViagem.localizarViagem(id);
						textField.setText(v.getDestino());
						textField_1.setText(v.getMotorista().getNome());
						textField_2.setText(v.getVeiculo().getPlaca());

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

		textField = new JTextField();
		textField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		textField.setBounds(111, 263, 179, 25);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		JLabel lblNewLabel = new JLabel("Destino: ");
		lblNewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblNewLabel.setBounds(26, 262, 75, 25);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Motorista:");
		lblNewLabel_1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(26, 299, 75, 25);
		frame.getContentPane().add(lblNewLabel_1);

		textField_1 = new JTextField();
		textField_1.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		textField_1.setColumns(10);
		textField_1.setBounds(111, 300, 179, 25);
		frame.getContentPane().add(textField_1);

		JLabel lblNewLabel_1_1 = new JLabel("Placa:");
		lblNewLabel_1_1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblNewLabel_1_1.setBounds(26, 335, 75, 25);
		frame.getContentPane().add(lblNewLabel_1_1);

		textField_2 = new JTextField();
		textField_2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		textField_2.setColumns(10);
		textField_2.setBounds(111, 336, 179, 25);
		frame.getContentPane().add(textField_2);

		button_1 = new JButton("Criar");
		button_1.setToolTipText("cadastrar nova pessoa");

		button_1.setBounds(21, 411, 95, 23);
		frame.getContentPane().add(button_1);

		button_2 = new JButton("Atualizar");
		button_2.setToolTipText("atualizar pessoa ");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// if (textField_1.getText().isEmpty())
				// label.setText("nome vazio");
				// else
				// atualizarPessoaSelecionada();
			}
		});
		button_2.setBounds(126, 411, 95, 23);
		frame.getContentPane().add(button_2);

		button_3 = new JButton("Apagar");
		button_3.setToolTipText("apagar pessoa e seus dados");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (textField_1.getText().isEmpty())
					label.setText("nome vazio");
				// else
				// apagarPessoaSelecionada();
			}
		});
		button_3.setBounds(231, 411, 95, 23);
		frame.getContentPane().add(button_3);

		button_4 = new JButton("Limpar");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField_1.setText("");
				textField_2.setText("");

			}
		});
		button_4.setBounds(336, 411, 95, 23);
		frame.getContentPane().add(button_4);

		btnCarregarFoto = new JButton("Carregar foto");
		btnCarregarFoto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (textField_2.getText().isEmpty()) {
					label.setText("selecione uma pessoa");
					return;
				}
				File file = selecionarArquivoFoto();
				if (file == null)
					return; // arquivo nao foi selecionado

				try {
					buffer = ImageIO.read(file); // ler imagem do arquivo
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

		button_6 = new JButton("Limpar foto");
		button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buffer = null;
				label_1.setIcon(null);
				label_1.setText("sem foto");
				label.setText("");
				label.setText("Precisa atualizar/criar pessoa para salvar a foto");

			}
		});
		button_6.setBounds(639, 408, 108, 23);
		frame.getContentPane().add(button_6);

	}

	// função que busca os dados
	public void listagem() {
		try {
			// objeto model contem todas as linhas e colunas da tabela
			DefaultTableModel model = new DefaultTableModel();
			table.setModel(model);

			// adicionar as colunas (0,1,2) do grid
			model.addColumn("Id");
			model.addColumn("Destino");
			model.addColumn("Motorista");
			model.addColumn("Veiculo");

			// adicionar as linhas do grid
			List<Viagem> lista = ControllerViagem.listarViagens();
			System.out.println(lista.getFirst());
			for (Viagem v : lista) {
				model.addRow(new Object[] { v.getId(), v.getDestino(), v.getMotorista().getNome(),
						v.getVeiculo().getPlaca() });
			}
			;

			// label_2.setText("resultados: " + lista.size() + " pessoas - selecione uma
			// linha para editar");
		} catch (Exception erro) {
			// label.setText(erro.getMessage());
		}
	}

	public File selecionarArquivoFoto() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Imagens", "jpg", "gif");
		chooser.setFileFilter(filter);
		try {
			// exibir pasta externa no Windows
			// chooser.setCurrentDirectory(new File("c:\\"));
			// exibir pasta interna \fotos
			chooser.setCurrentDirectory(new File((new File(".").getCanonicalPath() + "\\src\\fotos")));
		} catch (IOException e) {
		}
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.showOpenDialog(null);
		File file = chooser.getSelectedFile();
		return file;
	}
}
