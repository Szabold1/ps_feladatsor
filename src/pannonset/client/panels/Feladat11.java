package pannonset.client.panels;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelType;
import com.extjs.gxt.ui.client.data.HttpProxy;
import com.extjs.gxt.ui.client.data.JsonReader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
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
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.Element;
import com.google.gwt.i18n.client.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Feladat11 extends TabItem {
	private static final int WIDTH = 1000;
	private static final int HEIGHT = 400;
	private static final int COL_WIDTH = 200;
	private static final long ONE_YEAR_IN_MILLIS = 365L * 24 * 60 * 60 * 1000;
	private static final int MAX_YEARS = 100;
	private static final DateTimeFormat DATE_FORMAT = DateTimeFormat.getFormat("yyyy-MM-dd");
	private static final String DATA_FILE_NAME = GWT.getHostPageBaseURL() + "adatbazis11.php";
	private static final String REGEX_NAME = "^[\\p{L} ]+$";
	private static final String REGEX_NICKNAME = "^[\\p{L}]+$";
	private static final String REGEX_EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

	
	public Feladat11(String text) {
		this.setText(text);
	}

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		
		ContentPanel cp = createContentPanel();
		
		List<ColumnConfig> columns = defineColumns();
		
		ColumnModel cm = new ColumnModel(columns);
		
		final ListStore<ModelData> store = handleDataFetch();

		// set up the grid
		final Grid<ModelData> grid = new Grid<ModelData>(store, cm);
		grid.setSize(WIDTH, HEIGHT);

		ToolBar toolBar = createToolbarWithBtns(store, grid);

		// add double click listener to grid rows
		grid.addListener(Events.RowDoubleClick, new Listener<GridEvent<ModelData>>() {
			@Override
			public void handleEvent(GridEvent<ModelData> event) {
				ModelData selected = grid.getSelectionModel().getSelectedItem();
				if (selected != null) {
					final Window window = createWindowWithForm(store, selected);
					window.show();
				}
			}
		});

		// add components to the panel
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
		
		columns.add(new ColumnConfig("name", "Name", COL_WIDTH));
		columns.add(new ColumnConfig("nickname", "Nickname", COL_WIDTH));
		columns.add(new ColumnConfig("email", "E-mail", COL_WIDTH));
		
		ColumnConfig column = new ColumnConfig("birthdate", "Birthdate", COL_WIDTH);
		column.setDateTimeFormat(DATE_FORMAT);
		columns.add(column);
		
		columns.add(new ColumnConfig("favNum", "Favourite Number", COL_WIDTH));
		
		return columns;
	}
	
	private ListStore<ModelData> handleDataFetch() {
		// create the model type
		ModelType type = new ModelType();
		type.setRoot("data");
		type.addField("name", "name");
		type.addField("nickname", "nickname");
		type.addField("email", "email");
		type.addField("birthdate", "birthdate");
		type.addField("favNum", "favourite_number");
		
		// set up the loader
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, DATA_FILE_NAME);
		HttpProxy<String> proxy = new HttpProxy<String>(requestBuilder);
		JsonReader<ListLoadResult<ModelData>> reader = new JsonReader<ListLoadResult<ModelData>>(type);
		BaseListLoader<ListLoadResult<ModelData>> loader = new BaseListLoader<ListLoadResult<ModelData>>(proxy, reader);
		
		loader.load();

		return new ListStore<ModelData>(loader);
	}

	private ToolBar createToolbarWithBtns(final ListStore<ModelData> store, final Grid<ModelData> grid) {
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
				ModelData selected = grid.getSelectionModel().getSelectedItem();
				if (selected != null) {
					store.remove(selected);
				}
			}
		});
		toolBar.add(btnDelete);
		
		return toolBar;
	}

	private Window createWindowWithForm(final ListStore<ModelData> store, final ModelData existingData) {
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
				if (!nameF.validate() || !nicknameF.validate() || !emailF.validate()
						|| !birthdateF.validate() || !favNumF.validate()) {
					MessageBox.alert("Error", "Please fill out all fields correctly.", null);
					return;
				}

				ModelData data = existingData != null ? existingData : new BaseModelData();
				data.set("name", nameF.getValue());
				data.set("nickname", nicknameF.getValue());
				data.set("email", emailF.getValue());
				data.set("birthdate", birthdateF.getValue());
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
}