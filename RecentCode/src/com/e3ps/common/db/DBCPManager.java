//package com.e3ps.common.db;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.Enumeration;
//import java.util.Properties;
//
//import org.apache.commons.dbcp.BasicDataSource;
//import org.apache.commons.dbcp.ConnectionFactory;
//import org.apache.commons.dbcp.DataSourceConnectionFactory;
//import org.apache.commons.dbcp.PoolableConnectionFactory;
//import org.apache.commons.dbcp.PoolingDriver;
//import org.apache.commons.pool.impl.GenericObjectPool;
//
//import com.e3ps.common.log4j.Log4jPackages;
//
///**
// * DBCP를 기반으로 작성하였음. http://www.jakartaproject.com/article/jakarta/1111890409958
// */
//
//
//public class DBCPManager
//{
//	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
//    private static DBCPManager manager = null;
//
//    public static Connection getConnection(String connName)
//    {
//        if (manager == null)
//        {
//            initDrivers();
//        }
//
//        Connection conn = null;
//        try
//        {
//            conn = DriverManager.getConnection("jdbc:apache:commons:dbcp:" + connName);
//        }
//        catch (SQLException e)
//        {
//            e.printStackTrace();
//        }
//        return conn;
//    }
//
//    private synchronized static void initDrivers()
//    {
//        LOGGER.info(">> DBCPManager.initDrivers() ");
//        manager = new DBCPManager();
//        Properties dbProps = new Properties();
//
//        // File file = new
//        // File(ConfigImpl.getInstance().getString("properties.dbcp"));
//        // FileInputStream is = new FileInputStream(file);
//        InputStream is = manager.getClass().getResourceAsStream("/com/e3ps/db.properties");
//        try
//        {
//            dbProps.load(is);
//            manager.loadDrivers(dbProps);
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    private void loadDrivers(Properties props)
//    {
//        boolean defaultAutoCommit = false;
//        boolean defaultReadOnly = false;
//
//        String name = null;
//        Enumeration propNames = props.propertyNames();
//        while (propNames.hasMoreElements())
//        {
//            name = (String) propNames.nextElement();
//
//            if (name.endsWith(".driver"))
//            {
//                String pool = name.substring(0, name.lastIndexOf("."));
//                LOGGER.info("pool="+pool);
//                String driver = props.getProperty(pool + ".driver");
//                String url = props.getProperty(pool + ".url");
//                String user = props.getProperty(pool + ".user");
//                String password = props.getProperty(pool + ".password");
//                int maxActive = Integer.parseInt(props.getProperty(pool + ".maxActive"));
//                int maxIdle = Integer.parseInt(props.getProperty(pool + ".maxIdle"));
//                int maxWait = Integer.parseInt(props.getProperty(pool + ".maxWait"));
//                defaultAutoCommit = props.getProperty(pool + ".defaultAutoCommit").equals("true");
//                defaultReadOnly = props.getProperty(pool + ".defaultReadOnly").equals("true");
//
//                BasicDataSource bds = new BasicDataSource();
//                bds.setDriverClassName(driver);
//                bds.setUrl(url);
//                bds.setUsername(user);
//                bds.setPassword(password);
//                bds.setMaxActive(maxActive);
//                bds.setMaxIdle(maxIdle);
//                bds.setMaxWait(maxWait);
//                bds.setDefaultAutoCommit(defaultAutoCommit);
//                bds.setDefaultReadOnly(defaultReadOnly);
//
//                if (props.containsKey(pool + ".removeAbandoned")) bds.setRemoveAbandoned(props.getProperty(pool + ".removeAbandoned").equals("true"));
//                else bds.setRemoveAbandoned(true);
//
//                if (props.containsKey(pool + ".removeAbandonedTimeout")) bds.setRemoveAbandonedTimeout(Integer.parseInt(props.getProperty(pool
//                        + ".removeAbandonedTimeout")));
//                else bds.setRemoveAbandonedTimeout(60);
//
//                if (props.containsKey(pool + ".logAbandoned")) bds.setLogAbandoned(props.getProperty(pool + ".logAbandoned").equals("true"));
//                else bds.setLogAbandoned(true);
//
//                createPools(pool, bds);
//                
//                LOGGER.info("Initialized pool : " + pool);
//            }
//        }
//    }
//
//    private void createPools(String pool, BasicDataSource bds)
//    {
//        GenericObjectPool connectionPool = new GenericObjectPool(null);
//        connectionPool.setWhenExhaustedAction(GenericObjectPool.WHEN_EXHAUSTED_GROW);
//        ConnectionFactory connectionFactory = new DataSourceConnectionFactory(bds);
//        new PoolableConnectionFactory(connectionFactory, connectionPool, null, null, bds.getDefaultReadOnly(), bds.getDefaultAutoCommit());
//        new PoolingDriver().registerPool(pool, connectionPool);
//    }
//    
//    public static void main(String[] args)throws Exception{
//    	
//    	Connection con = null;
//    	Statement st = null;
//    	ResultSet rset = null;
//    	 try{      
//    		 
//    		 con = DBCPManager.getConnection("hr");
//    		 
//   			//con = DriverManager.getConnection("jdbc:mysql://128.111.99.29","plm", "plm*12");
//
//    		 st = con.createStatement(); 
//    		 rset= st.executeQuery("Select * from EP_DEPT_VIEW");
//    		 
//    	 }catch(SQLException se){
//    	   se.printStackTrace();
//    	 }finally{
//    	   try{
//    	    if(rset!=null)rset.close();
//    	    if(st!=null)st.close();
//    	    if(con!=null)con.close();
//    	 }catch(SQLException se){}
//    	}
//    }
//}