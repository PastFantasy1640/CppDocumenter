/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cppdocmenter;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author white
 */
public class DocBlock {
	
	private class Pair{
		public final String head;
		public final String body;
		public Pair(String head_, String body_){
			head = head_; body = body_;
		}
	}
	
	private final String accessbility;
	private final List<String> blocks;
	private String header;
	private List<String> author;
	private List<String> version;
	private List<String> since;
	private List<String> deprecated;
	private List<Pair> param;
	private List<String> return_str;
	private List<Pair> throws_str;
	private List<Pair> see;
	private List<String> summary;
	
	
	public DocBlock(String accessbility_){
		accessbility = accessbility_;
		blocks = new ArrayList<>();
	}
	
	public void addLine(String line){
		blocks.add(line);
	}
	
	public void addHeader(String header){
		this.header = CppDocmenter.htmlspecialchars(header);
	}
        
        public String getHeader(){
                return this.header;
        }
	
	public void parse(){
		for(String line : blocks){
			String[] lines_tmp = line.split(" ", 3);
			String[] lines = new String[3];
			switch(lines_tmp.length){
				case 3: lines[2] = lines_tmp[2];
				case 2: lines[1] = lines_tmp[1];
				case 1: lines[0] = lines_tmp[0];
				default:
			}
			
			switch (lines[0]) {
				case "@author":
					if(this.author == null) this.author = new ArrayList<>();
					this.author.add(lines[1]);
					break;
				case "@version":
					if(this.version == null) this.version = new ArrayList<>();
					this.version.add(lines[1]);
					break;
				case "@since":
					if(this.since == null) this.since = new ArrayList<>();
					this.since.add(lines[1]);
					break;
				case "@deprecated":
					if(this.deprecated == null) this.deprecated = new ArrayList<>();
					this.deprecated.add(lines[1]);
					break;
				case "@param":
					if(this.param == null) this.param = new ArrayList<>();
					this.param.add(new Pair(lines[1], lines[2]));
					break;
				case "@return":
					if(this.return_str == null) this.return_str = new ArrayList<>();
					this.return_str.add(lines[1] + lines[2]);
					break;
				case "@throws":
					if(this.throws_str == null) this.throws_str = new ArrayList<>();
					this.throws_str.add(new Pair(lines[1], lines[2]));
					break;
				case "@see":
					if(this.see == null) this.see = new ArrayList<>();
					this.see.add(new Pair(lines[1], lines[2]));
				default:
					//summary
					if(this.summary == null) this.summary = new ArrayList<>();
					this.summary.add(lines[1] + lines[2]);
					break;
			}
		}
	}
	
	public void getHTML(PrintWriter pw){
		pw.println("<h1>" + this.header + "</h1>");	
		if(this.since != null){
			pw.println("<section class=\"since\">");
			pw.println("<h1>Since</h1><p>" + this.since.get(0) + "</p>");
			pw.println("</section>");
		}
		
		if(this.version != null){
			pw.println("<section class=\"version\">");
			pw.println("<h2>Version</h2>");
			pw.println("<p>" + this.version.get(0) + "</p>");
			pw.println("</section>");
		}
		
		if(this.author != null){
			pw.println("<section class=\"author\">");
			pw.println("<h2>Author</h2>");
			for(String s : this.author) pw.println("<p>" + s + "</p>");
			pw.println("</section>");
		}
		
		if(this.deprecated != null){
			pw.println("<section class=\"deprecated\">");
			pw.println("<h2>Deprecated</h2>");
			for(String s : this.deprecated) pw.println("<p>" + s + "</p>");
			pw.println("</section>");
		}
		
		if(this.summary != null){
			pw.println("<section class=\"summary\">");
			pw.println("<h2>Summary</h2>");
			for(String s : this.summary) pw.println("<p>" + s + "</p>");
			pw.println("</section>");
		}
		
		if(this.param != null){
			pw.println("<section class=\"author\">");
			pw.println("<h2>Parameters</h2>");
			pw.println("<table>");
			for(Pair p : this.param) {
				pw.println("<tr><th>" + p.head + "</th><td>" + "</td></tr>");
			}
			pw.println("</table>");
			pw.println("</section>");
		}
		
		if(this.return_str != null){
			pw.println("<section class=\"return\">");
			pw.println("<h2>Return</h2>");
			for(String s : this.return_str) pw.println("<p>" + s + "</p>");
			pw.println("</section>");
		}
		
		if(this.throws_str != null){
			pw.println("<section class=\"throws\">");
			pw.println("<h2>Throws</h2>");
			pw.println("<table>");
			for(Pair p : this.throws_str) {
				pw.println("<tr><th>" + p.head + "</th><td>" + "</td></tr>");
			}
			pw.println("</table>");
			pw.println("</section>");
		}
		
		
		if(this.see != null){
			pw.println("<section class=\"see\">");
			pw.println("<h2>See also</h2>");
			for(Pair p : this.see) {
				if(p.body.isEmpty()){
					pw.println("<p>" + p.head + "</p");
				}else{
					pw.println("<p><a href=\"" + p.body + "\" >" + p.head + "</a></p>");
				}
			}
			pw.println("</section>");
		}
	}
}
