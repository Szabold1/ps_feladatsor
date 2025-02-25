package pannonset.client.panels;

import com.extjs.gxt.ui.client.data.JsonPagingLoadResultReader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.Loader;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.BaseDateFilterConfig;
import com.extjs.gxt.ui.client.data.BaseFilterPagingLoadConfig;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.BaseNumericFilterConfig;
import com.extjs.gxt.ui.client.data.ModelType;
import com.extjs.gxt.ui.client.data.HttpProxy;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.filters.DateFilter;
import com.extjs.gxt.ui.client.widget.grid.filters.Filter;
import com.extjs.gxt.ui.client.widget.grid.filters.GridFilters;
import com.extjs.gxt.ui.client.widget.grid.filters.NumericFilter;
import com.extjs.gxt.ui.client.widget.grid.filters.StringFilter;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Element;
import com.google.gwt.i18n.client.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Feladat13 extends TabItem {
	private static final int WIDTH = 1000;
	private static final int HEIGHT = 400;
	private static final int COL_WIDTH = 200;
	private static final int PAGE_MAX_ITEMS_NB = 5;
	private static final long ONE_YEAR_IN_MILLIS = 365L * 24 * 60 * 60 * 1000;
	private static final int MAX_YEARS = 100;
	private static final DateTimeFormat DATE_FORMAT = DateTimeFormat.getFormat("yyyy-MM-dd");
	private static final String DATA_URL_NAME = GWT.getHostPageBaseURL() + "adatbazis13.php";
	private static final String REGEX_NAME = "^[\\p{L} ]+$";
	private static final String REGEX_NICKNAME = "^[\\p{L}]+$";
	private static final String REGEX_EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
	
	private ColumnModel cm;
	private PagingLoader<PagingLoadResult<BaseModelData>> loader;
	private ListStore<ModelData> store;
	private Grid<ModelData> grid;
	private GridFilters filters;
	
	public Feladat13(String text) {
		this.setText(text);
	}

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		
		ContentPanel cp = createContentPanel();
		
		List<ColumnConfig> columns = defineColumns();
		
		cm = new ColumnModel(columns);

		filters = setupFilters();
		
		loader = setupLoader();
		
		store = new ListStore<ModelData>(loader);
		
		grid = setupGrid();
		grid.addPlugin(filters);
		
		// create toolbars
		ToolBar toolBar = createToolbarWithBtns();
		final PagingToolBar pagingToolBar = new PagingToolBar(PAGE_MAX_ITEMS_NB);
		pagingToolBar.bind(loader);
		
		// add components to the panel
		cp.setTopComponent(toolBar);
		cp.setBottomComponent(pagingToolBar);
		cp.add(grid);
		add(cp);
		
		loader.load();
	}

	private ContentPanel createContentPanel() {
		ContentPanel cp = new ContentPanel();
		cp.setHeading("Grid Example");
		cp.setLayout(new FitLayout());
		cp.setSize(WIDTH, HEIGHT);
		cp.setBodyBorder(true);
		return cp;
	}

	private List<ColumnConfig> defineColumns() {
		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
		
		columns.add(new ColumnConfig("name", "Name", COL_WIDTH));
		columns.add(new ColumnConfig("nickname", "Nickname", COL_WIDTH));
		columns.add(new ColumnConfig("email", "E-mail", COL_WIDTH));
		
		ColumnConfig column = new ColumnConfig("birthdate", "Birthdate", COL_WIDTH);
		column.setDateTimeFormat(DATE_FORMAT);
		columns.add(column);
		
		columns.add(new ColumnConfig("favNum", "Favourite Number", COL_WIDTH));
		
		return columns;
	}

	private GridFilters setupFilters() {
		filters = new GridFilters();
		filters.setLocal(false);
		
		filters.addFilter(new StringFilter("name"));
		filters.addFilter(new StringFilter("nickname"));
		filters.addFilter(new StringFilter("email"));
		filters.addFilter(new DateFilter("birthdate"));
		filters.addFilter(new NumericFilter("favNum"));

		return filters;
	}
	
	private PagingLoader<PagingLoadResult<BaseModelData>> setupLoader() {
		// create the model type
		ModelType type = new ModelType();
		type.setRoot("data");
		type.setTotalName("totalCount");
		type.addField("name", "name");
		type.addField("nickname", "nickname");
		type.addField("email", "email");
		type.addField("birthdate", "birthdate");
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
		loader.setRemoteSort(true);
		
		// add listener to send filters to the backend before loading data
		loader.addListener(Loader.BeforeLoad, new Listener<LoadEvent>() {
			@Override
			public void handleEvent(LoadEvent be) {
				StringBuilder stringBuilder = new StringBuilder();
				List<Filter> activeFilters = new ArrayList<Filter>(filters.getFilterData());
				String strValue = "";

				// add filters to the request
				for (int i = 0; i < activeFilters.size(); i++) {
					Filter filter = activeFilters.get(i);
					stringBuilder.append(filter.getDataIndex());
					stringBuilder.append(":");
					
					Object filterValue = filter.getValue();
					// if it is a list, it is date or number
					if (filterValue instanceof List<?>) {
						List<?> filterValues = (List<?>) filterValue;
						// if the first element is a date
						if (filterValues.get(0) instanceof BaseDateFilterConfig) {
							BaseDateFilterConfig first = (BaseDateFilterConfig) filterValues.get(0);
							Date date1 = (Date) first.getValue();
							strValue = first.getComparison() + "=" + DATE_FORMAT.format(date1);

							if (filterValues.size() > 1) {
								BaseDateFilterConfig second = (BaseDateFilterConfig) filterValues.get(1);
								Date date2 = (Date) second.getValue();
								strValue += "_" + second.getComparison() + "=" + DATE_FORMAT.format(date2);
								// e.g. birthdate:before=2015-02-13_after=2010-01-07
							}
						}
						// if the first element is a number
						else if (filterValues.get(0) instanceof BaseNumericFilterConfig) {
							BaseNumericFilterConfig first = (BaseNumericFilterConfig) filterValues.get(0);
							strValue = first.getComparison() + "=" + first.getValue().toString();

							if (filterValues.size() > 1) {
								BaseNumericFilterConfig second = (BaseNumericFilterConfig) filterValues.get(1);
								strValue += "_" + second.getComparison() + "=" + second.getValue().toString();
								// e.g. favNum:lt=10.0_gt=3.0
							}
						}
					}
					// if it is a string
					else
						strValue = (filter.getValue() != null) ? filter.getValue().toString() : "";
					
					stringBuilder.append(strValue);
					
					if (i < activeFilters.size() - 1)
						stringBuilder.append(",");
				}
				
				PagingLoadConfig config = be.getConfig();
				config.set("filter", stringBuilder.toString());
			}
		});
		
		return loader;
	}
	
	private Grid<ModelData> setupGrid() {
		grid = new Grid<ModelData>(store, cm);
		grid.setSize(WIDTH, HEIGHT);
		grid.setStateId("pagingGridExample");
		grid.setStateful(true);
		grid.setLoadMask(true);
		grid.setBorders(true);
		
		// add double click listener to grid rows
		grid.addListener(Events.RowDoubleClick, new Listener<GridEvent<ModelData>>() {
			@Override
			public void handleEvent(GridEvent<ModelData> event) {
				ModelData selected = grid.getSelectionModel().getSelectedItem();
				if (selected != null) {
					final Window window = createWindowWithForm(selected);
					window.show();
				}
			}
		});
		
		return grid;
	}

	private ToolBar createToolbarWithBtns() {
		ToolBar toolBar = new ToolBar();
		Button btnNew = new Button("Új");
		Button btnDelete = new Button("Törlés");

		btnNew.addListener(Events.OnClick, new Listener<ButtonEvent>() {
			@Override
			public void handleEvent(ButtonEvent event) {
				final Window window = createWindowWithForm(null);
				window.show();
			}
		});
		toolBar.add(btnNew);
		
		btnDelete.addListener(Events.OnClick, new Listener<ButtonEvent>() {
			@Override
			public void handleEvent(ButtonEvent event) {
				ModelData selected = grid.getSelectionModel().getSelectedItem();
				if (selected != null) {
					store.remove(selected);
				}
			}
		});
		toolBar.add(btnDelete);
		
		return toolBar;
	}

	private Window createWindowWithForm(final ModelData existingData) {
		final Window window = new Window();
		window.setSize("auto", "auto");

		FormPanel formPanel = new FormPanel();
		formPanel.setHeading(existingData != null ? "Edit data" : "Add New Entry");
		formPanel.setWidth(350);
		formPanel.setFrame(true);
		window.add(formPanel);

		// create input fields
		final TextField<String> nameF = new TextField<String>();
		nameF.setFieldLabel("Name");
		nameF.setAllowBlank(false);
		nameF.setRegex(REGEX_NAME);
		formPanel.add(nameF);

		final TextField<String> nicknameF = new TextField<String>();
		nicknameF.setFieldLabel("Nickname");
		nicknameF.setAllowBlank(false);
		nicknameF.setRegex(REGEX_NICKNAME);
		formPanel.add(nicknameF);

		final TextField<String> emailF = new TextField<String>();
		emailF.setFieldLabel("E-mail");
		emailF.setAllowBlank(false);
		emailF.setRegex(REGEX_EMAIL);
		if (existingData != null)
			emailF.setEnabled(false);
		formPanel.add(emailF);

		final DateField birthdateF = new DateField();
		birthdateF.setFieldLabel("Birthdate");
		birthdateF.setAllowBlank(false);
		birthdateF.setMinValue(new Date(System.currentTimeMillis() - (MAX_YEARS * ONE_YEAR_IN_MILLIS)));
		birthdateF.setMaxValue(new Date());
		formPanel.add(birthdateF);

		final NumberField favNumF = new NumberField();
		favNumF.setFieldLabel("Favourite Number");
		favNumF.setAllowBlank(false);
		favNumF.setAllowDecimals(false);
		favNumF.setMinValue(-1000000000);
		favNumF.setMaxValue(1000000000);
		formPanel.add(favNumF);

		// if there is existing data, fill the form fields with it
		if (existingData != null) {
			nameF.setValue((String) existingData.get("name"));
			nicknameF.setValue((String) existingData.get("nickname"));
			emailF.setValue((String) existingData.get("email"));
			
			if (existingData.get("birthdate") instanceof String) {
				birthdateF.setValue(DATE_FORMAT.parse((String) existingData.get("birthdate")));
			} else {
				birthdateF.setValue((Date) existingData.get("birthdate"));
			}

			if (existingData.get("favNum") instanceof String) {
				favNumF.setValue(Integer.parseInt((String) existingData.get("favNum")));
			} else {
				favNumF.setValue((Integer) existingData.get("favNum"));
			}
		}

		// buttons (save, cancel)
		ToolBar buttonBar = new ToolBar();
		Button saveBtn = new Button("Mentés");
		Button cancelBtn = new Button("Mégse");

		saveBtn.addListener(Events.OnClick, new Listener<ButtonEvent>() {
			@Override
			public void handleEvent(ButtonEvent event) {
				// validate the input fields
				if (!nameF.validate() || !nicknameF.validate() || !emailF.validate()
						|| !birthdateF.validate() || !favNumF.validate()) {
					MessageBox.alert("Error", "Please fill out all fields correctly.", null);
					return;
				}

				// save data into a hashmap
				Map<String, Object> dataMap = new HashMap<String, Object>();
				dataMap.put("name", nameF.getValue());
				dataMap.put("nickname", nicknameF.getValue());
				dataMap.put("email", emailF.getValue());
				dataMap.put("birthdate", birthdateF.getValue());
				dataMap.put("favNum", favNumF.getValue().intValue());

				// set data on the frontend
				ModelData data = existingData != null ? existingData : new BaseModelData();
				data.set("name", dataMap.get("name"));
				data.set("nickname", dataMap.get("nickname"));
				data.set("email", dataMap.get("email"));
				data.set("birthdate", dataMap.get("birthdate"));
				data.set("favNum", dataMap.get("favNum"));

				if (existingData != null)
					store.update(data);
				else
					store.add(data);

				// send data to the backend
				sendDataToDB(dataMap);
				
				window.hide();
			}
		});

		cancelBtn.addListener(Events.OnClick, new Listener<ButtonEvent>() {
			@Override
			public void handleEvent(ButtonEvent event) {
				window.hide();
			}
		});

		buttonBar.add(new FillToolItem());
		buttonBar.add(cancelBtn);
		buttonBar.add(saveBtn);

		window.setBottomComponent(buttonBar);
		return window;
	}

	private void sendDataToDB(Map<String, Object> dataMap) {
		StringBuilder dataToSend = new StringBuilder();

		// convert data to URL-encoded format
		for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
			if (entry.getValue() instanceof Date)
				entry.setValue(DATE_FORMAT.format((Date) entry.getValue()));
			
			String key = entry.getKey();
			String value = entry.getValue().toString();
			
			if (dataToSend.length() > 0)
				dataToSend.append("&");
				
			dataToSend.append(URL.encode(key))
						.append("=")
						.append(URL.encode(value));
		}
		
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, DATA_URL_NAME);
		requestBuilder.setHeader("Content-Type", "application/x-www-form-urlencoded");
		
		// send request
		try {
			requestBuilder.sendRequest(dataToSend.toString(), new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response) {
					if (response.getStatusCode() == 200) {
						Info.display("Success", "Data saved successfully!");
					}
				}

				@Override
				public void onError(Request request, Throwable exception) {
					com.google.gwt.user.client.Window.alert("Request failed: " + exception.getMessage());
				}
			});
		} catch (Exception e) {
			com.google.gwt.user.client.Window.alert("Error while sending request: " + e.getMessage());
		}
	}
}
