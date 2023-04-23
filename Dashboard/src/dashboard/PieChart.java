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
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

/**
 *
 * @author Daniel
 */
public class PieChart {

    private String titulo;
    
        
    public JPanel novoPainelGraficoPie(int coluna, JTable tabela) {

        ChartPanel painel = new ChartPanel(novoGraficoPie(coluna, tabela));
        painel.setMouseWheelEnabled(true);
        return painel;
    }

    public JFreeChart novoGraficoPie(int coluna, JTable tabela) {
        JFreeChart grafico = ChartFactory.createRingChart(titulo, novoPieDataset(coluna, tabela), true, true, true);
        grafico.setTitle(this.titulo);
        
        return grafico;
    }
    
    public PieDataset novoPieDataset(int coluna, JTable tabela){
        lista = new ArrayList<>();
        DefaultPieDataset ds = new DefaultPieDataset();
        
        
        for (int i = 0; i < tabela.getModel().getRowCount(); i++) {
            String[] dados = new String[tabela.getModel().getColumnCount()];
            
            for(int j = 0; j < tabela.getModel().getColumnCount(); j++){
                    dados[j] = tabela.getModel().getValueAt(i, j).toString();
            }
            
            lista.add(dados);
        }//JOGA OS DADOS DA TABELA NUM ARRAYLIST DE STRING[]
        this.titulo = lista.get(0)[coluna];
        int i = 0; //PARA CADA ROW EM MINHA TABELA
        for (String[] dados : lista) {
            
            try {
                
                if (i != 0) { //EVITA TITULOS EM OUTROS GRAFICOS
                    ds.insertValue(0,  dados[0], Float.valueOf(dados[coluna]));
                }

            } catch (NumberFormatException ex) {
                
                
                if (i != 0) {

                    if (ds.getIndex(dados[coluna]) == -1) {
                        
                        if(!dados[coluna].equals("")){ //VALORES NULOS NAO ENTRAM NO GRAFICO
                            ds.insertValue(0,  dados[coluna], 1);
                        }
                            
                    } else {
                        ds.setValue(dados[coluna], (Float.valueOf(ds.getValue(dados[coluna]).toString()) + 1 ));
                    }

                }
            }
   
            i++;
        }
        
        return ds;
    }
    
}
