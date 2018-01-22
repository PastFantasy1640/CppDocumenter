/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cppdocmenter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;



/**
 *
 * @author white
 */
public class DocConverter {
	public static void conv(File f, File t){
		
	}

	static void parse(String src){
		//改行分離
		String[] lines = src.split("\r\n");
		for(String l : lines) l = l.trim();
		
		//ブロックごとに分離	
		String accessibility = "private";
		boolean block_nextline = false;
		boolean block_open = false;
		List<DocBlock> block = new ArrayList<>();
		DocBlock now_block = null;
		
		for(String line : lines){
			if(line.isEmpty()) continue;
			if(line.substring(0, 8).equals("private:")) accessibility = "private";
			else if(line.length() >= 7 && line.substring(0, 7).equals("public:")) accessibility = "public";
			else if(line.length() >= 10 && line.substring(0, 10).equals("protected:")) accessibility = "protected";
			else if(line.length() >= 3 && line.substring(0, 3).equals("/**")) block_open = true;
			else if(line.length() >= 2 && line.substring(0, 2).equals("*/")) {
				block_open = false;
				block_nextline = false;
			}else if(block_nextline) {
			
				if(line.endsWith("{")) line = line.substring(line.length() - 1);
				String obj = line.replaceAll(";", "");
				
				block_nextline = false;			
				if(!obj.isEmpty() && now_block != null) block.add(now_block);
				now_block = null;
			}
		
			if(block_open){
				String[] comment = line.split(" ", 2);
				if(comment.length == 2){
					String str = comment[1].trim();
					if(!str.isEmpty()){
						if(now_block == null) now_block = new DocBlock(accessibility);
						now_block.addLine(str);
					}
				}
			}
		}
		
		//意味づけ
		for(DocBlock d : block) d.parse();
		
		//並び替え
		//$block = _sort_block($block);

		//return $block;
	}

}
