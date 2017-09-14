/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elements;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Daniel
 */
public class Switch extends Node
{
    private ArrayList<SwitchPort> ports;
    private HashMap<Integer, SwitchPort> portsMap;
    private HashMap<String, Integer> connections;
    private HashMap<String, Integer> adjacentSwitches;
    private HashMap<String, Integer> adjacentHosts;
    private HashMap<String, String> ipToMac;
    private HashMap<String, String> routingTable;
   
    private String dpid;
    private static int _id = 0;
    private final int id;
    private String sid;
    private String type = "Switch";
    private String routeType = "";
    
    public Switch()
    {
        this.initialize();
        
        _id++;
        
        this.id = _id;
        
        this.sid = "s" + this.id;
    }
            
    
    
    private void initialize()
    {
        this.ports = new ArrayList<>();
        this.portsMap = new HashMap<>();
        this.connections = new HashMap<>();
        this.adjacentSwitches = new HashMap<>();
        this.adjacentHosts = new HashMap<>();
        this.ipToMac = new HashMap<>();
        this.routingTable = new HashMap<>();
    }

    /**
     * @return the ports
     */
    public ArrayList getPorts()
    {
        return ports;
    }

    /**
     * @param ports the ports to set
     */
    public void setPorts(ArrayList ports)
    {
        this.ports = ports;
    }

    /**
     * @return the dpid
     */
    public String getDpid()
    {
        return dpid;
    }

    /**
     * @param dpid the dpid to set
     */
    public void setDpid(String dpid)
    {
        this.dpid = dpid;
    }
    
/*    public int getID()
    {
        return this.id;        
    }*/
    public int getID()
    {
        return this.id;        
    }
    /**
     * @return the sid
     */
    public String getSid()
    {
        return sid;
    }

    /**
     * @param sid the sid to set
     */
    public void setSid(String sid)
    {
        this.sid = sid;
    }
    
	public String getType() {
		// TODO Auto-generated method stub
		return this.type;
	}
    /**
     * method sets route type
     * @param x
     * set routeType
     */
    public void setRouteType(String x)
    {
        this.routeType = x;
    }
    /**
     * method returns routeType
     * @return routeType
     */
    public String getRouteType()
    {
        return routeType;
    }
	
}
