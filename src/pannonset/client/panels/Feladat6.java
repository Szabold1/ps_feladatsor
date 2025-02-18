package pannonset.client.panels;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;


public class Feladat6 extends TabItem {
	private NumberField num1Field;
	private NumberField num2Field;
	private TextField<String> operation;
	private TextField<String> resultField;
	private Button calculateBtnGet;
	private Button calculateBtnPost;
	
	public Feladat6(String text) {
		this.setText(text);
		
		num1Field = new NumberField();
		num1Field.setEmptyText("1.szám");
		num1Field.setAllowBlank(false);
		
		num2Field = new NumberField();
		num2Field.setEmptyText("2.szám");
		num2Field.setAllowBlank(false);
		
		operation = new TextField<String>();
		operation.setEmptyText("Művelet (+ - % *)");

		resultField = new TextField<String>();
		resultField.setEnabled(false);
		
		calculateBtnGet = new Button("Számol (GET)");
		calculateBtnGet.addListener(Events.OnClick, new Listener<ComponentEvent>() {
			@Override
			public void handleEvent(ComponentEvent event) {
				sendRequest("GET");
			}
		});
		
		calculateBtnPost = new Button("Számol (POST)");
		calculateBtnPost.addListener(Events.OnClick, new Listener<ComponentEvent>() {
			@Override
			public void handleEvent(ComponentEvent event) {
				sendRequest("POST");
			}
		});
	}
	
	private void sendRequest(String method) {
		Number num1 = num1Field.getValue();
		Number num2 = num2Field.getValue();
		String operationValue = operation.getRawValue();
		
		// check for empty input
		if (num1 == null || num2 == null) {
            MessageBox.alert("Hiba", "Az első két mezőt ki kell tölteni!", null);
            return;
        }
		
		// check if the num2 is negative
		if (num2 instanceof Double && (Double) num2 < 0) {
			MessageBox.alert("Hiba", "A második szám nem lehet negatív!", null);
			return;
		}
		
		// check if operation is valid
		if (!operationValue.isEmpty() && !operationValue.equals("+") && !operationValue.equals("-")
				&& !operationValue.equals("%") && !operationValue.equals("*")) {
			MessageBox.alert("Hiba", "Érvénytelen művelet! Csak a következő műveletek engedélyezettek: +, -, %, *", null);
			return;
		}
		
		String url = GWT.getHostPageBaseURL() + "szamologep.php";
		String dataToSend = "num1=" + num1 +
							"&num2=" + num2 +
							"&operation=" + URL.encodeQueryString(operationValue);
		RequestBuilder requestBuilder;
		
		// handle GET and POST request
		if (method.equalsIgnoreCase("GET")) {
			url += "?" + dataToSend;
			requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
		} else {
			requestBuilder = new RequestBuilder(RequestBuilder.POST, url);
			requestBuilder.setHeader("Content-Type", "application/x-www-form-urlencoded");
		}

		// send request
		try {
			requestBuilder.sendRequest(method.equalsIgnoreCase("GET") ? null : dataToSend, new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response) {
					if (response.getStatusCode() == 200) {
						resultField.setValue(response.getText());
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

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		
		// Add components
		add(num1Field);
		add(num2Field);
		add(operation);
		add(resultField);
		add(calculateBtnGet);
		add(calculateBtnPost);
	}
}
