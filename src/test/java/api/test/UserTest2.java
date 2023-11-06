package api.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.UserEndPoints;
import api.endpoints.UserEndPoints2;
import api.payload.User;
import io.restassured.response.Response;

public class UserTest2 {
	
	Faker faker;
	User userPayload;
	
	public Logger logger;
	
	@BeforeClass
	public void setup() {
		
		faker = new Faker();
		userPayload = new User();
		
		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setUsername(faker.name().username());
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setPassword(faker.internet().password(5, 10));
		userPayload.setPhone(faker.phoneNumber().cellPhone());
		
		//Logs
		logger=LogManager.getLogger(this.getClass());
		
	}
	
	@Test(priority = 1)
	public void testPostUser() {
		logger.info("********** Creating user **********");
		Response res = UserEndPoints2.createUser(userPayload);
		res.then().log().all();
		
		Assert.assertEquals(res.getStatusCode(), 200);
		logger.info("********** User is Created **********");
	}
	
	@Test(priority = 2)
	public void testGetUserByName() {
		logger.info("********** Reading user info **********");
		Response res = UserEndPoints2.readUser(this.userPayload.getUsername());
		res.then().log().all();
		
		Assert.assertEquals(res.getStatusCode(), 200);
		logger.info("********** User info is displayed **********");
	}
	
	@Test(priority = 3)
	public void testUpdateUserByName() {
		logger.info("********** Updating user **********");
		
		//update data using payload
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		
		Response response = UserEndPoints2.updateUser(this.userPayload.getUsername(), userPayload);
		response.then().log().body();
		
		Assert.assertEquals(response.getStatusCode(), 200);
		logger.info("********** User is updated **********");
		
		//Checking data after update
		Response response2 = UserEndPoints2.readUser(this.userPayload.getUsername());
		Assert.assertEquals(response2.getStatusCode(), 200);
		
	}
	
	@Test(priority = 4)
	public void testDeleteUserByName() {
		logger.info("********** Deleting user **********");
		Response res = UserEndPoints2.deleteUser(this.userPayload.getUsername());
		Assert.assertEquals(res.getStatusCode(), 200);
		logger.info("********** User is deleted **********");
		
	}

}