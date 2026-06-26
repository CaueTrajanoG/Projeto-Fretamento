package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import controller.ControllerMotorista;
import controller.ControllerVeiculo;
import model.Motorista;
import model.Veiculo;
import model.Viagem;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;

public class TelaVeiculos {
	private JDialog frame;
	private JScrollPane scrollPane;
	private JTable table;
	private JTextField textFieldPlaca;
	private JTextField textFieldCapacidade;

	public TelaVeiculos() {
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
		frame.setTitle("Registro de Veiculos");
		frame.setBounds(100, 100, 381, 461);
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
		scrollPane.setBounds(28, 34, 314, 180);
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
					// label.setText("");
					if (table.getSelectedRow() >= 0) {
						// copiar a pessoa selecionada para formulario de edicao
						String placa = (String) table.getValueAt(table.getSelectedRow(), 0);
						Veiculo v = ControllerVeiculo.buscarVeiculoPorPlaca(placa);
						textFieldPlaca.setText(v.getPlaca());
						textFieldCapacidade.setText(String.valueOf(v.getCapacidade()));	
					}
				} catch (Exception erro) {
					erro.printStackTrace();
				}
			}
		});
		
		textFieldPlaca = new JTextField();
		textFieldPlaca.setBounds(28, 245, 193, 31);
		frame.getContentPane().add(textFieldPlaca);
		textFieldPlaca.setColumns(10);
		
		textFieldCapacidade = new JTextField();
		textFieldCapacidade.setColumns(10);
		textFieldCapacidade.setBounds(28, 306, 193, 31);
		frame.getContentPane().add(textFieldCapacidade);
		
		JLabel lblPlaca = new JLabel("Placa");
		lblPlaca.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblPlaca.setBounds(28, 225, 82, 18);
		frame.getContentPane().add(lblPlaca);
		
		JLabel lblCapacidade = new JLabel("Capacidade");
		lblCapacidade.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblCapacidade.setBounds(28, 287, 82, 18);
		frame.getContentPane().add(lblCapacidade);
		
		JButton btnAtualizar = new JButton("Atualizar");
		btnAtualizar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnAtualizar.setBounds(27, 359, 104, 31);

		frame.getContentPane().add(btnAtualizar);
		
		JButton btnNovoVeiculo = new JButton("Novo Veiculo");
		btnNovoVeiculo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnNovoVeiculo.setBounds(231, 359, 111, 31);
		frame.getContentPane().add(btnNovoVeiculo);
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnRefresh.setBounds(246, 244, 96, 31);
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
			// objeto model contem todas as linhas e colunas da tabela
			DefaultTableModel model = new DefaultTableModel();
			table.setModel(model);

			// adicionar as colunas (0,1,2) do grid			
			model.addColumn("Placa");
			model.addColumn("Capacidade");
			
			// adicionar as linhas do grid
			List<Veiculo> lista = ControllerVeiculo.listarVeiculos();
			for (Veiculo v : lista) {
				model.addRow(new Object[] { v.getPlaca(), v.getCapacidade() });
			};
		} catch (Exception erro) {
			// label.setText(erro.getMessage());
		}
	}
	
	
}
