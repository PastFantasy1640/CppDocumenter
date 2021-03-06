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
		 * @param depth 階層の深さ
         */
	public static void conv(File origin, File f, File t, int depth){
		//ファイルの読み込み
                //origin
				String current_acc = "";
				String dir_depth = "";
				for(int i = 0; i < depth; i++) dir_depth += "../";
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
									printContent(pw, block);
                                }else if(line.contains("%INDEX%")){
									printIndex(pw, block);
								}else if(line.contains("%LINKDIR%")){
									pw.println(line.replaceAll("%LINKDIR%", dir_depth));
								}else{
									pw.println(line);
                                }
                        }
                        
						//acc閉じる
						if(!current_acc.isEmpty()){
							pw.println("</section>");
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
	
	static void printContent(PrintWriter pw, List<DocBlock> block){
		//ここに本体
		String current_acc = "";
		int i = 0;
		for(DocBlock b : block){
			if(!current_acc.equals(b.accessbility)){
				if(!current_acc.isEmpty()){
					//一回閉じる
					pw.println("</section>");
				}
				pw.println("<section class=\"" + b.accessbility + "\">");
				pw.println("<h1 class=\"acc\">" + b.accessbility + " Members" + "</h1>");
				current_acc = b.accessbility;
			}
			pw.println("<section>");
			pw.println("<a name=\"a" + i + "\"></a>");
			b.getHTML(pw);
			pw.println("</section>");
			i++;
		}
	}
	
	static void printIndex(PrintWriter pw, List<DocBlock> block){
		
		String current_acc = "";
		pw.println("<section class=\"index\">");
		pw.println("<section class=\"noacc\">");
		pw.println("<h1>Unleveled</h1>");
		pw.println("<ul>");
		
		int i = 0;
		for(DocBlock b : block){
			if(!current_acc.equals(b.accessbility)){
				pw.println("</ul>");
				pw.println("</section>");
				pw.println("<section class=\"" + b.accessbility + "\">");
				pw.println("<h1>" + toUpper(b.accessbility) + "</h1>");	//toUpperCase
				pw.println("<ul>");
			}
			pw.println("<li><a href=\"#a" + i + "\">" + b.getHeader() + "</a></li>");
			current_acc = b.accessbility;
			i++;
		}
		
		if(!current_acc.isEmpty()){
			pw.println("</ul>");
			pw.println("</section>");
		}
		pw.println("</section>");
	}

	static String toUpper(String str){
		if(str.isEmpty()) return str;
		String ret;
		ret = str.substring(0, 1).toUpperCase();
		if(str.length() >= 2) ret += str.substring(1).toLowerCase();
		return ret;
	}
	
	static List<DocBlock> parse(BufferedReader lines) throws IOException{
		//改行分離
                
		//ブロックごとに分離	
		String accessibility = "";
		boolean block_nextline = false;
		boolean block_open = false;
		List<DocBlock> block = new ArrayList<>();
		DocBlock now_block = null;
		
                String line;
		while((line = lines.readLine()) != null){
            line = line.trim();
			if(line.isEmpty()) continue;
			else if(line.contains("private:")) accessibility = "private";
			else if(line.contains("public:")) accessibility = "public";
			else if(line.contains("protected:")) accessibility = "protected";
			else if(line.contains("/**")) block_open = true;
			else if(line.contains("*/")) {
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
						if(now_block == null) {
							now_block = new DocBlock(accessibility);
						}
						now_block.addLine(str);
					}
				}
			}
		}
		
		//意味づけ
		for(DocBlock d : block) d.parse();
		
		//並び替え
		//$block = _sort_block($block);

		return block;
	}

}
