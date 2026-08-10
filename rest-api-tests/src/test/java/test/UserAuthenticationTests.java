package test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import api.client.AuthClient;
import io.restassured.response.Response;

public class UserAuthenticationTests {

    @Test
    public void shouldCompleteFullNoteWorkflow() {

        // Step 1: create request body
        Map<String,String> note = new HashMap<>();
        note.put("title", "Automation Note");
        note.put("description", "automating note creation using rest-assured framework");
        note.put("category", "Home");

        System.out.println("\n================ CREATE NOTE =================");
        // Step 2: CREATE note
        Response createResponse = AuthClient.postWithAuth(
                "/notes",
                note
        );

        createResponse.then().log().all();
        assertEquals(200, createResponse.statusCode());
        System.out.println("================ CREATE COMPLETE ================\n");

        String noteId = createResponse.jsonPath().getString("data.id");
        System.out.println("Created note ID: " + noteId);

        System.out.println("\n================ GET NOTE =================");
        // Step 3: GET note
        Response getResponse = AuthClient.getWithAuth(
                "/notes/" + noteId
        );

        getResponse.then().log().all();
        assertEquals(200, getResponse.statusCode());
        System.out.println("================ GET COMPLETE ================\n");

        System.out.println("\n================ UPDATE NOTE =================");
        // Step 4: UPDATE note
        note.put("completed", "false");
        note.put("title", "Updated Automation Note");

        Response updateResponse = AuthClient.putWithAuth(
                "/notes/" + noteId,
                note
        );

        updateResponse.then().log().all();
        assertEquals(200, updateResponse.statusCode());
        System.out.println("================ UPDATE COMPLETE ================\n");

        System.out.println("\n================ DELETE NOTE =================");
        // Step 5: DELETE note
        Response deleteResponse = AuthClient.deleteWithAuth(
                "/notes/" + noteId
        );

        deleteResponse.then().log().all();
        assertEquals(200, deleteResponse.statusCode());
        System.out.println("================ DELETE COMPLETE ================\n");
    }
}
