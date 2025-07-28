import java.io.*;
import java.util.*;
import java.io.FileReader;
import java.math.BigInteger;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Main {

    public static void main(String[] args) throws Exception {
        // Read both testcases
        JSONObject json1 = readJSON("testcasesample.json");
        JSONObject json2 = readJSON("testcasesecond.json");

        int secret1 = solve(json1);
        int secret2 = solve(json2);

        System.out.println("Secret 1: " + secret1);
        System.out.println("Secret 2: " + secret2);
    }

    static JSONObject readJSON(String filename) throws Exception {
        JSONParser parser = new JSONParser();
        FileReader reader = new FileReader(filename);
        return (JSONObject) parser.parse(reader);
    }

    static int solve(JSONObject json) {
        JSONObject keys = (JSONObject) json.get("keys");
        int k = Integer.parseInt(keys.get("k").toString());

        List<Integer> xList = new ArrayList<>();
        List<Long> yList = new ArrayList<>();

        for (Object key : json.keySet()) {
            if (key.equals("keys")) continue;
            int x = Integer.parseInt(key.toString());
            JSONObject obj = (JSONObject) json.get(key);
            int base = Integer.parseInt(obj.get("base").toString());
            String value = obj.get("value").toString();
            long y = new BigInteger(value, base).longValue();

            xList.add(x);
            yList.add(y);
        }

        int n = xList.size();
        Map<Long, Integer> freq = new HashMap<>();

        List<Integer> idx = new ArrayList<>();
        for (int i = 0; i < n; i++) idx.add(i);

        List<List<Integer>> combinations = generateCombinations(idx, k);

        for (List<Integer> comb : combinations) {
            List<Integer> xSub = new ArrayList<>();
            List<Long> ySub = new ArrayList<>();
            for (int i : comb) {
                xSub.add(xList.get(i));
                ySub.add(yList.get(i));
            }

            long res = lagrangeInterpolation(xSub, ySub);
            freq.put(res, freq.getOrDefault(res, 0) + 1);
        }

        return Collections.max(freq.entrySet(), Map.Entry.comparingByValue()).getKey().intValue();
    }

    static long lagrangeInterpolation(List<Integer> x, List<Long> y) {
        double total = 0.0;
        int k = x.size();
        for (int i = 0; i < k; i++) {
            double term = y.get(i);
            for (int j = 0; j < k; j++) {
                if (i != j) {
                    term *= (0.0 - x.get(j)) / (x.get(i) - x.get(j));
                }
            }
            total += term;
        }
        return Math.round(total);
    }

    
}