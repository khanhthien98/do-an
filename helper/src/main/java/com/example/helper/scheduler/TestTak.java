//package com.example.helper.scheduler;
//
//import java.io.IOException;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.select.Elements;
//
//public class TestTak {
//
//	public static void main(String[] args) {
//		String url = "https://giupviechvt.vn/nguoi-giup-viec/page/2";
//		Document doc = null;
//		try {
//			doc = Jsoup.connect(url).userAgent("Jsoup client").timeout(20000).get();
//			// lấy danh sách các thể <a> chứa bài viết từ kết quả trả về 
//			Elements lstArticles = doc.select("div.row-masonry div.post-item a");
//			for(int i = 0; i<lstArticles.size()-1;i++) {
//				System.out.println("--cong nhan");
//				//duong link
//				System.out.println("link:" + lstArticles.get(i).attr("href"));
//				//
//				
//
//				System.out.println("--cong nhan end ");
//			}
//		} catch (IOException e) {
//			if (e.getMessage().contains("HTTP error fetching URL")) {
//				System.out.println("ton tai trang");
//			}
//		}
//	}
//}
