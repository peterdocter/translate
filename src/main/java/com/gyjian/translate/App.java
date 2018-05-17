package com.gyjian.translate;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Hello world!
 *
 */

import com.baidu.translate.demo.TransApi;

public class App {
	// 在平台申请的APP_ID 详见
	// http://api.fanyi.baidu.com/api/trans/product/desktop?req=developer
	private static final String APP_ID = "20180515000160090";
	private static final String SECURITY_KEY = "";

	public static void main(String[] args) {
		String inFile = "r:\\a.srt";
		String[] inNames = inFile.split("\\.");
		String outFile = inNames[0] + ".cn." + inNames[1];

		FileOutputStream outSTr = null;
		BufferedOutputStream Buff = null;

		try {
			InputStream is = new FileInputStream(inFile);
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));

			outSTr = new FileOutputStream(new File(outFile));
			Buff = new BufferedOutputStream(outSTr);
			String reg = "^([0-9])*:([0-9])*"; //
			Pattern pattern = Pattern.compile(reg);

			String str = null;
			while (true) {
				str = reader.readLine();
				if (str != null) {
					System.out.println(str);

					Matcher matcher = pattern.matcher(str);

					if ((str.length() < 5) || matcher.find()) {
						Buff.write(str.getBytes());
						Buff.write('\n');
						Buff.flush();
					} else
						try {
							TransApi api = new TransApi(APP_ID, SECURITY_KEY);

							String query = str;
							query = (api.getTransResult(query, "en", "zh"));

							JSONObject jo = JSONObject.parseObject(query);
							JSONArray jArray = jo.getJSONArray("trans_result");
							JSONObject jo2 = (JSONObject) jArray.get(0);

							// Convert from Unicode to UTF-8
							String string = jo2.getString("dst");
							byte[] utf8 = string.getBytes("UTF-8");
							// Convert from UTF-8 to Unicode
							string = new String(utf8, "UTF-8");

							System.out.println(string);
							Buff.write(utf8);
							Buff.write('\n');
							Buff.write(str.getBytes());
							Buff.write('\n');

							Buff.flush();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

				}

				else
					break;
			}

			is.close();

			Buff.close();
			outSTr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
