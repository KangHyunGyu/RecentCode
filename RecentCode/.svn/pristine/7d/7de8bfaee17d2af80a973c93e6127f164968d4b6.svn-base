package com.e3ps.change.editor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

import wt.util.WTContext;

public class BOMManager extends JPanel
{
	private static final long serialVersionUID = -1779320152820683907L;
	
	JButton load_bt;
	JButton clear_bt;
	JButton save_bt;
    JButton undo;
    JButton redo;

    JRadioButton addChildMode;
    JRadioButton changeMode;
    JRadioButton baseCopyMode;
    
    public ButtonGroup group;

    public EditTreePanel editPanel;
    public SupportPanel supportPanel;

    public BOMManager(Component  frame)
    {
        
        System.out.println("= Simple EulB Editor =");
        System.out.println(" 2015.4");
        System.out.println(" YHJang");
         
        BEContext.setTop((EOEulBApplet)frame);
        BEContext.setModel(new DataModel());
        WTContext.init(frame);
        BEContext.handler = new EditHandler();
        
        setLayout(new BorderLayout());
        JToolBar br = new JToolBar();
        br.setFloatable(false);
        
        MenuAction ma = new MenuAction();
            
        try {
        	
            load_bt.setToolTipText("부품 또는 최종 품목을 엽니다.");
            load_bt.setMargin(new Insets(0, 0, 0, 0));
            load_bt.addActionListener(ma);
            br.add(load_bt);
            
        	clear_bt = new JButton(new ImageIcon(new URL(BEContext.host + "extcore/kores/pdm/images/bomeditor/init.png")));
        	clear_bt.setRolloverIcon(new ImageIcon(new URL(BEContext.host + "extcore/kores/pdm/images/bomeditor/init_ov.png")));
            clear_bt.setToolTipText("수정중인 부품을 닫습니다.");
            clear_bt.setMargin(new Insets(0, 0, 0, 0));
            clear_bt.addActionListener(ma);
            br.add(clear_bt);
            
			save_bt = new JButton(new ImageIcon(new URL(BEContext.host + "extcore/kores/pdm/images/bomeditor/save.png")));
			save_bt.setRolloverIcon(new ImageIcon(new URL(BEContext.host + "extcore/kores/pdm/images/bomeditor/save_ov.png")));
            save_bt.setToolTipText("편집 내용을 적용 합니다.");
            save_bt.setMargin(new Insets(0, 0, 0, 0));
            save_bt.addActionListener(ma);
            br.add(save_bt);

            br.addSeparator();
            
            undo = new JButton(new ImageIcon(new URL(BEContext.host + "extcore/kores/pdm/images/bomeditor/undo.png")));
            undo.setRolloverIcon(new ImageIcon(new URL(BEContext.host + "extcore/kores/pdm/images/bomeditor/undo_ov.png")));
            undo.setToolTipText("Undo");
            undo.setMargin(new Insets(0, 0, 0, 0));
            undo.addActionListener(ma);
            br.add(undo);

            redo = new JButton(new ImageIcon(new URL(BEContext.host + "extcore/kores/pdm/images/bomeditor/redo.png")));
            redo.setRolloverIcon(new ImageIcon(new URL(BEContext.host + "extcore/kores/pdm/images/bomeditor/redo_ov.png")));
            redo.setToolTipText("Redo");
            redo.setMargin(new Insets(0, 0, 0, 0));
            redo.addActionListener(ma);
            br.add(redo);
        } catch (MalformedURLException e) {
			e.printStackTrace();
		}

        add(br, BorderLayout.NORTH);

        JPanel conPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addChildMode = new JRadioButton("Add Child Mode");
        changeMode = new JRadioButton("Change Mode");
        baseCopyMode = new JRadioButton("Base Copy Mode");
        group = new ButtonGroup();
        group.add(addChildMode);
        group.add(changeMode);
        group.add(baseCopyMode);
        addChildMode.setSelected(true);
        conPanel.add(addChildMode);
        conPanel.add(changeMode);
        conPanel.add(baseCopyMode);

        JPanel panel = new JPanel();
        editPanel = new EditTreePanel();
        supportPanel = new SupportPanel();

        JSplitPane m_pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, editPanel, supportPanel);
        m_pane.setDividerSize(2);
        m_pane.setDividerLocation(280);
        m_pane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "e3PS BOM EDITOR"));
        panel.setLayout(new BorderLayout());
        panel.add(conPanel, BorderLayout.NORTH);
        panel.add(m_pane, BorderLayout.CENTER);
        BEStateBar bes = new BEStateBar();
        panel.add(bes, BorderLayout.SOUTH);
        add(panel, BorderLayout.CENTER);
        BEContext.setStateBar(bes);
        BEContext.setPanel(this);
    }

    public class MenuAction implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            Component c = (Component) e.getSource();
            if (c == load_bt)
            {
                try
                {
					PartSearchDialog spenel = new PartSearchDialog(BEContext.getTop(), "Part Search", BEContext.getView());
                    spenel.createDialog();
                }
                catch (Exception e1)
                {
                	e1.printStackTrace();
                    JOptionPane.showMessageDialog(BEContext.getTop(), ((Throwable) e1).getLocalizedMessage());

                }
            }
            else if( c ==clear_bt){
            	try
                {
            		if(editPanel!=null){
            			int i = JOptionPane.showOptionDialog(BEContext.getTop(), "작업중인 부품을 닫으시겠습니까?", "경고", -1, 2, null, new String[] { "Yes", "No" }, "No");
            			if (i == 1)return;
						editPanel.clearTree();
					}
                }
                catch (Exception e1)
                {
                	e1.printStackTrace();
                    JOptionPane.showMessageDialog(BEContext.getTop(), ((Throwable) e1).getLocalizedMessage());

                }
            }
            else if (c == save_bt)
            {
                try
                {
					 if (BEContext.model == null || BEContext.model.getRoot() == null)
                        return;
                    if (JOptionPane.showConfirmDialog(BEContext.getTop(),
                                                      "변경사항을 적용합니다.", "Commit",
                                                      JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                    {
                        //BEContext.saveEul();
                        //JOptionPane.showMessageDialog(BEContext.getTop() , "저장 되었습니다.");
                        
                        BEContext.top.saveChangeData();
                  
                    }
                    else
                    {
                        return;
                    }
                }
                catch (Exception e1)
                {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(BEContext.getTop(), ((Throwable) e1).getLocalizedMessage());
                }

            }
            else if (c == undo)
            {
                try
                {
                    if (BEContext.model == null || BEContext.model.getRoot() == null)
                        return;
                    BEContext.undo();
                }
                catch (Exception e1)
                {
                    e1.printStackTrace();
                }
            }
            else if (c == redo)
            {
                try
                {
                    if (BEContext.model == null || BEContext.model.getRoot() == null)
                        return;
                    BEContext.redo();
                }
                catch (Exception e1)
                {
                    e1.printStackTrace();
                }
            }
        }
    };
};