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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import hjow.common.core.Core;
import hjow.common.core.Releasable;
import hjow.common.module.CustomAbstractModule;
import hjow.common.module.HasShortcut;
import hjow.common.module.Module;
import hjow.common.module.builtin.BuiltinModule;
import hjow.common.ui.extend.HButton;
import hjow.common.ui.extend.HDesktopPane;
import hjow.common.ui.extend.HDialog;
import hjow.common.ui.extend.HFrame;
import hjow.common.ui.extend.HPanel;
import hjow.common.ui.extend.HTextArea;
import hjow.common.ui.module.ModuleDialog;
import hjow.common.util.GUIUtil;

public class SwingUI implements UI, ActionListener {
    private static final long serialVersionUID = -8275480990775585786L;
    protected Core core;
    
    protected HFrame frame;
    protected HDesktopPane desktopPane;
    protected WindowAdapter windowEventHandler;
    
    protected JFileChooser moduleChooser;
    
    protected Map<Long, ModuleDialog> dialogs;
    protected Map<Long, JInternalFrame> internalFrames;
    protected Map<Long, JMenuItem> menuItems;
    protected List<JInternalFrame> alreadyAdded;
    protected List<Module> modules;
    
    protected JMenuBar menuBar;
    protected JMenu mFile, mRun;
    protected JMenuItem mFileExit, mFileRestart, mFileLog, mFileOption, mFileLoadModule;
    
    protected LogFrame logFrame;
    protected PropertiesFrame propFrame;
    protected ModuleLoadAskDialog moduleLoadAsker;
    
    protected transient List<String> logQueue = new Vector<String>();

    @Override
    public void releaseResource() {
        frame.setVisible(false);
        frame.removeWindowListener(windowEventHandler);
        windowEventHandler = null;
        
        Set<Long> keysOf = menuItems.keySet();
        for(Long k : keysOf) {
            menuItems.get(k).removeActionListener(this);
        }
        menuItems.clear();
        
        mFileExit.removeActionListener(this);
        mFileLog.removeActionListener(this);
        mFileOption.removeActionListener(this);
        mFileLoadModule.removeActionListener(this);
        
        desktopPane.removeAll();
        dialogs.clear();
        internalFrames.clear();
        alreadyAdded.clear();
        modules.clear();
        this.core = null;
    }

    @Override
    public void init(Core core) {
        this.core = core;
        dialogs = new HashMap<Long, ModuleDialog>();
        internalFrames = new HashMap<Long, JInternalFrame>();
        menuItems = new HashMap<Long, JMenuItem>();
        alreadyAdded = new ArrayList<JInternalFrame>();
        modules = new ArrayList<Module>();
        
        String lookAndFeelOpt = Core.getProperty("look_and_feel");
        GUIUtil.setLookAndFeel(lookAndFeelOpt);
        GUIUtil.prepareFont();
        
        frame = new HFrame();
        frame.setTitle(core.getAppName());
        GUIUtil.stretchWindow(frame);
        GUIUtil.centerWindow(frame);

        frame.setLayout(new BorderLayout());
        
        desktopPane = new HDesktopPane();
        frame.add(desktopPane, BorderLayout.CENTER);
        
        moduleChooser = new JFileChooser();
        setFileFilter(moduleChooser, core.getModulePath());
        
        moduleLoadAsker = new ModuleLoadAskDialog(frame);
        
        menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        
        logFrame = new LogFrame();
        propFrame = new PropertiesFrame();
        
        prepareMenus();
        
        windowEventHandler = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Core.exit();
            }
        };
        frame.addWindowListener(windowEventHandler);
        
        showInternalFrame(logFrame);
        showInternalFrame(propFrame);
        
        logFrame.setVisible(false);
        propFrame.setVisible(false);
        
        refreshStyle();
        initMores();
    }
    
    /** 메뉴 파트를 초기화합니다. */
    protected void prepareMenus() {
    	mFile = new JMenu(Core.trans("File"));
        mRun  = new JMenu(Core.trans("Run"));
        
        menuBar.add(mFile);
        menuBar.add(mRun);
        
        mFileLoadModule = new JMenuItem(Core.trans("Load Module"));
        mFile.add(mFileLoadModule);
        
        mFileLoadModule.addActionListener(this);
        
        mFile.addSeparator();
        
        mFileOption = new JMenuItem(Core.trans("Configuration"));
        mFile.add(mFileOption);
        
        mFileOption.addActionListener(this);
        
        mFileLog = new JMenuItem(Core.trans("Log Viewer"));
        mFile.add(mFileLog);
        
        mFileLog.addActionListener(this);
        
        mFile.addSeparator();
        
        mFileRestart = new JMenuItem(Core.trans("Restart"));
        mFile.add(mFileRestart);
        
        mFileExit = new JMenuItem(Core.trans("Exit"));
        mFileExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
        mFile.add(mFileExit);
        
        mFileRestart.addActionListener(this);
        mFileExit.addActionListener(this);
    }
    
    /** 이 메소드는 초기화 후반부에 호출됩니다. 이 SwingUI 클래스를 상속받아 사용할 때 이 메소드를 오버라이드해 사용합니다. */
    protected void initMores() {}
    
    /** 스타일을 다시 적용합니다. */
    public void refreshStyle() {
    	String opacityOpt = Core.getProperty("ui_opacity");
    	if(opacityOpt == null) opacityOpt = "0.9F";
    	
    	float opacity = Float.parseFloat(opacityOpt);
    	frame.setAlphaRatio(opacity);
    	
    	if(opacity < 0.7F || opacity > 0.9F) desktopPane.setAlphaRatio(opacity);
    }

    @Override
    public void show() {
        frame.setVisible(true);
    }

    @Override
    public void attach(Module m) {
        modules.add(m);
        
        if(m.getComponentType() == Module.DESKTOP) {
            createNewInternalFrame(m);
        }
        
        if(m.getComponentType() == Module.DIALOG) {
        	createNewDialog(m);
        }
        
        if(m.getComponentType() != Module.NONE) {
            JMenuItem newMenuItem = new JMenuItem(m.getName());
            newMenuItem.setToolTipText(Core.trans(m.getDescription()));
            newMenuItem.addActionListener(this);
            
            if(m instanceof HasShortcut) {
                HasShortcut shortcutIns = (HasShortcut) m;
                if(shortcutIns.hasShortcut()) {
                    newMenuItem.setAccelerator(KeyStroke.getKeyStroke(shortcutIns.getShortcutKey().intValue(), shortcutIns.getShortcutMask().intValue()));
                }
            }
            
            mRun.add(newMenuItem);
            menuItems.put(new Long(m.getId()), newMenuItem);
        }
        
        m.initSecond(this);
    }
    
    protected ModuleDialog createNewDialog(Module m) {
    	ModuleDialog dialog = new ModuleDialog(m);
    	dialogs.put(new Long(m.getId()), dialog);
    	return dialog;
    }
    
    protected JInternalFrame createNewInternalFrame(Module m) {
        JInternalFrame newFrame = new JInternalFrame(m.getName(), true, true, true, true);
        
        int defaultWidth = 450;
        int defaultHeight = 350;
        
        int width = defaultWidth;
        int height = defaultHeight;
        
        int deskWidth  = desktopPane.getWidth()  - 100;
        int deskHeight = desktopPane.getHeight() - 100;
        
        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        int scrWidth    = (int) (scrSize.getWidth()  / 4.0);
        int scrHeight   = (int) (scrSize.getHeight() / 4.0);
        
        if(width  < scrWidth)   width  = scrWidth;
        if(height < scrHeight)  height = scrHeight;
        
        if(width  < deskWidth)  width  = deskWidth;
        if(height < deskHeight) height = deskHeight;
        
        newFrame.setSize(width, height);
        
        newFrame.setLayout(new BorderLayout());
        newFrame.setTitle(m.getName());
        newFrame.add(m.getComponent(), BorderLayout.CENTER);
        
        internalFrames.put(new Long(m.getId()), newFrame);
        
        if(m instanceof BuiltinModule) ((BuiltinModule) m).initFrame(newFrame);
        return newFrame;
    }

    @Override
    public void remove(Module m) {
        Long moduleId = new Long(m.getId());
        if(internalFrames.containsKey(moduleId)) {
            JInternalFrame moduleFrame = internalFrames.get(moduleId);
            try { 
                if(alreadyAdded.contains(moduleFrame)) {
                    desktopPane.remove(moduleFrame);
                    alreadyAdded.remove(moduleFrame);
                }
            } catch(Throwable t) {}
            internalFrames.remove(moduleId);
        }
        if(menuItems.containsKey(moduleId)) {
            mRun.remove(menuItems.get(moduleId));
            menuItems.remove(moduleId);
        }
        
        modules.remove(m);
    }
    
    /** 모듈 ID 를 통해 모듈 객체를 찾습니다. */
    protected Module findModule(long moduleId) {
        for(Module m : modules) {
            if(m.getId() == moduleId) {
                return m;
            }
        }
        return null;
    }
    
    /** 모듈을 실행합니다. */
    public void callModule(Module m) {
    	if(m.getComponentType() == Module.NONE) return;
        if(m.getComponentType() == Module.DESKTOP) {
            JInternalFrame moduleFrame = internalFrames.get(new Long(m.getId()));
            if(moduleFrame.isClosed()) {
                moduleFrame.removeAll();
                internalFrames.remove(new Long(m.getId()));
                moduleFrame = createNewInternalFrame(m);
                m.initSecond(this);
            }
            showInternalFrame(moduleFrame);
        } else if(m.getComponentType() == Module.DIALOG) {
        	ModuleDialog moduleDialog = dialogs.get(new Long(m.getId()));
        	moduleDialog.showFromUI(this);
        }
        
        m.run();
    }
    
    protected void showInternalFrame(JInternalFrame f) {
        if(! alreadyAdded.contains(f))
            desktopPane.add(f);
        alreadyAdded.add(f);
        f.setVisible(true);
    }
    
    @Override
    public void alert(String contents) {
        JOptionPane.showMessageDialog(frame, Core.trans(contents));
    }
    
    @Override
    public boolean askConfirm(String contents) {
        int selection = JOptionPane.showConfirmDialog(frame, Core.trans(contents), "", JOptionPane.YES_NO_OPTION);
        return selection == JOptionPane.YES_OPTION;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        
        Set<Long> keys = menuItems.keySet();
        for(Long keysOf : keys) {
            if(obj == menuItems.get(keysOf)) {
                Module m = findModule(keysOf.longValue());
                callModule(m);
                return;
            }
        }
        
        if(obj == mFileExit) {
            if(askConfirm("Do you want to close the application?")) Core.exit();
            return;
        }
        
        if(obj == mFileRestart) {
        	if(askConfirm("Do you want to restart the application?")) Core.restart();
            return;
        }
        
        if(obj == mFileLog) {
        	logFrame.setVisible(true);
        }
        
        if(obj == mFileOption) {
        	propFrame.setVisible(true);
        }
        
        if(obj == mFileLoadModule) {
        	SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					try {
						loadModuleAction();
					} catch(Throwable t) {
						alert(Core.trans("Error") + " : " + t.getMessage());
						Core.logError(t);
					}
				}
			});
        }
    }
    
    protected void loadModuleAction() {
    	int sel = moduleChooser.showOpenDialog(getFrame());
    	if(sel != JFileChooser.APPROVE_OPTION) return;
    	
    	File f = moduleChooser.getSelectedFile();
    	final CustomAbstractModule m = core.readModuleFromFile(f);
    	
    	moduleLoadAsker.show(m, new Runnable() {
			@Override
			public void run() {
				try {
					core.loadModuleOnRuntime(m, true);
				} catch(Throwable t) {
					alert(Core.trans("Error") + " : " + t.getMessage());
					Core.logError(t);
				}
			}
		});
    }

    @Override
    public Window getFrame() {
        return frame;
    }

    @Override
    public synchronized void log(String str) {
        if(logFrame != null) {
        	if(logQueue != null && logQueue.size() >= 1) {
        		for(String s : logQueue) {
        			logFrame.log(s);
        		}
        		logQueue.clear();
        	}
        	logFrame.log(str);
        } else {
        	if(logQueue == null) logQueue = new Vector<String>();
        	if(logQueue.size() >= 100) {
        		logQueue.remove(0);
        	}
        	logQueue.add(str);
        }
    }
    
    public static void setFileFilter(JFileChooser chooser, String modulePath) {
    	if(modulePath == null) chooser = new JFileChooser();
    	else chooser = new JFileChooser(modulePath);
    	
    	chooser.setMultiSelectionEnabled(false);
		
    	FileNameExtensionFilter filter = new FileNameExtensionFilter(Core.trans("Zipped XML based module"), "zmodule");
		chooser.setFileFilter(filter);
		
		filter = new FileNameExtensionFilter(Core.trans("Zipped JSON based module"), "zjmodule");
		chooser.addChoosableFileFilter(filter);
		
		filter = new FileNameExtensionFilter(Core.trans("JSON based module"), "jmodule");
		chooser.addChoosableFileFilter(filter);
		
		filter = new FileNameExtensionFilter(Core.trans("XML based module"), "xmodule");
		chooser.addChoosableFileFilter(filter);
    }
}
class LogFrame extends JInternalFrame implements Releasable, ActionListener {
	private static final long serialVersionUID = -3497170814654174633L;
	protected HTextArea txArea;
	protected JScrollPane scroll;
	protected HPanel pnControl;
	protected HButton btClear;
	
	public LogFrame() {
		super("Log", true, false, true, true);
		setSize(400, 300);
		setLayout(new BorderLayout());
		
		txArea = new HTextArea();
		txArea.setEditable(false);
		
		scroll = new JScrollPane(txArea);
		
		add(scroll, BorderLayout.CENTER);
		
		pnControl = new HPanel();
		add(pnControl, BorderLayout.NORTH);
		
		pnControl.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		btClear = new HButton(Core.trans("Clear"));
		btClear.addActionListener(this);
		pnControl.add(btClear);
	}
	
	public void log(String str) {
		txArea.append("\n" + str);
		txArea.setCaretPosition(txArea.getDocument().getLength() - 1);
	}

	@Override
	public void releaseResource() {
		btClear.removeActionListener(this);
		txArea.setText("");
		pnControl.removeAll();
		scroll.remove(txArea);
		removeAll();
		txArea = null;
		scroll = null;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object ob = e.getSource();
		if(ob == btClear) {
			txArea.setText("");
		}
	}
}
class PropertiesFrame extends JInternalFrame implements Releasable, ActionListener {
	private static final long serialVersionUID = 4530644367476653074L;
	protected PropertiesPanel propPanel;
	protected HPanel pnControl;
	protected HButton btnSave;
	
	public PropertiesFrame() {
		super(Core.trans("Configuration"), true, false, true, true);
		setSize(400, 300);
		setLayout(new BorderLayout());
		
		propPanel = new PropertiesPanel();
		add(propPanel, BorderLayout.CENTER);
		
		pnControl = new HPanel();
		add(pnControl, BorderLayout.SOUTH);
		
		pnControl.setLayout(new FlowLayout());
		
		btnSave = new HButton(Core.trans("Save"));
		btnSave.addActionListener(this);
		pnControl.add(btnSave);
		
		Core.sendPropertiesOnComponent(propPanel);
	}
	
	@Override
	public void releaseResource() {
		propPanel = null;
		pnControl.removeAll();
		btnSave.removeActionListener(this);
		removeAll();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj == btnSave) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					Core.saveConfig(propPanel);
					setVisible(false);
				}
			});
		}
	}
	
}

class ModuleLoadAskDialog extends HDialog {
	private static final long serialVersionUID = -4920979184385202164L;
	protected ModuleViewer viewer;
	protected transient Runnable afterAction;
	public ModuleLoadAskDialog(JFrame frame) {
		super(frame);
		
		setSize(450, 350);
		setLayout(new BorderLayout());
		
		viewer = new ModuleViewer();
		add(viewer, BorderLayout.CENTER);
		
		HPanel pnControl = new HPanel();
		add(pnControl, BorderLayout.SOUTH);
		
		pnControl.setLayout(new FlowLayout());
		
		HButton btnOk = new HButton(Core.trans("Load"));
		HButton btnCancel = new HButton(Core.trans("Cancel"));
		
		pnControl.add(btnOk);
		pnControl.add(btnCancel);
		
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				act(true);
			}
		});
		
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				act(false);
			}
		});
	}
	
	public void show(Module m, Runnable afterAction) {
		this.viewer.setModule(m);
		this.afterAction = afterAction;
	}
	
	protected void act(boolean yesOrNo) {
		if(yesOrNo) {
			SwingUtilities.invokeLater(afterAction);
		}
		setVisible(false);
	}
}