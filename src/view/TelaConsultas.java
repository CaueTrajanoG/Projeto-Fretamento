package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import model.Motorista;
import model.Viagem;
import requisito.FachadaViagem; // Certifique-se de que os métodos correspondentes existem na sua Fachada

public class TelaConsultas {
	protected static final Frame Frame = null;
	private JDialog frame;
	private JLabel labelMensagem;
	private DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	// Componentes da Consulta 1 (Por Data)
	private JTextField textFieldDataBusca;
	private JTable tableData;
	private JTextArea textAreaLogData;
	private JButton btnPesquisarData;

	// Componentes da Consulta 2 (Por Placa)
	private JTextField textFieldPlacaBusca;
	private JTable tablePlaca;
	private JTextArea textAreaLogPlaca;
	private JButton btnPesquisarPlaca;

	// Componentes da Consulta 3 (Motoristas com mais de N viagens)
	private JTextField textFieldQtdViagens;
	private JTextField textFieldDestinoBusca;
	private JTable tableMotoristas;
	private JTextArea textAreaLogMotoristas;
	private JButton btnPesquisarMotoristas;

	public TelaConsultas() {
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
		frame.setTitle("Consultas Avançadas e Específicas");
		frame.setBounds(100, 100, 800, 530);
		frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		// Componente de abas para separar de forma limpa as 3 consultas
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		tabbedPane.setBounds(20, 20, 745, 410);
		frame.getContentPane().add(tabbedPane);

		// ====================================================================
		// ABA 1: LISTAR VIAGENS NA DATA X
		// ====================================================================
		JPanel panelAba1 = new JPanel();
		panelAba1.setLayout(null);
		tabbedPane.addTab("1. Viagens por Data", null, panelAba1, "Filtra todas as viagens de uma data específica");

		JLabel lblData = new JLabel("Data (dd/MM/yyyy):");
		lblData.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblData.setBounds(20, 20, 130, 25);
		panelAba1.add(lblData);

		textFieldDataBusca = new JTextField();
		textFieldDataBusca.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		textFieldDataBusca.setBounds(150, 21, 150, 25);
		panelAba1.add(textFieldDataBusca);
		textFieldDataBusca.setColumns(10);

		btnPesquisarData = new JButton("Pesquisar");
		btnPesquisarData.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnPesquisarData.setBounds(315, 20, 110, 26);
		panelAba1.add(btnPesquisarData);

		JScrollPane scrollTableData = new JScrollPane();
		scrollTableData.setBounds(20, 65, 700, 160);
		panelAba1.add(scrollTableData);

		tableData = criarConfigurarTabela();
		scrollTableData.setViewportView(tableData);

		JScrollPane scrollTextData = new JScrollPane();
		scrollTextData.setBounds(20, 240, 700, 120);
		panelAba1.add(scrollTextData);

		textAreaLogData = criarConfigurarTextArea();
		scrollTextData.setViewportView(textAreaLogData);

		// ====================================================================
		// ABA 2: LISTAR VIAGENS COM VEÍCULO DE PLACA X
		// ====================================================================
		JPanel panelAba2 = new JPanel();
		panelAba2.setLayout(null);
		tabbedPane.addTab("2. Viagens por Placa", null, panelAba2, "Filtra todas as viagens associadas a uma placa");

		JLabel lblPlaca = new JLabel("Placa do Veículo:");
		lblPlaca.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblPlaca.setBounds(20, 20, 130, 25);
		panelAba2.add(lblPlaca);

		textFieldPlacaBusca = new JTextField();
		textFieldPlacaBusca.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		textFieldPlacaBusca.setBounds(150, 21, 150, 25);
		panelAba2.add(textFieldPlacaBusca);

		btnPesquisarPlaca = new JButton("Pesquisar");
		btnPesquisarPlaca.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnPesquisarPlaca.setBounds(315, 20, 110, 26);
		panelAba2.add(btnPesquisarPlaca);

		JScrollPane scrollTablePlaca = new JScrollPane();
		scrollTablePlaca.setBounds(20, 65, 700, 160);
		panelAba2.add(scrollTablePlaca);

		tablePlaca = criarConfigurarTabela();
		scrollTablePlaca.setViewportView(tablePlaca);

		JScrollPane scrollTextPlaca = new JScrollPane();
		scrollTextPlaca.setBounds(20, 240, 700, 120);
		panelAba2.add(scrollTextPlaca);

		textAreaLogPlaca = criarConfigurarTextArea();
		scrollTextPlaca.setViewportView(textAreaLogPlaca);

		// ====================================================================
		// ABA 3: MOTORISTAS COM MAIS DE N VIAGENS COM DESTINO X
		// ====================================================================
		JPanel panelAba3 = new JPanel();
		panelAba3.setLayout(null);
		tabbedPane.addTab("3. Motoristas Frequentes", null, panelAba3, "Busca motoristas com mais de N viagens para um destino");

		JLabel lblQtd = new JLabel("Mínimo Viagens (N):");
		lblQtd.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblQtd.setBounds(20, 20, 130, 25);
		panelAba3.add(lblQtd);

		textFieldQtdViagens = new JTextField();
		textFieldQtdViagens.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		textFieldQtdViagens.setBounds(150, 21, 70, 25);
		panelAba3.add(textFieldQtdViagens);

		JLabel lblDestino = new JLabel("Destino:");
		lblDestino.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblDestino.setBounds(240, 20, 60, 25);
		panelAba3.add(lblDestino);

		textFieldDestinoBusca = new JTextField();
		textFieldDestinoBusca.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		textFieldDestinoBusca.setBounds(305, 21, 150, 25);
		panelAba3.add(textFieldDestinoBusca);

		btnPesquisarMotoristas = new JButton("Pesquisar");
		btnPesquisarMotoristas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnPesquisarMotoristas.setBounds(470, 20, 110, 26);
		panelAba3.add(btnPesquisarMotoristas);

		JScrollPane scrollTableMotoristas = new JScrollPane();
		scrollTableMotoristas.setBounds(20, 65, 700, 160);
		panelAba3.add(scrollTableMotoristas);

		tableMotoristas = criarConfigurarTabela();
		scrollTableMotoristas.setViewportView(tableMotoristas);

		JScrollPane scrollTextMotoristas = new JScrollPane();
		scrollTextMotoristas.setBounds(20, 240, 700, 120);
		panelAba3.add(scrollTextMotoristas);

		textAreaLogMotoristas = criarConfigurarTextArea();
		scrollTextMotoristas.setViewportView(textAreaLogMotoristas);

		// Label global de feedback para erros e validações na barra inferior da janela
		labelMensagem = new JLabel("");
		labelMensagem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		labelMensagem.setForeground(Color.RED);
		labelMensagem.setBounds(20, 442, 745, 25);
		frame.getContentPane().add(labelMensagem);

		// Configura os ouvintes de ação
		configurarAcoes();
	}

	private JTable criarConfigurarTabela() {
		JTable novaTabela = new JTable() {
			public boolean isCellEditable(int rowIndex, int vColIndex) {
				return false;
			}
		};
		novaTabela.setGridColor(Color.BLACK);
		novaTabela.setRequestFocusEnabled(false);
		novaTabela.setFocusable(false);
		novaTabela.setBackground(Color.WHITE);
		novaTabela.setFillsViewportHeight(true);
		novaTabela.setRowSelectionAllowed(true);
		novaTabela.setFont(new Font("Tahoma", Font.PLAIN, 14));
		novaTabela.setBorder(new LineBorder(new Color(0, 0, 0)));
		novaTabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		novaTabela.setShowGrid(true);
		novaTabela.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		return novaTabela;
	}

	private JTextArea criarConfigurarTextArea() {
		JTextArea novaArea = new JTextArea();
		novaArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
		novaArea.setEditable(false);
		return novaArea;
	}

	private void aplicarAlinhamentoCentralizado(JTable tabela) {
		javax.swing.table.DefaultTableCellRenderer centralizado = new javax.swing.table.DefaultTableCellRenderer();
		centralizado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		for (int i = 0; i < tabela.getColumnCount(); i++) {
			tabela.getColumnModel().getColumn(i).setCellRenderer(centralizado);
		}
	}

	private void configurarAcoes() {
		btnPesquisarData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				consultaViagensPorData();
			}
		});

		btnPesquisarPlaca.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				consultaViagensPorPlaca();
			}
		});

		btnPesquisarMotoristas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				consultaMotoristasFrequentes();
			}
		});
	}

	// ====================================================================
	// MÉTODOS DE CONSULTA
	// ====================================================================

	private void consultaViagensPorData() {
		labelMensagem.setText("");
		String textoData = textFieldDataBusca.getText().trim();

		if (textoData.isEmpty()) {
			labelMensagem.setText("Informe a data para a pesquisa.");
			return;
		}

		try {
			// Faz o parse da string no formato dd/MM/yyyy para LocalDate
			LocalDate dataRef = LocalDate.parse(textoData, formatador);

			DefaultTableModel model = new DefaultTableModel();
			model.addColumn("Id");
			model.addColumn("Destino");
			model.addColumn("Placa Veículo");
			tableData.setModel(model);

			textAreaLogData.setText("Executando consulta de viagens para a data: " + textoData + "...\n");

			// !!! IMPORTANTE: Ajuste o nome do método da sua Fachada caso mude !!!
			List<Viagem> resultado = FachadaViagem.listarViagensPorData(dataRef);

			if (resultado.isEmpty()) {
				textAreaLogData.append("Nenhuma viagem encontrada para esta data.\n");
			} else {
				for (Viagem v : resultado) {
					String placa = (v.getVeiculo() != null) ? v.getVeiculo().getPlaca() : "N/A";
					model.addRow(new Object[] { v.getId(), v.getDestino(), placa });
				}
				textAreaLogData.append("Consulta concluída. " + resultado.size() + " registro(s) retornado(s).\n");
			}
			aplicarAlinhamentoCentralizado(tableData);

		} catch (java.time.format.DateTimeParseException dtpe) {
			labelMensagem.setText("Formato de data inválido! Use o padrão dd/MM/yyyy.");
			textAreaLogData.setText("Erro de conversão de data.");
		} catch (Exception ex) {
			labelMensagem.setText(ex.getMessage());
			textAreaLogData.setText("Erro na requisição: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	private void consultaViagensPorPlaca() {
		labelMensagem.setText("");
		String placa = textFieldPlacaBusca.getText().trim();

		if (placa.isEmpty()) {
			labelMensagem.setText("Informe a placa do veículo para a pesquisa.");
			return;
		}
		try {
			DefaultTableModel model = new DefaultTableModel();
			model.addColumn("Id");
			model.addColumn("Destino");
			model.addColumn("Data");
			tablePlaca.setModel(model);
			textAreaLogPlaca.setText("Executando consulta de viagens para a placa: " + placa + "...\n");

			// !!! IMPORTANTE: Ajuste o nome do método da sua Fachada caso mude !!!
			List<Viagem> resultado = FachadaViagem.listarViagensPorPlaca(placa);

			if (resultado.isEmpty()) {
				textAreaLogPlaca.append("Nenhuma viagem cadastrada com este veículo.\n");
			} else {
				for (Viagem v : resultado) {
					model.addRow(new Object[] { v.getId(), v.getDestino(), v.getData().format(formatador) });
				}
				textAreaLogPlaca.append("Consulta concluída. " + resultado.size() + " registro(s) encontrado(s).\n");
			}
			aplicarAlinhamentoCentralizado(tablePlaca);

		} catch (Exception ex) {
			labelMensagem.setText(ex.getMessage());
			textAreaLogPlaca.setText("Erro na requisição: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	private void consultaMotoristasFrequentes() {
		labelMensagem.setText("");
		String txtN = textFieldQtdViagens.getText().trim();
		String destino = textFieldDestinoBusca.getText().trim();

		if (txtN.isEmpty() || destino.isEmpty()) {
			labelMensagem.setText("Informe a quantidade mínima (N) e o destino desejado.");
			return;
		}

		try {
			int n = Integer.parseInt(txtN);

			DefaultTableModel model = new DefaultTableModel();
			model.addColumn("CNH");
			model.addColumn("Nome Motorista");
			tableMotoristas.setModel(model);

			textAreaLogMotoristas.setText("Buscando motoristas com mais de " + n + " viagens enviadas para: " + destino + "...\n");

			// !!! IMPORTANTE: Ajuste o nome do método da sua Fachada caso mude !!!
			List<Motorista> resultado = FachadaViagem.listarMotoristasComMaisDeNViagens(n, destino);

			if (resultado.isEmpty()) {
				textAreaLogMotoristas.append("Nenhum motorista atende aos critérios informados.\n");
			} else {
				for (Motorista m : resultado) {
					model.addRow(new Object[] { m.getCnh(), m.getNome() });
				}
				textAreaLogMotoristas.append("Consulta concluída. " + resultado.size() + " motorista(s) listado(s).\n");
			}
			aplicarAlinhamentoCentralizado(tableMotoristas);

		} catch (NumberFormatException nfe) {
			labelMensagem.setText("O campo 'Mínimo Viagens' deve conter apenas valores numéricos inteiros.");
			textAreaLogMotoristas.setText("Erro de validação de número inteiro.");
		} catch (Exception ex) {
			labelMensagem.setText(ex.getMessage());
			textAreaLogMotoristas.setText("Erro na requisição: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
}