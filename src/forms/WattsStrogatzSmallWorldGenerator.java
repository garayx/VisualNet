/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forms;

import common.CommonData;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import elements.Node;
import elements.Link;
import java.util.Random;

/**
 *
 * @author Daniel
 */
public class WattsStrogatzSmallWorldGenerator
{
    private EdgeFactory edgeFactory;
    private VertexFactory vertexFactory;
    private int numNodes;
    private int meanDeg;
    private float beta;
    
    public WattsStrogatzSmallWorldGenerator(EdgeFactory edgeFactory, VertexFactory vertexFactory, int numNodes, int meanDeg, float beta) 
    {
        this.edgeFactory = edgeFactory;
        this.vertexFactory = vertexFactory;
        this.numNodes = numNodes;
        this.meanDeg = meanDeg;
        this.beta = beta;
    }
    
    public Graph<Node, Link> create()
    {        
        Graph<Node, Link> graph = new SparseGraph<Node, Link>();        
        Object[] nodes = new Object[this.numNodes];
        
        for (int i = 0; i < numNodes; i++)
        {
            nodes[i] = this.vertexFactory.create();
            graph.addVertex((Node)nodes[i]);
            
        }
        
        for( int i = 0; i < this.numNodes; i++)
        {
            for( int j = 0; j < this.numNodes; j++)
            {
                if( Math.abs( (i - j ) ) % (this.numNodes - 1 - this.meanDeg / 2) <= this.meanDeg / 2 )
                {
                    common.CommonData.nodeLeft = (Node)nodes[i];
                    common.CommonData.nodeRight = (Node)nodes[j];
                    //Link l = edgeFactory.create();
                    
                    //graph.addEdge(l, (Node)nodes[i], (Node)nodes[j]);           
                    
                    if( i < j )
                    {
                        Random r = new Random();
                        if( r.nextGaussian() >= this.beta )
                        {
                            int k = r.nextInt(this.numNodes);                            
                            common.CommonData.nodeRight = (Node)nodes[k];
                            
                        }
                    }
                    
                    if( CommonData.nodeLeft.equals(CommonData.nodeRight) == false )
                    {
                    
                        Link l = edgeFactory.create();

                        graph.addEdge(l, common.CommonData.nodeLeft, common.CommonData.nodeRight);           
                    }
                                               
                }
            }
        }
        
        /*for( int i = 0; i < this.numNodes; i++)
        {
            for( int j = 0; j < this.numNodes; j++)
            {
                
            }
        
        }*/
        return graph;
        
    }
      
}
