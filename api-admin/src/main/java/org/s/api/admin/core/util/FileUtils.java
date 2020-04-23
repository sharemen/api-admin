package org.s.api.admin.core.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FileUtils {

	private static Logger LOG = LoggerFactory.getLogger(FileUtils.class); 
	/**
	 * 合并文件
	 * @param inputFiles
	 * @param outFile
	 */
	public static  void mergeFile(File[] inputFiles,File outFile) {
		
		BufferedOutputStream br = null;
		try {
			br = new BufferedOutputStream(new FileOutputStream(outFile));
			for(File f:inputFiles) {
//				System.out.println(f);
				BufferedInputStream bi = new BufferedInputStream(new FileInputStream(f));
				byte[] bb = new byte[1024];
				while(bi.read(bb)!=-1) {
					br.write(bb);
				}
				bi.close();
				br.flush();
			}
		} catch (Exception e) {
			LOG.error("合并文件出错",e);
		}finally {
			try {
				br.close();
			} catch (Exception e) {
			}
		}
	}
	
	 public static String readFileToString(String path) {
	        // 定义返回结果
	        String jsonString = "";

	        BufferedReader in = null;
	        try {
	            in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)), "UTF-8"));// 读取文件  
	            String thisLine = null;
	            while ((thisLine = in.readLine()) != null) {
	                jsonString += thisLine;
	            }
	            in.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (in != null) {
	                try {
	                    in.close();
	                } catch (IOException el) {
	                }
	            }
	        }

	        // 返回拼接好的JSON String
	        return jsonString;
	    }
	
	
	public static void main(String[] args) {
		
		String source = "F:\\data\\message\\videocache\\";
		String target = "D:\\vmware\\windows\\data\\videocache\\";
		
		String[] strs = new File(target).list();
		Set<String> set = new HashSet<>();
		for(String str:strs) {
			set.add(str);
		}
		
		int count = 0;
		for (String fileName : new File(source).list()) {
			String targetFileName =   fileName.replace("正在播放-", "")
			.replace("-无限资源", "")
			.replace(".m3u8.d", "")
			.replace(".", "")+ ".mp4";
			if(set.contains(targetFileName)) {
				continue;
			}
			targetFileName = target+targetFileName;

			
			File temp = new File(source + fileName);
			try {
				int length = temp.list().length - 1;
				File[] files = new File[length];
				for (int i = 1; i <= length; i++) {
					files[i - 1] = new File(temp.getPath() + "\\" + i);
				}
				mergeFile(files, new File(targetFileName));

			} catch (Exception e) {
				LOG.error("出错,fileName="+fileName,e);
				e.printStackTrace();
			}
			count++;
			if(count >=100)
				break;
		}
		
		
//		System.out.println("test");
//		File ff = new File();
//		for(String fileName :ff.list()) {
//			System.out.println(fileName);
//			
//			File temp = new File("F:\\data\\message\\videocache\\"+fileName);
//			
//			try {
//				int length = temp.list().length - 1;
//				File[] files = new File[length];
//				for(int i =1 ;i <=length ;i++) {
//					files[i-1] = new File(temp.getPath()+"\\"+i);
//				}
//				mergeFile(files, new File(+fileName+".mp4"));
//				
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
			
	}
}
