package com.example.retrofit2;

import retrofit2.http.GET;
import retrofit2.Call;
import java.util.List;
import com.example.retrofit2.Category;
public interface APIService {
    @GET("categories.php")
    Call<List<Category>> getCategoryAll();
}
