package model;

import java.util.ArrayList;
import java.util.List;

public class TreeNode{
	private List<Item> elements = new ArrayList<Item>(); 

	public List<Item> getElements() {
		return elements;
	}
	
	public void setElements(List<Item> elements) {
		this.elements = elements;
	}
	
	@Override
	public String toString() {
		return "TreeNode [elements=" + elements + "]";
	}
}
