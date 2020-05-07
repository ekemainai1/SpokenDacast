package com.example.spokenwapp.data.repository;

import com.example.spokenwapp.data.model.User;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class RepositoryService {

    @GET("orgs/Google/repos")
    Single<List<User>> getRepositories() {
        return null;
    }

    @GET("repos/{owner}/{name}")
    Single<User> getRepo(@Path("owner") String owner, @Path("name") String name) {
        return null;
    }
}
