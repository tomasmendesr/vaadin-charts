package ar.gov.sedronar.suit.ui.views.estadisticas;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

import ar.gov.sedronar.suit.services.WfsService;
@CDIView(BarView.VIEW_ID)
public class BarView  extends VerticalLayout implements View {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String VIEW_ID = "barChart";

	@Inject
	private WfsService service;
	
	@PostConstruct
	public void init(){
		ChartMultiLevelDrilldown chartBuilder = new ChartMultiLevelDrilldown(service.getAll());
		try {
			addComponent(chartBuilder.getChart(ChartType.BAR));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
}
