import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Server {
    public static final String REMOTE_URL = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setUserAgent("Cat Service")
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        HttpGet httpGet = new HttpGet(REMOTE_URL);

        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            List<Cat> catList = mapper.readValue(response.getEntity().getContent(), new TypeReference<>() {});

            List<Cat> filteredCatList = catList.stream()
                            .filter(cat -> {
                                Integer upvotes = cat.getUpvotes();
                                return upvotes != null && upvotes > 0;
                            })
                            .toList();

            filteredCatList.forEach(System.out::println);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
