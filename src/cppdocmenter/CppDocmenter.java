

package cppdocmenter;

import java.io.File;
import java.io.IOException;

/**
 * C++のヘッダファイルに書かれたcppdocを元にリファレンスを作成するDocumenterプログラム。
 * @author S.Shirao
 * @version 2018/01
 */
public class CppDocmenter {

	/**
	 * メイン関数。指定されたディレクトリ以下を再帰的に探索し、指定されたディレクトリ配下に吐き出す。
	 * コマンド引数に関しては、`CppDocumenter [fromdir] [todir]`
	 * @param args コマンド引数
	 */
	public static void main(String[] args) {
		
		//引数が正しいか
		if(args.length != 2){
			System.out.println("CppDocumenter usage : CppDocumenter [htmlfile] [fromdir] [todir]");
			System.exit(-1);
			return;
		}
		
		//指定されたディレクトリが有るか
		File html_file = new File(args[0]);
		File from_dir = new File(args[1]);
		File to_dir = new File(args[2]);
		if(!(from_dir.exists() && from_dir.isDirectory())){
			//from_dirが存在しない
			System.err.println("from_dir is not directory.");
			System.exit(-1);
		}
		if(!(to_dir.exists() && to_dir.isDirectory())){
			//to_dirが存在しない
			System.err.println("to_dir is not directory.");
			System.exit(-1);
		}
                if(!(html_file.exists() && html_file.isFile())){
                        //htmlファイルが存在しないかファイルではない
                        System.err.println("html file is not exist.");
                        System.exit(-1);
                }
		
		//ファイルの再帰探索とファイル生成
		Process(html_file, from_dir, to_dir);
		
	}
	
	/**
	 * ファイルの再帰探索関数。
	 * @param from_dir 探索するディレクトリ
	 * @param to_dir 保存先のディレクトリ。存在しない場合もありうる。その時は作成する。
	 */
	static void Process(File html_file, File from_dir, File to_dir){
		if(!(from_dir.exists() && from_dir.isDirectory())) return;	//存在しない
		
		File[] files = from_dir.listFiles();
		for(File f : files){
			if(f.isDirectory()){
				//再帰的に下に潜る
				try{
					System.out.println("[directory]" + new File(to_dir.getCanonicalPath() + "/" + f.getName()).getAbsolutePath());
					Process(html_file ,f, new File(to_dir.getCanonicalPath() + "/" + f.getName()));
				}catch(IOException e){
					System.err.println(e.toString());
					System.err.println("ディレクトリはスキップされました。");
					System.err.println(f.getAbsolutePath());					
				}
			}else if(f.isFile()){
				//処理する
				DocConverter.conv(html_file ,f, to_dir);
			}
		}
	}
	
	static public String htmlspecialchars(String src){
		return src;
	}
}
