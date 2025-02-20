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
import pannonset.client.panels.Feladat4;
import pannonset.client.panels.Feladat5;
import pannonset.client.panels.Feladat6;
import pannonset.client.panels.Feladat7;
import pannonset.client.panels.Feladat8;
import pannonset.client.panels.Feladat9;
import pannonset.client.panels.Feladat10;
import pannonset.client.panels.Feladat11;
import pannonset.client.panels.Feladat12;

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
		tp.add(new Feladat4("4.feladat"));
		tp.add(new Feladat5("5.feladat"));
		tp.add(new Feladat6("6.feladat"));
		tp.add(new Feladat7("7.feladat"));
		tp.add(new Feladat8("8.feladat"));
		tp.add(new Feladat9("9.feladat"));
		tp.add(new Feladat10("10.feladat"));
		tp.add(new Feladat11("11.feladat"));
		tp.add(new Feladat12("12.feladat"));
		
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