package com.xxl.api.mock;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationcontext-*.xml")
public class XxlApiTest {

	@Test
	public void test(){
    
	}

	
	@Test
	public void testGetURI(){
		 ///eae878ca-fca8-4c45-ac9a-ac313d47a73c
		 String str= "/service/common/v2/part/queryPartByOeNoVin/eae878ca-fca8-4c45-ac9a-ac313d47a73c";
		 String regex = "(\\/[a-z|0-9|A-Z]*\\.?[a-z|0-9|A-Z]*)+";
		 String regex2 = "([\\/[a-z]+]*\\.?[a-z|0-9]*)+";
		 String regex3 = "(\\p{XDigit}{8}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{12})(.*)?";
		 
		 Pattern pattern = Pattern.compile(regex3);
		 Matcher m = pattern.matcher(str);
		 System.out.println(m.find());
		 System.out.println(m.group(0));
	}
}
