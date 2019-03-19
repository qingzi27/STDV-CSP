package model;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Item implements Cloneable{
	private List<TreeMap<Integer,String>> time = new ArrayList<TreeMap<Integer,String>>(); //时间点
	private String value;   //item值
	private int posCount;   //正类计数
	private int negCount;   //负类计数
	private TreeNode child;  //孩子节点
	
	public Item() {
	}

	public Item(String value) {
		super();
		this.value = value;
	}

	public List<TreeMap<Integer, String>> getTime() {
		return time;
	}

	public void setTime(List<TreeMap<Integer, String>> time) {
		this.time = time;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getPosCount() {
		return posCount;
	}

	public void setPosCount(int posCount) {
		this.posCount = posCount;
	}

	public int getNegCount() {
		return negCount;
	}

	public void setNegCount(int negCount) {
		this.negCount = negCount;
	}

	public TreeNode getChild() {
		return child;
	}
	
	public void setChild(TreeNode child) {
		this.child = child;
	}
	
	@Override
	public String toString() {
		return "Item [value=" + value + ", posCount=" + posCount + ", negCount=" + negCount + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + negCount;
		result = prime * result + posCount;
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}
	
	/*@Override
	public String toString() {
		return "Item [time=" + time + ", value=" + value + ", posCount=" + posCount + ", negCount=" + negCount + "]";
	}*/

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if(!value.equals(other.value)) { 
			return false;
		}
		return true;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Item item = (Item) super.clone();
		item.setTime(new ArrayList<TreeMap<Integer,String>>());
		List<TreeMap<Integer,String>> times = item.getTime();
		times.addAll(this.time);
		return item;
	}
}
