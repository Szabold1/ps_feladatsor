package pannonset.client.panels;

import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.user.client.Element;

public class Feladat1 extends TabItem {
	public Feladat1(String text) {
		this.setText(text);
	}

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		
		Button b = new Button("Gomb");
		add(b);
	}
}