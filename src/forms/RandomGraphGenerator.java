/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forms;

import common.CommonData;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import elements.Link;
import elements.Node;
import java.util.Random;

/**
 *
 * @author Daniel
 */
public class RandomGraphGenerator
{
    private EdgeFactory edgeFactory;
    private VertexFactory vertexFactory;
    private int numNodes;
    private int meanDeg;
    private float beta;
    
    public RandomGraphGenerator(EdgeFactory edgeFactory, VertexFactory vertexFactory, int numNodes, int meanDeg, float beta) 
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
        
        Random r = new Random();
        
        for( int i = 0; i < this.numNodes; i++)
        {
            for( int j = 0; j < this.numNodes; j++)
            {
                if( r.nextGaussian() > beta )
                {
                    
                    common.CommonData.nodeLeft = (Node)nodes[i];
                    common.CommonData.nodeRight = (Node)nodes[j];
                                        
                    if( CommonData.nodeLeft.equals(CommonData.nodeRight) == false )
                    {
                    
                        Link l = edgeFactory.create();

                        graph.addEdge(l, common.CommonData.nodeLeft, common.CommonData.nodeRight, EdgeType.UNDIRECTED);           
                    }
                                               
                }
            }
        }
        
        return graph;
        
    }
    
}
