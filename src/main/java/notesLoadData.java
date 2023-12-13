import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.util.Iterator;

public class notesLoadData {

    static getInputs config = new getInputs();

    public static void main(String[] args) throws Exception {

        // Read inputs -  table name
        String tablename = config.getTableName();

        // TODO 0 BEGIN
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .build();

        //Use the DynamoDB document API wrapper
        DynamoDB dynamoDB = new DynamoDB(client);
        // TODO 0 END
        
        //Use Notes table as resource
        Table table = dynamoDB.getTable(tablename);

        //Input file processing...
        JsonParser parser = new JsonFactory().createParser(new File("notes.json"));
        JsonNode rootNode = new ObjectMapper().readTree(parser);

        //Set an iterator on each JSON note node
        Iterator<JsonNode> iter = rootNode.iterator();

        ObjectNode currentNode;

        System.out.format("\n\n Loading \"%s\" table with data from file \"notes.json\"\n\n", tablename);

        while (iter.hasNext()) {
            currentNode = (ObjectNode) iter.next();

            String userId = currentNode.path("UserId").asText();
            Integer noteId = currentNode.path("NoteId").asInt();
            String note = currentNode.path("Note").asText();

            //Load data into table
            try {
                table.putItem(
                        new Item()
                                .withPrimaryKey("UserId", userId, "NoteId", noteId)
                                .withString("Note", note)
                );

                System.out.println("PutItem succeeded: " + userId + " " + noteId + " " + note);
            } catch (AmazonServiceException e) {
                System.err.println(e.getMessage());
            } catch (Exception e) {
                System.err.println("Unable to add note: " + userId + " " + noteId);
                System.err.println(e.getMessage());
                break;
            }
        }
        parser.close();
    }
}
