import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;

import java.util.Iterator;

public class notesQuery {

    //Create DynamoDB client and use Document API wrapper
    static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    static DynamoDB dynamoDB = new DynamoDB(client);
    static getInputs config = new getInputs();

    public static void main(String[] args) throws Exception {

        //Initialize the Read operations inputs
        String qUserId = config.getQueryUser();

        //Get Notes table information
        Table table = dynamoDB.getTable(config.getTableName());

        //Query methods
        queryUserNotesWithProjections(table, qUserId);
    }

    private static void queryUserNotesWithProjections(Table table, String userId) throws Exception {

        //Build request: Query specification with Primary attributes/values to match desired item, but projects only "Notes" and "NoteId"
        // TODO 2 BEGIN
        QuerySpec spec = new QuerySpec()
                .withProjectionExpression("NoteId, Note")
                .withKeyConditionExpression("UserId = :v_Id")
                .withValueMap(new ValueMap()
                        .withString(":v_Id", userId));

        // TODO 2 END

        System.out.format(
                "\n \n Query all notes belong to a user \"%s\" and displaying \"NoteId\" and \"Note\" attributes only:\n", userId);

        // TODO 3 BEGIN
        ItemCollection<QueryOutcome> items = table.query(spec);
        // TODO 3 END

        // Process each item on the current page using page iterator
        Iterator<Item> item = items.iterator();
        while (item.hasNext()) {
            System.out.println(item.next().toJSONPretty());
        }
    }
}





