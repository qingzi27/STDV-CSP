package stdvcsp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Item;

public class FileUtil {
	public static List<String> readFile(String path) throws IOException{
		List<String> data = new ArrayList<String>();
		File file=new File(path);
        if(!file.exists()||file.isDirectory())
            throw new FileNotFoundException();
		BufferedReader br = new BufferedReader(new FileReader(file));
        String tmp = br.readLine();
        while (tmp!=null) {
        	tmp = tmp.toString();
        	if (tmp.trim().equals("")) {
				continue;
			}
			data.add(tmp);
			tmp = br.readLine();
		}
        br.close();
		return data;
	}
	
	public static void writefile(Item item ,String filename) {
		File file = new File(filename);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(file,true));
			
			bw.write(item + "\n");

			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
