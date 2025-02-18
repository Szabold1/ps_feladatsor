package pannonset.client.panels;

import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;

public class Feladat5 extends TabItem {
	private TextArea textArea;
	
	public Feladat5(String text) {
		this.setText(text);
		this.textArea = new TextArea();
		this.textArea.setSize("100%", "100%");
		this.textArea.setReadOnly(true);
		this.add(textArea);
	}

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		
		String url = GWT.getHostPageBaseURL() + "alapok.php";
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
		
		try {
			requestBuilder.sendRequest(null, new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response) {
					if (response.getStatusCode() == 200) {
						textArea.setValue(response.getText()
												.replace("<br>", "\n")
												.replace("<pre>", "")
												.replace("</pre>", ""));
					}
				}
				
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert("Request failed: " + exception.getMessage());
				}
			});
		} catch (Exception e) {
			Window.alert("Error while sending request: " + e.getMessage());
		}
	}
}