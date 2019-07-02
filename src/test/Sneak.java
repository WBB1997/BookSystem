package test;

import dao.impl.BookDaoImpl;
import model.Admin;
import model.Book;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import util.SqlStateListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Sneak {
    //BaseUrl
    public static final String BaseUrl = "https://book.douban.com/top250/";

    //拼接分页
    public static final String START = "?start=";

    //总页数
    public static final int PageNum = 10;



    public static void main(String[] args){
        getEveryBook();
    }

    // 获取每本书的URL
    public static String getEveryBook() {
        Admin a = new Admin("690252189", "a690252189");
        for (int i = 0; i < PageNum; i++) {
            try {
                String url = BaseUrl + START + i * 25;
                Document doc = Jsoup.connect(url).get();
                Elements links = doc.select(".nbg");
                // 遍历每页25个链接
                for (Element link : links) {
                    try {
                        Document childDoc = Jsoup.connect(link.attr("href")).get();
                        System.out.println("Begin :" + childDoc.baseUri());
                        Elements picInfo = childDoc.select("div#mainpic > a > img");
                        String titleInfo = childDoc.select("h1").first().text();
                        String picUrl = picInfo.attr("src");
                        ArrayList<String> info = new ArrayList<>();
                        info.add(picUrl);
                        info.add(titleInfo);

                        List<Node> bodyInfo = childDoc.select("div#info").get(0).childNodes();
                        for (int j = 0; j < bodyInfo.size() - 2; j++) {
                            Node node = bodyInfo.get(j);
                            if (node instanceof Element) {
                                Element innerElement = (Element) node;
                                if (innerElement.tagName().equals("span")) {
                                    String innerElementText = innerElement.text();
                                    if (innerElementText.equals("出品方:")
                                            || innerElementText.equals("原作名:")
                                            || innerElementText.equals("页数:")
                                            || innerElementText.equals("定价:")
                                            || innerElementText.equals("装帧:")
                                            || innerElementText.equals("丛书:")
                                            || innerElementText.equals("副标题:")
                                            || innerElementText.equals("译者:"))
                                        continue;
                                    if (bodyInfo.get(j + 2) instanceof Element) {
                                        Element element = (Element) bodyInfo.get(j + 2);
                                        if (element.tagName().equals("a"))
                                            info.add(element.text().trim());
                                        else {
                                            if (bodyInfo.get(j + 1) instanceof TextNode) {
                                                TextNode textNode = (TextNode) bodyInfo.get(j + 1);
                                                info.add(textNode.text());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if(info.size() < 6)
                            System.out.println("Error : " + info.toString());
                        else {
                            Book book = new Book();
                            book.setCover(info.get(0));
                            book.setName(info.get(1));
                            book.setAuthor(info.get(2));
                            book.setPublisher(info.get(3));
                            book.setPublishDate(info.get(4));
                            book.setISBN(info.get(5));
                            book.setAmount(5);
                            book.setAvailable(5);
                            new BookDaoImpl().PrivateAaddBook(a, book, new SqlStateListener() {
                                @Override
                                public void Error(int ErrorCode, String ErrorMessage) {
                                    System.out.println("Error：" + ErrorMessage);
                                }

                                @Override
                                public void Correct() {
                                    System.out.println("Ok");
                                }
                            });
                            System.out.println(info.toString());
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
