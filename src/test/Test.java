package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Transaction;
import model.TreeNode;
import stdvcsp.DataUtil;
import stdvcsp.FileUtil;
import stdvcsp.PatternMiner;
import stdvcsp.Tree;

public class Test {
	static int posCount;
	static int negCount;
	
	public static void main(String[] args) throws IOException, CloneNotSupportedException{
		String posPath = "F:/研究生/研究论文/数据集/ADLs/ADLs_pos.txt";
		String negPath = "F:/研究生/研究论文/数据集/ADLs/ADLs_neg.txt";
		
		long startTime = System.currentTimeMillis();   // 获取开始时间

		List<String> posDate = FileUtil.readFile(posPath);
		List<String> negDate = FileUtil.readFile(negPath);
		posCount = posDate.size();
		negCount = negDate.size();
		
		// 存每条transaction及其所有后缀子串
		List<Transaction> transactions = new ArrayList<Transaction>();
		
		// 获取每条transaction
		List<Transaction> transactions2 = DataUtil.dataHandle(posDate, negDate);
		
		// 获取每条transaction及其后缀并存入<List> transactions中
		for (Transaction transaction : transactions2) {
			transactions.add(transaction);
			for (Transaction transaction2 : Tree.suffixSubstring(transaction)) {
				transactions.add(transaction2);
			}
		}
		
		TreeNode root = new TreeNode();
		int key = 1;

		// 建树
		for (Transaction transaction : transactions) {
			Tree.buildTree(root, transaction, key);
		}
		
		List<Transaction> c1 = new ArrayList<Transaction>();
		List<Transaction> c2 = new ArrayList<Transaction>();
		// 模式挖掘
		PatternMiner.PatternList(root, c1, c2);
		
		// 模式挖掘结果
		/*PatternMiner.patternResultList(c1);
		System.out.println("--------------------");
		PatternMiner.patternResultList(c2);*/
		
		long endTime=System.currentTimeMillis(); // 获取结束时间
		System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
		
		System.out.println("总内存：" + Runtime.getRuntime().totalMemory()
				/ 1024 / 1024 + "M");
		System.out.println("空闲内存：" + Runtime.getRuntime().freeMemory()
				/ 1024 / 1024 + "M");
		System.out.println("已使用内存："
				+ (Runtime.getRuntime().totalMemory() - Runtime
						.getRuntime().freeMemory()) / 1024 / 1024 + "M");
	
	}
	
	// 获得正类transaction总数
	public static int posCount(){
		int count = posCount;
		return count;
	}

	// 获得负类transaction总数
	public static int negCount(){
		int count = negCount;
		return count;
	}
}
