package model;

import java.util.ArrayList;
import java.util.List;


public class Transaction implements Cloneable{
	private List<Item> items = new ArrayList<Item>();  //每条事物
	private String route;  // pos Or neg
	
	public Transaction() {
		super();
	}

	public Transaction(List<Item> items) {
		super();
		this.items = items;
	}

	public Transaction(String route) {
		super();
		this.route = route;
	}

	public Transaction(List<Item> items, String route) {
		super();
		this.items = items;
		this.route = route;
	}

	public List<Item> getItems() {
		return items;
	}
	
	public void setItems(List<Item> items) {
		this.items = items;
	}
	
	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	@Override
	public String toString() {
		return "Transaction [items=" + items + ", route=" + route + "]";
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		Transaction newtransaction = (Transaction)super.clone();
		List<Item> items = this.getItems();
		List<Item> newitems = new ArrayList<Item>();
		for (Item item : items) {
			newitems.add((Item) item.clone());
		}
		newtransaction.setItems(newitems);
		return newtransaction;
	}
}
