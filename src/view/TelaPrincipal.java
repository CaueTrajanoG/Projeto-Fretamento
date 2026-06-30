package view;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.SwingConstants;
import java.awt.EventQueue;

public class TelaPrincipal {
	private JFrame frame;
	private JMenu mnVeiculos;
	private JMenu mnViagens;
	private JMenu mnMotoristas;
	private JLabel label;

	public TelaPrincipal() {
		initialize();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaPrincipal window = new TelaPrincipal();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Agencia de Viagens");
		frame.setBounds(100, 100, 480, 420);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(false);

		// plano de fundo
		label = new JLabel("");
		label.setFont(new Font("Tahoma", Font.PLAIN, 26));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(0, 0, 444, 249);
		label.setText("Inicializando...");
		label.setBounds(0, 0, frame.getWidth(), frame.getHeight());

		try {
			ImageIcon imagem = new ImageIcon(getClass().getResource("/images/travelbus.jpg"));
			imagem = new ImageIcon(
					imagem.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_DEFAULT));
			label.setIcon(imagem);
		} catch (Exception e) {
			System.out.println("Aviso: Imagem de fundo não encontrada em src/images/travelbus.jpg");
		}
		frame.getContentPane().add(label);

		// menu bar
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		mnViagens = new JMenu("Viagens");
		menuBar.add(mnViagens);
		mnViagens.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new TelaViagens();
			}
		});
		
		mnVeiculos = new JMenu("Veiculos");
		menuBar.add(mnVeiculos);
		mnVeiculos.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new TelaVeiculos();
			}
		});
		
		mnMotoristas = new JMenu("Motoristas");
		menuBar.add(mnMotoristas);
		mnMotoristas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new TelaMotoristas();
			}
		});
		
		mnVeiculos = new JMenu("Consultas");
		menuBar.add(mnVeiculos);
		mnVeiculos.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new TelaConsultas();
			}
		});
		frame.getContentPane().setLayout(null);
	}
}
