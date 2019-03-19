package stdvcsp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import model.Item;
import model.Transaction;
import model.TreeNode;

public class Tree {
	// 对每条事物建树
	@SuppressWarnings("unchecked")
	public static final void buildTree(TreeNode root, Transaction transaction ,int key) {
		initiateLeftOrRightCount(transaction);   // 初始化正负类计数
		List<Item> items = transaction.getItems();
		if (items.size() > 0) {
			Item head = items.get(0);
			TreeNode currentNode = root;
			// 根节点中不包含头元素，将每个item依次添入树中
			if (!root.getElements().contains(head)) {
				for (int i = 0; i < items.size(); i++) {
					if(key > 1) {
						TreeMap<Integer, String> flag = new TreeMap<>();
						flag = (TreeMap<Integer, String>) items.get(i).getTime().get(0).clone();
						flag.put(key, flag.remove(1));
						items.get(i).getTime().clear();
						items.get(i).getTime().add(flag);
					}
					currentNode.getElements().add(items.get(i));
					if (i == items.size() - 1) {
						break;
					}
					items.get(i).setChild(new TreeNode());
					currentNode = items.get(i).getChild();
				}
			} 
			// 根节点中包含头元素
			else {
				int index = root.getElements().indexOf(head);
				Item existHead = root.getElements().get(index);
				
				existHead.setPosCount(existHead.getPosCount()+ head.getPosCount());
				existHead.setNegCount(existHead.getNegCount()+ head.getNegCount());
				
				int key1 = existHead.getTime().get(existHead.getTime().size()-1).lastKey();
				TreeMap<Integer, String> flag = new TreeMap<>();
				flag = (TreeMap<Integer, String>) head.getTime().get(0).clone();

				if (key1+1 > key) {
					flag.put(key1+1, flag.remove(1));
				}
				else {
					flag.put(key, flag.remove(1));
				}

				existHead.getTime().add(flag);
				
				key = existHead.getTime().get(existHead.getTime().size()-1).lastKey();
				
				currentNode = existHead.getChild();
				
				if (currentNode == null) {
					existHead.setChild(new TreeNode());
					currentNode = existHead.getChild();
				}

				List<Item> items2 = new ArrayList<Item>();
				for (int i = 1; i < items.size(); i++) {
					items2.add(items.get(i));
				}

				if (transaction.getRoute() == "pos") {
					Transaction transaction2 = new Transaction(items2, "pos");
					buildTree(currentNode, transaction2, key);
				} 
				else if (transaction.getRoute() == "neg") {
					Transaction transaction2 = new Transaction(items2, "neg");
					buildTree(currentNode, transaction2, key);
				}
			}
		}
	}
	
	// 初始化正负类计数
	public static final void initiateLeftOrRightCount(Transaction transaction) {
		List<Item> items = transaction.getItems();
		if (transaction.getRoute() == "pos") {
			for (int i = 0; i < items.size(); i++) {
				items.get(i).setPosCount(1);;
				items.get(i).setNegCount(0);;
			}
		} else if(transaction.getRoute() == "neg"){
			for (int i = 0; i < items.size(); i++) {
				items.get(i).setPosCount(0);;
				items.get(i).setNegCount(1);;
			}
		}
	}

	// 获取每条事物的后缀子串
	public static List<Transaction> suffixSubstring(Transaction transaction) throws CloneNotSupportedException{
		List<Item> items = transaction.getItems();
		List<Transaction> transactions = new ArrayList<Transaction>();
		if(items.size() > 0){
			if(transaction.getRoute() == "pos"){
				for (int i = 0; i < items.size()-1; i++) {
					Transaction newTransaction = new Transaction("pos");
					for (int j = i+1; j < items.size(); j++) {
						Item perItem = items.get(j);
						newTransaction.getItems().add(perItem);
					}
					transactions.add((Transaction) newTransaction.clone());
				}
			}
			else if(transaction.getRoute() == "neg"){
				for (int i = 0; i < items.size()-1; i++) {
					Transaction newTransaction = new Transaction("neg");
					for (int j = i+1; j < items.size(); j++) {
						Item perItem = items.get(j);
						newTransaction.getItems().add(perItem);
					}
					transactions.add((Transaction) newTransaction.clone());
				}
			}
		}
		return transactions;
	}
	
}
