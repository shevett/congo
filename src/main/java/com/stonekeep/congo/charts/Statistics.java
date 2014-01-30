package com.stonekeep.congo.charts;

import java.awt.Font;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import com.opensymphony.xwork2.ActionSupport;
import com.stonekeep.congo.dao.InvoiceDAO;
import com.stonekeep.congo.dao.RegistrationTypeDAO;
import com.stonekeep.congo.data.Convention;
import com.stonekeep.congo.data.RegistrationType;
import com.stonekeep.congo.reports.RegistrantStatusReport;

public class Statistics extends ActionSupport implements ServletResponseAware,SessionAware {
	
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(RegistrantStatusReport.class);

	HttpServletResponse response;
	private Map<String, Object> sessionData;
	private final RegistrationTypeDAO registrationTypeDAO;
	private final InvoiceDAO invoiceDAO;
	
	public Statistics(RegistrationTypeDAO registrationTypeDAO, InvoiceDAO invoiceDAO) {
		this.registrationTypeDAO = registrationTypeDAO;
		this.invoiceDAO = invoiceDAO;
	}


    public String execute() throws Exception{
        response.setContentType("image/png");
        OutputStream out = response.getOutputStream();
        Convention c = (Convention)sessionData.get("conference");
        
        // Create a simple Bar chart
        //DefaultValueDataset dataset = new org.jfree.data.general.DefaultValueDataset();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        if (c.conCap > 0) {
        	dataset.setValue(c.conCap,"Value","Event Cap");
        }
        dataset.setValue(c.numSubscribed,"Value","Subscribed");
        dataset.setValue(c.numRegistered,"Value","Registered");
        dataset.setValue(c.numBadged,"Value","Badged");
        dataset.setValue(c.numCheckedin,"Value","Checkedin");
        JFreeChart chart = ChartFactory.createBarChart(null,
        		null,null,dataset,PlotOrientation.HORIZONTAL,false,false,false);
        
        ChartUtilities.writeChartAsPNG(out, chart, 750, 125);

        return null;

    }
    
    public String statusByCount() throws Exception {
    	response.setContentType("image/png");
    	OutputStream out = response.getOutputStream();
    	Convention c = (Convention)sessionData.get("conference");
    	logger.info("Generating statusByCount pie chart for " + c.conName);
    	Map<String,RegistrationType> regTypes = new TreeMap(registrationTypeDAO.list(c.getConCID()));
    	DefaultPieDataset dataset = new DefaultPieDataset();

    	for (RegistrationType rt : regTypes.values()) {
    		logger.debug("Adding row " + rt.getRegName() + " with value " + rt.getRegCount());
    		if (rt.getRegCount() > 0) {
    			dataset.setValue(rt.getRegName() + " [" + rt.getRegCount() + "]",rt.getRegCount());
    		}
    	}
    	JFreeChart chart = ChartFactory.createPieChart3D(
    			"Registration Types Distribution : Counts",  // chart title
    			dataset,             // data
    			false,               // include legend
    			true,
    			false
    			);

    	PiePlot plot = (PiePlot) chart.getPlot();
    	plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
    	plot.setNoDataMessage("No data available");
    	plot.setCircular(false);
    	plot.setLabelGap(0.02);
    	ChartUtilities.writeChartAsPNG(out, chart, 800, 500);

    	return null;
    }

	@Override
	public void setServletResponse(HttpServletResponse arg0) {
		response = arg0;
	}

	@Override
	public void setSession(Map<String, Object> arg0) {
		this.sessionData = arg0;
	}
}
