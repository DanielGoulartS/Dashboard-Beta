/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dashboard;

import static dashboard.Dashboard.lista;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JTable;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Daniel
 */
public class BarChart {
    private String titulo;
    public CategoryDataset novoBarDataset(int coluna, JTable tabela){
        lista = new ArrayList<>();
        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        
        
        for (int i = 0; i < tabela.getModel().getRowCount(); i++) {
            String[] dados = new String[tabela.getModel().getColumnCount()];
            
            for(int j = 0; j < tabela.getModel().getColumnCount(); j++){
                    dados[j] = tabela.getModel().getValueAt(i, j).toString();
            }
            
            lista.add(dados);
        }//JOGA OS DADOS DA TABELA NUM ARRAYLIST DE STRING[]
        
        this.titulo = lista.get(0)[coluna];// Define o titulo
        
        int i = 0; //PARA CADA ROW EM MINHA TABELA
        for (String[] dados : lista) {
            
            try {
                
                if (i != 0) { //EVITA TITULOS EM OUTROS GRAFICOS
                    ds.addValue(Float.valueOf(dados[coluna]), dados[0], "");
                }

            } catch (NumberFormatException ex) {
                
                
                if (i != 0) {

                    if (ds.getRowIndex(dados[coluna]) == -1) {
                        
                        if(!dados[coluna].equals("")){  //VALORES NULOS NAO ENTRAM NO GRAFICO
                            ds.addValue(1, dados[coluna], "");
                        }
                            
                    } else {
                        ds.incrementValue(1, dados[coluna], "");
                    }

                }
            }
   
            i++;
        }
        
        return ds;
    }
    
    public JFreeChart novoGraficoBarra(String nomeValor, JTable tabela, int grandeza){
        JFreeChart jfc = ChartFactory.createBarChart(this.titulo, "legenda", nomeValor, novoBarDataset(grandeza, tabela));
        jfc.setTitle(this.titulo);
        return jfc;
    }
    
    public JPanel novoPainelGraficoBarra(String nomeValor, JTable tabela, int grandeza) {

        ChartPanel painel = new ChartPanel(novoGraficoBarra( nomeValor, tabela, grandeza));
        painel.setMouseWheelEnabled(true);
        return painel;
    }

    
}
