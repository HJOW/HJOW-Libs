/*
Copyright 2019 HJOW

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package hjow.common.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import hjow.common.core.Core;
import hjow.common.core.Releasable;
import hjow.common.json.JsonArray;
import hjow.common.json.JsonInstance;
import hjow.common.json.JsonObject;
import hjow.common.ui.extend.HButton;
import hjow.common.ui.extend.HCheckBox;
import hjow.common.ui.extend.HComboBox;
import hjow.common.ui.extend.HDialog;
import hjow.common.ui.extend.HLabel;
import hjow.common.ui.extend.HList;
import hjow.common.ui.extend.HPanel;
import hjow.common.ui.extend.HRadioButton;
import hjow.common.ui.extend.HSpinner;
import hjow.common.ui.extend.HSplitPane;
import hjow.common.ui.extend.HTabbedPane;
import hjow.common.ui.extend.HTextArea;
import hjow.common.ui.extend.HTextField;
import hjow.common.util.ClassUtil;
import hjow.common.util.DataUtil;

/** JSON 으로 레이아웃을 구성합니다. getRootComponent 메소드로 컴포넌트를 받아 원하는 위치에 add해 사용합니다. 더 이상 이 객체를 쓰지 않을 때에는 releaseResource 를 호출해 주세요. */
public class JsonPane implements Releasable {
	private static final long serialVersionUID = -1420018953858223569L;
	protected boolean locked = false;
	
	protected Map<Long, Object> components;
	protected HPanel pnMain;
	
	protected transient Map<String, Object> namedComponents;
	protected transient Map<String, List<Object>> taggedComponents;
	
	public JsonPane() {
		components = new Hashtable<Long, Object>();
		namedComponents = new Hashtable<String, Object>();
		taggedComponents = new HashMap<String, List<Object>>();
		pnMain = new HPanel();
		pnMain.setLayout(new BorderLayout());
	}
	
	public JsonPane(String json) {
		this();
		init((JsonObject) DataUtil.parseJson(json));
	}
	
	public JsonPane(JsonObject obj) {
		this();
		init(obj);
	}
	
	/** JSON 을 읽어 컴포넌트를 구성합니다. */
	public void init(JsonObject obj) {
		if(locked) throw new IllegalArgumentException("This JsonPane is already locked.");
		Component mainComp = process(obj);
		pnMain.add(mainComp, BorderLayout.CENTER);
		locked = true;
	}
	
	
	/** 컴포넌트 구현체를 반환합니다. */
	public Component getRootComponent() {
		if(! locked) throw new NullPointerException("This JsonPane is not initialized yet.");
		return pnMain;
	}

	@Override
	public void releaseResource() {
		if(locked) {
			pnMain.removeAll();
			locked = false;
		}
		Set<Long> keys = components.keySet();
		for(Long k : keys) {
			Object obj = components.get(k);
			try {
				if(obj instanceof HPanel     ) ((HPanel)      obj).removeAll();
				if(obj instanceof JScrollPane) ((JScrollPane) obj).removeAll();
			} catch(Throwable t) {
				Core.logError(t);
			}
		}
		components.clear();
		namedComponents.clear();
		taggedComponents.clear();
	}
	
	/** 새 컴포넌트 고유값 발급에 사용됩니다. */
	protected Long newComponentId() {
		long max = -1;
		Set<Long> keys = components.keySet();
		for(Long k : keys) {
			if(max < k.longValue()) max = k.longValue();
		}
		max = max + 1;
		return new Long(max);
	}
	
	/** 새 컴포넌트를 등록합니다. */
	protected void registerComponent(Object obj) {
		registerComponent(obj, null);
	}
	
	/** 새 컴포넌트를 등록합니다. */
	protected void registerComponent(Object obj, String name) {
		if(! components.containsValue(obj)) {
			components.put(newComponentId(), obj);
		}
		
		if(name == null) return;
		registerName(name, obj);
	}
	
	/** 컴포넌트 이름을 등록합니다. 이름은 고유해야 합니다. */
	protected void registerName(String name, Object obj) {
		if(namedComponents.containsKey(name)) throw new IllegalArgumentException("The name of component cannot be duplicated.");
		namedComponents.put(name, obj);
	}
	
	/** 컴포넌트 태그를 등록합니다. */
	protected void registerTag(String tag, Object obj) {
		List<Object> list = taggedComponents.get(tag);
		if(list == null) {
			list = new Vector<Object>();
		}
		list.add(obj);
		
		taggedComponents.put(tag, list);
	}
	
	private int parseInt(Object obj) {
		if(obj instanceof Number) return ((Number) obj).intValue();
		return new BigDecimal(String.valueOf(obj)).intValue();
	}
	
	/** 컴포넌트 고유값을 이용해 컴포넌트를 찾습니다. 레이아웃 매니저를 찾는데도 사용됩니다. */
	public Object findById(long compId) {
		Set<Long> keys = components.keySet();
		for(Long k : keys) {
			if(compId == k.longValue()) return components.get(k);
		}
		return null;
	}
	/** 컴포넌트에 지정한 name 을 이용해 컴포넌트를 찾습니다. 레이아웃 매니저를 찾는데도 사용됩니다. */
	public Object findByName(String name) {
		if(! namedComponents.containsKey(name)) return null;
		return namedComponents.get(name);
	}
	/** 컴포넌트에 지정한 tag 를 이용해 컴포넌트를 찾습니다. 레이아웃 매니저를 찾는데도 사용됩니다. */
	public List<Object> findsByTag(String tag) {
		List<Object> newList = new ArrayList<Object>();
		List<Object> targetList = taggedComponents.get(tag);
		for(Object o : targetList) {
			newList.add(o);
		}
		return newList;
	}
	
	public List<Object> findsByType(String types) {
		String typeVal = types.trim().toLowerCase();
		
		List<Object> newList = new ArrayList<Object>();
		Set<Long> keys = components.keySet();
		for(Long k : keys) {
			Object obj = components.get(k);
			if(obj == null) continue;
			
			if(typeVal.equals("panel")) {
				if(obj instanceof HPanel) newList.add(obj);
			}
			
			if(typeVal.equals("label")) {
				if(obj instanceof HLabel) newList.add(obj);
			}
			
			if(typeVal.equals("button")) {
				if(obj instanceof HButton) newList.add(obj);
			}
			
			if(typeVal.equals("textfield")) {
				if(obj instanceof HTextField) newList.add(obj);
			}
			
			if(typeVal.equals("numberfield")) {
				if(obj instanceof HSpinner) newList.add(obj);
			}
			
			if(typeVal.equals("textarea")) {
				if(obj instanceof HTextArea) newList.add(obj);
			}
			
			if(typeVal.equals("list")) {
				if(obj instanceof HList) newList.add(obj);
			}
			
			if(typeVal.equals("checkbox")) {
				if(obj instanceof HCheckBox) newList.add(obj);
			}
			
			if(typeVal.equals("radio")) {
				if(obj instanceof HRadioButton) newList.add(obj);
			}
			
			if(typeVal.equals("button_group")) {
				if(obj instanceof ButtonGroup) newList.add(obj);
			}
			
			if(typeVal.equals("combobox")) {
				if(obj instanceof HComboBox) newList.add(obj);
			}
			
			if(typeVal.equals("tab")) {
				if(obj instanceof HTabbedPane) newList.add(obj);
			}
			
			if(typeVal.equals("split")) {
				if(obj instanceof HSplitPane) newList.add(obj);
			}
			
			if(typeVal.equals("editor_pane")) {
				if(obj instanceof JEditorPane) newList.add(obj);
			}
			
			if(typeVal.equals("dialog")) {
				if(obj instanceof HDialog) newList.add(obj);
			}
			
			if(typeVal.equals("classic_web")) {
				if(obj instanceof ClassicWebPane) newList.add(obj);
			}
		}
		return newList;
	}
	
	/** 해당 JSON 객체를 통해 컴포넌트 유형을 알아내 분기합니다. */
	protected Component process(JsonObject obj) {
		if(obj == null) return null;
		
		String compTypeOpt = (String) obj.get("type");
		if(compTypeOpt == null) return null;
		
		compTypeOpt = compTypeOpt.trim().toLowerCase();
		
		if(compTypeOpt.equals("panel")) {
			return processPanel(obj);
		}
		
		if(compTypeOpt.equals("label")) {
			return processLabel(obj);
		}
		
		if(compTypeOpt.equals("button")) {
			return processButton(obj);
		}
		
		if(compTypeOpt.equals("textfield")) {
			return processTextField(obj);
		}
		
		if(compTypeOpt.equals("numberfield")) {
			return processNumberField(obj);
		}
		
		if(compTypeOpt.equals("textarea")) {
			return processTextArea(obj);
		}
		
		if(compTypeOpt.equals("list")) {
			return processList(obj);
		}
		
		if(compTypeOpt.equals("checkbox")) {
			return processCheckBox(obj);
		}
		
		if(compTypeOpt.equals("radio")) {
			return processRadio(obj);
		}
		
		if(compTypeOpt.equals("combobox")) {
			return processComboBox(obj);
		}
		
		if(compTypeOpt.equals("tab")) {
			return processTabPane(obj);
		}
		
		if(compTypeOpt.equals("split")) {
			return processSplitPane(obj);
		}
		
		if(compTypeOpt.equals("editor_pane")) {
			return processEditorPane(obj);
		}
		
		if(compTypeOpt.equals("dialog")) {
			return processDialog(obj);
		}
		
		if(compTypeOpt.equals("classic_web")) {
			return processClassicWeb(obj);
		}
		
		return processObject(obj);
	}
	
	protected Component processClassicWeb(JsonObject obj) {
		Object classType     = obj.get("type");
		if(! (String.valueOf(classType).trim().toLowerCase().equals("classic_web"))) throw new IllegalArgumentException("This is not classic web pane type.");
		
		ClassicWebPane comp = new ClassicWebPane();
		
		Object singleOpt = obj.get("enabled");
		if(singleOpt != null) {
			comp.setEnabled(Boolean.parseBoolean(String.valueOf(singleOpt)));
		}
		
		singleOpt = obj.get("page");
		if(singleOpt != null) {
			try {
				comp.goPage(singleOpt);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		singleOpt = obj.get("name");
		if(singleOpt != null) {
			registerName(String.valueOf(singleOpt), comp);
		}
		
		singleOpt = obj.get("tag");
		if(singleOpt != null) {
			registerTag(String.valueOf(singleOpt), comp);
		}
		
		processSetMethods(comp, obj, "setEnabled", "setName", "setTag", "setPage");
		registerComponent(comp);
		return processScroll(obj, comp);
	}
	
	protected Component processEditorPane(JsonObject obj) {
		Object classType     = obj.get("type");
		if(! (String.valueOf(classType).trim().toLowerCase().equals("editor_pane"))) throw new IllegalArgumentException("This is not editor pane type.");
		
		JEditorPane comp = new JEditorPane();
		
		Object singleOpt = obj.get("text");
		if(singleOpt != null) {
			comp.setText(String.valueOf(singleOpt));
		}
		
		singleOpt = obj.get("editable");
		if(singleOpt != null) {
			comp.setEditable(Boolean.parseBoolean(String.valueOf(singleOpt)));
		}
		
		singleOpt = obj.get("enabled");
		if(singleOpt != null) {
			comp.setEnabled(Boolean.parseBoolean(String.valueOf(singleOpt)));
		}
		
		singleOpt = obj.get("content_type");
		if(singleOpt != null) {
			comp.setContentType(String.valueOf(singleOpt));
		}
		
		singleOpt = obj.get("page");
		if(singleOpt != null) {
			try {
				comp.setPage(String.valueOf(singleOpt));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		singleOpt = obj.get("name");
		if(singleOpt != null) {
			registerName(String.valueOf(singleOpt), comp);
		}
		
		singleOpt = obj.get("tag");
		if(singleOpt != null) {
			registerTag(String.valueOf(singleOpt), comp);
		}
		
		processSetMethods(comp, obj, "setEnabled", "setName", "setTag", "setText", "setEditable", "setContentType", "setPage");
		registerComponent(comp);
		return processScroll(obj, comp);
	}
	
	protected Component processSplitPane(JsonObject obj) {
		Object classType     = obj.get("type");
		if(! (String.valueOf(classType).trim().toLowerCase().equals("split"))) throw new IllegalArgumentException("This is not split type.");
		
		HSplitPane comp = new HSplitPane();
		
		Object singleOpt = obj.get("enabled");
		if(singleOpt != null) {
			comp.setEnabled(Boolean.parseBoolean(String.valueOf(singleOpt)));
		}
		
		singleOpt = obj.get("name");
		if(singleOpt != null) {
			registerName(String.valueOf(singleOpt), comp);
		}
		
		singleOpt = obj.get("tag");
		if(singleOpt != null) {
			registerTag(String.valueOf(singleOpt), comp);
		}
		
		singleOpt = obj.get("orientation");
		if(singleOpt != null) {
			if(String.valueOf(singleOpt).trim().equalsIgnoreCase("horizontal")) {
				comp.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			} else if(String.valueOf(singleOpt).trim().equalsIgnoreCase("vertical")) {
				comp.setOrientation(JSplitPane.VERTICAL_SPLIT);
			}
		}
		
		singleOpt = obj.get("element_top");
		if(singleOpt != null) {
			JsonObject elementObj = null;
			if(singleOpt instanceof JsonObject) {
				elementObj = (JsonObject) singleOpt;
			} else {
				elementObj = (JsonObject) DataUtil.parseJson(String.valueOf(singleOpt));
			}
			comp.setTopComponent((Component) process(elementObj));
		}
		
		singleOpt = obj.get("element_bottom");
		if(singleOpt != null) {
			JsonObject elementObj = null;
			if(singleOpt instanceof JsonObject) {
				elementObj = (JsonObject) singleOpt;
			} else {
				elementObj = (JsonObject) DataUtil.parseJson(String.valueOf(singleOpt));
			}
			comp.setBottomComponent((Component) process(elementObj));
		}
		
		singleOpt = obj.get("element_left");
		if(singleOpt != null) {
			JsonObject elementObj = null;
			if(singleOpt instanceof JsonObject) {
				elementObj = (JsonObject) singleOpt;
			} else {
				elementObj = (JsonObject) DataUtil.parseJson(String.valueOf(singleOpt));
			}
			comp.setLeftComponent((Component) process(elementObj));
		}
		
		singleOpt = obj.get("element_right");
		if(singleOpt != null) {
			JsonObject elementObj = null;
			if(singleOpt instanceof JsonObject) {
				elementObj = (JsonObject) singleOpt;
			} else {
				elementObj = (JsonObject) DataUtil.parseJson(String.valueOf(singleOpt));
			}
			comp.setRightComponent((Component) process(elementObj));
		}
		processSetMethods(comp, obj, "setTag", "setEnabled", "setName", "setOrientation");
		registerComponent(comp);
		return comp;
	}
	
	protected Component processComboBox(JsonObject obj) {
		Object classType     = obj.get("type");
		if(! (String.valueOf(classType).trim().toLowerCase().equals("combobox"))) throw new IllegalArgumentException("This is not combobox type.");
		
		Object elements = obj.get("elements");
		Vector<String> elementList = null;
		if(elements != null) {
			elementList = new Vector<String>();
			if(elements instanceof JsonArray) {
				JsonArray arr = (JsonArray) elements;
				for(int edx=0; edx<arr.size(); edx++) {
					elementList.add(String.valueOf(arr.get(edx)));
				}
			} else {
				String elementStr = String.valueOf(elements);
				StringTokenizer commaTokenizer = new StringTokenizer(elementStr, ",");
				while(commaTokenizer.hasMoreTokens()) {
					elementList.add(commaTokenizer.nextToken().trim());
				}
			}
		}
		
		HComboBox comp = null;
		if(elementList != null) comp = new HComboBox(elementList);
		else comp = new HComboBox();
		
		Object singleOpt = obj.get("enabled");
		if(singleOpt != null) {
			comp.setEnabled(Boolean.parseBoolean(String.valueOf(singleOpt)));
		}
		
		singleOpt = obj.get("name");
		if(singleOpt != null) {
			registerName(String.valueOf(singleOpt), comp);
		}
		
		singleOpt = obj.get("tag");
		if(singleOpt != null) {
			registerTag(String.valueOf(singleOpt), comp);
		}
		
		singleOpt = obj.get("editable");
		if(singleOpt != null) {
			comp.setEditable(Boolean.parseBoolean(String.valueOf(singleOpt)));
		}
		processSetMethods(comp, obj, "setTag", "setEnabled", "setName", "setElements", "setEditable");
		registerComponent(comp);
		return comp;
	}
	
	protected Component processRadio(JsonObject obj) {
		Object classType     = obj.get("type");
		if(! (String.valueOf(classType).trim().toLowerCase().equals("radio"))) throw new IllegalArgumentException("This is not radio type.");
		
		HRadioButton comp = new HRadioButton();
		
		Object singleOpt = obj.get("enabled");
		if(singleOpt != null) {
			comp.setEnabled(Boolean.parseBoolean(String.valueOf(singleOpt)));
		}
		
		singleOpt = obj.get("name");
		if(singleOpt != null) {
			registerName(String.valueOf(singleOpt), comp);
		}
		
		singleOpt = obj.get("tag");
		if(singleOpt != null) {
			registerTag(String.valueOf(singleOpt), comp);
		}
		
		singleOpt = obj.get("text");
		if(singleOpt != null) {
			comp.setText(String.valueOf(singleOpt));
		}
		
		singleOpt = obj.get("group");
		if(singleOpt != null) {
			String groupName = String.valueOf(singleOpt);
			ButtonGroup group = null;
			if(namedComponents.containsKey(groupName)) {
				group = (ButtonGroup) namedComponents.get(groupName);
			} else {
				group = new ButtonGroup();
				registerName(groupName, group);
				registerComponent(group);
			}
			group.add(comp);
		}
		processSetMethods(comp, obj, "setTag", "setEnabled", "setName", "setText", "setButtonGroup");
		registerComponent(comp);
		return comp;
	}
	
	protected Component processCheckBox(JsonObject obj) {
		Object classType     = obj.get("type");
		if(! (String.valueOf(classType).trim().toLowerCase().equals("checkbox"))) throw new IllegalArgumentException("This is not checkbox type.");
		
		HCheckBox comp = new HCheckBox();
		
		Object singleOpt = obj.get("enabled");
		if(singleOpt != null) {
			comp.setEnabled(Boolean.parseBoolean(String.valueOf(singleOpt)));
		}
		
		singleOpt = obj.get("name");
		if(singleOpt != null) {
			registerName(String.valueOf(singleOpt), comp);
		}
		
		singleOpt = obj.get("tag");
		if(singleOpt != null) {
			registerTag(String.valueOf(singleOpt), comp);
		}
		
		singleOpt = obj.get("text");
		if(singleOpt != null) {
			comp.setText(String.valueOf(singleOpt));
		}
		processSetMethods(comp, obj, "setTag", "setEnabled", "setName", "setText");
		registerComponent(comp);
		return comp;
	}
	
	protected Component processList(JsonObject obj) {
		Object classType     = obj.get("type");
		if(! (String.valueOf(classType).trim().toLowerCase().equals("list"))) throw new IllegalArgumentException("This is not list type.");
		
		HList comp = new HList();
		
		Object singleOpt = obj.get("enabled");
		if(singleOpt != null) {
			comp.setEnabled(Boolean.parseBoolean(String.valueOf(singleOpt)));
		}
		
		singleOpt = obj.get("name");
		if(singleOpt != null) {
			registerName(String.valueOf(singleOpt), comp);
		}
		
		singleOpt = obj.get("tag");
		if(singleOpt != null) {
			registerTag(String.valueOf(singleOpt), comp);
		}
		
		Object elements = obj.get("elements");
		Vector<String> elementList = null;
		if(elements != null) {
			elementList = new Vector<String>();
			if(elements instanceof JsonArray) {
				JsonArray arr = (JsonArray) elements;
				for(int edx=0; edx<arr.size(); edx++) {
					elementList.add(String.valueOf(arr.get(edx)));
				}
			} else {
				String elementStr = String.valueOf(elements);
				StringTokenizer commaTokenizer = new StringTokenizer(elementStr, ",");
				while(commaTokenizer.hasMoreTokens()) {
					elementList.add(commaTokenizer.nextToken().trim());
				}
			}
			comp.setListData(elementList);
		}
		processSetMethods(comp, obj, "setEnabled", "setName", "setTag", "setElements");
		registerComponent(comp);
		return processScroll(obj, comp);
	}
	
	protected Component processTextArea(JsonObject obj) {
		Object classType     = obj.get("type");
		if(! (String.valueOf(classType).trim().toLowerCase().equals("textarea"))) throw new IllegalArgumentException("This is not textarea type.");
		
		HTextArea comp = new HTextArea();
		
		Object singleOpt = obj.get("text");
		if(singleOpt != null) {
			comp.setText(String.valueOf(singleOpt));
		}
		
		singleOpt = obj.get("editable");
		if(singleOpt != null) {
			comp.setEditable(Boolean.parseBoolean(String.valueOf(singleOpt)));
		}
		
		singleOpt = obj.get("linewrap");
		if(singleOpt != null) {
			comp.setLineWrap(Boolean.parseBoolean(String.valueOf(singleOpt)));
		}
		
		singleOpt = obj.get("enabled");
		if(singleOpt != null) {
			comp.setEnabled(Boolean.parseBoolean(String.valueOf(singleOpt)));
		}
		
		singleOpt = obj.get("name");
		if(singleOpt != null) {
			registerName(String.valueOf(singleOpt), comp);
		}
		
		singleOpt = obj.get("tag");
		if(singleOpt != null) {
			registerTag(String.valueOf(singleOpt), comp);
		}
		
		processSetMethods(comp, obj, "setEnabled", "setName", "setTag", "setText", "setEditable", "setLineWrap");
		registerComponent(comp);
		return processScroll(obj, comp);
	}
	
	protected HSpinner processNumberField(JsonObject obj) {
		Object classType     = obj.get("type");
		if(! (String.valueOf(classType).trim().toLowerCase().equals("numberfield"))) throw new IllegalArgumentException("This is not numberfield type.");
		
		HSpinner comp = new HSpinner();
		
		Object singleOpt = obj.get("value");
		if(singleOpt != null) {
			comp.setValue(parseNumber(singleOpt));
		}
		
		singleOpt = obj.get("enabled");
		if(singleOpt != null) {
			comp.setEnabled(Boolean.parseBoolean(String.valueOf(singleOpt)));
		}
		
		singleOpt = obj.get("name");
		if(singleOpt != null) {
			registerName(String.valueOf(singleOpt), comp);
		}
		
		singleOpt = obj.get("tag");
		if(singleOpt != null) {
			registerTag(String.valueOf(singleOpt), comp);
		}
		
		processSetMethods(comp, obj, "setEnabled", "setName", "setTag", "setValue", "setColumns");
		registerComponent(comp);
		return comp;
	}
	
	private Number parseNumber(Object obj) {
		if(obj instanceof Number) return (Number) obj;
		
		try {
			BigInteger intVal = new BigInteger(String.valueOf(obj));
			return new Long(intVal.longValue());
		} catch(NumberFormatException e) {
			BigDecimal doubleVal = new BigDecimal(String.valueOf(obj));
			return new Double(doubleVal.doubleValue());
		}
	}
	
	protected HTextField processTextField(JsonObject obj) {
		Object classType     = obj.get("type");
		if(! (String.valueOf(classType).trim().toLowerCase().equals("textfield"))) throw new IllegalArgumentException("This is not textfield type.");
		
		HTextField comp = new HTextField();
		
		Object singleOpt = obj.get("text");
		if(singleOpt != null) {
			comp.setText(String.valueOf(singleOpt));
		}
		
		singleOpt = obj.get("column");
		if(singleOpt != null) {
			comp.setColumns(parseInt(singleOpt));
		}
		
		singleOpt = obj.get("enabled");
		if(singleOpt != null) {
			comp.setEnabled(Boolean.parseBoolean(String.valueOf(singleOpt)));
		}
		
		singleOpt = obj.get("name");
		if(singleOpt != null) {
			registerName(String.valueOf(singleOpt), comp);
		}
		
		singleOpt = obj.get("tag");
		if(singleOpt != null) {
			registerTag(String.valueOf(singleOpt), comp);
		}
		
		processSetMethods(comp, obj, "setEnabled", "setName", "setTag", "setText", "setColumns");
		registerComponent(comp);
		return comp;
	}
	
	protected HButton processButton(JsonObject obj) {
		Object classType     = obj.get("type");
		if(! (String.valueOf(classType).trim().toLowerCase().equals("button"))) throw new IllegalArgumentException("This is not button type.");
		
		HButton button = new HButton();
		
		Object singleOpt = obj.get("text");
		if(singleOpt != null) {
			button.setText(String.valueOf(singleOpt));
		}
		
		singleOpt = obj.get("enabled");
		if(singleOpt != null) {
			button.setEnabled(Boolean.parseBoolean(String.valueOf(singleOpt)));
		}
		
		singleOpt = obj.get("name");
		if(singleOpt != null) {
			registerName(String.valueOf(singleOpt), button);
		}
		
		singleOpt = obj.get("tag");
		if(singleOpt != null) {
			registerTag(String.valueOf(singleOpt), button);
		}
		
		processSetMethods(button, obj, "setEnabled", "setName", "setTag", "setText");
		registerComponent(button);
		return button;
	}
	
	protected HLabel processLabel(JsonObject obj) {
		Object classType     = obj.get("type");
		if(! (String.valueOf(classType).trim().toLowerCase().equals("label"))) throw new IllegalArgumentException("This is not label type.");
		
		HLabel label = new HLabel();
		
		Object singleOpt = obj.get("text");
		if(singleOpt != null) {
			label.setText(String.valueOf(singleOpt));
		}
		
		singleOpt = obj.get("enabled");
		if(singleOpt != null) {
			label.setEnabled(Boolean.parseBoolean(String.valueOf(singleOpt)));
		}
		
		singleOpt = obj.get("name");
		if(singleOpt != null) {
			registerName(String.valueOf(singleOpt), label);
		}
		
		singleOpt = obj.get("tag");
		if(singleOpt != null) {
			registerTag(String.valueOf(singleOpt), label);
		}
		
		processSetMethods(label, obj, "setEnabled", "setName", "setTag", "setText");
		registerComponent(label);
		return label;
	}
	
	protected Component processTabPane(JsonObject obj) {
		Object classType     = obj.get("type");
		if(! (String.valueOf(classType).trim().toLowerCase().equals("tab"))) throw new IllegalArgumentException("This is not tab type.");
		
		HTabbedPane tab = new HTabbedPane();
		
		Object singleOpt = obj.get("enabled");
		if(singleOpt != null) {
			tab.setEnabled(Boolean.parseBoolean(String.valueOf(singleOpt)));
		}
		
		singleOpt = obj.get("name");
		if(singleOpt != null) {
			registerName(String.valueOf(singleOpt), tab);
		}
		
		singleOpt = obj.get("tag");
		if(singleOpt != null) {
			registerTag(String.valueOf(singleOpt), tab);
		}
		
		JsonObject elements = (JsonObject) obj.get("elements");
		if(elements != null) {
			Set<String> keys = elements.keySet();
			for(String k : keys) {
				tab.add(k, (Component) elements.get(k));
			}
		}
		processSetMethods(tab, obj, "setEnabled", "setName", "setTag", "setElements");
		registerComponent(tab);
		return processScroll(obj, tab);
	}
	
	protected HDialog processDialog(JsonObject obj) {
		return processDialog(obj, null);
	}
	
	protected HDialog processDialog(JsonObject obj, Window parent) {
		Object classType     = obj.get("type");
		if(! (String.valueOf(classType).trim().toLowerCase().equals("dialog"))) throw new IllegalArgumentException("This is not dialog type.");
		
		boolean isModal = false;
		
		HDialog dialog = null;
		Object singleOpt;
		
		if(parent != null) {
			singleOpt = obj.get("modal");
			
			if(singleOpt != null) {
				isModal = DataUtil.parseBoolean(singleOpt);
				if(parent instanceof JDialog) dialog = new HDialog((JDialog) parent, isModal);
				else if(parent instanceof JFrame) dialog = new HDialog((JFrame) parent, isModal);
			} else {
				dialog = new HDialog();
			}
		} else {
			dialog = new HDialog();
		}
		
		singleOpt = obj.get("child");
		if(singleOpt != null) {
			processDialog((JsonObject) singleOpt, dialog);
		}
		
		Object layoutOpt = obj.get("layout");
		if(layoutOpt != null) {
			if(layoutOpt instanceof JsonObject) {
				processLayout((JsonObject) layoutOpt, dialog);
			} else {
				String layoutStr = String.valueOf(layoutOpt);
				layoutStr = layoutStr.trim().toLowerCase();
				
				if(layoutStr.equals("border")) {
					Object layoutManager = new BorderLayout();
					dialog.setLayout((BorderLayout) layoutManager);
					registerComponent(layoutManager);
				}
				
				if(layoutStr.equals("flow")) {
					Object layoutManager = new FlowLayout();
					dialog.setLayout((FlowLayout) layoutManager);
					registerComponent(layoutManager);
				}
				
				if(layoutStr.equals("card")) {
					Object layoutManager = new CardLayout();
					dialog.setLayout((CardLayout) layoutManager);
					registerComponent(layoutManager);
				}
				
				if(layoutStr.equals("grid")) {
					Object rowOpt = obj.get("layout_row");
					Object colOpt = obj.get("layout_col");
					
					if(rowOpt == null) rowOpt = new Integer(1);
					if(colOpt == null) colOpt = new Integer(1);
					
					int row = parseInt(rowOpt);
					int col = parseInt(rowOpt);
					
					GridLayout layoutManager = new GridLayout(row, col);
					dialog.setLayout((GridLayout) layoutManager);
					registerComponent(layoutManager);
				}
				if(layoutStr.equals("none")) {
					dialog.setLayout(null);
				}
			}
		}
		
		singleOpt = obj.get("name");
		if(singleOpt != null) {
			registerName(String.valueOf(singleOpt), dialog);
		}
		
		singleOpt = obj.get("tag");
		if(singleOpt != null) {
			registerTag(String.valueOf(singleOpt), dialog);
		}
		
		singleOpt = obj.get("element");
		if(singleOpt != null) {
			dialog.add(process((JsonObject) singleOpt));
		}
		
		processSetMethods(dialog, obj, "setLayout", "setName", "setTag", "setElement");
		registerComponent(dialog);
		
		return dialog;
	}
	
	protected Component processPanel(JsonObject obj) {
		Object classType     = obj.get("type");
		if(! (String.valueOf(classType).trim().toLowerCase().equals("panel"))) throw new IllegalArgumentException("This is not panel type.");
		
		HPanel panel = new HPanel();
		
		Object layoutOpt = obj.get("layout");
		if(layoutOpt != null) {
			if(layoutOpt instanceof JsonObject) {
				processLayout((JsonObject) layoutOpt, panel);
			} else {
				String layoutStr = String.valueOf(layoutOpt);
				layoutStr = layoutStr.trim().toLowerCase();
				
				if(layoutStr.equals("border")) {
					Object layoutManager = new BorderLayout();
					panel.setLayout((BorderLayout) layoutManager);
					registerComponent(layoutManager);
				}
				
				if(layoutStr.equals("flow")) {
					Object layoutManager = new FlowLayout();
					panel.setLayout((FlowLayout) layoutManager);
					registerComponent(layoutManager);
				}
				
				if(layoutStr.equals("card")) {
					Object layoutManager = new CardLayout();
					panel.setLayout((CardLayout) layoutManager);
					registerComponent(layoutManager);
				}
				
				if(layoutStr.equals("grid")) {
					Object rowOpt = obj.get("layout_row");
					Object colOpt = obj.get("layout_col");
					
					if(rowOpt == null) rowOpt = new Integer(1);
					if(colOpt == null) colOpt = new Integer(1);
					
					int row = parseInt(rowOpt);
					int col = parseInt(rowOpt);
					
					GridLayout layoutManager = new GridLayout(row, col);
					panel.setLayout((GridLayout) layoutManager);
					registerComponent(layoutManager);
				}
				if(layoutStr.equals("none")) {
					panel.setLayout(null);
				}
			}
		}
		
		Object singleOpt = obj.get("enabled");
		if(singleOpt != null) {
			panel.setEnabled(Boolean.parseBoolean(String.valueOf(singleOpt)));
		}
		
		singleOpt = obj.get("name");
		if(singleOpt != null) {
			registerName(String.valueOf(singleOpt), panel);
		}
		
		singleOpt = obj.get("tag");
		if(singleOpt != null) {
			registerTag(String.valueOf(singleOpt), panel);
		}
		
		
		Object elementObj = obj.get("elements");
		JsonInstance elements = null;
		if(elementObj instanceof JsonInstance) elements = (JsonInstance) elementObj;
		else elements = (JsonInstance) DataUtil.parseJson(String.valueOf(elementObj));
		if(elements != null) {
			if(elements instanceof JsonObject) {
				JsonObject elemObj = (JsonObject) elements;
				Set<String> elemKeys = elemObj.keySet();
				for(String k : elemKeys) {
					String keyTrim = k.trim().toLowerCase();
					if(keyTrim.equals("south") || keyTrim.equals("bottom")) {
						panel.add(process((JsonObject) elemObj.get(k)), BorderLayout.SOUTH);
					}
					if(keyTrim.equals("north") || keyTrim.equals("top")) {
						panel.add(process((JsonObject) elemObj.get(k)), BorderLayout.NORTH);
					}
					if(keyTrim.equals("west") || keyTrim.equals("left")) {
						panel.add(process((JsonObject) elemObj.get(k)), BorderLayout.WEST);
					}
					if(keyTrim.equals("east") || keyTrim.equals("right")) {
						panel.add(process((JsonObject) elemObj.get(k)), BorderLayout.EAST);
					}
					if(keyTrim.equals("center")) {
						panel.add(process((JsonObject) elemObj.get(k)), BorderLayout.CENTER);
					}
				}
			} else if(elements instanceof JsonArray) {
				JsonArray arr = (JsonArray) elements;
				for(int edx=0; edx<arr.size(); edx++) {
					Component comp = process((JsonObject) arr.get(edx));
					if(comp instanceof HDialog) continue;
					panel.add(comp);
				}
			}
		}
		
		Object elementSouthObj = obj.get("element_south");
		JsonObject elementSouth = null;
		if(elementSouthObj instanceof JsonInstance) elementSouth = (JsonObject) elementSouthObj;
		else elementSouth = (JsonObject) DataUtil.parseJson(String.valueOf(elementSouthObj));
		if(elementSouth != null) {
			panel.add(process(elementSouth), BorderLayout.SOUTH);
		}
		
		elementSouthObj = obj.get("element_bottom");
		elementSouth = null;
		if(elementSouthObj instanceof JsonInstance) elementSouth = (JsonObject) elementSouthObj;
		else elementSouth = (JsonObject) DataUtil.parseJson(String.valueOf(elementSouthObj));
		if(elementSouth != null) {
			panel.add(process(elementSouth), BorderLayout.SOUTH);
		}
		
		Object elementNorthObj = obj.get("element_north");
		JsonObject elementNorth = null;
		if(elementNorthObj instanceof JsonInstance) elementNorth = (JsonObject) elementNorthObj;
		else elementNorth = (JsonObject) DataUtil.parseJson(String.valueOf(elementNorthObj));
		if(elementNorth != null) {
			panel.add(process(elementNorth), BorderLayout.NORTH);
		}
		
		elementNorthObj = obj.get("element_top");
		elementNorth = null;
		if(elementNorthObj instanceof JsonInstance) elementNorth = (JsonObject) elementNorthObj;
		else elementNorth = (JsonObject) DataUtil.parseJson(String.valueOf(elementNorthObj));
		if(elementNorth != null) {
			panel.add(process(elementNorth), BorderLayout.NORTH);
		}
		
		Object elementEastObj = obj.get("element_east");
		JsonObject elementEast = null;
		if(elementEastObj instanceof JsonInstance) elementEast = (JsonObject) elementEastObj;
		else elementEast = (JsonObject) DataUtil.parseJson(String.valueOf(elementEastObj));
		if(elementEast != null) {
			panel.add(process(elementEast), BorderLayout.EAST);
		}
		
		elementEastObj = obj.get("element_right");
		elementEast = null;
		if(elementEastObj instanceof JsonInstance) elementEast = (JsonObject) elementEastObj;
		else elementEast = (JsonObject) DataUtil.parseJson(String.valueOf(elementEastObj));
		if(elementEast != null) {
			panel.add(process(elementEast), BorderLayout.EAST);
		}
		
		Object elementWestObj = obj.get("element_west");
		JsonObject elementWest = null;
		if(elementWestObj instanceof JsonInstance) elementWest = (JsonObject) elementWestObj;
		else elementWest = (JsonObject) DataUtil.parseJson(String.valueOf(elementWestObj));
		if(elementWest != null) {
			panel.add(process(elementWest), BorderLayout.WEST);
		}
		
		elementWestObj = obj.get("element_left");
		elementWest = null;
		if(elementWestObj instanceof JsonInstance) elementWest = (JsonObject) elementWestObj;
		else elementWest = (JsonObject) DataUtil.parseJson(String.valueOf(elementWestObj));
		if(elementWest != null) {
			panel.add(process(elementWest), BorderLayout.WEST);
		}
		
		
		Object elementCenterObj = obj.get("element_center");
		JsonObject elementCenter = null;
		if(elementCenterObj instanceof JsonInstance) elementCenter = (JsonObject) elementCenterObj;
		else elementCenter = (JsonObject) DataUtil.parseJson(String.valueOf(elementCenterObj));
		if(elementCenter != null) {
			panel.add(process(elementCenter), BorderLayout.CENTER);
		}
		
		processSetMethods(panel, obj, "setLayout", "setEnabled", "setName", "setTag", "setElements");
		registerComponent(panel);
		
		return processScroll(obj, panel);
	}
	
	protected void processLayout(JsonObject layoutJSONObject, Container panel) {
		Object classType     = layoutJSONObject.get("type");
		if(! (String.valueOf(classType).trim().toLowerCase().equals("layout"))) throw new IllegalArgumentException("This is not layout type.");
		
		Object nameOpt = layoutJSONObject.get("name");
		String name = null;
		if(nameOpt != null) {
			name = String.valueOf(nameOpt);
		}
		
		nameOpt = layoutJSONObject.get("tag");
		String tag = null;
		if(nameOpt != null) {
			tag = String.valueOf(nameOpt);
		}
		
		String layoutTypeOpt = String.valueOf(layoutJSONObject.get("layout"));
		layoutTypeOpt = layoutTypeOpt.trim().toLowerCase();
		if(layoutTypeOpt.equals("border")) {
			Object layoutManager = new BorderLayout();
			panel.setLayout((BorderLayout) layoutManager);
			registerComponent(layoutManager);
			if(name != null) registerName(name, layoutManager);
			if(tag  != null) registerTag(tag  , layoutManager);
		}
		else if(layoutTypeOpt.equals("flow")) {
			Object alignOpt = layoutJSONObject.get("align");
			int alignCode = -1;
			if(alignOpt != null) {
				String aligns = String.valueOf(alignOpt).trim().toLowerCase();
				if(aligns.equals("left") || aligns.equals("west")) {
					alignCode = FlowLayout.LEFT;
				}
				if(aligns.equals("center")) {
					alignCode = FlowLayout.CENTER;
				}
				if(aligns.equals("right") || aligns.equals("east")) {
					alignCode = FlowLayout.RIGHT;
				}
			}
			
			Object layoutManager = null;
			if(alignCode != -1) layoutManager = new FlowLayout(alignCode);
			else layoutManager = new FlowLayout();
			panel.setLayout((FlowLayout) layoutManager);
			registerComponent(layoutManager);
			if(name != null) registerName(name, layoutManager);
			if(tag  != null) registerTag(tag  , layoutManager);
		}
		else if(layoutTypeOpt.equals("card")) {
			Object layoutManager = new CardLayout();
			panel.setLayout((CardLayout) layoutManager);
			registerComponent(layoutManager);
			if(name != null) registerName(name, layoutManager);
			if(tag  != null) registerTag(tag  , layoutManager);
		}
		else if(layoutTypeOpt.equals("grid")) {
			Object rowOpt = layoutJSONObject.get("row");
			Object colOpt = layoutJSONObject.get("col");
			
			if(rowOpt == null) rowOpt = new Integer(1);
			if(colOpt == null) colOpt = new Integer(1);
			
			int row = parseInt(rowOpt);
			int col = parseInt(rowOpt);
			
			GridLayout layoutManager = new GridLayout(row, col);
			panel.setLayout((GridLayout) layoutManager);
			registerComponent(layoutManager);
			if(name != null) registerName(name, layoutManager);
			if(tag  != null) registerTag(tag  , layoutManager);
		}
		else if(layoutTypeOpt.equals("none")) {
			panel.setLayout(null);
		}
		else {
			throw new IllegalArgumentException("Cannot find layout type.");
		}
	}
	
	protected Component processScroll(JsonObject compJsonObj, Component targetComponent) {
		boolean needScroll = false;
		int horizontalOpt = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER;
		int verticalOpt   = JScrollPane.VERTICAL_SCROLLBAR_NEVER;
		
		Object singleOpt = compJsonObj.get("scroll_x");
		if(singleOpt != null) {
			String singleOptVal = String.valueOf(singleOpt).trim().toLowerCase();
			if(singleOptVal.equals("as_needed")) {
				horizontalOpt = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED;
				needScroll = true;
			}
			if(singleOptVal.equals("always")) {
				horizontalOpt = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS;
				needScroll = true;
			}
		}
		
		singleOpt = compJsonObj.get("scroll_y");
		if(singleOpt != null) {
			String singleOptVal = String.valueOf(singleOpt).trim().toLowerCase();
			if(singleOptVal.equals("as_needed")) {
				verticalOpt = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED;
				needScroll = true;
			}
			if(singleOptVal.equals("always")) {
				verticalOpt = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
				needScroll = true;
			}
		}
		
		if(needScroll) {
			JScrollPane pane = new JScrollPane(targetComponent, verticalOpt, horizontalOpt);
			registerComponent(pane);
			return pane;
		}
		return targetComponent;
	}
	
	protected void processSetMethods(Object target, JsonObject jsonObj, String ... exceptMethods) {
		List<String> exceptList = new ArrayList<String>();
		if(exceptMethods != null) {
			for(String s : exceptMethods) {
				exceptList.add(s);
			}
		}
		
		Method[] methods = target.getClass().getMethods();
		
		for(Method m : methods) {
    		if(Modifier.isPublic(m.getModifiers())) {
    			try {
    				String methodName = m.getName();
        			if(! methodName.startsWith("set")) continue;
        			if(methodName.equals("set")) continue;
        			if(exceptList.contains(methodName)) continue;
        			
        			String newMethodName = ClassUtil.camelSetMethodToUnderbarWords(methodName);
        			if(jsonObj.get(newMethodName.toString()) == null) continue;
        			
        			Class<?>[] paramClasses = m.getParameterTypes();
        			if(paramClasses == null) continue;
        			if(paramClasses.length == 0) continue;
        			if(paramClasses.length >= 3) continue;
        			
        			if(paramClasses.length == 1) {
        				Class<?> paramClass = paramClasses[0];
        				
        				if(paramClass.getName().equals("java.lang.String") || paramClass.getName().equals("java.lang.CharSequence"))
            				m.invoke(target, String.valueOf(jsonObj.get(newMethodName.toString())));
            			else if(paramClass.getName().equals("java.lang.Integer"))
            				m.invoke(target, new BigDecimal(String.valueOf(jsonObj.get(newMethodName.toString()))).intValue());
            			else if(paramClass.getName().equals("java.lang.Double"))
            				m.invoke(target, new BigDecimal(String.valueOf(jsonObj.get(newMethodName.toString()))).doubleValue());
            			else if(paramClass.getName().equals("java.lang.Long"))
            				m.invoke(target, new BigDecimal(String.valueOf(jsonObj.get(newMethodName.toString()))).longValue());
            			else if(paramClass.getName().equals("java.lang.Boolean"))
            				m.invoke(target, DataUtil.parseBoolean(jsonObj.get(newMethodName.toString())));
            			else 
            				m.invoke(target, jsonObj.get(newMethodName.toString()));
        			} else {
        				Class<?> paramOneClass = paramClasses[0];
        				Class<?> paramTwoClass = paramClasses[1];
        				Object paramOne = null;
        				Object paramTwo = null;
        				
        				StringTokenizer commaTokenizer = new StringTokenizer(String.valueOf(jsonObj.get(newMethodName.toString())), ",");
        				String valueOne = commaTokenizer.nextToken().trim();
        				String valueTwo = commaTokenizer.nextToken().trim();
        				
        				if(paramOneClass.getName().equals("java.lang.String") || paramOneClass.getName().equals("java.lang.CharSequence"))
        					paramOne = valueOne;
            			else if(paramOneClass.getName().equals("java.lang.Integer"))
            				paramOne = new BigDecimal(valueOne).intValue();
            			else if(paramOneClass.getName().equals("java.lang.Double"))
            				paramOne = new BigDecimal(valueOne).doubleValue();
            			else if(paramOneClass.getName().equals("java.lang.Long"))
            				paramOne = new BigDecimal(valueOne).longValue();
            			else if(paramOneClass.getName().equals("java.lang.Boolean"))
            				paramOne = DataUtil.parseBoolean(jsonObj.get(newMethodName.toString()));
            			else 
            				paramOne = valueOne;
        				
        				if(paramTwoClass.getName().equals("java.lang.String") || paramTwoClass.getName().equals("java.lang.CharSequence"))
        					paramTwo = valueTwo;
            			else if(paramTwoClass.getName().equals("java.lang.Integer"))
            				paramTwo = new BigDecimal(valueTwo).intValue();
            			else if(paramTwoClass.getName().equals("java.lang.Double"))
            				paramTwo = new BigDecimal(valueTwo).doubleValue();
            			else if(paramTwoClass.getName().equals("java.lang.Long"))
            				paramTwo = new BigDecimal(valueTwo).longValue();
            			else if(paramTwoClass.getName().equals("java.lang.Boolean"))
            				paramTwo = DataUtil.parseBoolean(jsonObj.get(newMethodName.toString()));
            			else 
            				paramTwo = valueTwo;
        				
        				m.invoke(target, paramOne, paramTwo);
        			}
    			} catch(Throwable t) {
    				Core.logError(t);
    			}
    		}
    	}
	}
	
	@SuppressWarnings("unchecked")
	protected Component processObject(JsonObject obj) {
		Object classType = obj.get("type");
		
		try {
			Class<? extends Component> compClass = (Class<? extends Component>) Class.forName(String.valueOf(classType));
			Component comp = compClass.newInstance();
			
			processSetMethods(comp, obj);
	    	return comp;
		} catch(Throwable t) {
			throw new RuntimeException(t);
		}
	}
}
