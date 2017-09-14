/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elements;

/**
 *
 * @author Daniel
 */
public class NIC
{
    private String mac;
    private String ip;
    private int port;

    public NIC(String mac, String ip)
    {
        this.mac = mac;
        this.ip = ip;
        this.port = 228;
    }

    public NIC(int i) {
		// TODO Auto-generated constructor stub
        this.mac = null;
        this.ip = null;
        this.port = i;
	}

	/**
     * @return the ip
     */
    public String getIp()
    {
        return ip;
    }

    /**
     * @param ip the ip to set
     */
    public void setIp(String ip)
    {
        this.ip = ip;
    }

    /**
     * @return the MAC address 
     */
    public String getMac()
    {
        return mac;
    }

    /**
     * @param mac the MAC address to set
     */
    public void setMac(String mac)
    {
        this.mac = mac;
    }
    public int getPort()
    {
        return port;
    }
    
    
}