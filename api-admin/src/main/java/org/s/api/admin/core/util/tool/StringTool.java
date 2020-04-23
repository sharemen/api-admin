package org.s.api.admin.core.util.tool;

import java.util.ArrayList;
import java.util.List;

/**
 * string tool
 *
 * @author xuxueli 2019-04-27 17:01:11
 */
public class StringTool {

    public static final String EMPTY = "";

    public static boolean isBlank(final String str) {
        return str==null || str.trim().length()==0;
    }

    public static boolean isNotBlank(final String str) {
        return !isBlank(str);
    }


    public static String[] split(final String str, final String separatorChars) {
        if (isBlank(str)) {
            return null;
        }
        if (isBlank(separatorChars)) {
            return new String[]{str.trim()};
        }
        List<String> list = new ArrayList<>();
        for (String item : str.split(separatorChars)) {
            if (isNotBlank(item)) {
                list.add(item.trim());
            }
        }
        return list.toArray(new String[list.size()]);
    }

    public static String join(final String[] array, String separator) {
        if (array == null) {
            return null;
        }
        if (separator == null) {
            separator = EMPTY;
        }
        final StringBuilder buf = new StringBuilder();

        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }
    
    public  static  String    escape  (String  src) 
    { 
      int  i; 
      char  j; 
      StringBuffer  tmp  =  new  StringBuffer(); 
      tmp.ensureCapacity(src.length()*6); 
      for  (i=0;i<src.length()  ;i++  ) 
      { 
        j  =  src.charAt(i); 
        if  (Character.isDigit(j)  ||  Character.isLowerCase(j)  ||  Character.isUpperCase(j)) 
          tmp.append(j); 
        else 
          if  (j<256) 
          { 
          tmp.append(  "%"  ); 
          if  (j<16) 
            tmp.append(  "0"  ); 
          tmp.append(  Integer.toString(j,16)  ); 
          } 
          else 
          { 
          tmp.append(  "%u"  ); 
          tmp.append(  Integer.toString(j,16)  ); 
          } 
      } 
      return  tmp.toString(); 
    } 
    
    public  static  String    unescape  (String  src) 
    { 
      StringBuffer  tmp  =  new  StringBuffer(); 
      tmp.ensureCapacity(src.length()); 
      int    lastPos=0,pos=0; 
      char  ch; 
      while  (lastPos<src.length()) 
      { 
        pos  =  src.indexOf("%",lastPos); 
        if  (pos  ==  lastPos) 
          { 
          if  (src.charAt(pos+1)=='u') 
            { 
            ch  =  (char)Integer.parseInt(src.substring(pos+2,pos+6),16); 
            tmp.append(ch); 
            lastPos  =  pos+6; 
            } 
          else 
            { 
            ch  =  (char)Integer.parseInt(src.substring(pos+1,pos+3),16); 
            tmp.append(ch); 
            lastPos  =  pos+3; 
            } 
          } 
        else 
          { 
          if  (pos  ==  -1) 
            { 
            tmp.append(src.substring(lastPos)); 
            lastPos=src.length(); 
            } 
          else 
            { 
            tmp.append(src.substring(lastPos,pos)); 
            lastPos=pos; 
            } 
          } 
      } 
      return  tmp.toString(); 
    } 

    public static void main(String[] args) {
        System.out.println(isBlank("  "));
        System.out.println(isNotBlank("qwe"));
        System.out.println(split("a,b,cc,", ","));
        System.out.println(join(new String[]{"a","b","c"},","));
    }

}