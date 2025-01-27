package pannonset.client;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

import pannonset.client.panels.Feladat1;
import pannonset.client.panels.Feladat2;
import pannonset.client.panels.Feladat3;

public class Test implements EntryPoint {

	private ContentPanel cp;
	private TabPanel tp;

	@Override
	public void onModuleLoad() {
		cp = new ContentPanel();
		cp.setHeaderVisible(true);
		cp.setWidth("100%");
		cp.setFrame(true);
		cp.setHeading("TanProjekt");
		cp.setLayout(new FitLayout());

		tp = new TabPanel();
		tp.add(new Feladat1("1.feladat"));
		tp.add(new Feladat2("2.feladat"));
		tp.add(new Feladat3("3.feladat"));
		
		tp.setSelection(tp.getItem(tp.getItemCount() -1)); // mindig a legutolsó legyen megnyitva alapból

		cp.add(tp);
		Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				Resize();
			}
		});
		Resize();

		RootPanel.get("gwt").add(cp);
	}

	// Az ablak teljes magasságára méreteződjön
	private void Resize() {
		cp.setHeight(Window.getClientHeight());
	}
}