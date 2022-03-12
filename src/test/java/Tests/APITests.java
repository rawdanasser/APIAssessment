package Tests;


import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import Data.ConfigFileReader;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.*;
import java.util.ArrayList;
import java.util.Scanner;

public class APITests {

	public int UserId = 0;
	ConfigFileReader ConfigFileReader;
	
	@BeforeTest
	public void GetTheUserId()
	{
		Scanner sc= new Scanner(System.in);    
		System.out.println("Please Enter the User Id");
		UserId= sc.nextInt(); 
		ConfigFileReader = new ConfigFileReader();
	}
	
	@Test(priority = 1)
	public void GetRandomUser()
	{
		baseURI = ConfigFileReader.getApplicationUrl();
		ArrayList<String> email =
				given().
			         queryParam("id", UserId).
		             get("/users").
		        then().
	                 statusCode(200).
	                 extract().path("email");
	    System.out.println(email);
	}
	
	@Test(priority = 2)
	public void GetTheUSerPosts() 
	{
		
		baseURI = ConfigFileReader.getApplicationUrl();
		ArrayList<Object> PostId = 
				given().
				      queryParam("userId", UserId).
				      get("/posts").
				then().
			          statusCode(200).
			          extract().path("id");
		
		for(Object i : PostId)
		{
			if(i instanceof Integer)
				break;
			else
				System.out.println("There is an invalid ID "+ i);
		} 
	}
	
    @Test(priority = 3)
    public void CreatePost() 
    {
    	
    	CreatePost createPostObject = new CreatePost(UserId, "", "");
		
		baseURI = ConfigFileReader.getApplicationUrl();
		
		int id = given().
		   header("Content-Type","application/json").
		   contentType(ContentType.JSON).            
		   accept(ContentType.JSON).                 
		   body(createPostObject).
		when().
		   post("/posts").
		then().
		   log().body().
		extract().path("id");  	
		// Verify that the post is added
		given().
	      queryParam("id", id).
	    get("/posts").
	      then().
        statusCode(200).log().all();
    }
}
