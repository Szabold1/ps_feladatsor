package pannonset.client.panels;

import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.google.gwt.user.client.Element;
import java.util.ArrayList;
import java.util.List;



public class Feladat4 extends TabItem {
	private static final int NB_ROWS = 3;
	private static final int NB_COLS = 3;
	private static final int BTN_SIZE = 50;
	
	private List<List<Button>> buttons = new ArrayList<List<Button>>();
	
	public Feladat4(String text) {
		this.setText(text);
	}
	
	private void updateBtnValues(int row, int col) {
		Button clickedBtn = buttons.get(row).get(col);
		int value = Integer.parseInt(clickedBtn.getText());
 
		// update the values in the same row and column
		for (int i = 0; i < NB_ROWS; i++) {
			for (int j = 0; j < NB_COLS; j++) {
				if (i == row || j == col) {
					Button btn = buttons.get(i).get(j);
					if (btn == clickedBtn)
						continue;
					
					int newValue = Integer.parseInt(btn.getText()) + value;
					btn.setText(String.valueOf(newValue));
				}
			}
		}
	}

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);

		VBoxLayout vBox = new VBoxLayout();
		this.setLayout(vBox);
		
		for (int row = 0; row < NB_ROWS; row++) {
			LayoutContainer rowContainer = new LayoutContainer();
			HBoxLayout hBoxLayout = new HBoxLayout();
			rowContainer.setLayout(hBoxLayout);
			rowContainer.setSize("auto", "auto");
			
			List<Button> rowButtons = new ArrayList<Button>();
			
			for (int col = 0; col < NB_COLS; col++) {
				
				Button btn = new Button("1");
				btn.setSize(BTN_SIZE, BTN_SIZE);
				
				final int currentRow = row;
				final int currentCol = col;
				
				btn.addListener(Events.OnClick, new Listener<ComponentEvent>() {
					@Override
					public void handleEvent(ComponentEvent event) {
						updateBtnValues(currentRow, currentCol);
					}
				});
				
				rowButtons.add(btn);
				rowContainer.add(btn);
			}
			
			buttons.add(rowButtons);
			this.add(rowContainer);
		}
	
		this.layout();
	}
}
