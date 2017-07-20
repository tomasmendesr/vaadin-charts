package ar.gov.sedronar.suit.ui.views.estadisticas;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.AbstractPlotOptions;
import com.vaadin.addon.charts.model.AxisType;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.Cursor;
import com.vaadin.addon.charts.model.DataLabels;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.PlotOptionsBar;
import com.vaadin.addon.charts.model.PlotOptionsColumn;
import com.vaadin.addon.charts.model.PlotOptionsPie;
import com.vaadin.addon.charts.model.XAxis;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.ui.Component;

import ar.gov.sedronar.suit.models.wfs.SuitPoint;

public class ChartMultiLevelDrilldown{
	
	private List<SuitPoint> points;
	
	public ChartMultiLevelDrilldown(List<SuitPoint> points){
		this.points = points;
	}

    /**
     * Crea un nuevo grafico con navegacion.
     * Los tipos de grafico deben ser PIE, BAR o COLUMN.
     * 
     * @param type
     *            tipo de grafico.
     * @throws Exception 
     */
    public Component getChart(ChartType type) throws Exception {
    	final Chart chart = new Chart(type);
        chart.setId("chart");

        final Configuration conf = chart.getConfiguration();

        conf.setTitle("Estadisticas Points");
        conf.setSubTitle("Cantidad de points");
        conf.getLegend().setEnabled(false);

        XAxis x = new XAxis();
        x.setType(AxisType.CATEGORY);
        conf.addxAxis(x);

        YAxis y = new YAxis();
        y.setTitle("Cantidad de points cargados");
        y.setAllowDecimals(false);
        conf.addyAxis(y);

        conf.setPlotOptions(getConfigPlotOptions(type));
        
        DataSeries paisSeries = cargarSeriesPaises(type);
        
        conf.addSeries(paisSeries);
        return chart;
    }
    
    private DataSeries cargarSeriesPaises(ChartType type){
        DataSeries paisesSeries = new DataSeries("Points (Pais)");
        paisesSeries.setPlotOptions(getSeriesPlotOptions(type));
        
        List<Integer> idsPaises = points.stream()
        		.map(p -> p.getPais_id())
        		.distinct()
        		.collect(Collectors.toList());
        
        for(Integer idPais : idsPaises){
        	// Sumo cantidad de pois para el pais correspondiente.
        	Integer pointsFromidPais = points.stream().filter(p -> p.getPais_id().equals(idPais)).collect(Collectors.toList()).size();
        	
        	DataSeriesItem paisItem = new DataSeriesItem("Pais " + idPais.toString(), pointsFromidPais);
        	
        	DataSeries provinciaSeries = cargarSeriesProvincias(idPais, type);
        	
            paisesSeries.addItemWithDrilldown(paisItem, provinciaSeries);
        }
        
        return paisesSeries;
    }
    
    private DataSeries cargarSeriesProvincias(Integer idPais, ChartType type){
    	DataSeries provinciaSeries = new DataSeries("Points (Prov.)");
        provinciaSeries.setId("Pronvicias de " + idPais.toString());
        provinciaSeries.setPlotOptions(getSeriesPlotOptions(type));
        
        // Cargo cantidad de pois por id de cada pronvincia
        List<Integer> idsProvincias = points.stream()
        		.filter(p -> p.getPais_id().equals(idPais))
        		.map(p -> p.getProvincia_id())
        		.distinct()
        		.collect(Collectors.toList());
        
        for(Integer idProv : idsProvincias){
        	Integer pointsFromidProv = points.stream().filter(p -> p.getProvincia_id().equals(idProv)
        			&& p.getPais_id().equals(idPais)).collect(Collectors.toList()).size();
        	
        	DataSeriesItem provinciaItem = new DataSeriesItem("Pronvicia " + idProv.toString(), pointsFromidProv);
        	
        	DataSeries departamentoSeries = cargarSeriesDepartamentos(idProv, type);
            provinciaSeries.addItemWithDrilldown(provinciaItem, departamentoSeries);	
        }
        
        return provinciaSeries;
    }
    
    private DataSeries cargarSeriesDepartamentos(Integer idProv, ChartType type){
    	DataSeries dptoSeries = new DataSeries("Points (Dpto)");
        dptoSeries.setId("Departamentos de " + idProv.toString());
        dptoSeries.setPlotOptions(getSeriesPlotOptions(type));
        
        // Cargo cantidad de pois por id de cada pronvincia
        List<Integer> idsDeptos = points.stream()
        		.filter(p -> p.getProvincia_id().equals(idProv))
        		.map(p -> p.getDepartamento_id())
        		.distinct()
        		.collect(Collectors.toList());
        for(Integer idDpto : idsDeptos){
        	Integer pointsFromDpto = points.stream().filter(p -> p.getDepartamento_id().equals(idDpto)
        			&& p.getProvincia_id().equals(idProv)).collect(Collectors.toList()).size();
        	DataSeriesItem dptoItem = new DataSeriesItem("Departamento " + idDpto.toString(), pointsFromDpto);
        	
        	DataSeries localidadSeries = cargarSeriesLocalidades(idDpto, type);
            dptoSeries.addItemWithDrilldown(dptoItem, localidadSeries);	
        }
        
        return dptoSeries;
    }
    
    private DataSeries cargarSeriesLocalidades(Integer idDpto, ChartType type){
    	DataSeries localidadesSeries = new DataSeries("Points (Loc.)");
        localidadesSeries.setId("Localidades de " + idDpto.toString());
        localidadesSeries.setPlotOptions(getSeriesPlotOptions(type));
        
        List<Integer> idsLocalicades = points.stream()
        		.filter(p -> p.getDepartamento_id().equals(idDpto))
        		.map(p -> p.getLocalidad_id())
        		.distinct()
        		.collect(Collectors.toList());
        
        List<Integer> pointsFromLoc = new ArrayList<>();
        
        for(Integer idLoc : idsLocalicades){
        	Integer pointsSumFromLoc = points.stream().filter(p -> p.getLocalidad_id().equals(idLoc)
        			&& p.getDepartamento_id().equals(idDpto)).collect(Collectors.toList()).size();
        	pointsFromLoc.add(pointsSumFromLoc);
        }
        
        String[] stringLocalidades = new String[idsLocalicades.size()];
        stringLocalidades = idsLocalicades.stream()
        		.map(i -> "Localidad " + i.toString())
        		.collect(Collectors.toList())
        		.toArray(stringLocalidades);
        
        Number[] ys = new Number[pointsFromLoc.size()];
        ys = pointsFromLoc.toArray(ys);
       
        localidadesSeries.setData(stringLocalidades, ys);
        
        return localidadesSeries;
    }
    
    private AbstractPlotOptions getSeriesPlotOptions(ChartType type){
    	if(type.equals( ChartType.COLUMN)){
	    	PlotOptionsColumn plotOptionsColumn = new PlotOptionsColumn();
	        plotOptionsColumn.setColorByPoint(true);
	        return plotOptionsColumn;
    	}
    	else if(type.equals( ChartType.PIE)){
    		PlotOptionsPie plot = new PlotOptionsPie();
    		plot.setDataLabels(new DataLabels());
    		return plot;
    	}
    	else if(type.equals( ChartType.BAR)){
	    	PlotOptionsBar plot = new PlotOptionsBar();
	        plot.setColorByPoint(true);
	        return plot;
    	}
    	else
    		return null;
    }
    
    private AbstractPlotOptions getConfigPlotOptions(ChartType type) throws Exception{
    	 if(type.equals(ChartType.COLUMN)){
 	        PlotOptionsColumn column = new PlotOptionsColumn();
 	        column.setCursor(Cursor.POINTER);
 	        column.setDataLabels(new DataLabels(true));
 	        return column;
         }
         else if(type.equals(ChartType.PIE)){
         	PlotOptionsPie pie = new PlotOptionsPie();
             pie.setCursor(Cursor.POINTER);
             pie.setDataLabels(new DataLabels(true));
             return pie;
         }
         else if(type.equals(ChartType.BAR)){
         	PlotOptionsBar bar = new PlotOptionsBar();
             bar.setCursor(Cursor.POINTER);
             bar.setDataLabels(new DataLabels(true));
             return bar;
         }
         else 
        	 throw new Exception("No se admite el chartType ingresado");
    }
    
    
}
