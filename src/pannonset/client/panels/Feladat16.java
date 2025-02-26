package pannonset.client.panels;

import com.extjs.gxt.ui.client.data.JsonPagingLoadResultReader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.Loader;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.data.BaseFilterPagingLoadConfig;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.data.ModelType;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.HttpProxy;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.dnd.GridDragSource;
import com.extjs.gxt.ui.client.dnd.GridDropTarget;
import com.extjs.gxt.ui.client.dnd.DND;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.event.DNDListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Feladat16 extends TabItem {
	private static final int WIDTH = 600;
	private static final int HEIGHT = 300;
	private static final int COL_WIDTH = 130;
	private static final String DATA_URL_NAME = GWT.getHostPageBaseURL() + "adatbazis16.php";
	
	private ContentPanel cp;
	private ColumnModel cm;
	private PagingLoader<PagingLoadResult<BaseModelData>> loader;
	private Grid<ModelData> grid1;
	private Grid<ModelData> grid2;
	
	public Feladat16(String text) {
		this.setText(text);
	}

	@Override
	protected void onRender(Element parent, int index) {

		super.onRender(parent, index);
		
		ContentPanel cp = createContentPanel();
		
		List<ColumnConfig> columns = defineColumns();
		
		cm = new ColumnModel(columns);
		
		// set up 2 grids
		PagingLoader<PagingLoadResult<BaseModelData>> loader1 = setupLoader("grid1");
		ListStore<ModelData> store1 = new ListStore<ModelData>(loader1);
		grid1 = setupGrid(store1);
		RowData data = new RowData(.5, 1);
		data.setMargins(new Margins(6));
		cp.add(grid1, data);
		
		PagingLoader<PagingLoadResult<BaseModelData>> loader2 = setupLoader("grid2");
		ListStore<ModelData> store2 = new ListStore<ModelData>(loader2);
		grid2 = setupGrid(store2);
		data = new RowData(.5, 1);
		data.setMargins(new Margins(6, 6, 6, 0));
		cp.add(grid2, data);
		
		// set up drag & drop for both grids
		new GridDragSource(grid1);
		new GridDragSource(grid2);

		GridDropTarget dropTarget1 = new GridDropTarget(grid1);
		dropTarget1.setAllowSelfAsSource(false);
		dropTarget1.setOperation(DND.Operation.MOVE);
		dropTarget1.addDNDListener(new DNDListener() {
			@Override
			public void dragDrop(DNDEvent event) {
				super.dragDrop(event);
				handleDragAndDrop(event, "grid1");
			}
		});

		GridDropTarget dropTarget2 = new GridDropTarget(grid2);
		dropTarget2.setAllowSelfAsSource(false);
		dropTarget2.setOperation(DND.Operation.MOVE);
		dropTarget2.addDNDListener(new DNDListener() {
			@Override
			public void dragDrop(DNDEvent event) {
				super.dragDrop(event);
				handleDragAndDrop(event, "grid2");
			}
		});

		// load data
		loader1.load();
		loader2.load();
		
		// add content panel to the tab
		add(cp);
	}

	private ContentPanel createContentPanel() {
		cp = new ContentPanel();
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
	
	private PagingLoader<PagingLoadResult<BaseModelData>> setupLoader(final String location) {
		// create the model type
		ModelType type = new ModelType();
		type.setRoot("data");
		type.addField("name", "name");
		type.addField("favNum", "favourite_number");
		
		// set up the loader
		final RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, DATA_URL_NAME);
		HttpProxy<String> proxy = new HttpProxy<String>(requestBuilder);
		JsonPagingLoadResultReader<ListLoadResult<BaseModelData>> reader = new JsonPagingLoadResultReader<ListLoadResult<BaseModelData>>(type);
		loader = new BasePagingLoader<PagingLoadResult<BaseModelData>>(proxy, reader) {
			protected Object newLoadConfig() {
				BasePagingLoadConfig c = new BaseFilterPagingLoadConfig();
				return c;
			}
		};
		
		// add listener to send location (grid1, grid2) to backend before loading data
		loader.addListener(Loader.BeforeLoad, new Listener<LoadEvent>() {
			@Override
			public void handleEvent(LoadEvent be) {
				PagingLoadConfig config = be.getConfig();
				config.set("location", location);
			}
		});

		return loader;
	}
	
	private Grid<ModelData> setupGrid(ListStore<ModelData> store) {
		Grid<ModelData> grid = new Grid<ModelData>(store, cm);
		grid.setSize(WIDTH, HEIGHT);
		grid.setStateId("dragDropExample");
		grid.setStateful(true);
		grid.setLoadMask(true);
		grid.setBorders(true);
		
		return grid;
	}
	
	private void handleDragAndDrop(DNDEvent event, String newLocation) {
		Object data = event.getData();
		
		if (data instanceof List<?>) {
			List<?> rawList = (List<?>) data;

			for (Object item : rawList) {
				Map<String, Object> dataMap = new HashMap<String, Object>();
				
				if (item instanceof BaseModelData) {
					BaseModelData d = (BaseModelData) item;
					dataMap.put("name", d.getProperties().get("name"));
					dataMap.put("newLocation", newLocation);
					
					List<Grid<ModelData>> gridsInUse = new ArrayList<Grid<ModelData>>();
					gridsInUse.add(grid1);
					gridsInUse.add(grid2);
					sendDataToBackend(dataMap, gridsInUse);
				}
			}
		} else {
			System.err.println("Invalid data format for drag and drop.");
		}
	}
	
	private void sendDataToBackend(Map<String, Object> dataMap, final List<Grid<ModelData>> gridsInUse) {
		StringBuilder dataToSend = new StringBuilder();

		// convert data to URL-encoded format
		for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue().toString();
			
			if (dataToSend.length() > 0)
				dataToSend.append("&");
			
			dataToSend.append(URL.encode(key))
						.append("=")
						.append(URL.encode(value));
		}
		
		cp.mask("Mentés...");

		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, DATA_URL_NAME);
		requestBuilder.setHeader("Content-Type", "application/x-www-form-urlencoded");
		
		// send request
		try {
			requestBuilder.sendRequest(dataToSend.toString(), new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response) {
					if (response.getStatusCode() == 200) {
						Info.display("Success", "Data moved successfully!");
					}
					cp.unmask();
				}
				
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert("Request failed: " + exception.getMessage());
					cp.unmask();
				}
			});
		} catch (Exception e) {
			Window.alert("Error while sending request: " + e.getMessage());
			cp.unmask();
		}
	}
}