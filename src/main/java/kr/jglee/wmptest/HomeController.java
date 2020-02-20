package kr.jglee.wmptest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kr.jglee.wmptest.utils.TestUtils;

@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private HttpServletResponse response;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(String url, String type, String unit) throws IOException {
		if (TestUtils.isempty(url)) url = "https://front.wemakeprice.com/main";
		if (TestUtils.isempty(type)) type = "all";
		if (TestUtils.isempty(unit)) unit = "1";
		if (unit.startsWith("-") || unit.startsWith("0")) unit = "1";
		String goUrl = url;
		if (goUrl.matches("^\\/\\/.*$")) goUrl = "http:" + goUrl;
		if (!goUrl.matches("^https?:\\/\\/.*$")) goUrl = "http://" + goUrl;
		long begin, end;
		
		begin = System.currentTimeMillis();
		Document doc;
		try {
			doc = Jsoup.connect(goUrl)
			  .header("Connection", "keep-alive")
			  .header("Cache-Control", "max-age=0")
			  .header("Upgrade-Insecure-Requests", "1")
			  .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36")
			  .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
			  .header("Accept-Encoding", "gzip, deflate, br")
			  .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
			  .header("sec-fetch-mode", "navigate")
			  .header("sec-fetch-site", "none")
			  .header("sec-fetch-user", "?1")
			  .timeout(3000)
			  .ignoreHttpErrors(true)
			  .get();
		} catch (Exception e) {
			e.printStackTrace();
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter pw = response.getWriter();
			String ctx = request.getContextPath();
			if (TestUtils.isempty(ctx)) ctx = "/";
			String alert = String.format("<script>alert('%s'); location.href='%s';</script>", 
					"페이지를 가져오는데 실패하였습니다. 잠시후에 다시 시도해주세요.", ctx);
			System.out.println(alert);
			pw.println(alert);
			pw.close();
			return null;
		}
		end = System.currentTimeMillis();
		logger.info("[GET  ] {} {}ms", url, end - begin);
		
		boolean rmtag = type.equals("rmtag") ? true : false;
		begin = System.currentTimeMillis();
//		String teststr = "html<<htmlzcvdfj>>>>>>>>>>>313>12</html>";
//		char[] result = TestUtils.test(teststr, rmtag);
		char[] result = TestUtils.test(doc.toString(), rmtag);
		end = System.currentTimeMillis();
		logger.info("[PARSE] {}ms", end - begin);
		
		int mod = Integer.MAX_VALUE;
		if (!TestUtils.isoverflow(unit))
			mod = Integer.parseInt(unit);
		
		int remainder = result.length % mod;
		
		request.setAttribute("result", String.valueOf(result, 0, result.length - remainder));
		request.setAttribute("remainder", remainder > 0 ? 
				String.valueOf(result, result.length - remainder, remainder) : "");
		request.setAttribute("url", url);
		request.setAttribute("type", type);
		request.setAttribute("unit", unit);
				
		return "home";
	}
}

