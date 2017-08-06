package com.demo;

import com.google.gson.annotations.SerializedName;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface GraphRestService {

    @GET("/graph/render/{graphName}")
    Call<List<Content>> renderGraph(@Path("graphName") String graphName);

    @POST("/graph/list/{graphName}")
    @Headers({ "Content-Type: application/json", "Accept: application/json"})
    Call<List<String>> list(@Body GraphRequest req, @Path("graphName") String graphName);

    @POST("/graph/create/{graphName}")
    @Headers({ "Content-Type: application/json", "Accept: application/json"})
    Call<String> add(@Body GraphRequest object, @Path("graphName") String graphName);

    @POST("/graph/delete/{graphName}")
    Call<String> remove(@Body GraphRequest req, @Path("graphName") String graphName);

    class Content {
        private String text; //: 'Parent 1',
        private String href; //: '#parent1',
        private List<String> tags; //: ['4'],
        private List<Content> nodes; //: [
    }

    class GraphRequest {
        private String childName;
        private List<String> location;

        public String getChildName() {
            return childName;
        }

        public void setChildName(String childName) {
            this.childName = childName;
        }

        public List<String> getLocation() {
            return location;
        }

        public void setLocation(List<String> location) {
            this.location = location;
        }
    }
}
