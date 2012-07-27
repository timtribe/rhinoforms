package com.rhinoforms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mozilla.javascript.Scriptable;

import com.rhinoforms.serverside.InputPojo;

public class FormFlow {

	private int id;
	private Scriptable scope;
	private List<InputPojo> currentInputPojos;
	private Map<String, List<Form>> formLists;
	private Form currentForm;
	private List<Form> currentFormList;
	private Map<String, String> data;
	
	public FormFlow(Scriptable scope) {
		this.id = (int) (Math.random() * 100000000f);
		this.scope = scope;
		this.data = new HashMap<String, String>();
	}
	
	public String navigateToFirstForm() {
		this.currentFormList = formLists.get("main");
		this.currentForm = currentFormList.iterator().next();
		return currentForm.getPath();
	}
	
	public String navigateFlow(String action) throws NavigationError {
		Map<String, String> actions = currentForm.getActions();
		if (actions.containsKey(action)) {
			String actionTarget = actions.get(action);
			if (actionTarget.isEmpty()) {
				if (action.equals("next")) {
					currentForm = currentFormList.get(currentForm.getIndexInList() + 1);
				} else if (action.equals("back")) {
					currentForm = currentFormList.get(currentForm.getIndexInList() - 1);
				} else if (action.equals("finish")) {
					return null;
				}
			} else {
				String actionTargetFormId;
				if (actionTarget.contains(".")) {
					String[] actionTargetParts = actionTarget.split("\\.");
					currentFormList = formLists.get(actionTargetParts[0]);
					actionTargetFormId = actionTargetParts[1];
				} else {
					actionTargetFormId = actionTarget;
				}
				for (Form form : currentFormList) {
					if (form.getId().equals(actionTargetFormId)) {
						currentForm = form;
					}
				}
			}
		} else {
			throw new NavigationError("Action not valid for the current form.");
		}
		return currentForm.getPath();
	}
	
	public int getId() {
		return id;
	}
	
	public Scriptable getScope() {
		return scope;
	}

	public List<InputPojo> getCurrentInputPojos() {
		return currentInputPojos;
	}
	
	public void setCurrentInputPojos(List<InputPojo> currentInputPojos) {
		this.currentInputPojos = currentInputPojos;
	}
	
	public Map<String, List<Form>> getFormLists() {
		return formLists;
	}
	
	public void setFormLists(Map<String, List<Form>> formLists) {
		this.formLists = formLists;
	}
	
	public Map<String, String> getData() {
		return data;
	}
	
	public void setData(Map<String, String> data) {
		this.data = data;
	}

}
