/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cppdocmenter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;



/**
 *
 * @author white
 */
public class DocConverter {
        /**
         * 実際にファイルに変換して落とす関数
         * @param origin もとになるhtmlファイル
         * @param f 変換前のヘッダファイル
         * @param t 変換後の保存先
         */
	public static void conv(File origin, File f, File t){
		//ファイルの読み込み
                //origin
                try{
                        BufferedReader src =  new BufferedReader(new InputStreamReader(new FileInputStream(f), "Shift-JIS"));
                        //parse
                        List<DocBlock> block = DocConverter.parse(src);
                        String title = "[untitled]";
                        if(block.size() > 0 && block.get(0).getHeader() != null) title = block.get(0).getHeader();
                        
                        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(origin), "UTF-8"));
						if(!t.exists() || !t.isFile()) t.mkdir();
                        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(t.getCanonicalPath() + "/" + f.getName().replace('.', '_') + ".html"), "UTF-8"));
                        String line;
                        while((line = br.readLine()) != null){
                                line = line.replaceAll("%TITLE%", title);
                                if(line.contains("%CONTENT%")){
                                        //ここに本体
                                        for(DocBlock b : block){
											pw.println("<section>");
                                                b.getHTML(pw);
											pw.println("</section>");
                                        }
                                }else{
                                        pw.println(line);
                                }
                        }
                        
                        br.close();
                        src.close();
                        pw.close();
                }catch(FileNotFoundException e){
                    e.printStackTrace();
                }catch(IOException e){
                    e.printStackTrace();
                }
        }

	static List<DocBlock> parse(BufferedReader lines) throws IOException{
		//改行分離
                
		//ブロックごとに分離	
		String accessibility = "private";
		boolean block_nextline = false;
		boolean block_open = false;
		List<DocBlock> block = new ArrayList<>();
		DocBlock now_block = null;
		
                String line;
		while((line = lines.readLine()) != null){
            line = line.trim();
			if(line.isEmpty()) continue;
			if(line.length() >= 8 && line.substring(0, 8).equals("private:")) accessibility = "private";
			else if(line.length() >= 7 && line.substring(0, 7).equals("public:")) accessibility = "public";
			else if(line.length() >= 10 && line.substring(0, 10).equals("protected:")) accessibility = "protected";
			else if(line.length() >= 3 && line.substring(0, 3).equals("/**")) block_open = true;
			else if(line.length() >= 2 && line.substring(0, 2).equals("*/")) {
				block_open = false;
				block_nextline = true;
			}else if(block_nextline) {
			
				if(line.endsWith("{")) line = line.substring(0, line.length() - 1);
				block_nextline = false;		
				String obj = line.replaceAll(";", "");	
				if(now_block != null){
					now_block.addHeader(obj);
					block.add(now_block);
				}
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
		System.out.println("Size" + block.size());
		for(DocBlock d : block) d.parse();
		
		//並び替え
		//$block = _sort_block($block);

		return block;
	}

}
