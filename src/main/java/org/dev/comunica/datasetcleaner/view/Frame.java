package org.dev.comunica.datasetcleaner.view;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.dev.comunica.datasetcleaner.model.CSV;
import org.dev.comunica.datasetcleaner.model.Mensagem;
import org.dev.comunica.datasetcleaner.model.Coluna;

public class Frame extends JFrame {
    JPanel panel = new JPanel(new GridLayout(7, 2, 16, 16));
    Mensagem[] mensagens;
    Coluna[] colunas;
    ArrayList<JComboBox<Coluna>> combos = new ArrayList<JComboBox<Coluna>>(5);
    File file;

    public Frame() {
        super("Datasets Cleaner");

        // Aplica o tema nativo do sistema operacional
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Configuração do painel com margem interna (padding)
        panel.setBorder(new EmptyBorder(25, 30, 25, 30));
        panel.setBackground(new Color(248, 249, 250));

        setBody();
        add(panel);

        setSize(480, 520);
        setLocationRelativeTo(null); // Centraliza a janela no monitor
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void setBody() {
        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
        Color textColor = new Color(55, 65, 81);

        var lbTitulo = new JLabel("Titulo:");
        lbTitulo.setFont(labelFont);
        lbTitulo.setForeground(textColor);
        var txtTitulo = new JComboBox<Coluna>();
        txtTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combos.add(txtTitulo);
        txtTitulo.setEnabled(false);

        var lbTexto = new JLabel("Texto:");
        lbTexto.setFont(labelFont);
        lbTexto.setForeground(textColor);
        var txtTexto = new JComboBox<Coluna>();
        txtTexto.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combos.add(txtTexto);
        txtTexto.setEnabled(false);

        var lbVerificado = new JLabel("Verificado:");
        lbVerificado.setFont(labelFont);
        lbVerificado.setForeground(textColor);
        var txtVerificado = new JComboBox<Coluna>();
        txtVerificado.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combos.add(txtVerificado);
        txtVerificado.setEnabled(false);

        var lbUrl = new JLabel("URL (verificação):");
        lbUrl.setFont(labelFont);
        lbUrl.setForeground(textColor);
        var txtUrl = new JComboBox<Coluna>();
        txtUrl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combos.add(txtUrl);
        txtUrl.setEnabled(false);

        var lbResumo = new JLabel("Resumo (verificação):");
        lbResumo.setFont(labelFont);
        lbResumo.setForeground(textColor);
        var txtResumo = new JComboBox<Coluna>();
        txtResumo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combos.add(txtResumo);
        txtResumo.setEnabled(false);

        var selectSheet = new JButton("Escolher Arquivo (.csv)");
        selectSheet.setFont(new Font("Segoe UI", Font.BOLD, 12));
        selectSheet.setFocusPainted(false);
        selectSheet.setCursor(new Cursor(Cursor.HAND_CURSOR));
        selectSheet.setBackground(new Color(241, 245, 249));
        selectSheet.setForeground(new Color(15, 23, 42));

        var enviar = new JButton("Enviar");
        enviar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        enviar.setFocusPainted(false);
        enviar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        enviar.setBackground(new Color(37, 99, 235));
        enviar.setForeground(Color.WHITE);

        selectSheet.addActionListener(e -> {
            var chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("./"));
            chooser.setFileFilter(new FileNameExtensionFilter("CSV file (.csv)", "csv"));

            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    file = chooser.getSelectedFile();
                    colunas = CSV.lerColunas(file);

                    for (JComboBox<Coluna> c : combos) {
                        c.setModel(new DefaultComboBoxModel<>(colunas));
                        c.setEnabled(true);
                    }
                } catch (Exception err) {
                    JOptionPane.showMessageDialog(this, "Arquivo inválido ou não encontrado.");
                    err.printStackTrace();
                }
            }
        });

        enviar.addActionListener(e -> {
            var indexado = new int[combos.size()];
            for (int i = 0; i < combos.size(); i++) {
                // 1. Pega o item que o usuário de fato selecionou
                Coluna colunaSelecionada = (Coluna) combos.get(i).getSelectedItem();
                
                // 2. Extrai o valor dele
                indexado[i] = colunaSelecionada.valor();
            }

            try {
                mensagens = CSV.lerCSV(file, indexado);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            CSV.gerarCSV(mensagens, file.getName().substring(0, file.getName().length() - 4));
            
            // Dica: Adicione um aviso visual para saber que terminou!
            JOptionPane.showMessageDialog(this, "Arquivo gerado com sucesso!");
        });

        panel.add(lbTitulo);
        panel.add(txtTitulo);

        panel.add(lbTexto);
        panel.add(txtTexto);

        panel.add(lbVerificado);
        panel.add(txtVerificado);

        panel.add(lbUrl);
        panel.add(txtUrl);

        panel.add(lbResumo);
        panel.add(txtResumo);

        panel.add(selectSheet);
        panel.add(enviar);
    }
}