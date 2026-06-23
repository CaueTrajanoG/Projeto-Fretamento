package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import controller.ControllerMotorista;
import controller.ControllerViagem;
import model.Motorista;
import model.Viagem;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class TelaMotoristas {
	protected static final Frame Frame = null;
	private JDialog frame;
	private JScrollPane scrollPane;
	private JTable table;
	private JButton btn_refresh;
	private JTextField textField_nome;
	private JTextField textField_cnh;
	private JLabel lblNewLabel;
	private JLabel lblCnh;
	private JLabel lblNewLabel_1;
	private JTable table_historico;

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
		frame.setBounds(100, 100, 572, 453);
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

		// Selecionar linha da tabela
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					// label.setText("");
					if (table.getSelectedRow() >= 0) {
						// copiar a pessoa selecionada para formulario de edicao
						String cnh = (String) table.getValueAt(table.getSelectedRow(), 1);
						Motorista m = ControllerMotorista.localizarMotorista(cnh);
						textField_nome.setText(m.getNome());
						textField_cnh.setText(m.getCnh());

						DefaultTableModel modelHist = new DefaultTableModel();
						modelHist.addColumn("ID");
						modelHist.addColumn("Destino");
						table_historico.setModel(modelHist);

						if (m.getListaViagem() != null) {
							for (Viagem v : m.getListaViagem()) {
								modelHist.addRow(new Object[] { v.getId(), v.getDestino() });
							}
						}
					}
				} catch (Exception erro) {
					erro.printStackTrace();
				}
			}
		});

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
		btn_refresh.setBounds(325, 356, 103, 35);
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

		textField_cnh = new JTextField();
		textField_cnh.setColumns(10);
		textField_cnh.setBounds(28, 303, 275, 35);
		frame.getContentPane().add(textField_cnh);

		lblNewLabel = new JLabel("Nome");
		lblNewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblNewLabel.setBounds(28, 225, 118, 25);
		frame.getContentPane().add(lblNewLabel);

		lblCnh = new JLabel("CNH");
		lblCnh.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblCnh.setBounds(28, 285, 118, 25);
		frame.getContentPane().add(lblCnh);

		lblNewLabel_1 = new JLabel("Foto vai aqui");
		lblNewLabel_1.setBounds(341, 271, 72, 54);
		frame.getContentPane().add(lblNewLabel_1);

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
			List<Motorista> lista = ControllerMotorista.listarMotoristas();
			for (Motorista m : lista) {
				model.addRow(new Object[] { m.getNome(), m.getCnh() });
			}
			;

			// label_2.setText("resultados: " + lista.size() + " pessoas - selecione uma
			// linha para editar");
		} catch (Exception erro) {
			// label.setText(erro.getMessage());
		}
	}

	public void atualizarMotorista() {
	    try {
	        String nomeDigitado = textField_nome.getText();   // Campo de cima (Nome)
	        String cnhDigitada = textField_cnh.getText();  // Campo de baixo (CNH)

	        // 1. Primeiro validamos se a CNH realmente existe no banco
	        Motorista m = ControllerMotorista.localizarMotorista(cnhDigitada);
	        if (m == null) {
	            JOptionPane.showMessageDialog(frame, "Motorista não encontrado com a CNH: " + cnhDigitada);
	        } else { 
	            ControllerMotorista.alterarMotorista(cnhDigitada, nomeDigitado);	            
	            JOptionPane.showMessageDialog(frame, "Motorista atualizado com sucesso!");
	        }
	        listagem(); // Recarrega a tabela e limpa as seleções antigas
	        
	    } catch (Exception ex2) {
	        JOptionPane.showMessageDialog(frame, "Erro ao atualizar: " + ex2.getMessage());
	        ex2.printStackTrace();
	    }
	}
}
