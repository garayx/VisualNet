package common;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import elements.Controller;
import elements.Node;

public class CommonData {
	
	public static String selectedNode;
	//public static String NodeType;
	public static Node sourceNode = null;
	public static Node destinationNode = null;
	
	public static Node selectedVertex;
	public static Node nodeLeft;
	public static Node nodeRight;
    public static HashMap<String, Integer> idList;
	
    public static int switchCount = 0;
    public static int hostsCount = 0;
    public static int controllerCount = 0;
	
    public static ArrayList<String> macAddresses;
    public static ArrayList<String> ipAddresses;
    public static ArrayList<String> dpidAddresses;
//    public static HashMap<Object, String> NodeType;
    public static String[] lastIpAddress;
	public static int domainIndex;
	public static String domainName;
	public static Color currentColor;  
    
	public static void initialize()
    {
		//TODO
		common.CommonData.idList = new HashMap<String, Integer>();
//		common.CommonData.NodeType = new HashMap<Object, String>();
//		NodeType.put(elements.Switch.class, "Switch");
		
        common.CommonData.macAddresses = new ArrayList<>();
        common.CommonData.ipAddresses = new ArrayList<>();
        common.CommonData.dpidAddresses = new ArrayList<>();
        
        
        common.CommonData.lastIpAddress = new String[4];
        lastIpAddress[0] = "10";
        lastIpAddress[1] = "0";
        lastIpAddress[2] = "0";
        lastIpAddress[3] = "0";
    }
}
