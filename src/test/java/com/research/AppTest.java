package com.research;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void mapTest(){
        IntStream is = IntStream.rangeClosed(1, 100);
        IntStream is2 = is.map(e -> e * 2);
        printResult("mapTest",is2);
    }

    @Test
    public void filterTest() {
        IntStream is = IntStream.rangeClosed(1, 100);
        IntStream is2 = is.filter(e -> e % 2 == 0);
        printResult("filterTest", is2);
    }

    @Test
    public void reduceTest() {
         int x = IntStream.rangeClosed(1, 5).reduce(100, (a, b) -> a + b);
         printResult("reduceTest", x);
    }

    @Test
    public void collectTest() {
        String x = IntStream.rangeClosed(1,5).mapToObj(e -> String.format("test-%s",String.valueOf(e)))
                .collect(Collectors.joining(", ", "[", "]"));
        printResult("collectTest", x);
    }

    @Test
    public void parallelTest() {
        IntStream is = IntStream.rangeClosed(1, 1000).parallel();  // it will use common thread pool! bad idea
        long start = System.currentTimeMillis();
        is.forEach(this::worker);
        long duration = System.currentTimeMillis() - start;
        printResult("parallelTest", duration);

    }

    private int worker(int value) {
        try {
            Random rand = new Random();
            int r = rand.nextInt(50) + 1;
            String url = String.format("https://jsonplaceholder.typicode.com/todos/%s", r);

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            //add request header
            con.setRequestProperty("User-Agent", "Java client");

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            System.out.println(response.toString());
            return value * r;

        } catch (Exception ex) {
            System.out.println(String.format("**Error : %s", ex.getMessage()));
            return -999;
        }
    }

    private void printResult(String testName, IntStream is) {
        System.out.println(testName);
        is.forEach(e -> System.out.print(String.format("%d ", e)));
        System.out.println("");
    }

    private void printResult(String testName, IntStream is, long duration) {
        System.out.println(testName);
        is.forEach(e -> System.out.print(String.format("$$ %d ", e)));
        System.out.println(String.format("** Duration : %d ms", duration));
    }


    private void printResult(String testName, int value) {
        System.out.println(testName);
        System.out.println(value);
    }

    private void printResult(String testName, long value) {
        System.out.println(testName);
        System.out.println(String.format("END %s - %d ms",testName, value));
    }

    private void printResult(String testName, String value) {
        System.out.println(testName);
        System.out.println(value);
    }

}
