package com.e3ps.change.editor;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONObject;

import wt.util.WTContext;

public class EOEulBApplet extends JApplet
{
	private static final long serialVersionUID = 484638273983499834L;

    private static Object initSync = new Object();
    
    public void init()
    {
        WTContext.init(this);
    }
    
    public void stop()
    {
        synchronized(initSync)
        {
            WTContext.getContext(this).stop();
            super.stop();
        }
    }

    public void destroy()
    {
        synchronized(initSync)
        {
            WTContext.getContext(this).destroy(this);
            super.destroy();
        }
    }

    
    public void start()
    {
        
        synchronized(initSync)
        {
            WTContext.getContext(this).start();
            super.start();
        }
        BEContext.clear();
//        BOMManager ep = new BOMManager(this);
        Container con = getContentPane();
        con.setLayout(new BorderLayout());
//        con.add(ep);
        String bomReqOid = getParameter("bomReqOid");
        String reqType = getParameter("reqType");
        String topNumber = getParameter("topNumber");
        String topVersion = getParameter("topVersion");
        
        
        BEContext.ReqType = reqType;
        try {
        	// #. javasscript call
//        	try {
//        		
//        		
//        		
//        		
//        		JSObject window = JSObject.getWindow(this);
//            	String bomReqEntriesJson = (String)window.eval("getFormValue('bomReqEntriesJson')");
//            	BEContext.loadJonData(topNumber,topVersion,bomReqEntriesJson);
//            	
//            	
//        	} catch (JSException e) {
//        		e.printStackTrace();
//        	}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public static void openEditor(String oid)
    {
        try
        {
            JFrame frame = null;
           
            
                frame = new JFrame();
                frame.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent arg0)
                    {
                        if (JOptionPane.showConfirmDialog(BEContext.getTop(),
                                                          "종료하기전에 반드시 저장버튼을 눌러 작업 내용을 저장해 주십시오.\n이대로 종료 하시겠습니까?",
                                                          "Exit", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                        {
                            BEContext.getTop().setVisible(false);
                        }
                    }
                });

            Container con = frame.getContentPane();
//            BOMManager ep = new BOMManager(frame);
//            con.add(ep);
            frame.setSize(800, 700);
            frame.setVisible(true);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    public void saveChangeData()
    {
  
		try {
			JSONObject root = BEContext.support.preview.getChagneJson();
			String tam =root.getString("topAssyNumber");
			String tav =root.getString("topAssyVersion");
			JSONArray rows =root.getJSONArray("rows");
			
			getAppletContext().showDocument(new URL("javascript:beginEditingResult('"+tam+"','"+tav+"')"));
			
			for(int i=0; i<rows.length(); i++){
				JSONObject row = rows.getJSONObject(i);
				String jsonString = row.toJSONString();
				jsonString = jsonString.replaceAll("'", "\\\\\'");
				jsonString = jsonString.replaceAll("\"", "\\\\\"");
				getAppletContext().showDocument(new URL("javascript:addBomReqEntryRow('" + jsonString +"')"));
			}
			WTContext.getContext(this).
			getAppletContext().showDocument(new URL("javascript:endEditingResult()"));
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    

    public static void main(String[] args) throws Exception
    {
        System.out.println("start");
        wt.method.RemoteMethodServer methodServer = wt.method.RemoteMethodServer.getDefault();
        if (methodServer.getUserName() == null)
        {
            methodServer.setUserName("wcadmin");
            methodServer.setPassword("wcadmin");
        }
        
        
        BEContext.clear();
        
        openEditor(null);
    }
}