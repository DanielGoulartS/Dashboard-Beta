package dashboard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Daniel
 */
public class Dashboard {

    public static ArrayList<String[]> lista;
    public static PieChart pieChart;
    public static BarChart barChart;
    public static JFrame frame;
    public static JScrollPane scrollPane, scrollPane2, scrollPaneTA;
    public static JSplitPane jsp, jspEsq1, jspEsq2;
    public static JPanel painel, painel2;
    public static JButton btAddRow, btAddCol, btRemRow, btRemCol;
    public static JTextArea notesArea;

    public static void main(String[] args) {
        pieChart = new PieChart();
        barChart = new BarChart();

        //INICIAR JFRAME
        frame = new JFrame("DashBoard Beta");
        frame.setSize(1280, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getInsets().set(20, 20, 20, 20);
        frame.setLayout(new GridLayout(0, 1));

        //INICIAR PAINEL DIREITO
        painel = new JPanel(new GridLayout(0, 1, 20, 20));
        painel.setBackground(Color.BLACK);
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        painel.setPreferredSize(new Dimension(320, 3500));

        scrollPane2 = new JScrollPane(painel);
        scrollPane2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane2.getVerticalScrollBar().setUnitIncrement(30);

        //INICIAR PAINEL ESQUERDO        
        jspEsq1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        jspEsq1.setDividerLocation(400);
        
        jspEsq2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        jspEsq2.setDividerLocation(100);
        
        scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(30);

        notesArea = new JTextArea("======= Escreva suas notas aqui =======");
        scrollPaneTA = new JScrollPane(notesArea);
        
        btAddCol = new JButton("Nova Coluna");
        btAddRow = new JButton("Nova Linha");
        btRemCol = new JButton("Remover Última Coluna");
        btRemRow = new JButton("Remover Última Linha");
        
        painel2 = new JPanel(new GridLayout(2,2,20,20));
        painel2.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        painel2.add(btAddRow);
        painel2.add(btAddCol);
        painel2.add(btRemRow);
        painel2.add(btRemCol);
        
        jspEsq2.setLeftComponent(scrollPaneTA);
        jspEsq2.setRightComponent(painel2);
        jspEsq1.setRightComponent(jspEsq2);

        //ALIMENTAR PAINEL ESQUERDO
        String[] grandezas = {"Nome", "Atendimentos", "Resolvidos", "Nota Média p/ Atendim.", "Queixa mais Comum"};
        DefaultTableModel dtm = new DefaultTableModel(grandezas, 0);
        JTable tabela = new JTable(dtm);

        Object[][] dados = {
            grandezas,
            {"Ana", 29, 26, 8, "Financeiro"},
            {"Bia", 20, 19, 8, "Suporte"},
            {"Cleide", 29, 26, 7.5, "Suporte"},
            {"Dinah", 35, 30, 9, "Suporte"},
            {"Elisa", 32, 30, 6, "Produto"}};

        for (Object[] dado : dados) {
            dtm.addRow(dado);
        }

        tabela.addKeyListener(updateCharts(scrollPane2, painel, tabela));
        scrollPane.setViewportView(tabela);
        jspEsq1.setLeftComponent(scrollPane);

        //ALIMENTAR PAINEL DIREITO
        alimentarPainelDeGraficos(painel, tabela);

        //CRIA O SPLITPANE
        jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jspEsq1, scrollPane2);
        jsp.setDividerLocation(500);

        frame.add(jsp);
        frame.setVisible(true);

    }

    public static void alimentarPainelDeGraficos(JPanel painel, JTable tabela){
        
        //Dados estáticos para apresentação
        JPanel painelInicial = new JPanel(new GridLayout(0,2,20,20));
        painelInicial.setBackground(Color.black);
        JPanel card1 = new JPanel(new GridLayout(0,1));
        JPanel card2 = new JPanel(new GridLayout(0,1));
        card1.setBorder(BorderFactory.createEmptyBorder(70, 60, 80, 60));
        card2.setBorder(BorderFactory.createEmptyBorder(70, 60, 80, 60));
        
        JLabel card1Title = new JLabel("Vendas ao inicio do estudo:", SwingConstants.CENTER);
        JLabel card1Data = new JLabel("23/Semana", SwingConstants.CENTER);
        JLabel card2Title = new JLabel("Vendas ao fim do estudo:", SwingConstants.CENTER);        
        JLabel card2Data = new JLabel("15/Semana", SwingConstants.CENTER);
        
        card1Data.setFont(new Font("Arial", Font.BOLD, 40));
        card2Data.setFont(new Font("Arial", Font.BOLD, 40));
        
        card1.add(card1Title);
        card1.add(card1Data);
        card2.add(card2Title);
        card2.add(card2Data);
        
        painelInicial.add(card1);
        painelInicial.add(card2);
        
        painel.add(painelInicial);
        //Fim dos dados estáticos
        
        for(int i = 0; i < tabela.getModel().getColumnCount(); i++){
            painel.add(barChart.novoPainelGraficoBarra("", tabela, i));
            painel.add(pieChart.novoPainelGraficoPie(i, tabela));
        }
        
    }
    
    public static KeyListener updateCharts(JScrollPane scrollpane, JPanel painel, JTable tabela){
        return new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                }

            @Override
            public void keyPressed(KeyEvent e) {
                }

            @Override
            public void keyReleased(KeyEvent e) {
                painel.removeAll();
                alimentarPainelDeGraficos(painel, tabela);
                scrollpane.setViewportView(painel);
            }
        };
    }

}