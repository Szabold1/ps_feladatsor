package pannonset.client.panels;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Element;
import com.google.gwt.i18n.client.DateTimeFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Feladat7 extends TabItem {
	private static final int WIDTH = 800;
	private static final int HEIGHT = 400;
	
	public Feladat7(String text) {
		this.setText(text);
	}

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		
		ContentPanel cp = new ContentPanel();
		cp.setHeading("Grid Example");
		cp.setLayout(new FitLayout());
		cp.setSize(WIDTH, HEIGHT);
		cp.setBodyBorder(true);
		
		// define columns
		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
		
		ColumnConfig column = new ColumnConfig("name", "Name", WIDTH / 5);
		columns.add(column);
		
		column = new ColumnConfig("nickname", "Nickname", WIDTH / 5);
		columns.add(column);
		
		column = new ColumnConfig("birthdate", "Birthdate", WIDTH / 5);
		column.setDateTimeFormat(DateTimeFormat.getFormat("yyyy/MM/dd"));
		columns.add(column);
		
		column = new ColumnConfig("age", "Age", WIDTH / 5);
		columns.add(column);
		
		column = new ColumnConfig("favNum", "Favourite Number", WIDTH / 5);
		columns.add(column);
		
		// set up the grid
		ColumnModel cm = new ColumnModel(columns);
		final ListStore<BaseModelData> store = new ListStore<BaseModelData>();
		final Grid<BaseModelData> grid = new Grid<BaseModelData>(store, cm);
		grid.setSize(WIDTH, HEIGHT);
		
		// toolbar with buttons
		ToolBar toolBar = new ToolBar();
		
		Button btnNew = new Button("Új");
		btnNew.addListener(Events.OnClick, new Listener<ButtonEvent>() {
			@Override
			public void handleEvent(ButtonEvent event) {
				BaseModelData newData = new BaseModelData();
				store.add(newData);
			}
		});
		toolBar.add(btnNew);
		
		Button btnDelete = new Button("Törlés");
		btnDelete.addListener(Events.OnClick, new Listener<ButtonEvent>() {
			@Override
			public void handleEvent(ButtonEvent event) {
				BaseModelData selected = grid.getSelectionModel().getSelectedItem();
				if (selected != null) {
					store.remove(selected);
				}
			}
		});
		toolBar.add(btnDelete);
		
		// fill up the rows with data
		for (int i = 0; i < 20; i++) {
			BaseModelData newData = new BaseModelData();
			newData.set("name", "New User" + i);
			newData.set("nickname", "Nick" + i);
			
			long yearsInMillis = (long) (i + 10) * 365 * 24 * 60 * 60 * 1000L;
			Date birthdate = new Date(System.currentTimeMillis() - yearsInMillis);
			
			newData.set("birthdate", birthdate);
			newData.set("age", i + 10);
			newData.set("favNum", (new Random()).nextInt(100));
			
			store.add(newData);
		}
		
		cp.setTopComponent(toolBar);
		cp.add(grid);
		add(cp);
	}
}
