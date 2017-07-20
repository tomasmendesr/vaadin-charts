package ar.gov.sedronar.suit.ui.views.estadisticas;



import java.util.ArrayList;
import java.util.List;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import ar.gov.sedronar.suit.componentsTest.TreeCapas;
import ar.gov.sedronar.suit.componentsTest.icon.ComboBoxIcons;
import ar.gov.sedronar.suit.componentsTest.icon.IconSuit;
import ar.gov.sedronar.suit.componentsTest.icon.IconSuit.Family;
import ar.gov.sedronar.suit.dto.CapaItemDTO;

@CDIView(EstadisticasPointView.VIEW_ID)
public class EstadisticasPointView extends VerticalLayout implements View {

	public static final String VIEW_ID = "estadisticas";
	private static final long serialVersionUID = 1L;
	
	public EstadisticasPointView(){
		this.setSpacing(true);
		this.setMargin(true);
		setSizeFull();
		Button btnColumn = new Button("Ver grafico de columna");
		Button btnBar = new Button("Ver grafico de barra");
		Button btnPie = new Button("Ver grafico de torta");
		
		btnColumn.addClickListener(event -> UI.getCurrent().getNavigator().navigateTo(ColumnView.VIEW_ID));
		btnPie.addClickListener(event -> UI.getCurrent().getNavigator().navigateTo(PieView.VIEW_ID));
		btnBar.addClickListener(event -> UI.getCurrent().getNavigator().navigateTo(BarView.VIEW_ID));

		ComboBoxIcons cbIcons = new ComboBoxIcons();
		
		IconSuit icon1 = new IconSuit();
        icon1.setDescription("Point");
        icon1.setIconName("MAP_MARKER");
        icon1.setFamily(Family.FontAwesome);
        
        IconSuit icon2 = new IconSuit();
        icon2.setDescription("Arrow");
        icon2.setIconName("LOCATION_ARROW");
        icon2.setFamily(Family.FontAwesome);
        
		List<CapaItemDTO> capas = new ArrayList<>();
		CapaItemDTO capa1 = new CapaItemDTO();
		capa1.setNombreId("asd123");
		capa1.setOrganismo("Organismo 1");
		capa1.setIconPoint(icon1);
		capa1.setTitulo("primer capa");
		capas.add(capa1);
		
		CapaItemDTO capa2 = new CapaItemDTO();
		capa2.setNombreId("asd321");
		capa2.setOrganismo("Organismo 1");
		capa2.setIconPoint(icon2);
		capa2.setTitulo("segunda capa");
		capas.add(capa2);
		
		CapaItemDTO capa3 = new CapaItemDTO();
		capa3.setNombreId("dsa123");
		capa3.setOrganismo("Organismo 2");
		capa3.setIconPoint(icon1);
		capa3.setTitulo("tercer capa");
		capas.add(capa3);
		
		CapaItemDTO capa4 = new CapaItemDTO();
		capa4.setNombreId("asdd123");
		capa4.setOrganismo("Organismo 3");
		capa4.setIconPoint(icon2);
		capa4.setTitulo("cuarta capa");
		capas.add(capa4);
		
		//this.addComponents(new TreeCapas(capas), btnColumn, btnBar, btnPie, cbIcons.getComboBoxIcons());
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
			
	}

}
