package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import model.Motorista;
import model.Veiculo;
import model.Viagem;
import requisito.FachadaMotorista;
import requisito.FachadaVeiculo;
import requisito.FachadaViagem;

public class TelaAtualizarViagem extends JDialog {

    private JTextField txtData;
    private JTextField txtDestino;
    private JComboBox<Motorista> comboMotorista;
    private JComboBox<Veiculo> comboVeiculo;
    private JButton btnSalvar;
    private JButton btnCancelar;
    private Viagem viagemAtual; // Guarda a referência da viagem que será atualizada

    public TelaAtualizarViagem(Frame parent, Viagem viagem) {
        super(parent, "Atualizar Dados da Viagem", true); 
        this.viagemAtual = viagem;
        
        setSize(380, 320);
        setLocationRelativeTo(parent);
        setResizable(false);

        // Painel central
        JPanel painelFormulario = new JPanel();
        painelFormulario.setBounds(0, 0, 364, 238);
        painelFormulario.setBorder(new EmptyBorder(15, 15, 15, 15));
        painelFormulario.setLayout(null);

        // --- Campo: Data ---
        JLabel label = new JLabel("Data (DD/MM/AAAA):");
        label.setBounds(15, 11, 118, 16);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        painelFormulario.add(label);
        
        txtData = new JTextField();
        txtData.setBounds(15, 31, 334, 25);
        txtData.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        // Recupera a data atual do objeto e joga formatada no campo
        if (viagemAtual.getData() != null) {
            txtData.setText(viagemAtual.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
        painelFormulario.add(txtData);

        // --- Campo: Destino ---
        JLabel label_1 = new JLabel("Destino:");
        label_1.setBounds(15, 67, 40, 16);
        painelFormulario.add(label_1);
        
        txtDestino = new JTextField();
        txtDestino.setBounds(15, 86, 334, 25);
        txtDestino.setText(viagemAtual.getDestino()); // Seta o destino atual
        painelFormulario.add(txtDestino);

        // --- Campo: Motorista ---
        JLabel label_2 = new JLabel("Motorista:");
        label_2.setBounds(15, 122, 49, 14);
        painelFormulario.add(label_2);
        
        comboMotorista = new JComboBox<>();
        comboMotorista.setBounds(15, 136, 334, 25);
        carregarESelecionarMotoristas();
        painelFormulario.add(comboMotorista);

        // --- Campo: Veículo ---
        JLabel label_3 = new JLabel("Veículo (Placa):");
        label_3.setBounds(15, 174, 73, 14);
        painelFormulario.add(label_3);
        
        comboVeiculo = new JComboBox<>();
        comboVeiculo.setBounds(15, 187, 334, 25);
        carregarESelecionarVeiculos();
        getContentPane().setLayout(null);
        painelFormulario.add(comboVeiculo);

        getContentPane().add(painelFormulario);

        // --- Rodapé: Botões ---
        JPanel painelBotoes = new JPanel();
        painelBotoes.setBounds(0, 238, 364, 43);
        painelBotoes.setBorder(new EmptyBorder(0, 0, 10, 10)); 
        painelBotoes.setLayout(null);
        
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnCancelar.setBounds(251, 4, 103, 23);
        btnCancelar.addActionListener(e -> dispose());
        
        btnSalvar = new JButton("Atualizar");
        btnSalvar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnSalvar.setBounds(23, 4, 97, 23);
        btnSalvar.addActionListener(e -> atualizarViagem());
        
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);

        getContentPane().add(painelBotoes);
    }

    private void carregarESelecionarMotoristas() {
        try {
            List<Motorista> motoristas = FachadaMotorista.listarMotoristas(); 
            for (Motorista m : motoristas) {
                comboMotorista.addItem(m);
                // Se o ID desse motorista da lista for igual ao ID do motorista da viagem, seleciona ele
                if (viagemAtual.getMotorista() != null && m.getCnh() == viagemAtual.getMotorista().getCnh()) {
                    comboMotorista.setSelectedItem(m);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar motoristas: " + e.getMessage());
        }
    }

    private void carregarESelecionarVeiculos() {
        try {
            List<Veiculo> veiculos = FachadaVeiculo.listarVeiculos(); 
            for (Veiculo v : veiculos) {
                comboVeiculo.addItem(v);
                // Se a placa desse veículo for igual à placa do veículo da viagem, seleciona ele
                if (viagemAtual.getVeiculo() != null && v.getPlaca().equals(viagemAtual.getVeiculo().getPlaca())) {
                    comboVeiculo.setSelectedItem(v);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar veículos: " + e.getMessage());
        }
    }

    private void atualizarViagem() {
        try {
            if (txtDestino.getText().trim().isEmpty() || txtData.getText().trim().isEmpty()) {
                throw new Exception("Todos os campos de texto devem ser preenchidos.");
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String data = txtData.getText().trim();
            String destino = txtDestino.getText().trim();

            Motorista motoristaSelecionado = (Motorista) comboMotorista.getSelectedItem();
            Veiculo veiculoSelecionado = (Veiculo) comboVeiculo.getSelectedItem();

            if (motoristaSelecionado == null || veiculoSelecionado == null) {
                throw new Exception("É necessário selecionar um motorista e um veículo válidos.");
            }

            // Dependendo dos parâmetros da sua Fachada, passe as propriedades do motorista e veículo.
            // Se o seu método aceitar os objetos completos, use:
            // FachadaViagem.alterarViagem(viagemAtual.getId(), data, destino, motoristaSelecionado, veiculoSelecionado);
            
            // Caso sua fachada use Strings (como CNH e Placa) igual ao método anterior:
            String cnh = motoristaSelecionado.getCnh(); 
            String placa = veiculoSelecionado.getPlaca();
            
            FachadaViagem.alterarViagem(viagemAtual.getId(), destino, cnh, placa, data);

            JOptionPane.showMessageDialog(this, "Viagem atualizada com sucesso!");
            dispose(); 

        } catch (java.time.format.DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido! Use DD/MM/AAAA.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + ex.getMessage());
        }
    }
}