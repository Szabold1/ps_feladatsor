package pannonset.client.panels;

import com.extjs.gxt.ui.client.data.BaseModelData;
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
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Element;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Feladat10 extends TabItem {
	private static final int WIDTH = 1000;
	private static final int HEIGHT = 400;
	private static final int COL_WIDTH = 200;
	private static final long ONE_YEAR_IN_MILLIS = 365L * 24 * 60 * 60 * 1000;
	private static final int MAX_YEARS = 100;
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final String DUMMY_DATA_FILE_NAME = "pelda-adat.php";

	
	public Feladat10(String text) {
		this.setText(text);
	}

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		
		ContentPanel cp = createContentPanel();
		
		List<ColumnConfig> columns = defineColumns();
		
		// set up the grid
		ColumnModel cm = new ColumnModel(columns);
		final ListStore<BaseModelData> store = new ListStore<BaseModelData>();
		final Grid<BaseModelData> grid = new Grid<BaseModelData>(store, cm);
		grid.setSize(WIDTH, HEIGHT);

		// add double click listener to grid rows
		grid.addListener(Events.RowDoubleClick, new Listener<GridEvent<BaseModelData>>() {
			@Override
			public void handleEvent(GridEvent<BaseModelData> event) {
				BaseModelData selected = grid.getSelectionModel().getSelectedItem();
				if (selected != null) {
					final Window window = createWindowWithForm(store, selected);
					window.show();
				}
			}
		});

		fetchGridData(store);
		
		ToolBar toolBar = createToolbarWithBtns(store, grid);
		
		cp.setTopComponent(toolBar);
		cp.add(grid);
		add(cp);
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
		
		ColumnConfig column = new ColumnConfig("name", "Name", COL_WIDTH);
		columns.add(column);
		
		column = new ColumnConfig("nickname", "Nickname", COL_WIDTH);
		columns.add(column);
		
		column = new ColumnConfig("birthdate", "Birthdate", COL_WIDTH);
		column.setDateTimeFormat(DateTimeFormat.getFormat(DATE_FORMAT));
		columns.add(column);
		
		column = new ColumnConfig("age", "Age", COL_WIDTH);
		columns.add(column);
		
		column = new ColumnConfig("favNum", "Favourite Number", COL_WIDTH);
		columns.add(column);
		
		return columns;
	}
	
	private Window createWindowWithForm(final ListStore<BaseModelData> store, final BaseModelData existingData) {
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
		nameF.setRegex("^[\\p{L} ]+$"); // any Unicode letter and space
		formPanel.add(nameF);

		final TextField<String> nicknameF = new TextField<String>();
		nicknameF.setFieldLabel("Nickname");
		nicknameF.setAllowBlank(false);
		nicknameF.setRegex("^[\\p{L}]+$"); // any Unicode letter
		formPanel.add(nicknameF);

		final DateField birthdateF = new DateField();
		birthdateF.setFieldLabel("Birthdate");
		birthdateF.setAllowBlank(false);
		birthdateF.setMinValue(new Date(System.currentTimeMillis() - (MAX_YEARS * ONE_YEAR_IN_MILLIS)));
		birthdateF.setMaxValue(new Date());
		formPanel.add(birthdateF);

		final NumberField ageF = new NumberField();
		ageF.setFieldLabel("Age");
		ageF.setAllowBlank(false);
		ageF.setMinValue(0);
		ageF.setMaxValue(MAX_YEARS);
		formPanel.add(ageF);

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
			nameF.validate();
			nicknameF.setValue((String) existingData.get("nickname"));
			nicknameF.validate();
			birthdateF.setValue((Date) existingData.get("birthdate"));
			ageF.setValue((Number) existingData.get("age"));
			favNumF.setValue((Number) existingData.get("favNum"));
		}

		// buttons (save, cancel)
		ToolBar buttonBar = new ToolBar();
		Button saveBtn = new Button("Mentés");
		Button cancelBtn = new Button("Mégse");

		saveBtn.addListener(Events.OnClick, new Listener<ButtonEvent>() {
			@Override
			public void handleEvent(ButtonEvent event) {
				if (!nameF.validate() || !nicknameF.validate() || !birthdateF.validate() ||
						!ageF.validate() || !favNumF.validate()) {
					MessageBox.alert("Error", "Please fill out all fields correctly.", null);
					return;
				}

				BaseModelData data = existingData != null ? existingData : new BaseModelData();
				data.set("name", nameF.getValue());
				data.set("nickname", nicknameF.getValue());
				data.set("birthdate", birthdateF.getValue());
				data.set("age", ageF.getValue().intValue());
				data.set("favNum", favNumF.getValue().intValue());

				if (existingData != null)
					store.update(data);
				else
					store.add(data);
				
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

	private void fetchGridData(final ListStore<BaseModelData> store) {
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, DUMMY_DATA_FILE_NAME);
		
		try {
			requestBuilder.sendRequest(null, new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response) {
					if (response.getStatusCode() == 200) {
						JSONArray jsonArray = JSONParser.parseStrict(response.getText()).isArray();
						
						// fill up rows with data
						for (int i = 0; i < jsonArray.size(); i++) {
							JSONObject jsonObj = jsonArray.get(i).isObject();
							BaseModelData model = new BaseModelData();

							model.set("name", jsonObj.get("name").isString().stringValue());
							model.set("nickname", jsonObj.get("nickname").isString().stringValue());
							String dateStr = jsonObj.get("birthdate").isString().stringValue();
							DateTimeFormat dateFormat = DateTimeFormat.getFormat(DATE_FORMAT);
							model.set("birthdate", dateFormat.parse(dateStr));
							model.set("age", (int)jsonObj.get("age").isNumber().doubleValue());
							model.set("favNum", (int)jsonObj.get("favNum").isNumber().doubleValue());

							store.add(model);
						}
					} else {
						com.google.gwt.user.client.Window.alert("Error: " + response.getStatusText());
					}
				}
				
				@Override
				public void onError(Request request, Throwable exception) {
					com.google.gwt.user.client.Window.alert("Network error!");
				}
			});
		} catch (RequestException e) {
			com.google.gwt.user.client.Window.alert("Error sending request!");
		}
	}

	private ToolBar createToolbarWithBtns(final ListStore<BaseModelData> store, final Grid<BaseModelData> grid) {
		ToolBar toolBar = new ToolBar();
		Button btnNew = new Button("Új");
		Button btnDelete = new Button("Törlés");

		btnNew.addListener(Events.OnClick, new Listener<ButtonEvent>() {
			@Override
			public void handleEvent(ButtonEvent event) {
				final Window window = createWindowWithForm(store, null);
				window.show();
			}
		});
		toolBar.add(btnNew);
		
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
		
		return toolBar;
	}
}