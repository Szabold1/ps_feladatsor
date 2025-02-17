package pannonset.client.panels;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
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
	private TextField<Integer> num1Field;
	private TextField<Integer> num2Field;
	private TextField<String> operation;
	private TextField<String> resultField;
	private Button calculateBtn;
	
	public Feladat6(String text) {
		this.setText(text);
		
		num1Field = new TextField<Integer>();
		num1Field.setEmptyText("1.szám");
		num1Field.setAllowBlank(false);
		
		num2Field = new TextField<Integer>();
		num2Field.setEmptyText("2.szám");
		num2Field.setAllowBlank(false);
		
		operation = new TextField<String>();
		operation.setEmptyText("Művelet (+ - % *)");

		resultField = new TextField<String>();
		resultField.setEnabled(false);
		
		calculateBtn = new Button("Számol");
		calculateBtn.addListener(Events.OnClick, new Listener<ComponentEvent>() {
			@Override
			public void handleEvent(ComponentEvent event) {
				sendRequest();
			}
		});
	}
	
	private void sendRequest() {
		String num1 = num1Field.getRawValue();
		String num2 = num2Field.getRawValue();
		String operationValue = operation.getRawValue();
		
		// check for empty input
		if (num1 == null || num2 == null || num1.isEmpty() || num2.isEmpty()) {
            MessageBox.alert("Hiba", "Mindkét mezőt ki kell tölteni!", null);
            return;
        }
		
		// check if the values are integers
		try {
			Integer.parseInt(num1);
			Integer num2Int = Integer.parseInt(num2);
			if (num2Int < 0) {
				MessageBox.alert("Hiba", "A második szám nem lehet negatív!", null);
				return;
			}
		} catch (NumberFormatException e) {
			MessageBox.alert("Hiba", "Érvénytelen szám(ok)!", null);
			return;
		}
		
		// check if operation is valid
		if (!operationValue.equals("") && !operationValue.equals("+") && !operationValue.equals("-")
				&& !operationValue.equals("%") && !operationValue.equals("*")) {
			MessageBox.alert("Hiba", "Érvénytelen művelet! Csak a következő műveletek engedélyezettek: +, -, %, *", null);
			return;
		}
		
		String url = GWT.getHostPageBaseURL()
				+ "szamologep.php?num1=" + num1 + "&num2=" + num2 + "&operation=" + operationValue;
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));
		
		try {
			requestBuilder.sendRequest(null, new RequestCallback() {
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
		add(calculateBtn);
	}
}
