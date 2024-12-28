import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Scraper {
    public static void main(String[] args) {
        String baseUrl = "https://www.rottentomatoes.com/m/$MOVIE_ID/reviews";
        List<String> reviews = new ArrayList<>();
        
        try {
            for (int page = 0; page < 1; page++) {
                String url = baseUrl.replace("$MOVIE_ID", "nosferatu_2024");
                Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0").get();
                List<Element> l = doc.select(".load-more-manager");
                System.out.println(l.size());
                for (Element e : l) {
                	System.out.println(e.text());
                }
                
                // Adjust this selector based on Rotten Tomatoes' structure
                Elements reviewElements = doc.select(".review-data");
                if (reviewElements.isEmpty()) {
                    break; // No more reviews
                }
                
                for (Element review : reviewElements) {
                	Element scoreIcon = review.selectFirst("score-icon-critics");
                	String sentiment;
                    if (scoreIcon != null) {
                        sentiment = scoreIcon.attr("sentiment");
                    } else {
                        sentiment = "unknown";
                    }
                    reviews.add(review.text() + " " + sentiment);
                }
                page++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Print all reviews
        reviews.forEach(System.out::println);
    }
}
