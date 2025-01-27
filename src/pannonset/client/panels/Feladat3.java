package pannonset.client.panels;

import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Window;
import com.google.gwt.user.client.Element;

public class Feladat3 extends TabItem {
	public Feladat3(String text) {
		this.setText(text);
	}

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		
		LayoutContainer container = new LayoutContainer();
		container.setSize("100%", "100%");
		
        container.addListener(Events.OnClick, new Listener<ComponentEvent>() {
			@Override
			public void handleEvent(ComponentEvent event) {
				int x = event.getClientX();
				int y = event.getClientY();
				
				Window window = new Window();
				window.setSize(200, 100);
				window.setHeading("Window heading");
				window.setPosition(x, y);
				window.show();
			}
		});
		
		add(container);
	}
}
