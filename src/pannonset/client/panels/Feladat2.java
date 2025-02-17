package pannonset.client.panels;

import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.Label;
import com.google.gwt.user.client.Element;

public class Feladat2 extends TabItem {
	public Feladat2(String text) {
		this.setText(text);
	}
	
	private String reverseStr(String str) {
		if (str == null || str.trim().isEmpty()) {
			return null;
		}
		
		StringBuilder reversed = new StringBuilder();
		
		for (int i = str.length() - 1; i >= 0; i--) {
			reversed.append(str.charAt(i));
		}
		
		return reversed.toString();
	}
	
	private void handleBtnClick(TextField<String> textField, int btnNumber) {
		String reversed = reverseStr(textField.getValue());
		
		if (reversed == null) {
			MessageBox.alert("Error", "Please enter some text!", null);
			return;
		}
		
		switch(btnNumber) {
		case 1:
			MessageBox.alert("Title for MessageBox.alert", reversed, null);
			break;
		case 2:
			Info.display("Title for Info.display", reversed);
			break;
		case 3:
			Window window = new Window();
			window.setHeading("GXT Window Heading");
			window.setSize(400, 200);
			window.add(new Label(reversed));
			window.show();
			break;
		default:
			MessageBox.alert("Title", reversed, null);
		}
	}
	
	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		
		// Create text field for input		
		final TextField<String> textField = new TextField<String>();
		textField.setAllowBlank(false);  
		textField.setEmptyText("Enter some text here");
		
		// Add buttons and handle clicks on them
		Button b1 = new Button("MessageBox.alert", new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				handleBtnClick(textField, 1);
			}  
		});
		Button b2 = new Button("Info.display", new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				handleBtnClick(textField, 2);
			}  
		});
		Button b3 = new Button("GXT Window", new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				handleBtnClick(textField, 3);
			}  
		});

		// Add components
		add(textField);
		add(b1);
		add(b2);
		add(b3);
	}
}
