package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import api.client.PetClient;
import api.models.Pet;
import api.utils.Config;
import io.restassured.response.Response;
@Disabled
public class SubscriptionCreationTests {

    // create reusable API client (links test -> client layer)
    static PetClient petClient = new PetClient();

    /*
    Pet pet = new Pet(
                9001,
                "basic-plan",
                "active"
        );
    */


    @BeforeAll
    static void setup() {
        Config.setup();
    }

    @Test
    public void shouldCreateSubscriptionSuccessfully() {
        System.out.println("\n=== Running: shouldCreateSubscriptionSuccessfully ===");

        String body = api.utils.PayloadLoader.load("createSubscriptionValid.json");

        Response response = petClient.createPet(body);

        response.then().log().all();

        assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldGetPetById() {
        System.out.println("\n=== Running: shouldGetPetById ===");

        String body = api.utils.PayloadLoader.load("createSubscriptionValid.json");
        petClient.createPet(body);

        Response response = petClient.getPet(9001);

        response.then().log().all();

        Pet returnedPet = response.as(Pet.class);

        assertEquals(200, response.statusCode());

        assertEquals(9001, returnedPet.getId());
        assertEquals("basic-plan", returnedPet.getName());
        assertEquals("active", returnedPet.getStatus());
    }

    @Test
    public void shouldUpdatePet() {
        System.out.println("\n=== Running: shouldUpdatePet ===");

        // String createBody = """
        // {
        //   "id": 9001,
        //   "name": "test-subscription",
        //   "status": "available"
        // }
        // """;
        String body = api.utils.PayloadLoader.load("createSubscriptionValid.json");
        petClient.createPet(body);

        String updateBody = """
        {
          "id": 9002,
          "name": "updated-subscription",
          "status": "available"
        }
        """;

        Response response = petClient.updatePet(updateBody);

        response.then().log().all();

        System.out.println("Response status code: " + response.statusCode());
        System.out.println("Response body: " + response.getBody().asString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldDeletePet() {
        System.out.println("\n=== Running: shouldDeletePet ===");

        // String createBody = """
        // {
        //   "id": 9001,
        //   "name": "test-subscription",
        //   "status": "available"
        // }
        // """;

        //petClient.createPet(pet);//
        String body = api.utils.PayloadLoader.load("createSubscriptionValid.json");
        petClient.createPet(body);

        Response response = petClient.deletePet(9001);

        response.then().log().all();

        System.out.println("Response status code: " + response.statusCode());
        System.out.println("Response body: " + response.getBody().asString());

        assertEquals(200, response.statusCode());
    }


    @Test
    public void shouldReturnNotFoundForInvalidId() {
        System.out.println("\n=== Running: shouldReturnNotFoundForInvalidId ===");

        int nonExistingId = 9999999;

        Response response = petClient.getPet(nonExistingId);

        response.then().log().all();

        Pet returnedPet = response.as(Pet.class);
        System.out.println("Returned ID: " + returnedPet.getId());
        System.out.println("Returned Name: " + returnedPet.getName());
        System.out.println("Returned Status: " + returnedPet.getStatus());

        assertEquals(404, response.statusCode());
    }


    @Test
    public void shouldFailWhenPetNameMissing() {
        System.out.println("\n=== Running: shouldFailWhenPetNameMissing ===");

        // String invalidBody = """
        // {
        //   "name": "test-subscription",
        //   "status": "available"
        // }
        // """;
        String invalidBody = api.utils.PayloadLoader.load("createSubscriptionMissingName.json");
        Response response = petClient.createPet(invalidBody);

        response.then().log().all();

        Pet returnedPet = response.as(Pet.class);
        System.out.println("Returned ID: " + returnedPet.getId());
        System.out.println("Returned Name: no name provided" + returnedPet.getName());
        System.out.println("Returned Status: " + returnedPet.getStatus());

        assertEquals(400, response.statusCode());
    }


    @Test
    public void shouldFailWhenInvalidStatusValue() {
        System.out.println("\n=== Running: shouldFailWhenInvalidStatusValue ===");

        // String invalidStatus = """
        // {
        //   "id": 9002,
        //   "name": "bad-status-test",
        //   "status": "not-real-status"
        // }
        // """;
        String invalidStatus = api.utils.PayloadLoader.load("createSubscriptionInvalidStatus.json");
        Response response = petClient.createPet(invalidStatus);

        response.then().log().all();

        Pet returnedPet = response.as(Pet.class);
        System.out.println("Returned ID: " + returnedPet.getId());
        System.out.println("Returned Name: " + returnedPet.getName());
        System.out.println("Returned Status: " + returnedPet.getStatus());

        assertEquals(400, response.statusCode());
    }

    @Test
    public void shouldCreateSubscriptionUsingDataFactory() {
        System.out.println("\n=== Running: shouldCreateSubscriptionUsingDataFactory ===");

        // workbook example:
        // DataFactory builds JSON dynamically instead of loading static payload file

        String dynamicBody = api.utils.DataFactory.createSubscriptionBody(
                9010,
                "dynamic-plan",
                "active"
        );

        // send request using client layer
        Response response = petClient.createPet(dynamicBody);

        // print API response
        response.then().log().all();

        // convert response JSON -> Pet model
        Pet returnedPet = response.as(Pet.class);

        // validations
        assertEquals(200, response.statusCode());
        assertEquals(9010, returnedPet.getId());
        assertEquals("dynamic-plan", returnedPet.getName());
        assertEquals("active", returnedPet.getStatus());
    }
}