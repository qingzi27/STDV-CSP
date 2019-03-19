package stdvcsp;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import model.Item;
import model.Transaction;
import model.TreeNode;
import test.Test;

public class PatternMiner {
	static int posTotal = Test.posCount();
	static int negTotal = Test.negCount();
	static double posRatio;
	static double negRatio;
	static List<Item> items;
	static List<Integer> posTimePosition;
	static List<Integer> negTimePosition;
	
	public static void PatternList(TreeNode root, List<Transaction> c1, List<Transaction> c2) throws IOException, CloneNotSupportedException {
		List<Item> prefix = new ArrayList<Item>();
		patternMing(root, prefix, c1, c2);
		System.out.println(c1.size());
		System.out.println(c2.size());
		System.out.println("patternSize=" + (c1.size()+c2.size()));
	}
	
	private static void patternMing(TreeNode root, List<Item> prefix, List<Transaction> c1, List<Transaction> c2) throws IOException, CloneNotSupportedException {
		if (root != null && root.getElements().size() > 0) {
			for (Item item : root.getElements()) {
				prefix.add(item);
				Transaction transaction = new Transaction();
				transaction.setItems(prefix);
				if (isTightness(transaction)) {
					if(isCSP(transaction) == 0){
						c1.add((Transaction) transaction.clone());
					}
					else if(isCSP(transaction) == 1){
						c2.add((Transaction) transaction.clone());
					}
					else {
				    	if(isDCSP(transaction) == 0) {
				    		c1.add((Transaction) transaction.clone());
				    	}
				    	else if (isDCSP(transaction) == 1) {
				    		c2.add((Transaction) transaction.clone());
						}
					}
				}
				patternMing(item.getChild(), prefix, c1, c2);
				prefix.remove(prefix.size() - 1);
			}
		}
	}
	
	private static boolean isTightness(Transaction transaction) throws IOException {
		boolean flag = false;
		boolean posFlag = false;
		boolean negFlag = false;
		
		model.Pattern pattern = new model.Pattern();
		long tightness = pattern.getTightness();

		lastItemTimesPosition(transaction);
		
		//每条序列正类的时间戳
		List<String> posItemTimes = new ArrayList<String>();
		//每条序列负类的时间戳
		List<String> negItemTimes = new ArrayList<String>();
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		Date date = new Date();
		long sum = 0;
		List<Long> posTime = new ArrayList<Long>();
		List<Long> negTime = new ArrayList<Long>();
		//正负类紧密度
		long posVar = 0,negVar = 0;
		
		//正类中无数据
		if(posTimePosition.size() == 0) {
			posFlag = true;
		}
		//System.out.println("t="+transaction);
		if(posTimePosition.size() > 0) {
			for (int i = 0; i < posTimePosition.size(); i++) {
				int position = posTimePosition.get(i);
				for (int j = 0; j < items.size(); j++) {
					Item item = items.get(j);
					for (int k = 0; k < item.getTime().size(); k++) {
						if(item.getTime().get(k).get(position)!=null) {
							posItemTimes.add(item.getTime().get(k).get(position));
							break;
						}
					}
				}
				for (int j = 0; j < posItemTimes.size(); j++) {
					try {
						date = df.parse(posItemTimes.get(j));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					posTime.add(date.getTime()/(1000*60));
					sum += (date.getTime()/(1000*60));
				}
				if(posTime.size() > 0) {
					sum = sum / posTime.size();
					for (Long l : posTime) {
						posVar += (l - sum) * (l - sum);
					}
					posVar = posVar / posTime.size();
				}
				if(posVar <= tightness) {
					posFlag = true;
					posItemTimes.clear();
					posTime.clear();
					sum = 0;
					posVar = 0;
				}
				else {
					posFlag = false;
					break;
				}
			}
		}
		
		sum = 0;
		
		//负类中无数据
		if(negTimePosition.size() == 0) {
			negFlag = true;
		}
		
		if(negTimePosition.size() > 0) {
			for (int i = 0; i < negTimePosition.size(); i++) {
				int position = negTimePosition.get(i);
				for (int j = 0; j < items.size(); j++) {
					Item item = items.get(j);
					for (int k = 0; k < item.getTime().size(); k++) {
						if(item.getTime().get(k).get(position)!=null) {
							negItemTimes.add(item.getTime().get(k).get(position));
							break;
						}
					}
				}
				for (int j = 0; j < negItemTimes.size(); j++) {
					String s = negItemTimes.get(j).substring(1, negItemTimes.get(j).length());
					try {
						date = df.parse(s);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					negTime.add(date.getTime()/(1000*60));
					sum += (date.getTime()/(1000*60));
				}
				if(negTime.size() > 0) {
					sum = sum / negTime.size();
					for (Long l : negTime) {
						negVar += (l - sum) * (l - sum);
					}
					negVar = negVar / negTime.size();
				}
				if(negVar <= tightness) {
					negFlag = true;
					negItemTimes.clear();
					negTime.clear();
					sum = 0;
					negVar = 0;
				}
				else {
					negFlag = false;
					break;
				}
			}
		}
		if(posFlag == true && negFlag == true) {
			flag = true;
		}
		return flag;
	}
	
	private static int isCSP(Transaction transaction) throws IOException {
		int flag = -1;
		
		model.Pattern pattern = new model.Pattern();
		double posSup = pattern.getPosSup();
		double negSup = pattern.getNegSup();
		
		sup(transaction);

		if(posRatio >= posSup &&  negRatio < negSup){
			flag = 0;
		}
		else if(posRatio < negSup && negRatio >= posSup) {
			flag = 1;
		}
		
		return flag;
	}
	
	private static int isDCSP(Transaction transaction) throws IOException{
		int flag = -1;
		boolean posFlag = false;
		boolean negFlag = false;
		
		model.Pattern pattern = new model.Pattern();
		long tightness = pattern.getdTightness();
		long timeValue = pattern.getTimeValue();

		lastItemTimesPosition(transaction);
		
		int size = items.size();
	
		Item mediaItem= items.get((1+size)/2-1);
		List<TreeMap<Integer, String>> flag1 = mediaItem.getTime();
		List<String> mediaItemTimes = new ArrayList<String>();
		for (int i = 0; i < posTimePosition.size(); i++) {
			int position = posTimePosition.get(i);
			for (int j = 0; j < flag1.size(); j++) {
				if(flag1.get(j).get(position)!=null) {
					mediaItemTimes.add(flag1.get(j).get(position));
					break;
				}
			}
		}
		for (int i = 0; i < negTimePosition.size(); i++) {
			int position = negTimePosition.get(i);
			for (int j = 0; j < flag1.size(); j++) {
				if(flag1.get(j).get(position)!=null) {
					mediaItemTimes.add(flag1.get(j).get(position));
					break;
				}
			}
		}
			
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		Date date = new Date();
		long posSum = 0,negSum = 0;
		List<Long> posTime = new ArrayList<Long>();
		List<Long> negTime = new ArrayList<Long>();
		long posVar = 0,negVar = 0;
		
		for (String s : mediaItemTimes) {
			date = null;
			if(!s.startsWith("-")){
				try {
					date = df.parse(s);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				posTime.add(date.getTime()/(1000*60));
				posSum += (date.getTime()/(1000*60));
			}
			else if(s.startsWith("-")){
				s = s.substring(1, s.length());
				try {
					date = df.parse(s);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				negTime.add(date.getTime()/(1000*60));
				negSum += (date.getTime()/(1000*60));
			}
		}
		
		if(posTime.size() > 0) {
			posSum = posSum / posTime.size();
			for (Long l : posTime) {
				posVar += (l - posSum) * (l - posSum);
			}
			posVar = posVar / posTime.size();

			if(posVar <= tightness) {
				posFlag = true;
			}
		}
		if(negTime.size() > 0) {
			negSum = negSum / negTime.size();
			for (Long l : negTime) {
				negVar += (l - negSum) * (l - negSum);
			}
			negVar = negVar / negTime.size();

			if(negVar <= tightness) {
				negFlag = true;
			}
		}
		sup(transaction);
		
		if(posFlag == true && negFlag == true) {
			if(Math.abs(posSum-negSum) >= timeValue) {
				if(posRatio >= negRatio) {
					flag = 0;
				}
				else {
					flag = 1;
				}
			}
		}
		return flag;
	}
	
	private static void sup(Transaction transaction) {
		List<Item> items = transaction.getItems();
		int posCount = items.get(items.size() - 1).getPosCount();
		int negCount = items.get(items.size() - 1).getNegCount();
		posRatio = (double)posCount/posTotal;
		negRatio = (double)negCount/negTotal;
	}
	
	private static void lastItemTimesPosition(Transaction transaction) {
		items = transaction.getItems();

		//transaction中的最后一个item
		Item lastItem = items.get(items.size()-1);
		List<TreeMap<Integer, String>> lastItemTimes = lastItem.getTime();

		//正类中每条事务的最后一个Item的时间戳的路径源
		posTimePosition = new ArrayList<Integer>();
		//负类中每条事务的最后一个Item的时间戳的路径源
		negTimePosition = new ArrayList<Integer>();

		for (int i = 0; i < lastItemTimes.size(); i++) {
			TreeMap<Integer, String> s = lastItemTimes.get(i);
			if(!s.firstEntry().getValue().startsWith("-")) {
				posTimePosition.add(s.firstKey());
			}
			else if(s.firstEntry().getValue().startsWith("-")){
				negTimePosition.add(s.firstKey());
			}
		}
	}
	
	public static void patternResultList(List<Transaction> transactions){
		List<String> strs = new ArrayList<String>();
		if(!transactions.isEmpty()){
			for (Transaction transaction : transactions) {
				String prefix = "";
				List<Item> items = transaction.getItems();
				for (int i = 0;i < items.size();i++ ) {
					prefix += items.get(i).getValue() + " ";
					if(i == items.size()-1){
						strs.add(prefix);
					}
				}
			}
		}
		if(strs.isEmpty()){
			System.out.println("无模式");
		}
		else{
			for (String s : strs) {
				System.out.println(s);
			}
		}
	}
}
