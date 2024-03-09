// Movie.java
package com.movie.movie;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Movie {

    // Path to the CSV files
    private static final String MOVIES_FILE_PATH = "src/main/java/com/movie/movie/movies.csv";
    private static final String RATINGS_FILE_PATH = "src/main/java/com/movie/movie/ratings.csv";

    // Delimiter used in the CSV files
    private static final String DELIMITER = ",";

    // Map to store movie IDs and titles
    private static Map<Integer, String> movieMap = new HashMap<>();

    // Map to store user ratings
    private static Map<Integer, Map<Integer, Double>> userRatings = new HashMap<>();

    public static void main(String[] args) {
        loadMovies();
        loadRatings();

        // Recommend movies for user 1
        int userId = 1;
        List<Integer> recommendedMovies = recommendMovies(userId, 5);
        System.out.println("Recommended movies for user " + userId + ":");
        for (int movieId : recommendedMovies) {
            System.out.println(movieMap.get(movieId));
        }
    }

    // Load movies from CSV file into movieMap
    private static void loadMovies() {
        try (BufferedReader reader = new BufferedReader(new FileReader(MOVIES_FILE_PATH))) {
            // Skip the header line
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(DELIMITER);
                int movieId = Integer.parseInt(parts[0]);
                String title = parts[1];
                movieMap.put(movieId, title);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load user ratings from CSV file into userRatings map
    private static void loadRatings() {
        try (BufferedReader reader = new BufferedReader(new FileReader(RATINGS_FILE_PATH))) {
            // Skip the header line
            reader.readLine();
        //     String header = reader.readLine();
        // if (header != null && header.startsWith("userId")) {
        //     reader.readLine();
        // }
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(DELIMITER);
                int userId = Integer.parseInt(parts[0]);
                int movieId = Integer.parseInt(parts[1]);
                double rating = Double.parseDouble(parts[2]);

                userRatings.putIfAbsent(userId, new HashMap<>());
                userRatings.get(userId).put(movieId, rating);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Recommend top N movies for the given user
    private static List<Integer> recommendMovies(int userId, int numRecommendations) {
        // Map<Integer, Double> scores = new HashMap<>();
        // Map<Integer, Double> userRatings = Movie.userRatings.get(userId);

        // // Calculate similarity scores with other users
        // for (Map.Entry<Integer, Map<Integer, Double>> entry : Movie.userRatings.entrySet()) {
        //     int otherUserId = entry.getKey();
        //     if (otherUserId != userId) {
        //         double similarity = calculateSimilarity(userRatings, entry.getValue());
        //         for (Map.Entry<Integer, Double> movieRating : entry.getValue().entrySet()) {
        //             if (!userRatings.containsKey(movieRating.getKey())) {
        //                 scores.put(movieRating.getKey(), scores.getOrDefault(movieRating.getKey(), 0.0)
        //                         + similarity * movieRating.getValue());
        //             }
        //         }
        //     }
        // }

        // // Sort movies by score and get top N recommendations
        // List<Integer> recommendations = new ArrayList<>(scores.keySet());
        // recommendations.sort(Comparator.comparingDouble(scores::get).reversed());
        // return recommendations.subList(0, Math.min(numRecommendations, recommendations.size()));
      
            Map<Integer, Double> scores = new HashMap<>();
            Map<Integer, Double> userRating = userRatings.get(userId);
        
            // Calculate similarity scores with other users
            for (Map.Entry<Integer, Map<Integer, Double>> entry : userRatings.entrySet()) {
                int otherUserId = entry.getKey();
                if (otherUserId != userId) {
                    double similarity = calculateSimilarity(userRating, entry.getValue());
                    System.out.println("Similarity between user " + userId + " and user " + otherUserId + ": " + similarity);
                    for (Map.Entry<Integer, Double> movieRating : entry.getValue().entrySet()) {
                        if (!userRating.containsKey(movieRating.getKey())) {
                            scores.put(movieRating.getKey(), scores.getOrDefault(movieRating.getKey(), 0.0)
                                    + similarity * movieRating.getValue());
                        }
                    }
                }
            }
        
            // Sort movies by score and get top N recommendations
            List<Integer> recommendations = new ArrayList<>(scores.keySet());
            recommendations.sort(Comparator.comparingDouble(scores::get).reversed());
            System.out.println("Top recommendations: " + recommendations);
            return recommendations.subList(0, Math.min(numRecommendations, recommendations.size()));
        }
        
    

    // Calculate similarity between two users based on their ratings
    private static double calculateSimilarity(Map<Integer, Double> user1, Map<Integer, Double> user2) {
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (Map.Entry<Integer, Double> entry : user1.entrySet()) {
            int movieId = entry.getKey();
            if (user2.containsKey(movieId)) {
                dotProduct += entry.getValue() * user2.get(movieId);
                norm1 += Math.pow(entry.getValue(), 2);
                norm2 += Math.pow(user2.get(movieId), 2);
            }
        }

        if (dotProduct == 0.0 || (Math.sqrt(norm1) * Math.sqrt(norm2)) == 0.0) {
            return 0.0;
        }
        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }
}
