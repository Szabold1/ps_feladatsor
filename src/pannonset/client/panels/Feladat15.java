package pannonset.client.panels;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.dnd.GridDragSource;
import com.extjs.gxt.ui.client.dnd.GridDropTarget;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Feladat15 extends TabItem {
	private static final int WIDTH = 600;
	private static final int HEIGHT = 300;
	private static final int COL_WIDTH = 250;
	
	private ColumnModel cm;
	private ListStore<ModelData> store;
	
	public Feladat15(String text) {
		this.setText(text);
	}

	@Override
	protected void onRender(Element parent, int index) {

		super.onRender(parent, index);
		
		ContentPanel cp = createContentPanel();
				
		List<ColumnConfig> columns = defineColumns();
		
		cm = new ColumnModel(columns);
				
		store = new ListStore<ModelData>();
		fillUpStore(store);
		
		// set up the grids
		Grid<ModelData> grid1 = setupGrid(false);
		RowData data = new RowData(.5, 1);
		data.setMargins(new Margins(6));
		cp.add(grid1, data);
		
		Grid<ModelData> grid2 = setupGrid(true);
		data = new RowData(.5, 1);
		data.setMargins(new Margins(6, 6, 6, 0));
		cp.add(grid2, data);
		
		// set up drag & drop
		new GridDragSource(grid1);
		new GridDragSource(grid2);

		GridDropTarget dropTarget1 = new GridDropTarget(grid1);
		dropTarget1.setAllowSelfAsSource(false);

		GridDropTarget dropTarget2 = new GridDropTarget(grid2);
		dropTarget2.setAllowSelfAsSource(false);
		
		// add content panel to the tab
		add(cp);
	}

	private ContentPanel createContentPanel() {
		ContentPanel cp = new ContentPanel();
		cp.setHeading("Drag & Drop Example with Grid");
		cp.setLayout(new RowLayout(Orientation.HORIZONTAL));
		cp.setSize(WIDTH, HEIGHT);
		cp.setBodyBorder(true);
		return cp;
	}

	private List<ColumnConfig> defineColumns() {
		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
		
		columns.add(new ColumnConfig("name", "Name", COL_WIDTH));
		columns.add(new ColumnConfig("favNum", "Favourite Number", COL_WIDTH));
		
		return columns;
	}

	private void fillUpStore(ListStore<ModelData> store) {
		for (int i = 0; i < 10; i++) {
			ModelData model = new BaseModelData();
			model.set("name", "Name" + i);
			model.set("favNum", (new Random()).nextInt(1000));
			
			store.add(model);
		}
	}
	
	private Grid<ModelData> setupGrid(boolean noData) {
		Grid<ModelData> grid;
		
		if (noData)
			grid = new Grid<ModelData>(new ListStore<ModelData>(), cm);
		else
			grid = new Grid<ModelData>(store, cm);
			
		grid.setSize(WIDTH, HEIGHT);
		grid.setStateId("pagingGridExample");
		grid.setStateful(true);
		grid.setLoadMask(true);
		grid.setBorders(true);
		
		return grid;
	}
}