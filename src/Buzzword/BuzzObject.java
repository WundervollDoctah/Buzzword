package Buzzword;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

import BuzzScene.SceneManager;
import gui.Workspace;

public class BuzzObject{
	
	private BuzzObject parent;
	private Pane pane; //Pane to place objects in
	private String name;
	private double x;
	private double y;
	private ArrayList<NodeName> nodes = new ArrayList<>();
	private ArrayList<BuzzObject> children = new ArrayList<>();
	
	public BuzzObject(String name, Pane pane) {
		this(name, pane, 0, 0);
	}
	
	public BuzzObject(String name, Pane pane, double x, double y){
		this.pane = pane;
		this.name = name;
		this.x = x;
		this.y = y;
	}
	
	private class NodeName{
		
		public final String name;
		public final Node node;
		
		public NodeName(String name, Node node){
			this.name = name;
			this.node = node;
			
		}
	}
	
	public void alignNodes(){
		pane.relocate(x, y);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
		alignNodes();
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
		alignNodes();
	}
	
	public void setPos(double x, double y){
		this.x = x;
		this.y = y;
		alignNodes();
	}

	public <T> T getNode(Class<T> c) {
		for(NodeName n : nodes){
			if(n.node.getClass() == c){
				return (T) n.node;
			}
		}
		return null;
	}
	
	public Node getNode(String name){
		int index = findNode(name);
		if(index >= 0)
			return nodes.get(index).node;
		return null;
	}
	
	public <T> T[] getNodes(Class<T> c){
		ArrayList<T> typeNodes = new ArrayList<>();
		for(NodeName n : nodes){
			if(n.node.getClass() == c){
				typeNodes.add((T)n.node);
			}
		}
		return (T[]) typeNodes.toArray();
	}
	
	public void addNode(String name, Node n){
		NodeName nn = new NodeName(name, n);
		nodes.add(nn);
		n.relocate(x, y);
	}
	
	public void loadNodes(){
		for(NodeName n : nodes){
			pane.getChildren().add(n.node);
			Workspace.getSM().getGUI().getAppPane().getChildren().add(pane);
		}
		alignNodes();
	}
	
	public boolean removeNode(Node n){
		int index = findNode(n);
		if(index >= 0)
			return nodes.remove(n);
		return false;
	}
	
	public BuzzObject getChild(String name){
		for(BuzzObject bz : children)
			if(bz.name.equals(name))
				return bz;
		return null;
	}
	
	public BuzzObject getChild(int i){
		return children.get(i);
	}
	
	public void addChild(BuzzObject bz){
		children.add(bz);
		bz.setParent(this);
	}
	
	public void removeChild(BuzzObject bz){
		boolean isChild = children.remove(bz);
		if(isChild) bz.setParent(null);
	}

	public BuzzObject getParent() {
		return parent;
	}

	public void setParent(BuzzObject parent) {
		this.parent = parent;
	}
	
	private int findNode(Node n){
		for(int i = 0; i < nodes.size(); i++)
			if(nodes.get(i).node == n)
				return i;
		return -1;
	}
	
	private int findNode(String name){
		for(int i = 0; i < nodes.size(); i++)
			if(nodes.get(i).name.equals(name))
				return i;
		return -1;
	}
}
