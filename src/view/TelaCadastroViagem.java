package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import controller.ControllerViagem;
import controller.ControllerMotorista;
import controller.ControllerVeiculo;
import model.Motorista;
import model.Veiculo;

public class TelaCadastroViagem extends JDialog {

    private JTextField txtData;
    private JTextField txtDestino;
    private JComboBox<Motorista> comboMotorista;
    private JComboBox<Veiculo> comboVeiculo;
    private JButton btnSalvar;
    private JButton btnCancelar;

    public TelaCadastroViagem(Frame parent) {
        super(parent, "Cadastrar Nova Viagem", true); 
        setSize(380, 320);
        setLocationRelativeTo(parent);
        setResizable(false);

        // Painel central que vai conter o formulário empilhado verticalmente
        JPanel painelFormulario = new JPanel();
        painelFormulario.setBounds(0, 0, 364, 238);
        // Adiciona uma borda interna (margem) para os componentes não colarem nas bordas da tela
        painelFormulario.setBorder(new EmptyBorder(15, 15, 15, 15));
        painelFormulario.setLayout(null);

        // --- Campo: Data ---
        JLabel label = new JLabel("Data (DD/MM/AAAA):");
        label.setBounds(15, 11, 118, 16);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setVerticalAlignment(SwingConstants.TOP);
        painelFormulario.add(label);
        txtData = new JTextField();
        txtData.setBounds(15, 31, 334, 25);
        txtData.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtData.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        txtData.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25)); // Limita a altura do campo
        painelFormulario.add(txtData);

        // --- Campo: Destino ---
        JLabel label_1 = new JLabel("Destino:");
        label_1.setBounds(15, 67, 40, 16);
        painelFormulario.add(label_1);
        txtDestino = new JTextField();
        txtDestino.setBounds(15, 86, 334, 25);
        txtDestino.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        painelFormulario.add(txtDestino);

        // --- Campo: Motorista ---
        JLabel label_2 = new JLabel("Motorista:");
        label_2.setBounds(15, 122, 49, 14);
        painelFormulario.add(label_2);
        comboMotorista = new JComboBox<>();
        comboMotorista.setBounds(15, 136, 334, 25);
        comboMotorista.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        carregarMotoristas();
        painelFormulario.add(comboMotorista);

        // --- Campo: Veículo ---
        JLabel label_3 = new JLabel("Veículo (Placa):");
        label_3.setBounds(15, 174, 73, 14);
        painelFormulario.add(label_3);
        comboVeiculo = new JComboBox<>();
        comboVeiculo.setBounds(15, 187, 334, 25);
        comboVeiculo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        carregarVeiculos();
        getContentPane().setLayout(null);
        painelFormulario.add(comboVeiculo);

        // Adiciona o formulário no centro da tela
        getContentPane().add(painelFormulario);

        // --- Rodapé: Botões ---
        JPanel painelBotoes = new JPanel();
        painelBotoes.setBounds(0, 238, 364, 43);
        painelBotoes.setBorder(new EmptyBorder(0, 0, 10, 10)); // Margem nos botões
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnCancelar.setBounds(251, 4, 103, 23);
        painelBotoes.setLayout(null);
        btnSalvar = new JButton("Salvar");
        btnSalvar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnSalvar.setBounds(23, 4, 97, 23);
        painelBotoes.add(btnSalvar);
        btnSalvar.addActionListener(e -> salvarViagem());
        painelBotoes.add(btnCancelar);

        // Adiciona os botões na parte de baixo da tela
        getContentPane().add(painelBotoes);

        // --- Eventos dos Botões ---
        btnCancelar.addActionListener(e -> dispose());
    }

    private void carregarMotoristas() {
        try {
            List<Motorista> motoristas = ControllerMotorista.listarMotoristas(); 
            for (Motorista m : motoristas) {
                comboMotorista.addItem(m);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar motoristas: " + e.getMessage());
        }
    }

    private void carregarVeiculos() {
        try {
            List<Veiculo> veiculos = ControllerVeiculo.listarVeiculos(); 
            for (Veiculo v : veiculos) {
                comboVeiculo.addItem(v);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar veículos: " + e.getMessage());
        }
    }

    private void salvarViagem() {
        try {
            if (txtDestino.getText().trim().isEmpty() || txtData.getText().trim().isEmpty()) {
                throw new Exception("Todos os campos de texto devem ser preenchidos.");
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate data = LocalDate.parse(txtData.getText().trim(), formatter);
            String destino = txtDestino.getText().trim();

            Motorista motoristaSelecionado = (Motorista) comboMotorista.getSelectedItem();
            Veiculo veiculoSelecionado = (Veiculo) comboVeiculo.getSelectedItem();

            if (motoristaSelecionado == null || veiculoSelecionado == null) {
                throw new Exception("É necessário selecionar um motorista e um veículo válidos.");
            }

            ControllerViagem.criarViagem(data, destino, motoristaSelecionado, veiculoSelecionado);

            JOptionPane.showMessageDialog(this, "Viagem salva com sucesso!");
            dispose(); 

        } catch (java.time.format.DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido! Use DD/MM/AAAA.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage());
        }
    }
}