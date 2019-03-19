package stdvcsp;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import model.Item;
import model.Transaction;

public class DataUtil {	
	public static List<Transaction> dataHandle(List<String> posDate,List<String> negDate) {
		List<Transaction> transactions = new ArrayList<Transaction>();
		
		// 处理正类数据
		for (String string : posDate) {
			Transaction posTransaction = new Transaction("pos");
			String[] strs = string.split(" ");
			for (int i = 0; i < strs.length; i += 3) {
				if(i + 2 < strs.length){
					Item item = new Item();
					TreeMap<Integer,String> map = new TreeMap<Integer,String>();
					map.put(1, strs[i+1]);
					item.getTime().add(map);
					item.setValue(strs[i+2]);
					posTransaction.getItems().add(item);
				}
			}
			transactions.add(posTransaction);
		}
		
		// 处理负类数据
		for (String string : negDate) {
			Transaction negTransaction = new Transaction("neg");
			String[] strs = string.split(" ");
			for (int i = 0; i < strs.length; i += 3) {
				if (i + 2 < strs.length) {
					Item item = new Item();
					TreeMap<Integer,String> map = new TreeMap<Integer,String>();
					map.put(1, "-" + strs[i + 1]);
					item.getTime().add(map);
					item.setValue(strs[i + 2]);
					negTransaction.getItems().add(item);
				}
			}
			transactions.add(negTransaction);
		}
		return transactions;
	}
}
